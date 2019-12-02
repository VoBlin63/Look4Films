package ru.buryachenko.hw_look4films.utils;

import android.app.PendingIntent;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import ru.buryachenko.hw_look4films.App;
import ru.buryachenko.hw_look4films.R;
import ru.buryachenko.hw_look4films.activities.MainActivity;

import static ru.buryachenko.hw_look4films.utils.Constants.NOTIFICATION_CHANNEL_ID;


public class FilmNotification {
    private static int id = 0;

    public static void pushMessage(String title, String text) {
        Intent intent = new Intent(App.getInstance(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(App.getInstance(), 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(App.getInstance(), NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_movie)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(App.getInstance());
        notificationManager.notify(id++, builder.build());
    }
}
