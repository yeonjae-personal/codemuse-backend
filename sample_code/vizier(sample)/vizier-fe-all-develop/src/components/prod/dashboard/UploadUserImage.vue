<template>
  <v-card
    rounded="lg"
    class="flex upload-image-wrapper 2xl:w-[1370px] 2xl:h-[864px]"
  >
    <v-card-title
      class="d-flex justify-space-between align-center !pl-6 !pb-2 !pt-4 grid-item-child-header"
    >
      <div class="text-base font-medium color-[#3a3b3d]">
        <div class="icon-left"><UserImageIconDetail />{{ title }}</div>
      </div>
      <div class="icon-right text-base font-medium color-[#3a3b3d]">
        <button
          class="w-[96px] h-10 mr-8 bg-bg-primary flex justify-center items-center rounded-lg text-white text-[13px] leading-[13px] tracking-[0.5px] font-medium"
          @click="editImage"
        >
          <UpdateIcon class="mr-2" />
          {{ t("product_platform.dashboard.edit") }}
        </button>
        <button @click="closeDialog">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="19"
            height="20"
            viewBox="0 0 19 20"
            fill="none"
          >
            <path
              fill-rule="evenodd"
              clip-rule="evenodd"
              d="M5.77881 0.833313H13.5545C14.2924 0.833302 14.9014 0.833292 15.3975 0.873825C15.9128 0.915923 16.3864 1.00627 16.8313 1.23296C17.5212 1.58449 18.0822 2.14542 18.4337 2.83535C18.6604 3.28024 18.7507 3.75388 18.7928 4.26914C18.8334 4.76523 18.8333 5.37422 18.8333 6.1121V13.8879C18.8333 14.6257 18.8334 15.2347 18.7928 15.7308C18.7507 16.2461 18.6604 16.7197 18.4337 17.1646C18.0822 17.8545 17.5212 18.4155 16.8313 18.767C16.3864 18.9937 15.9128 19.084 15.3975 19.1261C14.9014 19.1667 14.2924 19.1667 13.5545 19.1666H5.77879C5.04091 19.1667 4.43192 19.1667 3.93583 19.1261C3.42057 19.084 2.94693 18.9937 2.50204 18.767C1.81211 18.4155 1.25118 17.8545 0.899644 17.1646C0.67296 16.7197 0.58261 16.2461 0.540512 15.7308C0.499979 15.2347 0.499989 14.6257 0.5 13.8878V6.11212C0.499989 5.37423 0.499979 4.76523 0.540512 4.26914C0.58261 3.75388 0.67296 3.28024 0.899644 2.83535C1.25118 2.14542 1.81211 1.58449 2.50204 1.23296C2.94693 1.00627 3.42057 0.915923 3.93583 0.873825C4.43192 0.833292 5.04092 0.833302 5.77881 0.833313ZM4.08512 2.70107C3.68324 2.7339 3.47772 2.79342 3.33435 2.86647C2.98939 3.04224 2.70892 3.3227 2.53316 3.66766C2.46011 3.81103 2.40059 4.01655 2.36776 4.41843C2.33405 4.83101 2.33333 5.36478 2.33333 6.14998V13.85C2.33333 14.6352 2.33405 15.1689 2.36776 15.5815C2.40059 15.9834 2.46011 16.1889 2.53316 16.3323C2.70892 16.6773 2.98939 16.9577 3.33435 17.1335C3.47772 17.2065 3.68324 17.2661 4.08512 17.2989C4.4977 17.3326 5.03147 17.3333 5.81667 17.3333H13.5167C14.3019 17.3333 14.8356 17.3326 15.2482 17.2989C15.6501 17.2661 15.8556 17.2065 15.999 17.1335C16.3439 16.9577 16.6244 16.6773 16.8002 16.3323C16.8732 16.1889 16.9327 15.9834 16.9656 15.5815C16.9993 15.1689 17 14.6352 17 13.85V6.14998C17 5.36478 16.9993 4.83101 16.9656 4.41843C16.9327 4.01655 16.8732 3.81103 16.8002 3.66766C16.6244 3.3227 16.3439 3.04224 15.999 2.86647C15.8556 2.79342 15.6501 2.7339 15.2482 2.70107C14.8356 2.66736 14.3019 2.66665 13.5167 2.66665H5.81667C5.03147 2.66665 4.4977 2.66736 4.08512 2.70107ZM6.26849 6.6018C6.62647 6.24382 7.20687 6.24382 7.56485 6.6018L9.66667 8.70362L11.7685 6.6018C12.1265 6.24382 12.7069 6.24382 13.0648 6.6018C13.4228 6.95978 13.4228 7.54018 13.0648 7.89816L10.963 9.99998L13.0648 12.1018C13.4228 12.4598 13.4228 13.0402 13.0648 13.3982C12.7069 13.7561 12.1265 13.7561 11.7685 13.3982L9.66667 11.2963L7.56485 13.3982C7.20687 13.7561 6.62647 13.7561 6.26849 13.3982C5.9105 13.0402 5.9105 12.4598 6.26849 12.1018L8.3703 9.99998L6.26849 7.89816C5.9105 7.54018 5.9105 6.95978 6.26849 6.6018Z"
              fill="#6B6D70"
            />
          </svg>
        </button>
      </div>
    </v-card-title>
    <v-card-title class="!pl-6 !pt-2 !pb-[22px] -mt-2">
      <div class="text-[13px] font-normal color-[#6B6D70] title-des">
        {{ desc }}
      </div>
    </v-card-title>
    <v-card-text
      class="flex justify-between w-[1330px] h-[712px] relative overflow-hidden mt-0 ml-5 !p-0"
    >
      <div class="w-[1330px] h-[712px] keen-slider-wrapper">
        <div ref="sliderRef" class="keen-slider">
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
                class="!h-[655px]"
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
          width="72"
          height="72"
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
          width="72"
          height="72"
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
            @click="sliderInstance.moveToIdx(idx - 1)"
          ></button>
        </div>
      </div>
    </v-card-text>
  </v-card>
</template>

<script setup>
import { UI_DASHBOARD } from "@/api/prod/path";
import { userImagesStore } from "@/store/userImagesStore";
import { httpClient } from "@/utils/http-common";
import KeenSlider from "keen-slider";
import "keen-slider/keen-slider.min.css";
import { VCard, VCardText, VCardTitle } from "vuetify/components";
import UpdateIcon from "../icons/UpdateIcon.vue";
import UserImageIconDetail from "../icons/UserImageIconDetail.vue";
import { useI18n } from "vue-i18n";

defineProps({
  desc: {
    type: String,
    default: "",
  },
  title: {
    type: String,
    default: "",
  },
});

const { t } = useI18n();
const activeSlideIndex = ref(
  parseInt(localStorage.getItem("activeSlideIndex")) || 0
);
const activeSlide = ref(0);
const imageStore = userImagesStore();
const { uploadedImagesExtend } = imageStore;
const emit = defineEmits(["close-dialog", "edit"]);
const sliderRef = ref(null);
const sliderInstance = ref(null);
const currentSlide = ref(0);
const perViewMode = ref(1);

function closeDialog() {
  emit("close-dialog");
}
function editImage() {
  emit("edit");
}
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
    mode: "free-snap",
    slides: {
      origin: "center",
      perView: perViewMode.value,
      spacing: 30,
    },
    slideChanged(slide) {
      currentSlide.value = slide.track.details.rel;
      activeSlide.value = slide.track.details.rel;
      activeSlideIndex.value = slide.track.details.rel;
      localStorage.setItem("activeSlideIndex", currentSlide.value);
      imageStore.setActiveSlideIndex(localStorage.getItem("activeSlideIndex"));
    },
  });
});

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
  height: 655px;
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
:deep(.mySlide) {
  border-radius: 8px;
  /* opacity: 0.4; */
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

:deep(.keen-slider__slide) {
  border: 1px solid var(--border-border-base, rgba(220, 224, 229, 1));
}
@media (min-width: 1536px) {
  :deep(.keen-slider__slide.active) {
    min-width: 1330px !important;
    max-width: 1330px !important;
    height: 655px;
  }
}
:deep(.keen-slider__slide.active) {
  border: 1px solid var(--border-border-base, rgba(220, 224, 229, 1));
  min-width: 1330px !important;
  max-width: 1330px !important;
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
  width: 56px;
  height: 56px;
  fill: #fff;
  cursor: pointer;
  transform: translateY(-50%);
}

.arrow--left {
  left: -4px;
  /* opacity: 0.4; */
}
/* .arrow--left:hover {
  opacity: 1;
} */

.arrow--right {
  right: -4px;
  /* opacity: 0.4; */
}
/* .arrow--right:hover {
  opacity: 1;
} */
.arrow {
  fill: none;
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
  padding: 16px 0;
}

.dot {
  border: none;
  width: 6px;
  height: 6px;
  background: #e6e9ed;
  border-radius: 50%;
  margin: 0 5px;
  cursor: pointer;
}

.dot.active {
  background: #ba1642;
  width: 24px;
  height: 6px;
  border-radius: 8px;
}

.active {
  opacity: 1;
}
.grid-item-child-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 60px;
}
.grid-item-child-header .icon-left {
  display: flex;
  align-items: center;
  gap: 9px;
  font-size: 16px;
  font-weight: 500;
  text-transform: capitalize;
  font-family: "Noto Sans KR";
}
.title-des {
  font-family: "Noto Sans KR";
}
.cancel-btn {
  border: 1px solid var(--border-border-base, rgba(220, 224, 229, 1));
}
.icon-right {
  margin-left: 10px;
  margin-top: 22px;
  font-size: 18px;
  display: flex;
  justify-content: space-between;
  padding: 0px 15px 20px 10px;
}
.icon-edit {
  margin-right: 20px;
}
</style>
