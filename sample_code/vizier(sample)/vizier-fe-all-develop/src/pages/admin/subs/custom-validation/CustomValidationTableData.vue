<template>
  <DataTable
    v-model:pageSize="pageSize"
    :headers="headerColumns"
    :data="customValidationItems"
    :pagination="{
      currentPage: currentPageNum,
      pageSize,
      totalPages: Math.ceil(totalItems / pageSize),
    }"
    :disable-change="isLoading"
    :is-show-total-search="false"
    @on-change-page="handleChangePage"
  >
    <template #row="{ item }">
      <td class="d-flex items-center">
        <div class="data-cell px-6 py-[10px] !w-[79px]">{{ item.no }}</div>
      </td>
      <template v-if="!!item.condition.length">
        <td class="d-flex items-center">
          <div class="data-cell">
            <CustomTooltip :content="item.condition?.[0]?.conditionItem" />
          </div>
        </td>
        <td class="d-flex items-center flex-col justify-center">
          <div
            v-for="(cond, index) in item.condition"
            :key="index"
            class="data-cell"
            :class="index !== 0 ? 'divider' : ''"
          >
            <CustomTooltip :content="$t(cond.conditionAttribute)" />
          </div>
        </td>
        <td class="d-flex items-center flex-col justify-center">
          <div
            v-for="(cond, index) in item.condition"
            :key="index"
            class="data-cell"
            :class="index !== 0 ? 'divider' : ''"
          >
            <CustomTooltip :content="cond.conditionValidation" />
          </div>
        </td>
      </template>
      <!-- No condition -->
      <template v-else>
        <td class="d-flex items-center">
          <div class="data-cell"></div>
        </td>
        <td class="d-flex items-center flex-col justify-center">
          <div class="data-cell"></div>
        </td>
        <td class="d-flex items-center flex-col justify-center">
          <div class="data-cell"></div>
        </td>
      </template>
      <!-- Action -->
      <template v-if="!!item.action.length">
        <td class="d-flex items-center">
          <div class="data-cell">
            <CustomTooltip :content="item.action?.[0]?.actionItem" />
          </div>
        </td>
        <td class="d-flex items-center flex-col justify-center">
          <div
            v-for="(act, index) in item.action"
            :key="index"
            class="data-cell"
            :class="index !== 0 ? 'divider' : ''"
          >
            <CustomTooltip :content="$t(act.actionAttribute)" />
          </div>
        </td>
        <td class="d-flex items-center flex-col justify-center">
          <div
            v-for="(act, index) in item.action"
            :key="index"
            class="data-cell"
            :class="index !== 0 ? 'divider' : ''"
          >
            <CustomTooltip :content="act.actionValidation" />
          </div>
        </td>
      </template>
      <!-- No action -->
      <template v-else>
        <td class="d-flex items-center">
          <div class="data-cell"></div>
        </td>
        <td class="d-flex items-center flex-col justify-center">
          <div class="data-cell"></div>
        </td>
        <td class="d-flex items-center flex-col justify-center">
          <div class="data-cell"></div>
        </td>
      </template>
      <td class="d-flex items-center">
        <div class="data-cell !w-[179px]">
          <CustomTooltip :content="item.registeredUser" />
        </div>
      </td>
      <td class="d-flex items-center">
        <div class="data-cell">
          <CustomTooltip :content="item.registeredDate" />
        </div>
      </td>
      <td class="d-flex items-center">
        <div class="data-cell !w-[179px]">
          <CustomTooltip :content="item.modifiedUser" />
        </div>
      </td>
      <td class="d-flex items-center">
        <div class="data-cell !w-[180px]">
          <CustomTooltip :content="item.modifiedDate" />
        </div>
      </td>
    </template>
  </DataTable>
</template>

<script setup lang="ts">
import { TableHeader } from "@/types/common";
import { useI18n } from "vue-i18n";

const props = defineProps({
  customValidationItems: {
    type: Array as PropType<any[]>,
    default: () => [],
  },
  searchBy: { type: String, default: "" },
  offerName: { type: String, default: "" },
  currentPage: { type: Number, default: 0 },
  totalItems: { type: Number, default: 0 },
  isLoading: { type: Boolean, default: false },
});

const emit = defineEmits(["pageSize", "currentPage"]);

const { t } = useI18n();

const pageSize = ref<number>(10);
const currentPageNum = ref<number>(props.currentPage);

const handleChangePage = (page) => {
  currentPageNum.value = page;
};

const headerColumns = computed<TableHeader[]>(() => [
  {
    title: t("product_platform.no"),
    key: "no",
    width: "80px",
    align: "start",
  },
  {
    title: t("product_platform.condition"),
    align: "start",
    key: "condition",
    children: [
      {
        title: t("product_platform.Item"),
        key: "conditionItem",
        align: "start",
        width: "160px",
      },
      {
        title: t("product_platform.attribute"),
        key: "conditionAttribute",
        align: "start",
        width: "160px",
      },
      {
        title: t("product_platform.validation"),
        key: "conditionValidation",
        align: "start",
        width: "160px",
      },
    ],
  },
  {
    title: t("product_platform.action"),
    key: "action",
    align: "start",
    children: [
      {
        title: t("product_platform.Item"),
        key: "actionItem",
        align: "start",
        width: "160px",
      },
      {
        title: t("product_platform.attribute"),
        key: "actionAttribute",
        align: "start",
        width: "160px",
      },
      {
        title: t("product_platform.validation"),
        key: "actionValidation",
        align: "start",
        width: "160px",
      },
    ],
  },
  {
    title: t("product_platform.registeredUser"),
    key: "registeredUser",
    align: "start",
    width: "180px",
  },
  {
    title: t("product_platform.registeredDate"),
    key: "registeredDate",
    align: "start",
    width: "160px",
  },
  {
    title: t("product_platform.modifiedUser"),
    key: "modifiedUser",
    width: "180px",
    align: "start",
  },
  {
    title: t("product_platform.modifiedDate"),
    key: "modifiedDate",
    width: "180px",
    align: "start",
  },
]);

watch(pageSize, (newValue) => {
  emit("pageSize", newValue);
  currentPageNum.value = 1;
});

watch(currentPageNum, (newValue) => {
  emit("currentPage", newValue);
});

watch(
  () => props.currentPage,
  (newValue) => {
    currentPageNum.value = newValue;
  }
);
</script>

<style lang="scss" scoped>
:deep(.highlight) {
  background-color: yellow;
}

.data-cell {
  padding: 10px 16px;
  min-height: 52px;
  display: flex;
  align-items: center;
  width: 159.3px;
  box-sizing: border-box;
  font-family: Noto Sans KR;
  font-weight: 400;
  font-size: 13px;
  line-height: 20px;
  letter-spacing: 0.25px;
  vertical-align: middle;
  color: #3a3b3d;

  > span {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
}

.divider {
  border-top: 1px solid #f0f2f5;
}

:deep(.data-table-body__row) {
  min-height: 52px;
  height: auto;
}

:deep(.no-data) {
  height: 52px;
}

td {
  &:not(:last-of-type) {
    border-right: 1px solid #f0f2f5;
  }
}
</style>
