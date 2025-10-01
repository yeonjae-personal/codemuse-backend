<script setup lang="ts">
import { httpClient } from "@/utils/http-common";
import { useGlobal, useUser } from "@/store";

import CfTextarea from "@/components/controls/CfTextarea.vue";
import CfButton from "@/components/controls/CfButton.vue";
import { CommonUtil } from "@/utils/common-util";

// #region Define Store
const userStore = useUser();
const globalStore = useGlobal();

const user = computed(() => {
  return userStore.user;
});

// #region Define init value
const formAddTodo = ref<any>(null);
const title = ref("");
const description = ref("");

// #region Define validate
const titleRules = ref([
  (value: string) => {
    if (value) return true;
    return translateMessage("todos.msg_todo_title_required");
  },
]);

// #region Define events
const { translateMessage } = CommonUtil.useTranslatedMessage();

const titleChangeHandle = (val: string) => {
  title.value = val;
};

const descriptionChangeHandle = (val: string) => {
  description.value = val;
};

const saveTodo = async () => {
  const { valid } = await formAddTodo.value.validate();
  if (valid) {
    try {
      const requestBody = {
        title: title.value,
        description: description.value,
      };
      const response = await httpClient.post(
        `todos/create/${user.value.id}`,
        requestBody
      );
      globalStore.setToastInfor(
        {
          title: translateMessage("common.msg_notification"),
          text: translateMessage("todos.msg_success_create"),
          border: "start",
          borderColor: "white",
          type: "success",
          icon: "$success",
        },
        5000
      );
      emit("closeDialog", response.data.data);
    } catch (err: any) {
      globalStore.setToastInfor(
        {
          title: translateMessage("common.msg_notification"),
          text: err.response.data.message,
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
          <div class="flex justify-between items-center gap-4">
            <cf-input
              :label="$t('todos.lbl_todo_title')"
              variant="underlined"
              :rules="titleRules"
              :model="title"
              @update:model="titleChangeHandle"
              @keydown.enter.prevent=""
            ></cf-input>
            <span>{{ user.username }}</span>
          </div>
          <div>
            <cf-textarea
              :label="$t('todos.lbl_todo_description')"
              :model="description"
              variant="solo"
              @update:model="descriptionChangeHandle"
            ></cf-textarea>
          </div>
        </div>
        <div class="flex gap-4">
          <cf-button
            :label="$t('common.btn_save')"
            rounded="xl"
            @click="saveTodo"
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
