<template>
  <template v-if="itemList.length">
    <div
      v-if="categoryName === TARGET_TYPE.COMPONENT"
      :id="props.categoryName"
      class="card-item-list extended-list"
    >
      <template
        v-if="impactAnalysisStore.getSearchPattern === TARGET_TYPE.OFFER"
      >
        <div
          v-if="findItemType(TYPE_PRODUCT.CHARACTERISTICS)"
          class="parent-list"
        >
          <p class="list-description-title">
            {{ TYPE_PRODUCT.CHARACTERISTICS }}
          </p>
          <div class="card-nested-list">
            <template
              v-for="(childItem, index) in itemList"
              :key="childItem.trgtUuid"
            >
              <div
                v-if="childItem.trgtTypeName === TYPE_PRODUCT.CHARACTERISTICS"
                ref="expansionCardElem"
                class="impact-card-container"
                :style="{ height: childItem.clientHeight }"
              >
                <cf-card-dropdown
                  :title="childItem.trgtProdItemNm"
                  :description="childItem.trgtProdItemCd"
                  :item="childItem"
                  :border-color-action="setHoverColor(childItem.trgtTypeCode)"
                  :display-border-left="setHoverColor(childItem.trgtTypeCode)"
                  :node="
                    childItem.trgtTypeName === TYPE_PRODUCT.CHARACTERISTICS
                      ? {
                          hideNodeLeft: true,
                          isActiveNodeLeft: false,
                          hideNodeRight: true,
                          isActiveNodeRight: false,
                        }
                      : nodeStatus(childItem)
                  "
                  :disable="isExpiredTime(childItem?.itemValidEndDtm)"
                  :expired="isExpiredTime(childItem?.validEndDtm)"
                  hide-detail
                  :active="childItem.trgtUuid === selectedCard?.trgtUuid"
                  :draggable="true"
                  :disable-arrow="isLoading"
                  expired-color="#E9EBF0"
                  disable-color="#E9EBF0"
                  show-icon-status
                  editable
                  :actions="listActions(childItem)"
                  @on-click-node-right="handleShowChild($event, index)"
                  @on-click-card="onChooseCard(childItem)"
                  @dragstart="handleDragStart($event, childItem)"
                >
                </cf-card-dropdown>
              </div>
            </template>
          </div>
        </div>
        <div
          v-if="
            impactAnalysisStore.getSelectedSearchItem.type !==
              TAB_FIELDS[2].value && findItemType(TYPE_PRODUCT.SERVICE)
          "
          class="parent-list"
        >
          <p class="list-description-title">{{ TYPE_PRODUCT.SERVICE }}</p>
          <div class="card-nested-list">
            <template
              v-for="(childItem, index) in itemList"
              :key="childItem.trgtUuid"
            >
              <div
                v-if="childItem.trgtTypeName === TYPE_PRODUCT.SERVICE"
                ref="expansionCardElem"
                class="impact-card-container"
                :style="{ height: childItem.clientHeight }"
              >
                <cf-card-dropdown
                  :item="childItem"
                  :title="childItem.trgtProdItemNm"
                  :description="childItem.trgtProdItemCd"
                  :border-color-action="setHoverColor(childItem.trgtTypeCode)"
                  :display-border-left="setHoverColor(childItem.trgtTypeCode)"
                  :node="nodeStatus(childItem)"
                  :disable="isExpiredTime(childItem?.itemValidEndDtm)"
                  :expired="isExpiredTime(childItem?.validEndDtm)"
                  hide-detail
                  :active="childItem.trgtUuid === selectedCard?.trgtUuid"
                  :disable-arrow="isLoading"
                  :draggable="true"
                  expired-color="#E9EBF0"
                  disable-color="#E9EBF0"
                  show-icon-status
                  editable
                  :actions="listActions(childItem)"
                  @on-click-node-right="handleShowChild($event, index)"
                  @on-click-card="onChooseCard(childItem)"
                  @dragstart="handleDragStart($event, childItem)"
                >
                </cf-card-dropdown>
              </div>
            </template>
          </div>
        </div>
        <div
          v-if="
            impactAnalysisStore.getSelectedSearchItem.type !==
              TAB_FIELDS[2].value && findItemType(TYPE_PRODUCT.BENEFIT)
          "
          class="parent-list"
        >
          <p class="list-description-title">{{ TYPE_PRODUCT.BENEFIT }}</p>
          <div class="card-nested-list">
            <template
              v-for="(childItem, index) in itemList"
              :key="childItem.trgtUuid"
            >
              <div
                v-if="childItem.trgtTypeName === TYPE_PRODUCT.BENEFIT"
                ref="expansionCardElem"
                class="impact-card-container"
                :style="{ height: childItem.clientHeight }"
              >
                <cf-card-dropdown
                  :item="childItem"
                  :title="childItem.trgtProdItemNm"
                  :description="childItem.trgtProdItemCd"
                  :border-color-action="setHoverColor(childItem.trgtTypeCode)"
                  :display-border-left="setHoverColor(childItem.trgtTypeCode)"
                  :node="nodeStatus(childItem)"
                  :disable="isExpiredTime(childItem?.itemValidEndDtm)"
                  :expired="isExpiredTime(childItem?.validEndDtm)"
                  hide-detail
                  :active="childItem.trgtUuid === selectedCard?.trgtUuid"
                  :draggable="true"
                  :disable-arrow="isLoading"
                  expired-color="#E9EBF0"
                  disable-color="#E9EBF0"
                  show-icon-status
                  editable
                  :actions="listActions(childItem)"
                  @on-click-node-right="handleShowChild($event, index)"
                  @on-click-card="onChooseCard(childItem)"
                  @dragstart="handleDragStart($event, childItem)"
                >
                </cf-card-dropdown>
              </div>
            </template>
          </div>
        </div>
        <div v-if="findItemType(TYPE_PRODUCT.PRICE)" class="parent-list">
          <p class="list-description-title">{{ TYPE_PRODUCT.PRICE }}</p>
          <div class="card-nested-list">
            <template
              v-for="(childItem, index) in itemList"
              :key="childItem.trgtUuid"
            >
              <div
                v-if="childItem.trgtTypeName === TYPE_PRODUCT.PRICE"
                ref="expansionCardElem"
                class="impact-card-container"
                :style="{ height: childItem.clientHeight }"
              >
                <cf-card-dropdown
                  :item="childItem"
                  :title="childItem.trgtProdItemNm"
                  :description="childItem.trgtProdItemCd"
                  :border-color-action="setHoverColor(childItem.trgtTypeCode)"
                  :display-border-left="setHoverColor(childItem.trgtTypeCode)"
                  :node="nodeStatus(childItem)"
                  :disable="isExpiredTime(childItem?.itemValidEndDtm)"
                  :expired="isExpiredTime(childItem?.validEndDtm)"
                  hide-detail
                  :active="childItem.trgtUuid === selectedCard?.trgtUuid"
                  :draggable="true"
                  :disable-arrow="isLoading"
                  expired-color="#E9EBF0"
                  disable-color="#E9EBF0"
                  show-icon-status
                  editable
                  :actions="listActions(childItem)"
                  @on-click-card="onChooseCard(childItem)"
                  @on-click-node-right="handleShowChild($event, index)"
                  @dragstart="handleDragStart($event, childItem)"
                >
                </cf-card-dropdown>
              </div>
            </template>
          </div>
        </div>
      </template>
      <template v-else>
        <div class="card-nested-list pt-[29px]">
          <template
            v-for="(childItem, index) in itemList"
            :key="childItem.baseUuid"
          >
            <div
              ref="expansionCardElem"
              class="impact-card-container"
              :style="{ height: childItem.clientHeight ?? null }"
            >
              <cf-card-dropdown
                :title="childItem.baseProdItemNm"
                :description="childItem.baseProdItemCd"
                :item="childItem"
                :border-color-action="setHoverColor(childItem.baseTypeCode)"
                :display-border-left="setHoverColor(childItem.baseTypeCode)"
                :node="nodeStatus(childItem)"
                hide-detail
                :disable-arrow="isLoading"
                :active="childItem.baseUuid === selectedCard?.baseUuid"
                :draggable="true"
                :disable="isExpiredTime(childItem?.itemValidEndDtm)"
                :expired="isExpiredTime(childItem?.validEndDtm)"
                expired-color="#E9EBF0"
                disable-color="#E9EBF0"
                show-icon-status
                editable
                :actions="listActions(childItem)"
                @on-click-node-left="handleShowChild($event, index)"
                @on-click-card="onChooseCard(childItem)"
                @dragstart="handleDragStart($event, childItem)"
              >
              </cf-card-dropdown>
            </div>
          </template>
        </div>
      </template>
    </div>
    <div v-else>
      <template v-for="item in itemList" :key="item.index">
        <div
          :id="item.index + 1 ? props.categoryName + item.index : ''"
          :style="{ top: `${item.topPosition}px`, width: '370px' }"
          class="card-item-list position-absolute"
        >
          <template
            v-for="childItem in item.selectedItemList"
            :key="childItem.id"
          >
            <div class="impact-card-container">
              <template v-if="props.categoryName === TARGET_TYPE.OFFER">
                <cf-card-dropdown
                  ref="expansionCardElem"
                  :item="childItem"
                  :title="childItem.baseProdItemNm"
                  :description="childItem.baseProdItemCd"
                  :border-color-action="
                    setHoverColor(childItem.baseSubTypeCode)
                  "
                  :type-of-prod="setIconType(childItem.baseSubTypeCode)"
                  :is-device-icon="
                    ['DE', 'DV'].includes(
                      childItem.baseSubTypeName.slice(0, 2).toUpperCase()
                    )
                  "
                  :icon-color="setIconColor(childItem.baseSubTypeCode)"
                  :node="nodeStatus(childItem)"
                  :disable="isExpiredTime(childItem?.itemValidEndDtm)"
                  :expired="isExpiredTime(childItem?.validEndDtm)"
                  hide-detail
                  :active="
                    childItem.baseProdItemCd === selectedCard?.baseProdItemCd &&
                    childItem.sortNo === selectedCard?.sortNo
                  "
                  :disable-arrow="isLoading"
                  :draggable="true"
                  expired-color="#E9EBF0"
                  disable-color="#E9EBF0"
                  show-icon-status
                  editable
                  :actions="listActions(childItem)"
                  @on-click-card="onChooseCard(childItem)"
                  @dragstart="handleDragStart($event, childItem)"
                >
                </cf-card-dropdown>
              </template>
              <template v-else-if="props.categoryName === TARGET_TYPE.RESOURCE">
                <cf-card-dropdown
                  ref="expansionCardElem"
                  :item="childItem"
                  :title="childItem.trgtProdItemNm"
                  :description="childItem.trgtProdItemCd"
                  border-color-action="purple"
                  class-name="card-round-style"
                  :node="nodeStatus(childItem)"
                  :disable="isExpiredTime(childItem?.itemValidEndDtm)"
                  :expired="isExpiredTime(childItem?.validEndDtm)"
                  hide-detail
                  :active="childItem.trgtUuid === selectedCard?.trgtUuid"
                  :disable-arrow="isLoading"
                  :draggable="true"
                  expired-color="#E9EBF0"
                  disable-color="#E9EBF0"
                  show-icon-status
                  editable
                  :actions="listActions(childItem)"
                  @on-click-card="onChooseCard(childItem)"
                  @dragstart="handleDragStart($event, childItem)"
                >
                  <template #icon>
                    <span
                      class="flex justify-center align-center w-[40px] h-[40px]"
                    >
                      <template
                        v-if="
                          childItem.trgtSubTypeName ===
                          RESOURCE_TYPE_FIELD[0].value
                        "
                      >
                        <RLinearIcon />
                      </template>
                      <template
                        v-if="
                          childItem.trgtSubTypeName ===
                          RESOURCE_TYPE_FIELD[1].value
                        "
                      >
                        <BLinearIcon />
                      </template>
                      <template
                        v-if="
                          childItem.trgtSubTypeName ===
                          RESOURCE_TYPE_FIELD[2].value
                        "
                      >
                        <SLinearIcon />
                      </template>
                    </span>
                  </template>
                </cf-card-dropdown>
              </template>
            </div>
          </template>
        </div>
      </template>
    </div>
  </template>
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";
import { useImpactAnalysisStore } from "@/store";
import { LARGE_ITEM_CODE } from "@/store/userPocket.store";
import { RESOURCE_TYPE_FIELD, TAB_FIELDS, TARGET_TYPE } from "@/constants/";
import { getUiOfferStructure } from "@/api/prod/offerApi";
import {
  setIconType,
  setIconColor,
  setHoverColor,
} from "@/utils/impact-analysis-utils";
import { TYPE_PRODUCT } from "@/constants/offer";
import { isExpiredTime } from "@/utils/format-data";
import { SearchBy } from "@/enums";
import useRedirect from "@/composables/useRedirect";
import useDragUserPocket from "@/composables/useDragUserPocket";
import OpenInNewIcon from "@/components/prod/icons/OpenInNewIcon.vue";
import RLinearIcon from "@/components/prod/icons/RLinearIcon.vue";
import type { ActionType } from "@/interfaces/prod";

const impactAnalysisStore = useImpactAnalysisStore();
const expansionCardElem = ref<any>(null);
const selectedCard = ref<any>(null);
const handleAddExtendedList = inject<any>("handleAddExtendedList");
const handleRemoveExtendedList = inject<any>("handleRemoveExtendedList");
const { t } = useI18n();
const {
  isLoading,
  paramTargetSearch,
  selectedNmCd,
  targetSearchList,
  isShowStatus,
} = storeToRefs(impactAnalysisStore);
const { resetTargetSearch, actionGetTargetSearchList } = impactAnalysisStore;
const { moveOfferSearchPage, moveComponentSearchPage, moveResourceSearchPage } =
  useRedirect();
const { handleDragUserPocket } = useDragUserPocket();

const props = defineProps({
  itemList: {
    type: Array as PropType<any>,
    default: () => [],
  },
  categoryName: {
    type: String,
    default: "",
  },
  selectedItem: {
    type: Object,
    default: null,
  },
});

const listActions = (item: any): ActionType[] => [
  {
    name: t("product_platform.openinNewWindow"),
    icon: OpenInNewIcon,
    onClick: () => {
      handleItemClick(item);
    },
  },
  {
    name: t("product_platform.open_in_impact_analysis"),
    icon: OpenInNewIcon,
    onClick: () => {
      resetTargetSearch();
      selectedNmCd.value = SearchBy.Code;
      handleSelectNewImpact(item);
    },
  },
];

const handleSelectNewImpact = (item: any) => {
  const handlers = {
    [LARGE_ITEM_CODE.OFFER]: async () => {
      paramTargetSearch.value.prodItemCd = item.baseProdItemCd;
      paramTargetSearch.value.type = LARGE_ITEM_CODE.OFFER;
      paramTargetSearch.value.subType = item.baseSubTypeCode;
      await actionGetTargetSearchList();
      targetSearchList.value.items[0].active = true;
      const findItem = targetSearchList.value.items[0];
      impactAnalysisStore.setSearchPattern(TARGET_TYPE.OFFER);
      impactAnalysisStore.setShouldReset(true);
      impactAnalysisStore.resetState();
      nextTick(() => {
        impactAnalysisStore.setSelectedSearchItem(findItem);
        isShowStatus.value.offer = true;
        isShowStatus.value.component = false;
        isShowStatus.value.resource = false;
      });
    },
    [LARGE_ITEM_CODE.COMPONENT]: async () => {
      paramTargetSearch.value.prodItemCd =
        impactAnalysisStore.getSearchPattern === TARGET_TYPE.OFFER
          ? item.trgtProdItemCd
          : item.baseProdItemCd;
      paramTargetSearch.value.type = LARGE_ITEM_CODE.COMPONENT;
      paramTargetSearch.value.subType =
        impactAnalysisStore.getSearchPattern === TARGET_TYPE.OFFER
          ? item.trgtSubTypeCode
          : item.baseSubTypeCode;
      paramTargetSearch.value.detlType =
        impactAnalysisStore.getSearchPattern === TARGET_TYPE.OFFER
          ? item.trgtTypeCode
          : item.baseTypeCode;
      await actionGetTargetSearchList();
      targetSearchList.value.items[0].active = true;
      const findItem = targetSearchList.value.items[0];
      impactAnalysisStore.setSearchPattern(TARGET_TYPE.COMPONENT);
      impactAnalysisStore.setShouldReset(true);
      impactAnalysisStore.resetState();
      nextTick(() => {
        impactAnalysisStore.setSelectedSearchItem(findItem);
        isShowStatus.value.offer = true;
        isShowStatus.value.component = true;
        isShowStatus.value.resource = true;
      });
    },
    [LARGE_ITEM_CODE.RESOURCE]: async () => {
      paramTargetSearch.value.prodItemCd = item.trgtProdItemCd;
      paramTargetSearch.value.type = LARGE_ITEM_CODE.RESOURCE;
      paramTargetSearch.value.subType = item.trgtSubTypeCode;
      await actionGetTargetSearchList();
      targetSearchList.value.items[0].active = true;
      const findItem = targetSearchList.value.items[0];
      impactAnalysisStore.setSearchPattern(TARGET_TYPE.RESOURCE);
      impactAnalysisStore.setShouldReset(true);
      impactAnalysisStore.resetState();
      nextTick(() => {
        impactAnalysisStore.setSelectedSearchItem(findItem);
        isShowStatus.value.offer = false;
        isShowStatus.value.component = false;
        isShowStatus.value.resource = true;
      });
    },
  };
  handlers[props.categoryName.charAt(0)]?.();
};

const handleItemClick = (item: any): void => {
  const handlers = {
    [LARGE_ITEM_CODE.OFFER]: () =>
      moveOfferSearchPage({
        itemCode: item.baseSubTypeCode,
        itemCodeName: "",
        objCode: item?.baseProdItemCd || "",
        objUuid: item?.baseUuid || "",
        offerType: item?.baseSubTypeCode || "",
      }),
    [LARGE_ITEM_CODE.COMPONENT]: () =>
      moveComponentSearchPage({
        code:
          impactAnalysisStore.getSearchPattern === TARGET_TYPE.OFFER
            ? item.trgtProdItemCd
            : item.baseProdItemCd,
        itemId:
          impactAnalysisStore.getSearchPattern === TARGET_TYPE.OFFER
            ? item.trgtUuid
            : item.baseUuid,
        middleType:
          impactAnalysisStore.getSearchPattern === TARGET_TYPE.OFFER
            ? item.trgtTypeCode
            : item.baseTypeCode,
        offerType: "",
        name:
          impactAnalysisStore.getSearchPattern === TARGET_TYPE.OFFER
            ? item.trgtProdItemNm
            : item.baseProdItemNm,
        subType:
          impactAnalysisStore.getSearchPattern === TARGET_TYPE.OFFER
            ? item.trgtSubTypeCode
            : item.baseSubTypeCode,
        type: item.type,
      }),
    [LARGE_ITEM_CODE.RESOURCE]: () =>
      moveResourceSearchPage({
        itemCode: item.trgtSubTypeCode,
        objCode: item.trgtProdItemCd,
      }),
  };
  handlers[props.categoryName.charAt(0)]?.();
};

onMounted(() => {
  if (props.categoryName === TARGET_TYPE.COMPONENT) {
    if (impactAnalysisStore.getSearchPattern === TARGET_TYPE.OFFER) {
      calpulatingTopOffset();
      impactAnalysisStore.setInitResourceComponentCoordinates();
    } else if (impactAnalysisStore.getSearchPattern === TARGET_TYPE.RESOURCE) {
      calpulatingTopOffset();
      impactAnalysisStore.setInitOfferComponentCoordinates();
    }
  } else {
    props.itemList.forEach((item, index) => {
      calpulatingTopOffset(item, index);
    });
  }
});

onUpdated(() => {
  if (props.categoryName === TARGET_TYPE.COMPONENT) {
    calpulatingTopOffset();
  } else {
    props.itemList.forEach((item, index) => {
      calpulatingTopOffset(item, index);
    });
  }
});

const findItemType = (type) => {
  return props.itemList.find((el: any) => el.trgtTypeName === type);
};

const nodeStatus = (item) => {
  const defaultStatus = {
    hideNodeLeft: true,
    isActiveNodeLeft: false,
    hideNodeRight: true,
    isActiveNodeRight: false,
  };
  switch (impactAnalysisStore.getSearchPattern) {
    case TARGET_TYPE.OFFER:
      return props.categoryName === TARGET_TYPE.COMPONENT
        ? {
            ...defaultStatus,
            hideNodeRight: !item.trgtProdItemCount,
            isActiveNodeRight: !!item.active,
          }
        : defaultStatus;
    case TARGET_TYPE.COMPONENT:
      return defaultStatus;
    case TARGET_TYPE.RESOURCE:
      return props.categoryName === TARGET_TYPE.COMPONENT
        ? {
            ...defaultStatus,
            hideNodeLeft: !item.baseProdItemCount,
            isActiveNodeLeft: !!item.active,
          }
        : defaultStatus;
  }
};

const calpulatingTopOffset = (item?: any, index = 0) => {
  if (props.categoryName == TARGET_TYPE.COMPONENT) {
    if (expansionCardElem.value) {
      let listCoordinates: any = [];
      for (const el of expansionCardElem.value) {
        listCoordinates.push(el.offsetTop);
      }
      if (impactAnalysisStore.getSearchPattern == TARGET_TYPE.OFFER) {
        impactAnalysisStore.setComponentToOfferCoordinates([listCoordinates]);
      } else {
        impactAnalysisStore.setComponentToResourceCoordinates([
          listCoordinates,
        ]);
      }
    }
  } else {
    if (!item) {
      return;
    }
    let elementId = document.getElementById(props.categoryName + index);
    if (elementId) {
      let listElement: any = elementId.getElementsByClassName(
        "impact-card-container"
      );
      let listCoordinates: any = [];
      for (const el of listElement) {
        listCoordinates.push(item.topPosition + el.offsetTop);
      }
      if (listCoordinates.length) {
        switch (props.categoryName) {
          case TARGET_TYPE.OFFER:
            impactAnalysisStore.setOfferCoordinates([listCoordinates], index);
            impactAnalysisStore.actionOfferComponentCoordinates(index);
            break;
          case TARGET_TYPE.RESOURCE:
            impactAnalysisStore.setResourceCoordinates(
              [listCoordinates],
              index
            );
            impactAnalysisStore.actionResourceComponentCoordinates(index);
            break;
        }
      }
    }
  }
};
const handleShowChild = async (event, index) => {
  const actionElement = expansionCardElem.value[index as number];
  let targetTop = actionElement.offsetTop;
  const list = props.itemList as any;
  list[index as number]["active"] = event.active;
  if (event.active) {
    impactAnalysisStore.setParamSearchStructure(
      impactAnalysisStore.getSearchPattern === TARGET_TYPE.OFFER
        ? { baseUuid: event.item.trgtUuid }
        : { trgtUuid: event.item.baseUuid }
    );
    isLoading.value = true;
    const { data: structureList } = await getStructureItemList(
      impactAnalysisStore.getParamSearchStructure
    );
    const selectedObj = {
      index: index,
      topPosition: targetTop,
      selectedItemList: structureList,
    };
    let heightDiff = `${
      64 * structureList.length + 16 * (structureList.length - 1)
    }px`;
    calpulatingTopOffset(selectedObj, index);
    const saveStoreListItem = props.itemList.map((item: any, childIndex) => {
      if (childIndex === index) {
        return { ...item, clientHeight: heightDiff };
      }
      return item;
    });
    impactAnalysisStore.setComponentItemList(saveStoreListItem);
    if (impactAnalysisStore.getSearchPattern === TARGET_TYPE.OFFER) {
      impactAnalysisStore.setIsShowResource(event.active);
      impactAnalysisStore.setComponentToResourceCoordinates(
        [[targetTop]],
        index
      );
      handleAddExtendedList(selectedObj, TARGET_TYPE.RESOURCE);
    }
    if (impactAnalysisStore.getSearchPattern === TARGET_TYPE.RESOURCE) {
      impactAnalysisStore.setIsShowOffer(event.active);
      impactAnalysisStore.setComponentToOfferCoordinates([[targetTop]], index);
      handleAddExtendedList(selectedObj, TARGET_TYPE.OFFER);
    }
  } else {
    impactAnalysisStore.getComponentItemList[index as number].clientHeight =
      null;
    calpulatingTopOffset();
    handleRemoveExtendedList(index);
  }
};

const getStructureItemList = async (param) => {
  const data = await getUiOfferStructure(param);
  return data;
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

const onChooseCard = (item) => {
  selectedCard.value = item;
};
</script>
