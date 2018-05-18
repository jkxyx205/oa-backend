package com.yodean.oa.sys.dictionary.core;

import com.yodean.oa.sys.dictionary.entity.Word;
import org.springframework.util.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;
import java.util.Objects;

/**
 * Created by rick on 5/17/18.
 */
@Convert
public class WordJpaConverter implements AttributeConverter<Word, String> {

    private static final String PARAM_SEPARATOR = "#";

    @Override
    public String convertToDatabaseColumn(Word word) {
        if (Objects.isNull(word)) return "";

        StringBuilder sb = new StringBuilder();
        return sb.append(word.getCategory()).append(PARAM_SEPARATOR).append(word.getName()).toString();
    }

    @Override
    public Word convertToEntityAttribute(String s) {
        if (StringUtils.hasText(s)) {
            String[] values = s.split(PARAM_SEPARATOR);

            Word word = new Word();
            word.setCategory(values[0]);
            word.setName(values[1]);
            return word;

        }
        return null;
    }
}
