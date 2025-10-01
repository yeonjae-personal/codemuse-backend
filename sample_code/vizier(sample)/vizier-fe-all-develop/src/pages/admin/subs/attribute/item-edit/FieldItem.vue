<template>
  <div class="field-wrapper">
    <div
      class="field-header"
      :class="{ expaned: item.isExpaned, disabled: !item.isUsed }"
      @click="handleExpaned"
    >
      <span class="field-sort">{{ props.item.sort }}</span>
      <span class="field-label">{{
        props.item.labelId ? t(item.labelId) : ""
      }}</span>
    </div>
    <div v-if="props.item.isExpaned" class="field-content">
      <div class="field-item">
        <div class="field-label">{{ t("product_platform.label_id") }}</div>
        <div class="field-value">{{ props.item.labelId }}</div>
      </div>
      <div class="field-item">
        <div class="field-label">{{ t("product_platform.field_type") }}</div>
        <div class="field-value">{{ props.item.fieldType }}</div>
      </div>
      <div v-if="isShowAttributeCode" class="field-item">
        <div class="field-label">
          {{ t("product_platform.attribute_code") }}
        </div>
        <div class="field-value">{{ props.item.attributeCode }}</div>
      </div>
      <div v-if="isShowMaxLength" class="field-item">
        <div class="field-label">{{ t("product_platform.max_length") }}</div>
        <div class="field-value">{{ props.item.maxLength }}</div>
      </div>
      <div class="field-item">
        <div class="field-label">{{ t("product_platform.disp_tab") }}</div>
        <div class="field-value">{{ dispTab }}</div>
      </div>
      <div class="field-item-custom">
        <div class="field-item">
          <div class="field-label-custom">
            {{ t("product_platform.mandatory") }}
          </div>
          <div class="field-value-custom">
            <v-switch
              v-model="fieldItem.isRequired"
              hide-details
              color="#FDCED5"
              base-color="#E6E9ED"
              inset
              density="compact"
              :readonly="true"
            />
          </div>
        </div>
        <div class="field-item">
          <div class="field-label-custom">
            {{ t("product_platform.advanced_search_y_n") }}
          </div>
          <div class="field-value-custom">
            <v-switch
              v-model="fieldItem.isAdvancedSearch"
              hide-details
              color="#FDCED5"
              base-color="#E6E9ED"
              inset
              density="compact"
              :readonly="true"
            />
          </div>
        </div>
      </div>
      <div class="field-item-custom">
        <div class="field-item">
          <div class="field-label-custom">
            {{ t("product_platform.useYn") }}
          </div>
          <div class="field-value-custom">
            <v-switch
              v-model="fieldItem.isUsed"
              hide-details
              color="#FDCED5"
              base-color="#E6E9ED"
              inset
              density="compact"
              :readonly="true"
            />
          </div>
        </div>
        <div v-if="isShowDispCard" class="field-item field-custom">
          <div class="field-label">
            {{ t("product_platform.disp_card_y_n") }}
          </div>
          <div class="field-value-custom">
            <v-switch
              v-model="fieldItem.isDispCard"
              hide-details
              color="#FDCED5"
              base-color="#E6E9ED"
              inset
              density="compact"
              :readonly="true"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { useI18n } from "vue-i18n";
import attributeManagementStore, {
  IAdditionalItem,
} from "@/store/admin/attributeManagement.store";

const { updateIsExpaned } = attributeManagementStore();
const { itemDetail } = storeToRefs(attributeManagementStore());

const props = defineProps({
  item: {
    type: Object as PropType<IAdditionalItem>,
    default: () => ({}),
  },
});

const { t } = useI18n();

const fieldItem = ref<IAdditionalItem>(props.item);

const dispTab = computed(() => {
  return props.item.dispTab === "A"
    ? t("product_platform.additional_tab")
    : t("product_platform.general_tab");
});

const isShowAttributeCode = computed(() => {
  return (
    props.item.fieldType && ["DM", "DL", "OB"].includes(props.item.fieldType)
  );
});

const isShowMaxLength = computed(() => {
  return (
    props.item.fieldType &&
    !["DP", "DM", "DL", "OB"].includes(props.item.fieldType)
  );
});

const isShowDispCard = computed<boolean>(
  () => itemDetail.value.generalInfo.item === "Offer"
);

watch(
  () => props.item,
  (value) => {
    fieldItem.value = value;
  },
  { deep: true }
);

const handleExpaned = () => {
  updateIsExpaned(props.item.id);
};
</script>
<style lang="scss" scoped>
.field-wrapper {
  width: calc(100% - 2px);
  min-height: 48px;
  border-radius: 16px;
  border: 1px solid #dce0e5;
  background: #ffffff;
  .field-header {
    background: #fff;
    padding: 12px;
    height: 48px;
    display: flex;
    align-items: center;
    column-gap: 8px;
    border-radius: 16px;
    transition: all 0.2s linear;
    cursor: pointer;

    &.disabled {
      background-color: #e9ebf0;
    }

    &.expaned {
      border-radius: 16px 16px 0 0 !important;
      background: #f7f8fa;
    }

    .field-sort {
      width: 24px;
      height: 24px;
      border-radius: 50%;
      background-color: #fff0f2;
      display: flex;
      justify-content: center;
      align-items: center;
      font-weight: 500;
      font-size: 11px;
      color: #ba1642;
    }
    .field-label {
      font-weight: 500;
      font-size: 13px;
      display: flex;
      align-items: center;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      flex: 1;
      color: #3a3b3d;
    }
    .field-button {
      width: 20px;
      height: 20px;
    }
  }

  .field-content {
    flex: 1;
    padding: 12px 16px;
    display: flex;
    flex-direction: column;
    row-gap: 12px;
    border-top: 1px solid #e6e9ed;
    border-radius: 0 0 16px 16px;
    .field-item {
      height: 32px;
      display: flex;
      column-gap: 8px;
      .field-label {
        flex: 0 0 104px;
        font-weight: 500;
        font-size: 13px;
        display: flex;
        align-items: center;
        color: #6b6d70;
        white-space: nowrap;
      }
      .field-value {
        width: 244px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        display: flex;
        align-items: center;
        font-size: 13px;
        letter-spacing: 0.25px;
        color: #3a3b3d;
      }
      .field-label-custom {
        flex: 0 0 auto;
        min-width: 104px;
        max-width: 130px;
        font-weight: 500;
        font-size: 13px;
        display: flex;
        align-items: center;
        color: #6b6d70;
      }
      .field-value-custom {
        display: flex;
        align-items: center;
        flex: 1;
        justify-content: flex-end;
        width: 36px;
      }
    }
    .field-item-custom {
      display: flex;
      align-items: center;
      column-gap: 8px;
      row-gap: 12px;
      justify-content: space-between;
      flex-wrap: wrap;
    }
    .field-custom {
      display: flex;
      flex: 1;
      justify-content: space-between;
      margin-left: 15px;
    }
  }
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
