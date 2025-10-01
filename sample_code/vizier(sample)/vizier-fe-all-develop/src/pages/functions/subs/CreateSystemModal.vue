<script setup lang="ts">
import { useGlobal } from "@/store";
import DatetimePicker from "@/components/controls/CfDatetimePicker.vue";
import { CommonUtil } from "@/utils/common-util";
import axios from "axios";

// #region Define Store
const globalStore = useGlobal();

const props = defineProps({
  data: {
    type: Object,
    default: null,
  },
});
const workType = props.data.workType;

// #region Define init value
const formAddTodo = ref<any>(null);
const sysCd = ref("");
const sysCdNm = ref("");
const validEndDtm = ref("");
const validStartDtm = ref("");

// #region Define events
const { translateMessage } = CommonUtil.useTranslatedMessage();
const sysCdRules = ref([
  (value: string) => {
    if (value) return true;
    return translateMessage("system.msg_sysCd_required");
  },
  (value: string) => {
    if (value && value.length <= 20) return true;
    return translateMessage("system.msg_error_maxlength");
  },
  (value: string) => {
    if (value.length === 0 || /^[A-Za-z 0-9]+$/.test(value)) return true;
    return translateMessage("system.msg_error_language");
  },
]);

const sysCdNmRules = ref([
  (value: string) => {
    if (value) return true;
    return translateMessage("system.msg_sysCdNm_required");
  },
  (value: string) => {
    if (value && value.length <= 20) return true;
    return translateMessage("system.msg_error_maxlength");
  },
]);

const sysCdNmChangeHandle = (val: string) => {
  sysCdNm.value = val;
};

const sysCdChangeHandle = (val: string) => {
  sysCd.value = val.toUpperCase();
};

const validateStartDate = () => {
  if (validStartDtm.value && new Date(validStartDtm.value) < new Date()) {
    globalStore.setToastInfor(
      {
        text: "유효시작일시는 현재보다 과거일 수 없음",
        border: "start",
        borderColor: "white",
        type: "error",
        icon: "$error",
        class: "bottom-center",
      },
      5000
    );
    return true;
  }
};

const validateEndDate = () => {
  if (
    validEndDtm.value &&
    new Date(validEndDtm.value) < new Date(validStartDtm.value)
  ) {
    globalStore.setToastInfor(
      {
        text: "유효종료일시는 유효시작일시보다 과거일 수 없음",
        border: "start",
        borderColor: "white",
        type: "error",
        icon: "$error",
        class: "bottom-center",
      },
      5000
    );
    return true;
  }
};

const startDateHandle = (val: string) => {
  validStartDtm.value = val;
  validateStartDate();
};

const endDateHandle = (val: string) => {
  validEndDtm.value = val;
  validateEndDate();
};

const saveSystem = async () => {
  if (validateStartDate() || validateEndDate()) return;
  const { valid } = await formAddTodo.value.validate();
  if (valid) {
    try {
      const requestBody: any = {
        sysCd: sysCd.value,
        sysCdNm: sysCdNm.value,
      };
      if (validStartDtm.value) {
        requestBody.validStartDtm = new Date(validStartDtm.value)
          .toJSON()
          .slice(0, 19);
      }
      if (validEndDtm.value) {
        requestBody.validEndDtm = new Date(validEndDtm.value)
          .toJSON()
          .slice(0, 19);
      }

      const customerBaseUrl = "cust";
      const orderBaseUrl = "ordr";
      let baseUrl = "";
      if (workType === "cust") {
        baseUrl = customerBaseUrl;
      } else {
        baseUrl = orderBaseUrl;
      }

      const response = await axios.post(
        `http://dev.service-billing.com/${baseUrl}/sys/v1`,
        requestBody
      );
      if (response.data.errorCode) {
        //error cod 있을경우
        globalStore.setToastInfor(
          {
            title: translateMessage("system.msg_error_create"),
            text: response.data.errorMsg,
            border: "start",
            borderColor: "white",
            type: "error",
            icon: "$error",
            class: "bottom-center",
          },
          5000
        );
      } else {
        globalStore.setToastInfor(
          {
            title: translateMessage("common.msg_notification"),
            text: translateMessage("system.msg_success_create"),
            border: "start",
            borderColor: "white",
            type: "success",
            icon: "$success",
            class: "bottom-center",
          },
          5000
        );
      }
      emit("closeDialog", response.data);
    } catch (err: any) {
      globalStore.setToastInfor(
        {
          title: translateMessage("common.msg_notification"),
          //text: response.data.errorMsg,
          text: err.toString(),
          border: "start",
          borderColor: "white",
          type: "error",
          icon: "$error",
          class: "bottom-center",
        },
        5000
      );
      emit("closeDialog", err.toString());
    }
  }
};

const emit = defineEmits(["closeDialog"]);
const closeModal = () => {
  emit("closeDialog");
};
</script>
<template>
  <div class="mx-auto prose prose-indigo">
    <div class="flex flex-col mt-[75px]">
      <v-form ref="formAddTodo" class="w-100">
        <div>
          <div class="row-span-12 flex">
            <div class="flex ml-[26px] mr-[26px] align-center">
              <label
                for="sysCd"
                class="w-[191px] h-[22px] font-semibold text-xl"
                ><span class="font-semibold text-[#FF0404]">*</span
                >시스템코드</label
              >
              <cf-input
                :model="sysCd"
                class="sysInput w-[419px]"
                variant="undefined"
                special-action="toUpperCase"
                :rules="sysCdRules"
                @update:model="sysCdChangeHandle"
              ></cf-input>
            </div>
          </div>
          <div class="row-span-12 flex">
            <div class="flex ml-[26px] align-center">
              <label
                for="sysCdNm"
                class="w-[191px] h-[22px] font-semibold text-xl"
                ><span class="font-semibold text-[#FF0404]">*</span
                >시스템명</label
              >
              <cf-input
                :model="sysCdNm"
                class="sysInput w-[419px] h-[60px]"
                variant="undefined"
                :rules="sysCdNmRules"
                @update:model="sysCdNmChangeHandle"
              ></cf-input>
            </div>
          </div>
          <div class="row-span-12 flex">
            <div class="flex ml-[26px] align-center mt-4">
              <label
                for="validStartDtm"
                class="w-[191px] h-[22px] font-semibold text-xl"
                >유효시작일시</label
              >
              <div>
                <DatetimePicker
                  :model="validStartDtm"
                  class="w-[419px]"
                  variant="outlined"
                  @update:model="startDateHandle"
                />
              </div>
            </div>
          </div>
          <div class="row-span-12 flex mt-6">
            <div class="flex ml-[26px] align-center">
              <label
                for="validEndDtm"
                class="w-[191px] h-[22px] font-semibold text-xl"
                >유효종료일시</label
              >
              <div class="w-[400px] h-200">
                <DatetimePicker
                  :model="validEndDtm"
                  class="w-[419px]"
                  variant="outlined"
                  @update:model="endDateHandle"
                />
              </div>
            </div>
          </div>
          <div class="flex justify-end mr-[26px] mt-[65px]">
            <cf-button label="저장" class="custom-btn" @click="saveSystem" />
            <cf-button label="닫기" class="custom-btn" @click="closeModal" />
          </div>
        </div>
      </v-form>
    </div>
  </div>
</template>

<style scoped>
.v-card-title {
  background-color: #e3e3e3;
}
.v-input__details {
  display: none;
}
.total {
  height: 24px;
  width: 124px;
}
.custom-btn {
  background-color: transparent;
  border-radius: 8px;
  border: 1px solid #828282;
  color: #000000;
  height: 46px !important;
  font-weight: 500;
  font-size: 20px;
  padding: 8px;
  width: 90px;
  margin-right: 10px;
}
.v-input__control {
  width: 419px;
}
.sysInput :deep(.v-field__input) {
  border: 1px solid #d9d9d9;
  border-radius: 5px;
  height: 41px !important;
}
.cf-datepicker-input :deep(.dp--menu-wrapper) {
  top: 60px !important;
  left: 200px !important;
  margin: 0 auto;
  display: block;
  position: fixed;
  width: 320px;
}
.cf-datepicker-input :deep(.dp__arrow_bottom) {
  height: 0px !important;
  width: 0px !important;
}
</style>
