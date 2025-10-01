<template>
  <div class="box">
    <div class="controls">
      <div class="icon maximize" @click="maximize"></div>
      <div class="icon close" @click="close"></div>
    </div>
    Recently Worked
  </div>
</template>

<script setup>
const emit = defineEmits(["close"]);
const isMaximized = ref(false);

const maximize = () => {
  isMaximized.value = !isMaximized.value;
};

const close = () => {
  emit("close", "RecentlyWorked");
};
</script>

<style scoped>
.box {
  background-color: #f5f5f5;
  border: 1px solid #ddd;
  padding: 20px;
  text-align: center;
  position: relative;
}

.controls {
  position: absolute;
  top: 10px;
  right: 10px;
  display: flex;
  gap: 10px;
}

.icon {
  width: 20px;
  height: 20px;
  background-color: #888;
  mask-size: cover;
  cursor: pointer;
}

.icon.maximize {
  mask-image: url("data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjAiIGhlaWdodD0iMjAiIHZpZXdCb3g9IjAgMCAyMCAyMCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHBhdGggZD0iTTEgMUgxOU0xIDFWMThIMTkiIHN0cm9rZT0iI0ZGRiIvPgo8L3N2Zz4K");
}

.icon.close {
  mask-image: url("data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjAiIGhlaWdodD0iMjAiIHZpZXdCb3g9IjAgMCAyMCAyMCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHBhdGggZD0iTTEgMUwxOSAxOU0xOSAxTDEgMTkiIHN0cm9rZT0iI0ZGRiIvPgo8L3N2Zz4K");
}

.icon:hover {
  background-color: #555;
}
</style>
