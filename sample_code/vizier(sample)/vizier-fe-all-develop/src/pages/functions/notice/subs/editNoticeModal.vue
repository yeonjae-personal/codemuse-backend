<script setup lang="ts">
import { CommonUtil } from "@/utils/common-util";
import { httpClient } from "@/utils/http-common";
import { useUser, useGlobal } from "@/store";

const userStore = useUser();
const globalStore = useGlobal();

const props = defineProps({
  data: {
    type: Object,
    default: null,
  },
});

const rules = ref({
  required: (value) => !!value || "Field is required",
});

const exampleRules = ref([
  (val) => !!val || "This field is required",
  (val) => (val.length && val.length <= 150) || "Max 150 keywords is required",
  (val) => (val.length && val.length >= 20) || "Min 20 keywords is required",
]);

const detail = ref(props.data.data.detail);
const title = ref(props.data.data.title);

const handleTitleModelUpdate = (value: string) => {
  title.value = value;
};
const handleDetailModelUpdate = (value: string) => {
  detail.value = value;
};

const emit = defineEmits(["closeDialog"]);
const closeModal = () => {
  emit("closeDialog");
};

const SaveModal = async () => {
  try {
    if (
      props.data.data.title !== title.value ||
      props.data.data.detail !== detail.value
    ) {
      if (title.value.length >= 20 || title.value.length <= 150) {
        const requestBody = {
          title: title.value,
          detail: detail.value,
        };
        if (!props.data.data.id) {
          await httpClient.post(
            `notices/add/${userStore.user.id}`,
            requestBody
          );
        } else {
          await httpClient.put(
            `notices/update/${props.data.data.id}/${userStore.user.id}`,
            requestBody
          );
        }
        globalStore.setToastInfor(
          {
            title: translateMessage("common.msg_notification"),
            text: translateMessage("common.msg_success_updated_description"),
            border: "start",
            borderColor: "white",
            type: "success",
            icon: "$success",
          },
          5000
        );
        emit("closeDialog", [
          {
            title: title.value,
            detail: detail.value,
          },
        ]);
      } else {
        globalStore.setToastInfor(
          {
            title: translateMessage("common.msg_notification"),
            text: "Tile is must between 20 and 50 characters",
            border: "start",
            borderColor: "white",
            type: "error",
            icon: "$error",
          },
          5000
        );
      }
    } else {
      globalStore.setToastInfor(
        {
          title: translateMessage("common.msg_notification"),
          text: translateMessage("common.msg_inform_update"),
          border: "start",
          borderColor: "white",
          type: "error",
          icon: "$error",
        },
        5000
      );
    }
  } catch (error: any) {
    console.error("Error saving data:", error.response.data.message);
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
const { translateMessage } = CommonUtil.useTranslatedMessage();
</script>

<template>
  <div v-if="userStore.user.level === 'Master'" class="py-2">
    <cf-input
      label="Title"
      :model="title"
      :rules="exampleRules"
      class="mb-1"
      @update:model="handleTitleModelUpdate"
    >
    </cf-input>
    <cf-textarea
      :model="detail"
      label="Detail"
      variant="outlined"
      :rules="[rules.required]"
      @update:model="handleDetailModelUpdate"
    ></cf-textarea>
    <div class="flex flex-row-reverse gap-2">
      <cf-button
        label="Close"
        rounded="xl"
        class="w-[80px]"
        @click="closeModal"
      />
      <cf-button
        label="Save"
        rounded="xl"
        class="w-[80px]"
        @click="SaveModal"
      />
    </div>
  </div>
</template>

<style scoped></style>
