<script setup lang="ts">
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-alpine.css";
import { httpClient } from "@/utils/http-common";
import VocapTable from "@/pages/vocap/subs/VocapTable.vue";
import SearchPanel from "@/pages/vocap/subs/SearchPanel.vue";
import { SearchData } from "@/pages/vocap/type";

const loading = ref(false);
const dataList = ref([]);

const handleSearchEvent = async (searchData: SearchData) => {
  await fetchData(searchData);
};

const fetchData = async (searchData?: SearchData) => {
  try {
    loading.value = true;
    const response = await httpClient.post(
      `/api/comm/voca/v1/list`,
      searchData
    );
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
    vocaDivsCd: [],
    stndYn: "",
  });
});
</script>

<template>
  <div class="p-4 mx-auto prose md:px-6 prose-indigo sm:rounded-md">
    <search-panel @search="handleSearchEvent"></search-panel>
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
    <vocap-table :data-list="dataList"></vocap-table>
  </div>
</template>
