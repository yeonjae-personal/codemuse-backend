<script setup lang="ts">
import { SPACE, ALL } from "@/constants/index";
import { useWindowSize } from "@vueuse/core";

const props = defineProps({
  attr: {
    type: String,
    default: "",
  },
  height: {
    type: Number,
    default: 32,
  },
  className: {
    type: String,
    default: "",
  },
  options: {
    type: Array,
    default: () => [],
  },
  modelValue: {
    type: [String, Number],
    default: "",
  },
  placeholder: {
    type: String,
    default: "",
  },
  itemTitle: {
    type: String,
    default: "cmcdDetlNm",
  },
  itemValue: {
    type: String,
    default: "cmcdDetlId",
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
  isShowChip: {
    type: Boolean,
    default: false,
  },
  showOptionNull: {
    type: Boolean,
    default: false,
  },
  defaultItemSelectAll: {
    type: Boolean,
    default: false,
  },
  showErrorMassage: {
    type: Boolean,
    default: true,
  },
  isShowTooltip: {
    type: Boolean,
    default: true,
  },
  onlyChevronDown: {
    type: Boolean,
    default: true,
  },
  zIndex: {
    type: Number,
    default: 10,
  },
});
const windowSize = useWindowSize();
const emit = defineEmits(["update:modelValue", "checkValid"]);
const inputContain = ref<any>(null);
const parentElement = ref<any>(null);
const selectBox = ref<any>(null);
const selectedTitle = ref<String>("");
const positionSelectList = ref<any>({ top: "0px", left: "0px", width: "0px" });
const positionErrorBox = ref<any>({ bottom: "0px", left: "0px" });
const vListHeight = ref<String>("0px");
const isInvalid = ref(false);
const isShowMsgBox = ref(false);
const isOpen = ref(props.openDefault);
const showUp = ref(false);
const allowScrollTarget = ref(false);
const scrollYNumber = ref(0);

const selectedValues = computed<String | Number | null>({
  get: () => {
    getTitle();
    return props.modelValue;
  },
  set: (value) => {
    getTitle();
    emit("update:modelValue", value);
  },
});

const computedOptions = computed<any>(() => {
  if (!props.required && props.defaultItemSelectAll) {
    const defaultItem = {
      [props.itemTitle]: ALL,
      [props.itemValue]: SPACE,
    };
    return [defaultItem, ...props.options];
  } else if (props.showOptionNull && !props.required) {
    const defaultItem = {
      [props.itemTitle]: SPACE,
      [props.itemValue]: "",
    };
    return [defaultItem, ...props.options];
  }
  return props.options;
});

const getTitle = () => {
  if (props.modelValue && props.options?.length) {
    const obj: any = computedOptions.value.find(
      (item: any) => item?.cmcdDetlId === props.modelValue
    );
    selectedTitle.value = obj?.cmcdDetlNm;
  } else {
    selectedTitle.value = "";
  }
};

const handleBlur = () => {
  isOpen.value = false;
  allowScrollTarget.value = false;
  if (!props.required) return;

  isInvalid.value =
    props.required &&
    (!selectedValues.value || !String(selectedValues.value).trim?.());
  emit("checkValid", isInvalid.value);
};
const resetValidate = () => {
  isInvalid.value = false;
};
const handleClick = (item) => {
  selectedValues.value = item.cmcdDetlId;
};
const onExpand = () => {
  if (props.disabled) {
    return;
  }
  isOpen.value = !isOpen.value;
  if (isOpen.value) {
    if (inputContain.value) {
      const rect = inputContain.value.getBoundingClientRect();
      const vList =
        computedOptions.value?.length < 4
          ? computedOptions.value?.length * 40
          : 160;
      if (
        Math.floor(windowSize.height.value - rect.top - props.height) < vList
      ) {
        positionSelectList.value = {
          bottom: `${windowSize.height.value - rect.top}px`,
          left: `${rect.left}px`,
          width: `${inputContain.value.clientWidth}px`,
        };
      } else {
        positionSelectList.value = {
          top: `${rect.top + props.height}px`,
          left: `${rect.left}px`,
          width: `${inputContain.value.clientWidth}px`,
        };
      }
      vListHeight.value = vList + "px";
    }
    findActiveItemPosition();
  } else {
    allowScrollTarget.value = false;
  }
};

const handleCloseOption = (event: any) => {
  if (!event.target.classList?.contains("select-element")) {
    isOpen.value = false;
    allowScrollTarget.value = false;
  }
};
const handleShowMsgBox = () => {
  const rect = inputContain.value.getBoundingClientRect();
  positionErrorBox.value = {
    bottom: `${windowSize.height.value - rect.top + 4}px`,
    right: `${
      windowSize.width.value - rect.left - inputContain.value.clientWidth
    }px`,
  };
  isShowMsgBox.value = isInvalid.value;
};

const changeDirectionIcon = () => {
  if (props.onlyChevronDown) {
    showUp.value = !props.onlyChevronDown;
    return;
  }
  if (inputContain.value) {
    const rect = inputContain.value.getBoundingClientRect();
    const vList =
      computedOptions.value?.length < 4
        ? computedOptions.value?.length * 40
        : 160;
    showUp.value =
      Math.floor(windowSize.height.value - rect.top - props.height) < vList;
  }
};

const findActiveItemPosition = () => {
  const activeItem = selectBox.value.querySelectorAll(".active-menu");
  if (activeItem[0]) {
    const selectListDom =
      selectBox.value.querySelectorAll(".custom-hover-menu");
    const contentScroll =
      selectBox.value.querySelectorAll(".scroll-content")[0];
    if (
      activeItem[0].offsetTop + activeItem[0].clientHeight * 4 >
      contentScroll.offsetHeight
    ) {
      if (contentScroll.offsetHeight < activeItem[0].clientHeight * 4) {
        scrollYNumber.value = 0;
      } else {
        scrollYNumber.value = selectListDom[selectListDom.length - 4].offsetTop;
      }
    } else {
      if (activeItem[0].previousElementSibling) {
        scrollYNumber.value = activeItem[0].previousElementSibling.offsetTop;
      } else scrollYNumber.value = activeItem[0].offsetTop;
    }
    allowScrollTarget.value = true;
  }
};

watch(
  () => props.required,
  () => {
    isInvalid.value = false;
  }
);

watch(
  () => props.options,
  () => {
    changeDirectionIcon();
  },
  { immediate: true }
);

onUpdated(async () => {
  getTitle();
});

onMounted(() => {
  if (!props.required && !props.modelValue && props.defaultItemSelectAll) {
    selectedValues.value = SPACE;
  } else if (!props.required && !props.modelValue && props.showOptionNull) {
    selectedValues.value = null;
  }
  getTitle();
  if (inputContain.value) {
    parentElement.value = inputContain.value.offsetParent;
  }
  changeDirectionIcon();
  document.addEventListener("wheel", handleCloseOption, true);
});

onUnmounted(() => {
  document.removeEventListener("wheel", handleCloseOption, true);
});

defineExpose({
  validate: handleBlur,
  resetValidate: resetValidate,
  component: inputContain,
});
</script>
<template>
  <div
    ref="inputContain"
    class="custom-select bg-[#FFFFFF] rounded-[8px] border border-lighter relative"
    :class="[
      className,
      isInvalid && showErrorMassage ? 'custom-select-error' : '',
    ]"
    tabindex="0"
    @blur="handleBlur"
    @mouseover="handleShowMsgBox"
    @mouseleave="
      () => {
        isShowMsgBox = false;
      }
    "
  >
    <div
      class="flex justify-between align-center px-3 text-text-base text-[13px] tracking-[0.25px] leading-[16.5px] cursor-pointer gap-1"
      :class="{
        'border-l-primary border-l-[2px] rounded-[8px]': required,
        'border-primary border-[1px] rounded-[8px] !border-l-[1px]': isInvalid,
        'disable-field': props.disabled,
        disabled: props.disabled,
      }"
      :style="{ height: height + 'px' }"
      @click="onExpand"
    >
      <div
        v-if="placeholder"
        class="flex flex-column justify-center h-full font-[400] w-[85%]"
      >
        <span
          class="placeholder-custom"
          :class="[{ small: selectedTitle || isOpen }]"
          >{{ placeholder }}
        </span>
        <span
          class="text-ellipsis value-custom"
          :class="[{ 'show-value': selectedTitle || isOpen }]"
        >
          <CustomTooltip
            :content="selectedTitle || ''"
            :disabled="!selectedTitle"
          />
        </span>
      </div>
      <div v-else class="flex items-center h-full font-[400] w-[85%]">
        <CustomTooltip
          :content="selectedTitle || ''"
          :disabled="!(isShowTooltip && selectedTitle)"
        />
      </div>
      <div class="flex items-center">
        <ChevronUp
          v-if="showUp"
          size="18"
          class="transition duration-150 ease-out"
          :class="{ 'rotate-180': isOpen }"
        />
        <ChevronDown
          v-if="!showUp"
          size="18"
          class="transition duration-150 ease-out"
          :class="{ 'rotate-180': isOpen }"
        />
      </div>
    </div>
    <Teleport to="body">
      <div
        ref="selectBox"
        class="wrapper"
        :class="{ open: isOpen }"
        :style="positionSelectList"
      >
        <LocomotiveComponent
          scroll-container-class="pl-0 pr-[0px] max-h-[160px] min-h-[0px]"
          :is-scroll-when-add-new="allowScrollTarget"
          :number-scroll-y="scrollYNumber"
          is-stop-propagation-wheel
        >
          <v-list density="compact" class="select-element">
            <v-list-item
              v-for="item in computedOptions"
              :id="item?.cmcdDetlId"
              :key="item?.cmcdDetlId"
              :value="item?.cmcdDetlId"
              :class="[
                'my-0 py-2 relative custom-hover-menu item-title-custom select-element',
                {
                  'active-menu': selectedValues === item?.cmcdDetlId,
                },
              ]"
              @mousedown="handleClick(item)"
            >
              <v-list-item-title
                class="text-text-base !text-[13px] !leading-[19.5px] !font-[400] !tracking-[0.1px] select-element"
                :class="{
                  '!text-text-primary': selectedValues === item?.cmcdDetlId,
                }"
              >
                {{ item.cmcdDetlNm }}
              </v-list-item-title>
            </v-list-item>
          </v-list>
        </LocomotiveComponent>
      </div>
      <div
        v-if="showErrorMassage && required"
        class="error-message"
        :class="[{ 'error-message-show': isShowMsgBox }]"
        :style="positionErrorBox"
      >
        <div class="error-message-text">
          {{ $t("product_platform.validate.requiredFieldInput") }}
        </div>
      </div>
    </Teleport>
  </div>
</template>

<style lang="scss" scoped>
.custom-select {
  font-family: "Noto Sans KR", sans-serif !important;
  .custom-checkbox {
    width: 20px;
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
}

.wrapper {
  height: 0px;
  box-shadow: 2px 2px 16px 0px #0000001f;
  position: absolute;
  display: grid;
  overflow: hidden;
  opacity: 0;
  border-radius: 8px;
  margin-top: 5px;
  transition:
    height 100ms,
    transform 300ms,
    opacity 300ms;
}

.open {
  z-index: v-bind(zIndex);
  height: v-bind(vListHeight);
  opacity: 1;
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
  justify-content: space-between;
  min-width: 80px;
  height: 0px;
  min-height: 0px;
  width: -moz-max-content !important;
  width: max-content !important;
  opacity: 0;
  position: absolute;
  background: var(--bg-inverse-bg-darker, #525457);
  border-radius: 4px;
  padding: 0px;
  box-shadow: 0px 2px 20px 0px rgba(0, 0, 0, 0.1019607843);
  transition:
    min-height 0.15s,
    height 0.15s,
    opacity 0.15s;

  &::before {
    content: "";
    display: none;
    width: 10px;
    height: 10px;
    opacity: 1;
    z-index: 2;
    position: absolute;
    right: 8px;
    top: 20px;
    background: var(--bg-inverse-bg-darker, #525457);
    transform: rotate(45deg);
  }
}
.error-message-show {
  opacity: 1;
  height: 28px;
  min-height: 14px;
  padding: 6px 15px;
  z-index: v-bind(zIndex);
  &::before {
    display: block;
  }
}
.error-message-text {
  color: #fff;
}
.active-menu {
  background-color: #fff0f2 !important;
}

.active-menu:hover {
  background-color: #fff0f2 !important;
}
.item-title-custom {
  padding-inline: 12px !important;
}
.placeholder-custom {
  color: #000;
  opacity: 0.4;
  font-size: 13px;
  transition: all 0.15s ease-out;
}
.small {
  font-size: 10px;
}
.value-custom {
  height: 0px;
  display: flex;
  align-items: center;
  transition: all 0.15s ease-out;
}
.show-value {
  height: 28px;
}
.select-element {
  font-family: "Noto Sans KR", sans-serif !important;
}
:deep(.v-list-item--active > .v-list-item__overlay) {
  opacity: 0;
}
</style>
