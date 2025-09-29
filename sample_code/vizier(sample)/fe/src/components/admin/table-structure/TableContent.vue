<template>
  <div
    class="bg-white rounded-[12px] h-full relative flex flex-column max-h-[calc(100vh-137px)] pt-6"
  >
    <div class="flex flex justify-between items-center px-4 h-[40px] mb-2">
      <div class="flex align-center gap-3">
        <span class="text-[#3A3B3D] text-[15px] font-[500]">
          {{
            isEditTable
              ? $t("product_platform.tableStructureEdit")
              : $t("product_platform.tableStructure")
          }}
        </span>
      </div>
      <div>
        <BaseButton
          v-if="!isEditTable"
          :color="ButtonColorType.Secondary"
          @click="handleEditMode"
        >
          <edit-icon class="mr-[6px]" />
          {{ $t("product_platform.edit") }}
        </BaseButton>
        <FileAction
          v-else
          title="Upload Table"
          description="Please upload Table excel file that you have downloaded."
          :is-downloading="downloading"
          @upload-file="handleUploadMatrixTable"
          @download-file="handleDownloadDataTable"
        />
      </div>
    </div>
    <div ref="tableMainContent" class="px-4 h-full">
      <LocomotiveComponent
        :scroll-container-class="['!px-0 table-scroll']"
        top-content-class="z-[1]"
        @call-lazy-load="handleLazyLoad"
      >
        <template #top-content-fixed>
          <table v-if="headerList.length" ref="headerTable">
            <thead>
              <tr class="flex border-b-[1px]">
                <td
                  class="text-[13px] font-[400] !min-w-[40px] text-[#6B6D70] header-item"
                >
                  No
                </td>
                <td
                  v-for="(header, index) in headerList"
                  :key="header.id"
                  class="text-[13px] font-[400] !min-w-[160px] text-[#6B6D70] header-item"
                  :class="[
                    activeCol === index
                      ? isEditTable
                        ? '!bg-[#FEE5E7]'
                        : '!bg-[#DCE0E5]'
                      : '',
                  ]"
                >
                  <div
                    :class="[!header?.isSort && 'l-icon']"
                    @click="handleSort(header)"
                  >
                    <SortTableIcon
                      :fill-up="header?.isSort === 'up' ? '#D9325A' : '#BDC1C7'"
                      :fill-down="
                        header?.isSort === 'down' ? '#D9325A' : '#BDC1C7'
                      "
                    />
                  </div>

                  <span class="max-w-[120px] text-ellipsis">
                    <CustomTooltip :content="header?.columnComment" is-inline />
                  </span>

                  <div :class="[!header?.isFilter && 'l-icon']">
                    <v-menu
                      :close-on-content-click="false"
                      :location="
                        index === headerList?.length - 1
                          ? 'bottom start'
                          : 'bottom center'
                      "
                    >
                      <template #activator="{ props }">
                        <FilterTableIcon
                          v-bind="props"
                          :fill="header?.isFilter ? '#D9325A' : '#BDC1C7'"
                        />
                      </template>

                      <template
                        v-if="
                          [COLUMN_FIELD_TYPE.TF, COLUMN_FIELD_TYPE.TA].includes(
                            header?.columnType
                          )
                        "
                      >
                        <div
                          class="bg-white w-[160px] h-[56px] px-3 py-[10px] mt-3 card-filter"
                        >
                          <BaseInputText
                            v-model="header.fieldValue"
                            :styles="['input-edit !max-w-[160px] h-[36px]']"
                            hide-details
                            :maxlength="header.attrMaxLength"
                            @input="debounceFilter(header.columnName)"
                          />
                        </div>
                      </template>
                      <template
                        v-else-if="
                          [COLUMN_FIELD_TYPE.NF, COLUMN_FIELD_TYPE.RF].includes(
                            header?.columnType
                          )
                        "
                      >
                        <div
                          class="bg-white w-[320px] h-[56px] px-3 py-[10px] mt-3 card-filter"
                        >
                          <div
                            class="flex items-center gap-3 font-medium text-text-base font-size-base font-base"
                          >
                            <BaseInputText
                              v-model="header.fieldValueMin"
                              :styles="['input-edit !max-w-[160px] h-[36px]']"
                              hide-details
                              :maxlength="Number(header.attrMaxLength) || null"
                              @keypress="
                                onlyNumber($event, Number(header.attrMaxLength))
                              "
                              @input="
                                changeValueNumberHeader($event, header, 'min')
                              "
                            />
                            ~
                            <BaseInputText
                              v-model="header.fieldValueMax"
                              :styles="['input-edit !max-w-[160px] h-[36px]']"
                              hide-details
                              :maxlength="Number(header.attrMaxLength) || null"
                              @keypress="
                                onlyNumber($event, Number(header.attrMaxLength))
                              "
                              @input="
                                changeValueNumberHeader($event, header, 'max')
                              "
                            />
                          </div>
                        </div>
                      </template>

                      <template
                        v-else-if="header?.columnType === COLUMN_FIELD_TYPE.DP"
                      >
                        <div
                          class="bg-white w-[320px] h-[56px] px-3 py-[10px] mt-3 card-filter"
                        >
                          <div
                            class="flex items-center gap-3 font-medium text-text-base font-size-base font-base field-dp-filter"
                          >
                            <BaseDateTimePicker
                              v-model="header.fieldValueMin"
                              :teleport-value="false"
                              :auto-apply="false"
                              input-mode
                              enable-time-picker
                              styles="absolute common-datetime-picker rounded-[12px]"
                              @update:model-value="
                                debounceFilter(header.columnName)
                              "
                            />
                            ~
                            <BaseDateTimePicker
                              v-model="header.fieldValueMax"
                              :teleport-value="false"
                              :auto-apply="false"
                              input-mode
                              enable-time-picker
                              styles="absolute common-datetime-picker rounded-[12px]"
                              @update:model-value="
                                debounceFilter(header.columnName)
                              "
                            />
                          </div>
                        </div>
                      </template>
                      <v-list v-else class="mt-3 ml-1 w-[160px] card-filter">
                        <LocomotiveComponent
                          is-show-scrollbar
                          scroll-container-class="max-h-[240px] !px-0 ma-0"
                          scroll-content-class=""
                          :is-stop-propagation-wheel="true"
                        >
                          <v-list-item
                            v-for="(item, i) in header.itemsDL"
                            :key="i"
                            :class="item.inUse && 'bg-item-checked'"
                          >
                            <v-list-item-title
                              class="flex align-center !h-[40px]"
                              :class="item.inUse && 'item-label-check'"
                            >
                              <v-checkbox
                                v-model="item.inUse"
                                :true-icon="TrueIcon"
                                :false-icon="FalseIcon"
                                density="compact"
                                class="custom-checkbox"
                                @change="debounceFilter(header.columnName)"
                              >
                                <template #label>
                                  <span class="!w-[90px] text-ellipsis mt-1">
                                    <CustomTooltip :content="item.cmcdDetlNm" />
                                  </span>
                                </template>
                              </v-checkbox>
                            </v-list-item-title>
                          </v-list-item>
                        </LocomotiveComponent>
                      </v-list>
                    </v-menu>
                  </div>
                </td>
                <td
                  v-if="regularColumnWidth"
                  class="text-[13px] font-[400] text-[#6B6D70] header-item border-l-[1px]"
                  :style="{ width: `${regularColumnWidth}px !important` }"
                ></td>
              </tr>
            </thead>
          </table>
        </template>
        <table class="table-structure-custom">
          <tbody id="body-table">
            <tr
              v-for="(item, index) in tableItemsList"
              :key="item.id"
              class="relative"
              :class="[
                item.status === 'removed'
                  ? '!bg-[#F7F8FA]'
                  : activeRow === index
                    ? isEditTable
                      ? '!bg-[#FFF0F2]'
                      : '!bg-[#F7F8FA]'
                    : '',
              ]"
              @contextmenu="handleRightClick($event, item, index)"
            >
              <th
                class="!h-[40px] !min-w-[40px] text-[13px] font-[400] !text-[#3a3b3d] text-center number-order"
                :class="[
                  checkRemovedTd(index) ? 'td-removed' : '',
                  activeRow === index
                    ? isEditTable
                      ? '!bg-[#FEE5E7]'
                      : '!bg-[#DCE0E5]'
                    : '!bg-[#f7f8fa]',
                ]"
              >
                {{ index + 1 }}
              </th>
              <td
                v-for="(td, indexTd) in Object.keys(item).filter(
                  (value) => value !== 'status'
                )"
                :key="`td_${indexTd}`"
                class="!h-[40px] min-w-[160px] max-w-[160px] text-[13px] font-[400] text-[#3a3b3d]"
                :class="[
                  activeCol === indexTd
                    ? isEditTable
                      ? 'editting'
                      : '!bg-[#F7F8FA]'
                    : '',
                  checkRemovedTd(index) ? 'td-removed' : '',
                  checkEditedTd(index, td, indexTd) && !checkRemovedTd(index)
                    ? 'active-td-edited'
                    : '',
                  checkSelectedTd(index, indexTd)
                    ? isEditTable
                      ? 'active-td-selected-edit'
                      : 'active-td-selected-view'
                    : '',
                  item.status === 'duplicated' ? '!text-[#BA1642]' : '',
                ]"
                @click="onSelectTd(index, indexTd)"
              >
                <template
                  v-if="
                    headerList[indexTd]?.columnKeyYn === RequiredYn.Yes &&
                    !['duplicated', 'new'].includes(item.status)
                  "
                >
                  <div class="input-td-value ml-2">
                    <div v-if="item[td]" class="!max-w-[140px] text-ellipsis">
                      <CustomTooltip :content="item[td]" is-inline />
                    </div>
                  </div>
                </template>
                <template v-else>
                  <template
                    v-if="
                      item.status !== 'removed' &&
                      canEditTd === indexTd &&
                      index === activeRow
                    "
                  >
                    <div class="input-td-value">
                      <template
                        v-if="
                          [COLUMN_FIELD_TYPE.DL, COLUMN_FIELD_TYPE.DM].includes(
                            headerList[indexTd]?.columnType
                          )
                        "
                      >
                        <BaseSelectScroll
                          v-if="
                            headerList[indexTd]?.columnType ===
                            COLUMN_FIELD_TYPE.DL
                          "
                          v-model="item[td]"
                          :options="
                            groupCodeData[headerList[indexTd]?.commGroupCode]
                          "
                          :default-item-select-all="false"
                          :height="36"
                          @update:model-value="handleBlurInput(index)"
                        />

                        <BaseMultiSelect
                          v-else
                          v-model="item[td]"
                          :options="
                            optionsMultiselect(
                              headerList[indexTd]?.commGroupCode
                            )
                          "
                          can-drop-up
                          @change-value="handleBlurInput(index)"
                        />
                      </template>
                      <template
                        v-else-if="
                          [COLUMN_FIELD_TYPE.NF, COLUMN_FIELD_TYPE.RF].includes(
                            headerList[indexTd]?.columnType
                          )
                        "
                      >
                        <!-- abc -->
                        <BaseInputText
                          v-model="item[td]"
                          :styles="['input-edit !max-w-[160px] h-[36px]']"
                          autofocus
                          hide-details
                          :maxlength="getMaxLengthByHeader(indexTd)"
                          @keypress="
                            onlyNumber($event, getMaxLengthField(indexTd, true))
                          "
                          @input="changeValueNumber($event, item, td, indexTd)"
                          @blur="
                            handleBlurInput(index);
                            handleBlurDecimal(index, td);
                          "
                          @keydown.enter="handleKeyEnterTextField(index)"
                        />
                      </template>
                      <template
                        v-else-if="
                          headerList[indexTd]?.columnType ===
                          COLUMN_FIELD_TYPE.DP
                        "
                      >
                        <BaseDateTimePicker
                          v-model="item[td]"
                          :auto-apply="false"
                          input-mode
                          enable-time-picker
                          styles="absolute common-datetime-picker"
                          @update:model-value="handleBlurInput(index)"
                        />
                      </template>

                      <template v-else>
                        <BaseInputText
                          v-model="item[td]"
                          :styles="['input-edit !max-w-[160px] h-[36px]']"
                          autofocus
                          hide-details
                          :maxlength="getMaxLengthByHeader(indexTd)"
                          @blur="handleBlurInput(index)"
                          @keydown.enter="handleKeyEnterTextField(index)"
                        />
                      </template>
                    </div>
                  </template>

                  <template v-else>
                    <div class="ml-2">
                      <template
                        v-if="
                          headerList[indexTd]?.columnType ===
                          COLUMN_FIELD_TYPE.DP
                        "
                      >
                        <template v-if="item[td]">
                          <CustomTooltip
                            :content="
                              item.status !== 'new' && !item[td]
                                ? '[NULL]'
                                : formatDateWithOutSeconds(item[td])
                            "
                          />
                        </template>
                        <div
                          v-else
                          class="text-[#BDC1C7] font-[400] text-[13px]"
                        >
                          <span v-if="item.status !== 'new'">[NULL]</span>
                        </div>
                      </template>
                      <template
                        v-else-if="
                          [COLUMN_FIELD_TYPE.DL, COLUMN_FIELD_TYPE.DM].includes(
                            headerList[indexTd]?.columnType
                          )
                        "
                      >
                        <template v-if="item[td]">
                          <CustomTooltip
                            :is-always-show="
                              headerList[indexTd]?.columnType ===
                                COLUMN_FIELD_TYPE.DM && item[td]?.length
                            "
                          >
                            <div
                              v-if="
                                headerList[indexTd]?.columnType ===
                                COLUMN_FIELD_TYPE.DM
                              "
                              :class="[
                                'dm-view',
                                item.status === 'removed' ? 'dm-removed' : '',
                              ]"
                            >
                              <BaseMultiSelect
                                v-model="item[td]"
                                :options="
                                  optionsMultiselect(
                                    headerList[indexTd]?.commGroupCode
                                  )
                                "
                                disabled-option
                                is-disabled-tooltip
                                can-drop-up
                                :disabled="true"
                              />
                            </div>
                            <span v-else>
                              {{ getValueDLText(indexTd, item[td]) }}
                            </span>
                            <template #content>
                              <span v-if="item.status !== 'new' && !item[td]"
                                >[NULL]</span
                              >
                              <span
                                v-if="
                                  headerList[indexTd]?.columnType ===
                                  COLUMN_FIELD_TYPE.DM
                                "
                              >
                                {{
                                  item[td]?.length
                                    ? (item[td] || []).join(",")
                                    : "[NULL]"
                                }}
                              </span>
                              <span v-else>
                                {{ getValueDLText(indexTd, item[td]) }}
                              </span>
                            </template>
                          </CustomTooltip>
                        </template>
                        <div
                          v-else
                          class="text-[#BDC1C7] font-[400] text-[13px]"
                        >
                          <span v-if="item.status !== 'new'">[NULL]</span>
                        </div>
                      </template>
                      <template v-else>
                        <div
                          v-if="item[td]"
                          class="!max-w-[140px] text-ellipsis"
                        >
                          <CustomTooltip
                            :content="
                              item.status !== 'new' && !item[td]
                                ? '[NULL]'
                                : item[td]
                            "
                            is-inline
                          />
                        </div>
                        <div
                          v-else
                          class="text-[#BDC1C7] font-[400] text-[13px]"
                        >
                          <span v-if="item.status !== 'new'">[NULL]</span>
                        </div>
                      </template>
                    </div>
                  </template>
                </template>
              </td>
              <td
                v-if="regularColumnWidth"
                class="!h-[40px] text-[13px] font-[400] text-[#3a3b3d]"
                :style="{ width: `${regularColumnWidth}px !important` }"
              ></td>
            </tr>
          </tbody>
        </table>
      </LocomotiveComponent>
    </div>
    <div
      v-if="isEditTable"
      class="flex justify-end items-center shrink-0 my-3 gap-2 mr-4"
    >
      <BaseButton :color="ButtonColorType.Gray" @click="handleCancel">
        {{ $t("product_platform.cancel") }}
      </BaseButton>
      <BaseButton :color="ButtonColorType.Secondary" @click="onSubmit">
        <SaveIcon class="mr-[6px]" />
        {{ $t("product_platform.save") }}
      </BaseButton>
    </div>
    <ArrowLeftIcon
      v-if="!isShowTableTypeDetail"
      class="absolute top-[174px] left-0 cursor-pointer text-[#525457] hover:text-[#303132] rotate-180"
      @click="onOpenTableTypeDetail"
    />
  </div>
  <base-popup
    v-model="openPopup"
    :icon="isCancel ? DialogIconType.Warning : DialogIconType.Info"
    :submit-button-text="$t('product_platform.btn_yes')"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="
      $t(
        isCancel
          ? 'product_platform.desc_cancel'
          : 'product_platform.desc_update'
      )
    "
    @on-close="
      () => {
        openPopup = false;
      }
    "
    @on-submit="handleSave"
  />
  <Teleport to="body">
    <TableRowAction
      v-model="openActions"
      :style="actionsBoxPosition"
      :options="options"
    />
  </Teleport>
</template>

<!-- eslint-disable security/detect-object-injection -->
<script lang="ts" setup>
import { useI18n } from "vue-i18n";
import { ButtonColorType, DialogIconType, RequiredYn } from "@/enums";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import TrueIcon from "@/components/prod/icons/TrueIcon.vue";
import FalseIcon from "@/components/prod/icons/FalseIcon.vue";
import useTableStructureStore from "@/store/admin/tableStructure.store";
import { ActionType } from "@/interfaces/prod";
import DuplicateRowIcon from "@/components/prod/icons/DuplicateRowIcon.vue";
import RemoveRowIcon from "@/components/prod/icons/RemoveRowIcon.vue";
import { formatDate, formatDateWithOutSeconds } from "@/utils/format-data";
import { DATE_FORMAT } from "@/constants/index";
import { useGroupCode } from "@/composables/useGroupCode";
import { useSnackbarStore } from "@/store";
import debounce from "lodash-es/debounce";
import cloneDeep from "lodash-es/cloneDeep";
import { useWindowSize, watchDebounced } from "@vueuse/core";
import {
  checkNumberIsInteger,
  formatDataTypeDecimal,
} from "@/utils/extend-utils";
import { useDownloadFile } from "@/composables/useDownloadFIle";
import { API_TABLE_EXPORT_EXCEL } from "@/api/admin/path";

const { width, height } = useWindowSize();
const { groupCodeData, search } = useGroupCode();
const useSnackbar = useSnackbarStore();
const tableStructureStore = useTableStructureStore();
const {
  loading,
  isEditTable,
  headerList,
  tableItemsList,
  tableListParams,
  headerListParams,
  tableItemsListTemp,
  tableSelected,
  isShowTableTypeDetail,
  isGetAllTableRecord,
  lastNameColumnFilter,
  isHasBeenCalled,
} = storeToRefs(tableStructureStore);
const {
  getListHeader,
  getListTableItems,
  putTableStructure,
  resetTableListParams,
} = tableStructureStore;
const { downloading, downloadFile } = useDownloadFile();

const { t, locale } = useI18n();
const tableMainContent = ref<HTMLElement | any>();
const headerTable = ref<HTMLElement | any>();
const activeRow = ref<any>(null);
const activeCol = ref(null);
const canEditTd = ref(null);
const finishCmcd = ref(false);
const isLazy = ref(false);
const finishMappingCodeHeader = ref(false);
const openPopup = ref(false);
const isCancel = ref(false);
const openActions = ref(false);
const regularColumnWidth = ref(0);
const actionsBoxPosition = ref<any>({ top: "0px", left: "0px" });

const onSelectTd = (indexRow, indexCol) => {
  if (tableItemsList.value[indexRow as number]?.status === "removed") {
    return;
  }
  canEditTd.value = null;
  if (
    activeRow.value === indexRow &&
    activeCol.value === indexCol &&
    isEditTable.value
  ) {
    canEditTd.value = indexCol;
  }

  activeRow.value = indexRow;
  activeCol.value = indexCol;
};
const getListHeaderAction = async () => {
  try {
    await getListHeader();
  } catch (error: any) {
    headerList.value = [];
    useSnackbar.showSnackbar(error.errorMsg, "error");
  }
};

const getMaxLengthByHeader = (index) => {
  let maxLength = Number(headerList.value[index as number]?.attrMaxLength);
  if (maxLength) {
    if (checkNumberIsInteger(maxLength)) {
      return maxLength;
    }
    return Number(maxLength) + 1;
  }
  return null;
};
const getMaxLengthField = (index, getAllValue = false) => {
  let maxLength = Number(headerList.value[index as number]?.attrMaxLength);
  if (maxLength) {
    if (checkNumberIsInteger(maxLength)) {
      return maxLength;
    }
    return getAllValue
      ? Number(maxLength)
      : Number((maxLength + "").split(".")[0]) + 1;
  }
  return null;
};

// TODO
const handleUploadMatrixTable = (_file: File) => {
  //
};

const handleDownloadDataTable = async () => {
  try {
    if (!tableSelected.value.tableName) return;
    const params: any = {
      tableName: tableSelected.value.tableName,
      language: locale.value || "en",
    };
    await downloadFile(API_TABLE_EXPORT_EXCEL, params, "TableStructure");
  } catch (error: any) {
    useSnackbar.showSnackbar(
      error?.errorMsg || t("product_platform.something_went_wrong"),
      "error"
    );
  }
};

const onFilterHeader = async (columnName) => {
  headerList.value = headerList.value?.map((header) => {
    let check = false;
    if (header.columnName === columnName) {
      switch (header.columnType) {
        case COLUMN_FIELD_TYPE.TF:
        case COLUMN_FIELD_TYPE.TA:
          check = !["", null, undefined].includes(header.fieldValue);
          break;
        case COLUMN_FIELD_TYPE.DL:
        case COLUMN_FIELD_TYPE.DM:
          check = header.itemsDL.some((itemInUse) => !itemInUse.inUse);
          break;
        case COLUMN_FIELD_TYPE.NF:
        case COLUMN_FIELD_TYPE.RF:
        case COLUMN_FIELD_TYPE.DP:
          check =
            !["", null, undefined].includes(header.fieldValueMin) ||
            !["", null, undefined].includes(header.fieldValueMax);
          break;
      }
      lastNameColumnFilter.value = columnName;
      return {
        ...header,
        isFilter: check,
        isLastColFilter: check,
      };
    }

    return { ...header, isLastColFilter: false };
  });

  tableListParams.value.fieldSearchs = headerList.value
    ?.filter((xxx) => xxx.isFilter)
    ?.map((header) => {
      if (header.isFilter) {
        return {
          fieldName: header?.columnName,
          fieldType: header?.columnType,
          fieldValue: [COLUMN_FIELD_TYPE.DL, COLUMN_FIELD_TYPE.DM].includes(
            header.columnType
          )
            ? JSON.stringify(
                header.itemsDL
                  .filter((item) => item.inUse)
                  .map((inUseItem) => inUseItem.cmcdDetlId)
              )
            : header?.fieldValue,
          fieldValueMin: header?.fieldValueMin,
          fieldValueMax: header?.fieldValueMax,
        };
      }
    });
  headerListParams.value.fieldSearchs = headerList.value
    ?.filter((xxx) => xxx.isLastColFilter)
    ?.map((header) => {
      if (header.isFilter) {
        return {
          fieldName: header?.columnName,
          fieldType: header?.columnType,
          fieldValue: [COLUMN_FIELD_TYPE.DL, COLUMN_FIELD_TYPE.DM].includes(
            header.columnType
          )
            ? JSON.stringify(
                header.itemsDL
                  .filter((item) => item.inUse)
                  .map((inUseItem) => inUseItem.cmcdDetlId)
              )
            : header?.fieldValue,
          fieldValueMin: header?.fieldValueMin,
          fieldValueMax: header?.fieldValueMax,
        };
      }
    });
  tableItemsList.value = [];
  tableItemsListTemp.value = [];
  isGetAllTableRecord.value = false;
  tableListParams.value.page = 1;
  await tableStructureStore.getListHeader(true);
  await tableStructureStore.getListTableItems();

  headerList.value.forEach((headerCol) => {
    if (
      !headerCol?.isLastColFilter &&
      [COLUMN_FIELD_TYPE.DL, COLUMN_FIELD_TYPE.DM].includes(
        headerCol.columnType
      )
    ) {
      const headerTotalSelect = groupCodeData.value[
        headerCol.commGroupCode
      ].map((item) => {
        const matchItem = headerCol.itemsDL.find(
          (col) => col.cmcdDetlId === item.cmcdDetlId
        );
        return matchItem
          ? matchItem
          : {
              ...item,
              inUse: true,
            };
      });
      headerCol.itemsDL = headerTotalSelect.filter((option) =>
        headerCol.allowedFilters.some((item) => item === option.cmcdDetlId)
      );
    }
  });
};
const debounceFilter = debounce(onFilterHeader, 800);
const handleEditMode = () => {
  canEditTd.value = null;
  activeRow.value = null;
  activeCol.value = null;
  isEditTable.value = true;
};

const addRowLast = () => {
  let objNew = {} as any;
  headerList.value.forEach((header) => {
    objNew[header.columnName] = null;
  });
  tableItemsListTemp.value.push({
    ...objNew,
    status: "new",
  });
  tableItemsList.value.push({
    ...objNew,
    status: "new",
  });
};

const onlyNumber = ($event, maxLength = null) => {
  let keyCode = $event.keyCode ? $event.keyCode : $event.which;

  if (maxLength && !checkNumberIsInteger(maxLength)) {
    if (keyCode !== 46 && (keyCode < 48 || keyCode > 57)) {
      $event.preventDefault();
    }
  } else {
    if (keyCode < 48 || keyCode > 57) {
      $event.preventDefault();
    }
  }
};

const changeValueNumber = (event, item, td, indexTd) => {
  let valueOld = event.target.value;
  let isDecimal = !checkNumberIsInteger(getMaxLengthField(indexTd, true));

  item[td as string] = formatDataTypeDecimal(
    valueOld,
    isDecimal,
    getMaxLengthField(indexTd, true)
  );
};

const changeValueNumberHeader = (event, header, type) => {
  let valueOld = event.target.value;
  let isDecimal = !checkNumberIsInteger(Number(header.attrMaxLength));

  if (type === "min") {
    header.fieldValueMin = formatDataTypeDecimal(
      valueOld,
      isDecimal,
      Number(header.attrMaxLength)
    );
  } else {
    header.fieldValueMax = formatDataTypeDecimal(
      valueOld,
      isDecimal,
      Number(header.attrMaxLength)
    );
  }
  debounceFilter(header.columnName);
};

const handleBlurInput = (indexRow) => {
  tableItemsList.value = tableItemsList.value.map((item, index) => {
    if (index === indexRow && !item.status) {
      return {
        ...item,
        status: "edited",
      };
    }
    return {
      ...item,
    };
  });

  if (indexRow === tableItemsList.value?.length - 1) {
    addRowLast();
  }
};

const handleBlurDecimal = (indexRow, td) => {
  let arr = tableItemsList.value[indexRow][td]?.split("");

  if (arr[arr.length - 1] === ".") {
    tableItemsList.value[indexRow][td] = arr
      .filter((xxx) => xxx !== ".")
      .join()
      .replaceAll(",", "");
  }
};

const handleCancel = () => {
  isCancel.value = true;
  openPopup.value = true;
};

const onSubmit = () => {
  isCancel.value = false;
  openPopup.value = true;
};
const handleKeyEnterTextField = (index) => {
  handleBlurInput(index);
  canEditTd.value = null;
};

const handleSave = async () => {
  if (isCancel.value) {
    openPopup.value = false;
    isEditTable.value = false;
    canEditTd.value = null;
    resetTableListParams();
    nextTick(async () => {
      await fetchTableData();
    });
  } else {
    const dataUpdateTable = {
      deleteData:
        tableItemsList.value
          ?.filter((item) => item.status === "removed")
          .map((item) =>
            Object.values(item)
              .filter(
                (xxx) =>
                  !["removed", "edited", "new", "duplicated"].includes(
                    String(xxx)
                  )
              )
              .map((value, index) => {
                if (
                  headerList.value[index]?.columnType === COLUMN_FIELD_TYPE.DP
                ) {
                  return formatDate(
                    value,
                    DATE_FORMAT.DATE_TYPE,
                    DATE_FORMAT.DEFAULT_DATE_FORMAT_MOMENT_OUTPUT
                  );
                }
                if (
                  headerList.value[index]?.columnType === COLUMN_FIELD_TYPE.DM
                ) {
                  return JSON.stringify(value);
                }
                return value ? String(value) : null;
              })
          ) || [],
      updateData:
        tableItemsList.value
          ?.filter((item) => item.status === "edited")
          .map((item) =>
            Object.values(item)
              .filter(
                (xxx) =>
                  !["removed", "edited", "new", "duplicated"].includes(
                    String(xxx)
                  )
              )
              .map((value, index) => {
                if (
                  headerList.value[index]?.columnType === COLUMN_FIELD_TYPE.DP
                ) {
                  return formatDate(
                    value,
                    DATE_FORMAT.DATE_TYPE,
                    DATE_FORMAT.DEFAULT_DATE_FORMAT_MOMENT_OUTPUT
                  );
                }
                if (
                  headerList.value[index]?.columnType === COLUMN_FIELD_TYPE.DM
                ) {
                  return JSON.stringify(value);
                }
                return value ? String(value) : null;
              })
          ) || [],
      addData:
        tableItemsList.value
          ?.filter((item) => ["new", "duplicated"].includes(item.status))
          .map((item) =>
            Object.values(item)
              .filter(
                (xxx) =>
                  !["removed", "edited", "new", "duplicated"].includes(
                    String(xxx)
                  )
              )
              .map((value, index) => {
                if (
                  headerList.value[index]?.columnType === COLUMN_FIELD_TYPE.DP
                ) {
                  return formatDate(
                    value,
                    DATE_FORMAT.DATE_TYPE,
                    DATE_FORMAT.DEFAULT_DATE_FORMAT_MOMENT_OUTPUT
                  );
                }
                if (
                  headerList.value[index]?.columnType === COLUMN_FIELD_TYPE.DM
                ) {
                  return JSON.stringify(value);
                }
                return value ? String(value) : null;
              })
          )
          .map((subArr) => {
            if (subArr?.length === headerList.value?.length) {
              return subArr.map((item) => {
                if (item === "null") {
                  return null;
                }
                return item;
              });
            }
            return headerList.value.map((_, index) => {
              if (subArr[index]) {
                return subArr[index];
              }
              return null;
            });
          })
          .filter((subArr) => subArr.some((sub) => !!sub)) || [],
    };

    try {
      const res = await putTableStructure(dataUpdateTable);

      if (res.status == 200) {
        useSnackbar.showSnackbar(
          t("product_platform.successfully_saved"),
          "success"
        );
        openPopup.value = false;
        isEditTable.value = false;
        isCancel.value = false;
        activeCol.value = null;
        activeRow.value = null;
        canEditTd.value = null;
        finishCmcd.value = false;
        finishMappingCodeHeader.value = false;
        resetTableListParams();
        await getListHeaderAction();
        await fetchTableData();
      }
    } catch (error: any) {
      // tableItemsList.value = tableItemsListTemp.value;
      openPopup.value = false;

      useSnackbar.showSnackbar(error.errorMsg, "error");
    }
  }
};

const optionsMultiselect = computed(() => (code: any) => {
  const result =
    groupCodeData.value[code]?.map((option: any) => ({
      label: option.cmcdDetlNm,
      value: option.cmcdDetlId,
    })) || [];
  return result;
});

const checkRemovedTd = (indexRow) => {
  return tableItemsList.value[indexRow]?.status === "removed";
};

const checkSelectedTd = (indexRow, indexTd) => {
  return activeRow.value === indexRow && activeCol.value === indexTd;
};

const checkEditedTd = (index, td, indexTd) => {
  let type = headerList.value[indexTd]?.columnType;

  if ([COLUMN_FIELD_TYPE.NF, COLUMN_FIELD_TYPE.RF].includes(type)) {
    return (
      tableItemsListTemp.value[index][td] !== tableItemsList.value[index][td]
    );
  }

  if (type === COLUMN_FIELD_TYPE.DP) {
    return (
      formatDate(
        tableItemsListTemp.value[index][td],
        DATE_FORMAT.DATE_TYPE,
        DATE_FORMAT.DEFAULT_DATE_FORMAT_MOMENT_OUTPUT
      ) !==
      formatDate(
        tableItemsList.value[index][td],
        DATE_FORMAT.DATE_TYPE,
        DATE_FORMAT.DEFAULT_DATE_FORMAT_MOMENT_OUTPUT
      )
    );
  }

  if ([COLUMN_FIELD_TYPE.DM].includes(type)) {
    return (
      tableItemsListTemp.value[index][td]?.length !==
      tableItemsList.value[index][td]?.length
    );
  }

  const valueTemp = tableItemsListTemp.value[index][td] || null;
  const value = tableItemsList.value[index][td] || null;

  return valueTemp !== value;
};

const handleSort = async (header) => {
  if (!header.isSort) {
    header.isSort = "up";
  } else {
    header.isSort = header.isSort === "up" ? "down" : null;
  }

  const columnName = header.columnName;
  const fieldSorts = tableListParams.value.fieldSorts || {};

  if (fieldSorts.hasOwnProperty(columnName)) {
    const currentSort = fieldSorts[columnName];

    delete fieldSorts[columnName];

    if (currentSort === "asc") {
      fieldSorts[columnName] = "desc";
    }
  } else {
    fieldSorts[columnName] = "asc";
  }

  tableItemsList.value = [];
  tableItemsListTemp.value = [];
  isGetAllTableRecord.value = false;
  tableListParams.value.page = 1;
  tableListParams.value.fieldSorts = { ...fieldSorts };

  await tableStructureStore.getListTableItems();
};

const handleRightClick = (event, item, index) => {
  if (isEditTable.value && item.status !== "new") {
    activeRow.value = index;
    actionsBoxPosition.value.top = event.clientY + "px";
    if (width.value - event.clientX >= 170) {
      actionsBoxPosition.value.left = event.clientX + 5 + "px";
    } else {
      actionsBoxPosition.value.left = event.clientX - 165 + "px";
    }
    openActions.value = true;
  }
};

const options = computed<ActionType[]>(() => {
  let ops = [
    {
      name: t("product_platform.duplicateRow"),
      icon: DuplicateRowIcon,
      onClick: () => {
        let itemDuplicate = cloneDeep({
          ...tableItemsList.value[activeRow.value || 0],
          status: "duplicated",
        });
        tableItemsList.value = [
          ...tableItemsList.value.slice(0, (activeRow.value || 0) + 1),
          itemDuplicate,
          ...tableItemsList.value.slice((activeRow.value || 0) + 1),
        ];
        tableItemsListTemp.value = cloneDeep([
          ...tableItemsListTemp.value.slice(0, (activeRow.value || 0) + 1),
          itemDuplicate,
          ...tableItemsListTemp.value.slice((activeRow.value || 0) + 1),
        ]);
        activeRow.value = Number(activeRow.value) + 1 || 0;
      },
    },
  ];
  if (tableItemsList.value[activeRow.value || 0]?.status !== "removed") {
    ops.push({
      name: t("product_platform.removeRow"),
      icon: RemoveRowIcon,
      onClick: () => {
        let itemRemove = tableItemsList.value[activeRow.value];
        if (itemRemove.status === "duplicated") {
          tableItemsList.value = tableItemsList.value?.filter(
            (xxx, indexT) => indexT !== activeRow.value
          );
          tableItemsListTemp.value = tableItemsListTemp.value?.filter(
            (xxx, indexT) => indexT !== activeRow.value
          );
        } else {
          tableItemsList.value = tableItemsList.value.map((itemTb, index) => {
            if (index === activeRow.value) {
              return {
                ...itemTb,
                status: "removed",
              };
            }
            return itemTb;
          });
        }

        activeCol.value = null;
        activeRow.value = null;
      },
    });
  }
  return ops;
});

const getListExistGroupCode = (code, keyColumn) => {
  const codeArr = groupCodeData.value[code as string];
  if (codeArr?.length) {
    let typeIsDM =
      headerList.value.find((header) => header.columnName === keyColumn)
        ?.columnType === COLUMN_FIELD_TYPE.DM;
    if (typeIsDM) {
      let arrNew: any = [];

      let arrVal = tableItemsList.value?.map(
        (item) => item[keyColumn as string]
      );
      arrVal.forEach((xxx) => {
        if (xxx?.length) {
          xxx?.forEach((yyy) => {
            if (!arrNew.find((arrItem) => arrItem === yyy)) {
              arrNew.push(yyy);
            }
          });
        }
      });

      return codeArr
        .filter((codeItem) => arrNew.includes(codeItem?.cmcdDetlId))
        .map((code) => ({
          ...code,
          inUse: true,
        }));
    } else {
      const arrCodeExistTable = tableItemsList.value?.map(
        (item) => item[keyColumn]
      );

      return codeArr
        .filter((codeItem) => arrCodeExistTable.includes(codeItem?.cmcdDetlId))
        .map((code) => ({
          ...code,
          inUse: true,
        }));
    }
  }

  return [];
};

const getHeightTable = computed(() => {
  if (isEditTable.value) {
    return "270px";
  }
  return "218px";
});

const onOpenTableTypeDetail = () => {
  isShowTableTypeDetail.value = true;
};
const getValueDLText = (indexTd, key) => {
  let code = headerList.value[indexTd as number]?.commGroupCode;

  if (groupCodeData.value && groupCodeData.value[code as string] && code) {
    const codeArr = groupCodeData.value[code as string] || [];
    if (codeArr.length) {
      return codeArr.find((code) => code.cmcdDetlId === key)?.cmcdDetlNm;
    }
  }

  return null;
};

const handleLazyLoad = async () => {
  if (!loading.value && !isGetAllTableRecord.value) {
    tableListParams.value.page += 1;
    isLazy.value = true;
    await fetchTableData();
  }
};

// const calcRowSizeFetchApi = () => {
//   const rowTotal = Math.floor((tableMainContent.value.clientHeight - 55) / 40);
//   tableListParams.value.size = rowTotal;
// };

const calcColumnAdd = () => {
  if (
    tableMainContent.value?.clientWidth - 32 >
    40 + headerList.value?.length * 160 + 10
  ) {
    regularColumnWidth.value =
      tableMainContent.value.clientWidth -
      (72 + headerList.value?.length * 160);
  } else {
    regularColumnWidth.value = 0;
  }
};

const fetchTableData = async () => {
  if (!tableSelected.value) {
    return;
  }
  if (!isEditTable.value) {
    // calcRowSizeFetchApi();
  }
  loading.value = true;
  await getListTableItems();
  loading.value = false;
  isLazy.value = false;
  calcColumnAdd();
};

watchDebounced(
  () => height.value,
  async () => {
    resetTableListParams();
    await fetchTableData();
  },
  { debounce: 1000, maxWait: 1000 }
);

watch(
  () => tableSelected.value,
  async (val) => {
    if (val) {
      tableItemsList.value = [];
      tableItemsListTemp.value = [];
      activeCol.value = null;
      activeRow.value = null;
      finishMappingCodeHeader.value = false;
      finishCmcd.value = false;

      await getListHeaderAction();
      await fetchTableData();
    }
  },
  { deep: true }
);

watch(
  () => headerList.value,
  async (newVal) => {
    let arrs = newVal
      .filter((xxx) => xxx.commGroupCode)
      .map((arr) => arr.commGroupCode);
    if (arrs.length) {
      if (!finishCmcd.value) {
        await search(arrs);
        finishCmcd.value = true;
      }
    }
  },
  { deep: true }
);

watch(
  () => [tableItemsList.value, isLazy.value],
  () => {
    if (!finishMappingCodeHeader.value || isLazy.value) {
      headerList.value = headerList.value.map((header) => {
        if (
          [COLUMN_FIELD_TYPE.DL, COLUMN_FIELD_TYPE.DM].includes(
            header.columnType
          ) &&
          !header?.isFilter
        ) {
          return {
            ...header,
            itemsDL:
              header.itemsDL?.length && !isLazy.value
                ? header.itemsDL
                : getListExistGroupCode(
                    header.commGroupCode,
                    header.columnName
                  ),
          };
        }

        return header;
      });
      // finishMappingCodeHeader.value = true;
    }
  }
);

onMounted(async () => {
  if (!isHasBeenCalled.value) {
    await getListHeaderAction();
    await fetchTableData();
  } else {
    isLazy.value = !isLazy.value;
  }
  [...document.querySelectorAll(".table-structure-custom")].forEach((el) => {
    el.addEventListener("contextmenu", (event) => event.preventDefault());
  });
  isHasBeenCalled.value = true;
});

defineExpose({ calcColumnAdd });
</script>
<style scoped lang="scss">
.table-scroll {
  border-radius: 8px;
  max-height: calc(100vh - v-bind(getHeightTable));
}
.table-scroll table {
  border-collapse: separate;
  border-spacing: 0;
}
.table-scroll th,
.table-scroll td {
  border-top: 1px solid #e6e9ed;
  border-left: 1px solid #e6e9ed;
  cursor: pointer;
}
.table-scroll .header-item {
  height: 40px;
  background: #f7f8fa;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 4px;
  &:first-child {
    border-top-left-radius: 8px;
    border-right-width: 1px;
    position: sticky;
    left: 0;
    z-index: 2;
  }
  &:last-child {
    border-top-right-radius: 8px;
  }
}
th:first-child {
  position: sticky;
  left: 0;
  z-index: 2;
  border-right: 1px solid #e6e9ed;
}
th:nth-child(2) {
  border-left: 0px;
}
th:last-child {
  border-right: 1px solid #e6e9ed;
}
td:nth-child(2) {
  border-left: 0px;
}
td:last-child {
  border-right: 1px solid #e6e9ed;
}
thead th:first-child {
  z-index: 5;
}
tbody tr:last-child {
  th,
  td {
    border-bottom: 1px solid #e6e9ed;
  }
}
/* top-left border-radius */
table thead tr:first-child th:first-child {
  border-top-left-radius: 8px;
}

/* top-right border-radius */
table thead tr:first-child th:last-child {
  border-top-right-radius: 8px;
}

/* bottom-left border-radius */
table tbody tr:last-child th:first-child {
  border-bottom-left-radius: 8px;
}

/* bottom-right border-radius */
table tbody tr:last-child td:last-child {
  border-bottom-right-radius: 8px;
}
table tbody tr:first-child {
  td,
  th {
    border-top-width: 0px;
  }
}
.header-item {
  &:hover {
    .l-icon {
      visibility: unset;
    }
  }

  .l-icon {
    visibility: hidden;
  }
}

:deep() .bg-item-checked {
  background-color: #fff0f2;
}
:deep() .v-list-item--active:not(.v-list-item--link) .v-list-item__overlay {
  opacity: 0 !important;
}
:deep() .v-list-item--active > .v-list-item__overlay {
  opacity: 0 !important;
}
:deep() .value-changed input {
  color: #ba1642 !important;
}
:deep() .v-list {
  box-shadow: 1px 1px 10px 0px #0000001f !important;
}
:deep() .v-list .v-label {
  opacity: unset !important;
}

:deep(.custom-checkbox .v-label) {
  font-size: 13px;
  font-weight: 500;
  line-height: 19.5px;
  letter-spacing: 0.25px;
  text-align: left;
}
:deep(.v-list-item) {
  &:hover {
    background-color: #fff0f2;
  }
  .v-input {
    height: 40px;
  }

  .v-label {
    margin-left: 10px;
    color: #3a3b3d;
    font-weight: 400;
    font-size: 13px;
    width: 120px;
  }
}

.card-filter {
  border-radius: 8px !important;
  box-shadow: 2px 2px 16px 0px #0000001f !important;
}

.input-td-value {
  max-width: 159px;
  :deep(.input-edit .v-field) {
    height: 37px !important;
    border-radius: unset;
  }
  :deep(.custom-text-field) {
    border: unset;
  }
  :deep(.v-field__field) {
    height: 37px !important;
  }
  :deep(.v-field__input) {
    padding: unset;
    padding-left: 8px;
  }
  :deep(.custom-select) {
    border-radius: unset !important;
  }
  :deep(.dp__input) {
    padding-left: 8px !important;
  }
  :deep(.custom-select) {
    & > div:first-of-type {
      margin-left: -5px !important;
    }
  }
  :deep(.multi-select) {
    border-radius: unset !important;
    height: 38px !important;
    background-color: unset !important;

    & > div:first-of-type {
      height: 38px !important;
      margin-left: -4px !important;
    }
    &.bg-\[\#FFFFFF\] {
      background-color: #fff !important;
    }
  }
}

td .dm-view {
  :deep().multi-select {
    border-radius: unset !important;
    height: 38px !important;
    margin-left: -11px;
    background-color: unset !important;
  }
  .border {
    border-width: 0px !important;
  }
  :deep().disable-field {
    height: 38px !important;
  }
}

.dm-removed {
  opacity: 0.2 !important;
}
.editting {
  background: #fff0f2;
  .input-td-value {
    height: 38px;
    line-height: 38px;
  }
}
.active-td-edited {
  color: #ba1642 !important;
  border: solid 1px #d9325a !important;
  .input-td-value {
    height: 38px;
  }
  :deep(.v-input) {
    height: 37px !important;
  }
  :deep(.input-edit .v-field) {
    height: 36px !important;
  }
}
:deep(.common-datetime-picker .dp__pointer) {
  height: 38px !important;
  border-radius: unset !important;
  max-width: 158px !important;
}
:deep(.field-dp-filter .common-datetime-picker input) {
  border-radius: 8px !important;
  height: 36px !important;
}

:deep(.card-filter .dp__input_wrap) {
  width: 130px !important;
  .dp__clear_icon {
    right: 23%;
  }
}

.td-removed {
  color: #bdc1c7 !important;
}
.active-td-selected-edit {
  border: solid 1px #d9325a !important;
}
.active-td-selected-view {
  border: solid 1px #3a3b3d !important;
}
</style>
