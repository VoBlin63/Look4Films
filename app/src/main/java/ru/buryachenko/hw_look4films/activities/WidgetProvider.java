package ru.buryachenko.hw_look4films.activities;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.bumptech.glide.request.transition.Transition;

import ru.buryachenko.hw_look4films.R;
import ru.buryachenko.hw_look4films.models.FilmInApp;
import ru.buryachenko.hw_look4films.utils.RandomPicture;
import ru.buryachenko.hw_look4films.utils.SharedPreferencesOperation;

import static android.appwidget.AppWidgetManager.ACTION_APPWIDGET_UPDATE;
import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;
import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_IDS;
import static ru.buryachenko.hw_look4films.utils.Constants.PREFERENCES_SELECTED_FILM;
import static ru.buryachenko.hw_look4films.utils.Constants.WIDGET_ACTION;

public class WidgetProvider extends AppWidgetProvider {

    final static String WIDGET_ACTION_CHANGE_LIKED = "widgetChangeLiked";
    final static String WIDGET_ACTION_PICTURE = "widgetPicturePressed";
    private String widgetData = "";

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        int appWidgetId = intent.getIntExtra(EXTRA_APPWIDGET_ID, 0);
        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case WIDGET_ACTION_CHANGE_LIKED:
                    changeLikedAndSaveWidgetData(context);
                    break;
            }
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
    }

    private void loadWidgetData() {
        widgetData = SharedPreferencesOperation.load(PREFERENCES_SELECTED_FILM, "");
    }

    private void changeLikedAndSaveWidgetData(Context context) {
        loadWidgetData();
        if (!widgetData.isEmpty()) {
            boolean newVal = FilmInApp.likedFromWidgetString(widgetData);
            widgetData = widgetData.replace(Boolean.toString(newVal), Boolean.toString(!newVal));
            SharedPreferencesOperation.save(PREFERENCES_SELECTED_FILM, widgetData);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        loadWidgetData();
        class PendingInner {
            private PendingIntent pendingIntent;

            private PendingInner(String action, int widgetId) {
                Intent intent = new Intent(context, WidgetProvider.class);
                intent.setAction(action);
                intent.putExtra(EXTRA_APPWIDGET_ID, widgetId);
                intent.putExtra(EXTRA_APPWIDGET_IDS, appWidgetIds);
                pendingIntent = PendingIntent.getBroadcast(context,
                        0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            }

            private PendingIntent result() {
                return pendingIntent;
            }
        }
        ComponentName thisWidget = new ComponentName(context, WidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_like);
            remoteViews.setImageViewResource(R.id.widgetLiked, FilmInApp.likedFromWidgetString(widgetData) ? R.drawable.liked : R.drawable.notliked);
            String pictureUrl = FilmInApp.imageUrlFromWidgetString(widgetData);
            if (pictureUrl.isEmpty()) {
                Drawable p = RandomPicture.make(context.getResources().getDimensionPixelSize(R.dimen.recyclerImageWidth), context.getResources().getDimensionPixelSize(R.dimen.recyclerImageHeight));
                remoteViews.setImageViewBitmap(R.id.widgetPicture, drawableToBitmap(p));
            } else {
                AppWidgetTarget appWidgetTarget = new AppWidgetTarget(context, R.id.widgetPicture, remoteViews, appWidgetIds) {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        super.onResourceReady(resource, transition);
                    }
                };
                Glide.with(context)
                        .asBitmap()
                        .load(pictureUrl)
                        .centerCrop()
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_favorite)
                        .into(appWidgetTarget);
            }
            remoteViews.setOnClickPendingIntent(R.id.widgetLiked, new PendingInner(WIDGET_ACTION_CHANGE_LIKED, widgetId).result());
            remoteViews.setOnClickPendingIntent(R.id.widgetPicture, new PendingInner(WIDGET_ACTION_CHANGE_LIKED, widgetId).result());
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

}
