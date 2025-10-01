<template>
  <div
    ref="swapper"
    class="bg-white rounded-[12px] w-full h-[82px] layout-card cursor-pointer p-3 relative zoom-animation"
    :class="[!disable && 'border-card', disable && '!bg-[#e9ebf0]']"
    :draggable="draggable && !disable"
    @dragstart="(event) => handleDragStart(event, event.target)"
    @dragend="handleDragEnd"
    @drag="handleDrag"
  >
    <div class="flex justify-between align-center h-[17px]">
      <span class="text-[#6b6d70] text-[11px] font-[400]">
        {{
          inSearchMode
            ? disable
              ? "Packed"
              : capitalizeFirstLetter(data.chngDataStusCode)
            : data.rgstUser
        }}
      </span>
      <div class="flex justify-between align-center gap-1">
        <span
          v-if="!inSearchMode"
          class="text-[#6b6d70] text-[11px] font-[400]"
        >
          {{
            formatDate(
              data.rgstDtm,
              DATE_FORMAT.DATE_TYPE,
              DATE_FORMAT.DATE_TYPE
            )
          }}
        </span>
        <div v-if="editable" class="relative leading-none z-10">
          <base-popover
            v-if="actions.length > 0"
            :options="actions"
            custom-location="bottom-left"
            class="flex-initial"
          >
            <template #activator>
              <DotsVerticalIcon />
            </template>
          </base-popover>
        </div>
      </div>
    </div>
    <div class="h-[1px] bg-[#f0f2f5] my-2"></div>
    <div class="flex justify-between align-center">
      <div
        class="w-full flex align-center gap-2 h-[24px]"
        :style="contentStyle"
      >
        <div ref="iconBox">
          <div
            v-if="
              [
                PUBLISH_COMPOSE_ITEM_TYPE.OFFER_STRT,
                PUBLISH_COMPOSE_ITEM_TYPE.OFFER_REL,
                PUBLISH_COMPOSE_ITEM_TYPE.COMPONENT_STRT,
                PUBLISH_COMPOSE_ITEM_TYPE.GROUP_STRT,
              ].includes(data.chngDataTypeCode)
            "
            class="flex align-center"
          >
            <div class="icon-box">
              <component :is="itemIcon.component" v-bind="itemIcon.props" />
            </div>
            <hr class="w-[6px] h-[1px] bg-darkerst" />
            <div class="icon-box">
              <component
                :is="itemIcon.componentAppend"
                v-bind="itemIcon.propsAppend"
              />
            </div>
          </div>
          <div v-else class="icon-box">
            <component :is="itemIcon.component" v-bind="itemIcon.props" />
          </div>
        </div>
        <div :style="descStyle">
          <CustomTooltip :content="data.chngDataCodeName">
            <span class="text-[#3a3b3d] text-[13px] font-[500] leading-6">
              {{ data.chngDataCodeName }}
            </span>
          </CustomTooltip>
        </div>
      </div>
      <div
        v-if="typeName"
        ref="typeBox"
        class="border-[1px] border-[#e6e9ed] bg-white rounded-[4px] h-[24px] flex align-center px-2"
      >
        <span class="text-[#6b6d70] text-[11px] font-[400]">
          {{ typeName }}
        </span>
      </div>
    </div>
    <template v-if="data?.isAdd">
      <div class="new-mark"></div>
    </template>
  </div>
</template>
<script lang="ts" setup>
import { DATE_FORMAT, OFFER_TYPE } from "@/constants/index";
import {
  PUBLISH_COMPOSE_ITEM_TYPE,
  PUBLISH_COMPOSE_ITEM_TYPE_LIST,
} from "@/constants/publish";
import { RESOURCE_ITEM_CODE } from "@/constants/resource";
import { ComposeItem } from "@/interfaces/prod/publishInterface";
import { capitalizeFirstLetter, formatDate } from "@/utils/format-data";
import { CSSProperties } from "vue";
import AIcon from "@/components/prod/icons/AIcon.vue";
import PIcon from "@/components/prod/icons/PIcon.vue";
import DIcon from "@/components/prod/icons/DIcon.vue";
import DeviceIcon from "@/components/prod/icons/DeviceIcon.vue";
import HelpIcon from "@/components/prod/icons/HelpIcon.vue";
import ComponentIcon from "@/components/prod/icons/ComponentIcon.vue";
import SLinearIcon from "@/components/prod/icons/SLinearIcon.vue";
import RLinearIcon from "@/components/prod/icons/RLinearIcon.vue";
import BLinearIcon from "@/components/prod/icons/BLinearIcon.vue";
import MultiEntityIcon from "@/components/prod/icons/MultiEntityIcon.vue";
import RelationIcon from "@/components/prod/icons/RelationIcon.vue";
import FolderIcon from "@/components/prod/icons/FolderIcon.vue";
import StructureIcon from "@/components/prod/icons/StructureIcon.vue";
import CategoryIcon from "@/components/prod/icons/CategoryIcon.vue";
import TableIcon from "@/components/prod/icons/TableIcon.vue";
import MatrixIcon from "@/components/prod/icons/MatrixIcon.vue";
import DecoIcon from "@/components/prod/icons/DecoIcon.vue";

const props = defineProps({
  data: {
    type: Object as () => ComposeItem,
    default: () => {},
    require: true,
  },
  actions: {
    type: Array as () => any[],
    default: () => [],
  },
  disable: {
    type: Boolean,
    default: false,
  },
  editable: {
    type: Boolean,
    default: true,
  },
  inSearchMode: {
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
const swapper = ref<HTMLElement | any>(null);
const iconBox = ref<HTMLElement | any>(null);
const typeBox = ref<HTMLElement | any>(null);
const contentStyle = ref<CSSProperties>({ width: "0px" });
const descStyle = ref<CSSProperties>({ width: "0px" });

const typeName = computed(() => {
  return PUBLISH_COMPOSE_ITEM_TYPE_LIST.find(
    (item) => item.cmcdDetlId === props.data.chngDataTypeCode
  )?.cmcdDetlNm;
});

const itemIcon = computed(() => {
  switch (props.data.chngDataTypeCode) {
    case PUBLISH_COMPOSE_ITEM_TYPE.OFFER:
      switch (props.data.chngDataItemCode) {
        case OFFER_TYPE.PRICEPLAN:
          return { component: PIcon };
        case OFFER_TYPE.ADDON:
          return { component: AIcon };
        case OFFER_TYPE.DISCOUNT:
          return { component: DIcon };
        case OFFER_TYPE.DEVICE:
          return {
            component: DeviceIcon,
            props: { width: "16", height: "16" },
          };
        default:
          return { component: HelpIcon, props: { size: "15" } };
      }
    case PUBLISH_COMPOSE_ITEM_TYPE.OFFER_STRT:
    case PUBLISH_COMPOSE_ITEM_TYPE.OFFER_REL:
      switch (props.data.chngDataItemCode) {
        case OFFER_TYPE.PRICEPLAN:
          return {
            component: PIcon,
            componentAppend:
              props.data.chngDataTypeCode ==
              PUBLISH_COMPOSE_ITEM_TYPE.OFFER_STRT
                ? StructureIcon
                : RelationIcon,
            propsAppend: { width: "11", height: "11" },
          };
        case OFFER_TYPE.ADDON:
          return {
            component: AIcon,
            componentAppend:
              props.data.chngDataTypeCode ==
              PUBLISH_COMPOSE_ITEM_TYPE.OFFER_STRT
                ? StructureIcon
                : RelationIcon,
            propsAppend: { width: "11", height: "11" },
          };
        case OFFER_TYPE.DISCOUNT:
          return {
            component: DIcon,
            componentAppend:
              props.data.chngDataTypeCode ==
              PUBLISH_COMPOSE_ITEM_TYPE.OFFER_STRT
                ? StructureIcon
                : RelationIcon,
            propsAppend: { width: "11", height: "11" },
          };
        case OFFER_TYPE.DEVICE:
          return {
            component: DeviceIcon,
            componentAppend:
              props.data.chngDataTypeCode ==
              PUBLISH_COMPOSE_ITEM_TYPE.OFFER_STRT
                ? StructureIcon
                : RelationIcon,
            props: { width: "16", height: "16" },
          };
        default:
          return { component: HelpIcon, props: { size: "15" } };
      }
    case PUBLISH_COMPOSE_ITEM_TYPE.COMPONENT:
      return { component: ComponentIcon, props: { width: "6", height: "6" } };
    case PUBLISH_COMPOSE_ITEM_TYPE.COMPONENT_STRT:
      return {
        component: ComponentIcon,
        componentAppend: StructureIcon,
        props: { width: "6", height: "6" },
        propsAppend: { width: "11", height: "11" },
      };
    case PUBLISH_COMPOSE_ITEM_TYPE.RESOURCE:
      switch (props.data.chngDataItemCode) {
        case RESOURCE_ITEM_CODE.SERVICE_ELEMENT:
          return {
            component: SLinearIcon,
            props: { width: "9", height: "12" },
          };
        case RESOURCE_ITEM_CODE.RATING_ELEMENT:
          return {
            component: RLinearIcon,
            props: { width: "9", height: "12" },
          };
        case RESOURCE_ITEM_CODE.BILLING_ELEMENT:
          return {
            component: BLinearIcon,
            props: { width: "9", height: "12" },
          };
        default:
          return { component: HelpIcon, props: { size: "15" } };
      }
    case PUBLISH_COMPOSE_ITEM_TYPE.GROUP:
      return {
        component: FolderIcon,
        props: { width: "11", height: "10" },
      };
    case PUBLISH_COMPOSE_ITEM_TYPE.GROUP_STRT:
      return {
        component: FolderIcon,
        componentAppend: StructureIcon,
        props: { width: "11", height: "10" },
        propsAppend: { width: "11", height: "11" },
      };
    case PUBLISH_COMPOSE_ITEM_TYPE.MTL_ENTITY:
      return {
        component: MultiEntityIcon,
        props: { width: "11", height: "11", fill: "#7D82EF" },
      };
    case PUBLISH_COMPOSE_ITEM_TYPE.RELATION:
      return { component: RelationIcon, props: { width: "11", height: "11" } };
    case PUBLISH_COMPOSE_ITEM_TYPE.CATEGORY:
      return { component: CategoryIcon, props: { width: "11", height: "11" } };
    case PUBLISH_COMPOSE_ITEM_TYPE.TABLE:
      return { component: TableIcon, props: { width: "11", height: "11" } };
    case PUBLISH_COMPOSE_ITEM_TYPE.FACTOR:
      return { component: DecoIcon, props: { width: "11", height: "11" } };
    case PUBLISH_COMPOSE_ITEM_TYPE.MATRIX:
      return { component: MatrixIcon };
    default:
      return { component: HelpIcon, props: { size: "15" } };
  }
});

const calcContentWidth = () => {
  if (iconBox.value) {
    contentStyle.value.width = `calc(100% - ${
      typeBox.value ? typeBox.value?.clientWidth + 4 : 0
    }px)`;
    descStyle.value.width = `calc(100% - ${iconBox.value.clientWidth + 8}px)`;
  }
};

const handleDragStart = (event, element) => {
  event.target.classList?.remove("zoom-animation");
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
  document.body.appendChild(hiddenDragImg);
  document.body.appendChild(dragImg);
  event.dataTransfer?.setDragImage(hiddenDragImg, 0, 0);
  props.onDragStart?.(event);
};

const handleDragEnd = (event) => {
  event.target.classList?.add("zoom-animation");
  const hiddenDragImg = document.getElementById("hiddenDragImg");
  const dragImage = document.getElementById("dragImg");
  props.onDragEnd?.(event);
  dragImage?.remove();
  hiddenDragImg?.remove();
};

const handleDrag = (event) => {
  const dragImage = document.getElementById("dragImg");
  if (dragImage && event.pageX !== 0 && event.pageY !== 0) {
    dragImage.style.left = `${event.pageX}px`;
    dragImage.style.top = `${event.pageY}px`;
  }
};

onMounted(() => {
  calcContentWidth();
});

onUpdated(() => {
  calcContentWidth();
});
</script>
<style lang="scss" scoped>
.border-card {
  border: solid 2px white;
}
.layout-card {
  box-shadow:
    -2px -2px 24px 0px #0000000a inset,
    1px 1px 12px 0px #0000001f;
}
.icon-box {
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #fff;
  height: 24px;
  min-width: 24px;
  text-align: center;
  border-radius: 4px;
  box-shadow: 0px 1px 2px 0px #191f4a0f;
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
</style>
