<!-- eslint-disable vue/multi-word-component-names -->
<script setup lang="ts">
import { useGlobal } from "@/store";
import editDescriptionModal from "./subs/editDescriptionModal.vue";
import { httpClient } from "@/utils/http-common";
import { CommonUtil } from "@/utils/common-util";
import CfSpinner from "@/components/controls/CfSpinner.vue";

const globalStore = useGlobal();
const { translateMessage } = CommonUtil.useTranslatedMessage();

const props = defineProps({
  name: {
    type: String as () =>
      | "Accordion"
      | "Alert"
      | "Avatar"
      | "Badge"
      | "Button"
      | "Card"
      | "ListGroup"
      | "Tabs"
      | "Modal"
      | "Toast"
      | "Footer"
      | "Input"
      | "File Input"
      | "Select"
      | "Textarea"
      | "Checkbox"
      | "Radio",
    required: true,
  },
});

const description = ref(null);
const loading = ref(false);

const showModal = async () => {
  const objectModal: any = {
    title: props.name + " " + translateMessage("common.lbl_description"),
    component: editDescriptionModal,
    dataInput: {
      data: {
        name: props.name,
        value: description.value,
      },
    },
    width: "800",
  };
  const data = await globalStore.openModal(objectModal);
  if (!data.isTrusted) {
    description.value = data;
  }
};

const fetchDataFromApi = async () => {
  try {
    const response = await httpClient.get(`descriptions/${props.name}`);
    if (response.data.status === "200") {
      description.value = response.data.data.description;
      loading.value = true;
    } else {
      globalStore.setToastInfor(
        {
          title: translateMessage("common.msg_notification"),
          text: response.data.message,
          border: "start",
          borderColor: "white",
          type: "error",
          icon: "$error",
        },
        5000
      );
    }
  } catch (error: any) {
    console.error("Error fetching data from API:", error);
    globalStore.setToastInfor(
      {
        title: translateMessage("common.msg_notification"),
        text: error.response.data.message,
        border: "start",
        borderColor: "white",
        type: "error",
        icon: "$error",
      },
      5000
    );
  }
};

onMounted(() => {
  fetchDataFromApi();
});
</script>

<template>
  <div v-if="loading === true">
    <h1
      class="mt-3 text-3xl font-extrabold tracking-tight text-slate-900 add-todo"
      @click="showModal"
    >
      {{ props.name }}
      <span class="mdi mdi-pencil-plus"></span>
    </h1>
    <p>
      {{ description }}
    </p>
  </div>
  <div v-else>
    <cf-spinner indeterminate color="pink"> </cf-spinner>
  </div>
</template>

<style scoped>
h1.add-todo:hover {
  cursor: pointer;
}
</style>
