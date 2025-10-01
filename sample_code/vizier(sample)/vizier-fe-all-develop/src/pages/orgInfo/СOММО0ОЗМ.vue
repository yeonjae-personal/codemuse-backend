<!-- eslint-disable vue/multi-word-component-names -->
<script setup lang="ts">
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-alpine.css";
import OrgInfoTable from "@/pages/orgInfo/subs/OrgInfoTable.vue";
import OrgInfoSearch from "@/pages/orgInfo/subs/OrgInfoSearch.vue";
import { OrgSearchRequest } from "@/pages/orgInfo/type";
import { httpClient } from "@/utils/http-common";
import { TreeViewNodeItem } from "@/types/common";
import { convertToTree, getOrgCdExpanded } from "./OrgUtils";

const treeData = ref<any[]>([]);
const loading = ref(false);
const dataList = ref([]);
const opened = ref<string[]>([]);

// method
const handleSearchEvent = async (searchData: OrgSearchRequest) => {
  await fetchData(searchData);
};

const fetchData = async (searchData?: OrgSearchRequest) => {
  try {
    loading.value = true;
    const response = await httpClient.get(
      `/api/comm/org/orgInfo/v1/mgmt/list`,
      { params: searchData }
    );

    dataList.value = response.data.data;
    if (treeData.value.length == 0) {
      treeData.value = convertToTree(response.data.data);
      opened.value = getOrgCdExpanded(response.data.data);
    }
  } catch (error) {
    console.error("Error fetching data:", error);
  } finally {
    loading.value = false;
  }
};

const fetchDataWithOrgCd = async (orgCd: unknown = "100000") => {
  try {
    loading.value = true;
    const response = await httpClient.get(`/api/comm/org/orgInfo/v1/${orgCd}`);

    dataList.value = response.data.data;
  } catch (error) {
    console.error("Error fetching data:", error);
  } finally {
    loading.value = false;
  }
};

const handleNodeClick = (node: TreeViewNodeItem) => {
  // node.id is orgCd
  fetchDataWithOrgCd(node.id);
};

onMounted(async () => {
  await fetchData({
    orgInfo: "",
    orgKdCd: "",
    orgStatCd: "",
  });
});
</script>

<template>
  <div class="p-4 mx-auto prose md:px-6 prose-indigo sm:rounded-md">
    <org-info-search @search="handleSearchEvent"></org-info-search>
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
    <v-row>
      <v-col cols="4">
        <v-treeview
          :items="treeData"
          :expand-icon="'mdi-plus-circle'"
          :collapse-icon="'mdi-minus-circle'"
          :opened="opened"
          item-value="id"
          item-text="orgCd"
          item-children="children"
          @click:select="handleNodeClick"
          @click:open="handleNodeClick"
        />
      </v-col>
      <v-col cols="8">
        <org-info-table :data-list="dataList"></org-info-table>
      </v-col>
    </v-row>
  </div>
</template>
