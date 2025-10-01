import { MenuItem } from "@/interfaces/prod/menu";

const pathList: Record<string, string> = {};

export const setPathList = (items: MenuItem[]): void => {
  items.forEach((item) => {
    if (item.children) {
      setPathList(item.children);
    } else if (item.scrnPathNm) {
      pathList[item.menuId] = `/temp-ui${item.scrnLinkUrl}`;
    }
  });
};

export const configPath = (item: MenuItem) => {
  return (
    pathList[item.menuId] || `/functions/product-platform/page/${item.menuId}`
  );
};

export const findMenuItem = (
  menuItems: MenuItem[],
  menuId: string
): MenuItem | null => {
  function traverse(
    nodes: MenuItem[],
    parentNames: string[] = []
  ): MenuItem | null {
    for (const item of nodes) {
      const currentNames = [...parentNames, item.menuNm];
      if (item.menuId === menuId) {
        const menuNm = currentNames.join(" ");
        const tabName = item.children
          ? menuNm
          : item.menuLv.toString() === "2"
            ? item.menuNm
            : [parentNames[parentNames.length - 1], item.menuNm].join(" ");
        const rawName = currentNames.join("").replace(/\s+/g, "");
        return { ...item, menuNm, rawName, tabName: tabName };
      }
      if (item.children) {
        const result = traverse(item.children, currentNames);
        if (result) return result;
      }
    }
    return null;
  }
  return traverse(menuItems);
};
