package document_manager.service;
/*
    Created by KhaiTT
    Time: 11:17 7/6/2022
*/

import document_manager.dto.ResponseObject;
import document_manager.dto.ResponsePageObject;
import document_manager.dto.UsersRequest;
import document_manager.entity.Users;
import document_manager.exception.UserNotFoundException;

public interface IUsersService  {
    ResponsePageObject<Users> findAll(String name, Long pageNumber, Long elementNumber);

    Users findByName(String name) throws UserNotFoundException;

    ResponseObject<Users> save(UsersRequest usersRequest);

    Users findById(Long id)  throws UserNotFoundException;

    ResponseObject<Users> findUserById(Long id) throws UserNotFoundException;

    ResponseObject<Users> delete(Long id) throws UserNotFoundException;

    ResponseObject<Users> update(UsersRequest usersRequest) throws UserNotFoundException;

    ResponsePageObject<Users> findAllByFullTextSearch(String name, Long pageNumber, Long elementNumber);
}
