<script setup lang="ts">
import Prism from "vue-prism-component";
import { useGlobal } from "@/store";
import { CommonUtil } from "@/utils/common-util";
import CfButton from "@/components/controls/CfButton.vue";
import cfAlertExample from "@/components/controls/examples/CfAlertExample";
// import Description from "@//components/Description/index.vue";

const sourceCode = ref(cfAlertExample);

const globalStore = useGlobal();

const button = ref("");

const { translateMessage } = CommonUtil.useTranslatedMessage();

const showAlertConfirm = async () => {
  const objectAlert: any = {
    title: translateMessage("common.msg_confirm"),
    text: translateMessage("todos.msg_confirm_delete"),
    width: "400",
  };
  const result = await globalStore.openAlertConfirm(objectAlert);
  if (result) {
    button.value = "OK";
  } else {
    button.value = "Cancel";
  }
};
</script>
<template>
  <div class="p-4 mx-auto prose md:px-6 prose-indigo sm:rounded-md">
    <!-- <description name="Alert" /> -->

    <h2 class="truncate text-base font-medium leading-7 text-slate-900">
      Examples
    </h2>

    <cf-button label="Show Alert Confirm" @click="showAlertConfirm"></cf-button>

    <span v-if="button" class="ml-4">You choose button: {{ button }}</span>

    <h2 class="truncate text-base font-medium leading-7 text-slate-900">
      Source Code
    </h2>
    <prism language="html">{{ sourceCode }}</prism>
  </div>
</template>
