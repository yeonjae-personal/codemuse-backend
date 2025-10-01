<script setup lang="ts">
import { useI18n } from "vue-i18n";
import cloneDeep from "lodash-es/cloneDeep";
import {
  ComponentSubType,
  dictionaryResourceTypeWithComponent,
} from "@/enums/component";
import {
  useResourceStore,
  useSnackbarStore,
  useComponentStore,
  useDragStore,
} from "@/store";
import { IResourceItem } from "@/interfaces/prod/resource";
import { isExpiredTime } from "@/utils/format-data";
import { RESOURCE_PARAMS_FILTER_DEFAULT } from "@/constants/resource";
import { RESOURCE_ITEM_CODE } from "@/constants/resource";
import { LARGE_ITEM_CODE } from "@/store/userPocket.store";
import { getListItemCodeApi } from "@/api/prod/commonApi";
import { ColNumber, LargeItemCode, SearchBy } from "@/enums";
import { useGroupCode } from "@/composables/useGroupCode";
import { OPTIONS_SELECT_SEARCH } from "@/constants/offer";
import useDragUserPocket from "@/composables/useDragUserPocket";
import SearchPane from "../../shared/SearchPane.vue";

const props = defineProps({
  resourceWithComponentLargeType: {
    type: String,
    default: "",
  },
  resourceWithComponentType: {
    type: String,
    default: ComponentSubType.RecurringCharge,
  },
  isFilterWithComponent: {
    type: Boolean,
    default: false,
  },
  isAdd: {
    type: Boolean,
    default: false,
  },
});

const emits = defineEmits(["on-click-item"]);

const { t } = useI18n();

const {
  showResourceAdd,
  showResourceImpactAnalyst,
  componentSelected,
  componentCreated,
  showResourceAddCreate,
  listResourceAdd,
  listResourceAddCreate,
  resourceParamsFilter,
  resourceParamsFilterCreate,
  componentCreateData,
  resourceTypes,
  componentDetail,
} = storeToRefs(useComponentStore());
const snackbarStore = useSnackbarStore();
const { getResourceDetail, getResourceListAdd } = useResourceStore();
const { ITEM_VALUE } = useGroupCode();
const { dragOfferType } = storeToRefs(useDragStore());
const { handleDragUserPocket } = useDragUserPocket();

const searchPaneRef = ref<InstanceType<typeof SearchPane>>();
const currentResourceActive = ref<any>({
  uuid: "",
});
const optionsType = ref<any[]>([]);
const componentKey = ref<number>(0);

const componentSelectedValue = computed(() =>
  props.isAdd ? componentCreated.value : componentSelected.value
);
const listResourceAddValue = computed(() =>
  props.isAdd ? listResourceAddCreate.value : listResourceAdd.value
);
const resourceParamsFilterValue = computed(() =>
  props.isAdd ? resourceParamsFilterCreate.value : resourceParamsFilter.value
);

const checkItemDragExist = computed(() => (uuid: string) => {
  if (props.isAdd) {
    return componentCreateData?.value?.resource.some(
      (item) => item.resourceUUID === uuid
    );
  }

  return componentDetail.value?.resources?.some(
    (item: any) => item.objUuid === uuid
  );
});

const closeComponentResourceAdd = () => {
  if (props.isAdd) {
    showResourceAddCreate.value = false;
  } else {
    showResourceAdd.value = false;
  }
  showResourceImpactAnalyst.value = false;
};

const getListResource = async (pageSize?: number): Promise<void> => {
  try {
    if (!pageSize) {
      searchPaneRef.value?.calcTotalItem();
    }
    const { resourceType, keyword, itemCode, size, page } =
      resourceParamsFilterValue.value;
    const resourceKeyMap = {
      name: "rscName",
      code: "rscCode",
    };
    const params = {
      [resourceKeyMap[resourceType as string]]: keyword || undefined,
      rscItemCode: itemCode || undefined,
      componentUUID: componentSelectedValue.value?.prodUuid || undefined,
      componentType: props.isAdd
        ? props.resourceWithComponentLargeType
        : componentSelectedValue.value?.itemType,
      componentSubType: props.isAdd
        ? props.resourceWithComponentType
        : componentSelectedValue.value?.itemCode,
      componentCreateType:
        componentSelectedValue.value?.itemCode ||
        componentCreateData.value?.general?.find(
          (item) => item.colName === "item_code"
        )?.attrVal,
      page,
      size: pageSize ? pageSize : searchPaneRef.value?.totalItem || size,
    };
    const res = await getResourceListAdd(params);
    listResourceAddValue.value.items = [];
    listResourceAddValue.value.total = 0;
    listResourceAddValue.value.totalSearch = 0;
    if (res?.data) {
      componentKey.value++;
      listResourceAddValue.value.items = res.data.elements;
      listResourceAddValue.value.currentPage = res.data.page;
      listResourceAddValue.value.pageSize = res.data.size;
      listResourceAddValue.value.totalItemSearch = res.data.totalElements;
      listResourceAddValue.value.totalPages = res.data.totalPages;
    } else {
      snackbarStore.showSnackbar(
        t("product_platform.something_went_wrong"),
        "error"
      );
    }
  } catch (err: any) {
    snackbarStore.showSnackbar(
      err?.errorMsg || t("product_platform.something_went_wrong"),
      "error"
    );
  }
};

const handleChangePageResource = (page: number) => {
  resourceParamsFilterValue.value.page = page;
  getListResource();
};

const handleSearch = async (
  pageSize?: number,
  isClick: boolean = false,
  page = 1
): Promise<void> => {
  resourceParamsFilterValue.value.size = pageSize;
  resourceParamsFilterValue.value.page = isClick ? 1 : page;
  getListResource(pageSize);
};

const handleResetSearch = () => {
  currentResourceActive.value.uuid = "";
  listResourceAddValue.value.items = [];
  listResourceAddValue.value.totalSearch = 0;
  if (props.isAdd) {
    resourceParamsFilterCreate.value = cloneDeep(
      RESOURCE_PARAMS_FILTER_DEFAULT
    );
  } else {
    resourceParamsFilter.value = cloneDeep(RESOURCE_PARAMS_FILTER_DEFAULT);
  }
};

const getResourceDetailLocal = async (item: IResourceItem) => {
  try {
    const res = await getResourceDetail({
      objUuid: item.uuid || "",
      itemCode: item.itemCode,
    });

    item["detail"] = {
      general: res?.data?.general,
      additional: res?.data?.additional || [],
    };
  } catch (err: any) {
    item["detail"] = null as any;
    snackbarStore.showSnackbar(
      err?.errorMsg || t("product_platform.something_went_wrong"),
      "error"
    );
  }
};

const handleClickResourceItem = async (event: any, item: IResourceItem) => {
  currentResourceActive.value.uuid = item.uuid;
  if (event?.isShowDetail) {
    if (!item.inforItemDetail) {
      await getResourceDetailLocal(item);
    }
    emits("on-click-item", item);
  }
};

const toggleShowDetail = async (event: any, item: IResourceItem) => {
  if (event) {
    await getResourceDetailLocal(item);
  }
};

const handleEnterSearch = () => {
  searchPaneRef.value?.handleSearch();
};

const handleDragStart = (event: DragEvent, item: any): void => {
  dragOfferType.value = item.itemCode;
  handleDragUserPocket(event, {
    userPocketType: LARGE_ITEM_CODE.RESOURCE,
    ...item,
  });
};

const handleDragEnd = () => {
  // dragOfferType.value = "";
};

watch(
  () => componentSelected.value,
  () => {
    if (!props.isAdd) {
      handleResetSearch();
      showResourceAdd.value = false;
    }
  },
  { deep: true }
);

onMounted(async () => {
  if (props.isFilterWithComponent) {
    resourceParamsFilterValue.value.itemCode =
      dictionaryResourceTypeWithComponent[props.resourceWithComponentType];
  }
  try {
    const { data } = await getListItemCodeApi({
      lItemCode: LargeItemCode.Resource,
    });
    optionsType.value = data;
  } catch (error: any) {
    snackbarStore.showSnackbar(
      error?.errorMsg || t("product_platform.something_went_wrong"),
      "error"
    );
  }
});
</script>

<template>
  <SearchPane
    ref="searchPaneRef"
    title="product_platform.resource_search"
    :model-list="listResourceAddValue.items || []"
    :pagination="{
      currentPage: listResourceAddValue.currentPage,
      totalPages: listResourceAddValue.totalPages,
      pageSize: listResourceAddValue.pageSize,
      totalItems: listResourceAddValue.totalItemSearch,
      totalSearchItems: listResourceAddValue.totalItemSearch,
    }"
    :pane-col="ColNumber.One"
    show-float-icon-left
    container-class="rounded-lg"
    icon-left-class="right-0"
    :item-height="62"
    @on-search="handleSearch"
    @on-reset="handleResetSearch"
    @on-click-float-left="closeComponentResourceAdd"
    @on-change-page="handleChangePageResource"
  >
    <template #custom-form>
      <div class="mt-2">
        <BaseSelectScroll
          v-model="resourceParamsFilterValue.itemCode"
          :options="
            optionsType.filter((x) =>
              resourceTypes.some((y) => y.trgtItemCode === x[ITEM_VALUE])
            )
          "
          :placeholder="$t('product_platform.type')"
          :default-item-select-all="true"
          :height="48"
        />
        <div class="grid grid-cols-3 gap-x-2 mt-2">
          <BaseSelectScroll
            v-model="resourceParamsFilterValue.resourceType"
            :options="OPTIONS_SELECT_SEARCH"
            :height="48"
            :show-error-massage="false"
            :default-item-select-all="false"
            :show-option-null="false"
          />
          <div class="col-span-2">
            <BaseInputSearch
              v-model="resourceParamsFilterValue.keyword"
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
    <template #custom-search-item="{ item }">
      <cf-card-dropdown
        :key="`${componentKey}_${item.code}`"
        type-bg="light"
        border-color-action="purple"
        class-name="card-round-style"
        :item="item"
        :title="item.name"
        :description="item.code"
        :search-text="resourceParamsFilterValue.keyword"
        :search-field="
          resourceParamsFilterValue.resourceType === 'name'
            ? SearchBy.Name
            : SearchBy.Code
        "
        :active="item.uuid === currentResourceActive?.uuid"
        :draggable="!checkItemDragExist(item.uuid)"
        :disable="checkItemDragExist(item.uuid)"
        :node="{
          hideNodeLeft: true,
          isActiveNodeLeft: false,
          hideNodeRight: true,
          isActiveNodeRight: false,
        }"
        :expired="isExpiredTime(item.endDate)"
        :expand="false"
        show-icon-status
        @on-click-card="handleClickResourceItem($event, item)"
        @on-click-show-detail="toggleShowDetail($event, item)"
        @dragstart="handleDragStart($event, item)"
        @dragend="handleDragEnd"
      >
        <template #icon>
          <span class="flex justify-center align-center w-[40px] h-[40px]">
            <template
              v-if="item.itemCode === RESOURCE_ITEM_CODE.RATING_ELEMENT"
            >
              <RLinearIcon />
            </template>
            <template
              v-if="item.itemCode === RESOURCE_ITEM_CODE.BILLING_ELEMENT"
            >
              <BLinearIcon />
            </template>
            <template
              v-if="item.itemCode === RESOURCE_ITEM_CODE.SERVICE_ELEMENT"
            >
              <SLinearIcon />
            </template>
          </span>
        </template>
        <template #detail>
          <ProductGrid
            v-if="item.detail"
            :data="item.detail"
            :type="LARGE_ITEM_CODE.RESOURCE"
          />
        </template>
      </cf-card-dropdown>
    </template>
  </SearchPane>
</template>
