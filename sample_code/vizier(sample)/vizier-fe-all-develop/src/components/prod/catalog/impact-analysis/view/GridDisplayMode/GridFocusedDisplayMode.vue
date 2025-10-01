<template>
  <div class="card-item-list focus-item-list">
    <div class="parent-list">
      <p class="list-description-title">
        {{ $t("product_platform.impactAnalysis.parent") }}
      </p>
      <template v-if="parentItem">
        <cf-card-dropdown
          v-if="categoryName === TARGET_TYPE.OFFER"
          :title="parentItem.prodItemNm"
          :description="parentItem.prodItemCd"
          type-bg="linear"
          :border-color-action="setHoverColor(selectedItem.subType)"
          :type-of-prod="setIconType(selectedItem.subType)"
          :is-device-icon="
            ['DE', 'DV'].includes(
              selectedItem?.subType?.slice(0, 2).toUpperCase()
            )
          "
          :icon-color="setIconColor(selectedItem.subType)"
          dropdown-icon="mdi-dots-vertical"
          :node="{
            hideNodeLeft: true,
            isActiveNodeLeft: false,
            hideNodeRight: true,
            isActiveNodeRight: false,
          }"
          :active="parentItem.prodUuid === selectedCard?.prodUuid"
          draggable
          hide-detail
          @on-click-card="onChooseCard($event, parentItem)"
          @dragstart="handleDragStart($event, selectedItem)"
        />
        <cf-card-dropdown
          v-if="categoryName === TARGET_TYPE.COMPONENT"
          :title="parentItem.prodItemNm"
          :description="parentItem.prodItemCd"
          type-bg="linear"
          :border-color-action="setHoverColor(selectedItem.detlType)"
          :display-border-left="setHoverColor(selectedItem.detlType)"
          dropdown-icon="mdi-dots-vertical"
          :node="{
            hideNodeLeft: true,
            isActiveNodeLeft: false,
            hideNodeRight: true,
            isActiveNodeRight: false,
          }"
          :active="parentItem.prodUuid === selectedCard?.prodUuid"
          draggable
          @on-click-card="onChooseCard($event, parentItem)"
          @dragstart="handleDragStart($event, selectedItem)"
        />
        <cf-card-dropdown
          v-if="categoryName === TARGET_TYPE.RESOURCE"
          :title="parentItem.prodItemNm"
          :description="parentItem.prodItemCd"
          class-name="card-round-style"
          type-bg="light"
          border-color-action="purple"
          :node="{
            hideNodeLeft: true,
            isActiveNodeLeft: false,
            hideNodeRight: true,
            isActiveNodeRight: false,
          }"
          hide-detail
          :active="parentItem.prodUuid === selectedCard?.prodUuid"
          draggable
          @on-click-card="onChooseCard($event, parentItem)"
          @dragstart="handleDragStart($event, parentItem)"
        >
          <template #icon>
            <span class="flex justify-center align-center w-[40px] h-[40px]">
              <template
                v-if="selectedItem.subType === RESOURCE_TYPE_FIELD[0].value"
              >
                <RLinearIcon />
              </template>
              <template
                v-if="selectedItem.subType === RESOURCE_TYPE_FIELD[1].value"
              >
                <BLinearIcon />
              </template>
              <template
                v-if="selectedItem.subType === RESOURCE_TYPE_FIELD[2].value"
              >
                <SLinearIcon />
              </template>
            </span>
          </template>
        </cf-card-dropdown>
      </template>
      <template v-else>
        <div class="empty-card">{{ $t("product_platform.noParents") }}</div>
      </template>
    </div>
    <div class="impact-card-container">
      <template v-if="selectedItem">
        <cf-card-dropdown
          v-if="categoryName === TARGET_TYPE.OFFER"
          ref="cfCard"
          :item="selectedItem"
          :title="selectedItem.prodItemNm"
          :description="selectedItem.prodItemCd"
          type-bg="linear"
          :border-color-action="setHoverColor(selectedItem.subType)"
          :type-of-prod="setIconType(selectedItem.subType)"
          :is-device-icon="
            ['DE', 'DV'].includes(
              selectedItem.subType.slice(0, 2).toUpperCase()
            )
          "
          :icon-color="setIconColor(selectedItem.subType)"
          show-icon-status
          :node="nodeDisplay"
          show-count
          draggable
          :active="selectedItem.prodUuid === selectedCard?.prodUuid"
          :disable-arrow="isLoading"
          @on-click-node-right="addItemToExtendedListLeftToRight"
          @on-click-node-left="addItemToExtendedListRightToLeft"
          @on-click-card="onChooseCard($event, selectedItem)"
          @on-click-show-detail="getProductDetail"
          @dragstart="handleDragStart($event, selectedItem)"
        >
          <template #detail>
            <ProductGrid
              :data="selectedItem.detail"
              :type="LARGE_ITEM_CODE.OFFER"
            />
          </template>
        </cf-card-dropdown>
        <cf-card-dropdown
          v-if="categoryName === TARGET_TYPE.COMPONENT"
          ref="cfCard"
          :item="selectedItem"
          :title="selectedItem.prodItemNm"
          :description="selectedItem.prodItemCd"
          type-bg="linear"
          :border-color-action="setHoverColor(selectedItem.detlType)"
          :display-border-left="setHoverColor(selectedItem.detlType)"
          show-icon-status
          :node="nodeDisplay"
          show-count
          :active="selectedItem.prodUuid === selectedCard?.prodUuid"
          draggable
          :disable-arrow="isLoading"
          @on-click-node-right="addItemToExtendedListLeftToRight"
          @on-click-node-left="addItemToExtendedListRightToLeft"
          @on-click-card="onChooseCard($event, selectedItem)"
          @on-click-show-detail="getProductDetail"
          @dragstart="handleDragStart($event, selectedItem)"
        >
          <template #detail>
            <ProductGrid
              :data="selectedItem.detail"
              :type="LARGE_ITEM_CODE.COMPONENT"
            />
          </template>
        </cf-card-dropdown>
        <cf-card-dropdown
          v-if="categoryName === TARGET_TYPE.RESOURCE"
          ref="cfCard"
          type-bg="light"
          :item="selectedItem"
          :title="selectedItem.prodItemNm"
          :description="selectedItem.prodItemCd"
          border-color-action="purple"
          class-name="card-round-style"
          show-icon-status
          :node="nodeDisplay"
          show-count
          :active="selectedItem.prodUuid === selectedCard?.prodUuid"
          draggable
          :disable-arrow="isLoading"
          @on-click-node-right="addItemToExtendedListLeftToRight"
          @on-click-node-left="addItemToExtendedListRightToLeft"
          @on-click-card="onChooseCard($event, selectedItem)"
          @on-click-show-detail="getProductDetail"
          @dragstart="handleDragStart($event, selectedItem)"
        >
          <template #icon>
            <span class="flex justify-center align-center w-[40px] h-[40px]">
              <template
                v-if="selectedItem.subType === RESOURCE_TYPE_FIELD[0].value"
              >
                <RLinearIcon />
              </template>
              <template
                v-if="selectedItem.subType === RESOURCE_TYPE_FIELD[1].value"
              >
                <BLinearIcon />
              </template>
              <template
                v-if="selectedItem.subType === RESOURCE_TYPE_FIELD[2].value"
              >
                <SLinearIcon />
              </template>
            </span>
          </template>
          <template #detail>
            <ProductGrid
              :data="selectedItem.detail"
              :type="LARGE_ITEM_CODE.RESOURCE"
            />
          </template>
        </cf-card-dropdown>
      </template>
    </div>
    <div class="parent-list">
      <p class="list-description-title">
        {{ $t("product_platform.impactAnalysis.siblings") }}
      </p>
      <div class="card-nested-list">
        <template v-if="siblingList.length">
          <template v-for="item in siblingList" :key="item.prodUuid">
            <cf-card-dropdown
              v-if="categoryName === TARGET_TYPE.OFFER"
              :title="item.prodItemNm"
              :description="item.prodItemCd"
              type-bg="linear"
              :border-color-action="
                setHoverColor(item.subType ?? selectedItem.subType)
              "
              :type-of-prod="setIconType(item.subType ?? selectedItem.subType)"
              :is-device-icon="
                ['DE', 'DV'].includes(item?.subType?.slice(0, 2).toUpperCase())
              "
              :icon-color="setIconColor(item.subType ?? selectedItem.subType)"
              dropdown-icon="mdi-dots-vertical"
              :node="{
                hideNodeLeft: true,
                isActiveNodeLeft: false,
                hideNodeRight: true,
                isActiveNodeRight: false,
              }"
              hide-detail
              draggable
              :active="item.prodUuid === selectedCard?.prodUuid"
              @on-click-card="onChooseCard($event, item)"
              @dragstart="handleDragStart($event, item)"
            />
            <cf-card-dropdown
              v-if="categoryName === TARGET_TYPE.COMPONENT"
              :title="item.prodItemNm"
              :description="item.prodItemCd"
              type-bg="linear"
              :border-color-action="
                setHoverColor(item.detlType ?? selectedItem.detlType)
              "
              :display-border-left="
                setHoverColor(item.detlType ?? selectedItem.detlType)
              "
              :node="{
                hideNodeLeft: true,
                isActiveNodeLeft: false,
                hideNodeRight: true,
                isActiveNodeRight: false,
              }"
              hide-detail
              draggable
              :active="item.prodUuid === selectedCard?.prodUuid"
              @on-click-card="onChooseCard($event, item)"
              @dragstart="handleDragStart($event, item)"
            >
            </cf-card-dropdown>
            <cf-card-dropdown
              v-if="categoryName === TARGET_TYPE.RESOURCE"
              :title="item.prodItemNm"
              :description="item.prodItemCd"
              type-bg="light"
              border-color-action="purple"
              class-name="card-round-style"
              :node="{
                hideNodeLeft: true,
                isActiveNodeLeft: false,
                hideNodeRight: true,
                isActiveNodeRight: false,
              }"
              hide-detail
              :active="item.prodUuid === selectedCard?.prodUuid"
              draggable
              @on-click-card="onChooseCard($event, item)"
              @dragstart="handleDragStart($event, item)"
            >
              <template #icon>
                <span
                  class="flex justify-center align-center w-[40px] h-[40px]"
                >
                  <template
                    v-if="selectedItem.subType === RESOURCE_TYPE_FIELD[0].value"
                  >
                    <RLinearIcon />
                  </template>
                  <template
                    v-if="selectedItem.subType === RESOURCE_TYPE_FIELD[1].value"
                  >
                    <BLinearIcon />
                  </template>
                  <template
                    v-if="selectedItem.subType === RESOURCE_TYPE_FIELD[2].value"
                  >
                    <SLinearIcon />
                  </template>
                </span>
              </template>
            </cf-card-dropdown>
          </template>
        </template>
        <template v-else>
          <div
            class="empty-card bg-[#F7F8FA] h-[64px] border-2-[#E6E9ED] font-weight-medium text-[#6B6D70]"
          >
            {{ $t("product_platform.noSibling") }}
          </div>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useImpactAnalysisStore } from "@/store";
import { RESOURCE_TYPE_FIELD, TARGET_TYPE } from "@/constants/impactAnalysis";
import RLinearIcon from "@/components/prod/icons/RLinearIcon.vue";
import SLinearIcon from "@/components/prod/icons/SLinearIcon.vue";
import BLinearIcon from "@/components/prod/icons/BLinearIcon.vue";
import cloneDeep from "lodash-es/cloneDeep";
import { getUiOfferStructure } from "@/api/prod/offerApi";
import {
  setIconType,
  setIconColor,
  setHoverColor,
} from "@/utils/impact-analysis-utils";
import { useGroupCode } from "@/composables/useGroupCode";
import { LARGE_ITEM_CODE } from "@/store/userPocket.store";
import useDragUserPocket from "@/composables/useDragUserPocket";

const props = defineProps({
  selectedItem: {
    type: Object as PropType<any>,
    default: () => {},
    require: true,
  },
  categoryName: {
    type: String,
    default: "",
  },
});
const impactAnalysisStore = useImpactAnalysisStore();
const {
  resourceItemList,
  offerItemList,
  resourceComponentCoordinates,
  offerComponentCoordinates,
  isLoading,
} = storeToRefs(impactAnalysisStore);
const { search, groupCodeData } = useGroupCode();
const { handleDragUserPocket } = useDragUserPocket();

const parentItem = ref<any>(null);
const siblingList = ref<any[]>([]);
const cfCard = ref(null);
const uniqueCodeList = ref<any[]>([]);
const commonCodeList = ref<any[]>([]);
const selectedCard = ref<any>();
const handleSetExtendedList = inject<any>("handleSetExtendedList");
const handleResetData = inject<any>("handleResetData");

watch(
  () => uniqueCodeList.value,
  async (val) => {
    await search(val);
  },
  { deep: true }
);

watch(
  () => groupCodeData.value,
  async (val) => {
    commonCodeList.value = val;
  },
  { deep: true }
);

onMounted(() => {
  getRelationData();
  const ele: any = document
    .getElementsByClassName("focus-item-list")[0]
    .getElementsByClassName("impact-card-container")[0];
  switch (props.categoryName) {
    case TARGET_TYPE.OFFER:
      impactAnalysisStore.setOfferCoordinates([[ele.offsetTop]]);
      break;
    case TARGET_TYPE.COMPONENT:
      impactAnalysisStore.setComponentToOfferCoordinates([[ele.offsetTop]]);
      impactAnalysisStore.setComponentToResourceCoordinates([[ele.offsetTop]]);
      break;
    case TARGET_TYPE.RESOURCE:
      impactAnalysisStore.setResourceCoordinates([[ele.offsetTop]]);
      break;
  }
});

const nodeDisplay = computed(() => {
  switch (props.categoryName) {
    case TARGET_TYPE.OFFER:
      return {
        hideNodeLeft: true,
        isActiveNodeLeft: false,
        hideNodeRight: !props.selectedItem?.trgtProdItemCount,
        isActiveNodeRight: impactAnalysisStore.getNodeFocusStatus.rightNode,
      };
    case TARGET_TYPE.COMPONENT:
      return {
        hideNodeLeft: !props.selectedItem?.baseProdItemCount,
        isActiveNodeLeft: impactAnalysisStore.getNodeFocusStatus.leftNode,
        hideNodeRight: !props.selectedItem?.trgtProdItemCount,
        isActiveNodeRight: impactAnalysisStore.getNodeFocusStatus.rightNode,
      };
    case TARGET_TYPE.RESOURCE:
      return {
        hideNodeLeft: !props.selectedItem?.baseProdItemCount,
        isActiveNodeLeft: impactAnalysisStore.getNodeFocusStatus.leftNode,
        hideNodeRight: true,
        isActiveNodeRight: false,
      };
  }
});

const addItemToExtendedListLeftToRight = async (event) => {
  impactAnalysisStore.setNodeFocusStatusRight(event.active);
  switch (props.categoryName) {
    case TARGET_TYPE.OFFER:
      if (event.active) {
        impactAnalysisStore.setParamSearchStructure({
          baseUuid: event.item.prodUuid,
        });
        isLoading.value = true;
        const componentData = await getUiOfferStructure(
          impactAnalysisStore.getParamSearchStructure
        );
        const extendedComponent = cloneDeep(componentData.data);
        handleSetExtendedList(extendedComponent, TARGET_TYPE.COMPONENT);
        isLoading.value = false;
      } else {
        handleResetData(TARGET_TYPE.RESOURCE);
        resourceItemList.value = [];
        resourceComponentCoordinates.value = [];
      }
      impactAnalysisStore.setIsShowComponent(event.active);
      impactAnalysisStore.setIsShowResource(false);
      break;
    case TARGET_TYPE.COMPONENT:
      if (event.active) {
        impactAnalysisStore.setParamSearchStructure({
          baseUuid: event.item.prodUuid,
        });
        isLoading.value = true;
        const resourceData = await getUiOfferStructure(
          impactAnalysisStore.getParamSearchStructure
        );
        const extendedResource = cloneDeep(resourceData.data);
        handleSetExtendedList(extendedResource, TARGET_TYPE.RESOURCE);
        isLoading.value = false;
      }
      impactAnalysisStore.setIsShowResource(event.active);
      break;
  }
};
const addItemToExtendedListRightToLeft = async (event) => {
  impactAnalysisStore.setNodeFocusStatusLeft(event.active);
  switch (props.categoryName) {
    case TARGET_TYPE.COMPONENT:
      if (event.active) {
        impactAnalysisStore.setParamSearchStructure({
          trgtUuid: event.item.prodUuid,
        });
        isLoading.value = true;
        const offerData = await getUiOfferStructure(
          impactAnalysisStore.getParamSearchStructure
        );
        const extendedOffer = cloneDeep(offerData.data);
        handleSetExtendedList(extendedOffer, TARGET_TYPE.OFFER);
        isLoading.value = false;
      }
      impactAnalysisStore.setIsShowOffer(event.active);
      break;
    case TARGET_TYPE.RESOURCE:
      if (event.active) {
        impactAnalysisStore.setParamSearchStructure({
          trgtUuid: event.item.prodUuid,
        });
        isLoading.value = true;
        const componentData = await getUiOfferStructure(
          impactAnalysisStore.getParamSearchStructure
        );
        const extendedComponent = cloneDeep(componentData.data);
        handleSetExtendedList(extendedComponent, TARGET_TYPE.COMPONENT);
        isLoading.value = false;
      } else {
        handleResetData(TARGET_TYPE.OFFER);
        offerItemList.value = [];
        offerComponentCoordinates.value = [];
      }
      impactAnalysisStore.setIsShowOffer(false);
      impactAnalysisStore.setIsShowComponent(event.active);
      break;
  }
};

const getRelationData = async () => {
  await impactAnalysisStore.actionGetRelation({
    prodUuid: props.selectedItem?.prodUuid,
  });
  parentItem.value = impactAnalysisStore.getParentItem;
  siblingList.value = impactAnalysisStore.getSiblingList;
};

const onChooseCard = (event, item) => {
  selectedCard.value = item;
  if (event.isShowDetail) {
    getProductDetail(event.isShowDetail);
  }
};

const getProductDetail = (event) => {
  if (event) {
    impactAnalysisStore.setOfferSelectedItemDetail();
  }
};

const handleDragStart = (event: DragEvent, item: any): void => {
  const categoryMap: Record<string, string> = {
    [TARGET_TYPE.OFFER]: LARGE_ITEM_CODE.OFFER,
    [TARGET_TYPE.COMPONENT]: LARGE_ITEM_CODE.COMPONENT,
    [TARGET_TYPE.RESOURCE]: LARGE_ITEM_CODE.RESOURCE,
  };
  const userPocketType = categoryMap[props.categoryName];
  if (userPocketType) {
    handleDragUserPocket(event, { userPocketType, ...item });
  }
};
</script>

<style scoped>
.detail-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}
</style>
