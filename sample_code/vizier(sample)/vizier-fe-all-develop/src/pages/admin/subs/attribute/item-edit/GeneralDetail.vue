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
            <span v-if="item.key !== 'used'">
              {{ itemDetail.generalInfo[item.key] }}
            </span>
            <v-switch
              v-else
              v-model="itemDetail.generalInfo[item.key]"
              hide-details
              color="#FDCED5"
              base-color="#E6E9ED"
              inset
              density="compact"
              :readonly="true"
            />
          </div>
        </template>
      </DetailPaneRow>
    </DetailPane>
    <div class="loup-item">
      <div class="loup-title" :class="{ disabled: !listUpperItem.length }">
        {{ t("product_platform.upper_item") }}
      </div>
      <div class="loup-content">
        <div v-if="listUpperItem.length" class="connect-line">
          <BaseCanvas
            id="attribute-management-line-2"
            :height="heightConnectLineUpperItem"
            :width="48"
            :list-coordinates="listCoordinatesUpperItem"
            style="transform: translateX(-8px)"
          />
        </div>
        <div class="list-item">
          <div
            v-for="upperItem in listUpperItem"
            :key="upperItem.id"
            class="item cursor-pointer"
          >
            <div class="item-ellipsis">{{ upperItem.name }}</div>
            <div class="item-type">{{ upperItem.strcTypeCode }}</div>
          </div>
        </div>
      </div>
    </div>
    <div class="loup-item">
      <div class="loup-title" :class="{ disabled: !listLowerItem.length }">
        {{ t("product_platform.lower_item") }}
      </div>
      <div class="loup-content">
        <div v-if="listLowerItem.length" class="connect-line">
          <BaseCanvas
            id="attribute-management-line-2"
            :height="heightConnectLineLowerItem"
            :width="48"
            :list-coordinates="listCoordinatesLowerItem"
            style="transform: translateX(-8px)"
          />
        </div>
        <div class="list-item">
          <div
            v-for="lowerItem in listLowerItem"
            :key="lowerItem.id"
            class="item cursor-pointer"
          >
            <div class="item-ellipsis">{{ lowerItem.name }}</div>
            <div class="item-type">{{ lowerItem.strcTypeCode }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { useI18n } from "vue-i18n";
import BaseCanvas from "@/components/prod/common/BaseCanvas.vue";
import attributeManagementStore from "@/store/admin/attributeManagement.store";
import DetailPane from "@/components/prod/layout/DetailPane.vue";
import DetailPaneRow from "@/components/prod/layout/DetailPaneRow.vue";

const { t } = useI18n();
const { itemDetail } = storeToRefs(attributeManagementStore());

const infoItems = computed(() => [
  {
    label: t("product_platform.selectBoxItem"),
    key: "item",
  },
  {
    label: t("product_platform.itemCode"),
    key: "itemCode",
  },
  {
    label: t("product_platform.item_name"),
    key: "itemName",
  },
  {
    label: t("product_platform.useYn"),
    key: "used",
  },
]);

const generalInfo = computed(() => {
  return itemDetail.value.generalInfo;
});

const listUpperItem = computed(() =>
  generalInfo.value.upperItems.filter((item) => item.code)
);

const listLowerItem = computed(() =>
  generalInfo.value.lowerItems.filter((item) => item.code)
);

const listCoordinatesUpperItem = computed(() => {
  return listUpperItem.value.map((_item, index) => ({
    leftStartPoint: 24,
    topStartPoint: 0,
    leftMovePoint: 48,
    topMovePoint: index === 0 ? 28 : 48 * index + 28,
    lineWidth: 1,
    strokeStyle: "#bdc1c7",
  }));
});

const heightConnectLineUpperItem = computed(() => {
  return listUpperItem.value.length * 48;
});

const listCoordinatesLowerItem = computed(() => {
  return listLowerItem.value.map((_item, index) => ({
    leftStartPoint: 24,
    topStartPoint: 0,
    leftMovePoint: 48,
    topMovePoint: index === 0 ? 28 : 48 * index + 28,
    lineWidth: 1,
    strokeStyle: "#bdc1c7",
  }));
});

const heightConnectLineLowerItem = computed(() => {
  return listLowerItem.value.length * 48;
});
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
  .loup-item {
    padding: 0 9.5px;

    .loup-title {
      text-transform: uppercase;
      color: #3a3b3d;
      font-size: 13px;
      font-weight: 500;
      padding: 6px 0;
    }
    .disabled {
      color: #bdc1c7;
    }
    .loup-content {
      display: flex;
      .connect-line {
        width: 48px;
      }
      .list-item {
        flex: 1;
        display: flex;
        flex-direction: column;
        row-gap: 8px;
        margin-top: 8px;
        .item {
          display: flex;
          align-items: center;
          justify-content: space-between;
          gap: 8px;
          height: 40px;
          max-width: 340px;
          padding: 10px 14px;
          border-radius: 99px;
          background-color: #f7f8fa;
          color: #3a3b3d;
          font-size: 13px;
        }
        .item-ellipsis {
          max-width: 295px;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }
        .item-type {
          font-family: Noto Sans KR;
          font-weight: 500;
          font-size: 13px;
          line-height: 150%;
          letter-spacing: 0.25px;
          color: #6b6d70;
        }
      }
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
