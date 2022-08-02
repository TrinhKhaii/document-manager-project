package document_manager.controller;

import document_manager.dto.*;
import document_manager.entity.Users;
import document_manager.exception.UserNotFoundException;
import document_manager.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Map;

/*
    Created by KhaiTT
    Time: 11:19 7/6/2022
*/
@RestController
@RequestMapping(value = "/api/users")
@CrossOrigin
public class UsersController {
    @Lazy
    @Autowired
    private IUsersService iUsersService;

    @PostMapping(value = "/list")
    public ResponseEntity<ResponsePageObject<Users>> getUserList(@Valid @RequestBody UserSearchParam searchParam) {
        return new ResponseEntity<>(iUsersService.findAll(searchParam.getName(),
                                                        searchParam.getPageNumber(),
                                                        searchParam.getElementNumber()),
                                                        HttpStatus.OK);
    }

    @PostMapping(value = "/save")
    public ResponseEntity<ResponseObject<Users>> saveNewUsers(@Valid @RequestBody UsersRequest usersRequest) {
        return new ResponseEntity<>(iUsersService.save(usersRequest), HttpStatus.OK);
    }

    @PostMapping(value = "/find-by-id")
    public ResponseEntity<ResponseObject<Users>> findUserById(@RequestBody Map<String, Long> userId) throws UserNotFoundException {
        return new ResponseEntity<>(iUsersService.findUserById(userId.get("id")), HttpStatus.OK);
    }

    @PostMapping(value = "/update")
    public ResponseEntity<ResponseObject<Users>> updateUser(@Valid @RequestBody UsersRequest usersRequest) throws UserNotFoundException {
        return new ResponseEntity<>(iUsersService.update(usersRequest), HttpStatus.OK);
    }

    @PostMapping(value = "/delete")
    public ResponseEntity<ResponseObject<Users>> deleteUserById(@RequestBody Map<String, Long> deleteId) throws UserNotFoundException {
        return new ResponseEntity<>(iUsersService.delete(deleteId.get("id")), HttpStatus.OK);
    }

    @PostMapping(value = "/list-fts")
    public ResponseEntity<ResponsePageObject<Users>> getUserListByFullTextSearch(@Valid @RequestBody FullTextSearchRequest fullTextSearchRequest) {
        return new ResponseEntity<>(iUsersService.findAllByFullTextSearch(fullTextSearchRequest.getSearchParam(),
                fullTextSearchRequest.getPageNumber(),
                fullTextSearchRequest.getElementNumber()),
                HttpStatus.OK);
    }
}
