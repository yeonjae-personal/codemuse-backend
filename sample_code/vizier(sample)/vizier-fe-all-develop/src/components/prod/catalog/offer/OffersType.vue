<template>
  <div class="w-full flex flex-col items-center rounded-lg">
    <div class="list-card p-4 rounded-b-lg w-[230px]">
      <ItemDrop
        v-if="isEdit"
        :class-name="`h-[48px] ${isEdit && listItems.length > 0 ? 'mb-6' : ''}`"
        :is-disabled="!multipleAcceptCode.includes(dragOfferType) && isDragging"
        @dragover="allowDrop($event)"
        @drop="drop($event)"
        @click.stop="handleClickDrop"
      />
      <div class="text-black flex flex-col gap-4">
        <ToggleVisibility
          v-for="(item, index) in listItems"
          :key="'toggle' + index"
          :custom-class="item.trgtItemDetlTypeCdNm !== 'New' ? 'mb-2' : ''"
          :data="item"
          :is-add="isAdd"
          :is-add-items="item.trgtItemDetlTypeCdNm === 'New'"
          :is-edit="isEdit"
          :offer-type="type"
          :screen="screen"
          :type="typeOffer"
          :duplicate="duplicate"
        />
      </div>
    </div>
    <DateTimePopup
      v-model:open-model="isOpenPopup"
      v-model="dateAddPicker"
      :modal-title="$t('product_platform.addComponent')"
      :min-end-date="moment().format(DATE_FORMAT.DATE_TYPE)"
      :text-btn-cancel="$t('product_platform.cancel')"
      :text-btn-add="$t('product_platform.save')"
      required-start-date
      @submit="handleSubmit"
      @close="handleClose"
    />
  </div>
</template>

<script lang="ts" setup>
import moment from "moment-timezone";
import { useI18n } from "vue-i18n";
import {
  useSnackbarStore,
  useStructureStore,
  useCreateStructureStore,
  useDuplicateStructureStore,
} from "@/store";
import { STRUCTURE_ITEM_SCREEN } from "@/constants/offer";
import { ACTION_TYPE, DATE_FORMAT, OFFER_TYPE } from "@/constants/";
import { TypeComponentCode } from "@/enums";
import { ComponentSubType } from "@/enums/component";
import { isExpiredTime } from "@/utils/format-data";
import { isValidDifferentItemRule } from "@/utils/custom-validation";

const props = defineProps({
  filteredRule: {
    type: Array as PropType<any[]>,
    default: () => [],
  },
  items: {
    type: Array<any>,
    default: () => [],
  },
  title: {
    type: String,
    default: "",
  },
  typeOffer: {
    type: String as PropType<TypeComponentCode>,
    default: "",
  },
  type: {
    type: String,
    default: OFFER_TYPE.PRICEPLAN,
  },
  isEdit: {
    type: Boolean,
    default: false,
  },
  screen: {
    type: String,
    default: STRUCTURE_ITEM_SCREEN.OFFER,
  },
  isAdd: {
    type: Boolean,
    default: false,
  },
  duplicate: {
    type: Boolean,
    default: false,
  },
  singleAcceptCode: {
    type: Array<any>,
    default: () => [],
  },
  multipleAcceptCode: {
    type: Array<any>,
    default: () => [],
  },
});

const emit = defineEmits(["handleClickDrop"]);

const { t } = useI18n();

const snackbarStore = useSnackbarStore();
const structureStore = useStructureStore();
const createStructureStore = useCreateStructureStore();
const duplicateStructureStore = useDuplicateStructureStore();

const componentInfo = ref<any>([]);

const selectedStore = computed(() => {
  if (props.isAdd) {
    return createStructureStore;
  } else if (props.duplicate) {
    return duplicateStructureStore;
  }
  return structureStore;
});

const { getStructureComponentDetail } = selectedStore.value;
const { dragOfferType, listAdd, showActionSave, isCreated, isDragging } =
  storeToRefs(selectedStore.value);

const isOpenPopup = ref(false);
const dateAddPicker = reactive({
  startDate: "",
  endDate: "",
});
const dropData = ref(null as any);

const listAddByType = computed(() => {
  return {
    type: null,
    items: listAdd.value.filter(
      (item) => item?.componentType === props.typeOffer
    ),
    isAddItems: true,
  };
});

const listItems = computed(() => {
  let result = [] as any;

  if (listAddByType.value.items.length > 0) {
    result = [...listAddByType.value.items, ...props.items];
  } else {
    result = [...props.items];
  }

  let listGroup = [] as any;

  result.forEach((item) => {
    if (
      !listGroup.filter((group) => group.itemCodeName === item.itemCodeName)
        .length
    ) {
      listGroup.push({
        ...item,
        items: result.filter((sub) => sub.itemCodeName === item.itemCodeName),
      });
    }
  });

  return listGroup;
});

const isIncludeRC = computed(
  () =>
    props.isAdd &&
    !!listAdd.value.find(
      (item: any) => item.itemCode === ComponentSubType.RecurringCharge
    )
);

const isIncludeDR = computed(
  () =>
    props.isAdd &&
    !!listAdd.value.find(
      (item: any) => item.itemCode === ComponentSubType.DiscountRate
    )
);

const allowDrop = (event: DragEvent) => {
  event.preventDefault();
};

const drop = async (event: DragEvent) => {
  event.preventDefault();
  dropData.value = event.dataTransfer?.getData("item")
    ? JSON.parse(event.dataTransfer.getData("item"))
    : null;
  try {
    const res = await getStructureComponentDetail({
      objUuid: dropData.value?.objUUID,
    });
    componentInfo.value = res.data.additional;
  } catch (error: any) {
    snackbarStore.showSnackbar(
      error?.errorMsg || t("product_platform.something_went_wrong"),
      "error"
    );
  }
  const result = isValidDifferentItemRule(
    props.filteredRule,
    componentInfo.value
  );

  // // Need click add first and then drag item
  if (!result) {
    snackbarStore.showSnackbar(
      t("product_platform.invalidCustomValidation"),
      "error"
    );
    return;
  }

  if (!props.multipleAcceptCode.includes(dragOfferType.value)) {
    snackbarStore.showSnackbar(
      t("product_platform.offerEntity.dropComponentFail"),
      "error"
    );
    return;
  }

  if (props.singleAcceptCode) {
    let itemsNotExpired = props.items.filter(
      (item) =>
        !isExpiredTime(item?.relationValidEndDtm) &&
        item.itemCode === dragOfferType.value
    );

    let itemsNotExpiredAdd = listAddByType.value?.items?.filter(
      (item) => !isExpiredTime(item?.relationValidEndDtm)
    );

    let list = itemsNotExpiredAdd?.concat(itemsNotExpired) || [];

    let checkExisted = list?.some(
      (item) => item.itemCode === dragOfferType.value
    );
    let checkType = props.singleAcceptCode.includes(dragOfferType.value);

    if (checkExisted && checkType) {
      snackbarStore.showSnackbar(
        t("product_platform.offerEntity.invalidEntitySingleMsg"),
        "error"
      );
      return;
    }
  }

  if (props.isAdd) {
    if (!isCreated.value) {
      snackbarStore.showSnackbar(
        t("product_platform.create_offer_first"),
        "error"
      );
      return;
    }
    if (!showActionSave.value) {
      snackbarStore.showSnackbar(
        t("product_platform.cannotDragItemHere"),
        "error"
      );
      return;
    }
  }

  const items: any = [];

  listItems.value.forEach((item) => {
    items.push(item.items);
  });

  const currentIndex = items
    .flat()
    .findIndex((item) => item?.objUuid === dropData.value.objUUID);

  if (currentIndex > -1) {
    snackbarStore.showSnackbar(t("product_platform.duplicateItem"), "error");
    return;
  }

  if (dropData.value) {
    if (
      (dropData.value.itemCode === ComponentSubType.RecurringCharge &&
        isIncludeRC.value &&
        props.type !== OFFER_TYPE.DISCOUNT) ||
      (dropData.value.itemCode === ComponentSubType.DiscountRate &&
        isIncludeDR.value &&
        props.type === OFFER_TYPE.DISCOUNT)
    ) {
      snackbarStore.showSnackbar(t("product_platform.removeBaseFee"), "error");
      return;
    } else {
      dateAddPicker.startDate = "";
      dateAddPicker.endDate = "";
      isOpenPopup.value = true;
      dragOfferType.value = null;
    }
  }
};

const handleClose = () => {
  isOpenPopup.value = false;
};

const handleSubmit = () => {
  if (!dateAddPicker.startDate) {
    snackbarStore.showSnackbar(
      t("product_platform.required_start_date"),
      "error"
    );
    return;
  }

  selectedStore.value.addDataToList({
    ...dropData.value,
    relationValidStartDtm: dateAddPicker.startDate,
    relationValidEndDtm: dateAddPicker.endDate,
    workTypeCode: dateAddPicker.endDate
      ? ACTION_TYPE.ADD_EXPIRED
      : ACTION_TYPE.ADD,
  });
  handleClose();
};

const handleClickDrop = () => {
  emit("handleClickDrop");
};
</script>

<style scoped>
.list-card {
  background: linear-gradient(
    90deg,
    #f8f9fa 0%,
    rgba(247, 248, 250, 0.28) 100%
  );
}
</style>
