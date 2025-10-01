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
const formAddOrder = ref<any>(null);
const ordrItemId = ref(props.data.dataRow.ordrItemId);
const sndSysCd = ref(props.data.dataRow.sysCd);
const sysCdNm = ref(props.data.dataRow.sysCdNm);
const ordrItemEngNm = ref(props.data.dataRow.ordrItemEngNm);
const ordrItemKornNm = ref(props.data.dataRow.ordrItemKornNm);
const validFormAddOrder = ref(false);
const showErrors = ref(false);
const onInputSysCd = () => {
  sndSysCd.value = sndSysCd.value.replace(/[^a-zA-Z0-9]/g, "").toUpperCase();
};
const sndSysCdRules = [
  (value: string) =>
    !!value || translateMessage("order.msg_ordrSndSysCd_required"),
];

const workCd = ref({ key: "ordr", value: "오더" });

const updateSndSysCdValue = (data: any) => {
  if (!data) return;
  else {
    sndSysCd.value = data.sysCd.toUpperCase();
    sysCdNm.value = data.sysCdNm.toUpperCase();
  }
};

const ordrItemEngNmRules = [
  (value: string) => !!value || "항목을 입력해 주세요.",
];
const ordrItemKornNmRules = [
  (value: string) => !!value || "항목명을 입력해 주세요.",
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

const inputOrdrItemEngNmErrors = computed(() => {
  const errors: string[] = [];
  for (const rule of ordrItemEngNmRules) {
    const error = rule(ordrItemEngNm.value);
    if (error !== true) {
      errors.push(error);
    }
  }
  return errors;
});

const inputOrdrItemKornNmErrors = computed(() => {
  const errors: string[] = [];
  for (const rule of ordrItemKornNmRules) {
    const error = rule(ordrItemKornNm.value);
    if (error !== true) {
      errors.push(error);
    }
  }
  return errors;
});

const onInputordrItemEngNm = () => {
  ordrItemEngNm.value = ordrItemEngNm.value
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
const saveOrder = async () => {
  showErrors.value = true;
  if (formAddOrder.value) {
    formAddOrder.value.validate();
    if (
      validFormAddOrder.value &&
      inputSndSysCdErrors.value.length === 0 &&
      inputOrdrItemEngNmErrors.value.length === 0 &&
      inputOrdrItemKornNmErrors.value.length === 0
    ) {
      try {
        const requestBody = [
          {
            ordrItemId: `${ordrItemId.value}`,
            sysCd: `${sndSysCd.value}`,
            ordrItemEngNm: `${ordrItemEngNm.value}`,
            ordrItemKornNm: `${ordrItemKornNm.value}`,
          },
        ];
        const response = await httpClient.post(
          `/api/ordr/ordritem/v1/ordritem`,
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
      <v-form ref="formAddOrder" v-model="validFormAddOrder" class="w-100">
        <div>
          <div class="row-span-12 flex">
            <div class="flex ml-[10px] mr-[26px] align-center">
              <label for="sysCd" class="w-[191px] h-[22px] font-medium text-xl"
                ><span class="font-semibold text-[#FF0404]">*</span
                >시스템코드</label
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
                for="ordrItemEngNm"
                class="w-[191px] h-[22px] font-medium text-xl"
                ><span class="font-semibold text-[#FF0404]">*</span>항목</label
              >
              <cf-input
                v-model="ordrItemEngNm"
                class="sysInput w-[469px] h-[41px]"
                :variant="undefined"
                :error-messages="showErrors ? inputOrdrItemEngNmErrors : []"
                @input="onInputordrItemEngNm"
              ></cf-input>
            </div>
          </div>
          <div class="row-span-12 flex pt-6">
            <div class="flex ml-[10px] align-center">
              <label
                for="ordrItemKornNm"
                class="w-[191px] h-[22px] font-medium text-xl"
                ><span class="font-semibold text-[#FF0404]">*</span
                >항목명</label
              >
              <cf-input
                v-model="ordrItemKornNm"
                class="sysInput w-[469px] h-[41px]"
                :variant="undefined"
                :error-messages="showErrors ? inputOrdrItemKornNmErrors : []"
                maxlength="50"
              ></cf-input>
            </div>
          </div>
          <div class="flex justify-end mr-[26px] mt-[30px]">
            <cf-button label="저장" class="custom-btn" @click="saveOrder" />
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
