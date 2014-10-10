package zdmatrix.hed.edimobileapp.data;

public class StaticData {
	
	public static final int nAPDULEN = 5;
	
	public static final String sSWOK = "0x9000";
	
	public static final String sWRITEAPDU = "80bf0100";
	public static final byte[] bWRITEAPDU = {(byte)0x80, (byte)0xbf, 0x01, 0x00, 0x00};
	public static final byte[] bGETRANDOMDATA = {0x00, (byte)0x84, 0x00, 0x00, 0x08};
	
}
