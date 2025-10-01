import { getRandomValue } from "../utilsMock";

const sysMsgLangCdOption =  ["KORN", "ENG"];
 
const mockData = (numRecords: number) => {
    const result = [];
    for (let i = 0; i < numRecords; i++) {
      result.push( {
        sysMsgId: "sysMsgId"+ i,
        sysMsgLangCd: getRandomValue(sysMsgLangCdOption),
        sysMsgLangNm: "sysMsgLangNm" + i,
        sysMsgCntn: "sysMsgCntn"+ i,

        rgstUsr: "rgstUsr",
        rgstDtm:  "2024-04-02 16:41:59",
        updDtm:  "2024-04-02 16:41:59",
      });
    }
    return result;
  };
  
export const sysmsgList = mockData(100);
 