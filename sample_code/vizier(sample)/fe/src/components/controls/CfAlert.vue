<script setup lang="ts">
import { useGlobal } from "@/store";

const props = defineProps({
  data: {
    type: Object,
    default: null,
  },
});

const globalStore = useGlobal();

const localShow = ref(props.data.show);

watch(
  () => props.data.show,
  (newVal) => {
    localShow.value = newVal;
  }
);

const closeAlertConfirm = (result: boolean) => {
  globalStore.closeAlertConfirm(result);
};
</script>

<template>
  <v-dialog v-model="localShow" :width="props.data.width" :persistent="true">
    <div v-if="props.data.isAlert">
      <v-card>
        <v-card-item v-if="props.data.title">
          <v-card-title>{{
            props.data.title || $t("common.msg_notification")
          }}</v-card-title>
        </v-card-item>
        <v-card-text v-if="props.data.text">
          {{ props.data.text }}
        </v-card-text>
        <v-card-actions class="justify-end">
          <cf-button
            :label="$t('common.btn_ok')"
            rounded="xl"
            @click="closeAlertConfirm(true)"
          ></cf-button>
        </v-card-actions>
        <slot></slot>
      </v-card>
    </div>
    <div v-else>
      <v-card
        v-if="props.data.action"
        :height="props.data.height"
        class="rounded-lg"
      >
        <v-card-item v-if="props.data.title">
          <v-card-title>{{
            props.data.title || $t("common.msg_confirm")
          }}</v-card-title>
        </v-card-item>
        <v-card-text v-if="props.data.text" class="type-custom-title">
          <v-card-text v-if="props.data.text" class="type-custom-title1">
            {{ props.data.text }}
          </v-card-text>
        </v-card-text>
        <v-card-actions class="action-custom-card justify-end">
          <cf-button
            class="custom-btn w-[90px] p-8"
            :label="$t('common.btn_ok')"
            rounded="lg"
            @click="closeAlertConfirm(true)"
          ></cf-button>
          <cf-button
            class="custom-btn w-[90px] p-8"
            :label="$t('common.btn_cancel')"
            rounded="lg"
            @click="closeAlertConfirm(false)"
          ></cf-button>
        </v-card-actions>
        <slot></slot>
      </v-card>
      <v-card v-else>
        <v-card-item v-if="props.data.title">
          <v-card-title>{{
            props.data.title || $t("common.msg_confirm")
          }}</v-card-title>
        </v-card-item>
        <v-card-text v-if="props.data.text">
          {{ props.data.text }}
        </v-card-text>
        <v-card-actions class="justify-end">
          <cf-button
            :label="$t('common.btn_ok')"
            rounded="xl"
            @click="closeAlertConfirm(true)"
          ></cf-button>
          <cf-button
            :label="$t('common.btn_cancel')"
            rounded="xl"
            @click="closeAlertConfirm(false)"
          ></cf-button>
        </v-card-actions>
        <slot></slot>
      </v-card>
    </div>
  </v-dialog>
</template>

<style scoped>
.custom-btn {
  color: #000000 !important;
  background: #ffffff;
  border: 1px solid #828282;
  margin-top: 2px;
}

.type-custom-card {
  border-radius: 8px !important;
}
.type-custom-title {
  /* background: #f5f5f5; */
  color: #000000;
  font-size: 24px;
  /* text-align: center; */
  height: 61px !important;
  padding: 16px !important;
  font-weight: 600;
  background: #ffffff;
  /* padding: 10px 0 10px 0px; */
}
.type-custom-title1 {
  height: 61px !important;
  width: 620px;
  background: #f5f5f5;
  margin: 55px 34px 0 34px;
  line-height: 22px;
  font-size: 20px;
  font-weight: 500;
  padding-top: 22px;
}

.--v-btn-height {
  height: 46px !important;
}

.v-card-actions :deep(.v-btn.v-btn--density-default) {
  height: 46px;
}
.v-card-actions {
  margin-left: 3%;
  margin-right: 3%;
  margin-bottom: 10px;
}
</style>
