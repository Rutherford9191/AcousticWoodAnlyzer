package com.tie.peter.Acoustics.GiutarTuner;

import com.tie.peter.Acoustics.GiutarTuner.tarsos.PitchDetectionResult;

public interface TunerUpdate {

    void updateNote(Note newNote, PitchDetectionResult result);

}
