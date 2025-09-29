import { useI18n } from "@/i18n";

export class CommonUtil {
  public static readonly STRING_EMPTY = "";

  public static useTranslatedMessage = () => {
    const { translateMessage, ...rest } = useI18n();
    return { translateMessage, ...rest };
  };
}

export const removeTabMenuWhenNoTabName = () => {
  const tabMenu = JSON.parse(localStorage.getItem("tabMenu") || "[]");
  if (tabMenu.length) {
    const invalid =
      tabMenu.map((item) => item.tabName).filter((item) => !item).length > 0;
    if (invalid) localStorage.removeItem("tabMenu");
  }
};
