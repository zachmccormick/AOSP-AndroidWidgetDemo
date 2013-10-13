package edu.vanderbilt.mccormick.widgetdemo;

import edu.vanderbilt.mccormick.widgetdemo.R;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.widget.RemoteViews;

public class StockTickerScrollThread extends Thread {
	
	public static String stringToScroll = "Loading!";
	private Context context;
	private AppWidgetManager appWidgetManager;
	private int[] appWidgetIds;
	
	public StockTickerScrollThread(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		this.context = context;
		this.appWidgetManager = appWidgetManager;
		this.appWidgetIds = appWidgetIds;
	}
	
	@Override
	public void run() {
		String currentString = stringToScroll;
		int currentOffset = 0;
		int currentEnd = 0;
		while(true) {
			if (!currentString.equals(stringToScroll)) {
				currentString = stringToScroll;
				currentOffset = 0;
			}
			if (currentOffset >= stringToScroll.length())
				currentOffset = 0;
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_stockticker);
			currentEnd = Math.min(currentOffset + 23, currentString.length());
			String toShow = stringToScroll.substring(currentOffset, currentEnd);
			while (toShow.length() < 23) {
				int tempEnd = Math.min(23-toShow.length(), currentString.length());
				toShow += stringToScroll.substring(0, tempEnd);
			}
			views.setTextViewText(R.id.textView1, toShow);
			
			appWidgetManager.updateAppWidget(appWidgetIds[0], views);
			++currentOffset;
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
