<template>
  <div
    class="bg-white p-4 pt-[24px] rounded-lg relative h-full"
    :class="{ 'edit-mode': isEditRule }"
  >
    <div class="flex justify-between items-center pl-3 pr-3 pb-3 h-[52px]">
      <div class="flex align-center gap-2 items-end">
        <h1 class="font-medium text-base text-text-base tracking-[0.5px]">
          {{ t("product_platform.ruleDetails") }}
        </h1>
      </div>
      <div>
        <BaseButton
          v-if="!isEditRule"
          :color="ButtonColorType.Secondary"
          @click="handleEdit"
        >
          <EditIcon class="mr-[6px]" />
          {{ $t("product_platform.edit") }}
        </BaseButton>
      </div>
    </div>
    <div>
      <BaseTabs
        v-model="selectedTab"
        :tabs="listTabs"
        :show-arrows="true"
        :center-active="true"
        :class-tabs="'min-h-[calc(100vh-335px)]'"
      />
    </div>
    <div v-if="isEditRule" class="flex justify-end pt-3 gap-2">
      <BaseButton :color="ButtonColorType.Gray" @click="handleCancel">
        {{ t("product_platform.cancel") }}
      </BaseButton>
      <BaseButton :color="ButtonColorType.Secondary" @click="handleSave">
        <SaveIcon class="mr-[6px]" />
        {{ $t("product_platform.save") }}
      </BaseButton>
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
import Attributes from "./Attributes.vue";
import useRuleEngineStore from "@/store/admin/ruleEngine.store";
import History from "./History.vue";
import { ButtonColorType, DialogIconType } from "@/enums";
import { TABS_RULE_DETAIL } from "@/interfaces/admin/rule-engine";
import { useSnackbarStore } from "@/store";
import cloneDeep from "lodash-es/cloneDeep";
const { t } = useI18n();
const ruleEngineStore = useRuleEngineStore();
const {
  isEditRule,
  ruleDetail,
  selectedTab,
  searchName,
  searchBy,
  isShowRuleStructure,
  listRules,
  listRulesTemp,
  ruleDetailTemp,
  isShowRuleDetail,
} = storeToRefs(ruleEngineStore);
const {
  setEditRule,
  setSelectedRule,
  removeAddNewRule,
  getListRules,
  setSelectedCate,
  setSelectedSubCate,
  createUpdateRule,
  validateRuleDetail,
} = ruleEngineStore;

const isShowPopupCancel = ref(false);
const isShowPopupSaveConfirm = ref(false);
const { showSnackbar } = useSnackbarStore();

const listTabs = computed(() => {
  const tabs: any = [
    {
      value: TABS_RULE_DETAIL.ATTRIBUTES,
      label: t("product_platform.attributes"),
      component: Attributes,
      props: {
        isDynamicScroll: true,
        class: "px-0",
      },
    },
    {
      value: TABS_RULE_DETAIL.HISTORY,
      label: t("product_platform.history"),
      component: History,
      props: {
        isDynamicScroll: true,
        dynamicScrollKey: "HISTORY_TAB",
      },
    },
  ];
  return tabs;
});

const handleClosePane = () => {
  if (isEditRule.value) {
    isShowPopupCancel.value = true;
  } else {
    isShowRuleDetail.value = false;
  }
};
const handleCancel = () => {
  isShowPopupCancel.value = true;
};
const handleSave = () => {
  isShowPopupSaveConfirm.value = true;
};
const handleEdit = () => {
  setEditRule(true);
};
const handleClosePopupCancel = () => {
  isShowPopupCancel.value = false;
};
const handleSubmitPopupCancel = () => {
  isShowPopupCancel.value = false;
  setEditRule(false);
  if (ruleDetail.value.isAddNew) {
    setSelectedRule("");
    removeAddNewRule();
    isShowRuleStructure.value = false;
  } else {
    isShowRuleDetail.value = false;
    listRules.value = cloneDeep(listRulesTemp.value);
    ruleDetail.value = cloneDeep(ruleDetailTemp.value);
  }
};
const handleClosePopupSaveConfirm = () => {
  isShowPopupSaveConfirm.value = false;
};

const handleSubmitPopupSaveConfirm = async () => {
  isShowPopupSaveConfirm.value = false;
  const message = validateRuleDetail();
  if (message) {
    showSnackbar(t(message), "error");
    return;
  }
  const response = await createUpdateRule();
  if (response?.status === 200) {
    showSnackbar("Save successfully", "success");
    setEditRule(false);
    setSelectedCate("");
    setSelectedSubCate("");
    getListRules(searchName.value, searchBy.value);
  } else {
    showSnackbar("Cannot save", "error");
  }
};
</script>

<style lang="scss" scoped>
.edit-mode {
  border: 1px solid #d9325a;
  box-shadow: 0px 0px 0px 4px #d9325a29;
}
</style>
