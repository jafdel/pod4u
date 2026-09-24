package org.pod4u.serialisation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.pod4u.audio.Track;

import tools.jackson.core.StreamReadFeature;
import tools.jackson.databind.*;
import tools.jackson.databind.json.JsonMapper;

import java.util.ArrayList;

public class TrackDeserialiser {
    public TrackDeserialiser() {

    }

    public static ArrayList<Track> deserialiseTracks(String[] input) {
        ArrayList<Track> trackList = new ArrayList<>();
        try {
            JsonMapper mapper = JsonMapper.builder().disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES).disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).changeDefaultVisibility(vc -> vc.withFieldVisibility(JsonAutoDetect.Visibility.ANY)).build().rebuild().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false).configure(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION, true).build();
            String json = "";
            for (int i=1; i<input.length; i++) {
                json = input[i].split(input[i].substring(input[i].lastIndexOf("}}")))[0] + "}";
                trackList.add(mapper.readValue(json, Track.class));
            }
            //json = input[input.length-1].substring(0, input[input.length-1].lastIndexOf("}"));
            //trackList.add(mapper.readValue(json, Track.class));
        } catch(Exception e) {
            IO.println(e);
        }
        return trackList;
    }
}
