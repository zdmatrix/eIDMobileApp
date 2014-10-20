package zdmatrix.hed.edimobileapp.data;

public class StaticData {
	
	public static final int nAPDULEN = 5;
	public static final int nFILEIDLEN = 2;
	public static final int nIMAGEWIDTH = 470;
	public static final int nBINDATALEN = 4;
	
	public static final int nSWSTARTINDEX = 4;
	public static final int nSWENDINDEX = 1;
	public static final int nDATAENDINDEX = 4;
	public static final int nDATA = 1;
	public static final int nSW = 0;
	public static final int nDONE_ONLY_SW = 0x1000;
	public static final int nFAILED_ONLY_SW = 0x1001;
	public static final int nDONE_WITH_DATA = 0x1002;
	public static final int nFAILED_WITH_DATA = 0x1003;
	public static final int nHAS_OTHERS_DATA = 0x1004;
	public static final int nWRONGRESPONSE = 0x1005;
	
	public static final int nRECHARGE = 0x2000;
	public static final int nEXPENSE = 0x2001;
	public static final int nREADBANLANCE = 0x2002;
	
	
	
	public static final int NUM0[]  = { 7, 1, 2, 4, 6, 5, 3, 1 };
	public static final int NUM1[]  = { 2, 2, 6 };
	public static final int NUM2[]  = { 6, 1, 2, 4, 3, 5, 6 };
	public static final int NUM3[]  = { 7, 1, 2, 4, 3, 4, 6, 5 };
	public static final int NUM4[]  = { 6, 1, 3, 4, 2, 4, 6 };
	public static final int NUM5[]  = { 6, 2, 1, 3, 4, 6, 5 };
	public static final int NUM6[]  = { 7, 2, 1, 3, 4, 6, 5, 3 };
	public static final int NUM7[]  = { 3, 1, 2, 6 };
	public static final int NUM8[]  = { 9, 1, 2, 4, 6, 5, 3, 1, 3, 4 };
	public static final int NUM9[]  = { 7, 4, 2, 1, 3, 4, 6, 5 };
	public static final int NUM09[][] = { NUM0, NUM1, NUM2, NUM3, NUM4, NUM5, NUM6, NUM7, NUM8, NUM9 };	
	public static final int ORG[][] ={
			{0, 0},
			{6, 0},
			{0, 6},
			{6, 6},
			{0, 12},
			{6, 12},
	};
	
	public static final String sSWOK = "9000";
	public static final String sSWWAIT = "6000";
	public static final String sSWERROR = " �Ƿ�SW";
	public static final String sFETCHNEXTDATA = "61";
	
	public static final String sGENERATEAUTHCODEERR = " ������֤��ʧ��";
	public static final String sGETAUTHCODEERR = " ȡ��֤��ʧ��";
	public static final String sSELECTFILEERR = " ѡ�ļ�ʧ��";
	
	public static final String sTRADEDATANULL = " �����뽻�׽�";
	public static final String sTRADEDONE = "���׳ɹ�";
	public static final String sOVERBANLANCE = " ���ѽ�����";
	public static final String sOVERECASHLIMIT = "��ֵ������������𳬹�1000��";
	public static final String sUPBINDATAERR = " �������ʧ�ܣ�";
	public static final String sREADBANLANCEERR = " �����ʧ��";
	public static final String sOUTOFTIME = " ������ʱ";
	public static final String sWRITEFIRSTLINEERR = " ��һ������дʧ��";
	public static final String sWRITESECONDLINEERR = " �ڶ�������дʧ��";
	public static final String sDISPLAYERR = " ��ʾ����ʧ��";
	
	public static final String sUNKNOWERR = " Unknow Error!";
	public static final String sCONNECTFAILED = " Connect Tag Failed!";
	public static final String sNONFCINTENT = " Not Detected NFC Intent!";
	public static final String sOPENNFC = " Please Open NFC in Setting First!";
	public static final String sNOTSUPPORTNFC = " Not Support NFC!";
//	public static final String sCONFIRMRECHARGE = "ȷ�ϳ�ֵ";
	
	public static final String sWRITEAPDU = "80bf0100";
	public static final byte[] bWRITELINEAPDU = {(byte)0x80, (byte)0xbf, 0x01, 0x00, 0x00};				//bWRITELINEAPDU[3] = 0x01,means first line
																										//bWRITELINEAPDU[3] = 0x02,means second line
	public static final byte[] bDISPLAYONCARD = {(byte)0x80, (byte)0xBF, 0x01, 0x03, 0x00};
	//	public static final byte[] bWRITESECONDLINEAPDU = {(byte)0x80, (byte)0xbf, 0x02, 0x00, 0x00};
	public static final byte[] bGETRANDOMDATA = {0x00, (byte)0x84, 0x00, 0x00, 0x08};
	public static final byte[] bFETCHNEXTDATA = {0x00, (byte)0xc0, 0x00, 0x00, 0x00};
	public static final byte[] bGENERATEUTHCODE = {(byte)0x80, (byte)0xbf, 0x08, 0x00, 0x00};
	public static final byte[] bSELECTFILE = {0x00, (byte)0xa4, 0x00, 0x00, 0x02};
	public static final byte[] bREADBINARAYDATA = {0x00, (byte)0xb0, 0x00, 0x00, 0x04};
	public static final byte[] bSENDFIRSTLINEDATA = {(byte)0x80, (byte)0xBF, 0x01, 0x01, 0x00};
	public static final byte[] bSENDSECONDLINEDATA = {(byte)0x80, (byte)0xBF, 0x01, 0x02, 0x00};
	
	public static final byte[] bUPDATEBINARAYDATA = {0x00, (byte)0xd6, 0x00, 0x00, 0x04};
	public static final byte[] bCARDINOTPMODE = {(byte)0x80, (byte)0xbf, 0x05, 0x00, 0x00};
	public static final byte[] bFETCHAUTHCODE = {0x00, (byte)0xc0, 0x00, 0x00, 0x06};
	public static final byte[] bBINARAYFILEID = {(byte)0x00, (byte)0x00};
	public static final byte[] bWAITCARDBUTTONPUSHED = {(byte)0x80, (byte)0xbf, 0x06, 0x00, 0x00};
	
}
