<template>
  <div class="relative">
    <slot></slot>
    <ValidationDialog
      :show-custom-validate="showCustomValidate"
      :attr="attr"
      :pass-data="passData"
      :is-add="isAdd"
      :is-duplicate="isDuplicate"
    />
  </div>
</template>
<script lang="ts" setup>
import ValidationDialog from "./ValidationDialog.vue";

type Props = {
  showCustomValidate?: boolean;
  attr: string;
  passData: any;
  isAdd: boolean;
  isDuplicate?: boolean;
};

defineProps<Props>();
</script>
