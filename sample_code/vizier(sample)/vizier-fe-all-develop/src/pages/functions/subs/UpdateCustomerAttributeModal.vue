<script setup lang="ts">
import { httpClient } from "@/utils/http-common";
import { useGlobal } from "@/store";
import { CommonUtil } from "@/utils/common-util";
import SysCdPage from "@/pages/solution/SystemPage.vue";
import { VForm } from "vuetify/components/VForm";

// #region Define Store
const globalStore = useGlobal();

const props = defineProps({
  data: {
    type: Object,
    default: null,
  },
});
const formCustAtrr = ref<any>(null);
const custAttrId = ref(props.data.dataRow.custAttrId);
const sndSysCd = ref(props.data.dataRow.sysCd);
const sysCdNm = ref(props.data.dataRow.sysCdNm);
const custAttrEngNm = ref(props.data.dataRow.custAttrEngNm);
const custAttrKornNm = ref(props.data.dataRow.custAttrKornNm);
const validformCustAtrr = ref(false);
const showErrors = ref(false);
const onInputSysCd = () => {
  sndSysCd.value = sndSysCd.value.replace(/[^a-zA-Z0-9]/g, "").toUpperCase();
};
const sndSysCdRules = [
  (value: string) =>
    !!value || translateMessage("order.msg_ordrSndSysCd_required"),
];

const workCd = ref({ key: "cust", value: "고객" });

const updateSndSysCdValue = (data: any) => {
  if (!data) return;
  else {
    sndSysCd.value = data.sysCd.toUpperCase();
    sysCdNm.value = data.sysCdNm.toUpperCase();
  }
};

const custAttrEngNmRules = [
  (value: string) => !!value || "고객속성명(영문)을 입력해 주세요.",
];
const custAttrKornNmRules = [
  (value: string) => !!value || "고객속성명(한글)을 입력해 주세요.",
];

const inputSndSysCdErrors = computed(() => {
  const errors: string[] = [];
  for (const rule of sndSysCdRules) {
    const error = rule(sndSysCd.value);
    if (error !== true) {
      errors.push(error);
    }
  }
  return errors;
});

const inputcustAttrEngNmErrors = computed(() => {
  const errors: string[] = [];
  for (const rule of custAttrEngNmRules) {
    const error = rule(custAttrEngNm.value);
    if (error !== true) {
      errors.push(error);
    }
  }
  return errors;
});

const inputcustAttrKornNmErrors = computed(() => {
  const errors: string[] = [];
  for (const rule of custAttrKornNmRules) {
    const error = rule(custAttrKornNm.value);
    if (error !== true) {
      errors.push(error);
    }
  }
  return errors;
});

const onInputcustAttrEngNm = () => {
  custAttrEngNm.value = custAttrEngNm.value
    .toLowerCase()
    .replace(/[^a-z0-9_]/g, "");
};
const searchSndSysCd = async () => {
  const objectModal: any = {
    title: "시스템 관리",
    component: SysCdPage,
    dataInput: {
      sysCd: sndSysCd.value,
      workType: workCd.value,
      screenType: "popUp",
    },
    width: "920",
    type: "custom",
  };
  const data = await globalStore.openModal(objectModal);

  if (data) {
    updateSndSysCdValue(data);
  }
};
const saveCustAttr = async () => {
  showErrors.value = true;
  if (formCustAtrr.value) {
    formCustAtrr.value.validate();
    if (
      validformCustAtrr.value &&
      inputSndSysCdErrors.value.length === 0 &&
      inputcustAttrEngNmErrors.value.length === 0 &&
      inputcustAttrKornNmErrors.value.length === 0
    ) {
      try {
        const requestBody = {
          custAttrId: `${custAttrId.value}`,
          sysCd: `${sndSysCd.value}`,
          custAttrEngNm: `${custAttrEngNm.value}`,
          custAttrKornNm: `${custAttrKornNm.value}`,
        };
        const response = await httpClient.post(
          `/api/cust/custattr/v1/custattr`,
          requestBody
        );
        if (response.status === 200 && !response.data.errorCode) {
          globalStore.setToastInfor(
            {
              text: "성공적으로 저장하였습니다.",
              type: "success",
              variant: "tonal",
              class: "bottom-center",
            },
            3000
          );

          emit("closeDialog", "SUCCESS");
        } else {
          globalStore.setToastInfor(
            {
              text: response.data.errorMsg,
              type: "error",
              variant: "tonal",
              class: "bottom-center",
            },
            3000
          );

          emit("closeDialog", "SUCCESS");
        }
      } catch (error: any) {
        console.log(error);
        globalStore.setToastInfor(
          {
            text: "저장에 실패하였습니다.",
            type: "error",
            variant: "tonal",
            class: "bottom-center",
          },
          3000
        );
        emit("closeDialog");
      }
    } else {
      console.log("Form is invalid");
    }
  }
};
const { translateMessage } = CommonUtil.useTranslatedMessage();

const emit = defineEmits(["closeDialog"]);
const closeModal = () => {
  emit("closeDialog");
};
</script>
<template>
  <div class="mx-auto prose prose-indigo">
    <div class="flex flex-col mt-[30px]">
      <v-form ref="formCustAtrr" v-model="validformCustAtrr" class="w-100">
        <div>
          <div class="row-span-12 flex">
            <div class="flex ml-[10px] mr-[26px] align-center">
              <label for="sysCd" class="w-[191px] h-[22px] font-medium text-xl"
                ><span class="font-semibold text-[#FF0404]">*</span
                >System</label
              >
              <cf-input
                v-model="sndSysCd"
                class="sysInput w-[188px] h-[41px]"
                :variant="undefined"
                maxlength="20"
                :error-messages="showErrors ? inputSndSysCdErrors : []"
                @input="onInputSysCd"
              ></cf-input>
              <v-btn
                prepend-icon="mdi-magnify mdi-24px"
                class="!w-[40px] !h-[41px] search-btn p-0 mr-1"
                @click="searchSndSysCd"
              >
              </v-btn>
              <cf-input
                v-model="sysCdNm"
                class="sysInput w-[214px] h-[41px] bg-[#D9D9D9]"
                :variant="undefined"
                special-action="toUpperCase"
                disabled
              ></cf-input>
            </div>
          </div>
          <div class="row-span-12 flex pt-6">
            <div class="flex ml-[10px] align-center">
              <label
                for="custAttrEngNm"
                class="w-[191px] h-[22px] font-medium text-xl -mt-5"
                ><span class="font-semibold text-[#FF0404]">*</span
                >CustomerAttribute (English)</label
              >
              <cf-input
                v-model="custAttrEngNm"
                maxlength="100"
                class="sysInput w-[469px] h-[41px]"
                :error-messages="showErrors ? inputcustAttrEngNmErrors : []"
                :variant="undefined"
                @input="onInputcustAttrEngNm"
              ></cf-input>
            </div>
          </div>
          <div class="row-span-12 flex pt-6">
            <div class="flex ml-[10px] align-center">
              <label
                for="custAttrKornNm"
                class="w-[191px] h-[22px] font-medium text-xl -mt-5"
                ><span class="font-semibold text-[#FF0404]">*</span
                >CustomerAttribute (Korean)</label
              >
              <cf-input
                v-model="custAttrKornNm"
                class="sysInput w-[469px] h-[41px]"
                :variant="undefined"
                :error-messages="showErrors ? inputcustAttrKornNmErrors : []"
                maxlength="100"
              ></cf-input>
            </div>
          </div>
          <div class="flex justify-end mr-[26px] mt-[30px]">
            <cf-button label="save" class="custom-btn" @click="saveCustAttr" />
            <cf-button label="close" class="custom-btn" @click="closeModal" />
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
  border-radius: 8px !important;
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
.sysInput :deep(.v-input__control .v-field .v-field__field .v-field__input) {
  border: 1px solid #d9d9d9;
  /* border-radius: 5px; */
  height: 41px !important;
  min-height: 41px;
  padding-bottom: 12px;
}
.sysInput :deep(.v-input__details) {
  min-height: 16px;
}
.cf-datepicker-input :deep(.dp--menu-wrapper) {
  top: 60px !important;
  left: 200px !important;
  margin: 0 auto;
  display: block;
  position: fixed;
  width: 320px;
  padding: 0;
}
.cf-datepicker-input :deep(.dp__arrow_bottom) {
  height: 0px !important;
  width: 0px !important;
}

.cf-datepicker-input :deep(input) {
  height: 41px !important;
}

.custom-file-input
  :deep(.v-input__control .v-field .v-field__field .v-field__input) {
  display: flex;
  justify-content: left;
  padding: 10px 8px 16px 16px;
  height: 40px;
  min-height: 0px;
}
.custom-file-input {
  border: 1px solid #d9d9d9;
}
.custom-file-input :deep(.mdi-menu-down::before) {
  content: "\F0140" !important;
}
.search-btn,
.v-btn {
  box-shadow: none !important;
  border: 1px solid #d9d9d9;
  border-radius: 0px !important;
}
.v-btn.v-btn--density-default {
  border-radius: 4px !important;
}
:deep() .v-input__details {
  padding-top: 20px;
  min-height: 20px !important;
}
:deep() .v-input__details .v-messages__message {
  font-size: 11px !important;
}
</style>
