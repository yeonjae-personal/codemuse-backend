import { getAllLanguages } from "@/api/prod/labelApi";
import i18n from "@/i18n";

interface LabelItem {
  langCode: string;
  labelName: string;
  labelDscr: string;
}

interface LabelData {
  labelId: string;
  items: LabelItem[];
}

interface NestedTranslations {
  [key: string]: any;
}

interface MultiLanguageTranslations {
  [langCode: string]: NestedTranslations;
}

function convert(data: LabelData[]): MultiLanguageTranslations {
  const result: MultiLanguageTranslations = {};

  data.forEach((item) => {
    const labelIdParts = item.labelId.split(".");
    const labelName = labelIdParts.pop(); // Lấy phần cuối của labelId

    item.items.forEach((subItem) => {
      const { langCode, labelName: subLabelName, labelDscr } = subItem;
      if (!result[langCode as string]) {
        result[langCode as string] = {};
      }

      let currentLevel = result[langCode as string];
      labelIdParts.forEach((part) => {
        if (!currentLevel[part as string]) {
          currentLevel[part as string] = {};
        }
        currentLevel = currentLevel[part as string];
      });

      if (labelName) {
        currentLevel[labelName as string] = subLabelName;
        if (labelDscr) currentLevel[`${labelName}Desc` as string] = labelDscr;
      }
    });
  });

  return result;
}

export async function fetchAndSaveTranslations() {
  try {
    const cachedTranslations = localStorage.getItem("translations");
    let translations: MultiLanguageTranslations = {};
    if (cachedTranslations) {
      translations = JSON.parse(cachedTranslations);
      applyTranslations(translations);
    }

    // console.time("fetchAndSaveTranslations");
    const response: any = await getAllLanguages();
    // console.timeEnd("fetchAndSaveTranslations");
    const apiTranslations: MultiLanguageTranslations = convert(
      response.data || []
    );

    if (!cachedTranslations) {
      applyTranslations(apiTranslations);
      localStorage.setItem("translations", JSON.stringify(apiTranslations));
      return;
    }

    if (!areTranslationsEqual(translations, apiTranslations)) {
      applyTranslations(apiTranslations);
      localStorage.setItem("translations", JSON.stringify(apiTranslations));
      return;
    }
  } catch (error) {
    console.error("Failed to fetch translations:", error);
  }
}

function applyTranslations(translations: MultiLanguageTranslations) {
  Object.keys(translations).forEach((langCode) => {
    i18n.global.setLocaleMessage(langCode, translations[langCode as string]);
  });
}

export function updateLabelI18n(data, labelId = "") {
  const translations: MultiLanguageTranslations = convert(data || []);
  Object.keys(translations).forEach((langCode) => {
    const currentMessage = i18n.global.getLocaleMessage(langCode);
    if (labelId) delete currentMessage[labelId as string];
    i18n.global.setLocaleMessage(langCode, {
      ...currentMessage,
      ...translations[langCode as string],
    });
  });
}

function areTranslationsEqual(
  cached: MultiLanguageTranslations,
  api: MultiLanguageTranslations
): boolean {
  const langCodes = new Set([...Object.keys(cached), ...Object.keys(api)]);

  for (const langCode of langCodes) {
    const cachedLang = cached[langCode as string] || {};
    const apiLang = api[langCode as string] || {};

    if (!deepEqual(cachedLang, apiLang)) {
      return false;
    }
  }

  return true;
}

function deepEqual(obj1: any, obj2: any): boolean {
  if (obj1 === obj2) return true;
  if (
    typeof obj1 !== "object" ||
    obj1 === null ||
    typeof obj2 !== "object" ||
    obj2 === null
  ) {
    return false;
  }

  const keys1 = Object.keys(obj1);
  const keys2 = Object.keys(obj2);

  if (keys1.length !== keys2.length) return false;

  for (const key of keys1) {
    if (
      !keys2.includes(key) ||
      !deepEqual(obj1[key as string], obj2[key as string])
    ) {
      return false;
    }
  }

  return true;
}
