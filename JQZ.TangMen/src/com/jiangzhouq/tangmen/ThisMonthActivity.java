package com.jiangzhouq.tangmen;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Xml;

public class ThisMonthActivity extends Activity{

	private List<Chapter> chapters = new  ArrayList<Chapter>();
	Chapter chapter = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		XmlPullParser parser = Xml.newPullParser();
		try{
			InputStream in = null;
			parser.setInput(in, "utf-8");
			int eventType = parser.getEventType();
			while(eventType != XmlPullParser.END_DOCUMENT){
				switch(eventType){
				case XmlPullParser.START_DOCUMENT:
					break;
					
				case XmlPullParser.START_TAG:
					String tag_name = parser.getName();
					if("chapter".equals(tag_name)){
						chapter = new Chapter();
						chapter.file = new String(parser.getAttributeValue(0));
						chapter.name = new String(parser.getAttributeValue(1));
						chapter.time = parser.getText();
					}
					break;
				case XmlPullParser.END_TAG:
					if("chapter".equals(parser.getName())){
						chapters.add(chapter);
					}
					break;
				}
				eventType = parser.next();
			}
		}catch (XmlPullParserException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
	}
	class Chapter{
		public String file;
		public String name;
		public String time;
	}
}
