import cloneDeep from "lodash-es/cloneDeep";
import { useSnackbarStore } from "@/store";
import {
  DEFAULT_SEARCH_PARAMS,
  DEFAULT_PAGINATION,
} from "@/constants/admin/rule-field";
import type {
  IRuleFieldSearchParams,
  IFieldItem,
  IPagination,
} from "@/interfaces/admin/rule-field";
import {
  getRuleFieldList,
  updateField,
} from "@/api/admin/rule-engine/ruleFieldApi";
import { v4 as uuidv4 } from "uuid";

const useRuleFieldStore = defineStore("ruleFieldStore", () => {
  const { showSnackbar } = useSnackbarStore();
  const listField = ref<IFieldItem[]>([]);
  const listFieldTemp = ref<IFieldItem[]>([]);
  const selectedField = ref<IFieldItem | null>(null);
  const isAddNew = ref<boolean>(false);
  const isOpenPopup = ref<boolean>(false);
  const isEditing = ref<boolean>(false);
  const editUuid = ref<string | null>(null);

  const searchParams = reactive<IRuleFieldSearchParams>(
    cloneDeep(DEFAULT_SEARCH_PARAMS)
  );
  const searchParamsHistory = ref<IRuleFieldSearchParams>(
    cloneDeep(DEFAULT_SEARCH_PARAMS)
  );

  const pagination = reactive<IPagination>(cloneDeep(DEFAULT_PAGINATION));

  const createNewFieldItem = (): IFieldItem => {
    return {
      fieldUuid: uuidv4(),
      fieldDispName: "",
      fieldKeyName: "",
      fieldDataType: "String",
      useYn: "Y",
      isAddNew: true,
    };
  };

  const getListField = async (
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
      const response = await getRuleFieldList(paramsSearch);
      listField.value = response.data.elements;
      listFieldTemp.value = cloneDeep(listField.value);
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

  const updateRuleField = async (field: IFieldItem) => {
    try {
      const response = await updateField({
        ...field,
        fieldUuid: field.isAddNew ? "" : field.fieldUuid,
      });
      return response;
    } catch (error: any) {
      showSnackbar(error.errorMsg, "error");
    }
  };

  const updateListField = () => {
    listField.value = cloneDeep(listFieldTemp.value);
  };

  const addNewFieldItem = (): void => {
    if (editUuid.value) {
      isOpenPopup.value = true;
      return;
    }
    if (!isAddNew.value) {
      const newItem = createNewFieldItem();
      if (listField.value.length === pagination.pageSize) {
        listFieldTemp.value = cloneDeep(listField.value);
        const newArr = listField.value.slice(0, -1);
        listField.value = [newItem, ...newArr];
      } else {
        listField.value = [newItem, ...listField.value];
      }
      selectedField.value = newItem;
      editUuid.value = newItem.fieldUuid;
    }
  };

  const resetStore = (): void => {
    listField.value = [];
    listFieldTemp.value = [];
    selectedField.value = null;
    isAddNew.value = false;
    isOpenPopup.value = false;
    isEditing.value = false;
    editUuid.value = null;
    Object.assign(searchParams, DEFAULT_SEARCH_PARAMS);
    Object.assign(pagination, DEFAULT_PAGINATION);
  };

  return {
    listField,
    listFieldTemp,
    selectedField,
    isAddNew,
    isOpenPopup,
    isEditing,
    searchParams,
    pagination,
    editUuid,
    getListField,
    addNewFieldItem,
    resetStore,
    updateRuleField,
    updateListField,
  };
});

export default useRuleFieldStore;
