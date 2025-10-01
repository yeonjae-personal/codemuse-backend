<template>
  <div
    class="attribute-wrapper zoom-animation"
    :class="{
      selected: showSelected,
      required: isRequiredField,
    }"
    draggable="true"
    @dragstart="(event) => handleDragStart(event, props.item)"
    @dragend="handleDragEnd"
    @drag="handleDrag"
    @click="handleClickItem"
  >
    <div class="attribute-content">
      <span class="attribute-title">{{ $t(props.item.name) }}</span>
      <div class="attribute-info">
        <span class="attribute-code">{{ props.item.attrType }}</span>
        <div
          v-if="props.item.condition || props.item.action"
          class="attribute-type"
        >
          <span v-if="props.item.condition" class="blue"></span>
          <span v-else class="white"></span>
          <span v-if="props.item.action" class="red"></span>
          <span v-else class="white"></span>
        </div>
        <div v-else class="attribute-type"><span class="gray"></span></div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import useDragUserPocket from "@/composables/useDragUserPocket";
import { BORDER_CONFIG } from "@/constants/index";
import { RequiredFieldType } from "@/enums/customValidation";
import customValidationStore from "@/store/admin/customValidation.store";

const props = defineProps({
  item: {
    type: Object,
    default: () => {},
  },
  showSelected: {
    type: Boolean,
    default: false,
  },
});

const emits = defineEmits(["clickItem"]);
const { updateDragItemType } = customValidationStore();
const { handleDragUserPocket } = useDragUserPocket();

const isRequiredField = computed(
  () => props.item.requiredYn === RequiredFieldType.Yes
);

const defaultBorderActive = ref(BORDER_CONFIG.ACTIVE);

const handleDragStart = (event: DragEvent, item: any): void => {
  const hiddenDragImg = (event.target as HTMLElement).cloneNode(
    true
  ) as HTMLElement;
  hiddenDragImg.id = "hiddenDragImg";
  hiddenDragImg.style.opacity = "0";
  const dragImg = (event.target as HTMLElement).cloneNode(true) as HTMLElement;
  dragImg.id = "dragImg";
  dragImg.style.position = "absolute";
  dragImg.style.width = "366px";
  dragImg.style.zIndex = "1000";
  document.body.appendChild(hiddenDragImg);
  document.body.appendChild(dragImg);
  event.dataTransfer?.setDragImage(hiddenDragImg, 0, 0);
  handleDragUserPocket(event, item);
  updateDragItemType(item.type);
};

const handleDragEnd = () => {
  const hiddenDragImg = document.getElementById("hiddenDragImg") as HTMLElement;
  const dragImage = document.getElementById("dragImg") as HTMLElement;
  dragImage.remove();
  hiddenDragImg.remove();
  updateDragItemType("");
};
const handleDrag = (event: DragEvent) => {
  const dragImage = document.getElementById("dragImg") as HTMLElement;
  if (dragImage) {
    dragImage.style.left = `${event.pageX}px`;
    dragImage.style.top = `${event.pageY}px`;
  }
};
const handleClickItem = () => {
  emits("clickItem", props.item.id);
};
</script>
<style lang="scss" scoped>
.attribute-wrapper:has(.blue, .red) {
  background: linear-gradient(
    105.78deg,
    #effaff 26.93%,
    #def5ff 63.74%,
    #c3e8f7 85.24%,
    #bce4f5 91.25%
  );
  box-shadow:
    6px 8px 10px 0px #0000000a,
    3px 3px 4px 0px #0000001f;
  position: relative;
  &:before {
    position: absolute;
    top: 0;
    left: 0;
    content: "";
    width: 100%;
    height: 100%;
    border-radius: 8px;
    border-left: 1px solid #b2ddff;
  }
}
.attribute-wrapper {
  height: 40px;
  border-radius: 8px;
  box-shadow:
    4px 4px 40px 0px #1b2e5c14,
    4px 4px 18px -4px #1b2e5c1f;
  font-family: "Noto Sans KR";
  position: relative;
  &:hover {
    cursor: pointer;
  }

  &:before {
    position: absolute;
    top: 0;
    left: 0;
    content: "";
    width: 100%;
    height: 100%;
    border-radius: 8px;
    border-left: 1px solid #e6e9ed;
  }

  &.required {
    border-left: none;
    &:before {
      position: absolute;
      top: 0;
      left: 0;
      content: "";
      width: 100%;
      height: 100%;
      border-radius: 8px;
      border-left: 2px solid #e0332d;
    }
  }

  .attribute-content:has(.blue, .red) {
    background: linear-gradient(
      105.78deg,
      #effaff 26.93%,
      #def5ff 63.74%,
      #c3e8f7 85.24%,
      #bce4f5 91.25%
    );
  }
  .attribute-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: #fff;
    padding: 4px 12px;
    border-radius: 6px;
    height: 100%;

    .attribute-title {
      font-size: 13px;
      line-height: 19.5px;
      letter-spacing: 0.25px;
      font-weight: 500;
      color: #3a3b3d;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }

    .attribute-info {
      display: flex;
      align-items: center;
      .attribute-code {
        font-size: 13px;
        line-height: 19.5px;
        letter-spacing: 0.25px;
        margin-right: 14px;
        color: #6b6d70;
      }
      .attribute-type {
        display: flex;
        flex-direction: column;
        row-gap: 4px;
        .blue {
          width: 4px;
          height: 4px;
          border-radius: 50%;
          background: #4054b2;
        }
        .red {
          width: 4px;
          height: 4px;
          border-radius: 50%;
          background: #d9325a;
        }
        .white {
          width: 4px;
          height: 4px;
          border-radius: 50%;
          background: transparent;
        }
        .gray {
          width: 4px;
          height: 4px;
          border-radius: 50%;
          background: #fff;
        }
      }
    }
  }
}
.selected:has(.blue, .red) {
  border: 2px solid v-bind(defaultBorderActive) !important;
}
</style>
