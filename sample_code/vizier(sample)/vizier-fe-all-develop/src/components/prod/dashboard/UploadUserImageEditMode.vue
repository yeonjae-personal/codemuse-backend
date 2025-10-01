<template>
  <v-card rounded="lg" class="flex upload-image-wrapper w-[1370px] h-[864px]">
    <v-card-title
      class="d-flex justify-space-between align-center !pl-6 !pb-2 !pt-[28px] !pr-[28px] grid-item-child-header"
    >
      <div class="text-base font-medium color-[#3a3b3d]">
        <div class="icon-left"><UserImageIconDetail />{{ title }}</div>
      </div>
      <button @click="closeEditDialog">
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
    </v-card-title>
    <v-card-title class="!pl-6 !pt-2 -mt-2">
      <div class="text-[13px] font-normal color-[#6B6D70] title-des">
        {{ desc }}
      </div>
    </v-card-title>
    <v-card-text class="flex justify-between h-[608px] relative">
      <div class="w-[1370px] h-[608px] wrapper-slide relative">
        <div
          ref="leftNavHover"
          class="w-[203px] h-[607px] absolute top-0 left-0 z-10 pointer-events-auto"
          v-on="hoverEvents"
        ></div>
        <div
          ref="rightNavHover"
          class="w-[203px] h-[607px] absolute top-0 right-0 z-10 pointer-events-auto"
          v-on="hoverEventsR"
        ></div>
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
                class="!h-[608px]"
              />
            </div>
            <div
              v-else
              class="upload-wrap"
              :class="{ 'delete-state': isDeleted, 'upload-state': isUpload }"
            >
              <upload-images
                :upload-msg="t('product_platform.dashboard.uploadMsg')"
                :multiple="true"
                :max="100"
                @changed="(event) => handleImageUpload(event, index)"
              />
            </div>

            <v-btn
              v-if="image.imageName && image.imagePath"
              class="custom-delete !w-[40px] !h-[40px] !p-0 !bg-none !shadow-none !min-w-[40px]"
              @click="handleDeleteImage(index)"
            >
              <svg
                width="40"
                height="40"
                viewBox="0 0 40 40"
                fill="none"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path
                  d="M0.5 8C0.5 3.85786 3.85786 0.5 8 0.5H32C36.1421 0.5 39.5 3.85786 39.5 8V32C39.5 36.1421 36.1421 39.5 32 39.5H8C3.85786 39.5 0.5 36.1421 0.5 32V8Z"
                  fill="white"
                />
                <path
                  d="M0.5 8C0.5 3.85786 3.85786 0.5 8 0.5H32C36.1421 0.5 39.5 3.85786 39.5 8V32C39.5 36.1421 36.1421 39.5 32 39.5H8C3.85786 39.5 0.5 36.1421 0.5 32V8Z"
                  stroke="#DCE0E6"
                />
                <path
                  fill-rule="evenodd"
                  clip-rule="evenodd"
                  d="M19.3011 10.8333H20.6986C21.1379 10.8333 21.5169 10.8333 21.829 10.8588C22.1585 10.8857 22.4862 10.9451 22.8015 11.1058C23.2719 11.3455 23.6543 11.7279 23.894 12.1983C24.0547 12.5137 24.1141 12.8413 24.141 13.1708C24.1639 13.4506 24.1662 13.7841 24.1665 14.1666H27.4998C27.9601 14.1666 28.3332 14.5397 28.3332 15C28.3332 15.4602 27.9601 15.8333 27.4998 15.8333H26.6665V24.3678C26.6665 25.0385 26.6665 25.5922 26.6297 26.0432C26.5914 26.5116 26.5093 26.9422 26.3032 27.3466C25.9836 27.9738 25.4737 28.4838 24.8465 28.8033C24.442 29.0094 24.0114 29.0915 23.543 29.1298C23.092 29.1667 22.5384 29.1667 21.8676 29.1666H18.1321C17.4613 29.1667 16.9076 29.1667 16.4566 29.1298C15.9882 29.0915 15.5576 29.0094 15.1532 28.8033C14.526 28.4838 14.0161 27.9738 13.6965 27.3466C13.4904 26.9422 13.4083 26.5116 13.37 26.0432C13.3332 25.5922 13.3332 25.0385 13.3332 24.3677L13.3332 15.8333H12.4998C12.0396 15.8333 11.6665 15.4602 11.6665 15C11.6665 14.5397 12.0396 14.1666 12.4998 14.1666H15.8332C15.8334 13.7841 15.8358 13.4506 15.8586 13.1708C15.8856 12.8413 15.945 12.5137 16.1057 12.1983C16.3453 11.7279 16.7278 11.3455 17.1982 11.1058C17.5135 10.9451 17.8412 10.8857 18.1706 10.8588C18.4828 10.8333 18.8618 10.8333 19.3011 10.8333ZM14.9998 15.8333V24.3333C14.9998 25.0471 15.0005 25.5324 15.0311 25.9074C15.061 26.2728 15.1151 26.4596 15.1815 26.59C15.3413 26.9036 15.5962 27.1585 15.9099 27.3183C16.0402 27.3847 16.227 27.4388 16.5924 27.4687C16.9674 27.4993 17.4527 27.5 18.1665 27.5H21.8332C22.547 27.5 23.0322 27.4993 23.4073 27.4687C23.7727 27.4388 23.9595 27.3847 24.0898 27.3183C24.4034 27.1585 24.6584 26.9036 24.8182 26.59C24.8846 26.4596 24.9387 26.2728 24.9685 25.9074C24.9992 25.5324 24.9998 25.0471 24.9998 24.3333V15.8333H14.9998ZM22.4998 14.1666H17.4999C17.5003 13.7791 17.5028 13.5141 17.5198 13.3065C17.5383 13.0801 17.5697 12.9962 17.5907 12.955C17.6706 12.7982 17.798 12.6707 17.9548 12.5908C17.996 12.5698 18.08 12.5384 18.3064 12.5199C18.5425 12.5006 18.8527 12.5 19.3332 12.5H20.6665C21.147 12.5 21.4572 12.5006 21.6933 12.5199C21.9197 12.5384 22.0036 12.5698 22.0448 12.5908C22.2016 12.6707 22.3291 12.7982 22.409 12.955C22.43 12.9962 22.4614 13.0801 22.4799 13.3065C22.4969 13.5141 22.4994 13.7791 22.4998 14.1666ZM18.3332 18.75C18.7934 18.75 19.1665 19.1231 19.1665 19.5833V23.75C19.1665 24.2102 18.7934 24.5833 18.3332 24.5833C17.8729 24.5833 17.4998 24.2102 17.4998 23.75V19.5833C17.4998 19.1231 17.8729 18.75 18.3332 18.75ZM21.6665 18.75C22.1267 18.75 22.4998 19.1231 22.4998 19.5833V23.75C22.4998 24.2102 22.1267 24.5833 21.6665 24.5833C21.2063 24.5833 20.8332 24.2102 20.8332 23.75V19.5833C20.8332 19.1231 21.2063 18.75 21.6665 18.75Z"
                  fill="#C7291D"
                />
              </svg>
            </v-btn>
          </div>
        </div>
        <svg
          v-if="sliderInstance"
          class="z-[99999999]"
          :class="{
            arrow: true,
            'arrow--left': true,
            'arrow--disabled': activeSlideIndex === 0,
            'arrow-visible': isArrowVisible,
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
          class="z-[99999999]"
          :class="{
            arrow: true,
            'arrow--right': true,
            'arrow--disabled':
              activeSlideIndex ===
              sliderInstance.track.details.slides.length - 1,
            'arrow-visible': isArrowVisibleR,
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
    <v-card-actions class="m-3 d-flex justify-end">
      <v-btn
        class="text-none w-[112px] !h-[44px] text-[13px] font-medium rounded-lg"
        color="#D9325A"
        variant="flat"
        :text="t('product_platform.dashboard.save')"
        @click="saveImage"
      ></v-btn>
      <v-btn
        class="cancel-btn text-none w-[112px] !h-[44px] border-[#DCE0E5] border-2 text-[13px] font-medium rounded-lg"
        variant="outlined"
        :text="t('product_platform.dashboard.cancel')"
        @click="cancelEditDialog"
      ></v-btn>
      <div class="w-[214px]"></div>
    </v-card-actions>
  </v-card>
</template>

<!-- eslint-disable security/detect-object-injection -->
<script setup>
import { UI_DASHBOARD } from "@/api/prod/path";
import { useSnackbarStore } from "@/store";
import { userImagesStore } from "@/store/userImagesStore";
import { httpClient } from "@/utils/http-common";
import KeenSlider from "keen-slider";
import "keen-slider/keen-slider.min.css";
import UploadImages from "vue-upload-drop-images";
import {
  VBtn,
  VCard,
  VCardActions,
  VCardText,
  VCardTitle,
} from "vuetify/components";
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
const isArrowVisible = ref(false);
const isArrowVisibleR = ref(false);
const activeSlide = ref(0);
const imageStore = userImagesStore();
const { deleteImage, uploadedImagesExtend } = imageStore;
const imageSeqToDelete = ref(null);
const emit = defineEmits([
  "close-edit-dialog",
  "save-image",
  "cancel-edit-dialog",
]);
const deleteIndex = ref(null);
const sliderRef = ref(null);
const sliderInstance = ref(null);
const currentSlide = ref(0);
const isDeleted = ref(false);
const isUpload = ref(false);
const { showSnackbar } = useSnackbarStore();
const tempImages = ref([]);
const flagDel = ref(false);

const activeSlideIndex = ref(
  parseInt(localStorage.getItem("activeSlideIndex")) || 0
);
function handleDeleteImage(index) {
  flagDel.value = true;
  const image = imageStore.uploadedImagesExtend.requests[index];
  deleteIndex.value = index;
  imageSeqToDelete.value = image.imageSeq;
  deleteImage(imageSeqToDelete.value);
}

function handleImageUpload(event, index) {
  isDeleted.value = false;
  isUpload.value = true;
  const latestFile = event[event.length - 1];
  if (latestFile) {
    const reader = new FileReader();
    reader.onload = () => {
      const existingImage = tempImages.value.find(
        (img) => img.imageSeq === index
      );
      if (existingImage) {
        existingImage.imageBase64 = reader.result;
        existingImage.imageName = latestFile.name;
      } else {
        tempImages.value.push({
          imageSeq: index,
          imageBase64: reader.result,
          imageName: latestFile.name,
        });
      }
    };
    reader.readAsDataURL(latestFile);
  } else {
    tempImages.value = tempImages.value.filter((img) => img.imageSeq !== index);
  }
}
const saveImage = async () => {
  tempImages.value.forEach((tempImage) => {
    const imageToUpdate = uploadedImagesExtend.requests.find(
      (img) => img.imageSeq === tempImage.imageSeq
    );
    if (imageToUpdate) {
      imageToUpdate.imageBase64 = tempImage.imageBase64;
      imageToUpdate.imageName = tempImage.imageName;
      imageToUpdate.imageSeq = tempImage.imageSeq;
    }
  });
  const filteredUploadedImagesExtend = ref({
    dsbdViewUuid: imageStore.dsbdViewUuid,
    requests: uploadedImagesExtend.requests,
  });
  try {
    const requestBody = filteredUploadedImagesExtend.value;
    const response = await httpClient.post(
      `${UI_DASHBOARD}/userimage`,
      requestBody
    );
    if (response.status === 200) {
      const result = response.data;
      imageStore.setUploadedImagesExtend(result);
      emit("save-image");
      showSnackbar(t("product_platform.dashboard.saveImgSuccess"), "success");
    }
  } catch (err) {
    console.error("Failed to save data:", err.errorMsg);
    showSnackbar(err.errorMsg, "error");
  }
};

function closeEditDialog() {
  emit("close-edit-dialog");
}

function cancelEditDialog() {
  emit("cancel-edit-dialog");
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
      perView: 1.5,
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
const hoverEvents = {
  mouseenter: () => {
    isArrowVisible.value = true;
  },
  mouseleave: () => {
    isArrowVisible.value = false;
  },
};
const hoverEventsR = {
  mouseenter: () => {
    isArrowVisibleR.value = true;
  },
  mouseleave: () => {
    isArrowVisibleR.value = false;
  },
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
</script>

<style scoped>
:deep(.upload-wrap.delete-state .imgsPreview) {
  display: none !important;
}
:deep(.upload-wrap.delete-state .beforeUpload) {
  display: block !important;
}

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
  height: 608px;
}

.imgsPreview {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

:deep(.beforeUpload) {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
:deep(.beforeUpload .icon) {
  height: 80px;
  width: 80px;
  display: none;
}
:deep(.imgsPreview .imageHolder) {
  width: 100%;
  height: 608px;
  margin: 0px;
}
:deep(.clearButton) {
  z-index: 10;
  display: none;
}
:deep(.imgsPreview .imageHolder .plus) {
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
  /* box-shadow: 0px 2px 12px 0px rgba(0, 0, 0, 0.24); */
  border-radius: 8px;
  /* opacity: 0.4; */
}
:deep(.v-card-text) {
  padding: 16px 0px 24px !important;
}
:deep(.popup-card .v-card-text) {
  padding: 0px !important;
}
:deep(.imgsPreview .imageHolder .delete) {
  color: #d9325a !important;
  background-color: #fff;
  border-radius: 8px;
  padding: 2px;
  top: 10px;
  right: 10px;
  width: 40px;
  height: 40px;
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

.imageHolder {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  background: linear-gradient(
    253.91deg,
    rgba(0, 0, 0, 0.24) -14.32%,
    rgba(0, 0, 0, 0) 24.16%
  );
}
:deep(.imageHolder img) {
  background: linear-gradient(
    253.91deg,
    rgba(0, 0, 0, 0.24) -14.32%,
    rgba(0, 0, 0, 0) 24.16%
  );
  aspect-ratio: 16 / 9;
  object-fit: contain !important;
}
:deep(.delete) {
  opacity: 0;
  transition: opacity 0.3s ease;
}
:deep(.keen-slider__slide:hover .delete) {
  opacity: 1;
}
:deep(.custom-delete) {
  position: absolute;
  right: 10px;
  top: 10px;
  opacity: 0;
  transition: opacity 0.3s ease;
  box-shadow: none !important;
  background: none !important;
}
:deep(.custom-delete:hover) {
  box-shadow: none !important;
  background: none !important;
}

.keen-slider__slide:hover .custom-delete {
  opacity: 1;
}
:deep(.keen-slider__slide.active .arrow) {
  display: none;
}
:deep(.keen-slider__slide) {
  border: 1px solid var(--border-border-base, rgba(220, 224, 229, 1));
  opacity: 0.4;
}
:deep(.keen-slider__slide.active) {
  border: 1px solid var(--border-border-base, rgba(220, 224, 229, 1));
  opacity: 1;
}
:deep(.keen-slider__slide:hover) {
  background: linear-gradient(
    238deg,
    rgba(0, 29, 42, 0.16) 0%,
    rgba(0, 29, 42, 0) 10.61%
  );
}

:deep(.upload-wrap .beforeUpload) {
  background-image: url("../../../assets/icons/add_image.png");
  background-size: 80px 80px;
  background-position: top;
  height: 120px;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
}
:deep(.drop) {
  border: 1px solid var(--border-border-primary, rgba(217, 50, 90, 1));
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
  cursor: pointer;
  transform: translateY(-50%);
}

.arrow--left {
  left: 0px;
  opacity: 0;
  transition: opacity 0.3s ease;
}
.arrow--left.arrow-visible {
  opacity: 1;
}
.arrow--left:hover {
  opacity: 1;
}

.arrow--right {
  right: 0px;
  opacity: 0;
  transition: opacity 0.3s ease;
}
.arrow--right:hover {
  opacity: 1;
}
.arrow--right.arrow-visible {
  opacity: 1;
}
.dots {
  display: flex;
  justify-content: center;
  padding: 14px 0;
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
  height: 50px;
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
.title-des,
.title-confirm {
  font-family: "Noto Sans KR";
}
.cancel-btn,
.save-btn {
  border: 1px solid var(--border-border-base, rgba(220, 224, 229, 1));
}
</style>
