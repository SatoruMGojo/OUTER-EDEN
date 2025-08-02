package src.ui;

import src.modelo.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class VentanaPrincipal extends JFrame {

    private Usuario usuario; // el usuario que inició sesión

    public VentanaPrincipal(Usuario usuario) {
        this.usuario = usuario;
        setTitle("Outer Eden - Inicio");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel lblBienvenida = new JLabel("¡Bienvenido a Outer Eden, " + usuario.getNombre() + "!", SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblBienvenida, BorderLayout.NORTH);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(4, 1, 10, 10));

        JButton btnCrearPost = new JButton("Crear Post");
        JButton btnVerFeed = new JButton("Ver Feed");
        JButton btnVerPerfil = new JButton("Ver Perfil");
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");

        // Acciones
        btnCrearPost.addActionListener((ActionEvent e) -> {
            CrearPostVentana crearPostVentana = new CrearPostVentana(usuario);
            crearPostVentana.setVisible(true);
        });

        btnVerFeed.addActionListener((ActionEvent e) -> {
            FeedVentana feedVentana = new FeedVentana(usuario);
            feedVentana.setVisible(true);
        });

        btnVerPerfil.addActionListener((ActionEvent e) -> {
                                                        //el que se muestra / el que esta viendo
            PerfilVentana perfilVentana = new PerfilVentana(usuario, usuario);
            perfilVentana.setVisible(true);
        });

        btnCerrarSesion.addActionListener((ActionEvent e) -> {
            dispose();
            JOptionPane.showMessageDialog(null, "Sesión cerrada.");
            new LoginForm().setVisible(true); // regresar a login
        });

        panelBotones.add(btnCrearPost);
        panelBotones.add(btnVerFeed);
        panelBotones.add(btnVerPerfil);
        panelBotones.add(btnCerrarSesion);

        add(panelBotones, BorderLayout.CENTER);
    }
}
