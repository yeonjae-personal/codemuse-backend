<template>
  <div class="px-[24px] pt-[24px]">
    <div class="flex justify-space-between align-center pb-[8px]">
      <div class="flex flex-nowrap items-center">
        <div class="filter flex items-center gap-2">
          <base-input-text
            v-model="searchParams.menuId"
            :width="'120px'"
            :placeholder="$t('product_platform.menuEntity.menuId')"
            :styles="'input-search'"
            class="w-[120px] !h-[48px]"
            rounded="4"
            @keyup.enter="handleSearch"
            @click:append-inner="handleSearch"
          />
          <base-input-text
            v-model="searchParams.scrnId"
            :width="'120px'"
            :placeholder="$t('product_platform.menuEntity.screenId')"
            :styles="'input-search'"
            class="w-[120px] !h-[48px]"
            rounded="4"
            @keyup.enter="handleSearch"
            @click:append-inner="handleSearch"
          />
          <base-input-text
            v-model="searchParams.menuNm"
            :width="'180px'"
            :placeholder="$t('product_platform.menuEntity.menuName')"
            :styles="'input-search'"
            class="w-[180px] !h-[48px]"
            rounded="4"
            @keyup.enter="handleSearch"
            @click:append-inner="handleSearch"
          />
          <base-input-text
            v-model="searchParams.rgstUsrNm"
            :readonly="true"
            :width="'180px'"
            :placeholder="$t('product_platform.menuEntity.registrant')"
            :styles="'input-search'"
            class="h-[48px] w-[180px]"
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
            :width="'180px'"
            :placeholder="$t('product_platform.menuEntity.approver')"
            :styles="'input-search'"
            :readonly="true"
            class="h-[48px] w-[180px]"
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
          <base-select
            v-model="searchParams.authCtrlYn"
            :width="'170px'"
            :label="$t('product_platform.menuEntity.permissionControl')"
            :density="'comfortable'"
            :items="permissionControlOptions"
            :item-title="'title'"
            :item-value="'value'"
            :default-item-select-all="false"
            class="h-[48px] !w-[170px]"
          />
          <SearchAndRefreshButton
            class="ml-[6px]"
            @handle-search="handleSearch"
            @handle-refresh="handleResetSearch"
          />
        </div>
      </div>
      <div class="flex gap-[8px]">
        <BaseButton
          :color="ButtonColorType.Gray"
          class="bg-light-blue-500 text-text-lighter"
          @click="handleShowPopup(FORM_TYPE_OPTION.UPDATE)"
        >
          <edit-icon :fill="'#6B6D70'" class="mr-[6px]" />
          {{ $t("product_platform.commonAdmin.edit") }}
        </BaseButton>
        <BaseButton
          :color="ButtonColorType.Secondary"
          @click="handleShowPopup(FORM_TYPE_OPTION.CREATE)"
        >
          <v-icon class="mr-[6px]">mdi-plus</v-icon>
          {{ $t("product_platform.commonAdmin.create") }}
        </BaseButton>
      </div>
    </div>
  </div>
  <BasePopup
    v-model="openPopup"
    :icon="isCancel ? DialogIconType.Warning : DialogIconType.Info"
    :submit-button-text="$t('product_platform.btn_yes')"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="
      isCancel
        ? $t('product_platform.desc_cancel')
        : $t('product_platform.desc_update')
    "
    @on-submit="handleSubmit"
  />
  <MenuUpdatePopup
    v-if="openPopupMenu"
    v-model="openPopupMenu"
    :data="selectedDomain"
    :form-type="formType"
    @reset-item-edit="resetItemEditSelected"
  />
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
import { ButtonColorType } from "@/enums";
import { DialogIconType } from "@/enums";
import { FORM_TYPE_OPTION } from "@/constants/admin/admin";
import { useSnackbarStore } from "@/store";
import { useMenuStoreInfo } from "@/store";
import { httpClient } from "@/utils/http-common";
import { useI18n } from "vue-i18n";
import SearchAndRefreshButton from "@/components/prod/common/SearchAndRefreshButton.vue";
import MenuUpdatePopup from "@/pages/admin/subs/menu/MenuUpdatePopup.vue";
import { HEIGHT_BUTTON, WIDTH_BUTTON } from "@/constants/index";

const UserOrgPopup = defineAsyncComponent(
  () => import("@/pages/admin/subs/user/UserOrgPopup.vue")
);

const { t } = useI18n();

const props = defineProps({
  itemSelected: {
    type: Object as PropType<any>,
    default: null,
  },
});

const menuStoreInfo = useMenuStoreInfo();
const useSnackbar = useSnackbarStore();

const openPopup = ref(false);
const isCancel = ref(false);
const openPopupScreenRegistrant = ref(false);
const openPopupScreenApprover = ref(false);

const emit = defineEmits(["searchMenu", "reset-item-menu-selected"]);

const dataListUser = ref([]);

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

const permissionControlOptions = computed(() => {
  return [
    { title: t("product_platform.commonAdmin.all"), value: " " },
    { title: t("product_platform.commonAdmin.enabled"), value: "Y" },
    { title: t("product_platform.commonAdmin.disabled"), value: "N" },
  ];
});

const showModalSelectRegistrant = () => {
  openPopupScreenRegistrant.value = true;
};

const showModalSelectApprover = () => {
  openPopupScreenApprover.value = true;
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
  };
  await menuStoreInfo.fetchMenuTree(request);
  let checkChange = false;
  Object.keys(request).forEach((key) => {
    if (request[key as string]) {
      checkChange = true;
    }
  });
  emit("searchMenu", checkChange);
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

const handleResetSearch = () => {
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
  handleSearch();
};

const handleSubmit = async () => {};

const selectedDomain = ref<any>(props.itemSelected);

const formType = ref();
const openPopupMenu = ref(false);

const handleShowPopup = (type: string) => {
  if (type === FORM_TYPE_OPTION.UPDATE) {
    if (selectedDomain.value) {
      formType.value = type;
      openPopupMenu.value = true;
    } else {
      useSnackbar.showSnackbar(
        t("product_platform.menuEntity.message.plsSelectMenu"),
        "error"
      );
    }
  } else {
    formType.value = type;
    openPopupMenu.value = true;
  }
};

const resetItemEditSelected = () => {
  emit("reset-item-menu-selected");
};

onMounted(async () => {
  await fetchDataUser();
});

watch(
  () => props.itemSelected,
  (newValue) => {
    if (newValue) {
      selectedDomain.value = newValue;
    }
  },
  { deep: true, immediate: true }
);
</script>

<style lang="scss" scoped>
.btn-custom:deep(span) {
  margin-left: 0px;
  color: #6b6d70;
}
</style>
