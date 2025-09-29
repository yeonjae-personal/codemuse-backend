<script setup lang="ts">
const props = defineProps({
  label: {
    type: String,
    required: true,
  },
  size: {
    type: String,
    default: "",
  },
  width: {
    type: String,
    default: "",
  },
  rounded: {
    type: String,
    default: undefined,
  },
  class: {
    type: String,
    default: "gray",
  },
  disabled: { type: Boolean, default: false },
});

const emit = defineEmits(["click"]);

const handleClick = () => {
  emit("click");
};
</script>

<template>
  <v-btn
    class="common-button"
    :class="props.class"
    :size="props.size"
    :rounded="props.rounded"
    :disabled="props.disabled"
    @click="handleClick"
  >
    <slot></slot>
    <span>{{ props.label }}</span>
  </v-btn>
</template>

<style scoped lang="scss">
.common-button {
  height: 40px !important;
  width: v-bind(width);
  display: inline-flex;
  justify-content: center;
  align-items: center;
  border: none;
  text-transform: unset;
  padding: 0px 16px;
  box-shadow: none;
  span {
    display: inline-block;
    margin-left: 6px;
    font-size: 13px;
    font-weight: 500;
    line-height: 13px;
    letter-spacing: 0.5px;
    text-align: left;
  }
  &:hover,
  &:focus {
    box-shadow: none;
  }
}
.orange {
  color: $color-3;
  background-color: $bg-color-4;
}
.gray {
  color: $color-1;
  background-color: $bg-color-1;
  border: 1px $border-color-1 solid;
}
</style>
