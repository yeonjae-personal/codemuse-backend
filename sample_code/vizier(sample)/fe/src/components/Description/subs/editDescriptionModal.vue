<script setup lang="ts">
import CfButton from "../../../components/controls/CfButton.vue";
import CfTextarea from "../../../components/controls/CfTextarea.vue";
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

const description = ref(props.data.data.value);

const handleModelUpdateDescription = (value: string) => {
  description.value = value;
};

const emit = defineEmits(["closeDialog"]);
const closeModal = () => {
  emit("closeDialog");
};

const SaveModal = async () => {
  try {
    if (props.data.data.value !== description.value) {
      const requestBody = {
        description: description.value,
        updatedBy: userStore.user.id,
      };
      await httpClient.put(`descriptions/${props.data.data.name}`, requestBody);
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
      emit("closeDialog", description.value);
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
  <div class="py-2">
    <cf-textarea
      :label="$t('common.area_edit_description')"
      variant="outlined"
      :model="description"
      @update:model="handleModelUpdateDescription"
    ></cf-textarea>
    <div class="flex flex-row-reverse gap-2">
      <cf-button
        :label="$t('common.btn_close')"
        rounded="xl"
        @click="closeModal"
      />
      <cf-button
        :label="$t('common.btn_save')"
        rounded="xl"
        @click="SaveModal"
      />
    </div>
  </div>
</template>

<style scoped></style>
