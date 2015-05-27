package com.potatoinc.mazeaddict.View;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.potatoinc.mazeaddict.Bus.WinEvent;
import com.potatoinc.mazeaddict.Model.Settings;
import com.potatoinc.mazeaddict.R;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by Erwan on 24/05/2015.
 */
public class Maze extends SurfaceView implements Runnable
{

    volatile boolean running = false;
    private SurfaceHolder holder;
    private Thread renderThread = null;
    private Boolean ended = false;

    private Point playerPosition;

    private Boolean created = false;
    private Paint black;
    private Paint white;
    private Paint orange;
    private Paint light_orange;
    private Integer SIZE = 15;

    private Point start;
    private Point end;
    private Integer width;
    private Integer heigth;
    private Boolean[][] cells;
    private ArrayList<Point> moves;

    public Maze(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        moves = new ArrayList<>();
        holder = getHolder();
    }

    int neighbors(Point pos, Point[] n)
    {
        int size = 0;
        try
        {
            if (pos.y > 1 && !cells[pos.x][pos.y - 2])
            {
                n[size] = new Point(pos.x, pos.y - 2);
                size++;
            }
            if (pos.y <= heigth - 4 && !cells[pos.x][pos.y + 2])
            {
                n[size] = new Point(pos.x, pos.y + 2);
                size++;
            }
            if (pos.x > 1 && !cells[pos.x - 2][pos.y])
            {
                n[size] = new Point(pos.x - 2, pos.y);
                size++;
            }
            if (pos.x <= width - 4 && !cells[pos.x + 2][pos.y])
            {
                n[size] = new Point(pos.x + 2, pos.y);
                size++;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return size;
    }

    void create_maze()
    {
        ArrayList<Point> q = new ArrayList<>();
        int visited = 1;
        Point pos = start;
        while (visited < width * heigth)
        {
            Point[] n = new Point[4];
            int nbn = neighbors(pos, n);
            if (nbn > 0)
            {
                Point rndn;
                if (nbn == 1)
                    rndn = n[0];
                else
                {
                    rndn = n[(int) (Math.random() * nbn)];
                }
                cells[pos.x][pos.y] = true;
                cells[rndn.x + (pos.x - rndn.x) / 2][rndn.y + (pos.y - rndn.y) / 2] = true;
                cells[rndn.x][rndn.y] = true;
                q.add(pos);
                pos = rndn;
                visited++;
            } else if (q.size() > 0)
            {
                pos = q.remove(q.size() - 1);
            } else
                break;
        }
        cells[start.x][start.y] = true;
        cells[end.x][end.y] = true;
    }

    public void movePlayer(String dir)
    {
        if (moves.size() != 0)
            return;
        switch (dir)
        {
            case "up":
                if (cells[playerPosition.x][playerPosition.y - 1])
                    moveTowardNextIntersect(new Point(playerPosition.x, playerPosition.y - 1));
                break;
            case "down":
                if (cells[playerPosition.x][playerPosition.y + 1])
                    moveTowardNextIntersect(new Point(playerPosition.x, playerPosition.y + 1));
                break;
            case "left":
                if (cells[playerPosition.x - 1][playerPosition.y])
                    moveTowardNextIntersect(new Point(playerPosition.x - 1, playerPosition.y));
                break;
            case "right":
                if (cells[playerPosition.x + 1][playerPosition.y])
                    moveTowardNextIntersect(new Point(playerPosition.x + 1, playerPosition.y));
                break;
        }
        this.invalidate();
    }

    private int checkMoveDirs(Boolean[][] cs, Point pos, Point[] n)
    {
        int size = 0;
        try
        {
            if (!cs[pos.x][pos.y - 1])
            {
                n[size] = new Point(pos.x, pos.y - 1);
                size++;
            }
            if (!cs[pos.x][pos.y + 1])
            {
                n[size] = new Point(pos.x, pos.y + 1);
                size++;
            }
            if (!cs[pos.x - 1][pos.y])
            {
                n[size] = new Point(pos.x - 1, pos.y);
                size++;
            }
            if (!cs[pos.x + 1][pos.y])
            {
                n[size] = new Point(pos.x + 1, pos.y);
                size++;
            }
        } catch (Exception e)
        {
            n[size] = new Point(pos.x, pos.y);
            e.printStackTrace();
        }
        return size;

    }

    private void moveTowardNextIntersect(Point dir)
    {
        Boolean[][] vCells = new Boolean[width][heigth];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < heigth; j++)
            {
                vCells[i][j] = false;
                if (!cells[i][j])
                    vCells[i][j] = true;
            }

        vCells[playerPosition.x][playerPosition.y] = true;
        playerPosition = dir;

        Point[] n = new Point[4];
        int nbn = checkMoveDirs(vCells, playerPosition, n);
        Point pos = playerPosition;
        while (nbn < 2 && nbn != 0)
        {
            vCells[pos.x][pos.y] = true;
            pos = n[0];
            vCells[n[0].x][n[0].y] = true;
            moves.add(new Point(pos.x, pos.y));
            this.invalidate();
            nbn = checkMoveDirs(vCells, pos, n);
        }
    }

    private void scaleCanvas(Canvas canvas)
    {
        float sx = width * SIZE;
        float sy = heigth * SIZE;

        sx = canvas.getWidth() / sx;
        sy = canvas.getHeight() / sy;

        if (sx > sy)
            sx = sy;
        else
            sy = sx;

        canvas.scale(sx, sy);
    }

    private void drawMaze(Canvas canvas)
    {
        width = Settings.mazeSize + Settings.mazeSize % 2;
        heigth = Settings.mazeSize + Settings.mazeSize % 2;



        scaleCanvas(canvas);

        if (!created)
        {
            black = new Paint();
            black.setColor(getResources().getColor(R.color.dark_grey));
            white = new Paint();
            white.setColor(getResources().getColor(R.color.white));
            orange = new Paint();
            orange.setColor(getResources().getColor(R.color.orange));
            light_orange = new Paint();
            light_orange.setColor(getResources().getColor(R.color.blue));

            start = new Point(1, heigth / 2);
            end = new Point(width - 1, heigth / 2);
            playerPosition = start;
            cells = new Boolean[width][heigth];
            for (int i = 0; i < width; i++)
                for (int j = 0; j < heigth; j++)
                    cells[i][j] = false;
            create_maze();
            created = true;
        }

        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < heigth; j++)
            {
                int ni = i * SIZE;
                int nj = j * SIZE;
                if (i == playerPosition.x && j == playerPosition.y)
                    canvas.drawRect(ni, nj, SIZE + ni, SIZE + nj, light_orange);
                else if (i == end.x && j == end.y)
                    canvas.drawRect(ni, nj, SIZE + ni, SIZE + nj, orange);
                else if (cells[i][j])
                    canvas.drawRect(ni, nj, SIZE + ni, SIZE + nj, white);
                else
                    canvas.drawRect(ni, nj, SIZE + ni, SIZE + nj, black);
            }
        }
    }

    @Override
    public void run()
    {
        while (running)
        {
            if (!holder.getSurface().isValid())
                continue;
            Canvas canvas = holder.lockCanvas();
            drawMaze(canvas);
            holder.unlockCanvasAndPost(canvas);
            update();
        }
    }

    public void resume()
    {
        running = true;
        renderThread = new Thread(this);
        renderThread.start();
    }

    private void update()
    {
        if (!ended && playerPosition.x == end.x && playerPosition.y == end.y)
        {
            ((Activity)getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    EventBus.getDefault().post(new WinEvent());
                    ended = true;
                }
            });

            renderThread.interrupt();
        }
        if (moves.size() != 0)
        {
            playerPosition = moves.remove(0);
        }
    }

    public void pause()
    {
        running = false;
        boolean retry = true;
        while (retry)
        {
            try
            {
                renderThread.join();
                retry = false;
            } catch (InterruptedException e)
            {
            }
        }
    }
}
