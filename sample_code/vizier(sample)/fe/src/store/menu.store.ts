import { configPath, setPathList } from "@/utils/config-path";
import { MenuItem } from "@/interfaces/prod/menu";
import useLoadingStore from "./loading.store";
import {
  useUser,
  useProductsStore,
  useStructureStore,
  useComponentStore,
  useResourceStore,
  usePricePlanStore,
  useAddOnStore,
  useDiscountStore,
  useCategoryStore,
  useImpactAnalysisStore,
  useExtendManagerStore,
  useExtendSearchStore,
  useExtendCreateStore,
  useSysMessageStore,
  useDomainStore,
  useCmCodeStore,
  useTerminologyStore,
  useScreenStore,
  useUrlStore,
  useMultiEntitySearchStore,
  useMultiEntityCreateStore,
  usePermissionStore,
  usePermissionGroupStore,
  useCreateStructureStore,
  useProductsCreateStore,
  useOfferDuplicateProcessStore,
  useOfferCreateProcessStore,
  useLabelStore,
  useFactorStore,
  useMatrixSearchPaneStore,
  useRuleEngineStore,
  useRelationSearchStore,
  useRelationCreateStore,
  useProductsDuplicateStore,
  useDuplicateStructureStore,
  useRelationManagerDuplicateStore,
  usePublishManagerStore,
} from "@/store";

import DashboardIcon from "@/components/prod/icons/DashboardIcon.vue";
import CategoryIcon from "@/components/prod/icons/CategoryIcon.vue";
import CodeIcon from "@/components/prod/icons/CodeIcon.vue";
import ExtendsIcon from "@/components/prod/icons/ExtendsIcon.vue";
import HelpIcon from "@/components/prod/icons/HelpIcon.vue";
import AdminIcon from "@/components/prod/icons/AdminIcon.vue";
import PuzzlesIcon from "@/components/prod/icons/PuzzlesIcon.vue";
import SettingsIcon from "@/components/prod/icons/SettingsIcon.vue";
import BizRuleIcon from "@/components/prod/icons/BizRuleIcon.vue";
import PublishIcon from "@/components/prod/icons/PublishIcon.vue";
import useMatrixStructureStore from "./admin/matrixStructure.store";
import useTableStructureStore from "./admin/tableStructure.store";
import customValidationStore from "./admin/customValidation.store";
import attributeManagementStore from "./admin/attributeManagement.store";

const addParentIds = (
  list: MenuItem[],
  grandParentId: string | null = null,
  parentId: string | null = null
): MenuItem[] => {
  return list.map((item) => {
    const newItem: MenuItem = { ...item, parentId, grandParentId };

    if (item.children) {
      newItem.children = addParentIds(
        item.children,
        item.menuLv === 1 ? item.menuId : grandParentId,
        item.menuId
      );
    }

    return newItem;
  });
};

const configIcon: { [key: string]: any } = {
  Dashboard: shallowRef(DashboardIcon),
  Category: shallowRef(CategoryIcon),
  Catalog: shallowRef(PuzzlesIcon),
  Code: shallowRef(CodeIcon),
  Settings: shallowRef(SettingsIcon),
  Extends: shallowRef(ExtendsIcon),
  Admin: shallowRef(AdminIcon),
  BizRule: shallowRef(BizRuleIcon),
  "Biz Rule": shallowRef(BizRuleIcon),
  Publish: shallowRef(PublishIcon),
};

const getLevelOneMenus = (menus: MenuItem[]): MenuItem[] => {
  return menus.map((item) => ({
    ...item,
    icon: configIcon[item.menuNm]
      ? configIcon[item.menuNm]
      : shallowRef(HelpIcon),
    path: configPath(item),
  }));
};

const useMenuStore = defineStore("menuStore", () => {
  const userStore = useUser();
  const productSore = useProductsStore();
  const productCreateSore = useProductsCreateStore();
  const structureStore = useStructureStore();
  const createStructureStore = useCreateStructureStore();
  const componentStore = useComponentStore();
  const resourceStore = useResourceStore();
  const loadingStore = useLoadingStore();
  const pricePlanStore = usePricePlanStore();
  const addOnStore = useAddOnStore();
  const discountStore = useDiscountStore();
  const categoryStore = useCategoryStore();
  const impactAnalysisStore = useImpactAnalysisStore();
  const extendManagerStore = useExtendManagerStore();
  const extendSearchStore = useExtendSearchStore();
  const extendCreateStore = useExtendCreateStore();
  const sysMessageStore = useSysMessageStore();
  const domainStore = useDomainStore();
  const cmCodeStore = useCmCodeStore();
  const terminologyStore = useTerminologyStore();
  const screenStore = useScreenStore();
  const urlStore = useUrlStore();
  const multiEntitySearchStore = useMultiEntitySearchStore();
  const multiEntityCreateStore = useMultiEntityCreateStore();
  const permissionStore = usePermissionStore();
  const permissionGroupStore = usePermissionGroupStore();
  const offerDuplicateProcessStore = useOfferDuplicateProcessStore();
  const offerOfferCreateProcessStore = useOfferCreateProcessStore();
  const labelStore = useLabelStore();
  const factorStore = useFactorStore();
  const matrixStructureStore = useMatrixStructureStore();
  const tableStructureStore = useTableStructureStore();
  const matrixSearchPaneStore = useMatrixSearchPaneStore();
  const ruleEngineStore = useRuleEngineStore();
  const relationSearchStore = useRelationSearchStore();
  const relationCreateStore = useRelationCreateStore();
  const publishManagerStore = usePublishManagerStore();
  const { resetCustomValidationStore } = customValidationStore();
  const attrMngStore = attributeManagementStore();
  const productsDuplicateStore = useProductsDuplicateStore();
  const duplicateStructureStore = useDuplicateStructureStore();
  const relationManagerDuplicateStore = useRelationManagerDuplicateStore();

  const menuItems = ref([] as any[]);
  const menuTree = ref([] as MenuItem[]);
  const menuDetailSelected = ref(null as any);
  const selectedMenuItem = ref(null as any);
  const isShowDetailLayout = ref(false);
  const activeMenu = ref(null as any);
  const activeMenuTree = ref([] as any[]);
  const parentId = ref(null as any);
  const openId = ref([] as number[]);

  const fetchTreeData = async () => {
    try {
      loadingStore.setLoading(true);
      const res = await userStore.getUserMenuList();
      if (res) {
        setPathList(res);
        setTreeData(res);
      }
    } catch (error) {
      throw error;
    } finally {
      loadingStore.setLoading(false);
    }
  };

  const updateSelectedMenuDetail = (data: any) => {
    menuDetailSelected.value = data;
  };

  const addMenuItem = (item: any) => {
    menuItems.value.push(item);
  };

  const removeMenuItem = (itemId: string) => {
    menuItems.value = menuItems.value.filter((item) => item.id !== itemId);
  };

  const updateMenuItem = (newItem: any) => {
    const index = menuItems.value.findIndex((item) => item.id === newItem.id);
    if (index !== -1) {
      // eslint-disable-next-line security/detect-object-injection
      menuItems.value[index] = newItem;
    }
  };

  const setSelectedMenuItem = (item: any) => {
    selectedMenuItem.value = item;
  };

  const setIsShowDetailLayout = (newVal: boolean) => {
    isShowDetailLayout.value = newVal;
  };

  const setTreeData = (data: MenuItem[]) => {
    menuTree.value = getLevelOneMenus(data);
    menuItems.value = addParentIds(data);
  };

  const setActiveMenu = (menu: any) => {
    activeMenu.value = menu;
  };

  const setActiveMenuTree = (data: any[]) => {
    activeMenuTree.value = data;
  };

  const setParentId = (itemId: any) => {
    parentId.value = itemId;
  };

  const setOpenId = (newVal: number[]) => {
    openId.value = newVal;
  };

  const resetOfferPage = () => {
    productSore.resetOffer();
    structureStore.resetStructure();
  };

  const resetOfferCreatePage = () => {
    productCreateSore.resetOffer();
    createStructureStore.resetStructure();
  };

  const resetOfferDuplicatePage = () => {
    productsDuplicateStore.resetOffer();
    duplicateStructureStore.resetStructure();
  };

  const resetOfferCreateAddOn = () => {
    addOnStore.resetStructure();
  };

  const resetOfferCreateDiscount = () => {
    discountStore.resetStructure();
  };

  const resetOfferCreatePricePlan = () => {
    pricePlanStore.resetStructure();
  };

  const resetComponentPage = () => {
    componentStore.resetComponentPage();
    resourceStore.resetComponentPage();
  };

  const resetComponentCreate = () => {
    componentStore.resetComponentCreate();
    resourceStore.resetComponentCreate();
  };

  const resetResourcePage = () => {
    resourceStore.resetResourceSearch();
  };
  const resetResourceCreate = () => {
    resourceStore.resetResourceCreate();
  };

  const removeMenuTab = (removeTab: any) => {
    if (removeTab?.id) {
      switch (removeTab.id) {
        case "17":
          resetOfferPage();
          break;
        case "22":
          resetOfferCreatePricePlan();
          break;
        case "23":
          resetOfferCreateAddOn();
          break;
        case "31":
          resetOfferCreateDiscount();
          break;
        case "42":
          extendManagerStore.$reset();
          break;
        case "60":
          resetComponentPage();
          matrixSearchPaneStore.$reset();
          break;
        case "61":
          setTimeout(() => {
            resetComponentCreate();
            matrixSearchPaneStore.$reset();
          }, 1000);
          break;
        case "65":
          setTimeout(() => {
            offerOfferCreateProcessStore.$reset();
            categoryStore.$reset();
          }, 1000);
          break;
        case "67":
          impactAnalysisStore.$reset();
          break;
        case "68":
          setTimeout(() => {
            resetResourcePage();
            matrixSearchPaneStore.$reset();
          }, 1000);
          break;
        case "69":
          resetResourceCreate();
          matrixSearchPaneStore.$reset();
          break;
        case "71":
          extendSearchStore.$reset();
          matrixSearchPaneStore.$reset();
          break;
        case "72":
          extendCreateStore.$reset();
          matrixSearchPaneStore.$reset();
          break;
        case "73":
          sysMessageStore.$reset();
          break;
        case "74":
          domainStore.$reset();
          break;
        case "75":
          cmCodeStore.$reset();
          break;
        case "76":
          terminologyStore.$reset();
          break;
        case "77":
          screenStore.$reset();
          urlStore.$reset();
          break;
        case "80":
          multiEntitySearchStore.$reset();
          break;
        case "81":
          multiEntityCreateStore.$reset();
          break;
        case "82":
          permissionStore.$reset();
          break;
        case "83":
          permissionGroupStore.$reset();
          break;
        case "92":
          resetOfferPage();
          offerOfferCreateProcessStore.$reset();
          break;
        case "93":
          setTimeout(() => {
            resetOfferCreatePage();
            offerOfferCreateProcessStore.$reset();
            // categoryStore.$reset();
          }, 1000);
          break;
        case "94":
          attrMngStore.resetStore();
          break;
        case "96":
          labelStore.resetStore();
          break;
        case "97":
          setTimeout(() => {
            factorStore.$reset();
          }, 1000);
          break;
        case "98":
          matrixStructureStore.$reset();
          break;
        case "99":
          tableStructureStore.$reset();
          break;
        case "Duplicate":
          extendManagerStore.$reset();
          offerDuplicateProcessStore.$reset();
          offerOfferCreateProcessStore.$reset();
          categoryStore.$reset();
          break;
        case "90":
          resetCustomValidationStore();
          break;
        case "100":
          ruleEngineStore.$reset();
          break;
        case "106":
          relationSearchStore.$reset();
          break;
        case "107":
          relationCreateStore.$reset();
          break;
        case "109":
          publishManagerStore.$reset();
          break;
        case "110":
          setTimeout(() => {
            resetOfferDuplicatePage();
            offerDuplicateProcessStore.$reset();
          }, 1000);
          break;
        case "111":
          setTimeout(() => {
            categoryStore.$reset();
            offerDuplicateProcessStore.$reset();
            offerOfferCreateProcessStore.$reset();
            relationManagerDuplicateStore.$reset();
          }, 1000);
          break;
        default:
          return;
      }
    }
  };

  return {
    menuItems,
    menuTree,
    menuDetailSelected,
    selectedMenuItem,
    isShowDetailLayout,
    activeMenu,
    activeMenuTree,
    parentId,
    openId,
    fetchTreeData,
    updateSelectedMenuDetail,
    addMenuItem,
    removeMenuItem,
    updateMenuItem,
    setSelectedMenuItem,
    setIsShowDetailLayout,
    setActiveMenu,
    setActiveMenuTree,
    setParentId,
    setOpenId,
    removeMenuTab,
  };
});

export default useMenuStore;
