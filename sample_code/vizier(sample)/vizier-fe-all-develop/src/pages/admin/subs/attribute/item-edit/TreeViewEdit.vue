<template>
  <div class="general-wrapper">
    <DetailPane>
      <DetailPaneRow
        v-for="item in infoItems"
        :key="item.key"
        :label="item.label"
      >
        <template #value="{ klass }">
          <div :class="klass">
            <span v-if="item.key !== 'nodeName'">
              {{ itemDetail.treeViewInfo[item.key] }}
            </span>
            <BaseInputText
              v-else
              v-model.trim="itemDetail.treeViewInfo[item.key]"
              class="node-name"
              :rules="useInputValidation({ maxLength: 30 })"
              :maxlength="30"
              required
              counter
            />
          </div>
        </template>
      </DetailPaneRow>
    </DetailPane>
    <div class="general-info gap-[6px]">
      <div class="info-item !h-[20px]">
        <div class="item-key !text-[#3A3B3D]">
          {{ t("product_platform.overview") }}
        </div>
      </div>
      <div class="item-value">
        <BaseTextArea
          v-model="itemDetail.treeViewInfo.overview"
          class="mt-[6px]"
          locomotive-content-class="px-[12px]"
          :rules="{ maxLength: 500 }"
          :maxlength="500"
          counter
        />
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import BaseInputText from "@/components/prod/common/BaseInputText.vue";
import { useInputValidation } from "@/composables/useInputValidation";
import attributeManagementStore from "@/store/admin/attributeManagement.store";
import { useI18n } from "vue-i18n";

const { itemDetail } = storeToRefs(attributeManagementStore());

const { t } = useI18n();

const infoItems = computed(() => [
  {
    label: t("product_platform.nodeName"),
    key: "nodeName",
  },
  {
    label: t("product_platform.dashboard.responsibleDept"),
    key: "responsibleDept",
  },
  {
    label: t("product_platform.dashboard.responsibleUser"),
    key: "responsibleUser",
  },
  {
    label: t("product_platform.creationDate"),
    key: "creationDate",
  },
]);
</script>
<style lang="scss" scoped>
.general-wrapper {
  display: flex;
  flex-direction: column;
  row-gap: 12px;
  height: calc(100% - 180px);

  .general-info {
    background-color: #f7f8fa;
    display: flex;
    flex-direction: column;
    padding: 12px;
    border-radius: 12px;
    row-gap: 8px;

    .info-item {
      display: flex;
      padding: 6px 0;
      height: 32px;

      .item-key {
        flex: 1;
        padding-right: 8px;
        text-align: left;
        font-size: 13px;
        font-weight: 500;
        color: #6b6d70;
        display: flex;
        align-items: center;
      }

      .item-value {
        flex: 2;
        font-size: 13px;
        font-weight: 500;
        color: #3a3b3d;
        display: flex;
        align-items: center;
      }
    }
  }
}

:deep(.custom-text-field .v-field__field) {
  height: 32px;
}

:deep(.custom-text-field .v-field) {
  height: 32px;
}

:deep(.custom-text-field .v-field__input) {
  padding: 0 0 0 16px;
  height: 38px;
  min-height: 38px;
}

:deep(.custom-text-field .v-input__control) {
  height: 32px;
}

:deep(.custom-textarea) {
  margin: 0;
}

:deep(.node-name .v-input--error:hover .v-input__details) {
  bottom: -29px;
}

:deep(.counter:hover .v-input__details) {
  bottom: -32px;
}

:deep(.node-name .v-input--error:hover::before),
:deep(.counter:hover::before) {
  top: auto;
  bottom: -9px;
}
</style>
