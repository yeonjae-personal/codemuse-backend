<template>
  <v-radio-group v-model="computedValue" :name="groupName" inline>
    <v-radio :label="yesLabel" :value="yesValue" color="#D9325A"></v-radio>
    <v-radio :label="noLabel" :value="noValue" color="#D9325A"></v-radio>
  </v-radio-group>
</template>

<script>
export default {
  props: {
    modelValue: {
      type: String,
      default: null,
    },
    initialValue: {
      type: String,
      default: null,
    },
    yesValue: {
      type: String,
      default: "yes",
    },
    noValue: {
      type: String,
      default: "no",
    },
    yesLabel: {
      type: String,
      default: "Yes",
    },
    noLabel: {
      type: String,
      default: "No",
    },
    groupName: {
      type: String,
      default: "custom-radio-group",
    },
    color: {
      type: String,
      default: "#D9325A",
    },
  },
  emits: ["update:modelValue"],
  setup(props, { emit }) {
    const computedValue = computed({
      get() {
        return props.modelValue !== null
          ? props.modelValue
          : props.initialValue;
      },
      set(value) {
        emit("update:modelValue", value);
      },
    });

    return {
      computedValue,
    };
  },
};
</script>

<style scoped>
:deep().v-input__details {
  display: none;
}
:deep(.v-input) {
  margin-bottom: 0px;
}
:deep(.v-selection-control-group) {
  justify-content: space-between;
  column-gap: 8px;
  row-gap: 12px;
}
:deep(.v-label) {
  font-family: "Noto Sans KR", sans-serif !important;
  font-size: 13px;
  color: #3a3b3d;
  font-weight: 500;
}
</style>
