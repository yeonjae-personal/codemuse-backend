<template>
  <div v-if="data" class="detail-content">
    <DetailGrid
      v-if="general?.length > 0"
      :category="TITLE_DETAILS.GENERAL"
      :attribute-list="general"
      :type="type"
      :code-list="groupCodeData"
      :item-code-list="itemCodeList"
    />
    <DetailGrid
      v-if="additional?.length > 0"
      :category="TITLE_DETAILS.ADDITIONAL"
      :attribute-list="additional"
      :type="type"
      :code-list="groupCodeData"
      :item-code-list="itemCodeList"
    />
  </div>
  <NoData v-else />
</template>

<script setup lang="ts">
import cloneDeep from "lodash-es/cloneDeep";
import union from "lodash-es/union";
import { getListItemCodeApi } from "@/api/prod/commonApi";
import { useGroupCode } from "@/composables/useGroupCode";
import { TITLE_DETAILS } from "@/constants/impactAnalysis";
import { DETAIL_TAB_TYPE } from "@/constants/index";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import { LARGE_ITEM_CODE } from "@/store/userPocket.store";

const { search, groupCodeData } = useGroupCode();
const uniqueCodeList = ref<any[]>([]);
const localGroupCodeData = ref<any>({});
const itemCodeList = ref<any[]>([]);

const props = defineProps({
  data: {
    type: Object,
    default: () => {},
  },
  type: {
    type: String,
    default: "",
  },
});

const general = computed(() => {
  const generalItems = props.data?.general ?? [];
  const additionalItems = props.data?.additional ?? [];
  if (!generalItems.length) return [];
  const sortedFilteredGeneral = generalItems
    .sort((cur, next) => cur.sortNo - next.sortNo)
    .filter(
      (item) =>
        item.fieldTypeCode !== COLUMN_FIELD_TYPE.HD ||
        (["item_large_code", "item_code"].includes(item.colName) &&
          props.type === LARGE_ITEM_CODE.COMPONENT)
    );

  const filteredAdditional = additionalItems.filter(
    (item) =>
      item.dispTab === DETAIL_TAB_TYPE.GENERAL &&
      item.fieldTypeCode !== COLUMN_FIELD_TYPE.HD
  );

  return [...sortedFilteredGeneral, ...filteredAdditional];
});

const additional = computed(() => {
  if (props.data?.additional?.length) {
    return props.data?.additional
      .filter((item) => item.dispTab !== DETAIL_TAB_TYPE.GENERAL)
      .map((item: any) => ({
        ...item,
        attrVal:
          item.fieldTypeCode === COLUMN_FIELD_TYPE.DM
            ? JSON.parse(item?.attrVal)?.filter((value: any) => value.trim()) ||
              []
            : item.attrVal,
      }));
  }
  return [];
});

watch(
  () => [general.value, additional.value],
  ([general, additional]) => {
    const extractCodes = (list: any[] = []) =>
      list
        .filter((item) =>
          [COLUMN_FIELD_TYPE.DL, COLUMN_FIELD_TYPE.DM].includes(
            item.fieldTypeCode
          )
        )
        .map((item) => item.commGroupCode);

    uniqueCodeList.value = union(
      extractCodes(general),
      extractCodes(additional)
    ).filter(Boolean);
  },
  { immediate: true, deep: true }
);

watch(
  () => uniqueCodeList.value,
  async (val) => {
    if (val?.length) {
      await search(val);
      localGroupCodeData.value = cloneDeep(groupCodeData.value);
    }
  },
  { deep: true }
);

onMounted(async () => {
  if (props.type) {
    const { data } = await getListItemCodeApi({
      lItemCode: props.type,
    } as any);
    if (data) {
      itemCodeList.value = data;
    }
  }
  if (uniqueCodeList.value?.length) {
    await search(uniqueCodeList.value);
    localGroupCodeData.value = cloneDeep(groupCodeData.value);
  }
});
</script>

<style>
.detail-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}
</style>
