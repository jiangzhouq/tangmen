package com.baidu.frontia.demo.acl;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.FrontiaACL;
import com.baidu.frontia.FrontiaAccount;
import com.baidu.frontia.FrontiaData;
import com.baidu.frontia.FrontiaFile;
import com.baidu.frontia.FrontiaQuery;
import com.baidu.frontia.FrontiaRole;
import com.baidu.frontia.FrontiaRole.CommonOperationListener;
import com.baidu.frontia.FrontiaUser;
import com.baidu.frontia.api.FrontiaAuthorization;
import com.baidu.frontia.api.FrontiaAuthorization.MediaType;
import com.baidu.frontia.api.FrontiaAuthorizationListener.AuthorizationListener;
import com.baidu.frontia.api.FrontiaStorage;
import com.baidu.frontia.api.FrontiaStorageListener;
import com.baidu.frontia.api.FrontiaStorageListener.DataInfoListener;
import com.baidu.frontia.api.FrontiaStorageListener.DataOperationListener;
import com.baidu.frontia.api.FrontiaStorageListener.FileListListener;
import com.baidu.frontia.api.FrontiaStorageListener.FileOperationListener;
import com.baidu.frontia.api.FrontiaStorageListener.FileProgressListener;
import com.baidu.frontia.api.FrontiaStorageListener.FileTransferListener;
import com.baidu.frontia.demo.Conf;
import com.baidu.frontia.demo.R;

public class ACLActivity extends Activity {
	
	private final static String TAG = "ACL";
	private FrontiaAuthorization authorization;
	private FrontiaStorage storage;

	
	private TextView info;
	private Button uploadFile;
	private Button downloadFile;
	private Button deleteFile;
	private Button listFile;
	private Button insertData;
	private Button updateData;
	private Button deleteData;
	private Button findData;
	private Button createRole;
	private Button findRole;
	private Button delRole;
	
	private FrontiaAccount mUser;
	private FrontiaData[] mData;
	private FrontiaFile[] mFile;
	private FrontiaRole[] mRole;
	private TextView views[];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		authorization = Frontia.getAuthorization();
		storage = Frontia.getStorage();
		//因为因为使用acl的时候需要指定一个FrontiaAccount,所以我们这里用百度账号登录，产生一个account~~
		authorization.authorize(this, MediaType.BAIDU.toString(), new AuthorizationListener() {
			
			@Override
			public void onSuccess(FrontiaUser arg0) {
				mUser = arg0;
				Frontia.setCurrentAccount(arg0);
				setContentView(R.layout.acl);
				setupViews();
				constructData();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				Log.d(TAG,"错误码为"+arg0+",错误信息为"+arg1);
			}
			
			@Override
			public void onCancel() {
				Log.d(TAG,"cancel");
			}
		});

	}
	private void setupViews() {
		info = (TextView)findViewById(R.id.info);
 		uploadFile = (Button)findViewById(R.id.uploadFile);
 		downloadFile = (Button)findViewById(R.id.downloadFile);
 		deleteFile = (Button)findViewById(R.id.deleteFile);
 		listFile = (Button)findViewById(R.id.listFile);
		insertData = (Button)findViewById(R.id.insertData);
		updateData = (Button)findViewById(R.id.updateData);
		deleteData = (Button)findViewById(R.id.deleteData);
		findData = (Button)findViewById(R.id.findData);
		createRole = (Button)findViewById(R.id.createRole);
		findRole = (Button)findViewById(R.id.findRole);
		delRole = (Button)findViewById(R.id.delRole);
		
		
		views = new TextView[4];
        int[] viewIds = new int[]{R.id.info1, R.id.info2, R.id.info3, R.id.info4};
        for(int i=0;i<4;i++){
            views[i] = (TextView) findViewById(viewIds[i]);
        }
		
        uploadFile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				clearViews();
				uploadFile();
			}
		});
        
        downloadFile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				clearViews();
				downloadFile();
			}
		});
       deleteFile.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			clearViews();
			deleteFile();
		}
       }) ;
       listFile.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			clearViews();
			list();
		}
	});
       insertData.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			clearViews();
			insertData();
		}
	});
       updateData.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			clearViews();
			updateData();
		}
	});
       deleteData.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			clearViews();
			deleteData();
		}
	});
       findData.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			clearViews();
			findData();
		}
	});
       createRole.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			clearViews();
			createRoleWithAcl();
		}
	});
       findRole.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			clearViews();
			findRoleWithAcl();
		}
	});
       delRole.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			clearViews();
			delRoleWithAcl();
		}
	});
       
	}
	
	
	private void clearViews(){
        info.setText("");
        for(int i=0;i<4;i++){
            views[i].setText("");
        }
    }
	
	/*
	 * 构造的数据中，四个FrontiaData，四个FrontiaFile，四个FrontiaRole
	 * 权限分别为可读可写，可读不可写，可写不可读，不可读不可写
	 * 如果不给对象设置ACL,或者设置了空的ACL,则该对象权限为可读可写
	 */
	private void constructData(){
		mData = new FrontiaData[4];
		mFile = new FrontiaFile[4];
		mRole = new FrontiaRole[4];
		
		FrontiaACL rwACL = new FrontiaACL();
		rwACL.setPublicReadable(true);
		rwACL.setPublicWritable(true);
		
		FrontiaACL rACL = new FrontiaACL();
		rACL.setAccountReadable(mUser, true);
		rACL.setAccountWritable(mUser, false);
		
		FrontiaACL wACL = new FrontiaACL();
		wACL.setAccountReadable(mUser, false);
		wACL.setAccountWritable(mUser, true);
		
		FrontiaACL acl = new FrontiaACL();
		acl.setAccountReadable(mUser, false);
		acl.setAccountWritable(mUser, false);
		
		FrontiaData data = new FrontiaData();
		data.put("name", "canRW");
		data.put("age", 20);
		data.setACL(rwACL);
		mData[0] = data;
		
		data = new FrontiaData();
		data.put("name", "canR");
		data.put("age", 30);
		data.setACL(rACL);
		mData[1] = data;
		
		data = new FrontiaData();
		data.put("name", "canW");
		data.put("age", 40);
		data.setACL(wACL);
		mData[2] = data;
		
		data = new FrontiaData();
		data.put("name", "cannotRW");
		data.put("age", 50);
		data.setACL(acl);
		mData[3] = data;
		
		FrontiaFile file = new FrontiaFile();
		file.setACL(rwACL);
		mFile[0] = file;
		
		file = new FrontiaFile();
		file.setACL(rACL);
		mFile[1] = file;
		
		file = new FrontiaFile();
		file.setACL(wACL);
		mFile[2] = file;
		
		file = new FrontiaFile();
		file.setACL(acl);
		mFile[3] = file;
				
		for(int i=0;i<4;i++){
            mFile[i].setNativePath(Conf.LOCAL_FILE_NAME);
            mFile[i].setRemotePath("[" + i + "]"+Conf.APP_STORAGE_FILE_NAME);
        }
		
		FrontiaRole role1 = new FrontiaRole("role_read_notWrite");
		role1.setACL(rACL);
        FrontiaRole role2 = new FrontiaRole("role_notRead_write");
        role2.setACL(wACL);
        FrontiaRole role3 = new FrontiaRole("role_read_write");
        role3.setACL(rwACL);
        FrontiaRole role4 = new FrontiaRole("role_notRead_notWrite");
        role4.setACL(acl);
        mRole[0] = role3;
        mRole[1] = role1;
        mRole[2] = role2;
        mRole[3] = role4;
	}
	
	private void uploadFile(){
		for(int i=0;i<4;i++){
            final int idx = i;

            storage.uploadFile(mFile[i],
                    new FileProgressListener() {
                        @Override
                        public void onProgress(String source, long bytes, long total) {
                            views[idx].setText(source + " upload......:"
                                    + bytes * 100 / total + "%");
                        }
                    },
                    new FileTransferListener() {
                        @Override
                        public void onSuccess(String source, String newTargetName) {

                            mFile[idx].setRemotePath(newTargetName);
                            views[idx].setText(source + " uploaded as "
                                    + newTargetName + " in the cloud.");
                        }

                        @Override
                        public void onFailure(String source, int errCode, String errMsg) {
                            views[idx].setText(source + " errCode:"
                                    + errCode + ", errMsg:" + errMsg);
                        }
                    }
            );
        }
	}
	
	protected void downloadFile() {

		//可以成功下载两个文件，因为还有文件没有读的权限

        for(int i=0;i<4;i++){
            final int idx = i;

            storage.downloadFile(mFile[i], new FileProgressListener() {

                        @Override
                        public void onProgress(String source, long bytes, long total) {
                            views[idx].setText(source + " download......:"
                                        + bytes * 100 / total + "%");

                        }

                    }, new FileTransferListener() {

                        @Override
                        public void onSuccess(String source, String newTargetName) {

                            views[idx].setText(source + " downloaded as "
                                        + newTargetName + " in the local.");

                        }

                        @Override
                        public void onFailure(String source, int errCode,
                                              String errMsg) {
                            views[idx].setText(source + " errCode:"
                                        + errCode + ", errMsg:" + errMsg);

                        }

                    });
        }
	}

	
	protected void list() {
		//可以获取两个文件，因为还有文件没有读的权限
		storage.listFiles(new FileListListener() {

			@Override
			public void onSuccess(List<FrontiaFile> list) {
				//应该可以list出来两个文件，因为上传的四个文件中只有两个是可读的
				StringBuilder sb = new StringBuilder();
				for (FrontiaFile info : list) {

					sb.append(info.getRemotePath()).append('\n').append("size: ")
							.append(info.getSize()).append('\n')
							.append("modified time: ")
							.append(info.getModifyTime().toString())
							.append('\n')
                    .append(info.getMD5()).append('\n').append(info.isDir()).append('\n');

				}
				if (null != info) {
					info.setText(sb.toString());
				}
			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				if (null != info) {
					info.setText("errCode:" + errCode + ", errMsg:"
							+ errMsg);
				}
			}

		});

	}

	protected void deleteFile() {

		//可以删除两个文件，因为还有两个文件没有写的权限

        for(int i=0;i<4;i++){
            final int idx = i;

            storage.deleteFile(mFile[i],
                    new FileOperationListener() {

                        @Override
                        public void onSuccess(String source) {
                            views[idx].setText(source + " is deleted");
                        }

                        @Override
                        public void onFailure(String source, int errCode,
                                              String errMsg) {
                            views[idx].setText(source + " errCode:"
                                    + errCode + ", errMsg:" + errMsg);
                        }

                    });
        }
	}
	
	private void insertData(){
		for(int i=0;i<4;i++){
            final int idx = i;

            storage.insertData(mData[i],
                    new FrontiaStorageListener.DataInsertListener() {

                        @Override
                        public void onSuccess() {
                            views[idx].setText("save data success\n");
                        }

                        @Override
                        public void onFailure(int errCode, String errMsg) {
                            views[idx].setText("errCode:" + errCode
                                    + ", errMsg:" + errMsg);
                        }

                    });
        }
	}
	
	protected void deleteData() {
		//根据条件可以查到四个数据，但是具有写权限的数据只有两个，所以只能删除两个数据
		FrontiaQuery query = new FrontiaQuery();
		query.lessThan("age", 100);

		storage.deleteData(
						query,
						new DataOperationListener() {

							@Override
							public void onSuccess(long count) {
								if (null != info) {
									info.setText("delete " + count
											+ " data.");
								}
							}

							@Override
							public void onFailure(int errCode, String errMsg) {
								if (null != info) {
									info.setText("errCode:"
											+ errCode + ", errMsg:" + errMsg);
								}
							}
						});
	}

	protected void updateData() {
		//根据条件可以查到两个数据，但是具有写权限的数据只有1个，所以只能更新一个数据
        FrontiaQuery query = new FrontiaQuery();
        query.lessThan("age", 40);

		FrontiaData data = new FrontiaData();
		data.put("name", "updated");
		data.put("age", 80);
        storage.updateData(
						query,
						data,
						new DataOperationListener() {

							@Override
							public void onSuccess(long count) {
								if (null != info) {
									info.setText("update " + count
											+ " data.");
								}
							}

							@Override
							public void onFailure(int errCode, String errMsg) {
								if (null != info) {
									info.setText("errCode:"
											+ errCode + ", errMsg:" + errMsg);
								}
							}
						});
	}

	protected void findData() {
		//根据条件可以查到两个数据，因为只有两个数据具有可读权限
		FrontiaQuery query = new FrontiaQuery();

		storage.findData(query,
				new DataInfoListener() {

					@Override
					public void onSuccess(List<FrontiaData> dataList) {
						if (null != info) {
                            StringBuilder sb = new StringBuilder();
                            int i = 0;
                            for(FrontiaData d : dataList){
                                sb.append(i).append(":").append(d.toJSON().toString()).append("\n");
                            }
                            info.setText("find data\n"
									+ sb.toString());
						}
					}

					@Override
					public void onFailure(int errCode, String errMsg) {
						if (null != info) {
							info.setText("errCode:" + errCode
									+ ", errMsg:" + errMsg);
						}
					}
				});

	}
	
	private void createRoleWithAcl(){
        for(int i = 0;i < 4;i++){
        	final int idx = i;
        	mRole[i].create(new CommonOperationListener() {
				
				@Override
				public void onSuccess() {
					views[idx].setText(mRole[idx].getId()+" saved");
				}
				
				@Override
				public void onFailure(int errCode, String errMsg) {
					views[idx].setText("errCode:" + errCode
                            + ", errMsg:" + errMsg);
				}
			});
        }
        
    }
	
	private void findRoleWithAcl(){
		//两个具有可读权限的role将被查到
		FrontiaRole.fetch(new FrontiaRole.FetchRoleListener() {
            @Override
            public void onSuccess(List<FrontiaRole> roleList) {
                if (null != info) {
                    StringBuilder buf = new StringBuilder();
                    for (FrontiaRole role : roleList) {
                        buf.append(role.getId()).append("\n");

                        List<FrontiaAccount> accounts = role.getMembers();
                        for (FrontiaAccount account : accounts) {
                            buf.append("    ").append(account.getId()).append("\n");
                        }
                    }
                    info.setText(buf.toString());
                }
            }

            @Override
            public void onFailure(int errCode, String errMsg) {
                if (null != info) {
                	info.setText("errCode:" + errCode
                            + ", errMsg:" + errMsg);
                }
            }
        });

    }
	
	private void delRoleWithAcl(){
	
		//两个具有可写权限的role将被删除
		for(int i = 0;i < 4;i++){
        	final int idx = i;
        	mRole[i].delete(new CommonOperationListener() {
				
				@Override
				public void onSuccess() {
					views[idx].setText(mRole[idx].getId()+" removed");
				}
				
				@Override
				public void onFailure(int errCode, String errMsg) {
					views[idx].setText("errCode:" + errCode
                            + ", errMsg:" + errMsg);
				}
			});
        }
		
    }

}
