package edu.vanderbilt.mccormick.widgetdemo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class StockTickerWidgetProvider extends AppWidgetProvider {

	Thread updaterThread = null;
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		// this will cause the actual scrolling to happen
		if (updaterThread == null) {
			updaterThread = new StockTickerScrollThread(context, appWidgetManager, appWidgetIds);
			updaterThread.start();
		}
		// and here we will start the intentService
		Intent i = new Intent(context, StockTickerUpdateService.class);
		SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		Set<String> symbolSet = sp.getStringSet("symbols", null);
		ArrayList<String> symbols = new ArrayList<String>(symbolSet);
		i.putStringArrayListExtra("symbols", symbols);
		context.startService(i);
	}
	
	@Override
	public void onEnabled(Context context) {
		SharedPreferences sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		SharedPreferences.Editor spEdit = sp.edit();
		Set<String> symbols = new HashSet<String>();
		symbols.add("GOOG");
		symbols.add("FB");
		symbols.add("YHOO");
		spEdit.putStringSet("symbols", symbols);
		spEdit.commit();
	}
}
