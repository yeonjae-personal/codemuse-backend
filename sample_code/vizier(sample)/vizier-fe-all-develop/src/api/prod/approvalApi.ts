import { httpClient } from "@/utils/http-common";
import { APPROVAL_FLOW_SEARCH } from "./path";

const getListApprovalSearch = (params) => {
  return httpClient.get(APPROVAL_FLOW_SEARCH, { params });
};

export { getListApprovalSearch };
