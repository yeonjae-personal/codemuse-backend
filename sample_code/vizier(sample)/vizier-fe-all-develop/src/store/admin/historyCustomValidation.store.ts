import { getCustomValidationHistory } from "@/api/prod/customValidationApi";
import {
  CustomValidationHistory,
  SelectedItem,
  ParamHistoryCustomValidation,
} from "@/interfaces/prod/HistoryCustomValidation";

import { getValueGeneral } from "@/constants/generalField";

const useHistoryCustomValidationStore = defineStore(
  "HistoryCustomValidation",
  () => {
    const history = ref<CustomValidationHistory | null>(null);

    const selectedItem = computed(() => history.value?.selectedItem);

    const filterNullCommGroupCodes = computed(() => {
      const changed = history.value?.changed;
      if (!changed) return [];
      const commGroupCodes = changed.flatMap((change) =>
        change.records.flatMap(
          (record) =>
            record.values?.flatMap((field) =>
              field.commGroupCode ? [field.commGroupCode] : []
            ) || []
        )
      );
      return Array.from(new Set(commGroupCodes)).concat("G00044");
    });

    async function fetchHistory(payload: ParamHistoryCustomValidation) {
      try {
        const response = await getCustomValidationHistory(payload);
        history.value = updateCommGroupCode(response.data);
      } catch (error) {
        history.value = null;
        throw error;
      }
    }

    const updateCommGroupCode = (
      data: CustomValidationHistory
    ): CustomValidationHistory => {
      data.changed?.forEach((change) => {
        change.records.forEach((record) => {
          record.values?.forEach((field) => {
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
  }
);

export default useHistoryCustomValidationStore;
