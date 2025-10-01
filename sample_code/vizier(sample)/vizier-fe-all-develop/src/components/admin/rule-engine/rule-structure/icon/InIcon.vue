<template>
  <div class="in-icon" :style="`color: ${color}`">IN</div>
</template>

<script lang="ts" setup>
type Props = {
  color?: string;
};

withDefaults(defineProps<Props>(), {
  color: "#6B6D70",
});
</script>

<style lang="scss" scoped>
.in-icon {
  font-family: Noto Sans KR;
  font-weight: 500;
  font-size: 11px;
}
</style>
