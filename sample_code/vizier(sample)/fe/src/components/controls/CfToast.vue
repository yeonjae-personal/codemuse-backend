<script setup lang="ts">


const props = defineProps({
  title: {
    type: String,
    default: "",
  },
  text: {
    type: String,
    default: "",
  },
  type: {
    type: String as () => "error" | "success" | "warning" | "info" | undefined,
    default: undefined,
  },
  icon: {
    type: String,
    default: undefined,
  },
  variant: {
    type: String as () =>
      | "text"
      | "flat"
      | "elevated"
      | "tonal"
      | "outlined"
      | "plain"
      | undefined,
    default: undefined,
  },
  color: {
    type: String,
    default: "",
  },
  border: {
    type: String as () =>
      | boolean
      | "end"
      | "start"
      | "top"
      | "bottom"
      | undefined,
    default: undefined,
  },
  borderColor: {
    type: String,
    default: "",
  },
  closable: {
    type: Boolean,
    default: false,
  },
});
</script>

<template>
  <div class="cf-toast-block">
    <v-alert
      :icon="props.icon"
      :type="props.type"
      :title="props.title"
      :text="props.text"
      :variant="props.variant"
      :border="props.border"
      :border-color="props.borderColor"
      :closable="props.closable"
      class="cf-toast"
    ></v-alert>
  </div>
</template>

<style scoped>
.cf-toast-block {
  position: fixed;
  z-index: 9999;
  width: 100%;
}

.cf-toast-block.top-left,
.cf-toast-block.top-center,
.cf-toast-block.top-right {
  top: 0;
}

.cf-toast-block.bottom-left,
.cf-toast-block.bottom-center,
.cf-toast-block.bottom-right {
  bottom: 0;
}

.cf-toast-block.top-left .cf-toast,
.cf-toast-block.bottom-left .cf-toast {
  float: left;
}
.cf-toast-block.top-center .cf-toast,
.cf-toast-block.bottom-center .cf-toast {
  margin: 0 auto;
}
.cf-toast-block.top-right .cf-toast,
.cf-toast-block.bottom-right .cf-toast {
  float: right;
}

.cf-toast {
  min-width: 450px;
  width: fit-content;
}
.cf-toast-block.relative {
  position: relative;
}
.cf-toast-block.relative .cf-toast {
  width: 100%;
}

.cf-toast-block.center {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.cf-toast-block.center .cf-toast {
  margin: 0 auto;
}
</style>
