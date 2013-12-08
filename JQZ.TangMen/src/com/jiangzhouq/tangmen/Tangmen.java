package com.jiangzhouq.tangmen;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.FrontiaFile;
import com.baidu.frontia.api.FrontiaStorage;
import com.baidu.frontia.api.FrontiaStorageListener.FileListListener;
import com.baidu.frontia.api.FrontiaStorageListener.FileProgressListener;
import com.baidu.frontia.api.FrontiaStorageListener.FileTransferListener;
import com.jiangzhouq.tangmen.data.Constants;
import com.suredigit.inappfeedback.FeedbackDialog;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;

public class Tangmen extends FragmentActivity {
	private static final boolean LOG_SWITCH = Constants.LOG_SWITCH;
	private static final String LOG_TAG = Constants.LOG_TAG;
	FrontiaStorage mCloudStorage;
	ArrayList<FrontiaFile> mTotalFile = new ArrayList<FrontiaFile>();
	FrontiaFile mFile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tangmen);
//		boolean isInit = Frontia.init(getApplicationContext(),
//				"7q6l1grGD7pYFoDG3pEKTxwE");
//		if (isInit) {
//			if (LOG_SWITCH)
//				Log.d(LOG_TAG, "Frontia init successfully!");
//		}
//		mCloudStorage = Frontia.getStorage();
//		list();
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
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.tangmen, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.action_settings:
				FeedbackDialog feedBackDialog = new FeedbackDialog(this, "AF-8565EE971698-9C");
				feedBackDialog.show();
				break;
			case R.id.action_feedback:
				FeedbackAgent agent = new FeedbackAgent(Tangmen.this);
				agent.startFeedbackActivity();
				
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	protected void list() {
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
				if (LOG_SWITCH)
					Log.d(LOG_TAG, "" + sb + "mTotalFile.size:" + mTotalFile.size());
				if(mTotalFile.size() > 0){
					for(int i =0; i < mTotalFile.size(); i++){
						if (LOG_SWITCH)
							Log.d(LOG_TAG, "Start to download RemotePath" + mTotalFile.get(i).getRemotePath() + " Native path:" + mTotalFile.get(i).getNativePath());
						downloadFile(mTotalFile.get(i));
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
}
