<template>
  <FourColumns>
    <ResourceCreateComponent
      :is-view-only="isViewOnly"
      :title="$t('product_platform.resource.resource_create')"
      :create-item-code-list="resourceItemCodeList"
      :item-code="paramsSearchEntityCreate.itemCode"
      is-create
      @handle-submit-resource="handleCreateResource"
      @handle-cancel-create="handleCancelCreate"
      @close-loading-component="handleCloseLoadingComponent"
    />
    <ResourceCreateMultiEntitySearch v-if="showEntitySearchCreate" />
    <component
      :is="asyncComponent"
      v-if="asyncComponent"
      class="flex h-full"
      is-share
      @on-close="handleCloseLoadingComponent"
    />
  </FourColumns>
</template>
<script lang="ts" setup>
import { useI18n } from "vue-i18n";
import { useResourceStore, useSnackbarStore } from "@/store";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import { ResourceType } from "@/enums/component";
import { LARGE_ITEM_CODE } from "@/store/userPocket.store";
import { getListItemCodeApi } from "@/api/prod/commonApi";
import { DATE_FORMAT, ITEMS_PAGE_TYPE } from "@/constants/index";
import { formatDate } from "@/utils/format-data";

const { t } = useI18n();
const modules = import.meta.glob("@/components/**/*.vue");
const snackbarStore = useSnackbarStore();
const resourceStore = useResourceStore();
const {
  resourceCodeCreate,
  showEntitySearchCreate,
  resourceCreateEntityList,
  paramsSearchEntityCreate,
  resourceItemCodeList,
  isViewOnly,
} = storeToRefs(resourceStore);
const { getMultiEntityTypes } = resourceStore;

const asyncComponent = shallowRef<any>(null);

const handleCreateResource = async (data: any) => {
  const payload: any = {
    general: data.general.filter((item) => !item.dispTab),
    additional: [
      ...data.general.filter((item) => item.dispTab),
      ...data.additional,
    ].map((item: any) => {
      let val = item.attrVal;
      if (item.fieldTypeCode === COLUMN_FIELD_TYPE.DM) {
        val = JSON.stringify(item.attrVal);
      }
      return {
        ...item,
        attrVal: val,
      };
    }),
    insertEntityObjRels: resourceCreateEntityList.value
      ?.flatMap((item) => item.objRel.filter((ent) => ent.isAdd))
      .map((item) => ({
        entityCode: item.entityCode,
        validEndDtm: item.validEndDtm
          ? formatDate(
              item.validStartDtm,
              DATE_FORMAT.DATE_TYPE,
              DATE_FORMAT.DATE_TYPE
            )
          : null,
        validStartDtm: item.validStartDtm
          ? formatDate(
              item.validStartDtm,
              DATE_FORMAT.DATE_TYPE,
              DATE_FORMAT.DATE_TYPE
            )
          : null,
      })),
  };
  try {
    const res = await resourceStore.createNewResource(payload);
    resourceCodeCreate.value = null;
    if (res?.data) {
      resourceCodeCreate.value = res.data?.code;
      snackbarStore.showSnackbar(
        t("product_platform.resource.create_resource_successfully"),
        "success"
      );
      isViewOnly.value = true;
      handleCloseLoadingComponent();
    }
    //await resourceStore.getFormatResourceCreate();
  } catch (err: any) {
    snackbarStore.showSnackbar(
      err?.errorMsg || t("product_platform.something_went_wrong"),
      "error"
    );
  } finally {
    showEntitySearchCreate.value = false;
  }
};

const handleCancelCreate = async () => {
  resourceStore.resetParamListEntitySearch(ITEMS_PAGE_TYPE.CREATE);
  await resourceStore.getFormatResourceCreate(ResourceType.RatingElement);
};
const loadComponent = async (componentPath) => {
  const loader: any = modules[`/${componentPath}`];
  if (!loader) {
    snackbarStore.showSnackbar(t("product_platform.ob_url_invalid"), "error");
    return;
  }
  asyncComponent.value = defineAsyncComponent(loader);
};
const handleCloseLoadingComponent = () => {
  asyncComponent.value = null;
};
onMounted(async () => {
  if (resourceItemCodeList.value?.length === 0) {
    try {
      const { data } = await getListItemCodeApi({
        lItemCode: LARGE_ITEM_CODE.RESOURCE,
      } as any);
      if (data) {
        resourceItemCodeList.value = data;
      }
      await resourceStore.getFormatResourceCreate(ResourceType.RatingElement);
      paramsSearchEntityCreate.value.itemCode = ResourceType.RatingElement;
      await getMultiEntityTypes();
    } catch (err: any) {
      snackbarStore.showSnackbar(
        err?.errorMsg || t("product_platform.something_went_wrong"),
        "error"
      );
    }
  }
});
provide("handleLoadComponent", loadComponent);
</script>
<style scoped></style>
