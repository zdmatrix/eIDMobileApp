package zdmatrix.hed.eidmobileapp.functionmoudle;


import java.util.Random;

import android.R.integer;

import mafei.hed.nfcapplication.NFCApplication;
import mafei.hed.nfcapplication.NFCMsgCode;
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
	
	public static String[] ErrorProcess(int n){
		String[] strRet = new String[2]; 
		switch (n) {
		case NFCMsgCode.nNOT_SUPPORT_NFC:
			strRet[StaticData.nDATA] = null;
			strRet[StaticData.nSW] = StaticData.sNOTSUPPORTNFC;
			break;
		case NFCMsgCode.nNOT_OPEN_NFC:
//			msg = "#" + String.format("%1$03d", nClickCount) + "  ";
			strRet[StaticData.nDATA] = null;
			strRet[StaticData.nSW] = StaticData.sOPENNFC;
			break;
		case NFCMsgCode.nNO_NFC_INTENT:
			strRet[StaticData.nSW] = StaticData.sNONFCINTENT;
			break;
		case NFCMsgCode.nTAG_CONNECT_FAILED:
			strRet[StaticData.nSW] = StaticData.sCONNECTFAILED;
			break;	
		default:
			strRet[StaticData.nDATA] = null;
			strRet[StaticData.nSW] = StaticData.sUNKNOWERR;
			break;
		}
		return strRet;
	}
	
	public static String[] APDUResponseProcess(String str){
		
		String[] strRet = new String[2]; 
		String strResponse;
		String tmp = "";
		int len = str.length();
		if(len < 4){
			strRet[StaticData.nDATA] = null;
			strRet[StaticData.nSW] = StaticData.sSWERROR;
		}else{			
			if(len > 4){
				tmp = str.substring(len - StaticData.nSWSTARTINDEX, len);
				if(tmp.substring(0, 1).equals(StaticData.sFETCHNEXTDATA)){
					strRet[StaticData.nDATA] += str.substring(0, len - StaticData.nDATAENDINDEX);
					do {
						int length = Integer.parseInt(tmp.substring(2, 3), 16);
						StaticData.bFETCHNEXTDATA[4] = (byte)length;
						strResponse = APDUCmd(StaticData.bFETCHNEXTDATA);
						strRet[StaticData.nDATA] += strResponse.substring(0, strResponse.length() - StaticData.nDATAENDINDEX);
						tmp = strResponse.substring(strResponse.length() - StaticData.nSWSTARTINDEX, strResponse.length() - StaticData.nSWENDINDEX);					
					} while (tmp.substring(0, 1).equals(StaticData.sFETCHNEXTDATA));
//					strRet[StaticData.nSW] = StaticData.sSWOK;
				}else if(tmp.equals("9000")){
					strRet[StaticData.nSW] = StaticData.sSWOK;
					strRet[StaticData.nDATA] = str.substring(0, len - StaticData.nDATAENDINDEX);
				}else{
					strRet[StaticData.nSW] = tmp;
					strRet[StaticData.nDATA] = str.substring(0, len - StaticData.nDATAENDINDEX);
				}
			}else{
				strRet[StaticData.nSW] = str;
			}			
		}
		return strRet;
	}
	
	public static String ReturnData(byte[] ret){
		String strRet = "";
		for(int i = 0; i < ret.length; i ++){
			strRet += String.format("%1$02x", (byte)ret[i]);			 
		}
		return strRet;
	}
	
	public static String APDUCmd(byte[] apdu){
		String strRet = "";
		byte[] ret = NFCApplication.DataTransfer(apdu);
		strRet = ReturnData(ret);
		return strRet;
	}
	
	public static String[] BinDataRW(String input, int bindata, int processtype){
		String[] strRet = new String[2];
		String response = "";
		String tmp = "";
		byte[] apdubanlance = new byte[7];
		byte[] apdudatarw = new byte[9];
		byte[] data = new byte[4];
		
		switch (processtype) {
		case StaticData.nEXPENSE:
			bindata = bindata - Integer.parseInt(input, 10);
			if(bindata < 0){
				strRet[StaticData.nSW] = StaticData.sOVERBANLANCE;
			}else{
				tmp = String.format("%1$08x", bindata);
				data = string2ByteArray(tmp);
				System.arraycopy(StaticData.bUPDATEBINARAYDATA, 0, apdudatarw, 0, StaticData.nAPDULEN);
				System.arraycopy(data, 0, apdudatarw, StaticData.nAPDULEN, StaticData.nBINDATALEN);
				response = APDUCmd(apdudatarw);
				strRet = APDUResponseProcess(response);
				if(!strRet[StaticData.nSW].equals(StaticData.sSWOK)){
					strRet[StaticData.nSW] += StaticData.sUPBINDATAERR;
				}					
			}			
			break;
		case StaticData.nRECHARGE:				
			bindata += Integer.parseInt(input, 10);
			if(bindata > 1000){
				strRet[StaticData.nSW] = StaticData.sOVERECASHLIMIT;
			}else{
				tmp = String.format("%1$08x", bindata);
				data = string2ByteArray(tmp);
				System.arraycopy(StaticData.bUPDATEBINARAYDATA, 0, apdudatarw, 0, StaticData.nAPDULEN);
				System.arraycopy(data, 0, apdudatarw, StaticData.nAPDULEN, StaticData.nBINDATALEN);
				response = APDUCmd(apdudatarw);
				strRet = APDUResponseProcess(response);
				if(!strRet[StaticData.nSW].equals(StaticData.sSWOK)){
					strRet[StaticData.nSW] += StaticData.sUPBINDATAERR;
				}
			}
			break;
		case StaticData.nREADBANLANCE:			
			System.arraycopy(StaticData.bSELECTFILE, 0, apdubanlance, 0, StaticData.nAPDULEN);
			System.arraycopy(StaticData.bBINARAYFILEID, 0, apdubanlance, StaticData.nAPDULEN, StaticData.nFILEIDLEN);
			response = FunctionMoudle.APDUCmd(apdubanlance);
			strRet = FunctionMoudle.APDUResponseProcess(response);
			if(strRet[StaticData.nSW].equals(StaticData.sSWOK)){
				response = FunctionMoudle.APDUCmd(StaticData.bREADBINARAYDATA);
				strRet = FunctionMoudle.APDUResponseProcess(response);
				if(!strRet[StaticData.nSW].equals(StaticData.sSWOK)){
					strRet[StaticData.nSW] += StaticData.sREADBANLANCEERR;					
				}				
			}else{
				strRet[StaticData.nSW] += StaticData.sSELECTFILEERR;
				
			}
			break;
		default:
			strRet[StaticData.nSW] += StaticData.sUNKNOWERR;
			break;
		}			
		return strRet;
	}
	
	public static byte[] getImageData(String sdata){
		int length = sdata.length();
//		int ndata[] = DataTypeTrans.stringDecToIntArray(sdata);
		int[] ndata = new int[length];
		char[] ctmp = sdata.toCharArray();
		for(int i = 0; i < length; i ++){
			ndata[i] = (int)(ctmp[i] - 0x30);
		}
		int ndataarray[] = new int[StaticData.nIMAGEWIDTH];
		byte[] bRet = new byte[StaticData.nIMAGEWIDTH];
		for(int i = 0; i < StaticData.nIMAGEWIDTH; i ++){
			ndataarray[i] = 0xff;
		}
			
		
		// 位图文件的类型，必须为BM
		ndataarray[0x00] = 0x42;		// B
		ndataarray[0x01] = 0x4D;		// M

		// 位图文件的大小，以字节为单位
		ndataarray[0x02] = 0xD6;
		ndataarray[0x03] = 0x01;
		//把4张图片合成在一起，4 × 470
//		ndataarray[0x02] = 0x58;
//		ndataarray[0x03] = 0x07;
		//
		ndataarray[0x04] = 0x00;
		ndataarray[0x05] = 0x00;

		// 位图文件保留字，必须为0
		ndataarray[0x06] = 0x00;
		ndataarray[0x07] = 0x00;
		ndataarray[0x08] = 0x00;
		ndataarray[0x09] = 0x00;

		// 位图数据的起始位置，以相对于位图
		ndataarray[0x0A] = 0x3E;
		ndataarray[0x0B] = 0x00;
		ndataarray[0x0C] = 0x00;
		ndataarray[0x0D] = 0x00;

		// 位图信息头的长度
		ndataarray[0x0E] = 0x28;
		ndataarray[0x0F] = 0x00;
		ndataarray[0x10] = 0x00;
		ndataarray[0x11] = 0x00;

		// 位图的宽度
		ndataarray[0x12] = 0xC0;
		ndataarray[0x13] = 0x00;
		ndataarray[0x14] = 0x00;
		ndataarray[0x15] = 0x00;

		// 位图的高度
		ndataarray[0x16] = 0x11;
//		ndataarray[0x16] = 0x44;		//为了4张图拼接在一起
		ndataarray[0x17] = 0x00;
		ndataarray[0x18] = 0x00;
		ndataarray[0x19] = 0x00;

		// 位图的位面数
		ndataarray[0x1A] = 0x01;
		ndataarray[0x1B] = 0x00;

		// 每个象素的位数
		ndataarray[0x1C] = 0x01;
		ndataarray[0x1D] = 0x00;

		// 压缩说明
		ndataarray[0x1E] = 0x00;
		ndataarray[0x1F] = 0x00;
		ndataarray[0x20] = 0x00;
		ndataarray[0x21] = 0x00;

		// 用字节数表示的位图数据的大小，该数必须是4的倍数
		ndataarray[0x22] = 0x98;
		ndataarray[0x23] = 0x01;
		
//		ndataarray[0x22] = 0x1a;
//		ndataarray[0x23] = 0x07;
		
		ndataarray[0x24] = 0x00;
		ndataarray[0x25] = 0x00;

		// 用象素/米表示的水平分辨率
		ndataarray[0x26] = 0x00;
		ndataarray[0x27] = 0x00;
		ndataarray[0x28] = 0x00;
		ndataarray[0x29] = 0x00;

		// 用象素/米表示的垂直分辨率
		ndataarray[0x2A] = 0x00;
		ndataarray[0x2B] = 0x00;
		ndataarray[0x2C] = 0x00;
		ndataarray[0x2D] = 0x00;

		// 位图使用的颜色数
		ndataarray[0x2E] = 0x00;
		ndataarray[0x2F] = 0x00;
		ndataarray[0x30] = 0x00;
		ndataarray[0x31] = 0x00;

		// 指定重要的颜色数
		ndataarray[0x32] = 0x00;
		ndataarray[0x33] = 0x00;
		ndataarray[0x34] = 0x00;
		ndataarray[0x35] = 0x00;

		// 调色板 (黑色)
		ndataarray[0x36] = 0x00;
		ndataarray[0x37] = 0x00;
		ndataarray[0x38] = 0x00;
		ndataarray[0x39] = 0x00;

		// 调色板 (底色)
		ndataarray[0x3A] = 0xFF;
		ndataarray[0x3B] = 0xFF;
		ndataarray[0x3C] = 0xFF;
		ndataarray[0x3D] = 0x00;
		
		
		int xstart = 0;
		switch(length){
		case 15:
		case 16:
			xstart = 0;
			break;
			
		case 13:
		case 14:
			xstart = 12;
			break;
			
		case 11:
		case 12:
			xstart = 24;
			break;
			
		case 9:
		case 10:
			xstart = 36;
			break;
		
		case 7:
		case 8:
			xstart = 48;
			break;
			
		case 5:
		case 6:
			xstart = 60;
			break;
		
		case 3:
		case 4:
			xstart = 72;
			break;	
			
		case 1:
		case 2:
			xstart = 84;
			break;		
			
		default:
			break;
			
			
		}
		int ystart = 0;
		
		for(int i = 0; i < length; i ++){
			int num = ndata[i] & 0x0f;
			display7Seg(xstart, ystart, StaticData.NUM09[num], ndataarray);
			xstart += 12;
		}
		for(int i = 0; i < StaticData.nIMAGEWIDTH; i ++){
			bRet[i] = (byte)ndataarray[i];
		}
		return bRet;
	}
	
	public static byte[] getWholeImage(int[] src, byte[] dst, int index){
		byte[] tmp = new byte[StaticData.nIMAGEWIDTH];
		for(int i = 0; i < StaticData.nIMAGEWIDTH; i ++)
			tmp[i] = (byte)src[i];
		System.arraycopy(tmp, 0, dst, index * StaticData.nIMAGEWIDTH, StaticData.nIMAGEWIDTH);
		return dst;
	}
	
	private static void display7Seg (int nx, int ny, final int[] staticbuf, int[] buf)
	{
		int		z = 0;
		int[][] p = new int[6][];
		for(int i = 0; i < 6; i ++)
			p[i] = new int[2];
		Random	rand = new Random();
		
		
		
		int x1, y1, x2 = 0, y2 = 0;

		for ( z = 0; z < 6; z++ )
		{
			int random = rand.nextInt(3);
			p[z][0] = StaticData.ORG[z][0] + random;
			random = rand.nextInt(3);
			p[z][1] = StaticData.ORG[z][1] + random;
		}
		
		x1 = p[staticbuf[1] - 1][0];
		y1 = p[staticbuf[1] - 1][1];
		for ( z = 2; z <= staticbuf[0]; z++ )
		{
			x2 = p[staticbuf[z] - 1][0];
			y2 = p[staticbuf[z] - 1][1];
			drawLine (nx + x1, ny + y1, nx + x2, ny + y2, 0, buf);
/*
			// 将边线加宽
			if ( x1 < 7 && x2 < 7 )
				drawLine ( nx + x1 - 1, ny + y1, nx + x2 - 1, ny + y2, 0, buf );
			else if ( x1 >= 7 && x2 >= 7 )
				drawLine ( nx + x1 + 1, ny + y1, nx + x2 + 1, ny + y2, 0, buf );
			else if ( y1 < 7 && y2 < 7 )
				drawLine ( nx + x1, ny + y1 - 1, nx + x2, ny + y2 - 1, 0, buf );
			else
				drawLine ( nx + x1, ny + y1 + 1, nx + x2, ny + y2 + 1, 0, buf );
*/
			// 交换坐标
			x1 = x2;
			y1 = y2;
			x2 = 0;
			y2 = 0;
		}

		
	}
	
	private static void drawLine(int x0, int y0, int x1, int y1, int color, int[] buf){
		boolean steep = false;
		int deltax;
		int deltay;
		int error;
		int ystep;
		int x;
		int y;
		steep = Math.abs(y1 - y0) > Math.abs(x1 - x0);
		if ( steep )
		{
			int tmp = 0;
			tmp = x0;
			x0 = y0;
			y0 = tmp;
			
			tmp = x1;
			x1 = y1;
			y1 = tmp;
//			swap ( x0, y0 );
//			swap ( x1, y1 );
		}

		if ( x0 > x1 )
		{
			int tmp = x0;
			x0 = x1;
			x1 = tmp;
			
			tmp = y0;
			y0 = y1;
			y1 = tmp;
//			swap ( x0, x1 );
//			swap ( y0, y1 );
		}

		deltax = x1 - x0;

		deltay = Math.abs( y1 - y0 );

		error = deltax / 2;

		y = y0;

		if ( y0 < y1 )
			ystep = 1;
		else
			ystep = -1;

		for ( x = x0; x <= x1; x ++ )
		{

			if ( steep )
				setPixel ( y, x, color, buf);
				
			else{
				
//				if(y < 0)
//					Log.v("zdmatrix", "输入的y像素错误，为：" + y);
				setPixel ( x, y, color, buf);
			}
			error = error - deltay;

			if ( error < 0 )
			{
				y = y + ystep;
				error = error + deltax;
			}
		}
	}

	private static void setPixel(int x, int y, int color, int[] buf){
		int gx = 0x3E + ((16 - y) * 24) + (x / 8);
		int gPixelMask = (0x80 >> (x % 8));
		buf[gx] &= ~gPixelMask;
	}
	
	public static byte[] string2ByteArray(String s){
		int len = s.length() / 2;
		byte[] bRet = new byte[len];
		for(int i = 0, k = 0; i < len; i ++){
			bRet[i] = (byte)Integer.parseInt(s.substring(k, k + 2), 16);
			k += 2;
		}
		return bRet;
	}
	

	
}
