package com.example.joshua.acoustics.GiutarTuner;

import com.example.joshua.acoustics.GiutarTuner.tarsos.PitchDetectionResult;

public interface TunerUpdate {

    void updateNote(Note newNote, PitchDetectionResult result);

}
