<template>
  <div
    class="bg-white p-4 pt-6 pb-3 rounded-lg relative"
    :class="{ 'edit-mode': isEditModeItemEdit }"
  >
    <div class="flex justify-between items-center pl-3 pr-3 pb-3">
      <div class="flex align-center gap-2 items-end">
        <h1 class="font-medium text-base text-text-base tracking-[0.5px]">
          {{
            isEditModeItemEdit
              ? itemDetail.generalInfo.isNew
                ? t("product_platform.item_create")
                : t("product_platform.item_edit")
              : t("product_platform.item_details")
          }}
        </h1>
      </div>
      <div v-show="!itemDetail.generalInfo.isNew" class="flex gap-x-2">
        <BaseButton
          v-if="!isEditModeItemEdit"
          :color="ButtonColorType.Secondary"
          @click="handleClickEdit"
        >
          <EditIcon class="mr-[6px]" />
          {{ $t("product_platform.edit") }}
        </BaseButton>

        <BaseButton
          v-if="isEditModeItemEdit"
          :color="ButtonColorType.Gray"
          @click="handleClickCancel"
        >
          {{ t("product_platform.cancel") }}
        </BaseButton>

        <BaseButton
          v-if="isEditModeItemEdit"
          :color="ButtonColorType.Secondary"
          @click="handleClickSave"
        >
          <SaveIcon class="mr-[6px]" />
          {{ $t("product_platform.save") }}
        </BaseButton>
      </div>
    </div>
    <div>
      <BaseTabs
        v-model="selectedTab"
        :tabs="listTabs"
        :show-arrows="true"
        :center-active="true"
        :class-loco="'max-h-[calc(100vh-290px)]'"
        :is-unused-loco="
          selectedTab === TABS_OFFER_DETAILS.ADDITIONAL && isEditModeItemEdit
        "
      />
    </div>
    <ArrowLeftIcon
      class="absolute top-[174px] right-[0] cursor-pointer text-[#525457] hover:text-[#303132]"
      @click="handleClosePane"
    />
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

<script lang="ts" setup>
import { useI18n } from "vue-i18n";
import cloneDeep from "lodash-es/cloneDeep";
import { TABS_OFFER_DETAILS } from "@/constants/offer";
import { DEFAULT_VALUE_ITEM_DETAIL } from "@/constants/admin/attribute";
import BaseTabs from "@/components/prod/common/BaseTabs.vue";
import GeneralDetail from "./item-edit/GeneralDetail.vue";
import AdditionalDetail from "./item-edit/AdditionalDetail.vue";
import attributeManagementStore from "@/store/admin/attributeManagement.store";
import GeneralEdit from "./item-edit/GeneralEdit.vue";
import AdditionalEdit from "./item-edit/AdditionalEdit.vue";
import TreeViewEdit from "./item-edit/TreeViewEdit.vue";
import TreeViewDetail from "./item-edit/TreeViewDetail.vue";
import { ButtonColorType, DialogIconType } from "@/enums";
import BasePopup from "@/components/prod/common/BasePopup.vue";
import { isEqual } from "lodash-es";

const { t } = useI18n();

const {
  selectedTab,
  isEditModeItemEdit,
  itemDetail,
  selectedItem,
  isShowLabelSearch,
  isShowAttributeCodeSearch,
  originItemDetail,
} = storeToRefs(attributeManagementStore());
const {
  resetItemDetail,
  updateSelectedItem,
  cloneDetailItem,
  saveAllAttributes,
  handleResetSearchLabel,
  handleResetSearchCommCode,
  checkIsExistAddNewItem,
} = attributeManagementStore();

const isShowPopupCancel = ref(false);
const isShowPopupSave = ref(false);
const isShowPopupSaveConfirm = ref(false);
const nextFunction = ref<(id: string) => void>(() => {});
const nextFunctionParam = ref("");
const isAddNewMode = computed<boolean>(
  () => !itemDetail.value.treeViewInfo.nodeId
);

const isNotChangeValue = (): boolean => {
  if (itemDetail.value?.generalInfo?.isNew) return false;
  return isEqual(itemDetail.value, originItemDetail.value);
};

const listTabs = computed(() => {
  const tabs: any = [
    {
      value: TABS_OFFER_DETAILS.GENERAL,
      label: t("product_platform.general"),
      component: isEditModeItemEdit.value ? GeneralEdit : GeneralDetail,
      props: {
        isDynamicScroll: true,
        dynamicScrollKey: "GENERAL_ATTRIBUTE_TAB",
      },
    },
    {
      value: TABS_OFFER_DETAILS.ADDITIONAL,
      label: t("product_platform.additional"),
      component: isEditModeItemEdit.value ? AdditionalEdit : AdditionalDetail,
      props: {
        isDynamicScroll: true,
        dynamicScrollKey: "ADDITIONAL_ATTRIBUTE_TAB",
      },
    },
  ];
  if (itemDetail.value.generalInfo.largeItemCode === "O") {
    tabs.push({
      value: TABS_OFFER_DETAILS.TREEVIEW,
      label: t("product_platform.treeview"),
      component:
        isEditModeItemEdit.value && isAddNewMode.value
          ? TreeViewEdit
          : TreeViewDetail,
    });
  }
  return tabs;
});

const handleClosePane = () => {
  isEditModeItemEdit.value = false;
  isShowLabelSearch.value = false;
  isShowAttributeCodeSearch.value = false;
  selectedItem.value = null;
  itemDetail.value = cloneDeep(DEFAULT_VALUE_ITEM_DETAIL);
  resetItemDetail();
  updateSelectedItem();
};

const handleClickEdit = () => {
  isEditModeItemEdit.value = true;
  cloneDetailItem({});
  handleResetSearchLabel();
  handleResetSearchCommCode();
};

const handleClickCancel = () => {
  isShowPopupCancel.value = true;
};

// Popup when click cancel button
const handleClosePopupCancel = () => {
  isShowPopupCancel.value = false;
};

const handleSubmitPopupCancel = () => {
  isEditModeItemEdit.value = false;
  isShowPopupCancel.value = false;
  isShowLabelSearch.value = false;
  isShowAttributeCodeSearch.value = false;
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
    isEditModeItemEdit.value = false;
    isShowLabelSearch.value = false;
    isShowAttributeCodeSearch.value = false;
    handleResetSearchLabel();
    handleResetSearchCommCode();
  }
};

const handleClickSave = () => {
  if (isNotChangeValue()) {
    isShowPopupSaveConfirm.value = false;
    isEditModeItemEdit.value = false;
    isShowLabelSearch.value = false;
    isShowAttributeCodeSearch.value = false;
    handleResetSearchLabel();
    handleResetSearchCommCode();
    return;
  }
  isShowPopupSaveConfirm.value = true;
};
</script>

<style lang="scss" scoped>
.edit-mode {
  border: 1px solid #d9325a;
  box-shadow: 0px 0px 0px 4px #d9325a29;
}
:deep(.scroll-container) {
  padding: 0;
}
</style>
