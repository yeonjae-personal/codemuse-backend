<template>
  <v-card
    rounded="lg"
    class="flex upload-image-wrapper w-[410px] h-[250px] !shadow-none"
  >
    <v-card-text
      class="flex justify-between w-[410px] h-[250px] relative overflow-hidden mt-0 !p-0"
    >
      <div class="w-[410px] h-[220px] keen-slider-wrapper">
        <div
          ref="sliderRef"
          class="keen-slider w-[410px] h-[220px] border rounded-lg"
        >
          <div
            v-for="(image, index) in uploadedImagesExtend.requests"
            :key="index"
            class="keen-slider__slide mySlide"
            :class="
              (`number-slide${index}`, { active: activeSlideIndex === index })
            "
          >
            <div v-if="image.imagePath" class="wraper-img">
              <img
                :src="image.imagePath"
                :alt="`Image ${index + 1}`"
                class="!h-[220px]"
              />
            </div>
          </div>
        </div>
        <svg
          v-if="sliderInstance"
          :class="{
            arrow: true,
            'arrow--left': true,
            'arrow--disabled': activeSlideIndex === 0,
          }"
          width="32"
          height="32"
          viewBox="0 0 72 72"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
          @click="goToPrevSlide"
        >
          <g filter="url(#filter0_bd_8924_8392)">
            <path
              d="M8 32C8 16.536 20.536 4 36 4C51.464 4 64 16.536 64 32C64 47.464 51.464 60 36 60C20.536 60 8 47.464 8 32Z"
              fill="url(#paint0_linear_8924_8392)"
            />
            <path
              d="M9 32C9 17.0883 21.0883 5 36 5C50.9117 5 63 17.0883 63 32C63 46.9117 50.9117 59 36 59C21.0883 59 9 46.9117 9 32Z"
              stroke="white"
              stroke-opacity="0.64"
              stroke-width="2"
            />
            <path
              fill-rule="evenodd"
              clip-rule="evenodd"
              d="M41.5607 21.9393C42.1464 22.5251 42.1464 23.4749 41.5607 24.0607L33.6213 32L41.5607 39.9393C42.1464 40.5251 42.1464 41.4749 41.5607 42.0607C40.9749 42.6464 40.0251 42.6464 39.4393 42.0607L30.4393 33.0607C29.8536 32.4749 29.8536 31.5251 30.4393 30.9393L39.4393 21.9393C40.0251 21.3536 40.9749 21.3536 41.5607 21.9393Z"
              fill="white"
            />
          </g>
          <defs>
            <filter
              id="filter0_bd_8924_8392"
              x="0"
              y="-4"
              width="72"
              height="76"
              filterUnits="userSpaceOnUse"
              color-interpolation-filters="sRGB"
            >
              <feFlood flood-opacity="0" result="BackgroundImageFix" />
              <feGaussianBlur in="BackgroundImageFix" stdDeviation="4" />
              <feComposite
                in2="SourceAlpha"
                operator="in"
                result="effect1_backgroundBlur_8924_8392"
              />
              <feColorMatrix
                in="SourceAlpha"
                type="matrix"
                values="0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 127 0"
                result="hardAlpha"
              />
              <feOffset dy="4" />
              <feGaussianBlur stdDeviation="4" />
              <feComposite in2="hardAlpha" operator="out" />
              <feColorMatrix
                type="matrix"
                values="0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0.08 0"
              />
              <feBlend
                mode="normal"
                in2="effect1_backgroundBlur_8924_8392"
                result="effect2_dropShadow_8924_8392"
              />
              <feBlend
                mode="normal"
                in="SourceGraphic"
                in2="effect2_dropShadow_8924_8392"
                result="shape"
              />
            </filter>
            <linearGradient
              id="paint0_linear_8924_8392"
              x1="8"
              y1="7.70588"
              x2="62.7501"
              y2="55.22"
              gradientUnits="userSpaceOnUse"
            >
              <stop stop-color="#E2E8F5" />
              <stop offset="1" stop-color="#ADB9D0" />
            </linearGradient>
          </defs>
        </svg>
        <svg
          v-if="sliderInstance"
          :class="{
            arrow: true,
            'arrow--right': true,
            'arrow--disabled':
              activeSlideIndex ===
              sliderInstance.track.details.slides.length - 1,
          }"
          width="32"
          height="32"
          viewBox="0 0 72 72"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
          @click="goToNextSlide"
        >
          <g filter="url(#filter0_bd_8924_8389)">
            <path
              d="M8 32C8 16.536 20.536 4 36 4C51.464 4 64 16.536 64 32C64 47.464 51.464 60 36 60C20.536 60 8 47.464 8 32Z"
              fill="url(#paint0_linear_8924_8389)"
            />
            <path
              d="M9 32C9 17.0883 21.0883 5 36 5C50.9117 5 63 17.0883 63 32C63 46.9117 50.9117 59 36 59C21.0883 59 9 46.9117 9 32Z"
              stroke="white"
              stroke-opacity="0.64"
              stroke-width="2"
            />
            <path
              fill-rule="evenodd"
              clip-rule="evenodd"
              d="M30.4393 21.9393C31.0251 21.3536 31.9749 21.3536 32.5607 21.9393L41.5607 30.9393C42.1464 31.5251 42.1464 32.4749 41.5607 33.0607L32.5607 42.0607C31.9749 42.6464 31.0251 42.6464 30.4393 42.0607C29.8536 41.4749 29.8536 40.5251 30.4393 39.9393L38.3787 32L30.4393 24.0607C29.8536 23.4749 29.8536 22.5251 30.4393 21.9393Z"
              fill="white"
            />
          </g>
          <defs>
            <filter
              id="filter0_bd_8924_8389"
              x="0"
              y="-4"
              width="72"
              height="76"
              filterUnits="userSpaceOnUse"
              color-interpolation-filters="sRGB"
            >
              <feFlood flood-opacity="0" result="BackgroundImageFix" />
              <feGaussianBlur in="BackgroundImageFix" stdDeviation="4" />
              <feComposite
                in2="SourceAlpha"
                operator="in"
                result="effect1_backgroundBlur_8924_8389"
              />
              <feColorMatrix
                in="SourceAlpha"
                type="matrix"
                values="0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 127 0"
                result="hardAlpha"
              />
              <feOffset dy="4" />
              <feGaussianBlur stdDeviation="4" />
              <feComposite in2="hardAlpha" operator="out" />
              <feColorMatrix
                type="matrix"
                values="0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0.08 0"
              />
              <feBlend
                mode="normal"
                in2="effect1_backgroundBlur_8924_8389"
                result="effect2_dropShadow_8924_8389"
              />
              <feBlend
                mode="normal"
                in="SourceGraphic"
                in2="effect2_dropShadow_8924_8389"
                result="shape"
              />
            </filter>
            <linearGradient
              id="paint0_linear_8924_8389"
              x1="8"
              y1="7.70588"
              x2="62.7501"
              y2="55.22"
              gradientUnits="userSpaceOnUse"
            >
              <stop stop-color="#E2E8F5" />
              <stop offset="1" stop-color="#ADB9D0" />
            </linearGradient>
          </defs>
        </svg>
        <div v-if="sliderInstance" class="dots">
          <button
            v-for="idx in dotHelper"
            :key="idx"
            :class="{ dot: true, active: idx === activeSlideIndex }"
          ></button>
        </div>
      </div>
    </v-card-text>
  </v-card>
</template>

<script setup lang="ts">
import { UI_DASHBOARD } from "@/api/prod/path";
import { userImagesStore } from "@/store/userImagesStore";
import { httpClient } from "@/utils/http-common";
import KeenSlider from "keen-slider";
import "keen-slider/keen-slider.min.css";

const activeSlideIndex = ref(
  Number(localStorage.getItem("activeSlideIndex")) || 0
);
const activeSlide = ref<any>(0);
const imageStore = userImagesStore();
const { uploadedImagesExtend } = imageStore;
const sliderRef = ref<any>(null);
const sliderInstance = ref<any>(null);
const currentSlide = ref(0);
const perViewMode = ref(1);

const fetchData = async () => {
  try {
    const response = await httpClient.get(`${UI_DASHBOARD}/userimage`, {
      params: {
        dsbdViewUuid: imageStore.dsbdViewUuid,
      },
    });
    const result = response.data;
    imageStore.setUploadedImagesExtend(result);
  } catch (error) {
    console.error("Error fetching data:", error);
  }
};

onMounted(() => {
  fetchData();
  sliderInstance.value = new KeenSlider(sliderRef.value, {
    initial: activeSlideIndex.value,
    loop: true,
    drag: false,
    mode: "free-snap",
    slides: {
      origin: "center",
      perView: perViewMode.value,
      spacing: 30,
    },
    slideChanged(slide: any) {
      currentSlide.value = slide?.track?.details.rel;
      activeSlide.value = slide?.track?.details.rel;
      activeSlideIndex.value = slide?.track?.details.rel;
      localStorage.setItem("activeSlideIndex", activeSlide.value as string);
      imageStore.setActiveSlideIndex(
        Number(localStorage.getItem("activeSlideIndex"))
      );
    },
  });
});
const initializeSlider = () => {
  const activeSlideIndexTemp = ref(
    Number(localStorage.getItem("activeSlideIndex")) || 0
  );
  sliderInstance.value = new KeenSlider(sliderRef.value, {
    initial: activeSlideIndexTemp.value,
    loop: true,
    mode: "free-snap",
    slides: {
      origin: "center",
      perView: perViewMode.value,
      spacing: 30,
    },
    slideChanged(slider: any) {
      currentSlide.value = slider?.track.details.rel;
      activeSlide.value = slider?.track.details.rel;
      activeSlideIndex.value = slider?.track.details.rel;
      localStorage.setItem("activeSlideIndex", activeSlide.value);
      imageStore.setActiveSlideIndex(
        Number(localStorage.getItem("activeSlideIndex"))
      );
    },
  });
  activeSlideIndex.value = activeSlideIndexTemp.value;
};
function goToPrevSlide() {
  if (sliderInstance.value) sliderInstance.value.prev();
}

function goToNextSlide() {
  if (sliderInstance.value) sliderInstance.value.next();
}

const dotHelper = computed(() =>
  sliderInstance.value
    ? [...Array(sliderInstance.value.track.details.slides.length).keys()]
    : []
);
watch(
  () => imageStore.activeSlideIndex,
  () => {
    initializeSlider();
  }
);

defineExpose({
  initializeSlider,
});
</script>

<style scoped>
:deep(.swiper-pagination-bullets.swiper-pagination-horizontal) {
  bottom: 30px;
}
:deep(.swiper-pagination-bullet-active) {
  background-color: #d9325a;
  width: 24px;
  height: 8px;
  border-radius: 5px;
}
.mySlide {
  width: 100%;
  height: 228px;
}

:deep(.clearButton) {
  z-index: 10;
  display: none;
}

:deep(.mainMessage) {
  font-size: 16px;
  font-weight: 500;
  color: #3a3b3d;
}
:deep(.container) {
  box-shadow: 0px 2px 40px 0px grey;
  border: none;
  padding: 0;
  border-radius: 0px;
  position: unset;
}
:deep(.v-card-text) {
  margin-top: 20px;
}
:deep(.popup-card .v-card-text) {
  padding: 0px !important;
}
:deep(.v-btn__content) {
  font-size: 13px;
  font-weight: 500;
}
:deep(.upload-image-wrapper .v-btn--size-default) {
  font-size: 13px !important;
}
.v-card-title {
  font-size: 16px;
  color: #3a3b3d;
}
[class^="number-slide"],
[class*=" number-slide"] {
  background: grey;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 50px;
  color: #fff;
  font-weight: 500;
  height: 300px;
  max-height: 100vh;
}

:deep(.keen-slider__slide.active .arrow) {
  display: none;
}

:deep(.keen-slider__slide.active) {
  min-width: 410px !important;
  max-width: 410px !important;
}

.keen-slider__slide img {
  width: 100%;
  height: auto;
  object-fit: contain;
  aspect-ratio: 16 / 9;
}
.arrow {
  position: absolute;
  top: 50%;
  width: 32px;
  height: 32px;
  fill: #fff;
  cursor: pointer;
  transform: translateY(-50%);
}

.arrow--left {
  left: 0px;
  /* opacity: 0.4; */
}
/* .arrow--left:hover {
  opacity: 1;
} */

.arrow--right {
  right: 0px;
  /* opacity: 0.4; */
}
/* .arrow--right:hover {
  opacity: 1;
} */
.arrow {
  fill: none;
}
.arrow--disabled {
  fill: rgba(255, 255, 255, 0.5);
}
.keen-slider-wrapper .arrow--left {
  opacity: 0;
}
.keen-slider-wrapper:hover .arrow--left {
  opacity: 1;
}
.keen-slider-wrapper .arrow--right {
  opacity: 0;
}
.keen-slider-wrapper:hover .arrow--right {
  opacity: 1;
}
.dots {
  display: flex;
  justify-content: center;
  padding-top: 17px;
}

.dot {
  border: none;
  width: 6px;
  height: 6px;
  background: #e6e9ed;
  border-radius: 50%;
  margin: 0 5px;
  cursor: default;
}

.dot.active {
  background: #ba1642;
  width: 24px;
  height: 6px;
  border-radius: 8px;
}

.active {
  box-shadow: 0px 2px 40px 0px rgba(0, 0, 0, 0.12);
  opacity: 1;
}
.grid-item-child-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 50px;
}
.grid-item-child-header .icon-left {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 18px;
  font-weight: 700;
  text-transform: capitalize;
}
.cancel-btn {
  border: 1px solid var(--border-border-base, rgba(220, 224, 229, 1));
}
.icon-right {
  margin-left: 10px;
  font-size: 18px;
  display: flex;
  justify-content: space-between;
  padding: 10px;
}
</style>
