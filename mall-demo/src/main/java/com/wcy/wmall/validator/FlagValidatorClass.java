package com.wcy.wmall.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @ClassName FlagValidatorClass
 * @Description 状态标记校验器
 * @Author wcy
 * @Date 2019-07-29 16:31
 * @Version 1.0
 **/
public class FlagValidatorClass implements ConstraintValidator<FlagValidator,Integer> {
    private String[] values;

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = false;
        for(int i = 0; i<values.length; i++ ){
            if(values[i].equals(String.valueOf(value))){
                isValid = true;
                break;
            }
        }
        return isValid;
    }

    @Override
    public void initialize(FlagValidator flagValidator) {
        this.values = flagValidator.value();
    }
}
