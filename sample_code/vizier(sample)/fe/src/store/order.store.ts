import { RowActions } from "@/pages/functions/subs/common/CommonConstants";

export interface OrderItem {
  ordrItemDetlId: string;
  ordrItemId: string;
  ordrItemAtvl: string;
  ordrAttrEngNm: string;
  ordrAttrKornNm: string;
  dataType: string;
  rowStatCd: string;
  isChecked: boolean;
  actionType?: RowActions;
}
interface RowState {
  rowData: OrderItem[];
}

export const useOrderStore = defineStore<
  "orderStore",
  RowState,
  {},
  { setRowData(data: OrderItem[]): void }
>({
  id: "orderStore",
  state: (): RowState => ({
    rowData: [],
  }),
  actions: {
    setRowData(data: OrderItem[]) {
      this.rowData = data;
    },
  },
});
