<template>
  <div class="rounded-lg border border-[#666] flex h-full bg-white">
    <div class="relative z-10 rounded-lg min-h-[300px] w-full">
      <div class="pt-6 pb-3 rounded h-full flex flex-col">
        <div class="flex px-6 z-30 flex-wrap justify-between items-center">
          <div class="flex flex-nowrap justify-between items-center gap-4">
            <div class="flex flex-nowrap items-center">
              <div class="filter flex items-center gap-2 ml-2">
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
                :total-items="pagination.totalItems ?? 0"
              />
            </div>

            <BaseButton
              :color="ButtonColorType.Secondary"
              :width="WIDTH_BUTTON.AUTO"
              @click="handleShowPopupVocab"
            >
              <v-icon class="mr-[6px]">mdi-plus</v-icon>
              Add Vocabs
            </BaseButton>

            <BaseButton
              :color="ButtonColorType.Secondary"
              :width="WIDTH_BUTTON.AUTO"
              @click="handleShowPopupTerm"
            >
              <v-icon class="mr-[6px]">mdi-plus</v-icon>
              Add Terms
            </BaseButton>

            <BaseButton
              :color="ButtonColorType.Secondary"
              :width="WIDTH_BUTTON.AUTO"
              @click="handleShowPopupDomain"
            >
              <v-icon class="mr-[6px]">mdi-plus</v-icon>
              Add Domain
            </BaseButton>

            <BaseButton
              :color="ButtonColorType.Secondary"
              :width="WIDTH_BUTTON.AUTO"
              @click="handleShowPopupCode"
            >
              <v-icon class="mr-[6px]">mdi-plus</v-icon>
              Add Code
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
            @double-clicked="handleRowDoubleClick"
          />
        </div>
      </div>
    </div>
  </div>

  <VocabUpdatePopup
    v-if="openPopupVocab"
    v-model="openPopupVocab"
    :form-type="FORM_TYPE_OPTION.CREATE"
  ></VocabUpdatePopup>

  <DomainUpdatePopup
    v-if="openPopupDomain"
    v-model="openPopupDomain"
    :data="selectedDomain"
    :form-type="formType"
  />

  <TermUpdatePopup
    v-if="openPopupTerm"
    v-model="openPopupTerm"
    :form-type="FORM_TYPE_OPTION.CREATE"
  />

  <CommonCodeUpdatePopup
    v-if="openPopupCommonCode"
    v-model="openPopupCommonCode"
    :form-type="FORM_TYPE_OPTION.CREATE"
    :code-type="CODE_TYPE.CODE_GROUP"
  />
</template>

<script setup lang="ts">
import { useSnackbarStore } from "@/store";
import { Domain } from "./types/domain";
import { CODE_TYPE } from "@/constants/admin/code";
import { FORM_TYPE_OPTION } from "@/constants/admin/admin";
import { DOMAIN_HEADERS, USE_YN_OPTION } from "@/constants/admin/domain";
import { useDomainStore } from "@/store";
import DataTableCustom from "@/pages/admin/subs/DataTableCustom.vue";
import SearchAndRefreshButton from "@/components/prod/common/SearchAndRefreshButton.vue";
import BaseTotalSearchResult from "@/components/prod/common/BaseTotalSearchResult.vue";
import { ButtonColorType } from "@/enums";
import { WIDTH_BUTTON } from "@/constants/index";
const CommonCodeUpdatePopup = defineAsyncComponent(
  () => import("@/pages/admin/subs/code/CommonCodeUpdatePopup.vue")
);

const DomainUpdatePopup = defineAsyncComponent(
  () => import("@/pages/admin/subs/domain/DomainUpdatePopup.vue")
);

const TermUpdatePopup = defineAsyncComponent(
  () => import("@/pages/admin/subs/term/TermUpdatePopup.vue")
);

const VocabUpdatePopup = defineAsyncComponent(
  () => import("@/pages/admin/subs/vocab/VocabUpdatePopup.vue")
);

const domainStore = useDomainStore();
const useSnackbar = useSnackbarStore();

const isLoadingTableData = ref(false);

const {
  searchParams,
  paginatedItems: currentPageData,
  pagination,
} = storeToRefs(useDomainStore());

const itemsType = computed(() => {
  return USE_YN_OPTION;
});

// TABLE
const headerTable = ref(DOMAIN_HEADERS);
const selectedValue = ref("name");

const totalSearchItems = computed(() => {
  return currentPageData.value.length || 0;
});

const handleSearch = async () => {
  await handleFetchData();
};

const handleResetSearch = async () => {
  searchParams.value = {
    srchWord: "",
    useYn: " ",
  };
};
const handleRowDoubleClick = (item: Domain) => {
  formType.value = FORM_TYPE_OPTION.UPDATE;
  selectedDomain.value = item;
  openPopupDomain.value = true;
};

onMounted(async () => {
  if (currentPageData.value.length === 0) {
    handleFetchData();
  }
});

const openPopupVocab = ref(false);
const handleShowPopupVocab = () => {
  openPopupVocab.value = true;
};

//  start domain form
const selectedDomain = ref<Domain | null>(null);
const formType = ref<string>(FORM_TYPE_OPTION.CREATE);
const openPopupDomain = ref(false);
const handleShowPopupDomain = () => {
  formType.value = FORM_TYPE_OPTION.CREATE;
  openPopupDomain.value = true;
};

const openPopupCommonCode = ref(false);
const handleShowPopupCode = () => {
  openPopupCommonCode.value = true;
};

const openPopupTerm = ref(false);
const handleShowPopupTerm = () => {
  openPopupTerm.value = true;
};

const handleFetchData = async () => {
  try {
    isLoadingTableData.value = true;
    await domainStore.fetchDomains();
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

.custom-button {
  width: 140px;
  height: 48px;
  color: #ba1642;
  background-color: #fff0f2;
}
.custom-row {
  gap: 16px;
}
</style>
