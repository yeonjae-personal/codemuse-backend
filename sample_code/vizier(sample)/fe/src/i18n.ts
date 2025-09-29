import { createI18n, I18nOptions, useI18n as baseUseI18n } from "vue-i18n";

const i18nOptions: I18nOptions = {
  legacy: false,
  locale: "ko",
  fallbackLocale: "ko",
  fallbackWarn: false,
  missingWarn: false,
  messages: {},
};

export const useI18n = () => {
  const { t: translateMessage, ...rest } = baseUseI18n(i18nOptions);
  return { translateMessage, ...rest };
};

const i18n = createI18n(i18nOptions);

export default i18n;
