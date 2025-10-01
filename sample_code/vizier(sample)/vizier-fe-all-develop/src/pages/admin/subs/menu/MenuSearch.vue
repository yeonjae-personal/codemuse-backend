<template>
  <BasePopup
    v-model="isOpen"
    :title="$t('product_platform.menuEntity.menuSearch')"
    :size="DialogSizeType.ELarge"
  >
    <template #body>
      <div class="w-[1200px] pt-5 gap-2">
        <!-- -------------- start row 1-------------- -->
        <div
          class="flex gap-2 px-6 flex-nowrap items-center w-[1200px] h-[48px] mb-2"
        >
          <div class="flex-1">
            <base-input-text
              v-model="searchParams.menuId"
              :width="'140px'"
              :placeholder="$t('product_platform.menuEntity.menuId')"
              :styles="'input-search'"
              class="h-[48px] w-[140px]"
              @keyup.enter="handleSearch"
              @click:append-inner="handleSearch"
            />
          </div>
          <div class="flex-1">
            <base-input-text
              v-model="searchParams.scrnId"
              :width="'140px'"
              :placeholder="$t('product_platform.menuEntity.screenId')"
              :styles="'input-search'"
              class="h-[48px] w-[140px]"
              @keyup.enter="handleSearch"
              @click:append-inner="handleSearch"
            />
          </div>
          <div class="flex-1">
            <base-input-text
              v-model="searchParams.menuNm"
              :width="'199px'"
              :placeholder="$t('product_platform.menuEntity.menuName')"
              :styles="'input-search'"
              class="h-[48px] w-[199px]"
              @keyup.enter="handleSearch"
              @click:append-inner="handleSearch"
            />
          </div>
          <div class="flex-1">
            <base-input-text
              v-model="searchParams.rgstUsrNm"
              :readonly="true"
              :width="'199px'"
              :placeholder="$t('product_platform.menuEntity.registrant')"
              :styles="'input-search'"
              class="h-[48px] w-[199px]"
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
          </div>
          <div class="flex-1">
            <base-input-text
              v-model="searchParams.authAprvUsrNm"
              :width="'199px'"
              :placeholder="$t('product_platform.menuEntity.approver')"
              :styles="'input-search'"
              :readonly="true"
              class="h-[48px] w-[199px]"
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
          </div>
          <div class="flex-1">
            <base-select
              v-model="searchParams.authCtrlYn"
              :width="'140px'"
              :label="$t('product_platform.menuEntity.permissionControl')"
              :density="'comfortable'"
              :items="permissionControlOptions"
              :item-title="'title'"
              :item-value="'value'"
              :default-item-select-all="false"
              class="h-[48px] !w-[140px]"
            />
          </div>
          <div class="flex-1">
            <SearchAndRefreshButton
              @handle-search="handleSearch"
              @handle-refresh="handleResetSearch"
            />
          </div>
        </div>
        <div class="flex items-start w-ful px-6 mt-3 gap-3">
          <div
            class="!max-w-[360px] tree-menu-list rounded-[12px] overflow-y-auto overflow-x-hidden h-[627px]"
          >
            <TreeMenuSearch
              :is-search="isSearch"
              @set-item-selected="onChangeItemSelected"
            />
          </div>
          <div class="!max-w-[780px]">
            <DetailMenuContentSearch :item="itemSelected" />
          </div>
        </div>
      </div>
    </template>

    <template #footer>
      <div class="flex justify-end gap-3">
        <BaseButton @click="handleConfirm()">
          {{ $t("product_platform.commonAdmin.confirm") }}
        </BaseButton>
        <BaseButton :color="ButtonColorType.Gray" @click="closeDialog()">
          {{ t("product_platform.cancel") }}
        </BaseButton>
      </div>
    </template>
  </BasePopup>
  <UserOrgPopup
    v-if="openPopupRegistrant"
    v-model="openPopupRegistrant"
    @selected-item="onSelectItemRegistrant"
  />
  <UserOrgPopup
    v-if="openPopupApprover"
    v-model="openPopupApprover"
    @selected-item="onSelectItemApprover"
  />
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";
import { ButtonColorType, ButtonSizeType, DialogSizeType } from "@/enums";
import { useSnackbarStore, useMenuStoreInfo } from "@/store";
import { httpClient } from "@/utils/http-common";
import TreeMenuSearch from "@/pages/admin/subs/menu/TreeMenuSearch.vue";
import DetailMenuContentSearch from "@/pages/admin/subs/menu/DetailMenuContentSearch.vue";
import { HEIGHT_BUTTON, WIDTH_BUTTON } from "@/constants/index";

const UserOrgPopup = defineAsyncComponent(
  () => import("@/pages/admin/subs/user/UserOrgPopup.vue")
);

const menuStoreInfo = useMenuStoreInfo();

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

const itemSelected = ref<any>(null);
const isSearch = ref(false);
const permissionControlOptions = computed(() => {
  return [
    { title: t("product_platform.commonAdmin.all"), value: " " },
    { title: t("product_platform.commonAdmin.enabled"), value: "Y" },
    { title: t("product_platform.commonAdmin.disabled"), value: "N" },
  ];
});
const searchParams = ref({
  menuId: "",
  scrnId: "",
  menuNm: "",
  authCtrlYn: " ",
  rgstUsrId: "",
  rgstUsrNm: "",
  authAprvUsrId: "",
  authAprvUsrNm: "",
});
const dataListUser = ref<any[]>([]);

const openPopupRegistrant = ref(false);
const openPopupApprover = ref(false);

const { t } = useI18n();
const useSnackbar = useSnackbarStore();
const onChangeItemSelected = (item) => {
  itemSelected.value = item;
};

// computed
const isOpen = computed({
  get() {
    return props.modelValue;
  },
  set(newValue) {
    emit("update:modelValue", newValue);
  },
});

const showModalSelectApprover = () => {
  openPopupApprover.value = true;
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
  openPopupRegistrant.value = true;
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
    menuId: "",
    scrnId: "",
    menuNm: "",
    authCtrlYn: " ",
    rgstUsrId: "",
    rgstUsrNm: "",
    authAprvUsrId: "",
    authAprvUsrNm: "",
  };
  itemSelected.value = null;
  handleSearch();
};

const handleSearch = async () => {
  const { menuId, scrnId, menuNm, authCtrlYn, rgstUsrId, authAprvUsrId } =
    searchParams.value;

  const request: any = {
    menuId: menuId.trim() || null,
    scrnId: scrnId.trim() || null,
    menuNm: menuNm.trim() || null,
    authCtrlYn: authCtrlYn.trim() || null,
    rgstUsrId: rgstUsrId.trim() || null,
    authAprvUsrId: authAprvUsrId.trim() || null,
    levelNoFrom: "1",
    levelNoTo: "2",
    actvYn: "Y",
  };
  await menuStoreInfo.fetchMenuTreePopup(request);
  let checkChange = false;
  Object.keys({
    menuId: menuId.trim() || null,
    scrnId: scrnId.trim() || null,
    menuNm: menuNm.trim() || null,
    authCtrlYn: authCtrlYn.trim() || null,
    rgstUsrId: rgstUsrId.trim() || null,
    authAprvUsrId: authAprvUsrId.trim() || null,
  }).forEach((item) => {
    if (request[item as string]) {
      checkChange = true;
    }
  });
  isSearch.value = checkChange;
  itemSelected.value = null;
};

const closeDialog = () => {
  isOpen.value = false;
};

const handleConfirm = () => {
  if (itemSelected.value) {
    emit("selectedItem", itemSelected.value);
    closeDialog();
  } else {
    useSnackbar.showSnackbar(
      t("product_platform.menuEntity.message.plsSelectMenu"),
      "error"
    );
  }
};

onMounted(async () => {
  await fetchDataUser();
});
</script>

<style lang="scss" scoped>
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

:deep(.tree-menu-list) {
  border: 1px solid rgba(230, 233, 237, 1);
}
</style>
