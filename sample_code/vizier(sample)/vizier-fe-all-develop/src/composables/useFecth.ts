

type ResponseType = {
  data: any;
};

export const useFetch = (callApiFunction: (params: any) => Promise<any>) => {
  const data = ref<ResponseType | null>(null);
  const error = ref<unknown>(null);
  const loading = ref(false);

  const fetchData = async (params?: any) => {
    loading.value = true;
    try {
      data.value = await callApiFunction(params);
    } catch (err) {
      error.value = err;
    } finally {
      loading.value = false;
    }
  };

  return { data, error, loading, fetchData };
};
