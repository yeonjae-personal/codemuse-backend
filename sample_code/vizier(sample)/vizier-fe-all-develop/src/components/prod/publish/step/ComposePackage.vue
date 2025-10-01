<template>
  <LocomotiveComponent
    v-if="composeItems?.length"
    scroll-container-class="px-2"
    scroll-content-class="flex flex-col gap-3 py-2"
  >
    <template #top-content-fixed>
      <ItemDrop
        v-if="isEdit || isCreate"
        class-name="h-[56px] mb-3"
        @drop="drop($event)"
        @dragover="allowDrop($event)"
      />
    </template>
    <template v-for="item in composeItems" :key="item.chngDataCode">
      <div class="px-1">
        <CardPublishType :data="item" :actions="listActions(item)" />
      </div>
    </template>
  </LocomotiveComponent>
  <template v-else>
    <div class="px-2 h-full">
      <ItemDrop
        v-if="isEdit || isCreate"
        class-name="h-[56px] mb-3"
        @drop="drop($event)"
        @dragover="allowDrop($event)"
      />
      <NoData class="max-h-[calc(100%-68px)]" />
    </div>
  </template>
</template>
<script setup lang="ts">
import { DRAG_PUBLISH_COMPOSE_ITEM_TYPE } from "@/constants/publish";
import { ComposeItem } from "@/interfaces/prod/publishInterface";
import { useI18n } from "vue-i18n";
import TrashIcon from "@/components/prod/icons/TrashIcon.vue";
import OpenInNewIcon from "@/components/prod/icons/OpenInNewIcon.vue";
import { ActionType } from "@/interfaces/prod";

const { t } = useI18n();
const emit = defineEmits([
  "update:detailModal",
  "open-side-pane",
  "add-new-item",
  "remove-item",
  "open-tab",
]);

const props = defineProps({
  isEdit: {
    type: Boolean,
    default: false,
  },
  isCreate: {
    type: Boolean,
    default: false,
  },
  dataList: {
    type: Array as () => ComposeItem[],
    default: () => [],
  },
  dragType: {
    type: String,
    default: "",
  },
});

const dropData = ref<any>(null);

const listActions = (item: ComposeItem): ActionType[] => {
  const openNewTab = {
    name: t("product_platform.openinNewWindow"),
    icon: OpenInNewIcon,
    iconProps: {
      class: "text-text-lighter",
    },
    onClick: () => {
      emit("open-tab", item);
    },
  };
  const removeAction = {
    name: t("LB00000500"),
    icon: TrashIcon,
    onClick: () => {
      emit("remove-item", item);
    },
  };
  return props.isEdit || props.isCreate
    ? [removeAction, openNewTab]
    : [openNewTab];
};

const composeItems = computed<ComposeItem[]>({
  get() {
    return props.dataList;
  },
  set(newVal) {
    emit("update:detailModal", newVal);
  },
});

const drop = (event: any) => {
  event.preventDefault();
  dropData.value = event.dataTransfer.getData("item")
    ? JSON.parse(event.dataTransfer.getData("item"))
    : null;
  if (dropData.value) {
    emit("add-new-item", dropData.value);
  }
};

const allowDrop = async (event: any) => {
  if (props.dragType !== DRAG_PUBLISH_COMPOSE_ITEM_TYPE) {
    return true;
  }
  event.preventDefault();
};
</script>
