<script setup lang="ts">
import { HEIGHT_BUTTON, WIDTH_BUTTON } from "@/constants/index";
import { ButtonColorType } from "@/enums";
defineProps({
  width: {
    type: String,
    default: WIDTH_BUTTON.ICON,
  },
  height: {
    type: String,
    default: HEIGHT_BUTTON.DEFAULT,
  },
});
</script>

<template>
  <BaseButton :color="ButtonColorType.Gray" :width="width" :height="height">
    <ResetIcon fill="#6B6D70" />
  </BaseButton>
</template>
