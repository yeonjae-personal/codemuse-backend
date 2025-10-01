export interface GlobalData {
  posCd: string;
  delrCd: string;
  workEmno: string;
  mrktId: string;
}

export interface Step1Data {
  email: string;
  selectedOption: string;
  name: string;
  custRnno: string;
  custId: string;
  custKdCd: string;
}

export interface Step2Data {
  selectedRequestTypeOption: string;
  address: string;
  billEmalAddr: string;
  nameReceiver: string;
  namePayer: string;
  birthDay: string;
  selectedHowToPayOption: string;
  bank: string;
  bankNumber: string;
  billAcntId: string;
  credit: string;
  creditCardNumber: string;
  validFrom: string;
  nationId: string;
  bussinessId: string;
  billType: string;
  paymCustRnno: string;
}

export interface Step3Data {
  agency: string;
  pos: string;
  workerCode: string;
  sellerCode: string;
}

export interface Step4Data {
  phoneCode: string;
  txno: string;
  tsno: string;
}

export interface Step5Data {
  ppProdCd: string;
  trmMdlCd: string;
  trmMdlNo: string; //단말 모델넘버
  trmAmt: number;
  usimMdlCd: string;
  usimMdlNo: string; //유심 모델넘버
  usimAmt: number;
  crteTrmList: [];
  sppsProdList: [];
  dcntInfoList: [];
}

export interface Step8Data {
  selectedCash: boolean;
  selectedCard: boolean;
  selectedIstt: boolean;
  cardCount: number;
  cashAmount: number;
  cardAmount: [];
  isttAmount: number;
  selectedIsttPeriod: string;
  selectedUsimPaymentKd: string;
}

export const useRegisterStore = defineStore("register", {
  state: () => ({
    globalData: {
      posCd: "",
      delrCd: "",
      workEmno: "",
      mrktId: "WOO",
    } as GlobalData,
    step1Data: {
      selectedOption: "",
      email: "",
      name: "",
      custRnno: "",
      custId: "",
      custKdCd: "",
    } as Step1Data,
    step2Data: {
      selectedRequestTypeOption: "",
      address: "",
      billEmalAddr: "",
      nameReceiver: "",
      namePayer: "",
      birthDay: "",
      selectedHowToPayOption: "",
      bank: "",
      bankNumber: "",
      billAcntId: "",
    } as Step2Data,
    step3Data: {
      agency: "",
      pos: "",
      workerCode: "",
      sellerCode: "",
    } as Step3Data,
    step4Data: { phoneCode: "010", txno: "", tsno: ""} as Step4Data,
    step5Data: {
      ppProdCd: "",
      trmMdlCd: "",
      trmMdlNo: "",
      trmAmt: 0,
      usimMdlCd: "",
      usimMdlNo: "",
      usimAmt: 0,
      crteTrmList: [],
      sppsProdList: [],
      dcntInfoList: [],
    } as Step5Data,
    step8Data: {
      selectedCash: false,
      selectedCard: false,
      selectedIstt: false,
      cardCount: 1,
      cashAmount: 0,
      cardAmount: [],
      isttAmount: 0,
      selectedIsttPeriod: "3개월",
      selectedUsimPaymentKd: "현금",
    } as Step8Data,
  }),
  actions: {
    setStepData(step: number, data: any) {
      (this as any)[`step${step}Data`] = data;
    },
    setGlobalData(key: keyof typeof this.globalData, value: any) {
      if (key in this.globalData) {
        this.globalData[key as string] = value;
      }
    },
  },
});
