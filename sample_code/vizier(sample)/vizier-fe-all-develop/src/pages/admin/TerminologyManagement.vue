<template>
  <div class="rounded-lg border border-[#666] flex h-full bg-white">
    <div class="relative z-10 rounded-lg min-h-[300px] w-full">
      <div class="pt-6 pb-3 rounded h-full flex flex-col">
        <div class="flex px-3 z-30 flex-wrap justify-between items-center">
          <div class="flex flex-nowrap justify-between items-center gap-2">
            <div class="flex flex-nowrap items-center">
              <div class="filter flex items-center gap-2">
                <base-input-text
                  v-model="searchParams.srchWord"
                  :width="'240px'"
                  :placeholder="'Search'"
                  :styles="'input-search'"
                  @keyup.enter="handleSearch"
                  @click:append-inner="handleSearch"
                />
                <div
                  class="w-[415px] h-[48px] flex justify-space-between px-2 rounded-lg border align-center"
                >
                  <div
                    class="flex items-center w-[41px] h-[20px] text-text-lighter"
                  >
                    Target
                  </div>
                  <v-checkbox
                    v-model="searchParams.vocaDivsCd"
                    :label="'Vocabs'"
                    value="VO"
                    :true-icon="TrueIcon"
                    :false-icon="FalseIcon"
                    density="compact"
                    class="custom-checkbox"
                  ></v-checkbox>
                  <v-checkbox
                    v-model="searchParams.vocaDivsCd"
                    :label="'Term'"
                    value="WO"
                    :true-icon="TrueIcon"
                    :false-icon="FalseIcon"
                    density="compact"
                    class="custom-checkbox"
                  ></v-checkbox>

                  <v-checkbox
                    v-model="searchParams.vocaDivsCd"
                    :label="'Domain'"
                    value="DO"
                    :true-icon="TrueIcon"
                    :false-icon="FalseIcon"
                    density="compact"
                    class="custom-checkbox"
                  ></v-checkbox>

                  <v-checkbox
                    v-model="searchParams.vocaDivsCd"
                    :label="'Code'"
                    value="CO"
                    :true-icon="TrueIcon"
                    :false-icon="FalseIcon"
                    density="compact"
                    class="custom-checkbox"
                  ></v-checkbox>
                </div>
                <base-select
                  v-model="searchParams.stndYn"
                  :label="'Standard'"
                  :density="'comfortable'"
                  :items="itemsType"
                  :item-title="'title'"
                  class="h-[48px] w-[120px]"
                />
              </div>

              <div>
                <SearchAndRefreshButton
                  class="ml-2"
                  @handle-search="handleSearch"
                  @handle-refresh="handleResetSearch"
                />
              </div>
            </div>
          </div>

          <div class="flex justify-center items-center ml-auto gap-2">
            <div class="text-[13px] mr-2">
              <BaseTotalSearchResult
                :total-search="totalSearchItems"
                :total-items="pagination.totalItems"
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
            class="pt-6"
            :class="'2xl:!max-h-[calc(100vh_-_301px)] !max-h-[calc(100vh_-_349px)]'"
            @double-clicked="handleRowClick"
          />
        </div>
      </div>
    </div>
  </div>

  <TermUpdatePopup
    v-if="openPopupTerm"
    v-model="openPopupTerm"
    :data="selectedItem"
    :form-type="formType"
  />

  <VocabUpdatePopup
    v-if="openPopupVocab"
    v-model="openPopupVocab"
    :data="selectedItem"
    :form-type="formType"
  />

  <DomainUpdatePopup
    v-if="openPopupDomain"
    v-model="openPopupDomain"
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
import { useTerminologyStore } from "@/store";
import { useSnackbarStore } from "@/store";
import {
  TERMINOLOGY_HEADERS,
  USE_YN_OPTION,
} from "@/constants/admin/terminology";
import { FORM_TYPE_OPTION } from "@/constants/admin/admin";
import { CODE_TYPE } from "@/constants/admin/code";
import DataTableCustom from "@/pages/admin/subs/DataTableCustom.vue";
import SearchAndRefreshButton from "@/components/prod/common/SearchAndRefreshButton.vue";
import BaseTotalSearchResult from "@/components/prod/common/BaseTotalSearchResult.vue";
import TrueIcon from "@/components/prod/icons/TrueIcon.vue";
import FalseIcon from "@/components/prod/icons/FalseIcon.vue";
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

const terminologyStore = useTerminologyStore();
const useSnackbar = useSnackbarStore();

const itemsType = computed(() => {
  return USE_YN_OPTION;
});

const totalSearchItems = computed(() => {
  return currentPageData.value?.length || 0;
});

const isLoadingTableData = ref(false);
const headerTable = ref(TERMINOLOGY_HEADERS);
const {
  searchParams,
  paginatedItems: currentPageData,
  pagination,
} = storeToRefs(useTerminologyStore());

const selectedItem = ref(undefined);

const formType = ref<string>(FORM_TYPE_OPTION.CREATE);

const openPopupTerm = ref(false);
const openPopupVocab = ref(false);
const openPopupDomain = ref(false);
const openPopupCommonCode = ref(false);

const handleSearch = async () => {
  await handleFetchData();
};

const handleResetSearch = async () => {
  searchParams.value = {
    srchWord: "",
    vocaDivsCd: [],
    stndYn: " ",
  };
};

const handleShowPopupTerm = () => {
  formType.value = FORM_TYPE_OPTION.CREATE;
  openPopupTerm.value = true;
};

const handleRowClick = (item: any) => {
  formType.value = FORM_TYPE_OPTION.UPDATE;

  selectedItem.value = item;
  if (item.vocaDivsCd === "WO") {
    openPopupVocab.value = true;
  } else {
    openPopupTerm.value = true;
  }
};

const handleShowPopupVocab = () => {
  formType.value = FORM_TYPE_OPTION.CREATE;
  openPopupVocab.value = true;
};

const handleShowPopupDomain = () => {
  openPopupDomain.value = true;
};

const handleShowPopupCode = () => {
  openPopupCommonCode.value = true;
};

const handleFetchData = async () => {
  try {
    isLoadingTableData.value = true;
    await terminologyStore.fetchTerminology();
    isLoadingTableData.value = false;
  } catch (error: any) {
    useSnackbar.showSnackbar(error?.errorMsg as string, "error");
    isLoadingTableData.value = false;
  }
};

onMounted(async () => {
  if (currentPageData.value.length === 0) {
    handleFetchData();
  }
});
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

:deep(.custom-checkbox .v-label) {
  font-family: Noto Sans KR;
  font-size: 13px;
  font-weight: 500;
  line-height: 19.5px;
  letter-spacing: 0.25px;
  text-align: left;
}
</style>
