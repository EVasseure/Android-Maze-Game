package com.potatoinc.mazeaddict.Ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.potatoinc.mazeaddict.Bus.NameChoosenEvent;
import com.potatoinc.mazeaddict.Bus.PopBackStackEvent;
import com.potatoinc.mazeaddict.Bus.SwitchFragmentEvent;
import com.potatoinc.mazeaddict.Bus.WinEvent;
import com.potatoinc.mazeaddict.Model.User;
import com.potatoinc.mazeaddict.R;

import de.greenrobot.event.EventBus;


public class MainActivity extends FragmentActivity {

    private Fragment menuFragment;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuFragment = new MenuFragment();
        loadData();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_container, menuFragment)
                .commit();

    }

    private void loadData()
    {
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        String username = sharedPref.getString("NAME", "none");
        Integer points = sharedPref.getInt("POINTS", 0);
        User.username = username;
        User.points = points;
    }

    private void saveData()
    {
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("POINTS", User.points);
        editor.putString("NAME", User.username);
        editor.apply();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        saveData();
        super.onStop();
    }

    @SuppressWarnings("unused")
    public void onEvent(SwitchFragmentEvent switchFragmentEvent) {
        String tag = ((Object) switchFragmentEvent.getFragment()).getClass().getName();
        if (getSupportFragmentManager().findFragmentByTag(tag) != null) {
            return;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (switchFragmentEvent.getDirection() != null) {
            if (switchFragmentEvent.getDirection() == SwitchFragmentEvent.Direction.VERTICAL) {
                transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.slide_in, R.anim.slide_out);
            } else if (switchFragmentEvent.getDirection() == SwitchFragmentEvent.Direction.HORIZONTAL) {
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
            } else {
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
            }
        }

        if (switchFragmentEvent.getReplaceFragment()) {
            transaction.replace(R.id.main_container, switchFragmentEvent.getFragment(), tag);
        } else {
            transaction.add(R.id.main_container, switchFragmentEvent.getFragment(), tag);
        }

        if (switchFragmentEvent.getAddToBackStack()) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
    @SuppressWarnings("unused")
    public void onEvent(NameChoosenEvent nameChoosenEvent) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("NAME", nameChoosenEvent.getName());
        editor.apply();
    }

    @SuppressWarnings("unused")
    public void onEvent(PopBackStackEvent popBackStackEvent) {
        getSupportFragmentManager().popBackStack();
    }

}
