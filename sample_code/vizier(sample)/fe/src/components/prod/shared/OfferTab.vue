<template>
  <template v-if="listOffer?.length">
    <div class="flex flex-col gap-3 py-2">
      <ItemDrop
        v-if="isEdit"
        @click.stop="handleOpenAddOffer"
        @drop="drop($event)"
        @dragover.prevent
      />
      <div v-for="(item, index) in paginatedData" :key="item.offrUuid">
        <cf-card-dropdown
          :title="item.offrNm"
          :description="item.offrCd"
          type-bg="linear"
          :border-color-action="setHoverColor(item.offrType)"
          :type-of-prod="setIconType(item.offrType)"
          :is-device-icon="
            ['DE', 'DV'].includes(item.offrType?.slice(0, 2).toUpperCase())
          "
          :icon-color="setIconColor(item.offrType)"
          :active="item.offrUuid === selectedOffer?.offrUuid"
          :node="{
            hideNodeLeft: true,
            isActiveNodeLeft: false,
            hideNodeRight: true,
            isActiveNodeRight: false,
          }"
          :actions="listActions(item, index, isEdit)"
          :disable="isExpiredTime(item?.itemValidEndDtm)"
          :expired="isExpiredTime(item?.validEndDtm)"
          :editable="isEdit"
          :expand="item?.expand"
          :is-new="!!item?.isAdd"
          show-icon-status
          :draggable="true"
          :style="{ 'z-index': 999 }"
          @on-click-card="onChooseCard($event, item)"
          @on-click-show-detail="handleExpandDetail($event, item)"
          @dragstart="
            handleDragUserPocket($event, {
              userPocketType: LARGE_ITEM_CODE.OFFER,
              ...item,
            })
          "
        >
          <template #detail>
            <div v-if="item?.detail" class="detail-content">
              <ProductGrid :data="item.detail" :type="LARGE_ITEM_CODE.OFFER" />
            </div>
          </template>
        </cf-card-dropdown>
      </div>
    </div>
  </template>
  <template v-else>
    <div
      v-if="isEdit"
      class="flex flex-column gap-[10px] px-[8px] rounded-t-[12px]"
    >
      <ItemDrop
        @click.stop="handleOpenAddOffer"
        @drop="drop($event)"
        @dragover.prevent
      />
    </div>
    <div v-else class="h-full w-full flex justify-center items-center">
      <NoData />
    </div>
  </template>
  <DateTimePopup
    v-model:open-model="isOpenAddOfferPopup"
    v-model="dateData"
    :modal-title="$t('product_platform.addOffer')"
    :text-btn-cancel="$t('product_platform.cancel')"
    :text-btn-add="$t('product_platform.save')"
    :min-end-date="
      moment().format(DATE_FORMAT.DATE_FORMAT_WITHOUT_TIME_REVERSE)
    "
    required-start-date
    @submit="handleSubmit(ACTION_CATAGORY.ADD)"
    @close="handleClose(ACTION_CATAGORY.ADD)"
  />
  <DateTimePopup
    v-model:open-model="isOpenChangeOfferPopup"
    v-model="dateOfferData"
    :modal-title="
      checkExpired(selectedInPopoverItem)
        ? $t('product_platform.enableOffer')
        : $t('product_platform.expireOffer')
    "
    :required-end-date="!checkExpired(selectedInPopoverItem)"
    required-start-date
    :disabled-start-date="!checkExpired(selectedInPopoverItem)"
    :text-btn-cancel="$t('product_platform.cancel')"
    :text-btn-add="$t('product_platform.save')"
    :min-end-date="currentDate"
    @submit="handleSubmit(ACTION_CATAGORY.CHANGE)"
    @close="handleClose(ACTION_CATAGORY.CHANGE)"
  />
</template>

<script setup lang="ts">
import useRedirect from "@/composables/useRedirect";
import useDragUserPocket from "@/composables/useDragUserPocket";
import TrashIcon from "@/components/prod/icons/TrashIcon.vue";
import HourglassIcon from "@/components/prod/icons/HourglassIcon.vue";
import CheckVerified from "@/components/prod/icons/CheckVerified.vue";
import OpenInNewIcon from "@/components/prod/icons/OpenInNewIcon.vue";
import moment from "moment-timezone";
import { useI18n } from "vue-i18n";
import { useSnackbarStore } from "@/store";
import { formatDateWithOutSeconds, isExpiredTime } from "@/utils/format-data";
import { ACTION_CATAGORY, OFFER_PARAMS_TYPE } from "@/constants/extendsManager";
import { getProductStructureDetailRootApi } from "@/api/prod/productApi";
import { LARGE_ITEM_CODE } from "@/store/userPocket.store";
import type { ActionType } from "@/interfaces/prod";
import { BasePaginationType, OfferTabItem } from "@/types/common";
import {
  setIconColor,
  setIconType,
  setHoverColor,
} from "@/utils/impact-analysis-utils";
import {
  ACTION_TYPE,
  ACTION_TYPE_NAME,
  DATE_FORMAT,
  OFFER_TYPE,
} from "@/constants/index";

const emit = defineEmits(["update:modelList"]);

type Props = {
  isEdit?: boolean;
  isDuplicate?: boolean;
  modelList: OfferTabItem[];
  pagination?: BasePaginationType;
};

const props = withDefaults(defineProps<Props>(), {
  isEdit: false,
  isDuplicate: false,
  modelList: () => [],
  pagination: () => ({ currentPage: 1, totalPages: 1, pageSize: 10 }),
});

const useSnackbar = useSnackbarStore();
const { t } = useI18n();
const { moveOfferSearchPage } = useRedirect();
const { handleDragUserPocket } = useDragUserPocket();

const itemsPerPage = 8;
const selectedOffer = ref<any>(null);
const dropData = ref<any>(null);
const isOpenAddOfferPopup = ref(false);
const isOpenChangeOfferPopup = ref(false);
const selectedInPopoverItem = ref<any>(null);
const actionType = ref("");

const dateOfferData = reactive({
  startDate: "",
  endDate: "",
});
const dateData = reactive({
  startDate: "",
  endDate: "",
});

const listOffer = computed<OfferTabItem[]>({
  get() {
    return props.modelList;
  },
  set(newVal) {
    emit("update:modelList", newVal);
  },
});

const currentDate = computed(() =>
  moment().format(DATE_FORMAT.DATE_FORMAT_WITHOUT_TIME_REVERSE)
);

const checkExpired = (item) => {
  if (
    moment(item?.validEndDtm, DATE_FORMAT.DATE_TYPE).isSame(new Date(), "day")
  ) {
    return true;
  }
  return (
    item?.validEndDtm &&
    moment(item?.validEndDtm, DATE_FORMAT.DATE_TYPE).diff(moment(), "days") < 0
  );
};

const listActions = (
  item: OfferTabItem,
  _index: number,
  isEdit: boolean = false
): ActionType[] => {
  const isItemExpired = isExpiredTime(item?.itemValidEndDtm);
  if (isItemExpired) return [];
  const isRelationExpired = checkExpired(item);
  const openNewTabAction = {
    name: t("product_platform.openinNewWindow"),
    icon: OpenInNewIcon,
    onClick: () => {
      moveOfferSearchPage({
        itemCode: item?.detail?.additional[0]?.itemCode || "",
        objCode: item?.offrCd || "",
        itemCodeName: item?.offrType || "",
        objUuid: item?.offrUuid || "",
      });
    },
  };
  if (isEdit) {
    if (item.isAdd || props.isDuplicate) {
      return [
        {
          name: t("product_platform.actionRemove"),
          icon: TrashIcon,
          onClick: () => {
            handleRemoveItem(item);
          },
        },
        openNewTabAction,
      ];
    }
    const action = {
      name: isRelationExpired
        ? t("product_platform.actionEnable")
        : t("product_platform.actionExpire"),
      icon: isRelationExpired ? CheckVerified : HourglassIcon,
      onClick: () => {
        actionType.value = isRelationExpired
          ? ACTION_TYPE_NAME.ENABLED
          : ACTION_TYPE_NAME.EXPIRED;
        dateOfferData.startDate =
          formatDateWithOutSeconds(item?.validStartDtm) || "";
        dateOfferData.endDate =
          formatDateWithOutSeconds(item?.validEndDtm) || "";
        selectedInPopoverItem.value = item;
        isOpenChangeOfferPopup.value = true;
      },
    };
    return [action, openNewTabAction];
  }
  return [];
};

const onChooseCard = async (event, item) => {
  selectedOffer.value = item;
  handleExpandDetail(event.isShowDetail, item);
};

const handleOpenAddOffer = () => {
  //   displayForm.value.addOffer = true;
};

const handleRemoveItem = (item: OfferTabItem) => {
  let index;
  if (item.offrUuid) {
    index = listOffer.value.findIndex(
      (offer: any) => offer.offrUuid === item.offrUuid
    );
  } else {
    return;
  }

  if (index !== -1) {
    listOffer.value.splice(index, 1);
  }
};

const drop = (event) => {
  event.preventDefault();
  dropData.value = event.dataTransfer.getData("item")
    ? JSON.parse(event.dataTransfer.getData("item"))
    : null;
  if (dropData.value) {
    let isDupplicateOffer = false;
    listOffer.value.forEach((element) => {
      if (element.offrUuid === dropData.value.offerUuid) {
        useSnackbar.showSnackbar("Duplicate Offer", "error");
        isDupplicateOffer = true;
        return;
      }
    });
    if (!isDupplicateOffer) {
      isOpenAddOfferPopup.value = true;
    }
  }
};
const handleSubmit = async (action) => {
  if (action === ACTION_CATAGORY.ADD && !dateData.startDate) {
    useSnackbar.showSnackbar(
      t("product_platform.required_start_date"),
      "error"
    );
    return;
  }
  if (
    (action === ACTION_CATAGORY.CHANGE && !dateOfferData.startDate) ||
    (action === ACTION_CATAGORY.CHANGE && !dateOfferData.endDate)
  ) {
    useSnackbar.showSnackbar(
      !dateOfferData.endDate
        ? t("product_platform.required_end_date")
        : t("product_platform.required_start_date"),
      "error"
    );
    return;
  }

  let validEndDate: any = null;
  if (action === ACTION_CATAGORY.ADD) {
    if (dateData.endDate) {
      validEndDate = moment(dateData.endDate, DATE_FORMAT.DATE_TYPE).format(
        DATE_FORMAT.DATE_TYPE
      );
    }
  } else {
    if (dateOfferData.endDate) {
      validEndDate = moment(
        dateOfferData.endDate,
        DATE_FORMAT.DATE_TYPE
      ).format(DATE_FORMAT.DATE_TYPE);
    }
  }

  const addData: OfferTabItem = {
    offrCd:
      action === ACTION_CATAGORY.ADD
        ? dropData.value.objCode
        : selectedInPopoverItem.value.offrCd,
    offrNm:
      action === ACTION_CATAGORY.ADD
        ? dropData.value.objName
        : selectedInPopoverItem.value.offrNm,
    offrType:
      action === ACTION_CATAGORY.ADD
        ? tranferItemCode(dropData.value.itemCode)
        : selectedInPopoverItem.value.offrType,
    offrUuid:
      action === ACTION_CATAGORY.ADD
        ? dropData.value.objUuid
        : selectedInPopoverItem.value.offrUuid,
    validStartDtm: moment(
      action === ACTION_CATAGORY.ADD
        ? dateData.startDate
        : dateOfferData.startDate,
      DATE_FORMAT.DATE_TYPE
    ).format(DATE_FORMAT.DATE_TYPE),
    validEndDtm: validEndDate,
    itemValidEndDtm: "",
    itemValidStartDtm: "",
  };
  if (action === ACTION_CATAGORY.ADD) {
    addData["workTypeCode"] = dateData.endDate
      ? ACTION_TYPE.ADD_EXPIRED
      : ACTION_TYPE.ADD;
    listOffer.value.forEach((ofr: OfferTabItem) => {
      ofr.expand = false;
    });
    listOffer.value.unshift({ ...addData, isAdd: true });
  } else {
    addData["workTypeCode"] =
      actionType.value === ACTION_TYPE_NAME.EXPIRED
        ? ACTION_TYPE.EXPIRED
        : dateData.endDate
          ? ACTION_TYPE.ENABLED_EXPIRED
          : ACTION_TYPE.ENABLED;
    listOffer.value = listOffer.value.map((item) => {
      if (item.offrUuid === selectedInPopoverItem.value.offrUuid) {
        return { ...addData, isUpdate: true };
      }
      return item;
    });
  }
  handleClose(action);
};

const tranferItemCode = (code) => {
  switch (code) {
    case OFFER_TYPE.PRICEPLAN:
      return OFFER_PARAMS_TYPE.PRICE_PLAN.TYPE;
    case OFFER_TYPE.ADDON:
      return OFFER_PARAMS_TYPE.ADDON.TYPE;
    case OFFER_TYPE.DISCOUNT:
      return OFFER_PARAMS_TYPE.DISCOUNT.TYPE;
    case OFFER_TYPE.DEVICE:
      return OFFER_PARAMS_TYPE.DEVICE.TYPE;
    case OFFER_TYPE.RENTAL:
      return OFFER_PARAMS_TYPE.RENTAL.TYPE;
    default:
      break;
  }
};

const handleClose = (action) => {
  if (action === ACTION_CATAGORY.ADD) {
    isOpenAddOfferPopup.value = false;
    dateData.endDate = "";
    dateData.startDate = "";
  } else {
    isOpenChangeOfferPopup.value = false;
    dateOfferData.endDate = "";
    dateOfferData.startDate = "";
  }
};

const handleExpandDetail = async (event, item) => {
  item["expand"] = event;
  if (event && !item?.detail) {
    const res = await getProductStructureDetailRootApi({
      objUuid: item.offrUuid,
    });
    if (res?.data) {
      item["detail"] = res.data;
    }
  }
};

const paginatedData = computed(() => {
  if (!listOffer.value) return [];

  if (props.isEdit) {
    return listOffer.value;
  }
  const start = (props.pagination.currentPage - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return listOffer.value.slice(start, end);
});

watch(
  () => props.isEdit,
  (value) => {
    if (value) {
      listOffer.value = listOffer.value.map((item) => ({
        ...item,
        expand: false,
      }));
    }
  }
);

onMounted(() => {});
</script>

<style scoped>
/* :deep().v-field {
  height: 32px;
  display: flex;
  align-items: center;
}
:deep().v-field__field {
  text-overflow: ellipsis;
  width: 170px;
}
.offer-edit-datetime-picker :deep().dp__pointer {
  height: 32px;
}
.detail-content {
  display: flex;
  flex-direction: column;
  gap: 10px;
} */
</style>
