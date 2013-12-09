package com.jiangzhouq.tangmen;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.umeng.fb.ConversationActivity;

public class FeedbackActivity extends ConversationActivity{

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		ActionBar action = getActionBar();
		action.setDisplayHomeAsUpEnabled(true);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, Menu.FIRST + 1, 0, "设置").setIcon(R.drawable.umeng_fb_arrow_right).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case android.R.id.home:
			finish();
			break;
		case Menu.FIRST +1:
			Intent intent = new Intent();
			intent.setClass(this, FBContactActivity.class);
			startActivity(intent);

			// play an Activity exit and entrance animation.
			if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.DONUT) {
				overridePendingTransition(
						R.anim.umeng_fb_slide_in_from_right,
						R.anim.umeng_fb_slide_out_from_left);
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
