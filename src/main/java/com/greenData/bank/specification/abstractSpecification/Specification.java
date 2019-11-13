package com.greenData.bank.specification.abstractSpecification;

import com.greenData.bank.specification.enums.ConcatenateType;

public interface Specification<T> {

    boolean isSatisfiedBy(T t) throws Exception;

    Specification<T> concatenate(ConcatenateType type, final Specification specification);

    Specification<T> and(Specification<T> specification);

    Specification<T> or(Specification<T> specification);
}
