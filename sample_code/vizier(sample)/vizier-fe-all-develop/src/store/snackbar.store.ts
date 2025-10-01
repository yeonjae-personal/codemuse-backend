const useSnackbarStore = defineStore("snackbarStore", () => {
  const visible = ref(false);
  const message = ref("");
  const type = ref("success");

  const showSnackbar = (
    messageVal: string,
    typeVal: "success" | "warning" | "info" | "error"
  ) => {
    visible.value = true;
    message.value = messageVal;
    type.value = typeVal;
    setTimeout(() => {
      visible.value = false;
    }, 5000);
  };

  const hideSnackbar = () => {
    visible.value = false;
  };

  return {
    visible,
    message,
    type,
    showSnackbar,
    hideSnackbar,
  };
});
export default useSnackbarStore;
