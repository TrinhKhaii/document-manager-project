package document_manager.annotation;
/*
    Created by KhaiTT
    Time: 08:16 7/7/2022
*/

import document_manager.entity.Users;
import document_manager.exception.UserNotFoundException;
import document_manager.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserIdConstaintValidator implements ConstraintValidator<UserId, Long> {
    @Lazy
    @Autowired
    private IUsersService iUsersService;

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext constraintValidatorContext) {
        Users user = null;
        try {
            user = iUsersService.findById(id);
        } catch (UserNotFoundException e) {
            return false;
        }
        return true;
    }
}
