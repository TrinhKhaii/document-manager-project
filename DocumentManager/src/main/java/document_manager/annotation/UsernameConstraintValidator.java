package document_manager.annotation;
/*
    Created by KhaiTT
    Time: 15:16 7/6/2022
*/

import document_manager.entity.Users;
import document_manager.exception.UserNotFoundException;
import document_manager.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameConstraintValidator implements ConstraintValidator<Username, String> {
    @Lazy
    @Autowired
    private IUsersService iUsersService;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        Users user = null;
        try {
            user = iUsersService.findByName(s);
        } catch (UserNotFoundException e) {
            return true;
        }
        return false;
    }
}
