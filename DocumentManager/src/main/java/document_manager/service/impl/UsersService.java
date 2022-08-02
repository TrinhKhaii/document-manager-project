package document_manager.service.impl;
/*
    Created by KhaiTT
    Time: 11:17 7/6/2022
*/

import document_manager.dto.ResponseObject;
import document_manager.dto.ResponsePageObject;
import document_manager.dto.UsersRequest;
import document_manager.entity.Users;
import document_manager.exception.UserNotFoundException;
import document_manager.repository.IUsersRepository;
import document_manager.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UsersService implements IUsersService {
    @Lazy
    @Autowired
    private IUsersRepository iUsersRepository;

    @Override
    @Cacheable(value = "userList")
    public ResponsePageObject<Users> findAll(String name, Long pageNumber, Long elementNumber) {
        List<Users> usersList = iUsersRepository.findAll(name, pageNumber, elementNumber);
        long totalRecord = iUsersRepository.totalRecordNumber(name);
        return new ResponsePageObject<>(true, "Lấy danh sách người dùng thành công.", usersList, pageNumber, totalRecord, elementNumber);
    }

    @Override
    public Users findByName(String name) throws UserNotFoundException {
        Users user = iUsersRepository.findByName(name);
        if (user != null) {
            return user;
        } else {
            throw new UserNotFoundException("Không tìm thấy người dùng trong hệ thống.");
        }
    }

    @Override
    public Users findById(Long id) throws UserNotFoundException {
        Users user = iUsersRepository.findById(id);
        if (user != null) {
            return user;
        } else {
            throw new UserNotFoundException("Không tìm thấy người dùng trong hệ thống.");
        }
    }

    @Override
    @Cacheable(value = "userId", key = "#id")
    public ResponseObject<Users> findUserById(Long id) throws UserNotFoundException {
        List<Users> usersList = new ArrayList<>();
        usersList.add(this.findById(id));
        return new ResponseObject<>(true, "Tìm thấy người dùng thành công.", usersList);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value="userList", allEntries=true)
    })
    public ResponseObject<Users> save(UsersRequest usersRequest) {
        Users users = new Users(0, usersRequest.getUserName(), usersRequest.isDelete());
        iUsersRepository.save(users);
        return new ResponseObject<>(true, "Thêm mới người dùng thành công.");
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value="userList", allEntries=true),
            @CacheEvict(value="userId",  key = "#id", allEntries=true) })
    public ResponseObject<Users> delete(Long id) throws UserNotFoundException {
        Users user = iUsersRepository.findById(id);
        if (user != null) {
            iUsersRepository.delete(id);
        } else {
            throw new UserNotFoundException("Không tìm thấy người dùng trong hệ thống.");
        }
        return new ResponseObject<>(true, "Xóa người dùng thành công.");
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value="userList", allEntries=true),
            @CacheEvict(value="userId", key = "#usersRequest.id", allEntries=true) })
    public ResponseObject<Users> update(UsersRequest usersRequest) throws UserNotFoundException {
        Users user = iUsersRepository.findById(usersRequest.getId());
        if (user != null) {
            Users users = new Users(usersRequest.getId(), usersRequest.getUserName(), usersRequest.isDelete());
            iUsersRepository.update(users);
        } else {
            throw new UserNotFoundException("Không tìm thấy người dùng trong hệ thống.");
        }
        return new ResponseObject<>(true, "Cập nhật thông tin người dùng thành công.");
    }

    @Override
    public ResponsePageObject<Users> findAllByFullTextSearch(String name, Long pageNumber, Long elementNumber) {
        List<Users> usersList = iUsersRepository.findAllByFullTextSearch(name, pageNumber, elementNumber);
        long totalRecord = iUsersRepository.totalRecordNumberOfFullTextSearch(name);
        return new ResponsePageObject<>(true, "Lấy danh sách người dùng thành công.", usersList, pageNumber, totalRecord, elementNumber);
    }
}
