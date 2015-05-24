package com.potatoinc.mazeaddict.Bus;

import android.support.v4.app.Fragment;

/**
 * Created by Erwan on 24/05/2015.
 */
public class SwitchFragmentEvent {
    private final Fragment fragment;
    private final Direction direction;
    private final Boolean replaceFragment;
    private final Boolean addToBackStack;

    public SwitchFragmentEvent(final Fragment fragment, final Direction direction, final Boolean replaceFragment, final Boolean addToBackStack)
    {
        this.fragment = fragment;
        this.direction = direction;
        this.replaceFragment = replaceFragment;
        this.addToBackStack = addToBackStack;
    }

    public Fragment getFragment()
    {
        return fragment;
    }

    public Direction getDirection()
    {
        return direction;
    }

    public Boolean getReplaceFragment()
    {
        return replaceFragment;
    }

    public Boolean getAddToBackStack()
    {
        return addToBackStack;
    }

    public enum Direction
    {
        VERTICAL, HORIZONTAL, ALPHA
    }
}
