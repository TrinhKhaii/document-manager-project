package document_manager.repository;
/*
    Created by KhaiTT
    Time: 10:50 7/6/2022
*/

import document_manager.entity.Users;

import java.util.List;

public interface IUsersRepository extends IGeneralRepository<Users>{
    List<Users> findAll(String name, Long pageNumber, Long elementNumber);
    Users findByName(String name);
    long totalRecordNumber(String name);

    List<Users> findAllByFullTextSearch(String name, Long pageNumber, Long elementNumber);

    long totalRecordNumberOfFullTextSearch(String name);
}
