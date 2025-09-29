
import { getRandomValue } from "@/mocks/utilsMock";

const useYnOption =  ["Y", "N"];
const cmcdDetName = ["BINARI", "BLOB", "CHAR", "OTHERS"]


 

const mockData = (numRecords: number) => {
    const result = [];
    for (let i = 0; i < numRecords; i++) {
      result.push( {
        cmcdGrpId: "DOMN_VS_CD"+ i,
        cmcdGrpNm: "cmcdGrpNm"+ i,
        cmcdGrpUseYn: getRandomValue(useYnOption),
        cmcdDetlId: ""+ i,
        cmcdDetlNm: getRandomValue(cmcdDetName),
        cmcdSortRank: "cmcdSortRank"+ i,
        cmcdDetlUseYn: "cmcdDetlUseYn"+ i,
        rgstUsr: "rgstUsr"+ i,
        rgstDtm:  "2024-04-02 16:41:59",
        updDtm:  "2024-04-02 16:41:59",
      });
    }
    return result;
  };
  
export const cmcdList = mockData(100);
 