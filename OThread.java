package com.data.handcar;

public class OThread extends Thread {
    private Game gra;
    public OThread(Game g)
    {
        super();
        this.gra =g;
    }
    public void run()
    {
        gra.otoczenie(gra.obj1,1);
        gra.otoczenie(gra.obj2, 2);
    }
}
