<template>
  <div class="dashboard">
    <div class="grid-container" :class="{ maximized: maximizedComponent }">
      <SubscriberTop10
        v-if="!closedComponents.includes('SubscriberTop10')"
        @close="handleClose"
      />
      <ItemVolume
        v-if="!closedComponents.includes('ItemVolume')"
        @close="handleClose"
      />
      <RecentlyWorked
        v-if="!closedComponents.includes('RecentlyWorked')"
        @close="handleClose"
      />
      <ProfileComponent class="profile" :closed-components="closedComponents" />
      <MonthlyReportItems
        v-if="!closedComponents.includes('MonthlyReportItems')"
        @close="handleClose"
      />
      <MonthlyReportUsers
        v-if="!closedComponents.includes('MonthlyReportUsers')"
        @close="handleClose"
      />
      <UserImage
        v-if="!closedComponents.includes('UserImage')"
        @close="handleClose"
      />
    </div>
  </div>
</template>

<script setup>
import SubscriberTop10 from "@/components/prod/dashboard/SubscriberTop10.vue";
import ItemVolume from "@/components/prod/dashboard/ItemVolume.vue";
import RecentlyWorked from "@/components/prod/dashboard/RecentlyWorked.vue";
import MonthlyReportItems from "@/components/prod/dashboard/MonthlyReportItems.vue";
import MonthlyReportUsers from "@/components/prod/dashboard/MonthlyReportUsers.vue";
import UserImage from "@/components/prod/dashboard/UserImage.vue";
import ProfileComponent from "@/components/prod/dashboard/ProfileComponent.vue";

const closedComponents = ref([]);
const maximizedComponent = ref(null);

const handleClose = (componentName) => {
  closedComponents.value.push(componentName);
};
</script>

<style scoped>
html,
body {
  margin: 0;
  padding: 0;
  height: 100%;
  overflow: hidden;
}

.dashboard {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%;
  width: 100%;
  background-color: #f0f0f0;
}

.header {
  width: 100%;
  text-align: center;
  padding: 20px 0;
  font-size: 24px;
  font-weight: bold;
  background-color: #fff;
  border-bottom: 1px solid #ddd;
}

.title {
  display: inline-block;
  margin-top: -10px; /* Adjust this value to move the text up or down */
}

.grid-container {
  display: grid;
  grid-template-columns: repeat(3, 1fr) 1fr;
  grid-template-rows: repeat(2, 1fr);
  gap: 10px;
  width: 100%;
  height: calc(100% - 60px); /* Adjust height to account for header */
  box-sizing: border-box;
  padding: 20px;
}

.grid-container.maximized {
  grid-template-columns: 1fr;
  grid-template-rows: 1fr;
}

.grid-container > div {
  overflow: hidden;
}

.profile {
  grid-row: span 2;
}
</style>
