<template>
  <v-list
    class="ops-list !py-0.5 rounded-lg"
    :class="[{ 'open-list': isListOpen }]"
    :style="style"
  >
    <v-list-item
      v-for="(item, index) in options"
      :key="index"
      :value="index"
      class="!mb-0 ops-list-item"
      :class="{ 'active-bg': item.active }"
      @click="handleClick(item)"
    >
      <v-list-item-title>
        <div class="flex items-center gap-[6px] ops-list-item">
          <component :is="item.icon" v-bind="item.iconProps"></component>
          {{ item.name }}
        </div>
      </v-list-item-title>
    </v-list-item>
  </v-list>
</template>

<script setup lang="ts">
const emit = defineEmits(["update:modelValue"]);
const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false,
  },
  options: {
    type: Array as PropType<
      {
        name: string;
        icon?: any;
        iconProps?: any;
        active?: Boolean;
        onClick: () => void;
      }[]
    >,
    default: () => [],
  },
  style: {
    type: Object,
    default: () => {},
  },
});

const isListOpen = computed({
  get() {
    return props.modelValue;
  },
  set(newVal) {
    emit("update:modelValue", newVal);
  },
});

const handleClick = (item) => {
  isListOpen.value = false;
  item?.onClick();
};

const handleVList = () => {
  isListOpen.value = false;
};

const handleClickOutside = (event: any): void => {
  if (!event.target.classList?.contains("ops-list-item")) {
    isListOpen.value = false;
  }
};

onMounted(() => {
  window.addEventListener("scroll", handleVList, true);
  window.addEventListener("wheel", handleVList, true);
  window.addEventListener("click", handleClickOutside, true);
  window.addEventListener("contextmenu", handleClickOutside, true);
});

onBeforeUnmount(() => {
  window.removeEventListener("scroll", handleVList, true);
  window.removeEventListener("wheel", handleVList, true);
  window.removeEventListener("click", handleClickOutside, true);
  window.removeEventListener("contextmenu", handleClickOutside, true);
});
</script>

<style lang="scss" scoped>
.ops-list {
  position: fixed;
  height: 0px;
  width: 0px;
  opacity: 0;
  transition:
    top 0.1s ease,
    left 0.1s ease,
    height 0.1s ease,
    width 0.4s ease,
    opacity 0.4s ease;
}
.open-list {
  width: 160px;
  height: auto;
  opacity: 1;
  box-shadow: 2px 2px 16px 0px #0000001f;
}
:deep()
  .v-list-item--density-default:not(.v-list-item--nav).v-list-item--one-line {
  padding: 0 12px;
  min-height: 40px;
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
