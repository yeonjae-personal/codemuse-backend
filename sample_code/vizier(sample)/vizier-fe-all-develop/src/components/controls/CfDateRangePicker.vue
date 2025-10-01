<script setup lang="ts">
import VueDatePicker from "@vuepic/vue-datepicker";
import "@vuepic/vue-datepicker/dist/main.css";

const props = defineProps({
  model: {
    type: null,
    required: true,
  },
  isInitDate: {
    type: Boolean,
    default: false,
  },
});

const internalValue = ref();
internalValue.value = props.model;

const format = ([start, end]) => {
  const formatSingleDate = (date: any) => {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");
    return `${year}-${month}-${day}`;
  };

  const startDate = formatSingleDate(start);
  const endDate = formatSingleDate(end);

  return `${startDate}~${endDate}`;
};

const emit = defineEmits(["update:model"]);
const emitUpdateModel = () => {
  emit("update:model", internalValue.value);
};
watch(
  () => props.isInitDate,
  () => (internalValue.value = [new Date(), new Date()])
);
</script>

<template>
  <div class="cf-datepicker">
    <VueDatePicker
      v-model="internalValue"
      range
      :format="format"
      multi-calendars
      position="left"
      @update:model-value="emitUpdateModel"
    ></VueDatePicker>
  </div>
</template>

<style>
.cf-datepicker {
  div > input {
    height: 40px !important;
  }
}
</style>
