<template>
  <div class="general-wrapper">
    <DetailPane>
      <DetailPaneRow
        v-for="item in infoItems"
        :key="item.key"
        :label="item.label"
        :value="itemDetail.treeViewInfo[item.key]"
      >
      </DetailPaneRow>
    </DetailPane>
    <div class="general-info gap-[6px]">
      <div class="info-item !h-[20px]">
        <div class="item-key !text-[#3A3B3D]">
          {{ t("product_platform.overview") }}
        </div>
      </div>
      <div class="item-value">
        <span class="text-overview">{{
          itemDetail.treeViewInfo.overview
        }}</span>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { useI18n } from "vue-i18n";
import attributeManagementStore from "@/store/admin/attributeManagement.store";
import DetailPane from "@/components/prod/layout/DetailPane.vue";

const { t } = useI18n();
const { itemDetail } = storeToRefs(attributeManagementStore());

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
    .text-overview {
      font-size: 13px;
      color: #3a3b3d;
    }
  }
}
</style>
