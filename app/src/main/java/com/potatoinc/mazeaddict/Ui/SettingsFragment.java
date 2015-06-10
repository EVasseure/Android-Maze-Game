package com.potatoinc.mazeaddict.Ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.potatoinc.mazeaddict.Bus.NameChoosenEvent;
import com.potatoinc.mazeaddict.Bus.PopBackStackEvent;
import com.potatoinc.mazeaddict.Bus.SwitchFragmentEvent;
import com.potatoinc.mazeaddict.Model.Settings;
import com.potatoinc.mazeaddict.Model.User;
import com.potatoinc.mazeaddict.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by Erwan on 24/05/2015.
 */
public class SettingsFragment extends Fragment {

    @InjectView(R.id.fragment_settings_size_size_value)
    EditText sizeValue;

    @InjectView(R.id.fragment_settings_others_trail_switch)
    ToggleButton trailToggle;

    @InjectView(R.id.fragment_settings_others_turning_back_switch)
    ToggleButton turningBackToggle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.inject(this, rootView);

        sizeValue.setText(String.valueOf(Settings.mazeSize));
        trailToggle.setChecked(Settings.trail);
        turningBackToggle.setChecked(Settings.noTurningBack);

        return rootView;
    }

    @OnClick(R.id.fragment_settings_validate_button)
    public void onValidate()
    {
        if (Integer.parseInt(sizeValue.getText().toString()) < 25 || Integer.parseInt(sizeValue.getText().toString()) > 150)
        {
            Toast.makeText(getActivity(), "The maze size must be between 25 and 150", Toast.LENGTH_LONG).show();
            return;
        }
        Settings.mazeSize = Integer.parseInt(sizeValue.getText().toString());
        EventBus.getDefault().post(new PopBackStackEvent());
    }

    @OnClick(R.id.fragment_settings_others_turning_back_switch)
    public void onTurningBackClick()
    {
        Settings.noTurningBack = turningBackToggle.isChecked();
    }

    @OnClick(R.id.fragment_settings_others_trail_switch)
    public void onTrailClick()
    {
        Settings.trail = trailToggle.isChecked();
    }
}
