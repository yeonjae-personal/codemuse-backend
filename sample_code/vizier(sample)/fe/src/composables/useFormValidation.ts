import { FormError, FormRef } from '@/interfaces/admin/admin';
import { useSnackbarStore } from '@/store';
import i18n from "@/i18n";

export const useFormValidation = (formRef: FormRef) => {
  const validateForm = async () => {
    const useSnackbar = useSnackbarStore();
    const $t = i18n.global.t;
    const { valid } = await formRef.value.validate();

    if (!valid) {
      const errors: FormError[] = formRef.value.errors || [];
      let firstErrorFieldId: string | null = null;

      const flattenedErrors = errors.flatMap(error => 
        error.errorMessages.map(errorMessage => ({ id: error.id, errorMessage }))
      );

      for (const { id, errorMessage } of flattenedErrors) {
        if (errorMessage.includes($t('product_platform.validate.requiredFieldInput'))) {
          useSnackbar.showSnackbar($t('product_platform.validate.requiredField'), "error");
          firstErrorFieldId = id;
          break;
        }

        const maxLengthMatch = errorMessage.match(/Max length (\d+) characters/) || errorMessage.match(/최대 길이는 (\d+) 자리 입니다./);
        if (maxLengthMatch ) {
          const maxLength = maxLengthMatch[1];
          useSnackbar.showSnackbar(`${$t('product_platform.validate.maxLength')} ${maxLength} ${$t('product_platform.validate.characters')}`, "error");
          firstErrorFieldId = id;
          break;
        }

        if(errorMessage.includes($t('product_platform.validate.onlyNumber'))) {
          useSnackbar.showSnackbar($t('product_platform.validate.onlyNumber'), "error");
          firstErrorFieldId = id;
        }

        if(errorMessage.includes($t('product_platform.validate.special'))) {
          useSnackbar.showSnackbar($t('product_platform.validate.special'), "error");
          firstErrorFieldId = id;
        }

        if(errorMessage.includes($t('product_platform.validate.email'))) {
          useSnackbar.showSnackbar($t('product_platform.validate.email'), "error");
          firstErrorFieldId = id;
        }
        
        if(errorMessage.includes($t('product_platform.validate.onlyEnKoNumber'))) {
          useSnackbar.showSnackbar($t('product_platform.validate.onlyEnKoNumber'), "error");
          firstErrorFieldId = id;
        }

        if(errorMessage.includes($t('product_platform.validate.onlyEnKo'))) {
          useSnackbar.showSnackbar($t('product_platform.validate.onlyEnKo'), "error");
          firstErrorFieldId = id;
        }

        if(errorMessage.includes($t('product_platform.validate.onlyEnNumber'))) {
          useSnackbar.showSnackbar($t('product_platform.validate.onlyEnNumber'), "error");
          firstErrorFieldId = id;
        }
      }

      return firstErrorFieldId;
    }

    return null;
  };

  return {
    validateForm,
  };
};