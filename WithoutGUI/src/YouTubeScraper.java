import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;

public class YouTubeScraper {

	public static void main(String[] args) throws IOException, InterruptedException{
		System.out.println("\t\t\t\t\t\tYouTube Downloader");
		System.out.println("Enter the song name");
		Scanner in = new Scanner(System.in);
		String name = in.nextLine();
		


		String youtubeURL = "https://www.youtube.com/results?search_query="+name;
		Document website = Jsoup.connect(youtubeURL).get();
		

		Elements subdiv = website.select("h3.yt-lockup-title>a");
		
		String[] seperateLinks = new String[subdiv.size()];
		String[] title = new String[subdiv.size()];
		int i =0;
		for(Element temp:subdiv)
		{	String a = temp.attr("href");
		
			if(a.contains("list"))
			{
				continue;
			}
			else{
				
				seperateLinks[i] = "https://www.youtube.com"+temp.attr("href");
				title[i] = temp.text();
				i++;
			}
		}
			for(int j=0; j<seperateLinks.length;j++){
				System.out.println((j+1)+": "+title[j]);
				
			}
			System.out.println("Please enter your choice");
			int choice = in.nextInt();
			choice--;
			String userchoice = seperateLinks[choice];
			
					
			BrowserEngine browser = BrowserFactory.getWebKit();
			
			com.ui4j.api.browser.Page docu = browser.navigate("http://www.listentoyoutube.com/");
			com.ui4j.api.dom.Document process = docu.getDocument();
			process.query("input[type='text']").get().setValue(userchoice);
			process.query("input[type='submit']").get().click();
			
			TimeUnit.SECONDS.sleep(20);
			String ur =(String) docu.executeScript("window.location.href");
			if(ur.contains("captcha")){
				System.out.println("Please go to http://www.listentoyoutube.com/captcha.php and prove you're not a robot and run the program again");
				System.exit(0);
			}
			ur=ur.substring(49, ur.length());
			
			String[] segments = ur.split("&");
			segments[1]=segments[1].substring(5, segments[1].length());
			/* segments[0] is the server number
			 * segments[1] is the hash code
			 * segments[2] is the file name
			 */
			segments[1]=segments[1].replaceAll("%253D%253D","");
			segments[2]=segments[2].substring(5, segments[2].length());
			String finalur = "http://"+segments[0]+".listentoyoutube.com/download/"+segments[1]+"==/"+segments[2];
			URL url = new URL(finalur);
			HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
			long fileSize = httpConnection.getContentLength();
			System.out.println(title[choice]+"\t\tSize : "+fileSize/1048576f+" mb");
			segments[2]=segments[2].replace("%7C", "");
			segments[2]=segments[2].replace("%22", "");
			segments[2]=segments[2].replace("%3F", "");
			segments[2]=java.net.URLDecoder.decode(segments[2], "UTF-8");
			String path = ""+segments[2]+".mp3";
			File file = new File(path);
			TimeUnit.SECONDS.sleep(5);
			try
			{	System.out.println("Downloading....");
				FileUtils.copyURLToFile(url, file);
				System.out.println("Download Complete");
			}
			catch(Exception e)
			{
				System.out.println("Got an IOException: " + e.getMessage());
				System.out.println("Download Failed");
			}
			finally{
				System.exit(0);
			}
			in.close();			
	}
}
