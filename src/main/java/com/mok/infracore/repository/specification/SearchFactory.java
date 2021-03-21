package com.mok.infracore.repository.specification;

import com.mok.infracore.domain.pagination.PaginationCriteria;
import com.mok.infracore.domain.pagination.SortBy;
import com.mok.infracore.domain.pagination.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author M_Khandan
 * Date: 3/15/2021
 * Time: 11:28 AM
 */
public class SearchFactory {

    public Pageable getPageable(PaginationCriteria paginationCriteria) {
        SortBy sorts = paginationCriteria.getSortBy();
        List<Sort.Order> sortList = new ArrayList<>();

        for (Map.Entry<String, SortOrder> entry : sorts.getSortBys().entrySet()) {
            String sortMethod = entry.getValue().value();
            String keySorterField = entry.getKey();

            if (sortMethod.equals(SortOrder.ASC.value())) {
                sortList.add(Sort.Order.asc(keySorterField));
            } else {
                sortList.add(Sort.Order.desc(keySorterField));
            }

        }

        return PageRequest.of(paginationCriteria.getIndex(), paginationCriteria.getPageSize(), Sort.by(sortList));

    }
}
