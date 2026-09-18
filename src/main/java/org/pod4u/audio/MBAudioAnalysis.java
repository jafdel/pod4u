package org.pod4u.audio;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tools.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonDeserialize(as = MBAudioAnalysis.class)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class MBAudioAnalysis extends AudioAnalysis {
    private final String mbid;

    @JsonCreator
    public MBAudioAnalysis(@JsonProperty("danceable") float danceability, @JsonProperty("instrumental") float instrumentalness, @JsonProperty("ambient") float ambience, @JsonProperty("spe") float speech, @JsonProperty("tonal") float tonality, @JsonProperty("bright") float timbre, @JsonProperty("acoustic") float acousticness, @JsonProperty("happy") float valence, @JsonProperty("aggressive") float aggression, @JsonProperty("replay_gain") float loudness, @JsonProperty("musicbrainz_recordingid") String id) {
        this.mbid = id;
        super(danceability, instrumentalness, ambience, speech, (((tonality+0.5f)%2) * timbre) * 12f, acousticness, valence, aggression, loudness);
    }

    public String getMBID() {
        return this.mbid;
    }

}
