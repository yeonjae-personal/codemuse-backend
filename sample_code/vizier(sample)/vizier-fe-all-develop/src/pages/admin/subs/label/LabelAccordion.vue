<template>
  <div
    :class="[
      'accordion cursor-pointer ',
      isActive ? `!border-[${BORDER_CONFIG.ACTIVE}] !border-[2px]` : '',
      { 'zoom-animation': !isExpand },
    ]"
    @click="handleClick"
  >
    <div :class="['accordion-header p-3', { '!pb-0': isExpand }]">
      <div class="accordion-header__title">
        <slot name="header">
          {{ title }}
        </slot>
      </div>
      <ChevronDown
        size="18"
        class="cursor-pointer transition duration-300 ease-out"
        :class="{ 'rotate-180': isExpand }"
        @click="toggleExpand"
      />
    </div>
    <transition name="accordion">
      <div
        v-if="isExpand"
        :class="['accordion-content', { '!pt-[6px]': isExpand }]"
      >
        <slot></slot>
      </div>
    </transition>
  </div>
</template>

<script lang="ts" setup>
import ChevronDown from "@/components/prod/icons/ChevronDown.vue";
import { BORDER_CONFIG } from "@/constants/index";

type Props = {
  title: string;
  isOpenDefault?: boolean;
  isActive: boolean;
};
const emit = defineEmits(["onClick"]);

const props = withDefaults(defineProps<Props>(), {
  isOpenDefault: false,
  isActive: false,
});

const isExpand = ref<boolean>(props.isOpenDefault);

const toggleExpand = (): void => {
  isExpand.value = !isExpand.value;
};

const handleClick = () => {
  emit("onClick", props.lang?.langCode);
};
</script>

<style lang="scss" scoped>
.accordion-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 8px;
  background: #f7f8fa;
  transition: all 0.1s ease-in-out;

  &__title {
    font-family: Noto Sans KR;
    font-weight: 500;
    font-size: 13px;
    line-height: 20px;
    letter-spacing: 0.25px;
    color: #3a3b3d;
  }
}

.accordion-content {
  height: 187px;
  padding: 12px;
  background-color: #f7f8fa;
}

.accordion-enter-active,
.accordion-leave-active {
  transition: all 0.2s ease-in-out;
  overflow: hidden;
}

.accordion-enter-from,
.accordion-leave-to {
  max-height: 0;
}

.accordion-enter-to,
.accordion-leave-from {
  max-height: 300px;
}
</style>
