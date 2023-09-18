import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ScrapeYouTube {
    static ArrayList<String> titles;
    static ArrayList<String> links;

    static void startScrape(String name){
        String youtubeURL = "https://www.youtube.com/results?search_query="+name;
        Document website = null;
        try {
            website = Jsoup.connect(youtubeURL).get();

            TextAreaAndProgressBar.addText("Scraped YouTube successfully, populating table...");
        } catch (IOException e) {
            e.printStackTrace();
            TextAreaAndProgressBar.addText("Error:  Could not scrape YouTube...\nTry Again");
        }
        
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
                title[i] = (i+1)+". "+temp.text();
                i++;
            }
        }
        for(int j=0; j<seperateLinks.length;j++){
            System.out.println(title[j]);
            
        }
        titles = new ArrayList<>(Arrays.asList(title));
        links = new ArrayList<>(Arrays.asList(seperateLinks));
        SelectSong.populateTable(titles);

    }
}
