package src.ui;

import src.dao.UsuarioDAO;
import src.modelo.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistroForm extends JFrame {
    private JTextField txtNombre;
    private JTextField txtCorreo;
    private JPasswordField txtContrasena;
    private JButton btnRegistrar;

    public RegistroForm() {
        setTitle("Registro de Usuario");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(4, 2));

        panel.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panel.add(txtNombre);

        panel.add(new JLabel("Correo:"));
        txtCorreo = new JTextField();
        panel.add(txtCorreo);

        panel.add(new JLabel("Contraseña:"));
        txtContrasena = new JPasswordField();
        panel.add(txtContrasena);

        btnRegistrar = new JButton("Registrarse");
        panel.add(new JLabel()); // celda vacía
        panel.add(btnRegistrar);

        add(panel);

        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = txtNombre.getText();
                String correo = txtCorreo.getText();
                String contrasena = new String(txtContrasena.getPassword());

                if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
                    JOptionPane.showMessageDialog(RegistroForm.this, "Todos los campos son obligatorios.");
                    return;
                }

                Usuario nuevoUsuario = new Usuario();
                nuevoUsuario.setNombre(nombre);
                nuevoUsuario.setCorreo(correo);
                nuevoUsuario.setContrasena(contrasena);
                nuevoUsuario.setFotoPerfil("https://yt3.googleusercontent.com/P1HN1TEmdSyQa_sxDt1Az-o2oUVz-N3ivnykzw2Ah-alUHKXt5FeUs75Y4B1aKz4wRBD848XpA=s900-c-k-c0x00ffffff-no-rj");

                boolean exito = UsuarioDAO.insertarUsuario(nuevoUsuario);
                if (exito) {
                    JOptionPane.showMessageDialog(RegistroForm.this, "Usuario registrado correctamente.");
                    dispose(); // Cierra el formulario
                } else {
                    JOptionPane.showMessageDialog(RegistroForm.this, "Error al registrar usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RegistroForm().setVisible(true);
        });
    }
}
