package com.jiangzhouq.tangmen;

import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SettingsActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		RelativeLayout contact = (RelativeLayout) findViewById(R.id.feedback);
		contact.setOnClickListener(this);
		RelativeLayout update = (RelativeLayout) findViewById (R.id.update);
		update.setOnClickListener(this);
		ActionBar action = getActionBar();
		action.setDisplayHomeAsUpEnabled(true);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.feedback:
			startActivity(new Intent(this, FeedbackActivity.class));
			break;
		case R.id.update:
			UmengUpdateAgent.forceUpdate(this);
			UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
		        @Override
		        public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo) {
		            switch (updateStatus) {
		            case 1: // has no update
		                Toast.makeText(SettingsActivity.this, "没有更新", Toast.LENGTH_SHORT)
		                        .show();
		                break;
		            case 3: // time out
		                Toast.makeText(SettingsActivity.this, "超时", Toast.LENGTH_SHORT)
		                        .show();
		                break;
		            }
		        }
		});
			break;
		}
	}
}
