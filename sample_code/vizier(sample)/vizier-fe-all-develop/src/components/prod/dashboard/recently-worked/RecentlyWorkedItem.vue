<template>
  <div class="recently-worked-table">
    <div class="row-head">
      <div>{{ t("product_platform.dashboard.type") }}</div>
      <div>{{ t("product_platform.dashboard.itemName") }}</div>
      <div>{{ t("product_platform.dashboard.work") }}</div>
      <div>{{ t("product_platform.dashboard.dateTime") }}</div>
    </div>
    <div v-for="item in data" :key="item.id" class="row">
      <div>{{ item.type }}</div>
      <div>{{ item.name }}</div>
      <div>
        <span class="label" :class="`type-${item.workCode}`">
          {{ item.work }}
        </span>
      </div>
      <div>{{ item.date }}</div>
    </div>
  </div>
</template>
<script setup>
import { UI_DASHBOARD_RECENTLYWORKED } from "@/api/prod/path";
import { httpClient } from "@/utils/http-common";
import { useI18n } from "vue-i18n";

const { locale, t } = useI18n();
const data = ref([]);
const fetchData = async () => {
  try {
    const response = await httpClient.get(UI_DASHBOARD_RECENTLYWORKED, {
      params: { view: "simple" },
    });
    data.value =
      response?.data?.map((item) => ({
        id: item.objUuid,
        type: item.type,
        name: item.objName,
        work: item.workTypeName,
        workCode: item.workTypeCode,
        date: item.workDate,
      })) || [];
  } catch {}
};

onMounted(() => {
  fetchData();
});

watch(
  () => locale.value,
  () => {
    fetchData();
  }
);
</script>
<style scoped lang="scss">
.recently-worked-table {
  display: flex;
  flex-direction: column;
  width: 100%;
  border-radius: 8px;
  border: 1px solid #e6e9ed;
  .row-head {
    display: flex;
    height: 48px;
    background: #f7f8fa;
    align-items: center;
    justify-content: space-between;
    font-size: 13px;
    font-weight: 500;
    width: 100%;
    border-radius: 8px 8px 0 0;
    > div {
      width: 23%;
      padding: 10px 8px;
      box-sizing: border-box;
      font-size: 13px;
    }
    > div:nth-child(2) {
      width: 31%;
    }
    > div:nth-child(3) {
      width: 21%;
    }
  }
  .row {
    display: flex;
    height: 52px;
    font-size: 13px;
    justify-content: space-between;
    align-items: center;
    border-top: 1px solid #f0f2f5;
    width: 100%;
    &:last-child {
      border-radius: 0 0 8px 8px;
    }
    > div {
      width: 23%;
      padding: 10px 8px;
      box-sizing: border-box;
      text-overflow: ellipsis;
      white-space: nowrap;
      overflow: hidden;
      font-size: 13px;
    }
    > div:nth-child(2) {
      width: 31%;
    }
    > div:nth-child(3) {
      width: 21%;
    }
    .label {
      font-size: 11px;
      padding: 4px 8px;
      border-radius: 4px;
    }
    .type-01 {
      background: #e8f4fc;
      color: #1570ef;
    }
    .type-02,
    .type-03 {
      background: #fef6ee;
      color: #e04f16;
    }
    .type-04 {
      background: #f0f2f5;
      color: #6b6d70;
    }
  }
}
</style>
