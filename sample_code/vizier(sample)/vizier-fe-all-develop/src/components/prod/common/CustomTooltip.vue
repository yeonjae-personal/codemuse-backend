<template>
  <div
    ref="textWrapper"
    class="text-truncate w-full text-inherit text-[length:inherit]"
    @mouseover="checkEllipsis"
    @mouseleave="handleMouseLeave"
  >
    <v-tooltip
      v-if="isEllipsis"
      v-bind="props"
      :disabled="disabled || !isEllipsis"
    >
      <slot name="content">
        <span v-html="String(content).replace(/\n/g, '<br/>')"></span>
      </slot>
    </v-tooltip>
    <span
      ref="textContent"
      :class="[
        'tracking-[0.3px] text-inherit text-[length:inherit]',
        contentClass,
      ]"
    >
      <slot name="default">{{ String(content) || "" }}</slot>
    </span>
  </div>
</template>

<script setup lang="ts">
interface Props {
  content?: string | number;
  disabled?: boolean;
  location?: string;
  eager?: boolean;
  activator?: string;
  maxWidth?: number;
  [key: string]: any;
  isInline?: boolean;
  isAlwaysShow?: boolean;
  zIndex?: number;
  contentClass?: string;
}

const props = withDefaults(defineProps<Props>(), {
  content: "",
  disabled: false,
  location: "top",
  eager: false,
  maxWidth: 500,
  activator: "parent",
  isInline: false,
  isAlwaysShow: false,
  zIndex: 9999,
  contentClass: "",
});

const instance = getCurrentInstance();

const textWrapper = ref<HTMLDivElement | null>(null);
const textContent = ref<HTMLElement | null>(null);
const isEllipsis = ref<boolean>(false);

const checkEllipsis = (): void => {
  nextTick(() => {
    if (props.isAlwaysShow) {
      isEllipsis.value = true;
      return;
    }
    if (textWrapper.value && textContent.value) {
      const wrapperWidth = textWrapper.value.clientWidth;
      const contentWidth = textContent.value.offsetWidth;
      if (props.isInline) {
        const parentElement = instance?.proxy?.$el?.parentElement;
        const style = window.getComputedStyle(parentElement);
        const maxWidth = parseFloat(style.maxWidth) || 0;
        const paddingX =
          parseFloat(style.paddingLeft) + parseFloat(style.paddingRight);
        const innerWidth = maxWidth - paddingX;
        isEllipsis.value = contentWidth > innerWidth;
      } else {
        isEllipsis.value = contentWidth > wrapperWidth;
      }
    }
  });
};

const handleMouseLeave = (): void => {
  nextTick(() => {
    isEllipsis.value = false;
  });
};
</script>
