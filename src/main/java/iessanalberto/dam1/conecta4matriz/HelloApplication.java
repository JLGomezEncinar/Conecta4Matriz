package iessanalberto.dam1.conecta4matriz;

import iessanalberto.dam1.conecta4matriz.navigation.Navigation;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
//Name: Conecta 4.
//Author: José Luis Gómez.
//Date: 9-5-2025.
/*Description: Juego de Conecta 4, al pulsar los botones de arriba se coloca la ficha
dependiendo del turno del jugador. */
public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Navigation.navigate("MainScreen");
    }

    public static void main(String[] args) {
        launch();
    }
}