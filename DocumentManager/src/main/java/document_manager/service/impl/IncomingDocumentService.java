package document_manager.service.impl;
/*
    Created by KhaiTT
    Time: 17:01 7/6/2022
*/

import document_manager.dto.IncomingDocumentRequest;
import document_manager.dto.IncomingDocumentResponse;
import document_manager.dto.ResponseObject;
import document_manager.dto.ResponsePageObject;
import document_manager.entity.IncomingDocument;
import document_manager.entity.Rotation;
import document_manager.exception.IncomingDocumentNotFoundException;
import document_manager.repository.IIncomingDocumentRepository;
import document_manager.repository.IRotationRepository;
import document_manager.service.IIncomingDocumentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class IncomingDocumentService implements IIncomingDocumentService {
    @Autowired
    private IIncomingDocumentRepository iIncomingDocumentRepository;

    @Autowired
    private IRotationRepository iRotationRepository;

    @Override
    @Cacheable(value = "incomingDocumentList")
    public ResponsePageObject<IncomingDocument> findAll(String excerpt, String serialNumber, String fromDate, String toDate, String signerName, Long pageNumber, Long elementNumber) {
        List<IncomingDocument> incomingDocumentList = iIncomingDocumentRepository.findAll(excerpt, serialNumber, fromDate, toDate, signerName, pageNumber, elementNumber);
        long totalRecord = iIncomingDocumentRepository.totalRecordNumber(excerpt, serialNumber, fromDate, toDate, signerName);
        return new ResponsePageObject<>(true, "Lấy danh sách văn bản đến thành công.", incomingDocumentList, pageNumber, totalRecord, elementNumber);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "incomingDocumentList", allEntries = true),
            @CacheEvict(value = "incomingDocumentDtoList", allEntries = true),
    })
    public ResponseObject<IncomingDocument> save(IncomingDocumentRequest incomingDocumentRequest) {
        IncomingDocument incomingDocument = new IncomingDocument();
        BeanUtils.copyProperties(incomingDocumentRequest, incomingDocument);
        incomingDocument.setSigningDate(new Date());
        iIncomingDocumentRepository.save(incomingDocument);
        Long newIncomingDocumentId = iIncomingDocumentRepository.findBySerialNumber(incomingDocument.getSerialNumber()).getId();
        Rotation rotation = new Rotation(0, newIncomingDocumentId, incomingDocumentRequest.getSignerId(), incomingDocumentRequest.getSignerId(), new Date(), false);
        iRotationRepository.save(rotation);
        return new ResponseObject<>(true, "Tạo văn bản thành công.");
    }

    @Override
    public IncomingDocument findBySerialNumber(String serialNumber) throws IncomingDocumentNotFoundException {
        IncomingDocument incomingDocument = iIncomingDocumentRepository.findBySerialNumber(serialNumber);
        if (incomingDocument != null) {
            return incomingDocument;
        } else {
            throw new IncomingDocumentNotFoundException("Văn bản đến không tồn tại.");
        }
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "incomingDocumentList", allEntries = true),
            @CacheEvict(value = "incomingDocumentDtoList", allEntries = true),
            @CacheEvict(value = "incomingDocumentId", key = "#id", allEntries = true)})
    public ResponseObject<IncomingDocument> deleteById(Long id) throws IncomingDocumentNotFoundException {
        IncomingDocument incomingDocument = iIncomingDocumentRepository.findById(id);
        if (incomingDocument != null) {
            iIncomingDocumentRepository.delete(id);
        } else {
            throw new IncomingDocumentNotFoundException("Không tìm thấy văn bản đến cần xóa trong hệ thống.");
        }
        return new ResponseObject<>(true, "Xóa văn bản thành công.");
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "incomingDocumentList", allEntries = true),
            @CacheEvict(value = "incomingDocumentDtoList", allEntries = true),
            @CacheEvict(value = "incomingDocumentId", key = "#incomingDocumentRequest.id", allEntries = true)
    })
    public ResponseObject<IncomingDocument> update(IncomingDocumentRequest incomingDocumentRequest) throws IncomingDocumentNotFoundException {
        IncomingDocument incomingDocument = iIncomingDocumentRepository.findById(incomingDocumentRequest.getId());
        if (incomingDocument != null) {
            IncomingDocument incomingDocument1 = new IncomingDocument(incomingDocumentRequest.getId(), incomingDocumentRequest.getExcerpt(), incomingDocumentRequest.getSerialNumber(), incomingDocumentRequest.getSigningDate(), incomingDocumentRequest.getSignerId(), incomingDocumentRequest.isDelete());
            iIncomingDocumentRepository.update(incomingDocument1);
        } else {
            throw new IncomingDocumentNotFoundException("Không tìm thấy văn bản đến trong hệ thống.");
        }
        return new ResponseObject<>(true, "Cập nhật thông tin văn bản thành công.");
    }

    @Override
    public IncomingDocument findById(Long id) throws IncomingDocumentNotFoundException {
        IncomingDocument incomingDocument = iIncomingDocumentRepository.findById(id);
        if (incomingDocument != null) {
            return incomingDocument;
        } else {
            throw new IncomingDocumentNotFoundException("Văn bản đến không tồn tại.");
        }
    }

    @Override
    @Cacheable(value = "incomingDocumentId", key = "#id")
    public ResponseObject<IncomingDocument> findDocumentById(Long id) throws IncomingDocumentNotFoundException {
        List<IncomingDocument> incomingDocumentList = new ArrayList<>();
        incomingDocumentList.add(this.findById(id));
        return new ResponseObject<>(true, "Tìm thấy văn bản đến thành công.", incomingDocumentList);
    }

    @Override
    @Cacheable(value = "incomingDocumentDtoList")
    public ResponsePageObject<IncomingDocumentResponse> findAllDocumentDto(String excerpt, String serialNumber, String fromDate, String toDate, String signerName, Long pageNumber, Long elementNumber) {
        List<IncomingDocumentResponse> incomingDocumentList = iIncomingDocumentRepository.findAllDocumentDto(excerpt, serialNumber, fromDate, toDate, signerName, pageNumber, elementNumber);
        long totalRecord = iIncomingDocumentRepository.totalRecordNumber(excerpt, serialNumber, fromDate, toDate, signerName);
        return new ResponsePageObject<>(true, "Lấy danh sách văn bản đến thành công.", incomingDocumentList, pageNumber, totalRecord, elementNumber);
    }

    @Override
    public ResponsePageObject<IncomingDocumentResponse> findAllDocumentDtoWithFullTextSearch(String searchParam, Long pageNumber, Long elementNumber) {
        List<IncomingDocumentResponse> incomingDocumentList = iIncomingDocumentRepository.findAllDocumentDtoWithFullTextSearch(searchParam, pageNumber, elementNumber);
        long totalRecord = iIncomingDocumentRepository.totalRecordNumberWithFullTextSearch(searchParam);
        return new ResponsePageObject<>(true, "Lấy danh sách văn bản đến thành công.", incomingDocumentList, pageNumber, totalRecord, elementNumber);
    }
}
