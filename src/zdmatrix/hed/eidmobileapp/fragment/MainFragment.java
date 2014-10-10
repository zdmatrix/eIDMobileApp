package zdmatrix.hed.eidmobileapp.fragment;


import zdmatrix.hed.eid.eidmobileapp.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainFragment extends Fragment{
	
	Button btnExit;
	Button btnTest;
	Button btnNewKey;
	Button btneCash;
	Button btnOTP;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        
        
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
    	
    	View view = inflater.inflate(R.layout.fragment_main, container,false);
    	
    	btnExit = (Button)view.findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new ClickEvent());
        
        btnTest = (Button)view.findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new ClickEvent());
        
        btnNewKey = (Button)view.findViewById(R.id.btnNewKey);
        btnNewKey.setOnClickListener(new ClickEvent());
        
        btneCash = (Button)view.findViewById(R.id.btneCash);
        btneCash.setOnClickListener(new ClickEvent());
        
        btnOTP = (Button)view.findViewById(R.id.btnOTP);
        btnOTP.setOnClickListener(new ClickEvent());
    	
    	return view;
    	
    }
    
    public class ClickEvent implements View.OnClickListener{
    	@Override
    	public void onClick(View v){
    		if(v == btnExit){
    			getActivity().finish();
    		}
    		if(v == btnTest){			
    			TestFragment testfragment = new TestFragment();
    			getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, testfragment).commit();
    		}
    		if(v == btneCash){
    			eCashFragment ecashfragment = new eCashFragment();
    			getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, ecashfragment).commit();
    		}
    		if(v == btnOTP){
    			OTPFragment otpfragment = new OTPFragment();
    			getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, otpfragment).commit();
    		}
    	}
    }
    

}
