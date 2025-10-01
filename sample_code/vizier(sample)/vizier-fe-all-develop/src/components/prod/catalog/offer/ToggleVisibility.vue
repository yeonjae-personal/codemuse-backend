<script setup lang="ts">
import { useI18n } from "vue-i18n";
import { STRUCTURE_ITEM_SCREEN } from "@/constants/offer";
import moment from "moment-timezone";
import { LargeItemCode, TypeComponentCode } from "@/enums";
import { getListResourceType } from "@/api/prod/componentApi";
import {
  useSnackbarStore,
  useStructureStore,
  useProductsStore,
  useComponentStore,
  useCreateStructureStore,
  useProductsCreateStore,
  useProductsDuplicateStore,
  useDuplicateStructureStore,
} from "@/store";
import useRedirect from "@/composables/useRedirect";
import useDragUserPocket from "@/composables/useDragUserPocket";
import {
  formatDateWithOutSeconds,
  isExpired,
  isExpiredTime,
} from "@/utils/format-data";
import {
  ACTION_TYPE,
  ACTION_TYPE_NAME,
  DATE_FORMAT,
  DETAIL_TAB_TYPE,
  OFFER_TYPE,
} from "@/constants/";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import TrashIcon from "@/components/prod/icons/TrashIcon.vue";
import CheckVerified from "@/components/prod/icons/CheckVerified.vue";
import HourglassIcon from "@/components/prod/icons/HourglassIcon.vue";
import OpenInNewIcon from "@/components/prod/icons/OpenInNewIcon.vue";
import { setHoverColor } from "@/utils/impact-analysis-utils";
import type { ActionType } from "@/interfaces/prod";

const props = defineProps({
  data: {
    type: Object,
    default: () => ({}),
  },
  type: {
    type: String as PropType<TypeComponentCode>,
    default: "",
  },
  isEdit: {
    type: Boolean,
    default: false,
  },
  isAddItems: {
    type: Boolean,
    default: false,
  },
  screen: {
    type: String,
    default: STRUCTURE_ITEM_SCREEN.OFFER,
  },
  customClass: {
    type: String,
    default: "",
  },
  offerType: {
    type: String,
    default: OFFER_TYPE.PRICEPLAN,
  },
  isAdd: {
    type: Boolean,
    default: false,
  },
  duplicate: {
    type: Boolean,
    default: false,
  },
});

const { t } = useI18n();

const snackbarStore = useSnackbarStore();
const structureStore = useStructureStore();
const productStore = useProductsStore();
const productCreateStore = useProductsCreateStore();
const createStructureStore = useCreateStructureStore();
const productsDuplicateStore = useProductsDuplicateStore();
const duplicateStructureStore = useDuplicateStructureStore();
const { moveComponentSearchPage } = useRedirect();
const { handleDragUserPocket } = useDragUserPocket();

const selectedStore = computed(() => {
  if (props.isAdd) {
    return createStructureStore;
  } else if (props.duplicate) {
    return duplicateStructureStore;
  }
  return structureStore;
});

const productStoreSelected = computed(() => {
  if (props.isAdd) {
    return productCreateStore;
  } else if (props.duplicate) {
    return productsDuplicateStore;
  }
  return productStore;
});
const {
  selectedComponent,
  selectedComponentData,
  showComponentDetail,
  showStructureDetail,
  showListStructure,
  structureData,
  isDuplicate,
  listComponentDuplicate,
  resourceTypes,
  isPendingFinish,
} = storeToRefs(selectedStore.value);
const { actionType } = storeToRefs(productStoreSelected.value);
const { getComponentDetail } = useComponentStore();

const open = ref(true);
const selectedItem = ref<any>(null);
const isOpenPopup = ref(false);
const dateData = reactive({
  startDate: "",
  endDate: "",
});

const expired = computed(() => {
  return (
    moment(
      selectedItem.value?.relationValidEndDtm || dateData.endDate,
      DATE_FORMAT.DATE_TYPE
    ).diff(moment(), "days") < 0
  );
});

const toggleOpen = () => {
  open.value = !open.value;
};

const checkExpiredRelation = (item: any) => {
  if (
    moment(item?.relationValidEndDtm, DATE_FORMAT.DATE_TYPE).isSame(
      new Date(),
      "day"
    )
  ) {
    return true;
  }
  return !props.isAddItems && isExpired(item?.relationValidEndDtm);
};

const listActions = (item: any): ActionType[] => {
  const isItemExpired = isExpired(item?.validEndDtm);
  if (isItemExpired) return [];
  const isRelationExpired = checkExpiredRelation(item);
  const actions = {
    name: isRelationExpired
      ? t("product_platform.actionEnable")
      : t("product_platform.actionExpire"),
    icon: isRelationExpired ? CheckVerified : HourglassIcon,
    onClick: () => {
      actionType.value = isRelationExpired
        ? ACTION_TYPE_NAME.ENABLED
        : ACTION_TYPE_NAME.EXPIRED;
      selectedItem.value = item;
      dateData.startDate =
        formatDateWithOutSeconds(item?.relationValidStartDtm) || "";
      nextTick(() => {
        dateData.endDate =
          formatDateWithOutSeconds(item?.relationValidEndDtm) || "";
      });
      isOpenPopup.value = true;
    },
  };
  const openNewTabAction = {
    name: t("product_platform.openinNewWindow"),
    icon: OpenInNewIcon,
    onClick: () => {
      moveComponentSearchPage({
        code: item.objCode,
        itemId: item.objUuid,
        middleType: props.type,
        offerType: item.itemCode,
        name: item.objName,
        subType: item.itemCodeName,
        type: item.itemCode,
      });
    },
  };
  if (props.isAddItems || isDuplicate.value || isPendingFinish.value) {
    return [
      {
        name: t("product_platform.actionRemove"),
        icon: TrashIcon,
        onClick: () => {
          if (props.isAddItems) {
            selectedStore.value.removeDataFromAdd(item);
          } else {
            structureData.value = structureData.value.map((offer) => {
              if (offer.mctgrItemCode === props.type) {
                return {
                  ...offer,
                  componentList: offer.componentList.filter(
                    (com) => com.objUuid !== item.objUuid
                  ),
                };
              }
              return offer;
            });
          }
        },
      },
      openNewTabAction,
    ];
  }
  if (listComponentDuplicate.value.length) {
    return [
      {
        name: t("product_platform.actionRemove"),
        icon: TrashIcon,
        onClick: () => {
          listComponentDuplicate.value = listComponentDuplicate.value.filter(
            (item) => item.objUUID !== item.objUUID
          );
          switch (props.type) {
            case TypeComponentCode.Benefit:
              structureData.value.benefit = structureData.value.benefit.filter(
                (item) => item.objUUID !== item.objUUID
              );
              break;
            case TypeComponentCode.Characteristics:
              structureData.value.characteristics =
                structureData.value.characteristics.filter(
                  (item) => item.objUUID !== item.objUUID
                );
              break;
            case TypeComponentCode.Price:
              structureData.value.price = structureData.value.price.filter(
                (item) => item.objUUID !== item.objUUID
              );
              break;
            case TypeComponentCode.Service:
              structureData.value.service = structureData.value.service.filter(
                (item) => item.objUUID !== item.objUUID
              );
              break;
          }
        },
      },
      openNewTabAction,
    ];
  }
  return [actions, openNewTabAction];
};

const handleClose = () => {
  isOpenPopup.value = false;
};
const handleSubmit = () => {
  if (!dateData.startDate) {
    snackbarStore.showSnackbar(
      t("product_platform.required_start_date"),
      "error"
    );
    return;
  }
  if (!dateData.endDate && !expired.value) {
    snackbarStore.showSnackbar(
      t("product_platform.required_end_date"),
      "error"
    );
    return;
  }
  const updatedItem = {
    ...selectedItem.value,
    relationValidStartDtm: dateData.startDate,
    relationValidEndDtm: dateData.endDate,
    isExpired: !expired.value,
    offerType: props.type,
  };
  if (props.isAddItems) {
    updatedItem["workTypeCode"] = dateData.endDate
      ? ACTION_TYPE.ADD_EXPIRED
      : ACTION_TYPE.ADD;

    selectedStore.value.updateAddData(updatedItem);
  } else {
    updatedItem["workTypeCode"] =
      actionType.value === ACTION_TYPE_NAME.EXPIRED
        ? ACTION_TYPE.EXPIRED
        : dateData.endDate
          ? ACTION_TYPE.ENABLED_EXPIRED
          : ACTION_TYPE.ENABLED;

    selectedStore.value.updateDataFromListUpdate(updatedItem);
    selectedStore.value.updateDataFromList(updatedItem);
  }
  handleClose();
};

const handleClickComponentItem = async (item: any) => {
  try {
    if (selectedComponent.value?.objUUID !== item?.objUuid) {
      selectedComponent.value = {
        ...item,
        componentType: props.type,
      };
      getComponentDetailData(item?.objUuid);
      await checkComponentExistResource(item?.itemCode);
    }
    showStructureDetail.value = false;
    showListStructure.value = false;
    showComponentDetail.value = true;
  } catch (error: any) {
    snackbarStore.showSnackbar(
      error?.errorMsg || t("product_platform.something_went_wrong"),
      "error"
    );
  }
};

const checkComponentExistResource = async (itemCode) => {
  if (itemCode) {
    const result = await getListResourceType({
      itemCode,
    });
    resourceTypes.value = result?.data || [];
  }
};

const getComponentDetailData = async (objUUID: any) => {
  if (objUUID) {
    try {
      const res = await getComponentDetail({
        objUuid: objUUID,
      });
      if (res?.data) {
        selectedComponentData.value = {
          general: [
            ...res.data.general.map((item) => {
              if (item.fieldTypeCode === COLUMN_FIELD_TYPE.DP) {
                return {
                  ...item,
                  attrVal: item.attrVal,
                };
              } else if (item.colName === "item_code") {
                return {
                  ...item,
                  fieldTypeCode: COLUMN_FIELD_TYPE.DL,
                };
              } else if (item.colName === "item_large_code") {
                return {
                  ...item,
                  fieldTypeCode: COLUMN_FIELD_TYPE.DL,
                };
              }
              return item;
            }),
            ...res.data?.additional
              .filter((item) => item.dispTab === DETAIL_TAB_TYPE.GENERAL)
              .map((item: any) => ({
                ...item,
                attrVal:
                  item.fieldTypeCode === COLUMN_FIELD_TYPE.DM
                    ? JSON.parse(item?.attrVal)?.filter((eco: any) =>
                        eco.trim()
                      ) || []
                    : item.attrVal,
              })),
          ],
          additional: res.data?.additional
            .filter((item) => item.dispTab !== DETAIL_TAB_TYPE.GENERAL)
            .map((item: any) => ({
              ...item,
              attrVal:
                item.fieldTypeCode === COLUMN_FIELD_TYPE.DM
                  ? JSON.parse(item?.attrVal)?.filter((eco: any) =>
                      eco.trim()
                    ) || []
                  : item.attrVal,
            })),
        };
        selectedComponentData.value.itemCode =
          selectedComponentData.value.itemDetlTypeCd || "";
      } else {
        selectedComponentData.value = null;
      }
    } catch (error: any) {
      selectedComponentData.value = null;
      snackbarStore.showSnackbar(
        error?.errorMsg || t("product_platform.something_went_wrong"),
        "error"
      );
    }
  }
};
</script>

<template>
  <div :class="customClass">
    <div
      v-if="data?.items?.length > 0 && !isAddItems"
      class="flex gap-2 cursor-pointer text-[10px] text-gray-500 mb-3 bg-darker p-1 w-fit rounded-[4px]"
      @click="toggleOpen"
    >
      <div>{{ data.itemCodeName || "" }}</div>
      <div>
        <v-icon v-if="!open" class="text-gray-500" size="14px">
          mdi-expand-all
        </v-icon>
        <v-icon v-else class="text-gray-500" size="14px"
          >n m mdi-collapse-all
        </v-icon>
      </div>
    </div>
    <div v-if="open" class="flex flex-col gap-3">
      <cf-card-dropdown
        v-for="item in data.items"
        :key="item.objUuid"
        :item="item"
        :title="item?.objName"
        :description="item.objCode"
        type-bg="linear"
        :border-color-action="setHoverColor(type)"
        :display-border-left="setHoverColor(type)"
        show-icon-status
        :actions="isAddItems || isEdit ? listActions(item) : []"
        editable
        :node="{
          hideNodeLeft: true,
          isActiveNodeLeft: false,
          hideNodeRight: true,
          isActiveNodeRight: false,
        }"
        :disable="isExpiredTime(item.validEndDtm)"
        :expired="isExpiredTime(item.relationValidEndDtm)"
        :active="
          selectedComponent?.objUuid &&
          selectedComponent?.objUuid === item?.objUuid
        "
        :is-new="isEdit && isAddItems"
        draggable
        @on-click-card="handleClickComponentItem(item)"
        @dragstart="
          handleDragUserPocket($event, {
            userPocketType: LargeItemCode.Component,
            ...item,
          })
        "
      />
    </div>
  </div>

  <DateTimePopup
    v-model:open-model="isOpenPopup"
    v-model="dateData"
    :modal-title="
      checkExpiredRelation(selectedItem)
        ? $t('product_platform.enableComponent')
        : $t('product_platform.expireComponent')
    "
    :min-end-date="
      moment().format(DATE_FORMAT.DATE_FORMAT_WITHOUT_TIME_REVERSE)
    "
    :disabled-start-date="!checkExpiredRelation(selectedItem)"
    :text-btn-cancel="$t('product_platform.cancel')"
    :text-btn-add="$t('product_platform.save')"
    required-start-date
    :required-end-date="!isExpiredTime(selectedItem?.relationValidEndDtm)"
    @submit="handleSubmit"
    @close="handleClose"
  />
</template>
