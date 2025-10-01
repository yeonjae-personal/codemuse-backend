<script setup lang="ts">
import { useGlobal } from "@/store";

const props = defineProps({
  modal: {
    type: Object,
    default: null,
  },
  index: {
    type: Number,
    default: 0,
  },
});

const globalStore = useGlobal();

const localShow = ref(props.modal.show);

watch(
  () => props.modal.show,
  (newVal) => {
    localShow.value = newVal;
  }
);

const closeDialog = (data?: any) => {
  globalStore.closeModal(props.index, data);
};
</script>

<template>
  <v-dialog
    v-model="localShow"
    :width="props.modal.width"
    :persistent="true"
    @click:outside="closeDialog"
  >
    <v-card v-if="props.modal.type" class="type-custom-card">
      <div style="background: #e3e3e3">
        <div class="flex justify-between" style="width: 95%">
          <v-card-title class="type-custom-title">{{
            props.modal.title
          }}</v-card-title>
          <button
            v-if="props.modal.iconClose"
            class="close-button"
            @click="closeDialog"
          >
            <span class="mdi mdi-window-close mdi-24px"></span>
          </button>
        </div>
      </div>
      <v-card-text>
        <component
          :is="props.modal.component"
          v-if="props.modal.component"
          :data="props.modal.dataInput"
          @close-dialog="closeDialog"
        />
      </v-card-text>
    </v-card>
    <v-card v-else>
      <v-card-title>{{ props.modal.title }}</v-card-title>
      <v-card-text>
        <component
          :is="props.modal.component"
          v-if="props.modal.component"
          :data="props.modal.dataInput"
          @close-dialog="closeDialog"
        />
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<style scoped>
.type-custom-card {
  border-radius: 8px !important;
}
.type-custom-title {
  font-size: 24px;
  font-weight: 600;
  padding: 27px 0 27px 54px;
}
</style>
