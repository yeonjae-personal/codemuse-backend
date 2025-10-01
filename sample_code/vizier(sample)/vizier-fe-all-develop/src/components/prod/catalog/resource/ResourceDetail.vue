<template>
  <div
    class="relative z-10 top-0 right-0 bg-white flex-shrink-0 rounded-lg"
    :class="isEdit && '!border !border-primary shadow-edit-mode'"
  >
    <v-form
      ref="formRef"
      v-model="form"
      class="w-full flex-grow-1"
      @submit.prevent="onSubmit"
    >
      <div
        class="w-full relative text-[12px] h-full flex flex-col px-4 pb-3 pt-6"
      >
        <div class="w-full flex justify-between items-center px-2 pb-3">
          <div
            class="text-text-base text-base-vnb font-medium leading-10 tracking-[0.5px] h-[40px]"
          >
            {{
              !isEdit
                ? $t("product_platform.resource_details")
                : isDuplicate
                  ? $t("product_platform.resource_duplicate")
                  : $t("product_platform.resource_edit")
            }}
          </div>
          <BaseButton
            v-if="!isEdit && currentTab !== OFFER_TABS_VALUE.HISTORY"
            :color="ButtonColorType.Secondary"
            @click="handleEdit"
          >
            <edit-icon class="mr-[6px]" />
            {{ $t("product_platform.edit") }}
          </BaseButton>
        </div>

        <div class="flex flex-col content w-full justify-center h-full">
          <div class="w-full flex-grow flex flex-col">
            <BaseTabs
              ref="baseTabs"
              v-model="currentTab"
              :tabs="tabsList"
              :show-arrows="!isEdit"
              :center-active="true"
              :class-loco="
                isEdit
                  ? 'max-h-[calc(100vh-342px)]'
                  : 'max-h-[calc(100vh-294px)]'
              "
            />
          </div>
        </div>
      </div>
      <div
        v-if="isEdit"
        class="absolute flex justify-end items-center shrink-0 my-3 gap-2 bottom-0 right-4"
      >
        <BaseButton :color="ButtonColorType.Gray" @click="handleCancel">
          {{ $t("product_platform.cancel") }}
        </BaseButton>
        <BaseButton
          type="submit"
          :color="ButtonColorType.Secondary"
          @click="onSubmit"
        >
          <save-icon class="mr-[6px]" />
          {{ $t("product_platform.save") }}
        </BaseButton>
      </div>
      <ArrowLeftIcon
        class="absolute top-[174px] right-0 cursor-pointer text-[#525457] hover:text-[#303132]"
        @click="closeResourceDetails"
      />
    </v-form>
  </div>

  <base-popup
    v-model="openPopupSave"
    :size="DialogSizeType.Medium"
    class="!w-[688px]"
  >
    <template #header>
      <v-card-title class="!p-4 !pb-0">
        <div class="flex justify-between">
          <div class="pt-2 pl-2">
            <WarningIcon />
          </div>
          <close-icon
            class="cursor-pointer mt-[-15px] mr-[-15px]"
            @click="handleClosePopupSave"
          />
        </div>
      </v-card-title>
    </template>
    <template #body>
      <v-card-text class="!px-6 !pb-2 !pt-3">
        <div
          class="font-medium text-[16px] leading-[24px] tracking-[0.5px] text-text-base"
        >
          {{ $t("product_platform.title_update_resource") }}
        </div>
        <div
          class="font-normal text-[13px] leading-[19.5px] tracking-[0.25px] text-text-lighter mt-1"
          v-html="$t('product_platform.desc_update_resource')"
        ></div>
      </v-card-text>
    </template>
    <template #footer>
      <div class="flex justify-end gap-3">
        <BaseButton @click="handleUpdateResource">
          {{ $t("product_platform.btn_yes_do_it") }}
        </BaseButton>
        <BaseButton :color="ButtonColorType.Gray" @click="handleClosePopupSave">
          {{ $t("product_platform.btn_cancel") }}
        </BaseButton>
      </div>
    </template>
  </base-popup>

  <base-popup
    v-model="openPopupCancel"
    :icon="DialogIconType.Warning"
    :submit-button-text="$t('product_platform.btn_yes')"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="$t('product_platform.desc_cancel')"
    @on-close="handleClosePopupCancel"
    @on-submit="handleCancelUpdate"
  />
</template>
<script setup lang="ts">
import cloneDeep from "lodash-es/cloneDeep";
import { useI18n } from "vue-i18n";
import { VForm } from "vuetify/lib/components/index.mjs";
import WarningIcon from "@/components/prod/icons/WarningIcon.vue";
import AdditionalTab from "@/components/prod/shared/AdditionalTab.vue";
import GeneralTab from "@/components/prod/shared/GeneralTab.vue";
import HistoryTab from "@/components/prod/shared/HistoryTab.vue";
import MultiEntityTab from "@/components/prod/shared/MultiEntityTab.vue";
import { getListItemCodeApi } from "@/api/prod/commonApi";
import { CUSTOM_VALIDATION_PAGE } from "@/constants/";
import {
  DATE_FORMAT,
  DETAIL_COMPONENT_NAME,
  DETAIL_TAB_TYPE,
} from "@/constants/index";
import { OFFER_TABS_VALUE } from "@/constants/offer";
import { getUserInfor } from "@/constants/userInfor";
import {
  ButtonColorType,
  DialogIconType,
  DialogSizeType,
  RequiredYn,
} from "@/enums";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import {
  useHistoryTabStore,
  useResourceStore,
  useSnackbarStore,
} from "@/store";
import customValidationStore from "@/store/admin/customValidation.store";
import { LARGE_ITEM_CODE } from "@/store/userPocket.store";
import { useCustomValidation } from "@/utils/custom-validation";
import { formatDate } from "@/utils/format-data";
import moment from "moment-timezone";
import { MULTI_ENTITY_APPLY_CATEGORY } from "@/constants/multiEntity";

const { listRulesOriginal } = storeToRefs(customValidationStore());
const { t } = useI18n();
const userInfor = getUserInfor();
const additionalDataTotal = ref<[any]>([{}]);
const triggerMountedTab = ref<boolean>(false);
const props = defineProps({
  isDuplicate: {
    type: Boolean,
    default: false,
  },
});

const emits = defineEmits(["fetch-data", "closeLoadingComponent"]);
const dateNow = moment(new Date()).format(DATE_FORMAT.YEAR_MONTH_DAY_FORMAT);
const form = ref<any>(false);
const formRef = ref<typeof VForm>();
const currentTab = ref(OFFER_TABS_VALUE.GENERAL);
const snackbarStore = useSnackbarStore();
const resourceStore = useResourceStore();
const historyStore = useHistoryTabStore();
const { resourceParamsFilter }: any = storeToRefs(resourceStore);
const {
  resourceDetail,
  isEdit,
  isDuplicate: duplicated,
  showDetail,
  resourceDetailMapped,
  resourceSelected,
  resourceDetailCloneMapped,
  multiEntityTypes,
  showEntitySearch,
  resourceEntityList,
  showResourceDuplicate,
} = storeToRefs(resourceStore);
const { getMultiEntityTypes, resetUpdateListEntity, getListResourceEntity } =
  resourceStore;
const { getListCustomValidation, setEditMode } = customValidationStore();
const {
  getRuleByAdditionalFields,
  applyRules,
  applyRulesInit,
  updateAdditionalField,
} = useCustomValidation();
const openPopupSave = ref(false);
const openPopupCancel = ref(false);
const resourceItemCodeList = ref<any[]>([]);
const baseTabs = ref<HTMLElement | any>(null);

const tabsList = computed(() => {
  let initTabs: any[] = [
    {
      value: OFFER_TABS_VALUE.GENERAL,
      label: t("product_platform.general"),
      component: GeneralTab,
      props: {
        modelValue: resourceDetailMapped.value?.general,
        pageName: DETAIL_COMPONENT_NAME.RESOURCE_SEARCH,
        isEdit: isEdit.value,
        createItemCodeList: resourceItemCodeList.value,
        page: CUSTOM_VALIDATION_PAGE.RESOURCE,
        type: resourceParamsFilter.value.itemCode,
        triggerMountedTab: triggerMountedTab.value,
      },
    },
    {
      value: OFFER_TABS_VALUE.ADDITIONAL,
      label: t("product_platform.additional"),
      component: AdditionalTab,
      props: {
        modelValue: resourceDetailMapped.value?.additional,
        pageName: DETAIL_COMPONENT_NAME.RESOURCE_SEARCH,
        isEdit: isEdit.value,
        page: CUSTOM_VALIDATION_PAGE.RESOURCE,
        type: resourceParamsFilter.value.itemCode,
        triggerMountedTab: triggerMountedTab.value,
      },
    },
  ];
  const multiComponent = multiEntityTypes.value?.find(
    (item) => item.value === resourceSelected.value?.itemCode
  );
  if (multiComponent) {
    initTabs.push({
      value: "Multi-Entity",
      label: t(`product_platform.multiEntity`),
      component: MultiEntityTab,
      props: {
        isEdit: isEdit.value,
        category: duplicated.value
          ? MULTI_ENTITY_APPLY_CATEGORY.DUPPLICATE
          : MULTI_ENTITY_APPLY_CATEGORY.EDIT,
        multiEntityTypes: multiEntityTypes.value,
        modelList: resourceEntityList.value,
        typeSelected: resourceSelected.value?.itemCode,
      },
      events: {
        onClickDrogArae: () => {
          showEntitySearch.value = true;
        },
      },
    });
  }
  if (!isEdit.value) {
    initTabs = [
      ...initTabs,
      {
        value: OFFER_TABS_VALUE.HISTORY,
        label: t("product_platform.history"),
        component: HistoryTab,
        props: {
          type: "resource",
        },
      },
    ];
  }
  return initTabs;
});
const isValid = computed(
  () =>
    !resourceDetailMapped.value.general.filter(
      (item: any) =>
        item.colName !== "obj_code" &&
        item.requiredYn === RequiredYn.Yes &&
        !item.attrVal
    ).length &&
    !resourceDetailMapped.value.additional.filter(
      (item: any) => item.requiredYn === RequiredYn.Yes && !item.attrVal?.length
    ).length
);

const initResourceDetail = async () => {
  if (resourceSelected.value) {
    await getResourceDetailData();
    resourceDetailMapped.value = cloneDeep(resourceDetail.value);
    resourceDetailCloneMapped.value = cloneDeep(resourceDetailMapped.value);
    if (!props.isDuplicate) {
      historyStore.fetchHistory({
        objUuid: resourceSelected.value.objUuid,
      });
    } else {
      isEdit.value = true;
    }
  }
};

const getResourceDetailData = async () => {
  try {
    const { data } = await resourceStore.getResourceDetail({
      objUuid: resourceSelected.value.objUuid,
      itemCode: resourceSelected.value.itemCode,
    });
    if (data) {
      additionalDataTotal.value = data.additional;
      data.general = data.general
        .sort((after, before) => after.sortNo - before.sortNo)
        .map((item) => {
          if (item.fieldTypeCode === COLUMN_FIELD_TYPE.DP) {
            if (item.colName === "valid_end_dtm" && props.isDuplicate) {
              return {
                ...item,
                attrVal: null,
              };
            }

            return {
              ...item,
              attrVal: item.attrVal,
            };
          } else if (item.colName === "item_code") {
            return {
              ...item,
              fieldTypeCode: COLUMN_FIELD_TYPE.DL,
            };
          } else if (
            props.isDuplicate &&
            ["valid_end_dtm", "obj_name", "obj_code"].includes(item.colName)
          ) {
            return {
              ...item,
              attrVal: null,
            };
          }
          return item;
        });
      data.additional = data?.additional.map((item: any) => ({
        ...item,
        attrVal:
          item.fieldTypeCode === COLUMN_FIELD_TYPE.DM
            ? JSON.parse(item?.attrVal)?.filter((eco: any) => eco.trim()) || []
            : item.attrVal,
      }));
      resourceDetail.value = {
        general: [
          ...data.general,
          ...data?.additional.filter(
            (item) => item.dispTab === DETAIL_TAB_TYPE.GENERAL
          ),
        ],
        additional: data?.additional.filter(
          (item) => item.dispTab !== DETAIL_TAB_TYPE.GENERAL
        ),
      };
    }
  } catch (err: any) {
    resourceDetail.value = null;
    snackbarStore.showSnackbar(
      err?.errorMsg || t("product_platform.something_went_wrong"),
      "error"
    );
  }
};

//METHODS
const handleEdit = () => {
  // set additonal to store
  triggerMountedTab.value = !triggerMountedTab.value;
  setEditMode(true);
  isEdit.value = true;
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
    openPopupSave.value = true;
  });
};

const handleCancel = () => {
  openPopupCancel.value = true;
};

const closeResourceDetails = () => {
  showEntitySearch.value = false;
  showDetail.value = false;
  resourceStore.resetSelectedItem();
};

const handleClosePopupSave = () => {
  openPopupSave.value = false;
};

const handleClosePopupCancel = () => {
  openPopupCancel.value = false;
};

const handleUpdateResource = async () => {
  const payload: any = {
    general: resourceDetailMapped.value.general
      .filter((item) => !item.dispTab)
      .map((resource) => {
        if (resource.colName === "chg_dept_name") {
          return {
            ...resource,
            attrVal: userInfor.chgDeptName,
          };
        }
        if (resource.colName === "chg_user") {
          return {
            ...resource,
            attrVal: userInfor.chgUser,
          };
        }
        return resource;
      }),
    additional: [
      ...resourceDetailMapped.value.general.filter((item) => item.dispTab),
      ...resourceDetailMapped.value.additional,
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
    insertEntityObjRels: resourceEntityList.value
      .flatMap((item) =>
        item.objRel.filter((ent) => ent.isAdd || props.isDuplicate)
      )
      .map((item) => ({
        entityCode: item.entityCode,
        validEndDtm: item.validEndDtm
          ? formatDate(
              item.validStartDtm,
              DATE_FORMAT.DATE_TYPE,
              DATE_FORMAT.DATE_TYPE
            )
          : null,
        validStartDtm: item.validStartDtm
          ? formatDate(
              item.validStartDtm,
              DATE_FORMAT.DATE_TYPE,
              DATE_FORMAT.DATE_TYPE
            )
          : null,
        rgstUser: userInfor.chgUser,
        updUser: userInfor.chgUser,
        rgstDtm: dateNow,
        updDtm: dateNow,
      })),
    updateEntityObjRels: props.isDuplicate
      ? []
      : resourceEntityList.value.flatMap((item) =>
          item.objRel.filter((ent) => ent.isUpdate)
        ),
  };
  try {
    const res = !props.isDuplicate
      ? await resourceStore.updateResource(payload)
      : await resourceStore.createNewResource(payload);

    if (res?.status === 200) {
      snackbarStore.showSnackbar(
        props.isDuplicate
          ? t("product_platform.duplicate_resource_successfully")
          : t("product_platform.successfully_saved"),
        "success"
      );
      handleClosePopupSave();
      emits("fetch-data");
      resetUpdateListEntity();
      showEntitySearch.value = false;
      if (props.isDuplicate) {
        resourceSelected.value.itemCode =
          resourceParamsFilter.value?.itemCode?.trim()
            ? resourceParamsFilter.value?.itemCode
            : resourceSelected.value.itemCode;
        resourceSelected.value.objUuid = res.data?.objUuid;
        duplicated.value = false;
        isEdit.value = false;
        await initResourceDetail();
      } else {
        await initResourceDetail();
        await getListResourceEntity();
        isEdit.value = false;
      }
    }
  } catch (err: any) {
    snackbarStore.showSnackbar(
      err?.errorMsg || t("product_platform.something_went_wrong"),
      "error"
    );
  }
};
const handleCancelUpdate = async () => {
  // resourceDetailMapped.value = cloneDeep(resourceDetailCloneMapped.value);
  openPopupCancel.value = false;
  await getResourceDetailData();
  await getListResourceEntity();
  showDetail.value = true;
  showResourceDuplicate.value = false;
  isEdit.value = false;
  duplicated.value = false;
  currentTab.value = OFFER_TABS_VALUE.GENERAL;
  emits("closeLoadingComponent");
};

watch(
  () => isEdit.value,
  (val) => {
    if (!val) {
      showEntitySearch.value = false;
    }
    if (val) {
      applyValidationRules();
    }
  }
);

watch([currentTab, isEdit], ([newTab]) => {
  emits("closeLoadingComponent");
  if (newTab === OFFER_TABS_VALUE.MULTI_ENTITY && isEdit.value) {
    showEntitySearch.value = true;
  } else {
    showEntitySearch.value = false;
  }
});

watch(
  () => resourceSelected.value,
  () => {
    initResourceDetail();
    getListResourceEntity();
    currentTab.value = OFFER_TABS_VALUE.GENERAL;
  },
  { deep: true }
);

onMounted(async () => {
  const { data } = await getListItemCodeApi({
    lItemCode: LARGE_ITEM_CODE.RESOURCE,
  } as any);
  if (data) {
    resourceItemCodeList.value = data;
  }
  initResourceDetail();

  getMultiEntityTypes();
  await getListResourceEntity();
  if (props.isDuplicate) {
    applyValidationRules();
  }
});

const dataTypeString = computed(() => {
  return JSON.stringify(resourceDetailMapped.value);
});

watch(dataTypeString, (newValueStr, oldValueStr) => {
  let newValue = JSON.parse(newValueStr);
  let oldValue = JSON.parse(oldValueStr);
  nextTick(() => {
    const { generalList, additionalList } = applyRules(
      oldValue,
      newValue,
      listRulesOriginal.value,
      resourceDetailMapped.value
    );
    if (resourceDetailMapped.value) {
      resourceDetailMapped.value.general = generalList;
      resourceDetailMapped.value.additional = additionalList;
    }
  });
});

const applyValidationRules = async () => {
  await getListCustomValidation({
    item: "R",
    type: resourceSelected.value.itemCode,
  });

  // Apply red, blue dots
  const mergeAdditionalField = [
    ...resourceDetailMapped.value.additional,
    ...resourceDetailMapped.value.general,
  ];
  const listRules = getRuleByAdditionalFields(
    mergeAdditionalField,
    listRulesOriginal.value
  );
  const newField = updateAdditionalField(
    resourceDetailMapped.value,
    listRules,
    {
      showRule: true,
      pageType: "R",
      type: resourceSelected.value.itemCode,
    }
  );
  resourceDetailMapped.value.general = newField.generalList;
  resourceDetailMapped.value.additional = newField.additionalList;

  // Apply init rules
  const listItems = [
    ...resourceDetailMapped.value?.additional,
    ...resourceDetailMapped.value?.general.filter(
      (item) => item.dispTab === "G"
    ),
  ];
  nextTick(() => {
    const { generalList, additionalList } = applyRulesInit(
      listItems,
      listRulesOriginal.value,
      resourceDetailMapped.value
    );
    resourceDetailMapped.value.general = generalList;
    resourceDetailMapped.value.additional = additionalList;
  });
};
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
}
.body {
  max-height: calc(100vh - 380px);
}
:deep(.v-window__container) {
  height: 100%;
}

:deep(.v-window) {
  flex: 1 1 0%;
}
</style>
