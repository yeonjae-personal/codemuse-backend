<!-- eslint-disable vue/multi-word-component-names -->
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
            <BaseInputText
              v-if="isEditRule && item.key === 'ruleName'"
              v-model.trim="ruleDetail[item.key]"
              class="node-name"
              :rules="useInputValidation({ maxLength: 50 })"
              :maxlength="50"
              required
              counter
              @update:model-value="handleChangeDetail"
            />
            <v-switch
              v-else-if="isEditRule && item.key === 'useYn'"
              v-model="ruleDetail[item.key]"
              hide-details
              color="#D9325A"
              base-color="#E6E9ED"
              inset
              density="compact"
              @update:model-value="handleChangeDetail"
            />
            <v-switch
              v-else-if="item.key === 'useYn'"
              v-model="ruleDetail[item.key]"
              hide-details
              color="#FDCED5"
              base-color="#E6E9ED"
              inset
              density="compact"
              :readonly="true"
            />
            <span v-else>
              {{ ruleDetail[item.key] || "-" }}
            </span>
          </div>
        </template>
      </DetailPaneRow>
    </DetailPane>
    <DetailPane>
      <DetailPaneRow is-overview :label="t('product_platform.overview')">
        <template #value="{ overViewKlass, overtTextKlass }">
          <BaseTextArea
            v-if="isEditRule"
            v-model="ruleDetail.overview"
            :class="overtTextKlass"
            :rules="{ maxLength: 500 }"
            :maxlength="500"
            counter
          />
          <span v-else :class="overViewKlass">{{ ruleDetail.overview }}</span>
        </template>
      </DetailPaneRow>
    </DetailPane>
  </div>
</template>
<script setup lang="ts">
import BaseInputText from "@/components/prod/common/BaseInputText.vue";
import BaseTextArea from "@/components/prod/common/BaseTextArea.vue";
import DetailPane from "@/components/prod/layout/DetailPane.vue";
import DetailPaneRow from "@/components/prod/layout/DetailPaneRow.vue";
import { useInputValidation } from "@/composables/useInputValidation";
import useRuleEngineStore from "@/store/admin/ruleEngine.store";
import { useI18n } from "vue-i18n";

const { t } = useI18n();
const ruleEngineStore = useRuleEngineStore();
const { updateRuleItem } = ruleEngineStore;
const { ruleDetail, isEditRule } = storeToRefs(ruleEngineStore);

const infoItems = computed(() => [
  {
    label: t("product_platform.name"),
    key: "ruleName",
  },
  {
    label: t("product_platform.dashboard.responsibleDept"),
    key: "department",
  },
  {
    label: t("product_platform.dashboard.responsibleUser"),
    key: "user",
  },
  {
    label: t("product_platform.creationDate"),
    key: "creationDate",
  },
  {
    label: t("product_platform.useYn"),
    key: "useYn",
  },
]);

const handleChangeDetail = () => {
  updateRuleItem(
    ruleDetail.value.ruleId,
    ruleDetail.value.ruleName,
    ruleDetail.value.useYn
  );
};
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
        color: #3a3b3d;
        display: flex;
        align-items: center;
      }
    }
    .text-overview {
      font-size: 11px;
      color: #3a3b3d;
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

:deep(.v-switch--inset .v-switch__track) {
  height: 20px;
  min-width: 36px;
  opacity: 1;
}
:deep(.v-switch--inset .v-switch__thumb) {
  height: 16px;
  width: 16px;
  left: 2px;
  right: unset;
  transform: none;
}
:deep(.v-switch--inset .v-selection-control--dirty .v-switch__thumb) {
  left: unset;
  right: 2px;
}
</style>
