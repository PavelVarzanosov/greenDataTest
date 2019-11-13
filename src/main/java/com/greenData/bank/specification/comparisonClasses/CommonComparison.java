package com.greenData.bank.specification.comparisonClasses;

import com.greenData.bank.models.enums.LegalForms;
import com.greenData.bank.specification.enums.Operator;

public class CommonComparison {
    public static boolean comparisonValues(Class<?> fieldType, Operator operator, Object firstObject, Object secondObject) throws Exception {
        if(fieldType.getSimpleName().equals("String")) {
            String firstArgument = (String) firstObject;
            String secondArgument = (String) secondObject;
            return StringComparison.comparisonStringValues(operator, firstArgument, secondArgument);
        } else if(fieldType.getSimpleName().equals("LegalForms")) {
            LegalForms firstLegalForm = LegalForms.valueOf(firstObject.toString());
            LegalForms secondLegalForm = LegalForms.valueOf(secondObject.toString());
            return LegalFormComparison.comparisonLegalFormValues(operator, firstLegalForm, secondLegalForm);
        } else if(fieldType.getSimpleName().equals("int")) {
            Integer firstArgument = new Integer(firstObject.toString());
            Integer secondArgument = new Integer(secondObject.toString());
            return IntegerComparison.comparisonIntegerValues(operator, firstArgument, secondArgument);
        } else if(fieldType.getSimpleName().equals("Object")) {
            return ObjectComparison.comparisonObjectValues(operator, firstObject, secondObject);
        } else {
            throw new Exception("Comparison class not found");
        }
    }
}
