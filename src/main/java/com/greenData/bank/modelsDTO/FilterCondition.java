package com.greenData.bank.modelsDTO;

import com.greenData.bank.specification.enums.ConcatenateType;
import com.greenData.bank.specification.enums.Operator;

import java.io.Serializable;

public class FilterCondition implements Serializable {
    private String fieldName;
    private String valueCondition;
    private Operator operator;
    private ConcatenateType concatenateType;

    public FilterCondition(String fieldName, String valueCondition, Operator operator, ConcatenateType concatenateType){
        this.fieldName = fieldName;
        this.valueCondition = valueCondition;
        this.operator = operator;
        this.concatenateType = concatenateType;
    }

    public String getValueCondition() {
        return valueCondition;
    }

    public ConcatenateType getConcatenateType() {
        return concatenateType;
    }

    public Operator getOperator() {
        return operator;
    }

    public String getFieldName() {
        return fieldName;
    }
}
