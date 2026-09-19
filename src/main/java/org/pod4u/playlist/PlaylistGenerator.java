package org.pod4u.playlist;

import org.pod4u.audio.Audio;
import org.pod4u.audio.MBAudioAnalysis;
import org.pod4u.audio.APIAudioAnalysis;
import org.pod4u.audio.Track;
import org.pod4u.mood.Mood;
import org.pod4u.serialisation.AudioDeserialiser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.sql.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Float.NaN;

public class PlaylistGenerator {
    private List<Audio> sortedAudio = new ArrayList<>();
    //private AudioAnalysis[] trackData;
    private Playlist playlist;

    public PlaylistGenerator(List<Track> savedTracks, AudioDeserialiser serialiser, Mood mood, String accessToken) throws SQLException {
        List<Audio> savedAudio = new ArrayList<Audio>(), otherAudio = new ArrayList<Audio>();
        String[] mainIds = new String[25], otherIds = new String[(savedTracks.size()/50)];
        try {
            HttpClient client = HttpClient.newHttpClient();
            //SpotifyApi api = new SpotifyApi.Builder().setAccessToken(accessToken).build();
            int index = 0;
            for (Track savedTrack : savedTracks) {
                String isrc = savedTrack.getIsrc();
                HttpRequest preRequest = HttpRequest.newBuilder().uri(new URI("http://localhost:3000/isrc?select=recording%28gid%29&isrc=eq." + isrc)).header("Content-Type", "application/json").header("Accept", "application/json").GET().build();
                HttpResponse<String> preResponse = client.send(preRequest, HttpResponse.BodyHandlers.ofString());
                String mbid = preResponse.body();
                if (!isrc.startsWith("USIR") && !mbid.replace("[", "").replace("]", "").isBlank() && !mbid.contains("null")) {
                    savedAudio.add(new Audio(savedTrack, mbid.split("gid\": \"")[1].split("\"")[0]));
                    /*String sql = "SELECT r.gid FROM isrc i JOIN recording r ON i.recording = r.id WHERE i.isrc=?";
                    Class.forName("org.newsclub.net.unix.AFUNIXSocketFactory$FactoryArg");
                    Connection conn = MBDatabaseConnection.getConnection();
                    var stmt = conn.prepareStatement(sql);
                    stmt.setObject(1, isrc);
                    var rs = stmt.executeQuery();
                    if (rs.next())
                        mainIds[savedAudio.size()-1] = rs.getString("gid");*/
                    mainIds[savedAudio.size()-1] = mbid.split("gid\": \"")[1].split("\"")[0];
                    if (savedAudio.size()%25==0 || savedAudio.size()+this.sortedAudio.size()+otherAudio.size()==savedTracks.size() || savedTrack.equals(savedTracks.getLast())) {
                        HttpRequest request = HttpRequest.newBuilder().uri(new URI("https://acousticbrainz.org/api/v1/high-level?recording_ids=" + Arrays.toString(mainIds).replace("[", "").replace("]", "").replace(", ", ";").replace("\"", "").replace(";null", "").replace(";\"null\"", ""))).header("Accept", "application/json").header("Authorization", "Bearer 6arnu8GSppFd4YdrVElxs8aWAMe8QrlkHn4AfEVo").GET().build();
                        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                        MBAudioAnalysis[] mbAudioData = serialiser.deserializeMBAudio(response.body().replaceFirst("\\{", "").replace("}}}}},", "}}}}},,").split("}}}}},,\"mbid_mapping\"")[0].concat("}}}}}").split(",,"), Arrays.toString(mainIds).replace("[", "").replace("]", "").replace("\"", "").replace(", ", ","));
                        for (int i=0; i<savedAudio.size(); i++) {
                            if (savedAudio.get(i).getCode()!=null && response.body().contains(savedAudio.get(i).getCode())) {
                                savedAudio.get(i).setData(mbAudioData[i], mood);
                                this.sortedAudio.add(savedAudio.get(i));
                            } else {
                                otherAudio.add(savedAudio.get(i));
                                if (otherIds[index].split(",").length>=51)
                                    otherIds[++index] = "," + savedAudio.get(i).getTrack().getId();
                                else
                                    otherIds[index] = otherIds[index].concat("," + savedAudio.get(i).getTrack().getId());
                            }
                        }
                        mainIds = new String[25];
                        savedAudio = new ArrayList<>();
                    }
                } else {
                    otherAudio.add(new Audio(savedTrack, isrc));
                    if (otherIds[index]==null)
                        otherIds[index] = "";
                    if (otherIds[index].split(",").length>=51)
                        otherIds[++index] = "," + savedTrack.getId();
                    else
                        otherIds[index] = otherIds[index].concat("," + savedTrack.getId());
                }
            }
            for (Audio audio : savedAudio) {
                otherAudio.add(audio);
                if (otherIds[index].split(",").length>=51)
                    otherIds[++index] = "," + audio.getTrack().getId();
                else
                    otherIds[index] = otherIds[index].concat("," + audio.getTrack().getId());
            }
            if (!otherAudio.isEmpty()) {
                index = 0;
                for (int i=0; i+50-otherAudio.size()<50; i+=50) {
                    HttpRequest request = HttpRequest.newBuilder().uri(new URI("https://spotify81.p.rapidapi.com/audio_features?ids=" + otherIds[index++].replaceFirst(",", ""))).header("Accept", "application/json").header("X-RapidAPI-Key", System.getenv("X-RAPIDAPI-KEY")).header("X-RapidAPI-Host", "spotify81.p.rapidapi.com").header("Content-Type", "application/json").GET().build();
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    List<APIAudioAnalysis> spAudioData = serialiser.deserializeAPIAudio(response.body().replace("{\"audio_features\":[", "").replace("]}", "").replace("{", "{{").replace("}", "}}").replace(",null,", ",{{\"danceability\":null,\"instrumentalness\":null,\"liveness\":null,\"speechiness\":null,\"tempo\":null,\"acousticness\":null,\"valence\":null,\"energy\":null,\"loudness\":null,\"id\":null}},").replace("{null,", "{{{\"danceability\":null\"instrumentalness\":null,\"liveness\":null,\"speechiness\":null,\"tempo\":null,\"acousticness\":null,\"valence\":null,\"energy\":null,\"loudness\":null,\"id\":null}},").split("},\\{"));
                    for (int o=0; o<spAudioData.size(); o++) {
                        if (otherAudio.get(i+o).getTrack().getId()!=null && response.body().contains(otherAudio.get(i+o).getTrack().getId()))
                            otherAudio.get(i+o).setData(spAudioData.get(o), mood);
                        else
                            IO.println("Data object not added!");
                    }
                }
            }
            this.sortedAudio.addAll(otherAudio);
            this.sortedAudio = this.sortedAudio.stream().parallel().distinct().sorted().toList();
        } catch(Exception e) {
            IO.println(e);
        }
    }

    public String selectAudio(long maxDuration, byte[] isMatch) {
        long totalDuration = 0;
        List<Audio> selectedAudio = new ArrayList<>();
        try {
            for (Audio audio : this.sortedAudio) {
                float tone = 0, loudness = 0;
                if (!String.valueOf(audio.getData()).contains("null")) {
                    if (!Float.valueOf(audio.getData().getTone()).toString().equals("null"))
                        tone = audio.getData().getTone();
                    if (!Float.valueOf(audio.getData().getLoudness()).toString().equals("null"))
                        loudness = audio.getData().getLoudness();
                }
                final boolean[] toneMatch = new boolean[]{tone > 9, tone <= 9 && tone > 6, tone <= 6 && tone > 3, tone <= 3 && tone > 0, tone == 0}, loudnessMatch = new boolean[]{loudness < -25, loudness >= -25 && loudness < -20, loudness >= -20 && loudness < -15, loudness >= -15 && loudness < -10, loudness >= -10 && loudness < -5, loudness >= -5};
                if (totalDuration + audio.getTrack().getDuration() <= maxDuration && toneMatch[isMatch[0]] && loudnessMatch[isMatch[1]]) {
                    totalDuration += audio.getTrack().getDuration();
                    selectedAudio.add(audio);
                }
            }
        } catch (Exception e) {
            IO.println(e);
        }
        this.playlist = new Playlist(selectedAudio, totalDuration);
        return getPlaylistInfo();
    }

    public String selectPlaylist(Playlist playlist) {
        this.playlist = playlist;
        return getPlaylistInfo();
    }

    private String getPlaylistInfo() {
        String info = "";
        for (int i=0; i<this.playlist.getSize(); i++) {
            Track track = this.playlist.getAudio(i).getTrack();
            String artist = "";
            for (int o=0; o<track.getArtists().length; o++)
                artist = artist.concat(track.getArtists()[o] + " • ");
            info = info.concat(track.getTitle() + "\r\n" + artist.replace(String.valueOf(artist.charAt(artist.length()-2)), "").trim() + "\r\n" + track.getUri() + "\r\n" + track.getDuration() + "\r\n\r\n");
        }
        return info.trim();
    }
}
