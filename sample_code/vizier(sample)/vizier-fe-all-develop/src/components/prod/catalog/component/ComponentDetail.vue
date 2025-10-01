<template>
  <div
    class="relative z-10 top-0 right-0 bg-white flex-shrink-0 rounded-lg"
    :class="isEdit && '!border !border-primary shadow-edit-mode'"
  >
    <div
      class="w-full relative text-[12px] h-full flex flex-col px-4 pb-3 pt-6"
    >
      <div class="w-full flex justify-between items-center px-2 pb-3">
        <div
          class="text-text-base text-base-vnb font-medium leading-10 tracking-[0.5px] h-[40px]"
        >
          {{ title }}
        </div>
        <BaseButton
          v-if="
            !isEdit &&
            currentTabEdit !== OFFER_TABS_VALUE.HISTORY &&
            !isDuplicate
          "
          :color="ButtonColorType.Secondary"
          @click="handleEdit"
        >
          <edit-icon class="mr-[6px]" />
          {{ $t("product_platform.edit") }}
        </BaseButton>
      </div>
      <v-form
        ref="formRef"
        v-model="isFormValid"
        class="w-full h-full flex-grow-1"
        @submit.prevent="onSubmit"
      >
        <BaseTabs
          ref="baseTabs"
          v-model="currentTabEdit"
          :tabs="getComponentTabs"
          :show-arrows="!isEdit"
          :center-active="true"
          :class-loco="
            isEdit
              ? 'max-h-[calc(100vh-350px)]'
              : currentTabEdit === OFFER_TABS_VALUE.RESOURCE
                ? 'max-h-[calc(100vh-330px)]'
                : 'max-h-[calc(100vh-300px)]'
          "
        />
      </v-form>
      <BasePagination
        v-if="
          totalResourceListItem > 0 &&
          currentTabEdit === OFFER_TABS_VALUE.RESOURCE &&
          !isEdit &&
          !isDuplicate
        "
        :pagination="{
          currentPage: paramsResourceFilter.page,
          totalPages: Math.ceil(
            totalResourceListItem / paramsResourceFilter.size
          ),
          pageSize: paramsResourceFilter.size,
        }"
        class="mb-3 mt-5"
        @on-change-page="handleChangePageResource"
      />
    </div>
    <div
      v-if="isEdit || isDuplicate"
      class="absolute flex justify-end items-center shrink-0 my-3 gap-2 bottom-0 right-4"
    >
      <BaseButton :color="ButtonColorType.Gray" @click="openPopup = true">
        {{ $t("product_platform.cancel") }}
      </BaseButton>
      <BaseButton :color="ButtonColorType.Secondary" @click="onSubmit">
        <save-icon class="mr-[6px]" />
        {{ $t("product_platform.save") }}
      </BaseButton>
    </div>
    <ArrowLeftIcon
      class="absolute top-[174px] right-0 cursor-pointer text-[#525457] hover:text-[#303132]"
      @click="closeComponentDetails"
    />
  </div>

  <base-popup
    v-model="openPopup"
    :icon="DialogIconType.Warning"
    :submit-button-text="$t('product_platform.btn_yes')"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="$t('product_platform.desc_cancel')"
    @on-close="closePopup"
    @on-submit="handleCancel"
  />

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
          v-html="$t('product_platform.desc_update_component')"
        ></div>
      </v-card-text>
    </template>
    <template #footer>
      <div class="flex justify-end gap-3">
        <BaseButton @click="handleUpdateComponent">
          {{ $t("product_platform.btn_yes_do_it") }}
        </BaseButton>
        <BaseButton :color="ButtonColorType.Gray" @click="handleClosePopupSave">
          {{ $t("product_platform.btn_cancel") }}
        </BaseButton>
      </div>
    </template>
  </base-popup>
</template>
<script setup lang="ts">
//THIRD-IMPORT
import cloneDeep from "lodash-es/cloneDeep";
import { useI18n } from "vue-i18n";
import { VForm } from "vuetify/lib/components/index.mjs";

//PROJECT-IMPORT
import {
  createComponentApi,
  updateComponentApi,
} from "@/api/prod/componentApi";
import { ACTION_TYPE, CUSTOM_VALIDATION_PAGE, DATE_FORMAT } from "@/constants/";
import { COMPONENTS_LAGRE_TYPE } from "@/constants/component";
import { DETAIL_TAB_TYPE } from "@/constants/index";
import { OFFER_TABS_VALUE } from "@/constants/offer";
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
import useComponentStore, {
  paramsFilterResourceDefault,
} from "@/store/component.store";
import { formatDate, isExpired } from "@/utils/format-data";
import { getListItemCodeApi } from "@/api/prod/commonApi";
import AdditionalTab from "@/components/prod/shared/AdditionalTab.vue";
import GeneralTab from "@/components/prod/shared/GeneralTab.vue";
import HistoryTab from "@/components/prod/shared/HistoryTab.vue";
import MultiEntityTab from "@/components/prod/shared/MultiEntityTab.vue";
import { DETAIL_COMPONENT_NAME } from "@/constants/index";
import customValidationStore from "@/store/admin/customValidation.store";
import {
  filteredConditionRules,
  useCustomValidation,
} from "@/utils/custom-validation";
import ResourceTab from "../../shared/ResourceTab.vue";
import { isEmpty } from "lodash-es";
import { getUserInfor } from "@/constants/userInfor";

const emits = defineEmits(["fetch-data", "closeLoadingComponent"]);
const props = defineProps({
  isDuplicate: {
    type: Boolean,
    default: false,
  },
});

const { t } = useI18n();
const { listRulesOriginal, totalAdditionalComponent } = storeToRefs(
  customValidationStore()
);
const { setTotalAdditional, getListCustomValidation, setEditMode } =
  customValidationStore();
const {
  getRuleByAdditionalFields,
  applyRules,
  applyRulesInit,
  updateAdditionalField,
} = useCustomValidation();
const useSnackbar = useSnackbarStore();
const historyStore = useHistoryTabStore();
const storeComponent = useComponentStore();
const { paramsFilterComponent, resourceTypes, componentEntityList } =
  storeToRefs(storeComponent);
const { getListResourceTypeByItemCode } = useComponentStore();
const filteredRules = ref<any[]>([]);
const componentAdditionalData = ref<[any]>([{}]);
const triggerMountedTab = ref<boolean>(false);
const baseTabs = ref<HTMLElement | any>(null);
const optionsSubType = ref<any[]>([]);
const openPopupSave = ref(false);
const formRef = ref<typeof VForm>();
const isFormValid = ref<any>(false);
const { getResourceList } = useResourceStore();
const { showSnackbar } = useSnackbarStore();
const {
  componentSelected,
  componentDetail,
  showResourceAdd,
  isEdit,
  resourceParamsFilter,
  multiEntityTypes,
  showEntitySearch,
  totalResourceListItem,
  currentTabEdit,
  isDuplicate: duplicate,
  paramsResourceFilter,
} = storeToRefs(useComponentStore());
const {
  getComponentDetail,
  resetSelectedItem,
  getMultiEntityTypes,
  getListComponentEntity,
} = useComponentStore();
const openPopup = ref(false);

const userInfor = getUserInfor();

const checkIsExistType = computed(() => {
  return multiEntityTypes.value?.find(
    (item) => item.value === componentSelected.value?.itemCode
  );
});

const getComponentTabs = computed(() => {
  let initTabs: any[] = [
    {
      value: OFFER_TABS_VALUE.GENERAL,
      label: t("product_platform.general"),
      component: GeneralTab,
      props: {
        modelValue: componentDetail.value?.general,
        pageName: DETAIL_COMPONENT_NAME.COMPONENT_SEARCH,
        createItemLargeCodeList: COMPONENTS_LAGRE_TYPE,
        createItemCodeList: optionsSubType.value,
        isEdit: isEdit.value || props.isDuplicate,
        page: CUSTOM_VALIDATION_PAGE.COMPONENT,
        type: paramsFilterComponent.value.type,
        subType: paramsFilterComponent.value.subType,
        triggerMountedTab: triggerMountedTab.value,
        isDuplicate: true,
      },
    },
    {
      value: OFFER_TABS_VALUE.ADDITIONAL,
      label: t("product_platform.additional"),
      component: AdditionalTab,
      props: {
        modelValue: componentDetail.value?.additional,
        pageName: DETAIL_COMPONENT_NAME.COMPONENT_SEARCH,
        isEdit: isEdit.value || props.isDuplicate,
        page: CUSTOM_VALIDATION_PAGE.COMPONENT,
        type: paramsFilterComponent.value.type,
        subType: paramsFilterComponent.value.subType,
        triggerMountedTab: triggerMountedTab.value,
        isDuplicate: true,
      },
    },
  ];
  if (componentSelected.value && resourceTypes.value?.length) {
    initTabs = [
      ...initTabs,
      {
        value: OFFER_TABS_VALUE.RESOURCE,
        label: t("product_platform.resource_title"),
        component: ResourceTab,
        props: {
          isEdit: isEdit.value || props.isDuplicate,
          modelList: componentDetail.value.resources,
          relationItem: componentSelected.value,
          typeAllowDrop: resourceTypes.value,
          filteredRule: filteredRules.value,
        },
        events: {
          onClickDrogArae: handleOpenSearchResource,
        },
      },
    ];
  }
  if (checkIsExistType.value) {
    initTabs.push({
      value: OFFER_TABS_VALUE.MULTI_ENTITY,
      label: t("product_platform.multiEntity"),
      component: MultiEntityTab,
      props: {
        isEdit: isEdit.value || props.isDuplicate,
        multiEntityTypes: multiEntityTypes.value,
        modelList: componentEntityList.value,
        typeSelected: componentSelected.value?.itemCode,
      },
      events: {
        onClickDrogArae: handleOpenSearch,
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
          type: "component",
        },
      },
    ];
  }

  return initTabs;
});

//Watch tab change
watch([currentTabEdit, isEdit], async ([newTab]) => {
  emits("closeLoadingComponent");
  if (
    newTab === OFFER_TABS_VALUE.RESOURCE &&
    isEdit.value &&
    !isExpired(componentSelected.value.validEndDtm)
  ) {
    await filteredRulesByComponent();
    showResourceAdd.value = true;
  } else {
    showResourceAdd.value = false;
  }
  if (newTab === OFFER_TABS_VALUE.MULTI_ENTITY && isEdit.value) {
    showEntitySearch.value = true;
    showResourceAdd.value = false;
  } else {
    showEntitySearch.value = false;
  }
});

const closeComponentDetails = () => {
  isEdit.value = false;
  componentSelected.value = null;
  resetSelectedItem();
};

const isInvalid = computed(
  () =>
    !!componentDetail.value?.general?.find(
      (item: any) =>
        item.requiredYn === RequiredYn.Yes &&
        (item.attrVal?.toString().trim().length === 0 ||
          item.attrVal === null) &&
        item.colName !== "obj_code"
    ) ||
    !!componentDetail.value?.additional?.find(
      (item: any) =>
        item.requiredYn === RequiredYn.Yes &&
        (item.attrVal?.toString().trim().length === 0 || item.attrVal === null)
    )
);

const onSubmit = async () => {
  await formRef.value?.validate();
  if (baseTabs.value?.componentRefs?.length) {
    baseTabs.value?.componentRefs.forEach((el) => el?.validationAllSelect?.());
  }
  nextTick(() => {
    if (!isFormValid.value || isInvalid.value) {
      useSnackbar.showSnackbar(
        t("product_platform.required_field_missing"),
        "error"
      );
      return;
    }
    openPopupSave.value = true;
  });
};

const title = computed(() =>
  props.isDuplicate
    ? t("product_platform.component_duplicate")
    : isEdit.value
      ? t("product_platform.component_edit")
      : t("product_platform.component_details")
);

// check custom validation Rule

const handleEdit = async () => {
  componentAdditionalData.value = componentDetail.value.additional.concat(
    componentDetail.value.general.filter((item) => item.dispTab === "G")
  );
  triggerMountedTab.value = !triggerMountedTab.value;
  setEditMode(true);
  isEdit.value = true;

  await getListCustomValidation({
    item: "C",
    type: componentSelected.value.itemType,
    subType: componentSelected.value.itemCode,
  });

  const listRules = getRuleByAdditionalFields(
    [...componentDetail.value.additional, ...componentDetail.value.general],
    listRulesOriginal.value
  );
  const newField = updateAdditionalField(componentDetail.value, listRules, {
    showRule: true,
    pageType: "C",
    type: componentSelected.value.itemType,
    subType: componentSelected.value.itemCode,
  });

  componentDetail.value.general = newField.generalList;
  componentDetail.value.additional = newField.additionalList;

  // Apply init rules
  const listItems = [
    ...componentDetail.value?.additional,
    ...componentDetail.value?.general.filter((item) => item.dispTab === "G"),
  ];
  nextTick(() => {
    const { generalList, additionalList } = applyRulesInit(
      listItems,
      listRulesOriginal.value,
      componentDetail.value
    );
    componentDetail.value.general = generalList;
    componentDetail.value.additional = additionalList;
  });
};

const filteredRulesByComponent = async () => {
  await getListCustomValidation({
    item: "C",
    type: componentSelected.value.itemType,
    subType: componentSelected.value.itemCode,
  });
  filteredRules.value = filteredConditionRules(
    listRulesOriginal.value,
    totalAdditionalComponent.value
  );
};

const closePopup = () => {
  openPopup.value = false;
  showResourceAdd.value = false;
};

const handleClosePopupSave = () => {
  openPopupSave.value = false;
};

const handleResetSearch = () => {
  resourceParamsFilter.value = cloneDeep(paramsFilterResourceDefault);
};

const handleCancel = async () => {
  duplicate.value = false;
  await initComponentDetail();
  await getListComponentEntity();
  closePopup();
  isEdit.value = false;
  currentTabEdit.value = OFFER_TABS_VALUE.GENERAL;
};

const handleUpdateComponent = async () => {
  if (!isFormValid.value) {
    useSnackbar.showSnackbar(
      t("product_platform.required_field_missing"),
      "error"
    );
    openPopupSave.value = false;
    return;
  }
  try {
    const payload = {
      general: componentDetail.value.general
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
      additional:
        [
          ...componentDetail.value?.additional,
          ...componentDetail.value.general.filter((item) => item.dispTab),
        ].map((item: any) => {
          let val = item.attrVal;
          if (item.fieldTypeCode === COLUMN_FIELD_TYPE.DM) {
            val = JSON.stringify(item.attrVal);
          }
          return {
            ...item,
            attrVal: val,
          };
        }) || [],
      resources:
        componentDetail.value?.resources
          ?.filter((item: any) => {
            return item.isAdd || item.isUpdate;
          })
          .map((rsc) => ({
            componentUUID: rsc.componentUUID,
            resourceUUID: rsc.resourceUUID,
            validEndDtm: formatDate(
              rsc.relationEndDate,
              DATE_FORMAT.DATE_TYPE,
              DATE_FORMAT.DATE_TYPE
            ),
            validStartDtm: formatDate(
              rsc.relationStartDate,
              DATE_FORMAT.DATE_TYPE,
              DATE_FORMAT.DATE_TYPE
            ),
            workTypeCode: rsc.workTypeCode,
          })) || [],
      resourceList: props.isDuplicate
        ? componentDetail.value?.resources.map((rsc) => ({
            componentUUID: "",
            resourceUUID: rsc.objUuid,
            validEndDtm: formatDate(
              rsc.relationEndDate,
              DATE_FORMAT.DATE_TYPE,
              DATE_FORMAT.DATE_TYPE
            ),
            validStartDtm: formatDate(
              rsc.relationStartDate,
              DATE_FORMAT.DATE_TYPE,
              DATE_FORMAT.DATE_TYPE
            ),
            workTypeCode: rsc.workTypeCode ?? ACTION_TYPE.ADD,
          })) || []
        : [],
      insertEntityObjRels: componentEntityList.value.flatMap((item) =>
        item.objRel
          .filter(
            (ent) => (!props.isDuplicate && ent.isAdd) || props.isDuplicate
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
          }))
      ),
      updateEntityObjRels: !props.isDuplicate
        ? componentEntityList.value.flatMap((item) =>
            item.objRel.filter((ent) => ent.isUpdate)
          )
        : [],
    };

    const { data, status } = props.isDuplicate
      ? await createComponentApi(payload)
      : await updateComponentApi(payload);
    if (status == 200) {
      // fetch new data history
      historyStore.fetchHistory({
        objUuid: props.isDuplicate
          ? data.objUuid
          : componentSelected.value?.objUuid,
      });
      showSnackbar(
        t("product_platform.toastMsg.component.successfullySaved"),
        "success"
      );
      emits("fetch-data");
      closePopup();
      handleResetSearch();
      paramsResourceFilter.value.size = 8;
      paramsResourceFilter.value.page = 1;
      if (props.isDuplicate) {
        componentSelected.value.uuid = data.objUuid;
        componentSelected.value.prodUuid = data.objUuid;
        componentSelected.value.objUuid = data.objUuid;
        componentSelected.value.itemUnique = data.objUuid;
        paramsResourceFilter.value.objUuid = data.objUuid;
        duplicate.value = false;
      } else {
        isEdit.value = false;
      }
      initComponentDetail();
      if (!componentDetail.value?.resources?.length) {
        await getResourceListLocal();
      }
      await getListComponentEntity();
    }
  } catch (error) {
    showSnackbar(
      t("product_platform.toastMsg.component.editComponentFailed"),
      "error"
    );
  }
  handleClosePopupSave();
};

const initComponentDetail = async () => {
  if (
    componentSelected.value &&
    (isEmpty(componentDetail.value.general) || props.isDuplicate)
  ) {
    try {
      const res = await getComponentDetail({
        objUuid: componentSelected.value?.itemUnique,
      });
      if (!props.isDuplicate) {
        historyStore.fetchHistory({
          objUuid: componentSelected.value?.itemUnique,
        });
      }
      if (res?.data) {
        if (props.isDuplicate) {
          res.data.additional.forEach((item) => {
            if (item.fieldTypeCode === COLUMN_FIELD_TYPE.DM) {
              item.attrVal =
                JSON.parse(item?.attrVal)?.filter((value: any) =>
                  value.trim()
                ) || [];
            }
          });
          componentDetail.value = {
            general: [
              ...res.data?.general,
              ...res.data?.additional.filter(
                (item) => item.dispTab === DETAIL_TAB_TYPE.GENERAL
              ),
            ],
            additional: res.data.additional.filter(
              (item) => item.dispTab !== DETAIL_TAB_TYPE.GENERAL
            ),
            resources: componentSelected.value?.resource || [],
          };
          componentDetail.value.general.forEach((item) => {
            if (item.colName === "obj_name") {
              item.attrVal = null;
            } else if (item.colName === "valid_end_dtm") {
              item.attrVal = null;
            } else if (item.colName === "obj_code") {
              item.attrVal = null;
            } else if (item.colName === "chg_dept_name") {
              item.attrVal = userInfor.chgDeptName;
            } else if (item.colName === "chg_user") {
              item.attrVal = userInfor.chgUser;
            } else if (item.fieldTypeCode === COLUMN_FIELD_TYPE.DP) {
              item.attrVal = item.attrVal;
            } else if (item.colName === "item_code") {
              item.fieldTypeCode = COLUMN_FIELD_TYPE.DL;
            }
          });
          const itemLargeType = {
            colName: "item_large_code",
            fieldTypeCode: COLUMN_FIELD_TYPE.DL,
            editYn: RequiredYn.No,
            sortNo: "0",
            useYn: RequiredYn.Yes,
            attrMaxLength: null,
            requiredYn: RequiredYn.Yes,
            labelId: "type",
            attrVal: componentSelected.value?.itemLargeType,
          };
          componentDetail.value.general.unshift(itemLargeType);
          componentDetail.value.general.push({
            colName: "dplcTrgtUuid",
            fieldTypeCode: "HD",
            editYn: null,
            sortNo: null,
            useYn: null,
            attrMaxLength: null,
            requiredYn: null,
            labelId: null,
            attrVal: componentSelected.value?.objUuid,
          });
        } else {
          componentDetail.value = {
            ...componentDetail.value,
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
                      ? JSON.parse(item?.attrVal)?.filter((value: any) =>
                          value.trim()
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
                    ? JSON.parse(item?.attrVal)?.filter((value: any) =>
                        value.trim()
                      ) || []
                    : item.attrVal,
              })),
          };
        }
      } else {
        componentDetail.value = null;
      }
    } catch (error: any) {
      componentDetail.value = null;
      // showSnackbar(error.errorMsg, "error");
    }
    mergeGeneralTabAndAdditionalTab(componentDetail.value || {});
  }
};

const getResourceListLocal = async () => {
  if (props.isDuplicate) {
    paramsResourceFilter.value.size = 500;
  }
  paramsResourceFilter.value.onlyValidDtm = props.isDuplicate;
  const { data } = await getResourceList(
    paramsResourceFilter.value,
    props.isDuplicate
  );
  componentDetail.value.resources = data.elements;
  totalResourceListItem.value = data.totalElements;
};

const handleChangePageResource = (page: number) => {
  paramsResourceFilter.value.page = page;
  getResourceListLocal();
};

const getListSubType = async (type: any) => {
  if (!type) return;
  try {
    const { data } = await getListItemCodeApi({
      mItemCode: type,
    });
    optionsSubType.value = data;
  } catch (error: any) {
    useSnackbar.showSnackbar(
      error?.errorMsg || t("product_platform.something_went_wrong"),
      "error"
    );
  }
};

const handleOpenSearch = () => {
  showEntitySearch.value = true;
};

const handleOpenSearchResource = () => {
  showResourceAdd.value = true;
};

watch(
  () => componentSelected.value,
  async (newVal) => {
    if (newVal) {
      await initComponentDetail();
      await getListSubType(newVal?.itemLargeType);
      paramsResourceFilter.value.objUuid = newVal?.objUuid || newVal?.prodUuid;
      if (checkIsExistType.value && !componentEntityList.value?.length) {
        await getListComponentEntity(props.isDuplicate);
      }
      if (!componentDetail.value?.resources?.length) {
        await getResourceListLocal();
        await getListResourceTypeByItemCode(newVal?.itemCode);
      }
    }
  },
  { immediate: true, deep: true }
);

const mergeGeneralTabAndAdditionalTab = (data) => {
  setTotalAdditional(
    DETAIL_COMPONENT_NAME.COMPONENT_SEARCH,
    data?.additional?.concat(
      data?.general?.filter((item) => item.dispTab === "G")
    )
  );
};

watch(
  () => [isEdit.value, props.isDuplicate],
  (val) => {
    if (!val[0] && !val[1]) {
      paramsResourceFilter.value.size = 8;
      paramsResourceFilter.value.page = 1;
      getResourceListLocal();
      showEntitySearch.value = false;
    } else {
      paramsResourceFilter.value.size = 500;
      getResourceListLocal();
    }
  }
);

const dataTypeString = computed(() => {
  return JSON.stringify(componentDetail.value);
});

watch(dataTypeString, (newValueStr, oldValueStr) => {
  let newValue = JSON.parse(newValueStr);
  let oldValue = JSON.parse(oldValueStr);
  nextTick(() => {
    const { generalList, additionalList } = applyRules(
      oldValue,
      newValue,
      listRulesOriginal.value,
      componentDetail.value
    );
    if (componentDetail.value) {
      componentDetail.value.general = generalList;
      componentDetail.value.additional = additionalList;
    }
  });
});

onMounted(() => {
  getMultiEntityTypes();
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
