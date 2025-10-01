import { OrgResponse, TreeNode } from "./type";

export const convertToTree = (data: any[]): TreeNode[] => {
    const nodes: { [key: string]: TreeNode } = {};
    const tree: TreeNode[] = [];
  
     
    data.forEach(item => {
      nodes[item.orgCd] = {
        id: item.orgCd,
        title: item.orgNm,
        level: item.orgLvCd,
        parentId: item.hposOrgCd,
        orgCdAllPathNm: item.orgCdAllPathNm,
        orgNmAllPathNm: item.orgNmAllPathNm,
        expanded: item.orgLvCd === "2" || item.orgLvCd === "1" // Expand nodes up to level 2
      };
    });
  
   
    data.forEach(item => {
      const node = nodes[item.orgCd];
      if (node.parentId) {
        const parentNode = nodes[node.parentId];
        if (parentNode) {
          if (!parentNode.children) {
            parentNode.children = [];
          }
          parentNode.children.push(node);
        }
      } else {
        tree.push(node);
      }
    });
  
   
    const updatePaths = (node: TreeNode, parentPathCd: string = '', parentPathNm: string = '') => {
      node.orgCdAllPathNm = parentPathCd ? `${parentPathCd}>${node.id}` : node.id;
      node.orgNmAllPathNm = parentPathNm ? `${parentPathNm}>${node.title}` : node.title;
      if (node.children) {
        node.children.forEach(child => updatePaths(child, node.orgCdAllPathNm, node.orgNmAllPathNm));
      }
    };
  
    tree.forEach(rootNode => updatePaths(rootNode));
  
    return tree;
  };


  export const getOrgCdExpanded = (data: OrgResponse[]): string[] => {
    return data
    .filter((org: any) => org.orgLvCd === '1' || org.orgLvCd === '2')
    .map((org: any) => org.orgCd);
  }