package document_manager.service;
/*
    Created by KhaiTT
    Time: 17:00 7/6/2022
*/

import document_manager.dto.IncomingDocumentRequest;
import document_manager.dto.IncomingDocumentResponse;
import document_manager.dto.ResponseObject;
import document_manager.dto.ResponsePageObject;
import document_manager.entity.IncomingDocument;
import document_manager.exception.IncomingDocumentNotFoundException;

public interface IIncomingDocumentService {
    ResponsePageObject<IncomingDocument> findAll(String excerpt, String serialNumber, String fromDate, String toDate, String signerName, Long pageNumber, Long elementNumber);

    ResponseObject<IncomingDocument> save(IncomingDocumentRequest incomingDocumentRequest);

    IncomingDocument findBySerialNumber(String serialNumber) throws IncomingDocumentNotFoundException;

    ResponseObject<IncomingDocument> deleteById(Long id) throws IncomingDocumentNotFoundException;

    ResponseObject<IncomingDocument> update(IncomingDocumentRequest incomingDocumentRequest) throws IncomingDocumentNotFoundException;

    IncomingDocument findById(Long id) throws IncomingDocumentNotFoundException;

    ResponseObject<IncomingDocument> findDocumentById(Long id) throws IncomingDocumentNotFoundException;

    ResponsePageObject<IncomingDocumentResponse> findAllDocumentDto(String excerpt, String serialNumber, String fromDate, String toDate, String signerName, Long pageNumber, Long elementNumber);

    ResponsePageObject<IncomingDocumentResponse> findAllDocumentDtoWithFullTextSearch(String searchParam, Long pageNumber, Long elementNumber);
}
