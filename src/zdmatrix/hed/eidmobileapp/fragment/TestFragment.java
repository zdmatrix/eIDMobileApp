package zdmatrix.hed.eidmobileapp.fragment;

import zdmatrix.hed.eid.eidmobileapp.R;
import android.content.Intent;
import android.nfc.ErrorCodes;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import mafei.hed.nfcapplication.*;

public class TestFragment extends Fragment{
	
	Button btnReturn;
	Button btnWrite;
	Button btnGetRandom;
	TextView tvTestDate;
	TextView tvTestResault;
	Handler handler;
	
	NFCApplication nfcApplication;
	NFCMsgCode errcode;
	
	int nRet;
	int nClickCount;
	String resault;
	String data;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState); 
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {    	
    	View view = inflater.inflate(R.layout.fragment_test, container,false); 
    	
    	btnReturn = (Button)view.findViewById(R.id.btnReturn);
    	btnReturn.setOnClickListener(new ClickEvent());
    	
    	btnWrite = (Button)view.findViewById(R.id.btnWrite);
    	btnWrite.setOnClickListener(new ClickEvent());
    	
    	btnGetRandom = (Button)view.findViewById(R.id.btnGetRandom);
    	btnGetRandom.setOnClickListener(new ClickEvent());
    	
    	tvTestDate = (TextView)view.findViewById(R.id.tvTestDate);
    	tvTestResault = (TextView)view.findViewById(R.id.tvTestResault);
    	nfcApplication = new NFCApplication();

    	handler = new Handler();
    	
    	nClickCount = 0;
    	resault = null;
    	data = null;
    	
    	return view;
    	
    }
	
	
	public class ClickEvent implements View.OnClickListener{
		@Override
		public void onClick(View v){
			
			if(v == btnReturn){
				MainFragment mainfragment = new MainFragment();
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, mainfragment).commit();
			}else{
				nClickCount ++;
				nRet = nfcApplication.isSupportNFC(getActivity());
				switch (nRet) {
				case NFCMsgCode.nNOT_SUPPORT_NFC:
					
					tvTestResault.append(NFCMsgCode.sNOT_SUPPORT_NFC + "\n");
					
					btnWrite.setClickable(false);
					btnGetRandom.setClickable(false);
					break;
				case NFCMsgCode.nNOT_OPEN_NFC:
//					msg = "#" + String.format("%1$03d", nClickCount) + "  ";
					tvTestResault.append(NFCMsgCode.sNOT_OPEN_NFC + "\n");
					break;
				case NFCMsgCode.nSUPPORT_NFC:
					if(v == btnGetRandom){
						new GetRandomThread().start();
					}else if(v == btnWrite){
						new WriteThread().start();
					}
					break;
				default:
					break;
				}
			}
		}
	}
	
	public class GetRandomThread extends Thread{
		@Override
		public void run(){
			
		}
	}
	
	public class WriteThread extends Thread{
		@Override
		public void run(){
			Intent intent = getActivity().getIntent();
			nRet = nfcApplication.isConnectTag(intent);
			if(nRet == NFCMsgCode.nTAG_CONNECT){
				byte[] apdu = new byte[]{(byte)0x80, (byte)0xbf, 0x01, 0x00, 0x02, 0x32, 0x31};
				byte[] ret = nfcApplication.DataTransfer(apdu);
				Log.i("STOP", "TEST");
			}
		}
	}
	
	Runnable runnableResault = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
	};
	
	

}
