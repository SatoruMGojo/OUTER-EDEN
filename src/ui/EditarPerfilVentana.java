package src.ui;

import src.dao.UsuarioDAO;
import src.modelo.Usuario;

import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;

public class EditarPerfilVentana extends JFrame {

    private Usuario usuario;
    private JTextField txtNombre, txtCorreo, txtFotoPerfil;
    private JTextArea txtBiografia;

    public EditarPerfilVentana(Usuario usuario) {
        this.usuario = usuario;

        setTitle("Editar Perfil");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nombre
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Nombre:"), gbc);
        txtNombre = new JTextField(usuario.getNombre(), 20);
        gbc.gridx = 1;
        add(txtNombre, gbc);

        // Correo
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Correo:"), gbc);
        txtCorreo = new JTextField(usuario.getCorreo(), 20);
        gbc.gridx = 1;
        add(txtCorreo, gbc);

        // Foto de perfil
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Foto de perfil (URL):"), gbc);
        txtFotoPerfil = new JTextField(usuario.getFotoPerfil() != null ? usuario.getFotoPerfil() : "", 20);
        gbc.gridx = 1;
        add(txtFotoPerfil, gbc);

        // Biografía
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        add(new JLabel("Biografía:"), gbc);

        txtBiografia = new JTextArea(usuario.getBiografia() != null ? usuario.getBiografia() : "", 5, 20);
        txtBiografia.setLineWrap(true);
        txtBiografia.setWrapStyleWord(true);
        JScrollPane scrollBiografia = new JScrollPane(txtBiografia);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(scrollBiografia, gbc);

        // Botón pa guardar
        JButton btnGuardar = new JButton("Guardar");
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(btnGuardar, gbc);

        btnGuardar.addActionListener(e -> guardarCambios());

        setVisible(true);
    }

    private void guardarCambios() {
        String nombre = txtNombre.getText().trim();
        String correo = txtCorreo.getText().trim();
        String foto = txtFotoPerfil.getText().trim();
        String biografia = txtBiografia.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.");
            return;
        }

        if (!validarCorreo(correo)) {
            JOptionPane.showMessageDialog(this, "El correo no es válido.");
            return;
        }

        if (!foto.isEmpty() && !validarURL(foto)) {
            JOptionPane.showMessageDialog(this, "La URL de la foto no es válida.");
            return;
        }

        // Para actualizar usuario y guardar en la BD
        usuario.setNombre(nombre);
        usuario.setCorreo(correo);
        usuario.setFotoPerfil(foto);
        usuario.setBiografia(biografia);

        boolean exito = UsuarioDAO.actualizarUsuario(usuario);
        if (exito) {
            JOptionPane.showMessageDialog(this, "Perfil actualizado correctamente.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar perfil.");
        }
    }

    private boolean validarCorreo(String correo) {
        // Regex para checar el correo
        String regex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(regex, correo);
    }

    private boolean validarURL(String url) {
        try {
            new java.net.URL(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
