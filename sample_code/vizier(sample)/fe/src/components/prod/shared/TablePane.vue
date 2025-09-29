<template>
  <div
    class="bg-white relative py-6"
    :class="[containerClass, `col-span-${paneCol}`]"
  >
    <div class="flex flex-col h-full">
      <div class="px-6 grid grid-cols-2 gap-4 w-full">
        <div class="flex justify-between items-center order-2">
          <div class="flex justify-between w-full">
            <SearchAndRefreshButton
              @handle-search="handleSearch"
              @handle-refresh="handleResetSearch()"
            />
            <template v-if="slots['header-search-pane']">
              <slot name="header-search-pane"></slot>
            </template>
            <div class="h-10 ml-2 py-4 flex items-center justify-center">
              <FileAction
                class-name="mr-2"
                title="Upload Components"
                description="Please upload Component excel file that you have downloaded."
                :is-downloading="downloading"
                @upload-file="handleUploadComponent"
                @download-file="handleDownloadDataTable"
              />
              <SwitchViewTable
                v-model="tableViewMode"
                class="ms-auto"
                @update:model-value="handleChangeViewMode"
              />
            </div>
          </div>
        </div>
        <template v-if="slots['custom-form']">
          <slot name="custom-form"></slot>
        </template>
        <v-form
          v-else-if="paneType"
          ref="form"
          class="w-full grid gap-2"
          :class="[formClass, 'grid-cols-2 order-1']"
          @submit.prevent=""
        >
          <div
            class="grid grid-cols-1 gap-2"
            :class="paramSearch.subType ? 'grid-cols-2' : 'grid-cols-1'"
          >
            <BaseSelectScroll
              ref="typeSelectScroll"
              v-model="paramSearch.type"
              :options="optionTypes"
              :height="48"
              :placeholder="$t(`product_platform.Type`)"
              :required="typeSelectRequire"
              :default-item-select-all="false"
            />
            <BaseSelectScroll
              v-if="paramSearch.subType"
              ref="subTypeSelectScroll"
              v-model="paramSearch.subType"
              :options="optionSubTypes"
              :height="48"
              :placeholder="$t(`product_platform.Type`)"
              :required="subTypeSelectRequire"
              :default-item-select-all="!subTypeSelectRequire"
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
                @keyup.enter="handleSearch"
              />
            </div>
          </div>
        </v-form>
      </div>
      <template v-if="slots['custom-search-main-content']">
        <slot name="custom-search-main-content"></slot>
      </template>
      <div v-else class="w-full h-full px-6 pb-3">
        <DataTable
          :key="dataTableKey"
          v-model:pageSize="tablePagination.pageSize"
          :headers="headerColumns"
          :data="dataTable"
          :pagination="pagination"
          :table-class-name="'h-[calc(100vh-300px)]'"
          :total-search-items="pagination.totalItems"
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
                    paramSearch.searchBy === SEARCH_BY_OPTIONS[1].value
                      ? highlightText(
                          item?.LB00000136 || '',
                          paramSearch.searchKey || ''
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
                    paramSearch.searchBy === SEARCH_BY_OPTIONS[0].value
                      ? highlightText(
                          item?.LB00000137 || '',
                          paramSearch.searchKey || ''
                        )
                      : item?.LB00000137
                  "
                />
              </CustomTooltip>
            </div>
          </template>
        </DataTable>
      </div>
    </div>
    <BasePopup
      v-model="openPopup"
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
import { Slots } from "vue";
import { ColNumber, DialogIconType, SearchPaneType } from "@/enums";
import { SEARCH_BY_OPTIONS } from "@/constants/";
import {
  BasePaginationType,
  BaseSearchPaneParam,
  BaseSearchPaneParamClass,
  TableHeader,
} from "@/types/common";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import { useGroupCode } from "@/composables/useGroupCode";
import { formatDateWithOutSeconds, highlightText } from "@/utils/format-data";
import { useI18n } from "vue-i18n";

type Props = {
  paneType?: SearchPaneType;
  paneCol?: ColNumber;
  containerClass?: String | Array<string>;
  formClass?: String | Array<string>;
  showFloatIconLeft?: boolean;
  showFloatIconRight?: boolean;
  modelList?: Array<any>;
  pagination?: BasePaginationType;
  showPagination?: boolean;
  popupContent?: string;
  modelParam?: BaseSearchPaneParam;
  optionTypes?: Array<any>;
  optionSubTypes?: Array<any>;
  typeSelectRequire?: boolean;
  subTypeSelectRequire?: boolean;
  isLoading?: boolean;
  downloading?: boolean;
  groupCodeData?: Object;
  viewMode?: string;
};

const props = withDefaults(defineProps<Props>(), {
  paneType: SearchPaneType.Offer,
  paneCol: ColNumber.One,
  showPagination: true,
  popupContent: "",
  typeSelectRequire: true,
  subTypeSelectRequire: false,
  isLoading: false,
  downloading: false,
  viewMode: "",
  modelParam: () => new BaseSearchPaneParamClass(),
  optionTypes: () => [],
  optionSubTypes: () => [],
  containerClass: () => [],
  formClass: () => [],
  modelList: () => [] as any[],
  pagination: () => ({}) as BasePaginationType,
  groupCodeData: () => ({}) as any,
});

const emits = defineEmits([
  "onSearch",
  "onReset",
  "closePopup",
  "submitPopup",
  "onChangePage",
  "onChangePageSize",
  "onDownload",
  "update:modelList",
  "update:modelParam",
  "update:pagination",
  "update:viewMode",
]);

interface CustomSlots extends Slots {
  "header-search-pane"?: () => VNode[];
  "search-button-append"?: () => VNode[];
  "custom-form"?: () => VNode[];
  "custom-search-main-content"?: () => VNode[];
  "custom-search-item"?: () => VNode[];
  default?: () => VNode[];
}

const { t } = useI18n();
const { getTextDisplay, search } = useGroupCode();
const slots = useSlots() as CustomSlots;
const openPopup = ref(false);
const dataTableKey = ref(Math.random());
const listCommonCode = ref<string[]>([]);
const typeSelectScroll = ref();
const subTypeSelectScroll = ref();

const headerColumns = computed(() => {
  const columns: TableHeader[] = [
    {
      title: t("product_platform.no"),
      key: "no",
      width: "80px",
    },
  ];
  //Filter ignore hidden field general
  const generalCols =
    props.modelList?.[0]?.general?.filter(
      (item) => item.fieldTypeCode !== COLUMN_FIELD_TYPE.HD
    ) || [];
  // additional field
  const additionalCols = props.modelList?.[0]?.additional || [];
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
  const rowsData = props.modelList?.map((row, index) => {
    const pagination = props.pagination;
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
        props.groupCodeData &&
        props.groupCodeData[item.commGroupCode]?.length
      ) {
        columns[item.labelId] = getTextDisplay(
          item.attrVal,
          item.fieldTypeCode,
          props.groupCodeData[item?.commGroupCode] || []
        );
      } else if (item.fieldTypeCode === COLUMN_FIELD_TYPE.DP) {
        columns[item.labelId] = formatDateWithOutSeconds(item.attrVal) || "-";
      } else if (item.colName === "item_code") {
        const options =
          props.paneType === SearchPaneType.Component
            ? props.optionSubTypes
            : props.optionTypes;
        columns[item.labelId] =
          options?.find((subType: any) => subType.cmcdDetlId === item.attrVal)
            ?.cmcdDetlNm || "-";
      } else {
        columns[item.labelId] = item.attrVal || "-";
      }
    });
    return columns;
  });
  return rowsData;
});

const paramSearch = computed({
  get() {
    return props.modelParam;
  },
  set(newVal) {
    emits("update:modelParam", newVal);
  },
});

const tablePagination = computed({
  get() {
    return props.pagination;
  },
  set(newVal) {
    emits("update:pagination", newVal);
  },
});

const tableViewMode = computed({
  get() {
    return props.viewMode;
  },
  set(newVal) {
    emits("update:viewMode", newVal);
  },
});

const handleSearch = () => {
  emits("onSearch");
};

const handleResetSearch = () => {
  emits("onReset");
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

const handleChangeSize = () => {
  emits("onChangePageSize");
};

const handleUploadComponent = () => {};
const handleDownloadDataTable = async () => {
  emits("onDownload");
};
const handleChangeViewMode = (value) => {
  emits("update:viewMode", value);
};

watch(
  () => listCommonCode.value,
  async (newVal) => {
    if (newVal?.length) {
      await search(newVal);
    }
  },
  { deep: true }
);

const validate = () => {
  typeSelectScroll?.value?.validate();
  subTypeSelectScroll?.value?.validate();
};

const resetValidate = () => {
  typeSelectScroll.value?.resetValidate();
  subTypeSelectScroll?.value?.resetValidate();
};

defineExpose({
  validate,
  resetValidate,
});
</script>

<style scoped lang="scss">
:deep() .highlight {
  background-color: yellow;
}
</style>
