package micromaster.galileo.edu.widgetexample;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Byron on 3/25/2017.
 */

public class ExampleAppWidgetProvider extends AppWidgetProvider {

    static Intent intent;

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context,appWidgetManager,appWidgetIds);

        //creates the intent to use with the button
        intent = new Intent(context, ExampleAppWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

        for (int appWidgetId : appWidgetIds) {
			//get the widget options
            Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
            //calls the update function
            updateWidget(context, appWidgetManager,appWidgetId,options);
        }

    }

    //method which handles when the layout changes
    @Override
    public void onAppWidgetOptionsChanged(Context context,
                                          AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {

        Log.d("DEBUG_TAG", "Changed dimensions");
        updateWidget(context, appWidgetManager, appWidgetId, newOptions);

    }

    // method used to handle the update of the widget
    public void updateWidget(Context context, AppWidgetManager appWidgetManager,

                             int appWidgetId, Bundle options) {
        // Get min width and height.
        int minWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
        int minHeight = options
                .getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
        //get the current widget layout according to the dimensions
        RemoteViews views = getRemoteViews(context, minWidth, minHeight);


        /*
        * Here is where we would handle the different configurations, such as different layouts, new views,
         * new collections and stuff like that.
        * On this example both layouts have the same views names and function so we can use the same methods for both
        * */

        //set the widget views
        views.setTextViewText(R.id.textView_name, getRandomName());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.button_name, pendingIntent);

        // Tell the AppWidgetManager to perform an update on the current app widget
        appWidgetManager.updateAppWidget(appWidgetId, views);


    }

    // gets the layout depending on the widget dimensions
    private RemoteViews getRemoteViews(Context context, int minWidth,
                                       int minHeight) {
        int rows = getCellsForSize(minHeight);
        int columns = getCellsForSize(minWidth);
        if(rows == 1)
            return new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout);
        else
            return new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout_2);

    }

    //calculates the widget rows and columns
    private static int getCellsForSize(int size) {
        int n = 2;
        while (70 * n - 30 < size) {
            ++n;
        }
        return n - 1;
    }

    //We are faking data, but this method should be a call to a ContentProvider or a DataBase to get real data
    private String getRandomName() {
        ArrayList<String> names = new ArrayList<>();
        names.add("John");
        names.add("Mary");
        names.add("Emma");
        names.add("William");
        names.add("Noah");
        names.add("Susan");
        names.add("Patricia");
        names.add("Robert");

        Random randomValue = new Random();
        int randomIndex = randomValue.nextInt(names.size());

        return names.get(randomIndex);
    }
}
