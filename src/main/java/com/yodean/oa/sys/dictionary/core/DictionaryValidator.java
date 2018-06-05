package com.yodean.oa.sys.dictionary.core;

import com.yodean.oa.common.config.Global;
import com.yodean.oa.sys.dictionary.entity.Word;
import com.yodean.oa.sys.dictionary.service.DictionaryService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * Created by rick on 5/17/18.
 */
public class DictionaryValidator implements ConstraintValidator<DictionaryConstraint, Word> {
    private DictionaryConstraint constraintAnnotation;


    @Override
    public void initialize(DictionaryConstraint constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(Word word, ConstraintValidatorContext constraintValidatorContext) {
        if(Objects.isNull(word)) return true;

        String category = constraintAnnotation.name();

        if (Objects.nonNull(category) && !Objects.equals(category, word.getCategory())) return false;

        DictionaryService dictionaryService = Global.applicationContext.getBean(DictionaryService.class);

        Word _word = dictionaryService.findByCategoryAndName(category, word.getName());

        return Objects.nonNull(_word);
    }
}
