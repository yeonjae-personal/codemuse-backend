<template>
  <div v-if="props.item.selected" class="action-buttons">
    <template v-if="props.item.isEdit">
      <button @click.stop.prevent="handleSave">
        <CustomTooltip
          :content="$t('product_platform.save')"
          is-always-show
          class="!w-auto p-1"
        >
          <SaveIcon fill="#6B6D70" size="18" />
        </CustomTooltip>
      </button>
      <button @click.stop.prevent="handleClose">
        <CustomTooltip
          :content="$t('product_platform.cancel')"
          is-always-show
          class="!w-auto p-1"
        >
          <CloseSmallIcon />
        </CustomTooltip>
      </button>
    </template>
    <template v-else>
      <button v-if="props.item.sort !== 1" @click.stop.prevent="handleMoveUp">
        <CustomTooltip
          :content="$t('product_platform.moveUp')"
          is-always-show
          class="!w-auto p-1"
        >
          <ArrowUpIcon />
        </CustomTooltip>
      </button>
      <button
        v-if="props.item.sort < countValidationItem"
        @click.stop.prevent="handleMoveDown"
      >
        <CustomTooltip
          :content="$t('product_platform.moveDown')"
          is-always-show
          class="!w-auto p-1"
        >
          <ArrowDownIcon />
        </CustomTooltip>
      </button>
      <button v-if="!props.item.disabled" @click.stop.prevent="handleEdit">
        <CustomTooltip
          :content="$t('product_platform.update')"
          is-always-show
          class="!w-auto p-1"
        >
          <EditIcon fill="#6B6D70" />
        </CustomTooltip>
      </button>
      <button
        v-if="countValidationItem > 1 && props.item.temp"
        @click.stop.prevent="handleRemove"
      >
        <CustomTooltip
          :content="$t('product_platform.actionRemove')"
          is-always-show
          class="!w-auto p-1"
        >
          <DeleteIcon />
        </CustomTooltip>
      </button>
      <button
        v-if="!props.item.temp && !props.item.disabled"
        @click.stop.prevent="handleExpired"
      >
        <CustomTooltip
          :content="$t('product_platform.actionExpire')"
          is-always-show
          class="!w-auto p-1"
        >
          <ExpireIcon />
        </CustomTooltip>
      </button>
      <button v-if="props.item.disabled" @click.stop.prevent="handleEnabled">
        <CustomTooltip
          :content="$t('product_platform.actionEnable')"
          is-always-show
          class="!w-auto p-1"
        >
          <EnableIcon />
        </CustomTooltip>
      </button>
      <button
        v-if="!props.item.temp && props.item.type === 'validation'"
        @click.stop.prevent="handleShowHistory"
      >
        <CustomTooltip
          :content="$t('product_platform.history')"
          is-always-show
          class="!w-auto p-1"
        >
          <HistoryIcon />
        </CustomTooltip>
      </button>
    </template>
  </div>
  <DateTimePopup
    v-model:open-model="isOpenPopup"
    v-model="dateData"
    :modal-title="$t('product_platform.expire_validation')"
    :min-end-date="dateData.startDate"
    :disabled-start-date="true"
    :is-show-popup-waring="false"
    :text-btn-cancel="$t('product_platform.cancel')"
    :text-btn-add="$t('product_platform.save')"
    @submit="handleSubmitDialog"
    @close="handleCloseDialog"
  />
  <DateTimePopup
    v-model:open-model="isOpenEnablePopup"
    v-model="dateData"
    :modal-title="$t('product_platform.enable_validation')"
    :min-start-date="currentDate"
    :min-end-date="dateData.startDate"
    :required-start-date="true"
    :text-btn-cancel="$t('product_platform.cancel')"
    :text-btn-add="$t('product_platform.save')"
    @submit="handleSubmitEnableDialog"
    @close="handleCloseEnableDialog"
  />
  <BasePopup
    v-model="confirmPopup"
    :cancel-button-text="$t('product_platform.cancel')"
    :submit-button-text="$t('product_platform.save')"
    persistent
    @on-close="handleCloseConfirm"
    @on-submit="handleSubmitConfirm"
  >
    <template #body>
      <v-card-text class="!px-6 !pb-2 !pt-4">
        <p>{{ $t("product_platform.theActionExitsInOtherRules") }}</p>
        <p>{{ $t("product_platform.doYouWantToContinueSavingIt") }}</p>
      </v-card-text>
    </template>
  </BasePopup>
</template>
<script lang="ts" setup>
type Props = {
  item: any;
};

import BasePopup from "@/components/prod/common/BasePopup.vue";
import DateTimePopup from "@/components/prod/common/DateTimePopup.vue";
import ArrowDownIcon from "@/components/prod/icons/ArrowDownIcon.vue";
import ArrowUpIcon from "@/components/prod/icons/ArrowUpIcon.vue";
import CloseSmallIcon from "@/components/prod/icons/CloseSmallIcon.vue";
import DeleteIcon from "@/components/prod/icons/DeleteIcon.vue";
import EditIcon from "@/components/prod/icons/EditIcon.vue";
import EnableIcon from "@/components/prod/icons/EnableIcon.vue";
import ExpireIcon from "@/components/prod/icons/ExpireIcon.vue";
import HistoryIcon from "@/components/prod/icons/HistoryIcon.vue";
import SaveIcon from "@/components/prod/icons/SaveIcon.vue";
import { DATE_FORMAT } from "@/constants/";
import { useSnackbarStore } from "@/store";
import customValidationStore from "@/store/admin/customValidation.store";
import moment from "moment-timezone";
import { useI18n } from "vue-i18n";
const { t } = useI18n();
const {
  deleteCustomValidationItem,
  updateEditCustomValidationItem,
  moveUpCustomValidationItem,
  moveDownCustomValidationItem,
  saveCustomValidationItem,
  updateShowHistory,
  updateCustomValidationItemDate,
  setValidCode,
  checkEnableValidationItem,
  checkDisabledItem,
  checkDuplicateAction,
} = customValidationStore();
const { countValidationItem } = storeToRefs(customValidationStore());
const { showSnackbar } = useSnackbarStore();
const props = defineProps<Props>();
const emits = defineEmits(["onCancel"]);

const currentAction = ref<"edit" | "enable" | "">("");
const isOpenPopup = ref(false);
const isOpenEnablePopup = ref(false);
const confirmPopup = ref(false);
// const startDate = ref(props.item.startDate);
// const endDate = ref(props.item.endDate);

const dateData = reactive({
  startDate: props.item.startDate,
  endDate: props.item.endDate,
});

const currentDate = computed(() =>
  moment().format(DATE_FORMAT.DATE_FORMAT_WITHOUT_TIME_REVERSE)
);

const handleSave = async () => {
  try {
    if (checkDuplicateAction(props.item.id)) {
      currentAction.value = "edit";
      confirmPopup.value = true;
    } else {
      await saveCustomValidationItem(props.item.id);
      showSnackbar(t("product_platform.saveSuccessfully"), "success");
    }
  } catch (error: any) {
    showSnackbar(error.errorMsg, "error");
  }
};

const handleCloseConfirm = () => {
  confirmPopup.value = false;
};

const handleSubmitConfirm = async () => {
  const actionHandlers: Record<string, () => Promise<void> | void> = {
    edit: async () => {
      await saveCustomValidationItem(props.item.id);
      showSnackbar(t("product_platform.saveSuccessfully"), "success");
    },
    enable: () => {
      updateCustomValidationItemDate(
        props.item.id,
        dateData.startDate,
        dateData.endDate
      );
      showSnackbar(t("product_platform.enableSuccessfully"), "success");
    },
  };

  try {
    if (currentAction.value in actionHandlers) {
      await actionHandlers[currentAction.value]();
    }
  } catch (error: any) {
    showSnackbar(error.errorMsg, "error");
  } finally {
    confirmPopup.value = false;
  }
};

const handleClose = () => {
  updateEditCustomValidationItem(props.item.id, false);
  emits("onCancel", true);
};

const handleEdit = () => {
  updateEditCustomValidationItem(props.item.id, true);
};

const handleRemove = () => {
  deleteCustomValidationItem(props.item.id);
};

const handleMoveUp = () => {
  moveUpCustomValidationItem(props.item.id);
};

const handleMoveDown = () => {
  moveDownCustomValidationItem(props.item.id);
};

const handleEnabled = () => {
  const message = checkEnableValidationItem(props.item);
  if (message) {
    showSnackbar(message, "error");
  } else {
    dateData.startDate = props.item.startDate;
    dateData.endDate = props.item.endDate;
    isOpenEnablePopup.value = true;
  }
};

const handleExpired = () => {
  dateData.startDate = props.item.startDate;
  dateData.endDate = props.item.endDate;
  isOpenPopup.value = true;
};

const handleCloseDialog = () => {
  isOpenPopup.value = false;
};

const handleSubmitDialog = () => {
  if (!dateData.endDate) {
    showSnackbar(t("product_platform.required_end_date"), "error");
    return;
  }
  try {
    updateCustomValidationItemDate(
      props.item.id,
      props.item.startDate,
      dateData.endDate
    );
    showSnackbar(t("product_platform.expireSuccessfully"), "success");
  } catch (error: any) {
    showSnackbar(error.errorMsg, "error");
  }
  isOpenPopup.value = false;
};

const handleCloseEnableDialog = () => {
  isOpenEnablePopup.value = false;
};

const handleSubmitEnableDialog = () => {
  if (!dateData.startDate) {
    showSnackbar(t("product_platform.required_start_date"), "error");
    return;
  }
  const isDisabledItem = checkDisabledItem(
    dateData.startDate,
    dateData.endDate
  );
  const isValidStartDate = moment(dateData.startDate).isBefore(
    currentDate.value
  );
  if (isDisabledItem || isValidStartDate) {
    showSnackbar(t("product_platform.pleaseInputCorrectDateToEnable"), "error");
    return;
  }
  try {
    if (checkDuplicateAction(props.item.id)) {
      currentAction.value = "enable";
      confirmPopup.value = true;
    } else {
      updateCustomValidationItemDate(
        props.item.id,
        dateData.startDate,
        dateData.endDate
      );
      showSnackbar(t("product_platform.enableSuccessfully"), "success");
    }
  } catch (error: any) {
    showSnackbar(error.errorMsg, "error");
  }
  isOpenEnablePopup.value = false;
};

const handleShowHistory = () => {
  updateShowHistory(true);
  setValidCode({ id: props.item.id });
};
</script>
<style lang="scss" scoped>
.action-buttons {
  display: flex;
  border-radius: 6px;
  border: 1px solid #dce0e5;
  box-shadow: 0px 4px 8px 0px #00000014;
  position: absolute;
  top: -16px;
  right: 16px;
  background: #fff;
  > button {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 30px;
    height: 30px;
    border-right: 1px solid #dce0e5;
    &:first-child {
      border-radius: 6px 0 0 6px;
    }
    &:last-child {
      border: none;
      border-radius: 0 6px 6px 0;
    }
    &:hover {
      background: #f7f8fa;
    }
  }
}
</style>
