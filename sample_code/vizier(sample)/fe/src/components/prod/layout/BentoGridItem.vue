<template>
  <div class="grid-item-child">
    <div v-if="item.name" class="grid-item-child-container">
      <div class="grid-item-child-header">
        <div class="icon-left"><ItemIcon />{{ t(item.name) }}</div>
        <div class="icon-right">
          <div v-if="item.code === 'user-image'" class="user-image">
            <div>
              <v-dialog
                v-model="isDialogOpen"
                class="2xl:w-[1370px] 2xl:h-[864px]"
                persistent
              >
                <template #activator="{ props: activatorProps }">
                  <button v-bind="activatorProps">
                    <DetailIcon />
                  </button>
                </template>
                <UploadUserImage
                  v-if="dialogMode === 'view'"
                  :desc="t(item.desc)"
                  :title="t(item.name)"
                  @close-dialog="closeViewDialog"
                  @edit="openEditDialog"
                />
                <UploadUserImageEditMode
                  v-else-if="dialogMode === 'edit'"
                  :desc="t(item.desc)"
                  :title="t(item.name)"
                  @save-image="saveImg"
                  @close-edit-dialog="closeEditDialog"
                  @cancel-edit-dialog="cancelEditDialog"
                />
              </v-dialog>
            </div>
          </div>
          <SubscriberTop10
            v-else-if="item.code === 'subscriber-top-10'"
            :desc="t(item.desc)"
            :title="t(item.name)"
          />
          <RecentlyWorkedDetail
            v-else-if="item.code === 'recently-worked'"
            :desc="t(item.desc)"
            :title="t(item.name)"
          />
          <button @click="removeItem">
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
      </div>
      <div class="item-content">
        <span class="description mb-5">{{ t(item.desc) }}</span>
        <div class="content">
          <RecentlyWorkedItem v-if="item.code === 'recently-worked'" />
          <div v-if="item.code === 'user-image'" class="userimg-wrapper">
            <UserImage ref="userImageRef" />
          </div>
          <SubscriberTop10Item v-if="item.code === 'subscriber-top-10'" />
          <MonthlyReportItemsChart
            v-if="item.code === 'monthly-report-about-items'"
          />
          <MonthlyReportUsersChart
            v-if="item.code === 'monthly-report-about-users'"
          />
          <ItemVolumnChart v-if="item.code === 'item-volume'" />
        </div>
      </div>
    </div>
    <BentoGridLoading v-else :is-loading="item.loading" />
  </div>
</template>

<script setup>
import ItemVolumnChart from "../dashboard/ItemVolumnChart.vue";
import MonthlyReportUsersChart from "../dashboard/MonthlyReportUsersChart.vue";
import SubscriberTop10 from "../dashboard/SubscriberTop10.vue";
import UploadUserImage from "../dashboard/UploadUserImage.vue";
import UploadUserImageEditMode from "../dashboard/UploadUserImageEditMode.vue";
import UserImage from "../dashboard/UserImage.vue";
import SubscriberTop10Item from "../dashboard/subscriber-top-10/SubscriberTop10Item.vue";
import DetailIcon from "../icons/DetailIcon.vue";
import ItemVolumeIcon from "../icons/ItemVolumeIcon.vue";
import RecentlyWorkedIcon from "../icons/RecentlyWorkedIcon.vue";
import ReportAboutUsersIcon from "../icons/ReportAboutUsersIcon.vue";
import SubscriberTop10Icon from "../icons/SubscriberTop10Icon.vue";
import UserImageIcon from "../icons/UserImageIcon.vue";
import RecentlyWorkedItem from "../dashboard/recently-worked/RecentlyWorkedItem.vue";
import { useI18n } from "vue-i18n";
import RecentlyWorkedDetail from "../dashboard/recently-worked/RecentlyWorkedDetail.vue";

const { t } = useI18n();
const isDialogOpen = ref(false);
const dialogMode = ref("view");
const props = defineProps({
  item: {
    type: Object,
    default: () => {},
  },
});

const emit = defineEmits(["removeItem"]);
const isViewDialogOpen = ref(false);
const isEditDialogOpen = ref(false);
const userImageRef = ref(null);

const removeItem = () => {
  emit("removeItem", props.item?.i);
};
const closeViewDialog = (_activeIndex) => {
  userImageRef.value?.initializeSlider();
  isDialogOpen.value = false;
};

const closeEditDialog = () => {
  isDialogOpen.value = false;
  isEditDialogOpen.value = false;

  setTimeout(() => {
    dialogMode.value = "view";
  }, 200);
};
const cancelEditDialog = () => {
  dialogMode.value = "view";
  isEditDialogOpen.value = false;
  setTimeout(() => {
    isViewDialogOpen.value = true;
  }, 200);
};
const saveImg = () => {
  dialogMode.value = "view";
  isEditDialogOpen.value = false;
  setTimeout(() => {
    isViewDialogOpen.value = true;
  }, 100);
};
function openEditDialog() {
  isViewDialogOpen.value = false;
  dialogMode.value = "edit";
  setTimeout(() => {
    isEditDialogOpen.value = true;
  }, 200);
}

const ItemIcon = computed(() => {
  switch (props.item.code) {
    case "subscriber-top-10":
      return SubscriberTop10Icon;
    case "item-volume":
      return ItemVolumeIcon;

    case "recently-worked":
      return RecentlyWorkedIcon;

    case "monthly-report-about-items":
      return ReportAboutUsersIcon;

    case "monthly-report-about-users":
      return ReportAboutUsersIcon;

    case "user-image":
      return UserImageIcon;

    default:
      break;
  }
});
</script>

<style scoped>
.grid-item-child {
  height: 100%;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #ffff;
  border: 1px solid #ffff;
  border-radius: 16px;
  box-shadow: 0px 4px 12px 0px rgba(30, 21, 83, 0.08);
  padding: 20px;
  overflow: hidden;
}

.grid-item-child-container {
  height: 100%;
  width: 100%;
  font-family: "Noto Sans KR";
}

.grid-item-child-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 36px;
}

.grid-item-child-header .icon-left {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 18px;
  font-weight: 700;
  text-transform: capitalize;
}

.grid-item-child-header .icon-left svg {
  width: 30px;
  height: 30px;
}

.grid-item-child-header .icon-right {
  margin-left: 10px;
  font-size: 18px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
}

.grid-item-child-header .icon-right button {
  margin-left: 10px;
}

.img-demo {
  width: 100%;
  height: 100%;
}

.item-content {
  display: flex;
  flex-direction: column;
  margin-top: 10px;
}

.item-content svg {
  width: 100%;
  height: 100%;
}

.description {
  font-size: 13px;
  font-weight: 400;
  line-height: 20px;
}
.content {
  overflow: hidden;
  display: flex;
  justify-content: center;
}
.user-image {
  display: flex;
}
</style>
