package com.jiangzhouq.tangmen;

import java.util.HashMap;
import java.util.Map;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.umeng.fb.ContactActivity;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.model.UserInfo;

public class FBContactActivity extends ContactActivity{
	private static final String KEY_UMENG_CONTACT_INFO_PLAIN_TEXT = "plain";
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		ActionBar action = getActionBar();
		action.setDisplayHomeAsUpEnabled(true);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, Menu.FIRST + 1, 0, "设置").setIcon(R.drawable.umeng_fb_tick_selector).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case android.R.id.home:
			back();
			break;
		case Menu.FIRST + 1:
			try {
				FeedbackAgent agent = new FeedbackAgent(this);
				EditText contactInfoEdit = (EditText) this
						.findViewById(R.id.umeng_fb_contact_info);
				UserInfo info = agent.getUserInfo();
				if (info == null)
					info = new UserInfo();
				Map<String, String> contact = info.getContact();
				if (contact == null)
					contact = new HashMap<String, String>();
				String contact_info = contactInfoEdit.getEditableText()
						.toString();
				contact.put(KEY_UMENG_CONTACT_INFO_PLAIN_TEXT, contact_info);
				info.setContact(contact);
				
				agent.setUserInfo(info);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			back();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	void back(){
		finish();
		// add transition animation for exit.
		if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.DONUT) {
			overridePendingTransition(
					R.anim.umeng_fb_slide_in_from_left,
					R.anim.umeng_fb_slide_out_from_right);
		}
	}
}
