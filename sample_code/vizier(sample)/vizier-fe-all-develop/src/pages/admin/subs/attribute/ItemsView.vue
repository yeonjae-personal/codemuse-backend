<template>
  <div class="bg-white p-3 pt-6 col-span-2 rounded-lg">
    <div class="flex justify-between items-center pl-3 pr-3 pb-3">
      <div class="flex align-center gap-2 items-end">
        <h1 class="font-medium text-base text-text-base tracking-[0.5px]">
          {{ t("product_platform.items_view") }}
        </h1>
      </div>
      <div class="flex gap-x-2">
        <BaseButton
          v-if="!isEditModeItemView"
          :color="ButtonColorType.Secondary"
          @click="handleClickEdit"
        >
          <EditIcon class="mr-[6px]" />
          {{ $t("product_platform.edit") }}
        </BaseButton>
        <BaseButton
          v-if="isEditModeItemView"
          :color="ButtonColorType.Gray"
          @click="handleClickCancel"
        >
          {{ t("product_platform.cancel") }}
        </BaseButton>
        <BaseButton
          v-if="isEditModeItemView && itemDetail.generalInfo.isNew"
          :color="ButtonColorType.Secondary"
          @click="handleClickSave"
        >
          <SaveIcon class="mr-[6px]" />
          {{ $t("product_platform.save") }}
        </BaseButton>
      </div>
    </div>
    <div class="item-view-wrapper">
      <div class="item-view-header">
        <div class="header-item">{{ t("product_platform.selectBoxItem") }}</div>
        <div class="header-item">{{ t("product_platform.Type") }}</div>
        <div class="header-item">{{ t("product_platform.subType") }}</div>
      </div>
      <div class="item-view-content">
        <LocomotiveComponent
          v-show="isMounted"
          scroll-container-class="!px-[0px]"
          scroll-content-class="flex"
          top-content-class="flex pt-[64px]"
          dynamic-scroll-key="ITEMS_VIEW_ATTRIBUTE"
          :number-scroll-y="currentY"
          :is-scroll-when-add-new="isScrollWhenAddNew"
          show-float-button
          is-custom-float-button
          is-dynamic-scroll
        >
          <div class="content-item">
            <ItemMenu
              v-for="item in listItems"
              :id="item.id"
              :ref="`item-${item.id}`"
              :key="item.name"
              :name="item.name"
              :selected="item.isOpenChild"
              :main-item="true"
              :is-open-child="item.isOpenChild"
              :is-edit="isEditModeItemView && item.code !== 'C'"
              @on-open-child="handleOpenType"
              @on-add-new="handleAddNewType(item)"
            />
          </div>
          <div
            v-if="selectedItemViewCode && listCoordinatesType.length"
            class="connect-line-type"
          >
            <BaseCanvas
              id="attribute-management-line-1"
              :height="heightConnectLineType"
              :width="60"
              :list-coordinates="listCoordinatesType"
              :mid-point="20"
            />
          </div>
          <div v-if="selectedItemViewCode" class="type-subtype-wrapper">
            <div
              v-for="type in filterTypes"
              :key="type.code"
              class="type-subtype-item"
            >
              <div class="content-item-2">
                <ItemMenu
                  :id="type.id"
                  :ref="`item-${type.id}`"
                  :key="type.code"
                  :name="type.name"
                  :selected="type.isSelected"
                  :is-open-child="type.isOpenChild"
                  :is-hide-action-button="type.parentCode !== 'C'"
                  :is-new="type.isNew"
                  :is-edit="isEditModeItemView"
                  :is-used="type.isUsed"
                  :margin-bottom="calcMarginBottom(type)"
                  @on-open-child="handleOpenChild"
                  @on-selected="handleSelectedType(type)"
                  @on-add-new="handleAddNewSubType(type)"
                />
              </div>
              <div v-if="type.isOpenChild" class="connect-line">
                <BaseCanvas
                  id="attribute-management-line-2"
                  :height="heightConnectLine(type.children)"
                  :width="81"
                  :list-coordinates="listCoordinates(type.children)"
                />
              </div>
              <div v-if="type.isOpenChild" class="content-item-2">
                <ItemMenu
                  v-for="subType in type.children"
                  :id="subType.id"
                  :ref="`item-${subType.id}`"
                  :key="subType.code"
                  :name="subType.name"
                  :selected="subType.isSelected"
                  :is-open-child="subType.isOpenChild"
                  :is-hide-action-button="true"
                  :is-new="subType.isNew"
                  :is-used="subType.isUsed"
                  @on-selected="handleSelectedSubType(subType)"
                />
              </div>
            </div>
          </div>
        </LocomotiveComponent>
      </div>
    </div>
    <BasePopup
      v-if="isShowPopupCancel"
      v-model="isShowPopupCancel"
      :content="t('product_platform.desc_cancel')"
      :icon="DialogIconType.Warning"
      :cancel-button-text="t('product_platform.btn_no')"
      :submit-button-text="t('product_platform.btn_yes')"
      @on-close="handleClosePopupCancel"
      @on-submit="handleSubmitPopupCancel"
    />
    <BasePopup
      v-if="isShowPopupSave"
      v-model="isShowPopupSave"
      :content="t('product_platform.desc_update')"
      :icon="DialogIconType.Info"
      :cancel-button-text="t('product_platform.btn_no')"
      :submit-button-text="t('product_platform.btn_yes')"
      @on-close="handleClosePopupSave"
      @on-submit="handleSubmitPopupSave"
    />
    <BasePopup
      v-if="isShowPopupSaveConfirm"
      v-model="isShowPopupSaveConfirm"
      :content="t('product_platform.desc_update')"
      :icon="DialogIconType.Info"
      :cancel-button-text="t('product_platform.btn_no')"
      :submit-button-text="t('product_platform.btn_yes')"
      @on-close="handleClosePopupSaveConfirm"
      @on-submit="handleSubmitPopupSaveConfirm"
    />
  </div>
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";
import isEqual from "lodash-es/isEqual";
import CfButtonIcon from "@/components/controls/CfButtonIcon.vue";
import ItemMenu from "./items-view/ItemMenu.vue";
import BaseCanvas from "@/components/prod/common/BaseCanvas.vue";
import attributeManagementStore from "@/store/admin/attributeManagement.store";
import LocomotiveComponent from "@/components/prod/common/LocomotiveComponent.vue";
import SaveIcon from "@/components/prod/icons/SaveIcon.vue";
import BaseButton from "@/components/prod/common/BaseButton.vue";
import { ButtonColorType, DialogIconType } from "@/enums";
import BasePopup from "@/components/prod/common/BasePopup.vue";
import { TABS_OFFER_DETAILS } from "@/constants/offer";

const { t } = useI18n();
const {
  listItems,
  listTypes,
  selectedItemViewCode,
  isEditModeItemView,
  isEditModeItemEdit,
  isShowLabelSearch,
  isShowAttributeCodeSearch,
  selectedItem,
  selectedTab,
  itemDetail,
  originItemDetail,
} = storeToRefs(attributeManagementStore());
const {
  setOpenType,
  setOpenChild,
  setSelectedType,
  setSelectedSubType,
  addSubType,
  addType,
  checkIsExistAddNewItem,
  getListItemsApi,
  cloneDetailItem,
  saveAllAttributes,
  resetItemDetail,
  getItemDetailApi,
  getLoupItemsApi,
  handleResetSearchLabel,
  handleResetSearchCommCode,
} = attributeManagementStore();

const isShowPopupCancel = ref(false);
const isShowPopupSave = ref(false);
const isShowPopupSaveConfirm = ref(false);
const nextFunction = ref<(id: string) => void>(() => {});
const nextFunctionParam = ref("");
const isMounted = ref<boolean>(false);
const isScrollWhenAddNew = ref<boolean>(false);
const instance = getCurrentInstance();
const currentY = ref<number>(0);

const filterTypes = computed(() => {
  return listTypes.value.filter(
    (item) => item.parentCode === selectedItemViewCode.value
  );
});

const listCoordinates = (children) => {
  return children
    .map((item, index) => ({
      leftStartPoint: 0,
      topStartPoint: 24,
      leftMovePoint: 81,
      topMovePoint: index === 0 ? 24 : 60 * index + 24,
      lineWidth: 2,
      strokeStyle: item.isSelected ? "#D9325A" : "#bdc1c7",
      selected: item.isSelected,
    }))
    .sort((cur, next) => cur.selected - next.selected);
};

const heightConnectLine = (children) => {
  return children.length * 60;
};

const calcMarginBottom = (item) => {
  return item.isOpenChild
    ? item.children.length > 0
      ? 60 * item.children.length - 48
      : 12
    : 12;
};

const listCoordinatesType = computed(() => {
  const currentSelectedItem = listItems.value.find((item) => item.isOpenChild);
  const currentSelectedIndex = listItems.value.findIndex(
    (item) => item.isOpenChild
  );
  let result: any = [];
  if (currentSelectedItem) {
    const listType = filterTypes.value.filter(
      (item) => item.parentCode === currentSelectedItem.code
    );
    let topMovePoint = 0;
    result = listType
      .map((item, index) => {
        const prevItem = index > 0 ? listType[index - 1] : null;
        if (prevItem) {
          const length = prevItem.isOpenChild ? prevItem.children.length : 0;
          topMovePoint += length > 0 ? length * 60 : 60;
        } else {
          topMovePoint += 24;
        }
        return {
          leftStartPoint: 0,
          topStartPoint:
            currentSelectedIndex === 0 ? 24 : 60 * currentSelectedIndex + 24,
          leftMovePoint: 60,
          topMovePoint: topMovePoint,
          lineWidth: 2,
          strokeStyle: item.isSelected ? "#D9325A" : "#bdc1c7",
          isSelected: item.isSelected,
        };
      })
      .sort((cur: any, next: any) => cur.isSelected - next.isSelected);
  }
  return result;
});

const heightConnectLineType = computed(() => {
  const height = Math.max(
    ...listCoordinatesType.value.map((item) => item.topMovePoint)
  );
  return height < 312 ? 312 : height;
});

const isNotChangeValue = (): boolean => {
  if (itemDetail.value?.generalInfo?.isNew) return false;
  return isEqual(itemDetail.value, originItemDetail.value);
};

//Handle events
const checkIsViewMode = (item, nexFunc) => {
  if (isNotChangeValue()) return true;
  if (
    selectedItem.value &&
    selectedItem.value != item.id &&
    isEditModeItemEdit.value
  ) {
    isShowPopupSave.value = true;
    nextFunctionParam.value = item;
    nextFunction.value = nexFunc;
    return false;
  }
  return true;
};

const handleOpenType = (id) => {
  setOpenType(id);
};

const handleOpenChild = (id) => {
  setOpenChild(id);
};

const handleSelectedType = (item) => {
  if (item.children.length > 0 || item.id === selectedItem.value?.id) return;
  //define function for reuse
  const funcSelectedType = async (item) => {
    await getLoupItemsApi(item.largeItemCode);
    setSelectedType(item.id);
    // Call api and update data
    const payload = {
      code: item.id,
      largeItemCode: item.largeItemCode,
      middleItemCode: item.middleItemCode,
    };
    isEditModeItemEdit.value = false;
    getItemDetailApi(payload);
  };
  if (checkIsViewMode(item, funcSelectedType)) {
    funcSelectedType(item);
    isEditModeItemEdit.value = false;
    selectedTab.value = TABS_OFFER_DETAILS.GENERAL;
  }
};

const handleSelectedSubType = (item) => {
  if (item.id === selectedItem.value?.id) return;
  //define function for reuse
  const funcSelectedSubType = async (item) => {
    await getLoupItemsApi(item.largeItemCode);
    setSelectedSubType(item.id);
    // Call api and update data
    const payload = {
      code: item.id,
      largeItemCode: item.largeItemCode,
      middleItemCode: item.middleItemCode,
    };
    isEditModeItemEdit.value = false;
    getItemDetailApi(payload);
  };

  if (checkIsViewMode(item, funcSelectedSubType)) {
    funcSelectedSubType(item);
    isEditModeItemEdit.value = false;
    selectedTab.value = TABS_OFFER_DETAILS.GENERAL;
  }
};

watch(
  selectedItem,
  (value) => {
    nextTick(() => {
      if (value && value.id.length > 20) {
        isScrollWhenAddNew.value = true;
        const element = instance?.proxy?.$refs[`item-${value?.id}`]![0]?.$el;
        currentY.value = element.offsetTop + 160;
      }
    });
  },
  { deep: true }
);

const handleAddNewSubType = (item) => {
  isScrollWhenAddNew.value = false;
  const addSubTypeFunc = async (item) => {
    await getLoupItemsApi(item.largeItemCode);
    addSubType(item.id);
  };
  if (checkIsViewMode(item, addSubTypeFunc)) {
    addSubTypeFunc(item);
    selectedTab.value = TABS_OFFER_DETAILS.GENERAL;
  }
};

const handleAddNewType = (item) => {
  isScrollWhenAddNew.value = false;
  const addTypeFunc = async (item) => {
    await getLoupItemsApi(item.largeItemCode);
    addType(item.id);
  };
  if (checkIsViewMode(item, addTypeFunc)) {
    addTypeFunc(item);
    selectedTab.value = TABS_OFFER_DETAILS.GENERAL;
  }
};

const handleClickEdit = () => {
  isEditModeItemView.value = true;
  cloneDetailItem({});
  handleResetSearchLabel();
  handleResetSearchCommCode();
};

const handleClickSave = () => {
  if (isNotChangeValue()) {
    isShowPopupSaveConfirm.value = false;
    isEditModeItemView.value = false;
    isShowLabelSearch.value = false;
    isShowAttributeCodeSearch.value = false;
    handleResetSearchLabel();
    handleResetSearchCommCode();
    return;
  }
  isShowPopupSaveConfirm.value = true;
};

const handleClickCancel = () => {
  if (itemDetail.value.generalInfo.isNew) {
    isShowPopupCancel.value = true;
  } else {
    isEditModeItemView.value = false;
  }
};

// Popup when click cancel button
const handleClosePopupCancel = () => {
  isShowPopupCancel.value = false;
};

const handleSubmitPopupCancel = () => {
  if (itemDetail.value.generalInfo.isNew) {
    isEditModeItemEdit.value = false;
  }
  isShowPopupCancel.value = false;
  isShowLabelSearch.value = false;
  isShowAttributeCodeSearch.value = false;
  isEditModeItemView.value = false;
  cloneDetailItem({ reset: true });
  handleResetSearchLabel();
  handleResetSearchCommCode();
  if (checkIsExistAddNewItem()) {
    resetItemDetail();
    selectedItem.value = null;
  }
};

// Popup when switch other item
const handleClosePopupSave = () => {
  isShowPopupSave.value = false;
  isShowLabelSearch.value = false;
  isShowAttributeCodeSearch.value = false;
  selectedTab.value = TABS_OFFER_DETAILS.GENERAL;
  cloneDetailItem({ reset: true });
  resetItemDetail();
  nextFunction.value?.(nextFunctionParam.value);
};

const handleSubmitPopupSave = async () => {
  isShowPopupSave.value = false;
  //Save item
  const flag = await saveAllAttributes();
  if (flag) {
    isShowLabelSearch.value = false;
    isShowAttributeCodeSearch.value = false;
    selectedTab.value = TABS_OFFER_DETAILS.GENERAL;
    cloneDetailItem({ reset: true });
    nextFunction.value?.(nextFunctionParam.value);
    handleResetSearchLabel();
    handleResetSearchCommCode();
  }
};

const handleClosePopupSaveConfirm = () => {
  isShowPopupSaveConfirm.value = false;
};

const handleSubmitPopupSaveConfirm = async () => {
  isShowPopupSaveConfirm.value = false;
  const flag = await saveAllAttributes();
  if (flag) {
    isEditModeItemView.value = false;
    isEditModeItemEdit.value = false;
    isShowLabelSearch.value = false;
    isShowAttributeCodeSearch.value = false;
    handleResetSearchLabel();
    handleResetSearchCommCode();
  }
};

onBeforeMount(() => {
  if (listItems.value.length === 0) {
    getListItemsApi();
  }
});

onMounted(() => {
  nextTick(() => {
    isMounted.value = true;
  });
});
</script>
<style scoped lang="scss">
.item-view-wrapper {
  display: flex;
  flex-direction: column;
  row-gap: 16px;
  font-family: "Noto Sans KR";
  background-color: #f0f2f5;
  border-radius: 8px;
  height: calc(100% - 52px);
  position: relative;
  .item-view-header {
    display: flex;
    column-gap: 24px;
    padding: 0 24px;
    margin-bottom: 16px;
    position: absolute;
    z-index: 99;
    width: 100%;
    .header-item {
      width: 256px;
      height: 48px;
      border-radius: 0 0 12px 12px;
      background-color: #f7f8fa;
      box-shadow: 0px 2px 16px 0px #0000001f;
      display: flex;
      justify-content: center;
      align-items: center;
      color: #6b6d70;
      font-size: 13px;
      font-weight: 500;
      &:first-child {
        background: #ba1642;
        color: #ffffff;
      }
    }
  }
  .item-view-content {
    display: flex;
    padding: 0 24px;
    overflow: auto;
    position: relative;
    height: 100%;
    &::-webkit-scrollbar {
      width: 0;
    }

    &::-webkit-scrollbar-thumb {
      background: #dce0e5;
      border-radius: 999px;
    }

    &::-webkit-scrollbar-track {
      background: transparent;
    }
    .type-subtype-wrapper {
      display: flex;
      flex-direction: column;
      width: 481px;
      padding: 12px 0;
    }
    .type-subtype-item {
      display: flex;
      justify-content: space-between;
    }
    .content-item {
      width: 240px;
      padding: 12px 20px;
      display: flex;
      flex-direction: column;

      &:first-child {
        background-color: #e6e9ed;
        border-radius: 24px;
        row-gap: 12px;
        padding: 12px;
        max-height: 312px;
        margin-left: 8px;
      }
    }
    .content-item-2 {
      width: 200px;
      display: flex;
      flex-direction: column;
    }
    .connect-line-type {
      padding-top: 12px;
    }
    .scroll-section {
      position: absolute;
      bottom: 0;
      left: 0;
      height: 80px;
      width: 100%;
      background: blue;
      opacity: 0.5;
    }
  }
}
</style>
