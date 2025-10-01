<template>
  <div
    ref="matrixMain"
    class="bg-white rounded-[12px] h-full relative flex flex-column max-h-[calc(100vh-137px)]"
  >
    <div
      class="flex justify-between items-center w-full pt-6 px-6 pb-3 relative z-1 bg-white rounded-[12px]"
    >
      <span class="text-[#3A3B3D] font-[500] text-[15px]">{{
        isEdit
          ? $t("product_platform.matrixEditor")
          : $t("product_platform.matrixViewer")
      }}</span>
      <BaseButton
        v-if="!isEdit"
        :color="ButtonColorType.Secondary"
        @click="handleEdit"
      >
        <edit-icon class="mr-[6px]" />
        {{ $t("product_platform.edit") }}
      </BaseButton>
      <div v-else class="flex gap-2">
        <BaseButton
          :color="ButtonColorType.Gray"
          :width="WIDTH_BUTTON.EXCEL"
          :disabled="downloading || isBuilder"
          @click="handleDownloadData"
        >
          <DownloadIcon class="mr-[6px]" />
          {{ $t("product_platform.download") }}
        </BaseButton>

        <BaseButton
          :color="ButtonColorType.Gray"
          :width="WIDTH_BUTTON.EXCEL"
          :disabled="isShowUploadPopup || isBuilder"
          @click="handleOpenPopupUpload"
        >
          <UploadIcon class="mr-[6px]" />
          {{ $t("product_platform.upload") }}
        </BaseButton>
        <BaseButton :color="ButtonColorType.Secondary" @click="handleBuilder">
          {{ $t("product_platform.builder") }}
        </BaseButton>
      </div>
    </div>
    <div ref="builderContent" :class="[isBuilder && 'px-3 pb-3']">
      <MatrixBuilder />
    </div>
    <div class="h-full">
      <MatrixTable
        :headers="headersTableMatrix"
        :items="listTableMatrix"
        :is-edit="isEdit"
        :is-create="isCreate"
        :is-read-only="isBuilder"
        :table-height="tableHeight"
        :is-multi-edit="isMultiEdit"
        @show-message-read-only="showMessageReadOnly"
        @trigger-filter-action="triggerFilterAction"
        @trigger-sort-action="triggerSortAction"
        @update-value-column="updateValueColumn"
        @update-all-value="updateAllValue"
        @update-single-value="updateSingleValue"
      />
      <div
        v-if="isEdit"
        class="absolute bottom-4 flex align-center gap-2 right-4"
      >
        <BaseButton
          :color="ButtonColorType.Gray"
          @click="
            () => {
              openPopupCancel = true;
            }
          "
        >
          {{ $t("product_platform.cancel") }}
        </BaseButton>
        <BaseButton
          :color="ButtonColorType.Secondary"
          class="z-[10]"
          @click="onOpenSavePopup"
        >
          <SaveIcon class="mr-[6px]" />
          {{ $t("product_platform.save") }}
        </BaseButton>
      </div>
    </div>

    <ShowDetailIcon
      v-if="!isShowMatrixSearch"
      class="absolute top-[207px] left-[0px] cursor-pointer text-[#525457] hover:text-[#303132] z-[5]"
      @click="onClose"
    />
  </div>
  <UploadMatrixPopup v-if="isShowUploadPopup" v-model="isShowUploadPopup" />
  <base-popup
    v-model="openPopupCancel"
    :icon="DialogIconType.Warning"
    :submit-button-text="$t('product_platform.btn_yes')"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="$t('product_platform.desc_cancel')"
    @on-close="
      () => {
        openPopupCancel = false;
      }
    "
    @on-submit="handleCancel"
  />
  <base-popup
    v-model="openPopupSave"
    :icon="DialogIconType.Warning"
    :submit-button-text="$t('product_platform.btn_yes')"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="$t('product_platform.desc_update')"
    @on-close="
      () => {
        openPopupSave = false;
      }
    "
    @on-submit="onSubmit"
  />
</template>

<!-- eslint-disable id-length -->
<script lang="ts" setup>
import { useDownloadFile } from "@/composables/useDownloadFIle";
import { useSnackbarStore } from "@/store";
import useMatrixStructureStore from "@/store/admin/matrixStructure.store";
import { ButtonColorType, DialogIconType } from "@/enums";
import { useI18n } from "vue-i18n";
import { DOWNLOAD_EXCEL_MATRIX } from "@/api/prod/path";
import { WIDTH_BUTTON } from "@/constants/index";

const UploadMatrixPopup = defineAsyncComponent(
  () => import("@/pages/admin/subs/matrix/UploadMatrixPopup.vue")
);

const { downloading, downloadFilePost } = useDownloadFile();
const useSnackbar = useSnackbarStore();
const { t } = useI18n();
const matrixStructureStore = useMatrixStructureStore();
const {
  isEdit,
  isBuilder,
  headersTableMatrix,
  listTableMatrix,
  listTableMatrixTemp,
  isShowMatrixSearch,
  matrixBuilderFactors,
  matrixSelected,
  isShowFactorSearch,
  isMultiEdit,
  isCreate,
  listTempItems,
} = storeToRefs(matrixStructureStore);
const isShowUploadPopup = ref<boolean>(false);
const isSaving = ref<boolean>(false);
const openPopupSave = ref<boolean>(false);
const openPopupCancel = ref<boolean>(false);
const matrixMain = ref<any>(null);
const builderContent = ref<any>(null);
const tableHeight = ref<string>("calc(100vh - 235px)");

const handleEdit = () => {
  isEdit.value = true;
  if (!isCreate.value) {
    isShowMatrixSearch.value = false;
  }
};

const handleDownloadData = async () => {
  const body = {
    matrixCode: matrixSelected.value?.isNew
      ? "NewMatrix"
      : matrixSelected.value.matrixCode,
    matrixCodeName: matrixSelected.value.matrixCodeName,
    builderDtos: matrixBuilderFactors.value,
    measureMDtos: listTableMatrix.value.map((dtoItem) => ({
      ...dtoItem,
      measureDDtos: dtoItem.measureDDtos?.filter(
        (factorItem) => factorItem.factorCode !== "VALUE"
      ),
    })),
  };
  await downloadFilePost(
    DOWNLOAD_EXCEL_MATRIX,
    body,
    `Matrix_${
      matrixSelected.value?.isNew
        ? "NewMatrix"
        : matrixSelected.value.matrixCode
    }`
  );
};
const handleOpenPopupUpload = () => {
  isShowUploadPopup.value = true;
};
const handleBuilder = () => {
  isBuilder.value = true;
};
const handleCancel = async () => {
  openPopupCancel.value = false;
  isEdit.value = false;
  isShowMatrixSearch.value = true;
  isShowFactorSearch.value = false;
  isBuilder.value = false;
  isMultiEdit.value = false;
  await matrixStructureStore.getHeaderTableMatrix(
    matrixSelected.value.matrixCode
  );
  await matrixStructureStore.getListTableMatrix(
    matrixSelected.value.matrixCode,
    {
      builderDtos: null,
    }
  );
  if (isCreate.value) {
    matrixSelected.value = null;
    await matrixStructureStore.getListMatrix();
    isCreate.value = false;
  }
};
const onOpenSavePopup = () => {
  if (isCreate.value && !matrixSelected.value.matrixCodeName) {
    useSnackbar.showSnackbar(
      t("product_platform.matrix_name_require_msg"),
      "error"
    );
    return;
  }
  openPopupSave.value = true;
};
const onSubmit = async () => {
  if (!isSaving.value) {
    try {
      isSaving.value = true;
      const data = {
        matrixCode: matrixSelected.value.matrixCode,
        matrixCodeName: matrixSelected.value.matrixCodeName,
        useYn: "Y",
        matrixDDtos: matrixBuilderFactors.value.map((fac, index) => ({
          ...fac,
          seqNo: index,
        })),
        measureMDtos: listTableMatrixTemp.value?.map((matrix) => {
          const valueNum = listTempItems.value?.find(
            (item) => item.rowId === matrix.rowId
          )?.matrixNumValue;
          return {
            ...matrix,
            matrixNumValue: valueNum
              ? valueNum
              : valueNum === 0
                ? 0
                : matrix?.matrixNumValue
                  ? matrix?.matrixNumValue
                  : matrix?.matrixNumValue === 0
                    ? 0
                    : null,
            measureDDtos: matrix.measureDDtos.filter(
              (dto) => dto.factorCode !== "VALUE"
            ),
          };
        }),
      };
      const res = isCreate.value
        ? await matrixStructureStore.postDetailMatrix(data)
        : await matrixStructureStore.putDetailMatrix(
            matrixSelected.value.matrixCode,
            data
          );
      if (res.status == 200) {
        useSnackbar.showSnackbar(
          t("product_platform.successfully_saved"),
          "success"
        );

        await matrixStructureStore.getHeaderTableMatrix(
          isCreate.value ? res.data : matrixSelected.value.matrixCode
        );
        await matrixStructureStore.getListTableMatrix(
          isCreate.value ? res.data : matrixSelected.value.matrixCode,
          {
            builderDtos: null,
          }
        );

        await matrixStructureStore.getListMatrix();
        if (isCreate.value) {
          matrixSelected.value = { matrixCode: res.data };
        }
        isEdit.value = false;
        isCreate.value = false;
        isShowMatrixSearch.value = true;
        isShowFactorSearch.value = false;
        isBuilder.value = false;
        isMultiEdit.value = false;
        listTempItems.value = [];
      }
    } catch (error) {
      useSnackbar.showSnackbar(
        t("product_platform.something_went_wrong"),
        "error"
      );
    } finally {
      openPopupSave.value = false;
      isSaving.value = false;
    }
  }
};

const onClose = () => {
  isShowMatrixSearch.value = true;
};
const showMessageReadOnly = () => {
  if (isBuilder.value) {
    useSnackbar.showSnackbar(t("product_platform.pleaseFinishBuild"), "error");
  }
};

const updateValueColumn = () => {
  isMultiEdit.value = !isMultiEdit.value;
};
const updateSingleValue = (item) => {
  if (listTempItems.value?.find((yyy) => yyy.rowId === item.rowId)) {
    listTempItems.value = listTempItems.value.map((zzz) => {
      if (zzz.rowId == item.rowId) {
        return {
          ...zzz,
          matrixNumValue: item.matrixNumValue,
        };
      }
      return zzz;
    });
  } else {
    listTempItems.value.push(item);
  }
};
const updateAllValue = (item) => {
  listTableMatrix.value = listTableMatrix.value.map((matrix) => ({
    ...matrix,
    matrixNumValue: item.matrixNumValue,
  }));
  listTableMatrix.value.forEach((xxx) => {
    if (listTempItems.value?.find((yyy) => yyy.rowId === xxx.rowId)) {
      listTempItems.value = listTempItems.value.map((zzz) => {
        if (zzz.rowId == xxx.rowId) {
          return {
            ...zzz,
            matrixNumValue: xxx.matrixNumValue,
          };
        }
        return zzz;
      });
    } else {
      listTempItems.value.push(xxx);
    }
  });
};

const triggerSortAction = (factorCode) => {
  //handle sort table header
  let indexSort = 0;
  headersTableMatrix.value = headersTableMatrix.value.map((header, index) => {
    if (header.factorCode === factorCode) {
      indexSort = index;
      return {
        ...header,
        valueSort:
          !header.valueSort || header.valueSort === "down" ? "up" : "down",
      };
    }

    return {
      ...header,
      valueSort: "",
    };
  });

  let valueHeaderSort = headersTableMatrix.value.find(
    (header) => header.factorCode === factorCode
  )?.valueSort;

  listTableMatrix.value = listTableMatrix.value.sort((a, b) => {
    const factorValueNameA =
      a.measureDDtos[indexSort as number].factorValueName;
    const factorValueNameB =
      b.measureDDtos[indexSort as number].factorValueName;

    if (valueHeaderSort === "up") {
      if (factorCode === "VALUE") {
        return Number(a.matrixNumValue) - Number(b.matrixNumValue);
      }

      return factorValueNameA
        .toString()
        .localeCompare(factorValueNameB.toString());
    }

    if (factorCode === "VALUE") {
      return Number(b.matrixNumValue) - Number(a.matrixNumValue);
    }

    return factorValueNameB
      .toString()
      .localeCompare(factorValueNameA.toString());
  });
};

const triggerFilterAction = () => {
  let itemChecked = [] as any;
  headersTableMatrix.value?.forEach((header) => {
    if (header.factorValues?.some((ft) => ft.inUse)) {
      itemChecked = [
        ...itemChecked,
        ...header.factorValues
          ?.filter((check) => check.inUse)
          ?.map((dt) => dt.factorValueCode),
      ];
    }
  });

  // filter item checked
  listTableMatrix.value = listTableMatrixTemp.value
    .map((abc) => {
      let itemRowId = listTempItems.value?.find(
        (row) => row.rowId === abc.rowId
      );
      return {
        ...abc,
        matrixNumValue: itemRowId?.matrixNumValue
          ? itemRowId?.matrixNumValue
          : abc.matrixNumValue,
      };
    })
    ?.filter((a1) => {
      let checkExit = true;
      a1.measureDDtos.forEach((xxx) => {
        if (xxx.factorValueCode && !itemChecked.includes(xxx.factorValueCode)) {
          checkExit = false;
        }
      });

      return checkExit;
    });

  // handle checked filter header table
  headersTableMatrix.value = headersTableMatrix.value.map((xxx) => {
    let checkFilter = false;

    xxx.factorValues?.forEach((yyy) => {
      let orgFactor = matrixBuilderFactors.value.find(
        (zzz) => zzz.factorCode === xxx.factorCode
      );

      orgFactor.factorValues.forEach((ftOrg) => {
        if (ftOrg.factorValueCode === yyy.factorValueCode) {
          if (ftOrg.inUse !== yyy.inUse) {
            checkFilter = true;
          }
        }
      });
    });
    return {
      ...xxx,
      isFilter: checkFilter,
    };
  });
};

watch(
  () => [
    isBuilder.value,
    isEdit.value,
    isShowMatrixSearch.value,
    isShowFactorSearch.value,
    matrixBuilderFactors.value.length,
  ],
  () => {
    setTimeout(() => {
      tableHeight.value =
        matrixMain.value?.clientHeight -
        76 -
        (isEdit.value ? 64 : 20) -
        builderContent.value?.clientHeight +
        "px";
    }, 500);
  },
  { immediate: true }
);
</script>
<style scoped lang="scss"></style>
