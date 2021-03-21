package com.mok.infracore.service.impl;

import com.mok.infracore.domain.User;
import com.mok.infracore.domain.pagination.PaginationCriteria;
import com.mok.infracore.repository.UserRepository;
import com.mok.infracore.repository.specification.GenericSpecification;
import com.mok.infracore.repository.specification.SearchCriteria;
import com.mok.infracore.repository.specification.SearchFactory;
import com.mok.infracore.repository.specification.SearchOperation;
import com.mok.infracore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.mok.infracore.util.AppUtil.isNumeric;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<User> findAll(PaginationCriteria paginationCriteria, int pageNumber) {

        SearchFactory searchFactory = new SearchFactory();
        paginationCriteria.setIndex(pageNumber);
        Pageable pageable = searchFactory.getPageable(paginationCriteria);
        //generic specification work with or you can extend with or/and operator
        GenericSpecification<User> userSpecification = fillSearchFilter(paginationCriteria);
        return userRepository.findAll(userSpecification, pageable);
    }

    /**
     * we user jquery data table search input text and we not user a form for search and must search only one value
     * each times, you can use a form for search with all fields of user or nay another entity in a real example
     * and change below method by your approach
     */
    private GenericSpecification<User> fillSearchFilter(PaginationCriteria paginationCriteria) {
        GenericSpecification<User> userSpecification = null;
        if (paginationCriteria.getFilterBy().getMapOfFilters().size() > 0) {
            userSpecification = new GenericSpecification<>();
            String value = paginationCriteria.getFilterBy().getMapOfFilters().entrySet().iterator().next().getValue();
            if (isNumeric(value)) {
                userSpecification.add(new SearchCriteria("id", Double.parseDouble(value), SearchOperation.EQUAL));
                userSpecification.add(new SearchCriteria("age", Double.parseDouble(value), SearchOperation.EQUAL));
            } else {
                userSpecification.add(new SearchCriteria("firstname", value, SearchOperation.MATCH));
                userSpecification.add(new SearchCriteria("lastname", value, SearchOperation.MATCH));
            }

        }
        return userSpecification;
    }


}
