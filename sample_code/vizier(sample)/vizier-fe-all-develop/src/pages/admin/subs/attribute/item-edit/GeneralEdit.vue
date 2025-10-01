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
            <span v-if="!['used', 'itemName', 'itemCode'].includes(item.key)">{{
              itemDetail.generalInfo[item.key]
            }}</span>
            <span
              v-if="item.key === 'itemCode' && !itemDetail.generalInfo.isNew"
            >
              {{ itemDetail.generalInfo[item.key] }}
            </span>
            <BaseInputText
              v-if="item.key === 'itemCode' && itemDetail.generalInfo.isNew"
              v-model.trim="itemDetail.generalInfo[item.key]"
              :rules="
                useInputValidation({
                  onlyCharsWithTwoChars: true,
                  maxLength: 2,
                })
              "
              :maxlength="2"
              required
              counter
              @update:model-value="handleChangeValue($event, item.key)"
            />
            <BaseInputText
              v-if="item.key === 'itemName'"
              v-model.trim="itemDetail.generalInfo[item.key]"
              :rules="useInputValidation({ maxLength: maxLengthNum })"
              :maxlength="maxLengthNum"
              required
              counter
            />
            <v-switch
              v-if="item.key === 'used'"
              v-model="itemDetail.generalInfo[item.key]"
              hide-details
              color="#D9325A"
              base-color="#E6E9ED"
              inset
              density="compact"
            />
          </div>
        </template>
      </DetailPaneRow>
    </DetailPane>
    <div class="loup-item">
      <div
        class="loup-title"
        :class="{ disabled: !itemDetail.generalInfo.upperItems.length }"
      >
        {{ t("product_platform.upper_item") }}
      </div>
      <div class="loup-content">
        <div
          v-if="itemDetail.generalInfo.upperItems.length"
          class="connect-line"
        >
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
            v-for="upperItem in itemDetail.generalInfo.upperItems"
            :key="upperItem.id"
          >
            <CustomSelectList
              v-model="upperItem.code"
              :options="upperOptions!"
              :placeholder="upperPlaceholder"
              @update:model-value="handleChangeUpperItem($event)"
              @remove-item="removeLoupItem(upperItem.id, 'upper')"
            >
              <template #typeCode>
                {{ upperItem.strcTypeCode }}
              </template>

              <template #header>
                <div class="base-select_prepend">
                  <BaseRadio
                    v-model="upperItem.strcTypeCode"
                    :yes-label="t('product_platform.single')"
                    :no-label="t('product_platform.multiple')"
                    yes-value="S"
                    no-value="M"
                    class="flex"
                    density="compact"
                  />
                </div>
              </template>
            </CustomSelectList>
          </div>
        </div>
      </div>
      <div v-if="showButtonAddUpper" class="add-new-section">
        <button class="add-new-button" @click="handleAddNewUpperItem">
          <PlusLargeIcon />
        </button>
      </div>
    </div>
    <div class="loup-item">
      <div
        class="loup-title"
        :class="{ disabled: !itemDetail.generalInfo.lowerItems.length }"
      >
        {{ t("product_platform.lower_item") }}
      </div>
      <div class="loup-content">
        <div
          v-if="itemDetail.generalInfo.lowerItems.length"
          class="connect-line"
        >
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
            v-for="lowerItem in itemDetail.generalInfo.lowerItems"
            :key="lowerItem.id"
            class=""
          >
            <CustomSelectList
              v-model="lowerItem.code"
              :options="lowerOptions!"
              :placeholder="lowerPlaceholder"
              @update:model-value="handleChangeLowerItem($event)"
              @remove-item="removeLoupItem(lowerItem.id, 'lower')"
            >
              <template #typeCode>
                {{ lowerItem.strcTypeCode }}
              </template>

              <template #header>
                <div class="base-select_prepend">
                  <BaseRadio
                    v-model="lowerItem.strcTypeCode"
                    :yes-label="t('product_platform.single')"
                    :no-label="t('product_platform.multiple')"
                    yes-value="S"
                    no-value="M"
                    class="flex"
                    density="compact"
                  />
                </div>
              </template>
            </CustomSelectList>
          </div>
        </div>
      </div>
      <div v-if="showButtonAddLower" class="add-new-section pb-[5px]">
        <button class="add-new-button" @click="handleAddNewLowerItem">
          <PlusLargeIcon />
        </button>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { useI18n } from "vue-i18n";
import cloneDeep from "lodash-es/cloneDeep";
import BaseCanvas from "@/components/prod/common/BaseCanvas.vue";
import BaseInputText from "@/components/prod/common/BaseInputText.vue";
import PlusLargeIcon from "@/components/prod/icons/PlusLargeIcon.vue";
import { useInputValidation } from "@/composables/useInputValidation";
import attributeManagementStore from "@/store/admin/attributeManagement.store";
import CustomSelectList from "./CustomSelectList.vue";
import DetailPane from "@/components/prod/layout/DetailPane.vue";
import DetailPaneRow from "@/components/prod/layout/DetailPaneRow.vue";

const { itemDetail, originItemDetail, loupItems } = storeToRefs(
  attributeManagementStore()
);
const { addLoupItem, removeLoupItem, updateItemsView } =
  attributeManagementStore();

const { t } = useI18n();

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

const upperOptions = computed(() => {
  const { upperItems = [] } = itemDetail.value.generalInfo;
  const addedOptions = upperItems.map(({ code }) => code);
  return loupItems.value?.upperItems.map((item) => ({
    name: item.itemName,
    value: item.itemCode,
    isDisabled: addedOptions.includes(item.itemCode),
  }));
});

const lowerOptions = computed(() => {
  const { lowerItems = [] } = itemDetail.value.generalInfo;
  const addedOptions = lowerItems.map(({ code }) => code);
  return loupItems.value?.lowerItems.map(({ itemName, itemCode }) => ({
    name: itemName,
    value: itemCode,
    isDisabled: addedOptions.includes(itemCode),
  }));
});

const showButtonAddUpper = computed(() => {
  return (
    itemDetail.value.generalInfo.upperItems.length <
    (loupItems.value?.upperItems.length || 0)
  );
});

const showButtonAddLower = computed(() => {
  return (
    itemDetail.value.generalInfo.lowerItems.length <
    (loupItems.value?.lowerItems.length || 0)
  );
});

const maxLengthNum = computed<number>(() =>
  itemDetail.value.generalInfo.largeItemCode === "O" ? 10 : 30
);

const generalInfo = computed(() => {
  return itemDetail.value.generalInfo;
});

const upperPlaceholder = computed<string>(() => {
  switch (itemDetail.value.generalInfo.largeItemCode) {
    case "R":
      return `- ${t("product_platform.component_select")} -`;
    case "C":
      return `- ${t("product_platform.offer_select")} -`;
    case "O":
    default:
      return "";
  }
});

const lowerPlaceholder = computed<string>(() => {
  switch (itemDetail.value.generalInfo.largeItemCode) {
    case "O":
      return `- ${t("product_platform.component_select")} -`;
    case "C":
      return `- ${t("product_platform.resource_select")} -`;
    case "R":
    default:
      return "";
  }
});

const getItemName = (
  list: {
    itemCode: string;
    itemName: string;
  }[],
  code: string
): string => {
  const currentItem = list.find((item) => item.itemCode === code);
  return currentItem?.itemName || "";
};

const handleChangeLowerItem = (value: string): void => {
  const itemName = getItemName(loupItems.value?.lowerItems!, value);
  itemDetail.value.generalInfo.lowerItems.forEach((item) => {
    if (item.code === value) {
      item.name = itemName;
    }
  });
};

const handleChangeUpperItem = (value: string): void => {
  const itemName = getItemName(loupItems.value?.upperItems!, value);
  itemDetail.value.generalInfo.upperItems.forEach((item) => {
    if (item.code === value) {
      item.name = itemName;
    }
  });
};

const handleAddLowerUpperItem = (): void => {
  nextTick(() => {
    if (
      loupItems.value?.upperItems?.length! > 0 &&
      itemDetail.value.generalInfo.upperItems.length === 0
    ) {
      addLoupItem("upper");
      originItemDetail.value = cloneDeep(itemDetail.value);
    }
    if (
      loupItems.value?.lowerItems?.length! > 0 &&
      itemDetail.value.generalInfo.lowerItems.length === 0
    ) {
      addLoupItem("lower");
      originItemDetail.value = cloneDeep(itemDetail.value);
    }
  });
};

onBeforeMount(() => {
  handleAddLowerUpperItem();
});

onBeforeUpdate(() => {
  handleAddLowerUpperItem();
});

const listCoordinatesUpperItem = computed(() => {
  return generalInfo.value.upperItems.map((_item, index) => ({
    leftStartPoint: 24,
    topStartPoint: 0,
    leftMovePoint: 48,
    topMovePoint: index === 0 ? 28 : 48 * index + 28,
    lineWidth: 1,
    strokeStyle: "#bdc1c7",
  }));
});
const heightConnectLineUpperItem = computed(() => {
  return generalInfo.value.upperItems.length * 48;
});
const listCoordinatesLowerItem = computed(() => {
  return generalInfo.value.lowerItems.map((_item, index) => ({
    leftStartPoint: 24,
    topStartPoint: 0,
    leftMovePoint: 48,
    topMovePoint: index === 0 ? 28 : 48 * index + 28,
    lineWidth: 1,
    strokeStyle: "#bdc1c7",
  }));
});
const heightConnectLineLowerItem = computed(() => {
  return generalInfo.value.lowerItems.length * 48;
});

const handleChangeValue = (value: string, key: string): void => {
  itemDetail.value.generalInfo[key as string] = value.toUpperCase();
};

//Handle Events
const handleAddNewUpperItem = () => {
  addLoupItem("upper");
};
const handleAddNewLowerItem = () => {
  addLoupItem("lower");
};

const itemDetailString = computed(() => {
  return JSON.stringify(itemDetail.value);
});

watch(itemDetailString, (value) => {
  const newItem = JSON.parse(value);
  if (newItem) {
    updateItemsView({
      name: newItem.generalInfo.itemName,
      code: newItem.generalInfo.itemCode,
      isUsed: newItem.generalInfo.used,
    });
  }
});
</script>
<style lang="scss" scoped>
.general-wrapper {
  display: flex;
  flex-direction: column;
  row-gap: 12px;
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
          padding: 10px 12px 10px 14px;
          border-radius: 99px;
          background-color: #f7f8fa;
          color: #3a3b3d;
          font-size: 13px;
        }
        .item-ellipsis {
          max-width: 262px;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }
      }
    }
    .add-new-section {
      display: flex;
      align-items: center;
      justify-content: center;
      margin-top: 12px;
      .add-new-button {
        width: 40px;
        height: 40px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        box-shadow:
          2px 1px 6px 0px #1e265b3d,
          2px 1px 18px 0px #1518421c inset,
          -5px -2px 6px 0px #ffffffa3 inset;
      }
    }
  }
}

.base-select_prepend {
  padding: 8px 12px;
  border-bottom: 1px solid #e6e9ed;

  :deep(.v-selection-control--density-compact) {
    --v-selection-control-size: 32px;
    gap: 4px;
    width: 152px;
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
:deep(.v-field__field) {
  height: 32px;
}
:deep(.v-field) {
  height: 32px;
}

:deep(.v-field__input) {
  padding: 0 0 0 16px;
  height: 38px;
  min-height: 38px;
}

:deep(.custom-text-field .v-input__control) {
  height: 32px;
}

:deep(.base-select) {
  border-radius: 99px !important;
}
:deep(.base-select .v-field__field) {
  height: 38px;
}
:deep(.base-select .v-field) {
  height: 38px;
}

:deep(.v-select .v-field .v-field__input > input) {
  top: 8px;
}

:deep(.v-label) {
  opacity: 1;
}
</style>
