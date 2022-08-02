package document_manager.repository;
/*
    Created by KhaiTT
    Time: 10:52 7/6/2022
*/

public interface IGeneralRepository<T> {
    T findById(Long id);

    void save(T t);

    void delete(Long id);

    void update(T t);
}
