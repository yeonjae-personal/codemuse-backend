<script setup lang="ts">
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-alpine.css";
import DomainTable from "@/pages/domain/subs/DomainTable.vue";
import DomainSearch from "@/pages/domain/subs/DomainSearch.vue";
import { DomainRequest } from "./type";
import { httpClient } from "@/utils/http-common";

const loading = ref(false);

// data
const dataList = ref([]);

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
    <DomainTable :data-list="dataList"></DomainTable>
  </div>
</template>
