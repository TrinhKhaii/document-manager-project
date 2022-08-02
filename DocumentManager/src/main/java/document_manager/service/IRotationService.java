package document_manager.service;
/*
    Created by KhaiTT
    Time: 11:02 7/7/2022
*/

import document_manager.dto.*;
import document_manager.entity.Rotation;
import document_manager.exception.RotationNotFoundException;
import document_manager.exception.WrongFormatException;
import net.sf.jasperreports.engine.JRException;
import java.io.FileNotFoundException;

public interface IRotationService {
    ResponsePageObject<Rotation> findAll(String excerpt, String senderName, String receiverName, String toDate, String fromDate, Long pageNumber, Long elementNumber);

    ResponsePageObject<RotationResponse> findAllRotationDto(String excerpt, String senderName, String receiverName, String toDate, String fromDate, Long pageNumber, Long elementNumber);

    ResponseObject<Rotation> save(RotationRequest rotationRequest);

    ResponseObject<Rotation> delete(Long id) throws RotationNotFoundException;

    Rotation findById(Long id) throws RotationNotFoundException;

    ResponseObject<Rotation> findRotationById(Long id) throws RotationNotFoundException;

    ResponseObject<Rotation> update(RotationRequest rotationRequest) throws RotationNotFoundException;

    ResponseObject<RotationListOfDocumentResponse> findAllRotationListOfDocumentById(Long id);

    ResponseObject<DocumentAmountReceivedResponse> findAllDocumentAmountReceived();

    ResponseObject<Rotation> saveMulti(RotationMultiRequest rotationMultiRequest);

    byte[] exportDocumentAmountReceived() throws FileNotFoundException, JRException, WrongFormatException;

    ResponseObject<RotationListOfDocumentResponse> exportRotationListOfDocumentById(Long id, String format) throws FileNotFoundException, JRException, WrongFormatException;

    ResponsePageObject<RotationResponse> findAllRotationDtoWithFullTextSearch(String searchParam, Long pageNumber, Long elementNumber);

}
