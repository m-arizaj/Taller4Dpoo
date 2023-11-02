package interfaz;
import javax.swing.*;
import java.awt.*;

public class Casilla extends JButton {
    private int fila;
    private int columna;
    private boolean encendida;

    public Casilla(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
        setPreferredSize(new Dimension(50, 50)); // Ajusta el tamaño de la casilla según tus necesidades
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public boolean isEncendida() {
        return encendida;
    }

    public void setEncendida(boolean encendida) {
        this.encendida = encendida;
        setBackground(encendida ? Color.YELLOW : Color.GRAY); // Configura el color según el estado
    }
}
