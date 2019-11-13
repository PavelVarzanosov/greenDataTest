package com.greenData.bank.specification.comparisonClasses;

import com.greenData.bank.specification.enums.Operator;

public class StringComparison {


    public static boolean comparisonStringValues(Operator operator, Object firstObject, Object secondObject) {
        String firstString  = (String) firstObject;
        String secondString = (String) secondObject;
        boolean comparisonResult;
        switch (operator) {
            case EQUAL:
                comparisonResult = firstString.equals(secondString);
                break;
            case NOT_EQUAL:
                comparisonResult = !firstString.equals(secondString);
                break;
            case LARGE:
                comparisonResult = firstString.compareTo(secondString) > 0;
                break;
            case LESS:
                comparisonResult = firstString.compareTo(secondString) < 0;
                break;
            default:
                comparisonResult = false;
                break;
        }
        return comparisonResult;
    }
}
