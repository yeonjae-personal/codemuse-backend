<template>
  <div v-if="isExistData || isEdit">
    <template
      v-for="(item, index) in multiEntityList"
      :key="item.entityTypeCode"
    >
      <div class="mt-[12px] pb-[8px] w-full px-[6px]">
        <span class="text-[15px] text-text-base font-medium">
          {{ findTitle(item.entityTypeName, item.entityScope) }}
        </span>
        <ItemDrop
          v-if="isEdit"
          class-name="mt-3"
          :is-disabled="item.entityTypeCode != dragOfferType && isDragging"
          @drop="drop($event, index)"
          @dragover="allowDrop($event, item.entityTypeCode)"
          @click="openMultiSearch(item)"
        />
        <template v-if="item.objRel?.length">
          <div
            v-for="(entity, entityIndex) in item.objRel"
            :key="entity.entityCode"
            class="mt-[12px]"
          >
            <cf-card-dropdown
              class-name="default entity-icon"
              :item="entity"
              :title="entity.entityName"
              :description="entity.entityCode"
              type-bg="linear"
              border-color-action="dark-blue"
              display-border-left="dark-blue"
              :active="entity.entityCode === selectedEntity?.entityCode"
              :node="{
                hideNodeLeft: true,
                isActiveNodeLeft: false,
                hideNodeRight: true,
                isActiveNodeRight: false,
              }"
              :disable="isExpiredTime(entity.itemValidEndDtm)"
              :expired="isExpiredTime(entity.validEndDtm)"
              show-icon-status
              :is-new="entity.isAdd && !isViewMode"
              :editable="isEdit"
              :actions="listActions(entity, index, entityIndex)"
              :expand="entity?.expand"
              @on-click-card="
                (event) => onChooseCard(event?.isShowDetail, entity)
              "
              @on-click-show-detail="onChooseCard($event, entity)"
            >
              <template #icon>
                <span
                  class="flex justify-center align-center w-[32px] h-[32px]"
                >
                  <multi-entity-icon></multi-entity-icon>
                </span>
              </template>
              <template #detail>
                <div class="flex flex-column gap-4">
                  <ProductGrid :data="entity.detail" />
                </div>
              </template>
            </cf-card-dropdown>
          </div>
        </template>
        <template v-else>
          <div
            v-if="!isEdit"
            class="empty-card w-full mt-[12px] bg-[#F7F8FA] h-[64px] border-2-[#E6E9ED] font-weight-medium text-[#6B6D70]"
          >
            No data display
          </div>
        </template>
      </div>
    </template>
  </div>
  <NoData v-else />
  <DateTimePopup
    v-model:open-model="isOpenExpiredPopup"
    v-model="dateData"
    :modal-title="
      checkExpired(selectedInPopoverItem)
        ? $t('product_platform.enableMultiEntity')
        : $t('product_platform.expireMultiEntity')
    "
    :min-end-date="
      moment().format(DATE_FORMAT.DATE_FORMAT_WITHOUT_TIME_REVERSE)
    "
    :disabled-start-date="!checkExpired(selectedInPopoverItem)"
    :text-btn-cancel="$t('product_platform.cancel')"
    :text-btn-add="$t('product_platform.save')"
    :required-start-date="true"
    :required-end-date="!checkExpired(selectedInPopoverItem)"
    @submit="handleSubmitExpirePopup"
    @close="handleCloseExpirePopup"
  />
  <DateTimePopup
    v-model:open-model="isOpenAddItemPopup"
    v-model="dateValid"
    :min-end-date="
      moment().format(DATE_FORMAT.DATE_FORMAT_WITHOUT_TIME_REVERSE)
    "
    :modal-title="$t('product_platform.addEntity')"
    :text-btn-cancel="$t('product_platform.cancel')"
    :text-btn-add="$t('product_platform.save')"
    :required-start-date="true"
    @submit="handleSubmitAddItem"
    @close="handleCloseAddItem"
  />
</template>
<script setup lang="ts">
import cloneDeep from "lodash-es/cloneDeep";
import { useI18n } from "vue-i18n";
import moment from "moment-timezone";
import { getMultiEntityDetail } from "@/api/prod/extendsApi";
import { useGroupCode } from "@/composables/useGroupCode";
import {
  MULTI_ENTITY_APPLY_CATEGORY,
  MULTI_ENTITY_DETAIL_DATA_GRID,
  MULTI_ENTITY_DETAIL_USER_TITLE_GRID,
  MULTI_ENTITY_SCOPE,
  MULTI_ENTITY_SUBTYPE,
} from "@/constants/multiEntity";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import { useDragStore, useSnackbarStore } from "@/store";
import useRedirect from "@/composables/useRedirect";
import { formatDate, isExpiredTime } from "@/utils/format-data";
import { ACTION_TYPE_NAME, DATE_FORMAT } from "@/constants/index";
import TrashIcon from "@/components/prod/icons/TrashIcon.vue";
import CheckVerified from "@/components/prod/icons/CheckVerified.vue";
import HourglassIcon from "@/components/prod/icons/HourglassIcon.vue";
import OpenInNewIcon from "@/components/prod/icons/OpenInNewIcon.vue";
import type { ActionType } from "@/interfaces/prod";
import { MultiEntityChildItem, MultiEntityTabItem } from "@/types/common";

const emit = defineEmits(["update:modelList", "onClickDrogArae"]);
type Props = {
  isEdit?: boolean;
  isViewMode?: boolean;
  category?: string;
  multiEntityTypes: Array<any>;
  modelList: MultiEntityTabItem[];
  selectedItem?: MultiEntityChildItem;
  relationItem?: any;
  typeSelected?: any;
};

const props = withDefaults(defineProps<Props>(), {
  isEdit: false,
  isViewMode: false,
  category: MULTI_ENTITY_APPLY_CATEGORY.EDIT,
  multiEntityTypes: () => [],
  modelList: () => [],
  selectedItem: undefined,
  relationItem: undefined,
  typeSelected: undefined,
});

const { t } = useI18n();
const { search } = useGroupCode();
const { dragOfferType, isDragging } = storeToRefs(useDragStore());
const useSnackbar = useSnackbarStore();
const { moveMultiEntitySearchPage } = useRedirect();

const selectedEntity = ref();
const uniqueCodeList = ref();
const selectedInPopoverItem = ref();
const isOpenAddItemPopup = ref(false);
const isOpenExpiredPopup = ref(false);
const dropData = ref();
const addPosition = ref();
const expiredPosition = ref();
const actionTypeOnItem = ref(ACTION_TYPE_NAME.EXPIRED);
const dateValid = reactive({
  startDate: "",
  endDate: "",
});
const dateData = reactive({
  startDate: "",
  endDate: "",
});
const multiEntityList = computed({
  get() {
    return props.modelList;
  },
  set(newValue) {
    emit("update:modelList", newValue);
  },
});
const isExistData = computed(() => {
  let status = false;
  props.modelList?.forEach((item) => {
    if (item?.objRel?.length) {
      status = true;
    }
  });
  return status;
});

const checkExpired = (item) => {
  if (!item?.validEndDtm) {
    return false;
  }
  if (
    moment(item?.validEndDtm, DATE_FORMAT.DATE_TYPE).isSame(new Date(), "day")
  ) {
    return true;
  }
  return (
    moment(item?.validEndDtm, DATE_FORMAT.DATE_TYPE).diff(moment(), "days") < 0
  );
};

const listActions = (
  item: MultiEntityChildItem,
  index: number,
  entityIndex: number
): ActionType[] => {
  const isItemExpired = isExpiredTime(item?.itemValidEndDtm);
  if (isItemExpired) return [];
  const isRelationExpired = checkExpired(item);
  const openNewTabAction = {
    name: t("product_platform.openinNewWindow"),
    icon: OpenInNewIcon,
    onClick: () => {
      moveMultiEntitySearchPage({
        entityTypeCode: item.entityTypeCode,
        itemCode: props.typeSelected,
        multiEntityCode: item.entityCode,
      });
    },
  };
  if (item.isAdd || props.category == MULTI_ENTITY_APPLY_CATEGORY.DUPPLICATE) {
    return [
      {
        name: t("product_platform.actionRemove"),
        icon: TrashIcon,
        onClick: () => {
          multiEntityList.value[index as number].objRel.splice(entityIndex, 1);
        },
      },
      openNewTabAction,
    ];
  }
  const action = {
    name: isRelationExpired
      ? t("product_platform.actionEnable")
      : t("product_platform.actionExpire"),
    icon: isRelationExpired ? CheckVerified : HourglassIcon,
    onClick: () => {
      let isDupplicateEnableItem = false;
      actionTypeOnItem.value = isRelationExpired
        ? ACTION_TYPE_NAME.ENABLED
        : ACTION_TYPE_NAME.EXPIRED;
      dateData.startDate = item.validStartDtm;
      selectedInPopoverItem.value = item;
      if (
        multiEntityList.value[index as number].entityScope ===
          MULTI_ENTITY_SCOPE.SINGLE &&
        multiEntityList.value[index as number].objRel?.length &&
        actionTypeOnItem.value === ACTION_TYPE_NAME.ENABLED
      ) {
        multiEntityList.value[index as number].objRel.forEach((item) => {
          if (!checkExpired(item)) {
            useSnackbar.showSnackbar(
              t(
                "product_platform.multiEntityDetailData.invalidEntitySingleMsg"
              ),
              "error"
            );
            isDupplicateEnableItem = true;
          }
        });
      }
      if (
        !isDupplicateEnableItem ||
        actionTypeOnItem.value !== ACTION_TYPE_NAME.ENABLED
      ) {
        isOpenExpiredPopup.value = true;
        addPosition.value = index;
        expiredPosition.value = entityIndex;
        nextTick(() => {
          dateData.endDate = item.validEndDtm;
        })
      }
    },
  };
  return [action, openNewTabAction];
};

const onChooseCard = async (event, item) => {
  item["expand"] = event;
  selectedEntity.value = item;
  if (event && !item?.detail) {
    await getEntityDetailInfo(item);
  }
};

const getEntityDetailInfo = async (item) => {
  const params = {
    entityCode: item.entityCode,
    entityTypeCode: item.entityTypeCode,
  };
  const { data } = await getMultiEntityDetail(params);
  const selectedEntityDetails = data;
  if (selectedEntityDetails) {
    let generalData;
    if (
      [
        MULTI_ENTITY_SUBTYPE.BUSINESS_LINE,
        MULTI_ENTITY_SUBTYPE.DISCOUNT_TARGET,
        MULTI_ENTITY_SUBTYPE.SALE_COMPANY,
      ].includes(selectedEntity.value.entityTypeCode)
    ) {
      generalData = cloneDeep(
        MULTI_ENTITY_DETAIL_DATA_GRID[selectedEntity.value.entityTypeCode]
      );
    } else {
      generalData = cloneDeep(MULTI_ENTITY_DETAIL_USER_TITLE_GRID);
    }
    generalData.forEach((item) => {
      if (selectedEntityDetails[item.labelId]) {
        item.attrVal = selectedEntityDetails[item.labelId];
        if (item.labelId == "itemCode" || item.labelId == "entityTypeCode") {
          item.attrVal = findType(item.attrVal);
        }
      }
    });
    const detail = {
      general: generalData,
      additional: selectedEntityDetails.additional,
    };
    const generalGroupCodes = detail.general
      .filter((item) => item.fieldTypeCode === COLUMN_FIELD_TYPE.DL)
      .map((item) => item.groupCode);
    const additionalGroupCodes = detail.additional
      .filter((item) => item.fieldTypeCode === COLUMN_FIELD_TYPE.DL)
      .map((item) => item.commGroupCode);
    uniqueCodeList.value = [
      ...new Set(generalGroupCodes.concat(additionalGroupCodes)),
    ];

    item["detail"] = detail;
  }
};

const findTitle = (title, scope) => {
  let after;
  switch (scope) {
    case MULTI_ENTITY_SCOPE.SINGLE:
      after = ` (${t("product_platform.single")})`;
      break;
    case MULTI_ENTITY_SCOPE.MULTIPLE:
      after = ` (${t("product_platform.multiple")})`;
      break;
  }
  return title + after;
};

const allowDrop = (event: DragEvent, itemType: string) => {
  if (itemType !== dragOfferType.value) {
    return true;
  }
  event.preventDefault();
};

const drop = (event: DragEvent, index: number) => {
  event.preventDefault();
  dropData.value = event.dataTransfer?.getData("item")
    ? JSON.parse(event.dataTransfer.getData("item"))
    : null;
  if (dropData.value) {
    let isDupplicate = false;
    if (
      multiEntityList.value[index as number].entityScope ===
        MULTI_ENTITY_SCOPE.SINGLE &&
      multiEntityList.value[index as number].objRel?.length
    ) {
      multiEntityList.value[index as number].objRel.forEach((item) => {
        if (!checkExpired(item)) {
          useSnackbar.showSnackbar(
            t("product_platform.multiEntityDetailData.invalidEntitySingleMsg"),
            "error"
          );
          isDupplicate = true;
        }
      });
    }
    multiEntityList.value[index as number].objRel?.forEach((item) => {
      if (item.entityCode === dropData.value.entityCode) {
        useSnackbar.showSnackbar(
          t("product_platform.multiEntityDuplicate"),
          "error"
        );
        isDupplicate = true;
      }
    });
    if (
      !isDupplicate &&
      multiEntityList.value[index as number].entityScope ===
        dropData.value.entityScope
    ) {
      addPosition.value = index;
      isOpenAddItemPopup.value = true;
    }
  }
};

const openMultiSearch = (item) => {
  emit("onClickDrogArae", item);
};

const handleSubmitAddItem = async () => {
  if (!dateValid.startDate) {
    useSnackbar.showSnackbar(
      t("product_platform.required_start_date"),
      "error"
    );
    return;
  }
  multiEntityList.value[addPosition.value].objRel.unshift({
    ...dropData.value,
    validStartDtm: dateValid.startDate,
    validEndDtm: dateValid.endDate,
    isAdd: true,
  });
  multiEntityList.value.forEach((item) => {
    item.objRel =
      item.objRel?.map((entity) => ({ ...entity, expand: false })) || [];
  });
  handleCloseAddItem();
};

const handleCloseAddItem = () => {
  isOpenAddItemPopup.value = false;
  dateValid.endDate = "";
  dateValid.startDate = "";
};

const handleSubmitExpirePopup = () => {
  if (
    actionTypeOnItem.value === ACTION_TYPE_NAME.EXPIRED &&
    !dateData.endDate
  ) {
    useSnackbar.showSnackbar(t("product_platform.required_end_date"), "error");
    return;
  }
  // if enable entity require start date
  if (
    actionTypeOnItem.value === ACTION_TYPE_NAME.ENABLED &&
    !dateData.startDate
  ) {
    useSnackbar.showSnackbar(
      t("product_platform.required_start_date"),
      "error"
    );
    return;
  }
  const formatStartDate = formatDate(
    dateData.startDate,
    DATE_FORMAT.DATE_TYPE,
    DATE_FORMAT.DATE_TYPE
  );
  const formatEndDate = formatDate(
    dateData.endDate,
    DATE_FORMAT.DATE_TYPE,
    DATE_FORMAT.DATE_TYPE
  );
  multiEntityList.value[addPosition.value].objRel[expiredPosition.value] =
    cloneDeep({
      ...selectedInPopoverItem.value,
      validStartDtm: formatStartDate,
      validEndDtm: formatEndDate,
      isUpdate: true,
    });
  handleCloseExpirePopup();
};

const handleCloseExpirePopup = () => {
  isOpenExpiredPopup.value = false;
};

const findType = (type) => {
  let typeName;
  props.multiEntityTypes.forEach((item) => {
    if (item.value === type) {
      typeName = item.label;
      return typeName;
    } else {
      if (item.subOptions.length > 0) {
        item.subOptions.find((subItem) => {
          if (subItem.value === type) {
            typeName = subItem.label;
            return typeName;
          }
        });
      } else {
        return "-";
      }
    }
  });
  return typeName;
};

watch(
  () => uniqueCodeList.value,
  (newVal) => {
    if (newVal.length) {
      search(newVal);
    }
  },
  { deep: true }
);

watch(
  () => props.isEdit,
  (value) => {
    if (value) {
      multiEntityList.value.forEach((item) => {
        item.objRel = item.objRel.map((relItem) => ({
          ...relItem,
          expand: false,
        }));
      });
    }
  }
);
</script>
<style scoped lang="scss">
.empty-card {
  display: flex;
  justify-content: center;
  align-items: center;
  background: #f7f8fa;
  height: 64px;
  border: 1px solid #e6e9ed;
  border-radius: 8px;
  color: #6b6d70;
  font-weight: 500;
  font-size: 11px;
}
</style>
