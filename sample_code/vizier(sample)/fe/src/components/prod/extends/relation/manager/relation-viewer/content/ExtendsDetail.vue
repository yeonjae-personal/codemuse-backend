<template>
  <div
    :class="[
      'h-full rounded-[8px]',
      isGridMode ? 'bg-[#F0F2F5] mx-[12px] mb-[12px]' : 'bg-[#fff] mx-6 mb-6',
    ]"
  >
    <RelationTable v-if="!isGridMode" />
    <div
      v-else
      class="mx-[auto] flex flex-column"
      :class="isEdit ? 'h-[calc(100vh-240px)]' : 'h-[calc(100vh-290px)]'"
    >
      <LocomotiveComponent
        scroll-content-class="flex flex-column"
        scroll-container-class="!pr-[0px] !pl-0 "
        top-content-class="flex"
      >
        <template #top-content-fixed>
          <DescriptionPanel :category="extendsView" />
        </template>
        <div
          ref="detailContent"
          class="mx-[auto] mt-[16px] flex relative"
          :class="{ 'px-[40px]': extendsView === EXTENDS_VIEW.DETAIL }"
        >
          <div
            :style="{
              width: extendsView === EXTENDS_VIEW.SIMPLE ? '400px' : '600px',
            }"
          >
            <ExtendsExpandColumn
              v-if="detailViewData.isShowLeaderCol"
              :items="detailViewData.expandColumnLeaderList"
              :category="EXTEND_CATEGORY.LEADER"
              :search-obj="searchParams"
              :active-obj="structureActiveMapLeader"
              :offer-duplicate-mode="offerDuplicateMode"
            />
          </div>
          <div class="w-[56px]">
            <ExtendCanvas
              v-if="detailViewData.isShowLeaderCol"
              id="canvas1"
              :width="56"
              :height="canvasHeight"
              :list-coordinates="detailViewData.leaderLineCoordinates"
            />
          </div>
          <div
            :style="{
              width: extendsView === EXTENDS_VIEW.SIMPLE ? '370px' : '344px',
            }"
          >
            <ExtendsFocusColumn
              v-if="
                (extendsView === EXTENDS_VIEW.SIMPLE &&
                  selectedItem &&
                  selectedItem?.leaderList &&
                  selectedItem?.followerList) ||
                (extendsView === EXTENDS_VIEW.DETAIL &&
                  detailViewData.focusColumnFollowerList?.length &&
                  detailViewData.focusColumnLeaderList?.length)
              "
              :item-width="extendsView === EXTENDS_VIEW.SIMPLE ? 370 : 344"
              :search-obj="searchParams"
              :active-obj="structureActiveFocusColumn"
              :offer-duplicate-mode="offerDuplicateMode"
            />
          </div>
          <div class="w-[56px]">
            <ExtendCanvas
              v-if="detailViewData.isShowFollowerCol"
              id="canvas2"
              :width="56"
              :height="canvasHeight"
              :list-coordinates="detailViewData.followerLineCoordinates"
            />
          </div>
          <div
            :style="{
              width: extendsView === EXTENDS_VIEW.SIMPLE ? '400px' : '600px',
            }"
          >
            <ExtendsExpandColumn
              v-if="detailViewData.isShowFollowerCol"
              :items="detailViewData.expandColumnFollowerList"
              :category="EXTEND_CATEGORY.FOLLOWER"
              :search-obj="searchParams"
              :active-obj="structureActiveMapFollower"
              :offer-duplicate-mode="offerDuplicateMode"
            />
          </div>
        </div>
      </LocomotiveComponent>
    </div>
  </div>
</template>

<script setup lang="ts">
import { EXTENDS_VIEW, EXTEND_CATEGORY } from "@/constants/extendsManager";
import {
  useExtendManagerStore,
  useRelationManagerDuplicateStore,
} from "@/store";

const props = defineProps({
  offerDuplicateMode: {
    type: Boolean,
    default: false,
  },
});
const extendManagerStore = useExtendManagerStore();
const relationManagerDuplicateStore = useRelationManagerDuplicateStore();
const selectedStore = computed(() =>
  props.offerDuplicateMode ? relationManagerDuplicateStore : extendManagerStore
);
const {
  isEdit,
  detailViewData,
  selectedItem,
  extendsView,
  paramsHightlightSearch,
  structureActiveMapLeader,
  structureActiveMapFollower,
  structureActiveFocusColumn,
  isGridMode,
} = storeToRefs(selectedStore.value);
const { calculateLeaderCoordinates, calculateFollowerCoordinates } =
  selectedStore.value;

const detailContent = ref();
const searchParams = ref();
const canvasHeight = ref(0);

onMounted(() => {
  setCanvasHeight();
});

onUpdated(() => {
  setCanvasHeight();
});

watch(
  () => [
    detailViewData.value.leaderCoordinates,
    detailViewData.value.focusFollowerCoordinates,
  ],
  () => {
    nextTick(() => {
      calculateLeaderCoordinates();
    });
  },
  { deep: true }
);
watch(
  () => [
    detailViewData.value.followerCoordinates,
    detailViewData.value.focusLeaderCoordinates,
  ],
  () => {
    nextTick(() => {
      calculateFollowerCoordinates();
    });
  },
  { deep: true }
);
watch(
  () => paramsHightlightSearch.value.keyword,
  () => {
    searchParams.value = paramsHightlightSearch.value;
  }
);

const setCanvasHeight = () => {
  let listGrib = document.getElementsByClassName("expand-manager-column");
  let listHeight: any[] = [];
  for (const el of listGrib) {
    listHeight.push(el.clientHeight);
  }
  canvasHeight.value = Math.max(...listHeight);
};
const setCoordinates = async (category) => {
  const coorList: any[] = [];
  await nextTick(() => {
    let elementId = document.getElementById(category);
    if (elementId) {
      let listElement: any = elementId.getElementsByClassName("expandList");
      for (const element of listElement) {
        coorList.push(element.offsetTop);
      }
      if (category === EXTEND_CATEGORY.LEADER) {
        detailViewData.value.leaderCoordinates = [...coorList];
      } else if (category === EXTEND_CATEGORY.FOLLOWER) {
        detailViewData.value.followerCoordinates = [...coorList];
      }
    }
  });
};

provide("handleCalCanvasHeight", { setCanvasHeight });
provide("handleCalCoordinates", { setCoordinates });
</script>

<style lang="scss" scoped>
.open-one-side {
  width: calc(100% - 436px);
}
.open-two-side {
  width: 1300px;
}
</style>
