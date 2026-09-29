package org.pod4u.audio;

import org.pod4u.mood.Mood;

public class Audio implements Comparable<Audio> {
    private final Track track;
    private AudioAnalysis data;
    private final String code;
    private int count;

    public Audio(Track track, String code) {
        this.track = track;
        this.code = code;
        this.count = 0;
    }

    public void setData(AudioAnalysis data, Mood mood) {
        this.data = new AudioAnalysis((float)Math.pow((float)Math.pow(data.getDanceability()-mood.getPride(), 2f), 1f/2f), (float)Math.pow((float)Math.pow(data.getInstrumentalness()-mood.getFocus(), 2f), 1f/2f), (float)Math.pow((float)Math.pow(data.getAmbience()-mood.getFear(), 2f), 1f/2f), (float)Math.pow((float)Math.pow(data.getSpeechiness()-mood.getExpressiveness(), 2f), 1f/2f), data.getTone(), (float)Math.pow((float)Math.pow(data.getAcousticness()-mood.getCalm(), 2f), 1f/2f), (float)Math.pow((float)Math.pow(data.getValence()-mood.getJoy(), 2f), 1f/2f), (float)Math.pow((float)Math.pow(data.getEnergy()-mood.getAnger(), 2f), 1f/2f), data.getLoudness());
    }

    public AudioAnalysis getData() {
        return this.data;
    }

    public Track getTrack() {
        return this.track;
    }

    public String getCode() {
        return this.code;
    }

    @Override
    public int compareTo(Audio a) {
        IO.println(a.getTrack().getTitle());
        final float[] checks = new float[]{this.data.getDanceability()-a.getData().getDanceability(), this.data.getInstrumentalness()-a.getData().getInstrumentalness(), this.data.getAmbience()-a.getData().getAmbience(), this.data.getSpeechiness()-a.getData().getSpeechiness(), this.data.getAcousticness()-a.getData().getAcousticness(), this.data.getValence()-a.getData().getValence(), this.data.getEnergy()-a.getData().getEnergy()};
        float total = 0, floor = 0;
        this.count++;
        try {
            for (float check : checks)
                total += check;
            /*for (float check : checks) {
                if (check > 0) {
                    count++;
                    total += check;
                } else if (check < 0)
                    floor += check;
            }*/
        } catch (Exception e) {
            IO.println(e + " (" + this.count + " - " + this.data + ")");
        }
        /*if (count<0)
            return 0;
        if (count>=4)
            return (int)(total / (8-count)) * 100000000;*/
        //return (int)(floor / (count+1)) * 100000000;
        if (String.valueOf(total).endsWith("5") || String.valueOf(total).endsWith("6") || String.valueOf(total).endsWith("7") || String.valueOf(total).endsWith("8") || String.valueOf(total).endsWith("9"))
            total = (float)(Math.ceil(total * Math.pow(10, 8)) / Math.pow(10, 8));
        return (int)(total * 100000000);
    }
}
