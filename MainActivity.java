package com.data.handcar;


import android.annotation.SuppressLint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;

import static java.lang.Thread.sleep;


public class MainActivity extends AppCompatActivity {
    private Button handcar_button;
    View tlo;
    private ImageView handcar;
    private TextView napis;
    private Game gra;
    private ImageView ob1,ob2,tory;
    private ImageView c1,c2,c3,c4,c5,ogien;
    private Button restart;
    private MainActivity ptr =this;
    int maxt=0,mint=0;
    int y1=0,y2=0,y3=0,y4=0,y5=0;
    int xo1=0,xo2=0;
    int height,width;
    Handler updateUIHandler;
    int MESSAGE_UPDATE_THREAD=1;
    Integer best=0;
    int licznik_reklam=0;
    private InterstitialAd mInterstitialAd;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //init ads
        MobileAds.initialize(this,"ca-app-pub-3143069734547022~3081266867");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3143069734547022/7769706511");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
        try
        {
            //read best score
            FileInputStream fIn = openFileInput("dane.txt");
            InputStreamReader isr = new InputStreamReader(fIn);
            int i;
            char c;
            String read="";
            while((i = isr.read())!=-1)
            {
                c = (char)i;
                read = read+c;
            }
            best = Integer.valueOf(read);
            isr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        handcar = findViewById(R.id.handcar_view);
        napis = findViewById(R.id.napis1);
        ob1=findViewById(R.id.otoczenie1);
        ob2=findViewById(R.id.otoczenie2);
        tory = findViewById(R.id.tory);
        c1=findViewById(R.id.cel_1);
        c2=findViewById(R.id.cel_2);
        c3=findViewById(R.id.cel_3);
        c4=findViewById(R.id.cel_4);
        c5=findViewById(R.id.cel_5);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        height = size.y;
        width=size.x;
        ogien=findViewById(R.id.fire);
        tlo = findViewById(R.id.tlo_reset);
        //end game screen
        updateUIHandler = new Handler()
        {
            //@Override
            public void handleMessage(Message msg) {
                if(msg.what == MESSAGE_UPDATE_THREAD)
                {
                    tlo.setVisibility(View.VISIBLE);
                }
            }
        };
        handcar_button = findViewById(R.id.main_button);
        //move of handcar
        handcar_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        handcar.setImageResource(R.drawable.handcar_2);
                        gra.click();
                        break;
                    case MotionEvent.ACTION_UP:
                        handcar.setImageResource(R.drawable.handcar_3);
                        break;
                }
                return true;
            }
        });
        restart = findViewById(R.id.restart);
        //ads every third lost
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                licznik_reklam++;
                if(licznik_reklam>=3)
                {
                    if (mInterstitialAd.isLoaded()) {
                        licznik_reklam=0;
                        mInterstitialAd.show();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }
                }
                handcar_button.setVisibility(View.VISIBLE);
                napis.setText("Tap");
                handcar.setY(maxt);
                handcar.setScaleX((float) (0.9 * (handcar.getY()) / (gra.max_tory) + 0.4 * ((gra.max_tory - handcar.getY()) / gra.max_tory)));
                handcar.setScaleY((float) (0.9 * (handcar.getY()) / (gra.max_tory) + 0.4 * ((gra.max_tory - handcar.getY()) / gra.max_tory)));
                tlo.setVisibility(View.GONE); //end game screen
                gra=new Game(handcar,napis,ob1,ob2,tory,c1,c2,c3,c4,c5,ogien,ptr);

            }
        });
        //start
        gra = new Game(handcar,napis,ob1,ob2,tory,c1,c2,c3,c4,c5,ogien,this);
    }

    void koniec()
    {
        handcar_button.setVisibility(View.INVISIBLE);
        try {
            sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //show end game screen
        Thread worker = new Thread(new Runnable()
        {
            @Override
            public void run() {
                Message message = new Message();
                message.what = MESSAGE_UPDATE_THREAD;
                updateUIHandler.sendMessage(message);
            }
        });
        worker.start();
        try {
            worker.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TextView t=findViewById(R.id.wynik);
        t.setText(napis.getText());
        TextView t2=findViewById(R.id.best);

        try {
            //new best
            if(gra.metry > best)best=gra.metry;
            String wpis = best.toString();
            // Write to the file
            FileOutputStream fOut = openFileOutput("dane.txt", MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write(wpis);
            osw.close();

            //Reading the file
            FileInputStream fIn = openFileInput("dane.txt");
            InputStreamReader isr = new InputStreamReader(fIn);

            char[] inputBuffer = new char[wpis.length()];
            isr.read(inputBuffer);
            String read = new String(inputBuffer);
            best = Integer.valueOf(read);

        } catch (IOException ioe)
        {
            ioe.printStackTrace();
        }

        t2.setText(best.toString()+" m");
        try {
            gra.mainThread.join();
        } catch (InterruptedException e) {

        }

    }
}