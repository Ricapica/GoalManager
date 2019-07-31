package com.goalmanager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ReminderWorker  extends Worker {

    private Context context;

    public ReminderWorker(@NonNull Context context, @NonNull WorkerParameters params){
        super(context, params);

        this.context = context;
    }


    @NonNull
    @Override
    public Result doWork(){
        Log.e("Rica", "IT IS TIME TO START THE JOB");
        createNotificationChannel();
        String goalName="";

        for(String s: getTags())
        {
            goalName = s;
        }
        Intent notificationIntent = new Intent(context,MainActivity.class);
        PendingIntent intent = PendingIntent.getActivity(context,0,notificationIntent,0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "hello")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Goal Reminder")
                .setContentText(goalName)
                .setContentIntent(intent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(0,builder.build());
        return Result.success();
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notif channel";
            String description = "it hold it";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("hello", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
