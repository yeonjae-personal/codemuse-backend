<template>
  <BasePopup
    v-model="isOpen"
    :title="'Domain Lookup'"
    :size="DialogSizeType.XMedium"
    :submit-button-text="t('product_platform.save')"
    :cancel-button-text="t('product_platform.cancel')"
  >
    <template #body>
      <DomainContent />
    </template>

    <template #footer>
      <div class="flex justify-end gap-3">
        <BaseButton @click="handleSave()"> Apply </BaseButton>
        <BaseButton :color="ButtonColorType.Gray" @click="closeDialog()">
          {{ t("product_platform.cancel") }}
        </BaseButton>
      </div>
    </template>
  </BasePopup>
</template>
<script setup lang="ts">
import { useI18n } from "vue-i18n";
import { ButtonColorType, DialogSizeType } from "@/enums";
import DomainContent from "@/pages/admin/subs/domain/DomainContent.vue";
const { t } = useI18n();
const emit = defineEmits(["update:modelValue", "applyDomainLookup"]);
import { useSnackbarStore, useDomainPopupStore } from "@/store";

const props = defineProps({
  open: {
    type: Boolean,
    default: true,
  },
  modelValue: {
    type: Boolean,
    default: false,
  },
  data: {
    type: Object,
    default: null,
  },
});
const domainLookupStore = useDomainPopupStore();
const useSnackbar = useSnackbarStore();
const isOpen = computed({
  get() {
    return props.modelValue;
  },
  set(newValue) {
    emit("update:modelValue", newValue);
  },
});
const selectedDomain = computed(() => domainLookupStore.getSelectedItem);

const closeDialog = () => {
  isOpen.value = false;
};
const handleSave = () => {
  if (!selectedDomain.value.domnNm) {
    useSnackbar.showSnackbar("Please select domain.", "error");
    return;
  }
  // applyDomainLookup
  emit("applyDomainLookup");
  closeDialog();
};
</script>

<style lang="scss" scoped>
.custom-button {
  width: 140px;
  height: 48px;
  color: #ba1642;
  background-color: #fff0f2;
}

.custom-button {
  width: 140px;
}

.custom-row {
  gap: 16px;
}
</style>
