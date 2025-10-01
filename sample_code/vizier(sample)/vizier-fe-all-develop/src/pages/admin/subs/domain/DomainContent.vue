<template>
  <div class="rounded-lg flex h-full bg-white">
    <div class="relative z-10 rounded-lg min-h-[300px] w-full">
      <div class="pt-6 pb-3 rounded h-full flex flex-col">
        <div class="flex px-6 z-30 flex-wrap justify-between items-center">
          <div class="flex flex-nowrap justify-between items-center gap-4">
            <div class="flex flex-nowrap items-center">
              <div class="filter flex items-center gap-2">
                <base-input-text
                  v-model="srchWord"
                  :min-width="'348px'"
                  :placeholder="'Search'"
                  :styles="'input-search'"
                  @keyup.enter="handleSearch"
                  @click:append-inner="handleSearch"
                />
                <base-select
                  v-model="itemTypeSelected"
                  :label="'Usage'"
                  :density="'comfortable'"
                  :items="itemsType"
                  :item-title="'title'"
                  class="h-[48px] w-[120px]"
                />
              </div>
              <div class="ml-4">
                <SearchAndRefreshButton
                  @handle-search="handleSearch"
                  @handle-refresh="handleResetSearch"
                />
              </div>
            </div>
          </div>
        </div>

        <div class="flex justify-between items-center w-ful mx-6">
          <div class="text-left">Domain List</div>
          <div class="text-[13px]">
            <BaseTotalSearchResult
              :total-search="totalSearchItems"
              :total-items="pagination.totalItems"
            />
          </div>
        </div>

        <div class="h-[322px] w-[640px]">
          <DomainLookupTable
            v-model:pageSize="pagination.pageSize"
            v-model:current-page="pagination.currentPage"
            :headers="headerTable"
            :data="currentPageData"
            :loading="isLoadingTableData"
            :total-items="pagination.totalItems || 0"
            :total-pages="pagination.totalPages || 0"
            :text-search="srchWord"
            :search-field="selectedValue"
            class="w-full"
            @click-detail="() => {}"
          >
          <template #item="{ item }">
            <tr :key="item.domnId" :class="{ 'selected-row': isSelected(item) }" @click="toggleSelection(item)">
              <td>
                <SelectionIcon
                  size="18"
                  fill="#6B6D70"
                  :selected="isSelected(item)"
                />
              </td>
              <td>
                <p1>{{ item.domnNm }}</p1>
              </td>
              <td>
                <p1>{{ item.domnGrpNm }}</p1>
              </td>
              <td>
                <p1>{{ item.domnEngNm }}</p1>
              </td>
              <td>
                <p1>{{ item.domnDivsNm }}</p1>
              </td>
              <td>
                <p1>{{ item.domnLen }}</p1>
              </td>
            </tr>
          </template>
          </DomainLookupTable>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { DOMAIN_LOOKUP_HEADERS, USE_YN_OPTION } from "@/constants/admin/domain";
import { useDomainPopupStore } from "@/store";

//components
import DomainLookupTable from "@/pages/admin/subs/domain/DomainLookupTable.vue";
import SearchAndRefreshButton from "@/components/prod/common/SearchAndRefreshButton.vue";
import BaseTotalSearchResult from "@/components/prod/common/BaseTotalSearchResult.vue";
import { DomainSearchParam } from "@/interfaces/admin/admin";
import { Domain } from "../../types/domain";

 
const domainStore = useDomainPopupStore();
const isLoadingTableData = ref(false);

const srchWord = ref("");
const itemTypeSelected = ref(" ");
const itemsType = computed(() => {
  return USE_YN_OPTION;
});

// TABLE
const headerTable = ref(DOMAIN_LOOKUP_HEADERS);
const currentPageData = computed(() => domainStore.paginatedItems);
const pagination = computed(() => domainStore.getPagination);

const selectedValue = ref("name");

const totalSearchItems = computed(() => {
  return currentPageData.value.length || 0;
});

const handleSearch = async () => {
  isLoadingTableData.value = true;
  domainStore.setSelectedDomain({
    domnNm: "",
    domnDivsCd:  "",
    domnLen: ""
});
  const request: DomainSearchParam = {
    srchWord: srchWord.value?.trim(),
    useYn: itemTypeSelected.value?.trim(),
  };

  await domainStore.fetchDomains(request);
  isLoadingTableData.value = false;
};

const handleResetSearch = async () => {
  srchWord.value = "";
  itemTypeSelected.value = " ";
};

onUnmounted(async () => {
  await domainStore.fetchDomains({
    srchWord: "",
    useYn: "",
  });
});

onMounted(async () => {
  isLoadingTableData.value = true;
  await domainStore.fetchDomains({
    srchWord: "",
    useYn: "",
  });

  domainStore.setSelectedDomain({
    domnNm: "",
    domnDivsCd:  "",
    domnLen: ""
});
  isLoadingTableData.value = false;
});


const selectedDomain = ref<Domain | null>(null);
const isSelected = (item: Domain) => {
  return  item.domnId === selectedDomain.value?.domnId;
};

const toggleSelection = (item: Domain) => {
  selectedDomain.value = item;
  domainStore.setSelectedDomain({
    domnNm: item.domnNm,
    domnDivsCd: item.domnDivsCd,
    domnLen: item.domnLen,
});
};
 
</script>

<style scoped>
.custom-text-field .v-label {
  font-size: 13px;
}

select option {
  background-color: #fff;
  color: #363636;
  height: 40px;
}

.show-details {
  margin-left: unset !important;
  margin-top: 12px;
}

.option-show-details {
  min-width: 70px !important;
}

select option:hover {
  background-color: #ededed;
  color: red;
}

select option:checked {
  background-color: #ededed;
}

.select {
  max-width: 130px;
  height: 40px;
}

.active-icon {
  background-color: #fff;
  color: #ba1642;
}

.v-input__icon--append-inner {
  cursor: pointer;
}

.v-field {
  border-radius: 8px;
}

.v-text-field:deep() {
  font-size: 13px !important;
}

.v-field--variant-solo {
  box-shadow: none !important;
}

.v-field__input {
  font-size: 13px;
}

:deep(.v-label) {
  font-size: 13px !important;
}

:deep().v-input__details {
  display: none;
}
</style>
