package com.upsin.embarcaciones_poo.gui;

import com.upsin.embarcaciones_poo.modelo.Almacen;
import com.upsin.embarcaciones_poo.modelo.Embarcacion;
import com.upsin.embarcaciones_poo.modelo.RetiroContenedor;
import com.upsin.embarcaciones_poo.servicio.AlmacenServicio;
import com.upsin.embarcaciones_poo.servicio.RetiroContenedorServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.Date;
import java.util.List;

@Component
public class VistaRetiros extends javax.swing.JFrame {

    private RetiroContenedorServicio retiroServicio;
    private AlmacenServicio almacenServicio;
    private DefaultTableModel tablaModelo;
    private RetiroContenedor retiro;
    private VistaMain vistaMain;
    private Integer permiso;





    @Autowired
    public VistaRetiros(RetiroContenedorServicio retiroServicio, AlmacenServicio almacenServicio) {
        this.retiroServicio = retiroServicio;
        this.almacenServicio = almacenServicio;
        this.retiro = new RetiroContenedor();
        initComponents();
        iniciarTabla();
        personalizarTablaRetiros();
        cargarAlmacenes();

        jTable1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccion();
            }
        });
    }

    public void setPermiso(Integer permiso){
        this.permiso = permiso;
    }

    private void personalizarTablaRetiros() {
        JTableHeader header = jTable1.getTableHeader();
        header.setBackground(new Color(0, 133, 189));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setBackground(Color.WHITE);
        cellRenderer.setForeground(Color.BLACK);
        cellRenderer.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        for (int i = 0; i < jTable1.getColumnCount(); i++) {
            jTable1.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        jTable1.setRowHeight(25);
        jTable1.setShowGrid(true);
        jTable1.setGridColor(new Color(0, 133, 189));
    }

    private void iniciarTabla() {
        tablaModelo = new DefaultTableModel(0, 2) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        String[] columnas = {
                "ID Retiro", "Almacén", "Receptor", "Tipo Vehículo",
                "Matrícula", "Empresa Transporte", "Fecha Retiro", "Observaciones"
        };
        tablaModelo.setColumnIdentifiers(columnas);
        jTable1.setModel(tablaModelo);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        listar();
    }

    public void cargarAlmacenes() {
        List<Almacen> almacenes = almacenServicio.listarAlmacen();
        jComboBox1.removeAllItems();
        for (Almacen a : almacenes) {
            jComboBox1.addItem(a);
        }
    }

    public void listar() {
        tablaModelo.setRowCount(0);
        List<RetiroContenedor> retiroContenedors = retiroServicio.listarRetiroContenedor();

        for (RetiroContenedor r : retiroContenedors) {
            Object[] renglon = {
                    r.getIdRetiro(),
                    r.getAlmacen() != null ? r.getAlmacen().getLoteAlmacen() : "N/A",
                    r.getNombreReceptor(),
                    r.getTipoVehiculo(),
                    r.getMatricula(),
                    r.getEmpresaTransporte(),
                    r.getFechaRetiro(),
                    r.getObservaciones()
            };
            tablaModelo.addRow(renglon);
        }
    }

    private void limpiar() {
        retiro = new RetiroContenedor();
        jComboBox1.setSelectedIndex(-1);
        NombreText.setText("");
        TipoVehiculoText.setText("");
        MatriculaText.setText("");
        EmpresaTrasnText.setText("");
        FechaRetiroDATE.setDate(null);
        ObservacionesText.setText("");
    }

    private void guardar() {
        try {
            if (jComboBox1.getItemCount() == 0 || jComboBox1.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un lote de almacén válido.");
                return;
            }

            Almacen almacen;
            try {
                almacen = (Almacen) jComboBox1.getSelectedItem();
            } catch (ClassCastException e) {
                JOptionPane.showMessageDialog(this, "Error al obtener el lote de almacén seleccionado.");
                return;
            }

            String nombreReceptor = NombreText.getText().trim();
            String tipoVehiculo = TipoVehiculoText.getText().trim();
            String matricula = MatriculaText.getText().trim();
            String empresaTransporte = EmpresaTrasnText.getText().trim();
            Date fechaRetiro;

            try {
                fechaRetiro = FechaRetiroDATE.getDate();
                if (fechaRetiro == null) {
                    JOptionPane.showMessageDialog(this, "Seleccione una fecha de retiro válida.");
                    return;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al leer la fecha de retiro.");
                return;
            }

            String observaciones = ObservacionesText.getText().trim();

            if (nombreReceptor.isEmpty() || tipoVehiculo.isEmpty() || matricula.isEmpty() ||
                    empresaTransporte.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Llena todos los campos obligatorios del formulario.");
                return;
            }

            retiro = new RetiroContenedor();
            retiro.setAlmacen(almacen);
            retiro.setNombreReceptor(nombreReceptor);
            retiro.setTipoVehiculo(tipoVehiculo);
            retiro.setMatricula(matricula);
            retiro.setEmpresaTransporte(empresaTransporte);
            retiro.setFechaRetiro(fechaRetiro);
            retiro.setObservaciones(observaciones);

            try {
                retiroServicio.guardar(retiro);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Ocurrió un error al guardar el retiro:\n" + e.getMessage());
                return;
            }

            limpiar();
            listar();
            JOptionPane.showMessageDialog(this, "Retiro guardado correctamente.");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage());
        }
    }

    private void cargarSeleccion() {
        int fila = jTable1.getSelectedRow();
        if (fila == -1) return;

        Integer id = (Integer) jTable1.getModel().getValueAt(fila, 0);
        retiro = retiroServicio.buscarPorId(id);

        if (retiro.getAlmacen() != null)
            jComboBox1.setSelectedItem(retiro.getAlmacen());
        else
            jComboBox1.setSelectedIndex(-1);

        NombreText.setText(retiro.getNombreReceptor());
        TipoVehiculoText.setText(retiro.getTipoVehiculo());
        MatriculaText.setText(retiro.getMatricula());
        EmpresaTrasnText.setText(retiro.getEmpresaTransporte());
        FechaRetiroDATE.setDate(retiro.getFechaRetiro());
        ObservacionesText.setText(retiro.getObservaciones());
    }

    private boolean verificarSeleccion() {
        if (retiro.getIdRetiro() == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un registro antes de continuar");
            return false;
        }
        return true;
    }

    private void eliminar() {
        if (verificarSeleccion()) {
            retiroServicio.eliminar(retiro);
            limpiar();
            listar();
        }
    }

    public void setVistaMain(VistaMain vistaMain) {
        this.vistaMain = vistaMain;
    }

    public void regresar(){
        this.setVisible(false);
        vistaMain.setVisible(true);
    }

    private boolean verificarPermisos(Integer nivel){
        if(permiso == nivel || permiso == 3 ){
            return true;
        }else{
            JOptionPane.showMessageDialog(this,"No tienes permisos para realizar la operacion");
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        EmpresaTrasnText = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        NombreText = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        TipoVehiculoText = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        MatriculaText = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        FechaRetiroDATE = new com.toedter.calendar.JDateChooser();
        jLabel9 = new javax.swing.JLabel();
        ObservacionesText = new javax.swing.JTextField();
        tabla = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        btnMain = new javax.swing.JLabel();
        btnLogin = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<Almacen>();

        jLabel6.setText("Matricula");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setText("Lote de almacen:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("Nombre del receptor:");

        EmpresaTrasnText.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        EmpresaTrasnText.setToolTipText("");

        jLabel4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel4.setText("Tipo de vehiculo:");

        NombreText.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel5.setText("Matricula:");
        jLabel5.setToolTipText("");

        TipoVehiculoText.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel7.setText("Fecha de retiro:");

        MatriculaText.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel8.setText("Empresa de transporte:");

        FechaRetiroDATE.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        FechaRetiroDATE.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel9.setText("Observaciones");

        ObservacionesText.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        tabla.setViewportView(jTable1);

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

        jLabel10.setFont(new java.awt.Font("Arial Black", 0, 30)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 51));
        jLabel10.setText("RETIROS");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(41, 41, 41)
                                .addComponent(btnMain)
                                .addGap(389, 389, 389)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnLogin)
                                .addGap(62, 62, 62))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel10)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(btnLogin)
                                                .addComponent(btnMain)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnGuardar.setBackground(new java.awt.Color(0, 133, 189));
        btnGuardar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setText("GUARDAR");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnModificar.setBackground(new java.awt.Color(0, 133, 189));
        btnModificar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnModificar.setForeground(new java.awt.Color(255, 255, 255));
        btnModificar.setText("MODIFICAR");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
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

        btnEliminar.setBackground(new java.awt.Color(0, 133, 189));
        btnEliminar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnEliminar.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminar.setText("ELIMINAR");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        jComboBox1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(54, 54, 54)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel3)
                                        .addComponent(NombreText, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
                                        .addComponent(MatriculaText, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(TipoVehiculoText, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(EmpresaTrasnText)
                                        .addComponent(jLabel5)
                                        .addComponent(jLabel8)
                                        .addComponent(FechaRetiroDATE, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
                                        .addComponent(jLabel7)
                                        .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(76, 76, 76)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel9)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(ObservacionesText, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(btnGuardar)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(btnModificar)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(btnLimpiar)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(btnEliminar))
                                                .addComponent(tabla, javax.swing.GroupLayout.PREFERRED_SIZE, 798, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(69, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(42, 42, 42)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel2)
                                                .addGap(18, 18, 18)
                                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(24, 24, 24)
                                                .addComponent(jLabel3)
                                                .addGap(18, 18, 18)
                                                .addComponent(NombreText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, Short.MAX_VALUE)
                                                .addComponent(jLabel4)
                                                .addGap(18, 18, 18)
                                                .addComponent(TipoVehiculoText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel5)
                                                .addGap(18, 18, 18)
                                                .addComponent(MatriculaText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(tabla, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addGap(24, 24, 24)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel8)
                                                .addGap(18, 18, 18)
                                                .addComponent(EmpresaTrasnText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel7)
                                                .addGap(18, 18, 18)
                                                .addComponent(FechaRetiroDATE, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel9)
                                                .addGap(18, 18, 18)
                                                .addComponent(ObservacionesText, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(87, 87, 87))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMainMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMainMouseClicked
        regresar();
    }//GEN-LAST:event_btnMainMouseClicked

    private void btnLoginMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLoginMouseClicked
        setVisible(false);
        vistaMain.volverLogin();
    }//GEN-LAST:event_btnLoginMouseClicked

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        if(verificarPermisos(1)){
            retiro = new RetiroContenedor();
            guardar();
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        if(verificarPermisos(1)){
            if (verificarSeleccion()) {
                guardar();
            }
        }
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiar();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        if(verificarPermisos(3)){
            if (verificarSeleccion()) {
                eliminar();
            }
        }
    }//GEN-LAST:event_btnEliminarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField EmpresaTrasnText;
    private com.toedter.calendar.JDateChooser FechaRetiroDATE;
    private javax.swing.JTextField MatriculaText;
    private javax.swing.JTextField NombreText;
    private javax.swing.JTextField ObservacionesText;
    private javax.swing.JTextField TipoVehiculoText;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JLabel btnLogin;
    private javax.swing.JLabel btnMain;
    private javax.swing.JButton btnModificar;
    private javax.swing.JComboBox<Almacen> jComboBox1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTable jTable1;
    private javax.swing.JScrollPane tabla;
    // End of variables declaration//GEN-END:variables
}
