<template>
  <div
    :class="[
      'drop-item-area',
      className,
      { 'is-dragging': isDragging, 'is-disabled': isDisabled },
    ]"
    @drop="handleDrag($event)"
    @dragover="handleDragOver($event)"
    @dragleave="handleDragLeaveLeave"
  >
    <FileIcon :color="colorIcon" />
    <span
      :class="[
        'drop-item-area__title',
        { 'is-dragging': isDragging, 'is-disabled': isDisabled },
      ]"
    >
      <slot>
        {{ $t("product_platform.drag_items") }}
      </slot>
    </span>
  </div>
</template>

<script setup lang="ts">
type Props = {
  className?: string;
  isDisabled?: boolean;
};

type Emits = {
  (e: "drop", event: DragEvent): void;
  (e: "dragover", event: DragEvent): void;
};

const props = withDefaults(defineProps<Props>(), {
  className: "",
  isDisabled: false,
});

const emit = defineEmits<Emits>();

const isDragging = ref<boolean>(false);

const colorIcon = computed<string>(() =>
  props.isDisabled ? "#bdc1c7" : isDragging.value ? "#3a3b3d" : "#6B6D70"
);
const handleDrag = (event: DragEvent): void => {
  if (props.isDisabled) return;
  emit("drop", event);
  isDragging.value = false;
};

const handleDragOver = (event: DragEvent): void => {
  if (props.isDisabled) return;
  emit("dragover", event);
  isDragging.value = true;
};

const handleDragLeaveLeave = (): void => {
  if (props.isDisabled) return;
  isDragging.value = false;
};
</script>

<style lang="scss" scoped>
.drop-item-area {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 4px;
  width: 100%;
  padding: 18px;
  border: 1px dashed #dce0e5;
  border-radius: 12px;
  background-color: #f7f8fa;
  transition: all 0.2s linear;
  font-size: 13px;
  cursor: pointer;
  will-change: transform;
  transform: translateZ(0);

  &.is-dragging {
    background-color: #fff;
    border-color: #3a3b3d;
    cursor: move;
  }

  &.is-disabled {
    border-color: #e6e9ed;
    cursor: no-drop;
  }

  &__title {
    font-family: "Noto Sans KR", sans-serif;
    font-size: inherit;
    line-height: 20px;
    letter-spacing: 0.25px;
    color: #6b6d70;
    transition: all 0.2s linear;

    &.is-dragging {
      color: #3a3b3d;
    }

    &.is-disabled {
      color: #bdc1c7;
    }
  }
}
</style>
