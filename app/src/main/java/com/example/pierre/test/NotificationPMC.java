package com.example.pierre.test;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class NotificationPMC extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Button button_notif = (Button) findViewById(R.id.button_notif);
        button_notif.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                createNotification();
                Toast t = Toast.makeText(getApplicationContext(), "Notif envoyée", Toast.LENGTH_SHORT);
                t.show();
            }
        });

    }

    private final void createNotification(){
        Notification myNotication;
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, 0);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setAutoCancel(false);
        builder.setTicker("Nouvelle notification !");
        builder.setContentTitle("Notification Park My Car");
        builder.setContentText("Une nouvelle place de parking est disponible...");
        builder.setSmallIcon(R.drawable.logo);
        builder.setContentIntent(pendingIntent);
        builder.setOngoing(true);
        builder.setSubText("Cliquez pour en savoir plus !");
        builder.setNumber(100);
        builder.build();

        myNotication = builder.getNotification();
        myNotication.vibrate = new long[]{100, 100, 200, 100};
        manager.notify(1, myNotication);
    }
}
