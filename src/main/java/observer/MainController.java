package observer;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class MainController {

    @FXML
    private Label serverStatusLabel;
    @FXML
    private Button startServerBtn;
    @FXML
    private Button stopServerBtn;

    @FXML
    private Label timeLabel;

    @FXML
    private Label componentTwoStatusLabel;
    @FXML
    private Spinner<Integer> delaySpinner;
    @FXML
    private Button startClipBtn;
    @FXML
    private Button stopClipBtn;

    @FXML
    private Circle animCircle;
    @FXML
    private Spinner<Integer> periodSpinner;
    @FXML
    private Button startAnimBtn;
    @FXML
    private Button stopAnimBtn;

    private TimeServer timeServer;
    private ComponentOne componentOne;
    private ComponentTwo componentTwo;
    private ComponentThree componentThree;
    private TranslateTransition componentThreeTransition;

    /**
     * Вызывается из ObserverApp после загрузки FXML. Передаёт модель (сервер и наблюдатели)
     * и выполняет привязку: метки к компонентам, анимацию к кругу, спиннеры и обработчики.
     */
    public void init(TimeServer timeServer, ComponentOne componentOne,
                     ComponentTwo componentTwo, ComponentThree componentThree) {
        this.timeServer = timeServer;
        this.componentOne = componentOne;
        this.componentTwo = componentTwo;
        this.componentThree = componentThree;

        // привязка метки к компоненту
        componentOne.setTimeLabel(timeLabel);

        // привязка анимации к кругу
        delaySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 60, 5));
        periodSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 60, 20));
        periodSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            // если значение изменилось, то меняем анимацию
            if (newVal != null && this.componentThree != null) {
                this.componentThree.setAnimationPeriod(newVal);
            }
        });

        // привязка спиннеров и обработчиков
        componentThree.setCircle(animCircle);
        componentThreeTransition = new TranslateTransition(Duration.seconds(20), animCircle);
        componentThreeTransition.setFromX(0);
        componentThreeTransition.setToX(150);
        componentThreeTransition.setCycleCount(1);
        componentThree.setTransition(componentThreeTransition);

        // запуск сервера
        componentTwo.onComp(5);
        componentTwoStatusLabel.setText("Ожидание 5 с...");

        updateServerStatus();
    }

    @FXML
    // обработчик кнопки "Запустить сервер"
    private void onStartServer() {
        timeServer.start();
        updateServerStatus();
        if (componentThreeTransition != null) componentThreeTransition.play();
    }

    @FXML
    // обработчик кнопки "Остановить сервер"
    private void onStopServer() {
        timeServer.stop();
        updateServerStatus();
        if (componentThreeTransition != null) componentThreeTransition.stop();
    }

    // обновляет статус сервера
    private void updateServerStatus() {
        if (serverStatusLabel != null && timeServer != null) {
            serverStatusLabel.setText(timeServer.isActive() ? "Активен" : "Неактивен");
        }
    }

    @FXML
    // обработчик кнопки "Запуск воспроизведения" компонента 2
    private void onStartClip() {
        int count = delaySpinner.getValue();
        componentTwo.onComp(count);
        componentTwoStatusLabel.setText("Ожидание " + count + " с...");
    }

    @FXML
    // обработчик кнопки "Остановить" компонента 2
    private void onStopClip() {
        componentTwo.offComp();
        componentTwoStatusLabel.setText("Не играет");
    }

    @FXML
    // обработчик кнопки "Запуск анимации" компонента 3
    private void onStartAnimation() {
        if (componentThreeTransition != null) componentThreeTransition.play();
    }

    @FXML
    // обработчик кнопки "Остановить анимацию" компонента 3
    private void onStopAnimation() {
        if (componentThreeTransition != null) componentThreeTransition.stop();
    }
}
