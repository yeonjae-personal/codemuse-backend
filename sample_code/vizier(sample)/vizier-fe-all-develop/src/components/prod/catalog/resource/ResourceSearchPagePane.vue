<template>
  <SearchPane
    v-if="isGridMode"
    :key="componentKey"
    ref="searchPaneRef"
    title="product_platform.resource_search"
    :title-append="titleAppend"
    :container-class="containerClass"
    :model-list="listResourceSearch?.items || []"
    :pagination="listResourceSearch?.pagination"
    :pane-col="showComponentSearch ? ColNumber.One : ColNumber.Two"
    :show-float-icon-left="showComponentSearch && isGridMode"
    :show-float-icon-right="!showComponentSearch && isGridMode"
    :open-popup="isShowPopup"
    :popup-content="popupContent"
    :item-height="62"
    @on-search="handleSearch"
    @on-reset="handleResetSearch(true)"
    @on-click-float-left="closeComponentSearch"
    @on-click-float-right="openComponentSearch"
    @close-popup="closePopupSave"
    @submit-popup="handleNewResource"
    @on-change-page="handleChangePage"
  >
    <template #search-button-append>
      <SearchDetailButton
        v-if="!isComponentGroupSearch && !showComponentSearch"
        class="bg-white ml-2"
        :is-active="isApplied"
        :disabled="isDisableAdvancedButton"
        @click="handleSearchDetail"
      />
      <BaseButton
        v-if="!showComponentSearch && isGridMode"
        class="ml-2"
        :color="ButtonColorType.Secondary"
        :width="WIDTH_BUTTON.AUTO"
        @click="toggleMode"
      >
        <SearchIcon fill="#BA1642" class="mr-[6px]" />

        {{
          isComponentGroupSearch
            ? $t("product_platform.without_component")
            : $t("product_platform.search_with_component")
        }}
      </BaseButton>
      <div
        v-if="
          (!isComponentGroupSearch && resourceList?.length > 0) || !isGridMode
        "
        class="h-10 ml-2 py-4 flex items-center justify-center"
      >
        <SwitchViewTable
          v-model="viewMode"
          class="ms-auto"
          @update:model-value="handleChangeViewMode"
        />
      </div>
    </template>
    <template #custom-form="{ pageSize }">
      <div
        :class="[
          'gap-2 grid w-full',
          showComponentSearch
            ? 'grid-flow-row grid-rows-2 mt-2'
            : isGridMode
              ? 'grid-flow-col grid-cols-2 mt-2'
              : 'grid-cols-2 order-1',
        ]"
      >
        <BaseSelectScroll
          ref="selectScroll"
          v-model="resourceParamsFilter.itemCode"
          :options="optionsType || []"
          :placeholder="$t('product_platform.type')"
          :default-item-select-all="false"
          :rules="[{ required: isRequiredType() }]"
          :height="48"
          :required="isRequiredType()"
        />
        <div class="grid grid-cols-[1fr_2fr] gap-2">
          <BaseSelectScroll
            v-model="resourceParamsFilter.resourceType"
            :options="optionsSearchType || []"
            :height="48"
          />
          <div class="flex items-center gap-2">
            <BaseInputSearch
              v-model="resourceParamsFilter.keyword"
              label="search"
              density="comfortable"
              hide-details
              rounded="4"
              single-line
              variant="solo"
              @keyup.enter="() => handleSearch(pageSize)"
            />
          </div>
        </div>
      </div>
    </template>
    <template #custom-search-item="{ item }">
      <cf-card-dropdown
        :key="item.objUuid"
        :title="item.objName"
        :description="item.objCode"
        class-name="card-round-style"
        type-bg="light"
        border-color-action="purple"
        :active="item.objUuid === resourceSelected?.objUuid"
        :search-text="resourceParamsFilter.keyword"
        :search-field="
          resourceParamsFilter.resourceType === 'prodItemCd'
            ? SearchBy.Code
            : SearchBy.Name
        "
        :node="{
          hideNodeLeft: true,
          isActiveNodeLeft: false,
          hideNodeRight: true,
          isActiveNodeRight: false,
        }"
        :disable="checkExpired(item)"
        :actions="listActions(item)"
        show-icon-status
        editable
        hide-detail
        draggable
        @on-click-card="handleSelectResource(item)"
        @dragstart="
          handleDragUserPocket($event, {
            userPocketType: LargeItemCode.Resource,
            ...item,
          })
        "
      >
        <template #icon>
          <span class="flex justify-center align-center w-[40px] h-[40px]">
            <template v-if="item.itemCode === ResourceType.RatingElement">
              <RLinearIcon />
            </template>
            <template v-if="item.itemCode === ResourceType.BillingElement">
              <BLinearIcon />
            </template>
            <template v-if="item.itemCode === ResourceType.ServiceElement">
              <SLinearIcon />
            </template>
          </span>
        </template>
      </cf-card-dropdown>
    </template>
  </SearchPane>
  <TablePane
    v-else
    :pane-col="ColNumber.Four"
    :view-mode="viewMode"
    :downloading="downloading"
    @update:view-mode="handleChangeViewMode"
    @on-search="handleSearch"
    @on-reset="handleResetSearch(true)"
    @on-change-page="handleChangePage"
    @on-download="handleDownloadDataTable"
  >
    <template #custom-form>
      <div
        :class="[
          'gap-2 grid w-full',
          showComponentSearch
            ? 'grid-flow-row grid-rows-2 mt-2'
            : isGridMode
              ? 'grid-flow-col grid-cols-2 mt-2'
              : 'grid-cols-2 order-1',
        ]"
      >
        <BaseSelectScroll
          ref="selectScroll"
          v-model="resourceParamsFilter.itemCode"
          :options="optionsType || []"
          :placeholder="$t('product_platform.type')"
          :default-item-select-all="false"
          :rules="[{ required: isRequiredType() }]"
          :height="48"
          :required="isRequiredType()"
        />
        <div class="grid grid-cols-[1fr_2fr] gap-2">
          <BaseSelectScroll
            v-model="resourceParamsFilter.resourceType"
            :options="optionsSearchType || []"
            :height="48"
          />
          <div class="flex items-center gap-2">
            <BaseInputSearch
              v-model="resourceParamsFilter.keyword"
              label="search"
              density="comfortable"
              hide-details
              rounded="4"
              single-line
              variant="solo"
              @keyup.enter="handleSearch"
            />
          </div>
        </div>
      </div>
    </template>
    <template #custom-search-main-content>
      <div class="w-full h-full px-6">
        <DataTable
          :key="dataTableKey"
          v-model:pageSize="resourceParamsFilter.pageSize"
          :headers="headerColumns"
          :data="dataTable"
          :table-class-name="'h-[calc(100vh-284px)]'"
          :pagination="{
            currentPage: resourceParamsFilter.page,
            totalPages: Math.ceil(totalSearchItems / resourceParamsFilter.size),
            pageSize: resourceParamsFilter.size,
          }"
          :total-search-items="totalSearchItems"
          :disable-change="isLoading"
          is-dynamic-table
          @on-change-page="handleChangePage"
          @on-change-size="handleChangeSize"
        >
          <template #[`LB00000136`]="{ item, width, align }">
            <div :style="{ width: width, textAlign: align }" class="p-4">
              <CustomTooltip :content="item?.LB00000136">
                <span
                  v-html="
                    resourceParamsFilter.resourceType ===
                    optionsSearchType[1].value
                      ? highlightText(
                          item?.LB00000136 || '',
                          resourceParamsFilter.keyword
                        )
                      : item?.LB00000136
                  "
                />
              </CustomTooltip>
            </div>
          </template>
          <template #[`LB00000137`]="{ item, width, align }">
            <div :style="{ width: width, textAlign: align }" class="p-4">
              <CustomTooltip :content="item?.LB00000137">
                <span
                  v-html="
                    resourceParamsFilter.resourceType ===
                    optionsSearchType[0].value
                      ? highlightText(
                          item?.LB00000137 || '',
                          resourceParamsFilter.keyword
                        )
                      : item?.LB00000137
                  "
                />
              </CustomTooltip>
            </div>
          </template>
        </DataTable>
      </div>
    </template>
  </TablePane>
  <AdvancedSearch
    v-if="openSearchDetail"
    v-model="openSearchDetail"
    :type="localSelectType"
    :model-list="advencedSearchList"
    class-custom="search-detail-resource"
    @on-close="closePopupSearchDetail"
    @on-submit="applyPopupSearchDetail"
    @on-reset="handleResetSearchDetail"
  />
</template>

<script lang="ts" setup>
import { useResourceStore, useSnackbarStore } from "@/store";
import { IResourceItem } from "@/interfaces/prod/resource";
import { ActionType } from "@/interfaces/prod";
import { ButtonColorType, ColNumber, LargeItemCode, SearchBy } from "@/enums";
import moment from "moment-timezone";
import {
  DATE_FORMAT,
  DEFAULT_PAGINATION,
  VIEW_MODE,
  WIDTH_BUTTON,
} from "@/constants/";
import cloneDeep from "lodash-es/cloneDeep";
import { useI18n } from "vue-i18n";
import {
  filterParamsAdvanced,
  formatDateWithOutSeconds,
  highlightText,
  isExpired,
} from "@/utils/format-data";
import DuplicateIcon from "@/components/prod/icons/DuplicateIcon.vue";
import {
  PRODUCT_ITEM_NAME,
  PRODUCT_ITEM_CODE,
  PARAMS_DEFAULT_COMPONENT,
} from "@/constants/resource";
import { getListItemCodeApi } from "@/api/prod/commonApi";
import { ResourceType } from "@/enums/component";
import { GROUP_DETAIL_CATEFORY } from "@/constants/extendsManager";
import { useDownloadFile } from "@/composables/useDownloadFIle";
import { EXPORT_RESOURCE_EXCEL_PATH } from "@/api/prod/path";
import DataTable from "@/components/bulk-upload/DataTable.vue";
import { TableHeader } from "@/types/common";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import { useGroupCode } from "@/composables/useGroupCode";
import useDragUserPocket from "@/composables/useDragUserPocket";
import SearchPane from "../../shared/SearchPane.vue";

const props = defineProps({
  refreshFlag: {
    type: Boolean,
    default: false,
  },
});

const { t, locale } = useI18n();

const { downloading, downloadFile } = useDownloadFile();
const { showSnackbar } = useSnackbarStore();
const { groupCodeData, search, getTextDisplay } = useGroupCode();
const {
  resetFilter,
  getResourceList,
  getResourceAdvancedList,
  getDataResourceListView,
  resetUpdateListEntity,
  resetResourceFilter,
  setAdvencedSearchList,
} = useResourceStore();
const {
  showDetail,
  isEdit,
  isDuplicate,
  resourceSelected,
  showResourceDuplicate,
  showComponentSearch,
  resourceCode,
  componentSelected,
  listResourceSearch,
  showEntitySearch,
  resourceParamsFilter,
  resourceAdvencedParams,
  advencedSearchList,
  isComponentGroupSearch,
  isResetValue,
  componentSearch,
  paramsFilterComponentGroup,
  viewMode,
} = storeToRefs(useResourceStore());
const { handleDragUserPocket } = useDragUserPocket();

const searchPaneRef = ref<InstanceType<typeof SearchPane>>();
const localSelectType = ref<string>("");
const newResourceClick = ref<any>(null);
const isShowPopup = ref<boolean>(false);
const popupContent = ref<string>("");
const selectScroll = ref();
const isApplied = ref<any>(false);
const optionsType = ref<any[]>([]);
const openSearchDetail = ref(false);
const currentAction = ref("");
const listCommonCode = ref<string[]>([]);
const isLoading = ref(false);
const dataTableKey = ref(Math.random());
const componentKey = ref<number>(0);

const titleAppend = computed(() => {
  return showComponentSearch.value
    ? ""
    : !isComponentGroupSearch.value
      ? "product_platform.resource_pool"
      : "product_platform.with_component_mode";
});

const containerClass = computed<string>(() =>
  showComponentSearch.value
    ? "rounded-r-lg border-l-[#E6E9ED] border-l-[1px]"
    : "rounded-lg"
);

const isGridMode = computed<boolean>(() => viewMode.value === VIEW_MODE.GRID);

const isDisableAdvancedButton = computed<boolean>(
  () => !resourceParamsFilter.value.itemCode?.trim()
);

const headerColumns = computed<TableHeader[]>(() => {
  const columns: TableHeader[] = [
    {
      title: t("product_platform.no"),
      key: "no",
      width: "80px",
    },
  ];
  const generalCols =
    listResourceSearch.value?.items?.[0]?.general?.filter(
      (item) => item.fieldTypeCode !== COLUMN_FIELD_TYPE.HD
    ) || [];
  const additionalCols = listResourceSearch.value?.items?.[0]?.additional || [];
  const result = [...generalCols, ...additionalCols].map((item) => {
    if (
      [COLUMN_FIELD_TYPE.DL, COLUMN_FIELD_TYPE.DM].includes(
        item.fieldTypeCode
      ) &&
      item?.commGroupCode
    ) {
      listCommonCode.value.push(item.commGroupCode);
    }
    return {
      title: t(item.labelId),
      sortable: false,
      key: item.labelId,
      width: "160px",
    };
  });
  return [...columns, ...result];
});

const dataTable = computed(() => {
  const rowsData = listResourceSearch.value?.items?.map((row, index) => {
    const pagination = listResourceSearch.value.pagination;
    const columns: any = [];
    columns["no"] =
      index + 1 + (pagination.currentPage - 1) * pagination.pageSize;
    const generalData =
      row?.general?.filter(
        (item) => item.fieldTypeCode !== COLUMN_FIELD_TYPE.HD
      ) || [];
    const additionalData = row?.additional || [];
    [...generalData, ...additionalData].forEach((item) => {
      if (
        [COLUMN_FIELD_TYPE.DL, COLUMN_FIELD_TYPE.DM].includes(
          item.fieldTypeCode
        ) &&
        groupCodeData.value &&
        groupCodeData.value[item.commGroupCode]?.length
      ) {
        columns[item.labelId] = getTextDisplay(
          item.attrVal,
          item.fieldTypeCode,
          groupCodeData?.value[item?.commGroupCode] || []
        );
      } else if (item.fieldTypeCode === COLUMN_FIELD_TYPE.DP) {
        columns[item.labelId] = formatDateWithOutSeconds(item.attrVal) || "-";
      } else if (item.colName === "item_code") {
        columns[item.labelId] =
          optionsType.value?.find(
            (subType: any) => subType.cmcdDetlId === item.attrVal
          )?.cmcdDetlNm || "-";
      } else {
        columns[item.labelId] = item.attrVal || "-";
      }
    });
    return columns;
  });
  return rowsData;
});

watch(
  () => listCommonCode.value,
  async (newVal) => {
    if (newVal?.length) {
      await search(newVal);
    }
  },
  { deep: true }
);

// const handleUploadResource = () => {
//   // TODO
// };

const handleDownloadDataTable = async (): Promise<void> => {
  try {
    if (!resourceParamsFilter.value.itemCode.trim()) {
      selectScroll.value?.validate();
      showSnackbar(t("product_platform.required_field_missing"), "error");
      return;
    }
    let typeSearch: Record<string, string> = {
      [PRODUCT_ITEM_CODE]: "objCode",
      [PRODUCT_ITEM_NAME]: "objName",
    };
    let params = {
      [typeSearch[resourceParamsFilter.value.resourceType]]:
        resourceParamsFilter.value.keyword
          ? resourceParamsFilter.value.keyword
          : undefined,
      itemCode: resourceParamsFilter.value.itemCode
        ? resourceParamsFilter.value.itemCode
        : undefined,
      page: resourceParamsFilter.value.page,
      size: resourceParamsFilter.value.size,
      language: locale.value || "en",
    };
    if (isApplied.value) {
      params = {
        ...filterParamsAdvanced(resourceAdvencedParams.value),
        ...params,
      };
    }
    await downloadFile(EXPORT_RESOURCE_EXCEL_PATH, params, "Resource");
  } catch (err: any) {
    showSnackbar(
      err?.errorMsg || t("product_platform.something_went_wrong"),
      "error"
    );
  }
};

const handleChangeViewMode = (value: string) => {
  viewMode.value = value;
  showResourceDuplicate.value = false;
  isEdit.value = false;
  resourceSelected.value = null;
  showDetail.value = false;
  showEntitySearch.value = false;
  listResourceSearch.value.items = [];
  listResourceSearch.value.pagination = cloneDeep(DEFAULT_PAGINATION);
  resetUpdateListEntity();
  handleSearch();
};

const optionsSearchType = [
  {
    name: "Name",
    value: PRODUCT_ITEM_NAME,
    cmcdDetlId: PRODUCT_ITEM_NAME,
    cmcdDetlNm: "Name",
  },
  {
    name: "Code",
    value: PRODUCT_ITEM_CODE,
    cmcdDetlId: PRODUCT_ITEM_CODE,
    cmcdDetlNm: "Code",
  },
];

const checkExpired = (item) => {
  if (!item?.validEndDtm) {
    return false;
  }
  if (
    moment(item?.validEndDtm, DATE_FORMAT.DATE_TYPE).isSame(new Date(), "day")
  ) {
    return true;
  }
  return (
    moment().diff(
      moment(item?.validEndDtm, DATE_FORMAT.DATE_TYPE).endOf("minutes")
    ) > 0
  );
};

const closePopupSave = () => {
  isShowPopup.value = false;
};

const handleNewResource = () => {
  if (currentAction.value === GROUP_DETAIL_CATEFORY.DUPLICATE) {
    resourceSelected.value = newResourceClick.value;
    showDetail.value = false;
    showResourceDuplicate.value = true;
    isDuplicate.value = true;
  } else {
    resourceSelected.value = newResourceClick.value;
    showDetail.value = true;
    showResourceDuplicate.value = false;
    isEdit.value = false;
    isDuplicate.value = false;
  }
  closePopupSave();
};

const isRequiredType = (): boolean => {
  return !(showComponentSearch.value || isComponentGroupSearch.value);
};

const handleSearch = async (
  pageSize?: number,
  isClick: boolean = false,
  page: number = 1
): Promise<void> => {
  if (!pageSize) {
    searchPaneRef.value?.calcTotalItem();
  }
  isResetValue.value = false;
  resourceCode.value = null;
  selectScroll.value.validate();
  const isNotRequired = isRequiredType();
  if (!resourceParamsFilter.value.itemCode.trim() && isNotRequired) {
    showSnackbar(t("product_platform.required_field_missing"), "error");
    return;
  }
  if (showComponentSearch.value && !componentSelected.value?.uuid) return;
  const pageSizeNum = pageSize ? pageSize : searchPaneRef.value?.totalItem;
  resourceParamsFilter.value.page = isClick ? 1 : page;
  resourceParamsFilter.value.size = showComponentSearch.value
    ? pageSizeNum || 7
    : isGridMode.value
      ? pageSizeNum || 14
      : 10;
  if (!isComponentGroupSearch.value) {
    resourceParamsFilter.value.componentUUID = null;
    localSelectType.value = resourceParamsFilter.value.itemCode;
    componentSelected.value = null;
  }
  await refreshListResource();
  if (isClick) {
    onCloseResourceDetail();
    showEntitySearch.value = false;
    isEdit.value = false;
  }
  showResourceDuplicate.value = false;
  dataTableKey.value = Math.random();
};

const handleResetSearch = (isClick: boolean = false) => {
  selectScroll.value.resetValidate();
  resetFilter();
  isApplied.value = false;
  showResourceDuplicate.value = false;
  resetResourceFilter();
  if (listResourceSearch.value?.items && listResourceSearch.value?.pagination) {
    listResourceSearch.value.items = [];
    listResourceSearch.value.pagination = cloneDeep(DEFAULT_PAGINATION);
  }
  onCloseResourceDetail();
  showEntitySearch.value = false;
  if (isClick) isResetValue.value = true;
};

defineExpose({ handleResetSearch });

const handleChangePage = async (page: number) => {
  resourceParamsFilter.value.page = page;
  await refreshListResource();
};

const handleChangeSize = async (pageSize: number) => {
  resourceParamsFilter.value.page = 1;
  resourceParamsFilter.value.size = pageSize;
  await refreshListResource();
};

const toggleMode = (): void => {
  if (isComponentGroupSearch.value) {
    isComponentGroupSearch.value = false;
    isResetValue.value = false;
  } else {
    showComponentSearch.value = true;
  }
  paramsFilterComponentGroup.value = cloneDeep(PARAMS_DEFAULT_COMPONENT);
  componentSearch.value.items = [];
  componentSelected.value = null;
  handleResetSearch();
};

const handleCalculatePage = (newPageSize: number): number => {
  const page = resourceParamsFilter.value.page;
  const size = resourceParamsFilter.value.size;
  const currentTotal = (page - 1) * size + 1;
  const newPage = Math.ceil(currentTotal / newPageSize);
  return newPage ? newPage : 1;
};

const closeComponentSearch = async () => {
  showComponentSearch.value = false;
  if (isComponentGroupSearch.value) {
    if (isResetValue.value) return;
    nextTick(() => {
      searchPaneRef.value?.calcTotalItem();
      const size = searchPaneRef.value?.totalItem || 14;
      const page = handleCalculatePage(size);
      resourceParamsFilter.value.page = page;
      handleSearch(size, false);
      componentKey.value++;
    });
  }
};

const openComponentSearch = async () => {
  showComponentSearch.value = true;
  isEdit.value = false;
  isDuplicate.value = false;
  if (isComponentGroupSearch.value) {
    if (isResetValue.value) return;
    nextTick(() => {
      searchPaneRef.value?.calcTotalItem();
      const size = searchPaneRef.value?.totalItem || 7;
      const page = handleCalculatePage(size);
      resourceParamsFilter.value.page = page;
      handleSearch(size, false);
    });
  } else {
    handleResetSearch();
  }
};

const handleSelectResource = async (resource: IResourceItem) => {
  currentAction.value = GROUP_DETAIL_CATEFORY.DETAIL;
  if (isEdit.value || isDuplicate.value) {
    popupContent.value = !isEdit.value
      ? t("product_platform.groupCancelDuplicate")
      : t("product_platform.groupCancelEdit");
    newResourceClick.value = resource;
    isShowPopup.value = true;
  } else {
    showResourceDuplicate.value = false;
    isEdit.value = false;
    resourceSelected.value = resource;
    showDetail.value = true;
    showEntitySearch.value = false;
    resetUpdateListEntity();
  }
};

const listActions = (item: IResourceItem): ActionType[] => {
  if (isExpired(item.endDate)) {
    return [];
  }
  return [
    {
      name: t("product_platform.duplicate"),
      icon: DuplicateIcon,
      onClick: () => {
        currentAction.value = GROUP_DETAIL_CATEFORY.DUPLICATE;
        if (isEdit.value || isDuplicate.value) {
          popupContent.value = !isEdit.value
            ? t("product_platform.groupCancelDuplicate")
            : t("product_platform.groupCancelEdit");
          newResourceClick.value = item;
          isShowPopup.value = true;
        } else {
          isDuplicate.value = true;
          showDetail.value = false;
          showResourceDuplicate.value = true;
          resourceSelected.value = item;
          resourceCode.value = null;
        }
      },
    },
  ];
};

const onCloseResourceDetail = () => {
  showDetail.value = false;
  resourceSelected.value = null;
  resourceCode.value = null;
};

const resourceList = computed(() => listResourceSearch.value?.items);

const totalSearchItems = computed(
  () => listResourceSearch.value?.pagination.totalSearchItems
);

const refreshListResource = async () => {
  let typeSearch: Record<string, string> = {
    [PRODUCT_ITEM_CODE]: "objCode",
    [PRODUCT_ITEM_NAME]: "objName",
  };
  let params = isComponentGroupSearch.value
    ? {
        objUuid: componentSelected.value?.uuid
          ? componentSelected.value?.uuid
          : undefined,
        [typeSearch[resourceParamsFilter.value.resourceType]]:
          resourceParamsFilter.value.keyword
            ? resourceParamsFilter.value.keyword
            : undefined,
        page: resourceParamsFilter.value.page,
        size: resourceParamsFilter.value.size,
        itemCode: resourceParamsFilter.value.itemCode
          ? resourceParamsFilter.value.itemCode
          : undefined,
      }
    : {
        [typeSearch[resourceParamsFilter.value.resourceType]]:
          resourceParamsFilter.value.keyword
            ? resourceParamsFilter.value.keyword
            : undefined,
        itemCode: resourceParamsFilter.value.itemCode
          ? resourceParamsFilter.value.itemCode
          : undefined,
        page: resourceParamsFilter.value.page,
        size: resourceParamsFilter.value.size,
      };
  if (isComponentGroupSearch.value) {
    params.objUuid = componentSelected.value?.uuid;
  }

  if (isApplied.value) {
    params = {
      ...filterParamsAdvanced(resourceAdvencedParams.value),
      ...params,
    };
  }
  try {
    isLoading.value = true;
    if (isComponentGroupSearch.value) {
      await getResourceList(params);
    } else if (isGridMode.value) {
      await getResourceAdvancedList(params);
    } else {
      await getDataResourceListView(params);
    }
    if (resourceCode.value) {
      const newItem = resourceList.value.filter(
        (item: any) => item?.code === resourceCode.value
      )[0];
      if (newItem) {
        handleSelectResource(newItem);
      } else {
        const tempParams = cloneDeep(params);
        tempParams.componentUUID = null;
        tempParams.itemCode = resourceSelected.value.itemCode;
        tempParams.code = resourceCode.value;
        tempParams.page = 1;
        const newItemData = await getResourceList(tempParams);
        handleSelectResource(newItemData.data.dataList[0]);
      }
    }
  } catch (error: any) {
    error?.errorMsg && showSnackbar(error?.errorMsg as string, "error");
  } finally {
    isLoading.value = false;
  }
};

const handleSearchDetail = async () => {
  if (resourceParamsFilter.value.itemCode) {
    if (!isApplied.value) {
      await setAdvencedSearchList(resourceParamsFilter.value.itemCode);
    }
    openSearchDetail.value = true;
  } else {
    showSnackbar(t("product_platform.required_field_missing"), "error");
  }
};

const applyPopupSearchDetail = async (event) => {
  isApplied.value = true;
  resourceAdvencedParams.value.general = event.general;
  resourceAdvencedParams.value.additional = event.additional;
  await handleSearch();
  openSearchDetail.value = false;
};

const handleResetSearchDetail = async () => {
  isApplied.value = false;
  closePopupSearchDetail();
  await refreshListResource();
};

const closePopupSearchDetail = () => {
  openSearchDetail.value = false;
};

watch(
  () => props.refreshFlag,
  async () => {
    await refreshListResource();
  }
);

watch(
  () => resourceParamsFilter.value.itemCode,
  (newVal) => {
    if (newVal) {
      isApplied.value = false;
    }
  }
);

onMounted(async () => {
  try {
    const { data } = await getListItemCodeApi({
      lItemCode: LargeItemCode.Resource,
    });
    optionsType.value = data;
  } catch (error: any) {
    showSnackbar(
      error?.errorMsg || t("product_platform.something_went_wrong"),
      "error"
    );
  }
});
</script>
