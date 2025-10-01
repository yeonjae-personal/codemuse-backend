<script setup lang="ts">
// import CfInput from "@/components/controls/CfInput.vue";
// import CfButton from "@/components/controls/CfButton.vue";
import { useGlobal, useUser } from "@/store";
import { CommonUtil } from "@/utils/common-util";
import TrueIcon from "@/components/prod/icons/TrueIcon.vue";
import FalseIcon from "@/components/prod/icons/FalseIcon.vue";

// #region Define Store
const userStore = useUser();
const globalStore = useGlobal();

// #region Define events
const { translateMessage } = CommonUtil.useTranslatedMessage();

// #region Define init value
const formLogin = ref<any>(null);
const userID = ref("");
const password = ref("");
const remember = ref(false);
const isShowPass = ref(false);

// #region Define validate
const userIDRules = ref([
  (value: string) => {
    if (value) return true;
    return translateMessage("login.msg_user_id_required");
  },
]);

const passwordRules = ref([
  (value: string) => {
    if (value) return true;
    return translateMessage("login.msg_password_required");
  },
]);

// const userIDChangeHandle = (val: string) => {
//   userID.value = val;
// };

const toggleRemember = () => {
  remember.value = !remember.value;
};

// const passwordChangeHandle = (val: string) => {
//   password.value = val;
// };

const loginInputHandle = async () => {
  const { valid } = await formLogin.value.validate();

  if (valid) {
    try {
      if (remember.value) {
        localStorage.setItem("rememberMe", userID.value);
      } else {
        localStorage.removeItem("rememberMe");
      }

      const requestBody = {
        userId: userID.value,
        password: password.value,
      };
      const response = await userStore.login(requestBody);
      console.log(response);
    } catch (err: any) {
      globalStore.setToastInfor(
        {
          title: translateMessage("common.msg_notification"),
          text: translateMessage("login.msg_error_login"),
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

const goToRegister = () => {
  userStore.isShowLogin = false;
};

const togglePass = () => {
  isShowPass.value = !isShowPass.value;
};

onMounted(async () => {
  const rememberMe = localStorage.getItem("rememberMe");
  if (rememberMe) {
    remember.value = true;
    userID.value = rememberMe;

    const firstErrorField = document.getElementById("input-2");
    if (firstErrorField) firstErrorField?.focus();
  } else {
    remember.value = false;
    userID.value = "";
  }
});
</script>
<template>
  <div class="auth-wrapper h-[100vh] flex justify-center items-center">
    <div class="auth-form h-[682px] w-[560px] rounded-[24px]">
      <div class="text-[18px] text-[#0F9396] text-align font-bold text-center">
        Welcome to VIZIER
      </div>
      <div class="text-[36px] text-[#ba1642] font-bold text-center mt-10 mb-8">
        LOGIN
      </div>
      <v-form ref="formLogin" class="flex gap-6 flex-col">
        <base-input-text
          v-model="userID"
          :styles="'input-form font-medium login-input'"
          :placeholder="'User ID'"
          :rules="userIDRules"
        >
        </base-input-text>
        <base-input-text
          v-model="password"
          :placeholder="'Password'"
          :type="!isShowPass ? 'password' : 'text'"
          :styles="'input-form font-medium login-input'"
          :rules="passwordRules"
          name="input-10-1"
          @enter="loginInputHandle"
        >
          <template #append-inner>
            <div
              class="cursor-pointer"
              @mousedown.prevent
              @mouseup.prevent
              @click="togglePass"
            >
              <eye-on v-if="!isShowPass" />
              <eye-off v-else />
            </div>
          </template>
        </base-input-text>
        <div class="flex justify-between items-center mt-2 h-[20px]">
          <div
            class="flex items-center text-[13px] font-medium text-[ #3a3b3d]"
          >
            <v-checkbox
              v-model="remember"
              :true-value="true"
              :false-value="false"
              :true-icon="TrueIcon"
              :false-icon="FalseIcon"
              :hide-details="true"
              density="compact"
              class="custom-checkbox"
            ></v-checkbox>
            <span class="ml-1 cursor-pointer" @click="toggleRemember"
              >Remember me</span
            >
          </div>
          <div class="text-[13px] font-medium text-[#ba1642] cursor-pointer">
            Forgot Password
          </div>
        </div>
      </v-form>
      <BaseButton
        class="text-[15px] text-[#ffffff] mt-[48px] text-center font-medium"
        width="464px"
        height="52px"
        @click="loginInputHandle"
      >
        LOGIN
        <div class="ml-2">
          <login-icon :fill="'#fff'" />
        </div>
      </BaseButton>
      <BaseButton
        class="text-[15px] !text-[#ba1642] mt-4 text-center font-medium btn-sign-up"
        width="464px"
        height="52px"
        color="#FFF0F2"
        @click="goToRegister"
      >
        SIGN UP
        <div class="ml-2">
          <sign-up-icon :fill="'#ba1642'" />
        </div>
      </BaseButton>
      <div
        class="text-[15px] mt-10 text-[#0F9396] text-align font-normal text-center"
      >
        Copyright ©2024 VIZIER
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.auth-wrapper {
  background: url(/src/assets/images/bg-auth.png);
  background-repeat: no-repeat;
  font-family: Noto Sans KR;
  background-size: cover;
}
.auth-form {
  padding: 48px 48px 80px;
  background: linear-gradient(
    188.32deg,
    rgba(255, 255, 255, 0.52) 49.61%,
    rgba(255, 255, 255, 0.32) 93.62%
  );
  box-shadow: 0px 2px 64px 0px #172d651f;
  box-shadow: 0px 0px 40px 0px #ffffffa3 inset;
}

:deep(.v-input__control) {
  height: 52px !important;
  .v-field {
    height: 52px !important;
  }
  input {
    font-size: 15px;
    padding: 0px 16px !important;
  }
  .v-field__field {
    height: 52px !important;
  }
  .v-field__input {
    height: 52px !important;
  }
}

:deep(.v-btn__content) {
  font-size: 15px;
  font-weight: 400;
  font-family: Noto Sans KR !important;
}
:deep(.btn-sign-up) {
  background: #fff0f2 !important;
}
:deep(.v-input--error) {
  border-color: #ff8fa1 !important;
}
.login-input :deep(.v-input__details) {
  bottom: 57px !important;
}
</style>
