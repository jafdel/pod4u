package org.pod4u.account;

import org.pod4u.audio.Track;
import org.pod4u.serialisation.TrackDeserialiser;

import java.util.ArrayList;
import java.util.List;

public class SpotifyAccount {
    private static String username;
    private static List<Track> trackList = new ArrayList<>();

    public static void setUsername(String displayName) {
        username = displayName.split("display_name\":\"")[1].split("\",")[0];
    }

    public static void addToTrackList(String input) {
        try {
            ArrayList<Track> tracks = TrackDeserialiser.deserialiseTracks(input.split("\"items\":\\[")[1].split("],\"limit\":")[0].replaceAll(",\\{\"album\":", "{\"album\":").replaceAll(",\\{\"album\":", "{\"album\":").replaceAll("\\{\"added_at\":\"", "").replace("type\":\"track\",\"uri", "type\":\"track\",\"track_").replace("\"uri\"", "\"u\"").replace("track_", "uri").split(",\"track\":"));
            for (Track track : tracks)
                trackList.add(track);
        } catch(Exception e) {
            IO.println(e);
        }
    }

    public static List<Track> getSavedTracks() {
        return trackList;
    }

    public static int getTrackListSize() {
        return trackList.size();
    }
}
