package document_manager.service.impl;
/*
    Created by KhaiTT
    Time: 11:03 7/7/2022
*/

import document_manager.dto.*;
import document_manager.entity.Rotation;
import document_manager.exception.RotationNotFoundException;
import document_manager.exception.WrongFormatException;
import document_manager.repository.IRotationRepository;
import document_manager.service.IRotationService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@Service
public class RotationService implements IRotationService {
    @Autowired
    @Lazy
    private IRotationRepository iRotationRepository;

    @Override
    @Cacheable(value = "rotationList")
    public ResponsePageObject<Rotation> findAll(String excerpt, String senderName, String receiverName, String toDate, String fromDate, Long pageNumber, Long elementNumber) {
        List<Rotation> rotationList = iRotationRepository.findAll(excerpt, senderName, receiverName, toDate, fromDate, pageNumber, elementNumber);
        long totalRecord = iRotationRepository.totalRecordNumber(excerpt, senderName, receiverName, toDate, fromDate);
        return new ResponsePageObject<>(true, "Lấy danh sách luân chuyển thành công.", rotationList, pageNumber, totalRecord, elementNumber);
    }

    @Override
    @Cacheable(value = "rotationDtoList")
    public ResponsePageObject<RotationResponse> findAllRotationDto(String excerpt, String senderName, String receiverName, String toDate, String fromDate, Long pageNumber, Long elementNumber) {
        List<RotationResponse> rotationResponses = iRotationRepository.findAllRotationDto(excerpt, senderName, receiverName, toDate, fromDate, pageNumber, elementNumber);
        long totalRecord = iRotationRepository.totalRecordNumber(excerpt, senderName, receiverName, toDate, fromDate);
        return new ResponsePageObject<>(true, "Lấy danh sách luân chuyển thành công.", rotationResponses,  pageNumber, totalRecord, elementNumber);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "rotationList", allEntries = true),
            @CacheEvict(value = "rotationDtoList", allEntries = true),
    })
    public ResponseObject<Rotation> save(RotationRequest rotationRequest) {
        Rotation rotation = new Rotation();
        BeanUtils.copyProperties(rotationRequest, rotation);
        rotation.setDeliveryDate(new Date());
        iRotationRepository.save(rotation);
        return new ResponseObject<>(true, "Thêm mới luân chuyển thành công.");
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "rotationList", allEntries = true),
            @CacheEvict(value = "rotationDtoList", allEntries = true),
            @CacheEvict(value = "rotationId", key = "#id", allEntries = true)})
    public ResponseObject<Rotation> delete(Long id) throws RotationNotFoundException {
        Rotation rotation = iRotationRepository.findById(id);
        if (rotation != null) {
            iRotationRepository.delete(id);
        } else {
            throw new RotationNotFoundException("Không tìm thấy thông tin luân chuyển trong hệ thống.");
        }
        return new ResponseObject<>(true, "Xóa luân chuyển thành công.");
    }

    @Override
    public Rotation findById(Long id) throws RotationNotFoundException {
        Rotation rotation = iRotationRepository.findById(id);
        if (rotation != null) {
            return rotation;
        } else {
            throw new RotationNotFoundException("Không tìm thấy thông tin luân chuyển trong hệ thống.");
        }
    }

    @Override
    @Cacheable(value = "rotationId", key = "#id")
    public ResponseObject<Rotation> findRotationById(Long id) throws RotationNotFoundException {
        List<Rotation> rotationList = new ArrayList<>();
        rotationList.add(this.findById(id));
        return new ResponseObject<>(true, "Tìm thấy luân chuyển thành công.", rotationList);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "rotationList", allEntries = true),
            @CacheEvict(value = "rotationDtoList", allEntries = true),
            @CacheEvict(value = "rotationId", key = "#rotationRequest.id", allEntries = true)
    })
    public ResponseObject<Rotation> update(RotationRequest rotationRequest) throws RotationNotFoundException {
        Rotation rotation = iRotationRepository.findById(rotationRequest.getId());
        if (rotation != null) {
            Rotation rotation1 = new Rotation(rotationRequest.getId(), rotationRequest.getIncomingDocumentId(), rotationRequest.getSenderId(), rotationRequest.getReceiverId(), rotationRequest.getDeliveryDate(), rotationRequest.isDelete());
            iRotationRepository.update(rotation1);
        } else {
            throw new RotationNotFoundException("Không tìm thấy thông tin luân chuyển trong hệ thống.");
        }
        return new ResponseObject<>(true, "Cập nhật thông tin luân chuyển thành công.");
    }

    @Override
    public ResponseObject<RotationListOfDocumentResponse> findAllRotationListOfDocumentById(Long id) {
        List<RotationListOfDocumentResponse> rotationListOfDocumentList = iRotationRepository.findAllRotationListOfDocumentById(id);
        return new ResponseObject<>(true, "Danh sách luân chuyển của văn bản với id: " + id, rotationListOfDocumentList);
    }

    @Override
    public ResponseObject<DocumentAmountReceivedResponse> findAllDocumentAmountReceived() {
        List<DocumentAmountReceivedResponse> documentAmountReceivedList = iRotationRepository.getDocumentAmountReceivedList();
        return new ResponseObject<>(true, "Thông kê số lượng văn bản nhận dc theo người.", documentAmountReceivedList);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "rotationList", allEntries = true),
            @CacheEvict(value = "rotationDtoList", allEntries = true),
    })
    public ResponseObject<Rotation> saveMulti(RotationMultiRequest rotationMultiRequest) {
        rotationMultiRequest.setDeliveryDate(new Date());
        iRotationRepository.saveMulti(rotationMultiRequest);
        return new ResponseObject<>(true, "Thêm mới thành công");
    }

    @Override
    public byte[] exportDocumentAmountReceived() throws FileNotFoundException, JRException {
        List<DocumentAmountReceivedResponse> documentAmountReceivedList = iRotationRepository.getDocumentAmountReceivedList();
        File file = ResourceUtils.getFile("classpath:document_amount_receiver_list.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(documentAmountReceivedList);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "KhaiTT");
        return JasperExportManager.exportReportToPdf(JasperFillManager.fillReport(jasperReport, parameters, dataSource));
    }

    @Override
    public ResponseObject<RotationListOfDocumentResponse> exportRotationListOfDocumentById(Long id, String format) throws FileNotFoundException, JRException, WrongFormatException {
        String path = "C:\\Users\\khaitt.UNITEK\\Downloads";
        List<RotationListOfDocumentResponse> rotationListOfDocumentList = iRotationRepository.findAllRotationListOfDocumentById(id);
        if (rotationListOfDocumentList.isEmpty()) {
            return new ResponseObject<>(false, "Không tìm thấy thông tin luân chuyển của văn bản.");
        }
        File file = ResourceUtils.getFile("classpath:rotation_list_of_document.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(rotationListOfDocumentList);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "KhaiTT");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        if (format.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\rotation_list_of_document_report.html");
        } else if (format.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\rotation_list_of_document_report.pdf");
        } else {
            throw new WrongFormatException("Hệ thống chỉ hỗ trợ xuất định dạng PDF hoặc HTML.");
        }
        return new ResponseObject<>(true, "Thống kê danh sách luân chuyển của văn bản đã được tải xuống ở đường dẫn: " + path);
    }

    @Override
    public ResponsePageObject<RotationResponse> findAllRotationDtoWithFullTextSearch(String searchParam, Long pageNumber, Long elementNumber) {
        List<RotationResponse> rotationList = iRotationRepository.findAllRotationDtoWithFullTextSearch(searchParam, pageNumber, elementNumber);
        long totalRecord = iRotationRepository.totalRecordNumberWithFullTextSearch(searchParam);
        return new ResponsePageObject<>(true, "Lấy danh sách luân chuyển thành công.", rotationList, pageNumber, totalRecord, elementNumber);
    }
}
