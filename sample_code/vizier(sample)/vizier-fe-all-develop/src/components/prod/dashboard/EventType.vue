<template>
  <ul class="list-event">
    <li
      v-for="item in items"
      :key="item"
      :class="selectedItem === item ? `${item}-active` : item"
      @click="handleClick(item)"
    ></li>
  </ul>
</template>
<script setup>
const props = defineProps({
  modelValue: {
    type: String,
    default: "",
  },
});

const emits = defineEmits(["update:modelValue"]);

const items = ref(["red", "yellow", "blue"]);
const selectedItem = ref(props.modelValue);

const handleClick = (color) => {
  selectedItem.value = color;
  emits("update:modelValue", color);
};

watch(
  () => props.modelValue,
  (newValue) => {
    selectedItem.value = newValue;
  }
);
</script>

<style lang="scss" scoped>
.list-event {
  list-style: none;
  display: flex;
  gap: 13px;
  li {
    width: 22px;
    height: 22px;
    border-radius: 50%;
    cursor: pointer;
  }
  .red {
    background-image: radial-gradient(circle, #f98181 50%, #f7f8fa 55% 60%);
  }
  .red:hover {
    background-image: radial-gradient(circle, #ec3636 50%, #f7f8fa 55% 60%);
  }
  .red-active {
    background-image: radial-gradient(
      circle,
      #ec3636 50%,
      white 55% 60%,
      #ec3636 65% 90%
    );
  }
  .yellow {
    background-image: radial-gradient(circle, #f6d88d 50%, #f7f8fa 55% 60%);
  }
  .yellow:hover {
    background-image: radial-gradient(circle, #ffde2a 50%, #f7f8fa 55% 60%);
  }
  .yellow-active {
    background-image: radial-gradient(
      circle,
      #ffde2a 50%,
      white 55% 60%,
      #ffde2a 65% 90%
    );
  }
  .blue {
    background-image: radial-gradient(circle, #a6e6ff 50%, #f7f8fa 55% 60%);
  }
  .blue:hover {
    background-image: radial-gradient(circle, #48cafe 50%, #f7f8fa 55% 60%);
  }
  .blue-active {
    background-image: radial-gradient(
      circle,
      #48cafe 50%,
      white 55% 60%,
      #48cafe 65% 90%
    );
  }
}
</style>
