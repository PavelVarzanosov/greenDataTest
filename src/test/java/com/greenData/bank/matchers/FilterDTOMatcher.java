package com.greenData.bank.matchers;

import com.greenData.bank.modelsDTO.FilterDTO;
import org.mockito.ArgumentMatcher;

public class FilterDTOMatcher  implements ArgumentMatcher<FilterDTO> {

    private FilterDTO left;

    public FilterDTOMatcher(FilterDTO filterDTO) {
        left = filterDTO;
    }

    @Override
    public boolean matches(FilterDTO right) {
        return left.getIsReverse() == (right.getIsReverse()) &&
                left.getFilterConditionList().size() == right.getFilterConditionList().size() &&
                left.getOffset() == (right.getOffset())&&
                left.getLimit() == (right.getLimit());
    }
}