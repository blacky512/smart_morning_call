package voice.tUDP;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.http.conn.util.InetAddressUtils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

/**
 * 서버소켓 주소를 알기 위해 사용
 * @type 1: IPv4, 2: IPv6 
 * @author kyobum
 *
 */
public class Utils {
	public final static int INET4ADDRESS = 1;
	public final static int INET6ADDRESS = 2;
	
	static ConnectivityManager	connManager;
	static boolean 				isWifi;
	
	public static String getIpAddress(Activity activity){
		connManager	= (ConnectivityManager) activity.getSystemService (Context.CONNECTIVITY_SERVICE);
		isWifi 		= connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
		
		if(isWifi){
			WifiManager wifiManager = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			int ipAddress = wifiInfo.getIpAddress();
	         	         
	        return intToIp(ipAddress);
		}else{
			//@ 3G 및 4G의 경우 안된다고 알리도록 추가해야함
			return getLocalIpAddress2();
			//return getLocalIpAddress(1);
	    }	
	}
	
	private static String intToIp(int i) { 
    	return (i  & 0xFF) + "." +
    	((i >> 8 ) & 0xFF) + "." +
    	((i >> 16 ) & 0xFF) + "." +
    	((i >> 24 ) & 0xFF);
    }
	 

	public static String getLocalIpAddress(int type) {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = ( NetworkInterface ) en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = ( InetAddress ) enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						switch (type) {
						case INET6ADDRESS:
							if (inetAddress instanceof Inet6Address) {
								return inetAddress.getHostAddress().toString();
							}
							break;

						case INET4ADDRESS:
							if (inetAddress instanceof Inet4Address) {
								return inetAddress.getHostAddress().toString();
							}
							break;
						}
					}
				}
			}
		} catch (SocketException ex) {
		}
		return null;
	}
	
	public static String getLocalIpAddress2() {
	    try {
	        for (Enumeration<NetworkInterface> en = NetworkInterface
	                .getNetworkInterfaces(); en.hasMoreElements();) {
	            NetworkInterface intf = en.nextElement();
	            for (Enumeration<InetAddress> enumIpAddr = intf
	                    .getInetAddresses(); enumIpAddr.hasMoreElements();) {
	                InetAddress inetAddress = enumIpAddr.nextElement();
	                System.out.println("ip1--:" + inetAddress);
	                System.out.println("ip2--:" + inetAddress.getHostAddress());

	      // for getting IPV4 format
	      if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())) {

	                    String ip = inetAddress.getHostAddress();
	                    
	                    // return inetAddress.getHostAddress().toString();
	                    return ip;
	                }
	            }
	        }
	    } catch (Exception ex) {
	        //Log.e("IP Address", ex.toString());
	    }
	    return null;
	}
	
}
