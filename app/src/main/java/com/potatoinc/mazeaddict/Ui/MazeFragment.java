package com.potatoinc.mazeaddict.Ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.potatoinc.mazeaddict.R;
import com.potatoinc.mazeaddict.View.Maze;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

public class MazeFragment extends Fragment {

    @InjectView(R.id.fragment_maze_maze)
    protected Maze maze;

    private float swipStartX;
    private float swipEndX;
    private float swipStartY;
    private float swipEndY;

    private float MIN_SWIP_DIST = 50;

    // private Handler frame = new Handler();
    // private static final int FRAME_RATE = 20; //50 frames per second

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maze, container, false);
        addTouchListener(rootView);

        ButterKnife.inject(this, rootView);
        /*Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                frame.postDelayed(frameUpdate, FRAME_RATE);
            }
        }, 1000);*/

        return rootView;
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

    /*private Runnable frameUpdate = new Runnable() {
        @Override
        synchronized public void run() {
            frame.removeCallbacks(frameUpdate);
            maze.invalidate();
            frame.postDelayed(frameUpdate, FRAME_RATE);
        }
    };*/
}
