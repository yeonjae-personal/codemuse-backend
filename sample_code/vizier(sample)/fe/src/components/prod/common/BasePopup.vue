<script setup lang="ts">
import {
  ButtonColorType,
  ButtonSizeType,
  DialogIconType,
  DialogSizeType,
} from "@/enums";
import InfoIcon from "@/components/prod/icons/InfoIcon.vue";
import WarningIcon from "@/components/prod/icons/WarningIcon.vue";
import CloseIcon from "@/components/prod/icons/CloseIcon.vue";
import BaseButton from "@/components/prod/common/BaseButton.vue";
import { WIDTH_BUTTON } from "@/constants/index";

const emit = defineEmits(["onClose", "onSubmit", "update:modelValue"]);
const sizeAmount = {
  [DialogSizeType.Small]: 400,
  [DialogSizeType.ESmall]: 560,
  [DialogSizeType.XMedium]: 640,
  [DialogSizeType.Medium]: 800,
  [DialogSizeType.Large]: 1000,
  [DialogSizeType.ELarge]: 1200,
};
const props = defineProps({
  title: {
    type: String,
    default: "",
  },
  isCustomPosition: {
    type: Boolean,
    default: false,
  },
  size: {
    type: String as PropType<DialogSizeType>,
    default: DialogSizeType.Small,
  },
  modelValue: {
    type: Boolean,
    default: false,
  },
  content: {
    type: String,
    default: "",
  },
  icon: {
    type: String as PropType<DialogIconType>,
    default: "",
    required: false,
  },
  submitButtonText: {
    type: String,
    default: "",
  },
  cancelButtonText: {
    type: String,
    default: "",
  },
  persistent: {
    type: Boolean,
    default: false,
  },
  isHideFooter: {
    type: Boolean,
    default: false,
  },
  classCustom: {
    type: String,
    default: null,
  },
});

const isOpen = computed({
  // getter
  get() {
    return props.modelValue;
  },
  // setter
  set(newValue) {
    emit("update:modelValue", newValue);
  },
});

const iconData = {
  [DialogIconType.Info]: InfoIcon,
  [DialogIconType.Warning]: WarningIcon,
};

const closeDialog = () => {
  emit("onClose");
  isOpen.value = false;
};

const submitDialog = () => {
  emit("onSubmit");
  //will close popup outside
};
</script>

<template>
  <v-dialog
    v-model="isOpen"
    :persistent="persistent"
    :max-width="sizeAmount[size]"
    :class="`${isCustomPosition && classCustom}`"
    @keydown.esc="closeDialog"
  >
    <template #default>
      <v-card class="!rounded-[12px] overflow-hidden">
        <slot name="header">
          <v-card-title>
            <div class="flex justify-between">
              <p>
                {{ title }}
              </p>
              <close-icon
                class="cursor-pointer mt-[-20px] mr-[-20px]"
                @click="closeDialog"
              />
            </div>
          </v-card-title>
        </slot>
        <slot name="icon">
          <div class="flex justify-center">
            <component :is="iconData[icon]" />
          </div>
        </slot>
        <slot name="body">
          <v-card-text class="!px-6 !pb-2 !pt-4">
            <div class="text-center font-medium text-lg text-text-base">
              {{ content }}
            </div>
          </v-card-text>
        </slot>

        <div v-if="!isHideFooter" class="p-6">
          <slot name="footer">
            <div class="grid grid-cols-2 w-full gap-3">
              <BaseButton :width="WIDTH_BUTTON.AUTO" @click="submitDialog()">
                {{ submitButtonText || $t("common.btn_ok") }}
              </BaseButton>
              <BaseButton
                :width="WIDTH_BUTTON.AUTO"
                :color="ButtonColorType.Gray"
                @click="closeDialog()"
              >
                {{ cancelButtonText || $t("common.btn_cancel") }}
              </BaseButton>
            </div>
          </slot>
        </div>
      </v-card>
    </template>
  </v-dialog>
</template>

<style lang="scss" scoped>
:deep(.v-card-text) {
  font-family: "Noto Sans KR", sans-serif !important;
}

:deep(.v-card-title) {
  padding: 24px 24px 0 24px;
}

.search-detail-offer:deep(.v-overlay__content) {
  top: 172px;
  left: 975px;
}
.search-detail-relation:deep(.v-overlay__content) {
  top: 165px;
  left: 915px;
}
.search-detail-component:deep(.v-overlay__content) {
  top: 167px;
  left: calc(50% - 225px);
}
.search-detail-resource:deep(.v-overlay__content) {
  top: 167px;
  left: calc(50% - 270px);
}
</style>
