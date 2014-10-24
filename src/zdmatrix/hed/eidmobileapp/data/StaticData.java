package zdmatrix.hed.eidmobileapp.data;

public class StaticData {
	
	public static final int nAPDULEN = 5;
	public static final int nFILEIDLEN = 2;
	public static final int nIMAGEWIDTH = 470;
	public static final int nBINDATALEN = 4;
	public static final int nSUBACCOUNTSTARTINDEX = 2;
	public static final int nSUBACCOUNTENDINDEX = 10;
	public static final int nSHOWNONEONCARD = 0xFFFFFFFF;
	public static final String sSHOWNONEONCARD = "0xFFFFFFFF";
	public static final int nRETURNSTRINGARRAYLEN = 2;
	
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
	public static final String sSWERROR = " 非法SW";
	public static final String sFETCHNEXTDATA = "61";
	
	public static final String sGENERATEAUTHCODEERR = " 生成认证码失败";
	public static final String sGETAUTHCODEERR = " 取认证码失败";

	
	public static final String sTRADEDATANULL = " 请输入交易金额！";
	public static final String sTRADEDONE = "交易成功";
	public static final String sOVERBANLANCE = " 消费金额超出余额！";
	public static final String sOVERECASHLIMIT = "充值金额过大，余额请勿超过1000！";
	public static final String sUPBINDATAERR = " 更新余额失败！";
	public static final String sREADBANLANCEERR = " 读余额失败";
	public static final String sOUTOFTIME = " 操作超时";
	public static final String sWRITEFIRSTLINEERR = " 第一行数据写失败";
	public static final String sWRITESECONDLINEERR = " 第二行数据写失败";
	public static final String sDISPLAYERR = " 显示数据失败";
	
	public static final String sUNKNOWERR = " Unknow Error!";
	public static final String sCONNECTFAILED = " Connect Tag Failed!";
	public static final String sNONFCINTENT = " Not Detected NFC Intent!";
	public static final String sOPENNFC = " Please Open NFC in Setting First!";
	public static final String sNOTSUPPORTNFC = " Not Support NFC!";
	public static final String sGENERATERSAKEYERR = " Generate RSA Key Err";
	public static final String sSELECTFILEERR = " Select File Err! File ID: ";
	public static final String sREADFILEERR = " Read File Err! File ID: ";
	public static final String sSELECTEIDAPPLETERR = " Select eID Applet Err";
	public static final String sUPDATEFILEERR = " Update File Err! File ID: ";
	public static final String sSHA1COMPRESSERR = " SHA1 Compress Err";
	
	
	
	public static final String sWRITEAPDU = "80bf0100";
	public static final byte[] bWRITELINEAPDU = {(byte)0x80, (byte)0xbf, 0x01, 0x00, 0x00};				//bWRITELINEAPDU[3] = 0x01,means first line
																										//bWRITELINEAPDU[3] = 0x02,means second line
	public static final byte[] bSHOWNONEONFIRSTLINE = {(byte)0x80, (byte)0xbf, 0x01, 0x01, 0x09, (byte)0x20, (byte)0x20, (byte)0x20, (byte)0x20, (byte)0x20, (byte)0x20, (byte)0x20, (byte)0x20, (byte)0x20};
	public static final byte[] bSHOWNONEONSECONDLINE = {(byte)0x80, (byte)0xbf, 0x01, 0x02, 0x09, (byte)0x20, (byte)0x20, (byte)0x20, (byte)0x20, (byte)0x20, (byte)0x20, (byte)0x20, (byte)0x20, (byte)0x20};
	public static final byte[] bDISPLAYONCARD = {(byte)0x80, (byte)0xBF, 0x01, 0x03, 0x00};
	//	public static final byte[] bWRITESECONDLINEAPDU = {(byte)0x80, (byte)0xbf, 0x02, 0x00, 0x00};
	public static final byte[] bGETRANDOMDATA = {0x00, (byte)0x84, 0x00, 0x00, 0x08};
	public static final byte[] bFETCHNEXTDATA = {0x00, (byte)0xc0, 0x00, 0x00, 0x00};
	public static final byte[] bGENERATEUTHCODE = {(byte)0x80, (byte)0xbf, 0x08, 0x00, 0x00};
	public static final byte[] bSELECTFILE = {0x00, (byte)0xa4, 0x00, 0x00, 0x02};
	
	public static final byte[] bBINARAYFILEID = {(byte)0x00, (byte)0x00};
	
	public static final byte[] bCONTAINERINDEXFILEID = {(byte)0x0F, (byte)0xFD};
	public static final String sUPDATECONTAINERINDEX = "00D60000023F00";
	
	public static final byte[] bFILEINDEXFILEID = {(byte)0x0F, (byte)0xFE};
	public static final String sUPDATEFILEINDEX = "00D6000018010003000300000000000100010001000300000001000100";
	public static final byte[] bCONTAINERINFOFILEID = {(byte)0x0F, (byte)0xFF};
	public static final String sUPDATECONTAINERINFO1 = "00D60000F07B34413741323642312D414241352D343865662D3842" +
			"36412D3234413442413432453738377D000000010000000000001110121013100000000000007B3942424133364134" +
			"2D344535442D343465332D383833332D4343344442464541303838367D000000200000000000000000121313130000" +
			"000000007B43424235303343332D453043362D346165392D393543392D3245374433394245424346347D0000000200" +
			"000000000000000000D3210000000000007B32323939393244382D343037422D346437352D414544372D3046344433" +
			"344145354638387D000000010000000000000000F2A0F3A0000000000000";
	public static final String sUPDATECONTAINERINFO2 = "00D600F0787B41363737443832422D333546352D343039342D4" +
			"24334312D3838334543304636374437397D000000040000000000001112121213120000000000007B4441384638434" +
			"4392D303243362D343931322D394134392D3738433231463638374142467D0000000400000000000000000000F3220" +
			"00000000000";
	
	public static final byte[] bMAXCONTAINERFILEID = {(byte)0x30, (byte)0x01};	
	public static final String sUPDATEMAXCONTAINERINFO = "00D600000106";
	
	public static final byte[] bEIDDIR = {(byte)0x3F, (byte)0x01};
	
	public static final byte[] bLOADERINDEXFILEID = {(byte)0x30, (byte)0x02};
	public static final String sUPDATELOADERINDEXINFO = "00D600000E4255505432303130383133333036";
	public static final String sREADLOADERINDEXINFO = "00B000000E";
	
	public static final byte[] bEIDMICXRSA2048FILEID = {(byte)0x21, (byte)0xD3};
	public static final String sUPDATEEIDRSA2048INFO1 = "00D60000F000083D7716ACF7071A189B9718131363B48255E0" +
			"C1D4D1C037733DC25A5589E37DAEDF13C42D0CBDD7D7C0C71757A698F46C46311C93CFF041A5A3CBE35804A5ED8BC7" +
			"C062B4A5DD524060F1895CC2F72D1389EFDC17C8B5BEFE4237717FF234DC8E15020EE6CE9DE6BF0D465CEE685CF884" +
			"CFB6EBDFCABBD7E708203A2532DF8A375F6A03BDB4173276FFB0143168D4204744272C31F77CBEF4A1505BC12D37D8" +
			"014AF2775D90F223A267BE365BC870E73F4282572F03510100C3A1BD92B94C539D96FE8EBF60FFC030E7484EF4B1B4" +
			"67AF2D88BEDAE4EB4E017592646526481B737DA50F64D13D19150B4C8F7CB6D4";
	public static final String sUPDATEEIDRSA2048INFO2 = "00D600F016B21EE62D3DABFAF8817C63DB09576DC8B8CA0100" +
			"0100";
	
	public static final byte[] bPERSONINDEXFILEID = {(byte)0x30, (byte)0x00};
	public static final String sUPDATEPERSONINDEXINFO = "00D60000802A06686417E6A0310C618F277036131BE187B84B" +
			"1FBBB9F0AD3EC355FB8468EFCAC874B1450EDFB89599083D0B6A38029BA9C8486164AF81CC6175B433C599FC66D485" +
			"337C7B3B48F41CBE7E844422CAC573E0C25AF5BFD1834D1F44FF77651292DB6F3BC67BC5BDEDE81919739DE03C04A0" +
			"91BA224F8E46489B6E2619C93F26";
	
	public static final byte[] bLOADERCAPBILITY = {(byte)0x40, (byte)0x01};
	public static final String sUPDATELOADERCAPBILITYINFO = "00D6000040000C000F0002000A000100000000000B050001" +
			"010102220001030000000000000000000000000000000000000000000000000000000000000000000000000000";
	
	public static final byte[] bREADBINARAYDATA = {0x00, (byte)0xb0, 0x00, 0x00, 0x04};
	public static final byte[] bSENDFIRSTLINEDATA = {(byte)0x80, (byte)0xBF, 0x01, 0x01, 0x00};
	public static final byte[] bSENDSECONDLINEDATA = {(byte)0x80, (byte)0xBF, 0x01, 0x02, 0x00};
	
	public static final byte[] bUPDATEBINARAYDATA = {0x00, (byte)0xd6, 0x00, 0x00, 0x04};
	public static final byte[] bCARDINOTPMODE = {(byte)0x80, (byte)0xbf, 0x05, 0x00, 0x00};
	public static final byte[] bFETCHAUTHCODE = {0x00, (byte)0xc0, 0x00, 0x00, 0x06};
	
	public static final byte[] bWAITCARDBUTTONPUSHED = {(byte)0x80, (byte)0xbf, 0x06, 0x00, 0x00};	
	public static final byte[] bSELECTEIDAPPLET = {0x00, (byte)0xa4, 0x00, 0x00, 0x08, (byte)0xA0, 0x00, 0x00, 0x00, 0x03 ,0x45, 0x49, 0x44, 0x00};
	
	public static final String sGENERATERSAKEY = "0042000008C0020000C202A0F2";
	
	public static final String sSHA1COMPRESSFILEDATA = "80C4410104C0023000";
	public static final String sSHA1COMPRESSEXTERNANDFILEDATA = "80C481011BC0023000C181140BE9EB7F21A1FD176A5B1BAF554B9288A025310F";
	
	public static final String sINSTALLRSAKEYSTEP1 = "80c6010156c2021312c450e7a10de6c297080e347c0a8ef52ff6f3c6f119536dfdef1963e0f" +
			"53ea9a2150ae7a5eed010d6270e55d37b0cf813fe02f6329776242cc11b9a3ac82c2371223bcb1887ce6839b3ead78dc3cde14d0307";
	public static final String sINSTALLRSAKEYSTEP2 = "80c6010156c2021312c550d2ac7cc3b9ee2a936e00e0b6901ae038a7885f7ea1cee2e496f89f" +
			"806d8842932c4c5ecda4f4cfdd05cf14b29e52e78e7925f458cd83dacbdedc342ce486283b9b454cde63c51a04fd8e0529d902e495";
	public static final String sINSTALLRSAKEYSTEP3 = "80c6010156c2021312c7501cb8e57dadd0b9bf45e51abde921b0e628792043dc7d9f5378fdc6" +
			"8934e5b9e5e4c938b7a2c0b9c231738d27d90c0683d7228490c2dc322d4f62083f029d03c9349f1b1ecacd6981baabed2a4a6ae98d";
	public static final String sINSTALLRSAKEYSTEP4 = "80c6010156c2021312c450e7a10de6c297080e347c0a8ef52ff6f3c6f119536dfdef1963e0f5" +
			"3ea9a2150ae7a5eed010d6270e55d37b0cf813fe02f6329776242cc11b9a3ac82c2371223bcb1887ce6839b3ead78dc3cde14d0307";
	public static final String sINSTALLRSAKEYSTEP5 = "80c6010156c2021312c85018ca8f4d759640b4281d63d31e3ee99d3763a5883d520baa656d84" +
			"819e0a470754964990a3d8616c3b61de84a329eb035297211475546e77fc97d88ea3c14391a1dcc14e60f2dadf2b81285c3427aea0";
	
	public static final String sEXPORTPUBLICKEY1 = "80c9210108c002a0f3c2021312";
	public static final String sEXPORTPUBLICKEY2 = "80c9218189";
	
	public static final String sVERIFYCERTDATA1 = "80c8000114cc0221d3cd02a0f3ce02a0f2cf06101110131012";
	public static final String sVERIFYCERTDATA2 = "80c88001fcc18203bf308203bb308202a3a00302010202087c3200c60002b8aa300d06092a86488" +
			"6f70d0101050500303d310b300906035504061302434e310b3009060355040a0c0247413110300e06035504030c0765494420434132310f300d06" +
			"035504050c06323031313030301e170d3134303730373032323730375a170d3137303730363032323730375a3035310f300d060355040a0c06333" +
			"130303030310b300906035504061302434e3115301306035504030c0c65494431323334353637383930819f300d06092a864886f70d0101010500" +
			"03818d00308189028181008267fc780a8797ff7be5d9d8b12a399f5dc8c645aaadaefcf544c4c57ea525";
	public static final String sVERIFYCERTDATA3 = "80c88041f8472a9e5b88a36d71c5d02bb43b90d3eb59ad1662c93d1be0417e9616455331e90ed56" +
			"531ef9487072ce3745ccfdc9c3db7659b99faf89a28c37084287801b3701854721aae0a3e76b5cdf07f7ba97d00f1b881a5b88148a55d78484e52" +
			"394cb6670203010001a382014930820145300c0603551d1304053003010100301d0603551d250416301406082b0601050507030206082b0601050" +
			"5070304300b0603551d0f0404030200c0301106096086480186f8420101040403020080303a0603551d2004333031302f06082a811c8145080101" +
			"3023302106082b060105050702011615687474703a2f2f6569642e6e65742e636e2f63707330";
	public static final String sVERIFYCERTDATA4 = "80c88041f8380603551d1f0431302f302da02ba0298627687474703a2f2f63726c2e6569642e6e6" +
			"5742e636e2f654944434132523230313130302e63726c304006082b0601050507010104343032303006082b060105050730028624687474703a2f" +
			"2f63657274732e6569642e6e65742e636e202f654944434132522e636572301f0603551d23041830168014cde385754622dfc6d51c10ae8a36ba7" +
			"eb821434c301d0603551d0e041604149364be2d6bb6ca51c230617317578a1b89245f3a300d06092a864886f70d010105050003820101000c377d" +
			"e2f2e109e60e7b96a33cbd4490d8ef2b17e130039e8410cc35beb0f5b0f212012219b811c1dd";
	public static final String sVERIFYCERTDATA5 = "80c88081d76697612a3e492bd3d87fcb3a8e1e2696f9d033e24eb8de5a092f288b615e2c89cf1c8" +
			"8904f979b496f192e9777f9b49264975ac2458e1e2b9c3677ecfceea4a972c54b856c293b36304a6d921122ea51a46523f0d7db12a5b49f621c4a" +
			"cccf8adc9f04abb1a69fee0102693bab244a537b8a86ef3891691d5d9f8ac65b5bbd92e4490271b35dff78b826fc06feac971c763b6a51c40bf56" +
			"fb4513e53e70846a4ebf1fc6438daa70423cee0caf7402c34fc9ba826045a0f74878142327b87b0f11890ce1604f3860e9415a87faf9e4bdcb21e" +
			"f5d364a42c";
	
	public static final String sRSAPRIVATEKEYSIGN = "8048400087c2021012c181800001fffffffffffffffffffffffffffffffffffffffffffffffff" +
			"fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
			"ffffffffffffff003021300906052b0e03021a050004145d211bad8f4ee70e16c7d343a838fc344a1ed961";
	
	public static final String sRSAPUBLICEKEYAUTH = "8046c00087c0021013c18180597002e862aebd55513d5a58d19504c404434faa7b85cef3ae0d9" +
			"e5a5327048678fadd68a52eb7840701d03768a011fe2432be240a495d4229acf4f0c099bb8c63d4fbe16766116bf9c15af6304271826598943ec8" +
			"4395204c7b923273245f2029de8832aa965528eea2f91dd7b7cebb0d5dced9959dae8a485469dcef902729";
	
	
	
	
	
	
	
	
	
	
}
