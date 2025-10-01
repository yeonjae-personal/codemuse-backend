<template>
  <div
    :class="[
      'w-full overflow-x-auto rounded-[12px] bg-darker custom-scroll custom-scroll-darker relative px-3 pb-3 box-border',
      showRightPane ? 'col-span-2' : 'col-span-3',
      {
        'border-[1px] border-primary edit-structure': showActionSave && !isAdd,
      },
    ]"
  >
    <div class="relative inline-flex min-w-full min-h-full">
      <div
        class="w-full mx-auto box-border px-5 pt-9 pb-0 flex flex-col items-center relative"
      >
        <!-- Root Card Item Wrapper -->
        <div
          :id="type ? 'rootOffer' + type : 'rootOffer'"
          class="flex justify-center relative arrow-root"
          :style="{ width: canvasWidth + 'px' }"
        >
          <RootCardItem
            :type="type"
            :class-card="
              showStructureDetail && !isAdd
                ? '!border-[1px] !border-red-500'
                : ''
            "
            :item="productDetail"
            :is-detail-view="isAdd"
            :screen="screenValue"
            :is-add="isAdd"
            :duplicate="duplicate"
            @view-detail="handleClickCard"
          />
        </div>

        <!-- Base Canvas -->
        <BaseCanvas
          v-if="listStructure?.length && productLineCoordinates"
          id="offer-searc-canvas"
          :width="canvasWidth"
          :height="120"
          :direction="CANVAS_DIRECTION.VERTICAL"
          :list-coordinates="productLineCoordinates"
        />

        <!-- Structure List Headers -->
        <div
          ref="listStructureEl"
          :class="[
            'flex gap-5 text-white !text-sm !font-medium relative flex-nowrap',
            { 'list-structure': listStructure?.length > 1 },
          ]"
        >
          <div
            v-for="(item, index) in listStructure"
            :key="'structure-' + index"
            :class="[
              'w-full relative structure-item',
              type ? 'structure-' + type : 'structure',
              {
                'opacity-30':
                  !concatValueMultiSingle(item)?.includes(dragOfferType) &&
                  isDragging,
              },
            ]"
          >
            <div
              :class="[
                'w-[230px] rounded-t-lg bg-inverse h-10 flex justify-center items-center sticky top-0 z-10 overflow-hidden',
                {
                  '!bg-inverse-light':
                    item.mctgrItemCode === TypeComponentCode.Characteristics,
                },
              ]"
            >
              {{ item.mctgrItemName }}
            </div>
          </div>
        </div>

        <!-- Offers List with LocomotiveComponent -->
        <div class="px-2">
          <LocomotiveComponent
            scroll-container-class="max-h-[calc(100vh_-_580px)]"
            scroll-content-class="flex gap-5 flex-nowrap"
          >
            <OffersType
              v-for="(item, index) in listStructure"
              :key="'offer-' + index"
              :class="{
                'opacity-30':
                  !concatValueMultiSingle(item)?.includes(dragOfferType) &&
                  isDragging,
              }"
              :is-add="isAdd"
              :is-edit="showActionSave"
              :items="item.componentList || []"
              :screen="screenValue"
              :type="type"
              :type-offer="item.mctgrItemCode"
              :single-accept-code="item.singleAcceptCode || []"
              :multiple-accept-code="concatValueMultiSingle(item)"
              :filtered-rule="filteredRules"
              :duplicate="duplicate"
              @handle-click-drop="handleClickDrop"
            />
          </LocomotiveComponent>
        </div>
      </div>

      <!-- Action Buttons -->
      <div v-if="!isAdd || !isCreatedStructure" class="absolute top-3 right-3">
        <template v-if="!showListStructure">
          <BaseButton
            v-if="isAdd && !showActionSave"
            :color="ButtonColorType.Gray"
            @click="handleAdd"
          >
            <IconAdd class="mr-[6px]" />
            {{ $t("product_platform.add") }}
          </BaseButton>

          <BaseButton
            v-if="!isAdd && !showActionSave"
            :color="ButtonColorType.Secondary"
            @click="handleEdit"
          >
            <edit-icon class="mr-[6px]" />
            {{ $t("product_platform.edit") }}
          </BaseButton>
        </template>

        <div v-if="showActionSave" class="flex gap-2 items-center">
          <BaseButton :color="ButtonColorType.Gray" @click="handleCancel">
            {{ $t("product_platform.cancel") }}
          </BaseButton>

          <BaseButton :color="ButtonColorType.Secondary" @click="handleSave">
            <save-icon class="mr-[6px]" />
            {{ $t("product_platform.save") }}
          </BaseButton>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { CANVAS_DIRECTION } from "@/constants/";
import { ButtonColorType, TypeComponentCode } from "@/enums";
import IconAdd from "@/assets/icons/plus.svg";

type Props = {
  listStructure: any;
  isDragging: boolean;
  isAdd: boolean;
  showActionSave: boolean;
  showRightPane: boolean;
  type: any;
  showStructureDetail: boolean;
  productDetail: any;
  screenValue: string;
  duplicate: boolean;
  productLineCoordinates: any;
  filteredRules: any;
  showListStructure: boolean;
  isCreatedStructure: boolean;
  dragOfferType?: string;
};

const props = defineProps<Props>();

const emit = defineEmits([
  "clickCard",
  "clickDrop",
  "add",
  "edit",
  "cancel",
  "save",
]);

const listStructureEl = ref<any>(null);

const canvasWidth = computed(() => {
  if (props.listStructure?.length) {
    return (
      230 * props.listStructure.length + (props.listStructure.length - 1) * 20
    );
  }
  return 0;
});

const concatValueMultiSingle = (item) => {
  if (item.singleAcceptCode) {
    return (item.multipleAcceptCode || [])?.concat(item.singleAcceptCode);
  }
  return item.multipleAcceptCode;
};

const handleClickCard = (): void => {
  emit("clickCard");
};

const handleClickDrop = (): void => {
  emit("clickDrop");
};

const handleAdd = (): void => {
  emit("add");
};

const handleEdit = (): void => {
  emit("edit");
};

const handleCancel = (): void => {
  emit("cancel");
};

const handleSave = (): void => {
  emit("save");
};
</script>

<style scoped>
.edit-structure {
  box-shadow: 0px 0px 0px 4px rgba(217, 50, 90, 0.16);
}
</style>
