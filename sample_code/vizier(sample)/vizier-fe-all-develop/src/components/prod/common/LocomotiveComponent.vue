<template>
  <div
    ref="scrollContainer"
    v-scroll="onScroll"
    class="scroll-container custom-scroll px-[10px]"
    :class="[scrollContainerClass, isDragging ? 'not-user-selected' : '']"
    @wheel="onWheel"
    @mouseleave="onMouseLeave"
  >
    <div ref="topContentFixed" :class="topContentClass">
      <slot name="top-content-fixed"></slot>
    </div>

    <div
      ref="scrollContent"
      :class="['scroll-content', scrollTopContentClass]"
      :style="contentStyle"
      @mousemove="getPointerPosition"
    >
      <div ref="slotContent" :class="scrollContentClass">
        <slot></slot>
      </div>
    </div>
    <div
      ref="scrollBarContainer"
      class="scrollbar-container"
      :class="{ '!w-[0px]': !showScrollbar }"
      :style="{
        transform: `translateX(${scrollBarContainerTransform}px)`,
        height: scrollBarContainerHeight,
      }"
    >
      <div
        ref="scrollBar"
        class="scroll-bar"
        :class="{ '!w-[0px]': !showScrollbar }"
        :style="{
          ...barStyle,
          transition: `transform ${transformDurationStyle}`,
        }"
        @mousedown="onMouseDown"
      ></div>
    </div>
    <div
      v-if="isDisplayFloatBtn"
      class="float-btn-container"
      :style="{
        transform: `translateX(${scrollBarContainerTransform}px)`,
      }"
    >
      <button
        class="bg-lighter float-btn-scroll"
        :class="{ 'rotate-180': isEndContent }"
        @click="onScrollToTopBottom"
      >
        <DoubleArrowIcon />
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { vScroll } from "@vueuse/components";

const props = defineProps({
  scrollContainerClass: {
    type: [String, Array],
    default: "",
  },
  scrollContentClass: {
    type: [String, Array],
    default: "",
  },
  scrollTopContentClass: {
    type: [String, Array],
    default: "",
  },
  topContentClass: {
    type: [String, Array],
    default: "",
  },
  transformDuration: {
    type: String,
    default: "0.75s",
  },
  transformFactor: {
    type: Number,
    default: 0.5,
  },
  isShowScrollbar: {
    type: Boolean,
    default: false,
  },
  isStopPropagationWheel: {
    type: Boolean,
    default: false,
  },
  alwaysEnd: {
    type: Boolean,
    default: false,
  },
  alwaysTop: {
    type: Boolean,
    default: false,
  },
  showFloatButton: {
    type: Boolean,
    default: false,
  },
  offsetFloatButton: { type: Number, default: 150 },
  isDynamicScroll: { type: Boolean, default: false },
  isCustomFloatButton: { type: Boolean, default: false },
  dynamicScrollKey: { type: String, default: "" },
  numberScrollY: { type: Number, default: 0 },
  isScrollWhenAddNew: { type: Boolean, default: false },
});
const emit = defineEmits([
  "changeHeight",
  "isWheel",
  "callLazyLoad",
  "scrollContainer",
]);

const currentY = ref(0);
const isDragging = ref(false);
const startY = ref(0);
const initialScrollY = ref(0);
const scrollContainer = ref();
const scrollContent = ref();
const topContentFixed = ref();
const topContentHeight = ref("0px");
const scrollBarContainer = ref();
const scrollBar = ref();
const slotContent = ref();
const showScrollbar = ref(props.isShowScrollbar);
const scrollBarContainerHeight = ref();
const scrollBarContainerTransform = ref();
const isMounted = ref<boolean>(false);
const isValidArea = ref<boolean>(false);
const isShowFloatBtn = ref(false);

const isEndContent = computed(() => {
  if (
    scrollContainer.value?.clientHeight &&
    scrollContent.value.offsetHeight &&
    topContentFixed.value
  ) {
    return (
      scrollContent.value.offsetHeight -
        scrollContainer.value.clientHeight +
        topContentFixed.value?.offsetHeight +
        currentY.value ===
      0
    );
  }
  return false;
});

const contentStyle = computed(() => {
  return {
    transform: `translateY(${currentY.value}px)`,
    transition: isDragging.value
      ? "none"
      : `transform ${transformDurationStyle.value}`,
  };
});

const barStyle = computed(() => {
  const containerHeight = scrollContainer.value?.clientHeight;
  const contentHeight = scrollContent.value?.offsetHeight;
  const topContentHeight = topContentFixed.value?.offsetHeight;
  const diffHeight = containerHeight - topContentHeight;

  const scrollBarHeight = (diffHeight / contentHeight) * diffHeight;
  const translateY =
    (-currentY.value / (contentHeight - diffHeight)) *
    (diffHeight - scrollBarHeight);
  return {
    height: `${scrollBarHeight}px`,
    transform: `translateY(${translateY}px)`,
  };
});

const stopPropagationWheel = computed(() => props.isStopPropagationWheel);

const transformDurationStyle = computed<string>(() => {
  if (props.isDynamicScroll && !isMounted.value) {
    return "none";
  }
  return props.transformDuration;
});

const isDisplayFloatBtn = computed<boolean>(() => {
  if (props.isCustomFloatButton) {
    return isShowFloatBtn.value && isValidArea.value;
  }
  return isShowFloatBtn.value;
});

const onWheel = (event) => {
  const contentHeight = scrollContent.value.offsetHeight;
  const topContentHeight = topContentFixed.value?.offsetHeight ?? 0;
  const delta = event.deltaY * props.transformFactor;
  const maxScroll =
    -scrollContent.value.offsetHeight -
    topContentFixed.value?.clientHeight +
    scrollContainer.value.clientHeight;

  if (maxScroll <= 0 && currentY.value === maxScroll) {
    emit("callLazyLoad");
  }

  if (
    stopPropagationWheel.value ||
    (currentY.value === 0 &&
      delta > 0 &&
      contentHeight - scrollContainer.value.clientHeight + currentY.value >
        0) ||
    (currentY.value !== 0 &&
      contentHeight - scrollContainer.value.clientHeight + currentY.value ===
        0 &&
      delta < 0)
  ) {
    event.stopPropagation();
  }
  currentY.value -= delta;
  currentY.value = Math.max(
    Math.min(currentY.value, 0),
    -contentHeight + (scrollContainer.value.clientHeight - topContentHeight)
  );
  if (
    currentY.value === 0 ||
    contentHeight - scrollContainer.value.clientHeight + currentY.value === 0
  ) {
    emit("isWheel", false);
  } else {
    emit("isWheel", true);
  }
};

const onScrollToTopBottom = (event) => {
  event.stopPropagation();
  event.preventDefault();
  if (isEndContent.value) {
    currentY.value = 0;
  } else {
    currentY.value =
      -scrollContent.value.offsetHeight -
      topContentFixed.value?.clientHeight +
      scrollContainer.value.clientHeight;
  }
};

const calcShowFloatBtn = () => {
  if (props.showFloatButton && scrollContent.value && scrollContainer.value) {
    isShowFloatBtn.value =
      scrollContent.value.offsetHeight > scrollContainer.value.clientHeight;
  }
};

const onScroll = (state) => {
  scrollBarContainerTransform.value = state.x.value;
};

const onMouseDown = (event) => {
  isDragging.value = true;
  startY.value = event.clientY;
  initialScrollY.value = currentY.value;

  document.addEventListener("mouseup", onMouseUp);
  document.addEventListener("mousemove", onMouseMove);
};

const onMouseUp = () => {
  isDragging.value = false;
  document.removeEventListener("mouseup", onMouseUp);
  document.removeEventListener("mousemove", onMouseMove);
};

const onMouseMove = (event) => {
  if (isDragging.value) {
    const deltaY = startY.value - event.clientY;
    currentY.value = initialScrollY.value + deltaY * 4;

    const contentHeight = scrollContent.value.offsetHeight;
    const topContentHeight = topContentFixed.value.offsetHeight;
    currentY.value = Math.max(
      Math.min(currentY.value, 0),
      -contentHeight + (scrollContainer.value.clientHeight - topContentHeight)
    );
  }
};

const scrollContainerResize = new ResizeObserver(() => {
  currentY.value += 0.1;
  currentY.value -= 0.1;
  calcScrollBarContainerHeight();
  calcShowFloatBtn();
});

const scrollContentResize = new ResizeObserver(() => {
  calcShowFloatBtn();
  if (scrollContent.value?.clientWidth === slotContent.value?.scrollWidth) {
    scrollContainer.value.scrollLeft = 0;
  }
  if (
    scrollContent.value?.offsetHeight + scrollContent.value?.offsetTop >
    scrollContainer.value?.clientHeight
  ) {
    currentY.value += 0.1;
    currentY.value -= 0.1;
    showScrollbar.value = props.isShowScrollbar;
    if (
      props.alwaysEnd ||
      scrollContainer.value.clientHeight -
        scrollContent.value.offsetHeight -
        scrollContent.value?.offsetTop >
        currentY.value
    ) {
      currentY.value =
        -scrollContent.value.offsetHeight -
        scrollContent.value?.offsetTop +
        scrollContainer.value.clientHeight;
    }
  } else {
    showScrollbar.value = false;
    currentY.value = 0;
  }
  calcScrollBarContainerHeight();
  setTimeout(() => {
    emit("changeHeight", scrollContainer.value?.clientHeight);
  }, 300);
});

const fixedContentResize = new ResizeObserver(() => {
  currentY.value += 0.1;
  currentY.value -= 0.1;
  if (topContentFixed.value?.clientHeight) {
    topContentHeight.value = `${topContentFixed.value?.clientHeight}px`;
    calcScrollBarContainerHeight();
  }
});

const calcScrollBarContainerHeight = () => {
  if (topContentFixed.value) {
    scrollBarContainerHeight.value = `${
      scrollContainer.value.clientHeight - topContentFixed.value.clientHeight
    }px`;
  } else {
    scrollBarContainerHeight.value = "100%";
  }
};

const observeSize = () => {
  scrollContainerResize.observe(scrollContainer.value);
  scrollContentResize.observe(scrollContent.value);
  fixedContentResize.observe(topContentFixed.value);
};
const unobserveSize = () => {
  scrollContainerResize.disconnect();
  scrollContentResize.disconnect();
  fixedContentResize.disconnect();
};

const onMouseLeave = (): void => {
  if (props.isCustomFloatButton) {
    isValidArea.value = false;
  }
};

const getPointerPosition = ({ clientY }: MouseEvent): void => {
  if (props.isCustomFloatButton) {
    const { top, height } = scrollContainer.value.getBoundingClientRect();
    if (top && height) {
      isValidArea.value = clientY - top >= height - props.offsetFloatButton;
    }
  }
};

const handleScrollContainer = (): void => {
  emit("scrollContainer");
};

watch(
  [() => props.numberScrollY, () => props.isScrollWhenAddNew],
  ([scrollY, isAddNew]) => {
    if (isAddNew) {
      const positionY = scrollContainer.value.clientHeight - scrollY;
      if (positionY <= 0) currentY.value = positionY;
    }
  }
);

onMounted(() => {
  scrollContainer.value?.addEventListener("scroll", handleScrollContainer);
  observeSize();
  calcScrollBarContainerHeight();
  if (props.alwaysEnd && !props.alwaysTop) {
    currentY.value =
      -scrollContent.value.offsetHeight + scrollContainer.value.clientHeight;
  }
  if (props.alwaysTop && !props.alwaysEnd) {
    currentY.value = 0;
  }
  const savedPositionY =
    Number(sessionStorage.getItem(props.dynamicScrollKey)) || 0;
  if (props.isDynamicScroll) {
    currentY.value = savedPositionY;
  }
  nextTick(() => {
    isMounted.value = true;
  });
});

onBeforeUnmount(() => {
  document.removeEventListener("mouseup", onMouseUp);
  document.removeEventListener("mousemove", onMouseMove);
  scrollContainer.value?.removeEventListener("scroll", handleScrollContainer);
  unobserveSize();
  if (props.isDynamicScroll) {
    sessionStorage.setItem(props.dynamicScrollKey, String(currentY.value));
  }
});
</script>

<style scoped lang="scss">
.scroll-container {
  width: 100%;
  height: 100%;
  overflow-y: hidden;
  overflow-x: auto;
  position: relative;
  display: flex;
  flex-direction: column;
}

.scroll-content {
  will-change: transform;
  flex-grow: 1;
  min-width: 100%;
  box-sizing: border-box;
}
.scrollbar-container {
  z-index: 2;
  position: absolute;
  right: 0;
  bottom: 0;
  width: 6px;
  border-radius: 999px;
  background: #e6e9ed;
}
.scroll-bar {
  width: 6px;
  background: #bdc1c7;
  border-radius: 999px;
  cursor: pointer;
}
.not-user-selected {
  p,
  span,
  div,
  abbr {
    user-select: none !important;
  }
}
.float-btn-container {
  display: flex;
  justify-content: center;
  align-items: center;
  background: transparent;
  height: 32px;
  position: absolute;
  bottom: 16px;
  cursor: pointer;
  left: 50%;
  transform: translateX(-50%);
}
.float-btn-scroll {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 32px;
  height: 32px;
  border-radius: 100%;
  padding: 8px;
  box-shadow: 0px 4px 8px 0px rgba(0, 0, 0, 0.12);
}
</style>
