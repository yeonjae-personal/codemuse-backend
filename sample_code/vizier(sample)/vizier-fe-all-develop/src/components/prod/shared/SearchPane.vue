<template>
  <div
    ref="swapperSearch"
    class="bg-white relative max-h-[calc(100vh-137px)]"
    :class="[containerClass, `col-span-${paneCol}`]"
  >
    <div class="flex flex-col h-full w-full">
      <div ref="formContainer" class="pt-6">
        <template v-if="slots['custom-header-search-pane']">
          <slot name="custom-header-search-pane"></slot>
        </template>
        <div v-else class="px-6">
          <div class="flex justify-between items-center">
            <div class="flex align-center gap-2">
              <h1 class="font-medium text-base text-text-base tracking-[0.5px]">
                {{ $t(`${title}`) }}
              </h1>
              <span
                v-if="titleAppend"
                class="font-weight-[400] text-[13px] text-[#6B6D70] leading-6"
              >
                {{ $t(`${titleAppend}`) }}
              </span>
            </div>

            <div class="flex">
              <SearchAndRefreshButton
                @handle-search="handleSearch"
                @handle-refresh="handleResetSearch"
              />
              <template v-if="slots['search-button-append']">
                <slot name="search-button-append"></slot>
              </template>
            </div>
          </div>
          <template v-if="slots['custom-form']">
            <slot name="custom-form" :page-size="totalItem"></slot>
          </template>
          <v-form
            v-else-if="paneType"
            ref="form"
            class="w-full grid gap-2"
            :class="[
              formClass,
              paneCol === ColNumber.One
                ? 'grid-flow-row grid-rows-2 mt-2'
                : 'grid-flow-col grid-cols-2 mt-2',
              {
                '!grid-rows-1':
                  paneCol === ColNumber.One && paramSearch.type == undefined,
              },
            ]"
            @submit.prevent=""
          >
            <div
              v-if="paramSearch.type != undefined"
              class="grid grid-cols-1 gap-2"
              :class="paramSearch.subType ? 'grid-cols-2' : 'grid-cols-1'"
            >
              <BaseSelectScroll
                ref="typeSelectScroll"
                v-model="paramSearch.type"
                :options="optionTypes"
                :height="48"
                :placeholder="$t(typePlaceholder)"
                :required="typeSelectRequire"
                :default-item-select-all="!typeSelectRequire"
                @update:model-value="handleChangeType"
              />
              <BaseSelectScroll
                v-if="paramSearch.subType"
                ref="subTypeSelectScroll"
                v-model="paramSearch.subType"
                :options="optionSubTypes"
                :height="48"
                :placeholder="$t(subtypePlaceholder)"
                :required="subTypeSelectRequire"
                :default-item-select-all="true"
              />
            </div>
            <div class="grid grid-cols-[1fr_2fr] gap-2">
              <BaseSelectScroll
                v-model="paramSearch.searchBy"
                :height="48"
                :options="SEARCH_BY_OPTIONS"
                :default-item-select-all="false"
              />
              <div class="flex items-center gap-2">
                <BaseInputSearch
                  v-model="paramSearch.searchKey"
                  density="comfortable"
                  label="search"
                  variant="solo"
                  hide-details
                  single-line
                  rounded="4"
                  @keyup.enter="handleSearch()"
                />
              </div>
            </div>
          </v-form>
        </div>
      </div>
      <div ref="scrollComponent" class="h-full">
        <template v-if="slots['custom-search-main-content']">
          <slot name="custom-search-main-content"></slot>
        </template>
        <div v-else class="flex flex-col h-full">
          <template v-if="dataList?.length > 0">
            <BaseTotalSearchResult
              v-if="!!pagination.totalSearchItems"
              class-name="px-6 pt-3 mb-1"
              :total-search="pagination?.totalSearchItems"
              :is-custom="isCustomSearchResult"
              :text-custom="textCustomSearchResult"
              :total-items="pagination?.totalItems"
            />
            <LocomotiveComponent
              :style="{ maxHeight: locoMaxheight }"
              :scroll-container-class="['!px-6']"
              :scroll-content-class="[
                'grid gap-3 py-2',
                paneCol == ColNumber.One &&
                paneType !== SearchPaneType.FactorTable
                  ? 'grid-cols-1'
                  : 'grid-cols-2',
              ]"
              @call-lazy-load="handleLazyLoad"
            >
              <div
                v-for="(item, index) in dataList"
                ref="itemElement"
                :key="item.itemUnique"
              >
                <template v-if="slots['custom-search-item']">
                  <slot
                    name="custom-search-item"
                    :item="item"
                    :index="index"
                  ></slot>
                </template>
                <template v-else>
                  <FactorTypeItem
                    v-if="paneType === SearchPaneType.FactorTable"
                    :title="item.itemName"
                    :active="item.itemUnique === selectedItem?.itemUnique"
                    :disable="item?.useYn === RequiredYn.No"
                    :type-code="item.itemUnique"
                    :search-text="paramSearch.searchKey"
                    :show-icon="item.showAppendIcon"
                    :height="itemHeight + 'px'"
                    @selected-item="onClickItem(item)"
                  />
                  <cf-card-dropdown
                    v-else
                    :class-name="itemClassName"
                    :title="item.itemName"
                    :description="item.itemDescription"
                    :search-text="paramSearch.searchKey"
                    :search-field="paramSearch.searchBy"
                    :type-bg="
                      paneType === SearchPaneType.Resource ? 'light' : 'linear'
                    "
                    :border-color-action="
                      setHoverColor(
                        paneType == SearchPaneType.Component
                          ? item?.itemLargeType
                          : item.itemType
                      )
                    "
                    :display-border-left="
                      paneType == SearchPaneType.Component
                        ? setHoverColor(item?.itemLargeType)
                        : paneType == SearchPaneType.MultiEntity
                          ? 'dark-blue'
                          : ''
                    "
                    :type-of-prod="
                      paneType == SearchPaneType.Offer
                        ? setIconType(item.itemType)
                        : ''
                    "
                    :icon-color="
                      paneType == SearchPaneType.Offer
                        ? setIconColor(item.itemType)
                        : ''
                    "
                    :active="item.itemUnique === selectedItem?.itemUnique"
                    :node="{
                      hideNodeLeft: true,
                      hideNodeRight: true,
                    }"
                    draggable
                    :show-icon-status="item.showAppendIcon"
                    :editable="item.editable"
                    :is-new="item.isNew"
                    :actions="searchItemActions(item)"
                    :disable="
                      checkExist(item.itemUnique) ||
                      isExpiredTime(item?.validEndDtm)
                    "
                    :expand="item.expand"
                    @on-click-card="onClickItem(item)"
                    @on-click-show-detail="onClickShowDetail(item, $event)"
                    @dragstart="handleDragStart($event, item)"
                    @drag-end="handleDragEnd"
                  >
                    <template
                      v-if="
                        [
                          SearchPaneType.Group,
                          SearchPaneType.MultiEntity,
                          SearchPaneType.Resource,
                        ].includes(paneType)
                      "
                      #icon
                    >
                      <span
                        class="flex justify-center align-center w-[40px] h-[40px]"
                      >
                        <FolderIcon v-if="paneType === SearchPaneType.Group" />
                        <MultiEntityIcon
                          v-else-if="paneType === SearchPaneType.MultiEntity"
                        />
                        <MultiEntityIcon
                          v-else-if="
                            item.itemType === SearchPaneType.MultiEntity
                          "
                        />
                        <RLinearIcon
                          v-else-if="
                            item.itemType === RESOURCE_ITEM_CODE.RATING_ELEMENT
                          "
                        />
                        <BLinearIcon
                          v-else-if="
                            item.itemType === RESOURCE_ITEM_CODE.BILLING_ELEMENT
                          "
                        />
                        <SLinearIcon
                          v-else-if="
                            item.itemType === RESOURCE_ITEM_CODE.SERVICE_ELEMENT
                          "
                        />
                      </span>
                    </template>
                    <template v-if="item.itemDetail" #detail>
                      <div class="detail-content">
                        <ProductGrid :data="item.itemDetail" :type="paneType" />
                      </div>
                    </template>
                  </cf-card-dropdown>
                </template>
              </div>
            </LocomotiveComponent>
            <BasePagination
              v-if="pagination.totalPages > 0 && showPagination"
              :pagination="pagination"
              class-name="!mt-[auto] mb-3 !relative left-0 !-translate-x-1"
              @on-change-page="handleChangePage"
            />
            <div ref="searchPaneFooter">
              <slot name="footer"></slot>
            </div>
          </template>
          <div
            v-if="pagination?.totalSearchItems === 0 || dataList?.length === 0"
            class="flex-1"
          >
            <NoData />
          </div>
        </div>
      </div>
    </div>
    <ArrowLeftIcon
      v-if="showFloatIconLeft"
      class="absolute top-[174px] cursor-pointer text-[#525457] hover:text-[#303132]"
      :class="iconLeftClass"
      @click="onClickLeft"
    />
    <ArrowLeftIcon
      v-if="showFloatIconRight"
      class="absolute top-[174px] !right-0 cursor-pointer text-[#525457] hover:text-[#303132] rotate-180"
      :class="iconRightClass"
      @click="onClickRight"
    />
    <BasePopup
      v-model="modelOpenPopup"
      :icon="DialogIconType.Warning"
      :submit-button-text="$t('product_platform.btn_yes')"
      :cancel-button-text="$t('product_platform.btn_no')"
      :content="popupContent"
      @on-close="closePopup"
      @on-submit="submitPopup"
    />
  </div>
</template>

<script setup lang="ts">
import ArrowLeftIcon from "@/components/prod/icons/ArrowLeftIcon.vue";
import { Slots } from "vue";
import { ColNumber, DialogIconType, RequiredYn, SearchPaneType } from "@/enums";
import { SEARCH_BY_OPTIONS, SPACE } from "@/constants/";
import { isExpiredTime } from "@/utils/format-data";
import {
  BaseItemSearchPaneDto,
  BasePaginationType,
  BaseSearchPaneParam,
  BaseSearchPaneParamClass,
} from "@/types/common";
import { useWindowSize } from "@vueuse/core";
import {
  setHoverColor,
  setIconColor,
  setIconType,
} from "@/utils/impact-analysis-utils";
import { RESOURCE_ITEM_CODE } from "@/constants/resource";
import { useDragStore } from "@/store";
import { debounce } from "lodash-es";

type Props = {
  paneType?: SearchPaneType;
  paneCol?: ColNumber;
  title: string;
  titleAppend?: string;
  containerClass?: String | Array<string>;
  formClass?: String | Array<string>;
  showFloatIconLeft?: boolean;
  showFloatIconRight?: boolean;
  modelList?: Array<BaseItemSearchPaneDto> | any;
  pagination?: BasePaginationType;
  showPagination?: boolean;
  openPopup?: boolean;
  popupContent?: string;
  modelParam?: BaseSearchPaneParam;
  modelInputText?: string;
  optionTypes?: Array<any>;
  optionSubTypes?: Array<any>;
  typeSelectRequire?: boolean;
  subTypeSelectRequire?: boolean;
  selectedItem?: BaseItemSearchPaneDto;
  itemHeight?: number;
  listItemNotDrag?: Array<any>;
  iconLeftClass?: Array<string> | string;
  iconRightClass?: Array<string> | string;
  typePlaceholder?: string;
  subtypePlaceholder?: string;
  scrollContainerClass?: Array<string> | string;
  isCustomSearchResult?: boolean;
  textCustomSearchResult?: string;
  searchItemActions?: (item: BaseItemSearchPaneDto) => any[];
};

const props = withDefaults(defineProps<Props>(), {
  paneType: SearchPaneType.Offer,
  paneCol: ColNumber.One,
  title: "",
  titleAppend: "",
  showFloatIconLeft: false,
  showFloatIconRight: false,
  showPagination: true,
  openPopup: false,
  popupContent: "",
  modelParam: () => new BaseSearchPaneParamClass(),
  modelInputText: undefined,
  typeSelectRequire: true,
  subTypeSelectRequire: false,
  isCustomSearchResult: false,
  textCustomSearchResult: "",
  itemHeight: 0,
  optionTypes: () => [],
  optionSubTypes: () => [],
  containerClass: () => [],
  formClass: () => [],
  modelList: () => [] as BaseItemSearchPaneDto[],
  pagination: () => ({}) as BasePaginationType,
  selectedItem: () => ({}) as BaseItemSearchPaneDto,
  searchItemActions: () => [],
  listItemNotDrag: () => [],
  iconLeftClass: "left-[-24px]",
  iconRightClass: "left-0",
  typePlaceholder: "product_platform.Type",
  subtypePlaceholder: "product_platform.sub_type",
  scrollContainerClass: "",
});

const emits = defineEmits([
  "onSearch",
  "onReset",
  "onClickFloatLeft",
  "onClickFloatRight",
  "closePopup",
  "submitPopup",
  "onChangePage",
  "update:modelList",
  "update:modelParam",
  "onDragStart",
  "onDragEnd",
  "onClickItem",
  "onClickShowDetail",
  "update:openPopup",
  "update:modelInputText",
  "onChangePageSize",
  "onLazyLoad",
]);

interface CustomSlots extends Slots {
  "custom-header-search-pane"?: () => VNode[];
  "search-button-append"?: () => VNode[];
  "custom-form"?: () => VNode[];
  "custom-search-main-content"?: () => VNode[];
  "custom-search-item"?: () => VNode[];
  footer?: () => VNode[];
  default?: () => VNode[];
}

const { height } = useWindowSize();
const { isDragging } = storeToRefs(useDragStore());
const slots = useSlots() as CustomSlots;
const swapperSearch = ref();
const formContainer = ref();
const typeSelectScroll = ref();
const subTypeSelectScroll = ref();
const scrollComponent = ref();
const itemElement = ref();
const searchPaneFooter = ref();
const totalItem = ref(props.pagination.pageSize);
const pageChange = ref(1);
const locoMaxheight = ref("500px");

const dataList = computed({
  get() {
    return props.modelList;
  },
  set(newVal) {
    emits("update:modelList", newVal);
  },
});

const paramSearch = computed({
  get() {
    return props.modelParam;
  },
  set(newVal) {
    emits("update:modelParam", newVal);
  },
});

const modelOpenPopup = computed({
  get() {
    return props.openPopup;
  },
  set(newVal) {
    emits("update:openPopup", newVal);
  },
});

const itemClassName = computed(() => {
  switch (props.paneType) {
    case SearchPaneType.Group:
      return "default group-icon";
    case SearchPaneType.MultiEntity:
      return "default entity-icon";
    case SearchPaneType.Resource:
      return "card-round-style";
    default:
      return "default";
  }
});

const checkExist = computed(() => (itemUnique: string) => {
  return !![...props.listItemNotDrag].find(
    (item: any) => item.itemUnique === itemUnique
  );
});

//TODO
// const currentInput = computed({
//   get() {
//     return props.modelInputText;
//   },
//   set(newVal) {
//     emits("update:modelInputText", newVal);
//   },
// });

const handleSearch = () => {
  calcTotalItem();
  emits("onSearch", totalItem.value, true, pageChange.value);
};

const handleResetSearch = () => {
  calcTotalItem();
  emits("onReset", totalItem.value);
};

const onClickLeft = () => {
  emits("onClickFloatLeft");
};

const onClickRight = () => {
  emits("onClickFloatRight");
};

const closePopup = () => {
  emits("closePopup");
};

const submitPopup = () => {
  emits("submitPopup");
};

const handleChangePage = (page) => {
  emits("onChangePage", page);
};

const onClickItem = (item) => {
  emits("onClickItem", item);
};

const onClickShowDetail = (item, event) => {
  emits("onClickShowDetail", item, event);
};

const handleDragStart = (event, item) => {
  emits("onDragStart", { event: event, item: item });
};

const handleDragEnd = () => {
  isDragging.value = false;
  emits("onDragEnd");
};

const validate = () => {
  typeSelectScroll?.value?.validate();
  subTypeSelectScroll?.value?.validate();
};

const resetValidate = () => {
  typeSelectScroll.value?.resetValidate();
  subTypeSelectScroll?.value?.resetValidate();
};

const calcTotalItem = () => {
  if (
    props.itemHeight ||
    (itemElement.value &&
      itemElement.value[0]?.clientHeight &&
      scrollComponent.value)
  ) {
    const containerHeight =
      swapperSearch.value.clientHeight - formContainer.value.clientHeight;
    const itemHeight = props.itemHeight
      ? props.itemHeight
      : itemElement.value[0]?.clientHeight;
    const footerHeight = searchPaneFooter.value?.clientHeight ?? 0;
    totalItem.value =
      Math.max(
        Math.floor(
          (containerHeight -
            36 -
            16 -
            (props.showPagination ? 64 : 24) -
            footerHeight) /
            (itemHeight + 12)
        ),
        1
      ) *
      (props.paneType === SearchPaneType.FactorTable
        ? 2
        : Number(props.paneCol));
    if (
      props.showPagination &&
      props.pagination.totalItems &&
      props.pagination.totalPages > 0
    ) {
      pageChange.value =
        totalItem.value * props.pagination.currentPage >
        props.pagination.totalItems
          ? Math.ceil(props.pagination.totalItems / totalItem.value)
          : props.pagination.currentPage;
    }
  }
};

const calcLocomotiveMaxHeight = () => {
  const containerHeight =
    swapperSearch.value.clientHeight - formContainer.value.clientHeight;
  const footerHeight = searchPaneFooter.value?.clientHeight ?? 0;
  if (scrollComponent.value) {
    const maxHeight =
      containerHeight - (props.showPagination ? 64 : 24) - footerHeight - 36;
    locoMaxheight.value = `${maxHeight}px`;
  }
};

const excuteSearch = async () => {
  calcTotalItem();
  emits("onSearch", totalItem.value, false, pageChange.value);
};

const debounceEmit = debounce(excuteSearch, 500);

const handleLazyLoad = () => {
  emits("onLazyLoad");
};

const handleChangeType = () => {
  if (paramSearch.value.subType !== undefined) {
    paramSearch.value.subType = SPACE;
  }
};

watch(
  () => height.value,
  async () => {
    if (!dataList.value?.length) return;
    await calcLocomotiveMaxHeight();
    debounceEmit();
  }
);

onMounted(() => {
  calcLocomotiveMaxHeight();
  if (dataList.value?.length) {
    calcTotalItem();
    emits("onSearch", totalItem.value, false, pageChange.value);
  }
});

defineExpose({
  totalItem,
  pageChange,
  validate,
  resetValidate,
  calcTotalItem,
  handleSearch,
});
</script>

<style scoped lang="scss">
:deep() .highlight {
  background-color: yellow;
}
</style>
