<template>
  <v-form
    ref="formRef"
    v-model="form"
    class="h-full"
    @submit.prevent="onSubmit"
  >
    <div class="h-full flex flex-col bg-white rounded-[12px] px-4 pt-6 pb-3">
      <div>
        <div class="w-full flex justify-between items-center px-2 h-10 mb-2">
          <div class="text-text-base text-base font-medium tracking-[0.5px]">
            {{ $t("product_platform.resource.resource_create") }}
          </div>
        </div>
        <div class="resource-item mb-2">
          <div
            class="resource-item h-16 w-full pl-3 pr-4 flex justify-between items-center border border-lighter rounded-[32px] shadow-resource-item"
          >
            <div class="flex gap-2 items-center">
              <div
                class="shrink-0 w-10 h-10 rounded-full flex justify-center items-center bg-white bg-opacity-[0.64] shadow-resource-icon"
              >
                <RatingElementResourceIcon
                  v-if="typeElement === ResourceType.RatingElement"
                />
                <BillingElementIcon
                  v-if="typeElement === ResourceType.BillingElement"
                />
                <ServiceElementIcon
                  v-if="typeElement === ResourceType.ServiceElement"
                />
              </div>
              <div class="w-full">
                <div
                  class="w-full truncate text-[13px] leading-[19.5px] font-medium text-text-base !max-w-[235px] min-h-[19.5px]"
                >
                  <CustomTooltip :content="handleDisplayResourceName" />
                </div>
                <div class="w-full text-[11px] leading-[16.5px] text-lighter">
                  <span class="font-normal">{{
                    isViewOnly
                      ? resourceCodeCreate
                      : $t("product_platform.auto_generation")
                  }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <BaseTabs
        ref="baseTabs"
        v-model="currentTab"
        :tabs="tabsList"
        :show-arrows="true"
        :center-active="true"
        :class-loco="'max-h-[calc(100vh-405px)]'"
      />
      <div
        v-if="!props.isViewOnly"
        class="w-ful flex justify-end items-center gap-2"
      >
        <BaseButton :color="ButtonColorType.Gray" @click="handleClickCancel">
          {{ $t("product_platform.cancel") }}
        </BaseButton>
        <BaseButton :color="ButtonColorType.Secondary" @click="onSubmit">
          <save-icon class="mr-[6px]" />
          {{ $t("product_platform.save") }}
        </BaseButton>
      </div>
    </div>
  </v-form>
  <base-popup
    v-model="openPopup"
    :icon="isCancel ? DialogIconType.Warning : DialogIconType.Info"
    :submit-button-text="$t('product_platform.btn_yes')"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="
      isCancel
        ? $t('product_platform.desc_cancel')
        : $t('product_platform.resource.create_a_new_resource')
    "
    @on-close="closePopupSave"
    @on-submit="handleCreateResource"
  />
</template>
<script setup lang="ts">
import AdditionalTab from "@/components/prod/shared/AdditionalTab.vue";
import GeneralTab from "@/components/prod/shared/GeneralTab.vue";
import MultiEntityTab from "../../shared/MultiEntityTab.vue";
import { VForm } from "vuetify/lib/components/index.mjs";
import { CUSTOM_VALIDATION_PAGE } from "@/constants/";
import { OFFER_TABS_VALUE } from "@/constants/offer";
import { ButtonColorType, DialogIconType, RequiredYn } from "@/enums";
import { ResourceType } from "@/enums/component";
import { useResourceStore, useSnackbarStore } from "@/store";
import { useI18n } from "vue-i18n";
import { useCustomValidation } from "@/utils/custom-validation";
import { MenuItemID } from "@/enums/redirect";
import customValidationStore from "@/store/admin/customValidation.store";

const { t } = useI18n();

const props = defineProps({
  itemCode: {
    type: String,
    default: "",
  },
  title: {
    type: String,
    default: "Resource Create",
    required: true,
  },
  isCreate: {
    type: Boolean,
    default: true,
  },
  isViewOnly: {
    type: Boolean,
    default: false,
  },
  createItemCodeList: {
    type: Array,
    default: () => [],
  },
});
const {
  getRuleByAdditionalFields,
  applyRules,
  applyRulesInit,
  updateAdditionalField,
} = useCustomValidation();
const { getListCustomValidation } = customValidationStore();
const { listRulesOriginal } = storeToRefs(customValidationStore());
const snackbarStore = useSnackbarStore();
const resourceStore = useResourceStore();
const {
  resourceCodeCreate,
  formatResource,
  resourceCreateEntityList,
  showEntitySearchCreate,
  paramsSearchEntityCreate,
  multiEntityTypes,
} = storeToRefs(resourceStore);

const emits = defineEmits([
  "handleSubmitResource",
  "handleCancelCreate",
  "changeItemCode",
  "closeLoadingComponent",
]);

const removeTab = inject<any>("removeTab");

const currentTab = ref(OFFER_TABS_VALUE.GENERAL);
const tab = ref(OFFER_TABS_VALUE.GENERAL);
const openPopup = ref(false);
const isCancel = ref(false);
const form = ref(false);
const formRef = ref<typeof VForm>();
const baseTabs = ref<HTMLElement | any>(null);

const tabsList = computed(() => {
  let initTabs: any[] = [
    {
      value: OFFER_TABS_VALUE.GENERAL,
      label: t("product_platform.general"),
      component: GeneralTab,
      props: {
        modelValue: formatResource.value?.general,
        isEdit: !props.isViewOnly,
        createItemCodeList: props.createItemCodeList,
        page: CUSTOM_VALIDATION_PAGE.RESOURCE,
        type: typeElement.value,
        isAdd: props.isCreate,
      },
    },
    {
      value: OFFER_TABS_VALUE.ADDITIONAL,
      label: t("product_platform.additional"),
      component: AdditionalTab,
      props: {
        modelValue: formatResource.value?.additional,
        isEdit: !props.isViewOnly,
        page: CUSTOM_VALIDATION_PAGE.RESOURCE,
        type: typeElement.value,
        isAdd: props.isCreate,
      },
    },
  ];
  if (resourceCreateEntityList.value.length) {
    initTabs.push({
      value: OFFER_TABS_VALUE.MULTI_ENTITY,
      label: t("product_platform.multiEntity"),
      component: MultiEntityTab,
      props: {
        isEdit: !props.isViewOnly,
        multiEntityTypes: multiEntityTypes.value,
        modelList: resourceCreateEntityList.value,
        typeSelected: paramsSearchEntityCreate.value?.itemCode,
      },
      events: {
        onClickDrogArae: handleOpenSearch,
      },
    });
  }
  return initTabs;
});

const handleOpenSearch = () => {
  showEntitySearchCreate.value = true;
};

const isValidGeneral = computed(
  () =>
    !formatResource.value.general.filter(
      (item: any) =>
        item.colName !== "obj_code" &&
        item.requiredYn === RequiredYn.Yes &&
        item.attrVal?.length === 0
    ).length
);

const isValidAdditional = computed(
  () =>
    !formatResource.value.additional.filter(
      (item: any) =>
        item.requiredYn === RequiredYn.Yes && item.attrVal?.length === 0
    ).length
);

const isValid = computed(() => isValidGeneral.value && isValidAdditional.value);

watch(
  () => props.isViewOnly,
  (newVal) => {
    if (newVal) {
      tab.value = OFFER_TABS_VALUE.GENERAL;
    }
  }
);

const typeElement = computed(() => {
  const itemCode = formatResource?.value?.general?.find(
    (el: any) => el.colName === "item_code"
  );
  return itemCode ? itemCode?.attrVal : ResourceType.RatingElement;
});

const handleDisplayResourceName = computed(() => {
  const resourceName = formatResource?.value?.general?.find(
    (el: any) => el.colName === "obj_name"
  )?.attrVal;
  return resourceName ? resourceName : t(`product_platform.resourceName`);
});

const handleClickCancel: ((...args: any[]) => void) | undefined = () => {
  openPopup.value = true;
  isCancel.value = true;
};

const onSubmit = async () => {
  await formRef.value?.validate();
  if (baseTabs.value?.componentRefs?.length) {
    baseTabs.value?.componentRefs.forEach((el) => el?.validationAllSelect?.());
  }
  nextTick(() => {
    if (!form.value || !isValid.value) {
      snackbarStore.showSnackbar(
        t("product_platform.required_field_missing"),
        "error"
      );
      return;
    }
    openPopup.value = true;
    isCancel.value = false;
  });
};

const closePopupSave = () => {
  openPopup.value = false;
  isCancel.value = false;
};

const handleCreateResource = () => {
  openPopup.value = false;
  if (isCancel.value) {
    removeTab(MenuItemID.ResourceCreate);
  } else {
    emits("closeLoadingComponent");
    emits("handleSubmitResource", formatResource.value);

    resourceCreateEntityList.value = resourceCreateEntityList.value.map(
      (resor) => ({
        ...resor,
        objRel: resor.objRel?.map((enti) => ({
          ...enti,
          isAdd: false,
        })),
      })
    );
  }
  setTimeout(() => {
    isCancel.value = false;
  }, 1000);
};

watch(
  () => currentTab.value,
  (newVal: String) => {
    emits("closeLoadingComponent");
    if (newVal === OFFER_TABS_VALUE.MULTI_ENTITY && !props.isViewOnly) {
      showEntitySearchCreate.value = true;
    } else {
      showEntitySearchCreate.value = false;
    }
  }
);

watch(
  () => typeElement.value,
  async (newVal: any) => {
    await resourceStore.getFormatResourceCreate(newVal);
    paramsSearchEntityCreate.value.itemCode = newVal;
  }
);

watch(formatResource, (value) => {
  if (value) {
    updateFormatResource();
  }
});

const updateFormatResource = async () => {
  await getListCustomValidation({
    item: "R",
    type: typeElement.value,
  });
  if (formatResource.value) {
    const listRules = getRuleByAdditionalFields(
      [...formatResource.value?.additional, ...formatResource.value?.general],
      listRulesOriginal.value
    );

    const newField = updateAdditionalField(formatResource.value, listRules, {
      showRule: true,
      pageType: "R",
      type: typeElement.value,
    });
    formatResource.value.general = newField.generalList;
    formatResource.value.additional = newField.additionalList;

    // Apply init rules
    const listItems = [
      ...formatResource.value?.additional,
      ...formatResource.value?.general?.filter((item) => item.dispTab === "G"),
    ];

    nextTick(() => {
      const { generalList, additionalList } = applyRulesInit(
        listItems,
        listRulesOriginal.value,
        formatResource.value
      );
      formatResource.value.general = generalList;
      formatResource.value.additional = additionalList;
    });
  }
};

const dataTypeString = computed(() => {
  return JSON.stringify(formatResource.value);
});

watch(dataTypeString, (newValueStr, oldValueStr) => {
  let newValue = JSON.parse(newValueStr);
  let oldValue = JSON.parse(oldValueStr);
  nextTick(() => {
    const { generalList, additionalList } = applyRules(
      oldValue,
      newValue,
      listRulesOriginal.value,
      formatResource.value
    );
    if (formatResource.value) {
      formatResource.value.general = generalList;
      formatResource.value.additional = additionalList;
    }
  });
});
</script>
<style scoped>
.v-tab.v-tab.v-btn {
  color: #bdc1c7;
  font-size: 13px;
}

:deep().v-field {
  height: 32px;
  display: flex;
  align-items: center;
}

.offer-edit-datetime-picker :deep().dp__pointer {
  height: 32px;
}

:deep().v-window__container {
  height: 100%;
}
</style>
