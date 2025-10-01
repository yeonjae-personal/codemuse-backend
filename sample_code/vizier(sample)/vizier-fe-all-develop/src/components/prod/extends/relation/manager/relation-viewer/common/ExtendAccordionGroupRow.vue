<template>
  <div class="flex" :class="{ reverse: category === EXTEND_CATEGORY.LEADER }">
    <ExtendsAccordion
      v-model:expand="relation.isExpand"
      type="normal"
      :title="relation.dpdcRelName"
      :disable="expiredRelation"
      :active="activeRelation"
      :class-name="extendsView === EXTENDS_VIEW.DETAIL ? '!w-[284px]' : ''"
      :count="countItem"
      :is-remove="isRemove && !offerList.length"
      draggable
      @on-expand="handleExpand($event, relation)"
      @on-remove="handleRemoveRalation(relation)"
      @on-click="handleClickRel($event, relation)"
      @dragstart="
        handleDragUserPocket($event, {
          userPocketType: LARGE_ITEM_CODE.RELATION,
          ...relation,
        })
      "
    >
      <div
        v-if="extendsView === EXTENDS_VIEW.SIMPLE"
        class="flex flex-column gap-[12px]"
      >
        <ItemDrop
          v-if="isEdit"
          @click.stop="handleOpenTargetSearch(TARGET_TYPES.OFFER)"
          @drop="drop($event, TARGET_TYPES.OFFER)"
          @dragover="allowDrop($event, TARGET_TYPES.OFFER)"
        >
          <span v-if="locale == 'en'">Drag <b>Offer</b> item here</span>
          <span v-else><b>오퍼</b> 아이템을 끌어다 놓으세요</span>
        </ItemDrop>
        <template v-for="(item, index) in offerList" :key="index">
          <cf-card-dropdown
            v-if="item.lctgrItemName === TARGET_TYPE.OFFER"
            :title="item.targetName || item.prodItemNm"
            :description="item.targetCode || item.prodItemCd"
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
            :width="extendsView === EXTENDS_VIEW.DETAIL ? 260 : 335"
            type-bg="linear"
            :border-color-action="setHoverColor(item.itemCode)"
            :type-of-prod="
              setIconType(item.itemCode ? item.itemCode : item.subType)
            "
            :is-device-icon="
              item.itemCode
                ? ['DE', 'DV'].includes(item.itemCode.slice(0, 2).toUpperCase())
                : ['DE', 'DV'].includes(item.subType.slice(0, 2).toUpperCase())
            "
            :icon-color="
              setIconColor(item.itemCode ? item.itemCode : item.subType)
            "
            :active="
              setActiveOffer(
                item.targetName,
                item.targetCode,
                item.targetUuid,
                relation.dpdcRelCode
              )
            "
            :node="{
              hideNodeLeft: true,
              isActiveNodeLeft: false,
              hideNodeRight: true,
              isActiveNodeRight: false,
            }"
            :expired="
              item.disable || (isExpiredTime(item.validEndDtm) && !item.isAdded)
            "
            :disable="isExpiredTime(item?.itemValidEndDtm)"
            :actions="
              listActions(item, item.isAdded, category, item.lctgrItemName)
            "
            :is-new="item.isAdded"
            hide-detail
            :show-icon-status="
              isEdit && item?.parentUuid === selectedItem?.prodUuid
            "
            editable
            draggable
            @on-click-card="
              handleShowTargetDetail($event, item, relation.dpdcRelCode)
            "
            @dragstart="handleOfferDragStart($event, item)"
          />
          <template v-if="item.childOffr?.length">
            <template v-for="offer in item.childOffr" :key="offer.offrCd">
              <cf-card-dropdown
                :title="offer.offrNm"
                :description="offer.offrCd"
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
                type-bg="linear"
                :border-color-action="setHoverColor(offer.offrType)"
                :type-of-prod="setIconType(offer.offrType)"
                :is-device-icon="
                  ['DE', 'DV'].includes(
                    offer.offrType.slice(0, 2).toUpperCase()
                  )
                "
                :icon-color="setIconColor(offer.offrType)"
                dropdown-icon="mdi-dots-vertical"
                :active="
                  setActiveOffer(
                    offer.offrNm,
                    offer.offrCd,
                    offer.offrUuid,
                    relation.dpdcRelCode
                  )
                "
                :node="{
                  hideNodeLeft: true,
                  isActiveNodeLeft: false,
                  hideNodeRight: true,
                  isActiveNodeRight: false,
                }"
                :expired="
                  item?.disable ||
                  ((isExpiredTime(item.validEndDtm) ||
                    isExpiredTime(offer.validEndDtm)) &&
                    !item.isAdded)
                "
                :disable="isExpiredTime(offer?.itemValidEndDtm)"
                hide-detail
                draggable
                @on-click-card="
                  handleShowTargetDetail($event, offer, relation.dpdcRelCode)
                "
                @dragstart="handleOfferDragStart($event, offer)"
              />
            </template>
          </template>
        </template>
      </div>
      <div v-else class="flex flex-column gap-[12px]">
        <ItemDrop
          v-if="isEdit"
          @click.stop="handleOpenTargetSearch(TARGET_TYPES.OFFER)"
          @drop="drop($event, TARGET_TYPES.OFFER)"
          @dragover="allowDrop($event, TARGET_TYPES.OFFER)"
        >
          <span v-if="locale == 'en'">Drag <b>Offer</b> item here</span>
          <span v-else><b>오퍼</b> 아이템을 끌어다 놓으세요</span>
        </ItemDrop>
        <template v-for="item in offerList" :key="item.objUuid">
          <template v-if="item.childOffr?.length && !item?.disable">
            <template v-for="offer in item.childOffr" :key="offer.offrUuid">
              <div class="flex justify-between align-center gap-[12px]">
                <div
                  class="icon-container"
                  :class="[
                    { 'opacity-[32%]': item.disable },
                    { 'icon-active': item.targetUuid === activeGroupUuid },
                  ]"
                  @click.stop="
                    handleShowTargetDetail(
                      $event,
                      item,
                      relation.dpdcRelCode,
                      true
                    )
                  "
                >
                  <FolderIcon />
                </div>
                <cf-card-dropdown
                  :title="offer.offrNm"
                  :description="offer.offrCd"
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
                  class-name="default max-w-[calc(100%-52px)]"
                  type-bg="linear"
                  :border-color-action="setHoverColor(offer.offrType)"
                  :type-of-prod="setIconType(offer.offrType)"
                  :is-device-icon="
                    ['DE', 'DV'].includes(
                      offer.offrType.slice(0, 2).toUpperCase()
                    )
                  "
                  :icon-color="setIconColor(offer.offrType)"
                  dropdown-icon="mdi-dots-vertical"
                  :active="
                    setActiveOffer(
                      offer.offrNm,
                      offer.offrCd,
                      offer.offrUuid,
                      relation.dpdcRelCode
                    )
                  "
                  :node="{
                    hideNodeLeft: true,
                    isActiveNodeLeft: false,
                    hideNodeRight: true,
                    isActiveNodeRight: false,
                  }"
                  :expired="
                    item?.disable ||
                    ((isExpiredTime(item.validEndDtm) ||
                      isExpiredTime(offer.validEndDtm)) &&
                      !item.isAdded)
                  "
                  :disable="isExpiredTime(offer?.itemValidEndDtm)"
                  hide-detail
                  draggable
                  @on-click-card="
                    handleShowTargetDetail($event, offer, relation.dpdcRelCode)
                  "
                  @dragstart="handleOfferDragStart($event, offer)"
                />
              </div>
            </template>
          </template>
          <template v-else>
            <cf-card-dropdown
              v-if="item.lctgrItemName === TARGET_TYPE.OFFER"
              :title="item.targetName"
              :description="item.targetCode"
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
              type-bg="linear"
              :border-color-action="setHoverColor(item.itemCode)"
              :type-of-prod="setIconType(item.itemCode)"
              :is-device-icon="
                ['DE', 'DV'].includes(item.itemCode.slice(0, 2).toUpperCase())
              "
              :icon-color="setIconColor(item.itemCode)"
              dropdown-icon="mdi-dots-vertical"
              :active="
                setActiveOffer(
                  item.targetName,
                  item.targetCode,
                  item.targetUuid,
                  relation.dpdcRelCode
                )
              "
              :node="{
                hideNodeLeft: true,
                isActiveNodeLeft: false,
                hideNodeRight: true,
                isActiveNodeRight: false,
              }"
              hide-detail
              :show-icon-status="
                isEdit &&
                (item.isAdded || item.allowRemove || item?.allowExpire)
              "
              :editable="
                isEdit &&
                (item.isAdded || item.allowRemove || item?.allowExpire)
              "
              draggable
              :actions="
                listActions(item, item.isAdded, category, item.lctgrItemName)
              "
              :expired="isExpiredTime(item.validEndDtm) && !item.isAdded"
              :disable="isExpiredTime(item?.itemValidEndDtm)"
              :is-new="item.isAdded"
              @on-click-card="
                handleShowTargetDetail($event, item, relation.dpdcRelCode)
              "
              @dragstart="handleOfferDragStart($event, item)"
            />
          </template>
        </template>
      </div>
    </ExtendsAccordion>
    <div
      v-if="
        extendsView === EXTENDS_VIEW.DETAIL &&
        (groupList.child.length || isEdit)
      "
      class="flex w-[32px]"
    >
      <hr class="dashed-line" />
    </div>
    <ExtendsAccordion
      v-if="
        extendsView === EXTENDS_VIEW.DETAIL &&
        (groupList.child.length || isEdit)
      "
      v-model:expand="relation.isExpandGroup"
      type="group"
      title="Group"
      :disable="expiredRelation"
      :active="activeRelationGroup"
      :class-name="extendsView === EXTENDS_VIEW.DETAIL ? '!w-[284px]' : ''"
      :count="groupList.child.length"
      @on-expand="handleExpandGroupRelation($event, relation)"
      @on-click="handleClickRelGroup($event, groupList)"
    >
      <div class="flex flex-column gap-[12px]">
        <ItemDrop
          v-if="isEdit"
          @click.stop="handleOpenTargetSearch(TARGET_TYPES.GROUP)"
          @drop="drop($event, TARGET_TYPES.GROUP)"
          @dragover="allowDrop($event, TARGET_TYPES.GROUP)"
        >
          <span v-if="locale == 'en'">Drag <b>Group</b> item here</span>
          <span v-else><b>그룹</b> 아이템을 끌어다 놓으세요</span>
        </ItemDrop>
        <template v-for="item in groupList.child" :key="item.objUuid">
          <cf-card-dropdown
            v-if="item.lctgrItemName !== TARGET_TYPE.OFFER"
            class-name="default group-icon"
            :title="item.targetName"
            :description="item.targetCode"
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
            :width="extendsView === EXTENDS_VIEW.DETAIL ? 260 : 335"
            :active="
              setActiveGroup(
                item.targetName,
                item.targetCode,
                item.targetUuid,
                relation.dpdcRelCode
              )
            "
            type-bg="linear"
            border-color-action="yellow"
            dropdown-icon="mdi-dots-vertical"
            :expired="
              groupList.disable ||
              (isExpiredTime(item.validEndDtm) && !item.isAdded)
            "
            :disable="isExpiredTime(item?.itemValidEndDtm)"
            :node="{
              hideNodeLeft: true,
              isActiveNodeLeft: false,
              hideNodeRight: true,
              isActiveNodeRight: false,
            }"
            hide-detail
            :show-icon-status="
              isEdit && (item.isAdded || item.allowRemove || item?.allowExpire)
            "
            :editable="
              isEdit && (item.isAdded || item.allowRemove || item?.allowExpire)
            "
            :actions="
              listActions(item, item.isAdded, category, item.lctgrItemName)
            "
            :is-new="item.isAdded"
            draggable
            @on-click-card="
              handleShowTargetDetail($event, item, relation.dpdcRelCode)
            "
            @dragstart="handleGroupDragStart($event, item)"
          >
            <template #icon>
              <span class="flex justify-center align-center w-[40px] h-[40px]">
                <FolderIcon />
              </span>
            </template>
          </cf-card-dropdown>
        </template>
      </div>
    </ExtendsAccordion>
  </div>
  <DateTimePopup
    v-model:open-model="isOpenPopup"
    v-model="dateData"
    :modal-title="
      isExpiredTime(selectedInPopoverItem?.validEndDtm)
        ? $t('product_platform.enableOffer')
        : $t('product_platform.expireOffer')
    "
    :disabled-start-date="!isExpiredTime(selectedInPopoverItem?.validEndDtm)"
    :text-btn-cancel="$t('product_platform.cancel')"
    :text-btn-add="$t('product_platform.save')"
    :min-end-date="
      moment().format(DATE_FORMAT.DATE_FORMAT_WITHOUT_TIME_REVERSE)
    "
    :required-end-date="!isExpiredTime(selectedInPopoverItem?.validEndDtm)"
    required-start-date
    @submit="handleSubmit"
    @close="handleClose"
  />
  <DateTimePopup
    v-model:open-model="isOpenAddItemPopup"
    v-model="dateValid"
    :modal-title="
      allowGroupDrop?.includes(dropData?.itemCode)
        ? $t('product_platform.addGroup')
        : $t('product_platform.addOffer')
    "
    :text-btn-cancel="$t('product_platform.cancel')"
    :text-btn-add="$t('product_platform.save')"
    required-start-date
    @submit="handleSubmitAddItem"
    @close="handleCloseAddItem"
  />
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";
import moment from "moment-timezone";
import {
  EXTEND_CATEGORY,
  EXTENDS_VIEW,
  SELECT_LIST_TYPE,
  TARGET_TYPES,
} from "@/constants/extendsManager";
import {
  ACTION_TYPE,
  DATE_FORMAT,
  DETAIL_TAB_TYPE,
  TARGET_TYPE,
} from "@/constants/index";
import {
  useDragStore,
  useExtendManagerStore,
  useSnackbarStore,
  useHistoryTabStore,
  useRelationManagerDuplicateStore,
} from "@/store";
import { searchByKeyword } from "@/utils/extend-utils";
import {
  setIconColor,
  setIconType,
  setHoverColor,
} from "@/utils/impact-analysis-utils";
import useRedirect from "@/composables/useRedirect";
import {
  formatDate,
  formatDateWithOutSeconds,
  isExpiredTime,
} from "@/utils/format-data";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import { getOfferGroupRelation } from "@/api/prod/extendsApi";
import { getUserInfor } from "@/constants/userInfor";
import useDragUserPocket from "@/composables/useDragUserPocket";
import { LARGE_ITEM_CODE } from "@/store/userPocket.store";
import OpenInNewIcon from "@/components/prod/icons/OpenInNewIcon.vue";
import TrashIcon from "@/components/prod/icons/TrashIcon.vue";
import CheckVerified from "@/components/prod/icons/CheckVerified.vue";
import HourglassIcon from "@/components/prod/icons/HourglassIcon.vue";
import type { ActionType } from "@/interfaces/prod";

const { t, locale } = useI18n();
const { dragOfferType } = storeToRefs(useDragStore());
const historyStore = useHistoryTabStore();
const useSnackbar = useSnackbarStore();
const { moveOfferSearchPage, moveGroupSearchPage } = useRedirect();
const extendManagerStore = useExtendManagerStore();
const relationManagerDuplicateStore = useRelationManagerDuplicateStore();
const { handleDragUserPocket } = useDragUserPocket();

const emit = defineEmits(["onRemove"]);
const props = defineProps({
  relation: {
    type: Object as PropType<any>,
    require: true,
    default: () => {},
  },
  category: {
    type: String,
    default: "",
  },
  isRemove: {
    type: Boolean,
    default: false,
  },
  activeRelation: {
    type: Boolean,
    default: false,
  },
  activeRelationGroup: {
    type: Boolean,
    default: false,
  },
  searchObj: {
    type: Object,
    default: new Object(),
  },
  activeObj: {
    type: Object,
    default: new Object(),
  },
  relIndex: {
    type: Number,
    required: true,
  },
  offerDuplicateMode: {
    type: Boolean,
    default: false,
  },
  expired: {
    type: Boolean,
    default: false,
  },
  expiredRelation: {
    type: Boolean,
    default: false,
  },
});

const offerList = ref<any>(props.relation?.child);
const relation = ref<any>(props.relation);
const dropData = ref<any>();
const selectedInPopoverItem = ref<any>();
const isOpenPopup = ref(false);
const isOpenAddItemPopup = ref(false);
const groupOfferRelation = ref([]);
const dateData = reactive({
  startDate: "",
  endDate: "",
});
const dateValid = reactive({
  startDate: "",
  endDate: "",
});

const selectedStore = computed(() =>
  props.offerDuplicateMode ? relationManagerDuplicateStore : extendManagerStore
);
const {
  extendsView,
  isEdit,
  sideDisplay,
  selectedItem,
  expandLeaderItem,
  expandFollowerItem,
  paramsExtendsTargetPost,
  paramsHightlightSearch,
  structureActiveMapLeader,
  structureActiveMapFollower,
  activeGroupUuid,
  selectedStructureData,
  targetSearch,
  allowOfferDrop,
  allowGroupDrop,
  detailViewData,
  paramsExtendsTargetSearchGroup,
  expandLeaderItemIndex,
  expandFollowerItemIndex,
  isSearchGroupParentOffer,
} = storeToRefs(selectedStore.value);
const {
  addParamsExtendsTargetPost,
  updateExtendOffer,
  getExtendsDependencyRelationDefinitionDetail,
  getProductStructureDetailRoot,
  removeItemExtendsTargetPost,
  getGroupDetailInfo,
  resetLeaderFollowerList,
  getLeaderList,
  getFollowerList,
  resetStructureActiveMap,
  resetParamsExtendsTargetSearchGroup,
  resetParamsExtendsTargetSearchOffer,
} = selectedStore.value;

watch(
  () => props.relation,
  (newVal: any) => {
    relation.value = newVal;
    offerList.value = newVal.child;
  },
  { deep: true }
);

const groupList = computed<any>(() => {
  const groups = { child: [] };
  if (extendsView.value === EXTENDS_VIEW.DETAIL && props.relation?.child) {
    let disable = true;
    const children = props.relation.child.filter((item) => {
      if (item.lctgrItemName === TARGET_TYPE.GROUP) {
        return { ...item, active: true };
      }
    });
    children.forEach((group) => {
      if (!group.disable) {
        disable = false;
      }
    });
    groups["dpdcRelCode"] = props.relation.dpdcRelCode;
    groups["child"] = children;
    groups["disable"] = disable;
    return groups;
  } else {
    return groups;
  }
});
const countItem = computed(() => {
  let count = 0;
  if (offerList.value) {
    offerList.value.forEach((el) => {
      if (el.childOffr?.length && !el.disable) {
        el.childOffr.forEach(() => {
          count += 1;
        });
      }
      if (el.lctgrItemName === TARGET_TYPE.OFFER) {
        count += 1;
      }
    });
  } else {
    return count;
  }
  return count;
});

const handleOpenTargetSearch = (target) => {
  targetSearch.value = target;
  isSearchGroupParentOffer.value = false;
  if (target === TARGET_TYPES.OFFER) {
    resetParamsExtendsTargetSearchGroup();
  } else {
    resetParamsExtendsTargetSearchOffer();
    resetParamsExtendsTargetSearchGroup();
  }
  paramsExtendsTargetSearchGroup.value.childOffrUuid = undefined;
  if (props.offerDuplicateMode) {
    sideDisplay.value.relationSearch = false;
  } else {
    sideDisplay.value.relationDetail = false;
    sideDisplay.value.offerDetail = false;
    sideDisplay.value.targetDetail = false;
  }
  sideDisplay.value.targetSearch = true;
};

const allowDrop = (event, category) => {
  if (
    (category === TARGET_TYPES.OFFER &&
      allowOfferDrop.value.includes(dragOfferType.value)) ||
    (category === TARGET_TYPES.GROUP &&
      allowGroupDrop.value.includes(dragOfferType.value))
  ) {
    event.preventDefault();
    return true;
  }
  return false;
};

const checkInvalidItemDrop = (
  listCheck,
  dropItem,
  groupChild: any = [],
  isGroup = false
) => {
  let invalidItem = false;
  if (isGroup) {
    const itemBeAdd =
      props.category === EXTEND_CATEGORY.FOLLOWER
        ? expandFollowerItem.value
        : expandLeaderItem.value;
    if (itemBeAdd?.offerGroupUuid === dropItem.objUuid) invalidItem = true;
  }

  listCheck.forEach((itemFocus) => {
    const list =
      props.category === EXTEND_CATEGORY.FOLLOWER
        ? itemFocus.followerList
        : itemFocus.leaderList;
    list?.forEach((rel) => {
      if (rel.dpdcRelUuid === props.relation?.dpdcRelUuid) {
        rel.child?.forEach((prod) => {
          if (prod?.targetUuid === dropItem.objUuid) invalidItem = true;
          if (groupChild?.length) {
            groupChild.forEach((item) => {
              if (item?.offrUuid === prod?.targetUuid) invalidItem = true;
            });
          }
          if (prod?.childOffr?.length) {
            prod.childOffr.forEach((offer) => {
              if (groupChild?.length) {
                groupChild.forEach((item) => {
                  if (item?.offrUuid === offer?.offrUuid) invalidItem = true;
                });
              } else {
                if (dropItem.objUuid === offer?.offrUuid) invalidItem = true;
              }
            });
          }
        });
      }
    });
  });
  return invalidItem;
};

const drop = async (event, _category) => {
  event.preventDefault();
  dropData.value = event.dataTransfer.getData("item")
    ? JSON.parse(event.dataTransfer.getData("item"))
    : null;
  if (dropData.value) {
    let isDupplicate = false;
    if (allowGroupDrop.value.includes(dropData.value.itemCode)) {
      let invalidItem = false;
      const { data: dropDataOffer } = await getOfferGroupRelation({
        objUuid: dropData.value.objUuid,
      });
      invalidItem = checkInvalidItemDrop(
        props.category === EXTEND_CATEGORY.FOLLOWER
          ? detailViewData.value.focusColumnLeaderList
          : detailViewData.value.focusColumnFollowerList,
        dropData.value,
        dropDataOffer,
        true
      );

      if (invalidItem) {
        useSnackbar.showSnackbar(
          t("product_platform.relation.addItemInvalid"),
          "error"
        );
        isDupplicate = true;
        return;
      }
      groupOfferRelation.value = dropDataOffer;
    } else if (allowOfferDrop.value.includes(dropData.value.itemCode)) {
      let invalidOffer = false;
      invalidOffer = checkInvalidItemDrop(
        props.category === EXTEND_CATEGORY.FOLLOWER
          ? detailViewData.value.focusColumnLeaderList
          : detailViewData.value.focusColumnFollowerList,
        dropData.value
      );
      if (invalidOffer) {
        useSnackbar.showSnackbar(
          t("product_platform.relation.addItemInvalid"),
          "error"
        );
        isDupplicate = true;
        return;
      }
    }
    if (!isDupplicate) {
      isOpenAddItemPopup.value = true;
    }
  }
};

const checkExpiredRelation = (item: any) => {
  if (
    moment(item?.validEndDtm, DATE_FORMAT.DATE_TYPE).isSame(new Date(), "day")
  ) {
    return true;
  }
  return isExpiredTime(item?.validEndDtm);
};

const listActions = (
  item: any,
  isAdd: boolean = false,
  category: string,
  largeItemCode: string
): ActionType[] => {
  const isItemExpired = isExpiredTime(item?.itemValidEndDtm);
  if (isItemExpired) return [];
  const openNewTabAction = {
    name: t("product_platform.openinNewWindow"),
    icon: OpenInNewIcon,
    onClick: () => {
      if (largeItemCode.toLowerCase() === TARGET_TYPES.OFFER) {
        moveOfferSearchPage({
          itemCode: item?.itemCode || "",
          objCode: item?.targetCode || "",
          itemCodeName: item?.itemCodeName || "",
          objUuid: item?.targetUuid || "",
        });
      }
      if (largeItemCode.toLowerCase() === TARGET_TYPES.GROUP) {
        moveGroupSearchPage({
          ...item,
          itemId: item.targetUuid,
          name: item.targetName,
          code: item.targetCode,
          type: item.itemCode,
          offerGroupTypeCode: item.itemCodeName,
        });
      }
    },
  };
  const expiredRelation = checkExpiredRelation(item);
  if (isAdd || item?.allowRemove) {
    return [
      {
        name: t("product_platform.actionRemove"),
        icon: TrashIcon,
        onClick: () => {
          let index;
          if (item.targetUuid) {
            index = offerList.value.findIndex(
              (offer) => offer.targetUuid === item.targetUuid
            );
          }
          if (index !== -1) {
            removeItemExtendsTargetPost(item, category);
            offerList.value.splice(index, 1);
            setCoordinates(props.category);
          }
        },
      },
      openNewTabAction,
    ];
  }
  const action = {
    name: expiredRelation
      ? t("product_platform.actionEnable")
      : t("product_platform.actionExpire"),
    icon: expiredRelation ? CheckVerified : HourglassIcon,
    onClick: () => {
      dateData.startDate = formatDateWithOutSeconds(item?.validStartDtm) || "";
      dateData.endDate = formatDateWithOutSeconds(item?.validEndDtm) || "";
      selectedInPopoverItem.value = item;
      isOpenPopup.value = true;
    },
  };
  return [action, openNewTabAction];
};

const handleSubmit = async () => {
  const userInfo = getUserInfor();
  const validateStart = !dateData.startDate;
  const validateStartAndEnd = !dateData.startDate || !dateData.endDate;
  const validateCondition = isExpiredTime(
    selectedInPopoverItem.value?.validEndDtm
  )
    ? validateStart
    : validateStartAndEnd;
  if (validateCondition) {
    useSnackbar.showSnackbar(
      validateStart
        ? t("product_platform.required_start_date")
        : t("product_platform.required_end_date"),
      "error"
    );
    return;
  }
  const updateData: any = {
    baseUuid:
      props.category === EXTEND_CATEGORY.FOLLOWER
        ? selectedItem.value?.prodUuid
        : selectedInPopoverItem.value.targetUuid,
    trgtUuid:
      props.category === EXTEND_CATEGORY.FOLLOWER
        ? selectedInPopoverItem.value.targetUuid
        : selectedItem.value.prodUuid,
    dpdcRelUuid: props.relation?.dpdcRelUuid,
    validStartDtm: formatDate(dateData.startDate),
    validEndDtm: formatDate(dateData.endDate),
    workTypeCode: isExpiredTime(selectedInPopoverItem.value?.validEndDtm)
      ? dateData.endDate
        ? ACTION_TYPE.ENABLED_EXPIRED
        : ACTION_TYPE.ENABLED
      : ACTION_TYPE.EXPIRED,
    chgDeptName: userInfo.chgDeptName,
    chgUser: userInfo.chgUser,
  };
  isOpenPopup.value = false;
  if (extendsView.value === EXTENDS_VIEW.DETAIL) {
    const findTargetItemIndex =
      paramsExtendsTargetPost.value.updateOffrDpdcLst.findIndex(
        (item) =>
          item.baseUuid === updateData.baseUuid &&
          item.dpdcRelUuid === updateData.dpdcRelUuid &&
          item.trgtUuid === updateData.trgtUuid
      );
    if (findTargetItemIndex !== -1) {
      paramsExtendsTargetPost.value.updateOffrDpdcLst[
        findTargetItemIndex as number
      ] = updateData;
    } else {
      paramsExtendsTargetPost.value.updateOffrDpdcLst.push(updateData);
    }
  } else {
    const res = await updateExtendOffer(updateData);
    if (res && res?.status === 200) {
      useSnackbar.showSnackbar(
        t("product_platform.successfully_saved"),
        "success"
      );
      resetLeaderFollowerList();
      getLeaderList();
      getFollowerList();
      detailViewData.value.isShowFollowerCol = false;
      detailViewData.value.isShowLeaderCol = false;
    } else {
      useSnackbar.showSnackbar(
        t("product_platform.something_went_wrong"),
        "error"
      );
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
  const uuid =
    extendsView.value === EXTENDS_VIEW.SIMPLE
      ? selectedItem.value.objUuid
      : props.category === EXTEND_CATEGORY.FOLLOWER
        ? expandLeaderItemIndex.value
          ? expandFollowerItem.value?.offerGroupUuid
          : selectedItem.value.objUuid
        : expandFollowerItemIndex.value
          ? expandLeaderItem.value?.offerGroupUuid
          : selectedItem.value.objUuid;
  const objItem = {
    ...dropData.value,
    parentUuid: uuid,
    targetUuid: dropData.value.objUuid,
    targetName: dropData.value.objName,
    targetCode: dropData.value.objCode,
    dpdcRelUuid: props.relation?.dpdcRelUuid,
    dpdcRelCode: props.relation?.dpdcRelCode,
    dpdcRelName: props.relation?.dpdcRelName,
    lctgrItemName: allowGroupDrop.value.includes(dropData.value.itemCode)
      ? TARGET_TYPE.GROUP
      : TARGET_TYPE.OFFER,
    validStartDtm: dateValid.startDate,
    validEndDtm: dateValid.startDate,
    childOffr: allowGroupDrop.value.includes(dropData.value.itemCode)
      ? groupOfferRelation.value
      : null,
    isAdded: true,
  };
  const addData = {
    baseUuid:
      props.category === EXTEND_CATEGORY.FOLLOWER
        ? uuid
        : dropData.value.objUuid,
    trgtUuid:
      props.category === EXTEND_CATEGORY.FOLLOWER
        ? dropData.value.objUuid
        : uuid,
    dpdcRelUuid: props.relation?.dpdcRelUuid,
    validStartDtm: dateValid.startDate,
    validEndDtm: dateValid.endDate ? dateValid.endDate : null,
    itemName: dropData.value.objName,
  };
  if (props.category === EXTEND_CATEGORY.FOLLOWER) {
    detailViewData.value.focusColumnLeaderList.forEach((prod, index) => {
      if (index === expandLeaderItemIndex.value) {
        prod.followerList[props.relIndex]?.child.unshift(objItem);
        if (!prod?.isAdded) {
          prod["addNewProd"] = true;
        }
      }
    });
  } else {
    detailViewData.value.focusColumnFollowerList.forEach((prod, index) => {
      if (index === expandFollowerItemIndex.value) {
        prod.leaderList[props.relIndex]?.child.unshift(objItem);
        if (!prod?.isAdded) {
          prod["addNewProd"] = true;
        }
      }
    });
  }
  addParamsExtendsTargetPost(addData);
  handleCloseAddItem();
  nextTick(() => {
    setCoordinates(props.category);
    setCanvasHeight();
  });
};

const handleClose = () => {
  isOpenPopup.value = false;
  dateData.endDate = "";
  dateData.startDate = "";
};

const handleCloseAddItem = () => {
  isOpenAddItemPopup.value = false;
  dateValid.endDate = "";
  dateValid.startDate = "";
};

const resetCanvas = () => {
  nextTick(() => {
    setCanvasHeight();
  });
  setCoordinates(props.category);
};

const handleExpand = (event, relation) => {
  relation["isExpand"] = event;
  resetCanvas();
};

const handleExpandGroupRelation = (event, relation) => {
  relation["isExpandGroup"] = event;
  resetCanvas();
};

const handleClickRel = async (event, item) => {
  paramsHightlightSearch.value.keyword = undefined;
  resetStructureActiveMap();
  if (event) {
    if (props.category === EXTEND_CATEGORY.LEADER) {
      structureActiveMapLeader.value.relation = item;
    } else {
      structureActiveMapFollower.value.relation = item;
    }
    await getExtendsDependencyRelationDefinitionDetail(item.dpdcRelUuid);
    sideDisplay.value.targetSearch = false;
    sideDisplay.value.targetDetail = false;
    sideDisplay.value.offerDetail = false;
    sideDisplay.value.relationDetail = true;
  }
};

const handleClickRelGroup = (event, item) => {
  paramsHightlightSearch.value.keyword = undefined;
  resetStructureActiveMap();
  if (event) {
    if (props.category === EXTEND_CATEGORY.LEADER) {
      structureActiveMapLeader.value.relationGroup = item;
    } else {
      structureActiveMapFollower.value.relationGroup = item;
    }
  }
};

const handleShowTargetDetail = async (
  _event,
  item,
  relCode,
  findGroup = false
) => {
  paramsHightlightSearch.value.keyword = undefined;
  resetStructureActiveMap();
  if (item.lctgrItemName !== TARGET_TYPE.OFFER) {
    activeGroupUuid.value = item.targetUuid;
  } else {
    activeGroupUuid.value = null;
  }
  const objUuid = item.targetUuid
    ? item.targetUuid
    : item.offrUuid
      ? item.offrUuid
      : item.prodUuid;
  historyStore.fetchHistory({ objUuid });

  if (item.lctgrItemName === TARGET_TYPE.OFFER || item.offrUuid) {
    if (props.category === EXTEND_CATEGORY.LEADER) {
      structureActiveMapLeader.value.offer.offrUuid = item.targetUuid
        ? item.targetUuid
        : item.offrUuid;
      structureActiveMapLeader.value.offer.relCode = relCode;
    } else {
      structureActiveMapFollower.value.offer.offrUuid = item.targetUuid
        ? item.targetUuid
        : item.offrUuid;
      structureActiveMapFollower.value.offer.relCode = relCode;
    }
    getOfferDetail(item);
    sideDisplay.value.targetSearch = false;
    sideDisplay.value.targetDetail = false;
    sideDisplay.value.relationDetail = false;
    sideDisplay.value.offerDetail = true;
  } else {
    if (props.category === EXTEND_CATEGORY.LEADER) {
      structureActiveMapLeader.value.group.targetUuid = item.targetUuid
        ? item.targetUuid
        : item.offrUuid;
      structureActiveMapLeader.value.group.relCode = relCode;
    } else {
      structureActiveMapFollower.value.group.targetUuid = item.targetUuid
        ? item.targetUuid
        : item.offrUuid;
      structureActiveMapFollower.value.group.relCode = relCode;
    }
    if (!findGroup) {
      await getGroupDetailInfo(item.targetUuid);
      sideDisplay.value.targetSearch = false;
      sideDisplay.value.offerDetail = false;
      sideDisplay.value.relationDetail = false;
      sideDisplay.value.targetDetail = true;
    }
  }
};

const handleRemoveRalation = (relation) => {
  emit("onRemove", relation.dpdcRelUuid);
  if (
    props.category === EXTEND_CATEGORY.LEADER &&
    structureActiveMapLeader.value.relation?.dpdcRelUuid ===
      relation.dpdcRelUuid
  ) {
    structureActiveMapLeader.value.relation = null;
  } else if (
    props.category === EXTEND_CATEGORY.FOLLOWER &&
    structureActiveMapLeader.value.relation?.dpdcRelUuid ===
      relation.dpdcRelUuid
  ) {
    structureActiveMapFollower.value.relation = null;
  }
};

const getOfferDetail = async (item) => {
  let params = {
    objUuid: item.offrUuid || item.targetUuid || item.prodUuid,
  };
  const { data } = await getProductStructureDetailRoot(params);
  if (data) {
    handleResponse(data);
  }
};

const handleResponse = (data) => {
  if (data) {
    selectedStructureData.value = {
      general: [
        ...data.general,
        ...data?.additional.filter(
          (i) => i.dispTab === DETAIL_TAB_TYPE.GENERAL
        ),
      ],
      additional: data?.additional
        .filter((i) => i.dispTab === DETAIL_TAB_TYPE.ADDITIONAL)
        .map((item) => ({
          ...item,
          attrVal:
            item.fieldTypeCode === COLUMN_FIELD_TYPE.DM
              ? JSON.parse(item?.attrVal)?.filter((val) => val.trim()) || []
              : item.attrVal,
        })),
    };
  } else {
    selectedStructureData.value = {
      general: [],
      additional: [],
    };
  }
};

const setActiveGroup = (name, code, uuid, relCd) => {
  return (
    searchByKeyword(
      name,
      code,
      SELECT_LIST_TYPE.GROUP,
      props.searchObj as any
    ) ||
    uuid === activeGroupUuid.value ||
    (uuid === props.activeObj.group?.targetUuid &&
      relCd === props.activeObj.group?.relCode)
  );
};

const setActiveOffer = (name, code, uuid, relCd) => {
  return (
    searchByKeyword(
      name,
      code,
      SELECT_LIST_TYPE.OFFER,
      props.searchObj as any
    ) ||
    (uuid === props.activeObj.offer?.offrUuid &&
      relCd === props.activeObj.offer?.relCode)
  );
};

const handleGroupDragStart = (event: DragEvent, item: any): void => {
  event.stopPropagation();
  handleDragUserPocket(event, {
    userPocketType: LARGE_ITEM_CODE.GROUP,
    ...item,
  });
};

const handleOfferDragStart = (event: DragEvent, item: any): void => {
  event.stopPropagation();
  handleDragUserPocket(event, {
    userPocketType: LARGE_ITEM_CODE.OFFER,
    ...item,
  });
};

const { setCoordinates } = inject<any>("handleCalCoordinates");
const { setCanvasHeight } = inject<any>("handleCalCanvasHeight");
</script>

<style lang="scss" scoped>
.dashed-line {
  border-top: 2px #bdc1c7 dashed;
  width: 100%;
  margin-top: 33px;
}
.reverse {
  flex-direction: row-reverse;
}
.icon-container {
  display: flex;
  justify-content: center;
  align-items: center;
  background: #fff6e9;
  border: 2px solid rgba(255, 255, 255, 1);
  border-radius: 8px;
  min-height: 40px;
  min-width: 40px;
  box-shadow: 0px 2px 12px 0px #00000014;
  cursor: pointer;
}
.icon-active {
  border-color: #d9325a;
}
</style>
