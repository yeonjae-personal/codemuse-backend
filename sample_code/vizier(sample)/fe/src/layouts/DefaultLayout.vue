<script setup lang="ts">
import isEmpty from "lodash-es/isEmpty";
import { useGlobal, useUser } from "@/store";
import CfModal from "@/components/controls/CfModal.vue";

const LoginPage = defineAsyncComponent(
  () => import("@/pages/functions/LoginPage.vue")
);

const RegisterPage = defineAsyncComponent(
  () => import("@/pages/functions/RegisterPage.vue")
);

const route = useRoute();

useHead({
  title: () => route.meta.title || "Vite + Vue Template",
  meta: [
    {
      property: "og:title",
      content: () => route.meta.title,
    },
    {
      name: "twitter:title",
      content: () => route.meta.title,
    },
  ],
});

const userStore = useUser();
const globalStore = useGlobal();

const alertVisibility = ref<boolean>(false);

const user = computed(() => userStore?.user);

const isShowLogin = computed(() => userStore?.isShowLogin);

const alertInformation = computed<any>(() => globalStore.toastInfor);

watch(
  () => globalStore.toastInfor,
  (toastInfor) => (alertVisibility.value = Object.keys(toastInfor).length > 0)
);
</script>

<template>
  <div class="relative channel-framework">
    <div
      class="absolute z-20 top-0 inset-x-0 flex justify-center overflow-hidden pointer-events-none"
    ></div>
    <div v-if="Object.keys(user).length > 0" class="overflow-hidden">
      <div class="max-w-8xl mx-auto px-4 sm:px-6 md:px-8">
        <!-- channel-sidebar -->
        <div
          class="hidden lg:block fixed z-20 inset-0 top-[3.8125rem] left-[max(0px,calc(50%-45rem))] right-auto w-[19rem] pb-10 pl-8 pr-6 overflow-y-auto"
        >
          <nav id="nav" class="lg:text-sm lg:leading-6 relative">
            <!-- sidebar-menu -->
          </nav>
        </div>
        <!-- channel-content -->
        <div class="lg:pl-[17.5rem]">
          <main class="max-w-3xl mx-auto relative z-20 xl:max-w-none"></main>
        </div>
      </div>
    </div>

    <div v-if="isEmpty(user)">
      <LoginPage v-if="isShowLogin" />
      <RegisterPage v-else />
    </div>

    <cf-toast
      v-if="alertVisibility"
      :title="alertInformation.title"
      :text="alertInformation.text"
      :type="alertInformation.type"
      :icon="alertInformation.icon"
      :variant="alertInformation.variant"
      :color="alertInformation.color"
      :border="alertInformation.border"
      :border-color="alertInformation.borderColor"
      :closable="true"
      :class="alertInformation.class"
    />

    <cf-alert :data="globalStore.alert" />

    <cf-modal
      v-for="(modal, index) in globalStore.modals"
      :key="index"
      :modal="modal"
      :index="index"
    />
  </div>
</template>

<style scoped>
.toogle-language :deep(.v-switch__track) {
  opacity: 1;
}
</style>
