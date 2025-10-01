<template>
  <div class="category-tree-wrapper">
    <div class="h-full">
      <div class="category-content-container">
        <div class="category-left-content bg-base rounded-lg">
          <LocomotiveComponent
            scroll-content-class="pb-[8px]"
            scroll-container-class="!px-[0px]"
            :scroll-top-content-class="scrollTopContentClass"
          >
            <template #top-content-fixed>
              <TreeListLevelDescriptionSection
                :level-description="levelDescription"
              />
            </template>
            <NoData v-if="!isShowTreeList" />
            <div v-else class="tree-view-main-content">
              <div>
                <div class="offer-tree-chart-area">
                  <TreeViewChart
                    v-show="isShowTreeList"
                    v-if="treeList?.length"
                    ref="treeViewElement"
                    v-model="treeList"
                    :tab="tab"
                    :is-edit="isEdit"
                    :node-size="nodeSize"
                    :is-expanded-all="isExpandedAllTreeNode"
                    :is-collapsed-all="isCollapsedAllTreeNode"
                    :filter-obj="searchCategoryFilterObj"
                    :last-clicked-catg-uuid="lastClickedCatgUuid"
                    :selected-category-item="selectedNodeItem"
                    @select-node="selectTreeNode"
                  />
                </div>
              </div>
            </div>
          </LocomotiveComponent>

          <TreeViewActionSection
            :node-size="nodeSize"
            @set-collapse-all="setCollapseAllNode"
            @set-expand-all="setExpandAllNode"
            @set-node-size="setNodeSize"
          />
        </div>
      </div>
    </div>
  </div>
  <base-popup
    v-model="openPopup"
    :icon="dialogIconType.Warning"
    :submit-button-text="$t('product_platform.btn_yes')"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="$t('product_platform.updatingConfirmSaved')"
    @on-close="closePopup"
    @on-submit="handleConfirm"
  />
</template>

<!-- eslint-disable security/detect-object-injection -->
<script>
import { useI18n } from "vue-i18n";
import cloneDeep from "lodash-es/cloneDeep";
import TreeViewChart from "./TreeView/TreeViewChart.vue";
import TreeViewActionSection from "./TreeView/TreeViewActionSection.vue";
import useCategoryStore from "@/store/category.store";
import TreeListLevelDescriptionSection from "./TreeView/TreeListLevelDescriptionSection.vue";
import { CATEGORY_VIEW_MODE, YES_NO_VALUE } from "@/constants/";
import { useSnackbarStore } from "@/store";
import { DialogIconType } from "@/enums";
import { CATEGORY_EDIT_ACTIONS } from "@/constants/category";
import { isEmpty } from "lodash-es";

export default {
  name: "CategoryTreeComponent",
  components: {
    TreeViewChart,
    TreeViewActionSection,
    TreeListLevelDescriptionSection,
  },
  inject: ["handleResetOfferList", "handleFetchOfferList"],
  provide() {
    return {
      handleHideChild: this.setShowChildren,
      addNewNode: this.handleAddNewNode,
      removedNode: this.handleRemoveNode,
      movedNode: this.handleMoveOfferFromNode,
      findFirstItem: this.handleFindFirstItem,
      validActionNode: this.validationUpdateList,
    };
  },
  props: {
    tab: {
      type: String,
      required: true,
    },
    lastClickedCatgUuid: {
      type: String,
      default: "",
    },
    isEdit: {
      type: Boolean,
      default: false,
    },
    selectedNodeItem: {
      type: Object,
      default: () => {},
    },
    searchFormHeight: {
      type: Number,
      default: 68,
    },
  },
  setup() {
    const categoryStore = useCategoryStore();
    const useSnackbar = useSnackbarStore();
    const dialogIconType = DialogIconType;
    const { t } = useI18n();
    return { categoryStore, useSnackbar, t, dialogIconType };
  },
  data: () => {
    return {
      nodeSize: "small",
      isExpandedAllTreeNode: false,
      isCollapsedAllTreeNode: false,
      dialog: false,
      isShowTreeList: true,
      selectedTreeNodeLeaf: null,
      selectedTreeNode: null,
      selectedTreeNodeOld: null,
      searchCategoryFilterObj: {
        searchText: "",
        searchField: "ctgrNodeName",
        action: false,
      },
      searchOfferFilterObj: {
        searchText: "",
        searchField: "name",
      },
      treeList: [],
      levelDescription: [],
      listUpdate: [],
      listOfferUpdateNotYetSave: [],
      validStatus: true,
      action: null,
      openPopup: false,
      findItem: null,
      addedNodeClone: null,
      newNodeClone: null,
      removeNodeClone: null,
    };
  },
  computed: {
    scrollTopContentClass() {
      return !this.isShowTreeList
        ? "w-full h-full flex justify-center items-center flex-wrap"
        : "";
    },
    offsetHeight() {
      return `100% - ${this.searchFormHeight}px`;
    },
  },
  watch: {
    findItem: {
      handler: async function (val) {
        if (val) {
          this.categoryStore.setSelectedCategoryTreeNode(val);
          await this.categoryStore.getCategoryOfferListTree({
            ctgrNodeUuid: val?.ctgrNodeUuid,
            size: 7,
          });
          this.categoryStore.setShowCategoryDetail(true);
        }
      },
    },
    "categoryStore.getSearchStatus": {
      handler: function (val) {
        if (
          val &&
          this.tab === this.categoryStore.getCategoryCurrentTab &&
          this.categoryStore.getCategoryView === CATEGORY_VIEW_MODE.TREE
        ) {
          this.isShowTreeList = true;
          this.categoryStore.setSearchCategoryFilterObjAction(false);
          this.searchCategoryFilterObj = {
            ...this.categoryStore.getSearchCategoryFilterObj,
          };
          this.categoryStore.setSelectedCategoryTreeNode(null);
          this.$nextTick().then(() => {
            this.categoryStore.setSearchStatus(false);
          });
        }
      },
    },
    "categoryStore.getIsFetchData": {
      handler: async function (newVal) {
        if (newVal) {
          if (this.treeList?.length) {
            const newData = this.categoryStore.getTreeData.map((newItem) =>
              this.getOldProperty(newItem, this.treeList)
            );
            this.treeList = [];
            await nextTick().then(() => {
              this.treeList = newData;
            });
          } else {
            this.treeList = this.categoryStore.getTreeData;
          }
          this.categoryStore.setIsFetchData(false);
        }
      },
    },
    "categoryStore.getSaveStatus": {
      handler: async function (newVal) {
        if (newVal) {
          this.listUpdate = [];
          this.validStatus = true;
          this.validationUpdateList(this.treeList);
          if (this.validStatus) {
            this.collectUpdateList(this.treeList);
            try {
              const res = await this.categoryStore.updateCategory(
                this.listUpdate
              );
              if (res.status === 200) {
                await this.fetchLevelDescription();
                this.categoryStore.setIsEdit(false);
              }
            } catch (error) {
              console.error(error);
            }
          } else {
            this.useSnackbar.showSnackbar(
              this.t("product_platform.addNodeFailMsg"),
              "error"
            );
          }
          this.categoryStore.setSaveStatus(false);
        }
      },
    },
    "categoryStore.getEditSearch": {
      handler: function (newVal) {
        if (newVal) {
          if (this.treeList?.length) {
            this.handleRemoveNodeNoValid(
              this.categoryStore.getSelectedCategoryTreeNode
            );
          }
          this.categoryStore.setEditSearch(false);
        }
      },
    },
  },
  created() {
    if (!this.categoryStore.getIsShowTreeData) {
      this.isShowTreeList = this.categoryStore.getIsShowTreeData;
    } else {
      if (this.categoryStore.getTreeData.length) {
        this.treeList = cloneDeep(this.categoryStore.getTreeData);
      } else {
        this.fetchTreeViewData();
      }
      this.fetchLevelDescription();
    }
    this.nodeSize = this.categoryStore.getNodeSize;
  },
  methods: {
    async selectTreeNode(treeNode, index, isLeaf) {
      if (!treeNode) {
        this.$refs.treeViewElement.resetNodeHighlight();
        this.$refs.treeViewElement.removeHighLightSibling();
      } else {
        if (
          this.categoryStore.getIsEdit &&
          this.categoryStore.getSelectedCategoryTreeNode &&
          treeNode.ctgrNodeUuid !==
            this.categoryStore.getSelectedCategoryTreeNode.ctgrNodeUuid &&
          !this.categoryStore.getSelectedCategoryTreeNode.ctgrNodeName
        ) {
          this.selectedTreeNode = treeNode;
          this.selectedTreeNodeOld = cloneDeep(
            this.categoryStore.getSelectedCategoryTreeNode
          );
          this.action = CATEGORY_EDIT_ACTIONS.CHANGE_NODE;
          this.openPopup = true;
        } else {
          this.selectedTreeNode = treeNode;
          this.categoryStore.setSelectedCategoryTreeNode(treeNode);
          if (isLeaf === YES_NO_VALUE.YES) {
            this.selectedTreeNodeLeaf = treeNode;
            if (!treeNode?.isNew) {
              this.categoryStore.setOpenOfferPanel(true);
            }
          } else {
            this.categoryStore.setShowCategoryDetail(true);
            if (!this.isEdit) {
              await this.categoryStore.getCategoryOfferListTree({
                ctgrNodeUuid: treeNode?.ctgrNodeUuid,
                size: 7,
              });
            }
          }
        }
      }
    },
    setCollapseAllNode() {
      this.validStatus = true;
      this.validationUpdateList(this.treeList);
      if (!this.validStatus) {
        this.action = CATEGORY_EDIT_ACTIONS.COLLAPSE;
        this.openPopup = true;
      } else {
        const cloneTreeList = [...this.treeList];
        this.treeList = [];
        this.$nextTick().then(() => {
          this.categoryStore.setSearchCategoryFilterObjAction(true);
          this.treeList = cloneDeep(this.setItemCollapse(cloneTreeList));
          this.categoryStore.setTreeData(this.treeList);
        });
      }
    },
    setExpandAllNode() {
      const cloneTreeList = [...this.treeList];
      this.treeList = [];
      this.$nextTick().then(() => {
        this.categoryStore.setSearchCategoryFilterObjAction(true);
        this.treeList = cloneDeep(this.setShowChildren(cloneTreeList, true));
        this.categoryStore.setTreeData(this.treeList);
      });
    },
    setNodeSize(nodeSize) {
      this.nodeSize = nodeSize;
      this.categoryStore.setNodeSize(nodeSize);
    },
    setShowChildren(items, value) {
      if (!items) {
        return [];
      }
      return items.map((item) => {
        item.showChilderen = value;
        if (item.children) {
          if (item.level === "1") {
            item.showChilderen = true;
          }
          this.setShowChildren(item.children, value);
        }
        return item;
      });
    },
    setItemCollapse(items) {
      if (!items) {
        return [];
      }
      return items.map((item) => {
        item.showChilderen = false;
        item.isChildActive = false;
        item.isActive = false;
        if (item.children) {
          if (item.level === "1") {
            item.showChilderen = true;
          }
          this.setItemCollapse(item.children);
        }
        return item;
      });
    },
    addIsActiveProperty(items) {
      return items.map((item) => {
        item.isActive = false;
        item.isChildActive = false;
        if (item.children) {
          item.children = this.addIsActiveProperty(item.children);
        }
        return item;
      });
    },
    getOldProperty(mergeObj, oldList) {
      if (oldList?.length) {
        const index = oldList.findIndex(
          (oldItem) => oldItem.ctgrNodeUuid === mergeObj.ctgrNodeUuid
        );
        if (index !== -1) {
          return {
            ...oldList[index],
            ...mergeObj,
            isNew: false,
            isChildActive: false,
            isActive: false,
            showChilderen: oldList[index].showChilderen,
            offerRel: null,
            children: mergeObj?.children?.length
              ? mergeObj.children.map((child) =>
                  this.getOldProperty(child, oldList[index].children)
                )
              : null,
          };
        } else {
          return mergeObj;
        }
      }
      return mergeObj;
    },
    async fetchTreeViewData() {
      await this.categoryStore.getTreeCategory();
      this.treeList = cloneDeep(
        this.addIsActiveProperty(this.categoryStore.getTreeData)
      );
    },
    async fetchLevelDescription() {
      this.levelDescription = [];
      try {
        if (isEmpty(this.categoryStore.getDescriptionData)) {
          await this.categoryStore.getLevelDescription();
        }
        for (const lvItem in this.categoryStore.getDescriptionData) {
          const content =
            this.categoryStore.getDescriptionData[lvItem].split("\r\n");
          let contentObj = {};
          if (content.length > 1) {
            contentObj = { primaryText: content[0], subText: content[1] };
          } else {
            contentObj = { primaryText: content[0] };
          }
          this.levelDescription.push(contentObj);
        }
      } catch (error) {
        this.useSnackbar.showSnackbar(error?.errorMsg, "error");
      }
    },
    findNodeAdded(rootArray, addedItem, newItem) {
      return rootArray.map((item) => {
        if (item?.ctgrNodeUuid === addedItem?.ctgrNodeUuid) {
          if (item?.children?.length) {
            return {
              ...item,
              children: [...item.children, newItem],
              showChilderen: true,
              isLeafCategoryNode: YES_NO_VALUE.NO,
            };
          } else {
            return {
              ...item,
              children: [newItem],
              showChilderen: true,
              isLeafCategoryNode: YES_NO_VALUE.NO,
            };
          }
        } else {
          return {
            ...item,
            children: item.children
              ? this.findNodeAdded(item.children, addedItem, newItem)
              : null,
          };
        }
      });
    },
    setDisbleChild(rootArray) {
      if (rootArray?.length) {
        return rootArray.map((item) => ({
          ...item,
          useYn: YES_NO_VALUE.NO,
          children: this.setDisbleChild(item.children),
        }));
      }
      return null;
    },
    findNodeRemoved(rootArray, removedNode) {
      if (!rootArray?.length) {
        return [];
      }
      const removeIndex = rootArray?.findIndex(
        (el) => el?.ctgrNodeUuid === removedNode?.ctgrNodeUuid
      );
      if (removeIndex !== -1) {
        if (removedNode?.isNew) {
          this.categoryStore.setSelectedCategoryTreeNode(null);
          return rootArray.filter((item, index) => index !== removeIndex);
        } else {
          this.categoryStore.setSelectedCategoryTreeNode(removedNode);

          return rootArray.map((item, index) => {
            if (index === removeIndex) {
              return {
                ...item,
                isActive: false,
                isChildActive: false,
                children: this.setDisbleChild(item.children),
                useYn: YES_NO_VALUE.NO,
                isUpdate: true,
              };
            }
            return item;
          });
        }
      }
      return rootArray.map((child) => {
        if (child?.children?.length) {
          return {
            ...child,
            isActive: false,
            isChildActive: false,
            children: this.findNodeRemoved(child.children, removedNode),
          };
        } else return child;
      });
    },
    findNodeChangeCount(
      rootArray,
      fromNodeUuid,
      toNode,
      offer,
      comeback = false
    ) {
      if (!rootArray?.length) {
        return [];
      }
      const fromIndex = rootArray?.findIndex(
        (el) => el?.ctgrNodeUuid === fromNodeUuid
      );
      const toIndex = rootArray?.findIndex(
        (el) => el?.ctgrNodeUuid === toNode?.ctgrNodeUuid
      );
      if (fromIndex !== -1 || toIndex !== -1) {
        return rootArray.map((item, index) => {
          if (index === fromIndex) {
            if (offer?.isMoved) {
              return {
                ...item,
                totalOfferCount: item.totalOfferCount - 1,
                offerRel: item.offerRel.filter(
                  (offr) =>
                    offr.objUuid !== offer.objUuid ||
                    offr.objUuid !== offer.prodUuid
                ),
              };
            }
            return {
              ...item,
              totalOfferCount: item.totalOfferCount - 1,
              offerRel: item?.offerRel
                ? [
                    ...item.offerRel,
                    {
                      ...offer,
                      objUuid: offer.prodUuid,
                    },
                  ]
                : [
                    {
                      ...offer,
                      objUuid: offer.prodUuid,
                    },
                  ],
            };
          } else if (index === toIndex) {
            if (comeback) {
              return {
                ...item,
                totalOfferCount: item.totalOfferCount + 1,
                isUpdate: true,
                offerRel: item.offerRel.filter(
                  (offr) =>
                    offr.objUuid !== offer.objUuid ||
                    offr.objUuid !== offer.prodUuid
                ),
              };
            }
            return {
              ...item,
              totalOfferCount: item.totalOfferCount + 1,
              isUpdate: true,
              offerRel: item?.offerRel
                ? [
                    ...item.offerRel,
                    {
                      ...offer,
                      objUuid: offer.prodUuid,
                      oldCtgrNodeUuid: offer.isMoved
                        ? offer.oldCtgrNodeUuid
                        : fromNodeUuid,
                      ctgrNodeUuid: item.ctgrNodeUuid,
                      isMoved: true,
                    },
                  ]
                : [
                    {
                      ...offer,
                      objUuid: offer.prodUuid,
                      oldCtgrNodeUuid: offer.isMoved
                        ? offer.oldCtgrNodeUuid
                        : fromNodeUuid,
                      ctgrNodeUuid: item.ctgrNodeUuid,
                      isMoved: true,
                    },
                  ],
            };
          }
          return item;
        });
      }
      return rootArray.map((child) => {
        if (child?.children?.length) {
          return {
            ...child,
            children: this.findNodeChangeCount(
              child.children,
              fromNodeUuid,
              toNode,
              offer,
              comeback
            ),
          };
        } else return child;
      });
    },
    handleAddNewNode(addedItem, newItem) {
      this.validStatus = true;
      this.validationUpdateList(this.treeList);
      if (!this.validStatus) {
        this.addedNodeClone = addedItem;
        this.newNodeClone = newItem;
        this.action = CATEGORY_EDIT_ACTIONS.ADD;
        this.openPopup = true;
        return;
      } else {
        const clone = cloneDeep(this.treeList);
        this.treeList = [];
        this.$nextTick().then(() => {
          this.treeList = cloneDeep(
            this.findNodeAdded(clone, addedItem, newItem)
          );
          this.categoryStore.setSelectedCategoryTreeNode(newItem);
          this.categoryStore.setShowCategoryDetail(true);
          this.categoryStore.setTreeData(this.treeList);

          this.handleResetOfferList();
        });
      }
    },
    handleRemoveNode(node) {
      this.validStatus = true;
      this.validationUpdateList(this.treeList);
      if (
        !this.validStatus &&
        node?.ctgrNodeUuid !==
          this.categoryStore.getSelectedCategoryTreeNode?.ctgrNodeUuid
      ) {
        this.removeNodeClone = node;
        this.action = CATEGORY_EDIT_ACTIONS.REMOVE;
        this.openPopup = true;
      } else {
        const clone = cloneDeep(this.treeList);
        this.treeList = [];
        this.$nextTick().then(() => {
          this.treeList = cloneDeep(this.findNodeRemoved(clone, node));
          this.categoryStore.setTreeData(this.treeList);
          this.categoryStore.setSearchProductStatus(true);
        });
      }
    },
    handleRemoveNodeNoValid(node) {
      this.categoryStore.setTreeData(this.treeList);
      this.treeList = [];
      this.$nextTick().then(() => {
        this.treeList = cloneDeep(
          this.findNodeRemoved(this.categoryStore.getTreeData, node)
        );
        this.categoryStore.setTreeData(this.treeList);
      });
    },
    handleRemoveThenAddNode(removeNode, addedNode, newNode) {
      const treeListClone = cloneDeep(
        this.findNodeRemoved(this.treeList, removeNode)
      );
      this.treeList = [];
      this.$nextTick().then(() => {
        this.treeList = this.findNodeAdded(treeListClone, addedNode, newNode);
        this.categoryStore.setSelectedCategoryTreeNode(newNode);
        this.categoryStore.setTreeData(this.treeList);
      });
    },
    handleRemoveThenRemoveNode(removeNodeNew, removeNodeOld) {
      const treeListClone = cloneDeep(
        this.findNodeRemoved(this.treeList, removeNodeNew)
      );
      this.treeList = [];
      this.$nextTick().then(() => {
        this.treeList = cloneDeep(
          this.findNodeRemoved(treeListClone, removeNodeOld)
        );
        this.categoryStore.setSearchProductStatus(true);
        this.categoryStore.setTreeData(this.treeList);
      });
    },
    handleMoveOfferFromNode(node, offer, comeback = false) {
      this.validStatus = true;
      if (!node || !offer) {
        return;
      }
      const nodeUuid = offer?.ctgrNodeUuid;
      this.categoryStore.setTreeData(this.treeList);
      this.treeList = [];
      this.$nextTick().then(() => {
        this.treeList = cloneDeep(
          this.findNodeChangeCount(
            this.categoryStore.getTreeData,
            nodeUuid,
            node,
            offer,
            comeback
          )
        );
        this.collectOfferUpdateNotSave(this.treeList);
        this.categoryStore.setListOfferUpdateNotSave(
          this.listOfferUpdateNotYetSave
        );
        this.listOfferUpdateNotYetSave = [];
      });
    },
    collectUpdateList(rootArray) {
      if (rootArray?.length) {
        rootArray.forEach((catg) => {
          if (catg?.isUpdate || catg?.isAddChild) {
            if (!catg?.ctgrNodeName) {
              useSnackbar.showSnackbar(
                t("product_platform.addNodeFailMsg"),
                "error"
              );
            }
            this.listUpdate.push({
              ...catg,
              offerRel: catg?.offerRel?.length ? [...catg.offerRel] : null,
              children: catg?.children?.length
                ? catg.children.filter((item) => item.isNew)
                : null,
            });
          }
          this.collectUpdateList(catg.children);
        });
      }
      return;
    },
    collectOfferUpdateNotSave(rootArray) {
      if (rootArray?.length) {
        rootArray.forEach((catg) => {
          if (catg?.isUpdate && catg?.offerRel?.length) {
            catg?.offerRel.forEach((offr) => {
              if (offr?.isMoved) {
                this.listOfferUpdateNotYetSave.push(offr);
              }
            });
          }
          this.collectOfferUpdateNotSave(catg.children);
        });
      }
      return;
    },
    validationUpdateList(rootArray) {
      if (rootArray?.length) {
        rootArray.forEach((catg) => {
          if (!catg?.ctgrNodeName && catg?.isNew) {
            this.validStatus = false;
            return;
          }
          this.validationUpdateList(catg.children);
        });
      }
    },
    async handleConfirm() {
      switch (this.action) {
        case CATEGORY_EDIT_ACTIONS.ADD:
          this.handleRemoveThenAddNode(
            this.categoryStore.getSelectedCategoryTreeNode,
            this.addedNodeClone,
            this.newNodeClone
          );
          this.openPopup = false;
          this.addedNodeClone = null;
          this.newNodeClone = null;
          break;
        case CATEGORY_EDIT_ACTIONS.REMOVE:
          this.handleRemoveThenRemoveNode(
            this.categoryStore.getSelectedCategoryTreeNode,
            this.removeNodeClone
          );
          this.openPopup = false;
          this.removeNodeClone = null;
          break;
        case CATEGORY_EDIT_ACTIONS.CHANGE_NODE:
          this.treeList = [];
          if (
            this.selectedTreeNode?.isLeafCategoryNode === YES_NO_VALUE.YES &&
            this.lastClickedCatgUuid
          ) {
            await this.categoryStore.getCategoryOfferListTree({
              ctgrNodeUuid: this.selectedTreeNode?.ctgrNodeUuid,
              size: 7,
            });
            this.categoryStore.setOpenOfferPanel(true);
          } else {
            this.categoryStore.setShowCategoryDetail(true);
          }
          this.$nextTick().then(() => {
            this.treeList = cloneDeep(
              this.findNodeRemoved(
                this.categoryStore.getTreeData,
                this.selectedTreeNodeOld
              )
            );
            this.categoryStore.setTreeData(this.treeList);
            this.categoryStore.setSelectedCategoryTreeNode(
              this.selectedTreeNode
            );
          });
          this.openPopup = false;
          break;
        case CATEGORY_EDIT_ACTIONS.COLLAPSE:
          this.treeList = [];
          this.categoryStore.setSelectedCategoryTreeNode(null);
          this.categoryStore.setOpenOfferPanel(true);
          this.$nextTick().then(() => {
            this.treeList = cloneDeep(
              this.setItemCollapse(this.categoryStore.getTreeData)
            );
          });
          this.openPopup = false;
          break;
      }
    },
    closePopup() {
      switch (this.action) {
        case CATEGORY_EDIT_ACTIONS.ADD:
        case CATEGORY_EDIT_ACTIONS.REMOVE:
        case CATEGORY_EDIT_ACTIONS.COLLAPSE:
          this.openPopup = false;
          break;
        case CATEGORY_EDIT_ACTIONS.CHANGE_NODE:
          const clone = cloneDeep(this.treeList);
          this.treeList = [];
          this.categoryStore.setSelectedCategoryTreeNode(
            this.selectedTreeNodeOld
          );
          this.categoryStore.setShowCategoryDetail(true);
          this.handleResetOfferList();
          this.$nextTick().then(() => {
            this.treeList = cloneDeep(clone);
          });
          this.openPopup = false;
          break;
      }
    },
    async handleFindFirstItem(findedItem) {
      const min = Math.min(...findedItem.map((item) => parseInt(item.level)));
      if (min === Infinity) {
        this.findItem = undefined;
        return;
      }
      this.findItem = findedItem.find((item) => item.level === `${min}`);
    },
  },
};
</script>

<style scoped>
.category-tree-wrapper {
  height: calc(v-bind(offsetHeight));
  padding: 0px 12px 12px;
  background-color: #fff;
}
.card-title {
  display: flex;
  justify-content: center;
  align-items: center;
}

.card-title > p {
  font-weight: 500;
  font-size: 18px;
  line-height: 27px;
  letter-spacing: 0.15px;
  font-family: "Noto Sans KR", sans-serif;
}

.card-letter {
  width: 52px;
  height: 44px;
  background-color: #f0f2f5;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 0 0 12px 0;
}

.card-letter > p {
  color: #e04f16;
}

:deep() .v-card-item {
  padding: 0;
}

.card-spacing-content {
  background-color: #f0f2f5;
  height: 8px;
  width: 100%;
}

:deep() .v-card-text {
  padding: 0;
  width: 100%;
  height: auto;
  display: flex;
  flex-direction: column;
}

.card-text-row {
  display: flex;
  gap: 8px;
  padding: 8px 16px;
  width: 100%;
  height: 37px;
}

.card-label,
.card-text {
  width: 142.5px;
  display: flex;
}

.card-label,
.card-text > p {
  font-weight: 500;
  font-size: 14px;
  line-height: 21px;
  letter-spacing: 0.1px;
  font-family: "Noto Sans KR", sans-serif;
}

.card-label > p {
  color: #6b6d70;
}

.card-text > p {
  color: #3a3b3d;
}

.card-text {
  flex-direction: row-reverse;
}

.card-date-container {
  height: 48px;
  width: 100%;
  padding: 12px 16px;
}

.card-master-header {
  background-color: #d9325a;
  height: 52px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.custom-border {
  border: 1px solid #dce0e5;
  border-radius: 12px;
}

.card-group-content {
  width: 100%;
  height: auto;
  display: flex;
  gap: 24px;
  flex-direction: column;
  padding: 16px;
  background-color: #f7f8fa;
}

.custom-chip {
  padding: 4px 8px;
  display: flex;
  gap: 2px;
  width: max-content;
}

.sub-card-title {
  padding: 8px 12px;
  border-top: 3px solid #d9325a;
}

.sub-card-text {
  font-family: "Noto Sans KR", sans-serif;
  font-size: 14px;
  line-height: 21px;
  letter-spacing: 0.1px;
  font-weight: 500;
}

.sub-card-header {
  display: flex;
  flex-direction: column;
}

.sub-card-text-content {
  color: #6b6d70;
  font-weight: 400;
  font-size: 12px;
  line-height: 18px;
  letter-spacing: 0.4px;
}

.cucon {
  background-color: #ffffff;
  border-radius: 8px 8px 0px 0px;
}

.category-action-container {
  height: 96px;
  padding: 24px;
}

.category-content-container {
  display: flex;
  height: 100%;
  width: 100%;
}

.tree-view-main-content {
  display: flex;
  gap: 56px;
}

.offer-tree-chart-area {
  width: 100%;
  padding: 22px 40px 12px;
  display: flex;
  -ms-overflow-style: none; /* Internet Explorer 10+ */
  scrollbar-width: none; /* Firefox */
}

.offer-tree-chart-area ::-webkit-scrollbar {
  display: none; /* Safari and Chrome */
}

.category-left-content {
  display: flex;
  flex-direction: column;
  gap: 10px;
  width: 100%;
  overflow: hidden;
}

.category-content-wrapper {
  display: flex;
  width: 100%;
}

.center-content {
  margin: auto;
}

.width-80 {
  width: 70%;
}

.side-width {
  width: calc(100% - 220px);
}

@media only screen and (min-width: 1800px) {
  .width-80 {
    width: 75%;
  }
}

@media only screen and (min-width: 2200px) {
  .width-80 {
    width: 80%;
  }
}

@media only screen and (min-width: 2800px) {
  .width-80 {
    width: 90%;
  }
}
</style>
