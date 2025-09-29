

const data = {
// ============= offer =============
custTypeCode: "G00006",
offerTypeCode: "G00002",
ageDivCode: "G00007",
dcTypeCode: "G00023",
pnltOcrcYn: "G00008",
// =======  resource ===========
type: "G00013",
rtngRscTypeCode: "G00036",
blngDivCode: "G00035",
acntSalesCode: "G00014",
pymntCmsnApplyYn: "G00008",
vatApplyYn: "G00008",
blngRscTypeCode:"G00014",
svcRscTypeCode: "G00033",
// =======  comp ===========
prvsnYn: "G00008",
shrngYn: "G00008",
ulmtYn: "G00008",
applyCycleCode: "G00009",
applyPitCode: "G00025",
rateApplyUnitCode: "G00026",
rtmRtngTrgtCode: "G00030",
blngWayCode: "G00032",
atpayTrgtYn: "G00008",
dcRgstDivCode: "G00038",
dcApplyPriodUnitCode: "G00039",
usePriodApplyCode: "G00040",
dplcnRgstPosibYn: "G00008",
rateDivCode: "G00042",
rateApplyTypeCode: "G00011",
dailyCalcCode: "G00010",
ppayDpayCode: "G00012",
applyUnitCode: "G00009",
icmOtgCode: "G00027",
dvcPrvsnYn: "G00008",
// =======  group ===========
offerGroupTypeCode: "G00043",
}as const;

type Data = typeof data;

export const getValueGeneral = <K extends keyof Data>(key: K | string): Data[K] | null => {
    return key in data ? data[key as K] : null;
};