<script setup lang="ts">
import { httpClient } from "@/utils/http-common";
import { useGlobal } from "@/store";
import { CommonUtil } from "@/utils/common-util";

// #region Define Store
const globalStore = useGlobal();

// #region Define init value
const formAddTodo = ref<any>(null);

const num = ref("");
const name = ref("");
const birth = ref("");
const address = ref("");
const gender = ref("");

// #region Define validate
const numRules = ref([
  (value: string) => {
    if (value) return true;
    return translateMessage("employee.msg_employee_num_required");
  },
]);

// #region Define events
const { translateMessage } = CommonUtil.useTranslatedMessage();

const numChangeHandle = (val: string) => {
  num.value = val;
};

const nameChangeHandle = (val: string) => {
  name.value = val;
};

const birthChangeHandle = (val: string) => {
  birth.value = val;
};

const addressChangeHandle = (val: string) => {
  address.value = val;
};

const genderChangeHandle = (val: string) => {
  gender.value = val;
};

const saveEmployee = async () => {
  const { valid } = await formAddTodo.value.validate();
  if (valid) {
    try {
      const requestBody = {
        num: num.value,
        name: name.value,
        birthdate: birth.value,
        address: address.value,
        gender: gender.value,
      };

      const response: any = await httpClient.post(
        `/api/comm/employees`,
        requestBody
      );
      if (response.data.errorCode) {
        //error cod 있을경우
        globalStore.setToastInfor(
          {
            title: translateMessage("common.msg_notification"),
            text: response.data.errorMsg,
            border: "start",
            borderColor: "white",
            type: "error",
            icon: "$error",
          },
          5000
        );
      } else {
        globalStore.setToastInfor(
          {
            title: translateMessage("common.msg_notification"),
            text: translateMessage("employee.msg_success_create"),
            border: "start",
            borderColor: "white",
            type: "success",
            icon: "$success",
          },
          5000
        );
      }
      emit("closeDialog", response.data.data);
    } catch (err: any) {
      globalStore.setToastInfor(
        {
          title: translateMessage("common.msg_notification"),
          text: response.data.errorMsg,
          border: "start",
          borderColor: "white",
          type: "error",
          icon: "$error",
        },
        5000
      );
    }
  }
};

const emit = defineEmits(["closeDialog"]);
const closeModal = () => {
  emit("closeDialog");
};
</script>
<template>
  <div class="mx-auto prose prose-indigo sm:rounded-md">
    <div class="flex flex-col w-100">
      <v-form ref="formAddTodo" class="w-100">
        <div class="flex flex-col gap-4">
          <CfInput
            :label="$t('employee.lbl_employee_num')"
            variant="underlined"
            :rules="numRules"
            :model="num"
            @update:model="numChangeHandle"
            @keydown.enter.prevent=""
          ></CfInput>
          <cf-input
            :label="$t('employee.lbl_employee_name')"
            variant="underlined"
            :model="num"
            @update:model="nameChangeHandle"
            @keydown.enter.prevent=""
          ></cf-input>
          <cf-input
            :label="$t('employee.lbl_employee_birth')"
            variant="underlined"
            :model="birth"
            @update:model="birthChangeHandle"
            @keydown.enter.prevent=""
          ></cf-input>
          <cf-input
            :label="$t('employee.lbl_employee_gender')"
            variant="underlined"
            :model="gender"
            @update:model="genderChangeHandle"
            @keydown.enter.prevent=""
          ></cf-input>
          <cf-input
            :label="$t('employee.lbl_employee_address')"
            variant="underlined"
            :model="address"
            @update:model="addressChangeHandle"
            @keydown.enter.prevent=""
          ></cf-input>
        </div>
        <div class="flex gap-4">
          <cf-button
            :label="$t('common.btn_save')"
            rounded="xl"
            @click="saveEmployee"
          />
          <cf-button
            :label="$t('common.btn_close')"
            rounded="xl"
            @click="closeModal"
          />
        </div>
      </v-form>
    </div>
  </div>
</template>

<style scoped></style>
