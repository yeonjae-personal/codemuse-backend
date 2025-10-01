<template>
  <BasePopup
    v-model="isOpen"
    :title="$t('product_platform.screenEntity.screenSearch')"
    :size="DialogSizeType.Medium"
  >
    <template #body>
      <div class="w-[800px] pt-6 gap-2">
        <!-- -------------- start row 1-------------- -->
        <div
          class="flex gap-2 px-6 flex-nowrap items-center w-[752px] h-[48px] mb-2"
        >
          <div class="flex-1">
            <base-input-text
              v-model="searchParams.scrnId"
              :width="'120px'"
              :placeholder="$t('product_platform.screenEntity.screenId')"
              :styles="'input-search'"
              class="h-[48px] w-[120px]"
              @keyup.enter="handleSearch"
              @click:append-inner="handleSearch"
            />
          </div>
          <div class="flex-1">
            <base-input-text
              v-model="searchParams.scrnNm"
              :width="'308px'"
              :placeholder="$t('product_platform.screenEntity.screenName')"
              :styles="'input-search'"
              class="h-[48px] w-[308px]"
              @keyup.enter="handleSearch"
              @click:append-inner="handleSearch"
            />
          </div>
          <div class="flex-1">
            <base-input-text
              v-model="searchParams.scrnPathNm"
              :width="'308px'"
              :placeholder="$t('product_platform.screenEntity.screenPath')"
              :styles="'input-search'"
              class="h-[48px] w-[308px]"
              @keyup.enter="handleSearch"
              @click:append-inner="handleSearch"
            />
          </div>
        </div>
        <div
          class="flex gap-2 px-6 flex-nowrap items-center w-[752px] h-[48px]"
        >
          <base-input-text
            v-model="searchParams.rgstUsrNm"
            :width="'250px'"
            :placeholder="$t('product_platform.screenEntity.registrant')"
            :styles="'input-search'"
            :readonly="true"
            class="h-[48px] !min-w-[250px]"
            @keyup.enter="handleSearch"
            @click:append-inner="handleSearch"
          >
            <template #append-inner>
              <div class="flex flex-row gap-1">
                <BaseButton
                  :color="ButtonColorType.Gray"
                  :width="WIDTH_BUTTON.FOR_INPUT"
                  :height="HEIGHT_BUTTON.FOR_INPUT"
                  @click="showModalSelectRegistrant"
                >
                  <SearchIcon fill="#6B6D70" />
                </BaseButton>

                <BaseButton
                  :color="ButtonColorType.Gray"
                  :width="WIDTH_BUTTON.FOR_INPUT"
                  :height="HEIGHT_BUTTON.FOR_INPUT"
                  @click="resetValueUser('registrant')"
                >
                  <delete-icon :fill="'#6B6D70'" />
                </BaseButton>
              </div>
            </template>
          </base-input-text>
          <base-input-text
            v-model="searchParams.authAprvUsrNm"
            :width="'250px'"
            :placeholder="$t('product_platform.screenEntity.approver')"
            :styles="'input-search'"
            :readonly="true"
            class="h-[48px] !min-w-[250px]"
            @keyup.enter="handleSearch"
            @click:append-inner="handleSearch"
          >
            <template #append-inner>
              <div class="flex flex-row gap-1">
                <BaseButton
                  :color="ButtonColorType.Gray"
                  :width="WIDTH_BUTTON.FOR_INPUT"
                  :height="HEIGHT_BUTTON.FOR_INPUT"
                  @click="showModalSelectApprover"
                >
                  <SearchIcon fill="#6B6D70" />
                </BaseButton>

                <BaseButton
                  :color="ButtonColorType.Gray"
                  :width="WIDTH_BUTTON.FOR_INPUT"
                  :height="HEIGHT_BUTTON.FOR_INPUT"
                  @click="resetValueUser('approver')"
                >
                  <delete-icon :fill="'#6B6D70'" />
                </BaseButton>
              </div>
            </template>
          </base-input-text>
          <div class="w-[140px]">
            <base-select
              v-model="searchParams.authCtrlYn"
              :width="'140px'"
              :label="$t('product_platform.screenEntity.permissionControl')"
              :density="'comfortable'"
              :items="permissionControlOptions"
              :item-title="'title'"
              :item-value="'value'"
              class="h-[48px] w-[140px]"
              :default-item-select-all="false"
            />
          </div>
          <div class="flex-1">
            <div class="w-[88px]">
              <SearchAndRefreshButton
                @handle-search="handleSearch"
                @handle-refresh="handleResetSearch"
              />
            </div>
          </div>
        </div>

        <!-- -------------- start row 2-------------- -->
        <div class="flex justify-between items-center w-ful px-6 pt-3 py-2">
          <div class="font-weight-medium font-base font-size-base">
            <p>{{ $t("product_platform.screenEntity.screenList") }}</p>
          </div>
          <div>
            <BaseTotalSearchResult
              :total-search="totalSearchItems"
              :total-items="pagination.totalItems"
            />
          </div>
        </div>
        <div class="max-h-[336px] flex-grow table-screen-popup px-6">
          <DataTableCustom
            v-model:pageSize="pagination.pageSize"
            v-model:current-page="pagination.currentPage"
            :headers="headerTable"
            :data="currentPageData"
            :loading="isLoadingTableData"
            :total-items="pagination.totalItems || 0"
            :total-pages="pagination.totalPages || 0"
            @click-detail="clickDetail"
          />
        </div>
      </div>
    </template>

    <template #footer>
      <div class="flex justify-end gap-3">
        <BaseButton :size="ButtonSizeType.Large" @click="handleConfirm()">
          {{ $t("product_platform.commonAdmin.confirm") }}
        </BaseButton>
        <BaseButton
          :size="ButtonSizeType.Large"
          :color="ButtonColorType.Gray"
          @click="closeDialog()"
        >
          {{ t("product_platform.cancel") }}
        </BaseButton>
      </div>
    </template>
  </BasePopup>
  <UserOrgPopup
    v-if="openPopupScreenRegistrant"
    v-model="openPopupScreenRegistrant"
    @selected-item="onSelectItemRegistrant"
  />
  <UserOrgPopup
    v-if="openPopupScreenApprover"
    v-model="openPopupScreenApprover"
    @selected-item="onSelectItemApprover"
  />
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";
import { ButtonColorType, ButtonSizeType, DialogSizeType } from "@/enums";
import { useSnackbarStore, useScreenStore } from "@/store";
import { httpClient } from "@/utils/http-common";
import DataTableCustom from "@/pages/admin/subs/DataTableCustom.vue";
import { HEIGHT_BUTTON, WIDTH_BUTTON } from "@/constants/index";

const UserOrgPopup = defineAsyncComponent(
  () => import("@/pages/admin/subs/user/UserOrgPopup.vue")
);

const screenStore = useScreenStore();
const { paginatedItems: currentPageData, pagination } =
  storeToRefs(useScreenStore());
const emit = defineEmits(["update:modelValue", "selectedItem"]);
const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false,
  },
  selectedItem: {
    type: Object as PropType<any>,
    default: () => {},
  },
});

const isLoadingTableData = ref(false);
const itemSelected = ref(null);
const headerTable = computed(() => {
  return [
    {
      title: t("product_platform.commonAdmin.select"),
      align: "start",
      sortable: false,
      key: "screenNo",
      class: "header",
    },
    {
      title: t("product_platform.screenEntity.screenId"),
      align: "start",
      sortable: false,
      key: "scrnId",
      class: "header",
    },
    {
      title: t("product_platform.screenEntity.screenName"),
      align: "start",
      sortable: false,
      key: "scrnNm",
      class: "header",
    },
    {
      title: t("product_platform.screenEntity.description"),
      align: "start",
      sortable: false,
      key: "scrnDscr",
      class: "header",
    },
    {
      title: t("product_platform.screenEntity.enabled"),
      align: "center",
      sortable: false,
      key: "enableDisable",
      class: "header",
    },
    {
      title: t("product_platform.screenEntity.permissionControl"),
      align: "center",
      sortable: false,
      key: "control",
      class: "header",
    },
    {
      title: t("product_platform.screenEntity.screenLinkUrl"),
      align: "start",
      sortable: false,
      key: "scrnLinkUrl",
      class: "header",
    },
    {
      title: t("product_platform.screenEntity.screenPath"),
      align: "start",
      sortable: false,
      key: "scrnPathNm",
      class: "header",
    },
    {
      title: t("product_platform.screenEntity.registrant"),
      align: "start",
      sortable: false,
      key: "rgstUsrNm",
      class: "header",
    },
    {
      title: t("product_platform.screenEntity.approver"),
      align: "start",
      sortable: false,
      key: "authAprvUsrNm",
      class: "header",
    },
    {
      title: t("product_platform.screenEntity.revisionDate"),
      align: "start",
      sortable: false,
      key: "updDtm",
      class: "header",
    },
  ];
});
const permissionControlOptions = computed(() => {
  return [
    { title: t("product_platform.commonAdmin.all"), value: " " },
    { title: t("product_platform.commonAdmin.enabled"), value: "Y" },
    { title: t("product_platform.commonAdmin.disabled"), value: "N" },
  ];
});
const searchParams = ref<any>({
  scrnId: "",
  scrnNm: "",
  scrnPathNm: "",
  rgstUsrId: "",
  rgstUsrNm: "",
  authAprvUsrId: "",
  authAprvUsrNm: "",
  authCtrlYn: " ",
  actv_yn: "Y",
});

const dataListUser = ref([]);
const openPopupScreenRegistrant = ref(false);
const openPopupScreenApprover = ref(false);

const { t } = useI18n();
const useSnackbar = useSnackbarStore();

// computed
const isOpen = computed({
  get() {
    return props.modelValue;
  },
  set(newValue) {
    emit("update:modelValue", newValue);
  },
});

const totalSearchItems = computed(() => {
  return currentPageData.value.length || 0;
});

const showModalSelectApprover = () => {
  openPopupScreenApprover.value = true;
};

const clickDetail = (item: any) => {
  itemSelected.value = item;
};

const onSelectItemRegistrant = (item) => {
  searchParams.value = {
    ...searchParams.value,
    rgstUsrId: item.userId,
    rgstUsrNm: item.userNm,
  };
};

const onSelectItemApprover = (item) => {
  searchParams.value = {
    ...searchParams.value,
    authAprvUsrId: item.userId,
    authAprvUsrNm: item.userNm,
  };
};
const showModalSelectRegistrant = () => {
  openPopupScreenRegistrant.value = true;
};

const fetchDataUser = async () => {
  try {
    const response = await httpClient.get(`/api/comm/user/userInfo/v1/list`);

    if (response.data.data) {
      dataListUser.value = response.data.data;
    }
  } catch (error) {
    console.error("Error fetching data:", error);
  }
};

const resetValueUser = (key) => {
  if (key === "registrant") {
    searchParams.value = {
      ...searchParams.value,
      rgstUsrId: "",
      rgstUsrNm: "",
    };
  } else {
    searchParams.value = {
      ...searchParams.value,
      authAprvUsrId: "",
      authAprvUsrNm: "",
    };
  }
};

const handleResetSearch = async () => {
  searchParams.value = {
    scrnId: "",
    scrnNm: "",
    scrnPathNm: "",
    rgstUsrId: "",
    rgstUsrNm: "",
    authAprvUsrId: "",
    authAprvUsrNm: "",
    authCtrlYn: " ",
  };
  itemSelected.value = null;
  handleSearch();
};

const handleSearch = async () => {
  isLoadingTableData.value = true;
  const { scrnId, scrnNm, scrnPathNm, rgstUsrId, authAprvUsrId, authCtrlYn } =
    searchParams.value;
  const request: any = {
    scrnId: scrnId.trim() || null,
    scrnNm: scrnNm.trim() || null,
    scrnPathNm: scrnPathNm.trim() || null,
    actvYn: "Y",
    authCtrlYn: authCtrlYn.trim() || null,
    rgstUsrId: rgstUsrId.trim() || null,
    authAprvUsrId: authAprvUsrId.trim() || null,
  };
  screenStore.setCurrentPage(1);
  await screenStore.fetchScreenManagement(request);
  itemSelected.value = null;
  isLoadingTableData.value = false;
};

const closeDialog = () => {
  isOpen.value = false;
};

const handleConfirm = () => {
  if (itemSelected.value) {
    emit("selectedItem", itemSelected.value);
    closeDialog();
    screenStore.setCurrentPage(1);
  } else {
    useSnackbar.showSnackbar(
      t("product_platform.commonAdmin.plsSelectOne"),
      "error"
    );
  }
};

onMounted(async () => {
  isLoadingTableData.value = true;
  await screenStore.fetchScreenManagement({ actvYn: "Y" });
  await fetchDataUser();
  isLoadingTableData.value = false;
});

watch(
  () => pagination,
  () => {
    itemSelected.value = null;
  },
  { deep: true, immediate: true }
);
</script>

<style lang="scss" scoped>
:deep(.v-data-table-header__content) {
  font-family: Noto Sans KR;
  font-size: 13px;
  font-weight: 500;
  line-height: 19.5px;
}
:deep(.v-switch--inset .v-switch__thumb) {
  height: 16px;
  width: 16px;
  left: -1px !important;
}

:deep(.v-switch--inset .v-switch__track) {
  height: 20px;
  min-width: 36px;
}
:deep(.v-switch__track) {
  opacity: 1;
  background-color: rgb(220 224 228);
}

:deep(.table-screen-popup) {
  .v-table {
    max-height: 278px !important;
  }
  .v-table__wrapper {
    border: solid 1px rgba(230, 233, 237, 1) !important;
    border-radius: 8px !important;
  }
}
</style>
