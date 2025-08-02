package src.ui;

import src.dao.ComentarioDAO;
import src.dao.PostDAO;
import src.dao.ReaccionDAO;
import src.modelo.Comentario;
import src.modelo.Post;
import src.modelo.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

import static src.dao.UsuarioDAO.obtenerUsuarioPorId;

public class FeedVentana extends JFrame {

    private Usuario usuario;

    public FeedVentana(Usuario usuario) {
        this.usuario = usuario;

        setTitle("Feed de Posts");
        setSize(600, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelPosts = new JPanel();
        panelPosts.setLayout(new BoxLayout(panelPosts, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(panelPosts);
        add(scrollPane, BorderLayout.CENTER);

        List<Post> posts = PostDAO.obtenerTodosLosPosts();

        for (Post post : posts) {
            JPanel panelPost = crearPanelPost(post);
            panelPosts.add(panelPost);
            panelPosts.add(Box.createVerticalStrut(10)); // espacio entre posts
        }

        setVisible(true);
    }

    private JPanel crearPanelPost(Post post) {
        JPanel panelPost = new JPanel(new BorderLayout(10, 10));
        panelPost.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panelPost.setMaximumSize(new Dimension(Integer.MAX_VALUE, 350));

        // Panel superior con foto, autor clickeable y contenido
        JPanel panelAutorContenido = new JPanel(new BorderLayout(10, 0));

        // Foto de perfil y autor del post
        ImageIcon fotoPerfilPost = obtenerImagenDesdeURL(post.getFotoPerfilAutor(), 50, 50);
        JLabel lblFotoPerfilPost = new JLabel(fotoPerfilPost);
        panelAutorContenido.add(lblFotoPerfilPost, BorderLayout.WEST);

        // Nombre del autor clickeable
        String autor = post.getNombreAutor() != null ? post.getNombreAutor() : "Usuario " + post.getIdUsuario();
        JLabel lblNombreAutor = new JLabel("<html><u>" + autor + "</u></html>");
        lblNombreAutor.setForeground(Color.BLUE);
        lblNombreAutor.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblNombreAutor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Usuario autorUsuario = obtenerUsuarioPorId(post.getIdUsuario());
                if (autorUsuario != null) {
                    new PerfilVentana(autorUsuario, usuario);
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró el usuario.");
                }
            }
        });

        // Contenido del postsito
        JTextArea txtContenido = new JTextArea(post.getContenido());
        txtContenido.setLineWrap(true);
        txtContenido.setWrapStyleWord(true);
        txtContenido.setEditable(false);
        txtContenido.setBackground(new Color(245, 245, 245)); // color claro
        txtContenido.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        txtContenido.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel panelTexto = new JPanel();
        panelTexto.setLayout(new BoxLayout(panelTexto, BoxLayout.Y_AXIS));
        panelTexto.add(lblNombreAutor);
        panelTexto.add(Box.createVerticalStrut(5));
        panelTexto.add(txtContenido);

        panelAutorContenido.add(panelTexto, BorderLayout.CENTER);

        panelPost.add(panelAutorContenido, BorderLayout.NORTH);

        // Panel de reacciones
        JPanel panelReacciones = new JPanel();

        JLabel lblLikes = new JLabel();
        JLabel lblDislikes = new JLabel();

        JButton btnLike = new JButton("👍 Like");
        JButton btnDislike = new JButton("👎 Dislike");

        // Detector de la reacción actual
        String reaccionActual = ReaccionDAO.obtenerReaccion(usuario.getId(), post.getId());
        if ("like".equals(reaccionActual)) {
            btnLike.setEnabled(false);
            btnDislike.setEnabled(true);
        } else if ("dislike".equals(reaccionActual)) {
            btnLike.setEnabled(true);
            btnDislike.setEnabled(false);
        } else {
            btnLike.setEnabled(true);
            btnDislike.setEnabled(true);
        }

        // Actualizador actualizable de contadores contables
        actualizarContadores(post, lblLikes, lblDislikes);

        btnLike.addActionListener(e -> {
            btnLike.setEnabled(false);
            btnDislike.setEnabled(false);

            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    String reaccion = ReaccionDAO.obtenerReaccion(usuario.getId(), post.getId());
                    if ("like".equals(reaccion)) {
                        ReaccionDAO.quitarReaccion(usuario.getId(), post.getId());
                    } else {
                        ReaccionDAO.darReaccion(usuario.getId(), post.getId(), "like");
                    }
                    return null;
                }

                @Override
                protected void done() {
                    actualizarContadores(post, lblLikes, lblDislikes);
                    btnLike.setEnabled(true);
                    btnDislike.setEnabled(true);
                }
            };
            worker.execute();
        });

        btnDislike.addActionListener(e -> {
            btnLike.setEnabled(false);
            btnDislike.setEnabled(false);

            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    String reaccion = ReaccionDAO.obtenerReaccion(usuario.getId(), post.getId());
                    if ("dislike".equals(reaccion)) {
                        ReaccionDAO.quitarReaccion(usuario.getId(), post.getId());
                    } else {
                        ReaccionDAO.darReaccion(usuario.getId(), post.getId(), "dislike");
                    }
                    return null;
                }

                @Override
                protected void done() {
                    actualizarContadores(post, lblLikes, lblDislikes);
                    btnLike.setEnabled(true);
                    btnDislike.setEnabled(true);
                }
            };
            worker.execute();
        });

        panelReacciones.add(btnLike);
        panelReacciones.add(lblLikes);
        panelReacciones.add(btnDislike);
        panelReacciones.add(lblDislikes);

        panelPost.add(panelReacciones, BorderLayout.CENTER);

        // Panel de comentarios
        JPanel panelComentarios = new JPanel();
        panelComentarios.setLayout(new BoxLayout(panelComentarios, BoxLayout.Y_AXIS));
        panelComentarios.setBorder(BorderFactory.createTitledBorder("Comentarios"));

        Runnable cargarComentarios = () -> {
            panelComentarios.removeAll();
            List<Comentario> comentarios = ComentarioDAO.obtenerComentariosPorPost(post.getId());
            for (Comentario c : comentarios) {
                panelComentarios.add(crearPanelComentario(c));
                panelComentarios.add(Box.createVerticalStrut(5));
            }
            panelComentarios.revalidate();
            panelComentarios.repaint();
        };

        // Para cargar los comentarios iniciales
        cargarComentarios.run();

        // Espacio para nuevo comentario
        JTextField txtNuevoComentario = new JTextField();
        txtNuevoComentario.setPreferredSize(new Dimension(200, 25));
        JButton btnEnviarComentario = new JButton("Comentar");

        JPanel panelNuevoComentario = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelNuevoComentario.add(txtNuevoComentario);
        panelNuevoComentario.add(btnEnviarComentario);

        btnEnviarComentario.addActionListener(e -> {
            String texto = txtNuevoComentario.getText().trim();
            if (!texto.isEmpty()) {
                btnEnviarComentario.setEnabled(false);

                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() {
                        Comentario nuevoComentario = new Comentario(texto, usuario.getId(), post.getId(), LocalDateTime.now());
                        ComentarioDAO.insertarComentario(nuevoComentario);
                        return null;
                    }

                    @Override
                    protected void done() {
                        cargarComentarios.run();
                        txtNuevoComentario.setText("");
                        btnEnviarComentario.setEnabled(true);
                    }
                }.execute();
            }
        });

        JPanel panelComentariosTotales = new JPanel(new BorderLayout());
        panelComentariosTotales.add(panelComentarios, BorderLayout.CENTER);
        panelComentariosTotales.add(panelNuevoComentario, BorderLayout.SOUTH);

        panelPost.add(panelComentariosTotales, BorderLayout.SOUTH);

        return panelPost;
    }

    private void actualizarContadores(Post post, JLabel lblLikes, JLabel lblDislikes) {
        int likes = ReaccionDAO.contarReacciones(post.getId(), "like");
        int dislikes = ReaccionDAO.contarReacciones(post.getId(), "dislike");
        lblLikes.setText("👍 " + likes);
        lblDislikes.setText("👎 " + dislikes);
    }

    private JPanel crearPanelComentario(Comentario comentario) {
        JPanel panelComentario = new JPanel(new BorderLayout(10, 0));
        panelComentario.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        ImageIcon fotoPerfil = obtenerImagenDesdeURL(comentario.getFotoPerfilAutor(), 40, 40);
        JLabel lblFotoPerfil = new JLabel(fotoPerfil);
        panelComentario.add(lblFotoPerfil, BorderLayout.WEST);

        String texto = comentario.getNombreAutor() + ": " + comentario.getContenido();
        JTextArea txtComentario = new JTextArea(texto);
        txtComentario.setLineWrap(true);
        txtComentario.setWrapStyleWord(true);
        txtComentario.setEditable(false);
        txtComentario.setBackground(null);
        txtComentario.setFont(new Font("Arial", Font.ITALIC, 12));
        txtComentario.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        panelComentario.add(txtComentario, BorderLayout.CENTER);

        return panelComentario;
    }

    private ImageIcon obtenerImagenDesdeURL(String url, int ancho, int alto) {
        try {
            ImageIcon original = new ImageIcon(new URL(url));
            Image img = original.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            return new ImageIcon();
        }
    }
}
