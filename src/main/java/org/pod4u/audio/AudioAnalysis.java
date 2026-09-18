package org.pod4u.audio;

public class AudioAnalysis {
    private float danceability;
    private float instrumentalness;
    private float ambience;
    private float speechiness;
    private float tone;
    private float acousticness;
    private float valence;
    private float energy;
    private float loudness;

    public AudioAnalysis() {

    }

    public AudioAnalysis(float danceability, float instrumentalness, float liveness, float speechiness, float tone, float acousticness, float valence, float energy, float loudness) {
        this.danceability = danceability;
        this.instrumentalness = instrumentalness;
        this.ambience = liveness;
        this.speechiness = speechiness;
        this.tone = tone;
        this.acousticness = acousticness;
        this.valence = valence;
        this.energy = energy;
        this.loudness = loudness;
    }

    public float getDanceability() {
        return this.danceability;
    }

    public float getInstrumentalness() {
        return this.instrumentalness;
    }

    public float getAmbience() {
        return this.ambience;
    }

    public float getSpeechiness() {
        return this.speechiness;
    }

    public float getTone() {
        return this.tone;
    }

    public float getAcousticness() {
        return this.acousticness;
    }

    public float getValence() {
        return this.valence;
    }

    public float getEnergy() {
        return this.energy;
    }

    public float getLoudness() {
        return this.loudness;
    }

}
