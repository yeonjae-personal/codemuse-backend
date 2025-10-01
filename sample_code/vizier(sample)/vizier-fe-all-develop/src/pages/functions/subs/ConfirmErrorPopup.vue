<script setup lang="ts">
const props = defineProps<{
  step: number;
  showErr: Boolean;
  mes: string;
}>();

const emits = defineEmits(["update:showErr", "confirm"]);

const dialogErr = ref(props.showErr);

const confirm = () => {
  emits("confirm");
  emits("update:showErr", false);
};

watch(
  () => props.showErr,
  (newVal) => {
    dialogErr.value = newVal;
  }
);
</script>
<template>
  <v-dialog v-model="dialogErr" max-width="300" persistent>
    <v-card class="!p-4 rounded-lg">
      <v-card-text class="!pt-7"
        >{{ mes }}</v-card-text
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
