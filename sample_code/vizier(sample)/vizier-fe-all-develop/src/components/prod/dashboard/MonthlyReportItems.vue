<template>
  <div :class="['box', { maximized: isMaximized }]">
    <div class="controls">
      <div class="icon maximize" @click="toggleMaximize"></div>
      <div class="icon close" @click="close"></div>
    </div>
    <h3 class="title">Monthly Report about Items</h3>
    <div id="chart"></div>
  </div>
</template>

<script setup>
const isMaximized = ref(false);

const toggleMaximize = () => {
  isMaximized.value = !isMaximized.value;
};

const close = () => {
  // 종료 기능 구현
};
</script>

<style scoped>
.box {
  background-color: #f5f5f5;
  border: 1px solid #ddd;
  padding: 20px;
  text-align: center;
  position: relative;
  transition: all 0.3s ease;
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

.title {
  margin-bottom: 20px;
}

#chart {
  width: 100%;
  height: 300px;
}

.maximized {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 10;
}
</style>
