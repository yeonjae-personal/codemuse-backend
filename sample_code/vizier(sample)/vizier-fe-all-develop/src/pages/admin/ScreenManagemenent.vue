<template>
  <ScreenTable class="h-[462px]" @select-scrn-id-item="getScrnId" />
    <UrlTable v-if="scrnId" :scrn-id="scrnId" />
</template>

<script setup lang="ts">
import UrlTable from "@/pages/admin/subs/screen/UrlTable.vue";
import ScreenTable from "@/pages/admin/subs/screen/ScreenTable.vue";

const scrnId = ref(null);

const getScrnId = (scrnIdValue) => {
  
  scrnId.value = scrnIdValue;
};
</script>

<style scoped>
.custom-text-field .v-label {
  font-size: 13px;
}

select option {
  background-color: #fff;
  color: #363636;
  height: 40px;
}

.show-details {
  margin-left: unset !important;
  margin-top: 12px;
}

.option-show-details {
  min-width: 70px !important;
}

select option:hover {
  background-color: #ededed;
  color: red;
}

select option:checked {
  background-color: #ededed;
}

.select {
  max-width: 130px;
  height: 40px;
}

.active-icon {
  background-color: #fff;
  color: #ba1642;
}

.v-input__icon--append-inner {
  cursor: pointer;
}

/* .v-list-item {
  margin-bottom: 16px;
} */
.v-field {
  border-radius: 8px;
}

.v-text-field:deep() {
  font-size: 13px !important;
}

.v-field--variant-solo {
  box-shadow: none !important;
}

.v-field__input {
  font-size: 13px;
}

:deep(.v-label) {
  font-size: 13px !important;
}

:deep().v-input__details {
  display: none;
}
</style>
