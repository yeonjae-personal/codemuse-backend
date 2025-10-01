<template>
  <div class="flex flex-column w-full">
    <v-range-slider
      v-model="rangeValue"
      :color="color"
      :density="density"
      :direction="direction"
      :step="step"
      strict
      :max="max"
      :min="min"
      :max-width="maxWidth"
      :min-width="minWidth"
      :rules="rules"
      :thumb-color="thumbColor"
      :thumb-size="thumbSize"
      :tick-size="tickSize"
      :track-color="trackColor"
      :track-size="trackSize"
      :hide-details="hideDetails"
      @update:model-value="changeValueSlide"
    >
    </v-range-slider>
    <div v-if="showMinMax" class="flex justify-between">
      <div class="min-max">{{ formatNumberLocaleString(min) }}</div>
      <div class="min-max">{{ formatNumberLocaleString(max) }}</div>
    </div>
    <div class="flex justify-between relative top-[-83px] field-input-number">
      <div class="field-min" :class="focusType === 'min' && 'z-[99999]'">
        <BaseInputText
          v-model="minField"
          styles="!min-w-[30px]"
          :style="{
            width: `${widthMinValue}px`,
          }"
          hide-details
          :maxlength="6"
          @keypress="onlyNumber($event, Number(item.attrMaxLength))"
          @keydown.enter="handleEnter($event.target.value, 'min')"
          @input="changeValueNumber($event, 'min')"
          @blur="onBlur('min')"
          @click="handleFocusField('min')"
        />
      </div>
      <div class="field-max" :class="focusType === 'max' && 'z-[99999]'">
        <BaseInputText
          v-model="maxField"
          styles="!min-w-[30px]"
          :style="{
            width: `${widthMaxValue}px`,
          }"
          :maxlength="6"
          hide-details
          @keypress="onlyNumber($event, Number(item.attrMaxLength))"
          @keydown.enter="handleEnter($event.target.value, 'max')"
          @input="changeValueNumber($event, 'max')"
          @blur="onBlur('max')"
          @click="handleFocusField('max')"
        />
      </div>
    </div>
  </div>
</template>

<!-- eslint-disable vue/no-dupe-keys -->
<script setup lang="ts">
import { useSnackbarStore } from "@/store";
import {
  checkNumberIsInteger,
  formatDataTypeDecimal,
} from "@/utils/extend-utils";
import { formatNumberLocaleString } from "@/utils/format-data";
import { useI18n } from "vue-i18n";

const props = defineProps({
  modelValue: {
    type: Array as PropType<number[]>,
    default: () => [0, 10],
  },
  minValue: {
    type: String,
    default: "",
  },
  maxValue: {
    type: String,
    default: "",
  },
  color: {
    type: String,
    default: "",
  },
  density: {
    type: String,
    default: "default",
  },
  direction: {
    type: String,
    default: "horizontal",
  },
  step: {
    type: String,
    default: "100",
  },
  maxWidth: {
    type: String,
    default: "",
  },
  minWidth: {
    type: String,
    default: "",
  },
  max: {
    type: Number,
    default: 100000,
  },
  min: {
    type: Number,
    default: 0,
  },
  rules: {
    type: Array,
    default: () => [],
  },
  thumbColor: {
    type: String,
    default: "",
  },
  thumbLabel: {
    type: [Boolean, String],
    default: "always",
  },
  thumbSize: {
    type: [String, Number],
    default: 24,
  },
  tickSize: {
    type: [String, Number],
    default: 4,
  },
  trackColor: {
    type: String,
    default: "#F0F2F5",
  },
  trackSize: {
    type: [String, Number],
    default: 8,
  },
  hideDetails: {
    type: Boolean,
    default: true,
  },
  showMinMax: {
    type: Boolean,
    default: true,
  },
  item: {
    type: Object,
    default: null,
  },
});

const minValue = ref(0);
const maxValue = ref(0);
const minField = ref(0);
const maxField = ref(0);
const focusType = ref("");
const temp1 = ref(0);
const type1 = ref(false);
const temp2 = ref(0);
const type2 = ref(false);

const useSnackbar = useSnackbarStore();
const { t } = useI18n();

const emit = defineEmits([
  "update:modelValue",
  "update:minValue",
  "update:maxValue",
]);

const rangeValue = computed({
  get() {
    return props.modelValue || [0, 0];
  },
  set(newValue) {
    minValue.value = (newValue[0] as number) || 0;
    maxValue.value = (newValue[1] as number) || 0;
    emit("update:modelValue", !newValue[0] && !newValue[1] ? [0, 0] : newValue);
    emit("update:minValue", `${newValue[0] || 0}`);
    emit("update:maxValue", `${newValue[1] || 0}`);
  },
});

const minThumbPercent = computed(() => {
  return 100 - (minValue.value / props.max) * 100 + "%";
});
const widthMinValue = computed(() => {
  if (minField.value) {
    return Number(String(minField.value).length) * 10 + 10;
  }
  return 20;
});
const widthMaxValue = computed(() => {
  if (maxField.value) {
    return Number(String(maxField.value).length) * 10 + 10;
  }
  return 20;
});
const maxThumbPercent = computed(() => {
  return 100 - (maxValue.value / props.max) * 100 + "%";
});
const minThumbPercent1 = computed(() => {
  return 100 - ((rangeValue.value[0] as number) / props.max) * 100 + "%";
});
const maxThumbPercent1 = computed(() => {
  return 100 - ((rangeValue.value[1] as number) / props.max) * 100 + "%";
});

const onlyNumber = ($event, maxLength) => {
  let keyCode = $event.keyCode ? $event.keyCode : $event.which;

  if (maxLength && !checkNumberIsInteger(maxLength)) {
    if (keyCode !== 46 && (keyCode < 48 || keyCode > 57)) {
      $event.preventDefault();
    }
  } else {
    if (keyCode < 48 || keyCode > 57) {
      $event.preventDefault();
    }
  }
};
const handleEnter = (value, type) => {
  let arr = value.toString().split("");

  if (arr[arr.length - 1] === ".") {
    value = arr
      .filter((xxx) => xxx !== ".")
      .join()
      .replaceAll(",", "");
  }
  if (type === "min") {
    minValue.value = (+value > 0 && (value as number)) || 0;
    minField.value = (+value > 0 && (value as number)) || 0;
    temp1.value = value;
    type1.value = true;
    emit("update:minValue", value);
  } else {
    if (+value < +minValue.value) {
      useSnackbar.showSnackbar(t("product_platform.maxBiggerMin"), "error");
      maxField.value = +props.max;
      maxValue.value = +props.max;
    } else {
      maxField.value = (+value > 0 && (value as number)) || 0;
      maxValue.value = (+value > 0 && (value as number)) || 0;
      temp2.value = value;
      type2.value = true;
    }
    emit("update:maxValue", maxValue.value);
  }
  emit("update:modelValue", [minField.value, maxField.value]);
};

const onBlur = (type) => {
  if (type === "min") {
    handleEnter(minField.value || 0, "min");
  } else {
    handleEnter(maxField.value || 0, "max");
  }
};
const handleFocusField = (type) => {
  focusType.value = type;
};

const changeValueNumber = (event, type) => {
  const vowelsRegex = /[ㅏ-ㅣ]/;
  const consonantsRegex = /[ㄱ-ㅎ]/;
  const koreanRegex = /[가-힣]/;
  let valueOld = event.target.value;

  let isDecimal = !checkNumberIsInteger(props.item?.attrMaxLength);

  const check =
    vowelsRegex.test(valueOld) ||
    consonantsRegex.test(valueOld) ||
    koreanRegex.test(valueOld);

  if (type === "min") {
    minField.value = formatDataTypeDecimal(
      valueOld,
      isDecimal,
      props.item?.attrMaxLength
    );
  } else {
    maxField.value = formatDataTypeDecimal(
      valueOld,
      isDecimal,
      props.item?.attrMaxLength
    );
  }

  if (!check) {
    if (+valueOld > props.max) {
      useSnackbar.showSnackbar(
        t("product_platform.maxValueOneHundredThou"),
        "error"
      );
      if (type === "min") {
        minField.value = 0;
      } else {
        maxField.value = +props.max;
      }
    }

    if (type === "min") {
      if (+valueOld > +maxField.value) {
        minField.value = 0;
        useSnackbar.showSnackbar(t("product_platform.minSmallerMax"), "error");
      }
    }
  }
};

const changeValueSlide = (event) => {
  let isChangeMin =
    event[0] !==
    Math.round(Number(temp1.value) / Number(props.step)) * Number(props.step);
  let isChangeMax =
    event[1] !==
    Math.round(Number(temp2.value) / Number(props.step)) * Number(props.step);

  if (isChangeMin) {
    type1.value = false;
  }

  if (isChangeMax) {
    type2.value = false;
  }
};

watch(
  () => minValue.value,
  (newValue: any) => {
    minField.value = newValue || rangeValue.value[0];
  },
  {
    immediate: true,
  }
);

watch(
  () => maxValue.value,
  (newValue: any) => {
    maxField.value = newValue || rangeValue.value[1];
  },
  {
    immediate: true,
  }
);
watch(
  () => rangeValue.value,
  (newValue: any) => {
    if (newValue[0] === 0 && newValue[1] === 0) {
      minField.value = 0;
      maxField.value = 0;
    } else {
      minField.value = type1.value ? temp1.value : (newValue[0] as number) || 0;
      maxField.value = type2.value ? temp2.value : (newValue[1] as number) || 0;
    }
  },
  {
    deep: true,
  }
);

onMounted(() => {
  const thumbs = document.getElementsByClassName("v-slider-thumb__surface");
  if (thumbs.length) {
    for (let i = 0; i < thumbs.length; i++) {
      thumbs[i as number].classList.add("bg-linear-1");
    }
  }

  if (!Number.isInteger(Number(rangeValue.value[0]) / Number(props.step))) {
    type1.value = true;
    temp1.value = Number(rangeValue.value[0]);
  }
  if (!Number.isInteger(Number(rangeValue.value[1]) / Number(props.step))) {
    type2.value = true;
    temp2.value = Number(rangeValue.value[1]);
  }
});
</script>

<style lang="scss" scoped>
:deep() .v-slider-thumb__surface {
  border: 2px #fff solid;
}
:deep() .v-slider-thumb__label {
  background: #fff;
  color: #3a3b3d;
  border: 1px #dce0e5 solid;
  border-radius: 8px;
  font-size: 11px;
  font-weight: 500;
}
:deep() .v-slider-track__background--opacity {
  opacity: 1;
  box-shadow: 0px 1px 6px 0px rgba(0, 0, 0, 0.16) inset;
}
:deep() .v-slider.v-input--horizontal .v-slider-thumb__label {
  bottom: 5px;
}
:deep() .v-slider-track__fill {
  background: linear-gradient(124.94deg, #6bdada 0%, #1e4984 100%);
}

:deep() .bg-linear-1 {
  background: linear-gradient(
    124.94deg,
    #6bdada v-bind(maxThumbPercent),
    #1e4984 v-bind(minThumbPercent)
  );
}
:deep() .bg-linear-2 {
  background: linear-gradient(
    124.94deg,
    #6bdada v-bind(maxThumbPercent),
    #1e4984 v-bind(minThumbPercent)
  );
}
:deep() .field-min {
  position: absolute;
  right: calc(v-bind(minThumbPercent1) - 30px);
}
:deep() .field-max {
  position: absolute;
  right: calc(v-bind(maxThumbPercent1) - 30px);
}
:deep() .v-slider-thumb__surface::before {
  background: unset;
}
:deep() .v-slider-thumb__ripple {
  height: unset;
  width: unset;
}

:deep(.field-input-number .v-field) {
  height: 25px !important;
}
:deep(.field-input-number .v-field__field) {
  height: 25px !important;
}
:deep(.field-input-number .v-input__control) {
  max-height: 25px !important;
}
:deep(.field-input-number .v-field__input) {
  padding: 0px 3px;
  min-height: unset;
  text-align: center;
}
:deep(.field-input-number .v-field--appended) {
  padding-inline-end: unset;
}

.min-max {
  background: #f0f2f5;
  padding: 4px 8px;
  border-radius: 999px;
  color: #6b6d70;
  font-size: 11px;
  font-weight: 400;
}
</style>
