package document_manager.controller;

import document_manager.dto.*;
import document_manager.entity.IncomingDocument;
import document_manager.exception.IncomingDocumentNotFoundException;
import document_manager.service.IIncomingDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Map;

/*
    Created by KhaiTT
    Time: 17:04 7/6/2022
*/
@RestController
@RequestMapping(value = "/api/incoming-document")
@CrossOrigin
public class IncomingDocumentController {
    @Lazy
    @Autowired
    private IIncomingDocumentService iIncomingDocumentService;

    @PostMapping(value = "/list")
    public ResponseEntity<ResponsePageObject<IncomingDocument>> getIncomingDocumentList(@Valid @RequestBody IncomingDocumentSearchParam searchParam) {
        return new ResponseEntity<>(iIncomingDocumentService.findAll(searchParam.getExcerpt(),
                                                                    searchParam.getSerialNumber(),
                                                                    searchParam.getFromDate(),
                                                                    searchParam.getToDate(),
                                                                    searchParam.getSignerName(),
                                                                    searchParam.getPageNumber(),
                                                                    searchParam.getElementNumber()),
                                                                    HttpStatus.OK);
    }

    @PostMapping(value = "/save")
    public ResponseEntity<ResponseObject<IncomingDocument>> saveIncomingDocument(@Valid @RequestBody IncomingDocumentRequest incomingDocumentRequest) {
        return new ResponseEntity<>(iIncomingDocumentService.save(incomingDocumentRequest), HttpStatus.OK);
    }

    @PostMapping(value = "/delete")
    public ResponseEntity<ResponseObject<IncomingDocument>> deleteIncomingDocument(@RequestBody Map<String, Long> deleteId) throws IncomingDocumentNotFoundException {
        return new ResponseEntity<>(iIncomingDocumentService.deleteById(deleteId.get("id")), HttpStatus.OK);
    }

    @PostMapping(value = "/update")
    public ResponseEntity<ResponseObject<IncomingDocument>> updateIncomingDocument(@Valid @RequestBody IncomingDocumentRequest incomingDocumentRequest) throws IncomingDocumentNotFoundException {
        return new ResponseEntity<>(iIncomingDocumentService.update(incomingDocumentRequest), HttpStatus.OK);
    }

    @PostMapping(value = "/find-by-id")
    public ResponseEntity<ResponseObject<IncomingDocument>> findDocumentById(@RequestBody Map<String, Long> documentId)  throws IncomingDocumentNotFoundException {
        return new ResponseEntity<>(iIncomingDocumentService.findDocumentById(documentId.get("id")), HttpStatus.OK);
    }

    @PostMapping(value = "/list-dto")
    public ResponseEntity<ResponsePageObject<IncomingDocumentResponse>> getIncomingDocumentDtoList(@Valid @RequestBody IncomingDocumentSearchParam searchParam) {
        return new ResponseEntity<>(iIncomingDocumentService.findAllDocumentDto(searchParam.getExcerpt(),
                searchParam.getSerialNumber(),
                searchParam.getFromDate(),
                searchParam.getToDate(),
                searchParam.getSignerName(),
                searchParam.getPageNumber(),
                searchParam.getElementNumber()),
                HttpStatus.OK);
    }

    @PostMapping(value = "/list-dto-fts")
    public ResponseEntity<ResponsePageObject<IncomingDocumentResponse>> getIncomingDocumentDtoListWithFullTextSearch(@Valid @RequestBody FullTextSearchRequest fullTextSearchRequest) {
        return new ResponseEntity<>(iIncomingDocumentService.findAllDocumentDtoWithFullTextSearch(fullTextSearchRequest.getSearchParam(),
                fullTextSearchRequest.getPageNumber(),
                fullTextSearchRequest.getElementNumber()),HttpStatus.OK);
    }
}
