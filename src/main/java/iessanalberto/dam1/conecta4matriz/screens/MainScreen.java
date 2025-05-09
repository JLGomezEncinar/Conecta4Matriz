package iessanalberto.dam1.conecta4matriz.screens;
import iessanalberto.dam1.conecta4matriz.models.Jugador;
import iessanalberto.dam1.conecta4matriz.navigation.Navigation;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Optional;

public class MainScreen {
    //Elementos del layout.
    private VBox root = new VBox();
    private HBox fila1 = new HBox();
    private HBox fila2 = new HBox();
    private Label lblJugador1 = new Label("Jugador 1");
    private Label lblJugador2 = new Label("Jugador 2");
    GridPane gridPane = new GridPane();

    private Button[][] casillas = new Button [6][7];
    //Variables que llevan el número de partidas ganadas por jugador y los empates.
    private int ganadasJugador1 = 0;
    private int empates = 0;
    private int ganadasJugador2 = 0;
    private Label lblVictoria1 = new Label("Victorias Jugador 1: \n" + ganadasJugador1);
    private Label lblVictoria2 = new Label("Victorias Jugador 2: \n" + ganadasJugador2);
    private Label lblEmpates = new Label("Partidas empatadas: \n" + empates);
    //Creamos los dos jugadores, cada uno con su color.
    private Jugador jugador1 = new Jugador("-fx-background-color: red;",true);
    private Jugador jugador2 = new Jugador("-fx-background-color: yellow;",false);
    //Variable que lleva los turnos (hasta el turno 7 como mínimo no puede haber ganador).
    private int turno = 1;


    public MainScreen() {
        //Creamos las casillas donde irán las fichas y las añadimos al GridPane.
        for (int fila = 0; fila <6; fila++){
            for (int columna = 0; columna<7;columna++){
                final int finalFila = fila;
                final int finalColumna = columna;
                Button casilla = new Button();
                casilla.setShape(new Circle(30));
                casilla.setMinSize(60,60);
                casilla.setMaxSize(60,60);
                casillas[finalFila][finalColumna] =casilla;
                casilla.setStyle("-fx-background-color: white;");


                gridPane.add(casilla,finalColumna,finalFila+1);
            }
        }
        //Creamos los botones que al pulsarlos colocarán las fichas.
        for (int botones = 0; botones < 7; botones++) {
            Button boton = new Button("+");
            boton.setUserData(botones);
            boton.setOnAction(event ->{
                //Al pulsar el botón guardamos los botones que están debajo en una ArrayList.
                ArrayList <Button> btnfilas = new ArrayList<>();
                int columna = Integer.parseInt(boton.getUserData().toString());
                for (int fila = 5; fila >=0; fila--) {
                    btnfilas.add(casillas[fila][columna]);
                }
                //Filtramos para encontrar el primero que está sin ficha.
                Optional<Button> btnVacio = btnfilas.stream().filter(b ->b.getStyle().contains("-fx-background-color: white;")).findFirst();
                        btnVacio.ifPresent(b -> {
                            //Ponemos la ficha del color correspondiente al jugador actual.
                                    b.setStyle(jugador1.isTurno() ? jugador1.getColor() : jugador2.getColor());
                                    int filaReal = -1;
                                    int i = 0;
                                    while (i < 6 && filaReal == -1) {
                                        if (casillas[i][columna] == b) {
                                            filaReal = i;
                                        }
                                        i++;
                                    }

                                    //Si se cumplen las condiciones de victoria muestra el mensaje de ganador.
           //Si la filaReal está en -1 es que hemos intentado poner ficha en una columna que está llena y no permite seguir hasta hacer un movimiento correcto.
                                    if (turno >= 7 && filaReal != -1) {
                                        if (hay4EnLinea(filaReal, columna, 1, 0, casillas) ||
                                                hay4EnLinea(filaReal, columna, 0, 1, casillas) ||
                                                hay4EnLinea(filaReal, columna, 1, 1, casillas) ||
                                                hay4EnLinea(filaReal, columna, 1, -1, casillas)) {

                                            mostrarAlerta(jugador1.isTurno() ? "Ha ganado el jugador 1" : "Ha ganado el jugador 2");
// El return es para evitar que nos salte la función de empate al reiniciar el tablero.
                                            return;
                                        }
                                    }
//Si el tablero no está lleno cambiamos turno y si está lleno es empate.
                            if(!tableroLleno()) {
                                jugador1.setTurno(!jugador1.isTurno());
                                jugador2.setTurno(!jugador2.isTurno());
                                if (jugador1.isTurno()) {
                                    lblJugador1.setStyle("-fx-background-color: red");
                                    lblJugador2.setStyle("");
                                } else {
                                    lblJugador2.setStyle("-fx-background-color: yellow");
                                    lblJugador1.setStyle("");
                                }
                                turno++;
                            } else {
                                mostrarAlerta("Empate");
                            }
                                });

            });
            gridPane.add(boton,botones,0);
        }
        //Añadimos elementos al layout.
        fila1.getChildren().addAll(lblJugador1,lblJugador2);
        fila2.getChildren().addAll(lblVictoria1, lblEmpates,lblVictoria2);
        root.getChildren().addAll(fila1,gridPane,fila2);
        configurarLayout();
    }
    //Función que muestra la alerta de fin de partida.
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Partida finalizada");
        alert.setHeaderText(mensaje);
        alert.setContentText("¿Quieres volver a jugar?");
        Optional<ButtonType> resultado = alert.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
        //Si aceptamos reinicia el tablero.
        reiniciarTablero(mensaje);
        //Si no aceptamos cierra la aplicación
        } else {
            Platform.exit();
        }
    }

    //Función que comprueba si hay cuatro o más en línea
    private boolean hay4EnLinea(int fila, int columna, int dx, int dy, Button[][] casillas) {
        String colorCasilla = casillas[fila][columna].getStyle();



        int contador = 1;

        // A partir de la ficha que hemos colocado busca si hay fichas de ese color.
        int nuevaFila = fila + dx;
        int nuevaColumna = columna + dy;

        while (nuevaFila >= 0 && nuevaFila < casillas.length &&
                nuevaColumna >= 0 && nuevaColumna < casillas[0].length &&
                colorCasilla.equals(casillas[nuevaFila][nuevaColumna].getStyle())) {
            contador++;
            nuevaFila += dx;
            nuevaColumna += dy;
        }

        // Después busca en la dirección contraria.
        nuevaFila = fila - dx;
        nuevaColumna = columna - dy;

        while (nuevaFila >= 0 && nuevaFila < casillas.length &&
                nuevaColumna >= 0 && nuevaColumna < casillas[0].length &&
                colorCasilla.equals(casillas[nuevaFila][nuevaColumna].getStyle())) {
            contador++;
            nuevaFila -= dx;
            nuevaColumna -= dy;
        }
        //Si la condición de retorno es true, hay cuatro o más en línea.
        return contador >= 4;
    }






    //Función que configura el layout.
    private void configurarLayout() {
        lblJugador1.setStyle("-fx-background-color: red");
        root.setSpacing(10);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10));
        fila1.setSpacing(50);
        fila2.setSpacing(50);
        gridPane.setStyle("-fx-background-color: blue;");
    }

    public VBox getRoot() {
        return root;
    }
    //Función que reinicia el tablero y suma victoria al ganador o empate.
    public void reiniciarTablero(String mensaje){
        for (int fila = 0; fila <6; fila++) {
            for (int columna = 0; columna < 7; columna++) {
                casillas[fila][columna].setStyle("-fx-background-color: white;");
            }
        }
        if (!mensaje.startsWith("Empate")) {
            if (jugador1.isTurno()) {
                ganadasJugador1++;
                lblVictoria1.setText("Victorias Jugador 1: \n" + ganadasJugador1);
            } else {
                ganadasJugador2++;
                lblVictoria2.setText("Victorias Jugador 2: \n" + ganadasJugador2);
            }
        } else {
            empates++;
            lblEmpates.setText("Partidas empatadas: \n"+empates);
        }
        jugador1.setTurno(true);
        lblJugador1.setStyle("-fx-background-color:red;");
        jugador2.setTurno(false);
        lblJugador2.setStyle("");
    }
    //Función que comprueba si todas las celdas están llenas
    private boolean tableroLleno() {
        boolean ocupado = true;
        for (int fila = 0; fila < 6; fila++){
            for (int columna = 0; columna < 7; columna++){
                if (casillas[fila][columna].getStyle().contains("-fx-background-color: white;")) {
                    ocupado = false;
                }
            }
        }
        return ocupado;
    }
}

