<template>
  <div
    class="tree-node"
    :class="{
      'tree-node-big-size': nodeSize != 's',
      'highlighted-tree-node-parent': item.isActive,
    }"
    :style="{
      '--parent-height': nodeBranchHeight,
    }"
  >
    <div
      class="px-[16px] py-[12px] zoom-animation bg-white relative z-[999]"
      :class="{
        'highlighted-tree-node': item.isActive,
      }"
      :style="{ opacity: disable ? '0.3' : '1' }"
      @click="$emit('selectItemNode', YES_NO_VALUE.NO)"
      @drop="emit('drop', $event)"
      @dragover="emit('dragover', $event)"
    >
      <div class="tree-node-prepend"></div>
      <div class="node-content-container">
        <div class="tree-node-title-container">
          <p class="tree-node-title">
            <CustomTooltip :content="item.ctgrNodeName">
              <span
                :style="{
                  'font-weight': '500',
                }"
                class="text-[#3A3B3D] my-[0px] text-truncate !no-underline"
                v-html="highlightedName"
              />
            </CustomTooltip>
          </p>
          <div v-if="isNew" class="new-mark"></div>
          <div
            v-if="showCount"
            class="number-count"
            :class="isNew && !editable ? 'mr-[5px]' : ''"
          >
            <p>{{ item.totalOfferCount }}</p>
          </div>
          <div
            v-if="editable && !disable"
            class="relative leading-none"
            :class="showCount ? '' : 'ml-[auto]'"
            @click="handleOpenOptions"
          >
            <base-popover
              :options="actions"
              custom-location="bottom-right"
              class="flex-initial"
              @open-options="emit('open-options')"
            >
              <template #activator>
                <DotsVerticalIcon />
              </template>
            </base-popover>
          </div>
        </div>
        <div v-if="nodeSize !== 'small'" class="tree-node-chip-group">
          <div v-if="showCount" class="node-chip-item">
            <v-chip label size="x-small" class="node-chip-item-text">{{
              item.totalOfferCount + " Offer"
            }}</v-chip>
          </div>

          <div class="node-chip-item">
            <v-chip
              v-if="nodeSize === 'large'"
              label
              size="x-small"
              class="node-chip-item-text"
              >{{ item.rgstUser }}</v-chip
            >
          </div>
        </div>
      </div>
      <div class="tree-node-toggle-btn">
        <v-btn
          ref="nodeRef"
          width="24px"
          height="24px"
          :color="toggleBtnColor"
          density="comfortable"
          class="custom-btn"
          :class="{ 'fetch-offer-btn': isShowingOffers }"
          icon="mdi-play"
          @click.stop="setToggle"
        ></v-btn>
      </div>
    </div>
  </div>
</template>

<script setup>
import { BORDER_CONFIG, YES_NO_VALUE } from "@/constants/index";
import { useCategoryStore } from "@/store";

const props = defineProps({
  nodeActiveType: {
    type: String,
    default: "select",
  },
  nodeSize: {
    type: String,
    default: "small",
  },
  isActive: {
    type: Boolean,
    default: false,
  },
  item: {
    type: Object,
    default: null,
  },
  branchHeight: {
    type: Number,
    default: 0,
  },
  index: {
    type: Number,
    default: 0,
  },
  isShowingOffers: {
    type: Boolean,
    default: false,
  },
  isNew: {
    type: Boolean,
    default: false,
  },
  disable: {
    type: Boolean,
    default: false,
  },
  showCount: {
    type: Boolean,
    default: true,
  },
  isNew: {
    type: Boolean,
    default: false,
  },
  editable: {
    type: Boolean,
    default: false,
  },
  actions: {
    type: Array,
    default: () => [],
  },
});
const defaultBorderActive = ref(BORDER_CONFIG.ACTIVE);

const categoryStore = useCategoryStore();
const { searchCategoryText } = storeToRefs(categoryStore);
onUpdated(() => {
  if (props.item.isActive) {
    emit("setHighlightSibling", props.index);
  }
});

const nodeBranchHeight = computed(() => {
  let height =
    props.item.showChilderen && props.branchHeight && props.nodeSize
      ? `${props.branchHeight + 30}px`
      : "calc(100% + 30px)";
  return height;
});

const toggleBtnColor = computed(() =>
  (props.item.children && props.item.isChildActive) ||
  (props.item.showChilderen && props.item.children)
    ? "#BA1642"
    : "#52545766"
);

const highlightedName = computed(() => {
  // eslint-disable-next-line security/detect-non-literal-regexp
  const regex = new RegExp(`(${searchCategoryText.value})`, "gi");
  return props.item.ctgrNodeName
    ? props.item.ctgrNodeName.replace(
        regex,
        '<span class="highlight">$1</span>'
      )
    : props.item.ctgrNodeName;
});

const emit = defineEmits([
  "setHighlightSibling",
  "setToggleChildNode",
  "selectItemNode",
  "drop",
  "dragover",
  "open-options",
]);
const emitToGrandparent = inject("handleFetchOfferList");

const setToggle = () => {
  if (props.item?.isLeafCategoryNode === YES_NO_VALUE.YES) {
    categoryStore.setIsSearchProductOfNode(true);
    getOfferList(props.item.ctgrNodeUuid);
    emit("selectItemNode", props.item?.isLeafCategoryNode);
  } else {
    emit("setToggleChildNode");
  }
};

const getOfferList = (nodeId) => {
  if (emitToGrandparent) {
    emitToGrandparent(nodeId);
  }
};
</script>

<style scoped>
.tree-node {
  width: 207.5px;
  height: 48px;
  border-radius: 4px;
  border-width: 1px 0px 0px 0px;
  box-shadow: 0px 4px 16px 0px #0000001f;
  position: relative;
  background-color: #ffffff;
}

.tree-node::before {
  content: "";
  position: absolute;
  width: 30px;
  height: 24px;
  top: 0;
  left: -30px;
  border-radius: 0 0 0 16px;
  border: 3px solid #bdc1c7;
  border-right: none;
  border-top: none;
  background: transparent;
  cursor: default;
}

.tree-node::after {
  content: "";
  position: absolute;
  width: 30px;
  border-right: none;
  border-top: none;
  display: block;
  border-left: 3px solid #bdc1c7;
  background: transparent;
  height: var(--parent-height);
  left: -30px;
  top: 0;
  cursor: default;
}

.firstNode {
  position: relative;
}

.firstNode::after {
  height: calc(var(--parent-height) - 24px);
  top: calc(24px - 3px);
  box-sizing: content-box;
  width: 27px;
  right: unset !important;
  left: calc(-60px + 3px);
  border-left: none;
  position: absolute;
  border-right: 3px solid #bdc1c7;
  border-top: 3px solid #bdc1c7;
  border-top-right-radius: 16px;
  background: transparent;
  cursor: default;
}

.firstNode::before {
  border-radius: 0 0 0 0;
  border-left: none;
  width: 56px;
  left: -56px;
}

.highlighted-tree-node-parent.firstNode::before {
  border-radius: 0 0 0 0;
  border-left: none;
}

.lastNode::after {
  display: none;
}

.tree-node:hover {
  cursor: pointer;
}

.tree-node-prepend {
  width: 8px;
  height: 28px;
  gap: 0px;
  border-radius: 5px;
  opacity: 0px;
  background-color: #bdc1c7;
  position: absolute;
  left: -3px;
  top: 10px;
  z-index: 10;
}

.hasMediumSizeNode > .tree-node > .tree-node-prepend,
.hasLargeSizeNode > .tree-node > .tree-node-prepend {
  top: 13px;
}

.tree-node-title-container {
  height: 24px;
  padding: 0px 8px 0px 0px;
  display: flex;
  gap: 4px;
  position: relative;
}

.tree-node-title {
  max-width: 156px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-size: 13px;
  font-weight: 500;
  line-height: 19.5px;
  letter-spacing: 0.25px;
  text-align: left;
  color: #3a3b3d;
  align-self: center;
}

.number-count {
  display: flex;
  align-items: center;
  margin-left: auto;
  padding: 4px 8px;
  background-color: #fff0f2;
  border-radius: 4px;
}

.number-count > p {
  font-weight: 500;
  font-family: "Noto Sans KR", sans-serif;
  text-align: center;
  line-height: 18px;
  font-size: 11px;
  letter-spacing: 0.5px;
  color: #ba1642;
}

.tree-node-toggle-btn {
  position: absolute;
  right: -10px;
  top: 10px;
  z-index: 10;
}
.node-content-container {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.tree-node-chip-group {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.highlighted-tree-node {
  outline: 2px solid v-bind(defaultBorderActive);
  position: relative;
}

.highlighted-tree-node::before {
  border: 3px solid v-bind(defaultBorderActive);
  border-right: none !important;
  border-top: none !important;
  background: transparent;
  border-radius: 0 0 0 17px;
  z-index: 9;
}
.highlighted-tree-node-parent::before {
  border: 3px solid v-bind(defaultBorderActive);
  border-right: none !important;
  border-top: none !important;
  background: transparent;
  border-radius: 0 0 0 17px;
  z-index: 9;
}
.highlighted-tree-node .tree-node-prepend {
  background-color: #ba1642;
}

:deep() .mdi-play::before {
  font-size: 14px !important;
  margin: auto;
  color: #ffff;
}

.highlighted-parent-node > .tree-node .tree-node-prepend {
  background-color: v-bind(defaultBorderActive);
}

.fetch-offer-btn {
  background-color: #4054b2 !important;
}

.node-chip-item-text {
  font-size: 11px;
  font-weight: 400;
  line-height: 16.5px;
  letter-spacing: 0.25px;
  text-align: center;
  height: 24px;
}

:deep() .highlight {
  background-color: yellow;
}

.new-mark {
  width: 8px;
  height: 8px;
  position: absolute;
  top: -8px;
  right: -7px;
  background: #ea4f3a;
  border-radius: 999px;
}
</style>
