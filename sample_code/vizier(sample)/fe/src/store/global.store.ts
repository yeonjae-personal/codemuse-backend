interface Modal {
  iconClose: boolean;
  title: string;
  component: any;
  dataInput: any;
  dataOutput: any;
  show: boolean;
  width: string | null;
  resolve?: (data: any) => void;
}
interface Alert {
  show: boolean;
  title: string;
  text: any;
  width: string | null;
  class: string;
  resolve?: (data: any) => void;
  isAlert: boolean;
}

/** Global Store */
const useGlobalStore = defineStore("global", {
  state: () => ({
    toastInfor: {},
    modals: [] as Modal[],
    alert: {} as Alert,
  }),
  getters: {
    getToastInfor(state) {
      return state.toastInfor;
    },
  },
  actions: {
    setToastInfor(data: any, timeout: number = 2000) {
      if (!data.class) {
        data.class = "top-right";
      }
      this.toastInfor = data;

      setTimeout(() => {
        this.toastInfor = {};
      }, timeout);
    },
    openModal(modal: Modal): Promise<any> {
      return new Promise((resolve) => {
        modal.show = true;
        modal.resolve = resolve;
        this.modals.push(modal);
      });
    },
    closeModal(index: number, data?: any) {
      const modal = this.modals[index as number];
      modal.dataOutput = data;
      modal.show = false;
      this.modals.splice(index, 1);
      if (modal.resolve) {
        modal.resolve(data);
      }
    },
    openAlertConfirm(alert: Alert): Promise<any> {
      return new Promise((resolve) => {
        alert.show = true;
        alert.resolve = resolve;
        this.alert = alert;
        alert.isAlert = false;
      });
    },
    closeAlertConfirm(data: boolean) {
      this.alert.show = false;
      if (this.alert.resolve) {
        this.alert.resolve(data);
      }
    },

    openAlertMessage(alert: Alert): Promise<any> {
      return new Promise((resolve) => {
        alert.show = true;
        alert.resolve = resolve;
        this.alert = alert;
        alert.isAlert = alert.isAlert ?? true;
      });
    },
  },
});

export default useGlobalStore;
