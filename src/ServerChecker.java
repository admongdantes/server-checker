import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class ServerChecker {

	private int totalCnt = 0;
	private int checkErrorCount = 3;
	private int checkErrorTerm = 5000;
	private String checkUrl;
	private final String LOG_FILE_NAME = "server_checker.log";
	private Calendar cal = Calendar.getInstance();
	private SimpleDateFormat sdf = new SimpleDateFormat("");
	
	public ServerChecker() {
		//checkUrl();
	}
	
	
	
	private void checkUrl() {
		URL url = null;
		
		try{
			url = new URL(checkUrl);
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
	
	private void startUrlCheck() {
		while(true) {
			try{
				Thread.sleep(checkErrorTerm);
				checkUrl();
				logToFile("");
				if(totalCnt > checkErrorCount) {
					stop();
					break;
				}
			} catch(Exception e) {
				stop();
				break;
			}
		}
	}
	
	private void logToFile(String msg) throws Exception {
		File f = new File("/Users/sayit/a.txt");
		FileOutputStream fos = new FileOutputStream(f, true);
		msg = msg + cal.toString();
		fos.write(msg.getBytes());
		fos.close();
	}
	private void stop() {
		System.out.println("stop");
		start();
	}

	private void start() {
		System.out.println("start");
		totalCnt = 0;
		
		startUrlCheck();
	}

	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ServerChecker c = new ServerChecker();
		c.setCheckUrl("http://122.199.152.200:8181/login.do");
		c.startUrlCheck();
	}


	/**
	 * üũ�� �ּҸ� ��ȯ�Ѵ�.
	 * @return
	 */
	public String getCheckUrl() {
		return checkUrl;
	}
	
	/**
	 * üũ�� �ּҸ� �����Ѵ�.
	 * @param checkUrl
	 */
	public void setCheckUrl(String checkUrl) {
		this.checkUrl = checkUrl;
	}
	
	/**
	 * üũ�� �������� �����Ѵ�.
	 * @param checkErrorCount
	 */
	public void setCheckErrorCount(int checkErrorCount) {
		this.checkErrorCount = checkErrorCount;
	}
	
	/**
	 * üũ�� �������� ��ȯ�Ѵ�.
	 * @return
	 */
	public int getCheckErrorCount() {
		return this.checkErrorCount;
	}
	
	/**
	 * üũ�ֱ⸦ �����Ѵ�.
	 * @param checkErrorTerm �и�������
	 */
	public void setCheckErrorTerm(int checkErrorTerm) {
		this.checkErrorTerm = checkErrorTerm;
	}
	
	/**
	 * üũ�ֱ⸦ ��ȯ�Ѵ�.
	 * @return �и�������
	 */
	public int getCheckErrorTerm() {
		return this.checkErrorTerm;
	}
	
	

}
