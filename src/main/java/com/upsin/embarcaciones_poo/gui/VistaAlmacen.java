package com.upsin.embarcaciones_poo.gui;

import com.upsin.embarcaciones_poo.modelo.*;
import com.upsin.embarcaciones_poo.servicio.AlmacenServicio;
import com.upsin.embarcaciones_poo.servicio.ContenedorServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Date;
import java.util.List;

@Component
public class VistaAlmacen extends javax.swing.JFrame {

    private AlmacenServicio almacenServicio;
    private VistaMain vistaMain;
    private Almacen almacen;
    private ContenedorServicio contenedorServicio;
    private DefaultTableModel tablaModelo;
    
    @Autowired
    public VistaAlmacen(AlmacenServicio almacenServicio, ContenedorServicio contenedorServicio) {
        this.almacenServicio = almacenServicio;
        this.contenedorServicio = contenedorServicio;
        this.almacen = new Almacen();
        initComponents();
        iniciarTabla();
        inicializarContenedores();
    }

   public void setVistaMain(VistaMain vistaMain) {
        this.vistaMain = vistaMain;
    }
   
   public void regresar(){
        this.setVisible(false);
        vistaMain.setVisible(true);
    }

    public void iniciarTabla(){
        // evitar la edicion de tablas
        this.tablaModelo = new DefaultTableModel(0, 2){
            @Override
            public boolean isCellEditable(int row,int column){return false;}
        };

        String[] nombresColumnas = {"Id","Lote Almacen","Contendor","Fecha de llegada","Estado"};

        this.tablaModelo.setColumnIdentifiers(nombresColumnas);
        this.tabla.setModel(tablaModelo);
        this.tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //Cargar listado de pacientes
        listar();
    }

    public void listar(){
        this.tablaModelo.setRowCount(0);

        List<Almacen> almacenes = almacenServicio.listarAlmacen();

        almacenes.forEach(almacen -> {

            Object[] renglon ={
                    almacen.getIdAlmacen(),
                    almacen.getLoteAlmacen(),
                    almacen.getContenedor().toString(),
                    almacen.getFechaLlegada(),
                    almacen.getEstado()
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

    private void guardar(){
        if(contenedoresComboBox.getItemCount() < 0){
            JOptionPane.showMessageDialog(this,"No existen registros de contenedores");
            return;
        }

        Contenedor contenedor = (Contenedor) contenedoresComboBox.getSelectedItem();
        String lote = loteField.getText();
        String  estado = estadoComboBox.getSelectedItem().toString();
        Date date = dateChooser.getDate();
        
        if(lote.isEmpty() || estado.isEmpty() || date==null || contenedor==null){
            JOptionPane.showMessageDialog(this,"Rellene todos los campos para continuar");
            return;
        }

        almacen.setLoteAlmacen(lote);
        almacen.setEstado(estado);
        almacen.setContenedor(contenedor);
        almacen.setFechaLlegada(date);

        almacenServicio.guardar(almacen);

        limpiar();
        listar();
    }

    private void cargarSeleccion(){
        var renglon = tabla.getSelectedRow();

        if(renglon == -1) return;

        Integer id = (Integer) tabla.getModel().getValueAt(renglon,0);
        almacen = almacenServicio.buscarPorId(id);

        loteField.setText(almacen.getLoteAlmacen());
        contenedoresComboBox.setSelectedItem(almacen.getContenedor());
        estadoComboBox.setSelectedItem(almacen.getEstado());
        dateChooser.setDate(almacen.getFechaLlegada());
    }

    private Boolean verificarSeleccion(){
        if(almacen.getIdAlmacen() == null){
            JOptionPane.showMessageDialog(this,"Selecciona un registro antes de continuar");
            return false;
        }
        return true;
    }

    private void eliminar(){
        almacenServicio.eliminar(almacen);

        limpiar();
        listar();
    }

    private void limpiar(){
        almacen =  new Almacen();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        guardarButton = new javax.swing.JButton();
        regresarButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        loteField = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        btnMain = new javax.swing.JLabel();
        btnLogin = new javax.swing.JLabel();
        editarButton = new javax.swing.JButton();
        eliminarButton = new javax.swing.JButton();
        limpiarButton = new javax.swing.JButton();
        contenedoresComboBox = new javax.swing.JComboBox<>();
        estadoComboBox = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        dateChooser = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        guardarButton.setText("Guardar");
        guardarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarButtonActionPerformed(evt);
            }
        });

        regresarButton.setText("Regresar");
        regresarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regresarButtonActionPerformed(evt);
            }
        });

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "id Contenedor", "Lote", "Fecha de Llegada", "Estado"
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

        jLabel2.setFont(new java.awt.Font("Arial Black", 0, 48)); // NOI18N
        jLabel2.setText("ALMACEN");

        jLabel3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel3.setText("Lote");

        jLabel4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel4.setText("Contenedor");

        jLabel5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel5.setText("Estado:");

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

        editarButton.setText("Editar");
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

        limpiarButton.setText("Limpiar");

        estadoComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Almacen", "Enviado" }));

        jLabel6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel6.setText("Fecha de llegada:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(guardarButton)
                        .addGap(26, 26, 26)
                        .addComponent(editarButton)
                        .addGap(26, 26, 26)
                        .addComponent(eliminarButton)
                        .addGap(18, 18, 18)
                        .addComponent(limpiarButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(loteField, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(estadoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(87, 87, 87)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(contenedoresComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, 162, Short.MAX_VALUE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 184, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 658, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(107, 107, 107))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(regresarButton)
                .addGap(432, 432, 432)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23)
                        .addComponent(jLabel2))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(95, 95, 95)
                        .addComponent(regresarButton)))
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(loteField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(contenedoresComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(59, 59, 59)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(estadoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(85, 85, 85)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(guardarButton)
                            .addComponent(editarButton)
                            .addComponent(eliminarButton)
                            .addComponent(limpiarButton))))
                .addGap(0, 150, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void guardarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarButtonActionPerformed
        almacen = new Almacen();
        guardar();
    }//GEN-LAST:event_guardarButtonActionPerformed

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

    private void editarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editarButtonActionPerformed
        if(verificarSeleccion()){
            guardar();
        }
    }//GEN-LAST:event_editarButtonActionPerformed

    private void eliminarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarButtonActionPerformed
        if(verificarSeleccion()){
            eliminar();
        }
    }//GEN-LAST:event_eliminarButtonActionPerformed

    private void tablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaMouseClicked
        cargarSeleccion();
    }//GEN-LAST:event_tablaMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnLogin;
    private javax.swing.JLabel btnMain;
    private javax.swing.JComboBox<Contenedor> contenedoresComboBox;
    private com.toedter.calendar.JDateChooser dateChooser;
    private javax.swing.JButton editarButton;
    private javax.swing.JButton eliminarButton;
    private javax.swing.JComboBox<String> estadoComboBox;
    private javax.swing.JButton guardarButton;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton limpiarButton;
    private javax.swing.JTextField loteField;
    private javax.swing.JButton regresarButton;
    private javax.swing.JTable tabla;
    // End of variables declaration//GEN-END:variables
}
