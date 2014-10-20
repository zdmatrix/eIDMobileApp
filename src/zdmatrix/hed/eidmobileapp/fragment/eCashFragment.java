package zdmatrix.hed.eidmobileapp.fragment;

import mafei.hed.nfcapplication.NFCApplication;
import mafei.hed.nfcapplication.NFCMsgCode;
import zdmatrix.hed.edimobileapp.data.StaticData;
import zdmatrix.hed.eid.eidmobileapp.R;
import zdmatrix.hed.eidmobileapp.functionmoudle.FunctionMoudle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class eCashFragment extends Fragment{
	
	Button btnRecharge;
	Button btnExpense;
	Button btnBanlance;
	Button btnReturn;

	EditText editTextRecharge;
	EditText editTextExpense;
	TextView tvBanlance;
	TextView tveCashResault;
	
	String strRechargeData;
	String strExpenseData;
	String strBanlance;
	String strTradeData;
	String[] resault;
	
	int nBanlance;
	int nRet;
	boolean bGetBanlance;
	boolean bConfirmRecharg;
	boolean bConfirmConsume;
	
	Handler handler;

	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState ){
		View view = inflater.inflate(R.layout.fragment_ecash, container, false);
		
		btnRecharge = (Button)view.findViewById(R.id.btnRecharge);
		btnRecharge.setOnClickListener(new ClickEvent());
		
		btnExpense = (Button)view.findViewById(R.id.btnLogIn);
		btnExpense.setOnClickListener(new ClickEvent());
		
		btnBanlance = (Button)view.findViewById(R.id.btnBanlance);
		btnBanlance.setOnClickListener(new ClickEvent());
		
		btnReturn = (Button)view.findViewById(R.id.btnReturn);
		btnReturn.setOnClickListener(new ClickEvent());
		
		editTextRecharge = (EditText)view.findViewById(R.id.etRecharge);
		editTextRecharge.setOnKeyListener(new EditText.OnKeyListener(){
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				strRechargeData = editTextRecharge.getText().toString();
				return false;
			}
		});
		
		editTextExpense = (EditText)view.findViewById(R.id.etResponseCode);
		editTextExpense.setOnKeyListener(new EditText.OnKeyListener(){
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				strExpenseData = editTextExpense.getText().toString();
				return false;
			}
		});
		
		tvBanlance = (TextView)view.findViewById(R.id.tvDstAccount);
		tveCashResault = (TextView)view.findViewById(R.id.tveCashResault);
		
		handler = new Handler();
		
		resault = new String[]{"", ""};
		nBanlance = 0;
		bGetBanlance = false;
		bConfirmRecharg = false;
		bConfirmConsume = false;
		
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
					if(v == btnRecharge){
						new RechargeThread().start();
					}else if(v == btnExpense){
						new ExpenseThread().start();
					}else if(v == btnBanlance){
						new GetBanlanceThread().start();
					}
					
				}else{
					resault = FunctionMoudle.ErrorProcess(nRet);
					handler.post(runnableDisWarning);
				}							
			}
		}
	}
	
	public class GetBanlanceThread extends Thread{
		@Override
		public void run(){
			resault = FunctionMoudle.readBanlance();
			if(!resault[StaticData.nSW].equals(StaticData.sSWOK)){
				nBanlance = Integer.parseInt(resault[StaticData.nDATA], 16);
				bGetBanlance = false;
				handler.post(runnableDisWarning);
				
			}else{
				bGetBanlance = true;
				handler.post(runnableDisBanlance);
				
			}
		}
	}
	
	public class RechargeThread extends Thread{
		@Override
		public void run(){
			if(strRechargeData == null){
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
					int n = 0;					
					n = nBanlance + Integer.parseInt(strRechargeData, 10);
					if(n > 1000){
						resault[StaticData.nSW] = StaticData.sOVERECASHLIMIT;
						handler.post(runnableDisWarning);
					}else{
						resault = FunctionMoudle.DisplayOnCard(Integer.parseInt(strRechargeData, 10), 0, false);
    					if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
    						nBanlance = n;
    						strTradeData = strRechargeData;
    						handler.post(runnableUpdateBinData);
        				}else{
        					handler.post(runnableDisWarning);
        				}
						
					}
				}
			}
		
		}
	}
	
	public class ExpenseThread extends Thread{
		@Override
		public void run(){
			if(strExpenseData == null){
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
					int n = 0;
					
					n = nBanlance - Integer.parseInt(strExpenseData, 10);
					if(n < 0){
						resault[StaticData.nSW] = StaticData.sOVERBANLANCE;
						handler.post(runnableDisWarning);
					}else{
						resault = FunctionMoudle.DisplayOnCard(Integer.parseInt(strExpenseData, 10), 0, false);
    					if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
    						nBanlance = n;
    						strTradeData = strExpenseData;
    						handler.post(runnableUpdateBinData);
        				}else{
        					handler.post(runnableDisWarning);
        				}
						
					}
				}
			}
			
			
		}
	}
	
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
	
	Runnable runnableUpdateBinData = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			AlertDialog dlg = new AlertDialog.Builder(getActivity())
            .setTitle("提示信息")
            .setMessage("确认充值" + strTradeData + "?\r\n请核对卡上显示的交易额度\r\n确认交易请按卡上按钮确认")
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
            					resault = FunctionMoudle.upDateBinData(nBanlance);
                				if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
                					try {
										sleep(800);
									} catch (Exception e) {
										// TODO: handle exception
										e.printStackTrace();
									}
                					resault[StaticData.nSW] = StaticData.sTRADEDONE;
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
			
            dlg.show();//显示
            
            
		}
	};
	
	
	
}
