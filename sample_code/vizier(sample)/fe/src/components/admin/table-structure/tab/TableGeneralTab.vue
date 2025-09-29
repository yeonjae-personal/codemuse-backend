<template>
  <div class="flex flex-col">
    <div
      class="bg-lighter p-3 text-text-lighter font-medium font-size-base rounded-[12px] flex flex-col gap-2"
    >
      <div class="flex gap-2 !min-h-[32px] items-center">
        <div class="w-[45%] !text-[13px]">
          {{ $t(`product_platform.code`) }}
        </div>
        <div class="w-[55%] text-text-base font-normal">
          {{ localGeneral?.tableTypeCode }}
        </div>
      </div>
      <div class="flex gap-2 !min-h-[32px] items-center">
        <div class="w-[45%] !text-[13px]">
          {{ $t(`product_platform.name`) }}
        </div>
        <div v-if="!isEditTableType" class="w-[55%] text-text-base font-normal">
          {{ localGeneral?.tableTypeName }}
        </div>
        <div v-else class="w-[55%] text-text-base font-normal">
          <BaseInputText
            v-model="localGeneral.tableTypeName"
            styles="input-edit custom"
            required
            @enter="handleSave"
          />
        </div>
      </div>
      <div
        v-if="localGeneral?.useYn"
        class="flex gap-2 !min-h-[32px] items-center"
      >
        <div class="w-[45%] !text-[13px]">
          {{ $t(`product_platform.useYn`) }}
        </div>
        <div class="w-[55%] text-text-base font-normal">
          <v-switch
            v-model="localGeneral.useYn"
            class="custom-switch"
            hide-details
            :color="isEditTableType ? '#D9325A' : '#FDCED5'"
            inset
            width="36"
            density="compact"
            :readonly="!isEditTableType"
            :false-value="RequiredYn.No"
            :true-value="RequiredYn.Yes"
          ></v-switch>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { RequiredYn } from "@/enums";
import useTableStructureStore from "@/store/admin/tableStructure.store";

const { isEditTableType } = storeToRefs(useTableStructureStore());
const handleSaveGeneralTab = inject<any>("handleSaveGeneralTab");

const props = defineProps({
  modelValue: {
    type: Object,
    default: () => {},
  },
});

const emit = defineEmits(["update:modelValue"]);

const localGeneral = computed({
  get() {
    return props.modelValue;
  },
  set(newVal) {
    emit("update:modelValue", newVal);
  },
});

const handleSave = () => {
  handleSaveGeneralTab();
};
</script>
<style scoped lang="scss">
/** custom switch button **/
:deep(.v-switch--inset .v-switch__thumb) {
  height: 16px;
  width: 16px;
  left: -1px !important;
}

:deep(.v-switch--inset .v-switch__track) {
  height: 20px;
  min-width: 36px;
}
:deep(.v-switch__track) {
  opacity: 1;
  background-color: rgb(220 224 228);
}
</style>
