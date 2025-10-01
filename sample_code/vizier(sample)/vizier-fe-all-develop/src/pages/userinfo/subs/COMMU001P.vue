<script setup lang="ts">
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-alpine.css";
import UserInfoTable from "@/pages/userinfo/subs/UserInfoTable.vue";
import UserInfoSearch from "@/pages/userinfo/subs/UserInfoSearch.vue";
import { userRequest } from "@/pages/userinfo/type";
import { httpClient } from "@/utils/http-common";

const loading = ref(false);
const emit = defineEmits(["closeDialog"]);
const dataList = ref([]);

const handleSearchEvent = async (searchData: userRequest) => {
  await fetchData(searchData);
};

const fetchData = async (searchData?: userRequest) => {
  try {
    loading.value = true;
    const response = await httpClient.get(`/api/comm/user/userInfo/v1/list`, {
      params: searchData,
    });

    dataList.value = response.data.data;
  } catch (error) {
    console.error("Error fetching data:", error);
  } finally {
    loading.value = false;
  }
};

const handelApplySelectedRow = (selectedRow: any) => {
  emit("closeDialog", selectedRow);
};

onMounted(async () => {
  await fetchData({
    userId: "",
    userNm: "",
    orgInfo: "",
  });
});
</script>

<template>
  <div class="p-4 mx-auto prose md:px-6 prose-indigo sm:rounded-md">
    <user-info-search @search="handleSearchEvent"></user-info-search>
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
    <user-info-table
      :data-list="dataList"
      :is-popup="true"
      @apply-selected-row="handelApplySelectedRow"
    ></user-info-table>
  </div>
</template>
