package org.pod4u.serialisation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.pod4u.audio.APIAudioAnalysis;
import org.pod4u.audio.AudioAnalysis;
import org.pod4u.audio.MBAudioAnalysis;
import tools.jackson.core.StreamReadFeature;
import tools.jackson.core.json.JsonReadFeature;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.json.JsonMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AudioDeserialiser {

    public AudioDeserialiser() {

    }

    public MBAudioAnalysis[] deserializeMBAudio(String[] input, String ids) {
        MBAudioAnalysis[] array = new MBAudioAnalysis[25];
        try {
            JsonMapper mapper = JsonMapper.builder().disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES).disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).changeDefaultVisibility(vc -> vc.withFieldVisibility(JsonAutoDetect.Visibility.ANY)).build().rebuild().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false).configure(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION, true).configure(JsonReadFeature.ALLOW_TRAILING_COMMA, true).build();
            for (String item : input)
                array[ids.indexOf(item.substring(1, 36)) / 37] = mapper.readValue(mapper.createParser("{\"danceable\":" + item.split("\"danceable\":")[1].split(",")[0] + ",\"instrumental\":" + item.split("\"instrumental\":")[1].split(",")[0] + ",\"ambient\":" + item.split("\"ambient\":")[1].split(",")[0] + ",\"spe\":" + item.split("\"spe\":")[1].split("}")[0] + ",\"bright\":" + item.split("\"bright\":")[1].split(",")[0] + ",\"tonal\":" + item.split("\"tonal\":")[1].split("}")[0] + ",\"acoustic\":" + item.split("\"acoustic\":")[1].split(",")[0] + ",\"happy\":" + item.split("\"happy\":")[1].split(",")[0] + ",\"aggressive\":" + item.split("\"aggressive\":")[1].split(",")[0] + ",\"replay_gain\":" + item.split("\"replay_gain\":")[1].replace("}", "").split(",")[0] + ",\"musicbrainz_recordingid\":\"" + item.split("\"musicbrainz_recordingid\":\\[\"")[1].split("\"")[0] + "\"}"), MBAudioAnalysis.class);
        } catch(Exception e) {
            IO.println(e);
        }
        return array;
    }

    public List<APIAudioAnalysis> deserializeAPIAudio(String[] input) {
        List<APIAudioAnalysis> list = new ArrayList<>();
        try {
            JsonMapper mapper = JsonMapper.builder().disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES).disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).changeDefaultVisibility(vc -> vc.withFieldVisibility(JsonAutoDetect.Visibility.ANY)).build().rebuild().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false).configure(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION, true).configure(JsonReadFeature.ALLOW_TRAILING_COMMA, true).build();
            for (String item : input)
                list.add(mapper.readValue(mapper.createParser(item.replace("{{", "{").replace("}}", "}")), APIAudioAnalysis.class));
        } catch (Exception e) {
            IO.println(e);
        }
        return list;
    }
}
