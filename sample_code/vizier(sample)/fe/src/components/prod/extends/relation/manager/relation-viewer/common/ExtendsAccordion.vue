<template>
  <div
    class="py-[9px] w-full"
    :class="className"
    :style="{ 'min-height': height }"
  >
    <div
      ref="accordion"
      class="accordion-swagger"
      :class="[
        isActive
          ? `!border-[${BORDER_CONFIG.ACTIVE}] !border-[2px]`
          : '!border-lighter !border-[1px]',
        { disable: disable },
        { 'zoom-animation cursor-pointer': !isExpand && !isDisableZoom },
        !expired
          ? 'border-white shadow-normal'
          : 'border-[#E9EBF0] shadow-disable',
        expired
          ? 'bg-[#E9EBF0]'
          : expiredRelation
            ? 'bg-[#E9EBF0]'
            : 'bg-white',
      ]"
      :draggable="draggable"
      @dragstart="(event) => handleDragStart(event, event.target)"
      @dragend="handleDragEnd"
      @drag="handleDrag"
      @mouseover="isHover = true"
      @mouseleave="isHover = false"
      @click="handleClick"
    >
      <div
        class="flex align-center gap-[6px] pr-[4px] h-[20px]"
        :class="[disable ? '!opacity-[32%]' : '']"
      >
        <v-icon
          v-if="type === 'normal'"
          icon="mdi-cog"
          :color="
            (isActive || isHover) && !disable && !isDisableZoom
              ? '#BA1642'
              : '#6B6D70'
          "
          size="16"
        ></v-icon>
        <FolderIcon v-if="type === 'group'" color="#BDC1C7" />
        <CustomTooltip :content="title">
          <span
            class="flex-grow-1 title text-[13px] text-[#3A3B3D] font-[500] !no-underline"
            :class="[expired ? '!opacity-[32%]' : '']"
            v-html="highlightedName"
          />
        </CustomTooltip>
        <template v-if="isShowIconAppend">
          <div v-if="count" class="notify-count">
            {{ count }}
          </div>
          <v-icon
            v-if="isRemove"
            class="cursor-pointer"
            icon="mdi-trash-can-outline"
            color="#6B6D70"
            size="small"
            @click.stop="handleRemove"
          ></v-icon>
          <div
            v-if="editable"
            class="relative leading-none z-10"
            :class="{ 'opacity-[32%]': expired }"
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
          <ChevronDown
            v-if="!editable"
            size="18"
            class="transition duration-150 ease-out"
            :class="{ 'rotate-180': isExpand }"
            @click.stop="handleExpand"
          />
        </template>
      </div>
      <div v-if="isRemove" class="new-mark"></div>
      <template v-if="isShowListProduct">
        <div v-show="isExpand" class="mt-3">
          <slot></slot>
        </div>
      </template>
      <template v-else>
        <div class="expand-items" :class="{ 'show-detail': isExpand }">
          <LocomotiveComponent
            scroll-container-class="!px-0"
            :is-stop-propagation-wheel="isStopPropagationWheelStatus"
            @is-wheel="handleWheelDetail"
          >
            <slot></slot>
          </LocomotiveComponent>
        </div>
      </template>
    </div>
  </div>
</template>

<script setup lang="ts">
import { escapeRegExp } from "@/utils/format-data";
import type { ActionType } from "@/interfaces/prod";
import { BORDER_CONFIG } from "@/constants/index";

const props = defineProps({
  type: {
    type: String,
    default: "",
    validator(value: string) {
      return ["normal", "group"].includes(value);
    },
  },
  detailHeight: {
    type: String,
    default: "360px",
  },
  searchText: {
    type: String,
    default: "",
  },
  searchField: {
    type: String,
    default: "name",
  },
  className: {
    type: String,
    default: "",
  },
  height: {
    type: String,
    default: "48px",
  },
  title: {
    type: String,
    require: true,
    default: "",
  },
  count: {
    type: Number,
    default: 0,
  },
  expand: {
    type: Boolean,
    default: false,
  },
  disable: {
    type: Boolean,
    default: false,
  },
  active: {
    type: Boolean,
    default: false,
  },
  isRemove: {
    type: Boolean,
    default: false,
  },
  expired: {
    type: Boolean,
    default: false,
  },
  expiredRelation: {
    type: Boolean,
    default: false,
  },
  isShowListProduct: {
    type: Boolean,
    default: true,
  },
  draggable: {
    type: Boolean,
    default: false,
  },
  editable: {
    type: Boolean,
    default: false,
  },
  isShowIconAppend: {
    type: Boolean,
    default: true,
  },
  actions: {
    type: Array as PropType<ActionType[]>,
    default: () => [],
  },
  onDragStart: {
    type: Function,
    default: () => {},
  },
  onDragEnd: {
    type: Function,
    default: () => {},
  },
  isDisableZoom: {
    type: Boolean,
    default: false,
  },
});
const emit = defineEmits([
  "onExpand",
  "onRemove",
  "onClick",
  "onWheelDetail",
  "open-options",
  "update:expand",
]);

const accordion = ref();
const isActive = ref(props.active);
const isStopPropagationWheelStatus = ref(true);
const isHover = ref(false);

const highlightedName = computed(() => {
  if (!props.searchText || props.searchField != "name") return props.title;
  const escapedSearchText = escapeRegExp(props.searchText);
  // eslint-disable-next-line security/detect-non-literal-regexp
  const regex = new RegExp(`(${escapedSearchText})`, "gi");
  return props.title.replace(regex, '<span class="highlight">$1</span>');
});

const isExpand = computed<boolean>({
  get: () => props.expand,
  set: (value: boolean) => {
    emit("update:expand", value);
  },
});

const handleExpand = () => {
  if (props.disable) {
    return;
  }
  emit("onExpand", !isExpand.value);
};

const handleRemove = () => {
  emit("onRemove");
};

const handleClick = () => {
  emit("onClick", true);
};

const handleDragStart = async (event, element) => {
  if (props.disable || !props.draggable) return;
  event.target.classList.remove("zoom-animation");
  if (isExpand.value) {
    isExpand.value = false;
  }
  await nextTick();
  const hiddenDragImg = element.cloneNode(true);
  hiddenDragImg.id = "hiddenDragImg";
  hiddenDragImg.style.opacity = "0";
  const dragImg = element.cloneNode(true);
  dragImg.id = "dragImg";
  dragImg.style.position = "absolute";
  dragImg.style.width = `${accordion.value.clientWidth}px`;
  dragImg.style.zIndex = "1000";
  document.body.appendChild(hiddenDragImg);
  document.body.appendChild(dragImg);
  event.dataTransfer?.setDragImage(hiddenDragImg, 0, 0);
  props.onDragStart(event);
};

const handleDragEnd = (event) => {
  if (props.disable || !props.draggable) return;
  event.target.classList.add("zoom-animation");
  const hiddenDragImg: any = document.getElementById("hiddenDragImg");
  const dragImage: any = document.getElementById("dragImg");
  dragImage?.remove?.();
  hiddenDragImg?.remove?.();
  props.onDragEnd(event);
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

watch(
  () => props.active,
  (newVal) => {
    isActive.value = newVal;
  }
);

watch(
  () => props.expand,
  (newVal) => {
    isExpand.value = newVal;
  }
);
</script>

<style lang="scss" scoped>
.shadow-normal {
  box-shadow:
    -2px -2px 24px 0px #0000000a inset,
    1px 1px 12px 0px #00000024;
}
.shadow-disable {
  box-shadow: -2px -2px 24px 0px #0000000a inset;
}
.accordion-swagger {
  display: flex;
  flex-direction: column;
  position: relative;
  border-radius: 8px;
  padding: 12px;
  .notify-count {
    background: #fff0f2;
    min-width: 28px;
    height: 24px;
    padding: 4px;
    border-radius: 4px;
    color: #ba1642;
    font-size: 11px;
    font-weight: 400;
    text-align: center;
  }
}
.title {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.disable {
  box-shadow: -2px -2px 24px 0px #0000000a inset !important;
  background: #e9ebf0 !important;
  & > * {
    color: #bdc1c7;
  }
}
.expand-items {
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
.new-mark {
  width: 8px;
  height: 8px;
  position: absolute;
  top: 6px;
  right: 7px;
  background: #ea4f3a;
  border-radius: 999px;
}
:deep() .highlight {
  background-color: yellow;
}
</style>
