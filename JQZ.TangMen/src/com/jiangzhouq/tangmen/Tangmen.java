package com.jiangzhouq.tangmen;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.FrontiaFile;
import com.baidu.frontia.api.FrontiaPush;
import com.baidu.frontia.api.FrontiaStorage;
import com.baidu.frontia.api.FrontiaStorageListener.FileListListener;
import com.baidu.frontia.api.FrontiaStorageListener.FileOperationListener;
import com.baidu.frontia.api.FrontiaStorageListener.FileProgressListener;
import com.baidu.frontia.api.FrontiaStorageListener.FileTransferListener;
import com.jiangzhouq.tangmen.data.Constants;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

public class Tangmen extends FragmentActivity implements OnClickListener{
	private static final boolean LOG_SWITCH = Constants.LOG_SWITCH;
	private static final String LOG_TAG = Constants.LOG_TAG;
	FrontiaStorage mCloudStorage;
	ArrayList<FrontiaFile> mTotalFile = new ArrayList<FrontiaFile>();
	FrontiaFile mFile;
	TextView showSync;
	int mDownloadFileCount = 0;
	int mTotalFileCount = 0;
	String sync_doing;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tangmen);
		sync_doing = getResources().getString(R.string.sync_doing);
		boolean isInit = Frontia.init(getApplicationContext(),
				"7q6l1grGD7pYFoDG3pEKTxwE");
		if (isInit) {
			if (LOG_SWITCH)
				Log.d(LOG_TAG, "Frontia init successfully!");
		}

		FrontiaPush mPush = Frontia.getPush();
		List<String> tags = new ArrayList<String>();
		tags.add("tangmen");
		mPush.setTags(tags);
		mPush.start();
		//check update
	    UmengUpdateAgent.update(this);
	    
	    RelativeLayout btn_month = (RelativeLayout) findViewById(R.id.btn_month);
	    btn_month.setOnClickListener(this);
	    RelativeLayout btn_total = (RelativeLayout) findViewById(R.id.btn_total);
	    btn_total.setOnClickListener(this);
	    RelativeLayout btn_settings = (RelativeLayout) findViewById(R.id.btn_settings);
	    btn_settings.setOnClickListener(this);
	}
	@Override
	protected void onResume() {
		MobclickAgent.onResume(this);
		MobclickAgent.setDebugMode(true);
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		MobclickAgent.onPause(this);
		super.onPause();
	}
	protected void list() {
		mDownloadFileCount = 0;
		mTotalFileCount = 0;
		mCloudStorage.listFiles(new FileListListener() {
			@Override
			public void onSuccess(List<FrontiaFile> list) {
				StringBuilder sb = new StringBuilder();
				for (FrontiaFile info : list) {
					String mRemotePath = info.getRemotePath();
					String mNativePath = Constants.NATIVE_PATH + mRemotePath;
					info.setNativePath(mNativePath);
					mTotalFile.add(info);
					sb.append(mRemotePath).append(" -> ").append(mNativePath).append('\n');
				}
				if(list.size() > 0 && null != showSync && showSync.VISIBLE == View.VISIBLE){
					mTotalFileCount = list.size();
					showSync.setText(String.format(sync_doing, mDownloadFileCount*100/mTotalFileCount));
				}
				if (LOG_SWITCH)
					Log.d(LOG_TAG, "mTotalFile.size:" + mTotalFile.size());
				if(mTotalFile.size() > 0){
					for(int i =0; i < mTotalFile.size(); i++){
//						if (LOG_SWITCH)
//							Log.d(LOG_TAG, "Start to download RemotePath" + mTotalFile.get(i).getRemotePath() + " Native path:" + mTotalFile.get(i).getNativePath());
						deleteFile(mTotalFile.get(i));
					}
					
				}
			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				String error = "errCode:" + errCode + ", errMsg:" + errMsg;
				if (LOG_SWITCH)
					Log.d(LOG_TAG, "" + error);
			}
		});
	}

	protected void downloadFile(FrontiaFile file) {
		mCloudStorage.downloadFile(file, new FileProgressListener() {

			@Override
			public void onProgress(String source, long bytes, long total) {
				if (LOG_SWITCH)
					Log.d(LOG_TAG, source + " download......:" + bytes * 100
							/ total + "%");

			}

		}, new FileTransferListener() {

			@Override
			public void onSuccess(String source, String newTargetName) {
				mDownloadFileCount ++;
				if(null != showSync && showSync.VISIBLE == View.VISIBLE){
					showSync.setText(String.format(sync_doing, mDownloadFileCount) + mTotalFileCount);
				}
				if (LOG_SWITCH)
					Log.d(LOG_TAG, source
							+ " downloaded as "
							+ newTargetName
							+ " in the local.\n提示：如果下载文件在本地有同名文件，则下载的文件会被按时间戳重命名哦~");

			}

			@Override
			public void onFailure(String source, int errCode, String errMsg) {
				String error = "errCode:" + errCode + ", errMsg:" + errMsg;
				if (LOG_SWITCH)
					Log.d(LOG_TAG, "" + error);

			}

		});
	}
	protected void deleteFile(FrontiaFile file) {
		mCloudStorage.deleteFile(file, new FileOperationListener() {
			
			@Override
			public void onSuccess(String source) {
				if (LOG_SWITCH)
					Log.d(LOG_TAG, "deleted " + source);
			}
			
			@Override
			public void onFailure(String arg0, int arg1, String arg2) {
				
			}
		});
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_settings:
			startActivity(new Intent(this, SettingsActivity.class));
			break;
		}
	}
}
