package zdmatrix.hed.eidmobileapp.fragment;


import java.util.Random;

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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class OTPFragment extends Fragment{
	
	Button btnChallengeCode;
	Button btnReturn;
	Button btnLogIn;
	
	TextView tvChallengeCode;
	TextView tvResponseCode;
	
	Handler handler;
	
	Random random;
	
	String strResponseCode;
	String[] resault;
	String strRandom;
	byte[] bRandom;
	
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
		
		btnLogIn = (Button)view.findViewById(R.id.btnExpense);
		btnLogIn.setOnClickListener(new ClickEvent());
		
		tvChallengeCode = (TextView)view.findViewById(R.id.tvChallengeCode);
		
		handler = new Handler();
		
		tvResponseCode = (TextView)view.findViewById(R.id.tvResponseCode);
		
		resault = new String[]{"", ""};
		bRandom = new byte[6];
		
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
					if(v == btnChallengeCode){
						new GenerateChallengeCodeThread().start();
					}else if(v == btnLogIn){
						new LogInThread().start();
					}
					
				}else{
					resault = FunctionMoudle.ErrorProcess(nRet);
					handler.post(runnableDisWarning);
				}
				
			}
		}
	}
	
	public class GenerateChallengeCodeThread extends Thread{
		@Override
		public void run(){
			handler.post(runnableDisableOtherButton);
			strRandom = "";
			for(int i = 0; i < bRandom.length; i ++){
				bRandom[i] = (byte)(Math.random() * 10);
				strRandom += String.format("%1$1d", (byte)bRandom[i]);
			}
			handler.post(runnableDisRandom);
			handler.post(runnableEnableOtherButton);
			
		}
	}
	
	public class LogInThread extends Thread{
		@Override
		public void run(){
			handler.post(runnableLogIn);
		}
	}
	
	
	Runnable runnableDisRandom = new Runnable(){
		@Override
		public void run(){
			tvChallengeCode.setText(strRandom);
			resault = FunctionMoudle.DisplayOnCard(Integer.parseInt(strRandom, 10), 0, false);
			if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
				handler.post(runnableGenerateResponseCode);
			}else {
				handler.post(runnableDisWarning);
			}
		}
	};
	
	Runnable runnableDisWarning = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
//			tveCashResault.setText(resault);
			AlertDialog dlg = new AlertDialog.Builder(getActivity())
            .setTitle("��ʾ��Ϣ")
            .setMessage(resault[StaticData.nSW])       
            .setView(null)//�����Զ���Ի������ʽ
            .setPositiveButton("ȷ��", //����"ȷ��"��ť
            new DialogInterface.OnClickListener() //�����¼�����
            {
                public void onClick(DialogInterface dialog, int whichButton) 
                {
                	;
                }
            })

            .create();//����            
            dlg.show();//��ʾ
		}
	};
	
	Runnable runnableGenerateResponseCode = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			AlertDialog dlg = new AlertDialog.Builder(getActivity())
            .setTitle("��ʾ��Ϣ")
            .setMessage("��˶Կ�����ʾ����ս����ֻ��յ�����ս���Ƿ�һ��\r\nȷ�����밴���ϰ�ť����Ӧ����")
            .setView(null)//�����Զ���Ի������ʽ
            .setPositiveButton("ȷ��", //����"ȷ��"��ť
            new DialogInterface.OnClickListener() //�����¼�����
            {
                public void onClick(DialogInterface dialog, int whichButton) 
                {
            		new Thread(){
            			public void run(){
            				handler.post(runnableDisableOtherButton);
            				resault = FunctionMoudle.WaitCardButtonPushed();
            				if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
            					strResponseCode = "";
            					for(int i = 0; i < bRandom.length; i ++){
            						bRandom[i] = (byte)(Math.random() * 10);
            						strResponseCode += String.format("%1$1d", (byte)bRandom[i]);
            					}
            					int line1 = Integer.parseInt(strRandom, 10);
            					int line2 = Integer.parseInt(strResponseCode, 10);
            					resault = FunctionMoudle.DisplayOnCard(line1, line2, true);
                				handler.post(runnableResponseCode);
            					if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
            						handler.post(runnableConfirm);
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
            
            .setNegativeButton("ȡ��",
            new DialogInterface.OnClickListener(){
            	public void onClick(DialogInterface dialog, int whichButton){
            		;
            	}
            }
            )

            .create();//����    
			Window window = dlg.getWindow();
			window.setGravity(Gravity.BOTTOM);
            dlg.show();//��ʾ
            
            
		}
	};
	
	Runnable runnableConfirm = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			AlertDialog dlg = new AlertDialog.Builder(getActivity())
            .setTitle("��ʾ��Ϣ")
            .setMessage("��˶Կ�����ʾ��Ӧ������ֻ��յ���Ӧ�����Ƿ�һ��\r\nȷ�����밴�ֻ��ϵ�¼��ť��¼")
            .setView(null)//�����Զ���Ի������ʽ
            .setPositiveButton("ȷ��", //����"ȷ��"��ť
            new DialogInterface.OnClickListener() //�����¼�����
            {
                public void onClick(DialogInterface dialog, int whichButton) 
                {
            		;
                }
            })
            
            .setNegativeButton("ȡ��",
            new DialogInterface.OnClickListener(){
            	public void onClick(DialogInterface dialog, int whichButton){
            		;
            	}
            }
            )

            .create();//����    
			Window window = dlg.getWindow();
			window.setGravity(Gravity.BOTTOM);
            dlg.show();//��ʾ
            
            
		}
	};
	
	Runnable runnableLogIn = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			AlertDialog dlg = new AlertDialog.Builder(getActivity())
            .setTitle("��ʾ��Ϣ")
            .setMessage("��¼�ɹ�")
            .setView(null)//�����Զ���Ի������ʽ
            .setPositiveButton("ȷ��", //����"ȷ��"��ť
            new DialogInterface.OnClickListener() //�����¼�����
            {
                public void onClick(DialogInterface dialog, int whichButton) 
                {
            		;
                }
            })            
            .create();//����    
            dlg.show();//��ʾ
            
            
		}
	};
	
	Runnable runnableResponseCode = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			tvResponseCode.setText(strResponseCode);
		}
	};
	
	Runnable runnableDisableOtherButton = new Runnable() {
		
		@Override
		public void run() {
		// TODO Auto-generated method stub
			btnChallengeCode.setEnabled(false);
			btnLogIn.setEnabled(false);
			btnReturn.setEnabled(false);
		}

	};
	
	Runnable runnableEnableOtherButton = new Runnable() {
		
		@Override
		public void run() {
		// TODO Auto-generated method stub
			btnChallengeCode.setEnabled(true);
			btnLogIn.setEnabled(true);
			btnReturn.setEnabled(true);
		}

	};
}
