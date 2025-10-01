<script setup lang="ts">
import editNoticeModal from "./subs/editNoticeModal.vue";
import { useUser, useGlobal } from "@/store";
import CfSpinner from "@/components/controls/CfSpinner.vue";

const userStore = useUser();
const globalStore = useGlobal();
const noticeData = ref<any>({
  title: "",
  detail: "",
  id: "",
});

const loading = ref(false);

const showModal = async () => {
  const objectModal: any = {
    title: "",
    component: editNoticeModal,
    dataInput: {
      data: {
        title: noticeData.value.title,
        detail: noticeData.value.detail,
        id: noticeData.value.id,
      },
    },
    width: "700",
  };
  const data = await globalStore.openModal(objectModal);
  if (!data.isTrusted) {
    noticeData.value.title = data[0].title;
    noticeData.value.detail = data[0].detail;
  }
};

const fetchDataFromApi = async () => {
  try {
    //수정필요
    const response = {
      data: {
        data: {
          title: "테스트Notice입니다",
          detail: "",
        },
      },
    };
    const notice = response.data.data;
    noticeData.value = notice;
    loading.value = true;
  } catch (error) {
    console.error("Error fetching notice:", error);
  }
};

onMounted(() => {
  fetchDataFromApi();
});
</script>
<template>
  <div class="p-4 mx-auto prose md:px-6 prose-indigo sm:rounded-md">
    <h1
      class="mt-3 text-3xl font-extrabold tracking-tight text-slate-900 add-todo"
    >
      Notice
      <span
        v-if="userStore.user.level === 'Master'"
        class="mdi mdi-pencil-plus"
        @click="showModal"
      ></span>
    </h1>

    <div class="border border-blue-600 pt-1 px-7">
      <div v-if="loading === true">
        <h1 class="mt-3 text-3xl font-extrabold tracking-tight text-slate-900">
          {{ noticeData.title }}
        </h1>
        <cf-textarea
          v-model="noticeData.detail"
          label=""
          variant="outlined"
          class="custom-text-area"
          rows="20"
          row-height="30"
          readonly
        ></cf-textarea>
      </div>
      <div v-else class="flex justify-center">
        <cf-spinner indeterminate color="pink"> </cf-spinner>
      </div>
    </div>
  </div>
</template>

<style scoped>
/*  */
</style>
