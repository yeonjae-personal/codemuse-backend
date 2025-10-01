<template>
  <div
    :class="[
      {
        'opacity-30': disable,
        'border-l-primary border-l-[2px] rounded-[8px]': required,
        'drop-invalid': isInvalid,
      },
      'relative wrapper',
    ]"
  >
    <input
      ref="input"
      type="text"
      class="absolute z-[-1] w-full"
      @blur="handleBlur"
    />
    <div
      v-if="dropValue.obName"
      class="flex justify-between align-center border border-[#DCE0E5] rounded-[6px] min-h-[32px] py-[6px] px-[12px]"
      :class="[checkDisabled ? 'bg-[#f0f2f5] opacity-40' : 'bg-white']"
      @drop="drop($event, dropValue.obName)"
      @dragover="!checkDisabled ? allowDrop($event) : null"
      @click.stop="emit('onClick')"
    >
      <div class="w-[165px] text-truncate !no-underline cursor-pointer">
        <CustomTooltip :content="dropValue.obName" />
      </div>
      <span class="cursor-pointer" @click.stop="handleRemove">
        <TrashIcon color="#C7291D" />
      </span>
    </div>
    <ItemDrop
      v-else
      class-name="h-[32px] !text-[11px]"
      :is-disabled="checkDisabled"
      @drop="drop($event, null)"
      @dragover="allowDrop($event)"
      @click.stop="handleClick"
    />
    <div v-if="required" class="error-message" :class="[{ show: isInvalid }]">
      <div class="error-message-text">
        {{ $t("product_platform.validate.requiredFieldInput") }}
      </div>
    </div>
  </div>
  <base-popup
    v-model="openPopup"
    :icon="DialogIconType.Info"
    :submit-button-text="$t('product_platform.btn_yes')"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="$t('product_platform.drop_change_item')"
    @on-close="
      () => {
        openPopup = false;
      }
    "
    @on-submit="handleSubmit"
  />
</template>

<script setup lang="ts">
import { DialogIconType } from "@/enums";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import { useDragStore } from "@/store";

const emit = defineEmits<{
  (e: "update:modelValue", event: any): void;
  (e: "drop", event: DragEvent): void;
  (e: "dragover", event: DragEvent): void;
  (e: "remove"): void;
  (e: "onClick"): void;
  (e: "checkValid", event: Boolean): void;
}>();
const { dragOfferType, isDragging, categoryDrag } = storeToRefs(useDragStore());
const input = ref<any>(null);
const openPopup = ref(false);
const props = defineProps({
  data: {
    type: Object,
    default: () => {},
  },
  disable: {
    type: Boolean,
    default: false,
  },
  required: {
    type: Boolean,
    default: false,
  },
});
const dropData = ref<any>(null);
const isInvalid = ref(false);

const dropValue = computed({
  get() {
    return props.data;
  },
  set(newVal) {
    emit("update:modelValue", newVal);
  },
});

const handleBlur = () => {
  if (!props.required) return;
  isInvalid.value = props.required && !dropValue.value.attrVal;
  emit("checkValid", isInvalid.value);
};

const handleRemove = () => {
  dropValue.value.attrVal = "";
  dropValue.value.obName = "";
  if (props.required) {
    isInvalid.value = true;
    emit("checkValid", isInvalid.value);
  }
};

const drop = (event: any, currentVal: any) => {
  event.preventDefault();
  dropData.value = event.dataTransfer.getData("item")
    ? JSON.parse(event.dataTransfer.getData("item"))
    : null;

  if (currentVal && currentVal !== dropData.value?.itemName) {
    openPopup.value = true;
    return;
  }
  changeValue();
};

const handleSubmit = () => {
  changeValue();
  openPopup.value = false;
};
const changeValue = () => {
  if (dropData.value) {
    dropValue.value.attrVal = dropData.value?.itemDescription;
    dropValue.value.obName = dropData.value?.itemName;
    if (props.required) {
      isInvalid.value = false;
      emit("checkValid", isInvalid.value);
    }
  }
};
const allowDrop = (event) => {
  event.preventDefault();
  if (dragOfferType.value !== COLUMN_FIELD_TYPE.OB && !isDragging.value) {
    return true;
  }
};

const handleClick = () => {
  if (input.value) {
    input.value.focus();
  }
  // categoryDrag.value = props.data?.commGroupCode;
  emit("onClick");
};

const checkDisabled = computed(() => {
  if (categoryDrag.value) {
    return categoryDrag.value !== props.data?.commGroupCode;
  }
  return false;
});
</script>

<style lang="scss" scoped>
.wrapper {
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
.drop-invalid {
  &:hover {
    &::before {
      opacity: 1;
      width: 10px;
      height: 10px;
    }
  }
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
