const cfCheckboxExample = `<script setup lang="ts">
import { ref } from 'vue';
import CfCheckbox from '@/components/controls/CfCheckbox.vue';

const singleCheckbox = ref(true);
const selectedItems = ref<string[]>([]);
const checkboxOptions = ref([
  { level: 'Dev', userid: '1' },
  { level: 'Manager', userid: '2' },
  { level: 'PM', userid: '3', disabled: true },
]);

const detectChange = (value: any) => {
  singleCheckbox.value = value;
};

const detectChangeGroup = ({ value, item }: { value: any; item: any }) => {
  if (value) {
    selectedItems.value.push(item.level);
  } else {
    selectedItems.value = selectedItems.value.filter((val: any) => val !== item.level);
  }
};
</script>
<template>
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
</template>
`;

export default cfCheckboxExample;
