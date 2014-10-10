package zdmatrix.hed.eidmobileapp.fragment;

import java.lang.reflect.Array;
import java.nio.channels.ByteChannel;

import zdmatrix.hed.edimobileapp.data.StaticData;
import zdmatrix.hed.eid.eidmobileapp.R;
import zdmatrix.hed.eidmobileapp.functionmoudle.FunctionMoudle;
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
	
	int nRet;
	int nClickCount;
	String resault;
	String data;
	
	Intent intent;
	
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
    	
    	btnWrite = (Button)view.findViewById(R.id.btnLogIn);
    	btnWrite.setOnClickListener(new ClickEvent());
    	
    	btnGetRandom = (Button)view.findViewById(R.id.btnChallengeCode);
    	btnGetRandom.setOnClickListener(new ClickEvent());
    	
    	tvTestDate = (TextView)view.findViewById(R.id.tvTestDate);
    	tvTestResault = (TextView)view.findViewById(R.id.tveCashResault);

    	handler = new Handler();
    	
    	nClickCount = 0;
    	resault = null;
    	data = null;
    	
    	return view;
    	
    }
	
	
	public class ClickEvent implements View.OnClickListener{
		@Override
		public void onClick(View v){
			intent = getActivity().getIntent();
			if(v == btnReturn){
				MainFragment mainfragment = new MainFragment();
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, mainfragment).commit();
			}else{
				nClickCount ++;
				nRet = NFCApplication.isSupportNFC(getActivity());
				switch (nRet) {
				case NFCMsgCode.nNOT_SUPPORT_NFC:
					data = null;
					resault = "Not Support NFC!";
					break;
				case NFCMsgCode.nNOT_OPEN_NFC:
//					msg = "#" + String.format("%1$03d", nClickCount) + "  ";
					data = null;
					resault = "Please Open NFC in Setting First!";
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
				handler.post(runnableDisResault);
				
			}
		}
	}
	
	public class GetRandomThread extends Thread{
		@Override
		public void run(){
			nRet = NFCApplication.isConnectTag(intent);
			switch (nRet) {
			case NFCMsgCode.nTAG_CONNECT:
				byte[] ret = NFCApplication.DataTransfer(StaticData.bGETRANDOMDATA);
				String sw = String.format("%1$#2x", (byte)ret[ret.length - 2]) + String.format("%1$02X", (byte)ret[ret.length - 1]);
				if(sw.equals("0x9000")){
					resault = "#" + String.format("%1$03d", nClickCount) + "读卡成功！";
					data = "";
					for(int i = 0; i < ret.length - 2; i ++){
						if(i == 0){
							data += String.format("%1$#2x", (byte)ret[i]);
						}else{
							data += String.format("%1$02x", (byte)ret[i]);
						}				 
					}
				}else{
					resault = "#" + String.format("%1$03d", nClickCount) + "读卡失败！";
					data = sw;
				}				
				break;
				
			case NFCMsgCode.nTAG_CONNECT_FAILED:
				resault = "请连接设备和卡片。";
				data = null;
				break;
			case NFCMsgCode.nNO_NFC_INTENT:
				resault = "检测不到NFC连接动作，请重试。";
				data = null;
				break;
			default:
				break;
			}			
			handler.post(runnableDisResault);
		}
	}
	
	public class WriteThread extends Thread{
		@Override
		public void run(){
			nRet = NFCApplication.isConnectTag(intent);
			switch (nRet) {
			case NFCMsgCode.nTAG_CONNECT:
				byte[] apdu = FunctionMoudle.GetDisData(nClickCount % 100);
				byte[] ret = NFCApplication.DataTransfer(apdu);
				data = String.format("%1$#2x", (byte)ret[0]) + String.format("%1$02X", (byte)ret[1]);
				if(data.equals("0x9000")){
					resault = "#" + String.format("%1$03d", nClickCount) + "写卡成功！";
				}else{
					resault = "#" + String.format("%1$03d", nClickCount) + "写卡失败！";
				}				
				break;
				
			case NFCMsgCode.nTAG_CONNECT_FAILED:
				resault = "请连接设备和卡片。";
				data = null;
				break;
			case NFCMsgCode.nNO_NFC_INTENT:
				resault = "检测不到NFC连接动作，请重试。";
				data = null;
				break;
			default:
				break;
			}			
			handler.post(runnableDisResault);
		}
	}
	
	Runnable runnableDisResault = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			tvTestResault.setText(resault);
			tvTestDate.setText(data);
		}
	};
	
	
	
	

}
