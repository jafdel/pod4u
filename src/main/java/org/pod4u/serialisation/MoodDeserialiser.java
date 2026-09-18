package org.pod4u.serialisation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.pod4u.mood.Mood;
import tools.jackson.core.StreamReadFeature;
import tools.jackson.core.json.JsonReadFeature;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.json.JsonMapper;

import java.util.ArrayList;

public class MoodDeserialiser {

    public MoodDeserialiser() {

    }

    public Mood deserialiseMood(String input) {
        try {
            JsonMapper mapper = JsonMapper.builder().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).changeDefaultVisibility(vc -> vc.withFieldVisibility(JsonAutoDetect.Visibility.ANY)).build().rebuild().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false).configure(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION, true).configure(JsonReadFeature.ALLOW_TRAILING_COMMA, true).build();
            String[] faces = input.split("faces\":\\[\\{")[1].split("\"attributes\":\\{")[1].split("],\"image_id\"")[0].replace("}}},", "}}}").split("}}}");
            String face = "{" + faces[0].replace("\"emotion\":{", "").replace("},\"beauty", ",\"beauty").replace("\"mouthstatus\":{", "").replace("},\"eyegaze", ",\"eyegaze").replace("\"skinstatus\":{", "").replace("},\"glass\":{\"value\"", ",\"glass\"").replace("\"smile\":{\"value\"", "\"smile\"").replace("},\"headpose\":{", ",").replace("},\"eyestatus", ",\"eyestatus") + "}";
            return mapper.readValue(face, Mood.class);
        } catch(Exception e) {
            IO.println(e);
            return null;
        }
    }
}
