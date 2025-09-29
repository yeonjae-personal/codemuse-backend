<template>
  <div
    ref="swapper"
    class="relative w-full"
    :class="[
      borderLeft,
      className,
      { ['border-active']: isActive },
      { disable: disable },
      !isShowDetail && !isDisableZoom && 'zoom-animation cursor-pointer',
    ]"
    :draggable="draggable && !disable"
    @click.stop="clickCard"
    @dragstart="(event) => handleDragStart(event, event.target)"
    @dragend="handleDragEnd"
    @drag="handleDrag"
  >
    <div
      class="flex flex-column w-full px-[11px] py-[10px] relative"
      :style="{
        background: disable ? disableColor : expired ? expiredColor : '#fff',
      }"
    >
      <div
        ref="mainContent"
        class="flex gap-[8px] align-center h-[40px] w-full"
      >
        <div v-if="$slots.icon || typeOfProd" ref="iconStart">
          <template v-if="$slots.icon">
            <div
              class="icon-container flex justify-center items-center"
              :class="{ 'opacity-[32%]': disable }"
            >
              <slot name="icon"></slot>
            </div>
          </template>
          <template v-if="typeOfProd">
            <div
              class="icon-container flex justify-center items-center text-[13px] font-black"
              :style="{ opacity: disable ? '0.32' : '1' }"
            >
              <DeviceIcon
                v-if="typeOfProd === OFFER_TYPE.DEVICE || isDeviceIcon"
              />
              <span
                v-else
                class="leading-4 text-[17px] font-bold"
                :style="{ color: iconColor }"
                >{{ typeOfProd }}</span
              >
            </div>
          </template>
        </div>
        <div
          class="flex flex-column flex-1"
          :class="{ 'opacity-[32%]': expired || disable }"
          :style="{ maxWidth: centerWidth }"
        >
          <CustomTooltip :content="title">
            <span
              class="text-[#3A3B3D] my-[0px] text-truncate !no-underline"
              :style="{
                'font-weight': '500',
                'font-size': titleSize,
              }"
              v-html="highlightedName"
            />
          </CustomTooltip>
          <CustomTooltip :content="description" :disabled="!description">
            <span
              class="text-[#6B6D70] my-[0px] !no-underline"
              :style="{
                'font-weight': '400',
                'font-size': descriptionSize,
              }"
              v-html="highlightedCode"
            />
          </CustomTooltip>
        </div>
        <div v-if="showIconStatus" ref="iconEnd">
          <div
            v-if="editable"
            class="relative leading-none z-10"
            :class="{ 'opacity-[32%]': expired || disable }"
          >
            <base-popover
              v-if="actions.length > 0"
              :options="actions"
              custom-location="bottom-left"
              class="flex-initial"
              @open-options="emit('open-options')"
            >
              <template #activator>
                <DotsVerticalIcon />
              </template>
            </base-popover>
          </div>
          <button
            v-else
            class="flex w-[18px] h-[18px] text-[#6B6D70] z-10"
            :class="{ 'opacity-[32%]': expired || disable }"
            @click.stop.prevent="clickShowDetail"
          >
            <ChevronDown
              size="18"
              class="transition duration-150 ease-out"
              :class="{ 'rotate-180': isShowDetail }"
            />
          </button>
        </div>
        <slot name="childCount"></slot>
      </div>
      <template v-if="!node.hideNodeLeft">
        <div
          class="flex flex-column-reverse justify-space-between position-absolute h-[52px] top-[-10px] left-[-15px] z-2"
        >
          <LeftNodeButtonIcon
            class="btn-node position-relative z-10"
            :active="isActiveNodeLeft"
            @click.stop="setToggleLeft"
          />
          <div v-if="showCount" class="node-hint left-[0px]">
            {{ item?.baseProdItemCount ?? 0 }}
          </div>
        </div>
      </template>
      <template v-if="!node.hideNodeRight">
        <div
          class="flex flex-column-reverse justify-space-between position-absolute h-[52px] top-[-10px] right-[-15px] z-2"
        >
          <RightNodeButtonIcon
            class="btn-node position-relative z-10"
            :active="isActiveNodeRight"
            @click.stop="setToggleRight"
          />
          <div v-if="showCount" class="node-hint right-[0px]">
            {{ item?.trgtProdItemCount ?? 0 }}
          </div>
        </div>
      </template>
      <template v-if="isNew">
        <div
          class="new-mark"
          :class="[{ 'round-style-new': calNewMarkPosition }]"
        ></div>
      </template>
      <div class="detail-container" :class="{ 'show-detail': isShowDetail }">
        <LocomotiveComponent
          scroll-content-class="py-[8px]"
          scroll-container-class="!px-[0px]"
          :is-stop-propagation-wheel="isStopPropagationWheelStatus"
          @is-wheel="handleWheelDetail"
        >
          <slot name="detail"></slot>
        </LocomotiveComponent>
      </div>
      <div class="absolute bottom-0 left-0 z-1 overflow-hidden h-[60px]">
        <!-- <BlurBlue /> -->
      </div>
      <div class="absolute top-0 right-0 z-1 overflow-hidden h-[60px]">
        <!-- <BlurPurple /> -->
      </div>
    </div>
  </div>
</template>

<!-- eslint-disable security/detect-non-literal-regexp -->
<script setup lang="ts">
import { BORDER_CONFIG, OFFER_TYPE } from "@/constants/index";
import { SearchBy } from "@/enums";
import { escapeRegExp } from "@/utils/format-data";

const props = defineProps({
  title: {
    type: String,
    default: "",
    require: true,
  },
  description: {
    type: String,
    default: "",
  },
  searchText: {
    type: String,
    default: "",
  },
  searchField: {
    type: String,
    default: "name",
  },
  titleSize: {
    type: String,
    default: "13px",
  },
  descriptionSize: {
    type: String,
    default: "11px",
  },
  item: {
    type: Object,
    default: () => {},
  },
  typeOfProd: {
    type: String,
    default: "",
  },
  hideDetail: {
    type: Boolean,
    default: false,
  },
  showIconStatus: {
    type: Boolean,
    default: false,
  },
  isDeviceIcon: {
    type: Boolean,
    default: false,
  },
  node: {
    type: Object,
    default: () =>
      new Object({
        hideNodeLeft: false,
        isActiveNodeLeft: false,
        hideNodeRight: false,
        isActiveNodeRight: false,
      }),
  },
  borderColorAction: {
    type: String,
    default: "",
    validator(value: string) {
      return [
        "pink",
        "blue",
        "green",
        "yellow",
        "purple",
        "red",
        "dark-blue",
        "teal",
        "",
      ].includes(value);
    },
  },
  displayBorderLeft: {
    type: String,
    default: "",
    validator(value: string) {
      return [
        "pink",
        "blue",
        "green",
        "yellow",
        "dark-blue",
        "teal",
        "",
      ].includes(value);
    },
  },
  className: {
    type: String,
    default: "default",
  },
  detailHeight: {
    type: String,
    default: "395px",
  },
  iconColor: {
    type: String,
    default: "#EB7A3D",
  },
  editable: {
    type: Boolean,
    default: false,
  },
  actions: {
    type: Array as PropType<any[]>,
    default: () => [],
  },
  active: {
    type: Boolean,
    default: false,
  },
  expired: {
    type: Boolean,
    default: false,
  },
  expiredColor: {
    type: String,
    default: "#F0F2F5",
  },
  disable: {
    type: Boolean,
    default: false,
  },
  disableColor: {
    type: String,
    default: "#F0F2F5",
  },
  disableArrow: {
    type: Boolean,
    default: false,
  },
  showCount: {
    type: Boolean,
    default: true,
  },
  expand: {
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
  preventActive: {
    type: Boolean,
    default: false,
  },
  isNew: {
    type: Boolean,
    default: false,
  },
  isDisableZoom: {
    type: Boolean,
    default: false,
  },
});
const emit = defineEmits([
  "onClickCard",
  "onClickNodeLeft",
  "onClickNodeRight",
  "onClickShowDetail",
  "open-options",
  "onWheelDetail",
]);

const isActive = ref(props.active);
const isActiveNodeLeft = ref(props.node.isActiveNodeLeft);
const isActiveNodeRight = ref(props.node.isActiveNodeRight);
const isShowDetail = ref(props.expand);
const detailHeight = ref(props.detailHeight);
const defaultBorderActive = ref(BORDER_CONFIG.ACTIVE);
const swapper = ref<HTMLElement | any>(null);
const mainContent = ref<HTMLElement | any>(null);
const iconStart = ref<HTMLElement | any>(null);
const iconEnd = ref<HTMLElement | any>(null);
const isStopPropagationWheelStatus = ref(true);
const slots = useSlots();

const centerWidth = computed(() => {
  const iconStartWidth = slots.icon || props.typeOfProd ? 48 : 0;
  const iconEndWidth = props.showIconStatus ? 26 : 0;
  return `calc(100% - ${iconStartWidth + iconEndWidth}px)`;
});

const calNewMarkPosition = computed(() => {
  let position = props.className.search("card-round-style");
  if (position !== -1) {
    return true;
  }
  return false;
});
const highlightedName = computed(() => {
  if (!props.searchText || props.searchField != SearchBy.Name)
    return props.title;
  const escapedSearchText = escapeRegExp(props.searchText);
  const regex = new RegExp(`(${escapedSearchText})`, "gi");
  return props.title.replace(regex, '<span class="highlight">$1</span>');
});

const highlightedCode = computed(() => {
  if (!props.searchText || props.searchField != SearchBy.Code)
    return props.description;
  const escapedSearchText = escapeRegExp(props.searchText);
  const regex = new RegExp(`(${escapedSearchText})`, "gi");
  return props.description.replace(regex, '<span class="highlight">$1</span>');
});

const borderLeft = computed(() => {
  if (props.disable) {
    return "";
  }
  switch (props.displayBorderLeft) {
    case "pink":
      return "border-left-pink";
    case "blue":
      return "border-left-blue";
    case "green":
      return "border-left-green";
    case "yellow":
      return "border-left-yellow";
    case "dark-blue":
      return "border-left-dark-blue";
    case "teal":
      return "border-left-teal";
  }
});

const clickCard = () => {
  emit("onClickCard", {
    isActive: isActive.value,
    isShowDetail: isShowDetail.value,
    item: props.item,
  });
};
const clickShowDetail = () => {
  isShowDetail.value = !props.hideDetail ? !isShowDetail.value : false;
  emit("onClickShowDetail", isShowDetail.value);
};

const setToggleLeft = () => {
  if (props.disableArrow) {
    return;
  }
  isActiveNodeLeft.value = !isActiveNodeLeft.value;
  emit("onClickNodeLeft", {
    item: props.item,
    active: isActiveNodeLeft.value,
  });
};

const setToggleRight = () => {
  if (props.disableArrow) {
    return;
  }
  isActiveNodeRight.value = !isActiveNodeRight.value;
  emit("onClickNodeRight", {
    item: props.item,
    active: isActiveNodeRight.value,
  });
};

watch(
  () => props.active,
  (newVal) => {
    isActive.value = newVal;
  }
);
watch(
  () => props.node,
  (newVal) => {
    isActiveNodeLeft.value = newVal.isActiveNodeLeft;
    isActiveNodeRight.value = newVal.isActiveNodeRight;
  },
  { deep: true }
);
watch(
  () => props.expand,
  (newVal) => {
    isShowDetail.value = newVal;
    emit("onClickShowDetail", isShowDetail.value);
  }
);

const handleDragStart = (event, element) => {
  if (props.disable || !props.draggable) return;
  event.target.classList.remove("zoom-animation");
  const hiddenDragImg = element.cloneNode(true);
  hiddenDragImg.id = "hiddenDragImg";
  hiddenDragImg.style.opacity = "0";
  const dragImg = element.cloneNode(true);
  dragImg.id = "dragImg";
  dragImg.style.position = "absolute";
  dragImg.style.width = swapper?.value?.clientWidth
    ? swapper?.value?.clientWidth + "px"
    : "370px";
  dragImg.style.zIndex = "1000";
  dragImg.childNodes[0]?.removeChild(dragImg.childNodes[0].childNodes[1]);
  dragImg.childNodes[0]?.removeChild(dragImg.childNodes[0].childNodes[4]);
  dragImg.childNodes[0]?.removeChild(dragImg.childNodes[0].childNodes[4]);
  document.body.appendChild(hiddenDragImg);
  document.body.appendChild(dragImg);
  event.dataTransfer?.setDragImage(hiddenDragImg, 0, 0);
  props.onDragStart(event);
};

const handleDragEnd = (event) => {
  if (props.disable || !props.draggable) return;
  event.target.classList.add("zoom-animation");
  const hiddenDragImg = document.getElementById("hiddenDragImg");
  const dragImage = document.getElementById("dragImg");
  props.onDragEnd(event);
  dragImage?.remove();
  hiddenDragImg?.remove();
};

const handleDrag = (event) => {
  if (props.disable || !props.draggable) return;
  const dragImage = document.getElementById("dragImg");
  if (dragImage && event.pageX !== 0 && event.pageY !== 0) {
    dragImage.style.left = `${event.pageX}px`;
    dragImage.style.top = `${event.pageY}px`;
  }
};

const handleWheelDetail = (event) => {
  isStopPropagationWheelStatus.value = event;
  emit("onWheelDetail", event);
};
</script>

<style scoped lang="scss">
.card-round-style {
  background-color: #fff;
  padding: 0px !important;
  border: 2px #e6e9ed solid;
  border-radius: 32px;
  > div {
    border-radius: 32px;
    box-shadow: -3px -4px 12px 0px #0000000a inset;
  }
  .icon-container {
    border-radius: 24px;
  }
}
.default {
  background-color: #fff;
  border: 2px solid transparent;
  border-radius: 12px;
  box-shadow: 1px 1px 12px 0px #0000001f;
  .icon-container {
    border-radius: 8px;
  }
  > div {
    border-radius: 10px;
    box-shadow: inset -2px -2px 24px 0px #0000000a;
  }
  &.disable {
    background: v-bind(disableColor);
    box-shadow: none;
  }
}
.bg {
  &-light {
    background: #fff;
  }
}
.icon-container {
  background: rgba(255, 255, 255, 0.64);
  border: 2px solid rgba(255, 255, 255, 1);
  height: 40px;
  width: 40px;
  box-shadow: 0px 2px 12px 0px #00000014;
}
.border-left {
  &-pink {
    border-left: 2px solid #fdced5;
  }
  &-blue {
    border-left: 2px solid #b2ddff;
  }
  &-green {
    border-left: 2px solid #abefc6;
  }
  &-yellow {
    border-left: 2px solid #f9dbaf;
  }
  &-dark-blue {
    border-left: 2px solid #7d82ef;
  }
  &-teal {
    border-left: 2px solid #666666;
  }
}
.border-active {
  border: 2px solid v-bind(defaultBorderActive);
}
.detail-container {
  opacity: 0;
  width: 100%;
  height: 0px;
  transition: height 0.3s linear;
}
.show-detail {
  opacity: 1;
  height: v-bind(detailHeight);
  overflow-y: auto;
  margin-top: 12px;

  &::-webkit-scrollbar {
    width: 6px;
    margin-left: 9px;
  }
  &::-webkit-scrollbar-thumb {
    background: #bdc1c7;
    border-radius: 999px;
  }
  &::-webkit-scrollbar-track {
    background: #e6e9ed;
  }
}

.node-hint {
  position: absolute;
  z-index: 10;
  visibility: hidden;
  opacity: 0;
  top: -3px;
  background: #ffffffcc;
  color: #6b6d70;
  font-weight: 500;
  height: 24px;
  padding: 0px 8px;
  border-radius: 4px;
  font-size: 13px;
  box-shadow: 0px 2px 20px 0px #0000001a;
  transition:
    visibility 0.3s linear,
    opacity 0.3s linear;
}
.btn-node:hover {
  & ~ div {
    visibility: visible;
    opacity: 1;
  }
}
:deep() .mdi-play::before {
  font-size: 14px !important;
  margin: auto;
  color: #ffff;
}

.group-icon {
  .icon-container {
    background-color: #fff6e9;
  }
}
.group-icon-default {
  .icon-container {
    background-color: #f0f2f5;
  }
}
.entity-icon {
  .icon-container {
    width: 32px;
    height: 32px;
    border-width: 0px;
    border-radius: 20px;
    background-color: #7d82ef;
  }
}
:deep() .highlight {
  background-color: yellow;
}
.new-mark {
  width: 8px;
  height: 8px;
  position: absolute;
  top: 6px;
  right: 7px;
  background: #ea4f3a;
  border-radius: 999px;
}
.new-mark.round-style-new {
  right: 18px;
}
</style>
