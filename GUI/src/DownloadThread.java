import javax.swing.*;
import java.awt.image.SinglePixelPackedSampleModel;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;

public class DownloadThread extends SwingWorker<Void, Void> {

    @Override
    protected Void doInBackground() throws Exception {
        try {
            TextAreaAndProgressBar.addText("Downloading...");

            URL url = new URL(ScrapeListenToYouTube.finalDownloadLink);
            HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
            long completeFileSize = httpConnection.getContentLength();

            java.io.BufferedInputStream in = new java.io.BufferedInputStream(httpConnection.getInputStream());
            java.io.FileOutputStream fos = new java.io.FileOutputStream(ScrapeListenToYouTube.finalPath);
            java.io.BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            byte[] data = new byte[1024];
            long downloadedFileSize = 0;
            int x = 0;
            while ((x = in.read(data, 0, 1024)) >= 0) {
                downloadedFileSize += x;

                final int currentProgress = (int) ((((double)downloadedFileSize) / ((double)completeFileSize)) * 100000d);

                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        TextAreaAndProgressBar.progressBar.setValue(currentProgress);
                    }
                });

                bout.write(data, 0, x);
            }

            bout.close();
            in.close();
            TextAreaAndProgressBar.addText("Download Completed. Enjoy!");
        }
        catch (FileNotFoundException e) {
            TextAreaAndProgressBar.addText("Woops, internal error occurred, please try again...");
            e.printStackTrace();
        }
        catch (IOException e) {
            TextAreaAndProgressBar.addText("Woops, internal error occurred, please try again...");
            e.printStackTrace();
        }
        return null;
    }
}
