<template>
  <div class="box">
    <h3>Profile</h3>
    <div v-for="component in closedComponents" :key="component" class="card">
      {{ component }}
    </div>
  </div>
</template>

<script setup>
defineProps({
  closedComponents: {
    type: Array,
    required: true,
  },
});
</script>

<style scoped>
.box {
  background-color: #f5f5f5;
  border: 1px solid #ddd;
  padding: 20px;
  text-align: center;
  height: 100%;
}

.card {
  background-color: #fff;
  border: 1px solid #ddd;
  padding: 10px;
  margin-top: 10px;
}
</style>
