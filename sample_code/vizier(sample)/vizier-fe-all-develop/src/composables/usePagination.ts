import { Paginate } from "@/interfaces/prod/Paginate";
export const usePaginate = (store: any, getItem: () => Promise<void>) => {
  const pagination = ref<Paginate>({
    currentPage: 1,
    perPage: 10,
  });

  const totalPages = computed<number>(() => {
    return store.getPagination.pageSize;
  });

  const totalSearchItems = computed<number>(() => {
    return store.getPagination.rows;
  });

  const updateTable = async (value: Paginate) => {
    pagination.value.currentPage = value.currentPage;
    pagination.value.perPage = value.perPage;
  };

  const handleChangePage = (newPage: number) => {
    pagination.value.currentPage = newPage;
  };

  const resetPagination = () => {
    pagination.value.currentPage = 1;
  };

  watch(
    () => pagination.value.currentPage,
    async () => {
      await getItem();
    }
  );
  watch(
    () => pagination.value.perPage,
    async (newVal) => {
      if (newVal) {
        pagination.value.currentPage = 1;
        await getItem();
      }
    }
  );

  return {
    pagination,
    totalPages,
    totalSearchItems,
    updateTable,
    handleChangePage,
    resetPagination,
  };
};
