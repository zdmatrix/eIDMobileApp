package zdmatrix.hed.eidmobileapp.fragment;

import mafei.hed.nfcapplication.NFCApplication;
import mafei.hed.nfcapplication.NFCMsgCode;
import zdmatrix.hed.eid.eidmobileapp.R;
import zdmatrix.hed.eidmobileapp.data.StaticData;
import zdmatrix.hed.eidmobileapp.functionmoudle.FunctionMoudle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class NewKeyFragment extends Fragment{
	
	Button btnReturn;
	Button btnTransform;
	Button btnBanlance;
	
	ImageView imgSrcAccount;
	ImageView imgDstAccount;
	ImageView imgTransformerAmount;
	ImageView imgAuthCode;
	
	EditText etTransformerAmount;
	TextView tvSrcAccount;
	TextView tvDstAccount;
	TextView tvBanlance;
	TextView tvNewKey;
	
	ScrollView scvNewKey;
	
	Handler handler;
	
	Bitmap bitmapSrcAccount;
	Bitmap bitmapDstAccount;
	Bitmap bitmapTransformerAmount;
	Bitmap bitmapAuthCode;
	
	byte[] bSrcAccount;
	byte[] bDstAccount;
	byte[] bTransformerAmount;
	byte[] bAuthCode;
	
	int nRet;
	int nBanlance;
	int nOffset;
	boolean bGetBanlance;
	
	String[] resault;
	String strTransformer;
	String strAuthCode;
	String strRandom;
	String strDisDstAccount;
	String strShow;
	
 	
	@Override
	public void onCreate(Bundle saveInstancStates){
		super.onCreate(saveInstancStates);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstancStates){
		View view = inflater.inflate(R.layout.fragment_newkey, container, false);
		
		
		btnReturn = (Button)view.findViewById(R.id.btnReturn);
		btnReturn.setOnClickListener(new ClickEvent());
		
		btnTransform = (Button)view.findViewById(R.id.btnTransform);
		btnTransform.setOnClickListener(new ClickEvent());
		
//		btnOk = (Button)view.findViewById(R.id.btnOk);
//		btnOk.setOnClickListener(new ClickEvent());
		
		btnBanlance = (Button)view.findViewById(R.id.btnBanlance);
		btnBanlance.setOnClickListener(new ClickEvent());
		
		imgSrcAccount = (ImageView)view.findViewById(R.id.imgSrcAccount);
		imgDstAccount = (ImageView)view.findViewById(R.id.imgDstAccount);
		imgTransformerAmount = (ImageView)view.findViewById(R.id.imgTransformerAmount);
		imgAuthCode = (ImageView)view.findViewById(R.id.imgAuthCode);
		
		etTransformerAmount = (EditText)view.findViewById(R.id.etTransformerAmount);
		etTransformerAmount.setOnKeyListener(new EditText.OnKeyListener(){

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				strTransformer = etTransformerAmount.getText().toString();
				return false;
			}			
		});
		tvDstAccount = (TextView)view.findViewById(R.id.tvDstAccountHint);
		tvSrcAccount = (TextView)view.findViewById(R.id.tvSrcAccountHint);
		tvBanlance = (TextView)view.findViewById(R.id.tvBanlance);
		tvNewKey = (TextView)view.findViewById(R.id.tvNewKey);
		tvNewKey.setMovementMethod(ScrollingMovementMethod.getInstance());
		scvNewKey = (ScrollView)view.findViewById(R.id.scvNewKey);
		
		handler = new Handler();
		
		bSrcAccount = new byte[StaticData.nIMAGEWIDTH];
		
		nRet = 0;
		nBanlance = 0;
		resault = new String[2];
		bGetBanlance = false;
		
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
				nRet = NFCApplication.isConnectTag(getActivity().getIntent());
				if(nRet == NFCMsgCode.nTAG_CONNECT){
					if(v == btnBanlance){
						new GetBanlanceThread().start();
					}else if(v == btnTransform){
						strShow = "";
						nOffset = 0;
						new VerifyDataThread().start();
					}
				}else{
					resault = FunctionMoudle.ErrorProcess(nRet);
					handler.post(runnableDisWarning);
				}			
			}
		}
	}
	
	public class VerifyDataThread extends Thread{
		@Override
		public void run(){
			if(strTransformer == null){
				resault[StaticData.nSW] = StaticData.sTRADEDATANULL;	
				handler.post(runnableDisWarning);
			}else{
				handler.post(runnableDisableOtherButton);
				if(!bGetBanlance){
					resault = FunctionMoudle.readBanlance();
					if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
						bGetBanlance = true;
						nBanlance = Integer.parseInt(resault[StaticData.nDATA], 16);
					}else{
						handler.post(runnableDisWarning);
					}
				}
				if(bGetBanlance){
					int n = nBanlance - Integer.parseInt(strTransformer, 10);
					if(n < 0){
						resault[StaticData.nSW] = StaticData.sOVERBANLANCE;
						handler.post(runnableDisWarning);
					}else{
						strDisDstAccount = tvDstAccount.getHint().toString().substring(StaticData.nSUBACCOUNTSTARTINDEX, StaticData.nSUBACCOUNTENDINDEX);
//						resault = FunctionMoudle.DisplayOnCard(Integer.parseInt(etTransformerAmount.getText().toString(), 10), Integer.parseInt(strTransformer, 10), true);
						
						resault = FunctionMoudle.DisplayOnCard(Integer.parseInt(strTransformer, 10), Integer.parseInt(strDisDstAccount, 10), true);
							if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
    						nBanlance = n;
//    						handler.post(runnableVerifyData);
    						Transform();
        				}else{
        					handler.post(runnableDisWarning);
        				}
						
					}
				}
				handler.post(runnableEnableOtherButton);
			}
			
		}
	}
	
	
	public class GetBanlanceThread extends Thread{
		@Override
		public void run(){
			resault = FunctionMoudle.readBanlance();
			if(!resault[StaticData.nSW].equals(StaticData.sSWOK)){
				handler.post(runnableDisWarning);
				bGetBanlance = false;
			}else{
				nBanlance = Integer.parseInt(resault[StaticData.nDATA], 16);
				handler.post(runnableDisBanlance);
				bGetBanlance = true;
			}
		}
	}
	
	Runnable runnableDisImg = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub			
			bitmapSrcAccount = BitmapFactory.decodeByteArray(bSrcAccount, 0, bSrcAccount.length);
			bitmapDstAccount = BitmapFactory.decodeByteArray(bDstAccount, 0, bDstAccount.length);
			bitmapTransformerAmount = BitmapFactory.decodeByteArray(bTransformerAmount, 0, bTransformerAmount.length);
			bitmapAuthCode = BitmapFactory.decodeByteArray(bAuthCode, 0, bAuthCode.length);
			imgSrcAccount.setImageBitmap(bitmapSrcAccount);
			imgDstAccount.setImageBitmap(bitmapDstAccount);
			imgTransformerAmount.setImageBitmap(bitmapTransformerAmount);
			imgAuthCode.setImageBitmap(bitmapAuthCode);			
		}
	};
	
	Runnable runnableDisBanlance = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			tvBanlance.setText(String.format("%1$s", nBanlance));
		}
	};
	
	Runnable runnableDisWarning = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
//			tveCashResault.setText(resault);
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
	
	public void Transform(){
		new Thread(){
			public void run(){
				handler.post(runnableDisableOtherButton);
				Show("确认交易 " + strTransformer + " 元?\r\n请核对卡上显示的交易额度和目的帐号\r\n确认请按卡上按钮确认，卡片根据交易信息生成认证码\r\n");
				resault = FunctionMoudle.WaitCardButtonPushed();
				if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
					byte[] r = new byte[6];
					strRandom = "";
					for(int i = 0; i < r.length; i ++){
						r[i] = (byte)(Math.random() * 10);
						strRandom += String.format("%1$1d", (byte)r[i]);
					}					
//					resault = FunctionMoudle.DisplayOnCard(Integer.parseInt(strDisDstAccount, 10), Integer.parseInt(strRandom, 10), true);
					resault = FunctionMoudle.DisplayOnCard(nBanlance, Integer.parseInt(strRandom, 10), true);
					if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
						bSrcAccount = FunctionMoudle.getImageData(tvSrcAccount.getHint().toString());
						bDstAccount = FunctionMoudle.getImageData(tvDstAccount.getHint().toString());
						bAuthCode = FunctionMoudle.getImageData(strRandom);
						bTransformerAmount = FunctionMoudle.getImageData(strTransformer);            						
						handler.post(runnableDisImg);
//						handler.post(runnableConfirmTransfor);
						ConfirmTransform();
    				}else{
    					handler.post(runnableDisWarning);  
    				}	
					
				}else{
					handler.post(runnableDisWarning);
				}
				handler.post(runnableEnableOtherButton);            				                      	
			}
		}.start();
	}
	
	public void ConfirmTransform(){
		new Thread(){
			public void run(){
				handler.post(runnableDisableOtherButton);
				Show("请核对卡上显示的认证码与手机上图片显示一致\r\n确认交易请再次按卡上按钮确认");
				resault = FunctionMoudle.WaitCardButtonPushed();
				if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
					resault = FunctionMoudle.upDateBinData(nBanlance);
					if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
						resault = FunctionMoudle.DisplayOnCard(StaticData.nSHOWNONEONCARD, 0, false);
						if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
							resault[StaticData.nSW] = StaticData.sTRADEDONE;
						}           						
					}					
				}
				handler.post(runnableDisWarning); 
				handler.post(runnableEnableOtherButton);
			}
		}.start();
	}
	
	Runnable runnableVerifyData = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			AlertDialog dlg = new AlertDialog.Builder(getActivity())
            .setTitle("提示信息")
            .setMessage("确认交易 " + strTransformer + " 元?\r\n请核对卡上显示的交易额度和目的帐号\r\n确认请按卡上按钮确认")
            .setView(null)//设置自定义对话框的样式
            .setPositiveButton("确定", //设置"确定"按钮
            new DialogInterface.OnClickListener() //设置事件监听
            {
                public void onClick(DialogInterface dialog, int whichButton) 
                {
            		new Thread(){
            			public void run(){
            				handler.post(runnableDisableOtherButton);
            				resault = FunctionMoudle.WaitCardButtonPushed();
            				if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
            					byte[] r = new byte[6];
            					strRandom = "";
            					for(int i = 0; i < r.length; i ++){
            						r[i] = (byte)(Math.random() * 10);
            						strRandom += String.format("%1$1d", (byte)r[i]);
            					}
            					
//            					resault = FunctionMoudle.DisplayOnCard(Integer.parseInt(strDisDstAccount, 10), Integer.parseInt(strRandom, 10), true);
            					resault = FunctionMoudle.DisplayOnCard(nBanlance, Integer.parseInt(strRandom, 10), true);
            					if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
            						bSrcAccount = FunctionMoudle.getImageData(tvSrcAccount.getHint().toString());
            						bDstAccount = FunctionMoudle.getImageData(tvDstAccount.getHint().toString());
            						bAuthCode = FunctionMoudle.getImageData(strRandom);
            						bTransformerAmount = FunctionMoudle.getImageData(strTransformer);            						
            						handler.post(runnableDisImg);
            						handler.post(runnableConfirmTransfor);
                				}else{
                					handler.post(runnableDisWarning);  
                				}	
            					
            				}else{
            					handler.post(runnableDisWarning);
            				}
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
            }
            )
            .create();//创建   
			Window window = dlg.getWindow();
			window.setGravity(Gravity.BOTTOM);
            dlg.show();//显示
		}
	};
	
	Runnable runnableConfirmTransfor = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			AlertDialog dlg = new AlertDialog.Builder(getActivity())
            .setTitle("提示信息")
            .setMessage("确认交易" + strTransformer + " 元?\r\n请核对卡上显示的认证码与手机上图片显示一致\r\n确认交易请按卡上按钮确认")
            .setView(null)//设置自定义对话框的样式
            .setPositiveButton("确定", //设置"确定"按钮
            new DialogInterface.OnClickListener() //设置事件监听
            {
                public void onClick(DialogInterface dialog, int whichButton) 
                {
            		new Thread(){
            			public void run(){
            				handler.post(runnableDisableOtherButton);
            				resault = FunctionMoudle.WaitCardButtonPushed();
            				if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
            					resault = FunctionMoudle.upDateBinData(nBanlance);
            					if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
            						resault = FunctionMoudle.DisplayOnCard(StaticData.nSHOWNONEONCARD, 0, false);
            						if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
            							resault[StaticData.nSW] = StaticData.sTRADEDONE;
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
            }
            )
            .create();//创建   
			Window window = dlg.getWindow();
			window.setGravity(Gravity.TOP);
            dlg.show();//显示
		}
	};
	
	Runnable runnableDisableOtherButton = new Runnable() {
		
		@Override
		public void run() {
		// TODO Auto-generated method stub
			btnBanlance.setEnabled(false);
			btnTransform.setEnabled(false);
			btnReturn.setEnabled(false);
		}

	};
	
	Runnable runnableEnableOtherButton = new Runnable() {
		
		@Override
		public void run() {
		// TODO Auto-generated method stub
			btnBanlance.setEnabled(true);
			btnTransform.setEnabled(true);
			btnReturn.setEnabled(true);
		}

	};
	
	Runnable runnableShow = new Runnable() {
		
		@Override
		public void run() {
		// TODO Auto-generated method stub
			tvNewKey.setText(strShow);
			scvNewKey.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					int offset = Math.abs(tvNewKey.getMeasuredHeight() - scvNewKey.getMeasuredHeight());
					scvNewKey.scrollTo(0, offset);
				}
			});
		}

	};
	
	public void Show(String msg){
		strShow += msg;
		handler.post(runnableShow);
	}
	
	
}
