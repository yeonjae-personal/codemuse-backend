<script setup lang="ts">
const props = defineProps({
  clearable: {
    type: Boolean,
    default: false,
  },
  disabled: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits(["updateModel"]);

const handleFileChange = (event: any) => {
  emit("updateModel", event);
};
</script>

<template>
  <v-file-input
    :clearable="props.clearable"
    :disabled="props.disabled"
    @update:model-value="handleFileChange"
  ></v-file-input>
</template>

<style scoped></style>
