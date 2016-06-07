package com.zcj.wei_shi_360.receiver;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.zcj.wei_shi_360.R;
import com.zcj.wei_shi_360.service.MyService;
import com.zcj.wei_shi_360.utils.ServiceStatusUtils;
import com.zcj.wei_shi_360.utils.SystemInfoUntil;

/**
 * Implementation of App Widget functionality.
 */
public class MyAppWidget extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i=new Intent(context, MyService.class);
        context.startService(i);
        super.onReceive(context, intent);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        Intent intent=new Intent(context, MyService.class);
        context.startService(intent);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        boolean serviceRunning = ServiceStatusUtils.isServiceRunning(context, "com.zcj.wei_shi_360.service.MyService");
        if (serviceRunning){
            Intent intent=new Intent(context,MyService.class);
            context.stopService(intent);
        }
    }
}

