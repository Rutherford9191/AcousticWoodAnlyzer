package com.example.joshua.Acoustics;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tyorikan.voicerecordingvisualizer.RecordingSampler;
import com.tyorikan.voicerecordingvisualizer.VisualizerView;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;


public class guitarQuality extends Fragment {

    RecordingSampler mRecordingSampler = null;
    FloatingActionButton fab = null;
    private OnFragmentInteractionListener mListener;
    private ColumnChartView chart;
    private ColumnChartData data;
    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLabels = true;
    private boolean hasLabelForSelected = false;
    public guitarQuality() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        mRecordingSampler = new RecordingSampler();
        mRecordingSampler.setSamplingInterval(100); // voice sampling interval
        mRecordingSampler.startRecording();
        mRecordingSampler.stopRecording();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wood_quality, container, false);
        VisualizerView visualizerView = (VisualizerView)view.findViewById(R.id.visualizer);
        mRecordingSampler.link(visualizerView);     // link to visualizer
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_action_acivate_mic));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRecordingSampler.isRecording()){
                    mRecordingSampler.stopRecording();
                    fab.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_action_acivate_mic));
                }else{
                    mRecordingSampler.startRecording();
                    fab.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_action_stop_mic));
                }
            }
        });

        chart = (ColumnChartView) view.findViewById(R.id.chart);
        generateDefaultData();

        return view;
    }

    private void generateDefaultData() {
        int numSubcolumns = 1;
        int numColumns = 4;
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
                values.add(new SubcolumnValue((float) Math.random() * 100, ChartUtils.pickColor()));
            }

            Column column = new Column(values);
            column.setHasLabels(hasLabels);
            column.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column);
        }
        data = new ColumnChartData(columns);

        if (hasAxes) {
            List<AxisValue> grades = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                grades.add(new AxisValue(j));
            }
            Axis axisX = new Axis().setValues(grades);
            Axis axisY = new Axis();
            if (hasAxesNames) {
                axisX.setName("Quality Grade");
            }
            data.setAxisXTop(axisX);
            data.setAxisYLeft(axisY);
        }
        chart.setColumnChartData(data);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
