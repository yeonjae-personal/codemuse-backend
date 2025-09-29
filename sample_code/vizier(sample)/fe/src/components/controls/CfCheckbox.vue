<script setup lang="ts">
const props = defineProps({
  isGroup: {
    type: Boolean,
    default: false,
  },
  model: {
    type: [Boolean, Array] as PropType<boolean | string[]>,
    default: new Array(),
  },
  options: {
    type: Array<any>,
    default: () => [],
  },
  disabled: {
    type: Boolean,
    default: false,
  },
  indeterminate: {
    type: Boolean,
    default: false,
  },
  label: {
    type: String,
    default: undefined,
  },
  labelGroup: {
    type: String,
    default: undefined,
  },
  valueGroup: {
    type: String,
    default: undefined,
  },
});

const singleVal = ref(props.model);

const emit = defineEmits(["onChange", "onChangeGroup"]);
const onChange = () => {
  emit("onChange", singleVal.value);
};

const onChangeGroup = (event: any, item: any) => {
  emit("onChangeGroup", {
    value: event,
    item: item,
  });
};
</script>

<template>
  <v-checkbox
    v-if="!isGroup"
    v-model="singleVal"
    :label="props.label"
    :disabled="props.disabled"
    :indeterminate="props.indeterminate"
    @update:model-value="onChange"
  ></v-checkbox>

  <v-checkbox
    v-for="(item, index) in options"
    v-else
    :key="index"
    :value="item[`${props.valueGroup}`]"
    :label="item[`${props.labelGroup}`]"
    :disabled="item.disabled || disabled"
    :indeterminate="item.indeterminate"
    @update:model-value="onChangeGroup($event, item)"
  ></v-checkbox>
</template>

<style scoped>
/*  */
</style>
