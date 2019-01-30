package utils;

public class NetworkUtil {
	
	public static String binarytoString(String data) {
	    String str = "";

	    for (int i = 0; i < data.length()/8; i++) {

	        int a = Integer.parseInt(data.substring(8*i,(i+1)*8),2);
	        str += (char)(a);
	    }

	    return str;
	}
	
	public static String stringToBinary(String data) {
		String bin = "";
		String tmp = "";
		for(int i = 0; i < data.length(); i++) {
			tmp = Integer.toBinaryString(data.charAt(i));
			while(tmp.length() < 8) {
				tmp = "0"+tmp;
			}
			bin += tmp;
		}
		return bin;
	}
}
