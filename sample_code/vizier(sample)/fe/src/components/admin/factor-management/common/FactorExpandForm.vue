<template>
  <div
    class="container"
    :class="[
      isActive
        ? `!border-[${BORDER_CONFIG.ACTIVE}] !border-[2px]`
        : '!border-[#E6E9ED] border-[1px]',
      {
        'gap-2': expand,
        'zoom-animation': !expand,
        '!bg-[#E9EBF0] ': disabled,
      },
    ]"
    @click="handleClick"
  >
    <div class="flex">
      <div class="flex-grow-1">
        <p
          class="h-[20px] w-full m-[0px] text-[13px] text-[#3A3B3D] text-ellipsis"
          :class="[
            {
              'opacity-[32%]': disabled,
            },
          ]"
        >
          {{ formData.factorValueName }}
        </p>
      </div>

      <div
        v-if="editable && !disabled && actions.length"
        class="relative leading-none"
      >
        <base-popover
          :options="actions"
          custom-location="bottom-left"
          class="flex-initial"
          @open-options="emit('open-options')"
        >
          <template #activator>
            <DotsVerticalIcon />
          </template>
        </base-popover>
      </div>
    </div>
    <div v-if="expand" class="overflow-hidden" :class="[{ expanded: expand }]">
      <v-form
        v-if="id"
        v-model="form"
        @click="
          ($event) => {
            $event.stopPropagation();
          }
        "
      >
        <DetailPane>
          <DetailPaneRow :label="$t(`product_platform.displayName`)">
            <template #value="{ klass }">
              <div v-if="!isEdit" :class="klass">
                {{ formData.factorValueName }}
              </div>
              <div v-else :class="klass">
                <BaseInputText
                  v-model="formData.factorValueName"
                  styles="input-edit custom"
                  required
                  :maxlength="500"
                  :counter="500"
                />
              </div>
            </template>
          </DetailPaneRow>
          <DetailPaneRow :label="$t(`product_platform.value`)">
            <template #value="{ klass }">
              <div v-if="!isEdit" :class="klass">
                {{ formData.value }}
              </div>
              <div v-else :class="klass">
                <BaseInputText
                  v-model="formData.value"
                  styles="input-edit custom"
                  :maxlength="10"
                  required
                  :rules="
                    useInputValidation({
                      maxLength: 10,
                      onlyNumbers: true,
                    })
                  "
                  :counter="10"
                  @keypress="onlyNumber"
                  @input="changeValueNumber($event, formData)"
                />
              </div>
            </template>
          </DetailPaneRow>
          <DetailPaneRow
            :label="$t(`product_platform.ID`)"
            :value="
              formData?.isAdded || formData?.isNew
                ? $t(`product_platform.auto_generation`)
                : formData.factorValueCode
            "
          />
          <DetailPaneRow :label="$t(`product_platform.useYn`)">
            <template #value="{ klass }">
              <div :class="klass">
                <v-switch
                  v-model="formData.useYn"
                  class="switch-custom"
                  hide-details
                  :color="isEdit ? '#D9325A' : '#FDCED5'"
                  inset
                  width="36"
                  density="compact"
                  :readonly="!isEdit"
                  :false-value="RequiredYn.No"
                  :true-value="RequiredYn.Yes"
                ></v-switch>
              </div>
            </template>
          </DetailPaneRow>
        </DetailPane>
      </v-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import DetailPane from "@/components/prod/layout/DetailPane.vue";
import { useInputValidation } from "@/composables/useInputValidation";
import { BORDER_CONFIG } from "@/constants/index";
import { RequiredYn } from "@/enums";

const emit = defineEmits(["update:formData", "onClick", "open-options"]);
const props = defineProps({
  id: {
    type: String,
    required: true,
  },
  title: {
    type: String,
    default: "",
  },
  formData: {
    type: Object,
    required: true,
  },
  isEdit: {
    type: Boolean,
    default: false,
  },
  expand: {
    type: Boolean,
    default: false,
  },
  disabled: {
    type: Boolean,
    default: false,
  },
  editable: {
    type: Boolean,
    default: false,
  },
  isActive: {
    type: Boolean,
    default: false,
  },
  actions: {
    type: Array as PropType<any[]>,
    default: () => [],
  },
});
const form = ref(false);
const formData = ref(props.formData);
const handleClick = () => {
  emit("onClick", !props.expand);
};
const changeValueNumber = (event, newVal) => {
  const vowelsRegex = /^\d+$/;
  let valueOld = event.target.value;
  newVal.value = valueOld
    .split("")
    .filter((val) => vowelsRegex.test(val))
    .join()
    .replaceAll(",", "");
};
const onlyNumber = ($event) => {
  let keyCode = $event.keyCode ? $event.keyCode : $event.which;
  if (keyCode < 48 || keyCode > 57) {
    $event.preventDefault();
  }
};

watch(
  () => formData.value,
  (val) => {
    emit("update:formData", val);
  },
  { deep: true }
);
</script>

<style lang="scss" scoped>
.container {
  display: flex;
  flex-direction: column;
  width: 100%;
  position: relative;
  padding: 10px 15px;
  border: 1px #e6e9ed solid;
  border-radius: 20px;
  cursor: pointer;
  overflow: hidden;
}
.switch-custom :deep(.v-switch__thumb) {
  height: 16px !important;
  width: 15px !important;
}
.switch-custom :deep(.v-switch__track) {
  height: 20px !important;
  width: 38px !important;
  min-width: 38px !important;
  opacity: 1;
}
.switch-custom :deep(.v-selection-control) {
  min-height: 20px !important;
}
.input-edit.custom :deep(.v-input__control) {
  height: 30px !important;
}
// .detail {
//   height: 0px;
//   transition: all 0.2s ease-in-out;
// }
.expanded {
  height: 176px;
}
</style>
