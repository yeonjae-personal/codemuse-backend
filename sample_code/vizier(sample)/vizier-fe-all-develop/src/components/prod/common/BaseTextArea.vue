<template>
  <div
    ref="textAreaWrapper"
    @mouseover="handleOpenFixedBox"
    @mouseout="handleCloseFixedBox"
  >
    <LocomotiveComponent
      :scroll-container-class="[
        'bg-white max-h-[130px] px-[13px] rounded-[8px] border border-[#dce0e5]',
        { required: required, 'invalid-err': invalid || overMaxLength },
        locomotiveContainerClass,
        { '!bg-[#f0f2f5]': disabled },
      ]"
      :scroll-content-class="[locomotiveContentClass, 'py-[12px]']"
      :number-scroll-y="scrollYNumber"
      :is-scroll-when-add-new="allowScrollTarget"
      :is-stop-propagation-wheel="isStopPropagationWheelStatus"
      @is-wheel="handleWheelDetail"
    >
      <textarea
        ref="textareaRef"
        v-model="valueInput"
        class="w-full min-h-[98px] overflow-hidden font-size-base textarea-component"
        :style="{
          lineHeight: '20px',
          resize: 'none',
          outline: 'none',
          height: textBoxHeight + 'px',
          color: '#3a3b3d',
          fontWeight: 400,
        }"
        :disabled="disabled"
        :maxlength="maxlength"
        :placeholder="placeholder"
        :class="textAreaClass"
        @input="updateVisualLine"
        @click="updateVisualLine"
        @keyup="updateVisualLine"
        @blur="handleBlur"
      >
      </textarea>
    </LocomotiveComponent>
    <Teleport v-if="rules?.maxLength && !disabled" to="body">
      <div
        ref="msgBox"
        :style="positionNotifyBox"
        class="message-box-wrapper"
        :class="[{ 'show-box': isOpenMsgBox }]"
      >
        <span class="count">
          {{ msgContent }}
        </span>
      </div>
    </Teleport>
  </div>
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";
import { useInputValidation } from "@/composables/useInputValidation";
import { useWindowSize } from "@vueuse/core";

const lineHeight = 20;
const props = defineProps({
  attr: {
    type: String,
    default: "",
  },
  label: {
    type: String,
    default: "",
  },
  placeholder: {
    type: String,
    default: "",
  },
  modelValue: {
    type: String,
    default: "",
  },
  disabled: {
    type: Boolean,
    default: false,
  },
  readonly: {
    type: Boolean,
    default: false,
  },
  maxlength: {
    type: [Number, String],
    default: null,
  },
  rules: {
    type: Object,
    default: () => ({}),
  },
  required: {
    type: Boolean,
    default: false,
  },
  errorMessages: {
    type: [String, Array],
    default: () => [],
  },
  counter: {
    type: [Boolean, Number],
    default: false,
  },
  refName: {
    type: String,
    default: "textarea",
  },
  showRequiredIcon: {
    type: Boolean,
    default: false,
  },
  hideDetails: {
    type: Boolean,
    default: false,
  },
  validateMode: {
    type: String,
    default: "blur",
  },
  textAreaClass: {
    type: String,
    default: "",
  },
  locomotiveContainerClass: {
    type: String,
    default: "",
  },
  locomotiveContentClass: {
    type: String,
    default: "",
  },
});
const { t } = useI18n();
const emit = defineEmits(["update:modelValue"]);
const { height } = useWindowSize();
const textAreaWrapper = ref<any>(null);
const textareaRef = ref<any>(null);
const msgBox = ref<any>(null);
const positionNotifyBox = ref<any>({ top: "0px", left: "0px" });
const isOpenMsgBox = ref(false);
const invalid = ref(false);
const overMaxLength = ref(false);
const textBoxHeight = ref(0);
const visualLine = ref(1);
const allowScrollTarget = ref(false);
const scrollYNumber = ref(0);
const isStopPropagationWheelStatus = ref(true);

const msgContent = computed(() => {
  if (invalid.value) {
    return `${t("product_platform.validate.requiredFieldInput")} ${
      props.modelValue?.length ?? 0
    } / ${props.rules?.maxLength}`;
  } else if (overMaxLength.value) {
    return `${t("product_platform.validate.maxLengthCharacter")} ${props.rules
      ?.maxLength} ${t("product_platform.validate.characters")} ${
      props.modelValue?.length ?? 0
    } / ${props.rules?.maxLength}`;
  } else {
    return `${props.modelValue?.length ?? 0} / ${props.rules?.maxLength}`;
  }
});
const updateVisualLine = () => {
  const ta = textareaRef.value;
  const selectionStart = ta.selectionStart;
  const div = document.createElement("div");
  const computed = getComputedStyle(ta);
  for (const prop of [
    "boxSizing",
    "width",
    "fontSize",
    "fontFamily",
    "lineHeight",
    "padding",
    "border",
    "whiteSpace",
    "letterSpacing",
  ]) {
    div.style[prop as string] = computed[prop as string] || "";
  }
  div.style.position = "absolute";
  div.style.visibility = "hidden";
  div.style.whiteSpace = "pre-wrap";
  div.style.wordWrap = "break-word";
  div.style.overflow = "auto";
  div.style.height = "auto";
  document.body.appendChild(div);
  const before = ta.value.slice(0, selectionStart);
  const after = ta.value.slice(selectionStart);
  div.textContent = before;
  const span = document.createElement("span");
  span.textContent = after.length ? after[0] : "\u200b";
  div.appendChild(span);
  const caretTop = span.offsetTop;
  visualLine.value = Math.floor(caretTop / lineHeight) + 1;
  document.body.removeChild(div);
  if (visualLine.value > 5) {
    textBoxHeight.value = 0;
    nextTick(() => {
      textBoxHeight.value = textareaRef.value.scrollHeight;
      allowScrollTarget.value = true;
      scrollYNumber.value = 130 + (visualLine.value - 5) * 20;
    });
  } else {
    allowScrollTarget.value = true;
    scrollYNumber.value = textAreaWrapper.value.clientHeight;
  }
};

const valueInput = computed({
  get() {
    return props.modelValue;
  },
  set(newValue) {
    emit("update:modelValue", newValue);
    if (textareaRef.value.scrollHeight > textareaRef.value.clientHeight) {
      setTimeout(() => {
        textBoxHeight.value = textareaRef.value.scrollHeight;
      }, 10);
    }
  },
});

const computedRules = computed(() => {
  return useInputValidation(props.rules);
});

const handleOpenFixedBox = () => {
  if (computedRules.value.length > 0) {
    const rect = textAreaWrapper.value.getBoundingClientRect();
    positionNotifyBox.value = {
      bottom: `${height.value - rect.top + 5}px`,
      left: `${
        rect.left +
        textAreaWrapper.value.clientWidth -
        msgBox.value?.clientWidth
      }px`,
    };
    isOpenMsgBox.value = true;
  }
};
const handleCloseFixedBox = () => {
  isOpenMsgBox.value = false;
};
const handleBlur = () => {
  if (props.rules.required && !valueInput.value) {
    invalid.value = true;
  } else {
    invalid.value = false;
  }
  if (
    props.rules.maxLength &&
    valueInput.value?.length > props.rules.maxLength
  ) {
    overMaxLength.value = true;
  } else {
    overMaxLength.value = false;
  }
};
const handleWheelDetail = (event) => {
  isStopPropagationWheelStatus.value = event;
};
onMounted(() => {
  textBoxHeight.value = textareaRef.value.scrollHeight;
});
</script>

<style lang="scss" scoped>
.custom-textarea {
  position: relative;
  :deep(.v-input__control) {
    box-shadow: none !important;
  }
  &::before {
    content: "";
    opacity: 0;
    z-index: 2;
    position: absolute;
    right: 8px;
    top: -10px;
    background: var(--bg-inverse-bg-darker, #525457);
    width: 10px;
    height: 10px;
    transform: rotate(45deg);
    transition: 0.3s;
  }
}
:deep().v-field {
  height: auto !important;
  border-radius: 0px;
  box-shadow: none !important;
}
:deep().v-field__field {
  align-items: center;
  border: none;
}
:deep().v-field__input {
  // height: 120px;
  padding: 13px 0px !important;
  // border-radius: 8px;
  font-family: "Noto Sans KR", sans-serif !important;
  font-size: 13px;
  color: #3a3b3d;
}

.v-field-label {
  font-size: 13px;
  margin-bottom: 8px;
}

.v-field-label--floating {
  margin-bottom: 4px;
}

.input-form:deep().v-field-label {
  font-size: 13px;
  color: #bdc1c7;
}
.input-form:deep().v-field-label--floating {
  font-size: 9px !important;
}

.input-form:deep().v-field--disabled {
  opacity: 1 !important;
  background-color: #f0f2f5 !important;
}

.input-form:deep(.v-field--disabled .v-field-label--floating) {
  color: #6b6d70 !important;
}

.required {
  border-left: 2px solid #d9325a !important;
}
:deep().v-input__details {
  height: 0px;
  min-height: 0px;
  min-width: 80px;
  position: absolute;
  bottom: 127px;
  right: 0px;
  opacity: 0;
  background: var(--bg-inverse-bg-darker, #525457);
  box-shadow: 0px 2px 20px 0px #0000001a;
  transition: 0.3s;
}

:deep(.v-input__details .v-messages) {
  color: white !important;
}

:deep(.v-input__details .v-messages) {
  color: white !important;
}

:deep(.v-input__details .v-counter) {
  color: white !important;
  padding-left: 8px;
}

:deep().v-field--error:not(.v-field--disabled) .v-label.v-field-label {
  color: #bdc1c7;
}
:deep().v-field__input::-webkit-scrollbar {
  width: 4px;
}
:deep().v-field__input::-webkit-scrollbar-track {
  background: #f1f1f1;
}
:deep().v-field__input::-webkit-scrollbar-thumb {
  background: #888;
}
:deep().v-field__input::-webkit-scrollbar-thumb:hover {
  background: #555;
}
.message-box-wrapper {
  display: flex;
  height: 0px;
  opacity: 0;
  position: absolute;
  z-index: 10;
  background: var(--bg-inverse-bg-darker, #525457);
  box-shadow: 0px 2px 20px 0px #0000001a;
  transition:
    height 0.3s ease-in-out,
    opacity 0.3s ease-in-out;
  padding: 4px 15px;
  border-radius: 4px;
  .count {
    color: #fff;
    font-size: 12px;
    line-height: 21.5px;
  }
  &::after {
    content: "";
    opacity: 0;
    z-index: 2;
    position: absolute;
    right: 8px;
    bottom: -3px;
    background: var(--bg-inverse-bg-darker, #525457);
    width: 10px;
    height: 10px;
    transform: rotate(45deg);
    transition: opacity 0.3s ease-in-out;
  }
}
.show-box {
  height: 31px;
  opacity: 1;
  &::after {
    opacity: 1;
  }
}
.invalid-err {
  border: 1px solid #d9325a !important;
}

.textarea-component {
  &:disabled {
    background-color: #f0f2f5;
  }
}
</style>
