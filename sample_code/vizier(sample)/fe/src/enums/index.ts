enum DialogSizeType {
  Small = "small",
  ESmall = "e-small",
  XMedium = "x-medium",
  Medium = "medium",
  Large = "large",
  ELarge = "e-large",
}

enum DialogIconType {
  Warning = "warning",
  Info = "info",
}

enum ButtonSizeType {
  // there are 4 types that are x-small, small, large, x-large
  XSmall = "x-small",
  Small = "small",
  Large = "large",
  XLarge = "x-large",
}

enum ButtonColorType {
  // there are 4 types that are x-small, small, large, x-large
  Primary = "primary",
  Secondary = "secondary",
  Gray = "gray",
  Blank = "blank",
}

enum TypeComponent {
  Service = "Service",
  Benefit = "Benefit",
  Characteristics = "Characteristics",
  Price = "Price",
}

enum TypeComponentCode {
  Service = "SR",
  Benefit = "BN",
  Characteristics = "CH",
  Price = "PR",
}

enum ChipType {
  Gray = "Gray",
  Green = "Green",
  Pink = "Pink",
  Blue = "Blue",
  LightPink = "LightPink",
}

enum LargeItemCode {
  Component = "C",
  Group = "G",
  Offer = "O",
  Benefit = "B",
  Resource = "R",
}

enum MiddleItemCode {
  PricePlan = "PL",
  AddOn = "AD",
  Discount = "DS",
  Device = "DE",
  Characteristics = "CH",
  Service = "SR",
  Benefit = "BN",
  Price = "PR",
  ServiceElement = "RS",
  RatingElement = "RA",
  BillingElement = "RB",
}

enum RequiredYn {
  Yes = "Y",
  No = "N",
}

enum SearchPaneType {
  Component = "C",
  Offer = "O",
  Resource = "R",
  Group = "G",
  MultiEntity = "M",
  FactorTable = "FT",
}

enum SearchBy {
  Name = "objName",
  Code = "objCode",
}

enum ColNumber {
  One = "1",
  Two = "2",
  Three = "3",
  Four = "4",
}

export {
  DialogSizeType,
  DialogIconType,
  ButtonSizeType,
  ButtonColorType,
  TypeComponent,
  ChipType,
  LargeItemCode,
  MiddleItemCode,
  TypeComponentCode,
  RequiredYn,
  SearchPaneType,
  SearchBy,
  ColNumber,
};
