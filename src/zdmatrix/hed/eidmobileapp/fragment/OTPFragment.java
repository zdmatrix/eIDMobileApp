package zdmatrix.hed.eidmobileapp.fragment;

import java.security.PublicKey;
import java.util.Currency;
import java.util.Random;

import mafei.hed.nfcapplication.NFCApplication;
import mafei.hed.nfcapplication.NFCMsgCode;

import zdmatrix.hed.eid.eidmobileapp.R;
import zdmatrix.hed.eidmobileapp.fragment.TestFragment.GetRandomThread;
import zdmatrix.hed.eidmobileapp.fragment.TestFragment.WriteThread;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageParser.NewPermissionInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class OTPFragment extends Fragment{
	
	Button btnChallengeCode;
	Button btnReturn;
	Button btnLogIn;
	
	TextView tvChallengeCode;
	EditText etResponseCode;
	
	Handler handler;
	
	Random random;
	
	String strResponseCode;
	String resault;
	String data;
	String strRandom;
	
	int nRet;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_otp, container, false);
		
		btnChallengeCode = (Button)view.findViewById(R.id.btnRecharge);
		btnChallengeCode.setOnClickListener(new ClickEvent());
		
		btnReturn = (Button)view.findViewById(R.id.btnReturn);
		btnReturn.setOnClickListener(new ClickEvent());
		
		btnLogIn = (Button)view.findViewById(R.id.btnLogIn);
		btnLogIn.setOnClickListener(new ClickEvent());
		
		tvChallengeCode = (TextView)view.findViewById(R.id.tvChallengeCode);
		
		handler = new Handler();
		
		etResponseCode = (EditText)view.findViewById(R.id.etResponseCode);
		etResponseCode.setOnKeyListener(new EditText.OnKeyListener(){

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				strResponseCode = etResponseCode.getText().toString();
				return false;
			}
			
		});
		
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
					data = null;
					resault = "Not Support NFC!";
					handler.post(runnableDisResault);
					break;
				case NFCMsgCode.nNOT_OPEN_NFC:
//					msg = "#" + String.format("%1$03d", nClickCount) + "  ";
					data = null;
					resault = "Please Open NFC in Setting First!";
					handler.post(runnableDisResault);
					break;
				case NFCMsgCode.nSUPPORT_NFC:
					if(v == btnChallengeCode){
						new GenerateChallengeCodeThread().start();
					}else if(v == btnLogIn){
						new LogInThread().start();
					}
					break;
				default:
					break;
				}
				
			}
		}
	}
	
	public class GenerateChallengeCodeThread extends Thread{
		@Override
		public void run(){
//			Time time = new Time();
//			random = new Random(time.toMillis(false));
			byte[] r = new byte[6];
//			random.nextBytes(r);
			strRandom = "";
			for(int i = 0; i < r.length; i ++){
				r[i] = (byte)(Math.random() * 10);
				strRandom += String.format("%1$1d", (byte)r[i]);
			}
			handler.post(runnableDisRandom);
			
		}
	}
	
	public class LogInThread extends Thread{
		@Override
		public void run(){
			
		}
	}
	
	Runnable runnableDisResault = new Runnable(){
		@Override
		public void run(){
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
	
	Runnable runnableDisRandom = new Runnable(){
		@Override
		public void run(){
			tvChallengeCode.setText(strRandom);
		}
	};
}
