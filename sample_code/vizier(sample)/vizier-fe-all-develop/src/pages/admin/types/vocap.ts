/**
 * 
 * @param vocaNm: Term name 용어명
 * @param vocaCstcInfo: Term composition information 용어구성정보
 * @param vocaEngAbb: Term English abbreviation 용어영문약자
 * @param vocaEngNm: Term English name 용어영문명
 * @param stndYn: Standard Y/N  표준여부(공통코드)
 * @param domnNm: Domain name 도메인명
 * @param domnDivsCd: Domain division code 도메인구분코드(공통코드)
 * @param domnLen: Data length 데이터길이
 * @param vocaDscr: Description  설명
 * @param vocaDivsCd: Term division code  용어구분코드()
 * @param domnId: Domain ID 도메인ID
 * @param vocaId: Term ID  용어ID
 *
 */
export type VocapType = {
    vocaNm: string;
    vocaCstcInfo: string;
    vocaEngAbb: string;
    vocaEngNm: string;
    stndYn: string;
    domnNm: string;
    domnDivsCd: string;
    domnLen: string;
    vocaDscr: string;
    vocaDivsCd: string;
    domnId: string;
    vocaId: string;
    };


