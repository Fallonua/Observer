package observer;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.animation.TranslateTransition;

/**
 * Наблюдатель (Компонент 3): анимация движения кружка со сменой направления и цвета.
 * Каждые animationPeriod секунд (по умолчанию 20) кружок меняет цвет (зелёный/синий),
 * разворачивается и едет в противоположную сторону. Обновление сцены выполняется через Platform.runLater.
 */
public class ComponentThree extends Observer {

    private Circle circle; // кружок, который будет перезапускать анимацию
    private TranslateTransition transition; // анимация движения кружка

    private int animationPeriod = 20; // период перезапуска анимации в секундах
    private int lastRestartAt = -1; // последний перезапуск анимации (чтобы не срабатывать несколько раз в одну секунду)
    private boolean movingRight = true; // направление движения кружка (true — вправо, false — влево)

    private static final Color COLOR_A = Color.GREEN; // цвет при движении вправо
    private static final Color COLOR_B = Color.BLUE; // цвет при движении влево

    private static final double DISTANCE = 150; // Расстояние перемещения в пикселях (fromX/toX)

    public ComponentThree(Subject subject) {
        super(subject);
    }

    /**
     * Установить кружок, цвет которого будет меняться при перезапуске.
     *
     * @param circle фигура Circle из сцены JavaFX
     */
    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    /**
     * Установить анимацию перемещения, которую компонент будет перезапускать и разворачивать.
     *
     * @param tt TranslateTransition для кружка
     */
    public void setTransition(TranslateTransition tt) {
        this.transition = tt;
    }

    /**
     * Задать период перезапуска анимации в секундах
     *
     * @param seconds период в секундах
     */
    public void setAnimationPeriod(int seconds) {
        this.animationPeriod = seconds;
    }

    @Override
    // Обновление состояния компонента
    public void update(Subject subject) {
        int state = subject.getState();
        // Срабатываем ровно в те секунды, когда state кратен периоду
        if (state > 0 && state % animationPeriod == 0 && state != lastRestartAt) {
            // Обновляем состояние, чтобы не срабатывать повторно в этой секунде
            lastRestartAt = state;
            // Перезапуск анимации и смена цвета
            if (circle != null && transition != null) {
                Platform.runLater(() -> {
                    circle.setFill(movingRight ? COLOR_B : COLOR_A);
                    movingRight = !movingRight;
                    double from = movingRight ? 0 : DISTANCE;
                    double to = movingRight ? DISTANCE : 0;
                    transition.setFromX(from);
                    transition.setToX(to);
                    transition.playFromStart();
                });
            }
        }
    }
}
