package com.yodean.oa.sys.dictionary.core;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.yodean.oa.sys.dictionary.entity.Word;

import java.io.IOException;

/**
 * Created by rick on 6/4/18.
 */
public class WordDeserializer extends JsonDeserializer<Word> {

    @Override
    public Word deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        return DictionaryUtils.string2Word(node.asText());
    }
}
