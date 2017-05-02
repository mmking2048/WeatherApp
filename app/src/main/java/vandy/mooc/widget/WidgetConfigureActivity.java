package vandy.mooc.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;

import vandy.mooc.R;
import vandy.mooc.common.Utils;

/**
 * Created by Yang on 1/20/2016.
 */
public class WidgetConfigureActivity extends Activity {

    // SharedPreferences file name
    private static final String PREF_STRING = "WeatherAppPrefs";

    // Key for saving the location in SharedPreferences
    // To make sure there is a distinct key for each widget,
    // add the widget ID to default template
    private final String keyTemplate = "location_widget";
    private String key;

    private int mAppWidgetId;

    private EditText setLocation;
    private Button cancelButton;
    private Button submitButton;

    private static final String TAG = "WidgetConfigActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        // Set default result code to RESULT_CANCELED
        setResult(RESULT_CANCELED);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        setContentView(R.layout.configure_widget_activity);
        setLocation = (EditText) findViewById(R.id.configure_location);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        submitButton = (Button) findViewById(R.id.submit_button);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "cancel");

                // Dismiss the activity
                finish();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "submit");

                Context context = WidgetConfigureActivity.this.getApplicationContext();

                String location = Utils.uppercaseInput(
                        context,
                        setLocation.getText().toString().trim(),
                        true);

                if (location != null) {
                    // Get the key for storing location
                    key = keyTemplate + mAppWidgetId;

                    // Get SharedPreferences
                    SharedPreferences settings = getSharedPreferences(PREF_STRING, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(key, location);

                    // Commit the edits
                    editor.commit();

                    // Get instance of AppWidgetManager
                    AppWidgetManager appWidgetManager =
                            AppWidgetManager.getInstance(context);

                    // Update the widget
                    RemoteViews remoteViews = new RemoteViews(
                            context.getPackageName(),
                            R.layout.widget_layout);
                    remoteViews.setTextViewText(R.id.widget_location, location);
                    appWidgetManager.updateAppWidget(mAppWidgetId, remoteViews);

                    // Beg for an update
                    Intent intent = new Intent(context, WeatherWidgetProvider.class);
                    intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                    int[] ids = { mAppWidgetId };
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                    sendBroadcast(intent);

                    // Set the return intent
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }
}
