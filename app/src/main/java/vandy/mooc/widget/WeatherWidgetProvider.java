package vandy.mooc.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import vandy.mooc.R;

/**
 * Created by Yang on 1/19/2016.
 */
public class WeatherWidgetProvider extends AppWidgetProvider {

    private static final String TAG = "WeatherWidgetProvider";

    // Update the widget by calling a started service
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Log.d(TAG, "onUpdate");

        // Get all ids
        ComponentName thisWidget = new ComponentName(context, WeatherWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        // Build the intent to call the service
        Intent intent = new Intent(context.getApplicationContext(), WidgetUpdateService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

        // Update the widgets via the service
        context.startService(intent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "onReceive");

        if (intent.getAction() != null) {

            Bundle extras = intent.getExtras();

            if (extras != null) {
                int widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);

                if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {

                    // Get the widget id and call onUpdate
                    int[] appWidgetIds = { widgetId };
                    onUpdate(context,
                            AppWidgetManager.getInstance(context),
                            appWidgetIds);
                } else {
                    // If the app widget is invalid, ask someone else to handle it
                    super.onReceive(context, intent);
                }
            }
        } else {
            super.onReceive(context, intent);
        }
    }
}