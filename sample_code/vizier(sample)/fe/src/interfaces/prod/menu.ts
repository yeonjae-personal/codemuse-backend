interface MenuItem {
  id: string;
  parentId: string | null;
  children: MenuItem[];
  menuNm: string;
  menuLv: number;
  menuId: string;
  grandParentId: string | null;
  isShowMenuDetail?: boolean;
  icon?: string;
  path: string;
  rawName?: string;
  scrnPathNm?: string;
  scrnLinkUrl?: string;
  tabName?: string;
}

export type { MenuItem };
