package com.superpotato.simplemaze.Bus;

/**
 * Created by Erwan on 26/05/2015.
 */
public class ValidateWinEvent
{
    private boolean win;

    public ValidateWinEvent(boolean win)
    {
        this.win = win;
    }

    public boolean isWin()
    {
        return win;
    }
}
