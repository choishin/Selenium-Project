package gourmet;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Main {

	public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
	public static final String WEB_DRIVER_PATH = "C:\\chromedriver.exe";
	public static String base_url;

	public static void main(String[] args) throws InterruptedException, IOException {

//		fileMake();
//		headWrite();
		ReadData();
//		fileClose();

	}

	static int rowsCnt;
	public static BufferedWriter bfw;
	public static boolean isFileExist;

	static void ReadData() throws IOException {

		try {

			System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--start-maximized"); // 전체화면으로 실행
			options.addArguments("--disable-popup-blocking"); // 팝업 무시
			options.addArguments("--disable-default-apps"); // 기본앱 사용안함

			ChromeDriver driver = new ChromeDriver(options);
			base_url = "https://m.map.naver.com/";
			driver.get(base_url);

			// 입력창에 검색어 순서대로 넣어서 결과 가지고 오기
			Thread.sleep(2000);
			driver.findElement(By.xpath("/html/body/div[2]/header/div[4]/div/div/div/span[1]")).click();
			Thread.sleep(2000);
			driver.findElement(By.xpath("/html/body/div[3]/div[1]/div[1]/form/div/div[2]/div/span[1]/input"))
					.sendKeys("서현역 한식 맛집");
			Thread.sleep(2000);
			driver.findElement(By.xpath("/html/body/div[3]/div[1]/div[1]/form/div/div[2]/div/span[1]/input"))
					.sendKeys(Keys.ENTER);
			Thread.sleep(2000);

			// 행개수 가져오기
			WebElement body = driver.findElement(By.xpath("/html/body/div[4]/div[2]/ul"));
			List<WebElement> rows = body.findElements(By.xpath("/html/body/div[4]/div[2]/ul/li"));
			rowsCnt = rows.size();
			System.out.println(rowsCnt);

			for (int i = 1; i <= rowsCnt; i++) {
				// 링크누르기
				try {
					Thread.sleep(5000);
					if (driver.findElements(By.xpath("/html/body/div[4]/div[2]/ul/li[" + i + "]/div[1]/a[2]")).size() > 0) {
						driver.findElement(By.xpath("/html/body/div[4]/div[2]/ul/li[" + i + "]/div[1]/a[2]")).click();
					} else if (driver.findElements(By.xpath("/html/body/div[4]/div[2]/ul/li[" + i + "]/div[1]/a")).size() > 0) {
						Thread.sleep(5000);
						driver.findElement(By.xpath("/html/body/div[4]/div[2]/ul/li[" + i + "]/div[1]/a")).click();
					}
				} catch (NoSuchElementException e) {
					System.out.println(e);
					continue;
				}
				// 링크열기
				Thread.sleep(3000);
				String currentURL = driver.getCurrentUrl();
				driver.get(currentURL);
				Thread.sleep(3000);
				// 이름가져오기
				String restaurantName = driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/div[1]/div/div/div[1]/span/span[1]")).getText();
				Thread.sleep(3000);
				// 별접가져오기
				String stars;
				try {
					if (driver.findElements(By.xpath("/html/body/div[3]/div/div[2]/div[1]/div/div/div[1]/div/span[1]/em")).size() > 0) {
						stars = driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/div[1]/div/div/div[1]/div/span[1]/em")).getText();
					} else {
						stars = "정보없음";
					}
				} catch (Exception e) {
					System.out.println(e);
					continue;
				}
				//방문자리뷰개수
				String visitorsReview;
				if (driver.findElements(By.xpath("/html/body/div[3]/div/div[2]/div[1]/div/div/div[1]/div/span[2]/a")).size() >0) {
					visitorsReview= driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/div[1]/div/div/div[1]/div/span[2]/a"))
						.getText();
				}else {
					visitorsReview = "정보없음";
				}
				//블로거리뷰개수
				String bloggerReview;
				if (driver.findElements(By.xpath("/html/body/div[3]/div/div[2]/div[1]/div/div/div[1]/div/span[3]/a")).size() >0) {
					bloggerReview = driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/div[1]/div/div/div[1]/div/span[3]/a")).getText();
				} else {
					bloggerReview = "정보없음";
				}
				//주소, 소개 등 정보 한번에 가져오기
				String context = driver.findElement(By.className("_6aUG7")).getText();
				System.out.println(restaurantName);
				System.out.println(stars);
				System.out.println(visitorsReview);
				System.out.println(bloggerReview);
				System.out.println(context);				
				//메뉴페이지에서 가격 긁어오기
				Thread.sleep(5000);
				driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/div[3]/div/div/div/div/a[2]")).click();
				Thread.sleep(3000);
				WebElement menus = driver.findElementByXPath("/html/body/div[3]/div/div[2]/div[4]/div/div[1]/div[1]/ul");
				int menuCnt = menus.findElements(By.className("_3j-Cj")).size();
				System.out.println(menuCnt);
				String menu = driver.findElement(By.className("_3j-Cj")).getText();
				System.out.println(menu);
				Thread.sleep(3000);
				driver.navigate().back();						
				driver.navigate().back();
			}

			driver.close();
			Thread.sleep(2000);
			driver.quit();

		} catch (InterruptedException e) {
			System.out.println(e);
		}
	}
}

//	static void fileMake() {
//		try {
//			File file = new File("vaccine_daily.csv");
//			if (file.exists() == false) {
//				isFileExist = false;
//			} else {
//				isFileExist = true;
//			}
//			bfw = new BufferedWriter(new FileWriter(file, true));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			System.out.println(e);
//		}
//	}
//
//	static void headWrite() throws IOException {
//
//		if (isFileExist == false) {
//			String head = "일자," + "구분," + "1회차접종(당일실적)," + "1회차접종(당일누계)," + "2회차접종(당일실적)," + "2회차접종(당일누계)," + "\n";
//			bfw.write(head);
//		}
//	}
//
//	static void fileWrite(String str) throws IOException {
//	
//			bfw.append(str);
//	
//	}
//
//	static void fileClose() {
//		try {
//			bfw.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//}
//
