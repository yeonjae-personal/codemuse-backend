<template>
  <BasePopup
    v-model="isOpen"
    :title="title"
    :size="DialogSizeType.XMedium"
    :submit-button-text="t('product_platform.save')"
    :cancel-button-text="t('product_platform.cancel')"
  >
    <template #body>
      <v-form ref="formRef">
        <div class="w-[640px gap-3">
          <!-- ------------ start row 0---------------------- -->
          <div class="pt-3 px-6">
            <div class="flex gap-4 mb-4 w-[592px] h-[48px]">
              <div class="flex-1">
                <base-input-text
                  v-model="analyticalTermName"
                  label="Analytical Term"
                  :styles="'input-form'"
                  :hide-details="true"
                >
                </base-input-text>
              </div>

              <BaseButton
                :color="ButtonColorType.Secondary"
                :width="WIDTH_BUTTON.AUTO"
                @click="fetchCharacterAnalysis(analyticalTermName)"
              >
                Analysis
              </BaseButton>

              <BaseButton
                :color="ButtonColorType.Secondary"
                :width="WIDTH_BUTTON.AUTO"
                @click="openPopupVocab = true"
              >
                Vocab Apply
              </BaseButton>
            </div>
          </div>
        </div>

        <!-- ------------ start row 1---------------------- -->
        <div class="h-[308px]">
          <TableAnalysis
            :headers="headerTable"
            :data="dataAnalysis"
            :loading="isLoadingTableData"
            :is-show-pagination="false"
            @click-detail="handleRowDoubleClick"
          >
            <template #item="{ item }">
              <tr
                :key="item.vocaId"
                :class="{ 'selected-row': isSelected(item) }"
              >
                <td class="text-center align-middle">
                  <v-checkbox
                    :checked="isSelected(item)"
                    :true-value="true"
                    :false-value="false"
                    :true-icon="TrueIcon"
                    :false-icon="FalseIcon"
                    :hide-details="true"
                    density="compact"
                    class="custom-checkbox"
                    @click="toggleSelection(item)"
                  ></v-checkbox>
                </td>
                <td>
                  <p1>{{ item.vocaNm }}</p1>
                </td>
                <td>
                  <p1>{{ item.vocaEngAbb }}</p1>
                </td>
                <td>
                  <p1>{{ item.vocaEngNm }}</p1>
                </td>
                <td>
                  <p1>{{ item.vocaDscr }}</p1>
                </td>
              </tr>
            </template>
          </TableAnalysis>
        </div>

        <!-- ------------------------------------------------- -->

        <div class="flex flex-col px-6 gap-2 pt-1">
          <div
            class="h-[30px] font-sans text-[15px] font-medium leading-[22.5px] text-left"
          >
            Analytics
          </div>

          <div class="flex w-[592px] pb-1 h-[48px]">
            <div class="flex-1">
              <base-input-text
                v-model="termConfigurationInformation"
                :styles="'input-form'"
                label="Terminology"
              >
              </base-input-text>
            </div>
          </div>

          <!-- ------------ start row 2---------------------- -->
          <div class="flex w-[592px] pb-1 h-[48px]">
            <div class="flex-1">
              <base-input-text
                v-model="termEnglishName"
                :styles="'input-form'"
                label="Term Name"
              >
              </base-input-text>
            </div>
          </div>

          <!-- ------------ start row 4---------------------- -->

          <div class="flex w-[592px] h-[48px]">
            <div class="flex-1">
              <base-input-text
                v-model="termAbbreviation"
                :styles="'input-form'"
                label="Abbreviation"
              >
              </base-input-text>
            </div>
          </div>
        </div>
        <!-- ---------------------------------- -->
      </v-form>
    </template>

    <template #footer>
      <div class="flex justify-end gap-3">
        <BaseButton @click="handleApply"> Apply </BaseButton>
        <BaseButton :color="ButtonColorType.Gray" @click="closeDialog()">
          {{ t("product_platform.cancel") }}
        </BaseButton>
      </div>
    </template>
  </BasePopup>

  <VocabUpdatePopup
    v-if="openPopupVocab"
    v-model="openPopupVocab"
    :form-type="FORM_TYPE_OPTION.CREATE"
  />
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";
import TableAnalysis from "@/pages/admin/subs/TableAnalysis.vue";
import { ButtonColorType, DialogSizeType } from "@/enums";
import { useSnackbarStore } from "@/store";
import { httpClient } from "@/utils/http-common";
import { FORM_TYPE_OPTION } from "@/constants/admin/admin";
import { TERM_ANALYSIS_HEADERS } from "@/constants/admin/terminology";
import TrueIcon from "@/components/prod/icons/TrueIcon.vue";
import FalseIcon from "@/components/prod/icons/FalseIcon.vue";
import { WIDTH_BUTTON } from "@/constants/index";

const VocabUpdatePopup = defineAsyncComponent(
  () => import("@/pages/admin/subs/vocab/VocabUpdatePopup.vue")
);

const emit = defineEmits(["update:modelValue", "apply"]);
const props = defineProps({
  formType: {
    type: String,
    default: FORM_TYPE_OPTION.CREATE,
  },
  modelValue: {
    type: Boolean,
    default: false,
  },
  data: {
    type: Object,
    default: null,
  },
});

const { t } = useI18n();
const useSnackbar = useSnackbarStore();

// computed
const title = "Term Analysis";
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

// form data
const analyticalTermName = ref("");
const termConfigurationInformation = ref("");
const termEnglishName = ref("");
const termAbbreviation = ref("");

const isLoadingTableData = ref(false);
const headerTable = ref(TERM_ANALYSIS_HEADERS);
const dataAnalysis = ref([]);

const openPopupVocab = ref(false);

onMounted(() => {
  if (props.data) {
    analyticalTermName.value = props.data.analyticalTermName;
  }
});

const closeDialog = () => {
  isOpen.value = false;
};

const fetchCharacterAnalysis = async (analWord: string) => {
  try {
    isLoadingTableData.value = true;
    const response = await httpClient.get(`/api/comm/voca/v1/anal`, {
      params: {
        analWord: analWord,
      },
    });
    dataAnalysis.value = response.data.list;
  } catch (error: any) {
    useSnackbar.showSnackbar(error?.errorMsg as string, "error");
  } finally {
    isLoadingTableData.value = false;
  }
};

const handleRowDoubleClick = (_row: any) => {
  //
};

const selectedItems = ref(new Set<any>());

const isSelected = (item: Record<string, any>) => {
  return Array.from(selectedItems.value).some(
    (selectedItem) => selectedItem.vocaId === item.vocaId
  );
};

const toggleSelection = (item: Record<string, any>) => {
  if (isSelected(item)) {
    selectedItems.value = new Set(
      Array.from(selectedItems.value).filter(
        (selectedItem) => selectedItem.vocaId !== item.vocaId
      )
    );
  } else {
    selectedItems.value.add(item);
  }
};

watch(
  selectedItems,
  () => {
    const selectedArray = Array.from(selectedItems.value);

    termConfigurationInformation.value = selectedArray
      .map((item: any) => item.vocaNm)
      .join("+");
    termAbbreviation.value = selectedArray
      .map((item: any) => item.vocaEngAbb)
      .join(" ");
    termEnglishName.value = selectedArray
      .map((item: any) => item.vocaEngNm)
      .join("_");
  },
  { deep: true }
);

const handleApply = () => {
  emit("apply", {
    termConfigurationInformation: termConfigurationInformation.value,
    termEnglishName: termEnglishName.value,
    termAbbreviation: termAbbreviation.value,
  });
  closeDialog();
};
</script>

<style lang="scss" scoped>
.custom-button {
  width: 140px;
  height: 48px;
  color: #ba1642;
  background-color: #fff0f2;
}

:deep(.custom-checkbox .v-label) {
  font-family: Noto Sans KR;
  font-size: 13px;
  font-weight: 500;
  line-height: 19.5px;
  letter-spacing: 0.25px;
  text-align: left;
}
</style>
