package com.data.handcar;

public class RunThread extends Thread {
    private Game gra;
    public RunThread(Game g)
    {
        super();
        this.gra =g;
    }
    public void run()
    {
        gra.ruch();
    }
}
