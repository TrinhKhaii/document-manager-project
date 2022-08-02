package document_manager.repository;
/*
    Created by KhaiTT
    Time: 16:51 7/6/2022
*/

import document_manager.dto.IncomingDocumentResponse;
import document_manager.entity.IncomingDocument;
import java.util.List;

public interface IIncomingDocumentRepository extends IGeneralRepository<IncomingDocument> {
    List<IncomingDocument> findAll(String excerpt, String serialNumber, String fromDate, String toDate, String signerName, Long pageNumber, Long elementNumber);

    IncomingDocument findBySerialNumber(String excerpt);

    long totalRecordNumber(String excerpt, String serialNumber, String fromDate, String toDate, String signerName);

    List<IncomingDocumentResponse> findAllDocumentDto(String excerpt, String serialNumber, String fromDate, String toDate, String signerName, Long pageNumber, Long elementNumber);

    List<IncomingDocumentResponse> findAllDocumentDtoWithFullTextSearch(String searchParam, Long pageNumber, Long elementNumber);

    long totalRecordNumberWithFullTextSearch(String searchParam);
}
