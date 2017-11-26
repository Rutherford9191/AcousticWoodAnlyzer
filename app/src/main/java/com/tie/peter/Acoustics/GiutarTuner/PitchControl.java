package com.tie.peter.Acoustics.GiutarTuner;

public interface PitchControl {

    void play(double frequency);
    void play(Note note);
    void stop();

}
