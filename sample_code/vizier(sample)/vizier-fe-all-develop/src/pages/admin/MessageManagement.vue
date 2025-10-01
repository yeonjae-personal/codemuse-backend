<template>
  <div class="rounded-lg border border-[#666] flex h-full bg-white">
    <div class="relative z-10 rounded-lg min-h-[300px] w-full">
      <div class="pt-6 pb-3 rounded h-full flex flex-col">
        <div class="flex px-6 z-30 flex-wrap justify-between items-center">
          <div class="flex flex-nowrap justify-between items-center gap-4">
            <div class="flex flex-nowrap items-center">
              <div class="filter flex items-center gap-2 ml-2">
                <base-input-text
                  v-model="searchParams.sysMsgId"
                  :min-width="'400px'"
                  :placeholder="'System Message ID'"
                  :styles="'input-search'"
                  @keyup.enter="handleFetchData"
                  @click:append-inner="handleFetchData"
                />

                <base-input-text
                  v-model="searchParams.sysMsgCntn"
                  :min-width="'400px'"
                  :placeholder="'System Message'"
                  :styles="'input-search'"
                  @keyup.enter="handleFetchData"
                  @click:append-inner="handleFetchData"
                />
              </div>

              <div class="ml-4">
                <SearchAndRefreshButton
                  @handle-search="handleFetchData"
                  @handle-refresh="handleResetSearch"
                />
              </div>
            </div>
          </div>

          <div class="flex justify-center items-center ml-auto gap-2">
            <div class="text-[13px] mr-6">
              <BaseTotalSearchResult
                :total-search="totalSearchItems"
                :total-items="pagination.totalItems ?? 0"
              />
            </div>
            <BaseButton :color="ButtonColorType.Gray" @click="handleEdit">
              <edit-icon :fill="'#6B6D70'" class="mr-[6px]" />
              Edit
            </BaseButton>

            <BaseButton
              :color="ButtonColorType.Secondary"
              @click="handleShowPopupMessage"
            >
              <v-icon class="mr-[6px]">mdi-plus</v-icon>
              Create
            </BaseButton>
          </div>
        </div>

        <div class="flex-grow">
          <DataTableCustom
            v-model:pageSize="pagination.pageSize"
            v-model:current-page="pagination.currentPage"
            :headers="headerTable"
            :data="currentPageData"
            :loading="isLoadingTableData"
            :total-items="pagination.totalItems || 0"
            :total-pages="pagination.totalPages || 0"
            :text-search="searchParams.sysMsgId"
            :search-field="selectedValue"
            class="pt-6"
            :class="'2xl:!max-h-[calc(100vh_-_301px)] !max-h-[calc(100vh_-_349px)]'"
            @click-detail="clickDetail"
            @double-clicked="handleRowDoubleClick"
          />
        </div>
      </div>
    </div>
  </div>
  <SysMessageUpdatePopup
    v-if="openPopupMessage"
    v-model="openPopupMessage"
    :data="selectedItem"
    :form-type="formType"
    @close-popup="handleClosePopupMessage"
    @reset-selected-item="selectedItem = undefined"
  />
</template>

<script setup lang="ts">
import { useSnackbarStore } from "@/store";
import { useSysMessageStore } from "@/store";
import { ButtonColorType } from "@/enums";
import { COLUMN_HEADERS } from "@/constants/admin/sysMessage";
import { FORM_TYPE_OPTION } from "@/constants/admin/admin";
import DataTableCustom from "@/pages/admin/subs/DataTableCustom.vue";
import SearchAndRefreshButton from "@/components/prod/common/SearchAndRefreshButton.vue";
import BaseTotalSearchResult from "@/components/prod/common/BaseTotalSearchResult.vue";

const SysMessageUpdatePopup = defineAsyncComponent(
  () => import("@/pages/admin/subs/message/SysMessageUpdatePopup.vue")
);

const sysMessageStore = useSysMessageStore();
const useSnackbar = useSnackbarStore();

const totalSearchItems = computed(() => {
  return currentPageData.value.length || 0;
});

const {
  searchParams,
  paginatedItems: currentPageData,
  pagination,
} = storeToRefs(useSysMessageStore());

// TABLE
const isLoadingTableData = ref(false);
const headerTable = ref(COLUMN_HEADERS);
const selectedValue = ref("name");

// Popup
// SysMessageUpdatePopup
const selectedItem = ref<Record<string, any> | undefined>();
const formType = ref<string>(FORM_TYPE_OPTION.CREATE);
const openPopupMessage = ref(false);

const handleEdit = () => {
  if (!selectedItem.value) {
    useSnackbar.showSnackbar("Please select a row", "error");
    return;
  }
  formType.value = FORM_TYPE_OPTION.UPDATE;
  openPopupMessage.value = true;
};

const handleFetchData = async () => {
  try {
    isLoadingTableData.value = true;
    await sysMessageStore.fetchSysMessages();
    isLoadingTableData.value = false;
  } catch (error: any) {
    useSnackbar.showSnackbar(error?.errorMsg as string, "error");
    isLoadingTableData.value = false;
  }
};

const handleResetSearch = async () => {
  searchParams.value = {
    sysMsgId: "",
    sysMsgCntn: "",
  };
};

const handleRowDoubleClick = (item: any) => {
  formType.value = FORM_TYPE_OPTION.UPDATE;
  selectedItem.value = item;
  openPopupMessage.value = true;
};

const clickDetail = (item: any) => {
  selectedItem.value = item;
};

onMounted(async () => {
  if (currentPageData.value.length === 0) {
    handleFetchData();
  }
});

const handleShowPopupMessage = () => {
  formType.value = FORM_TYPE_OPTION.CREATE;
  openPopupMessage.value = true;
};

const handleClosePopupMessage = async () => {
  handleFetchData();
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
