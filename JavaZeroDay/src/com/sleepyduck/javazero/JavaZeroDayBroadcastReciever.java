package com.sleepyduck.javazero;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Calendar;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

public class JavaZeroDayBroadcastReciever extends BroadcastReceiver {
	private class TimeAsynchTask extends AsyncTask<Object, Object, Calendar> {

		@Override
		protected Calendar doInBackground(final Object... params) {
			Log.i("JavaZeroDay", "doInBackground");
			HttpURLConnection urlConnection;
			try {
				urlConnection = (HttpURLConnection) new URL("http://java-0day.com/").openConnection();
				urlConnection.setConnectTimeout(2000);
				urlConnection.setDoOutput(false);

				final BufferedReader in = new BufferedReader(new InputStreamReader(
						urlConnection.getInputStream()));
				String line;
				while ((line = in.readLine()) != null) {
					Log.i("JavaZeroDay", "readLine: " + line);
					if (line.contains("var lastzeroday")) {
						Log.i("JavaZeroDay", "Line Accepted");
						final String[] lines = line.split("\"");
						Log.i("JavaZeroDay", "Line split:");
						for (final String s : lines)
							Log.i("JavaZeroDay", "\t" + s);
						if (lines.length > 2) {
							return parseDate(lines[1]);
						}
					}
				}

				urlConnection.disconnect();
			} catch (final SocketTimeoutException e) {
				e.printStackTrace();
			} catch (final Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(final Calendar result) {
			if (result != null) {
				mCalendar = result;
				Log.i("JavaZeroDay", "Got Calendar: " + result);
			} else {
				Log.i("JavaZeroDay", "Got no Calendar!");
				mRequested = false;
			}
		}

		private int extractDay(final String string) {
			return Integer.parseInt(string.split("[\\s,]")[1]);
		}

		private int extractHour(final String string) {
			return Integer.parseInt(string.split("[\\s:]")[3]);
		}

		private int extractMinute(final String string) {
			return Integer.parseInt(string.split("[\\s:]")[4]);
		}

		private int extractMonth(final String string) {
			final String month = string.split("\\s")[0];
			if (month.equals("January"))
				return 0;
			if (month.equals("Febuary"))
				return 1;
			if (month.equals("March"))
				return 2;
			if (month.equals("April"))
				return 3;
			if (month.equals("May"))
				return 4;
			if (month.equals("June"))
				return 5;
			if (month.equals("July"))
				return 6;
			if (month.equals("August"))
				return 7;
			if (month.equals("September"))
				return 8;
			if (month.equals("October"))
				return 9;
			if (month.equals("November"))
				return 10;
			if (month.equals("December"))
				return 11;
			return 0;
		}

		private int extractSecond(final String string) {
			return Integer.parseInt(string.split("[\\s:]")[5]);
		}

		private int extractYear(final String string) {
			return Integer.parseInt(string.split("\\s")[2]);
		}

		private Calendar parseDate(final String string) {
			final Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, extractYear(string));
			calendar.set(Calendar.MONTH, extractMonth(string));
			calendar.set(Calendar.DAY_OF_MONTH, extractDay(string));
			calendar.set(Calendar.HOUR_OF_DAY, extractHour(string));
			calendar.set(Calendar.MINUTE, extractMinute(string));
			calendar.set(Calendar.SECOND, extractSecond(string));
			calendar.set(Calendar.MILLISECOND, 0);
			return calendar;
		}
	}

	private static final long DAY = 86400000L;
	private static final long HOUR = 3600000;
	private static final long MINUTE = 60000;

	private static final long SECOND = 1000;
	private static Calendar mCalendar = null;
	private static boolean mRequested = false;

	private long getDays(final long millis) {
		return millis / DAY;
	}

	private long getHours(final long millis) {
		return (millis % DAY) / HOUR;
	}

	private long getMinutes(final long millis) {
		return (millis % HOUR) / MINUTE;
	}

	private long getSeconds(final long millis) {
		return (millis % MINUTE) / SECOND;
	}

	private long getTimseSinceLastZeroDay() {
		if (mCalendar == null || !mRequested) {
			requestTime();
		} else {
			return System.currentTimeMillis() - mCalendar.getTimeInMillis();
		}
		return 0;
	}

	private void requestTime() {
		mRequested = true;
		new TimeAsynchTask().execute();
	}

	@Override
	public void onReceive(final Context context, final Intent intent) {
		Log.i("JavaZeroDay", "onReceive");
		final RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.java_zero_day_appwidget);
		final ComponentName watchWidget = new ComponentName(context, JavaZeroDayAppWidgetProvider.class);

		final long millis = getTimseSinceLastZeroDay();
		remoteViews.setTextViewText(R.id.EditText, String.format("%dd - %d:%02d:%02d", getDays(millis),
				getHours(millis), getMinutes(millis), getSeconds(millis)));
		AppWidgetManager.getInstance(context).updateAppWidget(watchWidget, remoteViews);
	}

}
