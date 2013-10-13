package edu.vanderbilt.mccormick.widgetdemo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class StockTickerUpdateService extends IntentService {

	public StockTickerUpdateService(String name) {
		super(name);
	}
	
	public StockTickerUpdateService() {
		this("service");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		try {
			List<String> listSymbols = intent.getStringArrayListExtra("symbols");
			String urlString = "http://download.finance.yahoo.com/d/quotes.csv?s=";
			for (String symbol : listSymbols) {
				Log.i("TAG", symbol);
				urlString += symbol + ","; // extra ',' won't matter
			}
			urlString += "&f=l1&e=.csv";
			Log.i("TAG", urlString);
			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();
			String prices = convertStreamToString(conn.getInputStream(), "UTF-8");
			Log.i("TAG", prices);
			String[] price = prices.split("\\r?\\n");
			String scrollString = "";
			for (int i = 0; i < price.length; ++i) {
				scrollString += listSymbols.get(i) + ":" + price[i].trim() + " - ";
			}
			StockTickerScrollThread.stringToScroll = scrollString;
		} catch (MalformedURLException e) {
			// This is just an example :-)
			e.printStackTrace();
		} catch (IOException e) {
			// This is just an example :-)
			e.printStackTrace();
		}
		
	}
	
	// from: http://stackoverflow.com/questions/3479112/java-string-from-inputstream
	public static String convertStreamToString( InputStream is, String ecoding ) throws IOException
	{
	    StringBuilder sb = new StringBuilder( Math.max( 16, is.available() ) );
	    char[] tmp = new char[ 4096 ];

	    try {
	       InputStreamReader reader = new InputStreamReader( is, ecoding );
	       for( int cnt; ( cnt = reader.read( tmp ) ) > 0; )
	            sb.append( tmp, 0, cnt );
	    } finally {
	        is.close();
	    }
	    return sb.toString();
	}

}
