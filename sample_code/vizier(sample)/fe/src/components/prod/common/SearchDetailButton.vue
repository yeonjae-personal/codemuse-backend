<template>
  <BaseButton
    :width="WIDTH_BUTTON.ICON"
    :color="ButtonColorType.Gray"
    :disabled="disabled"
  >
    <SearchDetailActiveIcon
      v-if="isActive"
      :fill="disabled ? '#bdc1c7' : '#6B6D70'"
    />
    <SearchDetailIcon v-else :fill="disabled ? '#bdc1c7' : '#6B6D70'" />
    <div
      v-if="isActive"
      class="w-[6px] h-[6px] bg-[#D9325A] rounded-[999px] absolute top-[4px] right-[4px]"
    ></div>
  </BaseButton>
</template>

<script setup lang="ts">
import { WIDTH_BUTTON } from "@/constants/index";
import { ButtonColorType } from "@/enums";

defineProps({
  isActive: { type: Boolean, default: false },
  disabled: { type: Boolean, default: false },
});
</script>
