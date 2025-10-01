<script setup lang="ts">
import moment from "moment-timezone";
import { useI18n } from "vue-i18n";
import { cloneDeep } from "lodash-es";
import { COMPONENT_ACTION_TYPE } from "@/constants/component";
import { ACTION_TYPE, DATE_FORMAT, MULTI_OR_SINGLE } from "@/constants/index";
import { RESOURCE_ITEM_CODE } from "@/constants/resource";
import { IResourceItem } from "@/interfaces/prod/resource";
import { useDragStore, useSnackbarStore } from "@/store";
import { LARGE_ITEM_CODE } from "@/store/userPocket.store";
import useRedirect from "@/composables/useRedirect";
import useDragUserPocket from "@/composables/useDragUserPocket";
import { formatDateWithOutSeconds, isExpiredTime } from "@/utils/format-data";
import { isValidDifferentItemRule } from "@/utils/custom-validation";
import CheckVerified from "@/components/prod/icons/CheckVerified.vue";
import HourglassIcon from "@/components/prod/icons/HourglassIcon.vue";
import TrashIcon from "@/components/prod/icons/TrashIcon.vue";
import OpenInNewIcon from "@/components/prod/icons/OpenInNewIcon.vue";
import type { ActionType } from "@/interfaces/prod";
import { getResourceDetailApi } from "@/api/prod/resourceApi";
import { ResourceTabItem } from "@/types/common";

const { showSnackbar } = useSnackbarStore();
const { moveResourceSearchPage } = useRedirect();
const { handleDragUserPocket } = useDragUserPocket();
const emits = defineEmits(["update:modelList", "onClickDrogArae"]);

type Props = {
  isEdit?: boolean;
  isDuplicate?: boolean;
  filteredRule?: Array<any>;
  modelList: ResourceTabItem[];
  relationItem?: any;
  typeAllowDrop?: Array<any>;
};

const props = withDefaults(defineProps<Props>(), {
  isEdit: false,
  isDuplicate: false,
  relationItem: undefined,
  filteredRule: () => [],
  modelList: () => [],
  typeAllowDrop: () => [],
});

const listResource = computed({
  get() {
    return props.modelList;
  },
  set(newVal) {
    emits("update:modelList", newVal);
  },
});

const { dragOfferType } = storeToRefs(useDragStore());
const { t } = useI18n();
const actionType = ref("");
const resourceInfo = ref<any[]>([]);
const openPopupDate = ref<boolean>(false);
const selectedInPopoverItem = ref<ResourceTabItem | null>(null);
const dropData = ref<IResourceItem | any>(null);
const dateExpireData = reactive({
  startDate: "",
  endDate: "",
});

const isOpenPopupExpireDate = ref(false);
const currentResourceActive = ref<any>(null);

const dateAddPicker = reactive({
  startDate: "",
  endDate: "",
});

const allowDrop = async (event: DragEvent) => {
  event.preventDefault();
};

const drop = async (event: any) => {
  event.preventDefault();
  dropData.value = event.dataTransfer.getData("item")
    ? JSON.parse(event.dataTransfer.getData("item"))
    : null;
  try {
    const res = await getResourceDetailApi({
      objUuid: dropData.value?.uuid,
      itemCode: dropData.value?.itemCode,
    });
    resourceInfo.value = res.data.additional;
  } catch (error: any) {
    showSnackbar(
      error?.errorMsg || t("product_platform.something_went_wrong"),
      "error"
    );
  }
  const result = isValidDifferentItemRule(
    props.filteredRule,
    resourceInfo.value
  );
  let getTypeAccept = props.typeAllowDrop?.find(
    (item) => item.trgtItemCode === dragOfferType.value
  );

  if (getTypeAccept) {
    let itemsNotExpired = listResource.value.filter(
      (item) =>
        !isExpiredTime(item?.validEndDtm) &&
        item.itemCode === dragOfferType.value
    );

    let checkExisted = itemsNotExpired?.some(
      (item) => item.itemCode === dragOfferType.value
    );

    if (checkExisted && getTypeAccept.strcTypeCode === MULTI_OR_SINGLE.SINGLE) {
      showSnackbar(t("resource.invalidEntitySingleMsg"), "error");
      return;
    }
  } else {
    showSnackbar(t("resource.dropResourceFail"), "error");
    return;
  }

  // Need click add first and then drag item
  if (!result) {
    showSnackbar(t("product_platform.invalidCustomValidation"), "error");
    return;
  }
  if (dropData.value) {
    actionType.value = COMPONENT_ACTION_TYPE.ADD;
    openPopupDate.value = true;
  }
};

const checkExpiredRelation = (item: ResourceTabItem) => {
  if (
    moment(item?.relationEndDate, DATE_FORMAT.DATE_TYPE).isSame(
      new Date(),
      "day"
    )
  ) {
    return true;
  }
  return isExpiredTime(item?.relationEndDate);
};

const listActions = (item: ResourceTabItem): ActionType[] => {
  const isResourceExpired = isExpiredTime(item?.validEndDtm);
  if (isResourceExpired) return [];
  const isRelationExpired = checkExpiredRelation(item);
  const enableAction = {
    name: isRelationExpired
      ? t(`product_platform.actionEnable`)
      : t(`product_platform.actionExpire`),
    icon: isRelationExpired ? CheckVerified : HourglassIcon,
    onClick: () => {
      actionType.value = isRelationExpired
        ? COMPONENT_ACTION_TYPE.ENABLED
        : COMPONENT_ACTION_TYPE.EXPIRED;
      dateExpireData.startDate =
        formatDateWithOutSeconds(item?.relationStartDate) || "";
      dateExpireData.endDate =
        formatDateWithOutSeconds(item?.relationEndDate) || "";
      selectedInPopoverItem.value = cloneDeep(item);

      isOpenPopupExpireDate.value = true;
    },
  };
  const openNewTabAction = {
    name: t("product_platform.openinNewWindow"),
    icon: OpenInNewIcon,
    onClick: () => {
      moveResourceSearchPage({
        itemCode: item.itemCode,
        objCode: item.objCode,
      });
    },
  };
  if (item.isAdd || props.isDuplicate) {
    return [
      {
        name: t(`product_platform.actionRemove`),
        icon: TrashIcon,
        onClick: () => {
          const index = listResource.value.findIndex(
            (item) => item.objUuid === item.objUuid
          );
          if (index !== -1) {
            listResource.value.splice(index, 1);
          }
        },
      },
      openNewTabAction,
    ];
  }
  return [enableAction, openNewTabAction];
};

const handleClickResourceItem = async (item: any) => {
  currentResourceActive.value = item;
};

const handleShowExpand = async (event, item: any) => {
  item["expand"] = event;
  currentResourceActive.value = item;
  if (event) {
    await getResourceItemDetail(item);
  }
};
const getResourceItemDetail = async (item: any) => {
  try {
    const res = await getResourceDetailApi({
      objUuid: item.objUuid,
      itemCode: item.itemCode,
    });
    item["detail"] = {
      general: res?.data?.general,
      additional: res?.data?.additional,
    };
  } catch (err: any) {
    item["detail"] = null as any;
    showSnackbar(
      err?.errorMsg || t("product_platform.something_went_wrong"),
      "error"
    );
  }
};

const handleChangeRelationDate = () => {
  if (!selectedInPopoverItem.value?.objUuid) return;

  if (!dateExpireData.startDate) {
    showSnackbar(t("product_platform.required_start_date"), "error");
    return;
  }

  if (!isExpiredTime(selectedInPopoverItem.value?.relationEndDate)) {
    if (!dateExpireData.endDate) {
      showSnackbar(t("product_platform.required_end_date"), "error");
      return;
    }
  }

  const indexInListResource = listResource.value.findIndex(
    (item) => item.objUuid === selectedInPopoverItem.value?.objUuid
  );

  if (indexInListResource !== -1) {
    listResource.value[indexInListResource as number] = {
      ...selectedInPopoverItem.value,
      relationStartDate: dateExpireData.startDate,
      relationEndDate: dateExpireData.endDate,
      isUpdate: true,
      workTypeCode:
        actionType.value === COMPONENT_ACTION_TYPE.EXPIRED
          ? ACTION_TYPE.EXPIRED
          : dateExpireData.endDate
            ? ACTION_TYPE.ENABLED_EXPIRED
            : ACTION_TYPE.ENABLED,
      resourceUUID: selectedInPopoverItem.value.objUuid,
      componentUUID: props.relationItem.objUuid,
    };
  }
  isOpenPopupExpireDate.value = false;
};

const handleCloseExpireDate = () => {
  isOpenPopupExpireDate.value = false;
  dateExpireData.endDate = "";
  dateExpireData.startDate = "";
};

const handleSubmitAdd = () => {
  if (!dateAddPicker.startDate) {
    showSnackbar(t("product_platform.required_start_date"), "error");
    return;
  }
  listResource.value.unshift({
    itemCode: dropData.value.itemCode,
    objUuid: dropData.value.uuid,
    objName: dropData.value.name,
    objCode: dropData.value.code,
    validEndDtm: dropData.value.endDate,
    validStartDtm: dropData.value.startDate,
    relationEndDate: dateAddPicker.endDate,
    relationStartDate: dateAddPicker.startDate,
    workTypeCode: dateAddPicker.endDate
      ? ACTION_TYPE.ADD_EXPIRED
      : ACTION_TYPE.ADD,
    resourceUUID: dropData.value.uuid,
    componentUUID: props.relationItem?.objUuid,
    isAdd: true,
  });
  listResource.value.forEach((rsc) => {
    rsc["expand"] = false;
  });
  handleClosePopupDate();
  dragOfferType.value = "";
};

const handleClosePopupDate = () => {
  openPopupDate.value = false;
  dateAddPicker.startDate = "";
  dateAddPicker.endDate = "";
};

watch(
  () => props.isEdit,
  (val) => {
    if (val) {
      listResource.value.forEach((item) => {
        item.expand = false;
      });
    }
  }
);
</script>

<template>
  <div class="px-[6px]">
    <ItemDrop
      v-if="isEdit && (!isExpiredTime(relationItem?.endDate) || isDuplicate)"
      class-name="mb-3"
      @dragover="allowDrop($event)"
      @drop="drop($event)"
      @click.stop="emits('onClickDrogArae')"
    />
    <div
      v-if="listResource.length"
      class="flex flex-col gap-3 font-size-base py-2"
    >
      <template v-for="item in listResource" :key="item.objUuid">
        <cf-card-dropdown
          type-bg="light"
          border-color-action="purple"
          class-name="card-round-style"
          :item="item"
          :title="item.objName"
          :description="item.objCode"
          :active="item.objUuid === currentResourceActive?.objUuid"
          :draggable="true"
          :node="{
            hideNodeLeft: true,
            isActiveNodeLeft: false,
            hideNodeRight: true,
            isActiveNodeRight: false,
          }"
          :actions="listActions(item)"
          :disable="isExpiredTime(item.validEndDtm)"
          :expired="isExpiredTime(item.relationEndDate)"
          :expand="item?.expand"
          :editable="isEdit || isDuplicate"
          :is-new="item.isAdd"
          show-icon-status
          @on-click-card="handleClickResourceItem(item)"
          @on-click-show-detail="handleShowExpand($event, item)"
          @dragstart="
            handleDragUserPocket($event, {
              userPocketType: LARGE_ITEM_CODE.RESOURCE,
              ...item,
            })
          "
        >
          <template #icon>
            <span class="flex justify-center align-center w-[40px] h-[40px]">
              <template
                v-if="item.itemCode === RESOURCE_ITEM_CODE.RATING_ELEMENT"
              >
                <RLinearIcon />
              </template>
              <template
                v-if="item.itemCode === RESOURCE_ITEM_CODE.BILLING_ELEMENT"
              >
                <BLinearIcon />
              </template>
              <template
                v-if="item.itemCode === RESOURCE_ITEM_CODE.SERVICE_ELEMENT"
              >
                <SLinearIcon />
              </template>
            </span>
          </template>
          <template #detail>
            <ProductGrid :data="item.detail" :type="LARGE_ITEM_CODE.RESOURCE" />
          </template>
        </cf-card-dropdown>
      </template>
    </div>
    <div v-else :class="isEdit ? 'h-[calc(100vh-439px)]' : 'h-full'">
      <NoData />
    </div>
  </div>

  <DateTimePopup
    v-model:open-model="openPopupDate"
    v-model="dateAddPicker"
    :modal-title="$t('product_platform.addResource')"
    :min-end-date="
      moment().format(DATE_FORMAT.DATE_FORMAT_WITHOUT_TIME_REVERSE)
    "
    :text-btn-cancel="$t('product_platform.cancel')"
    :text-btn-add="$t('product_platform.save')"
    required-start-date
    @submit="handleSubmitAdd"
    @close="handleClosePopupDate"
  />

  <DateTimePopup
    v-model:open-model="isOpenPopupExpireDate"
    v-model="dateExpireData"
    :modal-title="
      isExpiredTime(
        selectedInPopoverItem && selectedInPopoverItem?.relationEndDate
      )
        ? $t('product_platform.enableResource')
        : $t('product_platform.expireResource')
    "
    :disabled-start-date="
      !isExpiredTime(
        selectedInPopoverItem && selectedInPopoverItem?.relationEndDate
      )
    "
    :required-start-date="true"
    :min-end-date="
      moment().format(DATE_FORMAT.DATE_FORMAT_WITHOUT_TIME_REVERSE)
    "
    :required-end-date="
      !isExpiredTime(
        selectedInPopoverItem && selectedInPopoverItem?.relationEndDate
      )
    "
    :text-btn-cancel="$t('product_platform.cancel')"
    :text-btn-add="$t('product_platform.save')"
    @submit="handleChangeRelationDate"
    @close="handleCloseExpireDate"
  />
</template>
