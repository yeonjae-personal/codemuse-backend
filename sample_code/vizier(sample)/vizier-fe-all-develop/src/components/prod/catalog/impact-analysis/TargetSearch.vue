<template>
  <SearchPane
    ref="searchPaneTargetSearch"
    title="product_platform.impactAnalysis.targetSearchTitle"
    container-class="rounded-lg"
    :pane-col="ColNumber.One"
    :item-height="62"
    :model-param="localParamTargetSearch"
    :model-list="targetSearchList.items || []"
    :pagination="targetSearchList?.pagination"
    @on-change-page="handleChangePage"
    @on-search="handleSearch"
    @on-reset="handleResetForm"
  >
    <template #custom-form>
      <div class="middle-form flex flex-column gap-[8px]">
        <BaseSelectScroll
          ref="selectScroll"
          v-model="localParamTargetSearch.type"
          :options="largeTypeList"
          class="form-item w-[50%] text-[13px]"
          :placeholder="$t('product_platform.selectBoxItem')"
          required
          :rules="useInputValidation({ required: true })"
          :default-item-select-all="false"
          :height="48"
          @update:model-value="handleChangeType"
        />
        <TargetOffer
          v-if="
            localParamTargetSearch.type === TARGET_TYPE_CODE.OFFER ||
            !localParamTargetSearch.type
          "
          ref="targetOffer"
          v-model="localParamTargetSearch.subType"
          :detail-list="detailList"
          @update:model-value="handleChangeSubType"
        />
        <TargetComponent
          v-if="localParamTargetSearch.type === TARGET_TYPE_CODE.COMPONENT"
          ref="targetComponent"
          :detail-list="detailList"
          :detl-type="localParamTargetSearch.detlType"
          :sub-type="localParamTargetSearch.subType"
          @update:detl-type="handleChangeDetlType"
          @update:sub-type="handleChangeSubType"
        />
        <TargetResource
          v-if="localParamTargetSearch.type === TARGET_TYPE_CODE.RESOURCE"
          ref="targetResource"
          v-model="localParamTargetSearch.subType"
          :detail-list="detailList"
          @update:model-value="handleChangeSubType"
        />
        <TargetInputSearch
          ref="targetInputSearch"
          :prod-item-nm="localParamTargetSearch.prodItemNm"
          :prod-item-cd="localParamTargetSearch.prodItemCd"
          :select-name-code="selectedNmCd"
          @update:prod-item-nm="handleChangeItemName"
          @update:prod-item-cd="handleChangeItemCode"
          @update:select-name-code="handleNameCode"
          @on-search="handleSearch"
        />
      </div>
    </template>
    <template #custom-search-item="{ item }">
      <cf-card-dropdown
        v-if="paramTargetSearch.type === TARGET_TYPE_CODE.OFFER"
        ref="cardItem"
        :width="'100%'"
        :title="item.prodItemNm"
        :description="item.prodItemCd"
        :search-text="
          selectedNmCd === SearchBy.Name
            ? localParamTargetSearch.prodItemNm
            : localParamTargetSearch.prodItemCd
        "
        :search-field="selectedNmCd"
        type-bg="linear"
        :border-color-action="setHoverColor(item.subType)"
        :type-of-prod="setIconType(item.subType)"
        :is-device-icon="
          ['DE', 'DV'].includes(item.subType.slice(0, 2).toUpperCase())
        "
        :icon-color="setIconColor(item.subType)"
        :disable="isExpiredTime(item?.validEndDtm)"
        hide-detail
        :node="{
          hideNodeLeft: true,
          isActiveNodeLeft: false,
          hideNodeRight: true,
          isActiveNodeRight: false,
        }"
        :active="selectedSearchItem?.prodUuid === item?.prodUuid"
        :draggable="!isExpiredTime(item?.validEndDtm)"
        @on-click-card="onChooseCard(item)"
        @dragstart="
          handleDragUserPocket($event, {
            userPocketType: LARGE_ITEM_CODE.OFFER,
            ...item,
          })
        "
      >
      </cf-card-dropdown>
      <cf-card-dropdown
        v-if="paramTargetSearch.type === TARGET_TYPE_CODE.RESOURCE"
        ref="cardItem"
        :width="355"
        :title="item.prodItemNm"
        :description="item.prodItemCd"
        :search-text="
          selectedNmCd === SearchBy.Name
            ? localParamTargetSearch.prodItemNm
            : localParamTargetSearch.prodItemCd
        "
        :search-field="selectedNmCd"
        :disable="isExpiredTime(item?.validEndDtm)"
        type-bg="light"
        border-color-action="purple"
        dropdown-icon="mdi-dots-vertical"
        class-name="card-round-style"
        hide-detail
        :node="{
          hideNodeLeft: true,
          isActiveNodeLeft: false,
          hideNodeRight: true,
          isActiveNodeRight: false,
        }"
        :active="selectedSearchItem?.prodUuid === item?.prodUuid"
        :draggable="!isExpiredTime(item?.validEndDtm)"
        @on-click-card="onChooseCard(item)"
        @dragstart="
          handleDragUserPocket($event, {
            userPocketType: LARGE_ITEM_CODE.RESOURCE,
            ...item,
          })
        "
      >
        <template #icon>
          <span class="flex justify-center align-center w-[40px] h-[40px]">
            <template v-if="item.subType === RESOURCE_TYPE_FIELD[0].value">
              <RLinearIcon />
            </template>
            <template v-if="item.subType === RESOURCE_TYPE_FIELD[1].value">
              <BLinearIcon />
            </template>
            <template v-if="item.subType === RESOURCE_TYPE_FIELD[2].value">
              <SLinearIcon />
            </template>
          </span>
        </template>
      </cf-card-dropdown>
      <cf-card-dropdown
        v-if="paramTargetSearch.type === TARGET_TYPE_CODE.COMPONENT"
        ref="cardItem"
        :width="355"
        :disable="isExpiredTime(item?.validEndDtm)"
        :title="item.prodItemNm"
        :description="item.prodItemCd"
        :search-text="
          selectedNmCd === SearchBy.Name
            ? localParamTargetSearch.prodItemNm
            : localParamTargetSearch.prodItemCd
        "
        :search-field="selectedNmCd"
        type-bg="linear"
        :border-color-action="setHoverColor(item.detlType)"
        :display-border-left="setHoverColor(item.detlType)"
        hide-detail
        :node="{
          hideNodeLeft: true,
          isActiveNodeLeft: false,
          hideNodeRight: true,
          isActiveNodeRight: false,
        }"
        :active="selectedSearchItem?.prodUuid === item?.prodUuid"
        :draggable="!isExpiredTime(item?.validEndDtm)"
        @on-click-card="onChooseCard(item)"
        @dragstart="
          handleDragUserPocket($event, {
            userPocketType: LARGE_ITEM_CODE.COMPONENT,
            ...item,
          })
        "
      >
      </cf-card-dropdown>
    </template>
  </SearchPane>
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";
import cloneDeep from "lodash-es/cloneDeep";
import { RESOURCE_TYPE_FIELD, SPACE } from "@/constants/";
import { useImpactAnalysisStore, useSnackbarStore } from "@/store";
import {
  setIconType,
  setIconColor,
  setHoverColor,
} from "@/utils/impact-analysis-utils";
import { isExpiredTime } from "@/utils/format-data";
import { TARGET_TYPE_CODE } from "@/constants/impactAnalysis";
import { paramTargetSearchDefault } from "@/store/impact-analysis.store";
import { useInputValidation } from "@/composables/useInputValidation";
import useDragUserPocket from "@/composables/useDragUserPocket";
import { LARGE_ITEM_CODE } from "@/store/userPocket.store";
import { ColNumber, SearchBy } from "@/enums";

type Props = {
  largeTypeList: any[];
};

const props = defineProps<Props>();

const { t } = useI18n();

const impactAnalysisStore = useImpactAnalysisStore();
const { actionGetTargetSearchList, resetParamListView } =
  useImpactAnalysisStore();
const {
  targetSearchList,
  paramTargetSearch,
  selectedNmCd,
  redirectFormPocket,
  selectedSearchItem,
} = storeToRefs(useImpactAnalysisStore());
const useSnackbar = useSnackbarStore();
const { handleDragUserPocket } = useDragUserPocket();

const gridViewParams = inject("gridViewParams", {
  lctgrItemCode: SPACE,
  objName: "",
  objCode: "",
});

const cardItem = ref<any>(null);
const targetOffer = ref<any>(null);
const selectScroll = ref();
const targetComponent = ref<any>(null);
const targetResource = ref<any>(null);
const targetInputSearch = ref<any>(null);
const selectedUuid = ref<any>(null);
const localParamTargetSearch = ref<any>(cloneDeep(paramTargetSearchDefault));

onBeforeMount(() => {
  localParamTargetSearch.value = cloneDeep(
    impactAnalysisStore.getParamTargetSearch
  );
});

watch(
  paramTargetSearch,
  (value) => {
    localParamTargetSearch.value = cloneDeep(value);
  },
  { deep: true }
);

watch(redirectFormPocket, (value) => {
  if (value) {
    setTimeout(() => {
      redirectFormPocket.value = false;
    }, 500);
  }
});

const detailList = computed(() => {
  let list;
  switch (localParamTargetSearch.value.type) {
    case TARGET_TYPE_CODE.OFFER:
      list = props.largeTypeList.find(
        (item) => item.value === TARGET_TYPE_CODE.OFFER
      );
      return list?.subOptions[0]?.subOptions;
    case TARGET_TYPE_CODE.COMPONENT:
      list = props.largeTypeList.find(
        (item) => item.value === TARGET_TYPE_CODE.COMPONENT
      );
      return list?.subOptions;
    case TARGET_TYPE_CODE.RESOURCE:
      list = props.largeTypeList.find(
        (item) => item.value === TARGET_TYPE_CODE.RESOURCE
      );
      return list?.subOptions[0]?.subOptions;
    default:
      return [];
  }
});

const handleResetForm = () => {
  selectScroll.value?.resetValidate();
  localParamTargetSearch.value = cloneDeep(paramTargetSearchDefault);
  impactAnalysisStore.resetTargetSearch();
  gridViewParams.lctgrItemCode = SPACE;
  gridViewParams.objName = "";
  gridViewParams.objCode = "";
  impactAnalysisStore.resetState();
};

const handleSearch = async (
  size?: number,
  isClick: boolean = true,
  page = 1
) => {
  try {
    if (redirectFormPocket.value) return;
    selectScroll.value.validate();
    if (!localParamTargetSearch.value?.type) {
      useSnackbar.showSnackbar(
        t("product_platform.required_field_missing"),
        "error"
      );
      return;
    }
    if (isClick) {
      const searchBy = props.largeTypeList.find(
        (item) => item.value === localParamTargetSearch.value.type
      );
      impactAnalysisStore.resetState();
      impactAnalysisStore.setShouldReset(false);
      impactAnalysisStore.setSearchPattern(searchBy?.label);
      impactAnalysisStore.setTargetSearchType(
        localParamTargetSearch.value.type
      );
    }
    localParamTargetSearch.value.page = isClick ? 1 : page;
    localParamTargetSearch.value.size = size;
    paramTargetSearch.value = cloneDeep(localParamTargetSearch.value);
    await actionGetTargetSearchList();
  } catch (error: any) {
    useSnackbar.showSnackbar(
      error?.errorMsg || t("product_platform.something_went_wrong"),
      "error"
    );
  }
};

const onChooseCard = (item) => {
  resetParamListView();
  gridViewParams.lctgrItemCode = SPACE;
  gridViewParams.objName = "";
  gridViewParams.objCode = "";
  selectedUuid.value = item.prodUuid;
  handleActiveCard(item);
  switch (localParamTargetSearch.value.type) {
    case TARGET_TYPE_CODE.OFFER:
      impactAnalysisStore.setShouldReset(true);
      impactAnalysisStore.resetState();
      nextTick(() => {
        impactAnalysisStore.setSelectedSearchItem(item);
        impactAnalysisStore.setIsShowOffer(true);
        impactAnalysisStore.setIsShowComponent(false);
        impactAnalysisStore.setIsShowResource(false);
      });
      break;
    case TARGET_TYPE_CODE.COMPONENT:
      impactAnalysisStore.setShouldReset(true);
      impactAnalysisStore.resetState();
      nextTick(() => {
        impactAnalysisStore.setSelectedSearchItem(item);
        impactAnalysisStore.setIsShowOffer(true);
        impactAnalysisStore.setIsShowComponent(true);
        impactAnalysisStore.setIsShowResource(true);
      });
      break;
    case TARGET_TYPE_CODE.RESOURCE:
      impactAnalysisStore.setShouldReset(true);
      impactAnalysisStore.resetState();
      nextTick(() => {
        impactAnalysisStore.setSelectedSearchItem(item);
        impactAnalysisStore.setIsShowOffer(false);
        impactAnalysisStore.setIsShowComponent(false);
        impactAnalysisStore.setIsShowResource(true);
      });
      break;
  }
};

const handleChangeType = (value) => {
  localParamTargetSearch.value.type = value === SPACE ? undefined : value;
  localParamTargetSearch.value.page = 1;
  localParamTargetSearch.value.detlType = SPACE;
  localParamTargetSearch.value.subType = SPACE;
  localParamTargetSearch.value.prodItemNm = undefined;
  localParamTargetSearch.value.prodItemCd = undefined;
};

const handleChangeSubType = (value) => {
  localParamTargetSearch.value.subType = value;
};

const handleChangeDetlType = (value) => {
  localParamTargetSearch.value.detlType = value;
};

const handleChangePage = async (pageNo) => {
  impactAnalysisStore.setTargetSearchPageNo(pageNo);
  await impactAnalysisStore.actionGetTargetSearchList(pageNo);
};

const handleChangeItemName = (value) => {
  localParamTargetSearch.value.prodItemNm = value;
};

const handleChangeItemCode = (value) => {
  localParamTargetSearch.value.prodItemCd = value;
};

const handleNameCode = (value) => {
  selectedNmCd.value = value;
};

const handleActiveCard = (obj) => {
  targetSearchList.value.items = targetSearchList.value.items.map((item) => {
    if (item.prodUuid === obj.prodUuid) {
      return { ...item, active: true };
    }
    return { ...item, active: false };
  });
};
</script>
