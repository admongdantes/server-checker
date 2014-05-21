import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ServerChecker {

	private int totalCnt = 0;
	private int checkErrorCount = 3;
	private int checkErrorTerm = 300000;
	private String checkUrl;
	private final String LOG_FILE_NAME = "/root/server_checker.log";
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private String serverStartCmd = "";
	private String serverStopCmd = "";
	
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
			logToFile("resultCode - "+result + " - errorCount : "+ totalCnt);
			if(result != 200) {
				totalCnt++;
			} else {
				totalCnt = 0;
			}
					
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void startUrlCheck() throws Exception {
		while(true) {
			try{
				Thread.sleep(checkErrorTerm);
				checkUrl();
				
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
		File f = new File(LOG_FILE_NAME);
		String date = sdf.format(new Date());
		FileOutputStream fos = new FileOutputStream(f, true);
		msg = date + " : "+msg + "\n";
		fos.write(msg.getBytes());
		fos.close();
	}
	private void stop() throws Exception {
		System.out.println("stop");
		BufferedReader br = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(serverStopCmd).getInputStream()));
		
		while(true) {
			String line = br.readLine();
			if(line == null) {
				break;
			}
			logToFile(line);
		}
		br.close();
		logToFile("server stop");
		
		Thread.sleep(5000);
		start();
	}

	private void start() throws Exception {
		System.out.println("start");
		totalCnt = 0;
		BufferedReader br = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(serverStartCmd).getInputStream()));
		
		while(true) {
			String line = br.readLine();
			if(line == null) {
				break;
			}
			logToFile(line);
		}
		br.close();
		logToFile("server start");
		startUrlCheck();
	}	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ServerChecker c = new ServerChecker();
		c.setCheckUrl("http://122.199.152.200:8181/login.do");
		c.setServerStartCmd("sh /webapps/tomcat/bin/startup.sh");
		c.setServerStopCmd("sh /webapps/tomcat/bin/shutdown.sh");
		
		try{
			c.startUrlCheck();
		}catch(Exception e) {
			e.printStackTrace();
		}
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
	
	private void setServerStartCmd(String cmd) {
		this.serverStartCmd = cmd;
	}
	
	private void setServerStopCmd(String cmd) {
		this.serverStopCmd = cmd;
	}
	
	private String getServerStartCmd() {
		return this.serverStartCmd;
	}
	
	private String getServerStopCmd() {
		return this.serverStopCmd;
	}
}
