<script lang="ts" setup>
import { VIEW_MODE } from "@/constants/index";
import { useResourceStore, useSnackbarStore } from "@/store";
import { useI18n } from "vue-i18n";

defineOptions({
  name: "CatalogResourceSearch",
});

const modules = import.meta.glob("@/components/**/*.vue");
const useSnackbar = useSnackbarStore();
const resourceStore = useResourceStore();
const {
  showDetail,
  resourceSelected,
  showResourceDuplicate,
  showComponentSearch,
  showEntitySearch,
  isDuplicate,
  viewMode,
} = storeToRefs(resourceStore);
const refreshFlag = ref(false);
const asyncComponent = shallowRef<any>(null);
const { t } = useI18n();

const fetchData = () => {
  refreshFlag.value = !refreshFlag.value;
  handleCloseLoadingComponent();
};

const resourceSearchPaneRef = ref<any>(null);

const resetResourcePaneSearch = () => {
  resourceSearchPaneRef.value!.handleResetSearch();
};
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
provide("handleLoadComponent", loadComponent);
</script>
<template>
  <FourColumns>
    <div
      class="grid grid-cols-2"
      :class="[viewMode === VIEW_MODE.GRID ? 'col-span-2' : 'col-span-4']"
    >
      <ResourceComponentSearch
        v-if="showComponentSearch"
        v-show="showComponentSearch"
        @reset-search="resetResourcePaneSearch"
      />
      <ResourceSearchPagePane
        ref="resourceSearchPaneRef"
        :refresh-flag="refreshFlag"
      />
    </div>
    <ResourceDetail
      v-if="showDetail"
      :key="resourceSelected?.uuid"
      :is-duplicate="isDuplicate"
      @fetch-data="fetchData"
      @close-loading-component="handleCloseLoadingComponent"
    />
    <ResourceDetail
      v-if="showResourceDuplicate"
      :key="resourceSelected?.uuid"
      :is-duplicate="isDuplicate"
      @fetch-data="fetchData"
      @close-loading-component="handleCloseLoadingComponent"
    />
    <ResourceEntitySearch v-if="showEntitySearch" />
    <component
      :is="asyncComponent"
      v-if="asyncComponent"
      class="flex h-full"
      is-share
      @on-close="handleCloseLoadingComponent"
    />
  </FourColumns>
</template>
