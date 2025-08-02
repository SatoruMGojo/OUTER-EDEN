package src.ui;

import src.dao.UsuarioDAO;
import src.modelo.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//desde aqui se prueba el programa
public class LoginForm extends JFrame {
    private JTextField txtCorreo;
    private JPasswordField txtContrasena;
    private JButton btnLogin;
    private JButton btnRegistrar;


    public LoginForm() {
        setTitle("Inicio de Sesión");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(4, 2));

        panel.add(new JLabel("Correo:"));
        txtCorreo = new JTextField();
        panel.add(txtCorreo);

        panel.add(new JLabel("Contraseña:"));
        txtContrasena = new JPasswordField();
        panel.add(txtContrasena);

        btnLogin = new JButton("Iniciar sesión");
        panel.add(new JLabel()); // celda vacía
        panel.add(btnLogin);

        btnRegistrar = new JButton("Registrarse");
        panel.add(new JLabel()); // otra celda vacia para que no se vea feo
        panel.add(btnRegistrar);

        add(panel);


        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String correo = txtCorreo.getText();
                String contrasena = new String(txtContrasena.getPassword());

                Usuario usuario = UsuarioDAO.buscarPorCorreoYContrasena(correo, contrasena);
                if (usuario != null) {
                    JOptionPane.showMessageDialog(LoginForm.this,
                            "¡Bienvenido, " + usuario.getNombre() + "!");
                    dispose();
                    VentanaPrincipal ventanaPrincipal = new VentanaPrincipal(usuario);
                    ventanaPrincipal.setVisible(true);


                } else {
                    JOptionPane.showMessageDialog(LoginForm.this,
                            "Correo o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegistroForm registroForm = new RegistroForm();
                registroForm.setVisible(true);
            }
        });


    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}
