<template>
  <div class="start-node">
    <div class="start-node__title">
      {{ t("product_platform.start") }}
    </div>
    <SingleConnectionLine
      :is-empty-rule="isEmptyStructure"
      :is-success="isTested"
      :class="{ 'is-hidden-arrow': isHiddenArrow }"
      @select-type="emit('select-type', $event)"
    />
  </div>
</template>

<script lang="ts" setup>
import { useI18n } from "vue-i18n";
import first from "lodash-es/first";
import SingleConnectionLine from "./SingleConnectionLine.vue";
import useRuleEngineStore from "@/store/admin/ruleEngine.store";

const emit = defineEmits(["select-type"]);
const { t } = useI18n();

const { ruleStructure, isEmptyStructure, isTested } =
  storeToRefs(useRuleEngineStore());

const isHiddenArrow = computed<boolean>(() => {
  const firstCondition = first(ruleStructure.value?.condition);
  return !!firstCondition && !!firstCondition?.logicType;
});
</script>

<style lang="scss" scoped>
.start-node {
  height: 77px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  &__title {
    display: flex;
    justify-content: center;
    align-items: center;
    flex-shrink: 0;
    width: 71px;
    border: 1px solid #2e90fa;
    background-color: #fff;
    box-shadow:
      0px 0px 0px 6px #7ba7ff29,
      0px 0px 0px 12px #bad4ff40;
    color: #1570ef;
    font-weight: 500;
    font-size: 13px;
    line-height: 150%;
    letter-spacing: 0.25px;
    padding: 8px;
    border-radius: 99px;
  }
}
</style>
