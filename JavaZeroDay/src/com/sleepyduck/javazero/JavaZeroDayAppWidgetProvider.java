package com.sleepyduck.javazero;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class JavaZeroDayAppWidgetProvider extends AppWidgetProvider {

	@Override
	public void onDeleted(final Context context, final int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		Log.i("JavaZeroDay", "onDisabled");
		final AlarmManager m = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		final Intent i = new Intent(context, JavaZeroDayBroadcastReciever.class);
		final PendingIntent service = PendingIntent.getBroadcast(context, 0, i,
				PendingIntent.FLAG_CANCEL_CURRENT);
		m.cancel(service);
	}

	@Override
	public void onDisabled(final Context context) {
		super.onDisabled(context);
		Log.i("JavaZeroDay", "onDisabled");
		final AlarmManager m = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		final Intent i = new Intent(context, JavaZeroDayBroadcastReciever.class);
		final PendingIntent service = PendingIntent.getBroadcast(context, 0, i,
				PendingIntent.FLAG_CANCEL_CURRENT);
		m.cancel(service);
	}

	@Override
	public void onEnabled(final Context context) {
		super.onEnabled(context);
		Log.i("JavaZeroDay", "onEnabled");
		final AlarmManager m = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		final Intent i = new Intent(context, JavaZeroDayBroadcastReciever.class);
		final PendingIntent service = PendingIntent.getBroadcast(context, 0, i,
				PendingIntent.FLAG_CANCEL_CURRENT);
		m.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + 1000, 1000, service);
	}

	@Override
	public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Log.i("JavaZeroDay", "onUpdate");
		final AlarmManager m = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		final Intent i = new Intent(context, JavaZeroDayBroadcastReciever.class);
		final PendingIntent service = PendingIntent.getBroadcast(context, 0, i,
				PendingIntent.FLAG_CANCEL_CURRENT);
		m.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + 1000, 1000, service);
	}
}
