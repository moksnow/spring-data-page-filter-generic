package com.mok.infracore.web;

import com.google.gson.Gson;
import com.mok.infracore.domain.User;
import com.mok.infracore.domain.pagination.DataTableRequest;
import com.mok.infracore.domain.pagination.DataTableResults;
import com.mok.infracore.domain.pagination.PaginationCriteria;
import com.mok.infracore.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/", "/datatable"})
    public String welcome(Map<String, Object> model) {
        return "datatable";
    }

    @GetMapping(value = "/select_all_users")
    @ResponseBody
    public String selectAllUsers(HttpServletRequest request) {

        DataTableRequest<User> dataTableInRQ = new DataTableRequest<>(request);

        PaginationCriteria pagination = dataTableInRQ.getPaginationRequest();

        Page<User> allPagination = userService.findAll(pagination, (dataTableInRQ.getStart()) / dataTableInRQ.getLength());

        DataTableResults<User> dataTableResult = mapPageToResponse(dataTableInRQ, allPagination);

        return new Gson().toJson(dataTableResult);
    }

    private DataTableResults<User> mapPageToResponse(DataTableRequest<User> dataTableInRQ, Page<User> allPagination) {
        DataTableResults<User> dataTableResult = new DataTableResults<>();
        dataTableResult.setDraw(dataTableInRQ.getDraw());
        dataTableResult.setListOfDataObjects(allPagination.getContent());
        dataTableResult.setRecordsFiltered(String.valueOf(allPagination.getTotalElements()));
        dataTableResult.setRecordsTotal(String.valueOf(allPagination.getTotalElements()));
        return dataTableResult;
    }
}
