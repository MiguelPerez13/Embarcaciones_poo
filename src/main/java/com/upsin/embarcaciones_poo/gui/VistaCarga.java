package com.upsin.embarcaciones_poo.gui;

import com.upsin.embarcaciones_poo.modelo.*;
import com.upsin.embarcaciones_poo.servicio.AlmacenServicio;
import com.upsin.embarcaciones_poo.servicio.EmbarcacionCargaServicio;
import com.upsin.embarcaciones_poo.servicio.EmbarcacionServicio;
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
public class VistaCarga extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VistaCarga.class.getName());
    private EmbarcacionCargaServicio embarcacionCargaServicio;
    private AlmacenServicio almacenServicio;
    private EmbarcacionServicio embarcacionServicio;
    private DefaultTableModel tablaModelo;
    private VistaMain vistaMain;
    private EmbarcacionCarga embarcacionCarga;
    private Integer permiso;

    @Autowired
    public VistaCarga(EmbarcacionCargaServicio embarcacionCargaServicio, AlmacenServicio almacenServicio, EmbarcacionServicio embarcacionServicio) {
        this.embarcacionCargaServicio = embarcacionCargaServicio;
        this.almacenServicio = almacenServicio;
        this.embarcacionServicio = embarcacionServicio;
        this.embarcacionCarga = new EmbarcacionCarga();
        initComponents();
        personalizarTablaEmbarcaciones();
        iniciarTabla();
        inicializarEmbarcaciones();
        inicializarAlmacen();
    }

    public void setPermiso(Integer permiso){
        this.permiso = permiso;
    }

    public void setVistaMain(VistaMain vistaMain) {
        this.vistaMain = vistaMain;
    }

    private void personalizarTablaEmbarcaciones() {
        JTableHeader header = tabla.getTableHeader();
        header.setBackground(new Color(0, 133, 189));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setBackground(Color.WHITE);
        cellRenderer.setForeground(Color.BLACK);
        cellRenderer.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        tabla.setRowHeight(25);
        tabla.setShowGrid(true);
        tabla.setGridColor(new Color(0, 133, 189));
    }

    public void iniciarTabla(){

        this.tablaModelo = new DefaultTableModel(0, 3){
            @Override
            public boolean isCellEditable(int row,int column){return false;}
        };

        String[] nombresColumnas = {"Embarcacion","Almacen","Fecha de Carga"};
        this.tablaModelo.setColumnIdentifiers(nombresColumnas);
        this.tabla.setModel(tablaModelo);
        this.tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        listar();
    }

    public void listar(){
        this.tablaModelo.setRowCount(0);
        List<EmbarcacionCarga> cargas = embarcacionCargaServicio.listarEmbarcacionCarga();

        cargas.forEach(descarga -> {
            Object[] renglon = {
                    descarga.getEmbarcacion(),
                    descarga.getAlmacen(),
                    descarga.getFechaCarga()
            };
            tablaModelo.addRow(renglon);
        });
    }

    public void inicializarEmbarcaciones(){
        List<Embarcacion> embarcaciones = embarcacionServicio.listarEmbarcacion();

        embarcacionComboBox.removeAllItems();

        embarcaciones.forEach(embarcacion -> {
            embarcacionComboBox.addItem(embarcacion);
        });
    }

    public void inicializarAlmacen(){
        List<Almacen> almacenes = almacenServicio.listarAlmacen();

        almacenComboBox.removeAllItems();

        almacenes.forEach(almacen -> {
            almacenComboBox.addItem(almacen);
        });
    }

    private void guardar(){
        if(embarcacionComboBox.getItemCount() == 0){
            JOptionPane.showMessageDialog(this,"No hay registros de embarcaciones");
            return;
        }

        if(almacenComboBox.getItemCount() == 0){
            JOptionPane.showMessageDialog(this,"No hay contenedores en almacen");
            return;
        }

        var embarcacion = (Embarcacion) embarcacionComboBox.getSelectedItem();
        var almacen = (Almacen) almacenComboBox.getSelectedItem();
        var fecha = this.fecha.getDate();

        if(embarcacion==null || almacen==null || fecha==null){
            JOptionPane.showMessageDialog(this,"Rellena todos los campos para continuar");
            return;
        }

        EmbarcacionCargaId id = new EmbarcacionCargaId(embarcacion.getIdEmbarcacion(),almacen.getIdAlmacen());

        embarcacionCarga.setId(id);
        embarcacionCarga.setEmbarcacion(embarcacion);
        embarcacionCarga.setAlmacen(almacen);
        embarcacionCarga.setFechaCarga(fecha);

        embarcacionCargaServicio.guardar(embarcacionCarga);

        limpiar();
        listar();
    }

    private void cargarSeleccion() {
        int renglon = tabla.getSelectedRow();
        if (renglon == -1) return;

        Embarcacion embarcacion = (Embarcacion) tabla.getModel().getValueAt(renglon, 0);
        Almacen almacen = (Almacen) tabla.getModel().getValueAt(renglon, 1);
        Date fechaCarga = (Date) tabla.getModel().getValueAt(renglon, 2);

        EmbarcacionCargaId id = new EmbarcacionCargaId(
                embarcacion.getIdEmbarcacion(),
                almacen.getIdAlmacen()
        );

        embarcacionCarga.setId(id);
        embarcacionCarga.setEmbarcacion(embarcacion);
        embarcacionCarga.setAlmacen(almacen);
        embarcacionCarga.setFechaCarga(fechaCarga);

        embarcacionComboBox.setSelectedItem(embarcacion);
        almacenComboBox.setSelectedItem(almacen);
        this.fecha.setDate(fechaCarga);
    }

    public void limpiar(){
        this.embarcacionCarga = new EmbarcacionCarga();

        this.fecha.setDate(null);
    }

    public void eliminar(){
        embarcacionCargaServicio.eliminar(embarcacionCarga);

        limpiar();
        listar();
    }

    public Boolean verificarSeleccion(){
        if(this.embarcacionCarga.getId()==null){
            JOptionPane.showMessageDialog(this,"Seleccione un registro antes de continuar");
            return false;
        }
        return true;
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

        jPanel1 = new javax.swing.JPanel();
        btnMain = new javax.swing.JLabel();
        btnLogin = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        embarcacionComboBox = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        almacenComboBox = new javax.swing.JComboBox<>();
        fecha = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        editarButton = new javax.swing.JButton();
        guardarButton = new javax.swing.JButton();
        limpiarButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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

        jLabel2.setFont(new java.awt.Font("Arial Black", 0, 30)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 51));
        jLabel2.setText("CARGA");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(btnMain)
                .addGap(429, 429, 429)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnLogin)
                .addGap(62, 62, 62))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(btnLogin)
                    .addComponent(btnMain))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        embarcacionComboBox.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        embarcacionComboBox.setForeground(new java.awt.Color(0, 0, 0));

        jLabel3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel3.setText("Embarcacion");

        jLabel4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel4.setText("Almacen");

        almacenComboBox.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        almacenComboBox.setForeground(new java.awt.Color(0, 0, 0));

        fecha.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        fecha.setForeground(new java.awt.Color(0, 0, 0));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel5.setText("Fecha Entrega: ");

        editarButton.setBackground(new java.awt.Color(0, 133, 189));
        editarButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        editarButton.setForeground(new java.awt.Color(255, 255, 255));
        editarButton.setText("MODIFICAR");
        editarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editarButtonActionPerformed(evt);
            }
        });

        guardarButton.setBackground(new java.awt.Color(0, 133, 189));
        guardarButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        guardarButton.setForeground(new java.awt.Color(255, 255, 255));
        guardarButton.setText("GUARDAR");
        guardarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarButtonActionPerformed(evt);
            }
        });

        limpiarButton.setBackground(new java.awt.Color(0, 133, 189));
        limpiarButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        limpiarButton.setForeground(new java.awt.Color(255, 255, 255));
        limpiarButton.setText("LIMPIAR");
        limpiarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limpiarButtonActionPerformed(evt);
            }
        });

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Embarcacion", "Almacen", "Fecha de Carga"
            }
        ));
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabla);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(78, 78, 78)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(guardarButton)
                        .addGap(64, 64, 64)
                        .addComponent(editarButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(limpiarButton))
                    .addComponent(fecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4)
                    .addComponent(almacenComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3)
                    .addComponent(embarcacionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(58, 58, 58)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 766, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(94, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(embarcacionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(almacenComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(77, 77, 77)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(guardarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(editarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(limpiarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 112, Short.MAX_VALUE))
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

    private void editarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editarButtonActionPerformed
        if(verificarPermisos(1)){
            if (verificarSeleccion()) {
                guardar();
            }
        }
    }//GEN-LAST:event_editarButtonActionPerformed

    private void guardarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarButtonActionPerformed
        if(verificarPermisos(1)){
            embarcacionCarga = new EmbarcacionCarga();
            guardar();
        }//GEN-LAST:event_guardarButtonActionPerformed
    }//GEN-LAST:event_guardarButtonActionPerformed

    private void limpiarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_limpiarButtonActionPerformed
        limpiar();
    }//GEN-LAST:event_limpiarButtonActionPerformed

    private void tablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaMouseClicked
        cargarSeleccion();
    }//GEN-LAST:event_tablaMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<Almacen> almacenComboBox;
    private javax.swing.JLabel btnLogin;
    private javax.swing.JLabel btnMain;
    private javax.swing.JButton editarButton;
    private javax.swing.JComboBox<Embarcacion> embarcacionComboBox;
    private com.toedter.calendar.JDateChooser fecha;
    private javax.swing.JButton guardarButton;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton limpiarButton;
    private javax.swing.JTable tabla;
    // End of variables declaration//GEN-END:variables
}
