<template>
  <div class="view-detail-template">
    <div class="left-side-section">
      <BentoGrid v-model="gridData" :data="dataDashboardView" />
    </div>
    <div class="right-side-section">
      <UserProfile />
      <BentoGridList :data="dataDashboardList" :list-item-code="listItemCode" />
      <BaseButton
        :color="ButtonColorType.Primary"
        width="100%"
        :disabled="loading"
        @click="saveDashboard"
      >
        {{
          loading
            ? t("product_platform.dashboard.saving")
            : t("product_platform.dashboard.saveLayout")
        }}
      </BaseButton>
      <CustomDatePicker />
    </div>
  </div>
</template>

<script setup>
import { UI_DASHBOARD } from "@/api/prod/path";
import CustomDatePicker from "@/components/controls/CustomDatePicker.vue";
import BentoGrid from "@/components/prod/layout/BentoGrid.vue";
import BentoGridList from "@/components/prod/layout/BentoGridList.vue";
import UserProfile from "@/components/prod/layout/UserProfile.vue";
import { ButtonColorType } from "@/enums";
import { useSnackbarStore } from "@/store";
import { userImagesStore } from "@/store/userImagesStore";
import { httpClient } from "@/utils/http-common";
import { useI18n } from "vue-i18n";
const { t } = useI18n();
const imageStore = userImagesStore();
const { showSnackbar } = useSnackbarStore();
const dataDashboardList = ref([]);
const dataDashboardView = ref([]);
const gridData = ref(null);
const loading = ref(false);
const listItemCode = ref([]);

const fetchData = async () => {
  try {
    const response = await httpClient.get(UI_DASHBOARD);
    dataDashboardList.value = response.data.dsbdviews || [];
    const dsbdViewUuidUserImage = dataDashboardList.value.find(
      (dt) => dt.dsbdViewCode === "user-image"
    );

    if (dsbdViewUuidUserImage) {
      imageStore.setDsbdViewUuid(dsbdViewUuidUserImage.dsbdViewUuid);
    }
    const sessionLayout = JSON.parse(sessionStorage.getItem("layout"));
    if (sessionLayout) {
      dataDashboardView.value = sessionLayout;
    } else {
      dataDashboardView.value = response.data.listviewdashboard;
    }
  } catch (error) {
    console.error("Error fetching data:", error);
  }
};

const saveDashboard = async () => {
  try {
    loading.value = true;
    const payload = gridData.value
      ?.filter((item) => item.id)
      ?.map((item) => {
        return {
          dsbdViewUuid: item.id,
          posX: item.x,
          posY: item.y,
        };
      });

    const response = await httpClient.post(`${UI_DASHBOARD}`, payload);

    if (response.status === 200) {
      showSnackbar(t("product_platform.dashboard.saveSuccessful"), "success");
      setTimeout(() => {
        loading.value = false;
      }, 3000);
      sessionStorage.removeItem("layout");
    } else {
      showSnackbar(t("product_platform.dashboard.saveFailed"), "error");
      setTimeout(() => {
        loading.value = false;
      }, 3000);
    }
  } catch (error) {
    showSnackbar(
      error?.errorMsg || t("product_platform.dashboard.serverError"),
      "error"
    );
    setTimeout(() => {
      loading.value = false;
    }, 3000);
    console.error("Failed to save data:", error);
  }
};

onMounted(() => {
  fetchData();
});

onUnmounted(() => {
  const jsonData = JSON.stringify(
    gridData.value
      ?.filter((item) => item.id)
      ?.map((item) => ({
        dsbdViewName: item.name,
        dsbdViewDscrCntn: item.desc,
        dsbdViewCode: item.code,
        dsbdViewUuid: item.id,
        posX: item.x,
        posY: item.y,
      }))
  );
  if (jsonData != undefined && jsonData != null) {
    sessionStorage.setItem("layout", jsonData);
  }
});
watch(
  gridData,
  (newValue) => {
    listItemCode.value = newValue.map((item) => item.code);
  },
  { deep: true }
);
</script>

<style scoped>
.view-detail-template {
  display: flex;
  height: 100%;
  width: 100%;
  overflow-y: hidden;
}

.left-side-section {
  height: 100%;
  width: 80%;
}

.right-side-section {
  margin-top: 3px;
  width: 20%;
  background-color: #ffff;
  border: 1px solid #ffff;
  border-radius: 16px;
  padding: 20px 20px 0;
  overflow-y: auto;
  scroll-behavior: smooth;
  scrollbar-width: thin;
  height: calc(100% - 10px);
}

.save-layout-btn {
  height: 48px;
  padding: 0 16px;
  background-color: #d9325a;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  width: 100%;
  font-family: Noto Sans KR;
  font-size: 13px;
  font-weight: 500;
  letter-spacing: 0.5px;
}

.save-layout-btn:disabled {
  background: rgba(218, 217, 226, 1);
}
</style>
