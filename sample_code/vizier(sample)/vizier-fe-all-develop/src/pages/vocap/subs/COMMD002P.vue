<script setup lang="ts">
import { httpClient } from "@/utils/http-common";
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-alpine.css";
import useGlobalStore from "@/store/global.store";
import DomainTable from "@/pages/domain/subs/DomainTable.vue";
import DomainSearch from "@/pages/domain/subs/DomainSearch.vue";
import { DomainRequest } from "@/pages/domain/type";

const emit = defineEmits(["closeDialog"]);
const loading = ref(false);

const globalStore = useGlobalStore();

// data
const dataList = ref([]);
const selectedDomain = ref<any>(null);

// method
const handleSearchEvent = async (searchData: DomainRequest) => {
  await fetchData(searchData);
};

const fetchData = async (searchData?: DomainRequest) => {
  try {
    loading.value = true;
    const response = await httpClient.get(`/api/comm/domn/v1/list`, {
      params: searchData,
    });

    dataList.value = response.data.data;
  } catch (error) {
    console.error("Error fetching data:", error);
  } finally {
    loading.value = false;
  }
};

const handleSelectedRow = async (domain: any) => {
  selectedDomain.value = domain;
};

const handleDoubleClick = (domain: any) => {
  selectedDomain.value = domain;
  closeDialog(domain);
};

const closeDialog = (domain?: any) => {
  if (!domain || !selectedDomain.value) {
    emit("closeDialog");
    return;
  }
  emit("closeDialog", {
    domnId: selectedDomain.value.domnId,
    domnNm: selectedDomain.value.domnNm,
    domnDivsCd: selectedDomain.value.domnDivsCd,
    domnLen: selectedDomain.value.domnLen,
  });
};

const applySearch = async () => {
  const objectAlert: any = {
    text: "도메인을 선택해 주세요.",
    width: "500",
    class: "custom-btn",
  };

  if (selectedDomain.value === null) {
    await globalStore.openAlertConfirm(objectAlert);
    return;
  }
  closeDialog(selectedDomain.value);
};

onMounted(async () => {
  await fetchData({
    srchWord: "",
    useYn: "",
  });
});
</script>

<template>
  <div class="p-4 mx-auto prose md:px-6 prose-indigo sm:rounded-md">
    <DomainSearch @search="handleSearchEvent"></DomainSearch>
    <v-dialog v-model="loading" max-width="320" persistent contained>
      <v-list class="py-2" color="primary" elevation="12" rounded="lg">
        <v-list-item title="Application is loading...">
          <template #prepend>
            <div class="pe-4">
              <v-icon color="pink" size="x-large"></v-icon>
            </div>
          </template>

          <template #append>
            <v-progress-circular
              color="pink"
              indeterminate="disable-shrink"
              size="30"
              width="2"
            ></v-progress-circular>
          </template>
        </v-list-item>
      </v-list>
    </v-dialog>
    <domain-table
      :data-list="dataList"
      :is-popup="true"
      @selected-row="handleSelectedRow"
      @double-clicked="handleDoubleClick"
    ></domain-table>
    <div class="flex justify-end mt-4" style="height: auto">
      <div class="pr-2">
        <cf-button
          label="확인"
          rounded="lg"
          class="custom-btn"
          @click="applySearch"
        />
      </div>
      <div class="pr-2">
        <cf-button
          label="닫기"
          rounded="lg"
          class="custom-btn"
          @click="closeDialog"
        />
      </div>
    </div>
  </div>
</template>

<style scoped>
.custom-btn {
  color: #000000;
  border: 1px solid #828282;
  background-color: white;
}
</style>
