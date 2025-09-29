<template>
  <div class="user-info-panel">
    <div class="profile-section">
      <div class="profile-pic">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="40"
          height="40"
          viewBox="0 0 40 40"
          fill="none"
        >
          <path
            d="M0 20C0 8.95431 8.95431 0 20 0C31.0457 0 40 8.95431 40 20C40 31.0457 31.0457 40 20 40C8.95431 40 0 31.0457 0 20Z"
            fill="#F0F2F5"
          />
          <path
            fill-rule="evenodd"
            clip-rule="evenodd"
            d="M20.0001 12C18.0671 12 16.5001 13.567 16.5001 15.5C16.5001 17.433 18.0671 19 20.0001 19C21.9331 19 23.5001 17.433 23.5001 15.5C23.5001 13.567 21.9331 12 20.0001 12ZM14.5001 15.5C14.5001 12.4624 16.9625 10 20.0001 10C23.0376 10 25.5001 12.4624 25.5001 15.5C25.5001 18.5376 23.0376 21 20.0001 21C16.9625 21 14.5001 18.5376 14.5001 15.5ZM17.3263 22.5C17.3834 22.5 17.4413 22.5 17.5001 22.5H22.5001C22.5589 22.5 22.6168 22.5 22.6738 22.5C23.9011 22.4995 24.7391 22.4992 25.4515 22.7153C27.0495 23.2 28.3 24.4506 28.7848 26.0486C29.0009 26.761 29.0006 27.599 29.0001 28.8263C29.0001 28.8833 29.0001 28.9412 29.0001 29C29.0001 29.5523 28.5524 30 28.0001 30C27.4478 30 27.0001 29.5523 27.0001 29C27.0001 27.5317 26.9892 27.0192 26.8709 26.6291C26.5801 25.6703 25.8297 24.92 24.8709 24.6292C24.4809 24.5108 23.9684 24.5 22.5001 24.5H17.5001C16.0318 24.5 15.5193 24.5108 15.1292 24.6292C14.1704 24.92 13.4201 25.6703 13.1293 26.6291C13.0109 27.0192 13.0001 27.5317 13.0001 29C13.0001 29.5523 12.5524 30 12.0001 30C11.4478 30 11.0001 29.5523 11.0001 29C11.0001 28.9412 11.0001 28.8833 11 28.8263C10.9996 27.599 10.9993 26.761 11.2154 26.0486C11.7001 24.4506 12.9507 23.2 14.5487 22.7153C15.2611 22.4992 16.0991 22.4995 17.3263 22.5Z"
            fill="#6B6D70"
          />
        </svg>
      </div>
      <div class="user-details">
        <h2>{{ userProfile.userName }}</h2>
        <p>{{ userProfile.userRole }}</p>
      </div>
    </div>
    <BaseButton :color="ButtonColorType.Secondary">
      {{ $t("product_platform.dashboard.profile") }}
    </BaseButton>
  </div>
</template>

<script setup>
import { ButtonColorType } from "@/enums";
import { useI18n } from "vue-i18n";

const { t } = useI18n();
const user = JSON.parse(localStorage.getItem("userInfo") || "{}");
const userProfile = ref({
  profilePicture: "https://via.placeholder.com/100",
  userName: user.userNm,
  userRole: "System Manager",
  activeTab: "Statistics",
  tabs: ["Statistics", "Personalized"],
  statistics: [
    { title: "Subscriber Top 10", icon: "fas fa-chart-bar" },
    { title: "Item Count / Rate", icon: "fas fa-chart-pie" },
    { title: "Recently Worked", icon: "fas fa-clock" },
  ],
});
</script>

<style scoped>
.user-info-panel {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #ffff;
  height: 40px;
}

.profile-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
}

.profile-pic {
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 50%;
  width: 40px;
  height: 40px;
}

.user-details h2 {
  font-family: Noto Sans KR;
  font-size: 18px;
  font-weight: 700;
  line-height: 27px;
  letter-spacing: 0.5px;
  text-align: left;
  color: rgba(58, 59, 61, 1);
  white-space: nowrap;

  @media (max-width: 1766px) {
    font-size: 14px;
  }

  @media (max-width: 1605px) {
    font-size: 10px;
  }
}

.user-details p {
  font-family: Noto Sans KR;
  font-size: 11px;
  font-weight: 500;
  line-height: 16.5px;
  letter-spacing: 0.25px;
  text-align: left;
  color: rgba(107, 109, 112, 1);
  white-space: nowrap;

  @media (max-width: 1766px) {
    font-size: 9px;
  }

  @media (max-width: 1605px) {
    font-size: 7px;
  }
}

.profile-btn {
  color: #ba1642;
  border-radius: 8px;
  padding: 0 16px;
  background-color: #fff0f2;
  height: 100%;
  justify-content: center;
  align-items: center;
  display: flex;

  @media (max-width: 1441px) {
    width: 35%;
  }

  @media (max-width: 1220px) {
    width: 25%;
    font-size: small;
  }
}

.profile-btn:hover {
  background-color: #ba1642;
  color: #fff0f2;
}
</style>
