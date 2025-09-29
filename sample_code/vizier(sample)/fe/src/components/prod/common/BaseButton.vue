<script setup lang="ts">
import { WIDTH_BUTTON, HEIGHT_BUTTON } from "@/constants/index";
import { ButtonColorType, ButtonSizeType } from "@/enums";
defineProps({
  size: {
    type: String as PropType<ButtonSizeType>,
    default: "",
  },
  rounded: {
    type: String as PropType<"0" | "xs" | "sm" | "lg" | "xl">,
    default: "lg",
  },
  color: {
    type: String as PropType<ButtonColorType | string>,
    default: ButtonColorType.Primary,
  },
  height: {
    type: String,
    default: HEIGHT_BUTTON.DEFAULT,
  },
  width: {
    type: String,
    default: WIDTH_BUTTON.DEFAULT,
  },
  disabled: { type: Boolean, default: false },
});

const classColorButton = {
  [ButtonColorType.Primary]:
    "!text-white !bg-bg-primary hover:!bg-primary-darker",
  [ButtonColorType.Secondary]:
    "!text-text-primary !bg-primary-lightest hover:!bg-primary-lighter",
  [ButtonColorType.Gray]:
    "border border-base !text-text-lighter !bg-white hover:!bg-lighter",
  [ButtonColorType.Blank]:
    "!bg-transparent !shadow-none !text-text-lighter hover:!bg-lighter",
};

const slots = defineSlots();
</script>

<template>
  <v-btn
    class="button v-btn--size-default !shadow-none"
    :class="classColorButton[color]"
    :size="size"
    :rounded="rounded"
    :height="height"
    :min-height="height"
    :width="width"
    :min-width="width"
    :disabled="disabled"
  >
    <slot>Default Button</slot>
    <template v-if="slots.append" #append>
      <slot name="append"></slot>
    </template>
  </v-btn>
</template>

<style scoped>
.button {
  display: inline-flex;
  justify-content: center;
  align-items: center;
  border: none;
  text-transform: unset;
}

:deep(.v-btn__content) {
  font-family: "Noto Sans KR", sans-serif !important;
  font-weight: 500;
  font-size: 13px;
  letter-spacing: 0.5px;
}
</style>
