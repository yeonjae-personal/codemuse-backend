<template>
  <div class="relative w-full" :class="[!expand && 'zoom-animation']">
    <div
      class="rounded-[8px] flex cursor-pointer min-h-[45px]"
      :class="[
        disable ? 'bg-[#E9EBF0]' : active ? 'bg-active-item' : 'bg-item',
        active
          ? `!border-[${BORDER_CONFIG.ACTIVE}] border-[2px]`
          : '!border-[#e6e9ed] border-[1px]',
      ]"
      :draggable="draggable"
      @mouseover="isHover = true"
      @mouseleave="isHover = false"
      @click="selectItem"
      @dragstart="
        (event) => handleDragStart(event, event.target as HTMLElement)
      "
      @dragend="handleDragEnd"
      @drag="handleDrag"
    >
      <div
        class="flex flex-col text-[#3A3B3D] text-[13px] w-full text-text-base font-weight-[400] px-4 py-[11px]"
        :class="[{ 'opacity-[32%]': disable, 'gap-[6px]': expand }]"
      >
        <div class="flex gap-[6px] h-[20px]">
          <slot name="appendIcon"></slot>
          <CustomTooltip :content="title">
            <span class="!no-underline" v-html="highlightedName" />
          </CustomTooltip>
        </div>

        <div
          class="factor-detail bg-white !border-[#F0F2F5] rounded-[4px] border-lightest gap-[6px]"
          :class="[{ 'show-expand': expand }]"
        >
          <div class="flex gap-2 !min-h-[20px] items-center">
            <div class="w-[45%] !text-[13px] text-[#6B6D70] font-weight-medium">
              {{ $t(`product_platform.displayName`) }}
            </div>
            <div
              class="w-[55%] text-ellipsis text-[#6B6D70] font-weight-medium"
            >
              {{ $t(`product_platform.value`) }}
            </div>
          </div>
          <LocomotiveComponent
            scroll-container-class="max-h-[100px]"
            :is-stop-propagation-wheel="isStopPropagationWheelStatus"
            @is-wheel="handleWheelDetail"
          >
            <div
              v-for="childItem in item.factorValueLst"
              :key="childItem.factorCode"
              class="flex gap-2 !min-h-[20px] items-center"
            >
              <div
                class="w-[45%] !text-[13px] text-[##3A3B3D] font-weight-regular"
              >
                {{ childItem?.factorValueName || "-" }}
              </div>
              <div
                class="w-[55%] text-ellipsis text-[##3A3B3D] font-weight-regular"
              >
                {{ childItem?.value || "-" }}
              </div>
            </div>
          </LocomotiveComponent>
        </div>
      </div>
    </div>
    <div v-if="!$slots.appendIcon" class="absolute top-[3px] left-[3px]">
      <deco-icon :fill="active || isHover ? '#D9325A' : '#DCE0E5'" />
    </div>
    <div
      v-if="isNew"
      class="absolute top-[6px] right-[6px] bg-[#EA4F3A] h-[10px] w-[10px] rounded-[10px]"
    ></div>
  </div>
</template>

<script setup lang="ts">
import { BORDER_CONFIG } from "@/constants/index";
import { escapeRegExp } from "@/utils/format-data";

const emit = defineEmits(["selected-item"]);
const props = defineProps({
  item: {
    type: Object,
    default: {} as Object,
  },
  title: {
    type: String,
    default: "",
  },
  searchText: {
    type: String,
    default: "",
  },
  height: {
    type: String,
    default: "78px",
  },
  active: {
    type: Boolean,
    default: false,
  },
  disable: {
    type: Boolean,
    default: false,
  },
  isNew: {
    type: Boolean,
    default: false,
  },
  isShowExpand: {
    type: Boolean,
    default: false,
  },
  draggable: {
    type: Boolean,
    default: false,
  },
  onDragStart: {
    type: Function,
    default: () => {},
  },
  onDragEnd: {
    type: Function,
    default: () => {},
  },
});
const isHover = ref(false);
const expand = ref(false);
const isStopPropagationWheelStatus = ref(false);

const selectItem = () => {
  if (props.isShowExpand) {
    expand.value = !expand.value;
  }
  emit("selected-item");
};

const handleWheelDetail = (event) => {
  isStopPropagationWheelStatus.value = event;
};

const handleDragStart = (event: DragEvent, element: HTMLElement) => {
  const hiddenDragImg = element.cloneNode(true) as HTMLElement;
  hiddenDragImg.id = "hiddenDragImg";
  hiddenDragImg.style.opacity = "0";

  const dragImg = element.cloneNode(true) as HTMLElement;
  dragImg.id = "dragImg";
  dragImg.style.position = "absolute";
  dragImg.style.width = "366px";
  dragImg.style.zIndex = "1000";

  document.body.appendChild(hiddenDragImg);
  document.body.appendChild(dragImg);

  event.dataTransfer?.setDragImage(hiddenDragImg, 0, 0);
  event.dataTransfer?.setData("item", JSON.stringify(props.item));
  props.onDragStart(event);
};

const handleDragEnd = (event: DragEvent) => {
  const hiddenDragImg = document.getElementById("hiddenDragImg") as HTMLElement;
  const dragImage = document.getElementById("dragImg") as HTMLElement;
  props.onDragEnd(event);
  dragImage.remove();
  hiddenDragImg.remove();
};

const handleDrag = (event: DragEvent) => {
  const dragImage = document.getElementById("dragImg") as HTMLElement;
  if (dragImage) {
    dragImage.style.left = `${event.pageX}px`;
    dragImage.style.top = `${event.pageY}px`;
  }
};

const highlightedName = computed(() => {
  if (!props.searchText) return props.title;
  const escapedSearchText = escapeRegExp(props.searchText);
  // eslint-disable-next-line security/detect-non-literal-regexp
  const regex = new RegExp(`(${escapedSearchText})`, "gi");
  return props.title.replace(regex, '<span class="highlight">$1</span>');
});
</script>
<style scoped>
.bg-item {
  background: linear-gradient(90deg, #f7f7ff 0%, rgba(247, 247, 255, 0.4) 100%);
}
.bg-active-item {
  background: linear-gradient(
    90deg,
    #fff0f2 0%,
    rgba(255, 240, 242, 0.24) 100%
  );
}
:deep() .highlight {
  background-color: yellow;
}
.factor-detail {
  width: 100%;
  height: 0px;
  display: flex;
  flex-direction: column;
  transition: all 0.2s ease-in-out;
  overflow: hidden;
}
.show-expand {
  padding: 12px;
  margin-top: 4px;
  height: 153px;
  border-width: 1px;
}
</style>
