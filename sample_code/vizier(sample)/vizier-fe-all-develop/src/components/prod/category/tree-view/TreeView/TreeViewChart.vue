<template>
  <div v-if="localItemList" ref="treeViewRef" class="tree-view-group">
    <template
      v-for="(item, index) in localItemList"
      :key="index + item?.ctgrNodeUuid"
    >
      <div
        ref="treeListContainerRef"
        class="tree-list-container"
        :class="{ firstNodeLevel: isFirstNodeLevel }"
      >
        <div
          ref="el"
          class="tree-master-node-list"
          :class="{
            hasMediumSizeNode: nodeSize === 'medium',
            hasLargeSizeNode: nodeSize === 'large',
            'highlighted-parent-node': item.isChildActive,
          }"
        >
          <TreeViewNodeItem
            ref="treeNodeRef"
            :node-size="nodeSize"
            :index="index"
            :item="item"
            :branch-height="parentHeights[index]"
            :is-active="item.isActive"
            :is-new="item?.isNew"
            :disable="item?.useYn === YES_NO_VALUE.NO || setOpacity(item)"
            :show-count="
              !isEdit ||
              (isEdit &&
                item?.isLeafCategoryNode === YES_NO_VALUE.YES &&
                item?.totalOfferCount !== 0)
            "
            :editable="
              isEdit &&
              (item?.totalOfferCount === 0 ||
                item?.isLeafCategoryNode === YES_NO_VALUE.NO)
            "
            :class="{
              lastNode: index === localItemList.length - 1,
              firstNode: index === 0,
            }"
            :actions="listActions(item, index)"
            :is-showing-offers="onCatCheck(item)"
            @set-highlight-sibling="setHighlightSiblings"
            @set-toggle-child-node="setToggleChildNode(item, index)"
            @select-item-node="setSelectedTreeNode(item, index, $event)"
            @drop="drop($event, item)"
            @dragover="allowDrop($event, item)"
          />
        </div>
        <div v-if="item.children" class="child-nodes">
          <TreeViewChart
            v-show="item.showChilderen"
            ref="childElementRef"
            :key="categoryStore.getChildTreeViewStatus"
            v-model="item.children"
            :is-edit="isEdit"
            :node-size="nodeSize"
            :is-first-node-level="false"
            :is-expanded-all="isExpandedAll"
            :is-collapsed-all="isCollapsedAll"
            :filter-obj="filterObj"
            :last-clicked-catg-uuid="lastClickedCatgUuid"
            @select-node="setSelectedTreeNode"
            @set-highlighted-parent-node="setHighlightedParentNode"
          />
        </div>
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";
import { v4 as uuidv4 } from "uuid";
import SimplePlusIcon from "@/components/prod/icons/SimplePlusIcon.vue";
import TrashIcon from "@/components/prod/icons/TrashIcon.vue";
import useCategoryStore from "@/store/category.store";
import { YES_NO_VALUE } from "@/constants/index";
import { getUserInfor } from "@/constants/userInfor";
import { OFFER_TABS_VALUE } from "@/constants/offer";
import { useDragStore, useSnackbarStore } from "@/store";

const props = defineProps({
  tab: {
    type: String,
    default: "",
  },
  modelValue: {
    type: Array,
    default: null,
  },
  isFirstNodeLevel: {
    type: Boolean,
    default: true,
  },
  nodeSize: {
    type: String,
    default: "small",
  },
  filterObj: {
    type: Object,
    default: () => {},
  },
  isExpandedAll: {
    type: Boolean,
    default: false,
  },
  isCollapsedAll: {
    type: Boolean,
    default: false,
  },
  lastClickedCatgUuid: {
    type: String,
    default: "",
  },
  selectedCategoryItem: {
    type: Object,
    default: () => {},
  },
  isEdit: {
    type: Boolean,
    default: false,
  },
});

const categoryStore = useCategoryStore();
const userInfor = getUserInfor();
const useSnackbar = useSnackbarStore();
const { isDragging } = storeToRefs(useDragStore());
const { dragOfferUuid } = storeToRefs(categoryStore);
const { t } = useI18n();

const emit = defineEmits([
  "selectNode",
  "setHighlightedParentNode",
  "setToggleChildNode",
  "update:modelValue",
]);

const treeNodeRef = ref<any>(null);
const parentHeights = ref([]);
const refList = ref<any>([]);
const treeListContainerRef = ref([]);
const el = ref<any>(null);
const treeViewRef = ref(null);
const childElementRef = ref(null);
const showOfferItem = ref();
const localItemList = ref<any>([...props.modelValue]);
const dropData = ref<any>();
const isFinded = ref<boolean>(false);
const listFind = ref<any[]>([]);

onMounted(() => {
  if (categoryStore.getSearchCategoryFilterObj.searchText) {
    listFind.value = [];
    handleSearch(
      categoryStore.getSearchCategoryFilterObj.searchText,
      categoryStore.getSearchCategoryFilterObj.action,
      "ctgrNodeName"
    );
  }
  if (categoryStore.getSelectedCategoryTreeNode) {
    if (props.lastClickedCatgUuid) {
      handleSearch(
        categoryStore.getSelectedCategoryTreeNode.ctgrNodeUuid,
        categoryStore.getSearchCategoryFilterObj.action,
        "ctgrNodeUuid"
      );
    } else {
      handleSearch(
        categoryStore.getSelectedCategoryTreeNode.ctgrNodeUuid,
        categoryStore.getSearchCategoryFilterObj.action,
        "ctgrNodeUuid"
      );
    }
  }
  nextTick(() => {
    updateParentHeights();
    if (treeNodeRef?.value?.length) {
      refList.value = [...treeNodeRef.value];
    }
  });
});
const updateParentHeights = () => {
  if (el?.value?.length) {
    parentHeights.value = el.value.map((container) => container.clientHeight);
  }
};

const currentNodeLevel = computed(() => {
  return localItemList.value[0]?.level;
});

watch(
  () => localItemList.value,
  async () => {
    await nextTick();
    updateParentHeights();
  },
  { deep: true }
);

watch(
  () => props.nodeSize,
  async () => {
    await nextTick();
    updateParentHeights();
  }
);

watch(
  () => props.filterObj,
  (newValue: any) => {
    listFind.value = [];
    handleSearch(newValue.searchText, false, "ctgrNodeName");
  },
  { deep: true }
);

watch(
  [showOfferItem, () => categoryStore.getChildTreeViewStatus],
  ([newItem, newStatus], [oldItem]) => {
    if (
      newItem &&
      newStatus &&
      newItem?.ctgrNodeUuid !== oldItem?.ctgrNodeUuid
    ) {
      localItemList.value.forEach((item) => {
        if (item.ctgrNodeUuid === newItem.ctgrNodeUuid) {
          setSelectedTreeNode(newItem, null, null);
          categoryStore.setChildTreeViewStatus(false);
        }
      });
    }
  }
);

const handleSearch = (searchValue, action, property) => {
  if (searchValue) {
    resetHighlightedString();
    if (currentNodeLevel.value === "1") {
      localItemList.value = resetSelectedNodeStatus(localItemList.value);
    }
    searchTree(searchValue, action, property);
    if (property === "ctgrNodeName") {
      findFirstItem(listFind.value);
    }
  } else {
    resetNodeHighlight();
  }
};

const searchInTree = (
  item,
  parents = [] as any,
  _parentIndex = 0,
  searchValue,
  action,
  property
) => {
  if (item.children && item.children.length > 0) {
    if (property === "ctgrNodeName") {
      if (
        item.level === "1" &&
        item[property as string]
          .toUpperCase()
          .includes(searchValue.toUpperCase())
      ) {
        listFind.value.push(item);
      }
    }
    item.children.forEach((childItem, index) => {
      if (
        childItem[property as string]
          ?.toUpperCase()
          .includes(searchValue.toUpperCase())
      ) {
        if (!action) {
          item.showChilderen = true;
        }
        if (property === "ctgrNodeUuid" && childItem?.isNew) {
          emit("selectNode", childItem, null, null);
        }
        if (property === "ctgrNodeName") {
          listFind.value.push(childItem);
        }
        setHighlightedParentNode(childItem);
        if (parents && !action) {
          parents.forEach((parent) => (parent.showChilderen = true));
        }
      } else {
        if (property !== "ctgrNodeUuid" && !action) {
          childItem.showChilderen = false;
          if (childItem.children?.length) {
            childItem.children.forEach(
              (child) => (child.showChilderen = false)
            );
          }
        }
      }

      searchInTree(
        childItem,
        [...parents, item],
        index,
        searchValue,
        action,
        property
      );
    });
  }
  setHighlightedStyleForBranches();
};

const searchTree = (searchValue, action, property) => {
  localItemList.value.forEach((item, index) => {
    searchInTree(item, [], index, searchValue, action, property);
  });
};

const setHighlightedStyleForBranches = () => {
  nextTick(() => {
    el.value.forEach((elem) => {
      if (elem.classList.contains("highlighted-parent-node")) {
        let currentElement = elem.parentElement;
        while (currentElement.previousSibling) {
          if (currentElement.previousElementSibling) {
            currentElement.previousElementSibling.classList.add(
              "highlighted-string"
            );
          }
          currentElement = currentElement.previousSibling;
        }
      }
    });
  });
};

const resetNodeHighlight = () => {
  resetHighlightedString();
  localItemList.value = resetSelectedNodeStatus(localItemList.value);
};

const setHighlightSiblings = async (index, shouldRemoveHighlightedSibling) => {
  await nextTick();
  if (shouldRemoveHighlightedSibling) {
    resetHighlightedString();
  }
  let elem: any = treeListContainerRef.value[index as number];
  if (elem) {
    let sibling = elem.previousElementSibling;
    while (sibling) {
      sibling.classList.add("previous");
      sibling = sibling.previousElementSibling;
    }
  }
};

const removeHighLightSibling = () => {
  const previousElements = document.querySelectorAll(".previous");
  const hightlightElements = document.querySelectorAll(".highlighted-string");
  previousElements.forEach((element) => {
    element.classList.remove("previous");
  });
  hightlightElements.forEach((el) => el.classList.remove("highlighted-string"));
};

const setSelectedTreeNode = async (treeNode, index, isLeaf) => {
  categoryStore.setCurrentTab(OFFER_TABS_VALUE.GENERAL);

  if (currentNodeLevel.value === "1") {
    localItemList.value = resetSelectedNodeStatus(localItemList.value);
  }
  emit("selectNode", treeNode, null, isLeaf);
  categoryStore.setPropertiesForTree(treeNode, "isActive", true);
  localItemList.value = updateActiveStatus(localItemList.value);
  if (currentNodeLevel.value === "1") {
    localItemList.value = localItemList.value.map((item) => {
      isFinded.value = false;
      findNodeActive(item.children, treeNode);
      return {
        ...item,
        isChildActive: isFinded.value,
        isActive: item.ctgrNodeUuid === treeNode.ctgrNodeUuid,
      };
    });
  }
  categoryStore.setSearchCategoryFilterObj("");
  removeHighLightSibling();
  setHighlightedNode(treeNode, "select");
  setHighlightedStyleForBranches();
  setHighlightSiblings(index, false);
  if (isLeaf === YES_NO_VALUE.NO) {
    resetLeafNode();
    categoryStore.setShowCategoryDetail(true);
  }
};

const resetHighlightedString = () => {
  treeListContainerRef.value.forEach((elem: any) => {
    if (elem.classList.contains("highlighted-string")) {
      elem.classList.remove("highlighted-string");
    }
    if (elem.classList.contains("previous")) {
      elem.classList.remove("previous");
    }
  });
};
const setHighlightedNode = (treeNode, activeType = "select") => {
  if (!treeNode) {
    return;
  }
  treeNode.nodeActiveType = activeType;
  treeNode.isActive = true;
};

const setHighlightedParentNode = (treeNode) => {
  emit("setHighlightedParentNode", treeNode);
  setHighlightedNode(treeNode, "search");
  localItemList.value = updateActiveStatus(localItemList.value);
};

const updateActiveStatus = (items) => {
  return items.map((item) => {
    if (item.children) {
      item.children = updateActiveStatus(item.children);
    }
    if (!item.isActive) {
      item.isChildActive =
        item.children &&
        item.children.some((child) => child.isActive || child.isChildActive);
    }
    return item;
  });
};

const setToggleChildNode = (item, _index) => {
  item.showChilderen = !item.showChilderen;
  item.children = hideChildrens(item.children, false);
  categoryStore.setPropertiesForTree(item, "showChilderen", item.showChilderen);
};

const resetSelectedNodeStatus = (valueList) => {
  return valueList.map((item) => {
    item.isActive = false;
    item.isChildActive = false;
    if (item.children) {
      item.children = resetSelectedNodeStatus(item.children);
    }
    return item;
  });
};

const onCatCheck = (item) => {
  const isShowOffer = props.lastClickedCatgUuid === item.ctgrNodeUuid;
  if (isShowOffer) {
    showOfferItem.value = item;
  }
  return isShowOffer;
};

const listActions = (item, _nodeIndex) => {
  const actions: any[] = [];
  const removeAction = {
    name: t("product_platform.removeCurrentNode"),
    icon: TrashIcon,
    iconProps: {
      class: "text-text-lighter",
    },
    onClick: () => {
      removedNode(item);
    },
  };
  const addNewAction = {
    name: t("product_platform.addChildNode"),
    icon: SimplePlusIcon,
    iconProps: {
      class: "text-text-lighter",
    },
    onClick: () => {
      handleAddNew(item);
    },
  };
  if (item?.totalOfferCount === 0) {
    if (item?.level !== "5") {
      actions.push(addNewAction);
    }
    actions.push(removeAction);
  } else {
    if (item?.level !== "5") {
      actions.push(addNewAction);
    }
  }
  return actions;
};

const handleAddNew = (item) => {
  if (item?.isNew && !item.ctgrNodeName) {
    useSnackbar.showSnackbar(t("product_platform.addNodeFailMsg"), "error");
    return;
  }
  const newObj: any = {
    ctgrNodeUuid: uuidv4(),
    ctgrNodeName: "",
    hpstCtgrNodeUuid: item.ctgrNodeUuid,
    isLeafCategoryNode: YES_NO_VALUE.YES,
    totalOfferCount: 0,
    level: `${parseInt(item.level) + 1}`,
    ctgrTabUuid: null,
    showChilderen: true,
    children: null,
    tclsCtgrYn: null,
    chgDeptName: userInfor.chgDeptName,
    chgUser: userInfor.chgUser,
    ctgrOvwCntn: null,
    isActive: false,
    isChildActive: false,
    useYn: YES_NO_VALUE.YES,
    isNew: true,
    offerRel: null,
    isAddChild: !item.isNew,
  };
  addNewNode(item, newObj);
};

const drop = (event: any, item: any) => {
  event.preventDefault();
  dropData.value = event.dataTransfer.getData("item")
    ? JSON.parse(event.dataTransfer.getData("item"))
    : null;
  if (dropData.value) {
    if (item.offerRel?.length) {
      let oldNodeOfferOwnerIndex = item.offerRel.findIndex(
        (offr) => offr?.ctgrNodeUuid === dropData.value?.oldCtgrNodeUuid
      );
      if (oldNodeOfferOwnerIndex !== -1) {
        moveNode(item, dropData.value, true);
        filterProductList(dropData.value);
        return;
      }
      let index = item.offerRel.findIndex(
        (offer) => offer?.prodUuid === dropData.value?.prodUuid
      );
      if (index !== -1) {
        useSnackbar.showSnackbar("Duplicate Offer", "error");
        return;
      }
    }
    moveNode(item, dropData.value);
    filterProductList(dropData.value);
  }
};

const allowDrop = (event: any, node: any) => {
  if (
    !props.isEdit ||
    (isDragging.value &&
      (node?.isLeafCategoryNode === YES_NO_VALUE.NO ||
        node?.ctgrNodeUuid === dragOfferUuid.value))
  ) {
    return true;
  } else {
    event?.preventDefault();
    return false;
  }
};

const setOpacity = (node: any) => {
  return (
    isDragging.value &&
    (node?.isLeafCategoryNode === YES_NO_VALUE.NO ||
      node?.ctgrNodeUuid === dragOfferUuid.value ||
      !props.isEdit)
  );
};

const findNodeActive = (rootArr, activeNode) => {
  if (!isFinded.value && rootArr?.length) {
    rootArr.forEach((item) => {
      if (item?.ctgrNodeUuid === activeNode?.ctgrNodeUuid) {
        isFinded.value = true;
      }
      findNodeActive(item.children, activeNode);
    });
  }
  return;
};

const filterProductList = (dropItem) => {
  if (categoryStore.getProductData?.length) {
    categoryStore.setProductData(
      categoryStore.getProductData.filter(
        (item) => item?.prodUuid !== dropItem?.prodUuid
      )
    );
  }
};

const hideChildrens = inject<any>("handleHideChild");
const addNewNode = inject<any>("addNewNode");
const removedNode = inject<any>("removedNode");
const moveNode = inject<any>("movedNode");
const resetLeafNode = inject<any>("handleResetOfferList");
const findFirstItem = inject<any>("findFirstItem");

defineExpose({
  resetNodeHighlight,
  setHighlightSiblings,
  removeHighLightSibling,
});
</script>

<style scoped>
.tree-master-node-list {
  width: 207.5px;
}
.hasMediumSizeNode > .tree-node {
  height: 84px;
}

.lastNode::after {
  display: none;
}

.lastNodeWithBranch,
.lastNodeWithBranch {
  position: relative;
}

.firstNodeLevel > .tree-master-node-list > .tree-node::after {
  display: none;
  background-color: red;
}

.firstNodeLevel > .tree-master-node-list > .tree-node::before {
  display: none;
}

.hasLargeSizeNode > .tree-node {
  height: 116px;
}

.highlighted-parent-node .tree-node:not(.firstNode)::before,
.highlighted-parent-node .tree-node.firstNode::before {
  border: 3px solid #d9325a;
  border-right: none;
  border-top: none;
  background: transparent;
  z-index: 1;
}

.highlighted-parent-node .tree-node.firstNode::before {
  border-left: none;
}

.previous > .tree-master-node-list > .tree-node:not(.firstNode)::after {
  border-left: 3px solid #d9325a;
  z-index: 1;
}

.previous > .tree-master-node-list > .tree-node.firstNode::after {
  border-right: 3px solid #d9325a;
  border-top: 3px solid #d9325a;
  background: transparent;
  z-index: 1;
}

.highlighted-string
  > .tree-master-node-list
  > .tree-node:not(.firstNode)::after {
  border-left: 3px solid #d9325a;
}

.highlighted-string > .tree-master-node-list > .tree-node.firstNode::after {
  border-right: 3px solid #d9325a;
  border-top: 3px solid #d9325a;
  background: transparent;
  z-index: 1;
}

.tree-view-group {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.tree-item {
  list-style: none;
}
.tree-list-container {
  display: flex;
  gap: 56px;
}

.child-item-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}
</style>
