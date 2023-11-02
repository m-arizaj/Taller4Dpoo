package interfaz;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import uniandes.dpoo.taller4.modelo.*;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.JComboBox;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;



// ¿Como funciona el juego?
// Primero se debe seleccionar el tamaño de la cuadricula del tablero (su valor predeterminado es de 5x5)
// Segundo se debe escoger el nivel de dificultad 
// Luego se puede oprimir inicio (Si no se hace el anterior paso al menos, inicio no funciona)
// En inicio pide el nombre e inicia el juego, de lo contrario si no se pone no se inicia.
// El boton reiniciar comienza desde cero el juego y toca volver a ingresar el nombre en inicio, la dificultad ya queda seleccionada
// El boton top 10 siempre funciona incluso sin jugar y se actualiza cada vez que se acaba una partida y un puntaje entra en el top 10
// Finalmente en el panel inferior se muestran las jugadas y el jugador
// Se usaron las clases proporcionadas y no se editaron




public class LightsOutUI extends JFrame {
    private Tablero tablero;
    private JPanel tableroPanel;
    private JButton reiniciarButton;
    private JButton iniciarButton;
    private JButton top10Button;
    private JList<RegistroTop10> top10List;
    private JComboBox<String> tamanoTableroComboBox;
    private int dificultad;
    private JLabel jugadasLabel;
    private JLabel jugadorLabel;
    private JPanel bottomPanel;
    boolean bandera;
    boolean juegoCurso;
    File archivo = new File("data/top10.csv");
    String nombre;

    public LightsOutUI() {
        setTitle("Lights Out Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        tablero = new Tablero(5);
        tableroPanel = new JPanel();
        tableroPanel.setLayout(new GridLayout(tablero.darTablero().length, tablero.darTablero().length));
        crearCasillas();
        reiniciarButton = new JButton("Reiniciar");
        iniciarButton = new JButton("Iniciar");
        top10Button = new JButton("Top 10");
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
        

        reiniciarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String seleccion = (String) tamanoTableroComboBox.getSelectedItem();
                int tamano = Integer.parseInt(seleccion.split("x")[0]);
                tablero = new Tablero(tamano);
                jugadasLabel.setText("Jugadas: " + tablero.darJugadas());
                jugadorLabel.setText("Jugador: -" );
                bottomPanel.revalidate();
                bottomPanel.repaint();
                bandera = false;
                actualizarTablero();
            }
        });
        nombre="-";
        iniciarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	 nombre = JOptionPane.showInputDialog("Por favor, ingresa tu nombre:");

                 if (nombre != null && !nombre.isEmpty()) {
                     JOptionPane.showMessageDialog(null, "Hola, " + nombre + "! El juego comenzará ahora.");
                     jugadorLabel.setText("Jugador: " + nombre);
                     bottomPanel.revalidate();
                     bottomPanel.repaint();
                     tablero.desordenar(dificultad);
                     actualizarTablero();
                     bandera= true;
                     juegoCurso= true;
                 } else {
                     JOptionPane.showMessageDialog(null, "No ingresaste un nombre. El juego no se iniciará.");
                 }
       
            }
        });

        top10Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarTop10();
            }
        });
        //Dropdown tamaños
        String[] tamanos = {"5x5", "6x6", "7x7", "8x8"};
        tamanoTableroComboBox = new JComboBox<>(tamanos);
        tamanoTableroComboBox.setSelectedItem("5x5");
        tamanoTableroComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String seleccion = (String) tamanoTableroComboBox.getSelectedItem();
                int tamano = Integer.parseInt(seleccion.split("x")[0]);
                tablero = new Tablero(tamano);
                bandera = false;
                actualizarTablero();
                revalidate();
                repaint();
            }
        });
        //Dificultades 
        JLabel dificultadLabel = new JLabel("Dificultad:");
        ButtonGroup dificultadGroup = new ButtonGroup();
        JRadioButton facilButton = new JRadioButton("Fácil");
        facilButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dificultad = 5;
            }
        });
        JRadioButton medioButton = new JRadioButton("Medio");
        medioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dificultad = 10;
            }
        });
        JRadioButton dificilButton = new JRadioButton("Difícil");
        dificilButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dificultad = 20;
            }
        });

        dificultadGroup.add(facilButton);
        dificultadGroup.add(medioButton);
        dificultadGroup.add(dificilButton);
        
        // Jugadas
        jugadasLabel = new JLabel("Jugadas: 0");
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        //Jugador
        jugadorLabel = new JLabel("Jugador: -");
        
        
        // Top panel
        topPanel.add(tamanoTableroComboBox);
        topPanel.add(dificultadLabel);
        topPanel.add(facilButton);
        topPanel.add(medioButton);
        topPanel.add(dificilButton);
        // Right panel
        iniciarButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, iniciarButton.getMaximumSize().height));
        reiniciarButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, reiniciarButton.getMaximumSize().height));
        top10Button.setMaximumSize(new Dimension(Integer.MAX_VALUE, top10Button.getMaximumSize().height));
        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(iniciarButton);
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(reiniciarButton);
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(top10Button);
        rightPanel.add(Box.createVerticalGlue());
        // Bottom panel
        bottomPanel.add(jugadasLabel);
        bottomPanel.add(jugadorLabel);
        

        // Crea una lista para el Top 10 y configura su modelo
        top10List = new JList<>();
        DefaultListModel<RegistroTop10> listModel = new DefaultListModel<>();
        top10List.setModel(listModel);

        // Agrega todos los componentes a la ventana
        add(tableroPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);
        add(rightPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void crearCasillas() {
        boolean[][] tableroActual = tablero.darTablero();
        tableroPanel.removeAll();

        for (int fila = 0; fila < tableroActual.length; fila++) {
            for (int columna = 0; columna < tableroActual[fila].length; columna++) {
                Casilla casilla = new Casilla(fila, columna);
                casilla.setEncendida(tableroActual[fila][columna]);
                casilla.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int fila = casilla.getFila();
                        int columna = casilla.getColumna();
                        tablero.jugar(fila, columna);
                        actualizarTablero();
                        jugadasLabel.setText("Jugadas: " + tablero.darJugadas());
                        bottomPanel.revalidate();
                        bottomPanel.repaint();
                    }
                });
                tableroPanel.add(casilla);
            }
        }
        tableroPanel.revalidate();
        tableroPanel.repaint();
    }

    private void actualizarTablero() {
        crearCasillas();
        tableroPanel.setLayout(new GridLayout(tablero.darTablero().length, tablero.darTablero().length));
        try {
            if (tablero.tableroIluminado()&& (bandera==true) && (juegoCurso==true)) {
                int puntaje = tablero.calcularPuntaje();
                JOptionPane.showMessageDialog(null,  "¡Felicidades! ha ganado el juego.");
                RegistroTop10 jugador = new RegistroTop10(nombre, puntaje);
                Top10 top10 = new Top10();
                top10.cargarRecords(archivo);
                if (top10.esTop10(puntaje)==true) {
                    top10.agregarRegistro(nombre, puntaje);
                    top10.salvarRecords(archivo); 
                }
                jugadorLabel.setText("Jugador: -");
                jugadasLabel.setText("Jugadas: 0");
                bottomPanel.revalidate();
                bottomPanel.repaint();
                String seleccion = (String) tamanoTableroComboBox.getSelectedItem();
                int tamano = Integer.parseInt(seleccion.split("x")[0]);
                tablero = new Tablero(tamano);
                bandera= false;
                juegoCurso= false;
                
            }
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        }
        
    }

    private void mostrarTop10() {
        JFrame top10Frame = new JFrame("Top 10");
        top10Frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        top10Frame.setSize(400, 300);

        JTable top10Table = new JTable();
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Jugador"); 
        model.addColumn("Puntaje"); 
        top10Table.setModel(model);

        Top10 top10 = new Top10();
        top10.cargarRecords(archivo);
        for (RegistroTop10 registro : top10.darRegistros()) {
            model.addRow(new Object[]{registro.darNombre(), registro.darPuntos()});
        }
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setForeground(Color.BLUE); 
        top10Table.getColumnModel().getColumn(1).setCellRenderer(renderer);
        
        JTableHeader header = top10Table.getTableHeader();
        header.setBackground(Color.GREEN);

        JScrollPane scrollPane = new JScrollPane(top10Table);
        top10Frame.add(scrollPane);

        top10Frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	FlatLightLaf.install();
                LightsOutUI lightsOutUI = new LightsOutUI();
                lightsOutUI.setVisible(true);
            }
        });
    }
}
