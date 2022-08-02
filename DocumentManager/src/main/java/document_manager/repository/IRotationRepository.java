package document_manager.repository;
/*
    Created by KhaiTT
    Time: 10:54 7/7/2022
*/

import document_manager.dto.*;
import document_manager.entity.Rotation;
import java.util.List;

public interface IRotationRepository extends IGeneralRepository<Rotation>{
    List<Rotation> findAll(String excerpt, String senderName, String receiverName, String fromDate, String toDate, Long pageNumber, Long elementNumber);

    List<RotationListOfDocumentResponse> findAllRotationListOfDocumentById(Long id);

    List<DocumentAmountReceivedResponse> getDocumentAmountReceivedList();

    long totalRecordNumber(String excerpt, String senderName, String receiverName, String fromDate, String toDate);

    void saveMulti(RotationMultiRequest rotationMultiRequest);

    List<RotationResponse> findAllRotationDto(String excerpt, String senderName, String receiverName, String fromDate, String toDate, Long pageNumber, Long elementNumber);

    List<RotationResponse> findAllRotationDtoWithFullTextSearch(String searchParam, Long pageNumber, Long elementNumber);

    long totalRecordNumberWithFullTextSearch(String searchParam);
}
