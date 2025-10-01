<script lang="ts">
import VueDatePicker from "@vuepic/vue-datepicker";
import "@vuepic/vue-datepicker/dist/main.css";

export default defineComponent({
  name: "DatetimePicker",
  components: { VueDatePicker },
  props: {
    model: {
      type: String || Date,
      default: null,
    },
  },
  emits: ["update:model"],
  setup(props, { emit }) {
    const internalValue = ref();

    if (props.model) {
      internalValue.value = new Date(props.model);
    }
    const formatTime = (num: number) => {
      return num < 10 ? `0${num}` : num;
    };
    const format = (date: any) => {
      const day = formatTime(date.getDate());
      const month = formatTime(date.getMonth() + 1);
      const year = date.getFullYear();

      const hour = formatTime(date.getHours());
      const min = formatTime(date.getMinutes());
      const second = formatTime(date.getSeconds());

      return `${year}-${month}-${day} ${hour}:${min}:${second}`;
    };

    const emitUpdateModel = () => {
      emit("update:model", internalValue.value);
    };

    return { internalValue, format, emitUpdateModel };
  },
});
</script>

<template>
  <div class="cf-datepicker-input">
    <VueDatePicker
      v-model="internalValue"
      enable-seconds
      :format="format"
      position="right"
      @update:model-value="emitUpdateModel"
    ></VueDatePicker>
  </div>
</template>

<style>
.cf-datepicker-input {
  div > input {
    height: 55px !important;
  }
}
</style>
