<template>
  <FourColumns>
    <TargetSearch :large-type-list="largeTypeList" />
    <OneView :large-type-list="largeTypeList" />
  </FourColumns>
</template>

<script lang="ts" setup>
import { getUiImpactAnalysisSelectItem } from "@/api/prod/impactAnalysisApi";
import TargetSearch from "@/components/prod/catalog/impact-analysis/TargetSearch.vue";
import OneView from "@/components/prod/catalog/impact-analysis/OneView.vue";
import { SPACE } from "@/constants/index";

const largeTypeList = ref<any[]>([]);
const gridViewParams = reactive({
  lctgrItemCode: SPACE,
  objName: "",
  objCode: "name",
});

provide("gridViewParams", gridViewParams);

onBeforeMount(async () => {
  const { data } = await getUiImpactAnalysisSelectItem();
  largeTypeList.value = data;
});
</script>
