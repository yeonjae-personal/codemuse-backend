<template>
  <BasePopup
    v-model="isOpen"
    :title="'Domain Create'"
    :size="DialogSizeType.XMedium"
    :submit-button-text="t('product_platform.save')"
    :cancel-button-text="t('product_platform.cancel')"
    :content="
      isCancelPopupDomain
        ? t('product_platform.desc_cancel')
        : t('product_platform.desc_update')
    "
  >
    <template #body>
      <div class="w-[640px] px-6 pt-6 gap-3">
        <!-- -------------- start row 1-------------- -->
        <div class="flex gap-4 mb-4 w-[592px] h-[48px]">
          <div class="flex-1">
            <base-input-text
              v-model="item.value"
              :required="true"
              label="Domain Name"
              :rules="useInputValidation({ required: true, maxLength: 150 })"
              :counter="150"
              :error-messages="'mesageDuplicate'"
              :styles="'input-form'"
            >
            </base-input-text>
          </div>

          <BaseButton
            :color="ButtonColorType.Secondary"
            :width="WIDTH_BUTTON.DUPLICATE_CHECK"
            :height="HEIGHT_BUTTON.DUPLICATE_CHECK"
          >
            Duplicate Check
          </BaseButton>
        </div>

        <!-- -------------- start row 2-------------- -->
        <div class="flex gap-4 mb-4 w-[592px] h-[48px]">
          <div class="flex-1">
            <base-input-text
              v-model="item.value"
              class=" "
              :required="true"
              :styles="'input-form'"
              label="English Domain Name"
            >
              <template #append-inner>
                <svg
                  width="12"
                  height="14"
                  viewBox="0 0 12 14"
                  fill="none"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    fill-rule="evenodd"
                    clip-rule="evenodd"
                    d="M8.19526 0.528636C8.45561 0.268287 8.87772 0.268287 9.13807 0.528636L11.8047 3.1953C12.0651 3.45565 12.0651 3.87776 11.8047 4.13811L9.13807 6.80478C8.87772 7.06513 8.45561 7.06513 8.19526 6.80478C7.93491 6.54443 7.93491 6.12232 8.19526 5.86197L9.72386 4.33337H0.666667C0.298477 4.33337 1.98682e-08 4.0349 1.98682e-08 3.66671C1.98682e-08 3.29852 0.298477 3.00004 0.666667 3.00004H9.72386L8.19526 1.47145C7.93491 1.2111 7.93491 0.788986 8.19526 0.528636ZM3.80474 7.1953C4.06509 7.45565 4.06509 7.87776 3.80474 8.13811L2.27614 9.66671H11.3333C11.7015 9.66671 12 9.96518 12 10.3334C12 10.7016 11.7015 11 11.3333 11H2.27614L3.80474 12.5286C4.06509 12.789 4.06509 13.2111 3.80474 13.4714C3.54439 13.7318 3.12228 13.7318 2.86193 13.4714L0.195262 10.8048C-0.0650874 10.5444 -0.0650874 10.1223 0.195262 9.86197L2.86193 7.1953C3.12228 6.93495 3.54439 6.93495 3.80474 7.1953Z"
                    fill="#6B6D70"
                  />
                </svg>
              </template>
            </base-input-text>
          </div>

          <div class="flex-1">
            <base-select
              v-model="domainGroup"
              :label="'Domain Groups'"
              :density="'comfortable'"
              :items="domainGroupOption"
              :item-title="'title'"
              :item-value="'value'"
              :required="true"
            />
          </div>
        </div>

        <!-- -------------- start row 3-------------- -->

        <div class="flex gap-4 mb-4 w-[592px] h-[48px]">
          <div class="flex-1">
            <base-select
              v-model="domainType"
              :label="'Domain Type'"
              :density="'comfortable'"
              :items="domainTypeOption"
              :item-title="'title'"
              :item-value="'value'"
              :required="true"
            />
          </div>
          <div class="flex-1">
            <base-select
              v-model="useYn"
              :label="'Usage'"
              :density="'comfortable'"
              :items="itemsType"
              :item-title="'title'"
              :item-value="'value'"
              :required="true"
            />
          </div>

          <div class="flex-1">
            <base-input-text
              class=" "
              :styles="'input-form'"
              label="Data Length"
            >
            </base-input-text>
          </div>
        </div>

        <!-- -------------- start row 4-------------- -->

        <div class="flex gap-4 mb-4 w-[592px] h-[48px]">
          <div class="flex-1">
            <base-input-text
              class=" "
              :styles="'input-form'"
              :required="true"
              label="Explanation"
            >
            </base-input-text>
          </div>
        </div>
      </div>
    </template>

    <template #footer>
      <div class="flex justify-end gap-3">
        <BaseButton @click="handleSave()">
          {{ t("product_platform.save") }}
        </BaseButton>
        <BaseButton :color="ButtonColorType.Gray" @click="closeDialog()">
          {{ t("product_platform.cancel") }}
        </BaseButton>
      </div>
    </template>
  </BasePopup>
</template>

<script setup>
import { useI18n } from "vue-i18n";
import { useDomainStore } from "@/store";

// import { DOMAIN_GROUP_OPTION_CREATE } from "@/constants/admin/domain";
import { useInputValidation } from "@/composables/useInputValidation";
import { HEIGHT_BUTTON, WIDTH_BUTTON } from "@/constants/index";

// import { useMenuStore } from "@/store";
// const menuStore = useMenuStore();
// const { selectedMenuItem } = storeToRefs(menuStore);

const { t } = useI18n();

const domainStore = useDomainStore();

const item = ref({
  domainName: "Name input",
  englishDomainName: "",
  domainGroups: "",
  domainType: "",
  usage: "",
});

// {
//   "domnId": "1485f86f-1b51-4a60-9",
//   "domnNm": "111",
//   "domnEngNm": "INDC DIVS",
//   "useYn": "N",
//   "domnLen": "111",
//   "domnDscr": "111",
//   "rgstUsr": "John",
//   "rgstDtm": "2024-09-25T03:07:01",
//   "updUsr": "John",
//   "updDtm": "2024-09-25T03:07:01"
// }

const useYn = ref("");

const itemsType = computed(() => {
  return USE_YN_OPTION_CREATE;
});

// DOMAIN_GROUP_OPTION_CREATE
const domainGroup = ref("");
const domainGroupOption = computed(() => {
  domainStore.getDomainGroupList();
});

// Domain Type
const domainType = ref("");
const domainTypeOption = computed(() => {
  domainStore.getDomainTypeList();
});

const isOpen = computed({
  // getter
  get() {
    return props.modelValue;
  },
  // setter
  set(newValue) {
    emit("update:modelValue", newValue);
  },
});
</script>

<style lang="scss" scoped>
.custom-button {
  width: 140px;
  height: 48px;
  color: #ba1642;
  background-color: #fff0f2;
}
.custom-row {
  gap: 16px;
}
</style>
