<template>
  <div class="base-form-container">
    <h2 v-if="!isAddMode" class="text-h4 font-weight-black text-orange">
      Detail management menu
    </h2>
    <div
      v-if="!selectedMenuItem && isAddMode === false"
      class="text-h6 text-grey-lighten-1 font-weight-light"
      style="align-self: center"
    >
      Select a Menu Item
    </div>
    <v-card v-else style="padding: 10px">
      <div class="action-button-group">
        <v-btn
          v-if="!isAddMode"
          rounded="0"
          color="#ff9800"
          size="large"
          @click="isEditMode = true"
          >UPDATE</v-btn
        >
        <v-btn
          v-if="!isAddMode"
          :disabled="isEditMode"
          color="#ff9800"
          rounded="0"
          size="large"
          @click="removeMenuItem"
          >DELETE</v-btn
        >
      </div>
      <v-alert
        v-if="isFormInvalid"
        color="error"
        icon="$error"
        title="Alert title"
        text="Please input value for required field"
      ></v-alert>
      <v-text-field
        v-model="titleFieldValue"
        :readonly="isFormDisabled"
        :counter="10"
        :error="v$.titleFieldValue.$error"
        label="Title"
        hide-details
      ></v-text-field>

      <v-select
        v-model="menuLevelFieldValue"
        :readonly="isFormDisabled"
        :error="v$.menuLevelFieldValue.$error"
        :items="[1, 2, 3]"
        label="Menu Level"
      ></v-select>

      <v-select
        v-if="isDisplayFirstLevelMenuItemSelect && menuLevelFieldValue"
        v-model="selectedFirstLevelMenuValue"
        :items="firstLevelElementOptionList"
        :error="v$.isFirstLevelMenuItemRequired.$invalid"
        :readonly="isFormDisabled"
        item-title="menuNm"
        item-value="menuId"
        label="First Level Menu Parent"
      ></v-select>

      <v-select
        v-if="isDisplaySecondLevelMenuItemSelect"
        v-model="selectedSecondLevelMenuValue"
        :items="secondLevelElementOptionList"
        :disabled="isFormDisabled"
        :error="v$.isSecondLevelMenuItemRequired.$invalid"
        item-title="menuNm"
        item-value="menuId"
        label="Second Level Menu Parent"
      >
      </v-select>
      <v-btn
        v-if="isEditMode"
        width="100%"
        color="#ff9800"
        rounded="0"
        size="large"
        @click="updateMenuItem"
        >Save</v-btn
      >
    </v-card>
  </div>
</template>

<script setup lang="ts">
import { useMenuStore } from "@/store";
import { v4 as uuidv4 } from "uuid";
import { useVuelidate } from "@vuelidate/core";
import { required } from "@vuelidate/validators";

const menuStore = useMenuStore();

// Props
const props = defineProps({
  isAddMode: { type: Boolean, required: true },
});

const emit = defineEmits(["setDialogStatus"]);

// Data
const { menuItems, selectedMenuItem } = storeToRefs(menuStore);
const titleFieldValue = ref("");
const menuLevelFieldValue = ref(0);
const selectedFirstLevelMenuValue = ref("");
const selectedSecondLevelMenuValue = ref("");
const isEditMode = ref(false);
const initialSelectedMenuItem = ref(null as any);
const rules = computed(() => {
  return {
    titleFieldValue: { required },
    menuLevelFieldValue: { required },
    isFirstLevelMenuItemRequired: () =>
      !(menuLevelFieldValue.value !== 1 && !selectedFirstLevelMenuValue.value),
    isSecondLevelMenuItemRequired: () =>
      !(menuLevelFieldValue.value === 3 && !selectedSecondLevelMenuValue.value),
  };
});

const v$ = useVuelidate(rules, {
  titleFieldValue,
  menuLevelFieldValue,
  isFirstLevelMenuItemRequired: selectedFirstLevelMenuValue,
  isSecondLevelMenuItemRequired: selectedSecondLevelMenuValue,
});

// Computed
const firstLevelElementOptionList = computed(() => {
  if (selectedMenuItem.value && menuItems.value) {
    if (
      menuLevelFieldValue.value === 3 &&
      selectedMenuItem.value.menuLv === 3
    ) {
      return menuItems.value.filter(
        (element) => element.menuId === selectedMenuItem.value.grandParentId
      );
    } else {
      return menuItems.value.filter(
        (element) =>
          element.menuLv === 1 &&
          element.menuId !== selectedMenuItem.value.menuId
      );
    }
  } else {
    return menuItems.value.filter((element) => element.menuLv === 1);
  }
});

const secondLevelElementOptionList = computed(() => {
  if (menuLevelFieldValue.value && menuItems.value) {
    if (menuLevelFieldValue.value === 3 && firstLevelElementOptionList.value) {
      let baseList = firstLevelElementOptionList.value.filter(
        (element) => element.menuId === selectedFirstLevelMenuValue.value
      );
      let array;
      if (!props.isAddMode) {
        array = baseList[0]?.children
          ? baseList[0].children.filter(
              (element: any) => element.menuId !== selectedMenuItem.value.menuId
            )
          : [];
      } else {
        array = baseList[0]?.children ? baseList[0]?.children : [];
      }

      return array;
    } else {
      return [];
    }
  }
  return [];
});

const isFormDisabled = computed(() => {
  return !isEditMode.value && !props.isAddMode;
});

const isMenuItemLevelChanged = computed(() => {
  return menuLevelFieldValue.value !== initialSelectedMenuItem.value.menuLv;
});

const isDisplaySecondLevelMenuItemSelect = computed(() => {
  return menuLevelFieldValue.value === 3 && isEditMode;
});

const isDisplayFirstLevelMenuItemSelect = computed(() => {
  return menuLevelFieldValue.value !== 1 && isEditMode;
});

const isFormInvalid = computed(() => {
  return v$.value.$errors.length;
});
// const menuItemList = computed(() => store.state.menuItems)

// Watcher
watch(
  () => selectedMenuItem,
  (newVal) => {
    isEditMode.value = false;
    initialSelectedMenuItem.value = newVal.value;
    if (!newVal.value) {
      return;
    }
    selectedFirstLevelMenuValue.value = "";
    selectedSecondLevelMenuValue.value = "";
    titleFieldValue.value = newVal.value.menuNm;
    menuLevelFieldValue.value = newVal.value.menuLv;

    if (newVal.value.menuLv === 1) {
      selectedFirstLevelMenuValue.value = newVal.value.menuId;
    }

    if (newVal.value.menuLv === 2) {
      selectedFirstLevelMenuValue.value = newVal.value.parentId;
      selectedSecondLevelMenuValue.value = newVal.value.menuId;
    }

    if (newVal.value.menuLv === 3) {
      selectedSecondLevelMenuValue.value = newVal.value.parentId;
      selectedFirstLevelMenuValue.value = newVal.value.grandParentId;
    }
  },
  { deep: true }
);

watch(
  () => menuLevelFieldValue,
  (newVal) => {
    if (!newVal.value || isFormDisabled.value) {
      return;
    }
    selectedSecondLevelMenuValue.value = "";
    selectedFirstLevelMenuValue.value = "";
  },
  { deep: true }
);

// Methods
function updateMenuItem() {
  v$.value.$validate();
  if (isFormInvalid.value) {
    return;
  }
  let payload = constructUpdateMenuItemPayload();
  menuStore.updateMenuItem(payload);
  if (isMenuItemLevelChanged.value) {
    menuStore.removeMenuItem(initialSelectedMenuItem.value);
  }
  resetSelectedMenuItem();
}

function removeMenuItem() {
  let payload = constructUpdateMenuItemPayload();
  menuStore.removeMenuItem(payload.menuId);
  resetSelectedMenuItem();
}

function addMenuItem() {
  v$.value.$validate();
  if (isFormInvalid.value) {
    return;
  }
  let payload = constructAddMenuItemPayload();
  menuStore.addMenuItem(payload);
  resetSelectedMenuItem();
  emit("setDialogStatus", false);
}

function resetSelectedMenuItem() {
  menuStore.setSelectedMenuItem(null);
}

function constructUpdateMenuItemPayload() {
  let payload = {
    menuId: selectedMenuItem.value.menuId,
    menuNm: titleFieldValue.value,
    menuLv: menuLevelFieldValue.value,
    grandParentId:
      menuLevelFieldValue.value === 3
        ? selectedFirstLevelMenuValue.value
        : null,
    parentId:
      menuLevelFieldValue.value === 2
        ? selectedFirstLevelMenuValue.value
        : menuLevelFieldValue.value === 3
          ? selectedSecondLevelMenuValue.value
          : null,
    children: initialSelectedMenuItem.value.children,
  };
  return payload;
}

function constructAddMenuItemPayload() {
  let payload = {
    menuId: uuidv4(),
    menuNm: titleFieldValue.value,
    menuLv: menuLevelFieldValue.value,
    grandParentId:
      menuLevelFieldValue.value === 3
        ? selectedFirstLevelMenuValue.value
        : null,
    parentId:
      menuLevelFieldValue.value === 2
        ? selectedFirstLevelMenuValue.value
        : menuLevelFieldValue.value === 3
          ? selectedSecondLevelMenuValue.value
          : null,
    children: null,
  };
  return payload;
}

defineExpose({
  addMenuItem,
});
</script>

<style scoped>
.base-form-container {
  display: flex;
  flex-direction: column;
  gap: 10px;
  height: 100%;
}

.action-button-group {
  display: flex;
  gap: 10px;
  margin: 10px;
  flex-direction: row-reverse;
}
</style>
