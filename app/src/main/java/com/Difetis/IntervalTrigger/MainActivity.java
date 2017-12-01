package com.Difetis.IntervalTrigger;

import android.app.Activity;
import android.content.Context;
import android.os.Vibrator;
import android.provider.Settings;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.os.SystemClock;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.os.Handler;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import android.widget.Toast;

public class MainActivity extends Activity   {
    private TextView chrono;
    private TextView actualTime;
    private Vibrator vibrator;

    Button stop;

    long MillisecondTime, StartTime, TimeBuff, LapTime, UpdateTime = 0L ;

    Handler chronoHandler;
    Handler timeHandler;
    int Seconds, Minutes, MilliSeconds, CentiSeconds ;

    ListView lapList ;

    String[] ListElements = new String[] {  };

    List<String> ListElementsArrayList ;

    ArrayAdapter<String> adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Settings.System.putInt(this.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS, 0);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = 0;
        getWindow().setAttributes(lp);

        setContentView(R.layout.activity_main);
        chrono = (TextView)findViewById(R.id.Chrono);
        stop = (Button)findViewById(R.id.buttonStop) ;
        lapList = (ListView)findViewById(R.id.lapList);
        actualTime = (TextView)findViewById(R.id.ActualTime);

        timeHandler = new Handler(getMainLooper());
        timeHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                actualTime.setText(new SimpleDateFormat("HH:mm").format(new Date()));
                timeHandler.postDelayed(this, 1000);
            }
        }, 10);

        //setAmbientEnabled();

        vibrator =  (Vibrator) this.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);

        chronoHandler = new Handler() ;

        ListElementsArrayList = new ArrayList<String>(Arrays.asList(ListElements));

        adapter = new ArrayAdapter<String>(MainActivity.this,
                R.layout.listlayout,
                ListElementsArrayList
        );

        lapList.setAdapter(adapter);

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // si le chrono est arreté, on reset
                if(StartTime == 0L){
                    MillisecondTime = 0L ;
                    StartTime = 0L ;
                    TimeBuff = 0L ;
                    UpdateTime = 0L ;
                    Seconds = 0 ;
                    Minutes = 0 ;
                    MilliSeconds = 0 ;

                    chrono.setText("00:00:00");

                    ListElementsArrayList.clear();

                    adapter.notifyDataSetChanged();


                }
                else{ // sinon on stop

                    TimeBuff += MillisecondTime;
                    chronoHandler.removeCallbacks(runnable);
                    StartTime = 0L;
                    stop.setText("Reset");

                    // on a plus besoin de maintenir l'écran toujours allumé
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                }

                // on vibre pour prévenir que l'action a bien été effectuée
                vibrator.vibrate(50);


            }
        });



    }

    /* pour gérer l'état always on
    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);

        mStateTextView.setTextColor(Color.WHITE);
        mStateTextView.getPaint().setAntiAlias(false);
    }

    @Override
public void onExitAmbient() {
    super.onExitAmbient();

    mStateTextView.setTextColor(Color.GREEN);
    mStateTextView.getPaint().setAntiAlias(true);
}
Up
*/
    public void onChronoClick(View v) {

        // si le chrono n'est pas lancé, on démarre
        if(StartTime == 0L)
        {
            // tant que le chrono est allumé on empeche l'écran de s'éteindre
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            // on stocke le timestamp de départ
            StartTime = SystemClock.uptimeMillis();
            // le laptime et startime sont identiques sur le premier lap
            LapTime = StartTime;
            // on lance la callback
            chronoHandler.postDelayed(runnable, 0);
        }else{ // sinon on sauvegarde le tour

            // on stop la callback le temps de réinitialiser le chrono
            chronoHandler.removeCallbacks(runnable);

            // on affiche le temps du lap sous forme de toast
            Toast.makeText(getApplicationContext(), chrono.getText().toString(), Toast.LENGTH_LONG).show();
            // on ajoute le dernier lap au début
            ListElementsArrayList.add(0,chrono.getText().toString());
            adapter.notifyDataSetChanged();

            // on redémarre en mettant à jour le laptime
            LapTime = SystemClock.uptimeMillis();
            chronoHandler.postDelayed(runnable, 0);

        }

        // on vibre pour prévenir que l'action a bien été effectuée
        vibrator.vibrate(50);

        stop.setText("Stop");
    }

    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - LapTime;

            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

            Minutes = Seconds / 60;

            Seconds = Seconds % 60;

            MilliSeconds = (int) (UpdateTime % 1000);

            CentiSeconds = MilliSeconds / 10;

            chrono.setText("" + String.format("%02d", Minutes) + ":"
                    + String.format("%02d", Seconds) + ":"
                    + String.format("%02d", CentiSeconds));

            chronoHandler.postDelayed(this, 0);
        }

    };

        @Override
    protected void onDestroy() {
        super.onDestroy();

            timeHandler = null;
            chronoHandler = null;

    }

    @Override
// Activity
    public boolean onKeyDown(int keyCode, KeyEvent event){


        Toast.makeText(getApplicationContext(), "onkeydown", Toast.LENGTH_SHORT).show();
        if (event.getRepeatCount() == 0) {
            if (keyCode == KeyEvent.KEYCODE_STEM_1) {
                // Do stuff
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_STEM_2) {
                // Do stuff
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_STEM_3) {
                // Do stuff
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
