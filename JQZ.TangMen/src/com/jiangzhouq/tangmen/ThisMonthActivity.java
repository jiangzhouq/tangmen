package com.jiangzhouq.tangmen;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.jiangzhouq.tangmen.data.Constants;

public class ThisMonthActivity extends Activity{

	private ArrayList <Map<String, String>> chapters = new  ArrayList<Map<String, String>>();
	Chapter chapter = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Map<String, String> map =  new HashMap<String, String>();
		XmlPullParser parser = Xml.newPullParser();
		File file = new File(Constants.NATIVE_PATH + "/test.xml");
		if (Constants.LOG_SWITCH)
			Log.d(Constants.LOG_TAG, "file:" + file.getName());
		try{
			InputStream in = new FileInputStream(file);
			parser.setInput(in, "utf-8");
			int eventType = parser.getEventType();
			if (Constants.LOG_SWITCH)
				Log.d(Constants.LOG_TAG, "eventType:" + eventType);
			while(eventType != XmlPullParser.END_DOCUMENT){
				switch(eventType){
				case XmlPullParser.START_DOCUMENT:
					break;
					
				case XmlPullParser.START_TAG:
					String tag_name = parser.getName();
					if("chapter".equals(tag_name)){
						map = new HashMap<String, String>();
						map.put("time", new String(parser.getAttributeValue(0)));
						map.put("name", new String(parser.getAttributeValue(1)));
						
						chapter = new Chapter();
						chapter.file = new String(parser.getAttributeValue(0));
						chapter.name = new String(parser.getAttributeValue(1));
						chapter.time = parser.getText();
						if (Constants.LOG_SWITCH)
							Log.d(Constants.LOG_TAG, chapter.file + " " + chapter.name + " " + chapter.time);
					}
					break;
				case XmlPullParser.END_TAG:
					if("chapter".equals(parser.getName())){
						chapters.add(map);
					}
					break;
				}
				eventType = parser.next();
			}
		}catch (XmlPullParserException e) {  
			if (Constants.LOG_SWITCH)
				Log.d(Constants.LOG_TAG, "e:" + e);
            e.printStackTrace();  
        } catch (IOException e) {  
            if (Constants.LOG_SWITCH)
				Log.d(Constants.LOG_TAG, "e:" + e);
            e.printStackTrace();  
        }  
		setContentView(R.layout.activity_thismonth);
		ListView list = (ListView) findViewById(R.id.listview);
		list.setAdapter(new SimpleAdapter(this, (List<? extends Map<String, ?>>) chapters, R.layout.listview_item, new String[]{ "name", "time"} , new int[]{R.id.name, R.id.time}));
	}
	class listAdapter extends SimpleAdapter{

		public listAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
			// TODO Auto-generated constructor stub
		}
		
	}
	class Chapter{
		public String file;
		public String name;
		public String time;
	}
}
