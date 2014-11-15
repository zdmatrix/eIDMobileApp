package zdmatrix.hed.eidmobileapp.fragment;


import zdmatrix.hed.eid.eidmobileapp.R;
import zdmatrix.hed.eidmobileapp.data.StaticData;
import zdmatrix.hed.eidmobileapp.functionmoudle.FunctionMoudle;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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
//	String resault;
//	String data;
	String[] resault;

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
    	
    	btnWrite = (Button)view.findViewById(R.id.btnExpense);
    	btnWrite.setOnClickListener(new ClickEvent());
    	
    	btnGetRandom = (Button)view.findViewById(R.id.btneIDAuth);
    	btnGetRandom.setOnClickListener(new ClickEvent());
    	
    	tvTestDate = (TextView)view.findViewById(R.id.tvTestDate);
    	tvTestResault = (TextView)view.findViewById(R.id.tveCashResault);

    	handler = new Handler();
    	
    	nClickCount = 0;

    	nRet = NFCApplication.isSupportNFC(getActivity());
		if(nRet != NFCMsgCode.nSUPPORT_NFC){
			resault = FunctionMoudle.ErrorProcess(nRet);
			handler.post(runnableDisResault);
		}
		
		
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
				nRet = NFCApplication.isConnectTag(intent);
				if(nRet == NFCMsgCode.nTAG_CONNECT){
					String str = FunctionMoudle.SelectApplet(StaticData.nEIDAPPLET);
					resault = FunctionMoudle.APDUResponseProcess(str);
					if(!resault[StaticData.nSW].equals(StaticData.sSWOK)){
						resault[StaticData.nSW] = "无法选中eID Applet！";
						handler.post(runnableDisResault);
					}else{
						if(v == btnGetRandom){
							new GetRandomThread().start();
						}
						if(v == btnWrite){
							new WriteThread().start();
						}
					}
					
				}else{
					resault = FunctionMoudle.ErrorProcess(nRet);
					handler.post(runnableDisResault);
				}											
			}
		}
	}
	
	public class GetRandomThread extends Thread{
		@Override
		public void run(){
			String strRet = FunctionMoudle.APDUCmd(StaticData.bGETRANDOMDATA);
			resault = FunctionMoudle.APDUResponseProcess(strRet);
			if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
				resault[StaticData.nSW] = "#" + String.format("%1$03d", nClickCount) + "读卡成功！";
			}else{
				resault[StaticData.nSW] = "#" + String.format("%1$03d", nClickCount) + "读卡失败！";					
			}								
			handler.post(runnableDisResault);
		}
	}
	
	public class WriteThread extends Thread{
		@Override
		public void run(){
			handler.post(runnableDisableOtherButton);
			resault = FunctionMoudle.DisplayOnCard(nClickCount % 100, 0, false);
			if(resault[StaticData.nSW].equals(StaticData.sSWOK)){
				resault[StaticData.nSW] = "#" + String.format("%1$03d", nClickCount) + "写卡成功！";
				resault[StaticData.nDATA] = null;
			}else{
				resault[StaticData.nDATA] = resault[StaticData.nSW];
				resault[StaticData.nSW] = "#" + String.format("%1$03d", nClickCount) + "写卡失败！";					
			}
			handler.post(runnableEnableOtherButton);
			handler.post(runnableDisResault);
		}
		
	}
	
	Runnable runnableDisResault = new Runnable() {
		
		@Override
		public void run() {
		// TODO Auto-generated method stub
			tvTestResault.setText(resault[StaticData.nSW]);
			tvTestDate.setText(resault[StaticData.nDATA]);
		}

	};
	
	Runnable runnableDisableOtherButton = new Runnable() {
		
		@Override
		public void run() {
		// TODO Auto-generated method stub
			btnGetRandom.setEnabled(false);
			btnWrite.setEnabled(false);
			btnReturn.setEnabled(false);
		}

	};
	
	Runnable runnableEnableOtherButton = new Runnable() {
		
		@Override
		public void run() {
		// TODO Auto-generated method stub
			btnGetRandom.setEnabled(true);
			btnWrite.setEnabled(true);
			btnReturn.setEnabled(true);
		}

	};
	
	
	

}
