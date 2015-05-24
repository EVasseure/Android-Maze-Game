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
public class MenuFragment extends Fragment {

    @InjectView(R.id.fragment_menu_hiname)
    protected TextView username;

    @InjectView(R.id.fragment_menu_score)
    protected TextView score;

    @InjectView(R.id.fragment_menu_namegetter_edittext)
    protected EditText nameEditText;

    @InjectView(R.id.fragment_menu_namegetter)
    protected RelativeLayout nameGetterLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);
        ButterKnife.inject(this, rootView);
        if (User.username.equals("none"))
            nameGetterLayout.setVisibility(View.VISIBLE);
        username.setText(User.username + "!");
        score.setText(User.points + " points.");
        return rootView;
    }

    @OnClick(R.id.fragment_menu_play_button)
    public void launchGame()
    {
        EventBus.getDefault().post(new SwitchFragmentEvent(new MazeFragment(), SwitchFragmentEvent.Direction.VERTICAL, false, true));
    }

    @OnClick(R.id.fragment_menu_settings_button)
    public void showSettings()
    {
        Toast.makeText(getActivity(), "Soon", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.fragment_menu_namegetter_validate)
    public void validatename()
    {
        User.username = nameEditText.getText().toString();
        username.setText(User.username + "!");
        nameGetterLayout.setVisibility(View.GONE);
        EventBus.getDefault().post(new NameChoosenEvent(User.username));
    }
}
