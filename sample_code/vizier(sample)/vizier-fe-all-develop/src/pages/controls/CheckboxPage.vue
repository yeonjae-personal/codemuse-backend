<script setup lang="ts">

import CfCheckbox from "@/components/controls/CfCheckbox.vue";
import Prism from "vue-prism-component";
import cfCheckboxExample from "@/components/controls/examples/CfCheckboxExample";
// import Description from "@/components/Description/index.vue";

const sourceCode = ref(cfCheckboxExample);

const singleCheckbox = ref(true);
const selectedItems = ref<string[]>([]);
const checkboxOptions = ref([
  { level: "Dev", userid: "1" },
  { level: "Manager", userid: "2" },
  { level: "PM", userid: "3", disabled: true },
]);

const detectChange = (value: any) => {
  singleCheckbox.value = value;
};

const detectChangeGroup = ({ value, item }: { value: any; item: any }) => {
  if (value) {
    selectedItems.value.push(item.level);
  } else {
    selectedItems.value = selectedItems.value.filter(
      (val: any) => val !== item.level
    );
  }
};
</script>
<template>
  <div class="p-4 mx-auto prose md:px-6 prose-indigo sm:rounded-md">
    <!-- <description name="Checkbox" /> -->

    <h2 class="truncate text-base font-medium leading-7 text-slate-900">
      Examples
    </h2>

    <div class="flex flex-col">
      <cf-checkbox
        :model="singleCheckbox"
        label="Single Checkbox"
        @on-change="detectChange"
      ></cf-checkbox>
      Result: {{ singleCheckbox }}
      <div class="flex">
        <cf-checkbox
          v-model="selectedItems"
          :is-group="true"
          :options="checkboxOptions"
          label-group="level"
          value-group="userid"
          @on-change-group="detectChangeGroup"
        ></cf-checkbox>
      </div>
      Result: {{ selectedItems }}
    </div>
    <h2 class="truncate text-base font-medium leading-7 text-slate-900">
      Source Code
    </h2>
    <prism language="html">{{ sourceCode }}</prism>
  </div>
</template>
