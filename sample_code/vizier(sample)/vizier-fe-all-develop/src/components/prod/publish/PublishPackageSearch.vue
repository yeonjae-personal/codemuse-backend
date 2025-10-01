<template>
  <SearchPane
    ref="searchPane"
    title="product_platform.publish_package_search"
    container-class="rounded-[12px]"
    :model-list="publishSearch.items"
    :model-param="paramFilterPublishSearch"
    :type-select-require="false"
    :option-types="publishSearchStatusList"
    :pagination="publishSearch.pagination"
    :pane-col="ColNumber.One"
    :selected-item="publishSelected"
    :open-popup="openPopup"
    :popup-content="$t('product_platform.updatingConfirmSaved')"
    :item-height="64"
    :type-placeholder="'LB00000464'"
    :is-custom-search-result="checkEnvIsProd && isLoad"
    text-custom-search-result="Load result"
    @on-click-item="handleClickItem"
    @on-search="handleSearch"
    @on-reset="handleResetSearch"
    @close-popup="closePopup"
    @submit-popup="handleConfirm"
    @on-change-page="handleChangePage"
  >
    <template #search-button-append>
      <BaseButton
        class="ml-2"
        :color="ButtonColorType.Secondary"
        @click="handleCreatePublishPackage"
      >
        <AddLabelIcon class="mr-[6px]" />
        {{ $t("product_platform.commonAdmin.create") }}
      </BaseButton>
      <!-- <BaseButton
        v-if="checkEnvIsProd"
        class="ml-2"
        :color="ButtonColorType.Secondary"
        @click="onLoad"
      >
        Load
      </BaseButton>
      <BaseButton
        v-else
        class="ml-2"
        :color="ButtonColorType.Secondary"
        @click="handleCreatePublishPackage"
      >
        <AddLabelIcon class="mr-[6px]" />
        {{ $t("product_platform.commonAdmin.create") }}
      </BaseButton> -->
    </template>
    <template #custom-search-item="{ item }">
      <CardPublishItem
        class="publish-card-item"
        :title="item.itemName"
        :key-type-default="SearchBy.Name"
        :search-text="paramFilterPublishSearch.searchKey"
        :search-field="paramFilterPublishSearch.searchBy"
        :bg-color="getColorStatusPublish(item.itemType)?.bg"
        :bd-color="getColorStatusPublish(item.itemType)?.border"
        :text-color="getColorStatusPublish(item.itemType)?.text"
        :start-date="item?.crteDtm"
        :end-date="item?.exprDtm || item?.duedDtm"
        :text-type="
          publishSearchStatusList.find(
            (status) => status.cmcdDetlId === item?.itemType
          )?.cmcdDetlNm
        "
        :key-type="item.itemType"
        :active="publishSelected?.itemUnique === item?.itemUnique"
        :is-new="item?.isNew"
        @click="handleClickItem(item)"
      />
    </template>
  </SearchPane>
</template>

<script setup lang="ts">
import { useGroupCode } from "@/composables/useGroupCode";
import {
  INIT_PUBLISH_PACKAGE_CREATE,
  PUBLISH_CODE_STATUS,
  PUBLISH_GENERAL_ATTRIBUTES_FORM_LIST,
  getColorStatusPublish,
} from "@/constants/publish";
import { ButtonColorType, ColNumber, SearchBy } from "@/enums";
import { usePublishManagerStore } from "@/store";
import { cloneDeep } from "lodash-es";
import { v4 as uuidv4 } from "uuid";
import moment from "moment-timezone";
import { DATE_FORMAT, ENV_CONFIG } from "@/constants/index";
import { getUserInfor } from "@/constants/userInfor";
import { PUBLISH_TABS_VALUE } from "@/constants/publish";
import SearchPane from "@/components/prod/shared/SearchPane.vue";

const { chgUser, chgDeptName } = getUserInfor();
const openPopup = ref(false);
const preGetDetail = ref(false);
const preChangePage = ref(false);
const preResizePage = ref(false);
const localSaveItem = ref<any>(null);
const localPreChangePage = ref<any>(0);
const searchPane = ref<typeof SearchPane>();

const {
  getPublishSearch,
  resetParamPublishSearch,
  getPublishPackageDetail,
  checkAllActions,
  resetAllStepData,
  resetDataStep1,
  resetAllStepStatus,
  resetTabProcessStatus,
  requestLoad,
} = usePublishManagerStore();
const {
  paramFilterPublishSearch,
  publishSearch,
  publishSelected,
  publishSearchStatusList,
  publishModeList,
  isCreatePublish,
  isCreateStep1,
  currentTab,
  publishGeneralAttributesData,
  publishGeneralAttributesListForm,
  isLoad,
} = storeToRefs(usePublishManagerStore());

const { groupCodeData, search } = useGroupCode();

const handleChangePage = async (page, create = false) => {
  if (isCreatePublish.value) {
    localPreChangePage.value = page;
    preChangePage.value = true;
    openPopup.value = true;
    return;
  }
  paramFilterPublishSearch.value.page = page;
  searchPane.value?.calcTotalItem();
  paramFilterPublishSearch.value.size =
    (searchPane.value?.totalItem || 10) - (create ? 1 : 0);
  await getPublishSearch();
};

const handleSearch = async (size = 7, isClick = false, page = 1) => {
  paramFilterPublishSearch.value.page = isClick ? 1 : page;
  paramFilterPublishSearch.value.size = size;
  isLoad.value = false;
  if (isClick) {
    resetAllStepStatus();
    publishSelected.value = null;
  } else {
    if (checkAllActions()) {
      preResizePage.value = true;
      openPopup.value = true;
      return;
    }
  }
  await getPublishSearch();
};

const handleResetSearch = () => {
  resetParamPublishSearch();
  resetAllStepStatus();
};

const handleClickItem = async (item) => {
  if (item.isNew) {
    return;
  }
  if (
    checkAllActions() &&
    publishSelected.value?.itemUnique != item.itemUnique
  ) {
    localSaveItem.value = item;
    openPopup.value = true;
    preGetDetail.value = true;
    return;
  }
  resetAllStepStatus();
  publishSelected.value = item;
  currentTab.value = PUBLISH_TABS_VALUE.GENERAL_ATTRIBUTES;
  await getPublishPackageDetail(item.pubRqstTaskCode);
};

const handleCreatePublishPackage = async () => {
  if (checkAllActions()) {
    openPopup.value = true;
  } else {
    confirmCreateProcess();
  }
};

const checkEnvIsProd = computed(() => {
  return process.env.NODE_ENV === ENV_CONFIG.PRODUCTION;
});

const onLoad = () => {
  isLoad.value = true;
  requestLoad();
};

const handleConfirm = async () => {
  if (preGetDetail.value) {
    resetAllStepData();
    await getPublishSearch();
    publishSelected.value = localSaveItem.value;
    await getPublishPackageDetail(localSaveItem.value.pubRqstTaskCode);
    preGetDetail.value = false;
  } else if (preChangePage.value) {
    publishSelected.value = null;
    resetAllStepData();
    paramFilterPublishSearch.value.page = localPreChangePage.value;
    await getPublishSearch();
    preChangePage.value = false;
  } else if (preResizePage.value) {
    publishSelected.value = null;
    resetAllStepData();
    await getPublishSearch();
  } else {
    confirmCreateProcess();
  }
  currentTab.value = PUBLISH_TABS_VALUE.GENERAL_ATTRIBUTES;
  openPopup.value = false;
};

const closePopup = () => {
  openPopup.value = false;
};

const confirmCreateProcess = async () => {
  resetAllStepStatus();
  resetTabProcessStatus();
  resetDataStep1();
  currentTab.value = PUBLISH_TABS_VALUE.GENERAL_ATTRIBUTES;
  await handleChangePage(1, true);
  const uuid = uuidv4();
  publishGeneralAttributesData.value = {
    ...INIT_PUBLISH_PACKAGE_CREATE,
    pubRqstTaskPubr: chgUser,
    pubRqstTaskPubrDeptCd: chgDeptName,
  };
  publishGeneralAttributesListForm.value =
    PUBLISH_GENERAL_ATTRIBUTES_FORM_LIST.map((item) => ({
      ...item,
      attrVal: publishGeneralAttributesData.value[item.colName],
    }));
  publishSearch.value.items.unshift({
    ...INIT_PUBLISH_PACKAGE_CREATE,
    itemUnique: uuid,
    itemType: PUBLISH_CODE_STATUS.CREATED,
    pubRqstTaskCode: uuid,
    pubRqstTaskPubr: chgUser,
    pubRqstTaskPubrDeptCd: chgDeptName,
    crteDtm: moment().format(DATE_FORMAT.DATE_FORMAT_WITHOUT_TIME_REVERSE),
    pubRqstStusCode: PUBLISH_CODE_STATUS.CREATED,
    isNew: true,
  });
  publishSelected.value = cloneDeep(publishSearch.value.items[0]);
  isCreatePublish.value = true;
  isCreateStep1.value = true;
};

const reSearch = () => {
  if (searchPane.value) {
    searchPane.value.handleSearch();
  }
};

watch(
  () => publishGeneralAttributesData.value,
  () => {
    if (isCreatePublish.value && isCreateStep1.value) {
      publishSearch.value.items[0].itemName =
        publishGeneralAttributesData.value.pubRqstTaskCodeName;
    }
  },
  { deep: true }
);

onMounted(async () => {
  await search(["G00061", "G00064", "G00067", "G00068"]);
  publishSearchStatusList.value = cloneDeep(groupCodeData.value["G00061"]);
  publishModeList.value = cloneDeep(groupCodeData.value["G00068"]);
});

defineExpose({ reSearch });
</script>

<style lang="scss" scoped>
.filter {
  display: grid;
  grid-template-columns: 1fr 2fr;
}
</style>
