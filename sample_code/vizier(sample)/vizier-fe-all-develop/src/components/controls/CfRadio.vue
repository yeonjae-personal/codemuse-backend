<script setup lang="ts">
const props = defineProps({
  radioItems: {
    type: Array<any>,
    required: true,
  },
  label: {
    type: String,
    required: true,
  },
  value: {
    type: String,
    required: true,
  },
});

const emit = defineEmits(["update:modelValue"]);

const selectedValue = ref("");

const handleChange = () => {
  emit("update:modelValue", selectedValue.value);
};
</script>

<template>
  <v-radio-group v-model="selectedValue" @change="handleChange">
    <v-radio
      v-for="item in radioItems"
      :key="item[props.value]"
      :label="item[props.label]"
      :value="item[props.value]"
    ></v-radio>
  </v-radio-group>
</template>
