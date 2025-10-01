<template>
  <div
    v-click-outside="handleClickOutside"
    class="relative z-1 text-[13px] border rounded-lg !bg-white w-full h-full"
    @click="toggleDropdown"
  >
    <div
      :class="[
        'w-full flex items-center justify-between p-[7px] h-full cursor-pointer',
        style,
        {
          'justify-center': !isShowValueSelect,
          'pl-[12px]': isShowValueSelect,
        },
      ]"
    >
      <div>
        <div class="text-[10px] text-text-lighter">{{ props?.label }}</div>
        <div
          v-if="isShowValueSelect"
          class="text-text-base-detail"
          :class="!selectedOption ? 'placeholder-text' : ''"
        >
          {{ selectedOption ? selectedOption.name : props.placeholder }}
        </div>
      </div>
      <div>
        <v-icon>mdi-chevron-down</v-icon>
      </div>
    </div>
    <div
      v-show="isOpen"
      class="w-full absolute left-0 mt-1 !bg-white border border-base shadow-lg z-10 rounded-lg !min-w-[120px] font-size-base"
    >
      <div
        v-for="option in options"
        :key="option.id"
        :class="[
          'p-2 cursor-pointer hover:bg-gray-200',
          { 'bg-gray-200': option.id === selectedOption?.id },
        ]"
        @click="selectOption(option)"
      >
        {{ option.name }}
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
const props = defineProps({
  options: {
    type: Array<any>,
    required: true,
  },
  modelValue: {
    type: Object,
    default: null,
  },
  style: {
    type: String,
    default: () => "",
  },
  label: {
    type: String,
    default: () => "",
  },
  placeholder: {
    type: String,
    default: "Select an option",
  },
  isShowValueSelect: {
    type: Boolean,
    default: true,
  },
});

const emit = defineEmits(["update:modelValue"]);

const isOpen = ref(false);
const selectedOption = ref(null as any);

const toggleDropdown = (event: any) => {
  event.stopPropagation();
  isOpen.value = !isOpen.value;
};

const selectOption = (option: any) => {
  selectedOption.value = option;
  emit("update:modelValue", option);
  isOpen.value = false;
};
const handleClickOutside = () => {
  isOpen.value = false;
};
watch(
  () => props.modelValue,
  (newValue) => {
    selectedOption.value = props.options.find(
      (option: any) =>
        option.id === newValue?.id ||
        option.value === newValue?.value ||
        option.value == newValue
    );
  },
  { immediate: true }
);

onMounted(() => {
  selectedOption.value = props.options.find(
    (option: any) => option.id === props.modelValue?.id
  );
});
</script>

<style scoped>
.truncate {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.custom-style-offer-edit {
  height: 32px;
}
.placeholder-text {
  color: #bdc1c7;
}
</style>
