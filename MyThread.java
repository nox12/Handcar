package com.data.handcar;

class MyThread extends Thread{
    private Game gra;
    public boolean koniec = false;
    public MyThread(Game g)
    {
        super();
        this.gra =g;
    }
    public void run()
    {
        while(!koniec)
        {
            while (gra.start) {
                gra.runThread.run();
                gra.oThread.run();
                gra.cThread.run();
                try {
                    gra.runThread.join();
                    //gra.oThread.join();
                    gra.cThread.join();
                } catch (InterruptedException e) {
                }
                try {
                    sleep(100);
                } catch (InterruptedException e) {

                }
            }
        }

    }

}
