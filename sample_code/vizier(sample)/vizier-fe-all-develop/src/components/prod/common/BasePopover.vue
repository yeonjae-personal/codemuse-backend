<script setup lang="ts">
import type { ActionType } from "@/interfaces/prod";

defineProps({
  options: {
    type: Array as PropType<ActionType[]>,
    default: () => [],
  },
  location: {
    type: String as PropType<"top" | "bottom" | "start" | "end" | "center">,
    default: "start",
  },
  openOnHover: { type: Boolean, default: false },
  customLocation: { type: String, default: "" },
  closeOnClick: { type: Boolean, default: true },
});

const emit = defineEmits(["openOptions"]);

const isPopoverOpen = ref<boolean>(false);

onMounted(() => {
  window.addEventListener("scroll", handleScroll, true);
  document.addEventListener("wheel", handleScroll, true);
});

onBeforeUnmount(() => {
  window.removeEventListener("scroll", handleScroll, true);
  document.removeEventListener("wheel", handleScroll, true);
});

const handleOpenOption = (): void => {
  emit("openOptions", isPopoverOpen.value);
};

const handleScroll = (): void => {
  isPopoverOpen.value = false;
};
</script>

<template>
  <v-menu
    v-model="isPopoverOpen"
    :location="location"
    :open-on-hover="openOnHover"
    :close-on-content-click="closeOnClick"
    :class="{
      'popover-bottom-left':
        customLocation === 'bottom-left' && location === 'start',
      'popover-bottom-right':
        customLocation === 'bottom-right' && location === 'start',
    }"
  >
    <template #activator="{ props: vProps }">
      <button v-bind="vProps" type="button" class="z-[999]">
        <div ref="slotElement" @vue:updated="handleOpenOption">
          <slot name="activator"> Popover </slot>
        </div>
      </button>
    </template>
    <v-list class="!py-0.5 rounded-lg">
      <v-list-item
        v-for="(item, index) in options"
        :key="index"
        :value="index"
        class="!mb-0"
        :class="{ 'active-bg': item.active }"
        @click="item.onClick"
      >
        <v-list-item-title>
          <div class="flex items-center gap-[6px]" v-bind="item.itemClass">
            <component :is="item.icon" v-bind="item.iconProps"></component>
            {{ item.name }}
          </div>
        </v-list-item-title>
      </v-list-item>
    </v-list>
  </v-menu>
</template>

<style lang="scss" scoped>
:deep()
  .v-list-item--density-default:not(.v-list-item--nav).v-list-item--one-line {
  padding: 0 12px;
  min-height: 32px;
}

:deep() .v-list-item-title {
  font-family: "Noto Sans KR", sans-serif !important;
  font-size: 13px;
}

:deep(.v-list) {
  box-shadow: 2px 2px 64px 0px rgba(0, 0, 0, 0.08) !important;
}

.active-bg {
  background: #fff0f2;
  color: #ba1642;
  svg {
    color: #ba1642;
  }
}
</style>
