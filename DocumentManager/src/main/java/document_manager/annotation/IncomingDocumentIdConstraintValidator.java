package document_manager.annotation;
/*
    Created by KhaiTT
    Time: 10:47 7/7/2022
*/

import document_manager.entity.IncomingDocument;
import document_manager.exception.IncomingDocumentNotFoundException;
import document_manager.service.IIncomingDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IncomingDocumentIdConstraintValidator implements ConstraintValidator<IncomingDocumentId, Long> {
    @Autowired
    @Lazy
    private IIncomingDocumentService iIncomingDocumentService;

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext constraintValidatorContext) {
        IncomingDocument incomingDocument = null;
        try {
            incomingDocument = iIncomingDocumentService.findById(id);
        } catch (IncomingDocumentNotFoundException e) {
            return false;
        }
        return true;
    }
}
