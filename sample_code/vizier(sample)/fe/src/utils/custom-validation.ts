import moment from "moment-timezone";
import cloneDeep from "lodash-es/cloneDeep";
import isEmpty from "lodash-es/isEmpty";
import isEqual from "lodash-es/isEqual";
import every from "lodash-es/every";
import includes from "lodash-es/includes";
import { DATE_FORMAT } from "@/constants/";
import { DFSDetectCycle } from "./extend-utils";
import { RequiredYn } from "@/enums";

const flatAllRules = (items: any[]): any[] => {
  const merged = {};

  items.forEach((item) => {
    item.datas.forEach((data) => {
      const uuid = data.attrUuid;

      if (!merged[uuid as string]) {
        merged[uuid as string] = {
          ...data,
          multipleValues: [],
          ranges: [],
          dateRanges: [],
        };
      }

      const mergedItem = merged[uuid as string];

      switch (data.fieldTypeCode) {
        case "DL":
        case "DM":
          mergedItem.multipleValues = Array.from(
            new Set([...mergedItem.multipleValues, ...data.multipleValues])
          );
          break;

        case "NF":
        case "RF":
          mergedItem.ranges.push({
            rangeStartVal: data.rangeStartVal || Number.NEGATIVE_INFINITY,
            rangeEndVal: data.rangeEndVal || Number.POSITIVE_INFINITY,
          });
          break;

        case "DP":
          mergedItem.dateRanges.push({
            rangeStartDtm:
              data.rangeStartDtm || new Date(-8640000000000000).toISOString(),
            rangeEndDtm:
              data.rangeEndDtm || new Date(8640000000000000).toISOString(),
          });
          break;

        default:
          break;
      }
    });
  });

  return Object.values(merged);
};

export const isValidDifferentItemRule = (
  filteredRules: any[],
  componentInfo: any[]
) => {
  if (isEmpty(filteredRules)) return true;
  const allRules = flatAllRules(filteredRules);
  const actionRules = allRules.filter(({ condType }: any) => condType === "A");
  return actionRules.every((data: any) => {
    const matchingComponent = componentInfo.find(
      ({ attrUuid }) => attrUuid === data.attrUuid
    );

    if (!matchingComponent) return true;

    const { fieldTypeCode: fieldType, attrVal } = matchingComponent;

    if (fieldType !== data.fieldTypeCode) return true;

    switch (fieldType) {
      case "NF":
      case "RF":
        return data.ranges.some(
          ({ rangeStartVal, rangeEndVal }) =>
            attrVal >= rangeStartVal && attrVal <= rangeEndVal
        );
      case "DP":
        return data.dateRanges.some(({ rangeStartDtm, rangeEndDtm }) =>
          moment(attrVal, DATE_FORMAT.DATE_TYPE).isBetween(
            moment(rangeStartDtm).format(DATE_FORMAT.DATE_TYPE),
            moment(rangeEndDtm).format(DATE_FORMAT.DATE_TYPE),
            undefined,
            "[]"
          )
        );

      case "DL":
      case "DM": {
        if (data.multipleValues.length === 0 && !attrVal) return true;
        return data.multipleValues.includes(attrVal);
      }

      default:
        return true;
    }
  });
};

const isConditionMet = (condition: any, allRule: any[]): boolean => {
  const attributeType = condition.fieldTypeCode;
  const attrUuid = condition.attrUuid;

  if (["DL", "DM"].includes(attributeType)) {
    return allRule.some(
      ({ attrUuid: uuid, attrVal }) =>
        uuid === attrUuid && condition.multipleValues.includes(attrVal)
    );
  }

  if (["NF", "RF"].includes(attributeType)) {
    const { rangeStartVal: startVal, rangeEndVal: endVal } = condition;

    if (startVal && !endVal) {
      return allRule.some(
        ({ attrUuid: uuid, attrVal }) =>
          uuid === attrUuid && Number(attrVal) >= Number(startVal)
      );
    }

    if (endVal && !startVal) {
      return allRule.some(
        ({ attrUuid: uuid, attrVal }) =>
          uuid === attrUuid && Number(attrVal) <= Number(endVal)
      );
    }

    if (startVal && endVal) {
      return allRule.some(
        ({ attrUuid: uuid, attrVal }) =>
          uuid === attrUuid &&
          Number(attrVal) >= Number(startVal) &&
          Number(attrVal) <= Number(endVal)
      );
    }
  }

  if (["DP"].includes(attributeType)) {
    const { rangeStartDtm: startDtm, rangeEndDtm: endDtm } = condition;
    if (startDtm && !endDtm) {
      return allRule.some(
        ({ attrUuid: uuid, attrVal }) =>
          uuid === attrUuid &&
          moment(attrVal, DATE_FORMAT.DATE_TYPE).isSameOrAfter(
            moment(startDtm).format(DATE_FORMAT.DATE_TYPE)
          )
      );
    }

    if (endDtm && !startDtm) {
      return allRule.some(
        ({ attrUuid: uuid, attrVal }) =>
          uuid === attrUuid &&
          moment(attrVal, DATE_FORMAT.DATE_TYPE).isSameOrBefore(
            moment(endDtm).format(DATE_FORMAT.DATE_TYPE)
          )
      );
    }

    if (startDtm && endDtm) {
      return allRule.some(
        ({ attrUuid: uuid, attrVal }) =>
          uuid === attrUuid &&
          moment(attrVal, DATE_FORMAT.DATE_TYPE).isBetween(
            moment(startDtm).format(DATE_FORMAT.DATE_TYPE),
            moment(endDtm).format(DATE_FORMAT.DATE_TYPE),
            undefined,
            "[]"
          )
      );
    }
  }

  return false;
};

export const filteredConditionRules = (
  listRulesOriginal: any[],
  allRule: any[]
) => {
  return listRulesOriginal.filter((rule) => {
    const conditions = rule.datas.filter(({ condType }) => condType === "C");
    return conditions.every((condition: any) =>
      isConditionMet(condition, allRule)
    );
  });
};

enum ADDITION_FIELD_TYPE {
  DL = "DL",
  DM = "DM",
  DP = "DP",
  NF = "NF",
  RF = "RF",
}

const getRulesMatchingWithCondition = (item, listRules) => {
  const conditionItemId = item.attrUuid;
  const conditionFieldTypeCode = item.fieldTypeCode;
  const conditionItemValue = item.attrVal;
  let conditions: any = [];
  switch (conditionFieldTypeCode) {
    case ADDITION_FIELD_TYPE.DL:
      conditions = listRules.filter(
        (rule) =>
          rule.condType === "C" &&
          rule.attrUuid === conditionItemId &&
          ((rule.multipleValues.length === 0 &&
            (conditionItemValue === "" || conditionItemValue === null)) ||
            rule.multipleValues.includes(conditionItemValue))
      );
      break;
    case ADDITION_FIELD_TYPE.DM:
      conditions = listRules.filter(
        (rule) =>
          rule.condType === "C" &&
          rule.attrUuid === conditionItemId &&
          ((rule.multipleValues.length === 0 &&
            conditionItemValue?.length === 0) ||
            (conditionItemValue
              ?.map((option) => rule.multipleValues.includes(option.trim()))
              .filter((option) => option).length > 0 &&
              conditionItemValue
                ?.map((option) => rule.multipleValues.includes(option.trim()))
                .filter((item) => !item).length === 0))
      );
      break;
    case ADDITION_FIELD_TYPE.DP:
      conditions = listRules.filter((rule) => {
        if (rule.rangeStartDtm !== null && rule.rangeEndDtm !== null) {
          return (
            rule.condType === "C" &&
            rule.attrUuid === conditionItemId &&
            moment(conditionItemValue).isSameOrAfter(
              moment(rule.rangeStartDtm).format(DATE_FORMAT.DATE_TYPE)
            ) &&
            moment(conditionItemValue).isSameOrBefore(
              moment(rule.rangeEndDtm).format(DATE_FORMAT.DATE_TYPE)
            )
          );
        } else if (rule.rangeStartDtm !== null) {
          return (
            rule.condType === "C" &&
            rule.attrUuid === conditionItemId &&
            moment(conditionItemValue).isSameOrAfter(
              moment(rule.rangeStartDtm).format(DATE_FORMAT.DATE_TYPE)
            )
          );
        } else if (rule.rangeEndDtm !== null) {
          return (
            rule.condType === "C" &&
            rule.attrUuid === conditionItemId &&
            moment(conditionItemValue).isSameOrBefore(
              moment(rule.rangeEndDtm).format(DATE_FORMAT.DATE_TYPE)
            )
          );
        }
        return false;
      });
      break;
    case ADDITION_FIELD_TYPE.NF:
    case ADDITION_FIELD_TYPE.RF:
      if (conditionItemValue) {
        conditions = listRules.filter((rule) => {
          if (rule.rangeStartVal !== null && rule.rangeEndVal !== null) {
            return (
              rule.condType === "C" &&
              rule.attrUuid === conditionItemId &&
              Number(conditionItemValue) >= rule.rangeStartVal &&
              Number(conditionItemValue) <= rule.rangeEndVal
            );
          } else if (rule.rangeStartVal !== null) {
            return (
              rule.condType === "C" &&
              rule.attrUuid === conditionItemId &&
              Number(conditionItemValue) >= rule.rangeStartVal
            );
          } else if (rule.rangeEndVal !== null) {
            return (
              rule.condType === "C" &&
              rule.attrUuid === conditionItemId &&
              Number(conditionItemValue) <= rule.rangeEndVal
            );
          }
          return false;
        });
      }
      break;

    default:
      break;
  }

  // Remove duplicate condition
  let listConditions: string[] = [];
  conditions.forEach((item) => {
    const stringItem = JSON.stringify(item);
    if (!listConditions.find((cond) => cond === stringItem))
      listConditions.push(stringItem);
  });
  listConditions = listConditions.map((item) => JSON.parse(item));
  return listConditions;
};

const updateActionToField = (
  conditions,
  listRules,
  baseData,
  updateRules,
  listRulesOriginal
) => {
  let result: any = [];
  const listActions: any = [];
  conditions.forEach((condition) => {
    // get related condition in a box
    const listRelatedCondition = listRules.filter(
      (rule) =>
        rule.condType === "C" &&
        rule.validCode === condition?.validCode &&
        rule.attrUuid !== condition.attrUuid
    );
    let countValidRelatedCondition = 0;

    listRelatedCondition.forEach((relatedCond) => {
      const relatedItemId = relatedCond.attrUuid;
      const relatedItemCode = relatedCond.fieldTypeCode;
      const currentAdditionalItem = baseData.find(
        (addItem) => addItem.attrUuid === relatedItemId
      );
      if (currentAdditionalItem) {
        const currentValue = currentAdditionalItem.attrVal;
        switch (relatedItemCode) {
          case ADDITION_FIELD_TYPE.DL:
            if (
              relatedCond.multipleValues.includes(currentValue) ||
              (relatedCond.multipleValues.length === 0 &&
                (currentValue === "" || currentValue === null))
            ) {
              countValidRelatedCondition += 1;
            }
            break;
          case ADDITION_FIELD_TYPE.DM:
            if (
              currentValue?.find((item) =>
                relatedCond.multipleValues.includes(item)
              ) ||
              (relatedCond.multipleValues.length === 0 &&
                (currentValue === "" || currentValue === null))
            ) {
              countValidRelatedCondition += 1;
            }
            break;
          case ADDITION_FIELD_TYPE.NF:
          case ADDITION_FIELD_TYPE.RF:
            if (
              relatedCond.rangeStartVal !== null &&
              relatedCond.rangeEndVal !== null &&
              Number(currentValue) >= relatedCond.rangeStartVal &&
              Number(currentValue) <= relatedCond.rangeEndVal
            ) {
              countValidRelatedCondition += 1;
            } else if (
              relatedCond.rangeStartVal !== null &&
              relatedCond.rangeEndVal === null &&
              Number(currentValue) >= relatedCond.rangeStartVal
            ) {
              countValidRelatedCondition += 1;
            } else if (
              relatedCond.rangeEndVal !== null &&
              relatedCond.rangeStartVal === null &&
              Number(currentValue) <= relatedCond.rangeEndVal
            ) {
              countValidRelatedCondition += 1;
            }
            break;
          case ADDITION_FIELD_TYPE.DP:
            if (
              relatedCond.condType === "C" &&
              relatedCond.rangeStartDtm !== null &&
              relatedCond.rangeEndDtm !== null &&
              moment(currentValue).isSameOrAfter(
                moment(relatedCond.rangeStartDtm).format(DATE_FORMAT.DATE_TYPE)
              ) &&
              moment(currentValue).isSameOrBefore(
                moment(relatedCond.rangeEndDtm).format(DATE_FORMAT.DATE_TYPE)
              )
            ) {
              countValidRelatedCondition += 1;
            } else if (
              relatedCond.condType === "C" &&
              relatedCond.rangeStartDtm !== null &&
              relatedCond.rangeEndVal === null &&
              moment(currentValue).isSameOrAfter(
                moment(relatedCond.rangeStartDtm).format(DATE_FORMAT.DATE_TYPE)
              )
            ) {
              countValidRelatedCondition += 1;
            } else if (
              relatedCond.condType === "C" &&
              relatedCond.rangeEndDtm !== null &&
              relatedCond.rangeStartDtm === null &&
              moment(currentValue).isSameOrBefore(
                moment(relatedCond.rangeEndDtm).format(DATE_FORMAT.DATE_TYPE)
              )
            ) {
              countValidRelatedCondition += 1;
            }
            break;
          default:
            break;
        }
      }
    });
    // If related condition and condition valid then apply rules to additional field
    if (
      condition &&
      countValidRelatedCondition === listRelatedCondition.length
    ) {
      // get all actions in cellbox to apply rules
      listActions.push(
        listRules.filter(
          (rule) =>
            rule.condType === "A" && rule.validCode === condition.validCode
        )
      );
    }
  });

  // Check and apply action by seq sort
  const { listNewActions } = getValidActionApplyToField({
    listActions: listActions.flat(),
    listRulesOriginal,
    baseData,
  });
  // Apply rules
  result = applyActions(listNewActions, baseData);

  // Check if addtional field matching other rules then continue apply rules to this field
  const nextAction = result.filter((element) =>
    listNewActions.some((item) => item.attrUuid === element.attrUuid)
  );
  if (nextAction) {
    nextAction.forEach((nAction) => {
      result.concat(updateRules(nAction, listRulesOriginal, result));
    });
  }

  return result;
};

const getValidActionApplyToField = ({
  listRulesOriginal,
  listActions,
  baseData,
  isReset = false,
}) => {
  const listOtherOriginalRules = listRulesOriginal
    .filter((rule) => rule.datas)
    .map((rule) =>
      rule.datas.map((attr) => ({ ...attr, ruleSeqNo: rule.seqNo }))
    )
    .flat();
  const listResetActions: any = [];
  const listNewActions: any = [];
  listActions.forEach((action) => {
    // Get other rules
    const listOtherRules = listOtherOriginalRules.filter((rule) => {
      return (
        rule.condType === "A" &&
        rule.attrUuid === action.attrUuid &&
        rule.validCode !== action.validCode
      );
    });

    // Check all condition of rule matching
    let listActionValid: any = isReset ? [] : [action];
    listOtherRules.forEach((otherRule) => {
      if (
        checkRelatedRuleMatchingCondition(
          otherRule,
          listOtherOriginalRules,
          baseData
        )
      ) {
        listActionValid.push(otherRule);
      }
    });
    // sort rule is smallest
    listActionValid = listActionValid.sort(
      (prev, next) => prev.ruleSeqNo - next.ruleSeqNo
    );
    if (listActionValid.length) {
      listNewActions.push(listActionValid[0]);
    } else {
      // Get other rules by action id
      const actionInOtherRules = listOtherOriginalRules
        .filter((rule) => {
          return (
            rule.condType === "A" &&
            rule.validCode !== action.validCode &&
            rule.attrUuid === action.attrUuid
          );
        })
        .map((item) => item.validCode);

      //filter action
      const otherRules = listOtherOriginalRules.filter((rule) => {
        return (
          rule.condType === "C" && actionInOtherRules.includes(rule.validCode)
        );
      });
      // Check action not matching condition then add to reset list
      let count = 0;
      otherRules.forEach((otherRule) => {
        if (
          checkRelatedRuleMatchingCondition(
            otherRule,
            listOtherOriginalRules,
            baseData
          )
        ) {
          count++;
        }
      });
      if (count === 0) {
        listResetActions.push(action);
      }
    }
  });
  return { listNewActions, listResetActions };
};

const checkRelatedRuleMatchingCondition = (item, listRules, baseData) => {
  const listRelatedCondition = listRules.filter(
    (rule) => rule.condType === "C" && rule.validCode === item?.validCode
  );
  let countValidRelatedCondition = 0;
  listRelatedCondition.forEach((relatedCond) => {
    const relatedItemId = relatedCond.attrUuid;
    const relatedItemCode = relatedCond.fieldTypeCode;
    const currentAdditionalItem = baseData.find(
      (addItem) => addItem.attrUuid === relatedItemId
    );
    if (currentAdditionalItem) {
      const currentValue = currentAdditionalItem.attrVal;
      switch (relatedItemCode) {
        case ADDITION_FIELD_TYPE.DL:
          if (
            relatedCond.multipleValues.includes(currentValue) ||
            (relatedCond.multipleValues.length === 0 &&
              (currentValue === "" || currentValue === null))
          ) {
            countValidRelatedCondition += 1;
          }
          break;
        case ADDITION_FIELD_TYPE.DM:
          if (
            currentValue?.find((item) =>
              relatedCond.multipleValues.includes(item)
            ) ||
            (relatedCond.multipleValues.length === 0 &&
              (currentValue === "" || currentValue === null))
          ) {
            countValidRelatedCondition += 1;
          }
          break;
        case ADDITION_FIELD_TYPE.NF:
        case ADDITION_FIELD_TYPE.RF:
          if (
            relatedCond.rangeStartVal !== null &&
            relatedCond.rangeEndVal !== null &&
            Number(currentValue) >= relatedCond.rangeStartVal &&
            Number(currentValue) <= relatedCond.rangeEndVal
          ) {
            countValidRelatedCondition += 1;
          } else if (
            relatedCond.rangeStartVal !== null &&
            relatedCond.rangeEndVal === null &&
            Number(currentValue) >= relatedCond.rangeStartVal
          ) {
            countValidRelatedCondition += 1;
          } else if (
            relatedCond.rangeEndVal !== null &&
            relatedCond.rangeStartVal === null &&
            Number(currentValue) <= relatedCond.rangeEndVal
          ) {
            countValidRelatedCondition += 1;
          }
          break;
        case ADDITION_FIELD_TYPE.DP:
          if (
            relatedCond.condType === "C" &&
            relatedCond.rangeStartDtm !== null &&
            relatedCond.rangeEndDtm !== null &&
            moment(currentValue).isSameOrAfter(
              moment(relatedCond.rangeStartDtm).format(DATE_FORMAT.DATE_TYPE)
            ) &&
            moment(currentValue).isSameOrBefore(
              moment(relatedCond.rangeEndDtm).format(DATE_FORMAT.DATE_TYPE)
            )
          ) {
            countValidRelatedCondition += 1;
          } else if (
            relatedCond.condType === "C" &&
            relatedCond.rangeStartDtm !== null &&
            relatedCond.rangeEndDtm === null &&
            moment(currentValue).isSameOrAfter(
              moment(relatedCond.rangeStartDtm).format(DATE_FORMAT.DATE_TYPE)
            )
          ) {
            countValidRelatedCondition += 1;
          } else if (
            relatedCond.condType === "C" &&
            relatedCond.rangeEndDtm !== null &&
            relatedCond.rangeStartDtm === null &&
            moment(currentValue).isSameOrBefore(
              moment(relatedCond.rangeEndDtm).format(DATE_FORMAT.DATE_TYPE)
            )
          ) {
            countValidRelatedCondition += 1;
          }
          break;
        default:
          break;
      }
    }
  });
  return countValidRelatedCondition === listRelatedCondition.length;
};

const resetFieldNotMatchingCondition = (
  conditions,
  listRules,
  currentId,
  baseData,
  listRulesOriginal,
  updateRules
) => {
  let resetList: any = [];
  const listValidConditions = conditions.map((condItem) => condItem.validCode);
  const hasCondition = listRules.filter(
    (rule) => rule.condType === "C" && rule.attrUuid === currentId
  );
  if (
    hasCondition.filter(
      (hasCondItem) => !listValidConditions.includes(hasCondItem.validCode)
    ).length > 0
  ) {
    hasCondition
      .filter(
        (hasCondItem) => !listValidConditions.includes(hasCondItem.validCode)
      )
      .forEach((cond) => {
        const listActions = listRules.filter(
          (rule) => rule.condType === "A" && rule.validCode === cond.validCode
        );
        // Check and apply action by seq sort
        const { listNewActions, listResetActions } = getValidActionApplyToField(
          {
            listActions,
            listRulesOriginal,
            baseData,
            isReset: true,
          }
        );

        if (listNewActions.length) {
          // Apply rules
          resetList = applyActions(listNewActions, baseData);
          // Check if addtional field matching other rules then continue apply rules to this field
          const nextAction = resetList.filter((element) =>
            listNewActions.some((item) => item.attrUuid === element.attrUuid)
          );
          if (nextAction) {
            nextAction.forEach((nAction) => {
              resetList.concat(
                updateRules(nAction, listRulesOriginal, resetList)
              );
            });
          }
        }
        resetList = resetList.filter(
          (rsItem) =>
            !listResetActions.find((item) => item.attrUuid === rsItem.attrUuid)
        );
        resetList.push(resetActions(listResetActions, baseData));
      });
  }
  return resetList.flat();
};

const applyActions = (listActions, additionalData) => {
  const listResult = additionalData.map((fieldItem) => {
    const actionItem = listActions.find(
      ({ attrUuid }) => attrUuid === fieldItem.attrUuid
    );
    // Apply action
    if (actionItem) {
      if (["DP"].includes(actionItem.fieldTypeCode)) {
        const isSameValue = moment(
          actionItem.rangeStartDtm,
          DATE_FORMAT.DATE_TYPE
        ).isSame(moment(actionItem.rangeEndDtm, DATE_FORMAT.DATE_TYPE));
        const isApplyStartValue =
          isSameValue || (actionItem.rangeStartDtm && actionItem.rangeEndDtm);
        const isValidValue =
          fieldItem.attrVal &&
          moment(fieldItem.attrVal, DATE_FORMAT.DATE_TYPE).isBetween(
            moment(actionItem.rangeStartDtm, DATE_FORMAT.DATE_TYPE),
            moment(actionItem.rangeEndDtm, DATE_FORMAT.DATE_TYPE),
            undefined,
            "[]"
          );
        const minDate = actionItem.rangeStartDtm
          ? moment(actionItem.rangeStartDtm).format(DATE_FORMAT.DATE_TYPE)
          : "";
        const maxDate = actionItem.rangeEndDtm
          ? moment(actionItem.rangeEndDtm).format(DATE_FORMAT.DATE_TYPE)
          : "";
        return {
          ...fieldItem,
          isApplyRule: true,
          clearable: !(actionItem.rangeStartDtm && actionItem.rangeEndDtm),
          disabled: isSameValue,
          minDate,
          maxDate,
          attrVal: isApplyStartValue
            ? isValidValue
              ? moment(fieldItem.attrVal).format(DATE_FORMAT.DATE_TYPE)
              : moment(actionItem.rangeStartDtm).format(DATE_FORMAT.DATE_TYPE)
            : moment(fieldItem.attrVal).format(DATE_FORMAT.DATE_TYPE),
        };
      }
      if (["NF", "RF"].includes(actionItem.fieldTypeCode)) {
        const isValidValue =
          fieldItem.attrVal &&
          fieldItem.attrVal >= 0 &&
          fieldItem.attrVal >= actionItem.rangeStartVal &&
          fieldItem.attrVal <= actionItem.rangeEndVal;
        const isApplyStartValue =
          actionItem.rangeStartVal === actionItem.rangeEndVal ||
          (actionItem.rangeStartVal >= 0 && actionItem.rangeEndVal >= 0);
        return {
          ...fieldItem,
          disabled: actionItem.rangeStartVal === actionItem.rangeEndVal,
          attrVal: isApplyStartValue
            ? isValidValue
              ? fieldItem.attrVal
              : actionItem.rangeStartVal
            : fieldItem.attrVal,
          minVal: actionItem.rangeStartVal,
          maxVal: actionItem.rangeEndVal,
          isApplyRule: true,
        };
      }
      if (["DL"].includes(actionItem.fieldTypeCode)) {
        const multiValueLength = actionItem.multipleValues.length;
        let value = fieldItem.attrVal;

        if (multiValueLength >= 1) {
          value = actionItem.multipleValues.includes(value)
            ? value
            : actionItem.multipleValues[0];
        } else if (
          !multiValueLength ||
          !actionItem.multipleValues.includes(fieldItem.attrVal)
        ) {
          value = "";
        }

        return {
          ...fieldItem,
          attrVal: value,
          disabled: multiValueLength <= 1,
          multipleValues: actionItem.multipleValues,
          showOptionNull: multiValueLength <= 1,
          isApplyRule: true,
        };
      }
      if (["DM"].includes(actionItem.fieldTypeCode)) {
        const multiValueLength = actionItem.multipleValues.length;
        const isRequired =
          multiValueLength > 1 ? RequiredYn.Yes : RequiredYn.No;
        const isValidValue = every(fieldItem.attrVal, (item) =>
          includes(actionItem.multipleValues, item)
        );
        const isMatchingValue = isEqual(
          fieldItem.attrVal,
          actionItem.multipleValues
        );
        const isNotValidValue = every(fieldItem.attrVal, (item) => {
          const notInclude = !includes(actionItem.multipleValues, item);
          if (notInclude) return false;
          return includes(actionItem.multipleValues, item);
        });
        return {
          ...fieldItem,
          requiredYn:
            fieldItem.requiredYn === RequiredYn.No
              ? isRequired
              : fieldItem.requiredYn,
          isApplyRequired: isRequired,
          attrVal:
            multiValueLength === 1
              ? [actionItem.multipleValues[0]]
              : multiValueLength === 0
                ? []
                : (isValidValue || isMatchingValue) && isNotValidValue
                  ? fieldItem.attrVal
                  : cloneDeep(actionItem.multipleValues),
          multipleValues: actionItem.multipleValues,
          disabled: multiValueLength <= 1,
          showOptionNull: multiValueLength <= 1,
          isApplyRule: true,
        };
      }
    }
    return { ...fieldItem, isApplyRule: false };
  });

  return listResult;
};

const resetActions = (listActions: any, additionalData: any) => {
  return listActions.map((fieldItem) => {
    const currentItem = additionalData.find(
      (item: any) => item?.attrUuid === fieldItem?.attrUuid
    );
    return currentItem
      ? {
          ...currentItem,
          requiredYn:
            currentItem.isApplyRequired === RequiredYn.Yes
              ? RequiredYn.No
              : currentItem.requiredYn,
          clearable: true,
          disabled: false,
          minDate: "",
          maxDate: "",
          minVal: "",
          maxVal: "",
          multipleValues: [],
          showOptionNull: currentItem.requiredYn === RequiredYn.No,
        }
      : fieldItem;
  });
};

export const useCustomValidation = () => {
  const updateRules = (item, listRulesOriginal, baseData) => {
    const currentId = item.attrUuid;
    // Flat list rules from api
    const listRules = listRulesOriginal
      .filter((rule) => rule.datas.some((data) => data.attrUuid === currentId))
      .map((rule) =>
        rule.datas.map((attr) => ({ ...attr, ruleSeqNo: rule.seqNo }))
      )
      .flat();

    // Get rules matching with condition
    const conditions = getRulesMatchingWithCondition(item, listRules);
    // Apply rules to addtional field
    let result = updateActionToField(
      conditions,
      listRules,
      baseData,
      updateRules,
      listRulesOriginal
    );
    // reset additional field if not matching the rules
    const resetList = resetFieldNotMatchingCondition(
      conditions,
      listRules,
      currentId,
      baseData,
      listRulesOriginal,
      updateRules
    );
    // merge reset and apply list
    if (result.length > 0) {
      result = result.map((item) => {
        const resetItem = resetList.find(
          (reset) => reset.attrUuid === item.attrUuid
        );
        return item.isApplyRule ? item : resetItem || item;
      });
    } else {
      result = resetList;
    }
    return result;
  };

  const getRuleByAdditionalFields = (
    additionalData: any,
    listRulesOriginal: any
  ) => {
    const listRules = listRulesOriginal.map((rule) => rule.datas).flat();
    const result = additionalData
      .filter((item) => {
        return listRules.find((rule: any) => rule?.attrUuid === item?.attrUuid);
      })
      .map((item) => ({
        ...item,
        types: checkAttrUuid(item.attrUuid, listRules),
      }));
    return result;
  };

  const checkAttrUuid = (attrUuid: string, listRules: any[]) => {
    const listTypes: string[] = [];
    listRules.forEach((item) => {
      if (item.attrUuid === attrUuid) {
        listTypes.push(item.condType);
      }
    });
    return listTypes;
  };

  const updateAdditionalField = (baseData, rules, additionalInfo = {}) => {
    const generalList = baseData.general.map((item) => {
      const ruleItem = rules.find((rule) => rule.attrUuid === item.attrUuid);
      return ruleItem ? { ...ruleItem, ...additionalInfo } : item;
    });
    const additionalList = baseData.additional.map((item) => {
      const ruleItem = rules.find((rule) => rule.attrUuid === item.attrUuid);
      return ruleItem ? { ...ruleItem, ...additionalInfo } : item;
    });
    return { generalList, additionalList };
  };

  const applyRules = (oldValue, newValue, listRulesOriginal, baseData) => {
    if (
      oldValue &&
      newValue &&
      Array.isArray(oldValue.general) &&
      Array.isArray(newValue.general)
    ) {
      const newAdditional = [
        ...newValue.additional,
        ...newValue.general?.filter((item) => item?.dispTab === "G"),
      ];
      const oldAdditional = [
        ...oldValue.additional,
        ...oldValue.general?.filter((item) => item?.dispTab === "G"),
      ];
      const listItemChanged = newAdditional.filter((newItem) =>
        oldAdditional.find(
          (oldItem) =>
            oldItem.attrUuid == newItem.attrUuid &&
            JSON.stringify(oldItem.attrVal) !== JSON.stringify(newItem.attrVal)
        )
      );

      let additional = baseData.additional;
      let general = baseData.general;

      if (listItemChanged.length > 0 && checkValidDFSCycle(listRulesOriginal)) {
        listItemChanged.forEach((item) => {
          const listRules = updateRules(item, listRulesOriginal, newAdditional);
          const generalList = general.map((item) => {
            const rule = listRules.find(
              ({ attrUuid }) => attrUuid === item.attrUuid
            );
            return rule ? rule : item;
          });
          const additionalList = additional.map((item) => {
            const rule = listRules.find(
              ({ attrUuid }) => attrUuid === item.attrUuid
            );
            return rule ? rule : item;
          });
          additional = additionalList;
          general = generalList;
        });

        return { generalList: general, additionalList: additional };
      }
    }

    return {
      generalList: baseData?.general,
      additionalList: baseData?.additional,
    };
  };

  const applyRulesInit = (listItem, listRulesOriginal, baseData) => {
    const listResult: any = [];
    if (checkValidDFSCycle(listRulesOriginal)) {
      const listItemString = listItem.map((item) =>
        JSON.stringify({ ...item, isApplyRule: false })
      );
      listItem.forEach((item) => {
        const listRules = updateRules(item, listRulesOriginal, listItem);
        const filtered = listRules.filter(
          (rule) => !listItemString.includes(JSON.stringify(rule))
        );
        listResult.push(filtered);
      });
    }
    return updateAdditionalField(baseData, listResult.flat());
  };

  const checkValidDFSCycle = (listRulesOriginal) => {
    const listRules: Array<string[]> = [];
    listRulesOriginal.forEach((rule) => {
      const conditions = rule.datas.filter((item) => item.condType === "C");
      const actions = rule.datas.filter((item) => item.condType === "A");
      conditions.forEach((cond) => {
        actions.forEach((act) => {
          listRules.push([cond.attrUuid, act.attrUuid]);
        });
      });
    });
    return !DFSDetectCycle(listRules);
  };

  return {
    updateRules,
    getRuleByAdditionalFields,
    updateAdditionalField,
    applyRules,
    applyRulesInit,
  };
};
