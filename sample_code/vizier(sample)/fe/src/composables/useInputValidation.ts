import i18n from "@/i18n";

const $t = i18n.global.t;
interface ValidationOptions {
  maxLength?: number | string | null;
  required?: boolean;
  noSpecialChars?: boolean;
  onlyNumbers?: boolean;
  isEmail?: boolean;
  engKorNumRule?: boolean;
  engKorRule?: boolean;
  engNumRule?: boolean;
  engKorNumHasSpaceRule?: boolean;
  engKorHasSpaceRule?: boolean;
  pwRule?: boolean;
  onlyCharsWithTwoChars?: boolean;
}

type RuleFunction = (value: string) => boolean | string;

const checkValueLength = (value: string, maxLength: number | string) => {
  if (value === undefined) {
    return true;
  }

  if (typeof value === "string" || Array.isArray(value)) {
    if (typeof maxLength === "string") {
      return value.length <= parseInt(maxLength);
    }
    return value.length <= maxLength;
  }

  return true;
};

export function useInputValidation({
  required,
  maxLength,
  noSpecialChars,
  onlyNumbers,
  isEmail,
  engKorNumRule,
  engKorRule,
  engNumRule,
  engKorNumHasSpaceRule,
  engKorHasSpaceRule,
  pwRule,
  onlyCharsWithTwoChars,
}: ValidationOptions): RuleFunction[] {
  const rules: RuleFunction[] = [];

  if (required) {
    rules.push(
      (value: string) =>
        !!value || $t("product_platform.validate.requiredFieldInput")
    );
  }
  if (maxLength) {
    rules.push(
      (value: string) =>
        checkValueLength(value, maxLength) ||
        `${$t(
          "product_platform.validate.maxLengthCharacter"
        )} ${maxLength} ${$t("product_platform.validate.characters")}`
    );
  }
  if (noSpecialChars) {
    rules.push(
      (value: string) =>
        /^[a-zA-Z0-9\s]*$/.test(value) ||
        $t("product_platform.validate.special")
    );
  }

  if (onlyNumbers) {
    rules.push((value: string) => {
      if (!value) return true;
      return /^\d+$/.test(value) || $t("product_platform.validate.onlyNumber");
    });
  }
  if (isEmail) {
    rules.push((value: string) => {
      if (!value) return true;
      const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z.-]+\.[a-zA-Z]{2,}$/;
      return emailPattern.test(value) || $t("product_platform.validate.email");
    });
  }

  if (engKorNumRule) {
    rules.push(
      (value: string) =>
        /^[a-zA-Z0-9가-힣]*$/.test(value) ||
        $t("product_platform.validate.onlyEnKoNumber")
    );
  }
  if (engKorRule) {
    rules.push(
      (value: string) =>
        /^[a-zA-Z가-힣]*$/.test(value) ||
        $t("product_platform.validate.onlyEnKo")
    );
  }
  if (engNumRule) {
    rules.push(
      (value: string) =>
        /^[a-zA-Z0-9]*$/.test(value) ||
        $t("product_platform.validate.onlyEnNumber")
    );
  }

  if (engKorNumHasSpaceRule) {
    rules.push(
      (value: string) =>
        /^[a-zA-Z0-9가-힣\s]*$/.test(value) ||
        $t("product_platform.validate.onlyEnKoNumber")
    );
  }
  if (engKorHasSpaceRule) {
    rules.push(
      (value: string) =>
        /^[a-zA-Z가-힣\s]*$/.test(value) ||
        $t("product_platform.validate.onlyEnKo")
    );
  }

  if (pwRule) {
    rules.push(
      (value: string) =>
        /^[^가-힣]+$[a-zA-Z0-9\W]*$/.test(value) ||
        $t("product_platform.validate.onlyEnNumSpecial")
    );
  }

  if (onlyCharsWithTwoChars) {
    rules.push(
      (value: string) =>
        /^[a-zA-Z]{2,}$/.test(value) ||
        $t("product_platform.validate.onlyEngTwoChars")
    );
  }

  return rules;
}
