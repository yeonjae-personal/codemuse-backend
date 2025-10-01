<template>
  <div class="w-[640px] px-6 pt-6 gap-3">
    <!-- -------------- start row 0-------------- -->
    <div class="flex gap-4 mb-4 w-[592px] h-[48px]">
      <div class="flex-1">
        <base-input-text
          class=" "
          :required="true"
          label="Code Group Information"
          :disabled="true"
        >
        </base-input-text>
      </div>
    </div>

    <!-- -------------- start row 1-------------- -->

    <div class="flex gap-4 mb-4 w-[592px] h-[48px]">
      <div class="flex-1">
        <base-input-text class=" " :required="true" label="Code Group ID">
        </base-input-text>
      </div>

      <BaseButton
        :color="ButtonColorType.Secondary"
        :width="WIDTH_BUTTON.DUPLICATE_CHECK"
        :height="HEIGHT_BUTTON.DUPLICATE_CHECK"
      >
        Duplicate Check
      </BaseButton>
    </div>
  </div>

  <!-- -------------- start row 2-------------- -->

  <div class="flex gap-4 mb-4 w-[592px] h-[48px]">
    <div class="flex-1">
      <base-input-text class=" " :required="true" label="Code Group Name">
      </base-input-text>
    </div>
  </div>

  <!-- -------------- start row 3-------------- -->

  <div class="flex gap-3">
    <div class="w-[150px]">
      <base-select
        v-model="item"
        :label="'Sorting Rank'"
        :density="'comfortable'"
        :items="[]"
        :item-title="'title'"
        :item-value="'value'"
        :required="true"
      />
    </div>

    <div class="w-[139px]">
      <base-select
        v-model="item"
        :label="'Usage'"
        :density="'comfortable'"
        :items="[]"
        :item-title="'title'"
        :item-value="'value'"
        :required="true"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { HEIGHT_BUTTON, WIDTH_BUTTON } from "@/constants/index";
import { ButtonColorType } from "@/enums";
// import { useMenuStore } from "@/store";
// const menuStore = useMenuStore();
// const { selectedMenuItem } = storeToRefs(menuStore);
</script>

<style lang="scss" scoped>
.custom-button {
  width: 140px;
  height: 48px;
  color: #ba1642;
  background-color: #fff0f2;
}

.custom-button {
  width: 140px;
}

.custom-row {
  gap: 16px;
}
</style>
