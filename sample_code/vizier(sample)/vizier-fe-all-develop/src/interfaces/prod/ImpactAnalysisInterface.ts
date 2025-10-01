export interface ParamsUiImpactAnalysisItems {
    page: number;
    size: number;
    prodItemNm: String;
    prodItemCd: String;
    type: String;
    detlType: String;
    subType: String;
}
export interface ParamsUiImpactAnalysisRelation {
    prodUuid: string
}
export interface DownloadParamsImpact {
    resourceUuid: string;
    offerUuid: string;
    componentUuid: string;
    language?: "ko" | "en";
}

