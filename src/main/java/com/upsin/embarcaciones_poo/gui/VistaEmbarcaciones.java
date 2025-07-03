package com.upsin.embarcaciones_poo.gui;

import com.upsin.embarcaciones_poo.modelo.Barco;
import com.upsin.embarcaciones_poo.modelo.Embarcacion;
import com.upsin.embarcaciones_poo.servicio.BarcoServicio;
import com.upsin.embarcaciones_poo.servicio.EmbarcacionServicio;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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

        Tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccion();
            }
        });

        btnModificar.addActionListener(evt -> btnModificarActionPerformed(evt));
        embarcacion = new Embarcacion();
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
        jLabel7 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnMain = new javax.swing.JLabel();
        btnLogin = new javax.swing.JLabel();
        btnEliminar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setText("Barco");

        jComboBox1.setModel(new DefaultComboBoxModel<Barco>());

        jLabel3.setText("Puerto de origen");

        PueroOrigenTEXT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PueroOrigenTEXTActionPerformed(evt);
            }
        });

        jLabel4.setText("Puerto de destino");

        jLabel5.setText("Fecha de salida");

        jLabel6.setText("Fecha de llegada");

        Tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(Tabla);

        jLabel7.setFont(new java.awt.Font("Arial Black", 0, 48)); // NOI18N
        jLabel7.setText("BARCOS ACTIVOS");

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(btnMain)
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
                    .addComponent(btnMain))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        btnModificar.setText("Modificar");

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(100, 100, 100)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(50, 50, 50)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(PueroOrigenTEXT, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3))
                                .addGap(53, 53, 53)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(PuertoDestinoTEXT, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4))
                                .addGap(64, 64, 64)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(FechaSalidaDATE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5))
                                .addGap(57, 57, 57)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(FechaLlegadaDATE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnGuardar)
                                        .addGap(29, 29, 29)
                                        .addComponent(btnModificar)
                                        .addGap(29, 29, 29)
                                        .addComponent(btnLimpiar)
                                        .addGap(28, 28, 28)
                                        .addComponent(btnEliminar))))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(456, 456, 456)
                        .addComponent(jLabel7)))
                .addContainerGap(120, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(42, 42, 42)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel2)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel5)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(PueroOrigenTEXT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(PuertoDestinoTEXT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(FechaSalidaDATE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(FechaLlegadaDATE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnEliminar)
                        .addComponent(btnLimpiar)
                        .addComponent(btnModificar)
                        .addComponent(btnGuardar)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(87, 87, 87))
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
