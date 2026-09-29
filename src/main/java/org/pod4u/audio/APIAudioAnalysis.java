package org.pod4u.audio;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tools.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonDeserialize(as = APIAudioAnalysis.class)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class APIAudioAnalysis extends AudioAnalysis {
    private String spid;

    @JsonCreator
    public APIAudioAnalysis(@JsonProperty("danceability") float danceability, @JsonProperty("instrumentalness") float instrumentalness, @JsonProperty("liveness") float liveness, @JsonProperty("speechiness") float speechiness, @JsonProperty("key") int key, @JsonProperty("mode") int mode, @JsonProperty("acousticness") float acousticness, @JsonProperty("valence") float valence, @JsonProperty("energy") float energy, @JsonProperty("loudness") float loudness, @JsonProperty("id") String id) {
        this.spid = id;
        super(danceability, instrumentalness, liveness, speechiness, (float)(((key+1)/2) + (6*mode)), acousticness, valence, energy, loudness);
    }

    public String getSpId() {
        return this.spid;
    }
}
