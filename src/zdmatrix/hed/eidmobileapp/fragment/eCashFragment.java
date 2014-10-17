package zdmatrix.hed.eidmobileapp.fragment;

import mafei.hed.nfcapplication.NFCApplication;
import mafei.hed.nfcapplication.NFCMsgCode;
import zdmatrix.hed.edimobileapp.data.StaticData;
import zdmatrix.hed.eid.eidmobileapp.R;
import zdmatrix.hed.eidmobileapp.functionmoudle.FunctionMoudle;
import android.R.integer;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
	String[] resault;
	
	int nBanlance;
	int nRet;
	boolean bGetBanlance;
	
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
		
		editTextRecharge = (EditText)view.findViewById(R.id.editTextRecharge);
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
		
		resault = new String[2];
		nBanlance = 0;
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
			resault = FunctionMoudle.BinDataRW(null, 0, StaticData.nREADBANLANCE);
			if(!resault[StaticData.nSW].equals(StaticData.sSWOK)){
				handler.post(runnableDisWarning);
				bGetBanlance = false;
			}else{
				handler.post(runnableDisBanlance);
				bGetBanlance = true;
			}
		}
	}
	
	public class RechargeThread extends Thread{
		@Override
		public void run(){
			if(strRechargeData == null){
				resault[StaticData.nSW] = "请输入充值金额";					
			}else{
				if(!bGetBanlance){
					resault = FunctionMoudle.BinDataRW(null, 0, StaticData.nREADBANLANCE);
					if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
						nBanlance = Integer.parseInt(resault[StaticData.nDATA], 16);
						resault = FunctionMoudle.BinDataRW(strRechargeData, nBanlance, StaticData.nRECHARGE);
					}
				}else{
					resault = FunctionMoudle.BinDataRW(strRechargeData, nBanlance, StaticData.nRECHARGE);
				}
			}
			if(!resault[StaticData.nSW].equals(StaticData.sSWOK)){
				handler.post(runnableDisWarning);
			}			
		}
	}
	
	public class ExpenseThread extends Thread{
		@Override
		public void run(){
			if(strExpenseData == null){
				resault[StaticData.nSW] = "请输入消费金额";
				
			}else{
				if(!bGetBanlance){
					resault = FunctionMoudle.BinDataRW(null, 0, StaticData.nREADBANLANCE);
					if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
						nBanlance = Integer.parseInt(resault[StaticData.nDATA], 16);
						resault = FunctionMoudle.BinDataRW(strExpenseData, nBanlance, StaticData.nEXPENSE);
					}
				}else{
					resault = FunctionMoudle.BinDataRW(strExpenseData, nBanlance, StaticData.nEXPENSE);
				}
			}
			if(!resault[StaticData.nSW].equals(StaticData.sSWOK)){
				handler.post(runnableDisWarning);
			}
			
			
		}
	}
	
	Runnable runnableDisBanlance = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			tvBanlance.setText(String.format("%1$s", Integer.parseInt(resault[StaticData.nDATA], 16)));
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
	
	
	
}
