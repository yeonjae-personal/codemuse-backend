import { getRandomValue } from "../utilsMock";

const useYnOptionRandom = ["Y", "N"];
const domainDivsCdOption = [
  "01",
  "02",
  "03",
  "04",
  "05",
  "06",
  "07",
  "08",
  "09",
  "10",
  "11",
  "1111",
  "11111",
  "12",
  "13",
  "14",
];
const domainGrpCdOption = [
  "01",
  "02",
  "03",
  "04",
  "05",
  "06",
  "07",
  "08",
  "09",
  "10",
  "11",
];

const mockData = (numRecords: number) => {
  const result = [];
  for (let i = 0; i < numRecords; i++) {
    result.push({
      domnId: "domnId" + i,
      domnNm: "테스트" + i,
      domnEngNm: "domnEngNm" + i,
      domnGrpCd: getRandomValue(domainGrpCdOption),
      domnGrpNm: "domnGrpNm " + i,
      domnDivsCd: getRandomValue(domainDivsCdOption),
      domnDivsNm: "domnDivsNm - label" + i,
      domnLen: "domnLen" + i,
      rgstUsr: "rgstUsr",
      rgstDtm: "2024-04-02 16:41:59",
      updDtm: "2024-04-02 16:41:59",
      useYn: getRandomValue(useYnOptionRandom),
      domnDscr: "domnDscr domnD" + i,
    });
  }
  return result;
};

export const domainList = mockData(100);

export const mockInit = {
  data: {
    domn_divs_cd: [
      {
        label: "BINARY",
        value: "01",
      },
      {
        label: "BLOB",
        value: "02",
      },
      {
        label: "CHAR",
        value: "03",
      },
      {
        label: "ENUM",
        value: "04",
      },
      {
        label: "LONGBLOB",
        value: "05",
      },
      {
        label: "LONGTEXT",
        value: "06",
      },
      {
        label: "MEDIUMBLOB",
        value: "07",
      },
      {
        label: "MEDIUMTEXT",
        value: "08",
      },
      {
        label: "SET",
        value: "09",
      },
      {
        label: "TEXT",
        value: "10",
      },
      {
        label: "TINYBLOB",
        value: "11",
      },
      {
        label: "2222",
        value: "1111",
      },
      {
        label: "22222",
        value: "11111",
      },
      {
        label: "TINYTEXT",
        value: "12",
      },
      {
        label: "VARBINARY",
        value: "13",
      },
      {
        label: "VARCHAR",
        value: "14",
      },
    ],
    domn_grp_cd: [
      {
        label: "금액",
        value: "01",
      },
      {
        label: "날짜",
        value: "02",
      },
      {
        label: "내용",
        value: "03",
      },
      {
        label: "명",
        value: "04",
      },
      {
        label: "번호",
        value: "05",
      },
      {
        label: "비율",
        value: "06",
      },
      {
        label: "삭제예정",
        value: "07",
      },
      {
        label: "수량",
        value: "08",
      },
      {
        label: "주소",
        value: "09",
      },
      {
        label: "척도",
        value: "10",
      },
      {
        label: "코드도메인",
        value: "11",
      },
    ],
  },
};