package com.upsin.embarcaciones_poo.gui;

import com.upsin.embarcaciones_poo.modelo.Contenedor;
import com.upsin.embarcaciones_poo.modelo.Embarcacion;
import com.upsin.embarcaciones_poo.modelo.EmbarcacionContenedor;
import com.upsin.embarcaciones_poo.modelo.EmbarcacionContenedorId;
import com.upsin.embarcaciones_poo.servicio.ContenedorServicio;
import com.upsin.embarcaciones_poo.servicio.EmbarcacionContenedorServicio;
import com.upsin.embarcaciones_poo.servicio.EmbarcacionServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

@Component
public class VistaEmbarcacionesContenedores extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VistaEmbarcacionesContenedores.class.getName());
    private VistaMain vistaMain;
    private EmbarcacionContenedorServicio embarcacionContenedorServicio;
    private EmbarcacionServicio embarcacionServicio;
    private ContenedorServicio contenedorServicio;
    private EmbarcacionContenedor embarcacionContenedor;
    private DefaultTableModel tablaModelo;

    
    @Autowired
    public VistaEmbarcacionesContenedores(EmbarcacionContenedorServicio embarcacionContenedorServicio, EmbarcacionServicio embarcacionServicio, ContenedorServicio contenedorServicio) {
        this.embarcacionContenedorServicio = embarcacionContenedorServicio;
        this.embarcacionServicio = embarcacionServicio;
        this.contenedorServicio = contenedorServicio;
        embarcacionContenedor = new EmbarcacionContenedor();
        initComponents();
        iniciarTabla();
        inicializarContenedores();
        inicializarEmbarcaciones();
    }

    public void iniciarTabla(){
        // evitar la edicion de tablas
        this.tablaModelo = new DefaultTableModel(0, 2){
            @Override
            public boolean isCellEditable(int row,int column){return false;}
        };

        String[] nombresColumnas = {"Embarcacion","Contenedor"};
        this.tablaModelo.setColumnIdentifiers(nombresColumnas);
        this.tabla.setModel(tablaModelo);
        this.tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //Cargar listado de pacientes
        listar();
    }

    public void listar(){
        this.tablaModelo.setRowCount(0);

        List<EmbarcacionContenedor> contenedores = embarcacionContenedorServicio.listarEmbarcacionContenedor();

        contenedores.forEach(contenedor -> {

            Object[] renglon ={
                    contenedor.getEmbarcacion(),
                    contenedor.getContenedor()
            };
            tablaModelo.addRow(renglon);
        });
    }

    public void inicializarContenedores(){
        List<Contenedor> contenedores = contenedorServicio.listarContenedores();

        contenedoresComboBox.removeAllItems();

        contenedores.forEach(contenedor -> {
            contenedoresComboBox.addItem(contenedor);
        });
    }

    public void inicializarEmbarcaciones(){
        List<Embarcacion> embarcaciones = embarcacionServicio.listarEmbarcacion();

        embarcacionesComboBox.removeAllItems();

        embarcaciones.forEach(embarcacion ->{
            embarcacionesComboBox.addItem(embarcacion);
        });
    }

    private void guardar(){
        if(contenedoresComboBox.getItemCount() < 0){
            JOptionPane.showMessageDialog(this,"No existen registros de contenedores");
            return;
        }

        if(embarcacionesComboBox.getItemCount() < 0){
            JOptionPane.showMessageDialog(this,"No existen registros de embarcaciones");
            return;
        }

        Contenedor contenedor = (Contenedor) contenedoresComboBox.getSelectedItem();
        Embarcacion embarcacion = (Embarcacion) embarcacionesComboBox.getSelectedItem();

        if(embarcacion ==  null || contenedor == null) return;

        embarcacionContenedor.setContenedor(contenedor);
        embarcacionContenedor.setEmbarcacion(embarcacion);

        EmbarcacionContenedorId embarcacionContenedorId = new EmbarcacionContenedorId(embarcacion.getIdEmbarcacion(),contenedor.getIdContenedor());
        embarcacionContenedor.setId(embarcacionContenedorId);

        embarcacionContenedorServicio.guardar(embarcacionContenedor);

        limpiar();
        listar();
    }

    private void cargarSeleccion(){
        var renglon = tabla.getSelectedRow();

        if(renglon == -1) return;

        Embarcacion embarcacion = (Embarcacion) tabla.getModel().getValueAt(renglon,0);
        Contenedor contenedor = (Contenedor) tabla.getModel().getValueAt(renglon,1);

        embarcacionesComboBox.setSelectedItem(embarcacion);
        contenedoresComboBox.setSelectedItem(contenedor);

        var id = new EmbarcacionContenedorId(embarcacion.getIdEmbarcacion(),contenedor.getIdContenedor());
        embarcacionContenedor.setId(id);
    }

    private Boolean verificarSeleccion(){
        if(embarcacionContenedor.getId() == null){
            JOptionPane.showMessageDialog(this,"Selecciona un registro antes de continuar");
            return false;
        }
        return true;
    }

    private void eliminar(){
        embarcacionContenedorServicio.eliminar(embarcacionContenedor);

        limpiar();
        listar();
    }

    private void limpiar(){
        embarcacionContenedor =  new EmbarcacionContenedor();
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

        embarcacionesComboBox = new javax.swing.JComboBox<>();
        contenedoresComboBox = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        regresarButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btnMain = new javax.swing.JLabel();
        btnLogin = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        guardarButton = new javax.swing.JButton();
        editarButton = new javax.swing.JButton();
        eliminarButton = new javax.swing.JButton();
        limpairButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setText("Embarcacion");

        jLabel3.setText("Contenedor");

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabla);

        regresarButton.setText("Regresar");
        regresarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regresarButtonActionPerformed(evt);
            }
        });

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

        jLabel6.setFont(new java.awt.Font("Arial Black", 0, 48)); // NOI18N
        jLabel6.setText("CONTENEDORES");

        guardarButton.setText("Guardar");
        guardarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarButtonActionPerformed(evt);
            }
        });

        editarButton.setText("Modificar");
        editarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editarButtonActionPerformed(evt);
            }
        });

        eliminarButton.setText("Eliminar");
        eliminarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarButtonActionPerformed(evt);
            }
        });

        limpairButton.setText("Limpiar");
        limpairButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limpairButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(regresarButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(441, 441, 441))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1122, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(37, 37, 37)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(embarcacionesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(73, 73, 73)
                                        .addComponent(guardarButton)
                                        .addGap(18, 18, 18)
                                        .addComponent(editarButton)
                                        .addGap(18, 18, 18)
                                        .addComponent(eliminarButton)
                                        .addGap(18, 18, 18)
                                        .addComponent(limpairButton))
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(contenedoresComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3))
                                .addGap(31, 31, 31)))
                        .addGap(109, 109, 109))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23)
                        .addComponent(jLabel6))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addComponent(regresarButton)))
                .addGap(54, 54, 54)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(contenedoresComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(embarcacionesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(guardarButton)
                    .addComponent(editarButton)
                    .addComponent(eliminarButton)
                    .addComponent(limpairButton))
                .addGap(29, 29, 29)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(57, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void regresarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regresarButtonActionPerformed
        regresar();
    }//GEN-LAST:event_regresarButtonActionPerformed

    private void btnMainMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMainMouseClicked
        regresar();
    }//GEN-LAST:event_btnMainMouseClicked

    private void btnLoginMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLoginMouseClicked
        VistaLogin login = new VistaLogin();  // Crear instancia
        login.setVisible(true);               // Mostrar nueva ventana
        this.setVisible(false);               // Ocultar la actual
    }//GEN-LAST:event_btnLoginMouseClicked

    private void guardarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarButtonActionPerformed
        guardar();
    }//GEN-LAST:event_guardarButtonActionPerformed

    private void editarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editarButtonActionPerformed
        if(verificarSeleccion()){
            eliminar();
            guardar();
        }
    }//GEN-LAST:event_editarButtonActionPerformed

    private void eliminarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarButtonActionPerformed
        if(verificarSeleccion()) eliminar();
    }//GEN-LAST:event_eliminarButtonActionPerformed

    private void limpairButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_limpairButtonActionPerformed
        limpiar();
    }//GEN-LAST:event_limpairButtonActionPerformed

    private void tablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaMouseClicked
        cargarSeleccion();
    }//GEN-LAST:event_tablaMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnLogin;
    private javax.swing.JLabel btnMain;
    private javax.swing.JComboBox<Contenedor> contenedoresComboBox;
    private javax.swing.JButton editarButton;
    private javax.swing.JButton eliminarButton;
    private javax.swing.JComboBox<Embarcacion> embarcacionesComboBox;
    private javax.swing.JButton guardarButton;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton limpairButton;
    private javax.swing.JButton regresarButton;
    private javax.swing.JTable tabla;
    // End of variables declaration//GEN-END:variables
}
