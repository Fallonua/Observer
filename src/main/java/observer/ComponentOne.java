package observer;

import javafx.application.Platform;
import javafx.scene.control.Label;

/**
 * Наблюдатель (Компонент 1): отображает текущее локальное время в виде текста «прошло N с».
 * Обновление надписи выполняется в потоке JavaFX через Platform.runLater, так как
 * уведомления приходят из потока TimeServer.
 */
public class ComponentOne extends Observer {

    // Элемент интерфейса, в котором отображается текст времени
    private Label timeLabel;

    public ComponentOne(Subject subject) {
        super(subject);
    }

    /**
     * Установить метку, которую компонент будет обновлять при каждом тике.
     * Вызывается из GUI после создания панели.
     *
     * @param timeLabel метка для отображения строки «прошло N с»
     */
    public void setTimeLabel(Label timeLabel) {
        this.timeLabel = timeLabel;
    }

    @Override
    // Обновить метку времени
    public void update(Subject subject) {
        int state = subject.getState();
        if (timeLabel != null) {
            Platform.runLater(() -> timeLabel.setText("прошло " + state + " с"));
        }
    }
}
