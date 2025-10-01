<template>
  <div class="relative z-10 top-0 right-0 bg-white col-span-1">
    <div class="w-full overflow-x-auto h-full relative text-[12px] pl-4 pr-2">
      <div class="w-full h-full flex relative">
        <v-form
          ref="formRef"
          v-model="form"
          class="w-full"
          @submit.prevent="onSubmit"
        >
          <div class="content w-full justify-start h-full flex flex-col">
            <div
              class="w-full flex justify-between items-center px-2 pt-3 pb-2"
            >
              <div
                class="text-text-base text-base-vnb font-medium leading-[40px]"
              >
                {{ title }}
              </div>
              <BaseButton
                v-if="
                  !isCreateOffer &&
                  !isAdd &&
                  currentTab != OFFER_TABS_VALUE.HISTORY &&
                  !isEditProduct &&
                  !isDuplicate
                "
                :color="ButtonColorType.Secondary"
                @click="handleEditOfferDetails"
              >
                <edit-icon class="mr-[6px]" />
                {{ $t("product_platform.edit") }}
              </BaseButton>
            </div>

            <div class="w-full flex-1 overflow-hidden">
              <BaseTabs
                ref="baseTabs"
                v-model="currentTab"
                :class-loco="
                  isEditProduct || isAdd
                    ? 'max-h-[calc(100vh-335px)]'
                    : 'max-h-[calc(100vh-290px)]'
                "
                :show-arrows="!isEditProduct"
                :tabs="offerTabs"
                :center-active="true"
                class="pr-2"
              />
            </div>

            <div
              v-if="isEditProduct && currentTab != OFFER_TABS_VALUE.HISTORY"
              class="flex justify-end pt-3 px-2 gap-2"
            >
              <BaseButton :color="ButtonColorType.Gray" @click="handleCancel">
                {{ t("product_platform.cancel") }}
              </BaseButton>
              <BaseButton :color="ButtonColorType.Secondary" @click="onSubmit">
                <SaveIcon class="mr-[6px]" />
                {{ $t("product_platform.save") }}
              </BaseButton>
            </div>
          </div>
        </v-form>
      </div>
    </div>
    <ShowDetailIcon
      v-if="!isDuplicate"
      class="absolute cursor-pointer text-[#525457] hover:text-[#303132]"
      :class="isAdd ? 'top-[174px] right-0 rotate-180' : 'top-[174px] left-0'"
      @click="closeStructureDetailPane"
    />
  </div>
  <base-popup
    v-model="openPopup"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="
      isCancel
        ? $t('product_platform.desc_cancel')
        : $t('product_platform.desc_update')
    "
    :icon="isCancel ? DialogIconType.Warning : DialogIconType.Info"
    :submit-button-text="$t('product_platform.btn_yes')"
    @on-close="closePopupSave"
    @on-submit="handleUpdateOfferDetails"
  />
</template>
<script lang="ts" setup>
import { VForm } from "vuetify/lib/components/index.mjs";
import ShowDetailIcon from "@/components/prod/icons/ShowDetailIcon.vue";
import AdditionalTab from "@/components/prod/shared/AdditionalTab.vue";
import GeneralTab from "@/components/prod/shared/GeneralTab.vue";
import HistoryTab from "@/components/prod/shared/HistoryTab.vue";
import { CUSTOM_VALIDATION_PAGE, OFFER_TYPE } from "@/constants/";
import { DETAIL_COMPONENT_NAME } from "@/constants/index";
import { OFFER_TABS_VALUE, STRUCTURE_ITEM_SCREEN } from "@/constants/offer";
import { ButtonColorType, DialogIconType, RequiredYn } from "@/enums";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import {
  useCreateStructureStore,
  useDuplicateStructureStore,
  useHistoryTabStore,
  useProductsCreateStore,
  useProductsDuplicateStore,
  useProductsStore,
  useSnackbarStore,
  useStructureStore,
} from "@/store";
import customValidationStore from "@/store/admin/customValidation.store";
import { useCustomValidation } from "@/utils/custom-validation";
import { useI18n } from "vue-i18n";
import { MenuItemID } from "@/enums/redirect";

const customValidationdata = ref(false);
const { t } = useI18n();
const props = defineProps({
  screen: {
    type: String,
    default: STRUCTURE_ITEM_SCREEN.OFFER,
  },
  type: {
    type: String,
    default: OFFER_TYPE.PRICEPLAN,
  },
  isAdd: {
    type: Boolean,
    default: false,
  },
  isDuplicate: {
    type: Boolean,
    default: false,
  },
  isCreateOffer: {
    type: Boolean,
    default: false,
  },
});
const emit = defineEmits([
  "closeStructureDetailPane",
  "updateSuccess",
  "cancelUpdate",
]);

const removeTab = inject<any>("removeTab");

const snackbarStore = useSnackbarStore();
const structureStore = useStructureStore();
const productStore = useProductsStore();
const productCreateStore = useProductsCreateStore();
const createStructureStore = useCreateStructureStore();
const productsDuplicateStore = useProductsDuplicateStore();
const duplicateStructureStore = useDuplicateStructureStore();
const historyStore = useHistoryTabStore();
const { listRulesOriginal } = storeToRefs(customValidationStore());
const { setEditMode, getListCustomValidation } = customValidationStore();
const {
  applyRulesInit,
  applyRules,
  getRuleByAdditionalFields,
  updateAdditionalField,
} = useCustomValidation();
const triggerMountedTab = ref<boolean>(false);
const formRef = ref<typeof VForm>();
const baseTabs = ref<HTMLElement | any>(null);

const selectedStore = computed(() => {
  if (props.isCreateOffer) {
    return createStructureStore;
  } else if (props.isDuplicate) {
    return duplicateStructureStore;
  }
  return structureStore;
});

const productStoreSelected = computed(() => {
  if (props.isCreateOffer) {
    return productCreateStore;
  } else if (props.isDuplicate) {
    return productsDuplicateStore;
  }
  return productStore;
});

const { itemsType } = storeToRefs(productStoreSelected.value);
const {
  selectedStructureData,
  isEditProduct,
  offerCode,
  offerUuid,
  isCreated,
} = storeToRefs(selectedStore.value);

const form = ref(false);
const openPopup = ref(false);
const isCancel = ref(false);
const currentTab = ref(OFFER_TABS_VALUE.GENERAL);
const title = computed(() => {
  if (props.type === OFFER_TYPE.DISCOUNT) {
    return isEditProduct.value && props.isAdd
      ? t("product_platform.discountCreate")
      : isEditProduct.value && props.isDuplicate
        ? t("product_platform.offerDuplicate")
        : isEditProduct.value
          ? t("product_platform.discountEdit")
          : t("product_platform.discountDetails");
  } else {
    return isEditProduct.value && props.isAdd
      ? t("product_platform.offerCreate")
      : isEditProduct.value && props.isDuplicate
        ? t("product_platform.offerDuplicate")
        : isEditProduct.value
          ? t("product_platform.offerEdit")
          : t("product_platform.offerDetails");
  }
});

const offerTabs = computed(() => {
  const listTabs = [
    {
      value: OFFER_TABS_VALUE.GENERAL,
      label: t("product_platform.general"),
      component: GeneralTab,
      props: {
        modelValue: selectedStructureData.value?.general,
        pageName: DETAIL_COMPONENT_NAME.OFFER_SEARCH,
        isEdit: isEditProduct.value,
        isAdd: props.isAdd && !isCreated.value,
        createItemCodeList: itemsType.value,
        page: CUSTOM_VALIDATION_PAGE.OFFER,
        type: props.type,
        isValidCustom: customValidationdata.value,
        triggerMountedTab: triggerMountedTab.value,
      },
    },
    {
      value: OFFER_TABS_VALUE.ADDITIONAL,
      label: t("product_platform.additional"),
      component: AdditionalTab,
      props: {
        modelValue: selectedStructureData.value?.additional,
        pageName: DETAIL_COMPONENT_NAME.OFFER_SEARCH,
        isEdit: isEditProduct.value,
        isAdd: props.isAdd && !isCreated.value,
        page: CUSTOM_VALIDATION_PAGE.OFFER,
        type: props.type,
        isValidCustom: customValidationdata.value,
        triggerMountedTab: triggerMountedTab.value,
      },
    },
  ];
  if (!isEditProduct.value && !props.isAdd && !props.isDuplicate) {
    return [
      ...listTabs,
      {
        value: OFFER_TABS_VALUE.HISTORY,
        label: t("product_platform.history"),
        component: HistoryTab,
      },
    ];
  }
  return listTabs;
});

const closePopupSave = () => {
  openPopup.value = false;
};

const handleUpdateOfferDetails = async () => {
  if (isCancel.value) {
    emit("cancelUpdate");
    isEditProduct.value = false;
    closePopupSave();
    removeTab(MenuItemID.OfferCreate);
  } else {
    try {
      const generalPayload = {
        general: selectedStructureData.value.general
          .filter((item) => !item.dispTab)
          .map((item: any) => {
            let val = item.attrVal;
            if (item.fieldTypeCode === COLUMN_FIELD_TYPE.DM) {
              val = JSON.stringify(item.attrVal);
            }
            return {
              ...item,
              attrVal: val,
            };
          }),
        additional: [
          ...selectedStructureData.value.general.filter((item) => item.dispTab),
          ...selectedStructureData.value.additional,
        ].map((item: any) => {
          let val = item.attrVal;
          if (item.fieldTypeCode === COLUMN_FIELD_TYPE.DM) {
            val = JSON.stringify(item.attrVal);
          }
          return {
            ...item,
            attrVal: val,
          };
        }),
      } as any;
      if (props.isAdd) {
        const data = {
          general: generalPayload.general,
          additional: generalPayload.additional,
        };
        const res = await productStoreSelected.value.createProduct(data);
        offerCode.value = res.data?.code || null;
        offerUuid.value = res.data?.objUuid || null;
        emit("updateSuccess", { objUuid: offerUuid.value });
        isCreated.value = true;
        snackbarStore.showSnackbar(
          t("product_platform.create_offer_successfully"),
          "success"
        );
      } else {
        const res = await productStoreSelected.value.updateProduct(
          generalPayload,
          props.isDuplicate
        );
        if (props.isDuplicate) {
          offerCode.value = res.data?.code || null;
          offerUuid.value = res.data?.objUuid || null;
        }

        emit("updateSuccess", res?.data);
        snackbarStore.showSnackbar(
          t("product_platform.successfully_saved"),
          "success"
        );
        closePopupSave();
      }
    } catch (error: any) {
      snackbarStore.showSnackbar(
        error?.errorMsg || t("product_platform.something_went_wrong"),
        "error"
      );
    }
  }
  currentTab.value = OFFER_TABS_VALUE.GENERAL;
  isEditProduct.value = false;
  closePopupSave();
};
const isInvalid = computed(() => {
  const listSelected = selectedStructureData.value?.general;
  const listSelectedAdditional = selectedStructureData.value?.additional;

  return (
    !!listSelected?.find(
      (item: any) =>
        item?.requiredYn === RequiredYn.Yes &&
        item?.colName !== "obj_code" &&
        !item?.attrVal?.length
    ) ||
    !!listSelectedAdditional.find(
      (item: any) => item.requiredYn === RequiredYn.Yes && !item.attrVal?.length
    )
  );
});

const onSubmit = async () => {
  await formRef.value?.validate();
  if (baseTabs.value?.componentRefs?.length) {
    baseTabs.value?.componentRefs.forEach((el) => el?.validationAllSelect?.());
  }
  nextTick(() => {
    if (!form.value || isInvalid.value) {
      snackbarStore.showSnackbar(
        t("product_platform.required_field_missing"),
        "error"
      );
      return;
    }
    isCancel.value = false;
    openPopup.value = true;
  });
};

const closeStructureDetailPane = () => {
  emit("closeStructureDetailPane");
};
const handleEditOfferDetails = async () => {
  triggerMountedTab.value = !triggerMountedTab.value;
  setEditMode(true);
  isEditProduct.value = !isEditProduct.value;
  // Apply validation
  applyValidationRules();
};
const handleCancel = () => {
  isCancel.value = true;
  openPopup.value = true;
};

const resetHistoryStore = () => {
  historyStore.resetHistory();
};

watch(
  () => props.isDuplicate,
  (newVal) => {
    if (newVal) {
      currentTab.value = OFFER_TABS_VALUE.GENERAL;
    }
  }
);

const dataTypeString = computed(() => {
  return JSON.stringify(selectedStructureData.value);
});

watch(dataTypeString, (newValueStr, oldValueStr) => {
  let newValue = JSON.parse(newValueStr);
  let oldValue = JSON.parse(oldValueStr);
  nextTick(() => {
    const { generalList, additionalList } = applyRules(
      oldValue,
      newValue,
      listRulesOriginal.value,
      selectedStructureData.value
    );
    if (selectedStructureData.value) {
      selectedStructureData.value.general = generalList;
      selectedStructureData.value.additional = additionalList;
    }
  });
});

const applyValidationRules = async () => {
  // Apply red, blue dots
  await getListCustomValidation({
    item: "O",
    type: props.type,
  });

  const listRules = getRuleByAdditionalFields(
    [
      ...selectedStructureData.value?.additional,
      ...selectedStructureData.value?.general,
    ],
    listRulesOriginal.value
  );

  const newField = updateAdditionalField(
    selectedStructureData.value,
    listRules,
    {
      showRule: true,
      pageType: "O",
      type: props.type,
    }
  );
  selectedStructureData.value.general = newField.generalList;
  selectedStructureData.value.additional = newField.additionalList;

  // Apply init rules
  const listItems = [
    ...selectedStructureData.value?.additional,
    ...selectedStructureData.value?.general.filter(
      (item) => item.dispTab === "G"
    ),
  ];
  nextTick(() => {
    const { generalList, additionalList } = applyRulesInit(
      listItems,
      listRulesOriginal.value,
      selectedStructureData.value
    );
    selectedStructureData.value.general = generalList;
    selectedStructureData.value.additional = additionalList;
  });
};

onMounted(async () => {
  if (props.isDuplicate) {
    applyValidationRules();
  }
});
onUnmounted(() => {
  resetHistoryStore();
});
</script>
<style scoped>
.active {
  background-color: #f14f4f;
  transition: all ease-in 0.7s;
}
p {
  margin-top: 12px;
  margin-bottom: 12px;
}
.selected-attribute {
  background-color: #faefef;
  border: 1px solid #e96565 !important;
  transition: all ease-in 0.8s;
}
.v-tab.v-tab.v-btn {
  color: #bdc1c7;
  font-size: 13px;
}
:deep().v-tabs-window {
  height: 100%;
}
:deep().v-window__container {
  height: 100% !important;
  display: initial;
}
.offer-edit-datetime-picker {
  width: 120px;
}
.offer-edit-datetime-picker :deep().dp__pointer {
  height: 32px;
}

:deep() .v-slide-group {
  overflow: inherit !important;
}
</style>
