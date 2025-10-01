/**
 * Type of request parameters for domain list search
 * @param srchWord: Search word
 * @param useYn: Use Y/N
 */
export type DomainRequest = {
  srchWord: string;
  useYn: string;
};

/**
 * Type of domain information
 * @param domnId: Domain ID
 * @param domnNm: Domain name
 * @param domnEngNm: English domain name
 * @param domnGrpCd: Domain group code
 * @param domnGrpNm: Domain group name
 * @param domnDivsCd: Domain division code
 * @param domnDivsNm: Domain division name
 * @param domnLen: Domain length
 * @param rgstUsr: Registered user
 * @param rgstDtm: Registration date
 * @param updDtm: Update date
 * @param useYn: Use Y/N
 * @param updUsr: Update user
 * @param domnDscr: Domain description
 * 
 */
export type Domain = {
  domnId: string;
  domnNm: string;
  domnEngNm: string;
  domnGrpCd: string;
  domnGrpNm: string;
  domnDivsCd: string;
  domnDivsNm: string;
  useYn: string;
  domnLen: string;
  rgstUsr: string;
  rgstDtm: string;
  updDtm?: string;
  updtUsr?: string;
  domnDscr: string;
};
