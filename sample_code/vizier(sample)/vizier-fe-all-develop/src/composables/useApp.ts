import { useSnackbarStore } from "@/store";
import { getLanguageLabelList } from "@/api/prod/labelApi";
import type { ILanguageList } from "@/interfaces/admin/label-management";

export default function useApp() {
  const { showSnackbar } = useSnackbarStore();

  const languageList = ref<ILanguageList[]>([]);

  onBeforeMount(() => {
    getListLanguage();
  });

  const getListLanguage = async (): Promise<void> => {
    try {
      const { data } = await getLanguageLabelList();
      languageList.value = data;
    } catch (error: any) {
      showSnackbar(error.errorMsg, "error");
    }
  };

  return { languageList };
}
