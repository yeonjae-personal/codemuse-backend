<template>
  <div class="category-information">
    <DetailPane is-not-rounded-bottom>
      <DetailPaneRow :label="$t(`product_platform.nodeName`)">
        <template #value="{ klass }">
          <div :class="klass" class="layout-node-name">
            <BaseValidationInputText
              v-if="isEditMode"
              v-model="selectedCategoryTreeNode.ctgrNodeName"
              :rules="{
                required: true,
                maxLength: 30,
              }"
              :maxlength="30"
              styles="catalog-input"
              @update:model-value="handleUpdateNodeName"
            />
            <p v-else class="category-information-item-value">
              {{ item.ctgrNodeName || "-" }}
            </p>
          </div>
        </template>
      </DetailPaneRow>
      <DetailPaneRow
        :label="$t(`product_platform.chgDeptName`)"
        :value="item.chgDeptName || '-'"
        :tooltip-content="$t(`product_platform.chgDeptName`)"
      />
      <DetailPaneRow
        :label="$t(`product_platform.chgUser`)"
        :value="item.chgUser || '-'"
      />
      <DetailPaneRow
        :label="$t(`product_platform.creationDate`)"
        :value="
          item.rgstDtm ? formatDateWithOutSeconds(item.rgstDtm) || '-' : '-'
        "
      />
    </DetailPane>
    <DetailPane is-not-rounded-top>
      <DetailPaneRow is-overview :label="$t('product_platform.bsfOvwCntn')">
        <template #value="{ overViewKlass, overtTextKlass }">
          <BaseTextArea
            v-if="isEditMode"
            v-model="selectedCategoryTreeNode.ctgrOvwCntn"
            :class="overtTextKlass"
            @update:model-value="handleUpdateNodeName"
          />
          <p v-else :class="overViewKlass">
            {{ item.ctgrOvwCntn || "-" }}
          </p>
        </template>
      </DetailPaneRow>
    </DetailPane>
  </div>
</template>

<script setup lang="ts">
import { formatDateWithOutSeconds } from "@/utils/format-data";
import { useCategoryStore } from "@/store";
import DetailPane from "@/components/prod/layout/DetailPane.vue";

defineProps({
  item: {
    type: Object,
    default: null,
  },
});

const categoryStore = useCategoryStore();

const isEditMode = computed(() => categoryStore.getIsEdit);
const selectedCategoryTreeNode = computed(
  () => categoryStore.getSelectedCategoryTreeNode
);
const handleUpdateNodeName = () => {
  categoryStore.setSelectedCategoryTreeNodeProperty(true);
};
</script>

<style scoped>
:deep(.v-card-text) {
  padding: unset !important;
}
.category-content-content-text {
  margin-bottom: 14px;
  margin-top: 14px;
}

.category-information-item {
  display: flex;
  align-items: center;
}

.category-information-item-label,
.category-information-item-value {
  font-size: 13px;
  line-height: 19.5px;
  letter-spacing: 0.25px;
  text-align: left;
  text-align: left;
  color: #6b6d70;
}
.category-information-item-label {
  width: 40%;
  font-weight: 500;
}

.category-information-item-value {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  width: 60%;
  font-weight: 400;
}

.category-detail-section {
  padding-left: 24px;
  display: flex;
  flex-direction: column;
  position: relative;
  margin-left: -10px;
}

.hide-button {
  position: absolute;
  top: 30%;
  left: -3px;
  height: 54px;
  width: 20px;
  border-bottom-right-radius: 40px;
  border-top-right-radius: 40px;
  display: flex;
  background: #5254571f;
  align-items: center;
  justify-content: center;
}

.hide-button:hover {
  cursor: pointer;
}

.hide-button-content {
  color: #f7f8fa;
}

.category-section-title-text {
  font-size: 15px;
  font-weight: 500;
  line-height: 24px;
  letter-spacing: 0.5px;
  text-align: left;
  color: #3a3b3d;
}

.category-overview-summary-content-text {
  color: #3a3b3d;
  font-size: 11px;
  font-weight: 400;
  line-height: 16.5px;
  letter-spacing: 0.25px;
  text-align: left;
}

.category-overview-header-content-text {
  font-size: 14px;
  font-weight: 500;
  line-height: 21px;
  letter-spacing: 0.25px;
  text-align: left;
  color: #3a3b3d;
}

.category-section-title {
  height: 72px;
  padding: 32px 8px 16px 8px;
}

.category-overview-content-text {
  background-color: #f7f8fa;
  padding: 12px 16px 20px 16px;
  border-bottom-left-radius: 12px;
  border-bottom-right-radius: 12px;
}

.category-overview-header-text {
  font-size: 13px;
  font-weight: 500;
  line-height: 19.5px;
  letter-spacing: 0.25px;
  text-align: left;
}

.category-information {
  display: flex;
  flex-direction: column;
  padding: 0px 4px;
  gap: 8px;
  height: calc(100vh - 340px);
  overflow-y: auto;
  scrollbar-width: thin;
}

.category-information-content-container {
  border-top-left-radius: 12px;
  border-top-right-radius: 12px;
  background-color: #f7f8fa;
  padding: 20px 16px;
}

.v-tab {
  text-transform: none !important;
}

.category-option-tab-item {
  color: #bdc1c7;
  border-bottom: 1px solid #bdc1c7;
}

.category-option-tab-item .v-btn__content {
  font-size: 13px;
  font-weight: 500;
  line-height: 19.5px;
  letter-spacing: 0.25px;
  text-align: center;
}

.result-items {
  height: calc(100vh - 520px);
}

:deep(.layout-node-name .v-input__control) {
  height: 30px !important;
}
:deep(.layout-node-name .v-field) {
  height: 30px !important;
}
:deep(.layout-node-name .v-field__field) {
  height: 30px !important;
}
:deep(.v-textarea .v-field__input) {
  -webkit-mask-image: unset;
}
</style>
