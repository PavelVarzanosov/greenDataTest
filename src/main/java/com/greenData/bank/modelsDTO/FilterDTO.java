package com.greenData.bank.modelsDTO;

import java.io.Serializable;
import java.util.List;

public class FilterDTO implements Serializable {
    private List<FilterCondition> filterConditionList;
    private boolean isReverse;
    private int offset;
    private int limit;

    public FilterDTO () {
    }

    public FilterDTO (List<FilterCondition> filterConditionList, boolean isReverse, int offset, int limit) {
        this.filterConditionList = filterConditionList;
        this.isReverse = isReverse;
        this.offset = offset;
        this.limit = limit;
    }

    public List<FilterCondition> getFilterConditionList() {
        return filterConditionList;
    }

    public boolean getIsReverse() {
        return isReverse;
    }

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }
}
