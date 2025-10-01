<script setup lang="ts">
import { ChipType } from "@/enums";

defineProps({
  content: {
    type: String,
    required: true,
  },
  removable: {
    type: Boolean,
    default: false,
  },
  type: {
    type: String as PropType<ChipType>,
    default: ChipType.Gray,
  },
});

const classByType = {
  [ChipType.Gray]: "bg-base text-text-lighter",
  [ChipType.Green]: "bg-success-lighter text-text-success",
  [ChipType.Blue]: "bg-info-lighter text-text-info",
  [ChipType.Pink]: "bg-error-lighter text-text-error",
  [ChipType.LightPink]: "bg-primary-lightest text-text-primary",
};

const emit = defineEmits(["on-remove"]);
</script>

<template>
  <div
    class="flex items-center gap-[6px] py-[3px] px-[8px] rounded-[4px] text-xs w-fit leading-[18px]"
    :class="classByType[type]"
  >
    <div>{{ content }}</div>
    <close-bold-icon
      v-if="removable"
      class="cursor-pointer"
      :class="classByType[type]"
      @click="emit('on-remove')"
    />
  </div>
</template>
