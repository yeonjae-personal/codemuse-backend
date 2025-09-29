<template>
  <div class="flex bg-[#F0F2F5] rounded-full p-[6px] w-[82px]">
    <div
      class="cursor-pointer rounded-full w-[32px] h-[32px] flex justify-center items-center text-[#6B6D70]"
      :class="{
        'active-icon': internalValue == items.leftItemValue,
      }"
      @click="emitUpdateModel(items.leftItemValue)"
    >
      <slot name="iconLeft"></slot>
    </div>
    <div
      class="flex justify-center items-center cursor-pointer rounded-full w-[32px] h-[32px] text-[#6B6D70] ml-[6px]"
      :class="{
        'active-icon': internalValue == items.rightItemValue,
      }"
      icon
      @click="emitUpdateModel(items.rightItemValue)"
    >
      <slot name="iconRight"></slot>
    </div>
  </div>
</template>

<script setup lang="ts">
const props = defineProps({
  model: {
    type: String,
    required: true,
  },
  items: {
    type: Object,
    required: true,
  },
});
const internalValue = ref(props.model);

const emit = defineEmits(["update:model"]);
const emitUpdateModel = (value: any) => {
  internalValue.value = value;
  emit("update:model", internalValue.value);
};
</script>

<style scoped lang="scss">
.active-icon {
  background-color: $bg-color-1;
  transition: ease-in-out 0.6s;
  :deep(svg > path) {
    fill: $color-3;
  }
}
</style>
