package com.greenData.bank.specification.comparisonClasses;

import com.greenData.bank.specification.enums.Operator;

public class IntegerComparison {
    public static boolean comparisonIntegerValues(Operator operator, Integer firstInteger, Integer secondInteger) {
        boolean comparisonResult;
        System.out.println("equal integer " + firstInteger + " " + secondInteger);
        switch (operator) {
            case EQUAL:
                comparisonResult = firstInteger.equals(secondInteger);
                break;
            case NOT_EQUAL:
                comparisonResult = !firstInteger.equals(secondInteger);
                break;
            case LARGE:
                comparisonResult = firstInteger > secondInteger;
                break;
            case LESS:
                comparisonResult = firstInteger < secondInteger;
                break;
            case LARGE_OR_EQUAL:
                comparisonResult = firstInteger >= secondInteger;
                break;
            case LESS_OR_EQUAL:
                comparisonResult = firstInteger <= secondInteger;
                break;
            default:
                comparisonResult = false;
                break;
        }
        return comparisonResult;
    }
}
