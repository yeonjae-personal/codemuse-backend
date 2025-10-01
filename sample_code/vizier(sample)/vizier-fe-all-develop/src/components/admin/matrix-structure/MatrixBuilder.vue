<template>
  <div
    class="matrix-builder-container"
    :class="[{ 'open-down border-1': isBuilder }]"
  >
    <div
      class="flex justify-between align-center px-3 py-2 h-[48px] bg-lighter rounded-t-[16px] border-b border-[#DCE0E5]"
    >
      <span class="text-[13px] text-[#3A3B3D] font-medium">{{
        $t("product_platform.matrixBuilder")
      }}</span>
      <CloseSmallIcon class="cursor-pointer" @click="closeBulder" />
    </div>
    <div class="flex py-4 px-3">
      <div class="flex-grow-1">
        <GridLayout
          v-if="matrixBuilderFactors?.length"
          ref="gridLayoutRef"
          v-model:layout="matrixBuilderFactors"
          :col-num="builderFactorCols"
          :is-resizable="false"
          :row-height="32"
          :margin="[16, 16]"
          :is-draggable="allowDrag"
          class=""
        >
          <GridItem
            v-for="(factor, index) in matrixBuilderFactors"
            :key="factor.i"
            :x="factor.x"
            :y="factor.y"
            :w="factor.w"
            :h="factor.h"
            :i="factor.i"
            :static="factor?.static"
            :drag-option="{
              axis: 'x',
            }"
            class="factor-drag"
            drag-allow-from=".vue-draggable-handle"
            drag-ignore-from=".factor-no-drag"
            @move="handleMoveFactor"
          >
            <MatrixBuilderItem
              :data="factor"
              :actions="listActions(factor)"
              :is-open="factor?.isOpen"
              :is-open-action-box="factor?.openActions"
              :active="factor?.isActive"
              editable
              @open-options="handleOpenAction($event, factor)"
              @on-open="handleOpenValueList($event, factor)"
              @change-factor-value="handleUpdateFactorValue($event, index)"
            />
          </GridItem>
        </GridLayout>
      </div>
      <div class="flex flex-column w-[auto] align-end gap-4">
        <ItemDrop
          class-name="h-[40px]"
          @click.stop="handleOpenFactorSearch"
          @drop="drop($event)"
          @dragover="allowDrop($event)"
        >
          {{ t("product_platform.dragFactorHere") }}
        </ItemDrop>
        <BaseButton :color="ButtonColorType.Secondary" @click="handleBuilder">
          {{ $t("product_platform.build") }}
        </BaseButton>
      </div>
    </div>
  </div>
  <base-popup
    v-model="openPopupBuild"
    :icon="DialogIconType.Warning"
    :submit-button-text="$t('product_platform.btn_yes')"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="$t('product_platform.buildMessage')"
    @on-close="
      () => {
        openPopupBuild = false;
      }
    "
    @on-submit="handleBuild"
  />
</template>

<!-- eslint-disable id-length -->
<script setup lang="ts">
import useMatrixStructureStore from "@/store/admin/matrixStructure.store";
import TrashIcon from "@/components/prod/icons/TrashIcon.vue";
import { useI18n } from "vue-i18n";
import { useDragStore, useSnackbarStore } from "@/store";
import cloneDeep from "lodash-es/cloneDeep";
import { GridItem, GridLayout } from "grid-layout-plus";
import { ButtonColorType, DialogIconType } from "@/enums";

const { dragOfferType } = storeToRefs(useDragStore());
const matrixStructureStore = useMatrixStructureStore();
const useSnackbar = useSnackbarStore();
const {
  isBuilder,
  isShowFactorSearch,
  matrixBuilderFactors,
  matrixSelected,
  headersTableMatrix,
  builderFactorCols,
} = storeToRefs(matrixStructureStore);
const { t } = useI18n();

const dropData = ref();
const openPopupBuild = ref(false);
const positionEndItem = ref<any>(null);
const allowDrag = ref(true);

const listActions = (factor) => {
  return [
    {
      name: t("product_platform.actionRemove"),
      icon: TrashIcon,
      iconProps: {
        class: "text-text-lighter",
      },
      onClick: () => {
        matrixBuilderFactors.value = matrixBuilderFactors.value.filter(
          (item: any) => item?.factorCode !== factor?.factorCode
        );
        updateData();
      },
    },
  ];
};

const buildHeight = computed(() => {
  return (
    80 +
    45 *
      Math.ceil(matrixBuilderFactors.value.length / builderFactorCols.value) +
    "px"
  );
});

const closeBulder = () => {
  isBuilder.value = false;
  openPopupBuild.value = false;
  isShowFactorSearch.value = false;
};

const handleOpenFactorSearch = () => {
  isShowFactorSearch.value = true;
};

const drop = (event: any) => {
  event.preventDefault();
  dropData.value = event.dataTransfer.getData("item")
    ? JSON.parse(event.dataTransfer.getData("item"))
    : null;
  if (dropData.value) {
    if (dropData.value?.factorValueLst?.length) {
      const addFactor = {
        seqNo: matrixBuilderFactors.value?.length + 1,
        factorCode: dropData.value.factorCode,
        factorName: dropData.value.factorName,
        factorValues: dropData.value.factorValueLst.map((value) => ({
          ...value,
          inUse: true,
        })),
        x: matrixBuilderFactors.value.length % builderFactorCols.value,
        y: Math.ceil(
          matrixBuilderFactors.value.length / builderFactorCols.value
        ),
        w: 1,
        h: 1,
        i: dropData.value.factorCode,
        loading: false,
      };
      matrixBuilderFactors.value.push(addFactor);
    } else {
      useSnackbar.showSnackbar(
        t("product_platform.factorLengthIsNull"),
        "error"
      );
    }
  }
};

const handleBuild = async () => {
  const param = cloneDeep(matrixBuilderFactors.value).map((item, index) => ({
    seqNo: index,
    factorCode: item.factorCode,
    factorName: item.factorName,
    factorValues: item.factorValues,
  }));
  matrixStructureStore.getListTableMatrix(matrixSelected.value?.matrixCode, {
    builderDtos: param,
  });

  headersTableMatrix.value = [
    ...cloneDeep(matrixBuilderFactors.value).map((factor) => ({
      ...factor,
      factorValues: factor.factorValues.filter((item) => item.inUse),
    })),
    {
      seqNo: cloneDeep(matrixBuilderFactors.value).length + 1,
      factorCode: "VALUE",
      factorName: "value",
    },
  ];

  closeBulder();
};
const handleMoveFactor = (i, newX, newY) => {
  const currentItem: any = matrixBuilderFactors.value.find(
    (item: any) => item.i === i
  );
  const swappedItem: any = matrixBuilderFactors.value.find(
    (item: any) => item.x === newX && item.y === newY
  );
  if (!swappedItem) {
    updateData();
  }
  if (currentItem && swappedItem) {
    const tempX = swappedItem.x;
    const tempY = swappedItem.y;
    swappedItem.x = currentItem.x;
    swappedItem.y = currentItem.y;
    currentItem.x = tempX;
    currentItem.y = tempY;
    matrixBuilderFactors.value = matrixBuilderFactors.value.sort((a, b) => {
      if (a.y === b.y) {
        return a.x - b.x;
      }
      return a.y - b.y;
    });
  }
};

const allowDrop = (event) => {
  if (dragOfferType.value !== "factor") {
    return true;
  }
  event.preventDefault();
};
const updateData = () => {
  matrixBuilderFactors.value = matrixBuilderFactors.value.map(
    (item: any, index) => ({
      ...item,
      x: index % builderFactorCols.value,
      y: Math.ceil(index / builderFactorCols.value),
      w: 1,
      h: 1,
      i: item.factorCode,
      loading: false,
    })
  );
  positionEndItem.value =
    matrixBuilderFactors.value[matrixBuilderFactors.value?.length - 1];
};
const handleOpenValueList = (event, factor) => {
  matrixBuilderFactors.value = matrixBuilderFactors.value.map((item) => {
    if (item?.factorCode === factor?.factorCode) {
      return {
        ...item,
        isOpen: event,
        isActive: event,
        openActions: false,
      };
    }
    return { ...item, isOpen: false, isActive: false, openActions: false };
  });
};
const handleUpdateFactorValue = (event, index) => {
  matrixBuilderFactors.value = matrixBuilderFactors.value.map(
    (item, factorIndex) => {
      if (factorIndex === index) {
        return {
          ...item,
          factorValues: cloneDeep(event),
        };
      }
      return item;
    }
  );
};
const handleOpenAction = (event, factor) => {
  matrixBuilderFactors.value = matrixBuilderFactors.value.map((item) => {
    if (item?.factorCode === factor?.factorCode) {
      return {
        ...item,
        openActions: event,
      };
    }
    return { ...item, openActions: false, isOpen: false };
  });
  allowDrag.value = !event;
};
const handleBuilder = () => {
  const validBuilder = checkBuilderValid();
  if (validBuilder) {
    useSnackbar.showSnackbar(
      t("product_platform.builder_invalid_msg"),
      "error"
    );
    return;
  }
  openPopupBuild.value = true;
};
const checkBuilderValid = () => {
  let checkInUse = false;
  matrixBuilderFactors.value.forEach((item) => {
    if (!item.factorValues?.some((value) => value.inUse)) {
      checkInUse = true;
    }
  });
  return checkInUse;
};
watch(
  () => builderFactorCols.value,
  () => {
    updateData();
  },
  { immediate: true }
);
watch(
  () => matrixBuilderFactors.value,
  (val) => {
    val.forEach((item) => {
      if (item?.isOpen) {
        allowDrag.value = false;
        return;
      }
      allowDrag.value = true;
    });
  },
  { deep: true }
);
onMounted(() => {
  updateData();
});
</script>

<style lang="scss" scoped>
.matrix-builder-container {
  width: 100%;
  height: 0px;
  min-height: 0px;
  position: relative;
  overflow: hidden;
  border-radius: 16px;
  border-color: #dce0e5;
  box-shadow: 0px 2px 16px 0px #13185c29;
  transition: all 0.2s ease-in-out;
}
.open-down {
  min-height: 176px;
  height: v-bind(buildHeight);
}
.factor-drag {
  top: -16px;
  left: -16px;
}
</style>
