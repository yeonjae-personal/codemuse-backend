<template>
  <FourColumns ref="container">
    <GroupAndOfferPane ref="groupOfferPane" />
    <GroupDetail
      v-if="displayForm.groupDetail"
      @close-loading-component="handleCloseLoadingComponent"
      @reload-group-search="handleReloadGroupPane"
    />
    <GroupDetail
      v-if="displayForm.groupDuplicate"
      :category="GROUP_DETAIL_CATEFORY.DUPLICATE"
      @close-loading-component="handleCloseLoadingComponent"
      @reload-group-search="handleReloadGroupPane"
    />
    <AddOfferPane v-if="displayForm.addOffer" />
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
import { useExtendSearchStore, useSnackbarStore } from "@/store";
import { GROUP_DETAIL_CATEFORY } from "@/constants/extendsManager";
import { useI18n } from "vue-i18n";
import { getListItemCodeApi } from "@/api/prod/commonApi";
import { LargeItemCode } from "@/enums";
import GroupAndOfferPane from "@/components/prod/extends/search/GroupAndOfferPane.vue";

const modules = import.meta.glob("@/components/**/*.vue");
const { displayForm, offerTypesList, groupTypeList } = storeToRefs(
  useExtendSearchStore()
);

const groupOfferPane = ref<InstanceType<typeof GroupAndOfferPane>>();
const useSnackbar = useSnackbarStore();
const asyncComponent = shallowRef<any>(null);
const groupSearchPane = ref<any>();
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

const handleReloadGroupPane = () => {
  if (groupOfferPane.value) {
    groupOfferPane.value.fetchList();
  }

  groupSearchPane.value?.handleSearch?.();
};

onMounted(async () => {
  try {
    const { data } = await getListItemCodeApi({
      lItemCode: LargeItemCode.Offer,
    });
    offerTypesList.value = data;
    const { data: options } = await getListItemCodeApi({
      lItemCode: LargeItemCode.Group,
    });
    groupTypeList.value = options;
  } catch (error: any) {
    useSnackbar.showSnackbar(
      error?.errorMsg || t("product_platform.something_went_wrong"),
      "error"
    );
  }
});

provide("handleLoadComponent", loadComponent);
</script>
