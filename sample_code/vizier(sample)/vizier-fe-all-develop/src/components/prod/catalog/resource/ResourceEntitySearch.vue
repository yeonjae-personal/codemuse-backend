<template>
  <SearchPane
    ref="searchPaneRef"
    title="product_platform.multiEntitySearch"
    container-class="rounded-lg"
    :pane-col="ColNumber.One"
    :model-list="entitySearch.items"
    :pagination="entitySearch.pagination"
    :item-height="62"
    icon-left-class="right-0"
    show-float-icon-left
    @on-click-float-left="onClose"
    @on-search="handleSearch"
    @on-reset="handleResetSearch"
    @on-change-page="handleChangePage"
    @on-drag-start="handleDragStart"
  >
    <template #custom-form>
      <div class="flex flex-column mt-2 gap-2">
        <div class="grid grid-cols-2 gap-2">
          <BaseSelectScroll
            v-model="paramsSearchEntity.itemCode"
            :options="multiEntityTypes"
            :placeholder="$t('product_platform.type')"
            :height="48"
            disabled
            default-item-select-all
          />
          <BaseSelectScroll
            v-model="paramsSearchEntity.entityTypeCode"
            :options="subTypeList"
            :placeholder="$t('product_platform.sub_type')"
            :height="48"
            default-item-select-all
          />
        </div>

        <div class="grid grid-cols-[1fr_2fr] gap-2">
          <BaseSelectScroll
            v-model="entitySearchNmCd"
            :options="NM_CD_FIELDS"
            :height="48"
            @update:model-value="handleChangeNmCd"
          />
          <div class="flex items-center gap-2">
            <BaseInputSearch
              v-model="inputValue"
              density="comfortable"
              label="search"
              variant="solo"
              hide-details
              single-line
              rounded="4"
              @keyup.enter="handleEnterSearch()"
            />
          </div>
        </div>
      </div>
    </template>
    <template #custom-search-item="{ item, index }">
      <cf-card-dropdown
        class-name="default entity-icon"
        :title="item.entityName"
        :description="item.entityCode"
        :search-text="inputValue"
        :search-field="entitySearchNmCd"
        type-bg="linear"
        border-color-action="dark-blue"
        display-border-left="dark-blue"
        :active="item.entityCode === selectedEntity?.entityCode"
        :node="{
          hideNodeLeft: true,
          isActiveNodeLeft: false,
          hideNodeRight: true,
          isActiveNodeRight: false,
        }"
        show-icon-status
        :draggable="!checkItemDragExist(item.entityCode)"
        :disable="checkItemDragExist(item.entityCode)"
        @dragstart="handleDragStart($event, item)"
        @dragend="handleDragEnd"
        @on-click-card="onChooseCard(item, index)"
        @on-click-show-detail="onChooseCard(item, index)"
      >
        <template #icon>
          <span class="flex justify-center align-center w-[32px] h-[32px]">
            <multi-entity-icon />
          </span>
        </template>
        <template #detail>
          <div class="flex flex-column gap-4">
            <ProductGrid :data="item.detail" />
          </div>
        </template>
      </cf-card-dropdown>
    </template>
  </SearchPane>
</template>

<script lang="ts" setup>
import cloneDeep from "lodash-es/cloneDeep";
import { useDragStore, useResourceStore } from "@/store";
import { NM_CD_FIELDS } from "@/constants/";
import {
  MULTI_ENTITY_DETAIL_DATA_GRID,
  MULTI_ENTITY_DETAIL_USER_TITLE_GRID,
  MULTI_ENTITY_SUBTYPE,
} from "@/constants/multiEntity";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import { getMultiEntityDetail } from "@/api/prod/extendsApi";
import { useGroupCode } from "@/composables/useGroupCode";
import useDragUserPocket from "@/composables/useDragUserPocket";
import { ColNumber } from "@/enums";
import SearchPane from "../../shared/SearchPane.vue";

const {
  entitySearch,
  entitySearchNmCd,
  multiEntityTypes,
  paramsSearchEntity,
  showEntitySearch,
  resourceSelected,
  resourceEntityList,
} = storeToRefs(useResourceStore());
const { getEntityList, resetParamListEntitySearch } = useResourceStore();
const { dragOfferType, isDragging } = storeToRefs(useDragStore());
const { search } = useGroupCode();
const { handleDragUserPocket } = useDragUserPocket();

const searchPaneRef = ref<InstanceType<typeof SearchPane>>();
const inputValue = ref();
const selectedEntity = ref();
const uniqueCodeList = ref();

const subTypeList = computed(() => {
  if (paramsSearchEntity.value.itemCode && multiEntityTypes.value?.length) {
    const list = multiEntityTypes.value.find(
      (item) => item.value === paramsSearchEntity.value.itemCode
    );
    return list.subOptions;
  }
  return [];
});

const checkItemDragExist = computed(() => (value: string) => {
  let arrs = [] as any;
  resourceEntityList.value.forEach((i) => {
    if (i.objRel?.length) {
      arrs = [...arrs, ...i.objRel];
    }
  });
  return !!arrs.some((item: any) => item.entityCode === value);
});

watch(
  () => uniqueCodeList.value,
  (newVal) => {
    if (newVal.length) {
      search(newVal);
    }
  },
  { deep: true }
);

onMounted(() => {
  paramsSearchEntity.value.itemCode = resourceSelected.value?.itemCode;
});

const handleSearch = async (
  pageSize?: number,
  isClick: boolean = false,
  page: number = 1
): Promise<void> => {
  if (!pageSize) {
    searchPaneRef.value?.calcTotalItem();
  }
  if (entitySearchNmCd.value === NM_CD_FIELDS[0].value) {
    paramsSearchEntity.value.multiEntityName = inputValue.value;
  } else {
    paramsSearchEntity.value.multiEntityCode = inputValue.value;
  }
  paramsSearchEntity.value.page = isClick ? 1 : page;
  paramsSearchEntity.value.size = pageSize
    ? pageSize
    : searchPaneRef.value?.totalItem || 7;
  await getEntityList();
};

const handleChangeNmCd = (): void => {
  if (entitySearchNmCd.value === NM_CD_FIELDS[0].value) {
    paramsSearchEntity.value.multiEntityCode = null;
    paramsSearchEntity.value.multiEntityName = inputValue.value;
  } else {
    paramsSearchEntity.value.multiEntityCode = inputValue.value;
    paramsSearchEntity.value.multiEntityName = null;
  }
};

const onChooseCard = async (item: any, index: number): Promise<void> => {
  selectedEntity.value = item;
  await getEntityDetailInfo(item, index);
};

const getEntityDetailInfo = async (
  item: any,
  _index: number
): Promise<void> => {
  const params = {
    entityCode: item.entityCode,
    entityTypeCode: item.entityTypeCode,
  };

  const { data } = await getMultiEntityDetail(params);
  const selectedEntityDetails = data;
  if (selectedEntityDetails) {
    let generalData;
    if (
      [
        MULTI_ENTITY_SUBTYPE.BUSINESS_LINE,
        MULTI_ENTITY_SUBTYPE.DISCOUNT_TARGET,
        MULTI_ENTITY_SUBTYPE.SALE_COMPANY,
      ].includes(selectedEntity.value.entityTypeCode)
    ) {
      generalData = cloneDeep(
        MULTI_ENTITY_DETAIL_DATA_GRID[selectedEntity.value.entityTypeCode]
      );
    } else {
      generalData = cloneDeep(MULTI_ENTITY_DETAIL_USER_TITLE_GRID);
    }
    generalData.forEach((item) => {
      if (selectedEntityDetails[item.labelId]) {
        item.attrVal = selectedEntityDetails[item.labelId];
        if (item.labelId == "itemCode" || item.labelId == "entityTypeCode") {
          item.attrVal = findType(item.attrVal);
        }
      }
    });
    const detail = {
      general: generalData,
      additional: selectedEntityDetails.additional,
    };
    const generalGroupCodes = detail.general
      .filter(
        (item) =>
          item.fieldTypeCode === COLUMN_FIELD_TYPE.DL && !!item.groupCode
      )
      .map((item) => item.groupCode);
    const additionalGroupCodes = detail.additional
      .filter(
        (item) =>
          item.fieldTypeCode === COLUMN_FIELD_TYPE.DL && !!item.commGroupCode
      )
      .map((item) => item.commGroupCode);
    uniqueCodeList.value = [
      ...new Set(generalGroupCodes.concat(additionalGroupCodes)),
    ];

    detail.general.push({
      labelId: "product_platform.ovwCntn",
      attrVal: selectedEntityDetails.ovwCntn,
      fieldTypeCode: COLUMN_FIELD_TYPE.TA,
      isShow: true,
    });
    item["detail"] = detail;
  }
};

const handleResetSearch = (): void => {
  resetParamListEntitySearch();
  selectedEntity.value = null;
  inputValue.value = null;
};

const handleChangePage = async (page: number): Promise<void> => {
  paramsSearchEntity.value.page = page;
  await getEntityList();
};

const handleDragStart = (event: DragEvent, item: any): void => {
  isDragging.value = true;
  dragOfferType.value = item.entityTypeCode;
  handleDragUserPocket(event, item);
};

const handleDragEnd = (): void => {
  isDragging.value = false;
};

const onClose = (): void => {
  showEntitySearch.value = false;
};

const findType = (type: string): string => {
  let typeName;
  multiEntityTypes.value?.forEach((item) => {
    if (item.value === type) {
      typeName = item.label;
      return typeName;
    } else {
      if (item.subOptions.length > 0) {
        item.subOptions.find((subItem) => {
          if (subItem.value === type) {
            typeName = subItem.label;
            return typeName;
          }
        });
      } else {
        return "-";
      }
    }
  });
  return typeName;
};

const handleEnterSearch = () => {
  searchPaneRef.value?.handleSearch();
};
</script>
