package vandy.mooc.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.List;

import vandy.mooc.R;
import vandy.mooc.common.Utils;
import vandy.mooc.model.aidl.WeatherData;
import vandy.mooc.model.services.WeatherServiceBase;
import vandy.mooc.utils.WeatherUtils;

/**
 * The purpose of this class is to get weather data and
 * update the widget.
 */
public class WidgetUpdateService extends WeatherServiceBase {

    // SharedPreferences file name
    private static final String PREF_STRING = "WeatherAppPrefs";

    // Key for saving the location in SharedPreferences
    // To make sure there is a distinct key for each widget,
    // add the widget ID to default template
    private final String keyTemplate = "location_widget";
    private String key;

    private static final String TAG = "service";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "WidgetUpdateService");

        Context context = this.getApplicationContext();

        AppWidgetManager appWidgetManager =
                AppWidgetManager.getInstance(this.getApplicationContext());
        int[] appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        for (int widgetId : appWidgetIds) {

            // Get key for SharedPreferences
            key = keyTemplate + widgetId;

            // Get location from SharedPreferences
            SharedPreferences settings = getSharedPreferences(PREF_STRING, 0);
            String location = settings.getString(key, null);

            if (location != null) {

                // Get updated weather info
                List<WeatherData> weatherDatas = getWeatherResults(location);

                RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                        R.layout.widget_layout);

                if (weatherDatas != null) {
                    WeatherData weatherData = weatherDatas.get(0);

                    // Set the image
                    int weatherId = (int) weatherData.getWeathers().get(0).getId();
                    remoteViews.setImageViewResource(R.id.widget_icon,
                            WeatherUtils.getArtResourceForWeatherCondition(weatherId));

                    // TODO Maybe allow users to choose celsius or farenheit during configuration?
                    // Set the text
                    final double temp = weatherData.getMain().getTemp();
                    final String tempFarhenheit =
                            WeatherUtils.formatTemperature(getApplicationContext(),
                                    temp,
                                    true)
                                    + "F";
                    remoteViews.setTextViewText(R.id.widget_weather, tempFarhenheit);

                    // Register an onClickListener
                    Intent clickIntent = new Intent(context, WeatherWidgetProvider.class);
                    clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                    clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            context, widgetId, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT
                    );
                    remoteViews.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);
                    appWidgetManager.updateAppWidget(widgetId, remoteViews);

                } else {

                    // Put retry message if we didn't get any data
                    remoteViews.setTextViewText(R.id.widget_weather, getString(R.string.widget_no_data));
                }

            } else {
                // If we couldn't get the location from SharedPreferences, we gotta do something.
                // FIXME What should we do if we can't get the location? Delete the widget?

                Log.d(TAG, "SharedPreferences not found");
                Utils.showToast(context, "Location not found");
            }
        }

        stopSelf();

        // Call to super
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
