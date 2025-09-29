<template>
  <Teleport to="body">
    <div
      v-if="message && visible"
      :class="[
        'fixed z-[9999] min-w-[288px] max-w-md  h-[53px] top-24 p-4 shadow-lg flex rounded-lg justify-between items-center space-x-2 transition-all duration-300',
        visible ? 'right-5 opacity-100' : 'right-[-100px] opacity-0 ',
        snackbarClass,
      ]"
      role="alert"
    >
      <div
        class="text-sm font-medium w-full break-words"
        v-html="message?.replaceAll('\n', '<br />')"
      />
      <CloseSnackbarIcon
        class="cursor-pointer"
        :color="iconColor"
        @click="closeSnackbar"
      /></div
  ></Teleport>
</template>

<script lang="ts">
import useSnackbarStore from "@/store/snackbar.store";

export default defineComponent({
  name: "SnackbarApp",
  setup() {
    const snackbarStore = useSnackbarStore();

    const { visible, message, type } = storeToRefs(snackbarStore);

    let timeoutId: number;

    const snackbarClass = computed(() => {
      switch (type.value) {
        case "success":
          return "border-success bg-[#ECFDF3] text-[#079455]";
        case "warning":
          return "border-warning bg-[#FEF6EE] text-[#E04F16]";
        case "info":
          return "border-info bg-[#E8F4FC] text-[#1570EF]";
        case "error":
          return "border-error bg-[#FEF3F2] text-[#C7291D]";
        default:
          return "";
      }
    });

    const iconColor = computed(() => {
      switch (type.value) {
        case "success":
          return "#079455";
        case "warning":
          return "#E04F16";
        case "info":
          return "#1570EF";
        case "error":
          return "#C7291D";
        default:
          return "#079455";
      }
    });

    const closeSnackbar = () => {
      snackbarStore.hideSnackbar();
    };

    onMounted(() => {
      timeoutId = window.setTimeout(() => {
        closeSnackbar();
      }, 5000);
    });

    onUnmounted(() => {
      clearTimeout(timeoutId);
    });

    return {
      visible,
      message,
      snackbarStore,
      snackbarClass,
      iconColor,
      closeSnackbar,
    };
  },
});
</script>

<style scoped lang="scss">
.border {
  &-success {
    border: 1px solid #abefc6;
  }
  &-warning {
    border: 1px solid #f9dbaf;
  }

  &-info {
    border: 1px solid #b2ddff;
  }
  &-error {
    border: 1px solid #fdced5;
  }
}
</style>
