const cfAlertExample = `<script setup lang="ts">
import { useGlobal } from '@/store';
import { CommonUtil } from '@/utils/common-util';
import CfButton from '@/components/controls/CfButton.vue';

const globalStore = useGlobal();

const button = ref('');

const { translateMessage } = CommonUtil.useTranslatedMessage();

const showAlertConfirm = async () => {
  const objectAlert: any = {
    title: translateMessage('common.msg_confirm'),
    text: translateMessage('todos.msg_confirm_delete'),
    width: '400',
  };
  const result = await globalStore.openAlertConfirm(objectAlert);
  if (result) {
    button.value = 'OK';
  } else {
    button.value = 'Cancel';
  }
};
</script>
<template>
  <cf-button label="Show Alert Confirm" @click="showAlertConfirm"></cf-button>

  <span v-if="button" class="ml-4">You choose button: {{ button }}</span>
</template>
`;
export default cfAlertExample;
