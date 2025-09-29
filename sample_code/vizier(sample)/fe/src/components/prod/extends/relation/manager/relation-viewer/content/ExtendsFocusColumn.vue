<template>
  <div class="flex flex-column gap-[32px] expand-manager-column">
    <template v-if="extendsView === EXTENDS_VIEW.SIMPLE">
      <div ref="listEl">
        <cf-card-dropdown
          v-if="selectedItem.prodItemNm"
          :item="selectedItem"
          :title="selectedItem.prodItemNm"
          :description="selectedItem.prodItemCd"
          :width="itemWidth"
          type-bg="linear"
          :border-color-action="'red'"
          :type-of-prod="setIconType(selectedItem.subType)"
          :is-device-icon="
            ['DE', 'DV'].includes(
              selectedItem.subType.slice(0, 2).toUpperCase()
            )
          "
          :active="localSelected?.objUuid === selectedItem?.objUuid"
          :icon-color="setIconColor(selectedItem.subType)"
          :search-text="
            searchObj?.type === SELECT_LIST_TYPE.OFFER ? searchObj?.keyword : ''
          "
          :search-field="
            searchObj?.type === SELECT_LIST_TYPE.OFFER
              ? searchObj?.searchBy
              : ''
          "
          :node="{
            hideNodeLeft:
              selectedItem.countData?.leaderNumber !== 0 || isEdit
                ? false
                : true,
            isActiveNodeLeft: detailViewData.isShowLeaderCol,
            hideNodeRight:
              selectedItem.countData?.followerNumber !== 0 || isEdit
                ? false
                : true,
            isActiveNodeRight: detailViewData.isShowFollowerCol,
          }"
          :show-count="false"
          hide-detail
          draggable
          @on-click-node-left="handleShowLeader($event, 0, !offerDuplicateMode)"
          @on-click-node-right="
            handleShowFollower($event, 0, !offerDuplicateMode)
          "
          @on-click-card="
            handleClick($event, selectedItem, 0, EXTEND_CATEGORY.LEADER)
          "
          @dragstart="
            handleDragUserPocket($event, {
              userPocketType: LARGE_ITEM_CODE.OFFER,
              ...selectedItem,
            })
          "
        >
        </cf-card-dropdown>
      </div>
    </template>
    <template v-else>
      <div class="flex flex-column">
        <span
          class="text-[13px] text-[#6B6D70] font-weight-medium block mb-2"
          >{{ $t("product_platform.relation.asLeader") }}</span
        >
        <div
          v-for="(item, leaderIndex) in detailViewData.focusColumnLeaderList"
          ref="leaderListEle"
          :key="leaderIndex"
        >
          <cf-card-dropdown
            v-if="item.prodItemNm"
            class-name="default mb-2"
            :item="item"
            :title="item.prodItemNm"
            :description="item.prodItemCd"
            :search-text="
              searchObj?.type === SELECT_LIST_TYPE.OFFER
                ? searchObj?.keyword
                : ''
            "
            :search-field="
              searchObj?.type === SELECT_LIST_TYPE.OFFER
                ? searchObj?.searchBy
                : ''
            "
            :width="itemWidth"
            type-bg="linear"
            :border-color-action="'red'"
            :type-of-prod="setIconType(item.subType)"
            :is-device-icon="
              ['DE', 'DV'].includes(item.subType.slice(0, 2).toUpperCase())
            "
            :icon-color="setIconColor(item.subType)"
            :node="{
              hideNodeLeft: true,
              isActiveNodeLeft: false,
              hideNodeRight:
                item.countData?.followerNumber !== 0 || isEdit ? false : true,
              isActiveNodeRight: item.activeNode,
            }"
            :active="setActiveOffer(item, leaderIndex, EXTEND_CATEGORY.LEADER)"
            :show-count="false"
            hide-detail
            draggable
            @on-click-node-right="handleShowFollower($event, leaderIndex, true)"
            @on-click-card="
              handleClick($event, item, leaderIndex, EXTEND_CATEGORY.LEADER)
            "
            @dragstart="
              handleDragUserPocket($event, {
                userPocketType: LARGE_ITEM_CODE.OFFER,
                ...item,
              })
            "
          >
          </cf-card-dropdown>
          <cf-card-dropdown
            v-if="item.offerGroupName"
            :item="item"
            class-name="default group-icon mb-2"
            :title="item.offerGroupName"
            :description="item.offerGroupCode"
            :width="itemWidth"
            :search-text="
              searchObj?.type === SELECT_LIST_TYPE.GROUP
                ? searchObj?.keyword
                : ''
            "
            :search-field="
              searchObj?.type === SELECT_LIST_TYPE.GROUP
                ? searchObj?.searchBy
                : ''
            "
            type-bg="linear"
            :border-color-action="'red'"
            :active="setActiveGroup(item, leaderIndex, EXTEND_CATEGORY.LEADER)"
            :node="{
              hideNodeLeft: true,
              isActiveNodeLeft: false,
              hideNodeRight: !item.followerList?.length && !isEdit,
              isActiveNodeRight: item.activeNode,
            }"
            :show-count="false"
            :is-new="item?.isAdded"
            :show-icon-status="isEdit && item?.isAdded"
            :actions="listActions(item, EXTEND_CATEGORY.LEADER)"
            hide-detail
            editable
            draggable
            @on-click-node-right="handleShowFollower($event, leaderIndex, true)"
            @on-click-card="
              handleClick($event, item, leaderIndex, EXTEND_CATEGORY.LEADER)
            "
            @dragstart="handleDragGroup($event, item)"
          >
            <template #icon>
              <span class="flex justify-center align-center w-[40px] h-[40px]">
                <FolderIcon />
              </span>
            </template>
          </cf-card-dropdown>
          <ItemDrop
            v-if="isEdit && item.name === 'dropBox'"
            class-name="mb-2"
            @click.stop="handleOpenTargetSearch"
            @drop="drop($event, EXTEND_CATEGORY.LEADER)"
            @dragover="allowDrop($event)"
          >
            <span v-if="locale == 'en'">Drag <b>Group</b> item here</span>
            <span v-else><b>그룹</b> 아이템을 끌어다 놓으세요</span>
          </ItemDrop>
        </div>
      </div>
      <div ref="followerListContainer" class="flex flex-column">
        <span
          class="text-[13px] text-[#6B6D70] font-weight-medium block mb-2"
          >{{ $t("product_platform.relation.asFollower") }}</span
        >
        <div
          v-for="(
            item, followerIndex
          ) in detailViewData.focusColumnFollowerList"
          :key="followerIndex"
          ref="followerListELe"
        >
          <cf-card-dropdown
            v-if="item.prodItemNm"
            class-name="default mb-2"
            :item="item"
            :title="item.prodItemNm"
            :description="item.prodItemCd"
            :search-text="
              searchObj?.type === SELECT_LIST_TYPE.OFFER
                ? searchObj?.keyword
                : ''
            "
            :search-field="
              searchObj?.type === SELECT_LIST_TYPE.OFFER
                ? searchObj?.searchBy
                : ''
            "
            :width="itemWidth"
            type-bg="linear"
            :border-color-action="'red'"
            :type-of-prod="setIconType(item.subType)"
            :is-device-icon="
              ['DE', 'DV'].includes(item.subType.slice(0, 2).toUpperCase())
            "
            :icon-color="setIconColor(item.subType)"
            :active="
              setActiveOffer(item, followerIndex, EXTEND_CATEGORY.FOLLOWER)
            "
            :node="{
              hideNodeLeft:
                item.countData?.leaderNumber !== 0 || isEdit ? false : true,
              isActiveNodeLeft: item.activeNode,
              hideNodeRight: true,
              isActiveNodeRight: false,
            }"
            hide-detail
            draggable
            :show-count="false"
            @on-click-node-left="handleShowLeader($event, followerIndex, true)"
            @on-click-card="
              handleClick($event, item, followerIndex, EXTEND_CATEGORY.FOLLOWER)
            "
            @dragstart="
              handleDragUserPocket($event, {
                userPocketType: LARGE_ITEM_CODE.OFFER,
                ...item,
              })
            "
          >
          </cf-card-dropdown>
          <cf-card-dropdown
            v-if="item.offerGroupName"
            :item="item"
            class-name="default group-icon mb-2"
            :title="item.offerGroupName"
            :description="item.offerGroupCode"
            :search-text="
              searchObj?.type === SELECT_LIST_TYPE.GROUP
                ? searchObj?.keyword
                : ''
            "
            :search-field="
              searchObj?.type === SELECT_LIST_TYPE.GROUP
                ? searchObj?.searchBy
                : ''
            "
            :width="itemWidth"
            type-bg="linear"
            :border-color-action="'red'"
            :active="
              setActiveGroup(item, followerIndex, EXTEND_CATEGORY.FOLLOWER)
            "
            :node="{
              hideNodeLeft: !item.leaderList?.length && !isEdit,
              isActiveNodeLeft: item.activeNode,
              hideNodeRight: true,
              isActiveNodeRight: false,
            }"
            hide-detail
            :show-count="false"
            :is-new="item?.isAdded"
            :show-icon-status="isEdit && item?.isAdded"
            :actions="listActions(item, EXTEND_CATEGORY.FOLLOWER)"
            editable
            draggable
            @on-click-node-left="handleShowLeader($event, followerIndex, true)"
            @on-click-card="
              handleClick($event, item, followerIndex, EXTEND_CATEGORY.FOLLOWER)
            "
            @dragstart="handleDragGroup($event, item)"
          >
            <template #icon>
              <span class="flex justify-center align-center w-[40px] h-[40px]">
                <FolderIcon />
              </span>
            </template>
          </cf-card-dropdown>
          <ItemDrop
            v-if="isEdit && item.name === 'dropBox'"
            class-name="mb-2"
            @click.stop="handleOpenTargetSearch"
            @drop="drop($event, EXTEND_CATEGORY.FOLLOWER)"
            @dragover="allowDrop($event)"
          >
            <span v-if="locale == 'en'">Drag <b>Group</b> item here</span>
            <span v-else><b>그룹</b> 아이템을 끌어다 놓으세요</span>
          </ItemDrop>
        </div>
      </div>
    </template>
  </div>
  <DateTimePopup
    v-model:open-model="isOpenAddItemPopup"
    v-model="dateValid"
    :modal-title="$t('product_platform.addGroup')"
    :text-btn-cancel="$t('product_platform.cancel')"
    :text-btn-add="$t('product_platform.save')"
    :min-end-date="
      moment().format(DATE_FORMAT.DATE_FORMAT_WITHOUT_TIME_REVERSE)
    "
    required-start-date
    @submit="handleSubmitAddItem"
    @close="handleCloseAddItem"
  />
</template>

<script setup lang="ts">
import {
  EXTENDS_VIEW,
  EXTEND_CATEGORY,
  SELECT_LIST_TYPE,
  TARGET_TYPES,
} from "@/constants/extendsManager";
import {
  useDragStore,
  useExtendManagerStore,
  useRelationManagerDuplicateStore,
  useSnackbarStore,
} from "@/store";
import { setIconColor, setIconType } from "@/utils/impact-analysis-utils";
import { searchByKeyword } from "@/utils/extend-utils";
import useDragUserPocket from "@/composables/useDragUserPocket";
import { LARGE_ITEM_CODE } from "@/store/userPocket.store";
import { useI18n } from "vue-i18n";
import moment from "moment-timezone";
import { DATE_FORMAT } from "@/constants/index";
import TrashIcon from "@/components/prod/icons/TrashIcon.vue";
import { formatDate } from "@/utils/format-data";
import {
  getExtendsDependencyFollower,
  getExtendsDependencyLeader,
} from "@/api/prod/extendsApi";
import useRedirect from "@/composables/useRedirect";
import OpenInNewIcon from "@/components/prod/icons/OpenInNewIcon.vue";

const extendManagerStore = useExtendManagerStore();
const relationManagerDuplicateStore = useRelationManagerDuplicateStore();
const useSnackbar = useSnackbarStore();
const { locale, t } = useI18n();
const { dragOfferType } = storeToRefs(useDragStore());
const { handleDragUserPocket } = useDragUserPocket();

const localSelected = ref();
const followerListContainer = ref();
const leaderListEle = ref();
const followerListELe = ref();
const listEl = ref();
const areaClick = ref();
const dropData = ref<any>();
const categories = ref<any>();
const isOpenAddItemPopup = ref(false);
const dateValid = reactive({
  startDate: "",
  endDate: "",
});

const props = defineProps({
  itemWidth: {
    type: Number,
    default: 370,
  },
  searchObj: {
    type: Object,
    default: new Object(),
  },
  activeObj: {
    type: Object,
    default: new Object(),
  },
  offerDuplicateMode: {
    type: Boolean,
    default: false,
  },
});

const selectedStore = computed(() =>
  props.offerDuplicateMode ? relationManagerDuplicateStore : extendManagerStore
);
const {
  isEdit,
  detailViewData,
  extendsView,
  selectedItem,
  expandLeaderItem,
  expandFollowerItem,
  expandLeaderItemIndex,
  expandFollowerItemIndex,
  followerOffsetTop,
  sideDisplay,
  targetSearch,
  structureActiveFocusColumn,
  paramsExtendsTargetSearchGroup,
  allowGroupDrop,
  isSearchGroupParentOffer,
  isDuplicateInitData,
} = storeToRefs(selectedStore.value);
const {
  getLeaderList,
  getFollowerList,
  resetStructureActiveMap,
  closeAllDetail,
  addGroupRelationOffer,
  removeGroupRelationOffer,
  resetParamsExtendsTargetSearchOffer,
  resetParamsExtendsTargetSearchGroup,
} = selectedStore.value;
const { moveGroupSearchPage } = useRedirect();

const listActions = (item, category) => {
  if (props.offerDuplicateMode || item?.isAdded) {
    return [
      {
        name: t("product_platform.actionRemove"),
        icon: TrashIcon,
        iconProps: {
          class: "text-text-lighter",
        },
        onClick: () => {
          const index =
            category === EXTEND_CATEGORY.FOLLOWER
              ? detailViewData.value.focusColumnFollowerList.findIndex(
                  (group) => item?.offerGroupUuid === group?.offerGroupUuid
                )
              : detailViewData.value.focusColumnLeaderList.findIndex(
                  (group) => item?.offerGroupUuid === group?.offerGroupUuid
                );
          if (index !== -1) {
            if (category === EXTEND_CATEGORY.FOLLOWER) {
              if (
                expandLeaderItem.value?.offerGroupUuid === item?.offerGroupUuid
              ) {
                detailViewData.value.expandColumnLeaderList = [];
                detailViewData.value.leaderCoordinates = [];
                detailViewData.value.isShowLeaderCol = false;
              }
              detailViewData.value.focusColumnFollowerList.splice(index, 1);
              if (!item?.isAdded) {
                detailViewData.value.listGroupFollowerFocusRemove.push(item);
              }
              if (expandFollowerItemIndex.value === 0) {
                detailViewData.value.focusColumnFollowerList[0].activeNode =
                  false;
                detailViewData.value.isShowLeaderCol = false;
              }
            } else {
              if (
                expandFollowerItem.value?.offerGroupUuid ===
                item?.offerGroupUuid
              ) {
                detailViewData.value.expandColumnFollowerList = [];
                detailViewData.value.followerCoordinates = [];
                detailViewData.value.isShowFollowerCol = false;
              }
              detailViewData.value.focusColumnLeaderList.splice(index, 1);
              if (!item?.isAdded) {
                detailViewData.value.listGroupLeaderFocusRemove.push(item);
              }
              if (expandLeaderItemIndex.value === 0) {
                detailViewData.value.focusColumnLeaderList[0].activeNode =
                  false;
                detailViewData.value.isShowFollowerCol = false;
              }
            }
            removeGroupRelationOffer(item.offerGroupUuid, category);
            nextTick(() => {
              const followerIndex =
                detailViewData.value.focusColumnFollowerList.findIndex(
                  (item) =>
                    item?.offerGroupUuid ===
                    expandLeaderItem.value?.offerGroupUuid
                );
              const leaderIndex =
                detailViewData.value.focusColumnLeaderList.findIndex(
                  (item) =>
                    item?.offerGroupUuid ===
                    expandFollowerItem.value?.offerGroupUuid
                );
              if (followerIndex !== -1) {
                detailViewData.value.focusFollowerCoordinates =
                  followerListELe.value[followerIndex as number]?.offsetTop;
              }
              if (leaderIndex !== -1) {
                detailViewData.value.focusLeaderCoordinates =
                  leaderListEle.value[leaderIndex as number]?.offsetTop;
              }
              setCanvasHeight();
              setCoordinates(EXTEND_CATEGORY.LEADER);
              setCoordinates(EXTEND_CATEGORY.FOLLOWER);
            });
          }
        },
      },
      {
        name: t("product_platform.openinNewWindow"),
        icon: OpenInNewIcon,
        onClick: () => {
          moveGroupSearchPage({
            itemId: item.offerGroupUuid,
            type: item.offerGroupCode.substring(2, 4),
            code: item.offerGroupCode,
            name: item.offerGroupName,
          });
        },
      },
    ];
  }
  return [];
};

const handleClick = async (event, item, index, area) => {
  if (extendsView.value === EXTENDS_VIEW.SIMPLE) {
    localSelected.value = item;
    const itemClone = {
      active: true,
      item: selectedItem.value,
    };
    await handleShowLeader(itemClone, index, true);
    await handleShowFollower(itemClone, index, true);
    detailViewData.value.isShowFollowerCol = true;
    detailViewData.value.isShowLeaderCol = true;
  } else {
    const itemClone = {
      active: true,
      item: { ...item, activeNode: true },
    };
    if (area === EXTEND_CATEGORY.LEADER) {
      await handleShowFollower(itemClone, index, true);
    } else {
      await handleShowLeader(itemClone, index, true);
    }
  }

  if (event) {
    resetStructureActiveMap();
    closeAllDetail();
    if (item.prodUuid) {
      structureActiveFocusColumn.value.offer.uuid = item.prodUuid;
      structureActiveFocusColumn.value.offer.index = index;
      structureActiveFocusColumn.value.offer.area = area;
    } else {
      structureActiveFocusColumn.value.group.uuid = item.offerGroupUuid;
      structureActiveFocusColumn.value.group.index = index;
      structureActiveFocusColumn.value.group.area = area;
    }
    areaClick.value = area;
  }
};

const handleDragGroup = (event: DragEvent, item: any): void => {
  console.log(item);
  handleDragUserPocket(event, {
    userPocketType: LARGE_ITEM_CODE.GROUP,
    ...item,
  });
};

const handleShowLeader = async (event, followerIndex, allowGetApi) => {
  expandFollowerItemIndex.value = followerIndex;
  detailViewData.value.focusColumnFollowerList =
    detailViewData.value.focusColumnFollowerList.map((item: any, index) => {
      if (followerIndex === index) {
        return { ...item, activeNode: event.active };
      }
      return { ...item, activeNode: false };
    });
  detailViewData.value.isShowLeaderCol = false;
  if (event.active && allowGetApi) {
    if (event.item.offerGroupUuid) {
      expandLeaderItem.value = event.item;
      detailViewData.value.expandColumnLeaderList = [];
      detailViewData.value.focusColumnFollowerList[
        followerIndex as number
      ]?.leaderList.forEach((rel) => {
        rel.child = rel.child.map((prod) => ({ ...prod, allowExpire: true }));
      });
      detailViewData.value.expandColumnLeaderList =
        detailViewData.value.focusColumnFollowerList[followerIndex as number]
          ?.leaderList;
    } else {
      detailViewData.value.expandColumnLeaderList = [];
      detailViewData.value.expandColumnLeaderList =
        detailViewData.value.focusColumnFollowerList[followerIndex as number]
          ?.leaderList || [];
    }
    if (extendsView.value === EXTENDS_VIEW.DETAIL) {
      detailViewData.value.focusFollowerCoordinates =
        followerListELe.value[followerIndex as number].offsetTop;
    } else {
      detailViewData.value.focusFollowerCoordinates = listEl.value.offsetTop;
    }
    detailViewData.value.isShowLeaderCol = event.active;
    setCanvasHeight();
  }
};

const handleShowFollower = async (event, leaderIndex, allowGetApi) => {
  expandLeaderItemIndex.value = leaderIndex;
  detailViewData.value.focusColumnLeaderList =
    detailViewData.value.focusColumnLeaderList.map((item: any, index) => {
      if (leaderIndex === index) {
        return { ...item, activeNode: event.active };
      }
      return { ...item, activeNode: false };
    });
  detailViewData.value.isShowFollowerCol = event.active;
  if (event.active && allowGetApi) {
    if (event.item.offerGroupUuid) {
      expandFollowerItem.value = event.item;
      detailViewData.value.expandColumnFollowerList = [];
      detailViewData.value.focusColumnLeaderList[
        leaderIndex as number
      ]?.followerList.forEach((rel) => {
        rel.child = rel.child.map((prod) => ({ ...prod, allowExpire: true }));
      });
      detailViewData.value.expandColumnFollowerList =
        detailViewData.value.focusColumnLeaderList[leaderIndex as number]
          ?.followerList || [];
    } else {
      detailViewData.value.expandColumnFollowerList = [];
      // if (!detailViewData.value.focusColumnLeaderList[leaderIndex]?.isFetch) {
      //   await getFollowerList(
      //     null,
      //     props.offerDuplicateMode,
      //     leaderIndex,
      //     extendsView.value === EXTENDS_VIEW.SIMPLE
      //   );
      // }
      detailViewData.value.expandColumnFollowerList =
        detailViewData.value.focusColumnLeaderList[leaderIndex as number]
          ?.followerList || [];
    }
    if (extendsView.value === EXTENDS_VIEW.DETAIL) {
      detailViewData.value.focusLeaderCoordinates =
        leaderListEle.value[leaderIndex as number].offsetTop;
    } else {
      detailViewData.value.focusLeaderCoordinates = listEl.value.offsetTop;
    }
    setCanvasHeight();
  }
};

const handleOpenTargetSearch = () => {
  targetSearch.value = TARGET_TYPES.GROUP;
  resetParamsExtendsTargetSearchOffer();
  resetParamsExtendsTargetSearchGroup();
  paramsExtendsTargetSearchGroup.value.childOffrUuid =
    selectedItem.value.objUuid;
  isSearchGroupParentOffer.value = true;
  if (props.offerDuplicateMode) {
    sideDisplay.value.relationSearch = false;
  } else {
    sideDisplay.value.relationDetail = false;
    sideDisplay.value.offerDetail = false;
    sideDisplay.value.targetDetail = false;
  }
  sideDisplay.value.offerSearch = false;
  sideDisplay.value.targetSearch = true;
};

watch(
  () => isEdit.value,
  () => {
    if (expandFollowerItemIndex.value || expandLeaderItemIndex.value) {
      nextTick(() => {
        if (extendsView.value === EXTENDS_VIEW.DETAIL) {
          detailViewData.value.focusFollowerCoordinates =
            followerListELe.value[expandFollowerItemIndex.value]?.offsetTop;
          detailViewData.value.focusLeaderCoordinates =
            leaderListEle.value[expandLeaderItemIndex.value]?.offsetTop;
          setCoordinates(EXTEND_CATEGORY.LEADER);
          setCoordinates(EXTEND_CATEGORY.FOLLOWER);
        }
      });
    } else {
      nextTick(() => {
        if (extendsView.value === EXTENDS_VIEW.DETAIL) {
          detailViewData.value.focusFollowerCoordinates =
            followerListELe.value[0]?.offsetTop;
          setCoordinates(EXTEND_CATEGORY.LEADER);
          setCoordinates(EXTEND_CATEGORY.FOLLOWER);
        }
      });
    }
  }
);

const setActiveGroup = (item, index, area) => {
  return (
    searchByKeyword(
      item.offerGroupName,
      item.offerGroupCode,
      SELECT_LIST_TYPE.GROUP,
      props?.searchObj as any
    ) ||
    (areaClick.value === area &&
      item.offerGroupUuid === props.activeObj?.group?.uuid &&
      index === props.activeObj?.group?.index)
  );
};

const setActiveOffer = (item, index, area) => {
  return (
    searchByKeyword(
      item.prodItemNm,
      item.prodItemCd,
      SELECT_LIST_TYPE.OFFER,
      props?.searchObj as any
    ) ||
    (areaClick.value === area &&
      item.prodUuid === props.activeObj?.offer?.uuid &&
      index === props.activeObj?.offer?.index)
  );
};

const checkInvalidItemDrop = (listCheck, dropItem, groupChild: any = []) => {
  let invalidItem = false;
  listCheck.forEach((itemFocus) => {
    if (itemFocus?.offerGroupUuid === dropItem.objUuid) invalidItem = true;
    const list =
      categories.value === EXTEND_CATEGORY.FOLLOWER
        ? itemFocus.leaderList
        : itemFocus.followerList;
    list?.forEach((rel) => {
      groupChild?.forEach((groupRelItem) => {
        if (rel.dpdcRelUuid === groupRelItem.dpdcRelUuid) {
          rel.child?.forEach((prod) => {
            if (prod?.targetUuid === groupRelItem?.targetUuid)
              invalidItem = true;
            if (prod?.childOffr?.length) {
              prod.childOffr.forEach((offer) => {
                if (groupRelItem?.targetUuid === offer?.offrUuid)
                  invalidItem = true;
              });
            }
            if (groupRelItem?.childOffr?.length) {
              groupRelItem.childOffr.forEach((offer) => {
                if (prod?.targetUuid === offer?.offrUuid) invalidItem = true;
                if (
                  prod?.childOffr?.length &&
                  prod?.childOffr?.some(
                    (offr) => offr?.offrUuid === offer?.offrUuid
                  )
                ) {
                  invalidItem = true;
                }
              });
            }
          });
        }
      });
    });
  });
  return invalidItem;
};

const allowDrop = (event) => {
  if (
    allowGroupDrop.value.includes(dragOfferType.value) &&
    isSearchGroupParentOffer.value
  ) {
    event.preventDefault();
    return true;
  }
  return false;
};

const drop = async (event, category) => {
  event.preventDefault();
  dropData.value = event.dataTransfer.getData("item")
    ? JSON.parse(event.dataTransfer.getData("item"))
    : null;
  if (dropData.value) {
    categories.value = category;
    let isDupplicate = false;
    let indexFind = false;
    if (category === EXTEND_CATEGORY.FOLLOWER) {
      const { data } = await getExtendsDependencyLeader({
        targetUuid: dropData.value.objUuid,
        onlyValidDtm: props.offerDuplicateMode,
      });
      indexFind = checkInvalidItemDrop(
        detailViewData.value.focusColumnFollowerList,
        dropData.value,
        data
      );
    } else {
      const { data } = await getExtendsDependencyFollower({
        targetUuid: dropData.value.objUuid,
        onlyValidDtm: props.offerDuplicateMode,
      });
      indexFind = checkInvalidItemDrop(
        detailViewData.value.focusColumnLeaderList,
        dropData.value,
        data
      );
    }

    if (indexFind) {
      useSnackbar.showSnackbar(
        t("product_platform.relation.addItemInvalid"),
        "error"
      );
      isDupplicate = true;
      return;
    }
    if (!isDupplicate) {
      isOpenAddItemPopup.value = true;
    }
  }
};

const handleSubmitAddItem = async () => {
  if (!dateValid.startDate) {
    useSnackbar.showSnackbar(
      t("product_platform.required_start_date"),
      "error"
    );
    return;
  }
  const objItem = {
    offerGroupUuid: dropData.value.objUuid,
    offerGroupName: dropData.value.objName,
    offerGroupCode: dropData.value.objCode,
    validStartDtm: formatDate(dateValid.startDate),
    validEndDtm: formatDate(dateValid.endDate),
    isAdded: true,
  };
  const addDate: any = {
    groupUuid: dropData.value.objUuid,
    offerUuid: selectedItem.value.objUuid,
    groupName: dropData.value.objName,
    validStartDtm: formatDate(dateValid.startDate),
    validEndDtm: formatDate(dateValid.endDate),
    isNew: true,
  };
  if (categories.value === EXTEND_CATEGORY.FOLLOWER) {
    detailViewData.value.focusColumnFollowerList.push(objItem);
    await getLeaderList(
      objItem.offerGroupUuid,
      props.offerDuplicateMode,
      detailViewData.value.focusColumnFollowerList.length - 1,
      extendsView.value === EXTENDS_VIEW.SIMPLE
    );
  } else {
    detailViewData.value.focusColumnLeaderList.push(objItem);
    await getFollowerList(
      objItem.offerGroupUuid,
      props.offerDuplicateMode,
      detailViewData.value.focusColumnLeaderList.length - 1,
      extendsView.value === EXTENDS_VIEW.SIMPLE
    );
  }
  addGroupRelationOffer(addDate);
  setStuctureLine();
  handleCloseAddItem();
};

const setStuctureLine = () => {
  nextTick(() => {
    detailViewData.value.focusFollowerCoordinates =
      followerListELe.value[expandFollowerItemIndex.value]?.offsetTop;
    setCanvasHeight();
    setCoordinates(EXTEND_CATEGORY.LEADER);
  });
};

const handleCloseAddItem = () => {
  isOpenAddItemPopup.value = false;
  dateValid.endDate = "";
  dateValid.startDate = "";
};

onMounted(() => {
  if (props.offerDuplicateMode && !isDuplicateInitData.value) {
    const itemClone = {
      active: true,
      item: { prodUuid: selectedItem.value.prodUuid, activeNode: true },
    };
    handleShowLeader(itemClone, 0, true);
    handleShowFollower(itemClone, 0, true);
  }
});

onUpdated(() => {
  if (
    detailViewData.value.focusColumnFollowerList?.length &&
    detailViewData.value.focusColumnLeaderList?.length &&
    extendsView.value === EXTENDS_VIEW.DETAIL
  ) {
    followerOffsetTop.value = followerListContainer.value.offsetTop;
  }
});

const { setCanvasHeight } = inject<any>("handleCalCanvasHeight");
const { setCoordinates } = inject<any>("handleCalCoordinates");
</script>

<style>
:deep(.group-icon .icon-container) {
  background-color: #fff6e9;
}
</style>
