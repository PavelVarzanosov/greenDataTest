package com.greenData.bank.specification.abstractSpecification;

import com.greenData.bank.specification.abstractSpecification.specificationImpl.AndSpecification;
import com.greenData.bank.specification.abstractSpecification.specificationImpl.OrSpecification;
import com.greenData.bank.specification.enums.ConcatenateType;

public abstract class AbstractSpecification<T> implements Specification<T> {

    public abstract boolean isSatisfiedBy(T t) throws Exception;

    public Specification<T> concatenate(ConcatenateType type, final Specification specification){
        Specification<T> resultSpecification = null;
        switch (type) {
            case AND:
                resultSpecification = and(specification);
                break;
            case OR:
                resultSpecification = or(specification);
                break;
            default:
                break;
        }
        return resultSpecification;
    }

    public Specification<T> and(final Specification specification) {
        return new AndSpecification<T>(this, specification);
    }

    public Specification<T> or(final Specification<T> specification) {
        return new OrSpecification<>(this, specification);
    }

}
