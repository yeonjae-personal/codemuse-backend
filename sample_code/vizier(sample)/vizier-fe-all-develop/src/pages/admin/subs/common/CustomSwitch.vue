<template>
  <v-switch
    
    hide-details
    inset
    class="custom-switch"
    color="#D9325A"
  ></v-switch>
</template>

<script setup lang="ts">
</script>

<style lang="scss" scoped>
:deep(.custom-switch .v-input--switch__thumb) {
  height: 16px;
  width: 16px;
  left: -1px !important;
}

:deep(.custom-switch .v-input--switch__track) {
  height: 20px;
  min-width: 36px;
}

:deep(.custom-switch .v-input--switch__track) {
  opacity: 1;
  background-color: rgb(220, 224, 228);
}
</style>