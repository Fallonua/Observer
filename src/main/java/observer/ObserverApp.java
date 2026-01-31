package observer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Paths;

public class ObserverApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        TimeServer timeServer = new TimeServer();
        ComponentOne componentOne = new ComponentOne(timeServer);

        // Проверка наличия файла с музыкой
        String mediaPath = Paths.get("src/main/resources/vivo.mp3").toAbsolutePath().toString();
        try {
            if (!java.nio.file.Files.exists(Paths.get(mediaPath))) {
                mediaPath = "";
            }
        } catch (Exception ignored) {
            mediaPath = "";
        }
        // Если файл не найден, то музыка не будет проигрываться
        ComponentTwo componentTwo = new ComponentTwo(timeServer, mediaPath);
        ComponentThree componentThree = new ComponentThree(timeServer);

        timeServer.attach(componentOne);
        timeServer.attach(componentTwo);
        timeServer.attach(componentThree);

        // Загрузка интерфейса из FXML файла
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
        Parent root = loader.load();

        // Инициализация контроллера
        MainController controller = loader.getController();
        controller.init(timeServer, componentOne, componentTwo, componentThree);

        // Открытие окна приложения
        Scene scene = new Scene(root, 700, 450);
        stage.setTitle("Observer TimeServer");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
