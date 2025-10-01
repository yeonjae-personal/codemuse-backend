<script setup lang="ts">
import CfButton from "@/components/controls/CfButton.vue";
import CfSpinner from "@/components/controls/CfSpinner.vue";

const noticeData = ref<any>({
  title: "",
  detail: "",
  id: "",
});
const loading = ref(false);

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

const emit = defineEmits(["closeDialog"]);
const closeModal = () => {
  emit("closeDialog");
};

onMounted(() => {
  fetchDataFromApi();
});
</script>
<template>
  <div v-if="loading === true">
    <div class="mx-auto prose md:px-6 prose-indigo sm:rounded-md">
      <h1 class="text-3xl font-extrabold tracking-tight text-slate-900">
        {{ noticeData.title }}
      </h1>
      <cf-textarea
        v-model="noticeData.detail"
        label=""
        variant="outlined"
        readonly
        rows="20"
        row-height="30"
      ></cf-textarea>
    </div>
    <div class="flex flex-row-reverse mb-1">
      <cf-button
        :label="$t('common.btn_close')"
        rounded="xl"
        class="w-[70px]"
        @click="closeModal"
      />
    </div>
  </div>
  <div v-else class="flex justify-center">
    <cf-spinner indeterminate color="pink"> </cf-spinner>
  </div>
</template>
