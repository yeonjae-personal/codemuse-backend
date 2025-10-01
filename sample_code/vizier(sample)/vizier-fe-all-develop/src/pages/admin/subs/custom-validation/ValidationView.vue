<template>
  <div class="relative flex-1 rounded-lg">
    <div class="flex flex-col h-full">
      <div class="flex justify-between items-center">
        <div class="flex align-center gap-2">
          <h1 class="font-medium text-base text-text-base tracking-[0.5px]">
            {{ $t("product_platform.custom_validation") }}
          </h1>
        </div>
        <div class="items-end">
          <span class="text-[13px] font-normal align-top text-[#6b6d70] pr-5"
            >{{ current + 1 }}/{{ totalSlide }}</span
          >
          <button class="btn-close">
            <CloseDialogIcon @click="closeDialog()" />
          </button>
        </div>
      </div>
      <div class="custom-validation-content">
        <div class="drop-item-area">
          <div class="drop-item">
            <div class="drop-item-header condition-item">
              {{ $t("product_platform.condition") }}
            </div>
          </div>
          <div class="drop-item">
            <div class="drop-item-header action-item">
              {{ $t("product_platform.action") }}
            </div>
          </div>
        </div>

        <div
          class="custom-validation-slide bg-white max-w-[888px] rounded-[12px]"
        >
          <div ref="sliderRef" class="keen-slider">
            <div
              v-for="(item, index) in customValidationItemsView"
              :key="item.id"
              class="keen-slider__slide"
              :style="{ height: `${heights[index]}px` }"
            >
              <div class="list-attributes">
                <ValidationViewItem :key="index" :index="index" :item="item" />
              </div>
            </div>
          </div>
          <CustomValidationPrevIcon
            v-if="slider && !hidePrevButton"
            class="z-[999]"
            :class="{
              arrow: true,
              'arrow--left': true,
              'arrow--disabled': current === 0,
            }"
            @click="slider.prev()"
          />
          <CustomValidationNextIcon
            v-if="slider && !hideNextButton"
            :class="{
              arrow: true,
              'arrow--right': true,
              'arrow--disabled':
                current === slider.track.details.slides.length - 1,
            }"
            @click="slider.next()"
          />
          <div class="right-section"></div>
          <div class="left-section"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import KeenSlider from "keen-slider";
import "keen-slider/keen-slider.min.css";
import ValidationViewItem from "./ValidationViewItem.vue";
import customValidationStore from "@/store/admin/customValidation.store";
import CustomValidationNextIcon from "@/components/prod/icons/CustomValidationNextIcon.vue";
import CustomValidationPrevIcon from "@/components/prod/icons/CustomValidationPrevIcon.vue";
const { customValidationItemsView } = storeToRefs(customValidationStore());
const sliderRef = ref();
const slider = ref();
const current = ref(0);
const totalSlide = ref(0);
const emit = defineEmits(["close-dialog", "change-slide"]);
const closeDialog = () => {
  emit("close-dialog");
};

const heights = computed(() => {
  return customValidationItemsView.value.map((item) => {
    const countItem = Math.max(item.conditions.length, item.actions.length);
    return countItem * 120 + 8;
  });
});

const initializeSlider = () => {
  const sliderElement = document.querySelector(".custom-validation-slide");

  if (sliderElement) {
    slider.value = new KeenSlider(sliderRef.value, {
      initial: current.value,
      loop: false,
      drag: false,
      slides: {
        perView: 1,
      },
      slideChanged: (slide: any) => {
        const index = slide.track.details.rel;
        current.value = index;
        slide.container.style.height = heights.value[index as number] + "px";
        emit("change-slide", index);
      },
      created(slide: any) {
        totalSlide.value = slide.track.details.slides.length;
        slide.container.style.height =
          slide.slides[slide.track.details.rel].offsetHeight + "px";
      },
    });
  }
};

const hideNextButton = computed(() => {
  return totalSlide.value === 1 || current.value === totalSlide.value - 1;
});
const hidePrevButton = computed(() => {
  return totalSlide.value === 1 || current.value === 0;
});
onMounted(() => {
  initializeSlider();
});
</script>
<style scoped lang="scss">
.custom-validation-content {
  margin-top: 10px;
  height: 100%;
  font-family: "Noto Sans KR";
  .drop-item-area {
    display: flex;
    column-gap: 24px;
    justify-content: space-between;
    margin-bottom: 16px;
    .drop-item {
      display: flex;
      flex-direction: column;
      row-gap: 16px;
      .drop-item-header {
        border-radius: 0 0 12px 12px;
        border-top: 2px solid #4054b2;
        background: #f7f8fa;
        height: 48px;
        width: 432px;
        display: flex;
        justify-content: center;
        align-items: center;
        box-shadow: 0px 2px 16px 0px #0000001f;
        font-size: 13px;
        font-weight: 500;
        color: #6b6d70;
      }
      .action-item {
        border-top-color: #d9325a;
      }
    }
  }
  .list-attributes {
    display: flex;
    flex-direction: column;
    row-gap: 24px;
    // height: calc(100dvh - 645px);
    overflow-y: auto;
    // padding: 20px 0;

    &::-webkit-scrollbar {
      width: 0;
    }
    &::-webkit-scrollbar-thumb {
      background: #dce0e5;
      border-radius: 999px;
    }
    &::-webkit-scrollbar-track {
      background: transparent;
    }
  }
}

.custom-validation-slide {
  position: relative;
}

.right-section {
  position: absolute;
  right: 0;
  top: 0;
  width: 45px;
  height: 100%;
  background: #fff;
  z-index: 99;
  border-radius: 0 12px 12px 0;
}

.left-section {
  position: absolute;
  left: 0;
  top: 0;
  width: 45px;
  height: 100%;
  background: #fff;
  z-index: 99;
  border-radius: 12px 0 0 12px;
}

.arrow {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  cursor: pointer;
  z-index: 100;
}

.arrow--left {
  left: 2px;
  opacity: 0.5;
  transition: opacity 0.3s ease;
}

.arrow--left:hover,
.arrow--right:hover {
  opacity: 1;
}

.arrow--right {
  right: 2px;
  opacity: 0.5;
  transition: opacity 0.3s ease;
}
.arrow--right.arrow-visible {
  opacity: 1;
}
.condition-attributes {
  display: flex;
  flex-direction: column;
  row-gap: 16px;
  min-height: 140px;
  .text-div {
    font-size: 13px;
    font-weight: 500;
    line-height: 20px;
    text-align: center;
    text-transform: capitalize;
    color: #6b6d70;
  }
}
.keen-slider {
  border-radius: 12px;
}
</style>
