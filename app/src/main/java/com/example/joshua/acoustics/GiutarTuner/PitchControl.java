package com.example.joshua.acoustics.GiutarTuner;

public interface PitchControl {

    void play(double frequency);
    void play(Note note);
    void stop();

}
