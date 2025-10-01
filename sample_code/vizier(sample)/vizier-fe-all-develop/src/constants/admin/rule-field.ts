export const RULE_FIELD_SEARCH_TYPE = {
  NAME: "name",
  KEY: "key",
} as const;

export const DEFAULT_SEARCH_PARAMS = {
  value: "",
  type: RULE_FIELD_SEARCH_TYPE.NAME,
  page: 1,
  size: 10,
};

export const DEFAULT_PAGINATION = {
  currentPage: 1,
  pageSize: 10,
  totalItems: 0,
  totalPages: 0,
  totalSearchItems: 0,
};
