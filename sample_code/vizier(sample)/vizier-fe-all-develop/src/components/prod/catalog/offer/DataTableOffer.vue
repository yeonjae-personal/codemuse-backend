<template>
  <div class="flex flex-column mx-6 mb-6" style="width: calc(100% - 48px)">
    <DataTable
      :key="componentKey"
      v-model:pageSize="itemsPerPage"
      :headers="headerColumns"
      :data="computedData"
      :pagination="{
        currentPage: localCurrentPage,
        pageSize: itemsPerPage,
        totalPages,
      }"
      :total-search-items="totalSearchItems"
      :disable-change="disableChange"
      is-dynamic-table
      @on-change-page="handleChangePage"
    >
      <template #[`LB00000137`]="{ item, width, align }">
        <div :style="{ width: width, textAlign: align }" class="p-4">
          <CustomTooltip :content="item?.LB00000137">
            <span v-html="highlightedName(item?.LB00000137)" />
          </CustomTooltip>
        </div>
      </template>
      <template #[`LB00000136`]="{ item, width, align }">
        <div :style="{ width: width, textAlign: align }" class="p-4">
          <CustomTooltip :content="item?.LB00000136">
            <span
              class="cursor-pointer hover:underline"
              :style="{ width: width, textAlign: align }"
              @click="clickDetail(item)"
              v-html="highlightedCode(item?.LB00000136)"
            />
          </CustomTooltip>
        </div>
      </template>
    </DataTable>
  </div>
</template>
<script setup lang="ts">
import { useI18n } from "vue-i18n";
import { useGroupCode } from "@/composables/useGroupCode";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import { useProductsStore, useSnackbarStore } from "@/store";
import { formatDate, formatDateWithOutSeconds } from "@/utils/format-data";
import cloneDeep from "lodash-es/cloneDeep";
import type { TableHeader } from "@/types/common";

const emit = defineEmits([
  "clickDetail",
  "update:pageSize",
  "update:currentPage",
]);

const props = defineProps({
  productType: { type: String, default: "" },
  data: { type: Array as PropType<any[]>, default: () => [] },
  totalPages: { type: Number, default: 0 },
  currentPage: { type: Number, default: 1 },
  totalSearchItems: { type: Number, default: 1 },
  textSearch: { type: String, default: "" },
  searchField: { type: String, default: "name" },
  pageSize: { type: Number, default: 10 },
  disableChange: { type: Boolean, default: false },
});

const { t, locale } = useI18n();
const snackbarStore = useSnackbarStore();
const productStore = useProductsStore();
const { groupCodeData, search, getTextDisplay } = useGroupCode();
const { isSearchTableData } = storeToRefs(useProductsStore());

const localGroupCodeData = ref<any>();
const headerColumns = ref<TableHeader[]>([]);
const computedData = ref<any[]>([]);
const componentKey = ref<number>(0);

const itemsPerPage = computed<number>({
  get: () => props.pageSize,
  set: (newVal) => {
    emit("update:pageSize", newVal);
  },
});

const localCurrentPage = computed<number>({
  get: () => props.currentPage,
  set: (newVal) => {
    emit("update:currentPage", newVal);
  },
});

watch(
  () => headerColumns.value,
  async (newVal: any, oldVal: any) => {
    if (newVal?.length && JSON.stringify(newVal) !== JSON.stringify(oldVal)) {
      const commonCodeList = newVal.reduce((codeArr, item) => {
        if (
          [COLUMN_FIELD_TYPE.DL, COLUMN_FIELD_TYPE.DM].includes(
            item.fieldTypeCode
          ) &&
          item?.commGroupCode
        ) {
          codeArr.push(item.commGroupCode);
        }
        return codeArr;
      }, []);
      if (commonCodeList?.length) {
        await search(commonCodeList);
        localGroupCodeData.value = cloneDeep(groupCodeData.value);
        computedData.value = handleGetData();
      }
    }
  },
  { immediate: true, deep: true }
);

watch(locale, (value) => {
  if (value) {
    initData();
  }
});

watch(
  () => itemsPerPage.value,
  () => {
    handleChangePage(1);
  }
);

watch(
  () => isSearchTableData.value,
  async (val) => {
    if (val) {
      await initData();
      nextTick(() => {
        componentKey.value++;
      });
      isSearchTableData.value = false;
    }
  }
);

onBeforeMount(() => {
  initData();
});

const initData = async () => {
  if (props.productType) {
    try {
      const { data } = await productStore.initProductCreate({
        itemCode: props.productType,
      });
      if (data) {
        headerColumns.value = [
          ...data?.general?.filter(
            (del) =>
              del.colName !== "item_code" &&
              del.fieldTypeCode !== COLUMN_FIELD_TYPE.HD
          ),
          ...data?.additional,
        ].map((item) => ({
          ...item,
          title:
            item.labelId === "categoryNode"
              ? t(`product_platform.${item.labelId}`)
              : t(item.labelId),
          key: item.labelId,
          align: "start",
          sortable: false,
          width: "160px",
        }));
        headerColumns.value.unshift({
          title: t("product_platform.no"),
          align: "start",
          sortable: false,
          key: "no",
          width: "80px",
        });
      }
      computedData.value = handleGetData();
    } catch (err: any) {
      snackbarStore.showSnackbar(
        err?.errorMsg || t("product_platform.something_went_wrong"),
        "error"
      );
    }
  }
};

const handleGetData = (): any[] => {
  let list: any[] = cloneDeep(props.data);
  let result = list.map((item, index) => {
    let combinedObj = {
      no: index + 1 + (localCurrentPage.value - 1) * itemsPerPage.value,
    };

    item.additional.forEach((subItem) => {
      if (subItem?.attrRefTableName && subItem?.tableColumns?.length) {
        combinedObj[subItem.labelId] = getTextDisplay(
          subItem.attrVal,
          subItem.fieldTypeCode,
          subItem?.tableColumns ?? []
        );
      } else if (
        [COLUMN_FIELD_TYPE.DL, COLUMN_FIELD_TYPE.DM].includes(
          subItem.fieldTypeCode
        ) &&
        localGroupCodeData.value &&
        localGroupCodeData.value[subItem.commGroupCode]?.length
      ) {
        combinedObj[subItem.labelId] = getTextDisplay(
          subItem.attrVal,
          subItem.fieldTypeCode,
          localGroupCodeData?.value[subItem?.commGroupCode] || []
        );
      } else if (subItem.fieldTypeCode === COLUMN_FIELD_TYPE.DP) {
        combinedObj[subItem.labelId] =
          formatDateWithOutSeconds(subItem.attrVal) || "-";
      } else {
        combinedObj[subItem.labelId] = subItem.attrVal || "-";
      }
    });

    item.general.forEach((subItem) => {
      if (
        subItem.fieldTypeCode !== COLUMN_FIELD_TYPE.HD &&
        subItem.colName !== "item_code"
      ) {
        if (subItem?.attrRefTableName && subItem?.tableColumns?.length) {
          combinedObj[subItem.labelId] = getTextDisplay(
            subItem.attrVal,
            subItem.fieldTypeCode,
            subItem?.tableColumns ?? []
          );
        } else if (
          [COLUMN_FIELD_TYPE.DL, COLUMN_FIELD_TYPE.DM].includes(
            subItem.fieldTypeCode
          ) &&
          localGroupCodeData.value &&
          localGroupCodeData?.value[subItem.commGroupCode]?.length
        ) {
          combinedObj[subItem.labelId] = getTextDisplay(
            subItem.attrVal,
            subItem.fieldTypeCode,
            localGroupCodeData?.value[subItem?.commGroupCode] || []
          );
        } else if (subItem.fieldTypeCode === COLUMN_FIELD_TYPE.DP) {
          combinedObj[subItem.labelId] = formatDate(subItem.attrVal) || "-";
        } else {
          combinedObj[subItem.labelId] = subItem.attrVal || "-";
        }
      }
    });
    return { ...item, ...combinedObj };
  });
  return result;
};

const highlightedName = (name: string) => {
  if (!props.textSearch || props.searchField !== "name") return name;
  // eslint-disable-next-line security/detect-non-literal-regexp
  const regex = new RegExp(`(${props.textSearch})`, "gi");
  return name.replace(regex, '<span class="highlight">$1</span>');
};

const highlightedCode = (codeVal: string) => {
  if (!props.textSearch || props.searchField !== "code") return codeVal;
  // eslint-disable-next-line security/detect-non-literal-regexp
  const regex = new RegExp(`(${props.textSearch})`, "gi");
  return codeVal.replace(regex, '<span class="highlight">$1</span>');
};

const clickDetail = (item: any) => {
  emit("clickDetail", item);
};

const handleChangePage = (page: number): void => {
  localCurrentPage.value = page;
};
</script>

<style scoped lang="scss">
:deep(.highlight) {
  background-color: yellow;
}
</style>
