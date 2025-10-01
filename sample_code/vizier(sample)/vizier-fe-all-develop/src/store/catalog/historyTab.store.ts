import { getUiHistoryTab } from "@/api/prod/productApi";
import {
  ParamUIHistoryTab,
  ProductHistory,
  SelectedItem,
} from "@/interfaces/prod/HistoryTab";

import { getValueGeneral } from "@/constants/generalField";

const useHistoryTabStore = defineStore("HistoryTab", () => {
  const history = ref<ProductHistory | null>(null);

  const selectedItem = computed(() => history.value?.selectedItem);

  const filterNullCommGroupCodes = computed(() => {
    const changed = history.value?.changed;
    if (!changed) return [];
    const commGroupCodes = changed.flatMap((change) =>
      change.records.flatMap(
        (record) =>
          record.fields?.flatMap((field) =>
            field.commGroupCode ? [field.commGroupCode] : []
          ) || []
      )
    );
    return Array.from(new Set(commGroupCodes)).concat("G00044");
  });

  async function fetchHistory(payload: ParamUIHistoryTab) {
    try {
      const response = await getUiHistoryTab(payload);
      history.value = updateCommGroupCode(response.data);
    } catch (error) {
      history.value = null;
      throw error;
    }
  }

  const updateCommGroupCode = (data: ProductHistory): ProductHistory => {
    data.changed?.forEach((change) => {
      change.records.forEach((record) => {
        record.fields?.forEach((field) => {
          if (field.commGroupCode === null) {
            field.commGroupCode = getValueGeneral(field.labelId);
          }
        });
      });
    });

    return data;
  };

  const setSelectedItem = (selectedItem: SelectedItem) => {
    history.value!.selectedItem = selectedItem;
  };
  // reset history state
  const resetHistory = () => {
    history.value = null;
  };

  return {
    history,
    selectedItem,
    setSelectedItem,
    fetchHistory,
    resetHistory,
    filterNullCommGroupCodes,
  };
});

export default useHistoryTabStore;
