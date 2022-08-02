package document_manager.annotation;
/*
    Created by KhaiTT
    Time: 08:43 7/7/2022
*/


import document_manager.entity.IncomingDocument;
import document_manager.exception.IncomingDocumentNotFoundException;
import document_manager.service.IIncomingDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SerialNumberConstraintValidator implements ConstraintValidator<SerialNumber, String> {
    @Autowired
    @Lazy
    private IIncomingDocumentService iIncomingDocumentService;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        IncomingDocument incomingDocument = null;
        try {
            incomingDocument = iIncomingDocumentService.findBySerialNumber(s);
        } catch (IncomingDocumentNotFoundException ex) {
            return true;
        }
        return false;
    }
}
