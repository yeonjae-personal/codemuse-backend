<template>
  <div class="relative">
    <div class="custom-behind left"></div>
    <LocomotiveComponent
      :style="{ maxHeight: tableHeight }"
      :scroll-container-class="'!px-[0px]'"
      top-content-class="maxtrix-table-fixed-header"
      :scroll-content-class="[
        isCreate && !items?.length ? 'w-full h-full flex' : '',
      ]"
    >
      <template #top-content-fixed>
        <div
          class="header-layout h-[44px] px-6 flex items-center mb-2"
          :style="{ width: `${widthHeader}` }"
        >
          <div
            v-for="(header, index) in headers"
            :key="header.factorCode"
            class="header-item flex items-center justify-center gap-1 min-w-[182px] px-9 uppercase text-[13px] text-[#3a3b3d] font-[500] cursor-pointer bg-white"
            :class="[index === headers.length - 1 && 'item-sticky']"
          >
            <div
              :class="[!header.valueSort && 'l-icon']"
              @click="handleSort(header)"
            >
              <SortTableIcon
                :fill-up="header.valueSort === 'up' ? '#D9325A' : '#BDC1C7'"
                :fill-down="header.valueSort === 'down' ? '#D9325A' : '#BDC1C7'"
              />
            </div>
            <span
              class="max-w-[70px] text-ellipsis"
              :class="[
                isMultiEdit && index === headers.length - 1
                  ? '!text-[#D9325A]'
                  : '',
              ]"
            >
              <CustomTooltip :content="header.factorName" is-inline />
            </span>
            <template v-if="index === headers.length - 1">
              <MultiEditIcon
                v-if="isEdit"
                :fill="isMultiEdit ? '#D9325A' : '#6B6D70'"
                @click="handleClickMultiEdit"
              />
            </template>
            <template v-else>
              <div :class="[!header.isFilter && 'l-icon']">
                <v-menu
                  :close-on-content-click="false"
                  width="230px"
                  location="bottom center"
                >
                  <template #activator="{ props: activatorProps }">
                    <FilterTableIcon
                      v-bind="activatorProps"
                      :fill="header.isFilter ? '#D9325A' : '#BDC1C7'"
                    />
                  </template>

                  <v-list class="mt-2 ml-10">
                    <LocomotiveComponent
                      scroll-container-class="max-h-[240px] !px-0 ma-0"
                      scroll-content-class=""
                      :is-stop-propagation-wheel="true"
                      is-show-scrollbar
                    >
                      <v-list-item
                        v-for="(item, i) in header?.factorValues"
                        :key="i"
                        :value="i"
                        :class="item.inUse && 'bg-item-checked'"
                      >
                        <v-list-item-title
                          class="flex align-center !h-[40px]"
                          :class="item.inUse && 'item-label-check'"
                        >
                          <v-checkbox
                            v-model="item.inUse"
                            :readonly="isReadOnly"
                            :true-icon="TrueIcon"
                            :false-icon="FalseIcon"
                            density="compact"
                            class="custom-checkbox"
                            @change="onChangeFilter"
                          >
                            <template #label>
                              <span class="!w-[120px] text-ellipsis mt-1">
                                <CustomTooltip
                                  :content="item.factorValueName"
                                />
                              </span>
                            </template>
                          </v-checkbox>
                        </v-list-item-title>
                      </v-list-item>
                    </LocomotiveComponent>
                  </v-list>
                </v-menu>
              </div>
            </template>
          </div>
        </div>
      </template>
      <div
        v-if="isCreate && !items?.length"
        class="m-auto text-lighter text-[15px]"
      >
        {{ $t("product_platform.add_factor_to_build_matrix") }}
      </div>
      <div
        v-else
        class="bg-white pb-2 px-6 flex flex-col relative"
        :style="{ width: `${widthHeader}` }"
      >
        <div
          v-for="item in items"
          :key="item.rowId"
          class="w-full flex items-center item-row"
          :style="{ width: `${widthTableRow}` }"
          :class="[
            (item.rowId === trHover || item.rowId === trSelected) &&
              'item-actived',
          ]"
          @click="
            () => {
              trSelected = item.rowId;
            }
          "
        >
          <template
            v-for="(col, index) in item.measureDDtos"
            :key="`matrix-${index}`"
          >
            <div
              v-if="col.factorCode === 'VALUE'"
              class="flex items-center justify-center min-w-[182px] text-[13px] text-[#3a3b3d] font-[400] h-[52px] !bg-white"
              :class="[index === item.measureDDtos.length - 1 && 'item-sticky']"
            >
              <BaseInputText
                v-model="item.matrixNumValue"
                :readonly="!isEdit || isReadOnly"
                :class="[item.isChanged && 'value-changed']"
                :styles="[
                  'input-edit !max-w-[166px] h-[36px]',
                  {
                    '!border-[#D9325A]':
                      itemValueSelected ||
                      `${item.rowId}-${index}` === valueSelected,
                  },
                ]"
                hide-details
                :maxlength="getMaxLengthField(item, true)"
                @keypress="onlyNumber($event, maxlengthHardCode)"
                @input="changeValueNumber($event, item)"
                @focus="handleFocusInput(item, index)"
                @blur="handleBlurDecimal(item)"
              />
            </div>
            <div
              v-else
              class="flex items-center justify-center min-w-[182px] max-w-[182px] py-2"
              @mousedown="handleFocusCell(`${item.rowId}-${index}`)"
            >
              <div
                class="min-w-[166px] max-w-[166px] border-[1px] h-[36px] text-[13px] text-[#3a3b3d] font-[400] rounded-[999px] bg-white px-3 py-2 text-ellipsis"
                :class="[
                  `${item.rowId}-${index}` === itemHover ||
                  `${item.rowId}-${index}` === itemSelected
                    ? 'border-[#D9325A] cursor-pointer'
                    : 'border-[#e6e9ed] cursor-pointer',
                ]"
              >
                {{ col.factorValueName || "-" }}
              </div>
            </div>
          </template>
        </div>
        <div class="w-[19px] h-full bg-white absolute right-0 top-0 z-10"></div>
      </div>
    </LocomotiveComponent>
    <div class="custom-behind right"></div>
  </div>
</template>
<script setup lang="ts">
import TrueIcon from "@/components/prod/icons/TrueIcon.vue";
import FalseIcon from "@/components/prod/icons/FalseIcon.vue";
import {
  checkNumberIsInteger,
  formatDataTypeDecimal,
} from "@/utils/extend-utils";

const props = defineProps({
  headers: {
    type: Array<any>,
    default: [],
  },
  items: {
    type: Array<any>,
    default: [],
  },
  isEdit: {
    type: Boolean,
    default: false,
  },
  isCreate: {
    type: Boolean,
    default: false,
  },
  isReadOnly: {
    type: Boolean,
    default: false,
  },
  isMultiEdit: {
    type: Boolean,
    default: false,
  },
  tableHeight: {
    type: String,
    default: "",
  },
});

const itemHover = ref("");
const trHover = ref("");
const trSelected = ref(null);
const itemSelected = ref<any>(null);
const valueSelected = ref<any>(null);
const itemValueSelected = ref<any>(null);
const widthHeader = ref("100%");
const widthTableRow = ref("100%");
const maxlengthHardCode = ref("15.6");

const emit = defineEmits([
  "showMessageReadOnly",
  "triggerFilterAction",
  "triggerSortAction",
  "updateValueColumn",
  "updateSingleValue",
  "updateAllValue",
]);
const handleFocusInput = (item, index) => {
  if (props.isMultiEdit) {
    itemValueSelected.value = item.rowId;
  } else {
    itemHover.value = "";
    itemSelected.value = null;
    valueSelected.value = `${item.rowId}-${index}`;
  }
};

const handleFocusCell = (value) => {
  itemSelected.value = value;
  itemHover.value = "";
  valueSelected.value = null;
};
const onChangeFilter = () => {
  if (props.isReadOnly) {
    emit("showMessageReadOnly");
    return;
  }
  emit("triggerFilterAction");
};

const onlyNumber = ($event, maxLength = null) => {
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

const handleClickMultiEdit = () => {
  if (props.isReadOnly) {
    emit("showMessageReadOnly");
    return;
  }
  itemHover.value = "";
  trHover.value = "";
  trSelected.value = null;
  itemSelected.value = null;
  valueSelected.value = null;
  itemValueSelected.value = null;
  emit("updateValueColumn");
};

const changeValueNumber = (event, item) => {
  let valueOld = event.target.value;
  let isDecimal = !checkNumberIsInteger(maxlengthHardCode.value);
  item.matrixNumValue = formatDataTypeDecimal(
    valueOld,
    isDecimal,
    maxlengthHardCode.value
  );

  if (props.isMultiEdit) {
    emit("updateAllValue", item);
  } else {
    emit("updateSingleValue", item);
  }
};

const handleBlurDecimal = (item) => {
  let isDecimal = !checkNumberIsInteger(maxlengthHardCode.value);
  if (isDecimal) {
    let arr = item.matrixNumValue.split("");

    if (arr[arr.length - 1] === ".") {
      item.matrixNumValue = arr
        .filter((xxx) => xxx !== ".")
        .join()
        .replaceAll(",", "");
    }
  }
};

const handleSort = (header) => {
  if (props.isReadOnly) {
    emit("showMessageReadOnly");
    return;
  }
  emit("triggerSortAction", header.factorCode);
};

const getMaxLengthField = (item, getAllValue = false) => {
  let maxLength = maxlengthHardCode.value;
  if (maxLength) {
    if (checkNumberIsInteger(maxLength)) {
      return maxLength;
    }
    return getAllValue
      ? Number(maxLength) + 1
      : Number((maxLength + "").split(".")[0]) + 1;
  }
  return null;
};

const widthHeaderCalc = () => {
  const scrollTopFix = document.getElementsByClassName(
    "maxtrix-table-fixed-header"
  )[0];
  const targetWidth = props.headers.length * 182 + 40;
  if (scrollTopFix && scrollTopFix?.clientWidth < targetWidth) {
    widthHeader.value = `${targetWidth}px`;
  } else widthHeader.value = "100%";
};

const widthTableRowCalc = () => {
  const scrollTopFix = document.getElementsByClassName(
    "maxtrix-table-fixed-header"
  )[0];
  const targetWidth = props.headers.length * 182 - 2;
  if (scrollTopFix && scrollTopFix?.clientWidth < targetWidth) {
    widthTableRow.value = `${targetWidth}px`;
  } else widthTableRow.value = "100%";
};

onMounted(() => {
  widthHeaderCalc();
  widthTableRowCalc();
});

onUpdated(() => {
  widthHeaderCalc();
  widthTableRowCalc();
});

watch(
  () => props.isEdit,
  () => {
    // clear all value selected
    itemHover.value = "";
    trHover.value = "";
    trSelected.value = null;
    itemSelected.value = null;
    valueSelected.value = null;
    itemValueSelected.value = null;
  },
  { deep: true }
);
</script>

<style scoped lang="scss">
.header-layout {
  position: relative;
  z-index: 1;
  background: #fff;
  box-shadow: 0px 0px 16px 0px #2226440f;
  border-bottom: 1px solid #f0f2f5;
  .header-item {
    &:hover {
      .l-icon {
        visibility: unset;
      }
    }

    .l-icon {
      visibility: hidden;
    }
  }
}

:deep().custom-text-field {
  border-radius: 17px !important;
}
:deep().input-edit {
  &:hover {
    border-color: #d9325a !important;
  }
  .v-field {
    height: 33px !important;
    border-radius: 17px !important;
  }
}

:deep(.custom-checkbox .v-label) {
  font-size: 13px;
  font-weight: 500;
  line-height: 19.5px;
  letter-spacing: 0.25px;
  text-align: left;
}
:deep(.v-list-item) {
  &:hover {
    background-color: #fff0f2;
  }
  .v-input {
    height: 40px;
  }

  .v-label {
    margin-left: 10px;
    color: #3a3b3d;
    font-weight: 400;
    font-size: 13px;
    width: 120px;
  }
}

.item-sticky {
  position: sticky;
  right: 17px;
  z-index: 10;

  &::after {
    content: "";
    width: 20px !important;
    height: 42px;
    position: absolute;
    z-index: -1;
    right: -17px;
    background-color: #fff !important;
  }
}
.upload-matrix-file__icon :deep() .item-label-check {
  .v-label {
    color: #3a3b3d !important;
    font-weight: 400;
    font-size: 13px;
  }
}
:deep() .bg-item-checked {
  background-color: #fff0f2;
}
:deep() .v-list-item--active:not(.v-list-item--link) .v-list-item__overlay {
  opacity: 0 !important;
}
:deep() .v-list-item--active > .v-list-item__overlay {
  opacity: 0 !important;
}
:deep() .value-changed input {
  color: #ba1642 !important;
}
:deep() .v-list {
  box-shadow: 1px 1px 10px 0px #0000001f !important;
}
:deep() .v-list .v-label {
  opacity: unset !important;
}

.item-actived {
  background-color: #fff0f2;
  .item-sticky {
    background-color: #fff0f2 !important;
    &::after {
      background-color: #fff0f2 !important;
    }
  }
}

// .item-row:hover {
//   .item-sticky {
//     background-color: #fff0f2 !important;
//   }
// }

.custom-behind {
  height: 100%;
  width: 20px;
  position: absolute;
  z-index: 2;
  background-color: white;
  top: 0px;
  &.right {
    right: 0px;
  }
  &.left {
    height: calc(100% - 52px);
    left: 0px;
    top: 52px;
  }
}
</style>
