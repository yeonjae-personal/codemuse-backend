<template>
  <div
    class="container relative bg-white py-6 rounded-[12px] w-full"
    :class="isEditTableType && '!border !border-primary shadow-edit-mode'"
  >
    <div class="flex flex justify-between items-center px-6 h-[40px] mb-3">
      <span class="text-[#3A3B3D] text-[15px] font-[500]">{{
        $t(
          !isEditTableType
            ? `product_platform.tableTypeDetails`
            : `product_platform.tableTypeEdit`
        )
      }}</span>
      <div>
        <BaseButton
          v-if="!isEditTableType && currentTab === TABS_NAME_COLLECTION.GENERAL"
          :color="ButtonColorType.Secondary"
          @click="handleEdit"
        >
          <edit-icon class="mr-[6px]" />
          {{ $t("product_platform.edit") }}
        </BaseButton>
      </div>
    </div>
    <v-form
      ref="generalForm"
      class="w-full flex-grow flex flex-col mt-1 px-1"
      @submit.prevent
    >
      <BaseTabs
        v-model="currentTab"
        :tabs="tableTypeDetailsTab"
        :show-arrows="true"
        :center-active="true"
      />
    </v-form>
    <div
      v-if="isEditTableType && currentTab === TABS_NAME_COLLECTION.GENERAL"
      class="absolute flex justify-end items-center shrink-0 my-3 gap-2 bottom-0 right-4"
    >
      <BaseButton :color="ButtonColorType.Gray" @click="onCancel">
        {{ $t("product_platform.cancel") }}
      </BaseButton>
      <BaseButton :color="ButtonColorType.Secondary" @click="onSubmit">
        <SaveIcon class="mr-[6px]" />
        {{ $t("product_platform.save") }}
      </BaseButton>
    </div>
    <ArrowLeftIcon
      v-if="!isEditTableType"
      class="absolute top-[174px] right-0 cursor-pointer text-[#525457] hover:text-[#303132]"
      @click="onCloseTableTypeDetail"
    />
    <ArrowLeftIcon
      v-if="!isShowTableTypeSearch"
      class="absolute top-[174px] left-0 cursor-pointer text-[#525457] hover:text-[#303132] rotate-180"
      @click="onOpenTableTypeSearch"
    />
  </div>
  <base-popup
    v-model="openPopupChangeTab"
    :icon="DialogIconType.Warning"
    :submit-button-text="$t('product_platform.btn_yes')"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="$t('product_platform.groupCancelEdit')"
    @on-close="handleCancelChangeTab"
    @on-submit="handleConfirmChangeTab"
  />
  <base-popup
    v-model="openPopup"
    :icon="isCancel ? DialogIconType.Warning : DialogIconType.Info"
    :submit-button-text="$t('product_platform.btn_yes')"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="
      $t(
        isCancel
          ? 'product_platform.desc_cancel'
          : 'product_platform.desc_update'
      )
    "
    @on-close="
      () => {
        openPopup = false;
        isCancel = false;
      }
    "
    @on-submit="handleSave"
  />
</template>
<script lang="ts" setup>
import { TABS_NAME_COLLECTION } from "@/constants/index";
import { ButtonColorType, DialogIconType } from "@/enums";
import useTableStructureStore from "@/store/admin/tableStructure.store";
import TableTab from "@/components/admin/table-structure/tab/TableTab.vue";
import TableGeneralTab from "@/components/admin/table-structure/tab/TableGeneralTab.vue";
import { useSnackbarStore } from "@/store";
import { useI18n } from "vue-i18n";
import { VForm } from "vuetify/lib/components/index.mjs";
const {
  currentTab,
  tableTypeDetails,
  isEditTableType,
  tableTypeGeneralInit,
  isShowTableTypeDetail,
  tableTypeSearchTotal,
  tableTypeSearchList,
  tableTypeSearchParams,
  isGetAllTableTypeSearch,
  isShowTableTypeSearch,
} = storeToRefs(useTableStructureStore());
const { putTableTypeDetail, getListTableTypeDetail, getListTableType } =
  useTableStructureStore();
const { t } = useI18n();
const useSnackbar = useSnackbarStore();
const openPopupChangeTab = ref(false);
const openPopup = ref(false);
const isCancel = ref(false);
const generalForm = ref<typeof VForm>();

const tableTypeDetailsTab = computed(() => {
  let initTabs: any[] = [
    {
      value: TABS_NAME_COLLECTION.GENERAL,
      label: t("product_platform.general"),
      component: TableGeneralTab,
      props: {
        modelValue: tableTypeDetails.value,
      },
    },
    {
      value: TABS_NAME_COLLECTION.TABLE,
      label: t("product_platform.table"),
      component: TableTab,
      onClick: () => {
        if (isEditTableType.value) {
          openPopupChangeTab.value = true;
          return;
        }
      },
    },
  ];

  return initTabs;
});

const handleEdit = () => {
  isEditTableType.value = true;
};

const isKeyValuesEqual = (obj1, obj2) => {
  const commonKeys = Object.keys(obj1).filter((key) => key in obj2);
  return commonKeys.every((key) => obj1[key as string] === obj2[key as string]);
};

const onSubmit = async () => {
  const valid = await generalForm.value?.validate();
  if (!valid.valid) {
    useSnackbar.showSnackbar(
      t("product_platform.required_field_missing"),
      "error"
    );
    return;
  }

  if (isKeyValuesEqual(tableTypeGeneralInit.value, tableTypeDetails.value)) {
    isEditTableType.value = false;
  } else openPopup.value = true;
};

const onCancel = () => {
  if (isKeyValuesEqual(tableTypeGeneralInit.value, tableTypeDetails.value)) {
    isEditTableType.value = false;
  } else {
    isCancel.value = true;
    openPopup.value = true;
  }
};

const handleSave = async () => {
  if (isCancel.value) {
    await getListTableTypeDetail();
    isEditTableType.value = false;
  } else {
    try {
      const res = await putTableTypeDetail();
      if (res.status == 200) {
        useSnackbar.showSnackbar(
          t("product_platform.successfully_saved"),
          "success"
        );
        await getListTableTypeDetail();
        // resetData();
        // await getListTableType();
      }
    } catch (error: any) {
      useSnackbar.showSnackbar(error.errorMsg, "error");
    } finally {
      isEditTableType.value = false;
    }
  }
  openPopup.value = false;
  isCancel.value = false;
};

const handleCancelChangeTab = () => {
  currentTab.value = TABS_NAME_COLLECTION.GENERAL;
  openPopupChangeTab.value = false;
};

const handleConfirmChangeTab = () => {
  isEditTableType.value = false;
  openPopupChangeTab.value = false;
};
const onCloseTableTypeDetail = () => {
  isShowTableTypeDetail.value = false;
};
const onOpenTableTypeSearch = () => {
  isShowTableTypeSearch.value = true;
};
const resetData = () => {
  tableTypeSearchTotal.value = 0;
  tableTypeSearchList.value = [];
  tableTypeSearchParams.value.page = 1;
  isGetAllTableTypeSearch.value = false;
};

watch(
  () => tableTypeDetails.value,
  (newVal) => {
    tableTypeSearchList.value.forEach((item) => {
      if (item?.tableTypeCode === newVal?.tableTypeCode) {
        item.tableTypeName = newVal.tableTypeName;
        item.useYn = newVal.useYn;
      }
    });
  },
  { deep: true }
);

provide("handleSaveGeneralTab", onSubmit);
</script>
