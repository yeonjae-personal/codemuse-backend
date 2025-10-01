<template>
  <FourColumns>
    <ComponentAndOfferPane ref="componentOfferPane" />
    <ComponentDetail
      v-if="showDetail"
      :is-duplicate="isDuplicate"
      @fetch-data="fetchData"
      @close-loading-component="handleCloseLoadingComponent"
    />
    <ComponentResourceAdd v-if="showResourceAdd" />
    <ComponentEntitySearch v-if="showEntitySearch" />
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
import { useI18n } from "vue-i18n";
import cloneDeep from "lodash-es/cloneDeep";
import { useComponentStore, useSnackbarStore } from "@/store";
import { RESOURCE_PARAMS_FILTER_DEFAULT } from "@/constants/resource";
import { getComponentSearchType } from "@/api/prod/componentApi";
import { GroupedItem, Item } from "@/types/catalog/component/ComponentSearch";
import { getListItemCodeApi } from "@/api/prod/commonApi";
import { LargeItemCode } from "@/enums";
import ComponentAndOfferPane from "@/components/prod/catalog/component/ComponentAndOfferPane.vue";

const modules = import.meta.glob("@/components/**/*.vue");
const { t } = useI18n();
const useSnackbar = useSnackbarStore();
const asyncComponent = ref<any>(null);
const componentOfferPane = ref<InstanceType<typeof ComponentAndOfferPane>>();
const storeComponent = useComponentStore();
const {
  showDetail,
  isDuplicate,
  showResourceAdd,
  paramsFilterComponent,
  isInOfferMode,
  showEntitySearch,
  resourceParamsFilter,
  listResourceAdd,
  optionsType,
  showOfferSearch,
  offersType,
} = storeToRefs(storeComponent);

const fetchData = () => {
  if (componentOfferPane.value) {
    componentOfferPane.value.fetchList(paramsFilterComponent.value?.size);
  }
  handleCloseLoadingComponent();
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

watch(
  () => paramsFilterComponent.value.baseUuid,
  () => {
    isDuplicate.value = false;
  }
);

watch(
  () => isInOfferMode.value,
  () => {
    showEntitySearch.value = false;
  }
);

watch(
  () => showOfferSearch.value,
  () => {
    resourceParamsFilter.value = cloneDeep(RESOURCE_PARAMS_FILTER_DEFAULT);
    listResourceAdd.value.items = [];
    listResourceAdd.value.totalSearch = 0;
  },
  {
    immediate: false,
  }
);

const groupByMiddleItemCode = (data: Item[]): GroupedItem[] => {
  const grouped = data.reduce((acc, item) => {
    let group: any = acc.find((group) => group.value === item.middleItemCode);
    if (!group) {
      group = {
        name: item.middleItemName,
        value: item.middleItemCode,
        cmcdDetlId: item.middleItemCode,
        cmcdDetlNm: item.middleItemName,
        sortNo: item.middleSortNo,
        children: [],
      };
      acc.push(group);
    }
    group.children.push({
      name: item.itemName,
      value: item.itemCode,
      cmcdDetlId: item.itemCode,
      cmcdDetlNm: item.itemName,
      sortNo: item.sortNo,
    });
    return acc;
  }, [] as GroupedItem[]);

  // Sort groups by sortNo
  grouped.sort((after, before) => after.sortNo - before.sortNo);

  // Sort children within each group by sortNo
  grouped.forEach((group) => {
    group.children.sort((after, before) => after.sortNo - before.sortNo);
  });

  return grouped;
};

onMounted(async () => {
  const res = await getComponentSearchType();
  optionsType.value = groupByMiddleItemCode(res.data);
  const { data } = await getListItemCodeApi({
    lItemCode: LargeItemCode.Offer,
  });
  offersType.value = data;
});

provide("handleLoadComponent", loadComponent);
</script>
