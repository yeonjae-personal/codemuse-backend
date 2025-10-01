<template>
  <SearchPane
    ref="searchPane"
    title="product_platform.approval_flow_search"
    container-class="rounded-[12px]"
    :model-list="approvalFlowSearch.items"
    :pagination="approvalFlowSearch.pagination"
    :pane-col="ColNumber.One"
    :item-height="102"
    @on-search="handleSearch"
    @on-reset="handleResetSearch"
    @on-change-page="handleChangePage"
  >
    <template #custom-form>
      <div class="grid gap-2 mt-2">
        <div class="filter gap-2">
          <BaseSelectScroll
            v-model="paramFilterApprovalFlowSearch.searchBy"
            :height="48"
            :options="APPROVAL_CODE_TYPE"
            :show-error-massage="false"
            :default-item-select-all="false"
            :show-option-null="false"
          />
          <BaseInputSearch
            v-model="paramFilterApprovalFlowSearch.keyword"
            density="comfortable"
            label="search"
            variant="solo"
            hide-details
            single-line
            rounded="4"
            @handle-search="handleEnterSearch"
          />
        </div>
      </div>
    </template>
    <template #custom-search-item="{ item }">
      <CardApprovalItem
        key-type-default="aprvFlowTmptName"
        :search-text="paramFilterApprovalFlowSearch.keyword"
        :search-field="paramFilterApprovalFlowSearch.searchBy"
        class="approval-card-item"
        :item="item"
        :title="item.aprvFlowTmptName"
        :bd-color="getColorStatusApproval(item.aprvFlowTmptTypeCode)"
        :num-review="item.numReview"
        :num-approval="item.numApproval"
        :description="item.aprvFlowTmptDscr"
        :disabled="checkExist(item)"
        :draggable="!checkExist(item)"
      />
    </template>
  </SearchPane>
</template>
<script setup lang="ts">
import {
  APPROVAL_CODE_TYPE,
  getColorStatusApproval,
} from "@/constants/publish";
import { ColNumber } from "@/enums";
import { useApprovalStore, usePublishManagerStore } from "@/store";

const { getApprovalFlowSearch, resetParamApprovalFlowSearch } =
  useApprovalStore();
const publishManagerStore = usePublishManagerStore();
const { publishApprovalFlowData } = storeToRefs(publishManagerStore);
const { paramFilterApprovalFlowSearch, approvalFlowSearch } =
  storeToRefs(useApprovalStore());
const searchPane = ref();

const handleSearch = async (size, isClick, page) => {
  paramFilterApprovalFlowSearch.value.page = isClick ? 1 : page;
  paramFilterApprovalFlowSearch.value.size = size;
  await getApprovalFlowSearch();
};

const handleResetSearch = () => {
  resetParamApprovalFlowSearch();
};

const checkExist = (item) => {
  if (publishApprovalFlowData.value?.aprvFlowTmptCode) {
    return (
      publishApprovalFlowData.value?.aprvFlowTmptCode === item.aprvFlowTmptCode
    );
  }
  // return approvalStepList.value?.some(
  //   (appr) => appr.aprvFlowTmptCode === item.aprvFlowTmptCode
  // );
};

const handleChangePage = async (page) => {
  paramFilterApprovalFlowSearch.value.page = page;
  await getApprovalFlowSearch();
};

const handleEnterSearch = () => {
  searchPane.value?.handleSearch();
};

onMounted(() => {
  searchPane.value?.handleSearch();
});
</script>
<style lang="scss" scoped>
.filter {
  display: grid;
  grid-template-columns: 1fr 2fr;
}
</style>
