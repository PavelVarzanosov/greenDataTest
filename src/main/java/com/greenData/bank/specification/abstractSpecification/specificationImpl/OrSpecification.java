package com.greenData.bank.specification.abstractSpecification.specificationImpl;

import com.greenData.bank.specification.abstractSpecification.AbstractSpecification;
import com.greenData.bank.specification.abstractSpecification.Specification;

public class OrSpecification<T> extends AbstractSpecification<T> {

    private Specification<T> specLeft;
    private Specification<T> specRight;

    public OrSpecification(final Specification<T> specLeft, final Specification<T> specRight) {
        this.specLeft = specLeft;
        this.specRight = specRight;
    }

    public boolean isSatisfiedBy(final T t) throws Exception {
        return specLeft.isSatisfiedBy(t) || specRight.isSatisfiedBy(t);
    }
}