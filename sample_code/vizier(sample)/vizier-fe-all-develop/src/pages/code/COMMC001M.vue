<script setup lang="ts">
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-alpine.css";
import CmcdTable from "@/pages/code/subs/CmcdTable.vue";
import CodeSearch from "@/pages/code/subs/CodeSearch.vue";
import { CmcdRequest } from "@/pages/code/type";
import { httpClient } from "@/utils/http-common";

const loading = ref(false);
const dataList = ref([]);

const handleSearchEvent = async (searchData: CmcdRequest) => {
  await fetchData(searchData);
};

const fetchData = async (searchData?: CmcdRequest) => {
  try {
    loading.value = true;
    const response = await httpClient.get(`/api/comm/cmcd/v1/list`, {
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
    srchType: "",
    srchWord: "",
    useYn: "",
  });
});
</script>

<template>
  <div class="p-4 mx-auto prose md:px-6 prose-indigo sm:rounded-md">
    <CodeSearch @search="handleSearchEvent"></CodeSearch>
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
    <CmcdTable :data-list="dataList"></CmcdTable>
  </div>
</template>
