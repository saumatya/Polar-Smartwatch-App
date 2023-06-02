package com.example.polar_watch;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.TaskStackBuilder;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class TrackingService extends Service {

    private static final String TAG = "TrackingService";
    public static final String TRACKING_ACTION = "com.example.helios.TRACKING";
    public static final String CHANNEL_ID = "TrackingChannel_ID";
    public static int ONGOING_NOTIFICATION_ID = 10; // We can set any number (without 0)!
    private CountDownTimer countDownTimer;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onCreate();
        createNotificationChannel();

        Intent it = new Intent(getBaseContext(), StartTrackingActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getBaseContext());
        stackBuilder.addNextIntentWithParentStack(it);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


        Notification notification = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("Start Tracking")
                .setContentText("")
                .setSmallIcon(R.drawable.ic_baseline_circle_24)
                .setContentIntent(pendingIntent)
                .build();

        // sets service to foreground mode
        startForeground(ONGOING_NOTIFICATION_ID, notification);
        // activates notification
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.notify(ONGOING_NOTIFICATION_ID, notification);

        startCountDownTimer(intent);

        return START_STICKY;
    }


    /**
     * starts count down timer.
     *
     * @param intent must be a valid Intent object.
     */
    private void startCountDownTimer(Intent intent) {

        long millisUntilFinished = intent.getLongExtra("start_value_for_timer", 150000000);

        intent.putExtra("countdown", millisUntilFinished);
        countDownTimer = new CountDownTimer(millisUntilFinished, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Check with connected watch
                // TODO wait 5 Second and request location
                // TODO connect to firebase

                 Log.i(TAG, "Countdown seconds remaining:" + millisUntilFinished / 1000);
                Intent intent = new Intent(TRACKING_ACTION);
                NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(getBaseContext().NOTIFICATION_SERVICE);
                manager.getNotificationChannel(CHANNEL_ID).setImportance(NotificationManager.IMPORTANCE_MIN);
                intent.putExtra("countdown", millisUntilFinished);
                sendBroadcast(intent);
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "Timer in TrackingService successfully finished");
                turnOffTimer();
            }
        };
        countDownTimer.start();

    }

    /**
     * creates notification channel.
     * This method is important if we want run this service in the foreground.
     */
    private void createNotificationChannel() {
        CharSequence name = getString(R.string.channel_name);
        String description = getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Log.i(TAG, "TimerService stopped");
        turnOffTimer();
    }

    /**
     * Turn off timer
     */
    private void turnOffTimer() {
        countDownTimer.cancel();  // stop countDownTimer
        stopForeground(STOP_FOREGROUND_REMOVE); // stop foreground mod for TrackingService
        stopSelf(); // stop TimerService
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("We do not need this function.");
    }
}