package src.ui;

import src.dao.PostDAO;
import src.modelo.Post;
import src.modelo.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CrearPostVentana extends JFrame {

    private JTextArea txtContenido;
    private JButton btnPublicar;
    private Usuario usuario;

    public CrearPostVentana(Usuario usuario) {
        this.usuario = usuario;

        setTitle("Crear Nuevo Post");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtContenido = new JTextArea();
        txtContenido.setLineWrap(true);
        txtContenido.setWrapStyleWord(true);

        btnPublicar = new JButton("Publicar");

        btnPublicar.addActionListener((ActionEvent e) -> {
            String contenido = txtContenido.getText().trim();
            if (contenido.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El contenido no puede estar vacío.");
                return;
            }

            Post nuevoPost = new Post();
            nuevoPost.setContenido(contenido);
            nuevoPost.setIdUsuario(usuario.getId());

            boolean exito = PostDAO.insertarPost(nuevoPost);

            if (exito) {
                JOptionPane.showMessageDialog(this, "¡Post publicado!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al publicar el post.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(new JScrollPane(txtContenido), BorderLayout.CENTER);
        panel.add(btnPublicar, BorderLayout.SOUTH);

        add(panel);
    }
}
