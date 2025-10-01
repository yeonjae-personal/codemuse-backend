<template>
  <div ref="container" class="flex h-full">
    <template v-if="viewType === VIEW_MODE.GRID">
      <FourColumns>
        <ConditionSearch />
        <CustomValidationGrid @change-view="handleChangeView" />
        <History v-if="showHistory" :valid-code="validCode" />
        <ActionSearch v-else />
      </FourColumns>
    </template>
    <template v-else>
      <CustomValidationTable @change-view="handleChangeView" />
    </template>
  </div>
</template>

<script setup lang="ts">
import ActionSearch from "./subs/custom-validation/ActionSearch.vue";
import History from "./subs/custom-validation/History.vue";
import ConditionSearch from "./subs/custom-validation/ConditionSearch.vue";
import CustomValidationGrid from "./subs/custom-validation/CustomValidationGrid.vue";
import customValidationStore from "@/store/admin/customValidation.store";
import { VIEW_MODE } from "@/constants/";
import CustomValidationTable from "./subs/custom-validation/CustomValidationTable.vue";

const { showHistory, validCode, viewType } = storeToRefs(
  customValidationStore()
);

const handleChangeView = (value: string): void => {
  viewType.value = value;
};
</script>
