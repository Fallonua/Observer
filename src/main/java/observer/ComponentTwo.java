package observer;

import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.URI;

/**
 * Наблюдатель (Компонент 2): проигрывает музыкальный клип по таймеру.
 * При включении (onComp) ждёт заданное число секунд с текущего момента, затем запускает воспроизведение,
 * через 2 секунды после старта останавливает воспроизведение.
 * Все обращения к MediaPlayer выполняются через Platform.runLater, так как update() вызывается из потока таймера.
 */
public class ComponentTwo extends Observer {

    private int start; // Время сервера, с которого отсчитывается задержка до воспроизведения
    private int count; // Задержка в секундах до запуска воспроизведения

    private boolean state; // Включена ли реакция на тики (включено — ждём start+count и играем)

    private final Media sound; // Звуковой ресурс (может быть null)
    private final MediaPlayer mediaPlayer; // Проигрыватель (может быть null)

    /**
     * Создаёт компонент и пытается загрузить аудио из filePath, из ресурса /vivo.mp3 или из файла vivo.mp3 в текущей директории.
     *
     * @param subject субъект времени (TimeServer)
     * @param filePath путь к MP3-файлу; может быть null или пустым — тогда используется ресурс или vivo.mp3
     */
    public ComponentTwo(Subject subject, String filePath) {
        super(subject);
        this.start = subject.getState();
        this.count = 0;
        this.state = false;

        // Пытаемся загрузить аудио
        URI uri = null;
        try {
            if (filePath != null && !filePath.isEmpty()) {
                File f = new File(filePath);
                if (f.exists()) uri = f.toURI();
            }
            if (uri == null && getClass().getResource("/vivo.mp3") != null) {
                uri = getClass().getResource("/vivo.mp3").toURI();
            }
            if (uri == null) {
                File f = new File("vivo.mp3");
                if (f.exists()) uri = f.toURI();
            }
        } catch (Exception ignored) {
        }

        // Создаём проигрыватель
        if (uri != null) {
            this.sound = new Media(uri.toString());
            this.mediaPlayer = new MediaPlayer(sound);
        } else {
            this.sound = null;
            this.mediaPlayer = null;
        }
    }

    // Включить воспроизведение с текущей задержкой (или 5 с по умолчанию)
    public void start() {
        onComp(count > 0 ? count : 5);
    }

    //Выключить реакцию на таймер и остановить воспроизведение
    public void stop() {
        offComp();
    }

    /**
     * Включить режим «ожидание воспроизведения»: через count секунд от текущего момента сервера — запуск, через ещё 2 с — остановка.
     *
     * @param count задержка в секундах до первого воспроизведения
     */
    public void onComp(int count) {
        this.count = count;
        this.state = true;
        this.start = subject.getState();
    }

    // Выключить режим воспроизведения и остановить плеер
    public void offComp() {
        this.state = false;
        if (mediaPlayer != null) Platform.runLater(mediaPlayer::stop);
    }

    @Override
    public void update(Subject st) {
        if (!state) return;
        int current = st.getState();

        // Наступила секунда запуска — начинаем воспроизведение и сдвигаем следующий старт
        if (current == start + count) {
            start += count;
            if (mediaPlayer != null) Platform.runLater(mediaPlayer::play);
        }
        // Через 2 секунды после старта — останавливаем
        if (current == start + 2 && mediaPlayer != null) {
            Platform.runLater(mediaPlayer::stop);
        }
    }

    /** @return true, если в данный момент идёт воспроизведение */
    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }
}
