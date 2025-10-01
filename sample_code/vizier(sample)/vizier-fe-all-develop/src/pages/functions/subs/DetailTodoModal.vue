<script setup lang="ts">
import { useGlobal, useUser } from "@/store";
import CfButton from "@/components/controls/CfButton.vue";
import UpdateTodoModal from "./UpdateTodoModal.vue";
import { CommonUtil } from "@/utils/common-util";
import { httpClient } from "@/utils/http-common";

const props = defineProps({
  data: {
    type: Object,
    default: null,
  },
});

// #region Define Store
const userStore = useUser();
const globalStore = useGlobal();

const user = computed(() => {
  return userStore.user;
});

const title = ref(props.data.title);
const description = ref(props.data.description);
const beforeData = ref(props.data);
const afterData = ref(props.data);

// #region Define events
const { translateMessage } = CommonUtil.useTranslatedMessage();

const deleteTodo = async () => {
  const objectAlert: any = {
    title: translateMessage("common.msg_confirm"),
    text: translateMessage("todos.msg_confirm_delete"),
    width: "400",
  };
  const result = await globalStore.openAlertConfirm(objectAlert);
  if (result) {
    try {
      const response = await httpClient.delete(
        `todos/delete/${user.value.id}/${props.data.id}`
      );
      globalStore.setToastInfor(
        {
          title: translateMessage("common.msg_notification"),
          text: translateMessage("todos.msg_success_delete"),
          border: "start",
          borderColor: "white",
          type: "success",
          icon: "$success",
        },
        5000
      );
      emit("closeDialog", response.data);
    } catch (error: any) {
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
  }
};

const editTodo = async () => {
  const objectModal: any = {
    title: translateMessage("todos.lbl_title_modal_update"),
    component: UpdateTodoModal,
    dataInput: afterData,
    width: "800",
  };
  const response = await globalStore.openModal(objectModal);
  if (response) {
    title.value = response.data.title;
    description.value = response.data.description;
    afterData.value = response.data;
  }
};

const emit = defineEmits(["closeDialog"]);
const closeModal = () => {
  if (beforeData.value !== afterData.value) {
    emit("closeDialog", afterData);
  } else {
    emit("closeDialog");
  }
};
</script>
<template>
  <div class="mx-auto sm:rounded-md cf-modal">
    <div class="flex flex-col w-100">
      <article class="prose prose-slate">
        <div class="flex flex-col gap-4">
          <div class="flex justify-between items-center gap-4">
            <h3>{{ title }}</h3>
            <h3>{{ user.username }}</h3>
          </div>
          <div>
            <span>{{ description }}</span>
          </div>
        </div>
        <div class="flex gap-4 mt-10">
          <cf-button
            :label="$t('common.btn_delete')"
            rounded="xl"
            @click="deleteTodo"
          />
          <cf-button
            :label="$t('common.btn_edit')"
            rounded="xl"
            @click="editTodo"
          />
          <cf-button
            :label="$t('common.btn_close')"
            rounded="xl"
            @click="closeModal"
          />
        </div>
      </article>
    </div>
  </div>
</template>

<style scoped>
.cf-modal h3 {
  margin: 0px;
}
</style>
