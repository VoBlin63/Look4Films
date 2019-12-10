package ru.buryachenko.hw_look4films.db;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import ru.buryachenko.hw_look4films.App;
import ru.buryachenko.hw_look4films.BuildConfig;
import ru.buryachenko.hw_look4films.R;
import ru.buryachenko.hw_look4films.api.responce.FilmJson;
import ru.buryachenko.hw_look4films.api.responce.WholeResponse;
import ru.buryachenko.hw_look4films.utils.SharedPreferencesOperation;

import static com.google.firebase.iid.FirebaseInstanceId.getInstance;
import static ru.buryachenko.hw_look4films.utils.Constants.FCM_KEY_MESSAGE;
import static ru.buryachenko.hw_look4films.utils.Constants.LOGTAG;
import static ru.buryachenko.hw_look4films.utils.Constants.PREFERENCES_TIME_TO_UPDATE;
import static ru.buryachenko.hw_look4films.utils.Constants.REFRESH_DB_PERIOD;


public class ServiceDb extends Service {

    private static final String apiKey = "c3e17ff26735628669886b00d573ab4d";
    private static final String language = "ru-RU";
    private static final String region = "RU";

    public static final String STATUS_SERVICE_IDLE = "lkesrjlksfgnsf";
    public static final String STATUS_SERVICE_BUSY = "3indflnglmfdnf";

    private static final long sleepBetweenPages = 60001L;
    private static final PublishSubject<String> status = PublishSubject.create();

    private Looper serviceLooper;
    private ServiceHandler serviceHandler;


    public static Observable<String> getStatus() {
        return status;
    }

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            Log.d(LOGTAG, "handleMessage, msg.arg1:[" + msg.arg1 + "]");

            long timeToRefresh = Long.parseLong(SharedPreferencesOperation.load(PREFERENCES_TIME_TO_UPDATE,"0"));
            if (new Date().getTime() >= timeToRefresh)  {
                onStartUpdate();
                int resultCount = pushFilmsInDb(apiKey, language, region);
                SharedPreferencesOperation.save(PREFERENCES_TIME_TO_UPDATE, Long.toString(new Date().getTime() + REFRESH_DB_PERIOD));
                onFinishUpdate(resultCount);
            } else {
                if (BuildConfig.DEBUG) {
                    Log.d(LOGTAG, "Пропускаем обновление БД - еще рано");
                }
            }
            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        Log.d(LOGTAG, "onCreate()");
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread(LOGTAG,
                Process.THREAD_PRIORITY_BACKGROUND){
            @Override
            public void run() {
                Log.d(LOGTAG, "run()");
                super.run();
                Log.d(LOGTAG, "super.run()");
            }
        };
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOGTAG, "onStartCommand()");

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        serviceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy()
    {
        if (BuildConfig.DEBUG) {
            Log.d(LOGTAG, "Service DB destroyed");
        }
    }


    private int pushFilmsInDb(String apiKey, String language, String region) {
        int res = 0;
        int page = 1;
        WholeResponse data;
        do {
            data =  getPage(apiKey, page, language, region);
            res += getCount(data);
            App.getInstance().filmsDb.daoFilm().insert(getFilmsFromPage(data));
            Log.d(LOGTAG, "Записана " + page + " страница из " + getPagesQuantity(data));
            try {
                Thread.sleep(sleepBetweenPages);
            } catch (InterruptedException e) {
                break;
            }
        } while ((data != null) && (page++ <= 52));//getPagesQuantity(data)));
        return res;
    }

    private WholeResponse getPage(String apiKey, int page, String language, String region) {
        WholeResponse res = null;
        try {
            res = App.getInstance().service.getFilms(apiKey, page, language, region).execute().body();
        } catch (IOException e) {
            if (BuildConfig.DEBUG) {
                Log.d(LOGTAG, "Ошибка получения страницы с фильмами: " + e);
            }
        }
        return  res;
    }


    private List<FilmInDb> getFilmsFromPage(WholeResponse page) {
        List<FilmInDb> res = new ArrayList<>();
        if (page != null) {
                for (FilmJson filmJson : page.getResults()) {
                    res.add(new FilmInDb(filmJson));
                }
            }
        return res;
    }

    private int getPagesQuantity(WholeResponse page) {
        int res = 0;
        if (page != null) {
            res = page.getTotalPages();
        }
        return res;
    }

    private int getCount(WholeResponse page) {
        return page.getResults().size();
    }

    private void onStartUpdate() {
        status.onNext(STATUS_SERVICE_BUSY);
        if (BuildConfig.DEBUG) {
            Log.d(LOGTAG, "Запускаем обновление БД");
        }
    }

    private void onFinishUpdate(int count) {
        sendNotificationThrowFCM(getString(R.string.notificationChannelFinishBodyPart1) + count + " " + getString(R.string.notificationChannelFinishBodyPart2));
//        FilmNotification.pushMessage(getString(R.string.notificationChannelFinishTitle), getString(R.string.notificationChannelFinishBodyPart1) + count + " " + getString(R.string.notificationChannelFinishBodyPart2));
        if (BuildConfig.DEBUG) {
            Log.d(LOGTAG, "Обновление БД завершено");
        }
        status.onNext(STATUS_SERVICE_IDLE);
    }

    private void sendNotificationThrowFCM(String message) {
        FirebaseMessaging fm = FirebaseMessaging.getInstance();
        fm.send(new RemoteMessage.Builder(App.getInstance().getString(R.string.senderIdFCM) + "@gcm.googleapis.com")
                .setMessageId("FINISH" + new Date().getTime())
                .addData(FCM_KEY_MESSAGE, message)
                .build());
    }
}
