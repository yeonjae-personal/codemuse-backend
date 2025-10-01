import { createPinia, type Pinia } from "pinia";

import piniaPluginPersistedstate from "pinia-plugin-persistedstate";

// Pinia Stores
import useConfig from "@/store/config.store";
import useUser from "@/store/users.store";
import useGlobal from "@/store/global.store";
import useCmcd from "@/store/cmcd.store";
import useLoadingStore from "@/store/loading.store";
import useMenuStore from "@/store/menu.store";
import useMenuStoreInfo from "@/store/menuInfo.store";
import useOrgStore from "@/store/org.store";
import useOrgSearchStore from "@/store/orgSearch.store";
import useNotificationStore from "@/store/notification.store";
import {
  useProductsStore,
  useProductsCreateStore,
  useProductsDuplicateStore,
} from "@/store/product.store";
import useCategoryStore from "@/store/category.store";
import useSnackbarStore from "@/store/snackbar.store";
import useComponentStore from "@/store/component.store";
import useResourceStore from "@/store/resource.store";
import useDragStore from "@/store/drag.store";
import {
  useStructureStore,
  useCreateStructureStore,
  useDuplicateStructureStore,
} from "@/store/structure.store";
import useImpactAnalysisStore from "@/store/impact-analysis.store";
import useCommonStore from "@/store/common.store";
import {
  useExtendManagerStore,
  useRelationManagerDuplicateStore,
} from "@/store/extendManager.store";
import useExtendSearchStore from "@/store/extendSearch.store";
import useDomainStore from "@/store/admin/domain.store";
import useCmCodeStore from "@/store/admin/code.store";
import useSysMessageStore from "@/store/admin/message.store";
import useTerminologyStore from "@/store/admin/terminology.store";
import useScreenStore from "@/store/admin/screen.store";
import useUserStore from "@/store/admin/user.store";
import usePermissionStore from "@/store/admin/permission.store";
import useUrlStore from "@/store/admin/url.store";
import useFactorStore from "@/store/admin/factor.store";
import useDomainPopupStore from "@/store/admin/domainPopup.store";
import useExtendCreateStore from "./extendCreate.store";
import usePricePlanStore from "@/store/pricePlan.store";
import useAddOnStore from "@/store/addOn.store";
import useDiscountStore from "@/store/discount.store";
import usePermissionGroupStore from "./admin/permissionGroup.store";
import {
  useMultiEntitySearchStore,
  useMultiEntityCreateStore,
} from "./multiEntitySearch.store";
import useHistoryTabStore from "./catalog/historyTab.store";
import useOfferDuplicateProcessStore from "./offerDuplicateProcess.store";
import useOfferCreateProcessStore from "./offerCreateProcess.store";
import useLabelStore from "./admin/label.store";
import useMatrixSearchPaneStore from "./matrixSearchPane.store";
import useRuleEngineStore from "./admin/ruleEngine.store";
import {
  useRelationSearchStore,
  useRelationCreateStore,
} from "./relationSearch.store";
import usePublishManagerStore from "./publishManager.store";
import useApprovalStore from "./approval.store";
import useComponentOBStore from "./componentOb.store";
import useResourceOBStore from "./resourceOb.store";

/** Pinia Store */
const pinia: Pinia = createPinia();
pinia.use(piniaPluginPersistedstate);

export default pinia;

export {
  useConfig,
  useGlobal,
  useUser,
  useCmcd,
  useLoadingStore,
  useMenuStore,
  useMenuStoreInfo,
  useOrgStore,
  useOrgSearchStore,
  useProductsStore,
  useCategoryStore,
  useSnackbarStore,
  useComponentStore,
  useResourceStore,
  useDragStore,
  useStructureStore,
  useImpactAnalysisStore,
  useCommonStore,
  useExtendManagerStore,
  useExtendSearchStore,
  useDomainStore,
  useCmCodeStore,
  useSysMessageStore,
  useTerminologyStore,
  useScreenStore,
  useUserStore,
  usePermissionStore,
  useUrlStore,
  useFactorStore,
  useExtendCreateStore,
  useDomainPopupStore,
  useMultiEntitySearchStore,
  usePricePlanStore,
  useAddOnStore,
  useDiscountStore,
  useMultiEntityCreateStore,
  usePermissionGroupStore,
  useHistoryTabStore,
  useCreateStructureStore,
  useProductsCreateStore,
  useOfferDuplicateProcessStore,
  useOfferCreateProcessStore,
  useLabelStore,
  useMatrixSearchPaneStore,
  useRuleEngineStore,
  useRelationSearchStore,
  useRelationCreateStore,
  usePublishManagerStore,
  useApprovalStore,
  useProductsDuplicateStore,
  useDuplicateStructureStore,
  useRelationManagerDuplicateStore,
  useNotificationStore,
  useComponentOBStore,
  useResourceOBStore,
};
