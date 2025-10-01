<template>
  <BasePopup
    v-model="isOpenPopup"
    :title="'Unpublished Items Notice'"
    :size="DialogSizeType.ELarge"
  >
    <template #body>
      <div class="w-full pt-6 px-6">
        <div
          class="w-full h-[42px] flex align-center border border-error bg-error-lighter p-3 rounded-[8px] gap-[6px] mb-3"
        >
          <WarningSmallIcon />
          <span class="text-error text-[12px]">
            You have items that haven't been published yet. Please review and
            take action before proceeding.
          </span>
        </div>
        <div class="flex justify-between">
          <span class="text-[13px] text-[#3a3b3d] font-medium"
            >Unpublished Item List</span
          >
          <BaseTotalSearchResult :total-search="1" :total-items="1" />
        </div>
        <DataTable
          :headers="tableColumnHeader"
          :data="EXAMPLE_DATA"
          is-dynamic-table
          :is-show-footer="false"
          :is-show-total-search="false"
        >
          <template #[`baseItemType`]="{ item, width, align }">
            <div :style="{ width: width, textAlign: align }" class="p-4">
              <CustomTooltip :content="item?.baseItemType">
                <span>{{ item?.baseItemType }}</span>
              </CustomTooltip>
            </div>
          </template>
          <template #[`baseItemName`]="{ item, width, align }">
            <div :style="{ width: width, textAlign: align }" class="p-4">
              <CustomTooltip :content="item?.baseItemName">
                <span>{{ item?.baseItemName }}</span>
              </CustomTooltip>
            </div>
          </template>
          <template #[`strcItemType`]="{ item, width, align }">
            <div :style="{ width: width, textAlign: align }" class="p-4">
              <CustomTooltip :content="item?.strcItemType">
                <span>{{ item?.strcItemType }}</span>
              </CustomTooltip>
            </div>
          </template>
          <template #[`strcItemName`]="{ item, width, align }">
            <div :style="{ width: width, textAlign: align }" class="p-4">
              <CustomTooltip :content="item?.strcItemName">
                <span>{{ item?.strcItemName }}</span>
              </CustomTooltip>
            </div>
          </template>
          <template #[`status`]="{ item, width, align }">
            <div :style="{ width: width, textAlign: align }" class="p-4">
              <v-chip
                :color="item.status == 'Packed' ? 'red' : ''"
                :text="item.status"
                size="small"
                label
              />
            </div>
          </template>
          <template #[`action`]="{ item, width, align }">
            <div
              :style="{ width: width, textAlign: align }"
              class="h-full flex align-center py-[10px] px-3"
            >
              <div
                v-if="item.status === 'Packed'"
                class="flex gap-[6px] cursor-pointer"
              >
                <span class="text-[13px] text-info font-medium">{{
                  item.action
                }}</span>
                <ArrowNarrowUpRightIcon color="#1570EF" />
              </div>
            </div>
          </template>
        </DataTable>
      </div>
    </template>
    <template #footer>
      <div class="flex justify-end">
        <BaseButton :color="ButtonColorType.Gray" @click="closeDialog()">
          {{ t("product_platform.cancel") }}
        </BaseButton>
      </div>
    </template>
  </BasePopup>
</template>
<script setup lang="ts">
import { ButtonColorType, DialogSizeType } from "@/enums";
import { TableHeader } from "@/types/common";
import { useI18n } from "vue-i18n";

const { t } = useI18n();
const emits = defineEmits(["update:openPopup"]);
const props = defineProps({
  openPopup: {
    type: Boolean,
    default: false,
  },
});

const isOpenPopup = computed({
  get() {
    return props.openPopup;
  },
  set(newVal) {
    emits("update:openPopup", newVal);
  },
});

const tableColumnHeader = computed<TableHeader[]>(() => [
  {
    title: "Base Item Type",
    key: "baseItemType",
    width: "140px",
    align: "start",
  },
  {
    title: "Base Item Name",
    key: "baseItemName",
    width: "300px",
    align: "start",
  },
  {
    title: "Structure Item Type",
    key: "strcItemType",
    width: "160px",
    align: "start",
  },
  {
    title: "Structure Item Name",
    key: "strcItemName",
    width: "300px",
    align: "start",
  },
  {
    title: "Status",
    key: "status",
    width: "92px",
    align: "start",
  },
  {
    title: "Action",
    key: "action",
    width: "157px",
    align: "center",
  },
]);

const EXAMPLE_DATA = [
  {
    baseItemType: "Offer",
    baseItemName: "LTE Basic",
    strcItemType: "Component",
    strcItemName: "General Data 10G",
    status: "Saved",
    action: "Move to Package",
  },
  {
    baseItemType: "Offer",
    baseItemName: "LTE Basic",
    strcItemType: "Component",
    strcItemName: "General Data 10G",
    status: "Saved",
    action: "Move to Package",
  },
  {
    baseItemType: "Offer",
    baseItemName: "LTE Basic",
    strcItemType: "Component",
    strcItemName: "General Data 10G",
    status: "Packed",
    action: "Move to Package",
  },
  {
    baseItemType: "Offer",
    baseItemName: "LTE Basic",
    strcItemType: "Component",
    strcItemName: "General Data 10G",
    status: "Saved",
    action: "Move to Package",
  },
  {
    baseItemType: "Offer",
    baseItemName: "LTE Basic",
    strcItemType: "Component",
    strcItemName: "General Data 10G",
    status: "Packed",
    action: "Move to Package",
  },
  {
    baseItemType: "Offer",
    baseItemName: "LTE Basic",
    strcItemType: "Component",
    strcItemName: "General Data 10G",
    status: "Packed",
    action: "Move to Package",
  },
  {
    baseItemType: "Offer",
    baseItemName: "LTE Basic",
    strcItemType: "Component",
    strcItemName: "General Data 10G",
    status: "Packed",
    action: "Move to Package",
  },
  {
    baseItemType: "Offer",
    baseItemName: "LTE Basic",
    strcItemType: "Component",
    strcItemName: "General Data 10G",
    status: "Saved",
    action: "Move to Package",
  },
];

const closeDialog = () => {
  isOpenPopup.value = false;
};
</script>
