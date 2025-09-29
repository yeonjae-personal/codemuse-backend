<script setup lang="ts">
import LayerIcon from "@/components/prod/icons/LayerIcon.vue";
import { useWindowSize } from "@vueuse/core";

const windowSize = useWindowSize();
const inputContain = ref<any>(null);
const parentElement = ref<any>(null);
const positionSelectList = ref<any>({ top: "0px", left: "0px", width: "0px" });
const vListHeight = ref<String>("0px");
const translateY = ref<String>("");
const props = defineProps({
  attr: {
    type: String,
    default: "",
  },
  options: {
    type: Array as PropType<any[]>,
    default: () => [],
  },
  modelValue: {
    type: Array,
    default: () => [],
  },
  disabled: {
    type: Boolean,
    default: false,
  },
  openDefault: {
    type: Boolean,
    default: false,
  },
  required: {
    type: Boolean,
    default: false,
  },
  disabledLayerIcon: {
    type: Boolean,
    default: false,
  },
  isShowChip: {
    type: Boolean,
    default: false,
  },
  canDropUp: {
    type: Boolean,
    default: false,
  },
  disabledOption: {
    type: Boolean,
    default: false,
  },
  isDisabledTooltip: {
    type: Boolean,
    default: false,
  },
});

const isInvalid = ref(false);

const selectedValues = computed<any[]>({
  get: () => props.modelValue,
  set: (value) => emit("update:modelValue", value),
});
const handleClose = (value) => {
  if (!props.disabled)
    selectedValues.value = selectedValues.value.filter((i) => i !== value);
};

const emit = defineEmits(["update:modelValue", "checkValid", "change-value"]);

const isOpen = ref(props.openDefault);

const toggleCheckbox = (value) => {
  if (props.disabled) return;
  const index = selectedValues.value.indexOf(value);
  if (index !== -1) {
    selectedValues?.value?.splice(index, 1);
  } else {
    selectedValues?.value?.push(value);
  }
};

const firstValueSelected = computed(() => {
  return (
    props.options.find((item) => selectedValues.value?.includes(item.value))
      ?.label || ""
  );
});

const handleBlur = () => {
  // isOpen.value = false;
  if (!props.required) return;
  isInvalid.value = props.required && !selectedValues.value?.length;
  emit("checkValid", isInvalid.value);
};
const resetValidate = () => {
  isInvalid.value = false;
};
const handleCloseSelectBox = () => {
  isOpen.value = false;
};

const handleOpen = (): void => {
  if (props.disabledOption) {
    return;
  }
  if (inputContain.value) {
    isOpen.value = true;
    const rect = inputContain.value.getBoundingClientRect();
    const vList =
      20 * props.options.length + 12 * (props.options.length - 1) + 24 < 160
        ? 20 * props.options.length + 12 * (props.options.length - 1) + 24
        : 160;
    if (
      Math.floor(
        windowSize.height.value - rect.top - inputContain.value.clientHeight
      ) < vList
    ) {
      positionSelectList.value = {
        top: `${rect.top}px`,
        left: `${rect.left}px`,
        width: `${inputContain.value.clientWidth}px`,
      };
      translateY.value = `translateY(${-(vList + 10)}px)`;
    } else {
      positionSelectList.value = {
        top: `${rect.top + inputContain.value.clientHeight}px`,
        left: `${rect.left}px`,
        width: `${inputContain.value.clientWidth}px`,
      };
    }
    vListHeight.value = vList + "px";
  }
};

const handleClickOutside = (event: any): void => {
  if (!event.target.classList?.contains("multi-element")) {
    isOpen.value = false;
  }
};
const handleCloseOption = (event: any) => {
  if (!event.target.classList?.contains("multi-element")) {
    isOpen.value = false;
  }
};

onMounted(async () => {
  if (inputContain.value) {
    parentElement.value = inputContain.value.offsetParent;
  }
  if (!selectedValues.value) {
    selectedValues.value = [];
  }
  document.addEventListener("wheel", handleCloseOption, true);
  document.addEventListener("click", handleClickOutside, true);
});
onBeforeUnmount(() => {
  document.removeEventListener("wheel", handleCloseOption, true);
  document.removeEventListener("click", handleClickOutside, true);
});
watch(
  selectedValues,
  () => {
    emit("change-value", selectedValues.value);
  },
  {
    deep: true,
  }
);
defineExpose({
  validate: handleBlur,
  resetValidate: resetValidate,
  component: inputContain,
});
</script>
<template>
  <div
    ref="inputContain"
    class="multi-select bg-[#FFFFFF] rounded-[8px] border border-lighter relative"
    :class="[isInvalid ? 'multi-select-error' : '']"
    tabindex="0"
    @blur="handleBlur"
  >
    <div
      class="flex justify-between pt-[6px] pb-[6px] px-3 text-text-base text-[13px] font-normal !h-[32px] !max-h-[40px] tracking-[0.25px] leading-[16.5px]"
      :class="{
        'border-l-primary border-l-[2px] rounded-[8px]': required,
        'border-primary border-[1px] rounded-[8px] !border-l-[1px]': isInvalid,
        'disable-field': props.disabled,
      }"
      @click="handleOpen"
    >
      <div class="flex gap-1 items-center w-[calc(100%-46px)]">
        <LayerIcon v-if="!disabledLayerIcon" />
        <div v-if="isShowChip" class="flex gap-2">
          <v-chip
            v-for="(item, index) in selectedValues?.slice(0, 3)"
            :key="index"
            class="!h-[20px] !max-w-[60px] !min-w-[60px]"
            label
            @click.stop="handleClose(item)"
          >
            <p
              class="text-[#6B6D70] font-medium text-[11px] ml-[5px] min-w-[50px] max-w-[50px] truncate cursor-pointer text-center"
            >
              <CustomTooltip
                :content="
                  isShowChip
                    ? options.find((i) => i.value === item)?.label
                    : item
                "
                location="bottom"
              />
            </p>
          </v-chip>
        </div>
        <span v-else class="w-[calc(100%-22px)] truncate">
          <CustomTooltip
            :content="firstValueSelected"
            :disabled="isDisabledTooltip"
          />
        </span>
        <span v-if="isShowChip && selectedValues?.length > 3">...</span>
      </div>
      <div class="flex gap-1 items-center">
        <div
          class="w-6 rounded-[4px] bg-primary-lightest text-text-primary flex items-center justify-center"
          :class="isShowChip ? 'h-5' : 'h-6'"
        >
          {{ selectedValues?.length || 0 }}
        </div>
        <ChevronDown
          size="18"
          class="transition duration-300 ease-out"
          :class="{ 'rotate-180': isOpen }"
        />
      </div>
    </div>
    <Teleport to="body">
      <div
        v-if="selectedValues"
        class="wrapper"
        :class="{ open: isOpen }"
        :style="positionSelectList"
      >
        <LocomotiveComponent
          scroll-content-class="py-3 px-1 space-y-3 multi-element"
          scroll-container-class=" max-h-[172px] min-h-[0px] multi-element"
          is-stop-propagation-wheel
        >
          <template v-for="option in options" :key="option.value">
            <div
              class="flex gap-2 items-center multi-element"
              @click.stop.prevent="toggleCheckbox(option.value)"
            >
              <div
                class="relative custom-checkbox multi-element"
                :class="{
                  checked: selectedValues.includes(option.value),
                  'checked-disabled':
                    selectedValues.includes(option.value) && disabled,
                  disabled: disabled && !selectedValues.includes(option.value),
                  'cursor-pointer': !disabled,
                }"
                :aria-checked="selectedValues.includes(option.value)"
                role="checkbox"
                :aria-labelledby="`label-${option.value}`"
              ></div>
              <CustomTooltip
                class="multi-element"
                content-class="multi-element"
              >
                <label
                  class="text-text-base text-[13px] font-normal tracking-[0.25px] leading-[16.5px] multi-element cursor-pointer truncate"
                >
                  {{ option.label }}
                </label>
                <template #content>
                  <span class="text-inherit text-[length:inherit] multi-element"
                    >{{ option.label }}
                  </span>
                </template>
              </CustomTooltip>
            </div>
          </template>
        </LocomotiveComponent>
      </div>
    </Teleport>
    <div v-if="required" class="error-message" :class="[{ show: isInvalid }]">
      <div class="error-message-text">
        {{ $t("product_platform.validate.requiredFieldInput") }}
      </div>
    </div>
    <div
      v-if="isOpen"
      class="absolute top-0 left-0 w-full h-full"
      @click="handleCloseSelectBox"
    ></div>
  </div>
</template>

<style lang="scss" scoped>
.wrapper {
  height: 0px;
  box-shadow: 2px 2px 16px 0px #0000001f;
  position: absolute;
  display: grid;
  overflow: hidden;
  background: #fff;
  font-family: "Noto Sans KR", sans-serif !important;
  opacity: 0;
  border-radius: 8px;
  margin-top: 5px;
  transition:
    height 100ms,
    transform 300ms,
    opacity 300ms;

  .custom-checkbox {
    min-width: 20px;
    height: 20px;
    background-color: #ffffff;
    border: 2px solid #dce0e5;
    border-radius: 6px;
  }
  .checked {
    background-color: #d9325a;
    border-color: #d9325a;
  }
  .checked-disabled {
    background: #fdced5;
    border-color: #fdced5;
  }

  .disabled {
    background: #f0f2f5;
    border-color: #e6e9ed;
  }
  .custom-checkbox.checked::after {
    content: url("@/assets/icons/checked.svg");
    font-size: 16px;
    color: white;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -40%);
  }
  &:hover {
    .error-message.show {
      opacity: 1;
      height: auto;
      min-height: 14px;
      padding: 6px 15px;
    }
  }
  &::before {
    content: "";
    opacity: 0;
    z-index: 2;
    position: absolute;
    right: 8px;
    top: -12px;
    background: var(--bg-inverse-bg-darker, #525457);
    transform: rotate(45deg);
    transition: 0.3s;
  }
}
.multi-select-error {
  &:hover {
    &::before {
      opacity: 1;
      width: 10px;
      height: 10px;
    }
  }
}
.expandable {
  min-height: 0;
  max-height: 210px;
}

.open {
  position: absolute;
  z-index: 2400;
  height: v-bind(vListHeight);
  transform: v-bind(translateY);
  opacity: 1;
  // border-top-right-radius: 0;
  // border-top-left-radius: 0;
  // border-bottom-right-radius: 10px;
  // border-bottom-left-radius: 10px;
}

.custom-scroll::-webkit-scrollbar {
  height: 6px;
  width: 6px;
}

.custom-scroll::-webkit-scrollbar-track {
  background: #e6e9ed;
}

.custom-scroll::-webkit-scrollbar-thumb {
  background: #bdc1c7;
  border-radius: 8px;
}

.custom-scroll::-webkit-scrollbar-thumb:hover {
  background: #bdc1c7;
  border-radius: 8px;
}
:deep(.v-chip__close) {
  font-size: 11px !important;
  color: #bdc1c7;
}
:deep(.v-chip) {
  padding: unset !important;
}

.border-top-left {
  border-radius: 12px 12px 0px 0px !important;
}
.border-bottom-right {
  border-radius: 0px 0px 12px 12px !important;
}
.error-message {
  display: flex;
  align-items: flex-end;
  font-size: 0.75rem;
  font-weight: 400;
  letter-spacing: 0.0333333333em;
  line-height: normal;
  overflow: hidden;
  justify-content: space-between;
  min-width: 80px;
  height: 0px;
  min-height: 0px;
  width: -moz-max-content !important;
  width: max-content !important;
  opacity: 0;
  position: absolute;
  bottom: 37px;
  right: 0px;
  background: var(--bg-inverse-bg-darker, #525457);
  border-radius: 4px;
  padding: 0px;
  box-shadow: 0px 2px 20px 0px rgba(0, 0, 0, 0.1019607843);
  transition: 0.3s;
}
.error-message-text {
  color: #fff;
}
</style>
