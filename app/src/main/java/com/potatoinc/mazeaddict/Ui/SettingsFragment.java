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

import com.potatoinc.mazeaddict.Bus.NameChoosenEvent;
import com.potatoinc.mazeaddict.Bus.SwitchFragmentEvent;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.inject(this, rootView);

        return rootView;
    }

}
