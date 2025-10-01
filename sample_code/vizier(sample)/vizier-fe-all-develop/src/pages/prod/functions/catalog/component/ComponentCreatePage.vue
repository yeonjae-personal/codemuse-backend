<script setup lang="ts">
import { useI18n } from "vue-i18n";
import cloneDeep from "lodash-es/cloneDeep";
import { VForm } from "vuetify/lib/components/index.mjs";
import { getListItemCodeApi } from "@/api/prod/commonApi";
import {
  createComponentApi,
  getComponentSearchType,
  getCreateFieldsInfoApi,
} from "@/api/prod/componentApi";
import AdditionalTab from "@/components/prod/shared/AdditionalTab.vue";
import GeneralTab from "@/components/prod/shared/GeneralTab.vue";
import { COMPONENTS_TYPE } from "@/constants/component";
import { DATE_FORMAT, DETAIL_TAB_TYPE } from "@/constants/index";
import { MULTI_ENTITY_APPLY_CATEGORY } from "@/constants/multiEntity";
import { OFFER_TABS_VALUE } from "@/constants/offer";
import { getUserInfor } from "@/constants/userInfor";
import {
  ButtonColorType,
  DialogIconType,
  RequiredYn,
  TypeComponentCode,
} from "@/enums";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import { useSnackbarStore } from "@/store";
import customValidationStore from "@/store/admin/customValidation.store";
import useComponentStore from "@/store/component.store";
import { GroupedItem, Item } from "@/types/catalog/component/ComponentSearch";
import {
  filteredConditionRules,
  useCustomValidation,
} from "@/utils/custom-validation";
import { MenuItemID } from "@/enums/redirect";
import { setHoverColor } from "@/utils/impact-analysis-utils";
import MultiEntityTab from "@/components/prod/shared/MultiEntityTab.vue";
import ResourceTab from "@/components/prod/shared/ResourceTab.vue";
import { formatDate } from "@/utils/format-data";
import { RESOURCE_PARAMS_FILTER_DEFAULT } from "@/constants/resource";

const removeTab = inject<any>("removeTab");
const modules = import.meta.glob("@/components/**/*.vue");
const {
  showResourceAddCreate,
  showEntitySearchCreate,
  componentCreateData,
  isViewMode,
  code,
  componentCreateEntityList,
  resourceTypes,
  optionsSubType,
  multiEntityTypes,
  paramsSearchEntityCreate,
  listResourceAddCreate,
  currentTabCreate,
  resourceParamsFilterCreate,
} = storeToRefs(useComponentStore());
const {
  resetComponentCreateData,
  getMultiEntityTypes,
  getListResourceTypeByItemCode,
} = useComponentStore();
const { listRulesOriginal } = storeToRefs(customValidationStore());
const { getListCustomValidation } = customValidationStore();
const useSnackbar = useSnackbarStore();
const userInfor = getUserInfor();
const { t } = useI18n();

const openPopupCancel = ref(false);
const openPopupSave = ref(false);
const isFormValid = ref(false);
const formRef = ref<typeof VForm>();
const filteredRules = ref<any[]>([]);
const asyncComponent = ref<any>(null);
const baseTabs = ref<HTMLElement | any>(null);
const { showSnackbar } = useSnackbarStore();
const {
  getRuleByAdditionalFields,
  applyRules,
  applyRulesInit,
  updateAdditionalField,
} = useCustomValidation();

const getCreateInfo = async (subType: string, type: string) => {
  try {
    const { data, status } = await getCreateFieldsInfoApi({
      itemCode: subType,
    });

    if (status === 200) {
      componentCreateData.value.general = [
        ...data?.general,
        ...data?.additional.filter(
          (item) => item.dispTab === DETAIL_TAB_TYPE.GENERAL
        ),
      ].map((item: any) => ({
        ...item,
        attrVal:
          item.fieldTypeCode === COLUMN_FIELD_TYPE.DM
            ? JSON.parse(item?.attrVal)?.filter((val: any) => val.trim()) || []
            : item.attrVal,
      }));

      componentCreateData.value.general.forEach((element) => {
        if (
          element.colName === "item_code" ||
          element.colName === "item_large_code"
        ) {
          element.fieldTypeCode = COLUMN_FIELD_TYPE.DL;
          element.editYn = RequiredYn.Yes;
        } else if (element.colName === "chg_dept_name") {
          element.attrVal = userInfor.chgDeptName;
        } else if (element.colName === "chg_user") {
          element.attrVal = userInfor.chgUser;
        }
      });
      componentCreateData.value.additional = data?.additional
        .filter((item) => item.dispTab !== DETAIL_TAB_TYPE.GENERAL)
        .map((item: any) => ({
          ...item,
          attrVal:
            item.fieldTypeCode === COLUMN_FIELD_TYPE.DM
              ? JSON.parse(item?.attrVal)?.filter((val: any) => val.trim()) ||
                []
              : item.attrVal,
        }));

      componentCreateEntityList.value = data?.entityItemInfos.map(
        (entityItem) => ({
          ...entityItem,
          objRel: entityItem.objRel ? entityItem.objRel : [],
        })
      );

      await getListCustomValidation({ item: "C", type, subType });

      const listRules = getRuleByAdditionalFields(
        [
          ...componentCreateData.value.additional,
          ...componentCreateData.value.general,
        ],
        listRulesOriginal.value
      );
      const newField = updateAdditionalField(
        componentCreateData.value,
        listRules,
        {
          showRule: true,
          pageType: "C",
          type: componentCreateLargeType.value,
          subType: componentCreateType.value,
        }
      );

      componentCreateData.value.general = newField.generalList;
      componentCreateData.value.additional = newField.additionalList;

      // Apply init rules
      const listItems = [
        ...componentCreateData.value?.additional,
        ...componentCreateData.value?.general.filter(
          (item) => item.dispTab === "G"
        ),
      ];
      nextTick(() => {
        const { generalList, additionalList } = applyRulesInit(
          listItems,
          listRulesOriginal.value,
          componentCreateData.value
        );
        componentCreateData.value.general = generalList;
        componentCreateData.value.additional = additionalList;
      });
    }
  } catch (error: any) {
    useSnackbar.showSnackbar(
      error?.errorMsg || t("product_platform.something_went_wrong"),
      "error"
    );
  }
};

const groupByMiddleItemCode = (data: Item[]): GroupedItem[] => {
  const grouped = data.reduce((acc, item) => {
    let group = acc.find((group) => group.value === item.middleItemCode);
    if (!group) {
      group = {
        name: item.middleItemName,
        value: item.middleItemCode,
        sortNo: item.middleSortNo,
        children: [],
      };
      acc.push(group);
    }
    group.children.push({
      name: item.itemName,
      value: item.itemCode,
      sortNo: item.sortNo,
    });
    return acc;
  }, [] as GroupedItem[]);

  // Sort groups by sortNo
  grouped.sort((cur, next) => cur.sortNo - next.sortNo);

  // Sort children within each group by sortNo
  grouped.forEach((group) => {
    group.children.sort((cur, next) => cur.sortNo - next.sortNo);
  });

  return grouped;
};

const optionsType = ref<any>(
  COMPONENTS_TYPE.map((item) => ({ ...item, name: t(item.name) }))
);

const listTabs = computed(() => {
  let tabs: any = [
    {
      value: OFFER_TABS_VALUE.GENERAL,
      label: t("product_platform.general"),
      component: GeneralTab,
      props: {
        modelValue: componentCreateData.value?.general,
        isEdit: !isViewMode.value,
        isAdd: !isViewMode.value,
        createItemLargeCodeList: optionsType.value.map((item) => ({
          ...item,
          cmcdDetlNm: item.name || null,
          cmcdDetlId: item.value || null,
        })),
        createItemCodeList: optionsSubType.value,
      },
      onClick: () => {
        showResourceAddCreate.value = false;
        showEntitySearchCreate.value = false;
      },
    },
    {
      value: OFFER_TABS_VALUE.ADDITIONAL,
      label: t("product_platform.additional"),
      component: AdditionalTab,
      props: {
        modelValue: componentCreateData.value?.additional,
        isEdit: !isViewMode.value,
        isAdd: !isViewMode.value,
      },
      onClick: () => {
        showResourceAddCreate.value = false;
        showEntitySearchCreate.value = false;
      },
    },
  ];
  if (resourceTypes.value?.length) {
    tabs.push({
      value: OFFER_TABS_VALUE.RESOURCE,
      label: t("product_platform.resource_title"),
      component: ResourceTab,
      props: {
        isEdit: !isViewMode.value,
        modelList: componentCreateData.value?.resource,
        filteredRule: filteredRules.value,
        typeAllowDrop: resourceTypes.value,
      },
      onClick: () => {
        filteredRulesByComponent();
        showResourceAddCreate.value = true;
        showEntitySearchCreate.value = false;
        handleCloseLoadingComponent();
      },
      events: {
        onClickDrogArae: handleOpenSearchResource,
      },
    });
  }
  if (componentCreateEntityList.value.length) {
    tabs.push({
      value: OFFER_TABS_VALUE.MULTI_ENTITY,
      label: t("product_platform.multiEntity"),
      component: MultiEntityTab,
      props: {
        isEdit: !isViewMode.value,
        isViewMode: isViewMode.value,
        category: MULTI_ENTITY_APPLY_CATEGORY.DUPPLICATE,
        multiEntityTypes: multiEntityTypes.value,
        modelList: componentCreateEntityList.value,
        typeSelected: paramsSearchEntityCreate.value?.itemCode,
      },
      onClick: () => {
        if (!isViewMode.value) {
          showResourceAddCreate.value = false;
          showEntitySearchCreate.value = true;
        }
      },
      events: {
        onClickDrogArae: handleOpenSearch,
      },
    });
  }

  return tabs;
});

const itemTypeGeneral = computed(() => {
  return componentCreateData.value?.general?.find(
    (item) => item.colName === "item_code"
  )?.attrVal;
});

const componentCreateLargeType = computed(() => {
  if (componentCreateData.value?.general?.length) {
    return componentCreateData.value.general.find(
      (item) => item.colName === "item_large_code"
    ).attrVal;
  }
  return "";
});

const componentCreateType = computed(() => {
  if (componentCreateData.value?.general?.length) {
    return componentCreateData.value.general.find(
      (item) => item.colName === "item_code"
    ).attrVal;
  }
  return "";
});

const handleOpenSearch = () => {
  showEntitySearchCreate.value = true;
};

const handleOpenSearchResource = () => {
  showResourceAddCreate.value = true;
};

const handleCancel = async () => {
  openPopupCancel.value = false;
  resetComponentCreateData();
  await getListSubType(TypeComponentCode.Characteristics);
  getCreateInfo(
    optionsSubType.value[0].itemCode,
    TypeComponentCode.Characteristics
  );
  currentTabCreate.value = OFFER_TABS_VALUE.GENERAL;
  removeTab(MenuItemID.ComponentCreate);
};

const handleSave = async () => {
  try {
    const payload = {
      general: componentCreateData.value.general
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
          ...componentCreateData.value?.additional,
          ...componentCreateData.value.general.filter((item) => item.dispTab),
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

      resourceList: componentCreateData.value.resource.map((item) => ({
        resourceUUID: item?.resourceUUID,
        resourceCode: item?.objCode,
        validStartDtm: item?.relationStartDate
          ? formatDate(
              item?.relationStartDate,
              DATE_FORMAT.DATE_TYPE,
              DATE_FORMAT.DATE_TYPE
            )
          : null,
        validEndDtm: item?.relationEndDate
          ? formatDate(
              item?.relationEndDate,
              DATE_FORMAT.DATE_TYPE,
              DATE_FORMAT.DATE_TYPE
            )
          : null,
      })),
      insertEntityObjRels: componentCreateEntityList.value
        .flatMap((item) => item.objRel)
        .filter((ent) => ent.isAdd)
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
        })),
    };
    const { data, status } = await createComponentApi(payload);
    if (status === 200) {
      // componentCreateData.value.general.objCode = data?.code;
      // componentCreated.value.prodUuid = data?.objUUID;
      code.value = data.code;
      showResourceAddCreate.value = false;
      currentTabCreate.value = OFFER_TABS_VALUE.GENERAL;
      showSnackbar(t("product_platform.create_component_success"), "success");
      isViewMode.value = true;
      componentCreateData.value.resource =
        componentCreateData.value?.resource.map((resourceItem) => ({
          ...resourceItem,
          isAdd: false,
        }));
      openPopupSave.value = false;
      handleCloseLoadingComponent();
    }
    showEntitySearchCreate.value = false;
  } catch (error: any) {
    showSnackbar(
      error?.errorMsg || t("product_platform.something_went_wrong"),
      "error"
    );
  }
};

const isInvalid = computed(
  () =>
    !!componentCreateData.value?.general?.find(
      (item: any) =>
        item.requiredYn === RequiredYn.Yes &&
        (item.attrVal?.length === 0 || item.attrVal === null) &&
        item.colName !== "obj_code"
    ) ||
    !!componentCreateData.value?.additional?.find(
      (item: any) =>
        item.requiredYn === RequiredYn.Yes &&
        (item.attrVal?.length === 0 || item.attrVal === null)
    )
);
const onClickSave = async () => {
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

const getListSubType = async (type: any) => {
  if (!type) return;
  try {
    const { data } = await getListItemCodeApi({
      mItemCode: type,
    });
    optionsSubType.value = data;

    if (data?.length && componentCreateData.value?.general?.length) {
      componentCreateData.value.general.forEach((item) => {
        if (item.colName === "item_code") {
          item.attrVal = optionsSubType.value[0].itemCode;
        }
      });
    }
  } catch (error: any) {
    useSnackbar.showSnackbar(
      error?.errorMsg || t("product_platform.something_went_wrong"),
      "error"
    );
  }
};

const filteredRulesByComponent = () => {
  const allRule = [
    ...componentCreateData.value.additional,
    ...componentCreateData.value.general,
  ];
  filteredRules.value = filteredConditionRules(
    listRulesOriginal.value,
    allRule
  );
};

const loadComponent = async (componentPath) => {
  const loader: any = modules[`/${componentPath}`];
  if (!loader) {
    useSnackbar.showSnackbar(t("product_platform.ob_url_invalid"), "error");
    return;
  }
  asyncComponent.value = defineAsyncComponent(loader);
};
const handleCloseLoadingComponent = () => {
  asyncComponent.value = null;
};

watch(
  () => itemTypeGeneral.value,
  async (newVal) => {
    await getListResourceTypeByItemCode(newVal);
  },
  { immediate: true }
);

watch(
  () => componentCreateLargeType.value,
  (newVal) => {
    getListSubType(newVal);
  }
);

watch(
  () => componentCreateType.value,
  (newVal) => {
    if (newVal) {
      getCreateInfo(newVal, componentCreateLargeType.value);
      listResourceAddCreate.value.items = [];
      listResourceAddCreate.value.totalSearch = 0;
      resourceParamsFilterCreate.value = cloneDeep(
        RESOURCE_PARAMS_FILTER_DEFAULT
      );
    }
  }
);

onMounted(async () => {
  if (!componentCreateLargeType.value) {
    await getListSubType(TypeComponentCode.Characteristics);
    getCreateInfo(
      optionsSubType.value[0].itemCode,
      TypeComponentCode.Characteristics
    );
    getMultiEntityTypes();
    const res = await getComponentSearchType();
    optionsType.value = groupByMiddleItemCode(res.data);
  }
});

const dataTypeString = computed(() => {
  return JSON.stringify(componentCreateData.value);
});

watch(dataTypeString, (newValueStr, oldValueStr) => {
  let newValue = JSON.parse(newValueStr);
  let oldValue = JSON.parse(oldValueStr);
  nextTick(() => {
    const { generalList, additionalList } = applyRules(
      oldValue,
      newValue,
      listRulesOriginal.value,
      componentCreateData.value
    );
    if (componentCreateData.value) {
      componentCreateData.value.general = generalList;
      componentCreateData.value.additional = additionalList;
    }
  });
});

provide("handleLoadComponent", loadComponent);
</script>

<template>
  <FourColumns>
    <v-form
      ref="formRef"
      v-model="isFormValid"
      class="col-span-1"
      @submit.prevent="onClickSave"
    >
      <div
        class="px-4 py-6 relative z-10 rounded-[12px] top-0 right-0 bg-white flex flex-col flex-shrink-0 h-full"
      >
        <div class="w-full flex justify-between items-center px-2 h-10 mb-2">
          <div class="text-text-base text-base font-medium tracking-[0.5px]">
            {{ $t("product_platform.component_create") }}
          </div>
        </div>
        <cf-card-dropdown
          :title="
            (componentCreateData.general.length &&
              componentCreateData.general?.find(
                (item) => item.colName === 'obj_name'
              )?.attrVal) ||
            $t('product_platform.componentName')
          "
          :description="code || $t('product_platform.auto_generation')"
          type-bg="linear"
          :display-border-left="setHoverColor(componentCreateLargeType)"
          is-disable-zoom
          :node="{
            hideNodeLeft: true,
            isActiveNodeLeft: false,
            hideNodeRight: true,
            isActiveNodeRight: false,
          }"
          show-count
        >
          <template #childCount>
            <div
              class="text-text-primary text-[11px] w-6 h-6 flex items-center justify-center bg-primary-lighter rounded !border-[1px] !border-primary-lighter"
            >
              {{ componentCreateData.resource.length }}
            </div>
          </template>
        </cf-card-dropdown>
        <div class="flex flex-col mt-2 flex-grow">
          <BaseTabs
            ref="baseTabs"
            v-model="currentTabCreate"
            :tabs="listTabs"
            :show-arrows="true"
            :center-active="true"
            :class-loco="
              isViewMode
                ? 'max-h-[calc(100vh-380px)]'
                : 'max-h-[calc(100vh-399px)]'
            "
          />
        </div>
        <div
          v-if="!isViewMode"
          class="flex justify-end items-center shrink-0 pt-6 gap-2 absolute bottom-3 right-4"
        >
          <BaseButton
            :color="ButtonColorType.Gray"
            @click="openPopupCancel = true"
          >
            {{ $t("product_platform.btn_cancel") }}
          </BaseButton>
          <BaseButton :color="ButtonColorType.Secondary" type="submit">
            <save-icon class="mr-[6px]" />
            {{ $t("product_platform.save") }}
          </BaseButton>
        </div>
      </div>
    </v-form>
    <ComponentCreateEntitySearch v-if="showEntitySearchCreate" />
    <ComponentResourceAdd
      v-if="showResourceAddCreate && !isViewMode"
      :resource-with-component-large-type="componentCreateLargeType"
      :resource-with-component-type="componentCreateType"
      is-add
    />
    <component
      :is="asyncComponent"
      v-if="!showResourceAddCreate"
      class="h-full"
      is-share
      @on-close="handleCloseLoadingComponent"
    />
  </FourColumns>

  <base-popup
    v-model="openPopupCancel"
    :icon="DialogIconType.Warning"
    :submit-button-text="$t('product_platform.btn_yes')"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="$t('product_platform.desc_cancel')"
    @on-submit="handleCancel"
  />
  <base-popup
    v-model="openPopupSave"
    :icon="DialogIconType.Info"
    :submit-button-text="$t('product_platform.btn_yes')"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="$t('product_platform.create_a_new_component')"
    @on-submit="handleSave"
  />
</template>

<style scoped>
:deep().v-window__container {
  display: block !important;
  flex-direction: unset !important;
}
</style>
