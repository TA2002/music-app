import javax.swing.*;

public class ScrapeYouTubeThread extends SwingWorker<Void, Void>{

    @Override
    protected Void doInBackground() throws Exception {
        ScrapeYouTube.startScrape(SongPanel.songName);
        return null;
    }
}
