<template>
  <div class="bg-[#F0F2F5] h-full mx-[12px] mb-[12px] rounded-[8px]">
    <div class="grip-custom-scroll custom-scroll">
      <LocomotiveComponent
        is-show-scrollbar
        scroll-container-class="!px-[0px]"
        scroll-content-class="flex pb-[40px]"
        top-content-class="flex pb-4"
      >
        <template #top-content-fixed>
          <div
            class="relative m-auto flex gap-[24px] px-[12px]"
            style="z-index: 1"
          >
            <div
              class="description-panel min-w-[400px]"
              :class="{
                'active-panel':
                  impactAnalysisStore.getIsShowStatus.offer &&
                  impactAnalysisStore.getSearchPattern == TARGET_TYPE.OFFER,
              }"
            >
              <p class="description-title">
                {{ $t("product_platform.offer_title") }}
              </p>
            </div>
            <div
              class="description-panel min-w-[400px]"
              :class="{
                'active-panel':
                  impactAnalysisStore.getIsShowStatus.component &&
                  impactAnalysisStore.getSearchPattern == TARGET_TYPE.COMPONENT,
              }"
            >
              <p class="description-title">
                {{ $t("product_platform.component_title") }}
              </p>
            </div>
            <div
              class="description-panel min-w-[400px]"
              :class="{
                'active-panel':
                  impactAnalysisStore.getIsShowStatus.resource &&
                  impactAnalysisStore.getSearchPattern == TARGET_TYPE.RESOURCE,
              }"
            >
              <p class="description-title">
                {{ $t("product_platform.resource_title") }}
              </p>
            </div>
          </div>
        </template>
        <div class="catalog-container">
          <div class="w-[370px]">
            <BaseGrid
              v-if="impactAnalysisStore.getIsShowStatus.offer"
              class="base-grid"
              :selected-item="impactAnalysisStore.getSelectedSearchItem"
              :item-list="extendedOfferList"
              :mode="
                impactAnalysisStore.getSearchPattern === TARGET_TYPE.OFFER
                  ? 'focused'
                  : 'extended'
              "
              :category-name="TARGET_TYPE.OFFER"
            />
          </div>
          <div class="w-[56px]">
            <CanvasDisplay
              v-if="
                impactAnalysisStore.getIsShowStatus.offer &&
                impactAnalysisStore.getIsShowStatus.component
              "
              id="canvas1"
              :width="56"
              :height="canvasHeight"
              :list-coordinates="canvasListOffCom"
            />
          </div>
          <div class="w-[370px]">
            <BaseGrid
              v-if="impactAnalysisStore.getIsShowStatus.component"
              class="base-grid"
              :selected-item="impactAnalysisStore.getSelectedSearchItem"
              :item-list="extendedComponentList"
              :mode="
                impactAnalysisStore.getSearchPattern === TARGET_TYPE.COMPONENT
                  ? 'focused'
                  : 'extended'
              "
              :category-name="TARGET_TYPE.COMPONENT"
            />
          </div>
          <div class="w-[56px]">
            <CanvasDisplay
              v-if="
                impactAnalysisStore.getIsShowStatus.component &&
                impactAnalysisStore.getIsShowStatus.resource
              "
              id="canvas2"
              :width="56"
              :height="canvasHeight"
              :list-coordinates="canvasListComRes"
            />
          </div>
          <div class="w-[370px]">
            <BaseGrid
              v-if="impactAnalysisStore.getIsShowStatus.resource"
              class="base-grid"
              :selected-item="impactAnalysisStore.getSelectedSearchItem"
              :item-list="extendedResourceList"
              :mode="
                impactAnalysisStore.getSearchPattern === TARGET_TYPE.RESOURCE
                  ? 'focused'
                  : 'extended'
              "
              :category-name="TARGET_TYPE.RESOURCE"
            />
          </div>
        </div>
      </LocomotiveComponent>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useImpactAnalysisStore } from "@/store";
import cloneDeep from "lodash-es/cloneDeep";
import { TARGET_TYPE } from "@/constants/";
import { TYPE_PRODUCT } from "@/constants/offer";

const impactAnalysisStore = useImpactAnalysisStore();
const { isLoading } = storeToRefs(impactAnalysisStore);
const extendedComponentList = ref<any[]>([]);
const extendedResourceList = ref<any[]>([]);
const extendedOfferList = ref<any[]>([]);
const canvasHeight = ref(0);
const canvasListOffCom = ref<any>(null);
const canvasListComRes = ref<any>(null);

onMounted(() => {
  extendedOfferList.value = impactAnalysisStore.getOfferItemList;
  extendedComponentList.value = impactAnalysisStore.getComponentItemList;
  extendedResourceList.value = impactAnalysisStore.getResourceItemList;
});

onBeforeUpdate(() => {
  if (impactAnalysisStore.getShouldReset) {
    extendedComponentList.value = [];
    extendedResourceList.value = [];
    extendedOfferList.value = [];
    canvasHeight.value = 0;
    canvasListOffCom.value = null;
    canvasListComRes.value = null;
    impactAnalysisStore.setShouldReset(false);
  }
});

const setCanvasHeight = () => {
  let listGrib = document.getElementsByClassName("card-item-list");
  let listHeight: any = [];
  for (const el of listGrib) {
    listHeight.push(el.clientHeight);
  }
  canvasHeight.value = Math.max(...listHeight);
};

watch(
  () => impactAnalysisStore.getComponentItemList,
  (newVal) => {
    extendedComponentList.value = newVal;
  },
  { deep: true }
);
watch(
  () => impactAnalysisStore.getResourceItemList,
  (newVal) => {
    extendedResourceList.value = newVal;
  },
  { deep: true }
);

watch(
  () => impactAnalysisStore.getResourceComponentCoordinates,
  (val) => {
    if (impactAnalysisStore.getSearchPattern === TARGET_TYPE.OFFER) {
      canvasListComRes.value = cloneDeep(val);
    }
  },
  { deep: true }
);
watch(
  () => impactAnalysisStore.getOfferComponentCoordinates,
  (val) => {
    if (impactAnalysisStore.getSearchPattern === TARGET_TYPE.RESOURCE) {
      canvasListOffCom.value = cloneDeep(val);
    }
  },
  { deep: true }
);

watch(
  () => [
    impactAnalysisStore.getOfferCoordinates,
    impactAnalysisStore.getComponentToOfferCoordinates,
  ],
  () => {
    if (
      impactAnalysisStore.getSearchPattern === TARGET_TYPE.OFFER ||
      impactAnalysisStore.getSearchPattern === TARGET_TYPE.COMPONENT
    ) {
      impactAnalysisStore.actionOfferComponentCoordinates();
      if (impactAnalysisStore.getOfferComponentCoordinates.length) {
        canvasListOffCom.value =
          impactAnalysisStore.getOfferComponentCoordinates;
      }
    }
  },
  { deep: true }
);

watch(
  () => [
    impactAnalysisStore.getResourceCoordinates,
    impactAnalysisStore.getComponentToResourceCoordinates,
  ],
  () => {
    if (
      impactAnalysisStore.getSearchPattern === TARGET_TYPE.RESOURCE ||
      impactAnalysisStore.getSearchPattern === TARGET_TYPE.COMPONENT
    ) {
      impactAnalysisStore.actionResourceComponentCoordinates();
      if (impactAnalysisStore.getResourceComponentCoordinates.length) {
        canvasListComRes.value = cloneDeep(
          impactAnalysisStore.getResourceComponentCoordinates
        );
      }
    }
  },
  { deep: true }
);

watch(
  () => [
    impactAnalysisStore.getResourceComponentCoordinates,
    impactAnalysisStore.getOfferComponentCoordinates,
  ],
  () => {
    setCanvasHeight();
  },
  { deep: true }
);

const setExtendedList = async (item, type) => {
  const selectedObj = {
    index: 0,
    topPosition: 29,
    selectedItemList: item,
  };
  switch (type) {
    case TARGET_TYPE.OFFER:
      extendedOfferList.value = [selectedObj];
      impactAnalysisStore.setOfferItemList([selectedObj]);
      break;
    case TARGET_TYPE.COMPONENT:
      const itemSortList = sortList(item);
      extendedComponentList.value =
        impactAnalysisStore.getSearchPattern === TARGET_TYPE.OFFER
          ? itemSortList
          : item;
      impactAnalysisStore.setComponentItemList(extendedComponentList.value);
      break;
    case TARGET_TYPE.RESOURCE:
      extendedResourceList.value = [selectedObj];
      impactAnalysisStore.setResourceItemList([selectedObj]);
      break;
  }
};

const addItemToExtendedList = (item, type) => {
  if (type === TARGET_TYPE.OFFER) {
    if (!extendedOfferList.value.length) {
      impactAnalysisStore.getComponentItemList.forEach(() => {
        extendedOfferList.value.push({ topPosition: 0 });
      });
    }
    nextTick(() => {
      impactAnalysisStore.actionOfferComponentCoordinates(item.index);
      canvasListOffCom.value = cloneDeep(
        impactAnalysisStore.getOfferComponentCoordinates
      );
      let flatArr =
        impactAnalysisStore.getComponentToResourceCoordinates.flat(2);
      flatArr.forEach((value, index) => {
        extendedOfferList.value[index as number].topPosition = value;
      });
      extendedOfferList.value[item.index] = cloneDeep(item);
      reUpdateCoordinatesForOffer();
      isLoading.value = false;
    });
  } else {
    if (!extendedResourceList.value.length) {
      impactAnalysisStore.getComponentItemList.forEach(() => {
        extendedResourceList.value.push({ topPosition: 0 });
      });
    }
    nextTick(() => {
      impactAnalysisStore.actionResourceComponentCoordinates(item.index);
      canvasListComRes.value = cloneDeep(
        impactAnalysisStore.getResourceComponentCoordinates
      );
      let flatArr = impactAnalysisStore.getComponentToOfferCoordinates.flat(2);
      flatArr.forEach((value, index) => {
        extendedResourceList.value[index as number].topPosition = value;
      });
      extendedResourceList.value[item.index] = cloneDeep(item);
      reUpdateCoordinatesForResource();
      isLoading.value = false;
    });
  }
};
const removeItemToExtendedList = (index) => {
  if (impactAnalysisStore.getSearchPattern === TARGET_TYPE.OFFER) {
    nextTick(() => {
      canvasListComRes.value =
        impactAnalysisStore.getResourceComponentCoordinates;
      extendedResourceList.value[index as number] = {};
      let flatArr = impactAnalysisStore.getComponentToOfferCoordinates.flat(2);
      extendedResourceList.value.forEach((item, indexResourceList) => {
        if (!item) {
          return;
        } else {
          item.topPosition = flatArr[indexResourceList as number];
        }
      });
      reUpdateCoordinatesForResource();
      impactAnalysisStore.actionResourceComponentCoordinates(index);
      impactAnalysisStore.removeComponentToResourceCoordinates(index);
      impactAnalysisStore.removeResourceCoordinates(index);
      impactAnalysisStore.removeResourceComponentCoordinates(index);
      canvasListComRes.value = cloneDeep(
        impactAnalysisStore.getResourceComponentCoordinates
      );
    });
  }
  if (impactAnalysisStore.getSearchPattern === TARGET_TYPE.RESOURCE) {
    nextTick(() => {
      canvasListOffCom.value = impactAnalysisStore.getOfferComponentCoordinates;
      extendedOfferList.value[index as number] = {};
      let flatArr =
        impactAnalysisStore.getComponentToResourceCoordinates.flat(2);
      extendedOfferList.value.forEach((item, indexResourceList) => {
        if (!item) {
          return;
        } else {
          item.topPosition = flatArr[indexResourceList as number];
        }
      });
      reUpdateCoordinatesForOffer();
      impactAnalysisStore.actionOfferComponentCoordinates(index);
      impactAnalysisStore.removeComponentToOfferCoordinates(index);
      impactAnalysisStore.removeOfferCoordinates(index);
      impactAnalysisStore.removeOfferComponentCoordinates(index);
      canvasListOffCom.value = cloneDeep(
        impactAnalysisStore.getOfferComponentCoordinates
      );
    });
  }
};
const reUpdateCoordinatesForResource = () => {
  const flagArr = impactAnalysisStore.getComponentToOfferCoordinates.flat(2);
  impactAnalysisStore.getComponentToResourceCoordinates.forEach(
    (item, index) => {
      if (!item.length) {
        return;
      }
      let newValue = flagArr[index as number];
      impactAnalysisStore.setComponentToResourceCoordinates(
        [[newValue]],
        index
      );
    }
  );
};
const reUpdateCoordinatesForOffer = () => {
  const flagArr = impactAnalysisStore.getComponentToResourceCoordinates.flat(2);
  impactAnalysisStore.getComponentToOfferCoordinates.forEach((item, index) => {
    if (!item.length) {
      return;
    }
    let newValue = flagArr[index as number];
    impactAnalysisStore.setComponentToOfferCoordinates([[newValue]], index);
  });
};

const sortList = (itemList) => {
  const sortOrder = [
    TYPE_PRODUCT.CHARACTERISTICS,
    TYPE_PRODUCT.SERVICE,
    TYPE_PRODUCT.BENEFIT,
    TYPE_PRODUCT.PRICE,
  ];
  let sortedList = [...itemList];
  sortedList.sort((perious, next) => {
    return (
      sortOrder.indexOf(perious.trgtTypeName) -
      sortOrder.indexOf(next.trgtTypeName)
    );
  });
  return sortedList;
};

const resetData = (type) => {
  if (type === TARGET_TYPE.OFFER) {
    extendedOfferList.value = [];
  } else if (type === TARGET_TYPE.RESOURCE) {
    extendedResourceList.value = [];
  }
};

provide("handleSetExtendedList", setExtendedList);
provide("handleAddExtendedList", addItemToExtendedList);
provide("handleRemoveExtendedList", removeItemToExtendedList);
provide("handleResetData", resetData);
</script>
<style lang="scss">
.offers-section,
.component-section,
.resource-section {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.catalog-container {
  position: relative;
  padding-left: 24px;
  padding-right: 24px;
  display: flex;
  margin: auto;
}

.card-item-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.focus-item-list {
  gap: 40px !important;
}

.extended-list {
  gap: 24px;
}

.parent-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.card-nested-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.list-description-title {
  color: #6b6d70;
  font-size: 14px;
  font-weight: 500;
  line-height: 21px;
  letter-spacing: 0.25px;
  height: 21px;
}

.single-item {
  width: 369px;
  height: 68px;
  padding: 12px;
  display: flex;
  justify-content: center;
  align-items: center;
  border-color: #ffffff;
  box-shadow: 1px 1px 12px 0px #0000001f;
  box-shadow: -2px -2px 24px 0px #0000000a inset;
  background: linear-gradient(130.95deg, #fcfdff 3.07%, #ebeef5 94.84%);
  position: relative;
}

.mt-30 {
  margin-top: 30px;
}

.description-container {
  background-color: #f0f2f5;
  padding-bottom: 16px;
}

.description-wrapper {
  background-color: #ffffff;
  padding: 0px 12px;
}

.description-panel {
  padding: 0px 12px;
  background-color: #f7f8fa;
  border-radius: 0px 0px 12px 12px;
  box-shadow: 0px 2px 16px 0px #0000001f;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.description-title {
  color: #6b6d70;
  text-align: center;
  font-weight: 500;
  font-size: 13px;
  letter-spacing: 0.25px;
  line-height: 19.5px;
  width: 100%;
}

.active-panel {
  background: #ba1642;
  .description-title {
    color: #fff !important;
  }
}
.grip-custom-scroll {
  height: calc(100vh - 230px);
}
.empty-card {
  display: flex;
  justify-content: center;
  align-items: center;
  background: #f7f8fa;
  height: 64px;
  border: 1px solid #e6e9ed;
  border-radius: 8px;
  color: #6b6d70;
  font-weight: 500;
  font-size: 11px;
}
</style>
