package zdmatrix.hed.eidmobileapp.fragment;

import mafei.hed.nfcapplication.NFCApplication;
import mafei.hed.nfcapplication.NFCMsgCode;
import zdmatrix.hed.edimobileapp.data.StaticData;
import zdmatrix.hed.eid.eidmobileapp.R;
import zdmatrix.hed.eidmobileapp.fragment.eCashFragment.ExpenseThread;
import zdmatrix.hed.eidmobileapp.fragment.eCashFragment.RechargeThread;
import zdmatrix.hed.eidmobileapp.functionmoudle.FunctionMoudle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class NewKeyFragment extends Fragment{
	
	Button btnReturn;
	Button btnVerifyData;
	Button btnOk;
	Button btnBanlance;
	
	ImageView imgSrcAccount;
	ImageView imgDstAccount;
	ImageView imgTransformerAmount;
	ImageView imgAuthCode;
	
	EditText etTransformerAmount;
	TextView tvSrcAccount;
	TextView tvDstAccount;
	TextView tvBanlance;
	
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
	boolean bGetBanlance;
	
	String[] resault;
	String strTransformer;
	String strAuthCode;
	String strRandom;
 	
	@Override
	public void onCreate(Bundle saveInstancStates){
		super.onCreate(saveInstancStates);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstancStates){
		View view = inflater.inflate(R.layout.fragment_newkey, container, false);
		
		btnReturn = (Button)view.findViewById(R.id.btnReturn);
		btnReturn.setOnClickListener(new ClickEvent());
		
		btnVerifyData = (Button)view.findViewById(R.id.btnVerifyData);
		btnVerifyData.setOnClickListener(new ClickEvent());
		
		btnOk = (Button)view.findViewById(R.id.btnOk);
		btnOk.setOnClickListener(new ClickEvent());
		
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
		tvDstAccount = (TextView)view.findViewById(R.id.tvDstAccount);
		tvSrcAccount = (TextView)view.findViewById(R.id.tvSrcAccount);
		tvBanlance = (TextView)view.findViewById(R.id.tvBanlance);
		
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
					if(v == btnOk){
						new ConfirmTransformThread().start();
					}else if(v == btnBanlance){
						new GetBanlanceThread().start();
					}else if(v == btnVerifyData){
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
						String str = tvDstAccount.getHint().toString();
//						resault = FunctionMoudle.DisplayOnCard(Integer.parseInt(etTransformerAmount.getText().toString(), 10), Integer.parseInt(strTransformer, 10), true);
						resault = FunctionMoudle.DisplayOnCard(Integer.parseInt(str, 10), Integer.parseInt(strTransformer, 10), true);
							if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
    						nBanlance = n;
    						handler.post(runnableVerifyData);
        				}else{
        					handler.post(runnableDisWarning);
        				}
						
					}
				}
			}
			
			/*
			String response = "";
			response = FunctionMoudle.APDUCmd(StaticData.bGENERATEUTHCODE);
			resault = FunctionMoudle.APDUResponseProcess(response);
			if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
				response = FunctionMoudle.APDUCmd(StaticData.bFETCHAUTHCODE);
				resault = FunctionMoudle.APDUResponseProcess(response);
				if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
					Log.i("PAUSE", "have a look");
				}else{
					resault[StaticData.nSW] += StaticData.sGETAUTHCODEERR;
					handler.post(runnableDisWarning);
				}
			}else{
				resault[StaticData.nSW] += StaticData.sGENERATEAUTHCODEERR;
				handler.post(runnableDisWarning);
			}
			*/
			
		}
	}
	
	public class ConfirmTransformThread extends Thread{
		@Override
		public void run(){
			
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
	
	Runnable runnableVerifyData = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			AlertDialog dlg = new AlertDialog.Builder(getActivity())
            .setTitle("提示信息")
            .setMessage("确认交易" + strTransformer + "?\r\n请核对卡上显示的交易额度和目的帐号\r\n确认请按卡上按钮确认")
            .setView(null)//设置自定义对话框的样式
            .setPositiveButton("确定", //设置"确定"按钮
            new DialogInterface.OnClickListener() //设置事件监听
            {
                public void onClick(DialogInterface dialog, int whichButton) 
                {
            		new Thread(){
            			public void run(){
            				resault = FunctionMoudle.WaitCardButtonPushed();
            				if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
            					byte[] r = new byte[6];
//            					random.nextBytes(r);
            					strRandom = "";
            					for(int i = 0; i < r.length; i ++){
            						r[i] = (byte)(Math.random() * 10);
            						strRandom += String.format("%1$1d", (byte)r[i]);
            					}
            					
            					resault = FunctionMoudle.DisplayOnCard(Integer.parseInt(strRandom, 10), Integer.parseInt(strTransformer, 10), true);
            					if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
            						bSrcAccount = FunctionMoudle.getImageData(tvSrcAccount.getHint().toString());
            						bDstAccount = FunctionMoudle.getImageData(tvDstAccount.getHint().toString());
            						bAuthCode = FunctionMoudle.getImageData(strAuthCode);
            						bTransformerAmount = FunctionMoudle.getImageData(strTransformer);            						
            						handler.post(runnableDisImg);
            						handler.post(runnableConfirmTransfor);
                				}	
            					
            				}
            				handler.post(runnableDisWarning);            				                      	
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
            .setMessage("确认交易" + strTransformer + "?\r\n请核对卡上显示的目的帐号和认证码与手机上图片显示一致\r\n确认交易请按卡上按钮确认")
            .setView(null)//设置自定义对话框的样式
            .setPositiveButton("确定", //设置"确定"按钮
            new DialogInterface.OnClickListener() //设置事件监听
            {
                public void onClick(DialogInterface dialog, int whichButton) 
                {
            		new Thread(){
            			public void run(){
            				resault = FunctionMoudle.WaitCardButtonPushed();
            				if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
            					resault[StaticData.nSW] = StaticData.sTRADEDONE;
            					
            				}
            				handler.post(runnableDisWarning);            				                      	
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
}
