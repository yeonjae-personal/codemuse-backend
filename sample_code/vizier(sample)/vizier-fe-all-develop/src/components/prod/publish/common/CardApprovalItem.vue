<template>
  <div
    class="w-full rounded-[16px] zoom-animation"
    :draggable="draggable"
    @dragstart="(event) => handleDragStart(event, event.target as HTMLElement)"
    @dragend="handleDragEnd"
    @drag="handleDrag"
  >
    <div
      class="w-full rounded-[16px]"
      :style="{
        border: ` ${disabled && 'solid 0px #fff'}`,
        borderLeft: `solid 2px ${disabled ? '#bdc1c7' : bdColor}`,
      }"
    >
      <div
        class="w-full h-[102px] rounded-[15px] border-[1px] flex flex-col cursor-pointer bg-[#f7f8fa]"
        :class="[disabled ? 'bg-[#f7f8fa]' : 'bg-white']"
        @mouseover="isHover = true"
        @mouseleave="isHover = false"
      >
        <div
          class="h-[36px] bg-[#f7f8fa] w-full py-2 px-3 rounded-tl-[13px] rounded-tr-[13px] text-[13px] text-[#3a3b3d] text-[13px] font-[500] truncate border-b-[1px]"
        >
          <abbr
            :title="title"
            class="text-[#3A3B3D] my-[0px] text-truncate !no-underline"
            v-html="highlightedName"
          >
          </abbr>
        </div>
        <div
          class="h-[33px] w-full py-2 px-3 text-[13px] text-[#6b6d70] text-[11px] font-[400] truncate border-b-[1px]"
        >
          {{ numReview }} {{ $t("product_platform.review") }} -
          {{ numApproval }} {{ $t("product_platform.approval") }}
        </div>
        <div
          class="h-[33px] w-full py-2 px-3 text-[13px] text-[#6b6d70] text-[11px] font-[400] truncate"
        >
          {{ description }}
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { escapeRegExp } from "@/utils/format-data";

const props = defineProps({
  active: {
    type: Boolean,
    default: false,
  },
  bdColor: {
    type: String,
    default: "",
  },
  title: {
    type: String,
    default: "",
  },
  description: {
    type: String,
    default: "",
  },
  numReview: {
    type: [String, Number],
    default: 0,
  },
  numApproval: {
    type: [String, Number],
    default: 0,
  },
  disabled: {
    type: Boolean,
    default: false,
  },
  searchText: {
    type: String,
    default: "",
  },
  searchField: {
    type: String,
    default: "",
  },
  keyTypeDefault: {
    type: String,
    default: "",
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
  item: {
    type: Object,
    default: () => {},
  },
});

const highlightedName = computed(() => {
  if (!props.searchText || props.searchField != props.keyTypeDefault)
    return props.title;
  const escapedSearchText = escapeRegExp(props.searchText);
  const regex = new RegExp(`(${escapedSearchText})`, "gi");
  return props.title.replace(regex, '<span class="highlight">$1</span>');
});

const isHover = ref(false);

const handleDragStart = (event: any, element: HTMLElement) => {
  event.target.classList.remove("zoom-animation");
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

const handleDragEnd = (event: any) => {
  event.target.classList.add("zoom-animation");
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
</script>
<style lang="scss" scoped>
:deep() .highlight {
  background-color: yellow;
}
</style>
