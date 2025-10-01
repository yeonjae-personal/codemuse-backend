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
  rounded: {
    type: String,
    default: undefined,
  },
});

const emit = defineEmits(["click"]);

const handleClick = () => {
  emit("click");
};
</script>

<template>
  <v-btn
    class="common-button v-btn--size-default"
    :size="props.size"
    :rounded="props.rounded"
    @click="handleClick"
  >
    {{ props.label }}
  </v-btn>
</template>

<style scoped>
.common-button {
  display: inline-flex;
  justify-content: center;
  align-items: center;
  border: none;
  color: #fff;
  background-color: #e6007e;
  text-transform: unset;
}

.common-button:hover {
  color: #fff;
  background-color: #730024;
}
</style>
