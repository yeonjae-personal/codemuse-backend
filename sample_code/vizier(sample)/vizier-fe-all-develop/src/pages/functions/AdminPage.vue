<script setup lang="ts">
import CfTabs from "@/components/controls/CfTabs.vue";
import CfAvatar from "@/components/controls/CfAvatar.vue";
import CfDropdown from "@/components/controls/CfDropdown.vue";
import CfBadge from "@/components/controls/CfBadge.vue";
import { useUser, useGlobal } from "@/store";
import { CommonUtil } from "@/utils/common-util";
import { useI18n } from "vue-i18n";
import CfSpinner from "@/components/controls/CfSpinner.vue";

const { locale } = useI18n();

const userStore = useUser();
const globalStore = useGlobal();
const loading = ref(false);

const users = ref([
  // Data user
]);

const filteredUsers = ref<Array<any>>([
  // Data FilterData
]);

const { translateMessage } = CommonUtil.useTranslatedMessage();

const headerTable = ref({
  value: "All",
  label: translateMessage("user_management.lbl_all"),
});

const exTabs = ref([
  {
    value: "All",
    label: translateMessage("user_management.lbl_all"),
    slot: "one",
  },
  {
    value: "Master",
    label: translateMessage("user_management.lbl_master"),
    slot: "one",
  },
  {
    value: "Manager",
    label: translateMessage("user_management.lbl_manager"),
    slot: "one",
  },
  {
    value: "Developer",
    label: translateMessage("user_management.lbl_developer"),
    slot: "one",
  },
]);

const levels = ref([
  { value: "Master", label: translateMessage("user_management.lbl_master") },
  { value: "Manager", label: translateMessage("user_management.lbl_manager") },
  {
    value: "Developer",
    label: translateMessage("user_management.lbl_developer"),
  },
]);

const temporaryUpdatedUser = ref<Array<any>>([
  // variable use for update level.
]);

const updateUserLevel = (user: any, newLevel: any) => {
  // when change data from dropdown. user will push to temporaryupdateduser

  const existingUserIndex = temporaryUpdatedUser.value.findIndex(
    (us: any) => us.id === user.id
  );
  if (newLevel === null) {
    // If newLevel is null, remove the user from temporaryUpdatedUser
    if (existingUserIndex !== -1) {
      temporaryUpdatedUser.value.splice(existingUserIndex, 1);
      console.log(temporaryUpdatedUser.value);
    }
  } else {
    if (existingUserIndex !== -1) {
      temporaryUpdatedUser.value[existingUserIndex as number].level =
        newLevel.value;
    } else {
      temporaryUpdatedUser.value.push({
        id: user.id,
        level: newLevel.value,
      });
    }
    console.log(temporaryUpdatedUser.value);
  }
};

const filterUsers = (level: any) => {
  // filter function user
  filteredUsers.value = users.value.filter(
    (user: any) => user.level === level || level === "All"
  );
  switch (level) {
    case "Master":
      headerTable.value = {
        value: level,
        label: translateMessage("user_management.lbl_master"),
      };
      break;
    case "Manager":
      headerTable.value = {
        value: level,
        label: translateMessage("user_management.lbl_manager"),
      };
      break;
    case "Developer":
      headerTable.value = {
        value: level,
        label: translateMessage("user_management.lbl_developer"),
      };
      break;

    default:
      headerTable.value = {
        value: level,
        label: translateMessage("user_management.lbl_all"),
      };
      break;
  }
  temporaryUpdatedUser.value = [];
};

onMounted(async () => {
  // get all data
  try {
    const response = await userStore.getAllUsers();
    users.value = response.data;
    filteredUsers.value = response.data;
    loading.value = true;
  } catch (err: any) {
    globalStore.setToastInfor(
      {
        title: translateMessage("common.msg_notification"),
        text: err.message,
        border: "start",
        borderColor: "white",
        type: "error",
        icon: "$error",
      },
      5000
    );
  }
});

const deleteUser = async (user: any) => {
  const objectAlert: any = {
    title: translateMessage("common.msg_confirm"),
    text: translateMessage("user_management.msg_confirm_delete"),
    width: "610",
  };
  const result = await globalStore.openAlertConfirm(objectAlert);
  if (result) {
    if (userStore.user.id === user.id) {
      globalStore.setToastInfor(
        {
          title: translateMessage("common.msg_notification"),
          text: translateMessage("user_management.msg_error_delete"),
          border: "start",
          borderColor: "white",
          type: "error",
          icon: "$error",
        },
        5000
      );
    } else {
      try {
        const response = await userStore.deleteUser(user.id);
        console.log(response);
        filteredUsers.value = filteredUsers.value.filter(
          (us) => us.id !== user.id
        );
        const updatedUsers = await userStore.getAllUsers();
        users.value = updatedUsers.data;
        globalStore.setToastInfor(
          {
            title: translateMessage("common.msg_notification"),
            text: translateMessage("user_management.msg_success_delete"),
            border: "start",
            borderColor: "white",
            type: "success",
            icon: "$success",
          },
          5000
        );
      } catch (err: any) {
        globalStore.setToastInfor(
          {
            title: translateMessage("common.msg_notification"),
            text: err.message,
            border: "start",
            borderColor: "white",
            type: "error",
            icon: "$error",
          },
          5000
        );
      }
    }
  }
};

const handleChangeLevelForAllRecords = async () => {
  try {
    const dataToUpdate = temporaryUpdatedUser.value.map((user) => ({
      id: user.id,
      level: user.level,
      updateBy: userStore.user.id,
    }));

    const res = await userStore.changeLevelForAllRecords(dataToUpdate);
    console.log(res);
    isReset.value = true;

    const updatedUsers = await userStore.getAllUsers();
    users.value = updatedUsers.data;
    filteredUsers.value = updatedUsers.data.filter(
      (user: any) =>
        user.level === headerTable.value.value ||
        headerTable.value.value === "All"
    );
    isReset.value = false;

    globalStore.setToastInfor(
      {
        title: translateMessage("common.msg_notification"),
        text: translateMessage("user_management.msg_success_update"),
        border: "start",
        borderColor: "white",
        type: "success",
        icon: "$success",
      },
      5000
    );
  } catch (err: any) {
    console.error("Error changing level for all records:", err);
    globalStore.setToastInfor(
      {
        title: translateMessage("common.msg_notification"),
        text: err.message,
        border: "start",
        borderColor: "white",
        type: "error",
        icon: "$error",
      },
      5000
    );
  }
};

const getLevelName = (level: string) => {
  switch (level) {
    case "Master":
      return translateMessage("user_management.lbl_master");
      break;
    case "Manager":
      return translateMessage("user_management.lbl_manager");
      break;
    case "Developer":
      return translateMessage("user_management.lbl_developer");
      break;

    default:
      return translateMessage("user_management.lbl_all");
      break;
  }
};

watch(
  () => locale.value,
  () => {
    updateLanguage();
  }
);

const updateLanguage = () => {
  exTabs.value = [
    {
      value: "All",
      label: translateMessage("user_management.lbl_all"),
      slot: "one",
    },
    {
      value: "Master",
      label: translateMessage("user_management.lbl_master"),
      slot: "one",
    },
    {
      value: "Manager",
      label: translateMessage("user_management.lbl_manager"),
      slot: "one",
    },
    {
      value: "Developer",
      label: translateMessage("user_management.lbl_developer"),
      slot: "one",
    },
  ];
  levels.value = [
    { value: "Master", label: translateMessage("user_management.lbl_master") },
    {
      value: "Manager",
      label: translateMessage("user_management.lbl_manager"),
    },
    {
      value: "Developer",
      label: translateMessage("user_management.lbl_developer"),
    },
  ];
};

const isReset = ref(false);
</script>

<template>
  <div class="p-4 mx-auto prose md:px-6 prose-indigo sm:rounded-md">
    <h1 class="mt-3 text-3xl font-extrabold tracking-tight text-slate-900">
      {{ $t("user_management.lbl_title_page") }}
    </h1>

    <cf-tabs
      :tabs="exTabs"
      bg-color="primary"
      align-tabs="title"
      selected="one"
      @tab-change="filterUsers"
    >
      <template v-for="t in exTabs" :key="t.value" #[t.slot]>
        <div v-if="t.slot === 'one'" class="max-h-700 overflow-y-auto">
          <h4>
            {{ $t("user_management.lbl_level_user_list", [headerTable.label]) }}
          </h4>
          <div v-if="loading === true">
            <template v-for="user in filteredUsers" :key="user.id">
              <div class="flex justify-between items-center">
                <div class="flex gap-4 items-center w-1/6">
                  <cf-avatar
                    image="https://static.thenounproject.com/png/363640-200.png"
                  ></cf-avatar>
                  <span>{{ user.username }}</span>
                </div>
                <cf-badge
                  :content="user.level ? getLevelName(user.level) : 'User'"
                  color="error"
                ></cf-badge>
                <div class="w-2/5 mt-6">
                  <cf-dropdown
                    :label="$t('user_management.lbl_level')"
                    variant="solo"
                    :items="levels"
                    item-title="label"
                    item-value="value"
                    :model="user.selectedLevel"
                    :is-reset-value="isReset"
                    @update:model="updateUserLevel(user, $event)"
                  ></cf-dropdown>
                </div>
                <cf-button
                  :label="$t('common.btn_delete')"
                  @click="deleteUser(user)"
                />
              </div>
            </template>
          </div>
          <div v-else class="flex justify-center">
            <cf-spinner indeterminate color="pink"> </cf-spinner>
          </div>
        </div>
        <div class="flex justify-end mt-4">
          <button
            v-if="temporaryUpdatedUser.length > 0"
            class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-700"
            @click="handleChangeLevelForAllRecords"
          >
            {{ $t("common.btn_save") }}
          </button>
        </div>
      </template>
    </cf-tabs>
  </div>
</template>

<style scoped>
/*  */
</style>
