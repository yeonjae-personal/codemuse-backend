<template>
  <div ref="wrapper" class="bento-wrapper" style="height: 100%">
    <GridLayout
      ref="gridLayoutRef"
      v-model:layout="layout"
      :is-resizable="false"
      :col-num="3"
      :is-bounded="true"
      class="bento-grid"
      :vertical-compact="false"
      :compact-type="null"
      :row-height="rowHeight"
    >
      <GridItem
        v-for="item in layout"
        :key="item.i"
        :x="item.x"
        :y="item.y"
        :w="item.w"
        :h="item.h"
        :i="item.i"
        :static="item.static"
        class="grid-item"
        @move="moveEvent"
      >
        <RemoteMFE :item="item" @remove-item="removeItem" />
      </GridItem>
    </GridLayout>
  </div>
</template>

<!-- eslint-disable id-length -->
<script setup>
import { useSnackbarStore } from "@/store";
import { GridItem, GridLayout } from "grid-layout-plus";
import { defineAsyncComponent } from "vue";

const RemoteMFE = defineAsyncComponent({
  // @ts-ignore
  loader: () => import("remote/remote-app"),
  timeout: 10000, // optional: 10초 이상 걸리면 실패 처리
  onError(error, retry, fail, attempts) {
    console.warn("❌ Remote DashBoard load failed:", error);
    if (attempts <= 2) {
      retry(); // 최대 2번 재시도
    } else {
      fail(); // 실패 확정
    }
  },
  loadingComponent: {
    template: `<div> Loading remote module...</div>`,
  },
  errorComponent: {
    template: `<div> Remote service is temporarily unavailable.</div>`,
  },
});

const props = defineProps({
  modelValue: {
    type: Object,
    default: () => {},
  },
  data: {
    type: Array,
    default: () => [],
  },
});
const emit = defineEmits(["update:modelValue"]);
const { showSnackbar } = useSnackbarStore();

const gridData = () => {
  const result = [];
  let index = 1;
  for (let y = 0; y < 2; y++) {
    for (let x = 0; x < 3; x++) {
      result.push({
        x: x,
        y: y,
        w: 1,
        h: 1,
        i: index++,
        static: false,
        loading: false,
        id: "",
        name: "",
        code: "",
        desc: "",
      });
    }
  }
  return result;
};

const layout = ref(gridData());

const gridLayoutRef = ref();
const wrapper = ref();
const rowHeight = ref(0);

const updateRowHeight = () => {
  rowHeight.value = window.innerHeight / 2 - 75;
};

function filterLoading(x, y) {
  const itemIndex = layout.value.findIndex(
    (item) => item.name === "" && item.x === x && item.y === y
  );

  layout.value = layout.value.map((item, index) => {
    return { ...item, loading: index === itemIndex };
  });
  return itemIndex + 1;
}

function handleDragMoving(event) {
  const { mouseAt, dragItem } = event.detail;
  const parentRect = wrapper.value?.getBoundingClientRect();

  if (!parentRect || !gridLayoutRef.value) return;

  const cellWidth = parentRect.width / 3;
  const cellHeight = parentRect.height / 2;
  const mouseX = mouseAt.x - parentRect.left;
  const mouseY = mouseAt.y - parentRect.top;
  const gridCell = getGridCell(mouseX, mouseY, cellWidth, cellHeight);
  dragItem.i = filterLoading(gridCell.col, gridCell.row);
}

function getGridCell(mouseX, mouseY, cellWidth, cellHeight) {
  const col = Math.floor(mouseX / cellWidth);
  const row = Math.floor(mouseY / cellHeight);
  return { col, row };
}

async function handleDragEnded(event) {
  const { mouseAt, dragItem } = event.detail;
  const parentRect = wrapper.value?.getBoundingClientRect();
  if (!parentRect) return;
  const cellWidth = parentRect.width / 3;
  const cellHeight = parentRect.height / 2;
  const mouseX = mouseAt.x - parentRect.left;
  const mouseY = mouseAt.y - parentRect.top;
  const gridCell = getGridCell(mouseX, mouseY, cellWidth, cellHeight);
  setTimeout(() => {
    layout.value = layout.value.map((item) => {
      return { ...item, loading: false };
    });
  }, 300);
  const itemIndex = layout.value.findIndex(
    (item) => item.name === dragItem.name
  );
  const currentItem = layout.value.find(
    (item) => item.x === gridCell.col && item.y === gridCell.row
  );
  if (currentItem.name) {
    showSnackbar("Item cannot be placed here: Space already occupied", "error");
    return;
  }
  if (itemIndex !== -1) {
    showSnackbar("Item already existed", "error");
    return;
  }
  currentItem.name = dragItem.name;
  currentItem.id = dragItem.id;
  currentItem.code = dragItem.code;
  currentItem.desc = dragItem.desc;
}

watch(
  () => props.data,
  () => {
    layout.value = layout.value.map((item) => {
      const newItem = props.data.find(
        (gridItem) => gridItem.posX === item.x && gridItem.posY === item.y
      );
      return newItem
        ? {
            ...item,
            name: newItem.dsbdViewName,
            desc: newItem.dsbdViewDscrCntn,
            code: newItem.dsbdViewCode,
            id: newItem.dsbdViewUuid,
          }
        : item;
    });
  }
);

watch(layout, (newValue) => {
  emit("update:modelValue", newValue);
});

onMounted(() => {
  updateRowHeight();
  window.addEventListener("resize", updateRowHeight);
  document.addEventListener("drag-moving", handleDragMoving);
  document.addEventListener("drag-ended", handleDragEnded);
});

onBeforeUnmount(() => {
  document.removeEventListener("drag-moving", handleDragMoving);
  document.removeEventListener("drag-ended", handleDragEnded);
  window.removeEventListener("resize", updateRowHeight);
});

function moveEvent(i, newX, newY) {
  const currentItem = layout.value.find((item) => item.i === i);
  const swappedItem = layout.value.find(
    (item) => item.x === newX && item.y === newY
  );
  if (currentItem && swappedItem) {
    const tempX = swappedItem.x;
    const tempY = swappedItem.y;
    swappedItem.x = currentItem.x;
    swappedItem.y = currentItem.y;
    currentItem.x = tempX;
    currentItem.y = tempY;
  }
}

function removeItem(id) {
  const currentItem = layout.value.find((item) => item.i === id);
  if (currentItem) {
    currentItem.code = "";
    currentItem.name = "";
    currentItem.desc = "";
    currentItem.id = "";
  } else {
    showSnackbar("Cannot delete item", "error");
  }
}
</script>

<style>
.vgl-layout {
  --vgl-placeholder-bg: none;
  --vgl-placeholder-opacity: 0;
  --vgl-placeholder-z-index: -9999;
}

.vgl-layout .vgl-item--placeholder {
  display: none !important;
}

.vgl-item--placeholder {
  display: none !important;
}

.bento-wrapper {
  margin-left: -13px;
  margin-top: -11px;
  margin-right: 4px;
}

.bento-grid {
  width: 100%;
  height: 100%;
}

.bento-grid .grid-item {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 4px;
}
</style>
