package src.ui;

import src.dao.PostDAO;
import src.dao.ReaccionDAO;
import src.modelo.Post;
import src.modelo.Usuario;
import src.ai.GenerarBiografia;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class PerfilVentana extends JFrame {

    private Usuario usuario;         // Usuario del perfil que se muestra - when
    private Usuario usuarioLogueado; // Usuario que está viendo el perfil - pov
    private JPanel panelPrincipal;
    private JPanel panelPostsPropios;
    private JPanel panelPostsLikeados;


    private JTextArea areaBiografia;

    public PerfilVentana(Usuario usuario, Usuario usuarioLogueado) {
        this.usuario = usuario;
        this.usuarioLogueado = usuarioLogueado;

        setTitle("Perfil de " + usuario.getNombre());
        setSize(500, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Mostrar foto y nombre
        JLabel lblNombre = new JLabel(usuario.getNombre());
        lblNombre.setFont(new Font("Arial", Font.BOLD, 18));
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        ImageIcon fotoPerfil = obtenerImagenDesdeURL(usuario.getFotoPerfil(), 100, 100);
        JLabel lblFoto = new JLabel(fotoPerfil);
        lblFoto.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelPrincipal.add(lblFoto);
        panelPrincipal.add(Box.createVerticalStrut(5));
        panelPrincipal.add(lblNombre);
        panelPrincipal.add(Box.createVerticalStrut(10));

        // 1.5 Mostrar la biografía (nuevo)
        areaBiografia = new JTextArea(usuario.getBiografia() != null && !usuario.getBiografia().isEmpty() ? usuario.getBiografia() : "No hay biografía aún.");
        areaBiografia.setWrapStyleWord(true);
        areaBiografia.setLineWrap(true);
        areaBiografia.setEditable(false);
        areaBiografia.setBackground(null);
        areaBiografia.setBorder(BorderFactory.createTitledBorder("Biografía"));
        areaBiografia.setMaximumSize(new Dimension(450, 100));
        areaBiografia.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelPrincipal.add(areaBiografia);
        panelPrincipal.add(Box.createVerticalStrut(15));

        // 2. Panel de Posts Propios
        panelPostsPropios = new JPanel();
        panelPostsPropios.setLayout(new BoxLayout(panelPostsPropios, BoxLayout.Y_AXIS));
        panelPostsPropios.setBorder(BorderFactory.createTitledBorder("Posts Del Usuario"));
        cargarPostsPropios();

        // 3. Panel de Posts Likeados
        panelPostsLikeados = new JPanel();
        panelPostsLikeados.setLayout(new BoxLayout(panelPostsLikeados, BoxLayout.Y_AXIS));
        panelPostsLikeados.setBorder(BorderFactory.createTitledBorder("Posts likeados"));
        cargarPostsLikeados();

        // paneles de posts para el panel principal
        panelPrincipal.add(panelPostsPropios);
        panelPrincipal.add(Box.createVerticalStrut(10));
        panelPrincipal.add(panelPostsLikeados);

        // 4. Botones, solo para el dueno del perfil
        if (usuario.getId() == usuarioLogueado.getId()) {
            // Botón editar perfil
            JButton btnEditar = new JButton("Editar Perfil");
            btnEditar.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnEditar.addActionListener(e -> {
                new EditarPerfilVentana(usuarioLogueado);
            });

            // Botón para generar la biografía con IA
            JButton btnGenerarBio = new JButton("Generar Biografía IA");
            btnGenerarBio.setAlignmentX(Component.CENTER_ALIGNMENT);

            btnGenerarBio.addActionListener(e -> {
                btnGenerarBio.setEnabled(false); // Para evitar clics múltiples
                btnGenerarBio.setText("Generando...");

                SwingWorker<Void, Void> worker = new SwingWorker<>() {
                    String bioNueva = "";

                    @Override
                    protected Void doInBackground() {
                        try {
                            bioNueva = GenerarBiografia.generarBiografia(usuario.getNombre());
                        } catch (IOException ex) {
                            bioNueva = null;
                            ex.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void done() {
                        btnGenerarBio.setEnabled(true);
                        btnGenerarBio.setText("Generar Biografía IA");

                        if (bioNueva == null || bioNueva.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Error al generar la biografía con IA.");
                            return;
                        }

                        usuario.setBiografia(bioNueva);
                        areaBiografia.setText(bioNueva);

                        boolean actualizado = src.dao.UsuarioDAO.actualizarUsuario(usuario);
                        if (actualizado) {
                            JOptionPane.showMessageDialog(null, "¡Biografía actualizada con éxito!");
                        } else {
                            JOptionPane.showMessageDialog(null, "⚠Error al guardar en la base de datos.");
                        }
                    }
                };

                worker.execute();
            });


            panelPrincipal.add(Box.createVerticalStrut(15));
            panelPrincipal.add(btnEditar);
            panelPrincipal.add(Box.createVerticalStrut(10));
            panelPrincipal.add(btnGenerarBio);
        }

        add(new JScrollPane(panelPrincipal));
        setVisible(true);
    }

    private void cargarPostsPropios() {
        List<Post> posts = PostDAO.obtenerPostsPorUsuario(usuario.getId());
        for (Post post : posts) {
            panelPostsPropios.add(crearPanelPost(post));
        }
    }

    private void cargarPostsLikeados() {
        List<Post> posts = PostDAO.obtenerPostsLikeadosPorUsuario(usuario.getId());
        for (Post post : posts) {
            panelPostsLikeados.add(crearPanelPost(post));
        }
    }

    private JPanel crearPanelPost(Post post) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.setBackground(Color.WHITE);

        // Panel superior: foto + nombre del autor del post
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.setBackground(Color.WHITE);

        ImageIcon icon = obtenerImagenDesdeURL(post.getFotoPerfilAutor(), 40, 40);
        JLabel lblFoto = new JLabel(icon);
        panelSuperior.add(lblFoto);

        JLabel lblNombre = new JLabel(post.getNombreAutor());
        lblNombre.setFont(new Font("Arial", Font.BOLD, 14));
        panelSuperior.add(lblNombre);

        // Contenido del post
        JTextArea areaContenido = new JTextArea(post.getContenido());
        areaContenido.setWrapStyleWord(true);
        areaContenido.setLineWrap(true);
        areaContenido.setEditable(false);
        areaContenido.setBackground(null);
        areaContenido.setBorder(null);

        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(areaContenido, BorderLayout.CENTER);

        return panel;
    }

    private ImageIcon obtenerImagenDesdeURL(String url, int ancho, int alto) {
        try {
            ImageIcon original = new ImageIcon(new URL(url));
            Image img = original.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            return new ImageIcon(); // Imagen vacía en caso de emboscada
        }
    }
}
