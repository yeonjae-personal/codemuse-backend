<script setup lang="ts">
const props = defineProps<{
  step: number;
  data: string;
  showDialog: Boolean;
}>();

const emits = defineEmits(["update:showDialog", "confirm"]);

const dialog = ref(props.showDialog);

const confirm = () => {
  emits("confirm");
  emits("update:showDialog", false);
};

watch(
  () => props.showDialog,
  (newVal) => {
    dialog.value = newVal;
  }
);
</script>
<template>
  <v-dialog v-model="dialog" max-width="215" persistent>
    <v-card class="!p-4 rounded-lg">
      <v-card-text v-if="step === 1" class="!pt-7 text-center"
        >고객 정보 등록이<br />완료되었습니다.</v-card-text
      >
      <v-card-text v-if="step === 2" class="!pt-7 !pr-0 !pl-0 text-center"
        >납부/청구 정보 등록이 완료되었습니다.</v-card-text
      >
      <v-card-text v-if="step === 3" class="!pt-7 !pl-[20px] text-center"
        >유치정보 저장 API</v-card-text
      >
      <v-card-text v-if="step === 4" class="!pt-7 text-center"
        >전화번호 채번이<br />완료되었습니다.</v-card-text
      >
      <v-card-text v-if="step === 8" class="!pt-7 text-center"
        >신규가입이<br />완료되었습니다.<br /><br />상품번호 :
        {{ data }}</v-card-text
      >
      <v-card-actions class="w-full">
        <v-spacer></v-spacer>
        <v-btn class="w-full !bg-[#B2CEE2] !text-[#2A2A2A]" @click="confirm"
          >완료</v-btn
        >
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<style scoped></style>
