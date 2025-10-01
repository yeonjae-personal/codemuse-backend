interface Pagination {
  totalSearchItems: number;
  currentPage: number;
  pageSize: number;
  totalItems: number;
  totalPages: number;
  optionItemPerPage?: any[] | null;
}

export type { Pagination };
