package org.pod4u.playlist;

import javafx.collections.transformation.SortedList;
import org.pod4u.audio.Audio;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private final List<Audio> selectedAudio;
    private String name;
    private int index;
    private boolean isPlaying;
    private final long duration;

    public Playlist(List<Audio> selectedAudio, long duration) {
        this.selectedAudio = selectedAudio;
        this.duration = duration;
        this.index = 0;
        this.isPlaying = false;
    }

    public int getSize() {
        return this.selectedAudio.size();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Audio getAudio(int i) {
        return this.selectedAudio.get(i);
    }
}
