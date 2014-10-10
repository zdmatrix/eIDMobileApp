package zdmatrix.hed.eidmobileapp.functionmoudle;

import zdmatrix.hed.edimobileapp.data.StaticData;

public class FunctionMoudle {
	
	public static byte[] GetDisData(int n){
		String str = Integer.toString(n);
		byte[] ch = str.getBytes();		
		StaticData.bWRITEAPDU[4] = (byte)ch.length;
		byte[] ret = new byte[ch.length + StaticData.nAPDULEN];
		System.arraycopy(StaticData.bWRITEAPDU, 0, ret, 0, StaticData.nAPDULEN);
		System.arraycopy(ch, 0, ret, StaticData.nAPDULEN, ch.length);
		return ret;
		
	}
	
}
