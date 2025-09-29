<template>
  <div
    class="container relative bg-white pt-6 rounded-[12px]"
    :class="
      isEditFactorTypeDetail && '!border !border-primary shadow-edit-mode'
    "
  >
    <div class="flex flex-col h-full">
      <div class="flex justify-between items-center mb-2 h-[40px] px-6">
        <h1 class="font-medium text-[15px] text-text-base tracking-[0.5px]">
          {{
            isEditFactorTypeDetail
              ? $t(`product_platform.factorTypeEdit`)
              : $t(`product_platform.factorTypeDetails`)
          }}
        </h1>
        <BaseButton
          v-if="!isEditFactorTypeDetail"
          :color="ButtonColorType.Secondary"
          @click="handleEdit"
        >
          <edit-icon class="mr-[6px]" />
          {{ $t("product_platform.edit") }}
        </BaseButton>
      </div>
      <div class="w-full flex-grow flex flex-col mt-1 px-1">
        <BaseTabs
          v-model="currentTab"
          :tabs="getFactorTabs"
          :show-arrows="true"
          :center-active="true"
        />
      </div>

      <div
        v-if="
          isEditFactorTypeDetail && !isAddNewFactorChild && !isEditFactorDetail
        "
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
    </div>
  </div>
  <base-popup
    v-model="openPopupCancel"
    :icon="DialogIconType.Warning"
    :submit-button-text="$t('product_platform.btn_yes')"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="$t('product_platform.desc_cancel')"
    @on-close="
      () => {
        openPopupCancel = false;
      }
    "
    @on-submit="handleCancel"
  />
  <base-popup
    v-model="openPopupSave"
    :icon="DialogIconType.Info"
    :submit-button-text="$t('product_platform.btn_yes')"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="$t('product_platform.desc_update')"
    @on-close="
      () => {
        openPopupSave = false;
      }
    "
    @on-submit="handleSubmit"
  />
</template>
<script setup lang="ts">
import { ButtonColorType, DialogIconType } from "@/enums";
import FactorTab from "./factor-types/FactorTab.vue";
import FactorGeneralTab from "./factor-types/FactorGeneralTab.vue";
import { useI18n } from "vue-i18n";
import useFactorStore from "@/store/admin/factor.store";
import { useSnackbarStore } from "@/store";
import cloneDeep from "lodash-es/cloneDeep";
import { TABS_NAME_COLLECTION } from "@/constants/index";

const useSnackbar = useSnackbarStore();
const { t } = useI18n();
const openPopupSave = ref(false);
const openPopupCancel = ref(false);
const {
  currentTab,
  paramFilterDetail,
  factorTypeSelected,
  factorTypeDetail,
  factorTypeDetailBeforeEdit,
  isEditFactorTypeDetail,
  factorDetail,
  factorSelected,
  isEditFactorDetail,
  isAddNewFactorChild,
} = storeToRefs(useFactorStore());

const { getDetailFactorType, putDetailFactorType, getListFactorsType } =
  useFactorStore();

const getFactorTabs = computed(() => {
  let initTabs: any[] = [
    {
      value: TABS_NAME_COLLECTION.GENERAL,
      label: t("product_platform.general"),
      component: FactorGeneralTab,
      props: {
        modelValue: factorTypeDetail.value,
      },
    },
    {
      value: TABS_NAME_COLLECTION.FACTOR,
      label: t("product_platform.factor"),
      component: FactorTab,
      props: {
        modelValue: factorTypeDetail.value,
      },
    },
  ];

  return initTabs;
});

const isUpdateGeneral = computed(() => {
  return (
    factorTypeDetail.value?.factorTypeName !==
      factorTypeDetailBeforeEdit.value?.factorTypeName ||
    factorTypeDetail.value?.useYn !== factorTypeDetailBeforeEdit.value?.useYn
  );
});

const handleEdit = async () => {
  isEditFactorTypeDetail.value = true;
};

const onCancel = () => {
  const itemUpdate = factorTypeDetail.value?.factorLst?.filter(
    (item) => item?.isAdded || item?.isEdit
  );
  if (itemUpdate?.length || isUpdateGeneral.value) {
    openPopupCancel.value = true;
  } else {
    isEditFactorTypeDetail.value = false;
  }
};

const onSubmit = () => {
  const itemUpdate = factorTypeDetail.value?.factorLst?.filter(
    (item) => item?.isAdded || item?.isEdit
  );
  if (itemUpdate?.length || isUpdateGeneral.value) {
    openPopupSave.value = true;
  } else {
    isEditFactorTypeDetail.value = false;
  }
};

const handleCancel = async () => {
  factorDetail.value = null;
  factorSelected.value = null;
  await getDetailFactorType();
  openPopupCancel.value = false;
  isEditFactorTypeDetail.value = false;
  isEditFactorDetail.value = false;
  currentTab.value = TABS_NAME_COLLECTION.GENERAL;
};

const handleSubmit = async () => {
  try {
    const param = {
      ...factorTypeDetail.value,
      factorLst: factorTypeDetail.value?.factorLst?.filter(
        (item) => item?.isAdded || item?.isEdit
      ),
      factorSearchLst: null,
    };
    const res = await putDetailFactorType(param);
    if (res.status == 200) {
      useSnackbar.showSnackbar(
        t("product_platform.successfully_saved"),
        "success"
      );
      await getDetailFactorType();
      isEditFactorTypeDetail.value = false;
      factorDetail.value = null;
      await getListFactorsType();
      factorSelected.value = null;
      currentTab.value = TABS_NAME_COLLECTION.GENERAL;
    }
  } catch (error: any) {
    useSnackbar.showSnackbar(error.errorMsg, "error");
  } finally {
    openPopupSave.value = false;
  }
};

watch(
  () => factorTypeSelected.value.factorLst,
  (newVal) => {
    if (newVal) {
      currentTab.value = TABS_NAME_COLLECTION.GENERAL;
      isEditFactorTypeDetail.value = false;
      paramFilterDetail.value.factorName = "";
    }
  },
  { deep: true }
);
watch(
  () => [factorTypeDetail.value?.factorLst, isEditFactorTypeDetail.value],
  (newVal: any) => {
    if (newVal[0]?.length) {
      factorTypeDetail.value.pagination = {
        ...newVal[0].pagination,
        currentPage: factorTypeDetail.value.pagination?.currentPage ?? 1,
        totalSearchItems: newVal[0]?.length,
        pageSize: isEditFactorTypeDetail.value ? 7 : 8,
        totalItems: newVal[0]?.length,
        totalPages: Math.ceil(
          newVal[0]?.length / (isEditFactorTypeDetail.value ? 7 : 8)
        ),
      };
    }
  },
  { deep: true }
);
watch(
  () => isEditFactorTypeDetail.value,
  (val) => {
    if (val) {
      factorTypeDetailBeforeEdit.value = cloneDeep(factorTypeDetail.value);
    }
  }
);
provide("handleSaveGeneralTab", onSubmit);
</script>

<style scoped>
.add-new-button {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow:
    2px 1px 6px 0px #1e265b3d,
    2px 1px 18px 0px #1518421c inset,
    -5px -2px 6px 0px #ffffffa3 inset;
}
.custom-position-add-btn {
  position: fixed;
  bottom: 80px;
  z-index: 999;
  display: flex;
  justify-content: center;
  width: 420px;
}
</style>
