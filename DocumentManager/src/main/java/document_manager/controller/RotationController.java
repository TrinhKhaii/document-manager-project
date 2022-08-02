package document_manager.controller;

import document_manager.dto.*;
import document_manager.entity.Rotation;
import document_manager.exception.RotationNotFoundException;
import document_manager.exception.WrongFormatException;
import document_manager.service.IRotationService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.util.Map;

/*
    Created by KhaiTT
    Time: 10:53 7/7/2022
*/
@RestController
@RequestMapping(value = "/api/rotation")
@CrossOrigin
public class RotationController {
    @Autowired
    private IRotationService iRotationService;

    @PostMapping(value = "/list")
    public ResponseEntity<ResponsePageObject<Rotation>> getRotationList(@Valid @RequestBody RotationSearchParam searchParam) {
        return new ResponseEntity<>(iRotationService.findAll(searchParam.getExcerpt(),
                                                            searchParam.getSenderName(),
                                                            searchParam.getReceiverName(),
                                                            searchParam.getFromDate(),
                                                            searchParam.getToDate(),
                                                            searchParam.getPageNumber(),
                                                            searchParam.getElementNumber()),
                                                            HttpStatus.OK);
    }

    @PostMapping(value = "/list-dto")
    public ResponseEntity<ResponsePageObject<RotationResponse>> getRotationDtoList(@Valid @RequestBody RotationSearchParam searchParam) {
        return new ResponseEntity<>(iRotationService.findAllRotationDto(searchParam.getExcerpt(),
                searchParam.getSenderName(),
                searchParam.getReceiverName(),
                searchParam.getFromDate(),
                searchParam.getToDate(),
                searchParam.getPageNumber(),
                searchParam.getElementNumber()),
                HttpStatus.OK);
    }

    @PostMapping(value = "/save")
    public ResponseEntity<ResponseObject<Rotation>> saveNewRotation(@Valid @RequestBody RotationRequest rotationRequest) {
        return new ResponseEntity<>(iRotationService.save(rotationRequest), HttpStatus.OK);
    }

    @PostMapping(value = "/find-by-id")
    public ResponseEntity<ResponseObject<Rotation>> findById(@RequestBody Map<String, Long> rotationId) throws RotationNotFoundException {
        return new ResponseEntity<>(iRotationService.findRotationById(rotationId.get("id")), HttpStatus.OK);
    }

    @PostMapping(value = "/delete")
    public ResponseEntity<ResponseObject<Rotation>> deleteRotation(@RequestBody Map<String, Long> deleteId) throws RotationNotFoundException {
        return new ResponseEntity<>(iRotationService.delete(deleteId.get("id")), HttpStatus.OK);
    }

    @PostMapping(value = "/update")
    public ResponseEntity<ResponseObject<Rotation>> updateRotation(@Valid @RequestBody RotationRequest rotationRequest) throws RotationNotFoundException {
        return new ResponseEntity<>(iRotationService.update(rotationRequest), HttpStatus.OK);
    }

    @PostMapping(value = "/rotation-list-of-document")
    public ResponseEntity<ResponseObject<RotationListOfDocumentResponse>> getRotationListOfDocument(@RequestBody Map<String, Long> documentId) {
        return new ResponseEntity<>(iRotationService.findAllRotationListOfDocumentById(documentId.get("id")), HttpStatus.OK);
    }

    @GetMapping(value = "/document-amount-received-list")
    public ResponseEntity<ResponseObject<DocumentAmountReceivedResponse>> getDocumentAmountReceivedList() {
        return new ResponseEntity<>(iRotationService.findAllDocumentAmountReceived(), HttpStatus.OK);
    }

    @PostMapping(value = "/save-multi")
    public ResponseEntity<ResponseObject<Rotation>> saveMulti(@Valid @RequestBody RotationMultiRequest rotationMultiRequest) {
        return new ResponseEntity<>(iRotationService.saveMulti(rotationMultiRequest), HttpStatus.OK);
    }

    @GetMapping(value = "/document_amount_receiver_list-report", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getDocumentAmountReceivedListReport() throws JRException, FileNotFoundException, WrongFormatException {
        return new ResponseEntity<>(iRotationService.exportDocumentAmountReceived(), HttpStatus.OK);
    }

    @PostMapping(value = "/rotation-list-of-document-report")
    public ResponseEntity<ResponseObject<RotationListOfDocumentResponse>> getRotationListOfDocumentReport(@RequestBody Map<String, String> documentId) throws JRException, FileNotFoundException, WrongFormatException {
        return new ResponseEntity<>(iRotationService.exportRotationListOfDocumentById(Long.valueOf(documentId.get("id")), documentId.get("format")), HttpStatus.OK);
    }

    @PostMapping(value = "/list-dto-fts")
    public ResponseEntity<ResponsePageObject<RotationResponse>> getRotationDtoListWithFullTextSearch(@Valid @RequestBody FullTextSearchRequest fullTextSearchRequest) {
        return new ResponseEntity<>(iRotationService.findAllRotationDtoWithFullTextSearch(fullTextSearchRequest.getSearchParam(),
                fullTextSearchRequest.getPageNumber(),
                fullTextSearchRequest.getElementNumber()),
                HttpStatus.OK);
    }
}
