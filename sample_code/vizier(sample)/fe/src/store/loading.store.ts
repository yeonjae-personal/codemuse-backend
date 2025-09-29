const useLoadingStore = defineStore("loadingStore", () => {
  const loading = ref(false);

  const setLoading = (payload: boolean) => {
    loading.value = payload;
  };

  return {
    loading,
    setLoading,
  };
});

export default useLoadingStore;
