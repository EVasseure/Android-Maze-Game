package com.potatoinc.mazeaddict.Ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.potatoinc.mazeaddict.Bus.GiveUpEvent;
import com.potatoinc.mazeaddict.Bus.NameChoosenEvent;
import com.potatoinc.mazeaddict.Bus.PopBackStackEvent;
import com.potatoinc.mazeaddict.Bus.SwitchFragmentEvent;
import com.potatoinc.mazeaddict.Bus.SwitchToMazeFragmentEvent;
import com.potatoinc.mazeaddict.Bus.ValidateWinEvent;
import com.potatoinc.mazeaddict.Bus.WinEvent;
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
public class MenuFragment extends Fragment {

    @InjectView(R.id.fragment_menu_hiname)
    protected TextView username;

    @InjectView(R.id.fragment_menu_score)
    protected TextView score;

    @InjectView(R.id.fragment_menu_namegetter_edittext)
    protected EditText nameEditText;

    @InjectView(R.id.fragment_menu_namegetter)
    protected RelativeLayout nameGetterLayout;

    @InjectView(R.id.fragment_menu_layout)
    protected RelativeLayout mainLayout;

    @InjectView(R.id.fragment_menu_rate)
    protected ImageButton rateButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);
        ButterKnife.inject(this, rootView);
        if (User.nb_games >= 5)
            rateButton.setVisibility(View.VISIBLE);
        if (User.username.equals("none"))
            nameGetterLayout.setVisibility(View.VISIBLE);
        username.setText(User.username + "!");
        score.setText(User.points + " points.");
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @OnClick(R.id.fragment_menu_play_button)
    public void launchGame()
    {
        ValueAnimator animator = ObjectAnimator.ofFloat(mainLayout, "alpha", 1f, 0f);
        animator.setDuration(500)
                .addListener(new Animator.AnimatorListener()
                {
                    @Override
                    public void onAnimationStart(Animator animation)
                    {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        EventBus.getDefault().post(new SwitchToMazeFragmentEvent());
                    }

                    @Override
                    public void onAnimationCancel(Animator animation)
                    {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation)
                    {

                    }
                });
        animator.start();
    }

    @OnClick(R.id.fragment_menu_settings_button)
    public void showSettings()
    {
        EventBus.getDefault().post(new SwitchFragmentEvent(new SettingsFragment(), SwitchFragmentEvent.Direction.VERTICAL, false, true));
    }

    @OnClick(R.id.fragment_menu_namegetter_validate)
    public void validatename()
    {
        User.username = nameEditText.getText().toString();
        username.setText(User.username + "!");
        nameGetterLayout.setVisibility(View.GONE);
        EventBus.getDefault().post(new NameChoosenEvent(User.username));
    }

    @SuppressWarnings("unused")
    public void onEvent(ValidateWinEvent winEvent) {
        User.nb_games += 1;
        if (User.nb_games == 5)
        {
            AlertDialog alertDialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.start_rate_message)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("market://details?id=" + getResources().getString(R.string.app_url)));
                            if (!tryStartingActivity(intent)) {
                                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getResources().getString(R.string.app_url)));
                                if (!tryStartingActivity(intent)) {
                                    Toast.makeText(getActivity(), R.string.cant_open_market, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    })
                    .setNegativeButton(R.string.no_thank_you, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            // cancel
                        }
                    });
            alertDialog = builder.create();
            alertDialog.show();
            rateButton.setVisibility(View.VISIBLE);
        }
        ValueAnimator animator = ObjectAnimator.ofFloat(mainLayout, "alpha", 0f, 1f);
        animator.setDuration(10)
                .addListener(new Animator.AnimatorListener()
                {
                    @Override
                    public void onAnimationStart(Animator animation)
                    {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        EventBus.getDefault().post(new PopBackStackEvent());
                    }

                    @Override
                    public void onAnimationCancel(Animator animation)
                    {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation)
                    {

                    }
                });
        animator.start();
        if (winEvent.isWin())
        {
            int points = Settings.mazeSize;
            if (Settings.noTurningBack)
                points *= 2;
            if (Settings.trail)
                points *= 0.8;
            User.points += points;
            score.setText(User.points + " points.");
        }
    }

    @SuppressWarnings("unused")
    public void onEvent(GiveUpEvent giveUpEvent) {
        ValueAnimator animator = ObjectAnimator.ofFloat(mainLayout, "alpha", 0f, 1f);
        animator.setDuration(10)
                .addListener(new Animator.AnimatorListener()
                {
                    @Override
                    public void onAnimationStart(Animator animation)
                    {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        EventBus.getDefault().post(new PopBackStackEvent());
                    }

                    @Override
                    public void onAnimationCancel(Animator animation)
                    {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation)
                    {

                    }
                });
        animator.start();
    }

    private boolean tryStartingActivity(Intent intent)
    {
        try
        {
            startActivity(intent);
            return true;
        }
        catch (ActivityNotFoundException e)
        {
            return false;
        }
    }

    @OnClick(R.id.fragment_menu_rate)
    public void rate()
    {
        AlertDialog alertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.rate_message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("market://details?id=" + getResources().getString(R.string.app_url)));
                        if (!tryStartingActivity(intent)) {
                            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getResources().getString(R.string.app_url)));
                            if (!tryStartingActivity(intent)) {
                                Toast.makeText(getActivity(), R.string.cant_open_market, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .setNegativeButton(R.string.no_thank_you, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // cancel
                    }
                });
        alertDialog = builder.create();
        alertDialog.show();

    }
}
