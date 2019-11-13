package com.greenData.bank.specification.comparisonClasses;

import com.greenData.bank.specification.enums.Operator;

public class ObjectComparison {
    protected static boolean comparisonObjectValues(Operator operator, Object firstObject, Object secondObject) {
        boolean comparisonResult;
        switch (operator) {
            case EQUAL:
                comparisonResult = firstObject.equals(secondObject);
                break;
            case NOT_EQUAL:
                comparisonResult = !firstObject.equals(secondObject);
                break;
            default:
                comparisonResult = false;
                break;
        }
        return comparisonResult;
    }
}
