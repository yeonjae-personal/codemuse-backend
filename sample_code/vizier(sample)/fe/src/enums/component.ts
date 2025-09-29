enum OfferType {
  PricePlan = "Price Plan",
  Discount = "Discount",
}

enum ResourceType {
  RatingElement = "RE",
  BillingElement = "BE",
  ServiceElement = "SE",
}

enum ComponentSubType {
  Additional = "AD",
  Message = "MS",
  Voice = "VO",
  RatingDiscount = "RD",
  Allowance = "AW",
  UsageCharge = "UC",
  RecurringCharge = "RC",
  DiscountRate = "DR",
  SalesInformation = "SI",
  BillingInformation = "BI",
  QoSInformation = "QS",
  DiscountTarget = "DT",
  LineOfBusinessInformation = "LB",
  SpamInformation = "SP",
  DiscountInformation = "DI",
}

const ComponentSubTypeMap: { [key: string]: string } = {
  AD: "Additional",
  MS: "Message",
  VO: "Voice",
  RD: "RatingDiscount",
  AW: "Allowance",
  UC: "UsageCharge",
  RC: "RecurringCharge",
  DR: "DiscountRate",
  SI: "SalesInformation",
  BI: "BillingInformation",
  QS: "QoSInformation",
  DT: "DiscountTarget",
  LB: "LineOfBusinessInformation",
  SP: "SpamInformation",
};

const dictionaryResourceTypeWithComponent: Record<string, string> = {
  [ComponentSubType.RecurringCharge]: ResourceType.BillingElement,
  [ComponentSubType.UsageCharge]: ResourceType.RatingElement,
  [ComponentSubType.Voice]: ResourceType.ServiceElement,
  [ComponentSubType.Message]: ResourceType.ServiceElement,
  [ComponentSubType.Additional]: ResourceType.ServiceElement,
  [ComponentSubType.Allowance]: ResourceType.RatingElement,
};

enum ResourceName {
  Name = "name",
  Code = "code",
}

export {
  OfferType,
  ResourceType,
  ResourceName,
  ComponentSubType,
  dictionaryResourceTypeWithComponent,
  ComponentSubTypeMap,
};
