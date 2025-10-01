<template>
  <div class="flex flex-col h-full bg-white flex-grow w-[780px] relative">
    <div
      v-if="isLoadingContent"
      class="overflow-y-auto bg-clip-content rounded-[12px] content-menu-list"
    >
      <div class="flex border-lightest align-center">
        <div class="w-[240px] custom-col text-[13px] font-medium">
          {{ $t("product_platform.menuEntity.menuName") }}
        </div>
        <div class="flex-1 custom-col-right text-[13px] font-normal">
          {{ selectedMenuItem?.menuNm || "-" }}
        </div>
      </div>

      <div class="flex border-lightest align-center">
        <div class="w-[240px] custom-col text-[13px] font-medium">
          {{ $t("product_platform.menuEntity.menuId") }}
        </div>
        <div class="flex-1 custom-col-right text-[13px] font-normal">
          {{ selectedMenuItem?.menuId || "-" }}
        </div>
      </div>

      <div class="flex border-lightest align-center">
        <div class="w-[240px] custom-col text-[13px] font-medium">
          {{ $t("product_platform.menuEntity.level") }}
        </div>
        <div class="flex-1 custom-col-right text-[13px] font-normal">
          {{ selectedMenuItem?.menuLvNo || "-" }}
        </div>
      </div>

      <div class="flex border-lightest align-center">
        <div class="w-[240px] custom-col text-[13px] font-medium">
          {{ $t("product_platform.menuEntity.enabled") }}
        </div>
        <div class="flex-1 custom-col-right text-[13px] font-normal">
          <v-switch
            v-model="selectedMenuItem.actvYn"
            hide-details
            class="pt-2"
            color="rgba(253, 206, 213, 1)"
            inset
            width="40"
            density="compact"
            :readonly="true"
          ></v-switch>
        </div>
      </div>

      <div class="flex border-lightest align-center">
        <div class="w-[240px] custom-col text-[13px] font-medium">
          {{ $t("product_platform.menuEntity.permissionControl") }}
        </div>
        <div class="flex-1 custom-col-right text-[13px] font-normal h-[56px]">
          <v-switch
            v-model="selectedMenuItem.authCtrlYn"
            hide-details
            class="pt-2"
            color="rgba(253, 206, 213, 1)"
            inset
            width="40"
            density="compact"
            :readonly="true"
          ></v-switch>
        </div>
      </div>

      <div class="flex border-lightest align-center">
        <div class="w-[240px] custom-col text-[13px] font-medium">
          {{ $t("product_platform.menuEntity.explanation") }}
        </div>
        <div class="flex-1 custom-col-right text-[13px] font-normal">
          {{ selectedMenuItem?.menuDscr || "-" }}
        </div>
      </div>

      <div class="flex border-lightest align-center">
        <div class="w-[240px] custom-col text-[13px] font-medium">
          {{ $t("product_platform.menuEntity.sortOrder") }}
        </div>
        <div class="flex-1 custom-col-right text-[13px] font-normal">
          {{ selectedMenuItem?.sortOrd || "-" }}
        </div>
      </div>

      <div class="flex border-lightest align-center">
        <div class="w-[240px] custom-col text-[13px] font-medium">
          {{ $t("product_platform.menuEntity.screenId") }}
        </div>
        <div class="flex-1 custom-col-right text-[13px] font-normal">
          {{ selectedMenuItem?.scrnId || "-" }}
        </div>
      </div>

      <div class="flex border-lightest align-center">
        <div class="w-[240px] custom-col text-[13px] font-medium">
          {{ $t("product_platform.menuEntity.screenName") }}
        </div>
        <div class="flex-1 custom-col-right text-[13px] font-normal">
          {{ selectedMenuItem?.scrnNm || "-" }}
        </div>
      </div>

      <div class="flex border-lightest align-center">
        <div class="w-[240px] custom-col text-[13px] font-medium">
          {{ $t("product_platform.menuEntity.registrant") }}
        </div>
        <div class="flex-1 custom-col-right text-[13px] font-normal">
          {{ selectedMenuItem?.rgstUsrNm || "-" }}
        </div>
      </div>

      <div class="flex border-lightest align-center">
        <div class="w-[240px] custom-col text-[13px] font-medium">
          {{ $t("product_platform.menuEntity.menuApprover") }}
        </div>
        <div class="flex-1 custom-col-right text-[13px] font-normal">
          {{ selectedMenuItem?.authAprvUsrNm || "-" }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
const isLoadingContent = ref(false);

const props = defineProps({
  item: {
    type: Object,
    default: null,
  },
});
const selectedMenuItem = computed(() => {
  return props?.item || null;
});

watch(
  () => props.item,
  (newValue) => {
    isLoadingContent.value = newValue;
  },
  { deep: true, immediate: true }
);
</script>

<style lang="scss" scoped>
.custom-col {
  width: 240px;
  height: 56px;
  padding: 0px 16px;
  background-color: #f7f8fa;
  line-height: 56px;
}

.custom-col-right {
  width: 100%;
  height: 56px;
  padding: 0px 16px;
  line-height: 56px;
}

.border-lightest {
  border-bottom: 1px solid var(--border-border-lightest, #f0f2f5);
}

.border-lightest:last-child {
  border-bottom: unset;
}

:deep(.v-switch--inset .v-switch__thumb) {
  height: 16px;
  width: 16px;
  left: -1px !important;
}

:deep(.v-switch--inset .v-switch__track) {
  height: 20px;
  min-width: 36px;
}

:deep(.v-switch__track) {
  opacity: 1;
  background-color: rgb(220 224 228);
}
:deep(.content-menu-list) {
  border: 1px solid rgba(230, 233, 237, 1);
}
</style>
