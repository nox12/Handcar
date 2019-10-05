package com.data.handcar;

public class CThread extends Thread{
    private Game gra;
    public CThread(Game g)
    {
        super();
        this.gra =g;
    }
    public void run()
    {
        gra.cele();
    }
}
