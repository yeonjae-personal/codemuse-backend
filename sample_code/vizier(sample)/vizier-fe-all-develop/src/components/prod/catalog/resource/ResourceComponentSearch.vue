<script setup lang="ts">
import { useI18n } from "vue-i18n";
import cloneDeep from "lodash-es/cloneDeep";
import { COMPONENTS_TYPE } from "@/constants/component";
import { useComponentStore, useSnackbarStore, useResourceStore } from "@/store";
import { ColNumber, LargeItemCode, SearchBy, SearchPaneType } from "@/enums";
import { getListItemCodeApi } from "@/api/prod/commonApi";
import { DEFAULT_PAGINATION, SPACE } from "@/constants/index";
import { getComponentSearchType } from "@/api/prod/componentApi";
import { PARAMS_DEFAULT_COMPONENT } from "@/constants/resource";
import useDragUserPocket from "@/composables/useDragUserPocket";
import SearchPane from "../../shared/SearchPane.vue";
import { BaseItemSearchPaneDto } from "@/types/common";

const emits = defineEmits(["resetSearch"]);

const { t } = useI18n();
const { getComponentDetail } = useComponentStore();
const {
  getListComponentSearch,
  getResourceList,
  resetResourceFilter,
  resetFilter,
} = useResourceStore();
const { showSnackbar } = useSnackbarStore();
const { handleDragUserPocket } = useDragUserPocket();
const {
  showComponentSearch,
  showDetail,
  componentSearch,
  componentSelected,
  paramsFilterComponentGroup,
  isResetValue,
  isComponentGroupSearch,
  resourceParamsFilter,
  showEntitySearch,
} = storeToRefs(useResourceStore());

const searchPane = ref<InstanceType<typeof SearchPane>>();
const optionsType = ref(
  COMPONENTS_TYPE.map((item) => ({ ...item, name: t(item.name) }))
);
const optionsSubType = ref<any[]>([]);

const handleCloseComponentSearch = (): void => {
  showComponentSearch.value = false;
};

const handleSearch = async (
  pageSize: number,
  isClick: boolean = false,
  page = 1
): Promise<void> => {
  searchPane.value?.validate();
  if (!paramsFilterComponentGroup.value.type) {
    showSnackbar(t("product_platform.required_field_missing"), "error");
    return;
  }
  paramsFilterComponentGroup.value.page = isClick ? 1 : page;
  paramsFilterComponentGroup.value.size = pageSize;
  componentSearch.value = {
    items: [] as any[],
    pagination: cloneDeep({ ...DEFAULT_PAGINATION, pageSize }),
  };
  await getListComponent();
};

const handleResetSearch = (): void => {
  searchPane.value?.resetValidate();
  paramsFilterComponentGroup.value = cloneDeep(PARAMS_DEFAULT_COMPONENT);
  componentSearch.value.items = [];
  optionsSubType.value = [];
  componentSelected.value = null;
  isComponentGroupSearch.value = false;
  emits("resetSearch");
};

const getListSubType = async (type: any): Promise<void> => {
  if (!type) return;
  try {
    const { data } = await getListItemCodeApi({
      mItemCode: type,
    });
    optionsSubType.value = data;
  } catch (error: any) {
    showSnackbar(error?.errorMsg as string, "error");
  }
};

const getListComponent = async (): Promise<void> => {
  try {
    const params = {
      ...paramsFilterComponentGroup.value,
      onlyValidDtm: true,
      componentType: paramsFilterComponentGroup.value.type || null,
      componentSubType: paramsFilterComponentGroup.value.subType || null,
      ["componentName"]:
        paramsFilterComponentGroup.value.searchBy === SearchBy.Name &&
        Boolean(paramsFilterComponentGroup.value.searchKey)
          ? paramsFilterComponentGroup.value.searchKey
          : undefined,
      ["componentCode"]:
        paramsFilterComponentGroup.value.searchBy === SearchBy.Code &&
        Boolean(paramsFilterComponentGroup.value.searchKey)
          ? paramsFilterComponentGroup.value.searchKey
          : undefined,
    };
    const res = await getListComponentSearch(params);
    if (res?.data) {
      componentSearch.value.items = res.data.elements.map((com) => {
        const dto = new BaseItemSearchPaneDto(
          com.uuid,
          com.name,
          com.code,
          com.itemCode,
          com.endDate,
          com.startDate
        );
        return { ...com, ...dto, itemLargeType: com.itemType };
      }) as any;
      componentSearch.value.pagination.totalSearchItems =
        res.data?.totalElements;
      componentSearch.value.pagination.totalItems = res.data?.totalElements;
      componentSearch.value.pagination.currentPage = res.data?.page;
      componentSearch.value.pagination.totalPages = res.data?.totalPages;
      componentSearch.value.pagination.pageSize = res.data?.size;
    } else {
      componentSearch.value.items = [];
      componentSearch.value.pagination.totalSearchItems = 0;
      componentSearch.value.pagination.totalPages = 0;
    }
  } catch (error: any) {
    showSnackbar(error?.errorMsg as string, "error");
  }
};

watch(
  () => paramsFilterComponentGroup.value.type,
  (value) => {
    paramsFilterComponentGroup.value.subType = SPACE;
    getListSubType(value);
  }
);

const handleChangePage = async (page: number): Promise<void> => {
  if (paramsFilterComponentGroup.value.page === page) return;
  paramsFilterComponentGroup.value.page = page;
  await getListComponent();
};

const getResourceGroupList = async (uuid: string): Promise<void> => {
  resetResourceFilter();
  resetFilter();
  resourceParamsFilter.value.componentUUID = uuid;
  resourceParamsFilter.value.itemCode = " ";
  resourceParamsFilter.value.size = paramsFilterComponentGroup.value.size;
  resourceParamsFilter.value.page = 1;
  const params = {
    objUuid: uuid ? uuid : undefined,
    page: resourceParamsFilter.value.page,
    size: resourceParamsFilter.value.size,
  };
  await getResourceList(params);
};

const handleShowComponentDetail = async (item: any, event): Promise<void> => {
  if (event) {
    if (!item["itemDetail"]) {
      await getDetailComponentItem(item);
    }
    item["isShowExpand"] = true;
  }
};

const handleDragStart = ({
  event,
  item,
}: {
  event: DragEvent;
  item: any;
}): void => {
  handleDragUserPocket(event, {
    userPocketType: LargeItemCode.Component,
    ...item,
  });
};

const handleClickItem = async (item: any): Promise<void> => {
  isComponentGroupSearch.value = true;
  isResetValue.value = false;
  componentSelected.value = item;
  showDetail.value = false;
  showEntitySearch.value = false;
  await getResourceGroupList(item.uuid);
};

const getDetailComponentItem = async (item: any): Promise<void> => {
  try {
    const res = await getComponentDetail({
      objUuid: item?.uuid,
      size: 7,
    });
    if (res?.status != 200) {
      item["itemDetail"] = null as any;
      showSnackbar(res.data?.errorMsg || "", "error");
      return;
    }
    item["itemDetail"] = res.data;
  } catch (error: any) {
    showSnackbar(error?.errorMsg || "", "error");
  }
};

const groupByMiddleItemCode = (data: any): any[] => {
  const grouped = data.reduce((acc, item) => {
    let group: any = acc.find((group) => group.value === item.middleItemCode);
    if (!group) {
      group = {
        name: item.middleItemName,
        value: item.middleItemCode,
        cmcdDetlId: item.middleItemCode,
        cmcdDetlNm: item.middleItemName,
        sortNo: item.middleSortNo,
        children: [],
      };
      acc.push(group);
    }
    group.children.push({
      name: item.itemName,
      value: item.itemCode,
      cmcdDetlId: item.itemCode,
      cmcdDetlNm: item.itemName,
      sortNo: item.sortNo,
    });
    return acc;
  }, []);

  // Sort groups by sortNo
  grouped.sort((cur, next) => cur.sortNo - next.sortNo);

  // Sort children within each group by sortNo
  grouped.forEach((group) => {
    group.children.sort((cur, next) => cur.sortNo - next.sortNo);
  });

  return grouped;
};

onMounted(async () => {
  const res = await getComponentSearchType();
  optionsType.value = groupByMiddleItemCode(res.data);
});
</script>

<template>
  <SearchPane
    ref="searchPane"
    title="product_platform.component_search"
    container-class="!bg-lighter rounded-l-lg"
    :pane-type="SearchPaneType.Component"
    :pane-col="ColNumber.One"
    :model-param="paramsFilterComponentGroup"
    :model-list="componentSearch.items"
    :pagination="componentSearch.pagination"
    :option-types="optionsType"
    :option-sub-types="optionsSubType"
    :selected-item="componentSelected"
    :item-height="62"
    show-float-icon-left
    @on-click-float-left="handleCloseComponentSearch"
    @on-search="handleSearch"
    @on-reset="handleResetSearch"
    @on-click-item="handleClickItem"
    @on-click-show-detail="handleShowComponentDetail"
    @on-change-page="handleChangePage"
    @on-drag-start="handleDragStart"
  />
</template>
