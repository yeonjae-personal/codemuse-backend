import cloneDeep from "lodash-es/cloneDeep";
import { useSnackbarStore } from "@/store";
import { getLabelList, getLanguageLabelList } from "@/api/prod/labelApi";
import {
  DEFAULT_SEARCH_PARAMS,
  DEFAULT_PAGINATION,
} from "@/constants/admin/label";
import type {
  ILabelSearchParams,
  ILabelItem,
  IPagination,
  ILanguageList,
} from "@/interfaces/admin/label-management";

const useLabelStore = defineStore("labelStore", () => {
  const { showSnackbar } = useSnackbarStore();
  const listLabel = ref<ILabelItem[]>([]);
  const listLabelTemp = ref<ILabelItem[]>([]);
  const selectedLabel = ref<ILabelItem | null>(null);
  const listLanguageLabel = ref<ILanguageList[]>([]);
  const isAddNew = ref<boolean>(false);
  const isOpenPopup = ref<boolean>(false);
  const isEditing = ref<boolean>(false);
  const componentKey = ref<number>(0);

  const searchParams = reactive<ILabelSearchParams>(
    cloneDeep(DEFAULT_SEARCH_PARAMS)
  );

  const searchParamsHistory = ref<ILabelSearchParams>(
    cloneDeep(DEFAULT_SEARCH_PARAMS)
  );

  const pagination = reactive<IPagination>(cloneDeep(DEFAULT_PAGINATION));

  const createNewLabelItem = (): ILabelItem => {
    return {
      labelId: "product_platform.auto_generation",
      items: listLanguageLabel.value.map((item) => ({
        langCode: item.langCode,
        regionCode: item.regionCode,
        labelCode: "",
        labelDscr: "",
        labelName: "",
      })),
    };
  };

  const getListLabel = async (
    isPagingClick: boolean = false
  ): Promise<void> => {
    try {
      const paramsSearch = { ...searchParams };
      if (isPagingClick) {
        Object.assign(paramsSearch, {
          ...searchParamsHistory.value,
          page: searchParams.page,
          size: searchParams.size,
        });
      } else {
        searchParamsHistory.value = { ...searchParams };
      }
      const response = await getLabelList(paramsSearch);
      listLabel.value = response.data.elements;
      pagination.currentPage = searchParams.page;
      pagination.pageSize = searchParams.size;
      pagination.totalItems = response.data.totalElements;
      pagination.totalSearchItems = response.data.totalElements;
      pagination.totalPages = Math.ceil(
        response.data.totalElements / searchParams.size
      );
    } catch (error: any) {
      showSnackbar(error.errorMsg, "error");
    }
  };

  const getListLanguageLabel = async (): Promise<void> => {
    try {
      const { data } = await getLanguageLabelList();
      listLanguageLabel.value = data;
    } catch (error: any) {
      showSnackbar(error.errorMsg, "error");
    }
  };

  const addNewLabelItem = (): void => {
    if (isEditing.value) {
      isOpenPopup.value = true;
      return;
    }
    if (!isAddNew.value) {
      componentKey.value++;
      const newItem = createNewLabelItem();
      if (listLabel.value.length === pagination.pageSize) {
        listLabelTemp.value = cloneDeep(listLabel.value);
        const newArr = listLabel.value.slice(0, -1);
        listLabel.value = [newItem, ...newArr];
      } else {
        listLabel.value = [newItem, ...listLabel.value];
      }
      selectedLabel.value = newItem;
      isAddNew.value = true;
    }
  };

  const resetStore = (): void => {
    listLabel.value = [];
    listLabelTemp.value = [];
    listLanguageLabel.value = [];
    selectedLabel.value = null;
    isAddNew.value = false;
    isOpenPopup.value = false;
    isEditing.value = false;
    componentKey.value = 0;
    Object.assign(searchParams, DEFAULT_SEARCH_PARAMS);
    Object.assign(pagination, DEFAULT_PAGINATION);
  };

  return {
    listLabel,
    listLabelTemp,
    listLanguageLabel,
    selectedLabel,
    isAddNew,
    isOpenPopup,
    isEditing,
    searchParams,
    pagination,
    componentKey,
    getListLabel,
    getListLanguageLabel,
    addNewLabelItem,
    resetStore,
  };
});

export default useLabelStore;
