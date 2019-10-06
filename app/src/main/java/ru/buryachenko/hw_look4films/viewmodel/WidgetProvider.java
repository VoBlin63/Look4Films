package ru.buryachenko.hw_look4films.viewmodel;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Arrays;

import ru.buryachenko.hw_look4films.R;
import ru.buryachenko.hw_look4films.models.FilmInApp;

import static android.appwidget.AppWidgetManager.ACTION_APPWIDGET_UPDATE;
import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;
import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_IDS;
import static ru.buryachenko.hw_look4films.utils.Constants.LOGTAG;
import static ru.buryachenko.hw_look4films.utils.Constants.PREFERENCES_SELECTED_FILM;
import static ru.buryachenko.hw_look4films.utils.Constants.WIDGET_ACTION;

public class WidgetProvider extends AppWidgetProvider {

    final static String WIDGET_ACTION_CHANGE_LIKED = "widgetChangeLiked";
    final static String WIDGET_ACTION_PICTURE = "widgetPicturePressed";
    private SharedPreferences settings;
    private String widgetData = "";

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d(LOGTAG, "onDeleted(), ids:" + Arrays.toString(appWidgetIds));
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        Log.d(LOGTAG, "onAppWidgetOptionsChanged(), id:" + appWidgetId);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d(LOGTAG, "onDisabled()");
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(LOGTAG, "onEnabled()");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(LOGTAG, "onReceive() -----------  " + intent.getAction());
        int appWidgetId = intent.getIntExtra(EXTRA_APPWIDGET_ID, 0);
//        Log.d(LOGTAG, "onReceive " + " appWidgetId = " + appWidgetId);
        if (WIDGET_ACTION_CHANGE_LIKED.equals(intent.getAction())) {
            changeLikedAndSaveWidgetData(context);
        }
        if (WIDGET_ACTION_CHANGE_LIKED.equals(intent.getAction())
                || WIDGET_ACTION_PICTURE.equals(intent.getAction())) {
            Intent intentSending = new Intent(context, WidgetProvider.class);
            intentSending.setAction(ACTION_APPWIDGET_UPDATE);
            intentSending.putExtra(EXTRA_APPWIDGET_IDS, intent.getIntArrayExtra(EXTRA_APPWIDGET_IDS));
            intentSending.putExtra(EXTRA_APPWIDGET_ID, appWidgetId);
            intentSending.putExtra(WIDGET_ACTION, intent.getAction());
            context.sendBroadcast(intentSending);
        }
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
        Log.d(LOGTAG, "onRestored(), newIds:" + Arrays.toString(newWidgetIds) + " , oldIds:" + Arrays.toString(oldWidgetIds));
    }

    private void loadWidgetData(Context context) {
        if (settings == null)
            settings = PreferenceManager.getDefaultSharedPreferences(context);
        widgetData = "";
        if(settings.contains(PREFERENCES_SELECTED_FILM)) {
            widgetData = settings.getString(PREFERENCES_SELECTED_FILM, "");
        }
    }
    private void changeLikedAndSaveWidgetData(Context context) {
        if (settings == null)
            settings = PreferenceManager.getDefaultSharedPreferences(context);
        loadWidgetData(context);
        if (!widgetData.isEmpty()) {
            boolean newVal = FilmInApp.likedFromWidgetString(widgetData);
            widgetData = widgetData.replace(Boolean.toString(newVal), Boolean.toString(!newVal));
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(PREFERENCES_SELECTED_FILM, widgetData);
            editor.apply();
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d(LOGTAG, "onUpdate(), ids:" + Arrays.toString(appWidgetIds));
        loadWidgetData(context);
        class PendingInner {
            private PendingIntent pendingIntent;
            PendingInner(String action, int widgetId) {
                Intent intent = new Intent(context, WidgetProvider.class);
                intent.setAction(action);
                intent.putExtra(EXTRA_APPWIDGET_ID, widgetId);
                intent.putExtra(EXTRA_APPWIDGET_IDS, appWidgetIds);
                pendingIntent = PendingIntent.getBroadcast(context,
                        0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            }
            PendingIntent result() {
                return pendingIntent;
            }
        }
        ComponentName thisWidget = new ComponentName(context, WidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
            remoteViews.setImageViewResource(R.id.widgetLiked, FilmInApp.likedFromWidgetString(widgetData)? R.drawable.liked : R.drawable.notliked);
            Integer picture = FilmInApp.pictureResourceFromWidgetString(widgetData);
            if (picture == null) {
                //remoteViews.setImageViewResource(R.id.widgetPicture, picture);
            }
            else
                remoteViews.setImageViewResource(R.id.widgetPicture, picture);
//            remoteViews.setTextViewText(R.id.widgetText, "smth" + number);
            // Register an onClickListener
//            remoteViews.setOnClickPendingIntent(R.id.widgetNext, new PendingInner(WIDGET_ACTION_BUTTON_NEXT, widgetId).result());
            remoteViews.setOnClickPendingIntent(R.id.widgetLiked, new PendingInner(WIDGET_ACTION_CHANGE_LIKED, widgetId).result());
            remoteViews.setOnClickPendingIntent(R.id.widgetPicture, new PendingInner(WIDGET_ACTION_PICTURE, widgetId).result());
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }
}
