<template>
  <div class="rounded-lg border border-[#666] flex h-full bg-white">
    <div class="relative z-10 rounded-lg min-h-[300px] w-full">
      <div class="pt-6 pb-3 rounded h-full flex flex-col">
        <div class="flex px-6 z-30 flex-wrap justify-between items-center">
          <div class="flex flex-nowrap justify-between items-center gap-4">
            <div class="flex flex-nowrap items-center">
              <div class="filter flex items-center gap-2 ml-2">
                <base-select
                  v-model="searchParams.srchType"
                  :label="'Category'"
                  :density="'comfortable'"
                  :items="itemsCategory"
                  :item-title="'title'"
                  class="h-[48px] w-[150px]"
                />

                <base-input-text
                  v-model="searchParams.srchWord"
                  :min-width="'400px'"
                  :placeholder="'Search'"
                  :styles="'input-search'"
                  @keyup.enter="handleSearch"
                  @click:append-inner="handleSearch"
                />
                <base-select
                  v-model="searchParams.useYn"
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

          <div class="flex justify-center items-center ml-auto gap-2">
            <div class="text-[13px] mr-6">
              <BaseTotalSearchResult
                :total-search="totalSearchItems"
                :total-items="pagination.totalItems"
              />
            </div>

            <BaseButton
              :color="ButtonColorType.Gray"
              :width="WIDTH_BUTTON.AUTO"
              @click="handleShowPopUp(CODE_TYPE.CODE_GROUP)"
            >
              <v-icon class="mr-[6px]">mdi-plus</v-icon>
              Add Code Group
            </BaseButton>

            <BaseButton
              :color="ButtonColorType.Secondary"
              :width="WIDTH_BUTTON.AUTO"
              @click="handleShowPopUp(CODE_TYPE.CODE_DETAIL)"
            >
              <v-icon class="mr-[6px]">mdi-plus</v-icon>
              Add Code Details
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
            :search-field="selectedValue"
            class="pt-6"
            :class="'2xl:!max-h-[calc(100vh_-_301px)] !max-h-[calc(100vh_-_349px)]'"
            @click-detail="clickDetail"
          />
        </div>
      </div>
    </div>
  </div>
  <CommonCodeUpdatePopup
    v-if="openPopupCommonCode"
    v-model="openPopupCommonCode"
    :data="selectedCommonCode"
    :form-type="formType"
    :code-type="codeType"
  />
</template>

<script setup lang="ts">
import {
  SEARCH_TYPE_OPTION,
  USE_YN_OPTION,
  COLUMN_HEADERS,
  CODE_TYPE,
} from "@/constants/admin/code";
import { useCmCodeStore } from "@/store";
//components
import { useSnackbarStore } from "@/store";
import DataTableCustom from "@/pages/admin/subs/DataTableCustom.vue";
import SearchAndRefreshButton from "@/components/prod/common/SearchAndRefreshButton.vue";
import BaseTotalSearchResult from "@/components/prod/common/BaseTotalSearchResult.vue";
import { FORM_TYPE_OPTION } from "@/constants/admin/admin";
import { ButtonColorType } from "@/enums";
import { WIDTH_BUTTON } from "@/constants/index";

const CommonCodeUpdatePopup = defineAsyncComponent(
  () => import("@/pages/admin/subs/code/CommonCodeUpdatePopup.vue")
);

const commonCodeStore = useCmCodeStore();
const useSnackbar = useSnackbarStore();
const {
  searchParams,
  paginatedItems: currentPageData,
  pagination,
} = storeToRefs(useCmCodeStore());

const itemsCategory = computed(() => {
  return SEARCH_TYPE_OPTION;
});
const totalSearchItems = computed(() => {
  return currentPageData.value.length || 0;
});

const itemsType = computed(() => {
  return USE_YN_OPTION;
});

const isLoadingTableData = ref(false);
const headerTable = ref(COLUMN_HEADERS);
const selectedValue = ref("name");

const openPopupCommonCode = ref(false);
const selectedCommonCode = ref(null);
const formType = ref<string>("");

const codeType = ref();

const handleSearch = async () => {
  await handleFetchData();
};

const handleResetSearch = async () => {
  searchParams.value = {
    srchType: " ",
    srchWord: "",
    useYn: " ",
  };
};

onMounted(async () => {
  if (currentPageData.value.length === 0) {
    handleFetchData();
  }
});

const clickDetail = (item: any) => {
  selectedCommonCode.value = item;
};

const handleShowPopUp = (type: string) => {
  if (!selectedCommonCode.value && type === CODE_TYPE.CODE_DETAIL) {
    useSnackbar.showSnackbar("Please select a row", "error");
    return;
  }
  formType.value = FORM_TYPE_OPTION.CREATE;
  codeType.value = type;
  openPopupCommonCode.value = true;
};

const handleFetchData = async () => {
  try {
    isLoadingTableData.value = true;
    await commonCodeStore.fetchData();
    isLoadingTableData.value = false;
  } catch (error: any) {
    useSnackbar.showSnackbar(error?.errorMsg as string, "error");
    isLoadingTableData.value = false;
  }
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
