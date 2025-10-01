export const LABEL_SEARCH_TYPE = {
  NAME: "name",
  CODE: "code",
} as const;

export const DEFAULT_SEARCH_PARAMS = {
  value: "",
  type: LABEL_SEARCH_TYPE.NAME,
  page: 1,
  size: 14,
};

export const DEFAULT_PAGINATION = {
  currentPage: 1,
  pageSize: 14,
  totalItems: 0,
  totalPages: 0,
  totalSearchItems: 0,
};

export const ALLOWED_FILE_TYPE = [
  "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
];

export const MAX_SIZE_UPLOAD = 5;
