package zdmatrix.hed.eidmobileapp.fragment;

import java.security.PublicKey;

import mafei.hed.nfcapplication.NFCApplication;
import mafei.hed.nfcapplication.NFCMsgCode;
import zdmatrix.hed.edimobileapp.data.StaticData;
import zdmatrix.hed.eid.eidmobileapp.R;
import zdmatrix.hed.eidmobileapp.fragment.TestFragment.GetRandomThread;
import zdmatrix.hed.eidmobileapp.fragment.TestFragment.WriteThread;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageParser.NewPermissionInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.Log;
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
	String resault;
	
	int nBanlance;
	int nRet;
	
	Handler handler;

	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState ){
		View view = inflater.inflate(R.layout.fragment_ecash, container, false);
		
		btnRecharge = (Button)view.findViewById(R.id.btnChallengeCode);
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
		
		tvBanlance = (TextView)view.findViewById(R.id.tvBanlance);
		tveCashResault = (TextView)view.findViewById(R.id.tveCashResault);
		
		handler = new Handler();
		
		return view;		
	}
	
	public class ClickEvent implements View.OnClickListener{
		@Override
		public void onClick(View v){
			if(v == btnReturn){
				MainFragment mainFragment = new MainFragment();
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();
			}else{
				nRet = NFCApplication.isSupportNFC(getActivity());
				switch (nRet) {
				case NFCMsgCode.nNOT_SUPPORT_NFC:
					resault = "Not Support NFC!";
					handler.post(runnableDisResault);
					break;
				case NFCMsgCode.nNOT_OPEN_NFC:
//					msg = "#" + String.format("%1$03d", nClickCount) + "  ";
					resault = "Please Open NFC in Setting First!";
					handler.post(runnableDisResault);
					break;
				case NFCMsgCode.nSUPPORT_NFC:
					if(v == btnBanlance){
						new GetBanlanceThread().start();
					}else if(v == btnRecharge){
						new RechargeThread().start();
					}else if(v == btnExpense){
						new ExpenseThread().start();
					}
					break;
				default:
					break;
				}
				
				
			}
		}
	}
	
	public class GetBanlanceThread extends Thread{
		@Override
		public void run(){
//			byte[] ret = NFCApplication.DataTransfer(StaticData.bSELECTFILE);
		}
	}
	
	public class RechargeThread extends Thread{
		@Override
		public void run(){
			if(Integer.parseInt(strRechargeData, 10) + nBanlance > 1000){
				resault = "充值金额过大，余额请勿超过1000！";	
			}else{
				
			}
			handler.post(runnableDisResault);
		}
	}
	
	public class ExpenseThread extends Thread{
		@Override
		public void run(){
			if(Integer.parseInt(strExpenseData, 10) < nBanlance){
				resault = "消费金额过大，超出余额！";
			}else{
				
			}
			handler.post(runnableDisResault);
		}
	}
	
	Runnable runnableDisResault = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
//			tveCashResault.setText(resault);
			final ProgressDialog m_Dialog;
			AlertDialog dlg = new AlertDialog.Builder(getActivity())
            .setTitle("提示信息")
            .setMessage(resault)       
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
