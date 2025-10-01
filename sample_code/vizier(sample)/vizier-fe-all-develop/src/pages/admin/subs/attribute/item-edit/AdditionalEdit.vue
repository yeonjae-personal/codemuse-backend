<template>
  <div class="additional-tab">
    <LocomotiveComponent
      v-show="isMounted"
      :scroll-top-content-class="['flex justify-center']"
      scroll-container-class="pl-[10px]"
      scroll-content-class="grow"
      dynamic-scroll-key="EDIT_ADDITIONAL_ATTRIBUTE_TAB"
      :number-scroll-y="currentY"
      :is-scroll-when-add-new="isScrollWhenAddNew"
      is-dynamic-scroll
    >
      <div class="additional-wrapper">
        <VueDraggable
          v-model="itemDetail.additionalInfo"
          handle=".item-draggable"
          class="draggable-wrapper"
          easing="ease-in"
          animation="200"
        >
          <FieldItemEdit
            v-for="(item, index) in itemDetail.additionalInfo"
            :key="item.id"
            :ref="`additional-${item.id}`"
            :item="item"
            :order="index + 1"
          />
        </VueDraggable>
        <div ref="target" class="add-new-section">
          <button class="add-new-button" @click="handleAddNewAdditionalItem">
            <PlusLargeIcon />
          </button>
        </div>
      </div>
    </LocomotiveComponent>
    <div v-if="!targetIsVisible" class="sticky-button">
      <button class="add-new-button" @click="handleAddNewAdditionalItem">
        <PlusLargeIcon />
      </button>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { VueDraggable } from "vue-draggable-plus";
import FieldItemEdit from "./FieldItemEdit.vue";
import PlusLargeIcon from "@/components/prod/icons/PlusLargeIcon.vue";
import attributeManagementStore from "@/store/admin/attributeManagement.store";
import { useIntersectionObserver } from "@vueuse/core";

const target = ref<HTMLDivElement>();
const currentY = ref<number>(0);
const targetIsVisible = ref(false);
const isMounted = ref<boolean>(false);
const isScrollWhenAddNew = ref<boolean>(false);
const instance = getCurrentInstance();

useIntersectionObserver(target, ([entry]) => {
  targetIsVisible.value = entry?.isIntersecting || false;
});

const { itemDetail } = storeToRefs(attributeManagementStore());
const { addAdditionalItem } = attributeManagementStore();

const handleAddNewAdditionalItem = () => {
  isScrollWhenAddNew.value = false;
  const newItem = addAdditionalItem();
  nextTick(() => {
    isScrollWhenAddNew.value = true;
    const element = instance?.proxy?.$refs[`additional-${newItem.id}`]![0]?.$el;
    currentY.value = element.offsetTop + 350;
  });
};

onMounted(() => {
  nextTick(() => {
    isMounted.value = true;
  });
});
</script>
<style lang="scss" scoped>
.additional-tab {
  height: calc(100vh - 290px);
  overflow-y: auto;
  overflow-x: hidden;
  &::-webkit-scrollbar {
    display: none;
  }

  .sticky-button {
    position: absolute;
    bottom: 5px;
    left: 186px;
    // left: 50.9%;
    // transform: translateX(-50.9%);
    .add-new-button {
      width: 40px;
      height: 40px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      box-shadow:
        2px 1px 6px 0px #1e265b3d,
        2px 1px 18px 0px #1518421c inset,
        -5px -2px 6px 0px #ffffffa3 inset;
    }
  }
}
.additional-wrapper {
  display: flex;
  flex-direction: column;
  row-gap: 16px;
  padding-left: 6px;
  .add-new-section {
    display: flex;
    align-items: center;
    justify-content: center;
    padding-bottom: 5px;
    .add-new-button {
      width: 40px;
      height: 40px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      box-shadow:
        2px 1px 6px 0px #1e265b3d,
        2px 1px 18px 0px #1518421c inset,
        -5px -2px 6px 0px #ffffffa3 inset;
    }
  }
}

.draggable-wrapper {
  display: flex;
  flex-direction: column;
  row-gap: 16px;
}
</style>
