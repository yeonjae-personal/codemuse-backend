<template>
  <div
    class="rounded-[12px] w-full min-h-[48px] cursor-pointer"
    :class="[
      !expand && 'zoom-animation',
      isActive
        ? `border-[2px] !border-[${BORDER_CONFIG.ACTIVE}]`
        : 'border-[1px] !border-[#e6e9ed]',
    ]"
  >
    <div
      class="w-full pa-3 flex justify-between align-center rounded-tl-[12px] rounded-tr-[12px]"
      :class="[expand && 'bg-[#f7f8fa] border-b-[1px] border-b-[#e6e9ed]']"
      @click="emit('handleClick')"
    >
      <div class="flex align-center gap-2">
        <div
          class="rounded-[50%] bg-[#fff0f2] h-[24px] w-[24px] text-center text-[#ba1642] text-[11px] font-[500] leading-[23px]"
        >
          {{ item.sortNo }}
        </div>
        <div class="text-[#3a3b3d] text-[13px] font-[500]">
          {{ title }}
        </div>
      </div>
      <div v-if="aprvDtm" class="text-[#6b6d70] text-[11px] font-[400]">
        {{ formatDate(aprvDtm, DATE_FORMAT.DATE_FORMAT_WITHOUT_TIME_REVERSE) }}
      </div>
    </div>
    <template v-if="expand">
      <div class="my-3 px-4">
        <div class="flex gap-2">
          <div class="text-[#6b6d70] text-[13px] font-[500] w-[30%]">
            {{ $t("product_platform.approvers") }}
          </div>
          <div class="flex flex-col w-[60%]">
            <LocomotiveComponent
              scroll-container-class="!px-0 max-h-[100px]"
              scroll-content-class=""
              :is-stop-propagation-wheel="isStopPropagationWheelStatus"
              @is-wheel="handleWheelDetail"
            >
              <div class="flex flex-col gap-[6px]">
                <template
                  v-for="app in item?.pubAprvSubStepLDtos"
                  :key="app.aprvFlowTmptCode"
                >
                  <div class="text-[#3a3b3d] text-[13px] font-[500] truncate">
                    {{ app.aprvUser }}
                  </div>
                </template>
              </div>
            </LocomotiveComponent>
          </div>
        </div>
      </div>
      <div class="my-3 px-4">
        <div class="flex gap-2 align-center">
          <div class="text-[#6b6d70] text-[13px] font-[500] w-[30%]">
            {{ $t("product_platform.time_limit") }}
          </div>
          <div class="">
            <div v-if="isShowTimeInput" class="flex align-center gap-2">
              <BaseInputText
                v-model="data.lmtTm"
                styles="input-edit w-[80px]"
                :maxlength="4"
                @keypress="onlyNumber"
                @input="changeValueNumber($event, item)"
              />
              <span class="text-[#3a3b3d] text-[13px] font-[500]">{{
                $t("product_platform.days")
              }}</span>
            </div>
            <div v-else class="text-[#3a3b3d] text-[13px] font-[500] truncate">
              {{ item.lmtTm }} {{ $t("product_platform.days") }}
            </div>
          </div>
        </div>
      </div>
      <div v-if="!isShowTimeInput" class="h-[1px] bg-[#e6e9ed] mb-3 mx-4"></div>
      <div
        v-if="!isShowTimeInput"
        class="flex justify-end gap-2 mx-4 mb-3 action-flow"
      >
        <BaseButton
          :color="ButtonColorType.Gray"
          :disabled="!isDisabledAction"
          @click="emit('showReject')"
        >
          {{ $t("product_platform.reject") }}
        </BaseButton>
        <BaseButton
          :color="ButtonColorType.Secondary"
          :disabled="!isDisabledAction"
          @click="emit('showApprove')"
        >
          {{ $t("product_platform.approve") }}
        </BaseButton>
      </div>
    </template>
  </div>
</template>
<script lang="ts" setup>
import { BORDER_CONFIG, DATE_FORMAT } from "@/constants/index";
import { ButtonColorType } from "@/enums";
import { formatDate } from "@/utils/format-data";

const props = defineProps({
  expand: {
    type: Boolean,
    default: false,
  },
  isShowTimeInput: {
    type: Boolean,
    default: false,
  },
  isDisabledAction: {
    type: Boolean,
    default: false,
  },
  isActive: {
    type: Boolean,
    default: false,
  },
  numStep: {
    type: [Number, String],
    default: "",
  },
  item: {
    type: Object,
    default: () => {},
  },
  title: {
    type: String,
    default: "",
  },
  aprvDtm: {
    type: String,
    default: "",
  },
});
const emit = defineEmits([
  "handleClick",
  "showReject",
  "showApprove",
  "update:item",
]);
const isStopPropagationWheelStatus = ref(false);
const handleWheelDetail = (event) => {
  isStopPropagationWheelStatus.value = event;
};
const data = computed({
  get() {
    return props.item;
  },
  set(newVal) {
    emit("update:item", newVal);
  },
});

const changeValueNumber = (event, newVal) => {
  const vowelsRegex = /^\d+$/;
  let valueOld = event.target.value;
  if (valueOld.length < 2 && Number(valueOld) === 0) {
    newVal.lmtTm = null;
  } else {
    newVal.lmtTm = valueOld
      .split("")
      .filter((val) => vowelsRegex.test(val))
      .join()
      .replaceAll(",", "");
  }
};
const onlyNumber = ($event) => {
  let keyCode = $event.keyCode ? $event.keyCode : $event.which;

  if (keyCode < 48 || keyCode > 57) {
    $event.preventDefault();
  }
};
</script>
