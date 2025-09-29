export type Item = {
    itemCode: string;
    itemName: string;
    middleItemCode: string;
    middleItemName: string;
    largeItemCode: string;
    largeItemName: string;
    sortNo: number;
    middleSortNo: number;
    largeSortNo: number;
  };
  
  export type GroupedItem = {
    name: string;
    value: string;
    sortNo: number;
    children: {
      name: string;
      value: string;
      sortNo: number;
    }[];
  };
  