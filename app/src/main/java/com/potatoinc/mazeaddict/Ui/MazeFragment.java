package com.potatoinc.mazeaddict.Ui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.potatoinc.mazeaddict.Bus.DrawingDoneEvent;
import com.potatoinc.mazeaddict.Bus.GiveUpEvent;
import com.potatoinc.mazeaddict.Bus.LoseEvent;
import com.potatoinc.mazeaddict.Bus.PopBackStackEvent;
import com.potatoinc.mazeaddict.Bus.ValidateWinEvent;
import com.potatoinc.mazeaddict.Bus.WinEvent;
import com.potatoinc.mazeaddict.Model.User;
import com.potatoinc.mazeaddict.R;
import com.potatoinc.mazeaddict.View.Maze;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class MazeFragment extends Fragment {

    @InjectView(R.id.fragment_maze_maze)
    protected Maze maze;

    @InjectView(R.id.fragment_maze_timer)
    protected Chronometer chronometer;

    @InjectView(R.id.fragment_maze_timer_text)
    protected TextView timerTextView;

    @InjectView(R.id.fragment_maze_win_message)
    protected TextView winMessage;

    @InjectView(R.id.fragment_maze_win)
    protected RelativeLayout winLayout;

    private float swipStartX;
    private float swipEndX;
    private float swipStartY;
    private float swipEndY;
    private long countUp;
    private boolean win;

    private float MIN_SWIP_DIST = 50;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maze, container, false);
        addTouchListener(rootView);

        ButterKnife.inject(this, rootView);

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){
            @Override
            public void onChronometerTick(Chronometer arg0) {
                countUp = (SystemClock.elapsedRealtime() - arg0.getBase()) / 1000;
                String asText = (countUp / 60) + ":" + (countUp % 60);
                timerTextView.setText(asText);
            }
        });
        chronometer.start();

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

    private void addTouchListener(View view)
    {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        swipStartX = event.getX();
                        swipStartY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        swipEndX = event.getX();
                        swipEndY = event.getY();
                        float deltaX = swipEndX - swipStartX;
                        float deltaY = swipEndY - swipStartY;

                        if (Math.abs(deltaX) > Math.abs(deltaY)) {
                            if (deltaX > MIN_SWIP_DIST) {
                                maze.movePlayer("right");
                            } else if (deltaX < -MIN_SWIP_DIST) {
                                maze.movePlayer("left");
                            }
                        }
                        else {
                            if (deltaY > MIN_SWIP_DIST) {
                                maze.movePlayer("down");
                            } else if (deltaY < -MIN_SWIP_DIST) {
                                maze.movePlayer("up");
                            }
                        }

                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        maze.resume();
    };

    @Override
    public void onPause() {
        super.onPause();
        maze.pause();
    };

    @OnClick (R.id.fragment_maze_giveup_button)
    public void onGiveUp()
    {
        EventBus.getDefault().post(new GiveUpEvent());
    }

    @SuppressWarnings("unused")
    public void onEvent(WinEvent winEvent) {
        win = true;
        winLayout.setVisibility(View.VISIBLE);
    }

    @SuppressWarnings("unused")
    public void onEvent(LoseEvent loseEvent) {
        win = false;
        winMessage.setText(getResources().getString(R.string.lost));
        winLayout.setVisibility(View.VISIBLE);
    }

    @OnClick (R.id.fragment_maze_win_validate)
    public void onClickOkay()
    {
        EventBus.getDefault().post(new ValidateWinEvent(win));
    }
}
