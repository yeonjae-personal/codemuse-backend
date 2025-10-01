<template>
  <FourColumns ref="container" class="h-full">
    <GridColumn v-if="isShowRuleList" :span="1">
      <RuleSearch />
    </GridColumn>
    <GridColumn v-if="selectedRule && isShowRuleDetail" :span="1">
      <RuleDetail />
    </GridColumn>
    <GridColumn v-if="isShowRuleStructure" :span="ruleStructureSpan">
      <RuleStructure />
    </GridColumn>
    <GridColumn v-if="isShowRuleField" :span="1">
      <FieldList />
    </GridColumn>
    <GridColumn v-if="isShowRuleTest" :span="1">
      <TestInput />
    </GridColumn>
    <GridColumn v-if="isShowRuleReport" :span="1">
      <RuleReport />
    </GridColumn>
  </FourColumns>
</template>

<script lang="ts" setup>
import FieldList from "@/components/admin/rule-engine/FieldList.vue";
import RuleStructure from "@/components/admin/rule-engine/rule-structure/RuleStructure.vue";
import RuleDetail from "@/components/admin/rule-engine/RuleDetail.vue";
import RuleReport from "@/components/admin/rule-engine/RuleReport.vue";
import RuleSearch from "@/components/admin/rule-engine/RuleSearch.vue";
import TestInput from "@/components/admin/rule-engine/TestInput.vue";
import useRuleEngineStore from "@/store/admin/ruleEngine.store";
const ruleEngineStore = useRuleEngineStore();
const {
  selectedRule,
  isShowRuleStructure,
  isShowRuleList,
  isShowRuleField,
  isShowRuleTest,
  isShowRuleDetail,
  isShowRuleReport,
} = storeToRefs(ruleEngineStore);
const ruleStructureSpan = computed(() => {
  let count = 0;
  if (isShowRuleList.value) count++;
  if (isShowRuleField.value) count++;
  if (isShowRuleTest.value) count++;
  if (isShowRuleDetail.value) count++;
  if (isShowRuleReport.value) count++;
  return count === 0 ? 4 : count === 1 ? 3 : 2;
});
</script>
