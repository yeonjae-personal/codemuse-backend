<template>
  <DetailPane>
    <DetailPaneRow
      :label="$t(`product_platform.code`)"
      :value="localGeneral?.factorTypeCode"
    />
    <DetailPaneRow :label="$t(`product_platform.name`)">
      <template #value="{ klass }">
        <div v-if="!isEditFactorTypeDetail" :class="klass">
          {{ localGeneral?.factorTypeName }}
        </div>
        <div v-else :class="klass">
          <BaseInputText
            v-model="localGeneral.factorTypeName"
            styles="input-edit custom"
            required
            @enter="handleSave"
          />
        </div>
      </template>
    </DetailPaneRow>
    <DetailPaneRow
      v-if="localGeneral?.useYn"
      :label="$t(`product_platform.useYn`)"
    >
      <template #value="{ klass }">
        <div :class="klass">
          <v-switch
            v-model="localGeneral.useYn"
            class="custom-switch"
            hide-details
            :color="isEditFactorTypeDetail ? '#D9325A' : '#FDCED5'"
            inset
            width="36"
            density="compact"
            :readonly="!isEditFactorTypeDetail"
            :false-value="RequiredYn.No"
            :true-value="RequiredYn.Yes"
          ></v-switch>
        </div>
      </template>
    </DetailPaneRow>
  </DetailPane>
</template>
<script setup lang="ts">
import useFactorStore from "@/store/admin/factor.store";
import { RequiredYn } from "@/enums";
import DetailPane from "@/components/prod/layout/DetailPane.vue";
import DetailPaneRow from "@/components/prod/layout/DetailPaneRow.vue";
const { isEditFactorTypeDetail } = storeToRefs(useFactorStore());
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
<style scoped>
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
