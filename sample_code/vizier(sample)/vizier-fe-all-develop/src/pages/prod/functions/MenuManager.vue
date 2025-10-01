<template>
  <div>
    <div class="menu-builder-page-container">
      <v-row>
        <v-col>
          <div class="action-menu-container">
            <v-btn
              color="#ff9800"
              rounded="0"
              size="large"
              @click="dialog = true"
              >ADD NEW ITEM</v-btn
            >
          </div>
          <v-card-text>
            <v-treeview
              v-if="menuItems"
              :items="menuItems"
              density="compact"
              return-object
              activatable
              open-all
            >
              <template #title="{ item }">
                <div
                  style="width: 100%; height: 100%; display: block"
                  @click.stop="setSelectedMenuItem(item)"
                >
                  {{ item.menuNm }}
                </div>
              </template>
              <template #prepend="{ item }">
                <div v-if="!item.children" style="width: 28px"></div>
              </template>
            </v-treeview>
          </v-card-text>
        </v-col>
      </v-row>
    </div>

    <v-dialog v-model="dialog" width="1000">
      <v-card title="Add new menu item">
        <BaseForm
          ref="baseFormRef"
          :is-add-mode="true"
          @set-dialog-status="setDialogStatus"
        />
        <template #actions>
          <v-btn class="ms-auto" text="Ok" @click="addMenuListItem"></v-btn>
        </template>
      </v-card>
    </v-dialog>
  </div>
</template>

<script setup lang="ts">
import { useMenuStore } from "@/store";
import { VTreeview } from "vuetify/labs/VTreeview";
import BaseForm from "@/components/prod/layout/BaseForm.vue";

const menuStore = useMenuStore();

const { menuItems } = storeToRefs(menuStore);
const dialog = ref(false);
const baseFormRef = ref(null);

// method

async function setSelectedMenuItem(elem) {
  menuStore.setIsShowDetailLayout(true);
  await nextTick();
  menuStore.setSelectedMenuItem(elem);
}

function addMenuListItem() {
  baseFormRef.value.addMenuItem();
}

function setDialogStatus(dialogStatus) {
  dialog.value = dialogStatus;
}
</script>

<style scoped>
.action-menu-container {
  display: flex;
  flex-direction: row-reverse;
  margin: 10px 20px;
}
</style>
