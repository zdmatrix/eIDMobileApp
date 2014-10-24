package zdmatrix.hed.eidmobileapp.fragment;

import mafei.hed.nfcapplication.NFCApplication;
import mafei.hed.nfcapplication.NFCMsgCode;
import zdmatrix.hed.eid.eidmobileapp.R;
import zdmatrix.hed.eidmobileapp.data.StaticData;
import zdmatrix.hed.eidmobileapp.functionmoudle.FunctionMoudle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

public class eCashFragment extends Fragment{
	
	Button btnRecharge;
	Button btnExpense;
	Button btnBanlance;
	Button btnReturn;

	EditText editTextRecharge;
	EditText editTextExpense;
	
	TextView tvBanlance;
	TextView tveCashShow;
	
	ScrollView scveCash;
	
	
	String strRechargeData;
	String strExpenseData;
	String strBanlance;
	String strTradeData;
	String[] resault;
	String strShow;
	
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
		
		btnExpense = (Button)view.findViewById(R.id.btnExpense);
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
		
		editTextExpense = (EditText)view.findViewById(R.id.etExpense);
		editTextExpense.setOnKeyListener(new EditText.OnKeyListener(){
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				strExpenseData = editTextExpense.getText().toString();
				return false;
			}
		});
		
		tvBanlance = (TextView)view.findViewById(R.id.tvBanlance);
		
		tveCashShow = (TextView)view.findViewById(R.id.tveCashShow);
		tveCashShow.setMovementMethod(ScrollingMovementMethod.getInstance());
		
		scveCash = (ScrollView)view.findViewById(R.id.scveCash);
		
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
						strShow = "";
						new RechargeThread().start();
					}else if(v == btnExpense){
						strShow = "";
						new ExpenseThread().start();
					}else if(v == btnBanlance){
						strShow = "";
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
				
				bGetBanlance = false;
				handler.post(runnableDisWarning);
				
			}else{
				nBanlance = Integer.parseInt(resault[StaticData.nDATA], 16);
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
//    						handler.post(runnableUpdateBinData);
    						UpdateBinData();
        				}else{
        					handler.post(runnableDisWarning);
        				}
						
					}
				}
				handler.post(runnableEnableOtherButton);
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
//    						handler.post(runnableUpdateBinData);
    						UpdateBinData();
        				}else{
        					handler.post(runnableDisWarning);
        				}
						
					}
				}
				handler.post(runnableEnableOtherButton);
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
	
	public void UpdateBinData(){
		new Thread(){
			public void run(){
				handler.post(runnableDisableOtherButton);
				Show("确认交易 " + strTradeData + " 元?\r\n请核对卡上显示的交易额度\r\n确认交易请按卡上按钮确认");
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
    					resault = FunctionMoudle.DisplayOnCard(Integer.parseInt(strTradeData, 10), nBanlance, true);
//    					if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
//    						resault[StaticData.nSW] = StaticData.sTRADEDONE;
//    					}    					
    				}	
				}
//				handler.post(runnableDisWarning); 
				Show(StaticData.sTRADEDONE + "\n\r");
				handler.post(runnableEnableOtherButton);
			}
		}.start();
	}
/*	
	Runnable runnableUpdateBinData = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			
			AlertDialog dlg = new AlertDialog.Builder(getActivity())
            .setTitle("提示信息")
            .setMessage("确认交易 " + strTradeData + " 元?\r\n请核对卡上显示的交易额度\r\n确认交易请按卡上按钮确认")
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
                					try {
										sleep(800);
									} catch (Exception e) {
										// TODO: handle exception
										e.printStackTrace();
									}
                					resault = FunctionMoudle.DisplayOnCard(Integer.parseInt(strTradeData, 10), nBanlance, true);
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
            dlg.show();//显示
            
            
		}
	};
*/	
	Runnable runnableDisableOtherButton = new Runnable() {
		
		@Override
		public void run() {
		// TODO Auto-generated method stub
			btnBanlance.setEnabled(false);
			btnExpense.setEnabled(false);
			btnReturn.setEnabled(false);
			btnRecharge.setEnabled(false);
		}

	};
	
	Runnable runnableEnableOtherButton = new Runnable() {
		
		@Override
		public void run() {
		// TODO Auto-generated method stub
			btnBanlance.setEnabled(true);
			btnExpense.setEnabled(true);
			btnReturn.setEnabled(true);
			btnRecharge.setEnabled(true);
		}

	};
	
	Runnable runnableShow = new Runnable() {
		
		@Override
		public void run() {
		// TODO Auto-generated method stub
			tveCashShow.setText(strShow);
			scveCash.scrollTo(0, tveCashShow.getHeight());
		}

	};
	
	public void Show(String msg){
		strShow += msg;
		handler.post(runnableShow);
	}
	
	
	
	
}
