package com.flynaut.healthtag.view.pulseoximeter.utils;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.flynaut.healthtag.R;
import com.flynaut.healthtag.view.pulseoximeter.utils.ble.BluetoothManager;
import com.flynaut.healthtag.view.pulseoximeter.utils.ble.ParseRunnable;
import com.flynaut.healthtag.view.pulseoximeter.utils.ble.WaveForm;
import com.flynaut.healthtag.view.pulseoximeter.utils.device_list.DeviceAdapter;
import com.flynaut.healthtag.view.pulseoximeter.utils.device_list.DeviceListDialog;

/*
 * @deprecated  MainActivity
 * @author zl
 * @date 2022/12/2 13:44
 */
public class MainActivityPulse extends Fragment implements View.OnClickListener {

    private TextView mSpo2;
    private TextView mPr;
    private TextView mPi;
    private TextView mRR;
    private WaveForm mWaveForm;
    private BluetoothManager ble;
    private DeviceListDialog dialog;
    private ParseRunnable mParseRunnable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_main_pukse, container, false);
        _init(view);
        return view;
    }




    private void _init(View view) {
        mSpo2 = view.findViewById(R.id.spo2);
        mPr = view.findViewById(R.id.pr);
        mPi = view.findViewById(R.id.pi);
        mRR = view.findViewById(R.id.rr);
        mWaveForm = view.findViewById(R.id.wave);
        TextView version = view.findViewById(R.id.version);
        mWaveForm.setWaveformVisibility(true);
        Button search = view.findViewById(R.id.search);
        search.setOnClickListener(this);
        _runnable();
        DeviceAdapter adapter = new DeviceAdapter(getActivity());
        new Thread(mParseRunnable).start();
        ble = new BluetoothManager(getActivity(), adapter, mParseRunnable, mWaveForm);
        dialog = new DeviceListDialog(getActivity(), ble, adapter);
        ble.scanRule();

        //version.setText(Version.getVersionName());
    }

    private void _runnable() {
        mParseRunnable = new ParseRunnable(new ParseRunnable.OnDataChangeListener() {
            @Override
            public void spo2Val(int spo2) {
                getActivity().runOnUiThread(() -> mSpo2.setText(spo2 > 0 ? (spo2 + "") : "--"));
            }

            @Override
            public void prVal(int pr) {
                getActivity().runOnUiThread(() -> mPr.setText(pr > 0 ? (pr + "") : "--"));
            }

            @Override
            public void waveVal(int wave) {
                getActivity().runOnUiThread(() -> mWaveForm.addAmplitude(wave));
            }

            @Override
            public void piVal(double pi) {
                getActivity().runOnUiThread(() -> mPi.setText(pi > 0 ? (pi + "") : "--"));
            }

            @Override
            public void rrVal(int rr) {
                getActivity().runOnUiThread(() -> mRR.setText(rr > 0 ? (rr + "") : "--"));
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.search) {
            //Permissions.all(this, ble, dialog);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mParseRunnable.setStop(false);

    }
}