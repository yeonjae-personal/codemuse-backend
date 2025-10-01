<template>
  <FourColumns ref="container">
    <GroupCreate
      ref="groupGreatePane"
      @close-loading-component="handleCloseLoadingComponent"
      @on-cancel="handleCancelCreateGroup"
    />
    <AddOfferPane v-if="isShowAddOffer" is-create />
    <component
      :is="asyncComponent"
      v-if="asyncComponent"
      class="flex h-full"
      is-share
      @on-close="handleCloseLoadingComponent"
    />
  </FourColumns>
</template>

<script setup lang="ts">
import { getListItemCodeApi } from "@/api/prod/commonApi";
import { LargeItemCode } from "@/enums";
import { useExtendCreateStore, useSnackbarStore } from "@/store";
import { useI18n } from "vue-i18n";

const extendCreateStore = useExtendCreateStore();
const { isShowAddOffer, offerTypesList } = storeToRefs(extendCreateStore);
const modules = import.meta.glob("@/components/**/*.vue");
const useSnackbar = useSnackbarStore();
const groupGreatePane = ref<any>(null);
const asyncComponent = ref<any>(null);
const { t } = useI18n();

const loadComponent = async (componentPath) => {
  const loader: any = modules[`/${componentPath}`];
  if (!loader) {
    useSnackbar.showSnackbar(t("product_platform.ob_url_invalid"), "error");
    return;
  }
  asyncComponent.value = defineAsyncComponent(loader);
};

const handleCloseLoadingComponent = () => {
  asyncComponent.value = null;
};

const handleCancelCreateGroup = () => {
  extendCreateStore.$reset();
  groupGreatePane?.value?.getInitCreateGroup?.();
};

onMounted(async () => {
  try {
    const { data } = await getListItemCodeApi({
      lItemCode: LargeItemCode.Offer,
    });
    offerTypesList.value = data;
  } catch (error: any) {
    useSnackbar.showSnackbar(
      error?.errorMsg || t("product_platform.something_went_wrong"),
      "error"
    );
  }
});

provide("handleLoadComponent", loadComponent);
</script>

<style lang="scss"></style>
