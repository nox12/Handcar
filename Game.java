package com.data.handcar;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class Game
{
    private int speed_click=80;
    private int speed = 16;
    private ImageView handcar;
    private int min_tory = 210;
    int max_tory = 1150;
    MyThread mainThread;
    RunThread runThread;
    OThread oThread;
    CThread cThread;
    Integer metry=0;
    private TextView napis;
    private int zycie=1;
    private Random rand;
    private int ilosc_cel=1;
    private int aktywny_cel=0;
    private float o1_x=0,o2_x=0;
    private int l_zmiana=0;
    ImageView obj1,obj2,tory;
    private ImageView c1,c2,c3,c4,c5,ogien;
    private int i1=0,i2=0,i3=0,i4=0,i5=0;
    private float c1_y,c2_y,c3_y,c4_y,c5_y;
    private MainActivity okno;
    boolean start = false;

    public Game(ImageView v,TextView text,ImageView ob1,ImageView ob2,ImageView tory,ImageView c1,ImageView c2,ImageView c3,ImageView c4,ImageView c5,ImageView og,MainActivity okno)
    {
        this.handcar=v;
        this.napis=text;
        this.runThread = new RunThread(this);
        this.cThread= new CThread(this);
        this.oThread = new OThread(this);
        this.mainThread=new MyThread(this);
        this.obj1=ob1;
        this.obj2=ob2;
        this.tory=tory;
        this.c1=c1;
        this.c2=c2;
        this.c3=c3;
        this.c4=c4;
        this.c5=c5;
        this.ogien=og;
        this.rand = new Random();
        obj1.setY(max_tory+700);
        obj2.setY(max_tory+700);
        ogien.setY(max_tory+700);
        c1.setY(max_tory+700);
        c2.setY(max_tory+700);
        c3.setY(max_tory+700);
        c4.setY(max_tory+700);
        c5.setY(max_tory+700);
        this.okno=okno;
    }
    public void click()
    {
        if(!start)
        {
            //first time set basic values
            if(okno.maxt==0)
            {
                okno.maxt=okno.height-(int)(handcar.getHeight()*1.1);
                okno.mint=(int)(0.18*okno.height);
                okno.y5=okno.height-(int)(handcar.getHeight()*0.75);
                okno.y1=okno.mint+(int)(c1.getHeight()*0.4);
                float pom=(okno.y5-okno.y1)/4;
                okno.y4=okno.y5-(int)(1.1*pom);
                okno.y3=okno.y4-(int)(1.3*pom);
                okno.y2=okno.y3-(int)(0.8*pom);
                okno.xo1= (int)c1.getX()-obj1.getWidth()-10;
                okno.xo2 = (int)(0.55*okno.width);
            }
            //read basic values
            min_tory=okno.mint;
            max_tory=okno.maxt;
            c1_y = okno.y1;
            c2_y = okno.y2;
            c3_y = okno.y3;
            c4_y = okno.y4;
            c5_y = okno.y5;
            o1_x=okno.xo1;
            o2_x=okno.xo2;
            start=true;
            this.mainThread.start();
        }
        //if game is running move handcar up
        else if(handcar.getY() > (min_tory-(int)(handcar.getHeight()*0.4))) handcar.setY(handcar.getY()-speed_click);
    }
    public void ruch()
    {
        //move handcar down
        if(start) {
            if (handcar.getY() < max_tory) handcar.setY(handcar.getY() + speed);
            float skala = (float) ((0.85*handcar.getY()) / (max_tory) + 0.3*((max_tory-handcar.getY())/max_tory));
            handcar.setScaleX(skala);
            handcar.setScaleY(skala);
            this.metry += speed / 10;
            napis.setText((new Integer(metry).toString()) + " m");
            if (l_zmiana>2) {
                tory.setImageResource(R.drawable.tory2);
                l_zmiana++;
                if (l_zmiana > 4) {
                    l_zmiana = 0;
                }
            } else {
                tory.setImageResource(R.drawable.tory1);
                l_zmiana++;
            }
            if(metry>200 && ilosc_cel<2){ilosc_cel=2;speed+=4;}
            if(metry>800 && ilosc_cel<3){ilosc_cel=3;}
            if(metry>2400 && ilosc_cel<4){ilosc_cel=4;}
        }
    }
    //background movement
    public void otoczenie(ImageView objekt, int tryb)
    {
        if(objekt.getY() < max_tory+550)
        {
            objekt.setY(objekt.getY()+speed+10);
        }
        if(objekt.getY() >= max_tory+550)
        {
            int n = rand.nextInt(50);
            if(tryb ==1)
            {
                if(n<10)
                {
                    objekt.setY(min_tory-180);
                    objekt.setX(o1_x-10);
                }
            }
            else if(tryb ==2)
            {
                if(n<6)
                {
                    objekt.setY(min_tory-180);
                    objekt.setX(o2_x+10);
                }
            }
        }
        float skala = (float) (objekt.getY()/(max_tory+500)+0.4*((max_tory+500-objekt.getY())/(max_tory+500)));
        objekt.setScaleX(skala);
        objekt.setScaleY(skala);
        if(tryb ==1)objekt.setX(objekt.getX()-5);
        else objekt.setX(objekt.getX()+5);
    }
    //targets to avoid
    public void cele()
    {
        if(aktywny_cel < ilosc_cel)
        {
            int los = rand.nextInt(5);
            if(los == 0)
            {
                if(i1 == 0)
                {
                    i1=1;
                    c1.setY(c1_y);
                    aktywny_cel++;
                }
            }
            if(los == 1)
            {
                if(i2 == 0)
                {
                    i2=1;
                    c2.setY(c2_y);
                    aktywny_cel++;
                }
            }
            if(los == 2)
            {
                if(i3 == 0)
                {
                    i3=1;
                    c3.setY(c3_y);
                    aktywny_cel++;
                }
            }
            if(los == 3)
            {
                if(i4 == 0)
                {
                    i4=1;
                    c4.setY(c4_y);
                    aktywny_cel++;
                }
            }
            if(los == 4)
            {
                if(i5 == 0)
                {
                    i5=1;
                    c5.setY(c5_y);
                    aktywny_cel++;
                }
            }
        }
        if(i1>0)atak(c1,1);
        if(i2>0)atak(c2,2);
        if(i3>0)atak(c3,3);
        if(i4>0)atak(c4,4);
        if(i5>0)atak(c5,5);
    }
    //behavior of targets
    private void atak(ImageView cel, int i)
    {
        int ii=0;
        switch(i)
        {
            case 1: ii=i1;i1++;break;
            case 2: ii=i2;i2++;break;
            case 3: ii=i3;i3++;break;
            case 4: ii=i4;i4++;break;
            case 5: ii=i5;i5++;break;
        }

        if(ii<10)
        {
            if(ii%4 <2)
            {
                cel.setImageResource(R.drawable.cel_or);
            }
            else
            {
                cel.setImageResource(R.drawable.cel_or2);
            }
        }
        else if(ii<15)
        {
            if(ii%4 <2)
            {
                cel.setImageResource(R.drawable.cel_n);
            }
            else
            {
                cel.setImageResource(R.drawable.cel_n2);
            }
        }
        else
        {
            if(ii%4 <2)
            {
                cel.setImageResource(R.drawable.cel_red);
            }
            else
            {
                cel.setImageResource(R.drawable.cel_red2);
            }
        }
        if(ii >= 20)
        {
            switch(i)
            {
                case 1: i1=0;break;
                case 2: i2=0;break;
                case 3: i3=0;break;
                case 4: i4=0;break;
                case 5: i5=0;break;
            }
            aktywny_cel--;
            //if hit
            if(((handcar.getY()+(handcar.getHeight()*0.32)) < (cel.getY()+cel.getHeight())) && ((handcar.getY()+handcar.getHeight()*0.78) > (cel.getY()+cel.getHeight()*0.2)))
            {
                if(zycie>0)zycie--;
                if(zycie <= 0)
                {
                    ogien.setY(cel.getY()-(int)(ogien.getHeight()*0.3));
                    ogien.setScaleX((float) (ogien.getY()/(max_tory+500)+0.5*((max_tory+500-ogien.getY())/(max_tory+500))));
                    ogien.setScaleY((float) (ogien.getY()/(max_tory+500)+0.5*((max_tory+500-ogien.getY())/(max_tory+500))));
                    start=false;
                    mainThread.koniec=true;
                    okno.koniec();
                }
            }
            cel.setY(max_tory+700);
        }
    }
}
