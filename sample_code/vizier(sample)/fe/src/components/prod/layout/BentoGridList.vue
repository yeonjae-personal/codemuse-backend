<template>
  <div class="layout-container">
    <div class="content">
      <div class="tabs">
        <button
          :class="{ active: activeTab === 'Statistics' }"
          @click="activeTab = 'Statistics'"
        >
          {{ t("product_platform.dashboard.statistics") }}
        </button>
        <button
          :class="{ active: activeTab === 'Personalized' }"
          @click="activeTab = 'Personalized'"
        >
          {{ t("product_platform.dashboard.personalized") }}
        </button>
      </div>
      <VueDraggable
        v-model="computedData"
        animation="150"
        class="widget-content overflow-x-auto custom-scroll"
      >
        <div
          v-for="item in computedData"
          :key="item.id"
          class="widget-item draggable"
          :class="{ disabled: item.disabled }"
          :draggable="!item.disabled"
          unselectable="on"
          @drag="drag"
          @dragend="dragEnd"
          @dragstart="handleDragStart($event, item)"
        >
          <WidgetItem
            :widget-name="t(item.dsbdViewName)"
            :widget-disabled="item.disabled"
          />
        </div>
      </VueDraggable>
    </div>
  </div>
</template>

<!-- eslint-disable id-length -->
<script setup>
import { useI18n } from "vue-i18n";
import throttle from "lodash-es/throttle";
import { VueDraggable } from "vue-draggable-plus";
import WidgetItem from "./WidgetItem.vue";
import useDragUserPocket from "@/composables/useDragUserPocket";

const { t } = useI18n();
const props = defineProps({
  data: {
    type: Array,
    default: () => [],
  },
  listItemCode: {
    type: Array,
    default: () => [],
  },
});

const { handleDragUserPocket } = useDragUserPocket();

const dropId = "drop";
const activeTab = ref("Statistics");

const computedData = computed(() => {
  const statisticsData = props.data.filter(
    (item) => item.ctgrTypeCode === "satistics"
  );
  const personalizedData = props.data.filter(
    (item) => item.ctgrTypeCode === "personalized"
  );
  const result =
    activeTab.value === "Statistics" ? statisticsData : personalizedData;
  return result
    .map((item) => {
      return {
        ...item,
        disabled: item.dsbdViewCode
          ? props.listItemCode.includes(item.dsbdViewCode)
          : false,
      };
    })
    .sort((a, b) => a.sortValue - b.sortValue);
});

const mouseAt = { x: -1, y: -1 };
const dragItem = ref({
  x: -1,
  y: -1,
  w: 1,
  h: 1,
  i: "",
  static: false,
  loading: false,
  id: "",
  name: "",
  code: "",
  desc: "",
});

function syncMousePosition(event) {
  event.preventDefault();
  mouseAt.x = event.clientX;
  mouseAt.y = event.clientY;
}

onMounted(() => {
  document.addEventListener("dragover", syncMousePosition);
});

onBeforeUnmount(() => {
  document.removeEventListener("dragover", syncMousePosition);
});

const handleDragStart = (event, item) => {
  const placeHolder = document.querySelector(".vgl-item--placeholder");
  if (placeHolder) placeHolder.remove();
  handleDragUserPocket(event, item);
  dragItem.value.name = item.dsbdViewName;
  dragItem.value.id = item.dsbdViewUuid;
  dragItem.value.code = item.dsbdViewCode;
  dragItem.value.disabled = item.disabled;
  dragItem.value.desc = item.dsbdViewDscrCntn;
  dragItem.value.w = 1;
  dragItem.value.h = 1;
};

const drag = throttle(() => {
  if (dragItem.value.disabled) return;
  const dragEvent = new CustomEvent("drag-moving", {
    detail: { mouseAt, dragItem: dragItem.value, dropId },
  });
  document.dispatchEvent(dragEvent);
}, 100);

function dragEnd() {
  if (dragItem.value.disabled) return;
  const dragEndEvent = new CustomEvent("drag-ended", {
    detail: { mouseAt, dragItem: dragItem.value, dropId },
  });
  document.dispatchEvent(dragEndEvent);
}
</script>

<style scoped>
.layout-container {
  border: 2px solid #ffff;
  font-family: Arial, sans-serif;
  margin: 15px 0;
}

.header {
  margin-bottom: 12px;
  text-align: left;
}

.header h2 {
  margin: 0;
  font-size: 1.2rem;
}

.header span {
  color: grey;
  font-size: 0.9rem;
}

.tabs {
  display: flex;
  margin-bottom: 8px;
  background-color: #f0f2f5;
  border-radius: 999px;
  padding: 4px;
}

.tabs button {
  padding: 6px 12px 6px 12px;
  gap: 4px;
  border-radius: 999px;
  width: 50%;
  font-family: Noto Sans KR;
  font-size: 11px;
  font-weight: 500;
  line-height: 16.5px;
  letter-spacing: 0.25px;
  text-align: center;
  color: rgba(107, 109, 112, 1);
}

.tabs button.active {
  color: rgba(186, 22, 66, 1);
  background: #ffff;
}

.widget-content {
  display: flex;
  flex-direction: column;
  gap: 12px;
  height: 288px;
  padding-right: 4px;
}

.widget-item {
  height: 46px;
  width: 100%;
}

.widget-item.draggable {
  cursor: grab;
  user-select: none;
}

.widget-item.draggable:active {
  cursor: grabbing;
}

.widget-item.disabled {
  cursor: not-allowed;
}
</style>
