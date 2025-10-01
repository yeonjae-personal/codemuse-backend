<template>
  <div class="detail-grid-container">
    <div class="detail-section-title">
      {{
        category === TITLE_DETAILS.GENERAL
          ? $t("product_platform.general")
          : $t("product_platform.additional")
      }}
    </div>
    <div
      class="detail-section-attribute-list"
      :class="{
        'resource-detail':
          type === LARGE_ITEM_CODE.RESOURCE ||
          type === LARGE_ITEM_CODE.RELATION,
      }"
    >
      <template v-for="(item, index) in listValue" :key="index">
        <div
          v-if="category === TITLE_DETAILS.GENERAL"
          class="detail-section-attribute-item"
        >
          <p class="attribute-label">
            <CustomTooltip
              :content="$t(`${item.labelId}Desc`)"
              :is-always-show="!!item.labelDscr"
            >
              {{
                item.labelId?.includes("LB") &&
                !item.key &&
                item.hasOwnProperty("attrVal")
                  ? $t(item.labelId)
                  : item?.key?.includes("product_platform.") ||
                      item?.labelId?.includes("product_platform.")
                    ? t(item?.key || item?.labelId)
                    : $t(`product_platform.${item.labelId || item.key}`)
              }}
            </CustomTooltip>
          </p>
          <p class="attribute-value">
            <CustomTooltip
              :content="
                getDisplayGeneralValue(
                  item,
                  type,
                  itemCodeListCp,
                  COMPONENTS_LAGRE_TYPE_COMP,
                  codeList
                )
              "
              location="bottom"
            />
          </p>
        </div>
        <div
          v-else-if="category === TITLE_DETAILS.ADDITIONAL"
          class="detail-section-attribute-item"
        >
          <p class="attribute-label">
            <CustomTooltip
              :content="$t(`${item.labelId}Desc`)"
              :is-always-show="!!item.labelDscr"
            >
              {{ $t(item.labelId) }}
            </CustomTooltip>
          </p>
          <p class="attribute-value">
            <CustomTooltip
              :content="getDisplayAdditionalValue(item, codeList)"
              location="bottom"
            />
          </p>
        </div>
      </template>
    </div>
  </div>
</template>

<script setup lang="ts">
import { TITLE_DETAILS } from "@/constants/impactAnalysis";
import { useGroupCode } from "@/composables/useGroupCode";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import { LARGE_ITEM_CODE } from "@/store/userPocket.store";
import { COMPONENTS_LAGRE_TYPE } from "@/constants/component";
import { useI18n } from "vue-i18n";

const { getTextDisplay } = useGroupCode();
const props = defineProps({
  category: {
    type: String,
    default: "",
  },
  attributeList: {
    type: Array,
    default: () => [],
  },
  codeList: {
    type: Object,
    default: () => {},
  },
  type: {
    type: String,
    default: "",
  },
  itemCodeList: {
    type: Array,
    default: () => [],
  },
});

const { t } = useI18n();

const COMPONENTS_LAGRE_TYPE_COMP = computed(() => {
  return COMPONENTS_LAGRE_TYPE.map((item) => ({
    ...item,
    cmcdDetlNm: t(item.cmcdDetlNm),
  }));
});

const listValue = computed<any[]>(() => props.attributeList);
const itemCodeListCp = computed(() => props.itemCodeList);

const multiSelectText = (values, code) => {
  const arrayParse = typeof values === "string" ? JSON.parse(values) : values;
  if (!arrayParse?.length) {
    return "-";
  }
  const textList: any[] = [];
  const listGroup = props.codeList[code as string];
  if (listGroup?.length && arrayParse?.length) {
    arrayParse.forEach((item) => {
      textList.push(
        listGroup?.find((group) => group.cmcdDetlId === item).cmcdDetlNm ?? ""
      );
    });
    return textList.join(",");
  } else {
    return "-";
  }
};

const getDisplayGeneralValue = (
  item,
  type,
  itemCodeListCp,
  COMPONENTS_LAGRE_TYPE_COMP,
  codeList
) => {
  if (item?.hasOwnProperty("value")) {
    return item.value;
  }

  if (item?.fieldTypeCode === COLUMN_FIELD_TYPE.DM) {
    return multiSelectText(item?.attrVal, item?.commGroupCode);
  }

  const value = item?.value || item?.attrVal || "";
  const isSpecialCol =
    item?.colName === "item_code" ||
    (item?.colName === "item_large_code" && type === LARGE_ITEM_CODE.COMPONENT);
  const fieldType = isSpecialCol ? COLUMN_FIELD_TYPE.DL : item.fieldTypeCode;

  let thirdParam = [];
  if (item?.colName === "item_code" && itemCodeListCp?.length) {
    thirdParam = itemCodeListCp;
  } else if (item.colName === "item_large_code") {
    thirdParam = COMPONENTS_LAGRE_TYPE_COMP;
  } else if (item?.attrRefTableName) {
    thirdParam = item?.tableColumns ?? [];
  } else if (item?.commGroupCode) {
    thirdParam = codeList[item.commGroupCode] || [];
  }

  return getTextDisplay(value, fieldType, thirdParam);
};

const getDisplayAdditionalValue = (item, codeList) => {
  if (item?.fieldTypeCode !== COLUMN_FIELD_TYPE.DM) {
    const value = item?.attrVal || "";
    const fieldType = item?.fieldTypeCode;
    let thirdParam = [];
    if (item?.attrRefTableName) {
      thirdParam = item?.tableColumns ?? [];
    } else if (item?.commGroupCode) {
      thirdParam = codeList[item.commGroupCode] || [];
    }
    return getTextDisplay(value, fieldType, thirdParam);
  }
  return multiSelectText(item?.attrVal, item?.commGroupCode);
};
</script>

<style scoped>
.detail-grid-container,
.detail-section-attribute-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.detail-section-attribute-item {
  display: flex;
  padding: 8px 12px;
  background-color: #f7f8fa;
  min-height: 37px;
  border-radius: 8px;
}

.attribute-label {
  font-family: "Noto Sans KR";
  font-size: 13px;
  font-weight: 500;
  line-height: 19.5px;
  letter-spacing: 0.25px;
  text-align: left;
  color: #6b6d70;
  width: 45%;
  white-space: wrap;
}

.detail-section-title {
  font-family: Noto Sans KR;
  font-size: 11px;
  font-weight: 500;
  line-height: 16.5px;
  letter-spacing: 0.25px;
  color: #6b6d70;
}

.attribute-value {
  padding-left: 8px;
  font-family: Noto Sans KR;
  font-size: 13px;
  font-weight: 500;
  line-height: 19.5px;
  letter-spacing: 0.25px;
  width: 55%;
  color: #3a3b3d;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
}
</style>
