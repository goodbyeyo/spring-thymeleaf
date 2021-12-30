package spring.itemproject.validation;

import org.junit.jupiter.api.Test;
import spring.itemproject.domain.item.Item;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class BeanValidationTest {

    @Test
    void beanValidation() {
        /* 검증기 생성 */
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Item item = new Item();
        item.setItemName(" ");
        item.setPrice(0);
        item.setQuantity(10000000);

        /* 검증 실행 */
        Set<ConstraintViolation<Item>> validations = validator.validate(item);
        
        for (ConstraintViolation<Item> validation : validations) {
            System.out.println("validation = " + validation);
            System.out.println("validation = " + validation.getMessage());
        }
    }
}
