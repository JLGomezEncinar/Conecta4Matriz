package iessanalberto.dam1.conecta4matriz.screens;
import iessanalberto.dam1.conecta4matriz.models.Jugador;
import iessanalberto.dam1.conecta4matriz.navigation.Navigation;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Optional;

public class MainScreen {
    private HBox root = new HBox();
    GridPane gridPane = new GridPane();

    private Button[][] casillas = new Button [6][7];

    private Jugador jugador1 = new Jugador("-fx-background-color: red;",true);
    private Jugador jugador2 = new Jugador("-fx-background-color: yellow;",false);
    private int turno = 1;


    public MainScreen() {

        for (int fila = 0; fila <6; fila++){
            for (int columna = 0; columna<7;columna++){
                final int finalFila = fila;
                final int finalColumna = columna;
                Button casilla = new Button();
                casilla.setShape(new Circle(30));
                casilla.setMinSize(60,60);
                casilla.setMaxSize(60,60);
                casillas[finalFila][finalColumna] =casilla;

                casilla.setDisable(true);


                gridPane.add(casilla,finalColumna,finalFila+1);
            }
        }
        for (int botones = 0; botones < 7; botones++) {
            Button boton = new Button("+");
            boton.setUserData(botones);
            boton.setOnAction(event ->{
                ArrayList <Button> btnfilas = new ArrayList<>();
                int columna = Integer.parseInt(boton.getUserData().toString());
                for (int fila = 5; fila >=0; fila--) {
                    btnfilas.add(casillas[fila][columna]);
                }

                Optional<Button> btnVacio = btnfilas.stream().filter(b ->b.getStyle().isEmpty()).findFirst();
                        btnVacio.ifPresent(b -> {
                                    b.setStyle(jugador1.isTurno() ? jugador1.getColor() : jugador2.getColor());
                                    int filaReal = -1;
                                    int i = 0;
                                    while (i < 6 && filaReal == -1) {
                                        if (casillas[i][columna] == b) {
                                            filaReal = i;
                                        }
                                        i++;
                                    }


                                    if (turno >= 7 && filaReal != -1) {
                                        if (hay4EnLinea(filaReal, columna, 1, 0, casillas) ||
                                                hay4EnLinea(filaReal, columna, 0, 1, casillas) ||
                                                hay4EnLinea(filaReal, columna, 1, 1, casillas) ||
                                                hay4EnLinea(filaReal, columna, 1, -1, casillas)) {

                                            mostrarAlerta(jugador1.isTurno() ? "Ha ganado el jugador 1" : "Ha ganado el jugador 2");
                                        }
                                    }
                                });
                jugador1.setTurno(!jugador1.isTurno());
                jugador2.setTurno(!jugador2.isTurno());
                turno++;
            });
            gridPane.add(boton,botones,0);
        }
        root.getChildren().addAll(gridPane);
        configurarLayout();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Partida finalizada");
        alert.setHeaderText(mensaje);
        alert.setContentText("¿Quieres volver a jugar?");
        Optional<ButtonType> resultado = alert.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            Navigation.navigate("MainScreen");
            //Si no aceptamos cierra la aplicación
        } else {
            Platform.exit();
        }
    }


    private boolean hay4EnLinea(int fila, int columna, int dx, int dy, Button[][] casillas) {
        String colorCasilla = casillas[fila][columna].getStyle();

        if (colorCasilla.isEmpty()) {
            return false;
        }

        int contador = 1; // Incluye la casilla original

        // Buscar en dirección positiva (dx, dy)
        int nuevaFila = fila + dx;
        int nuevaColumna = columna + dy;

        while (nuevaFila >= 0 && nuevaFila < casillas.length &&
                nuevaColumna >= 0 && nuevaColumna < casillas[0].length &&
                colorCasilla.equals(casillas[nuevaFila][nuevaColumna].getStyle())) {
            contador++;
            nuevaFila += dx;
            nuevaColumna += dy;
        }

        // Buscar en dirección contraria (-dx, -dy)
        nuevaFila = fila - dx;
        nuevaColumna = columna - dy;

        while (nuevaFila >= 0 && nuevaFila < casillas.length &&
                nuevaColumna >= 0 && nuevaColumna < casillas[0].length &&
                colorCasilla.equals(casillas[nuevaFila][nuevaColumna].getStyle())) {
            contador++;
            nuevaFila -= dx;
            nuevaColumna -= dy;
        }

        return contador >= 4;
    }







    private void configurarLayout() {
        root.setSpacing(10);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: blue;");
    }

    public HBox getRoot() {
        return root;
    }
}
