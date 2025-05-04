package iessanalberto.dam1.conecta4matriz.models;

public class Jugador {
    private String color;
    private boolean turno;

    public Jugador(String color, boolean turno) {
        this.color = color;
        this.turno = turno;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isTurno() {
        return turno;
    }

    public void setTurno(boolean turno) {
        this.turno = turno;
    }
}
