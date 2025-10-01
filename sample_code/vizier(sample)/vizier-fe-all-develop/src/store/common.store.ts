import { ITEMS_LIST_VALUE } from "@/constants/";
import {
  getListSubTypeCommonApi,
  getListOptionsByCodeApi,
} from "@/api/prod/commonApi";
import { SubTypeParamsI, ParamsGetListOptions } from "@/interfaces/prod";

const useCommonStore = defineStore(
  "commonStore",
  () => {
    const data = ref([] as any[]);
    const error = ref(null as any);
    const offerTypes = ref([] as any[]);
    const componentTypes = ref([] as any[]);
    const resourceTypes = ref([] as any[]);
    const getListSubType = async (params?: SubTypeParamsI) => {
      try {
        const res = await getListSubTypeCommonApi(params);
        data.value = res.data;
        resourceTypes.value = res.data.data.filter((el: any) => {
          return el.itemType === ITEMS_LIST_VALUE.RESOURCE;
        });
        offerTypes.value = res.data.data.filter((el: any) => {
          return el.itemType === ITEMS_LIST_VALUE.OFFER;
        });
        componentTypes.value = res.data.data.filter((el: any) => {
          return (
            el.itemType !== ITEMS_LIST_VALUE.RESOURCE &&
            el.itemType !== ITEMS_LIST_VALUE.OFFER
          );
        });
      } catch (err: any) {
        throw new Error(err);
      }
    };
    const getListOptionsByCode = async (params: ParamsGetListOptions) => {
      try {
        const res = await getListOptionsByCodeApi(params);
        return res;
      } catch (err: any) {
        throw new Error(err);
      }
    };

    return {
      data,
      error,
      getListSubType,
      resourceTypes,
      offerTypes,
      componentTypes,
      getListOptionsByCode,
    };
  },
  {
    persist: {
      storage: sessionStorage,
      paths: ["listItem", "offerTypes", "componentTypes", "resourceTypes"],
    },
  }
);

export default useCommonStore;
