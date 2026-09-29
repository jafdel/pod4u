package org.pod4u.mood;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import tools.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(as = Mood.class)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Mood {
    @JsonProperty("beauty")
    private float pride;
    @JsonProperty("stain")
    private float expressiveness;
    @JsonProperty("darkCircle")
    private float fear;
    @JsonProperty("health")
    private float focus;
    @JsonProperty("eyeGaze")
    private float clarity;
    @JsonProperty("close")
    private float calm;
    @JsonProperty("smile")
    private float joy;
    @JsonProperty("anger")
    private float anger;
    @JsonProperty("roll_angle")
    private float presence;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public Mood() {

    }

    @JsonCreator
    public Mood(@JsonProperty("health") float health, @JsonProperty("beauty") Object beauty, @JsonProperty("dark_circle") float darkCircle, @JsonProperty("anger") float anger, @JsonProperty("eyegaze") Object eyeGaze, @JsonProperty("eyestatus") Object eyeStatus, @JsonProperty("smile") float smile, @JsonProperty("close") float mouthClosed, @JsonProperty("roll_angle") float rollAngle, @JsonProperty("stain") float stain) {
        this.pride = (Float.parseFloat(beauty.toString().split("=")[1].replace(", female_score", "")) + Float.parseFloat(beauty.toString().split("=")[2].replace("}", ""))) / 200;
        IO.println(this.pride);
        this.focus = health / 100;
        IO.println(this.focus);
        this.fear = darkCircle / 100;
        IO.println(this.fear);
        this.expressiveness = stain / 100;
        IO.println(this.expressiveness);
        float leftTotal = 0, rightTotal = 0, status = Float.parseFloat(eyeStatus.toString().split("=")[2].split(",")[0]) + Float.parseFloat(eyeStatus.toString().split("=")[5].split(",")[0]) + Float.parseFloat(eyeStatus.toString().split("=")[9].split(",")[0]) + Float.parseFloat(eyeStatus.toString().split("=")[12].split(",")[0]);
        for (int i=2; i<7; i++) {
            leftTotal += (float)Math.pow((float)Math.pow(Float.parseFloat(eyeGaze.toString().replace("={", "=").split("=")[i].replace("}, ", ",").split(",")[0])*100, 2f), 1f/2f);
            rightTotal += (float)Math.pow((float)Math.pow(Float.parseFloat(eyeGaze.toString().replace("={", "=").split("=")[i+6].replace("}}", ",").split(",")[0])*100, 2f), 1f/2f);
        }
        this.clarity = (1200 - leftTotal - rightTotal - status) / 100;
        IO.println(this.clarity);
        this.calm = mouthClosed / 100;
        IO.println(this.calm);
        this.joy = smile / 100;
        IO.println(this.joy);
        this.anger = anger / 100;
        IO.println(this.anger);
        this.presence = (float)Math.pow((float)Math.pow((rollAngle/2) - (((rollAngle/2)%30)*((rollAngle/2)/60)), 2f), 1/2f) * -1;
        IO.println(this.presence);
    }

    @JsonProperty
    public float getPride() {
        return this.pride;
    }

    @JsonProperty
    public float getExpressiveness() {
        return this.expressiveness;
    }

    @JsonProperty
    public float getFear() {
        return this.fear;
    }

    @JsonProperty
    public float getFocus() {
        return this.focus;
    }

    @JsonProperty
    public float getClarity() {
        return this.clarity;
    }

    @JsonProperty
    public float getCalm() {
        return this.calm;
    }

    @JsonProperty
    public float getJoy() {
        return this.joy;
    }

    @JsonProperty
    public float getAnger() {
        return this.anger;
    }

    @JsonProperty
    public float getPresence() {
        return this.presence;
    }
}