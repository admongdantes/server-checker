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
	 * 체크할 주소를 반환한다.
	 * @return
	 */
	public String getCheckUrl() {
		return checkUrl;
	}
	
	/**
	 * 체크할 주소를 설정한다.
	 * @param checkUrl
	 */
	public void setCheckUrl(String checkUrl) {
		this.checkUrl = checkUrl;
	}
	
	/**
	 * 체크할 에러수를 설정한다.
	 * @param checkErrorCount
	 */
	public void setCheckErrorCount(int checkErrorCount) {
		this.checkErrorCount = checkErrorCount;
	}
	
	/**
	 * 체크할 에러수를 반환한다.
	 * @return
	 */
	public int getCheckErrorCount() {
		return this.checkErrorCount;
	}
	
	/**
	 * 체크주기를 세팅한다.
	 * @param checkErrorTerm 밀리세컨드
	 */
	public void setCheckErrorTerm(int checkErrorTerm) {
		this.checkErrorTerm = checkErrorTerm;
	}
	
	/**
	 * 체크주기를 반환한다.
	 * @return 밀리세컨드
	 */
	public int getCheckErrorTerm() {
		return this.checkErrorTerm;
	}
	
	

}
