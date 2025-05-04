module iessanalberto.dam1.conecta4matriz {
    requires javafx.controls;
    requires javafx.fxml;


    opens iessanalberto.dam1.conecta4matriz to javafx.fxml;
    exports iessanalberto.dam1.conecta4matriz;
}