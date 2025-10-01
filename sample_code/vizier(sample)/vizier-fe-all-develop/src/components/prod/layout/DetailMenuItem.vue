<template>
  <v-container>
    <v-sheet
      class="d-flex justify-center flex-wrap text-center"
      height="1000"
      max-width="800"
      width="100%"
      rounded
    >
      <div>
        <h2 class="text-h4 font-weight-black text-orange mt-3">
          Detailed menu content
        </h2>

        <v-list dense>
          <v-list-item v-for="(value, key) in menuDetailSelected" :key="key">
            <template #prepend>
              <div class="text-h6 font-weight-medium mb-2">
                {{ key }} : {{ value }}
              </div>
            </template>
          </v-list-item>
        </v-list>
      </div>
    </v-sheet>
  </v-container>
</template>

<script setup lang="ts">
import { useMenuStore } from "@/store";

const menuStore = useMenuStore();

const { menuDetailSelected } = storeToRefs(menuStore);
</script>
