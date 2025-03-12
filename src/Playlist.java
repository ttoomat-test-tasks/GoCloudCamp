import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Класс-хранилище плейлиста с музыкой и полями.
 */
public class Playlist /*extends Thread */{
    private LinkedList<Track> tracks = new LinkedList<>();
    private int playind = 0;

    public void setPlayind(int lastid) {
        this.playind = lastid;
    }

    public int size() {
        return tracks.size();
    }

    public void Play() {
        //for (int i = playind; i < tracks.size(); i++) {
        int i = playind;
            if (tracks.get(i).getInstance().isRunning()) {
                tracks.get(i).getInstance().close();
            }
            tracks.get(i).start();
        //}
    }

    /**
     * Возможно, у трека должен быть экземпляр типа Clip или в том роде
     * а то Track чисто хранит инфу о треке (path и duration), но не сам трек.
     * Не хотелось бы несколько раз открывать и закрывать поток только чтоб узнать duration...
     * ? правильно ли, что мы здесь открываем instance в потоке?
     */
    static class Track extends Thread{
        private String path;
        private long duration;
        private Clip instance;

        public Track(String path_name) {
            path = path_name;
            try {
                File f = new File(path);
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(f.toURI().toURL());
                instance = AudioSystem.getClip();
                instance.open(audioInputStream);
                setDuration(instance.getMicrosecondLength()/1000);
            } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
                System.err.println(e.getMessage());
            }

        }

        @Override
        public void run() {
            try {
                instance.start();
                Track.sleep(duration);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }

        public Clip getInstance() {
            return instance;
        }

        public String getPath() {
            return path;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }

        public long getDuration() {
            return duration;
        }
    }

    public Playlist() {

    }

    /**
     * Чтобы не замарачиваться с добавлением файлов, а быстро проверить работу проги.
     * @param debugmode true, false без разницы
     */
    public Playlist(boolean debugmode) {
        Track track1 = new Track("DVRST - Close Eyes.wav");
        tracks.add(track1);
    }

    /**
     * Функция, проигрывающая только данный трек
     */
    public void play(int number, boolean a) {
        try {
            tracks.get(number).getInstance().start();
            Thread.sleep(tracks.get(number).getDuration());
        }
        catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }


    /*
    Закрываем все потоки, а чё
     */
    public void pause() {
        for (Track track : tracks) {
            track.instance.close();
        }
    }
    public void pause(int ind) {
        tracks.get(ind).instance.close();
    }

    /**
    @TODO сделать промежуток между играемыми подряд файлами
     */
    public void play(int number) {
        try {
            for (int i = number; i < tracks.size(); i += 1) {

                //tr.start();
                Track t = tracks.get(i);
                t.getInstance().start();
                Thread.sleep(t.getDuration());
            }
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    public void playAll() {
        for (int i = 0; i < tracks.size(); i += 1) {
            play(i);
        }
    }

    public void add(Track track) {
        tracks.add(track);
    }

    public void add(String path_name) {
        tracks.add(new Track(path_name));
    }

    public void delete_by_id(int id) {
        tracks.remove(id);
    }
}
