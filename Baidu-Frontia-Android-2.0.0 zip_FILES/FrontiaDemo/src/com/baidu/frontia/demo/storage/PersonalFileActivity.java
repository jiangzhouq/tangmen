package com.baidu.frontia.demo.storage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.FrontiaUser;
import com.baidu.frontia.api.FrontiaAuthorization;
import com.baidu.frontia.api.FrontiaAuthorization.MediaType;
import com.baidu.frontia.api.FrontiaAuthorizationListener.AuthorizationListener;
import com.baidu.frontia.api.FrontiaPersonalStorage;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.FileInfoListener;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.FileInfoResult;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.FileListListener;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.FileOperationListener;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.FileProgressListener;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.FileTransferListener;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.FileUploadListener;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.QuotaListener;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.QuotaResult;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.ThumbnailListener;
import com.baidu.frontia.demo.Conf;
import com.baidu.frontia.demo.R;

public class PersonalFileActivity extends Activity {

	private FrontiaPersonalStorage mCloudStorage;
	private FrontiaAuthorization authorization;

	private TextView mResultTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ArrayList<String> list = new ArrayList<String>();
		list.add("basic");
		list.add("netdisk");
		mCloudStorage = Frontia.getPersonalStorage();
		authorization = Frontia.getAuthorization();
		authorization.authorize(this,MediaType.BAIDU.toString(),list,new AuthorizationListener() {
			
			@Override
			public void onSuccess(FrontiaUser arg0) {
				Frontia.setCurrentAccount(arg0);
				setupViews();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				TextView view = new TextView(PersonalFileActivity.this);
				view.setText("登录百度账号失败，错误为:"+arg0+""+arg1+"，只有登录百度账号才能使用个人文件存储功能，请返回重新尝试登录");
				setContentView(view);
			}
			
			@Override
			public void onCancel() {
				TextView view = new TextView(PersonalFileActivity.this);
				view.setText("只有登录百度账号才能使用个人文件存储功能，请返回重新尝试登录");
				setContentView(view);
			}
		});
	}
	
	private void setupViews() {
		setContentView(R.layout.personal_file_storage);

		mResultTextView = (TextView) findViewById(R.id.personalFileResult);

		Button createDirButton = (Button) findViewById(R.id.createDir);
		createDirButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				createDir();
			}

		});

        Button deleteDirButton = (Button) findViewById(R.id.deleteDir);
        deleteDirButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                deleteDir();
            }
        });

		Button uploadFileButton = (Button) findViewById(R.id.uploadPersonalFile);
		uploadFileButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				uploadFile();
			}

		});

		Button stopUploadFileButton = (Button) findViewById(R.id.stopUploadPersonalFile);
		stopUploadFileButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				stopUploadFile();
			}

		});

		Button downloadFileButton = (Button) findViewById(R.id.downloadPersonalFile);
		downloadFileButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				downloadFile();
			}

		});

		Button downloadStreamFileButton = (Button) findViewById(R.id.downloadPersonalStreamFile);
		downloadStreamFileButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				downloadStreamFile();
			}

		});

		Button stopDownloadFileButton = (Button) findViewById(R.id.stopDownloadPersonalFile);
		stopDownloadFileButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				stopDownloadFile();
			}

		});

		Button deleteFileButton = (Button) findViewById(R.id.deletePersonalFile);
		deleteFileButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteFile();
			}

		});

		Button listButton = (Button) findViewById(R.id.personalList);
		listButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				list();
			}

		});

		Button imageListButton = (Button) findViewById(R.id.personalImageList);
		imageListButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				imageList();
			}

		});

		Button videoListButton = (Button) findViewById(R.id.personalVideoList);
		videoListButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				videoList();
			}

		});

		Button audioListButton = (Button) findViewById(R.id.personalAudioList);
		audioListButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				audioList();
			}

		});

		Button docListButton = (Button) findViewById(R.id.personalDocList);
		docListButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				docList();
			}

		});

		Button quotaButton = (Button) findViewById(R.id.personalQuota);
		quotaButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				quota();
			}

		});

		Button thumbnailButton = (Button) findViewById(R.id.personalThumbnail);
		thumbnailButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				thumbnail();
			}

		});

	}

	protected void createDir() {
		mCloudStorage.makeDir(
				Conf.PERSON_STORAGE_DIR_NAME,
				new FileInfoListener() {

					@Override
					public void onSuccess(FileInfoResult result) {
						StringBuilder sb = new StringBuilder();
						sb.append("create dir success\n")
								.append(result.getPath())
								.append('\n')
								.append("size: ")
								.append(result.getSize())
								.append('\n')
								.append("modified time: ")
								.append(new Date(result.getModifyTime() * 1000));
						if (null != mResultTextView) {
							mResultTextView.setText(sb.toString());
						}
					}

					@Override
					public void onFailure(int errCode, String errMsg) {
						if (null != mResultTextView) {
							mResultTextView.setText("errCode:" + errCode
									+ ", errMsg:" + errMsg);
						}
					}
				});
	}

    private void deleteDir(){
        mCloudStorage.deleteFile(Conf.PERSON_STORAGE_DIR_NAME, new FileOperationListener() {
            @Override
            public void onSuccess(String s) {
                mResultTextView.setText(Conf.PERSON_STORAGE_DIR_NAME + " deleted");
            }

            @Override
            public void onFailure(String s, int errCode, String errMsg) {
                mResultTextView.setText("errCode:" + errCode
                        + ", errMsg:" + errMsg);
            }
        });
    }

	protected void uploadFile() {

        mCloudStorage.uploadFile(Conf.LOCAL_FILE_NAME,
				Conf.PERSON_STORAGE_FILE_NAME,
				new FileProgressListener() {

					@Override
					public void onProgress(String source, long bytes, long total) {
						if (null != mResultTextView) {
							mResultTextView.setText(source + " upload......:"
									+ bytes * 100 / total + "%");
						}

					}

				}, new FileUploadListener() {

					@Override
					public void onSuccess(String source,
							FileInfoResult result) {
						StringBuilder sb = new StringBuilder();
						sb.append(source)
								.append(':')
								.append(result.getPath())
								.append('\n')
								.append("size: ")
								.append(result.getSize())
								.append('\n')
								.append("modified time: ")
								.append(new Date(result.getModifyTime()*1000));
						if (null != mResultTextView) {
							mResultTextView.setText(sb.toString());
						}

					}

					@Override
					public void onFailure(String source, int errCode,
							String errMsg) {
						if (null != mResultTextView) {
							mResultTextView.setText(source + " errCode:"
									+ errCode + ", errMsg:" + errMsg);
						}

					}

				});
    }

	protected void stopUploadFile() {
		mCloudStorage.stopTransferring(Conf.LOCAL_FILE_NAME,
				Conf.PERSON_STORAGE_FILE_NAME);
	}

	protected void downloadFile() {
		mCloudStorage.downloadFile(Conf.PERSON_STORAGE_FILE_NAME,
				Conf.LOCAL_FILE_NAME,
				new FileProgressListener() {

					@Override
					public void onProgress(String source, long bytes, long total) {
						if (null != mResultTextView) {
							mResultTextView.setText(source + " download......:"
									+ bytes * 100 / total + "%");
						}
					}

				}, new FileTransferListener() {

					@Override
					public void onSuccess(String source, String newTargetName) {
						if (null != mResultTextView) {
							mResultTextView.setText(source + " downloaded as "
									+ newTargetName + " in the local.");
						}
					}

					@Override
					public void onFailure(String source, int errCode,
							String errMsg) {
						if (null != mResultTextView) {
							mResultTextView.setText(source + " errCode:"
									+ errCode + ", errMsg:" + errMsg);
						}
					}

				});
	}

	protected void downloadStreamFile() {
		mCloudStorage.downloadFileFromStream(
				Conf.PERSON_STORAGE_FILE_NAME, Conf.LOCAL_FILE_NAME,
				new FileProgressListener() {

					@Override
					public void onProgress(String source, long bytes, long total) {
						if (null != mResultTextView) {
							mResultTextView.setText(source + " download......:"
									+ bytes * 100 / total + "%");
						}

					}

				}, new FileTransferListener() {

					@Override
					public void onSuccess(String source, String newTargetName) {
						if (null != mResultTextView) {
							mResultTextView.setText(source + " downloaded as "
									+ newTargetName + " in the local.");
						}

					}

					@Override
					public void onFailure(String source, int errCode,
							String errMsg) {
						if (null != mResultTextView) {
							mResultTextView.setText(source + " errCode:"
									+ errCode + ", errMsg:" + errMsg);
						}

					}

				});
	}

	protected void stopDownloadFile() {
		mCloudStorage.stopTransferring(Conf.PERSON_STORAGE_FILE_NAME,
				Conf.LOCAL_FILE_NAME);
	}

	protected void deleteFile() {
		mCloudStorage.deleteFile(Conf.PERSON_STORAGE_FILE_NAME,
				new FileOperationListener() {

					@Override
					public void onSuccess(String source) {
						if (null != mResultTextView) {
							mResultTextView.setText(source + " is deleted");
						}
					}

					@Override
					public void onFailure(String source, int errCode,
							String errMsg) {
						if (null != mResultTextView) {
							mResultTextView.setText(source + " errCode:"
									+ errCode + ", errMsg:" + errMsg);
						}

					}

				});
	}

	protected void list() {
		mCloudStorage.list(Conf.PERSON_STORAGE_DIR_NAME, null,
				null, new FileListListener() {

					@Override
					public void onSuccess(List<FileInfoResult> result) {
						StringBuilder sb = new StringBuilder();
						for (FileInfoResult info : result) {
							sb.append(info.getPath())
									.append('\n')
									.append("size: ")
									.append(info.getSize())
									.append('\n')
									.append("modified time: ")
									.append(new Date(info.getModifyTime()*1000)
											.toString()).append('\n');
						}
						if (null != mResultTextView) {
							mResultTextView.setText(sb.toString());
						}
					}

					@Override
					public void onFailure(int errCode, String errMsg) {
						if (null != mResultTextView) {
							mResultTextView.setText("errCode:" + errCode
									+ ", errMsg:" + errMsg);
						}
					}
				});
	}

	protected void imageList() {
		mCloudStorage.imageStream(
				new FileListListener() {

					@Override
					public void onSuccess(List<FileInfoResult> result) {
						StringBuilder sb = new StringBuilder();
						for (FileInfoResult info : result) {
							sb.append(info.getPath())
									.append('\n')
									.append("size: ")
									.append(info.getSize())
									.append('\n')
									.append("modified time: ")
									.append(new Date(info.getModifyTime())
											.toString()).append('\n');

						}
						if (null != mResultTextView) {
							mResultTextView.setText(sb.toString());
						}
					}

					@Override
					public void onFailure(int errCode, String errMsg) {
						if (null != mResultTextView) {
							mResultTextView.setText("errCode:" + errCode
									+ ", errMsg:" + errMsg);
						}
					}
				});
	}

	protected void videoList() {
		mCloudStorage.videoStream(
				new FileListListener() {

					@Override
					public void onSuccess(List<FileInfoResult> result) {
						StringBuilder sb = new StringBuilder();
						for (FileInfoResult info : result) {

							sb.append(info.getPath())
									.append('\n')
									.append("size: ")
									.append(info.getSize())
									.append('\n')
									.append("modified time: ")
									.append(new Date(info.getModifyTime())
											.toString()).append('\n');

						}
						if (null != mResultTextView) {
							mResultTextView.setText(sb.toString());
						}
					}

					@Override
					public void onFailure(int errCode, String errMsg) {
						if (null != mResultTextView) {
							mResultTextView.setText("errCode:" + errCode
									+ ", errMsg:" + errMsg);
						}
					}
				});
	}

	protected void audioList() {
		mCloudStorage.audioStream(
				new FileListListener() {

					@Override
					public void onSuccess(List<FileInfoResult> result) {
						StringBuilder sb = new StringBuilder();
						for (FileInfoResult info : result) {
							sb.append(info.getPath())
									.append('\n')
									.append("size: ")
									.append(info.getSize())
									.append('\n')
									.append("modified time: ")
									.append(new Date(info.getModifyTime())
											.toString()).append('\n');

						}
						if (null != mResultTextView) {
							mResultTextView.setText(sb.toString());
						}
					}

					@Override
					public void onFailure(int errCode, String errMsg) {
						if (null != mResultTextView) {
							mResultTextView.setText("errCode:" + errCode
									+ ", errMsg:" + errMsg);
						}
					}
				});
	}

	protected void docList() {
		mCloudStorage.docStream(
				new FileListListener() {

					@Override
					public void onSuccess(List<FileInfoResult> result) {
						StringBuilder sb = new StringBuilder();
						for (FileInfoResult info : result) {
							sb.append(info.getPath())
									.append('\n')
									.append("size: ")
									.append(info.getSize())
									.append('\n')
									.append("modified time: ")
									.append(new Date(info.getModifyTime())
											.toString()).append('\n');

						}
						if (null != mResultTextView) {
							mResultTextView.setText(sb.toString());
						}
					}

					@Override
					public void onFailure(int errCode, String errMsg) {
						if (null != mResultTextView) {
							mResultTextView.setText("errCode:" + errCode
									+ ", errMsg:" + errMsg);
						}
					}
				});
	}

	protected void quota() {

		mCloudStorage.quota(
				new QuotaListener() {

					@Override
					public void onSuccess(QuotaResult result) {
						StringBuilder sb = new StringBuilder();
						sb.append("total: ").append(result.getTotal())
								.append('\n').append("used: ")
								.append(result.getUsed()).toString();
						if (null != mResultTextView) {
							mResultTextView.setText(sb.toString());
						}
					}

					@Override
					public void onFailure(int errCode, String errMsg) {
						if (null != mResultTextView) {
							mResultTextView.setText("errCode:" + errCode
									+ ", errMsg:" + errMsg);
						}
					}
				});
	}

	protected void thumbnail() {
		mCloudStorage.thumbnail(Conf.PERSON_STORAGE_FILE_NAME, 10,
				10, 10,
				new ThumbnailListener() {

					@Override
					public void onSuccess(Bitmap bitmap) {
						if (null != mResultTextView) {
							mResultTextView.setText(bitmap.toString());
						}
					}

					@Override
					public void onFailure(int errCode, String errMsg) {
						if (null != mResultTextView) {
							mResultTextView.setText("errCode:" + errCode
									+ ", errMsg:" + errMsg);
						}
					}
				});
	}
}
