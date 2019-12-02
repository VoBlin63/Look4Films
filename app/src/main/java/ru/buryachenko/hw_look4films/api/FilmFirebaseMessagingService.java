
package ru.buryachenko.hw_look4films.api;

import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.annotation.RequiresApi;
import ru.buryachenko.hw_look4films.BuildConfig;
import ru.buryachenko.hw_look4films.utils.FilmNotification;

import static ru.buryachenko.hw_look4films.utils.Constants.LOGTAG;

public class FilmFirebaseMessagingService extends FirebaseMessagingService {
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            FilmNotification.pushMessage(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
            if(remoteMessage.getData().size() > 0){
                remoteMessage.getData().forEach((key,data)-> {
                    Log.d(LOGTAG,key + "->" + data);
                });
            }
        }
         else {
             if (BuildConfig.DEBUG) {
                 Log.d(LOGTAG, "Получено пустое RemoteMessage!");
             }
        }
    }
}
