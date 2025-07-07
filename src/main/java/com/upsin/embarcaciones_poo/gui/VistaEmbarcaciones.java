package com.upsin.embarcaciones_poo.gui;

import com.upsin.embarcaciones_poo.modelo.Barco;
import com.upsin.embarcaciones_poo.modelo.Embarcacion;
import com.upsin.embarcaciones_poo.servicio.BarcoServicio;
import com.upsin.embarcaciones_poo.servicio.EmbarcacionServicio;
import org.springframework.stereotype.Component;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.Date;
import java.util.List;

@Component
public class VistaEmbarcaciones extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VistaEmbarcaciones.class.getName());
    private DefaultTableModel tablaModelo;
    private VistaMain vistaMain;
    private BarcoServicio barcoServicio;
    private EmbarcacionServicio embarcacionServicio;
    private Embarcacion embarcacion;



    public VistaEmbarcaciones(EmbarcacionServicio embarcacionServicio, BarcoServicio barcoServicio) {
        this.embarcacionServicio = embarcacionServicio;
        this.barcoServicio = barcoServicio;
        initComponents();
        inicializarComboBox();
        iniciarTabla();
        personalizarTablaEmbarcaciones2();

        Tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccion();
            }
        });

        btnModificar.addActionListener(evt -> btnModificarActionPerformed(evt));
        embarcacion = new Embarcacion();
    }

    private void personalizarTablaEmbarcaciones2() {
        JTableHeader header = Tabla.getTableHeader();
        header.setBackground(new Color(0, 133, 189));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setBackground(Color.WHITE);
        cellRenderer.setForeground(Color.BLACK);
        cellRenderer.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        for (int i = 0; i < Tabla.getColumnCount(); i++) {
            Tabla.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        Tabla.setRowHeight(25);
        Tabla.setShowGrid(true);
        Tabla.setGridColor(new Color(0, 133, 189));
    }


    public void inicializarComboBox() {
        try {
            jComboBox1.removeAllItems();
            List<Barco> barcos = barcoServicio.listarBarco();
            if (barcos == null || barcos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontraron barcos disponibles.", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }
            for (Barco barco : barcos) {
                if (barco != null) {
                    jComboBox1.addItem(barco);
                }
            }
            if (jComboBox1.getItemCount() > 0) {
                jComboBox1.setSelectedIndex(0);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error cargando barcos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void personalizarTablaEmbarcaciones() {
        JTableHeader header = Tabla.getTableHeader();
        header.setBackground(new Color(0, 133, 189));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setBackground(Color.WHITE);
        cellRenderer.setForeground(Color.BLACK);
        cellRenderer.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        for (int i = 0; i < Tabla.getColumnCount(); i++) {
            Tabla.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        Tabla.setRowHeight(25);
        Tabla.setShowGrid(true);
        Tabla.setGridColor(new Color(0, 133, 189));
    }

    public void iniciarTabla() {
        try {
            tablaModelo = new DefaultTableModel(new String[]{"ID", "Barco", "Puerto Origen", "Puerto Destino", "Fechas"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            Tabla.setModel(tablaModelo);
            Tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            listar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error inicializando tabla: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void listar() {
        try {
            tablaModelo.setRowCount(0);
            List<Embarcacion> embarcaciones = embarcacionServicio.listarEmbarcacion();
            if (embarcaciones == null || embarcaciones.isEmpty()) {
                return;
            }
            for (Embarcacion em : embarcaciones) {
                if (em != null && em.getBarco() != null) {
                    Object[] fila = {
                            em.getIdEmbarcacion(),
                            em.getBarco().getNombre(),
                            em.getPuertoOrigen(),
                            em.getPuertoDestino(),
                            "Salida: " + em.getFechaSalida() + ", Llegada: " + em.getFechaLlegada()
                    };
                    tablaModelo.addRow(fila);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al listar embarcaciones: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void guardar() {
        try {
            Barco barcoSeleccionado = (Barco) jComboBox1.getSelectedItem();
            if (barcoSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Selecciona un barco.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String puertoOrigen = PueroOrigenTEXT.getText().trim();
            String puertoDestino = PuertoDestinoTEXT.getText().trim();
            Date fechaSalida = FechaSalidaDATE.getDate();
            Date fechaLlegada = FechaLlegadaDATE.getDate();

            if (puertoOrigen.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingresa el puerto de origen.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (puertoDestino.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingresa el puerto de destino.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (fechaSalida == null) {
                JOptionPane.showMessageDialog(this, "Selecciona la fecha de salida.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (fechaLlegada == null) {
                JOptionPane.showMessageDialog(this, "Selecciona la fecha de llegada.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (fechaLlegada.before(fechaSalida)) {
                JOptionPane.showMessageDialog(this, "La fecha de llegada no puede ser anterior a la fecha de salida.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Embarcacion nuevaEmbarcacion = new Embarcacion();
            nuevaEmbarcacion.setBarco(barcoSeleccionado);
            nuevaEmbarcacion.setPuertoOrigen(puertoOrigen);
            nuevaEmbarcacion.setPuertoDestino(puertoDestino);
            nuevaEmbarcacion.setFechaSalida(fechaSalida);
            nuevaEmbarcacion.setFechaLlegada(fechaLlegada);

            embarcacionServicio.guardar(nuevaEmbarcacion);
            JOptionPane.showMessageDialog(this, "Embarcación guardada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            limpiar();
            listar();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error guardando embarcación: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cargarTabla() {
        List<Embarcacion> lista = embarcacionServicio.listarEmbarcacion();
        DefaultTableModel modelo = (DefaultTableModel) Tabla.getModel();
        modelo.setRowCount(0);  // Limpiar tabla

        for (Embarcacion e : lista) {
            Object[] fila = {
                    e.getBarco().getNombre(),
                    e.getPuertoOrigen(),
                    e.getPuertoDestino(),
                    e.getFechaSalida(),
                    e.getFechaLlegada()
            };
            modelo.addRow(fila);
        }
    }



    public void cargarSeleccion() {
        try {
            int fila = Tabla.getSelectedRow();
            if (fila == -1) return;

            Integer id = (Integer) tablaModelo.getValueAt(fila, 0);
            if (id == null) return;

            Embarcacion e = embarcacionServicio.buscarPorId(id);
            if (e == null) return;

            embarcacion = e;
            jComboBox1.setSelectedItem(embarcacion.getBarco());
            PueroOrigenTEXT.setText(embarcacion.getPuertoOrigen());
            PuertoDestinoTEXT.setText(embarcacion.getPuertoDestino());
            FechaSalidaDATE.setDate(embarcacion.getFechaSalida());
            FechaLlegadaDATE.setDate(embarcacion.getFechaLlegada());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error cargando selección: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void limpiar() {
        try {
            embarcacion = new Embarcacion();
            if (jComboBox1.getItemCount() > 0) {
                jComboBox1.setSelectedIndex(0);
            }
            PueroOrigenTEXT.setText("");
            PuertoDestinoTEXT.setText("");
            FechaSalidaDATE.setDate(null);
            FechaLlegadaDATE.setDate(null);
            Tabla.clearSelection();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error limpiando formulario: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eliminar() {
        try {
            if (embarcacion == null || embarcacion.getIdEmbarcacion() == null) {
                JOptionPane.showMessageDialog(this, "Selecciona un registro para eliminar.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Estás seguro de eliminar la embarcación seleccionada?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                embarcacionServicio.eliminar(embarcacion);
                JOptionPane.showMessageDialog(this, "Embarcación eliminada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiar();
                listar();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error eliminando embarcación: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (embarcacion == null || embarcacion.getIdEmbarcacion() == null) {
                JOptionPane.showMessageDialog(this, "Selecciona un registro para modificar.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Barco barcoSeleccionado = (Barco) jComboBox1.getSelectedItem();
            String puertoOrigen = PueroOrigenTEXT.getText().trim();
            String puertoDestino = PuertoDestinoTEXT.getText().trim();
            Date fechaSalida = FechaSalidaDATE.getDate();
            Date fechaLlegada = FechaLlegadaDATE.getDate();

            if (barcoSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Selecciona un barco.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (puertoOrigen.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingresa el puerto de origen.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (puertoDestino.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingresa el puerto de destino.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (fechaSalida == null) {
                JOptionPane.showMessageDialog(this, "Selecciona la fecha de salida.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (fechaLlegada == null) {
                JOptionPane.showMessageDialog(this, "Selecciona la fecha de llegada.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (fechaLlegada.before(fechaSalida)) {
                JOptionPane.showMessageDialog(this, "La fecha de llegada no puede ser anterior a la fecha de salida.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            embarcacion.setBarco(barcoSeleccionado);
            embarcacion.setPuertoOrigen(puertoOrigen);
            embarcacion.setPuertoDestino(puertoDestino);
            embarcacion.setFechaSalida(fechaSalida);
            embarcacion.setFechaLlegada(fechaLlegada);

            embarcacionServicio.guardar(embarcacion);

            JOptionPane.showMessageDialog(this, "Embarcación modificada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            limpiar();
            listar();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error modificando embarcación: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }









    public void setVistaMain(VistaMain vistaMain) {
        this.vistaMain = vistaMain;
    }
   
   public void regresar(){
        this.setVisible(false);
        vistaMain.setVisible(true);
    }
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        PueroOrigenTEXT = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        PuertoDestinoTEXT = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        FechaSalidaDATE = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        FechaLlegadaDATE = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabla = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        btnMain = new javax.swing.JLabel();
        btnLogin = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btnEliminar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setText("Barco:");

        jComboBox1.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel3.setText("Puerto de origen:");

        PueroOrigenTEXT.setBackground(new java.awt.Color(255, 255, 255));
        PueroOrigenTEXT.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        PueroOrigenTEXT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PueroOrigenTEXTActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel4.setText("Puerto de destino:");

        PuertoDestinoTEXT.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel5.setText("Fecha de salida:");

        FechaSalidaDATE.setBackground(new java.awt.Color(255, 255, 255));
        FechaSalidaDATE.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel6.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel6.setText("Fecha de llegada:");

        FechaLlegadaDATE.setBackground(new java.awt.Color(255, 255, 255));
        FechaLlegadaDATE.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        Tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "BARCO", "PUERTO ORIGEN", "PUERTO DESTINO", "FECHA SALIDA", "FECHA LLEGADA"
            }
        ));
        jScrollPane1.setViewportView(Tabla);

        jPanel1.setBackground(new java.awt.Color(102, 204, 255));
        jPanel1.setForeground(new java.awt.Color(102, 204, 255));

        btnMain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/LogoFInal.png"))); // NOI18N
        btnMain.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnMainMouseClicked(evt);
            }
        });

        btnLogin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/icono_CerrarSesion.png"))); // NOI18N
        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLoginMouseClicked(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Arial Black", 0, 30)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 51));
        jLabel7.setText("BARCOS ACTIVOS");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(btnMain)
                .addGap(352, 352, 352)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnLogin)
                .addGap(62, 62, 62))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnLogin)
                    .addComponent(btnMain)
                    .addComponent(jLabel7))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnEliminar.setBackground(new java.awt.Color(0, 133, 189));
        btnEliminar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnEliminar.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminar.setText("ELIMINAR");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnLimpiar.setBackground(new java.awt.Color(0, 133, 189));
        btnLimpiar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLimpiar.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiar.setText("LIMPIAR");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        btnModificar.setBackground(new java.awt.Color(0, 133, 189));
        btnModificar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnModificar.setForeground(new java.awt.Color(255, 255, 255));
        btnModificar.setText("MODIFICAR");

        btnGuardar.setBackground(new java.awt.Color(0, 133, 189));
        btnGuardar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setText("GUARDAR");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(FechaLlegadaDATE, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(FechaSalidaDATE, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel4)
                        .addComponent(jLabel5)
                        .addComponent(jLabel2)
                        .addComponent(jLabel3)
                        .addComponent(PueroOrigenTEXT, javax.swing.GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE)
                        .addComponent(PuertoDestinoTEXT)
                        .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnGuardar)
                        .addGap(18, 18, 18)
                        .addComponent(btnModificar)
                        .addGap(18, 18, 18)
                        .addComponent(btnLimpiar)
                        .addGap(18, 18, 18)
                        .addComponent(btnEliminar)))
                .addGap(36, 36, 36)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 820, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 72, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(PueroOrigenTEXT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(PuertoDestinoTEXT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(FechaSalidaDATE, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(FechaLlegadaDATE, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 481, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(118, 118, 118))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMainMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMainMouseClicked
        regresar();
    }//GEN-LAST:event_btnMainMouseClicked

    private void btnLoginMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLoginMouseClicked
        VistaLogin login = new VistaLogin();  // Crear instancia
        login.setVisible(true);               // Mostrar nueva ventana
        this.setVisible(false);               // Ocultar la actual
    }//GEN-LAST:event_btnLoginMouseClicked

    private void PueroOrigenTEXTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PueroOrigenTEXTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PueroOrigenTEXTActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        guardar();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiar();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        eliminar();
    }//GEN-LAST:event_btnEliminarActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser FechaLlegadaDATE;
    private com.toedter.calendar.JDateChooser FechaSalidaDATE;
    private javax.swing.JTextField PueroOrigenTEXT;
    private javax.swing.JTextField PuertoDestinoTEXT;
    private javax.swing.JTable Tabla;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JLabel btnLogin;
    private javax.swing.JLabel btnMain;
    private javax.swing.JButton btnModificar;
    private javax.swing.JComboBox<Barco> jComboBox1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
