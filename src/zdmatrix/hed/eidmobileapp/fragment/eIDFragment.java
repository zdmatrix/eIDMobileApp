package zdmatrix.hed.eidmobileapp.fragment;

import mafei.hed.nfcapplication.NFCApplication;
import mafei.hed.nfcapplication.NFCMsgCode;
import zdmatrix.hed.eid.eidmobileapp.R;
import zdmatrix.hed.eidmobileapp.data.StaticData;
import zdmatrix.hed.eidmobileapp.functionmoudle.FunctionMoudle;

import android.R.integer;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class eIDFragment extends Fragment{
	
	Button btnReturn;
	Button btneIDVerify;
	
	TextView tvBeVerifyedData;
	TextView tvVerifyedData;
	TextView tvShow;
	ScrollView scv;
	
	ImageView imgDisplay;
	ImageView imgTitle;
	Bitmap bitmap;
	
	Handler handler;
	
	int nRet;
	String[] resault;
	String strRandom;
	String strRetRandom;
	String strDisData;
	String strShow;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_eid, container, false);
		
		btnReturn = (Button)view.findViewById(R.id.btnReturn);
		btnReturn.setOnClickListener(new ClickEvent());
		
		btneIDVerify = (Button)view.findViewById(R.id.btnRecharge);
		btneIDVerify.setOnClickListener(new ClickEvent());
		
		scv = (ScrollView)view.findViewById(R.id.scveCash);
		tvBeVerifyedData = (TextView)view.findViewById(R.id.tvBeVerifyedData);
		tvVerifyedData = (TextView)view.findViewById(R.id.tvVerifyedData);
		tvShow = (TextView)view.findViewById(R.id.tveCashShow);
		tvShow.setMovementMethod(ScrollingMovementMethod.getInstance());
		
		imgDisplay = (ImageView)view.findViewById(R.id.imgDisplay);
		Resources resources = view.getResources();
		imgDisplay.setImageDrawable(resources.getDrawable(R.drawable.logo));
		
		imgTitle = (ImageView)view.findViewById(R.id.imgTitle);
		imgTitle.setImageDrawable(resources.getDrawable(R.drawable.eidtitle));
		
		handler = new Handler();
		resault = new String[]{"", ""};
		strShow = new String("");
		
		nRet = NFCApplication.isSupportNFC(getActivity());
		nRet = NFCApplication.isSupportNFC(getActivity());
		if(nRet != NFCMsgCode.nSUPPORT_NFC){
			resault = FunctionMoudle.ErrorProcess(nRet);
			handler.post(runnableDisWarning);
		}
		return view;
	}
	
	public class ClickEvent implements View.OnClickListener{
		@Override
		public void onClick(View v){
			if(v == btnReturn){
				MainFragment mainFragment = new MainFragment();
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();
			}else{
				if(v == btneIDVerify){
					nRet = NFCApplication.isConnectTag(getActivity().getIntent());
					if(nRet == NFCMsgCode.nTAG_CONNECT){
						strShow = "";
						new VerifyThread().start();
					}else{
						resault = FunctionMoudle.ErrorProcess(nRet);
						handler.post(runnableDisWarning);
					}
				}
			}
		}
	}
	
	public class VerifyThread extends Thread{
		@Override
		public void run(){
			handler.post(runnableDisableOtherButton);
			resault[StaticData.nSW] = "";
			
			Show("认证过程开始！\r\n\r\n");
			
			byte[] r = new byte[8];
			strRandom = "";
			for(int i = 0; i < r.length; i ++){
				r[i] = (byte)(Math.random() * 10);
				strRandom += String.format("%1$1x", (byte)r[i]);
			}
			handler.post(runnableDisRandom);
//			resault = FunctionMoudle.DisplayOnCard(Integer.parseInt(strRandom, 10), 0, false);
			resault = FunctionMoudle.Display(strRandom, null, false);
			if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
				Verify();
			}else{
				handler.post(runnableDisWarning);
			}
			handler.post(runnableEnableOtherButton);
		}
	}

	
	
	Runnable runnableDisWarning = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			AlertDialog dlg = new AlertDialog.Builder(getActivity())
            .setTitle("提示信息")
            .setMessage(resault[StaticData.nSW])      
            .setView(null)//设置自定义对话框的样式
            .setPositiveButton("确定", //设置"确定"按钮
            new DialogInterface.OnClickListener() //设置事件监听
            {
                public void onClick(DialogInterface dialog, int whichButton) 
                {
                	;
                }
            })
            .create();//创建            
            dlg.show();//显示
		}
	};

	/*
	Runnable runnableVerify = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			AlertDialog dlg = new AlertDialog.Builder(getActivity())
			.setTitle("提示信息")
			.setMessage("确认手机上的待认证数据和卡上显示的数据一致\r\n确认后请按下卡上按钮进行认证")
			.setPositiveButton("确定", 
			new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					new Thread(){
						public void run(){
							handler.post(runnableDisableOtherButton);
							resault = FunctionMoudle.WaitCardButtonPushed();
							if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
								resault = FunctionMoudle.eIDPersonalized();
								if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
									resault = FunctionMoudle.eIDAuthen();
									if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
										byte[] r = new byte[8];
										strRetRandom = "";
										for(int i = 0; i < r.length; i ++){
											r[i] = (byte)(Math.random() * 10);
//											if(r[i] > 9){
//												strDisData += "0";
//											}else{
//												strDisData += String.format("%1$1x", (byte)r[i]);
//											}
											strRetRandom += String.format("%1$1x", (byte)r[i]);
										}
										handler.post(runnableDisRetRandom);
//										strRetRandom = strDisData;
										resault = FunctionMoudle.DisplayOnCard(Integer.parseInt(strRandom, 10), Integer.parseInt(strRetRandom, 10), true);
										resault[StaticData.nSW] = " 认证成功!";
																				
									}
								}
							}
							handler.post(runnableDisWarning);
							handler.post(runnableEnableOtherButton);
						}
					}.start();
				}
			})
			.setNegativeButton("取消",
            new DialogInterface.OnClickListener(){
            	public void onClick(DialogInterface dialog, int whichButton){
            		;
            	}
            })
            .create();
			
			Window window = dlg.getWindow();
			window.setGravity(Gravity.BOTTOM);
			
            dlg.show();
		}
	};
	*/
	
	public void Verify(){
		Show("确认手机上的待认证数据和卡上显示的数据一致\r\n确认后请按下卡上按钮进行认证\r\n等待卡上按键按下……\r\n");
		handler.post(runnableDisableOtherButton);
		resault = FunctionMoudle.WaitCardButtonPushed();
		if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
			Show(" 卡上按键按下\r\n开始卡个人化……\r\n");
			resault = FunctionMoudle.eIDPersonalized();
			
			if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
				Show(" 卡个人化完成\r\n开始eID认证……\r\n");
				resault = FunctionMoudle.eIDAuthen();
				if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
					Show(" eID认证成功！\r\n");
					byte[] r = new byte[8];
					strRetRandom = "";
					for(int i = 0; i < r.length; i ++){
						r[i] = (byte)(Math.random() * 10);
						strRetRandom += String.format("%1$1x", (byte)r[i]);
					}
					handler.post(runnableDisRetRandom);

					resault = FunctionMoudle.DisplayOnCard(Integer.parseInt(strRandom, 10), Integer.parseInt(strRetRandom, 10), true);
					resault[StaticData.nSW] = " 认证成功!";
															
				}
			}else{
				Show(resault[StaticData.nSW]);
			}
		}
		
		handler.post(runnableEnableOtherButton);
	}
	
	Runnable runnableVerify = new Runnable() {
		@Override
		public void run(){
			Show("确认手机上的待认证数据和卡上显示的数据一致\r\n确认后请按下卡上按钮进行认证\r\n等待卡上按键按下……\r\n");
			handler.post(runnableDisableOtherButton);
			resault = FunctionMoudle.WaitCardButtonPushed();
			if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
				Show("卡上按键按下\r\n开始卡个人化……\r\n");
				resault = FunctionMoudle.eIDPersonalized();
				
				if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
					Show("卡个人化完成\r\n开始eID认证……\r\n");
					resault = FunctionMoudle.eIDAuthen();
					if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
						Show("eID认证成功！\r\n");
						byte[] r = new byte[8];
						strRetRandom = "";
						for(int i = 0; i < r.length; i ++){
							r[i] = (byte)(Math.random() * 10);
							strRetRandom += String.format("%1$1x", (byte)r[i]);
						}
						handler.post(runnableDisRetRandom);

						resault = FunctionMoudle.DisplayOnCard(Integer.parseInt(strRandom, 10), Integer.parseInt(strRetRandom, 10), true);
						resault[StaticData.nSW] = " 认证成功!";
																
					}
				}
			}
			
			handler.post(runnableEnableOtherButton);
		}
		
	};
	
	Runnable runnableDisableOtherButton = new Runnable() {
		
		@Override
		public void run() {
		// TODO Auto-generated method stub
			
			btnReturn.setEnabled(false);
			btneIDVerify.setEnabled(false);
		}

	};
	
	Runnable runnableEnableOtherButton = new Runnable() {
		
		@Override
		public void run() {
		// TODO Auto-generated method stub
			
			btnReturn.setEnabled(true);
			btneIDVerify.setEnabled(true);
		}

	};
	
	Runnable runnableDisRandom = new Runnable() {
		
		@Override
		public void run() {
		// TODO Auto-generated method stub
			
			tvBeVerifyedData.setText(strRandom);
			
		}

	};
	
	Runnable runnableDisRetRandom = new Runnable() {
		
		@Override
		public void run() {
		// TODO Auto-generated method stub
			tvVerifyedData.setText(strRetRandom);
		}

	};
	
	Runnable runnableShow = new Runnable() {
		
		@Override
		public void run() {
			tvShow.setText(strShow);
			scv.scrollTo(0, tvShow.getHeight());
		}
	};
	
	Runnable runnableVerifyThread = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			AlertDialog dlg = new AlertDialog.Builder(getActivity())
			.setTitle("提示信息")
			.setMessage("确认手机上的待认证数据和卡上显示的数据一致\r\n确认后请按下卡上按钮进行认证")
			.setPositiveButton("确定", 
			new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					new Thread(){
						public void run(){
							handler.post(runnableDisableOtherButton);
							resault = FunctionMoudle.WaitCardButtonPushed();
							if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
								resault = FunctionMoudle.eIDPersonalized();
								if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
									resault = FunctionMoudle.eIDAuthen();
									if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
										byte[] r = new byte[8];
										strRetRandom = "";
										for(int i = 0; i < r.length; i ++){
											r[i] = (byte)(Math.random() * 10);
//											if(r[i] > 9){
//												strDisData += "0";
//											}else{
//												strDisData += String.format("%1$1x", (byte)r[i]);
//											}
											strRetRandom += String.format("%1$1x", (byte)r[i]);
										}
										handler.post(runnableDisRetRandom);
//										strRetRandom = strDisData;
										resault = FunctionMoudle.DisplayOnCard(Integer.parseInt(strRandom, 10), Integer.parseInt(strRetRandom, 10), true);
										resault[StaticData.nSW] = " 认证成功!";
																				
									}
								}
							}
							handler.post(runnableDisWarning);
							handler.post(runnableEnableOtherButton);
						}
					}.start();
				}
			})
			.setNegativeButton("取消",
            new DialogInterface.OnClickListener(){
            	public void onClick(DialogInterface dialog, int whichButton){
            		;
            	}
            })
            .create();
			
			Window window = dlg.getWindow();
			window.setGravity(Gravity.BOTTOM);
			
            dlg.show();
		}
	};
	
	public void Show(String msg){
		strShow += msg;
		handler.post(runnableShow);
	}
}
