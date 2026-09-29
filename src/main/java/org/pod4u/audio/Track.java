package org.pod4u.audio;

import com.fasterxml.jackson.annotation.*;
import tools.jackson.databind.annotation.JsonDeserialize;

import java.util.LinkedHashMap;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(as = Track.class)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Track {
    @JsonProperty("artists")
    private String[] artists;
    @JsonProperty("duration_ms")
    private Integer duration;
    @JsonProperty("uri")
    private String uri;
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String title;
    @JsonProperty("external_ids")
    private String isrc;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public Track() {

    }

    @JsonCreator
    public Track(@JsonProperty("artists") Object artists, @JsonProperty("duration_ms") Integer duration, @JsonProperty("uri") String uri, @JsonProperty("id") String id, @JsonProperty("name") String title, @JsonProperty("external_ids") LinkedHashMap<String, String> isrc) {
        IO.println(id);
        IO.println(artists);
        IO.println(title);
        IO.println(uri);
        IO.println(duration);
        IO.println(isrc);
        this.id = id;
        this.uri = uri;
        this.title = title;
        this.artists = new String[artists.toString().split("}, \\{").length];
        for (int i=0; i<this.artists.length; i++)
            this.artists[i] = artists.toString().split("}, \\{")[i].split("name=")[1].split(", type=")[0];
        //this.albumTitle = albumTitle;
        //this.albumArt = albumArt;
        this.duration = duration;
        this.isrc = isrc.toString().split("=")[1].split("}")[0];
    }

    @JsonProperty
    public String getId() {
        return this.id;
    }

    @JsonProperty
    public Object getUri() {
        return this.uri;
    }

    @JsonProperty
    public String getTitle() {
        return this.title;
    }

    @JsonProperty
    public Object[] getArtists() {
        return this.artists;
    }

    /*public String getAlbumTitle() {
        return this.albumTitle;
    }*/

    /*public String getAlbumArt() {
        return this.albumArt;
    }*/

    @JsonProperty
    public Integer getDuration() {
        return this.duration;
    }

    @JsonProperty
    public String getIsrc() {
        return this.isrc;
    }
}
