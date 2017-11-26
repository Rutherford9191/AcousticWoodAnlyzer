package com.example.joshua.Acoustics.GiutarTuner;

import com.example.joshua.Acoustics.GiutarTuner.tarsos.PitchDetectionResult;

public interface TunerUpdate {

    void updateNote(Note newNote, PitchDetectionResult result);

}
