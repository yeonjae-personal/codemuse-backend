import {
  getGroup,
  getGroupDetail,
  putOfferGroupDuplicate,
} from "@/api/prod/extendsApi";
import { getUserInfor } from "@/constants/userInfor";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import { DETAIL_TAB_TYPE } from "@/constants/";

const useOfferDuplicateProcessStore = defineStore(
  "offerDuplicateProcessStore",
  {
    state: () => ({
      offerDuplicateInGroupMode: false,
      offerDuplicateInRelationMode: false,
      offerBeClonedUuid: "" as string,
      offerBeClonedCode: "" as string,
      offerDuplicated: null as any,
      selectedGroup: null as any,
      groupsOffer: [] as any[],
      groupsFinish: [] as any[],
      groupDetailData: {
        generalTab: [] as any[],
        additionalTab: [] as any[],
        offerTab: [] as any[],
      },
    }),
    actions: {
      resetProcess() {
        this.offerDuplicateInGroupMode = false;
        this.offerDuplicateInRelationMode = false;
        this.offerBeClonedUuid = "";
        this.offerDuplicated = null;
        this.selectedGroup = null;
        this.groupsFinish = [];
        this.groupsOffer = [];
      },

      async getListGroupByOffer(params) {
        return await getGroup(params);
      },

      async finishOfferGroupDuplicate(params) {
        try {
          return await putOfferGroupDuplicate(params);
        } catch (error) {
          throw error;
        }
      },

      async getGroupDetailInfo(isAdd = false) {
        const { data } = await getGroupDetail(this.selectedGroup.objUuid);
        if (data) {
          const userInfor = getUserInfor();
          data.general = data.general
            .sort((after, before) => after.sortNo - before.sortNo)
            .map((item) => {
              if (item.colName === "chg_dept_name" && isAdd) {
                return {
                  ...item,
                  attrVal: userInfor.chgDeptName,
                };
              }
              if (item.colName === "chg_user" && isAdd) {
                return {
                  ...item,
                  attrVal: userInfor.chgUser,
                };
              }
              if (item.fieldTypeCode === COLUMN_FIELD_TYPE.DP) {
                return {
                  ...item,
                  attrVal: item.attrVal,
                };
              }
              return item;
            });
          this.groupDetailData.generalTab = [
            ...data.general,
            ...data?.additional.filter(
              (item) => item.dispTab === DETAIL_TAB_TYPE.GENERAL
            ),
          ];
          this.groupDetailData.additionalTab = data?.additional
            .filter((item) => item.dispTab !== DETAIL_TAB_TYPE.GENERAL)
            .map((item: any) => ({
              ...item,
              attrVal:
                item.fieldTypeCode === COLUMN_FIELD_TYPE.DM
                  ? JSON.parse(item?.attrVal)?.filter((value: any) =>
                      value.trim()
                    ) || []
                  : item.attrVal,
            }));
          // Filter expired date (validEndDtm must > current date)

          if (
            this.groupsFinish.length &&
            this.groupsFinish?.some(
              (item) => item.objUuid === this.selectedGroup?.objUuid
            ) &&
            data.childOffr?.some(
              (xxx) => xxx.offrUuid === this.offerDuplicated?.objUuid
            )
          ) {
            this.groupDetailData.offerTab = data.childOffr;
          } else {
            const itemSame = data.childOffr.find(
              (item) => item.offrUuid === this.offerBeClonedUuid
            );
            this.groupDetailData.offerTab = [
              {
                offrUuid: this.offerDuplicated?.objUuid,
                offrCd: this.offerDuplicated?.objCode,
                offrNm: this.offerDuplicated?.objName,
                offrType: this.offerDuplicated?.itemCode,
                workTypeCode: "01",
                validStartDtm: itemSame?.validStartDtm,
                validEndDtm: itemSame?.validEndDtm,
                itemNew: true,
              },
              ...data.childOffr,
            ];
          }
        }
      },
    },
  }
);

export default useOfferDuplicateProcessStore;
