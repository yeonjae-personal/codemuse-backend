<template>
  <FourColumns>
    <FactorTypeSearch />
    <FactorTypeDetail v-if="factorTypeSelected?.factorTypeCode" />
    <FactorDetail
      v-if="factorDetail"
      :is-create="isCreateFactorDetail"
      @edit-success="handleEditFactorTypeChildSucces"
      @add-factor-success="handleAddFactorTypeChildSucces"
      @cancel-create-factor="handleCancelAddFactorTypeChild"
      @cancel-edit-factor="handleCancelEditFactor"
    />
  </FourColumns>
</template>
<script lang="ts" setup>
import useFactorStore from "@/store/admin/factor.store";
import cloneDeep from "lodash-es/cloneDeep";

const factorStore = useFactorStore();
const {
  factorTypeDetail,
  factorTypeSelected,
  factorDetail,
  isCreateFactorDetail,
  factorSelected,
  isEditFactorDetail,
  isAddNewFactorChild,
  factorTypeDetailBeforeEdit,
  paginationFactorDetail,
} = storeToRefs(factorStore);

const handleAddFactorTypeChildSucces = () => {
  isEditFactorDetail.value = false;
  isCreateFactorDetail.value = false;
  isAddNewFactorChild.value = false;
  factorTypeDetailBeforeEdit.value = cloneDeep(factorTypeDetail.value);
  paginationFactorDetail.value = {
    totalSearchItems: factorDetail.value.factorValueLst?.length,
    currentPage: Math.ceil(factorDetail.value.factorValueLst?.length / 10),
    pageSize: 10,
    totalItems: factorDetail.value.factorValueLst?.length,
    totalPages: Math.ceil(factorDetail.value.factorValueLst?.length / 10),
  };
};

const handleEditFactorTypeChildSucces = () => {
  factorTypeDetailBeforeEdit.value = cloneDeep(factorTypeDetail.value);
  paginationFactorDetail.value = {
    totalSearchItems: factorDetail.value.factorValueLst?.length,
    currentPage: Math.ceil(factorDetail.value.factorValueLst?.length / 10),
    pageSize: 10,
    totalItems: factorDetail.value.factorValueLst?.length,
    totalPages: Math.ceil(factorDetail.value.factorValueLst?.length / 10),
  };
};

const handleCancelAddFactorTypeChild = async () => {
  if (
    factorTypeDetail.value.pagination.totalItems %
      factorTypeDetail.value.pagination.pageSize ===
    1
  ) {
    factorTypeDetail.value.pagination.currentPage = 1;
  }
  factorDetail.value = null;
  factorSelected.value = null;
  isEditFactorDetail.value = false;
  isCreateFactorDetail.value = false;
  isAddNewFactorChild.value = false;
  if (factorTypeDetail.value?.factorLst?.length) {
    factorTypeDetail.value.factorLst = factorTypeDetail.value?.factorLst.filter(
      (item) => !item.isNew
    );
  }
};

const handleCancelEditFactor = (detail) => {
  factorTypeDetail.value = cloneDeep(factorTypeDetailBeforeEdit.value);
  if (factorTypeDetail.value.factorLst?.length) {
    const index = factorTypeDetail.value.factorLst.findIndex(
      (item) => item?.factorCode === detail?.factorCode
    );
    if (index !== -1) {
      factorDetail.value = factorTypeDetail.value?.factorLst[index as number];
    }
  }
};
</script>
