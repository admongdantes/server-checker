import java.net.HttpURLConnection;
import java.net.URL;


public class ServerChecker {

	private int totalCnt = 0;
	
	public ServerChecker() {
		//checkUrl();
	}
	
	private void checkUrl() {
		URL url = null;
		
		try{
			url = new URL("http://122.199.152.200:8181/login.do");
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.connect();
			int result = conn.getResponseCode();
			System.out.println("result : " + result);
			
			if(result != 200) {
				totalCnt++;
			} else {
				totalCnt = 0;
			}
					
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void aaa() {
		while(true) {
			try{
				Thread.sleep(5000);
				checkUrl();
				
				if(totalCnt > 3) {
					stop();
					break;
				}
			} catch(Exception e) {
				stop();
				break;
			}
		}
	}
	
	private void stop() {
		System.out.println("stop");
		start();
	}

	private void start() {
		System.out.println("start");
		totalCnt = 0;
		
		aaa();
	}

	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ServerChecker c = new ServerChecker();
		c.aaa();
	}

}
