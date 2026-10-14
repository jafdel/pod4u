package org.pod4u.account;

import javafx.concurrent.Task;
import org.pod4u.audio.Track;
import org.pod4u.serialisation.TrackDeserialiser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SpotifyAccount {
    private static String username;
    private static List<Track> trackList = new ArrayList<>();

    private static class FXAsyncUtil {
        private static final ExecutorService executor = Executors.newCachedThreadPool();

        public static CompletableFuture<ArrayList<Track>> runAsync(Task<ArrayList<Track>> task) {
            CompletableFuture<ArrayList<Track>> future = new CompletableFuture<>();
            task.setOnSucceeded(e -> future.complete(task.getValue()));
            task.setOnFailed(e -> future.completeExceptionally(task.getException()));
            executor.submit(task);
            return future;
        }

        public static void shutdown() {
            executor.shutdown();
        }
    }

    public static void setUsername(String displayName) {
        username = displayName.split("display_name\":\"")[1].split("\",")[0];
    }

    public static void addToTrackList(String input) {
        try {
            /*Task<ArrayList<Track>> task = new Task<>() {
                @Override
                protected ArrayList<Track> call() throws Exception {*/
                    ArrayList<Track> tracks = TrackDeserialiser.deserialiseTracks(input.split("\"items\":\\[")[1].split("],\"limit\":")[0].replaceAll(",\\{\"album\":", "{\"album\":").replaceAll(",\\{\"album\":", "{\"album\":").replaceAll("\\{\"added_at\":\"", "").replace("type\":\"track\",\"uri", "type\":\"track\",\"track_").replace("\"uri\"", "\"u\"").replace("track_", "uri").split(",\"track\":"));
                /*}
            };
            FXAsyncUtil.runAsync(task).thenAccept(result -> trackList.addAll(result));*/
            trackList.addAll(tracks);
            IO.println(trackList.size());
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
