package com.smusic.app.service.musicsource;

//import jBittorrentAPI.Constants;
//import jBittorrentAPI.DownloadManager;
//import jBittorrentAPI.TorrentFile;
//import jBittorrentAPI.TorrentProcessor;
//import jBittorrentAPI.Utils;

/**
 * Created by sergey on 29.04.17.
 */
@Deprecated
//TODO:think about implementation
public class TorrentService {

//    public void download(){
//        Constants.SAVEPATH="/Users/sergey/Downloads/jbitttorrent_tmp";
//
//        String torrentPath="/Users/sergey/Downloads/[new-rutor.org]UltraISO.Premium.Edition.9.6.1.3016.Final.Retail.2.torrent";
//        TorrentProcessor tp = new TorrentProcessor();
//
//        TorrentFile tf = tp.getTorrentFile(tp.parseTorrent(torrentPath));
//
//        DownloadManager dm = new DownloadManager(tf, Utils.generateID());
//
//        // Запуск закачки
//        dm.startListening(6882, 6889);
//        dm.startTrackerUpdate();
////        dm.blockUntilCompletion();
//
//        while(true)
//        {
//            // Если загрузка завершена, то ожидание прерывается
//            if(dm.isComplete())
//            {
//                break;
//            }
//
//            try
//            {
//                Thread.sleep(1000);
//            }
//            catch(InterruptedException ex)
//            {
//                ex.printStackTrace();
//            }
//            dm.stopTrackerUpdate();
//            dm.closeTempFiles();
//
//            String torrentSavedTo = tp.getTorrentFile(tp.parseTorrent(torrentPath)).saveAs;
//        }
//    }
}
