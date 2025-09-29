import { UI_USERPOCKET_GET_ITEM } from "@/api/prod/path";
import { sortByName } from "@/utils/extend-utils";
import { httpClient } from "@/utils/http-common";
import moment from "moment-timezone";
import useSnackbarStore from "./snackbar.store";
import { useI18n } from "vue-i18n";

interface ChildItem {
  itemId: string;
  offerType?: string;
  name: string;
  code: string;
  itemType?: string;
  middleType?: string;
  draggable?: boolean;
  offerGroupTypeCode?: string;
  validEndDtm?: string;
  validStartDtm?: string;
  type?: string;
  subType?: string;
}
interface IUserPocket {
  code: string;
  name: string;
  items: ChildItem[];
}

export enum LARGE_ITEM_CODE {
  OFFER = "O",
  COMPONENT = "C",
  RESOURCE = "R",
  GROUP = "G",
  RELATION = "B",
}

const userPocketStore = defineStore("userPocketStore", () => {
  const userPocketData = ref<IUserPocket[]>([]);
  const { showSnackbar } = useSnackbarStore();
  const { t } = useI18n();
  const getUserPocketData = async () => {
    try {
      const response = await httpClient.get(UI_USERPOCKET_GET_ITEM);
      const data = response.data.map((item) => ({
        code: item.largeItemCode,
        name: item.largeItemName,
        items: item.datas
          .map((data) => ({
            itemId: data.uuid,
            itemType: item.largeItemCode,
            offerType: data.type,
            name: data.name,
            code: data.code,
            middleType: data.middleType,
            type: data.type,
            subType: data.subType,
            offerGroupTypeCode: data.offerGroupTypeCode,
            validEndDtm: data.itemValidEnd,
            validStartDtm: data.itemValidStart,
            draggable: data.itemValidEnd
              ? moment(data.itemValidEnd).isSameOrAfter(moment())
              : true,
          }))
          .sort(sortByName),
      }));
      userPocketData.value = data;
    } catch {}
  };

  const countUserPocketItems = computed(() => {
    const result: Array<unknown> = [];
    userPocketData.value.forEach((item) => {
      result.push(item.items);
    });
    return result.flat().length;
  });

  const countUserPocketGroup = computed(() => userPocketData.value.length);

  const maxItemInUserPocket = computed(() => {
    switch (countUserPocketGroup.value) {
      case 1:
        return 12;
      case 2:
        return 11;
      case 3:
        return 10;
      case 4:
        return 9;
      case 5:
        return 8;
      default:
        return 10;
    }
  });

  const checkIsExistUserPocket = (data) => {
    let uuid = "";
    switch (data.userPocketType) {
      case LARGE_ITEM_CODE.OFFER:
        uuid =
          data.uuid ||
          data.objUUID ||
          data.prodUuid ||
          data.objUuid ||
          data.offrUuid ||
          data.baseUuid ||
          data.targetUuid;
        break;
      case LARGE_ITEM_CODE.COMPONENT:
        if (data.baseUuid) {
          if (data.baseItemCode === "O" && data.trgtItemCode === "C") {
            uuid = data.trgtUuid;
          } else {
            uuid =
              data.uuid ||
              data.objUUID ||
              data.prodUuid ||
              data.objUuid ||
              data.baseUuid;
          }
        } else {
          uuid =
            data.uuid ||
            data.objUUID ||
            data.prodUuid ||
            data.objUuid ||
            data.trgtUuid;
        }
        break;
      case LARGE_ITEM_CODE.RESOURCE:
        uuid =
          data.uuid ||
          data.objUUID ||
          data.prodUuid ||
          data.objUuid ||
          data.trgtUuid;
        break;
      case LARGE_ITEM_CODE.GROUP:
        uuid = data.objUuid || data.targetUuid || data.offerGroupUuid;
        break;
      case LARGE_ITEM_CODE.RELATION:
        uuid = data.dpdcRelUuid || data.objUuid;
        break;
      default:
        break;
    }
    for (const item of userPocketData.value) {
      const currentItem = item.items.find(
        (childItem) => childItem.itemId === uuid
      );
      if (currentItem) return true;
    }
    return false;
  };

  const addUserPocketItem = async (data: any) => {
    try {
      let uuid = "";
      let category = userPocketData.value.find(
        (item) => item.code === data.userPocketType
      );
      if (!category) {
        const categoryName = Object.keys(LARGE_ITEM_CODE).find(
          (item) =>
            item.charAt(0).toLocaleLowerCase() ===
            data.userPocketType.toLocaleLowerCase()
        );
        userPocketData.value.push({
          code: data.userPocketType,
          name: categoryName || "Other",
          items: [],
        });
        category = userPocketData.value[userPocketData.value.length - 1];
      }

      if (category) {
        switch (data.userPocketType) {
          case LARGE_ITEM_CODE.OFFER:
            category.items.push({
              itemId:
                data.objUUID ||
                data.prodUuid ||
                data.objUuid ||
                data.offrUuid ||
                data.baseUuid ||
                data.targetUuid,
              offerType: data.itemCode || "",
              name:
                data.objName ||
                data.prodNm ||
                data.prodItemNm ||
                data.offrNm ||
                data.baseProdItemNm ||
                data.targetName,
              code:
                data.objCode ||
                data.prodCd ||
                data.prodItemCd ||
                data.offrCd ||
                data.baseProdItemCd ||
                data.targetCode,
              itemType: data.userPocketType,
            });
            uuid =
              data.objUUID ||
              data.prodUuid ||
              data.objUuid ||
              data.offrUuid ||
              data.baseUuid ||
              data.targetUuid;
            break;
          case LARGE_ITEM_CODE.COMPONENT:
            if (data.baseUuid) {
              if (data.baseItemCode === "O" && data.trgtItemCode === "C") {
                category?.items.push({
                  itemId: data.trgtUuid,
                  middleType: data.itemType || "",
                  name: data.trgtProdItemNm,
                  code: data.trgtProdItemCd,
                  itemType: data.userPocketType,
                });
                uuid = data.trgtUuid;
              } else {
                category?.items.push({
                  itemId:
                    data.uuid ||
                    data.objUUID ||
                    data.prodUuid ||
                    data.objUuid ||
                    data.baseUuid,
                  middleType: data.itemType || "",
                  name:
                    data.name ||
                    data.objName ||
                    data.prodNm ||
                    data.prodItemNm ||
                    data.baseProdItemNm,
                  code:
                    data.code ||
                    data.objCode ||
                    data.prodCd ||
                    data.prodItemCd ||
                    data.baseProdItemCd,
                  itemType: data.userPocketType,
                });
                uuid =
                  data.uuid ||
                  data.objUUID ||
                  data.prodUuid ||
                  data.objUuid ||
                  data.baseUuid;
              }
            } else {
              category?.items.push({
                itemId:
                  data.uuid ||
                  data.objUUID ||
                  data.prodUuid ||
                  data.objUuid ||
                  data.trgtUuid,
                middleType: data.itemType || "",
                name:
                  data.name ||
                  data.objName ||
                  data.prodNm ||
                  data.prodItemNm ||
                  data.trgtProdItemNm,
                code:
                  data.code ||
                  data.objCode ||
                  data.prodCd ||
                  data.prodItemCd ||
                  data.trgtProdItemCd,
                itemType: data.userPocketType,
              });
              uuid =
                data.uuid ||
                data.objUUID ||
                data.prodUuid ||
                data.objUuid ||
                data.trgtUuid;
            }
            break;
          case LARGE_ITEM_CODE.GROUP:
            category?.items.push({
              itemId: data.objUuid || data.targetUuid || data.offerGroupUuid,
              offerType: data.itemCode || "",
              name: data.objName || data.targetName || data.offerGroupName,
              code: data.objCode || data.targetCode || data.offerGroupCode,
              itemType: data.userPocketType,
            });
            uuid = data.objUuid || data.targetUuid || data.offerGroupUuid;
            break;
          case LARGE_ITEM_CODE.RESOURCE:
            category?.items.push({
              itemId:
                data.uuid ||
                data.objUUID ||
                data.prodUuid ||
                data.objUuid ||
                data.trgtUuid,
              offerType: data.itemCode || "",
              name:
                data.name ||
                data.objName ||
                data.prodNm ||
                data.prodItemNm ||
                data.trgtProdItemNm,
              code:
                data.code ||
                data.objCode ||
                data.prodCd ||
                data.prodItemCd ||
                data.trgtProdItemCd,
              itemType: data.userPocketType,
            });
            uuid =
              data.uuid ||
              data.objUUID ||
              data.prodUuid ||
              data.objUuid ||
              data.trgtUuid;
            break;
          case LARGE_ITEM_CODE.RELATION:
            category?.items.push({
              itemId: data.dpdcRelUuid || data.objUuid,
              offerType: data.itemCode || "",
              name: data.dpdcRelName || data.objName,
              code: data.dpdcRelCode || data.objCode,
              itemType: data.userPocketType,
            });
            uuid = data.dpdcRelUuid || data.objUuid;
            break;
          default:
            break;
        }
        category.items.sort(sortByName);
        await httpClient
          .post(UI_USERPOCKET_GET_ITEM, { uuid })
          .then(() => {
            getUserPocketData();
          })
          .catch((error) => {
            showSnackbar(t(error.errorMsg), "error");
          });
      }
    } catch (error) {
      throw error;
    }
  };

  const removeUserPocketItem = async (data) => {
    try {
      await httpClient
        .delete(UI_USERPOCKET_GET_ITEM, {
          params: { uuid: data.itemId },
        })
        .then(() => {
          userPocketData.value = userPocketData.value
            .map((item) => {
              if (item.code === data.itemType) {
                item.items = item.items.filter((i) => i.itemId !== data.itemId);
              }
              return item;
            })
            .filter((item) => item.items.length !== 0);
          getUserPocketData();
        });
    } catch {}
  };

  return {
    userPocketData,
    getUserPocketData,
    addUserPocketItem,
    removeUserPocketItem,
    checkIsExistUserPocket,
    countUserPocketItems,
    countUserPocketGroup,
    maxItemInUserPocket,
  };
});
export default userPocketStore;
