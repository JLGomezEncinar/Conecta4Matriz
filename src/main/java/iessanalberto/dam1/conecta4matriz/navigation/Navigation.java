package iessanalberto.dam1.conecta4matriz.navigation;
import iessanalberto.dam1.conecta4matriz.screens.MainScreen;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Navigation {
    private static final Stage stage = new Stage();

    public static void navigate (String destination) {
        switch (destination) {
            case "MainScreen" -> {
                MainScreen mainScreen = new MainScreen();
                Scene mainScene = new Scene(mainScreen.getRoot(),640,480);
                stage.setTitle("Conecta 4");
                stage.setScene(mainScene);
                stage.show();
            }
        }
    }
}
