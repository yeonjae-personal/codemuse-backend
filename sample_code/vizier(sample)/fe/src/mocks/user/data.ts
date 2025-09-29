
 
import { userResponse } from "../../pages/userinfo/type";
import { getRandomValue } from "../utilsMock";

const mockData = (numRecords: number) => {
    const result: userResponse[] = [];
    for (let i = 0; i < numRecords; i++) {
      result.push({
        userId: "userId"+i,
        userNm: "userNm" ,
        userKdCd: getRandomValue(["S1", "S2", "S3"]),
        userKdCdNm: "userKdCdNm" + i,
        orgCd: "orgCd" + i,
        orgNm: "orgNm" + i,
        whofStatCd: getRandomValue(["whofStatCd1", "whofStatCd2", "whofStatCd3", "whofStatCd4", "whofStatCd5"]),
        whofStatNm: "whofStatNm" + i,
        updDtm: "2024-04-02 16:41:59"
      });
    }
    return result;
  };
  
export const userList = mockData(100);
 