<template>
  <v-card class="w-full">
    <v-card-title>
      <div class="left-icon">{{ t("product_platform.custom_validation") }}</div>
      <div class="d-flex gap-2">
        <BaseButton
          :width="WIDTH_BUTTON.EXCEL"
          :color="ButtonColorType.Gray"
          :disabled="downloading"
          @click="onClickDownload"
        >
          <DownloadIcon class="mr-[6px]" />
          {{ t("product_platform.dashboard.download") }}
        </BaseButton>
        <SwitchViewTable
          v-model="viewMode"
          class="ms-auto"
          @update:model-value="handleChangeView"
        />
      </div>
    </v-card-title>
    <div class="card-content">
      <div class="search-section mb-4">
        <div class="left-section">
          <div class="condition">
            <BaseSelectScroll
              v-model="tableConditions.condition"
              :options="[
                {
                  cmcdDetlId: 'C',
                  cmcdDetlNm: t('product_platform.condition'),
                },
                {
                  cmcdDetlId: 'A',
                  cmcdDetlNm: t('product_platform.action'),
                },
              ]"
              placeholder=""
              :default-item-select-all="false"
              class="w-full"
              :required="true"
              :height="48"
            />
          </div>
          <div class="item-type">
            <BaseSelectScroll
              v-model="tableConditions.itemType"
              :options="ITEM_TYPE_FIELDS"
              :placeholder="t('product_platform.Item')"
              class="w-full"
              :height="48"
              @update:model-value="handleChangeCategory"
            />
          </div>
          <div class="type">
            <BaseSelectScroll
              v-model="tableConditions.type"
              :options="tableConditions.listType"
              :placeholder="t('product_platform.type')"
              :show-option-null="false"
              class="catalog-select-filter"
              :height="48"
              @update:model-value="handleChangeType"
            />
          </div>
          <div
            v-if="TARGET_TYPE_CODE.COMPONENT === tableConditions.itemType"
            class="sub-type"
          >
            <BaseSelectScroll
              v-model="tableConditions.subType"
              :options="tableConditions.listSubType"
              :placeholder="t('product_platform.subType')"
              :show-option-null="false"
              :height="48"
              class="catalog-select-filter"
            />
          </div>
          <div class="action-buttons">
            <SearchAndRefreshButton
              @handle-search="handleSearch"
              @handle-refresh="handleResetSearch"
            />
          </div>
        </div>
        <div class="right-section">
          <p>
            {{ t("product_platform.dashboard.searchResult") }}:
            <span class="total-item">{{ totalItems }}</span>
          </p>
        </div>
      </div>
      <div class="content-section">
        <CustomValidationTableData
          :custom-validation-items="tableConditions.customValidationItems"
          :total-items="totalItems"
          :current-page="currentPage"
          :is-loading="isLoading"
          @page-size="updatePageSize"
          @current-page="updateCurrentPage"
        />
      </div>
    </div>
  </v-card>
</template>
<script setup>
import {
  UI_DASHBOARD_GET_MIDDLE_ITEM,
  UI_GET_CUSTOM_VALIDATION,
  UI_GET_CUSTOM_VALIDATION_EXCEL,
} from "@/api/prod/path";
import BaseButton from "@/components/prod/common/BaseButton.vue";
import SearchAndRefreshButton from "@/components/prod/common/SearchAndRefreshButton.vue";
import SwitchViewTable from "@/components/prod/common/SwitchViewTable.vue";
import { VIEW_MODE, WIDTH_BUTTON } from "@/constants/";
import { ButtonColorType } from "@/enums";
import { httpClient } from "@/utils/http-common";
import { useI18n } from "vue-i18n";
import CustomValidationTableData from "./CustomValidationTableData.vue";
import { getComponentSearchType } from "@/api/prod/componentApi";
import customValidationStore from "@/store/admin/customValidation.store";
import { useDownloadFile } from "@/composables/useDownloadFIle";

const emits = defineEmits(["changeView"]);
const viewMode = ref(VIEW_MODE.LIST);

const { locale, t } = useI18n();
const { downloading, downloadFile } = useDownloadFile();
const {
  conditionSearchItem,
  conditionSearchType,
  conditionSearchSubType,
  listItemTypeStore,
  listItemSubTypeStore,
  listComponentTypesStore,
  listCategoryStoreAct,
  isChangeConditionSearch,
} = storeToRefs(customValidationStore());
const { tableConditions } = customValidationStore();

const pageSize = ref(10);
const currentPage = ref(1);
const totalItems = ref(0);
const listComponentTypes = ref([]);
const listCategory = ref([]);
const isMounted = ref(false);
const isLoading = ref(false);

const TARGET_TYPE_CODE = {
  OFFER: "O",
  COMPONENT: "C",
  RESOURCE: "R",
};

const ITEM_TYPE_FIELDS = [
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

const handleChangeView = (value) => {
  emits("changeView", value);
};

const updatePageSize = (value) => {
  pageSize.value = value;
  fetchData();
};

const updateCurrentPage = (value) => {
  currentPage.value = value;
  fetchData();
};

const handleChangeCategory = (category) => {
  tableConditions.type = " ";
  tableConditions.subType = " ";
  if (isMounted.value) {
    getTypeByCategory(category);
  }
};

const handleChangeType = (code) => {
  if (tableConditions.itemType === TARGET_TYPE_CODE.COMPONENT) {
    tableConditions.subType = " ";
    tableConditions.listSubType = listComponentTypes.value
      .filter((item) => item.middleItemCode === code)
      .map((item) => ({
        title: item.itemName,
        value: item.itemCode,
        cmcdDetlId: item.itemCode,
        cmcdDetlNm: item.itemName,
      }));
  }
};

const groupByMiddleItemCode = (data) => {
  const grouped = data.reduce((acc, item) => {
    let group = acc.find((group) => group.value === item.middleItemCode);
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
  }, []);

  return grouped;
};

const getTypeByCategory = async (largeItemCode) => {
  if (largeItemCode === TARGET_TYPE_CODE.COMPONENT) {
    const res = await getComponentSearchType();
    listComponentTypes.value = res.data.data;
    tableConditions.listType = groupByMiddleItemCode(res.data.data);
  } else {
    const params = {};
    if (largeItemCode?.trim().length > 0) {
      params.largeItemCode = largeItemCode;
    }
    const response = await httpClient.get(UI_DASHBOARD_GET_MIDDLE_ITEM, {
      params,
    });
    tableConditions.listType =
      response?.data?.map((item) => ({
        value: item.code,
        title: item.name,
        cmcdDetlId: item.code,
        cmcdDetlNm: item.name,
      })) || [];
  }
};

const fetchData = async () => {
  isLoading.value = true;
  try {
    const params = {
      searchBy: tableConditions.condition,
      item: tableConditions.itemType,
      type: tableConditions.type,
      subType: tableConditions.subType,
      page: currentPage.value,
      size: pageSize.value,
      view: "listview",
    };
    const response = await httpClient.get(UI_GET_CUSTOM_VALIDATION, {
      params: params,
    });

    tableConditions.customValidationItems = response?.data?.data?.map(
      (item, index) => {
        const firstConditionItem =
          item.data.filter((cond) => cond.condType === "C")?.[0] || null;
        const firstActionItem =
          item.data.filter((cond) => cond.condType === "A")?.[0] || null;

        return {
          ...item,
          no: index + 1,
          condition: item.data
            .filter((cond) => cond.condType === "C")
            .map((cond) => ({
              conditionItem: firstConditionItem?.itemCodeName || "",
              conditionAttribute: cond.labelId,
              conditionValidation: cond.attrValue,
            })),
          action: item.data
            .filter((act) => act.condType === "A")
            .map((act) => ({
              actionItem: firstActionItem?.itemCodeName || "",
              actionAttribute: act.labelId,
              actionValidation: act.attrValue,
            })),
          registeredUser: item.rgstUser,
          registeredDate: item.rgstDtm,
          modifiedUser: item.updUser,
          modifiedDate: item.updDtm,
        };
      }
    );
    totalItems.value = response.data.totalItems;
  } catch {
    //
  } finally {
    isLoading.value = false;
  }
};

const handleSearch = () => {
  if (tableConditions.condition === "C") {
    if (
      (tableConditions.itemType &&
        tableConditions.itemType !== "C" &&
        tableConditions.type) ||
      (tableConditions.itemType === "C" &&
        tableConditions.type &&
        tableConditions.subType)
    ) {
      if (
        conditionSearchItem.value?.value !== tableConditions.itemType.trim() ||
        conditionSearchType.value?.value !== tableConditions.type.trim() ||
        conditionSearchSubType.value?.value !== tableConditions.subType.trim()
      ) {
        isChangeConditionSearch.value = true;
      }
      if (
        tableConditions.itemType === "C" &&
        tableConditions.itemType?.trim() &&
        tableConditions.type?.trim() &&
        tableConditions.subType?.trim()
      ) {
        conditionSearchItem.value = ITEM_TYPE_FIELDS.find(
          (item) => item.value === tableConditions.itemType
        );
        conditionSearchType.value = tableConditions.listType.find(
          (item) => item.value === tableConditions.type
        );
        conditionSearchSubType.value = tableConditions.listSubType.find(
          (item) => item.value === tableConditions.subType
        );
        listItemTypeStore.value = tableConditions.listType;
        listItemSubTypeStore.value = tableConditions.listSubType;
        listComponentTypesStore.value = listComponentTypes.value;
      } else if (
        tableConditions.itemType?.trim() &&
        tableConditions.type?.trim()
      ) {
        conditionSearchItem.value = ITEM_TYPE_FIELDS.find(
          (item) => item.value === tableConditions.itemType
        );
        conditionSearchType.value = tableConditions.listType.find(
          (item) => item.value === tableConditions.type
        );
        listItemTypeStore.value = tableConditions.listType;
        listItemSubTypeStore.value = tableConditions.listSubType;
      }

      switch (tableConditions.itemType || "") {
        case TARGET_TYPE_CODE.OFFER:
          listCategory.value = ITEM_TYPE_FIELDS.filter((item) =>
            [TARGET_TYPE_CODE.OFFER, TARGET_TYPE_CODE.COMPONENT].includes(
              item.value
            )
          );
          break;
        case TARGET_TYPE_CODE.RESOURCE:
          listCategory.value = ITEM_TYPE_FIELDS.filter((item) =>
            [TARGET_TYPE_CODE.RESOURCE].includes(item.value)
          );
          break;
        case TARGET_TYPE_CODE.COMPONENT:
          if (conditionSearchType.value?.value === "CH") {
            listCategory.value = ITEM_TYPE_FIELDS.filter((item) =>
              [TARGET_TYPE_CODE.COMPONENT].includes(item.value)
            );
          } else {
            listCategory.value = ITEM_TYPE_FIELDS.filter((item) =>
              [TARGET_TYPE_CODE.RESOURCE, TARGET_TYPE_CODE.COMPONENT].includes(
                item.value
              )
            );
          }
          break;

        default:
          listCategory.value = [];
          break;
      }
      listCategoryStoreAct.value = listCategory.value;
    }
  }
  currentPage.value = 1;
  fetchData();
};

const handleResetSearch = () => {
  tableConditions.itemType = " ";
  tableConditions.type = " ";
  tableConditions.subType = " ";
  tableConditions.listType = [];
  tableConditions.listSubType = [];
  fetchData();
};

const onClickDownload = () => {
  const params = {
    searchBy: tableConditions.condition,
    language: locale.value || "en",
  };
  if (tableConditions.itemType) {
    params.item = tableConditions.itemType;
  }
  if (tableConditions.type) {
    params.type = tableConditions.type;
  }
  if (tableConditions.subType) {
    params.subType = tableConditions.subType;
  }
  downloadFile(
    UI_GET_CUSTOM_VALIDATION_EXCEL,
    params,
    "CustomValidation",
    "xlsx",
    "YYYYMMDD"
  );
};

onBeforeMount(async () => {
  if (
    conditionSearchItem.value?.value !== tableConditions.itemType.trim() ||
    conditionSearchType.value?.value !== tableConditions.type.trim() ||
    conditionSearchSubType.value?.value !== tableConditions.subType.trim()
  ) {
    await getTypeByCategory(conditionSearchItem.value?.value || "");
    tableConditions.listSubType = listComponentTypes.value
      .filter(
        (item) => item.middleItemCode === conditionSearchType.value?.value
      )
      .map((item) => ({
        title: item.itemName,
        value: item.itemCode,
        cmcdDetlId: item.itemCode,
        cmcdDetlNm: item.itemName,
      }));
    tableConditions.itemType = conditionSearchItem.value?.value || " ";
    tableConditions.type = conditionSearchType.value?.value || " ";
    tableConditions.subType = conditionSearchSubType.value?.value || " ";
    currentPage.value = 1;
    fetchData();
  }
});

onMounted(() => {
  isMounted.value = true;
});
</script>
<style scoped lang="scss">
.v-card-title {
  padding: 24px 24px 0;
  font-family: "Noto Sans KR";
  font-size: 16px;
  font-weight: 500;
  line-height: 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #3a3b3d;
  .left-icon {
    display: flex;
    align-items: center;
    > svg {
      margin-right: 8px;
      width: 24px;
      height: 24px;
    }
  }
}
.card-sub-title {
  padding: 6px 24px 0;
  font-size: 13px;
  color: #6b6d70;
  font-family: "Noto Sans KR";
}
.card-content {
  padding: 24px 24px 10px;
  .search-section {
    width: 100%;
    display: flex;
    justify-content: space-between;
    .left-section {
      display: flex;
      align-items: center;
      .condition {
        width: 140px;
        margin-right: 8px;
      }
      .item-type {
        width: 140px;
        margin-right: 8px;
      }
      .type {
        width: 140px;
        margin-right: 8px;
      }
      .sub-type {
        width: 140px;
        margin-right: 8px;
      }
      .action-buttons {
        margin-left: 8px;
      }
    }
    .right-section {
      display: flex;
      width: 100%;
      font-family: "Noto Sans KR";
      font-size: 13px;
      font-weight: 500;
      justify-content: flex-end;
      align-items: flex-end;
      color: #6b6d70;
      .total-item {
        color: #3a3b3d;
      }
    }
  }
  .content-section {
    height: calc(100vh - 316px);
    display: flex;
    align-items: center;
  }
}
</style>
