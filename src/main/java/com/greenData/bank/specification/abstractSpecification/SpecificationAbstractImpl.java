package com.greenData.bank.specification.abstractSpecification;

import com.greenData.bank.specification.comparisonClasses.CommonComparison;
import com.greenData.bank.specification.enums.Operator;

import java.lang.reflect.Field;

public class SpecificationAbstractImpl<T> extends AbstractSpecification<T> {

    private String fieldName;
    protected Operator operator;
    private String receivedValue;
    private Field field;
    private Class<?> fieldType;
    private Object goalValueInClass;

    public SpecificationAbstractImpl(String fieldName, Operator operator, String receivedValue) {
        this.fieldName = fieldName;
        this.operator = operator;
        this.receivedValue = receivedValue;
    }

    public boolean isSatisfiedBy(final T t) throws Exception {
        getFieldFromString(t);
        getFieldType();
        getFieldValue(t);
        castReceivedValue();
        return comparisonValues();
    }

    private void getFieldFromString(T t) throws NoSuchFieldException {
        Class<?> clazz = t.getClass();
        field = clazz.getDeclaredField(fieldName);
    }

    private void getFieldType() {
        fieldType = field.getType();
    }

    private void getFieldValue(T t) throws Exception {
        field.setAccessible(true);
        goalValueInClass = field.get(t);
    }

    private void castReceivedValue() {
    }

    private boolean comparisonValues() throws Exception {
        return CommonComparison.comparisonValues(fieldType, operator, receivedValue, goalValueInClass);
    }
}
