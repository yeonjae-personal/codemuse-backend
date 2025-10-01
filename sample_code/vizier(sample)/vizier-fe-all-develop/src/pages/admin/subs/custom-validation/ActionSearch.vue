<template>
  <div class="bg-white relative pt-6 pb-3 rounded-lg">
    <div class="flex flex-col h-full">
      <v-form ref="form" class="pl-6 pr-6">
        <div class="flex justify-between items-center">
          <div class="flex align-center gap-2 items-end">
            <h1 class="font-medium text-base text-text-base tracking-[0.5px]">
              {{ $t("product_platform.action_search") }}
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
              :options="listCategoryStoreAct"
              :placeholder="$t(`product_platform.Item`)"
              :default-item-select-all="false"
              :required="true"
              :height="48"
              @update:model-value="handleChangeCategory"
            />
            <BaseSelectScroll
              v-model="conditionType"
              :options="actionTypeOptions"
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
              :options="actionSubTypeOptions"
              :default-item-select-all="false"
              :placeholder="$t(`product_platform.subType`)"
              :height="48"
              :required="true"
            />
          </div>
        </div>
      </v-form>
      <LocomotiveComponent
        scroll-container-class="result-wrapper h-full"
        scroll-content-class="h-full"
        dynamic-scroll-key="VALIDATION_ACTION_SEARCH"
        is-dynamic-scroll
      >
        <div v-if="actionAttributes.length > 0">
          <!-- General -->
          <p class="list-title">{{ $t(`product_platform.general`) }}</p>
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
                selectedAttribute?.type === 'action' &&
                item.id === selectedAttribute?.attrId
              "
              @click-item="handleSelectedItem"
            />
          </div>

          <!-- Additional -->
          <p class="list-title mt-6">
            {{ $t(`product_platform.additional`) }}
          </p>
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
                selectedAttribute?.type === 'action' &&
                item.id === selectedAttribute?.attrId
              "
              @click-item="handleSelectedItem"
            />
          </div>
        </div>

        <!-- NoData -->
        <NoData
          v-if="
            (isClickSearch || isEmptyAction) && actionAttributes.length === 0
          "
        />
      </LocomotiveComponent>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";
import get from "lodash-es/get";
import { UI_GET_CUSTOM_VALIDATION } from "@/api/prod/path";
import { useSnackbarStore } from "@/store";
import customValidationStore from "@/store/admin/customValidation.store";
import { httpClient } from "@/utils/http-common";
import AttributeItem from "./AttributeItem.vue";
import { DisplayAttributeTab } from "@/enums/customValidation";
import BaseSelectScroll from "@/components/prod/common/BaseSelectScroll.vue";

const {
  updateSelectedAttributeItem,
  getTypeOfAttribute,
  transformToAttributeItem,
} = customValidationStore();
const {
  selectedAttribute,
  conditionSearchItemAct,
  conditionSearchTypeAct,
  conditionSearchSubTypeAct,
  listCategoryStoreAct,
  listItemTypeStoreAct,
  listItemSubTypeStoreAct,
  actionAttributes,
  isEmptyAction,
} = storeToRefs(customValidationStore());
const { showSnackbar } = useSnackbarStore();
const { t } = useI18n();

const conditionItem = ref<string>(get(conditionSearchItemAct, "value"));
const conditionType = ref<string>(get(conditionSearchTypeAct, "value"));
const conditionSubType = ref<string>(get(conditionSearchSubTypeAct, "value"));
const isClickSearch = ref<boolean>(false);
const resultRef = ref();

const actionTypeOptions = computed(() => {
  if (!conditionItem.value) return [];
  return listItemTypeStoreAct.value.filter(
    ({ parentCode }) => parentCode === conditionItem.value
  );
});

const actionSubTypeOptions = computed(() => {
  if (!conditionType.value) return [];
  return listItemSubTypeStoreAct.value.filter(
    ({ parentCode }) => parentCode === conditionType.value
  );
});

const listAdditionalAttr = computed(() =>
  actionAttributes.value
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
  actionAttributes.value
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
  updateSelectedAttributeItem(id, "action");
};

const getListAttributes = async (item, type, action, subType) => {
  try {
    const response = await httpClient.get(UI_GET_CUSTOM_VALIDATION, {
      params: { item, type, action, subType, view: "gridview" },
    });
    transformToAttributeItem(response.data.attrActions, action);
    isEmptyAction.value = response.data.attrActions.length === 0;
  } catch {
    transformToAttributeItem([], action);
    showSnackbar(t("product_platform.internalServerError"), "error");
  } finally {
    isClickSearch.value = true;
  }
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
  conditionSearchItemAct.value = conditionItem.value;
  conditionSearchTypeAct.value = conditionType.value;
  conditionSearchSubTypeAct.value = conditionSubType.value;
  getListAttributes(
    conditionItem.value,
    conditionType.value,
    "action",
    conditionSubType.value
  );
};

const handleResetSearch = () => {
  conditionItem.value = "";
  conditionType.value = "";
  conditionSubType.value = "";
  isClickSearch.value = false;
  transformToAttributeItem([], "action");
};

const handleChangeCategory = () => {
  conditionType.value = "";
  conditionSubType.value = "";
};

const handleChangeConditionType = () => {
  if (conditionItem.value === "C") {
    conditionSubType.value = "";
  }
};

watch(
  listCategoryStoreAct,
  (_value) => {
    handleResetSearch();
  },
  { deep: true }
);
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
    color: #6b6d70;
  }
  .result {
    display: flex;
    flex-direction: column;
    row-gap: 12px;
    padding: 0 0 5px;
  }
}
</style>
