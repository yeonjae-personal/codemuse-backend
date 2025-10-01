<template>
  <div
    class="bg-white relative rounded-r-lg rounded-[10px]"
    :class="isEditFactorDetail && '!border !border-primary shadow-edit-mode'"
  >
    <div class="flex justify-between align-center px-6 pb-2 pt-6 h-[72px]">
      <span class="font-medium text-[15px] text-text-base tracking-[0.5px]">{{
        isCreate
          ? $t("product_platform.factorCreate")
          : isEditFactorDetail
            ? $t("product_platform.factorEdit")
            : $t("product_platform.factorDetails")
      }}</span>
      <BaseButton
        v-if="!isEditFactorDetail"
        :color="ButtonColorType.Secondary"
        @click="handleEditFactorDetail"
      >
        <edit-icon class="mr-[6px]" />
        {{ $t("product_platform.edit") }}
      </BaseButton>
    </div>
    <v-form
      v-model="isFormValid"
      class="flex flex-col px-4 gap-6 h-[calc(100%-140px)]"
    >
      <DetailPane>
        <DetailPaneRow
          :label="$t(`product_platform.factorCode`)"
          :value="
            isCreate || factorDetail.isAdded
              ? $t(`product_platform.auto_generation`)
              : factorDetail?.factorCode
          "
        />
        <DetailPaneRow :label="$t(`product_platform.factorName`)">
          <template #value="{ klass }">
            <div v-if="!isEditFactorDetail" :class="klass">
              {{ factorDetail.factorName }}
            </div>
            <div v-else :class="klass">
              <BaseInputText
                v-model="factorDetail.factorName"
                styles="input-edit custom"
                required
                :maxlength="500"
                :counter="500"
              />
            </div>
          </template>
        </DetailPaneRow>
        <DetailPaneRow :label="$t(`product_platform.useYn`)">
          <template #value="{ klass }">
            <div :class="klass">
              <v-switch
                v-model="factorDetail.useYn"
                class="switch-custom"
                hide-details
                :color="isEditFactorDetail ? '#D9325A' : '#FDCED5'"
                inset
                width="36"
                density="compact"
                :readonly="!isEditFactorDetail"
                :false-value="RequiredYn.No"
                :true-value="RequiredYn.Yes"
              ></v-switch>
            </div>
          </template>
        </DetailPaneRow>
      </DetailPane>
      <div id="locoSwapper">
        <LocomotiveComponent
          :scroll-container-class="scrollContainerClass"
          scroll-content-class="flex flex-col gap-4"
          :always-end="scrollEnd"
        >
          <div
            v-if="factorDetail.factorValueLst?.length"
            ref="factorValueList"
            class="flex gap-2"
          >
            <div class="w-[36px] h-full">
              <BaseCanvas
                id="factor-canvas"
                :width="36"
                :height="canvasWidth"
                :direction="CANVAS_DIRECTION.HORIZONTAL"
                :list-coordinates="factorLineCoordinates"
                :mid-point="12"
              ></BaseCanvas>
            </div>
            <div
              ref="factorItemCont"
              class="flex flex-col flex-grow-1 py-2 gap-2"
            >
              <div
                v-for="item in listItems"
                ref="factorItems"
                :key="item.factorValueCode"
                class="pr-3"
              >
                <FactorExpandForm
                  :id="item.factorValueCode"
                  :form-data="item"
                  :is-active="selectedItem === item.factorValueCode"
                  :title="item.factorValueName"
                  :is-edit="isEditFactorDetail"
                  :disabled="item.useYn === RequiredYn.No"
                  :editable="
                    isEditFactorDetail && (item?.isAdded || item?.isNew)
                  "
                  :actions="listActions(item)"
                  :expand="item?.expand"
                  @on-click="handleClickFactorExpand($event, item)"
                  @update:form-data="handleUpdateValue"
                ></FactorExpandForm>
              </div>
            </div>
          </div>
        </LocomotiveComponent>
        <div
          v-if="isEditFactorDetail && !showBtnFixed"
          class="flex justify-center mt-6"
        >
          <button class="add-new-button" @click="handleAddNewFactorSubItem">
            <PlusLargeIcon />
          </button>
        </div>
        <button
          v-if="isEditFactorDetail && showBtnFixed"
          class="add-new-button float"
          @click="handleAddNewFactorSubItem"
        >
          <PlusLargeIcon />
        </button>
      </div>
    </v-form>
    <BasePagination
      v-if="listItems?.length"
      :pagination="paginationFactorDetail"
      :class-name="isEditFactorDetail ? 'mb-[70px]' : 'mb-3'"
      @on-change-page="handleChangePage"
    />
    <div
      v-if="isEditFactorDetail || isCreate"
      class="absolute flex justify-end items-center shrink-0 my-3 gap-2 bottom-0 right-4"
    >
      <BaseButton :color="ButtonColorType.Gray" @click="handleCancel">
        {{ $t("product_platform.cancel") }}
      </BaseButton>
      <BaseButton :color="ButtonColorType.Secondary" @click="onSubmit">
        {{ $t("product_platform.add") }}
      </BaseButton>
    </div>
  </div>
</template>
<script setup lang="ts">
import TrashIcon from "@/components/prod/icons/TrashIcon.vue";
import { CANVAS_DIRECTION } from "@/constants/index";
import { ButtonColorType, RequiredYn } from "@/enums";
import { useSnackbarStore } from "@/store";
import useFactorStore from "@/store/admin/factor.store";
import { useI18n } from "vue-i18n";
import { v4 as uuidv4 } from "uuid";
import cloneDeep from "lodash-es/cloneDeep";
import DetailPane from "@/components/prod/layout/DetailPane.vue";
import DetailPaneRow from "@/components/prod/layout/DetailPaneRow.vue";

const factorStore = useFactorStore();
const {
  factorDetail,
  isEditFactorDetail,
  factorTypeDetail,
  isEditFactorTypeDetail,
  paginationFactorDetail,
  factorSelected,
} = storeToRefs(factorStore);
const { t } = useI18n();
const useSnackbar = useSnackbarStore();

const props = defineProps({
  isCreate: {
    type: Boolean,
    default: false,
  },
});
const factorValueList = ref<any>(null);
const selectedItem = ref<any>(null);
const factorItemCont = ref<any>(null);
const factorItems = ref<any>(null);
const factorLineCoordinates = ref<any>([]);
const isFormValid = ref(false);
const canvasWidth = ref(0);
const showBtnFixed = ref(false);
const scrollEnd = ref(false);
const emit = defineEmits([
  "addFactorSuccess",
  "cancelCreateFactor",
  "editSuccess",
  "cancelEditFactor",
]);

const scrollContainerClass = computed(() => {
  const baseClass = "!px-0 !h-auto";
  const heightClass = isEditFactorDetail.value
    ? "max-h-[calc(100vh-515px)]"
    : "max-h-[calc(100vh-450px)]";
  return [heightClass, baseClass];
});

const checkFactorValueListHeight = () => {
  const scrollCont = document.getElementById("locoSwapper");
  if (scrollCont?.clientHeight && factorItems.value?.length) {
    let itemHeight = 0;
    for (const el of factorItems.value) {
      itemHeight += el.clientHeight;
    }
    if (itemHeight >= scrollCont?.clientHeight - 81) {
      showBtnFixed.value = true;
    } else {
      showBtnFixed.value = false;
    }
  } else {
    showBtnFixed.value = false;
  }
};

const handleCanvasMatrix = () => {
  factorLineCoordinates.value = [];
  if (factorItems.value?.length) {
    for (const el of factorItems.value) {
      const comProElCoor = {
        leftStartPoint: 12,
        topStartPoint: 0,
        leftMovePoint: 36,
        topMovePoint: el.offsetTop + 21,
        lineWidth: 1,
        strokeStyle: "#DCE0E5",
      };
      factorLineCoordinates.value.push(comProElCoor);
    }
  }
};

const setCanvasWidth = () => {
  if (factorDetail.value?.factorValueLst && factorItemCont.value) {
    canvasWidth.value = factorItemCont.value.clientHeight;
  }
};

const handleClickFactorExpand = (event, item) => {
  selectedItem.value = item.factorValueCode;
  scrollEnd.value = false;
  factorDetail.value?.factorValueLst.forEach((factor) => {
    if (factor?.factorValueCode === item?.factorValueCode) {
      factor["expand"] = event;
    }
  });
  canvasWidth.value = 0;
  nextTick(() => {
    setCanvasWidth();
    handleCanvasMatrix();
    checkFactorValueListHeight();
  });
};

const listActions = (item) => {
  if (item?.isAdded || item?.isNew) {
    return [
      {
        name: t("product_platform.actionRemove"),
        icon: TrashIcon,
        iconProps: {
          class: "text-text-lighter",
        },
        onClick: () => {
          factorDetail.value.factorValueLst =
            factorDetail.value.factorValueLst.filter(
              (factor) => factor?.factorValueCode !== item?.factorValueCode
            );
          paginationFactorDetail.value.currentPage = Math.ceil(
            factorDetail.value.factorValueLst.length /
              paginationFactorDetail.value.pageSize
          );
          nextTick(() => {
            checkFactorValueListHeight();
          });
        },
      },
    ];
  }
  return [];
};

const handleAddNewFactorSubItem = (event) => {
  event.preventDefault();
  scrollEnd.value = true;
  const newItem = {
    factorValueCode: uuidv4(),
    factorValueName: "",
    value: "",
    factorCode: factorDetail.value.factorCode,
    useYn: RequiredYn.Yes,
    isNew: true,
  };
  factorDetail.value.factorValueLst.push(newItem);
  nextTick(() => {
    paginationFactorDetail.value.currentPage =
      paginationFactorDetail.value.totalPages;
    setCanvasWidth();
    handleCanvasMatrix();
    checkFactorValueListHeight();
  });
};

const handleEditFactorDetail = async () => {
  if (!isEditFactorTypeDetail.value) {
    factorDetail.value = factorTypeDetail.value?.factorLst?.find(
      (item) => item.factorCode === factorDetail.value?.factorCode
    );
  }
  isEditFactorDetail.value = true;
  isEditFactorTypeDetail.value = true;
  nextTick(() => {
    setCanvasWidth();
    handleCanvasMatrix();
    checkFactorValueListHeight();
  });
};

const handleSubmit = async () => {
  if (props.isCreate) {
    factorDetail.value["isAdded"] = true;
    factorDetail.value["isNew"] = false;
    factorDetail.value.factorValueLst.forEach((item) => {
      if (item?.isNew) {
        item = { ...item, isNew: false, isAdded: true };
      }
    });
    emit("addFactorSuccess", factorDetail.value);
  } else {
    if (!factorDetail.value?.isAdded && !factorDetail.value?.isNew) {
      factorDetail.value["isEdit"] = true;
    }
    factorDetail.value.factorValueLst.forEach((item) => {
      if (item?.isNew) {
        item = { ...item, isNew: false, isAdded: true };
      }
    });
    isEditFactorDetail.value = false;
    emit("editSuccess", factorDetail.value);
  }
  nextTick(() => {
    setCanvasWidth();
    handleCanvasMatrix();
  });
};

const checkDuplicatesName = (arr) => {
  let unique = new Set();
  for (let i = 0; i < arr.length; i++) {
    if (unique.has(arr[i as number]?.factorValueName.toUpperCase())) {
      return true;
    }
    unique.add(arr[i as number].factorValueName.toUpperCase());
  }
  return false;
};

const listItems = computed(() => {
  let items = cloneDeep(factorDetail.value.factorValueLst);
  let startIndex =
    (paginationFactorDetail.value.currentPage - 1) *
    paginationFactorDetail.value.pageSize;
  let endIndex = startIndex + paginationFactorDetail.value.pageSize;
  items = items.filter(
    (_item, index) => index >= startIndex && index < endIndex
  );
  return items;
});

const handleChangePage = (page) => {
  paginationFactorDetail.value.currentPage = page;
  nextTick(() => {
    setCanvasWidth();
    handleCanvasMatrix();
    checkFactorValueListHeight();
  });
};

const onSubmit = () => {
  let invalidChildForm = false;
  let invalidFactorName = false;
  factorDetail.value.factorValueLst.forEach((item) => {
    if (!item.factorValueName || (!item.value && item.value !== 0)) {
      invalidChildForm = true;
    }
  });

  factorTypeDetail.value?.factorLst?.forEach((type) => {
    if (
      type?.factorName.toUpperCase() ===
        factorDetail.value?.factorName.toUpperCase() &&
      type?.factorCode !== factorDetail.value?.factorCode
    ) {
      invalidFactorName = true;
    }
  });

  let invalidDisplayName = checkDuplicatesName(
    factorDetail.value.factorValueLst
  );
  if (!isFormValid.value || invalidChildForm) {
    useSnackbar.showSnackbar(
      t("product_platform.required_field_missing"),
      "error"
    );
    return;
  }
  if (invalidDisplayName || invalidFactorName) {
    useSnackbar.showSnackbar(
      t("product_platform.display_name_duplicated"),
      "error"
    );
    return;
  }
  handleSubmit();
};

const handleCancel = async () => {
  isEditFactorDetail.value = false;
  if (props.isCreate) {
    emit("cancelCreateFactor");
    return;
  }
  if (factorDetail.value.factorValueLst?.length) {
    factorDetail.value.factorValueLst =
      factorDetail.value.factorValueLst.filter((item) => !item.isNew);
  }
  emit("cancelEditFactor", factorDetail.value);
  nextTick(() => {
    setCanvasWidth();
    handleCanvasMatrix();
  });
};
const handleUpdateValue = (emitData) => {
  factorDetail.value.factorValueLst = factorDetail.value.factorValueLst.map(
    (factor) => {
      if (factor?.factorValueCode === emitData?.factorValueCode) {
        return { ...factor, ...emitData };
      }
      return factor;
    }
  );
};
onMounted(() => {
  setCanvasWidth();
  handleCanvasMatrix();
  checkFactorValueListHeight();
});

watch(
  () => [factorDetail.value.factorValueLst, isEditFactorDetail.value],
  (newVal) => {
    if (newVal[0]?.length) {
      canvasWidth.value = 0;
      paginationFactorDetail.value = {
        totalSearchItems: newVal[0]?.length,
        currentPage: paginationFactorDetail.value?.currentPage ?? 1,
        pageSize: isEditFactorDetail.value ? 8 : 10,
        totalItems: newVal[0]?.length,
        totalPages: Math.ceil(
          newVal[0]?.length / (isEditFactorDetail.value ? 8 : 10)
        ),
      };
      nextTick(() => {
        setCanvasWidth();
        handleCanvasMatrix();
        checkFactorValueListHeight();
      });
    }
  },
  { deep: true, immediate: true }
);
watch(
  () => listItems.value,
  (newVal) => {
    if (newVal?.length) {
      canvasWidth.value = 0;
      nextTick(() => {
        setCanvasWidth();
        handleCanvasMatrix();
        checkFactorValueListHeight();
      });
    }
  },
  { deep: true }
);
watch(
  () => props.isCreate,
  (val) => {
    if (val) {
      showBtnFixed.value = false;
    }
  }
);
watch(
  () => factorSelected.value,
  (val) => {
    if (val) {
      selectedItem.value = null;
    }
  },
  {
    deep: true,
  }
);
</script>
<style lang="scss" scoped>
.switch-custom :deep(.v-switch__thumb) {
  height: 16px !important;
  width: 15px !important;
}
.switch-custom :deep(.v-switch__track) {
  height: 20px !important;
  width: 38px !important;
  min-width: 38px !important;
  opacity: 1;
}
.switch-custom :deep(.v-selection-control) {
  min-height: 20px !important;
}
.input-edit.custom :deep(.v-input__control) {
  height: 30px !important;
}
.add-new-section {
  display: flex;
  align-items: center;
  justify-content: center;
  padding-bottom: 5px;
}
.add-new-button {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow:
    2px 1px 6px 0px rgba(30, 38, 91, 0.24),
    2px 1px 18px 0px rgba(21, 24, 66, 0.11) inset,
    -5px -2px 6px 0px rgba(255, 255, 255, 0.64) inset;
}
/** custom switch button **/
:deep(.v-switch--inset .v-switch__thumb) {
  height: 16px;
  width: 16px;
  left: -1px !important;
}

:deep(.v-switch--inset .v-switch__track) {
  height: 20px;
  min-width: 36px;
}
:deep(.v-switch__track) {
  opacity: 1;
  background-color: rgb(220 224 228);
}
.add-new-button.float {
  position: absolute;
  bottom: 126px;
  z-index: 999;
  left: 45.5%;
}
#locoSwapper {
  height: 100%;
  max-height: calc(100vh - 550px);

  .scroll-container {
    overflow-x: hidden !important;
  }
}
</style>
