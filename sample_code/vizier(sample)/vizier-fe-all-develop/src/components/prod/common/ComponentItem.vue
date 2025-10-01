<script lang="ts">
export enum ColorComponentType {
  Pink = "pink",
  Gray = "gray",
}
</script>

<script setup lang="ts">
import { TypeComponentCode } from "@/enums";
import { highlightText } from "@/utils/format-data";
import { ActionType } from "@/interfaces/prod";
import BlurBlue from "@/components/prod/icons/BlurBlue.vue";
import BlurPurple from "@/components/prod/icons/BlurPurple.vue";

const props = defineProps({
  title: {
    type: String,
    default: "",
  },
  isActive: {
    type: Boolean,
    default: false,
  },
  expired: {
    type: Boolean,
    default: false,
  },
  draggable: {
    type: Boolean,
    default: false,
  },
  expiredRelation: {
    type: Boolean,
    default: false,
  },
  expandable: {
    type: Boolean,
    default: false,
  },
  name: {
    type: String,
    default: "",
  },
  code: {
    type: String,
    default: "",
  },
  type: {
    type: String as PropType<TypeComponentCode>,
    default: "",
  },
  className: {
    type: String,
    default: "",
  },
  actions: {
    type: Array as PropType<ActionType[]>,
    default: () => [],
  },
  color: {
    type: String as PropType<ColorComponentType>,
    default: ColorComponentType.Gray,
  },
  location: {
    type: String as PropType<"top" | "bottom" | "start" | "end" | "center">,
    default: "start",
  },
  onDragStart: {
    type: Function as PropType<(event: DragEvent) => void>,
    default: () => {},
  },
  onDragEnd: {
    type: Function as PropType<(event: DragEvent) => void>,
    default: () => {},
  },
  openOptions: {
    type: Function as PropType<() => void>,
    default: () => {},
  },
  searchTypeObj: {
    type: Object as PropType<{
      field: string;
      value: string;
      keysCheck: Record<FieldDisplay, string>;
    }>,
    default: () => ({
      value: "",
      key: "",
      keysCheck: {},
    }),
  },
  numbers: {
    type: Number,
    default: 0,
  },
  showNumbers: {
    type: Boolean,
    default: false,
  },
  showExpand: {
    type: Boolean,
    default: false,
  },
  isNewItem: {
    type: Boolean,
    default: false,
  },
  isWhiteLayout: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits(["openOptions", "toggleCollapse"]);

const isHover = ref(false);
const content = ref<HTMLElement | null>(null);
const borderColorMapping: Record<TypeComponentCode, string> = {
  [TypeComponentCode.Characteristics]: "border-l-primary-lighter",
  [TypeComponentCode.Service]: "border-l-info-lighter",
  [TypeComponentCode.Benefit]: "border-l-success-lighter",
  [TypeComponentCode.Price]: "border-l-warning-lighter",
};
const shadowMapping: Record<TypeComponentCode, string> = {
  [TypeComponentCode.Characteristics]: "shadow-component-item-characteristics",
  [TypeComponentCode.Service]: "shadow-component-item-service",
  [TypeComponentCode.Benefit]: "shadow-component-item-benefit",
  [TypeComponentCode.Price]: "shadow-component-item-price",
};

const borderActiveMapping: Record<TypeComponentCode, string> = {
  [TypeComponentCode.Characteristics]: "!border-primary-lighter",
  [TypeComponentCode.Service]: "!border-info-lighter",
  [TypeComponentCode.Benefit]: "!border-success-lighter",
  [TypeComponentCode.Price]: "!border-warning-lighter",
};
const borderLeftActiveMapping: Record<TypeComponentCode, string> = {
  [TypeComponentCode.Characteristics]:
    "!border-l-[2px] !border-l-primary-lighter",
  [TypeComponentCode.Service]: "!border-l-[2px] !border-l-info-lighter",
  [TypeComponentCode.Benefit]: "!border-l-[2px] !border-l-success-lighter",
  [TypeComponentCode.Price]: "!border-l-[2px] !border-l-warning-lighter",
};

const getBorderByType = (type: TypeComponentCode) => {
  // eslint-disable-next-line security/detect-object-injection
  return borderColorMapping[type];
};

const getBorderActiveByType = (type: TypeComponentCode) => {
  // eslint-disable-next-line security/detect-object-injection
  return borderActiveMapping[type];
};

const getShadowByType = (type: TypeComponentCode) => {
  return shadowMapping[type as string];
};

const handleOpenOptions = () => {
  emit("openOptions");
};

const getClassNotHover = (type) => {
  if (!props.expired) {
    return `${
      borderLeftActiveMapping[type as string]
    } border-r-[2px] border-white border-y-[2px]`;
  }
  return `border-transparent border-[2px]`;
};

const handleDragStart = (event: DragEvent, element: any) => {
  const childrenElement = element.children[0];
  const hiddenDragImg = childrenElement.cloneNode(true) as HTMLElement;
  hiddenDragImg.id = "hiddenDragImg";
  hiddenDragImg.style.opacity = "0";
  const dragImg = childrenElement.cloneNode(true) as HTMLElement;
  dragImg.id = "dragImg";
  dragImg.style.position = "absolute";
  dragImg.style.width =
    props.showExpand && props.expandable ? "376px" : "208px";
  dragImg.style.zIndex = "1000";
  document.body.appendChild(hiddenDragImg);
  document.body.appendChild(dragImg);
  event.dataTransfer?.setDragImage(hiddenDragImg, 0, 0);
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

enum FieldDisplay {
  Code = "code",
  Name = "name",
}

const highlightedText = computed(() => (type: FieldDisplay) => {
  const valueToHighlight = type === FieldDisplay.Name ? props.name : props.code;
  if (
    !props.searchTypeObj.value ||
    // eslint-disable-next-line security/detect-object-injection
    props.searchTypeObj.field !== props.searchTypeObj.keysCheck[type]
  ) {
    return valueToHighlight;
  }
  // eslint-disable-next-line security/detect-non-literal-regexp
  return highlightText(valueToHighlight, props.searchTypeObj.value);
});

const getColorBgCard = computed(() => {
  return props.isWhiteLayout ? "#F0F2F5" : "#e9ebf0";
});
</script>

<template>
  <div
    class="rounded-[12px] relative overflow-hidden transition-all duration-100"
    :class="[
      getBorderByType(type),
      (isActive || isHover) && `!border-l-transparent`,
      expired
        ? 'bg-expired'
        : expiredRelation
          ? 'bg-expired-relation'
          : 'bg-component',
      color !== ColorComponentType.Pink && getShadowByType(type),
    ]"
    :draggable="draggable"
    @dragstart="(event) => handleDragStart(event, event.target)"
    @dragend="handleDragEnd"
    @drag="handleDrag"
    @mouseover="isHover = true"
    @mouseleave="isHover = false"
  >
    <div
      :class="[
        'cursor-pointer rounded-[12px] py-2 relative z-20 transition-all duration-100',
        (isActive || isHover) && `${getBorderActiveByType(type)}`,
        !isActive && !isHover ? getClassNotHover(type) : 'border-[2px]',
        className,
      ]"
    >
      <div
        class="flex items-center justify-between gap-5 px-3 w-full h-full"
        :class="[expired || expiredRelation ? '!opacity-[32%]' : '']"
      >
        <div class="flex-1 min-w-0">
          <CustomTooltip :content="title">
            <span
              class="text-text-base text-[13px] lead-[19.5px] tracking-[0.25px] font-medium truncate w-full"
              v-html="highlightedText(FieldDisplay.Name)"
            />
          </CustomTooltip>
          <div
            class="text-text-lighter text-[11px] leading-[16.5px] tracking-[0.25px]"
            v-html="highlightedText(FieldDisplay.Code)"
          ></div>
        </div>
        <div
          v-if="showNumbers"
          class="text-text-primary text-[11px] w-6 h-6 flex items-center justify-center bg-primary-lighter rounded !border-[1px] !border-primary-lighter"
        >
          {{ numbers }}
        </div>

        <base-popover
          v-if="actions.length > 0"
          :options="actions"
          :location="location"
          custom-location="bottom-left"
          class="flex-initial"
          @open-options="handleOpenOptions"
        >
          <template #activator>
            <div>
              <DotsVerticalIcon />
            </div>
          </template>
        </base-popover>

        <ChevronDown
          v-if="expandable"
          size="20"
          class="transition duration-300 ease-out flex-initial"
          :class="{ 'rotate-180': showExpand }"
          @click.stop="emit('toggleCollapse')"
        />
      </div>
    </div>
    <div v-if="isNewItem" class="new-mark"></div>
    <div class="absolute bottom-0 left-0 z-1">
      <BlurBlue />
    </div>
    <div class="absolute top-0 right-0 z-1">
      <BlurPurple />
    </div>
  </div>
</template>

<style lang="scss">
.collapse-enter-active,
.collapse-leave-active {
  transition: max-height 300ms ease;
}

.collapse-enter-from,
.collapse-leave-to {
  max-height: 0;
}

/* Assuming the maximum height your element can reach is 1000px */
.collapse-enter-to,
.collapse-leave-from {
  max-height: 1000px;
}

.highlight {
  background-color: yellow !important;
}

.bg-component {
  background: #ffffff;
}
.new-mark {
  width: 8px;
  height: 8px;
  position: absolute;
  top: 9px;
  right: 9px;
  background: #ea4f3a;
  border-radius: 999px;
}

.bg-expired {
  background: v-bind(getColorBgCard);
  box-shadow: 1px 1px 12px 0px #0000000a;

  box-shadow: -2px -2px 24px 0px #0000000a inset;
}
.bg-expired-relation {
  background: v-bind(getColorBgCard);
}
</style>
