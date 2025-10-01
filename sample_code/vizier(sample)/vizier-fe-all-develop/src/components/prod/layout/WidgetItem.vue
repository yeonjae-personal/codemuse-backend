<template>
  <div
    class="bento-widget widget-instance"
    :class="{ disabled: widgetDisabled }"
  >
    <div
      v-show="!widgetDisabled"
      class="blur-effect design-component-instance-node"
    />
    <div v-show="!widgetDisabled" class="div widget-2" />
    <svg
      class="bar-chart-square-01-1 bar-chart-square"
      fill="none"
      height="20"
      viewBox="0 0 20 20"
      width="20"
      xmlns="http://www.w3.org/2000/svg"
    >
      <path
        className="path"
        clipRule="evenodd"
        d="M6.46591 1.6665H13.5347C14.2056 1.66649 14.7592 1.66648 15.2102 1.70333C15.6786 1.7416 16.1092 1.82374 16.5136 2.02982C17.1408 2.34939 17.6508 2.85933 17.9703 3.48654C18.1764 3.89098 18.2586 4.32157 18.2968 4.78998C18.3337 5.24097 18.3337 5.7946 18.3337 6.4654V13.5343C18.3337 14.2051 18.3337 14.7587 18.2968 15.2097C18.2586 15.6781 18.1764 16.1087 17.9703 16.5131C17.6508 17.1403 17.1408 17.6503 16.5136 17.9699C16.1092 18.1759 15.6786 18.2581 15.2102 18.2963C14.7592 18.3332 14.2056 18.3332 13.5348 18.3332H6.46589C5.79509 18.3332 5.24146 18.3332 4.79047 18.2963C4.32206 18.2581 3.89147 18.1759 3.48702 17.9699C2.85982 17.6503 2.34988 17.1403 2.0303 16.5131C1.82423 16.1087 1.74209 15.6781 1.70382 15.2097C1.66697 14.7587 1.66698 14.2051 1.66699 13.5343V6.46542C1.66698 5.79461 1.66697 5.24098 1.70382 4.78998C1.74209 4.32157 1.82423 3.89098 2.0303 3.48654C2.34988 2.85933 2.85982 2.34939 3.48702 2.02982C3.89147 1.82374 4.32206 1.7416 4.79047 1.70333C5.24147 1.66648 5.7951 1.66649 6.46591 1.6665ZM4.92619 3.36446C4.56084 3.39431 4.37401 3.44842 4.24368 3.51483C3.93007 3.67462 3.6751 3.92958 3.51532 4.24319C3.44891 4.37352 3.3948 4.56036 3.36495 4.9257C3.33431 5.30078 3.33366 5.78602 3.33366 6.49984V13.4998C3.33366 14.2137 3.33431 14.6989 3.36495 15.074C3.3948 15.4393 3.44891 15.6262 3.51532 15.7565C3.6751 16.0701 3.93007 16.3251 4.24368 16.4848C4.37401 16.5513 4.56085 16.6054 4.92619 16.6352C5.30127 16.6659 5.78651 16.6665 6.50033 16.6665H13.5003C14.2141 16.6665 14.6994 16.6659 15.0745 16.6352C15.4398 16.6054 15.6266 16.5513 15.757 16.4848C16.0706 16.3251 16.3255 16.0701 16.4853 15.7565C16.5517 15.6262 16.6058 15.4393 16.6357 15.074C16.6663 14.6989 16.667 14.2137 16.667 13.4998V6.49984C16.667 5.78602 16.6663 5.30078 16.6357 4.9257C16.6058 4.56036 16.5517 4.37352 16.4853 4.24319C16.3255 3.92958 16.0706 3.67462 15.757 3.51483C15.6266 3.44842 15.4398 3.39431 15.0745 3.36446C14.6994 3.33382 14.2141 3.33317 13.5003 3.33317H6.50033C5.78651 3.33317 5.30127 3.33382 4.92619 3.36446ZM10.0003 4.99984C10.4606 4.99984 10.8337 5.37293 10.8337 5.83317V14.1665C10.8337 14.6267 10.4606 14.9998 10.0003 14.9998C9.54009 14.9998 9.16699 14.6267 9.16699 14.1665V5.83317C9.16699 5.37293 9.54009 4.99984 10.0003 4.99984ZM13.3337 8.33317C13.7939 8.33317 14.167 8.70627 14.167 9.1665V14.1665C14.167 14.6267 13.7939 14.9998 13.3337 14.9998C12.8734 14.9998 12.5003 14.6267 12.5003 14.1665V9.1665C12.5003 8.70627 12.8734 8.33317 13.3337 8.33317ZM6.66699 9.99984C7.12723 9.99984 7.50033 10.3729 7.50033 10.8332V14.1665C7.50033 14.6267 7.12723 14.9998 6.66699 14.9998C6.20676 14.9998 5.83366 14.6267 5.83366 14.1665V10.8332C5.83366 10.3729 6.20676 9.99984 6.66699 9.99984Z"
        :fill="widgetDisabled ? '#3A3B3D' : '#2E90FA'"
        fillRule="evenodd"
      />
    </svg>
    <div class="text-wrapper">
      <CustomTooltip
        :content="widgetName"
        :disabled="widgetDisabled"
        location="bottom"
      />
    </div>
    <svg
      class="dots-grid-2 dots-grid"
      fill="none"
      height="24"
      viewBox="0 0 24 24"
      width="24"
      xmlns="http://www.w3.org/2000/svg"
    >
      <path
        className="path"
        clipRule="evenodd"
        d="M7 5C7 3.89543 7.89543 3 9 3C10.1046 3 11 3.89543 11 5C11 6.10457 10.1046 7 9 7C7.89543 7 7 6.10457 7 5ZM14 5C14 3.89543 14.8954 3 16 3C17.1046 3 18 3.89543 18 5C18 6.10457 17.1046 7 16 7C14.8954 7 14 6.10457 14 5ZM7 12C7 10.8954 7.89543 10 9 10C10.1046 10 11 10.8954 11 12C11 13.1046 10.1046 14 9 14C7.89543 14 7 13.1046 7 12ZM14 12C14 10.8954 14.8954 10 16 10C17.1046 10 18 10.8954 18 12C18 13.1046 17.1046 14 16 14C14.8954 14 14 13.1046 14 12ZM7 19C7 17.8954 7.89543 17 9 17C10.1046 17 11 17.8954 11 19C11 20.1046 10.1046 21 9 21C7.89543 21 7 20.1046 7 19ZM14 19C14 17.8954 14.8954 17 16 17C17.1046 17 18 17.8954 18 19C18 20.1046 17.1046 21 16 21C14.8954 21 14 20.1046 14 19Z"
        fill="#6B6D70"
        fillRule="evenodd"
      />
    </svg>
  </div>
</template>

<script setup>
defineProps({
  widgetName: {
    type: String,
    default: "",
  },
  widgetDisabled: {
    type: Boolean,
    default: false,
  },
});
</script>

<style scoped>
.widget-instance {
  border-image: unset;
  box-shadow: unset;
  width: 312px;
}

.design-component-instance-node {
  background: unset;
  border-radius: unset;
  filter: unset;
  height: 52px;
  left: 235px;
  opacity: unset;
  top: 0;
  transform: unset;
  width: 77px;
}

.widget-2 {
  left: 223px;
}

.bento-widget {
  align-items: center;
  background-color: rgba(255, 255, 255, 1);
  /* border-bottom-style: solid;
    border-bottom-width: 2px;
    border-color: transparent;
    border-image: linear-gradient(to bottom, rgb(101.11, 166.38, 255), rgb(107.34, 218.1, 218.1)) 1; */
  border-radius: 8px;
  box-shadow:
    0px 2px 4px #3f67b529,
    inset -2px -2px 4px #1a33590a;
  display: flex;
  gap: 8px;
  height: 52px;
  overflow: hidden;
  padding: 12px 16px 12px 16px;
  position: relative;
  width: 100%;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
}

.disabled {
  opacity: 0.5;
  background: #f0f2f5;
  color: #f0f2f5;
  box-shadow: none;
}

.bento-widget .blur-effect {
  background: linear-gradient(
    180deg,
    rgba(37.42, 190.23, 255, 0.2) 0%,
    rgba(45.55, 192.65, 255, 0.04) 100%
  );
  border-radius: 65.5px;
  filter: blur(12px);
  height: 131px;
  left: 239px;
  opacity: 0.64;
  position: absolute;
  top: -50px;
  transform: rotate(180deg);
  width: 131px;
}

.bento-widget .div {
  background: linear-gradient(
    180deg,
    rgba(255, 107.46, 160.57, 0.04) 0%,
    rgba(255, 106.11, 159.9, 0.2) 100%
  );
  border-radius: 35px;
  filter: blur(12px);
  height: 70px;
  left: 215px;
  opacity: 0.64;
  position: absolute;
  top: 18px;
  width: 70px;
}

.bento-widget .bar-chart-square {
  height: 20px !important;
  position: relative !important;
  width: 20px !important;
}

.bento-widget .text-wrapper {
  color: rgba(58, 59, 61, 1);
  flex: 1;
  font-family: "Noto Sans KR", Helvetica;
  font-size: 13px;
  font-style: normal;
  font-weight: 500;
  letter-spacing: 0.25px;
  line-height: 150%;
  position: relative;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
}

.bento-widget .dots-grid {
  height: 16px !important;
  position: relative !important;
  width: 16px !important;
}
</style>
