<!-- eslint-disable vue/no-mutating-props -->
<template>
  <div
    class="attribute-type-wrapper"
    :class="{
      'show-arrow': props.showArrow,
    }"
  >
    <div
      :class="[
        'attribute-type-header',
        { 'is-required': props.item.requiredYn === RequiredFieldType.Yes },
        {
          'selected-condition':
            props.item.id === selectedAttr?.attrId &&
            props.item.type === 'condition',
        },
        {
          'selected-action':
            props.item.id === selectedAttr?.attrId &&
            props.item.type === 'action',
        },
      ]"
    >
      <span class="header-title">
        {{ $t(`${item.labelId}`) }}
        <div class="attribute-info">
          <span class="attribute-code">{{ props.item.attrType }}</span>
          <div class="attribute-type">
            <span v-if="props.item.type === 'condition'" class="blue"></span>
            <span v-else class="white"></span>
            <span v-if="props.item.type === 'action'" class="red"></span>
            <span v-else class="white"></span>
          </div>
        </div>
      </span>
    </div>
    <div class="attribute-type-content">
      <div
        v-if="item.attrType === 'NF' || item.attrType === 'RF'"
        class="number-field"
      >
        <BaseInputText
          v-model="item.rangeStartVal"
          type="number"
          styles="!w-auto"
          :disabled="true"
          @blur="checkNumberValid"
        />
        <span>~</span>
        <BaseInputText
          v-model="item.rangeEndVal"
          type="number"
          styles="!w-auto"
          :disabled="true"
          @blur="checkNumberValid"
        />
      </div>
      <div v-if="item.attrType === 'DP'" class="datepicker-field">
        <BaseDateTimePicker
          v-model="item.rangeStartDtm"
          class="!w-auto"
          :disabled="true"
          enable-time-picker
        />
        <span>~</span>
        <BaseDateTimePicker
          v-model="item.rangeEndDtm"
          class="!w-auto"
          :disabled="true"
          enable-time-picker
        />
      </div>
      <CustomDataList
        v-if="['DL', 'DM'].includes(item.attrType)"
        :selected-options="props.item.multipleValues"
        :options="listOptions"
      />
    </div>
  </div>
</template>

<!-- eslint-disable vue/no-mutating-props -->
<script setup lang="ts">
import BaseDateTimePicker from "@/components/prod/common/BaseDateTimePicker.vue";
import { IAttributeItem } from "@/interfaces/admin/admin";
import customValidationStore from "@/store/admin/customValidation.store";
import useCmcdStore from "@/store/cmcd.store";
import CustomDataList from "./CustomDataList.vue";
import { RequiredFieldType } from "@/enums/customValidation";

interface Props {
  item: IAttributeItem;
  parentId: string;
  showArrow?: boolean;
}

defineEmits(["update:modelValue"]);

const { selectedAttr } = storeToRefs(customValidationStore());
const { search } = useCmcdStore();
const props = defineProps<Props>();

const listOptions = ref([]);

const checkNumberValid = () => {
  if (Number(props.item.numberFromField) >= Number(props.item.numberToField)) {
    props.item.numberToField = "";
    return;
  }
};
onMounted(() => {
  const getListOptions = async (code) => {
    const response = await search([code]);
    listOptions.value = response[code as string].map((item) => {
      return {
        name: item.cmcdDetlNm,
        value: item.cmcdDetlId,
        label: item.cmcdDetlNm,
      };
    });
  };
  if (["DM", "DL"].includes(props.item.attrType)) {
    getListOptions(props.item.code);
  }
});
</script>

<style lang="scss" scoped>
.attribute-type-wrapper {
  width: 360px;
  min-height: 88px;
  // background: linear-gradient(105.78deg,#effaff 26.93%,#def5ff 63.74%,#c3e8f7 85.24%,#bce4f5 91.25%);
  // box-shadow: 0px 1px 2px 1px #191c6312;
  border-radius: 8px;
  // padding: 4px;
  display: flex;
  flex-direction: column;
  row-gap: 8px;
  font-family: "Noto Sans KR";
  position: relative;
  border: 1px solid transparent;
  margin-bottom: 12px;
  &:last-child {
    margin-bottom: 0;
  }
  .attribute-type-header {
    height: 40px;
    border-radius: 6px;
    padding: 10px 16px;
    background: linear-gradient(
      105.78deg,
      #effaff 26.93%,
      #def5ff 63.74%,
      #c3e8f7 85.24%,
      #bce4f5 91.25%
    );
    box-shadow:
      6px 8px 10px 0px #0000000a,
      3px 3px 4px 0px #0000001f;
    display: flex;
    justify-content: space-between;
    align-items: center;
    position: relative;
    &:before {
      position: absolute;
      top: 0;
      left: 0;
      content: "";
      width: 100%;
      height: 100%;
      border-radius: 8px;
      border-left: 1px solid #b2ddff;
    }
    &.is-required {
      &::before {
        position: absolute;
        top: 0;
        left: 0;
        content: "";
        width: 10px;
        height: 100%;
        border-radius: 8px;
        border-left: 2px solid #e0332d;
      }
    }

    .header-title {
      font-size: 13px;
      font-weight: 500;
      color: #3a3b3d;
      display: flex;
      justify-content: space-between;
      width: 100%;
      .attribute-info {
        display: flex;
        align-items: center;
        .attribute-code {
          font-size: 13px;
          line-height: 19.5px;
          letter-spacing: 0.25px;
          margin-right: 14px;
          color: #6b6d70;
        }
        .attribute-type {
          display: flex;
          flex-direction: column;
          row-gap: 4px;
          .blue {
            width: 4px;
            height: 4px;
            border-radius: 50%;
            background: #4054b2;
          }
          .red {
            width: 4px;
            height: 4px;
            border-radius: 50%;
            background: #d9325a;
          }
          .white {
            width: 4px;
            height: 4px;
            border-radius: 50%;
            background: transparent;
          }
          .gray {
            width: 4px;
            height: 4px;
            border-radius: 50%;
            background: #fff;
          }
        }
      }
    }
  }
  .attribute-type-content {
    padding: 0 8px;

    .number-field {
      display: flex;
      justify-content: space-between;
      align-items: center;
      color: #6b6d70;
    }
    .datepicker-field {
      display: flex;
      justify-content: space-between;
      align-items: center;
      color: #6b6d70;
    }
  }
  &:hover {
    cursor: pointer;
  }
}

.show-arrow {
  &::before {
    display: block;
    content: "";
    width: 0;
    height: 0;
    border-top: 5px solid transparent;
    border-left: 10px solid #bdc1c7;
    border-bottom: 5px solid transparent;
    position: absolute;
    left: -10px;
    top: 18px;
  }
}

.disabled {
  pointer-events: none;
  cursor: not-allowed;
  opacity: 0.5;
}

:deep(.custom-base-multiselect) {
  max-height: 32px !important;
  height: 32px;
}

:deep(.multi-select .open) {
  position: absolute;
  z-index: 999;
  border: solid 1px #dce0e5;
  width: 334px;
  background: #fff;
  margin-top: -5px;
  border-bottom-right-radius: 10px;
  border-bottom-left-radius: 10px;
}
.selected-condition {
  border-color: #4054b2;
}

.selected-action {
  border-color: #d9325a;
}

:deep(.number-field .v-input) {
  width: auto;
}
:deep(.v-field__field) {
  height: 32px !important;
}
:deep(.v-field) {
  height: 32px !important;
}
:deep(.custom-text-field .v-input__control) {
  height: 32px !important;
}
</style>
