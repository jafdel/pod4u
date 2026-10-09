package org.pod4u.serialisation;

import org.pod4u.audio.Track;

import tools.jackson.core.StreamReadFeature;
import tools.jackson.databind.*;
import tools.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.ArrayList;

public class TrackDeserialiser {
    private static ArrayList<Track> trackList;

    public TrackDeserialiser() {

    }

    public static ArrayList<Track> deserialiseTracks(String[] input) throws InterruptedException {
        ArrayList<Track> trackList = new ArrayList<>();
        JsonMapper mapper = JsonMapper.builder().disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES).disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).changeDefaultVisibility(vc -> vc.withFieldVisibility(JsonAutoDetect.Visibility.ANY)).build().rebuild().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false).configure(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION, true).build();
        String json = "";
        IO.println("Length: " + input.length);
        for (int i = 1; i < input.length; i++) {
            json = input[i].split(input[i].substring(input[i].lastIndexOf("}}")))[0] + "}";
            trackList.add(mapper.readValue(json, Track.class));
        }
        IO.println("Size: " + trackList.size());
        return trackList;
        //new Thread(task).start();
        //IO.println("Tracks: " + trackList.size());
        //new Thread(buildMapper).start();
        //return TrackDeserialiser.trackList;
    }

    public static ArrayList<Track> getTrackList() {
        return TrackDeserialiser.trackList;
    }
}
