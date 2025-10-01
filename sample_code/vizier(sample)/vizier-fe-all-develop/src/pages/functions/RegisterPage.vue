<script setup lang="ts">
import { useUser } from "@/store";
const userStore = useUser();
const userID = ref("");
const userName = ref("");
const password = ref("");
const passwordConfirm = ref("");
const isShowPass = ref(false);
const isShowPassCf = ref(false);

const togglePass = () => {
  isShowPass.value = !isShowPass.value;
};
const togglePassCf = () => {
  isShowPassCf.value = !isShowPassCf.value;
};

const backToLogin = () => {
  userStore.isShowLogin = true;
};
</script>
<template>
  <div class="auth-wrapper h-[100vh] flex justify-center items-center">
    <div class="auth-form h-[682px] w-[560px] rounded-[24px]">
      <div class="text-[18px] text-[#0F9396] text-align font-bold text-center">
        Welcome to VIZIER
      </div>
      <div
        class="text-[36px] text-[#ba1642] font-bold text-center mt-10 mb-8 ml-[-5px]"
      >
        <div class="absolute mt-4 cursor-pointer" @click="backToLogin">
          <back-login-icon />
        </div>
        SIGN UP
      </div>
      <base-input-text
        v-model="userID"
        :placeholder="'User ID'"
        :styles="'input-search w-[464px] !h-[52px] font-medium'"
      />
      <base-input-text
        v-model="userName"
        :placeholder="'User Name'"
        :styles="'input-search w-[464px] !h-[52px] font-medium mt-6'"
      />
      <base-input-text
        v-model="password"
        :placeholder="'Password'"
        :type="!isShowPass ? 'password' : 'text'"
        :styles="'input-search w-[464px] !h-[52px] font-medium mt-6'"
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
      <base-input-text
        v-model="passwordConfirm"
        :placeholder="'Confirm Password'"
        :type="!isShowPassCf ? 'password' : 'text'"
        :styles="'input-search w-[464px] !h-[52px] font-medium mt-6'"
      >
        <template #append-inner>
          <div
            class="cursor-pointer"
            @mousedown.prevent
            @mouseup.prevent
            @click="togglePassCf"
          >
            <eye-on v-if="!isShowPassCf" />
            <eye-off v-else />
          </div>
        </template>
      </base-input-text>
      <BaseButton
        class="text-[15px] mt-[24px] text-center font-medium"
        width="464px"
        height="52px"
      >
        SIGN UP
        <div class="ml-2">
          <sign-up-icon :fill="'#fff'" />
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
  background-image: url(/src/assets/images/bg-auth.png);
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
}

:deep(.v-btn__content) {
  font-size: 15px;
  font-weight: 400;
  font-family: Noto Sans KR !important;
}
:deep(.v-input--error) {
  border-color: #ff8fa1 !important;
}
</style>
