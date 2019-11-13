package com.greenData.bank.specification.comparisonClasses;

import com.greenData.bank.models.enums.LegalForms;
import com.greenData.bank.specification.enums.Operator;

public class LegalFormComparison {
    protected static boolean comparisonLegalFormValues(Operator operator, LegalForms firstLegalForm, LegalForms secondLegalForm) {
        boolean comparisonResult;
        switch (operator) {
            case EQUAL:
                comparisonResult = firstLegalForm == secondLegalForm;
                break;
            case NOT_EQUAL:
                comparisonResult = firstLegalForm != secondLegalForm;
                break;
            default:
                comparisonResult = false;
                break;
        }
        return comparisonResult;
    }
}
