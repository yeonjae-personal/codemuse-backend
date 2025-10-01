<template>
  <div class="label-search-filter px-6">
    <BaseSelectScroll
      v-model="searchParams.type"
      :default-item-select-all="false"
      :options="labelSearchTypeOptions"
      class="!w-[120px] flex-shrink-0"
      :height="48"
    />
    <BaseInputSearch
      v-model.trim="searchParams.value"
      density="comfortable"
      label="search"
      variant="solo"
      hide-details
      single-line
      rounded="4"
      @handle-search="handleSearchLabel"
    />
    <SearchAndRefreshButton
      @handle-search="handleSearchLabel"
      @handle-refresh="handleResetLabelSearch"
    />
  </div>
</template>

<script lang="ts" setup>
import { useI18n } from "vue-i18n";
import useLabelStore from "@/store/admin/label.store";
import {
  LABEL_SEARCH_TYPE,
  DEFAULT_SEARCH_PARAMS,
  DEFAULT_PAGINATION,
} from "@/constants/admin/label";
import BaseSelectScroll from "@/components/prod/common/BaseSelectScroll.vue";

const { t } = useI18n();
const { searchParams, pagination, getListLabel } = useLabelStore();
const { selectedLabel, isOpenPopup, isEditing, isAddNew } =
  storeToRefs(useLabelStore());

const labelSearchTypeOptions = computed(() => [
  {
    cmcdDetlNm: t("product_platform.name"),
    cmcdDetlId: LABEL_SEARCH_TYPE.NAME,
  },
  {
    cmcdDetlNm: t("product_platform.code"),
    cmcdDetlId: LABEL_SEARCH_TYPE.CODE,
  },
]);

const handleSearchLabel = (): void => {
  if (isEditing.value || isAddNew.value) {
    isOpenPopup.value = true;
    return;
  }
  selectedLabel.value = null;
  searchParams.page = 1;
  getListLabel();
};

const handleResetLabelSearch = (): void => {
  if (isEditing.value || isAddNew.value) {
    isOpenPopup.value = true;
    return;
  }
  selectedLabel.value = null;
  Object.assign(searchParams, DEFAULT_SEARCH_PARAMS);
  Object.assign(pagination, DEFAULT_PAGINATION);
  getListLabel();
};
</script>

<style lang="scss" scoped>
.label-search-filter {
  display: flex;
  align-items: center;
  gap: 8px;
}
</style>
