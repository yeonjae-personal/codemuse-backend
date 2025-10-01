<template>
  <div class="bg-white relative pt-6 pb-3 rounded-lg">
    <div class="flex flex-col h-full">
      <v-form ref="form" class="pl-6 pr-6">
        <div class="flex justify-between items-center">
          <div class="flex align-center gap-2 items-end">
            <h1 class="font-medium text-base text-text-base tracking-[0.5px]">
              {{ $t("product_platform.condition_search") }}
            </h1>
          </div>

          <div class="flex">
            <SearchAndRefreshButton
              @handle-search="handleSearch"
              @handle-refresh="handleResetSearch"
            />
          </div>
        </div>

        <!-- Filter -->
        <div class="filter mt-2 gap-2">
          <div class="grid grid-cols-2 gap-x-2">
            <BaseSelectScroll
              v-model="conditionItem"
              :options="CATEGORY_FIELDS"
              :placeholder="$t(`product_platform.Item`)"
              :default-item-select-all="false"
              :required="true"
              :height="48"
              @update:model-value="handleChangeCategory"
            />
            <BaseSelectScroll
              v-model="conditionType"
              :options="listItemType"
              :default-item-select-all="false"
              :placeholder="$t(`product_platform.Type`)"
              :required="true"
              :height="48"
              @update:model-value="handleChangeConditionType"
            />
          </div>

          <div class="grid grid-cols-1">
            <BaseSelectScroll
              v-if="conditionItem === 'C'"
              v-model="conditionSubType"
              :options="listItemSubType"
              :default-item-select-all="false"
              :placeholder="$t(`product_platform.subType`)"
              :required="true"
              :height="48"
            />
          </div>
        </div>
      </v-form>
      <LocomotiveComponent
        scroll-container-class="result-wrapper h-full"
        scroll-content-class="h-full"
        dynamic-scroll-key="VALIDATION_CONDITION_SEARCH"
        is-dynamic-scroll
      >
        <div v-if="conditionAttributes.length > 0">
          <!-- General -->
          <p class="list-title">{{ $t("product_platform.general") }}</p>
          <NoData v-if="isClickSearch && listGeneralAttr.length === 0" />
          <div
            v-else
            id="condition-search-result"
            ref="resultRef"
            class="result"
          >
            <AttributeItem
              v-for="item in listGeneralAttr"
              :key="item.id"
              :item="item"
              :show-selected="
                selectedAttribute?.type === 'condition' &&
                item.id === selectedAttribute?.attrId
              "
              @click-item="handleSelectedItem"
            />
          </div>

          <!-- Additional -->
          <p class="list-title mt-6">{{ $t("product_platform.additional") }}</p>
          <NoData v-if="isClickSearch && listAdditionalAttr.length === 0" />
          <div
            v-else
            id="condition-search-result"
            ref="resultRef"
            class="result"
          >
            <AttributeItem
              v-for="item in listAdditionalAttr"
              :key="item.id"
              :item="item"
              :show-selected="
                selectedAttribute?.type === 'condition' &&
                item.id === selectedAttribute?.attrId
              "
              @click-item="handleSelectedItem"
            />
          </div>
        </div>

        <!-- NoData -->
        <NoData
          v-if="
            (isEmptyCondition || isClickSearch) &&
            conditionAttributes.length === 0
          "
        />
      </LocomotiveComponent>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";
import uniqBy from "lodash-es/uniqBy";
import { getComponentSearchType } from "@/api/prod/componentApi";
import { getValidationActionStructure } from "@/api/prod/customValidationApi";
import {
  UI_DASHBOARD_GET_MIDDLE_ITEM,
  UI_GET_CUSTOM_VALIDATION,
} from "@/api/prod/path";
import { useSnackbarStore } from "@/store";
import customValidationStore from "@/store/admin/customValidation.store";
import { Item } from "@/types/catalog/component/ComponentSearch";
import { httpClient } from "@/utils/http-common";
import AttributeItem from "./AttributeItem.vue";
import NoData from "@/components/prod/common/NoData.vue";
import { DisplayAttributeTab } from "@/enums/customValidation";
import type { ValidationActionStructureParams } from "@/interfaces/prod/custom-validation";
import BaseSelectScroll from "@/components/prod/common/BaseSelectScroll.vue";

const {
  conditionSearchItem,
  conditionSearchType,
  conditionSearchSubType,
  selectedAttribute,
  listItemTypeStore,
  listItemSubTypeStore,
  listComponentTypesStore,
  listCategoryStoreAct,
  listItemTypeStoreAct,
  listItemSubTypeStoreAct,
  conditionAttributes,
  conditionSearchItemAct,
  conditionSearchTypeAct,
  conditionSearchSubTypeAct,
  isEmptyCondition,
  isEmptyAction,
  isChangeConditionSearch,
} = storeToRefs(customValidationStore());
const {
  transformToCustomValidationItem,
  updateSelectedAttributeItem,
  resetCustomValidationStore,
  selectedCustomValidationItem,
  getTypeOfAttribute,
  updateShowHistory,
  transformToAttributeItem,
} = customValidationStore();
const { showSnackbar } = useSnackbarStore();
const { t } = useI18n();

const TARGET_TYPE_CODE = {
  OFFER: "O",
  COMPONENT: "C",
  RESOURCE: "R",
  GROUP: "G",
};

const CATEGORY_FIELDS = [
  {
    value: TARGET_TYPE_CODE.OFFER,
    title: "Offer",
    cmcdDetlId: TARGET_TYPE_CODE.OFFER,
    cmcdDetlNm: "Offer",
  },
  {
    value: TARGET_TYPE_CODE.COMPONENT,
    title: "Component",
    cmcdDetlId: TARGET_TYPE_CODE.COMPONENT,
    cmcdDetlNm: "Component",
  },
  {
    value: TARGET_TYPE_CODE.RESOURCE,
    title: "Resource",
    cmcdDetlId: TARGET_TYPE_CODE.RESOURCE,
    cmcdDetlNm: "Resource",
  },
];

const listItemType = ref<{ title: string; value: string }[]>(
  listItemTypeStore.value
);
const listItemSubType = ref<{ title: string; value: string }[]>(
  listItemSubTypeStore.value
);
const listComponentTypes = ref<Item[]>(listComponentTypesStore.value);
const conditionItem = ref(conditionSearchItem.value?.value || "");
const conditionType = ref(conditionSearchType.value?.value || "");
const conditionSubType = ref(conditionSearchSubType.value?.value || "");
const resultRef = ref();
const isClickSearch = ref<boolean>(false);

const listAdditionalAttr = computed(() =>
  conditionAttributes.value
    .filter((condition) => condition.dispTab === DisplayAttributeTab.Additional)
    .map((item) => {
      const types = getTypeOfAttribute(item.id);
      return {
        ...item,
        condition: types.includes("C"),
        action: types.includes("A"),
      };
    })
);

const listGeneralAttr = computed(() =>
  conditionAttributes.value
    .filter((condition) => condition.dispTab === DisplayAttributeTab.General)
    .map((item) => {
      const types = getTypeOfAttribute(item.id);
      return {
        ...item,
        condition: types.includes("C"),
        action: types.includes("A"),
      };
    })
);

const handleSelectedItem = (id: string): void => {
  updateSelectedAttributeItem(id, "condition");
};

const addSubType = (parentCode: string, types: any[]): void => {
  types.forEach((type) => {
    listItemSubTypeStoreAct.value.push({
      value: type.itemCode,
      title: type.itemName,
      cmcdDetlId: type.itemCode,
      cmcdDetlNm: type.itemName,
      parentCode,
    });
  });
};

const processComponentItems = (parent: any): void => {
  parent.componentItem.forEach((component) => {
    listItemTypeStoreAct.value.push({
      value: component.itemCode,
      title: component.itemName,
      cmcdDetlId: component.itemCode,
      cmcdDetlNm: component.itemName,
      parentCode: parent.largeItemCode,
    });

    if (component.types?.length > 0) {
      addSubType(component.itemCode, component.types);
    }
  });
};

const transformAllOptions = (data: any[]): void => {
  data.forEach((item) => {
    if (item.largeItemCode && item.largeItemName) {
      listCategoryStoreAct.value.push({
        value: item.largeItemCode,
        title: item.largeItemName,
        cmcdDetlId: item.largeItemCode,
        cmcdDetlNm: item.largeItemName,
      });
    }

    switch (item.largeItemCode) {
      case "C":
        if (item.componentItem?.length > 0) {
          processComponentItems(item);
        } else {
          if (item.middleItemCode && item.middleItemName) {
            listItemTypeStoreAct.value.push({
              value: item.middleItemCode,
              title: item.middleItemName,
              cmcdDetlId: item.middleItemCode,
              cmcdDetlNm: item.middleItemName,
              parentCode: item.largeItemCode,
            });
          }
          if (item.itemCode && item.itemName) {
            listItemSubTypeStoreAct.value.push({
              value: item.itemCode,
              title: item.itemName,
              cmcdDetlId: item.itemCode,
              cmcdDetlNm: item.itemName,
              parentCode: item.middleItemCode,
            });
          }
        }
        break;

      default:
        if (item.itemCode && item.itemName) {
          listItemTypeStoreAct.value.push({
            value: item.itemCode,
            title: item.itemName,
            cmcdDetlId: item.itemCode,
            cmcdDetlNm: item.itemName,
            parentCode: item.largeItemCode,
          });
        }
        if (item.types?.length > 0) {
          item.types.forEach((type) => {
            listItemTypeStoreAct.value.push({
              value: type.itemCode,
              title: type.itemName,
              cmcdDetlId: type.itemCode,
              cmcdDetlNm: type.itemName,
              parentCode: item.largeItemCode,
            });
          });
        }
        break;
    }
  });

  listCategoryStoreAct.value = uniqBy(listCategoryStoreAct.value, "value");
  listItemTypeStoreAct.value = uniqBy(listItemTypeStoreAct.value, "value");
  listItemSubTypeStoreAct.value = uniqBy(
    listItemSubTypeStoreAct.value,
    "value"
  );
};

const handleResetActionOption = (): void => {
  listCategoryStoreAct.value = [];
  listItemTypeStoreAct.value = [];
  listItemSubTypeStoreAct.value = [];
  conditionSearchItemAct.value = "";
  conditionSearchTypeAct.value = "";
  conditionSearchSubTypeAct.value = "";
};

const getListValidationAction = async (): Promise<void> => {
  try {
    const params: ValidationActionStructureParams = {
      item: conditionItem.value,
      type: conditionType.value,
      subType: conditionSubType.value,
    };
    handleResetActionOption();
    const response = await getValidationActionStructure(params);
    transformAllOptions(response.data);
  } catch {
    handleResetActionOption();
    showSnackbar(t("product_platform.internalServerError"), "error");
  }
};

const getListAttributes = async (item, type, action, subType) => {
  try {
    const response = await httpClient.get(UI_GET_CUSTOM_VALIDATION, {
      params: {
        item,
        type,
        action,
        subType,
        view: "gridview",
      },
    });
    transformToAttributeItem(response.data.attrConditions, action);
    transformToCustomValidationItem(response.data.validations);
    isEmptyCondition.value = response.data.attrConditions.length === 0;
    isEmptyAction.value = false;
  } catch {
    transformToAttributeItem([], action);
    transformToCustomValidationItem([]);
    showSnackbar(t("product_platform.internalServerError"), "error");
  } finally {
    isClickSearch.value = true;
  }
};

const groupByMiddleItemCode = (
  data: Item[]
): {
  title: string;
  value: string;
  cmcdDetlId: string;
  cmcdDetlNm: string;
}[] => {
  const grouped = data.reduce(
    (acc, item) => {
      let group = acc.find(({ value }) => value === item.middleItemCode);
      if (!group) {
        group = {
          title: item.middleItemName,
          value: item.middleItemCode,
          cmcdDetlId: item.middleItemCode,
          cmcdDetlNm: item.middleItemName,
        };
        acc.push(group);
      }
      return acc;
    },
    [] as {
      title: string;
      value: string;
      cmcdDetlId: string;
      cmcdDetlNm: string;
    }[]
  );

  return grouped;
};

const getTypeByCategory = async (largeItemCode) => {
  if (largeItemCode === TARGET_TYPE_CODE.COMPONENT) {
    const res = await getComponentSearchType();
    listComponentTypes.value = res.data || [];
    listItemType.value = groupByMiddleItemCode(res.data || []);
    listComponentTypesStore.value = res.data || [];
  } else {
    const params = {} as any;
    if (largeItemCode.trim().length > 0) {
      params.largeItemCode = largeItemCode;
    }
    const response = await httpClient.get(UI_DASHBOARD_GET_MIDDLE_ITEM, {
      params,
    });
    listItemType.value =
      response?.data?.map((item) => ({
        value: item.code,
        title: item.name,
        cmcdDetlId: item.code,
        cmcdDetlNm: item.name,
      })) || [];
  }
};

const handleChangeCategory = (category) => {
  conditionType.value = "";
  conditionSubType.value = "";
  listItemType.value = [];
  listItemSubType.value = [];
  getTypeByCategory(category);
};

const handleSearch = () => {
  if (
    conditionItem.value === "" ||
    conditionType.value === "" ||
    (conditionSubType.value === "" && conditionItem.value === "C")
  ) {
    showSnackbar(t("product_platform.required_field_missing"), "error");
    return false;
  }
  conditionSearchItem.value = CATEGORY_FIELDS.find(
    (item) => item.value === conditionItem.value
  );
  conditionSearchType.value = listItemType.value.find(
    (item) => item.value === conditionType.value
  );
  conditionSearchSubType.value = listItemSubType.value.find(
    (item) => item.value === conditionSubType.value
  );
  listItemTypeStore.value = listItemType.value;
  listItemSubTypeStore.value = listItemSubType.value;

  getListAttributes(
    conditionItem.value,
    conditionType.value,
    "condition",
    conditionSubType.value
  );
  updateShowHistory(false);
  getListValidationAction();
};

const handleResetSearch = () => {
  conditionItem.value = "";
  conditionType.value = "";
  conditionSubType.value = "";
  conditionSearchItem.value = undefined;
  conditionSearchType.value = undefined;
  conditionSearchSubType.value = undefined;
  listItemType.value = [];
  listItemSubType.value = [];
  isClickSearch.value = false;
  handleResetActionOption();
  resetCustomValidationStore();
};

const handleChangeConditionType = (code: string) => {
  if (conditionItem.value === TARGET_TYPE_CODE.COMPONENT) {
    conditionSubType.value = "";
    listItemSubType.value = listComponentTypes.value
      .filter((item) => item.middleItemCode === code)
      .map((item) => ({
        title: item.itemName,
        value: item.itemCode,
        cmcdDetlId: item.itemCode,
        cmcdDetlNm: item.itemName,
      }));
  }
};

const onClickOutside = (callback: (value: string) => void) => {
  const condition = document.getElementById("#condition-search-result");
  const action = document.getElementById("#action-search-result");
  const listCustomValidation = document.getElementById(
    "#list-custom-validation"
  );
  if (condition && action) {
    document.addEventListener("click", (event: any) => {
      if (!condition.contains(event.target) && !action.contains(event.target)) {
        callback("attribute");
      } else if (!listCustomValidation?.contains(event.target)) {
        callback("custom-validation");
      }
    });
  }
};

onMounted(() => {
  // Change by list result
  onClickOutside((type: string) => {
    if (type === "attribute") {
      handleSelectedItem("");
    } else {
      selectedCustomValidationItem("");
    }
  });
  if (isChangeConditionSearch.value) {
    handleSearch();
    isChangeConditionSearch.value = false;
  }
});
</script>

<style lang="scss" scoped>
.filter {
  width: 100%;
  display: grid;
}

.result-wrapper {
  padding: 0 24px 0 24px;
  font-family: "Noto Sans KR";
  margin-top: 32px;

  .list-title {
    font-size: 13px;
    font-weight: 500;
    margin-bottom: 12px;
    color: #3a3b3d;
  }

  .result {
    display: flex;
    flex-direction: column;
    row-gap: 12px;
    padding: 0 0 5px;
  }
}
</style>
