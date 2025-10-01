<template>
  <div class="bg-white relative pt-5 pb-3 col-span-2 rounded-lg">
    <div class="flex flex-col h-full">
      <div class="flex justify-between items-center px-[24px]">
        <div class="flex align-center gap-2 items-end">
          <h1 class="font-medium text-base text-text-base tracking-[0.5px]">
            {{
              customValidationItems.length
                ? conditionSearchSubType?.title ||
                  conditionSearchType?.title ||
                  ""
                : ""
            }}
            {{ $t("product_platform.custom_validation") }}
          </h1>
        </div>
        <div class="flex">
          <switch-view-table
            v-model="viewMode"
            class="ms-auto"
            @update:model-value="handleChangeView"
          />
        </div>
      </div>
      <div class="custom-validation-content">
        <div class="drop-item-area">
          <div class="drop-item">
            <div class="drop-item-header condition-item">
              {{ $t("product_platform.condition") }}
            </div>
          </div>
          <div class="drop-item">
            <div class="drop-item-header action-item">
              {{ $t("product_platform.action") }}
            </div>
          </div>
        </div>
        <LocomotiveComponent
          id="list-custom-validation"
          scroll-container-class="!px-0 list-attributes"
          scroll-content-class="d-flex flex-col gap-[15px] pt-4 pb-[10px] h-full relative w-[calc(100%-1px)] px-[24px]"
          dynamic-scroll-key="DYNAMIC_SCROLL_Y"
          is-dynamic-scroll
        >
          <template
            v-for="(item, index) in customValidationItems"
            :key="item.id"
          >
            <ValidationItem
              v-if="item.type === 'validation'"
              :item="item"
              :index="index"
              :number-items="customValidationItems.length"
            />
            <MemoItem v-else :item="item" :index="index" />
          </template>
          <div id="bottom-lomcomotive"></div>
        </LocomotiveComponent>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { VIEW_MODE } from "@/constants/";
import customValidationStore from "@/store/admin/customValidation.store";
import MemoItem from "./MemoItem.vue";
import ValidationItem from "./ValidationItem.vue";

const emits = defineEmits(["changeView"]);
const viewMode = ref(VIEW_MODE.GRID);
const { customValidationItems, conditionSearchType, conditionSearchSubType } =
  storeToRefs(customValidationStore());

const handleChangeView = (value) => {
  emits("changeView", value);
};
</script>
<style scoped lang="scss">
.custom-validation-content {
  background: #fff;
  border-radius: 8px;
  margin-top: 8px;
  height: 100%;
  font-family: "Noto Sans KR";

  .drop-item-area {
    display: flex;
    justify-content: space-between;
    column-gap: 24px;
    padding: 0 24px;
    .drop-item {
      display: flex;
      flex-direction: column;
      row-gap: 16px;
      flex: 1;

      .drop-item-header {
        border-radius: 0 0 12px 12px;
        border-top: 2px solid #4054b2;
        background: #f7f8fa;
        height: 48px;
        // width: 406px;
        display: flex;
        justify-content: center;
        align-items: center;
        box-shadow: 0px 2px 16px 0px #0000001f;
        font-size: 13px;
        font-weight: 500;
        color: #6b6d70;
      }

      .action-item {
        border-top-color: #d9325a;
      }
    }
  }

  .list-attributes {
    height: calc(100vh - 265px);
  }
}

#bottom-lomcomotive {
  position: absolute;
  bottom: 10px;
}
</style>
