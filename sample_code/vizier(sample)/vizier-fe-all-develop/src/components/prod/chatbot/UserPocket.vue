<template>
  <div class="user-pocket-wrapper">
    <div class="drop-section">
      <ItemDrop @drop="drop($event)" @dragover="allowDrop($event)" />
    </div>
    <div class="list-item-userpocket">
      <ul class="category">
        <li
          v-for="catItem in userPocketData"
          :key="catItem.code"
          class="category-item"
        >
          <div class="category-title">
            <span class="rect"></span>{{ catItem.name }}
          </div>
          <ul class="child-wrapper">
            <VueDraggable v-model="catItem.items" sort handle=".item-draggable">
              <template v-for="item in catItem.items" :key="item?.itemId">
                <li
                  v-if="catItem.code === 'O'"
                  class="offer-item zoom-animation"
                  :class="item.draggable ? 'item-draggable' : 'not-draggable'"
                  @dragstart="(event) => handleDragComponentStart(event, item)"
                  @dragend="handleDragComponentEnd"
                >
                  <span
                    class="offer-type"
                    :class="`type-${item.offerType?.charAt(0).toLowerCase()}`"
                  >
                    {{ item.offerType?.charAt(0) }}
                  </span>
                  <div class="offer-info">
                    <span class="title">
                      <CustomTooltip :content="item.name" />
                    </span>
                    <span class="code">{{ item.code }}</span>
                  </div>
                  <BasePopover
                    :options="
                      listActionsForOffer({ itemType: catItem.code, ...item })
                    "
                    custom-location="bottom-left"
                    class="flex-initial"
                  >
                    <template #activator>
                      <DotsVerticalIcon />
                    </template>
                  </BasePopover>
                </li>
                <li
                  v-if="catItem.code === 'C'"
                  class="component-item zoom-animation"
                  :class="[
                    `type-${item.middleType?.toLowerCase()}`,
                    item.draggable ? 'item-draggable' : 'not-draggable',
                  ]"
                  @dragstart="(event) => handleDragComponentStart(event, item)"
                  @dragend="handleDragComponentEnd"
                >
                  <div class="item-wrapper">
                    <div
                      class="component-info"
                      :class="`type-${item.middleType?.toLowerCase()}`"
                    >
                      <span class="title">
                        <CustomTooltip :content="item.name" />
                      </span>
                      <span class="code">{{ item.code }}</span>
                    </div>
                    <BasePopover
                      :options="
                        listActionsForComponentResource({
                          itemType: catItem.code,
                          ...item,
                        })
                      "
                      custom-location="bottom-left"
                      class="flex-initial"
                    >
                      <template #activator>
                        <DotsVerticalIcon />
                      </template>
                    </BasePopover>
                  </div>
                </li>
                <li
                  v-if="catItem.code === 'R'"
                  class="resource-item zoom-animation"
                  :class="item.draggable ? 'item-draggable' : 'not-draggable'"
                  @dragstart="(event) => handleDragComponentStart(event, item)"
                  @dragend="handleDragComponentEnd"
                >
                  <span class="resource-type">
                    <span
                      :class="`gradient-${item.offerType
                        ?.charAt(0)
                        .toLocaleLowerCase()}`"
                    >
                      {{ item.offerType?.charAt(0) }}
                    </span>
                  </span>
                  <div class="resource-info">
                    <span class="title">
                      <CustomTooltip :content="item.name" />
                    </span>
                    <span class="code">{{ item.code }}</span>
                  </div>
                  <BasePopover
                    :options="
                      listActionsForComponentResource({
                        itemType: catItem.code,
                        ...item,
                      })
                    "
                    custom-location="bottom-left"
                    class="flex-initial"
                  >
                    <template #activator>
                      <DotsVerticalIcon />
                    </template>
                  </BasePopover>
                </li>
                <li
                  v-if="catItem.code === 'G'"
                  class="group-item zoom-animation"
                  :class="item.draggable ? 'item-draggable' : 'not-draggable'"
                  @dragstart="(event) => handleDragComponentStart(event, item)"
                  @dragend="handleDragComponentEnd"
                >
                  <span class="group-type">
                    <FolderIcon />
                  </span>
                  <div class="group-info">
                    <span class="title">
                      <CustomTooltip :content="item.name" />
                    </span>
                  </div>
                  <BasePopover
                    :options="
                      listActionsForGroup({ itemType: catItem.code, ...item })
                    "
                    custom-location="bottom-left"
                    class="flex-initial"
                  >
                    <template #activator>
                      <DotsVerticalIcon />
                    </template>
                  </BasePopover>
                </li>
                <li
                  v-if="catItem.code === 'B'"
                  class="relation-item zoom-animation"
                  :class="item.draggable ? 'item-draggable' : 'not-draggable'"
                  @dragstart="(event) => handleDragComponentStart(event, item)"
                  @dragend="handleDragComponentEnd"
                >
                  <span class="relation-type">
                    <v-icon icon="mdi-cog" color="#BDC1C7"></v-icon>
                  </span>
                  <div class="relation-info">
                    <span class="title">
                      <CustomTooltip :content="item.name" />
                    </span>
                  </div>
                  <BasePopover
                    :options="
                      listActionsForRelation({
                        itemType: catItem.code,
                        ...item,
                      })
                    "
                    custom-location="bottom-left"
                    class="flex-initial relation-option-class"
                  >
                    <template #activator>
                      <DotsVerticalIcon />
                    </template>
                  </BasePopover>
                </li>
              </template>
            </VueDraggable>
          </ul>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import moment from "moment-timezone";
import { VueDraggable } from "vue-draggable-plus";
import { useI18n } from "vue-i18n";
import router from "@/router";
import { SELECT_LIST_TYPE } from "@/constants/extendsManager";
import {
  useAddOnStore,
  useDiscountStore,
  useDragStore,
  usePricePlanStore,
  useSnackbarStore,
  useStructureStore,
  useCreateStructureStore,
  useMultiEntityCreateStore,
  useMultiEntitySearchStore,
} from "@/store";
import userPocketStore, { LARGE_ITEM_CODE } from "@/store/userPocket.store";
import useDragUserPocket from "@/composables/useDragUserPocket";
import useRedirect from "@/composables/useRedirect";
import { MULTI_ENTITY_DROP_TYPE } from "@/constants/multiEntity";
import { LargeItemCode } from "@/enums";
import BasePopover from "../common/BasePopover.vue";
import DeleteIcon from "../icons/DeleteIcon.vue";
import FolderIcon from "../icons/FolderIcon.vue";
import OpenInNewIcon from "../icons/OpenInNewIcon.vue";

const { t, locale } = useI18n();
const structureStore = useStructureStore();
const createStructureStore = useCreateStructureStore();
const pricePlaneStore = usePricePlanStore();
const addOnStore = useAddOnStore();
const discountStore = useDiscountStore();
const { dragOfferType: dragType, isDragging } = storeToRefs(useDragStore());
const { userPocketData, countUserPocketItems, maxItemInUserPocket } =
  storeToRefs(userPocketStore());
const {
  getUserPocketData,
  addUserPocketItem,
  removeUserPocketItem,
  checkIsExistUserPocket,
} = userPocketStore();
const { dragType: dragSearch } = storeToRefs(useMultiEntitySearchStore());
const { dragType: dragCreate } = storeToRefs(useMultiEntityCreateStore());
const { showSnackbar } = useSnackbarStore();
const {
  moveOfferSearchPage,
  moveComponentSearchPage,
  moveResourceSearchPage,
  moveGroupSearchPage,
  moveRelationManagerPage,
  moveRelationSearchPage,
  moveImpactAnalysisPage,
  moveTreeViewPage,
  moveGroupWithOfferPage,
} = useRedirect();
const { handleDragUserPocket } = useDragUserPocket();

const relationPaths = ["/temp-ui/extends/relation/manager"];
const offerCreatePaths = [
  "/temp-ui/catalog/offer-create/priceplan",
  "/temp-ui/catalog/offer-create/add-on",
  "/temp-ui/catalog/offer-create/discount",
];

onBeforeMount(() => {
  getUserPocketData();
});

const handleItemClick = (item: any): void => {
  const handlers = {
    [LARGE_ITEM_CODE.OFFER]: () =>
      moveOfferSearchPage({
        itemCode: item.offerType,
        itemCodeName: "",
        objCode: item?.code || "",
        objUuid: item?.itemId || "",
        offerType: item?.offerType || "",
      }),
    [LARGE_ITEM_CODE.COMPONENT]: () =>
      moveComponentSearchPage({
        code: item.code,
        itemId: item.itemId,
        middleType: item.middleType,
        offerType: item.offerType,
        name: item.name,
        subType: item.subType,
        type: item.type,
      }),
    [LARGE_ITEM_CODE.GROUP]: () => moveGroupSearchPage(item),
    [LARGE_ITEM_CODE.RESOURCE]: () =>
      moveResourceSearchPage({
        itemCode: item.offerType,
        objCode: item.code,
      }),
    [LARGE_ITEM_CODE.RELATION]: () => moveRelationSearchPage(item),
  };

  handlers[item.itemType]?.();
};

const listActionsForOffer = (item: any) => [
  {
    name: t("product_platform.openinNewWindow"),
    icon: OpenInNewIcon,
    iconProps: { class: "text-text-lighter" },
    onClick: () => {
      handleItemClick(item);
    },
  },
  {
    name: t("product_platform.open_in_tree_view"),
    icon: OpenInNewIcon,
    iconProps: { class: "text-text-lighter" },
    onClick: () => {
      moveTreeViewPage(item);
    },
  },
  {
    name: t("product_platform.open_in_relation_manager"),
    icon: OpenInNewIcon,
    iconProps: { class: "text-text-lighter" },
    onClick: () => {
      moveRelationManagerPage({
        itemType: item.itemType,
        offerType: item.offerType,
        code: item.code,
      });
    },
  },
  {
    name: t("product_platform.open_in_impact_analysis"),
    icon: OpenInNewIcon,
    iconProps: { class: "text-text-lighter" },
    onClick: () => {
      moveImpactAnalysisPage(item);
    },
  },
  {
    name: t("product_platform.open_in_group"),
    icon: OpenInNewIcon,
    iconProps: { class: "text-text-lighter" },
    onClick: () => {
      moveGroupWithOfferPage(item);
    },
  },
  {
    name: t("product_platform.remove_from_pocket"),
    icon: DeleteIcon,
    iconProps: { fill: "#C7291D" },
    itemClass: { class: "text-[#C7291D]" },
    onClick: () => {
      removeUserPocketItem(item);
    },
  },
];

const listActionsForComponentResource = (item: any) => [
  {
    name: t("product_platform.openinNewWindow"),
    icon: OpenInNewIcon,
    iconProps: { class: "text-text-lighter" },
    onClick: () => {
      handleItemClick(item);
    },
  },
  {
    name: t("product_platform.open_in_impact_analysis"),
    icon: OpenInNewIcon,
    iconProps: { class: "text-text-lighter" },
    onClick: () => {
      moveImpactAnalysisPage(item);
    },
  },
  {
    name: t("product_platform.remove_from_pocket"),
    icon: DeleteIcon,
    iconProps: { fill: "#C7291D" },
    itemClass: { class: "text-[#C7291D]" },
    onClick: () => {
      removeUserPocketItem(item);
    },
  },
];

const listActionsForGroup = (item: any) => [
  {
    name: t("product_platform.openinNewWindow"),
    icon: OpenInNewIcon,
    iconProps: { class: "text-text-lighter" },
    onClick: () => {
      handleItemClick(item);
    },
  },
  {
    name: t("product_platform.remove_from_pocket"),
    icon: DeleteIcon,
    iconProps: { fill: "#C7291D" },
    itemClass: { class: "text-[#C7291D]" },
    onClick: () => {
      removeUserPocketItem(item);
    },
  },
];

const listActionsForRelation = (item: any) => [
  {
    name: t("product_platform.openinNewWindow"),
    icon: OpenInNewIcon,
    iconProps: { class: "text-text-lighter" },
    onClick: () => {
      handleItemClick(item);
    },
  },
  {
    name: t("product_platform.remove_from_pocket"),
    icon: DeleteIcon,
    iconProps: { fill: "#C7291D" },
    itemClass: { class: "text-[#C7291D]" },
    onClick: () => {
      removeUserPocketItem(item);
    },
  },
];

const handleDragComponentStart = (event: DragEvent, item: any) => {
  const path = router.currentRoute.value.path;
  if (relationPaths.includes(path)) {
    if (!["O", "G", "B"].includes(item.itemType)) {
      return false;
    }
  } else if (offerCreatePaths.includes(path)) {
    if (!["C"].includes(item.itemType)) {
      return false;
    }
  }

  const newItem = {
    componentType: item.middleType,
    itemCode: item.type,
    objCode: item.code,
    objName: item.name,
    objUUID: item.itemId,
    objUuid: item.itemId,
    validEndDtm: item.validEndDtm,
    validStartDtm: item.validStartDtm,
    uuid: item.itemId,
    offerUuid: item.itemId,
    prodItemNm: item.name,
    prodUuid: item.itemId,
    prodItemCd: item.code,
    dpdcRelUuid: item.itemId,
    dpdcRelName: item.name,
    dpdcRelCode: item.code,
    ...item,
  };
  const dragOfferType = newItem?.itemCode;
  // Handle for relation page
  if (relationPaths.includes(path)) {
    dragType.value = dragOfferType || "";
    if (item.itemType === "G") {
      dragType.value = SELECT_LIST_TYPE.GROUP;
      newItem.type = "Group";
    } else if (item.itemType === "B") {
      dragType.value = SELECT_LIST_TYPE.RELATION;
    }
  } else {
    dragType.value = dragOfferType || "";
  }
  addOnStore.dragOfferType = dragOfferType || "";
  discountStore.dragOfferType = dragOfferType || "";
  pricePlaneStore.dragOfferType = dragOfferType || "";
  structureStore.dragOfferType = dragOfferType || "";
  createStructureStore.dragOfferType = dragOfferType || "";
  switch (item.itemType) {
    case LargeItemCode.Offer:
      dragSearch.value = MULTI_ENTITY_DROP_TYPE.OFFER;
      dragCreate.value = MULTI_ENTITY_DROP_TYPE.OFFER;
      break;
    case LargeItemCode.Component:
      dragSearch.value = MULTI_ENTITY_DROP_TYPE.COMPONENT;
      dragCreate.value = MULTI_ENTITY_DROP_TYPE.COMPONENT;
      break;
    case LargeItemCode.Resource:
      dragSearch.value = MULTI_ENTITY_DROP_TYPE.RESOURCE;
      dragCreate.value = MULTI_ENTITY_DROP_TYPE.RESOURCE;
      break;
    case LargeItemCode.Group:
      dragSearch.value = MULTI_ENTITY_DROP_TYPE.GROUP;
      dragCreate.value = MULTI_ENTITY_DROP_TYPE.GROUP;
      break;
    default:
      dragSearch.value = "";
      dragCreate.value = "";
      break;
  }
  addOnStore.isDragging = true;
  discountStore.isDragging = true;
  pricePlaneStore.isDragging = true;
  structureStore.isDragging = true;
  createStructureStore.isDragging = true;
  isDragging.value = true;
  handleDragUserPocket(event, newItem);
};

const handleDragComponentEnd = () => {
  addOnStore.isDragging = false;
  discountStore.isDragging = false;
  pricePlaneStore.isDragging = false;
  structureStore.isDragging = false;
  createStructureStore.isDragging = false;
  isDragging.value = false;
};

const allowDrop = (event: DragEvent): void => {
  event.preventDefault();
};

const drop = (event: DragEvent): void => {
  event.preventDefault();
  try {
    const item = event.dataTransfer?.getData("item")
      ? JSON.parse(event.dataTransfer.getData("item"))
      : null;
    if (!item || !item?.userPocketType) {
      showSnackbar(t("product_platform.inValidItem"), "error");
      return;
    }
    if (countUserPocketItems.value === maxItemInUserPocket.value) {
      showSnackbar(t("product_platform.maxItemUP"), "error");
      return;
    }
    if (!checkValidDate(item)) {
      showSnackbar(t("product_platform.inValidItem"), "error");
      return;
    }
    if (checkIsExistUserPocket(item)) {
      showSnackbar(t("product_platform.itemExist"), "error");
      return;
    }
    addUserPocketItem(item);
  } catch (error) {
    showSnackbar("Cannot drag item here", "error");
  }
};

const checkValidDate = (data) => {
  let endDate = null;
  switch (data.userPocketType) {
    case LARGE_ITEM_CODE.OFFER:
      if (data.itemValidEndDtm || data.itemValidEndDtm === null) {
        endDate = data.itemValidEndDtm;
      } else {
        endDate = data.validEndDtm || data.validEndDtm;
      }
      break;
    case LARGE_ITEM_CODE.GROUP:
      endDate = data.validEndDtm;
      break;
    case LARGE_ITEM_CODE.COMPONENT:
      if (data.itemValidEndDtm || data.itemValidEndDtm === null) {
        endDate = data.itemValidEndDtm;
      } else {
        endDate = data.validEndDtm || data.validEndDtm || data.endDate;
      }
      break;
    case LARGE_ITEM_CODE.RESOURCE:
      if (data.itemValidEndDtm || data.itemValidEndDtm === null) {
        endDate = data.itemValidEndDtm;
      } else {
        endDate = data.validEndDtm || data.validEndDtm;
      }
      break;
    case LARGE_ITEM_CODE.RELATION:
      if (data.itemValidEndDtm || data.itemValidEndDtm === null) {
        endDate = data.itemValidEndDtm;
      } else {
        endDate = data.relationValidEndDate || data.validEndDtm;
      }
      break;
    default:
      break;
  }
  if (!endDate) {
    return true;
  }
  const today = moment();
  if (moment(endDate).isBefore(today)) {
    return false;
  }
  return true;
};

watch(
  () => locale.value,
  () => {
    getUserPocketData();
  }
);
</script>

<style lang="scss">
.user-pocket-wrapper {
  font-family: "Noto Sans KR";
  .drop-section {
    height: 88px;
    padding: 16px;
  }

  .list-item-userpocket {
    margin: 0 4px 0 16px;
    padding-bottom: 20px;
    &::-webkit-scrollbar {
      height: 6px;
      width: 6px;
    }

    &::-webkit-scrollbar-track {
      background: #fff;
    }

    &::-webkit-scrollbar-thumb {
      background: #dce0e5;
      border-radius: 6px;
    }

    &::-webkit-scrollbar-thumb:hover {
      background: #dce0e5;
      border-radius: 6px;
    }

    .category {
      list-style: none;
      margin: 0;
      padding: 0;
      .category-item {
        margin-bottom: 24px;
        display: flex;
        flex-direction: column;
        .category-title {
          display: flex;
          align-items: center;
          font-size: 15px;
          font-weight: 500;
          color: #3a3b3d;
          text-transform: capitalize;
        }
        &:last-child {
          margin-bottom: 0;
        }
      }
      .rect {
        display: block;
        width: 6px;
        height: 20px;
        border-radius: 2px;
        background: linear-gradient(#1e4984, #6bdada);
        margin-right: 6px;
      }
    }
  }
  .child-wrapper {
    list-style: none;
    margin: 8px 0 0 12px;
    padding: 0;
    .item-draggable {
      cursor: pointer;
    }
    .not-draggable {
      cursor: not-allowed;
      opacity: 0.4;
    }
    .offer-item {
      width: 292px;
      height: 50px;
      border-radius: 12px;
      padding: 8px;
      margin-bottom: 8px;
      border: 2px solid #fff;
      background: linear-gradient(#fcfdff, #ebeef5);
      box-shadow: 1px 1px 12px 0px #0000001f;
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: 8px;
      &:hover {
        // border-color: #eb7a3d;
        &:has(.type-a) {
          border-color: #9947d3;
        }
        &:has(.type-d) {
          border-color: #23b27f;
        }
      }
      .offer-type {
        width: 32px;
        height: 32px;
        border-radius: 8px;
        display: flex;
        justify-content: center;
        align-items: center;
        background: #ffffffa3;
        border: 1px solid #fff;
        font-size: 20px;
        font-weight: 800;
        box-shadow: 0px 2px 12px 0px #0000000f;
      }
      .type-p {
        color: #eb7a3d;
      }
      .type-a {
        color: #9947d3;
      }
      .type-d {
        color: #23b27f;
      }
      .offer-info {
        display: flex;
        flex-direction: column;
        flex: 1;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        .title {
          font-size: 11px;
          font-weight: 500;
          color: #3a3b3d;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }
        .code {
          font-size: 11px;
          font-weight: 400;
          color: #6b6d70;
        }
      }
      &:last-child {
        margin-bottom: 0;
      }
    }

    .component-item {
      width: 292px;
      height: 50px;
      border-radius: 14px;
      margin-bottom: 8px;
      background: #b2ddff;
      box-shadow: 1px 1px 12px 0px #0000001f;
      .item-wrapper {
        width: 289px;
        height: 50px;
        border-radius: 12px;
        padding: 8px;
        margin-left: 3px;
        border: 2px solid #fff;
        border-left: none;
        background: linear-gradient(#fcfdff, #ebeef5);
        display: flex;
        align-items: center;
        justify-content: space-between;
        gap: 8px;
        .component-info {
          display: flex;
          flex-direction: column;
          flex: 1;
          background: transparent;
          overflow: hidden;
          .title {
            font-size: 11px;
            font-weight: 500;
            color: #3a3b3d;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
          }
          .code {
            font-size: 11px;
            font-weight: 400;
            color: #6b6d70;
          }
        }
        &:last-child {
          margin-bottom: 0;
        }
      }
    }

    .type-ch {
      background: #fdced5;
    }

    .type-bn {
      background: #abefc6;
    }

    .type-pr {
      background: #f9dbaf;
    }

    .resource-item {
      width: 292px;
      height: 50px;
      border-radius: 32px;
      padding: 8px;
      margin-bottom: 8px;
      border: 1px solid #e6e9ed;
      background: #fff;
      box-shadow: -3px -4px 12px 0px #0000000a inset;
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: 8px;
      .resource-type {
        width: 32px;
        height: 32px;
        border-radius: 24px;
        display: flex;
        justify-content: center;
        align-items: center;
        background: #ffffffa3;
        border: 1px solid #fff;
        box-shadow: 0px 2px 12px 0px #00000014;
        font-size: 20px;
        font-weight: 800;
        .gradient-r {
          background-image: linear-gradient(#6d10a5, #fb6b8e);
          color: transparent;
          background-clip: text;
        }
        .gradient-b {
          background-image: linear-gradient(#8a20cb, #708fff);
          color: transparent;
          background-clip: text;
        }
        .gradient-s {
          background-image: linear-gradient(#6d10a5, #26f6dd);
          color: transparent;
          background-clip: text;
        }
        .gradient-q {
          background-image: linear-gradient(#9910a5, #ffd057);
          color: transparent;
          background-clip: text;
        }
      }
      .resource-info {
        display: flex;
        flex-direction: column;
        flex: 1;
        overflow: hidden;
        .title {
          font-size: 11px;
          font-weight: 500;
          color: #3a3b3d;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }
        .code {
          font-size: 11px;
          font-weight: 400;
          color: #6b6d70;
        }
      }
      &:last-child {
        margin-bottom: 0;
      }
    }

    .group-item {
      width: 292px;
      height: 50px;
      border-radius: 12px;
      padding: 8px;
      margin-bottom: 8px;
      border: 2px solid #fff;
      background: #f7f8fa;
      box-shadow: 1px 1px 12px 0px #0000001f;
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: 8px;
      .group-type {
        width: 32px;
        height: 32px;
        border-radius: 8px;
        display: flex;
        justify-content: center;
        align-items: center;
        background: #fff6e9;
        border: 1px solid #fff;
        font-size: 20px;
        font-weight: 800;
        color: #eb7a3d;
      }
      .group-info {
        display: flex;
        flex-direction: column;
        flex: 1;
        overflow: hidden;
        .title {
          font-size: 11px;
          font-weight: 500;
          color: #3a3b3d;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }
        .code {
          font-size: 11px;
          font-weight: 400;
          color: #6b6d70;
        }
      }
      &:last-child {
        margin-bottom: 0;
      }
    }

    .relation-item {
      width: 292px;
      height: 50px;
      border-radius: 12px;
      padding: 8px;
      margin-bottom: 8px;
      border: 2px solid #fff;
      background: #f7f8fa;
      box-shadow: 1px 1px 12px 0px #0000001f;
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: 8px;
      .relation-type {
        width: 32px;
        height: 32px;
        display: flex;
        justify-content: center;
        align-items: center;
        font-size: 20px;
        font-weight: 800;
      }
      .relation-info {
        display: flex;
        flex-direction: column;
        flex: 1;
        overflow: hidden;
        .title {
          font-size: 11px;
          font-weight: 500;
          color: #3a3b3d;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }
        .code {
          font-size: 11px;
          font-weight: 400;
          color: #6b6d70;
        }
      }
      &:last-child {
        margin-bottom: 0;
      }
    }
  }
}
:deep(.custom-delete) {
  fill: red;
}
</style>
