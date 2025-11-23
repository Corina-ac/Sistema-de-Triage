package view;

import controller.TriageController;
import model.Paciente;
import model.NivelPrioridad;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URL; 
import javax.imageio.ImageIO; 

public class TriageView extends JPanel {

    private TriageController controlador;
    private JTable tablaColaEspera;
    private DefaultTableModel modeloTabla;
    private JTextField campoDni;
    private JTextField campoNombre;
    private JTextField campoApellido;
    private JTextField campoFechaNac;
    private JTextArea areaSintomas;
    private JLabel etiquetaEstado;
    
    // URL del logo proporcionada por el usuario
    private static final String LOGO_URL = "https://images4.imagebam.com/9d/1d/e3/ME17YGH0_o.png";
    private static final int LOGO_WIDTH = 150; // Tamaño mediano
    private static final int LOGO_HEIGHT = 150;

    private static final String[] NOMBRES_COLUMNAS = {"Prioridad", "DNI", "Nombre", "Sintomas", "Llegada"};

    public TriageView() {
        setLayout(new BorderLayout());
        inicializarComponentes();
        setPreferredSize(new Dimension(1200, 750));
    }

    public void setControlador(TriageController controlador) {
        this.controlador = controlador;
    }

    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // --- Nuevo Panel Superior (Norte) para Logo y Título ---
        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.setBorder(new EmptyBorder(0, 0, 10, 0)); // Espacio abajo del header
        
        // 1. Cargar el Logo
        JLabel logoLabel = crearLogoLabel();
        panelNorte.add(logoLabel, BorderLayout.WEST);
        
        // 2. Etiqueta de Estado (Título Principal)
        etiquetaEstado = new JLabel("Listo. Ingrese un paciente o use las opciones de gestion.");
        etiquetaEstado.setHorizontalAlignment(SwingConstants.CENTER);
        etiquetaEstado.setFont(new Font("SansSerif", Font.BOLD, 14));
        panelNorte.add(etiquetaEstado, BorderLayout.CENTER);
        
        panelPrincipal.add(panelNorte, BorderLayout.NORTH);
        // --------------------------------------------------------

        JPanel panelRegistro = crearPanelRegistro();
        panelPrincipal.add(panelRegistro, BorderLayout.WEST);

        JPanel panelCola = crearPanelCola();
        panelPrincipal.add(panelCola, BorderLayout.CENTER);

        JPanel panelAcciones = crearPanelAcciones(); 
        panelPrincipal.add(panelAcciones, BorderLayout.SOUTH);

        add(panelPrincipal, BorderLayout.CENTER);
    }
    
    private JLabel crearLogoLabel() {
        JLabel label = new JLabel();
        try {
            URL url = new URL(LOGO_URL);
            Image originalImage = ImageIO.read(url);
            
            // Escalar la imagen al tamaño deseado
            Image scaledImage = originalImage.getScaledInstance(LOGO_WIDTH, LOGO_HEIGHT, Image.SCALE_SMOOTH);
            
            label.setIcon(new ImageIcon(scaledImage));
            // Agregamos un borde vacío para separar el logo del título
            label.setBorder(new EmptyBorder(0, 0, 0, 15)); 
        } catch (Exception e) {
            System.err.println("Error al cargar el logo desde la URL: " + e.getMessage());
            label.setText("LOGO ERROR");
            label.setFont(new Font("SansSerif", Font.BOLD, 12));
            label.setPreferredSize(new Dimension(LOGO_WIDTH, LOGO_HEIGHT));
        }
        return label;
    }

    private JPanel crearPanelRegistro() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("1. Registro de Paciente - Uso de TDA"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        campoDni = new JTextField(15);
        campoNombre = new JTextField(15);
        campoApellido = new JTextField(15);
        campoFechaNac = new JTextField(15);
        areaSintomas = new JTextArea(5, 15);
        areaSintomas.setLineWrap(true);
        JScrollPane scrollSintomas = new JScrollPane(areaSintomas);

        int fila = 0;

        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel("DNI:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = fila++;
        panel.add(campoDni, gbc);

        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = fila++;
        panel.add(campoNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel("Apellido:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = fila++;
        panel.add(campoApellido, gbc);

        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel("Fecha Nac. (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        gbc.gridy = fila++;
        panel.add(campoFechaNac, gbc);

        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel("Sintomas:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = fila++;
        gbc.weighty = 1.0;
        panel.add(scrollSintomas, gbc);

        JButton botonRegistrar = new JButton("Registrar y Asignar Prioridad");
        botonRegistrar.setBackground(new Color(60, 179, 113));
        botonRegistrar.setForeground(Color.WHITE);
        botonRegistrar.addActionListener(e -> {
            controlador.manejarRegistroPacienteGUI(
                campoDni.getText(),
                campoNombre.getText(),
                campoApellido.getText(),
                campoFechaNac.getText(),
                areaSintomas.getText()
            );
        });

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        panel.add(botonRegistrar, gbc);

        return panel;
    }

    private JPanel crearPanelCola() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("8. Cola de Atencion - PriorityQueue Manual"));

        modeloTabla = new DefaultTableModel(NOMBRES_COLUMNAS, 0) {
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tablaColaEspera = new JTable(modeloTabla);

        tablaColaEspera.getColumnModel().getColumn(0).setPreferredWidth(100);
        tablaColaEspera.getColumnModel().getColumn(1).setPreferredWidth(80);
        tablaColaEspera.getColumnModel().getColumn(4).setPreferredWidth(120);

        JScrollPane scrollPane = new JScrollPane(tablaColaEspera);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
    
    private JPanel crearPanelAcciones() {
        JPanel panel = new JPanel(new GridBagLayout()); 
        panel.setBorder(BorderFactory.createTitledBorder("Gestion y Demostraciones"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8); 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Fila 1: Botón Atender y Ordenamiento ---
        
        JButton botonAtender = new JButton("10. Atender Siguiente (Dequeue)");
        botonAtender.setBackground(new Color(255, 69, 0));
        botonAtender.setForeground(Color.WHITE);
        botonAtender.addActionListener(e -> controlador.manejarAtenderSiguiente());
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(botonAtender, gbc);

        // BOTÓN DE ORDENAMIENTO POR INTERCAMBIO (ANTES/DESPUÉS)
        JButton btnOrdenarIntercambio = new JButton("7. Ordenar por Intercambio (Joyanes) - Ver Antes/Después");
        btnOrdenarIntercambio.setBackground(new Color(173, 216, 230)); 
        btnOrdenarIntercambio.setForeground(Color.BLACK);
        btnOrdenarIntercambio.addActionListener(e -> {
            if (controlador != null) {
                controlador.manejarOrdenamientoIntercambio();
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(btnOrdenarIntercambio, gbc);

        // --- Fila 2: Botones de Funciones de Gestión ---
        
        JButton botonBusquedaRecursiva = new JButton("6. Busqueda Recursiva Critica");
        botonBusquedaRecursiva.addActionListener(e -> mostrarDialogoBusquedaRecursiva());
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(botonBusquedaRecursiva, gbc);

        JButton botonMemoriaEstatica = new JButton("4. Demostrar Memoria Estatica/Listas");
        botonMemoriaEstatica.addActionListener(e -> controlador.manejarGestionMemoriaEstatica());
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(botonMemoriaEstatica, gbc);


        // --- Fila 3 y 4: Botones de Cambio de Prioridad (Colores) ---
        
        JLabel etiquetaPrioridad = new JLabel("Cambio de Prioridad Manual (DNI):");
        etiquetaPrioridad.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; 
        panel.add(etiquetaPrioridad, gbc);
        
        JPanel panelColores = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        
        crearBotonPrioridad(panelColores, "ROJO (1)", NivelPrioridad.ROJO, new Color(255, 0, 0));
        crearBotonPrioridad(panelColores, "NARANJA (2)", NivelPrioridad.NARANJA, new Color(255, 140, 0));
        crearBotonPrioridad(panelColores, "AMARILLO (3)", NivelPrioridad.AMARILLO, new Color(255, 255, 0));
        crearBotonPrioridad(panelColores, "VERDE (4)", NivelPrioridad.VERDE, new Color(0, 128, 0));
        crearBotonPrioridad(panelColores, "AZUL (5)", NivelPrioridad.AZUL, new Color(0, 0, 255));
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; 
        panel.add(panelColores, gbc);

        return panel;
    }

    private void crearBotonPrioridad(JPanel panelContenedor, String texto, NivelPrioridad prioridad, Color color) {
        JButton boton = new JButton(texto);
        boton.setBackground(color);
        // Ajuste de color de texto para visibilidad
        if (prioridad == NivelPrioridad.ROJO || prioridad == NivelPrioridad.NARANJA || prioridad == NivelPrioridad.AZUL) {
            boton.setForeground(Color.WHITE); 
        } else {
            boton.setForeground(Color.BLACK); 
        }

        boton.addActionListener(e -> {
            Frame padre = (Frame) SwingUtilities.getWindowAncestor(this);
            String dni = JOptionPane.showInputDialog(padre, 
                                                     "Ingrese DNI para cambiar a " + texto + ":", 
                                                     "Cambio de Prioridad", 
                                                     JOptionPane.QUESTION_MESSAGE);
            if (dni != null && !dni.trim().isEmpty()) {
                controlador.manejarCambioPrioridad(dni.trim(), prioridad);
            } else {
                mostrarError("Cambio de prioridad cancelado o DNI inválido.");
            }
        });
        panelContenedor.add(boton);
    }
    
    private void mostrarDialogoBusquedaRecursiva() {
        Frame padre = (Frame) SwingUtilities.getWindowAncestor(this);

        String dni = JOptionPane.showInputDialog(padre, "Ingrese el DNI para la Busqueda Recursiva:");
        if (dni != null && !dni.trim().isEmpty()) {
            controlador.manejarBusquedaRecursiva(dni);
        } else {
            mostrarError("Busqueda cancelada.");
        }
    }

    public void mostrarExito(String mensaje) {
        etiquetaEstado.setText("<html><font color='green'>[EXITO] " + mensaje + "</font></html>");
        // Frame padre = (Frame) SwingUtilities.getWindowAncestor(this);
        // JOptionPane.showMessageDialog(padre, mensaje, "Exito", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String mensaje) {
        etiquetaEstado.setText("<html><font color='red'>[ERROR] " + mensaje + "</font></html>");
        Frame padre = (Frame) SwingUtilities.getWindowAncestor(this);
        JOptionPane.showMessageDialog(padre, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarDetallesPaciente(Paciente paciente, String titulo) {
        if (paciente == null) {
            mostrarError("Paciente no encontrado.");
            return;
        }

        Frame padre = (Frame) SwingUtilities.getWindowAncestor(this);

        String detalles = String.format(
            "<html><h2>%s</h2>" +
            "<b>DNI:</b> %s<br>" +
            "<b>Nombre:</b> %s<br>" +
            "<b>Prioridad:</b> <font color='%s'>%s</font> (Cod: %d)<br>" +
            "<b>Sintomas:</b> %s<br>" +
            "<b>Hora de Llegada:</b> %s<br>" +
            "</html>",
            titulo,
            paciente.getDni(),
            paciente.getNombreCompleto(),
            obtenerColorPrioridad(paciente.getNivelPrioridad()),
            paciente.getNivelPrioridad().getDescripcion(),
            paciente.getNivelPrioridad().getCodigo(),
            paciente.getSintomas(),
            paciente.getHoraLlegada()
        );

        JOptionPane.showMessageDialog(padre, detalles, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarListaPacientes(Paciente[] pacientes, String titulo) {
        Frame padre = (Frame) SwingUtilities.getWindowAncestor(this);

        JTextArea areaTexto = new JTextArea(15, 50);
        areaTexto.setEditable(false);
        areaTexto.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        areaTexto.setText(String.format("--- %s (%d pacientes) ---\n", titulo, pacientes.length));
        areaTexto.append(String.format("%-15s | %-30s | %-10s%n", "PRIORIDAD", "NOMBRE COMPLETO", "DNI"));
        areaTexto.append("----------------------------------------------------------\n");

        if (pacientes.length == 0) {
            areaTexto.append("[INFO] No hay pacientes en esta lista.\n");
        } else {
            for (int i = 0; i < pacientes.length; i++) {
                Paciente p = pacientes[i];
                areaTexto.append(String.format("%-15s | %-30s | %-10s%n",
                    p.getNivelPrioridad().getDescripcion(),
                    p.getNombreCompleto(),
                    p.getDni()));
            }
        }

        JScrollPane scrollPane = new JScrollPane(areaTexto);
        JOptionPane.showMessageDialog(padre, scrollPane, titulo, JOptionPane.PLAIN_MESSAGE);
    }

    public void actualizarColaEspera(Paciente[] pacientesEnEspera) {
        modeloTabla.setRowCount(0);

        for (int i = 0; i < pacientesEnEspera.length; i++) {
            Paciente p = pacientesEnEspera[i];
            Object[] fila = new Object[5];
            fila[0] = p.getNivelPrioridad().getDescripcion();
            fila[1] = p.getDni();
            fila[2] = p.getNombreCompleto();
            String sintomasCortos = p.getSintomas();
            if (sintomasCortos.length() > 30) {
                sintomasCortos = sintomasCortos.substring(0, 30) + "...";
            }
            fila[3] = sintomasCortos;
            fila[4] = p.getHoraLlegada();

            modeloTabla.addRow(fila);
        }
    }

    public void limpiarCamposRegistro() {
        campoDni.setText("");
        campoNombre.setText("");
        campoApellido.setText("");
        campoFechaNac.setText("");
        areaSintomas.setText("");
    }

    private String obtenerColorPrioridad(NivelPrioridad prioridad) {
        if (prioridad == NivelPrioridad.ROJO) {
            return "red";
        }
        if (prioridad == NivelPrioridad.NARANJA) {
            return "orange";
        }
        if (prioridad == NivelPrioridad.AMARILLO) {
            return "gold";
        }
        if (prioridad == NivelPrioridad.VERDE) {
            return "green";
        }
        return "blue";
    }
}