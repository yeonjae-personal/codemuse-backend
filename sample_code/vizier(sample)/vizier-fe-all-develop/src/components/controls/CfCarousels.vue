<script setup lang="ts">


const props = defineProps({
  items: {
    type: Array<any>,
    default: [],
  },
});
</script>

<template>
  <v-carousel>
    <v-carousel-item v-for="(item, index) in props.items" :key="index" cover>
      <v-sheet :color="item.colors" height="100%">
        <slot :item="item"></slot>
      </v-sheet>
    </v-carousel-item>
  </v-carousel>
</template>

<style scoped>
/*  */
</style>
