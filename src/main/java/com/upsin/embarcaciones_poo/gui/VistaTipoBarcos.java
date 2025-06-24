package com.upsin.embarcaciones_poo.gui;
import com.upsin.embarcaciones_poo.modelo.TipoBarco;
import com.upsin.embarcaciones_poo.servicio.TipoBarcoServicio;
import java.util.List;

import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VistaTipoBarcos extends javax.swing.JFrame {

    private TipoBarcoServicio tipoBarcoServicio;
    private DefaultTableModel tablaModelo;
    private TipoBarco tipoBarco;
    private VistaMain vistaMain;
    
    // constructor de la vista con @Autowired
    @Autowired
    public VistaTipoBarcos(TipoBarcoServicio tipoBarcoServicio) {
        this.tipoBarcoServicio = tipoBarcoServicio;
        initComponents();
        iniciarTabla();
    }    
    
    // setter de vista main
    public void setVistaMain(VistaMain vistaMain) {
        this.vistaMain = vistaMain;
    }
    
    // inicializar la tabla con el formato de la tabla tipo barcos
    public void iniciarTabla(){
            // evitar la edicion de tablas
        this.tablaModelo = new DefaultTableModel(0, 3){
            @Override
            public boolean isCellEditable(int row,int column){return false;}
        };

        String[] nombresColumnas = {"ID", "Nombre", "Descripcion"};
        this.tablaModelo.setColumnIdentifiers(nombresColumnas);
        this.tabla.setModel(tablaModelo);
        this.tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //Cargar listado de pacientes
        listar();
    }

    // listar los tipos de barcos de la base de datos
    private void listar(){
        this.tablaModelo.setRowCount(0);
        List<TipoBarco> tiposBarcos = tipoBarcoServicio.listarBarcos();
        tiposBarcos.forEach(tipoBarco -> {
            Object[] renglon = {
                tipoBarco.getIdTipoBarco(),
                tipoBarco.getNombreTipo(),
                tipoBarco.getDescripcion()
            };
            this.tablaModelo.addRow(renglon);
        });
    }
    
    //guarda el registro en la base de datos, en caso de que la variable tipoBarco  
    //tenga un id, en vez de guardar va a editar ese registro
    public void guardar(){      
        String nombre = nombreLabel.getText();
        String descripcion = descripcionLabel.getText();
        
        if( true ){
            //aca irian las validaciones          
            tipoBarco.setNombreTipo(nombre);
            tipoBarco.setDescripcion(descripcion);
            
            tipoBarcoServicio.guardarTipoBarco(tipoBarco);
        }else{
            //aca si no se se validan
            System.out.println("hola");
        }
        listar();
        limpiar();
    }
    
    //carga el registro seleccionado de la tabla para su edicion o eliminacion
    public void cargarSeleccion(){
        var renglon = tabla.getSelectedRow();
        Integer id = (Integer) tabla.getModel().getValueAt(renglon, 0);
        
        this.tipoBarco = tipoBarcoServicio.bucarTipobarcoId(id);
        
        nombreLabel.setText(tipoBarco.getNombreTipo());
        descripcionLabel.setText(tipoBarco.getDescripcion());
    }
    
    //elimina el registro seleccionado
    public void eliminar(){
        tipoBarcoServicio.eliminarTipoBarco(tipoBarco);
    }
    
    //limpia el formulatio y la variable tipoBarco
    public void limpiar(){
        nombreLabel.setText("");
        descripcionLabel.setText("");
        this.tipoBarco=null;
    }
    
    //regresa a la vista main
    public void regresar(){
        this.setVisible(false);
        vistaMain.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        nombreLabel = new javax.swing.JTextField();
        descripcionLabel = new javax.swing.JTextField();
        guardatButton = new javax.swing.JButton();
        salirButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        eliminarButton = new javax.swing.JButton();
        editarButton = new javax.swing.JButton();
        limpiarButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setForeground(new java.awt.Color(0, 102, 102));
        jLabel1.setText("Mar Azul Embaraciones");

        jLabel2.setFont(new java.awt.Font("Arial Black", 0, 24)); // NOI18N
        jLabel2.setText("Registro Tipos de Barco");

        jLabel4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel4.setText("Nombre:");

        jLabel5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel5.setText("Descripcion:");

        descripcionLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                descripcionLabelActionPerformed(evt);
            }
        });

        guardatButton.setText("Guardar");
        guardatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardatButtonActionPerformed(evt);
            }
        });

        salirButton.setText("Salir");
        salirButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salirButtonActionPerformed(evt);
            }
        });

        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabla);

        eliminarButton.setText("Eliminar");
        eliminarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarButtonActionPerformed(evt);
            }
        });

        editarButton.setText("Editar");
        editarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editarButtonActionPerformed(evt);
            }
        });

        limpiarButton.setText("Limpiar");
        limpiarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limpiarButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(guardatButton)
                        .addGap(18, 18, 18)
                        .addComponent(editarButton)
                        .addGap(18, 18, 18)
                        .addComponent(eliminarButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(limpiarButton)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(nombreLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(descripcionLabel)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 547, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(73, 73, 73))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(258, 258, 258)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(salirButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(15, 15, 15)
                .addComponent(limpiarButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nombreLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(descripcionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(guardatButton)
                            .addComponent(editarButton)
                            .addComponent(eliminarButton))))
                .addGap(46, 46, 46)
                .addComponent(salirButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void descripcionLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_descripcionLabelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_descripcionLabelActionPerformed

    private void guardatButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardatButtonActionPerformed
        this.tipoBarco = new TipoBarco(null,"","");
        guardar();
    }//GEN-LAST:event_guardatButtonActionPerformed

    private void eliminarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarButtonActionPerformed
        eliminar();
        limpiar();
        listar();
    }//GEN-LAST:event_eliminarButtonActionPerformed

    private void editarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editarButtonActionPerformed
        guardar();
    }//GEN-LAST:event_editarButtonActionPerformed

    private void limpiarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_limpiarButtonActionPerformed
        regresar();
    }//GEN-LAST:event_limpiarButtonActionPerformed

    private void tablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaMouseClicked
        cargarSeleccion();
    }//GEN-LAST:event_tablaMouseClicked

    private void salirButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salirButtonActionPerformed
        regresar();
    }//GEN-LAST:event_salirButtonActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField descripcionLabel;
    private javax.swing.JButton editarButton;
    private javax.swing.JButton eliminarButton;
    private javax.swing.JButton guardatButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton limpiarButton;
    private javax.swing.JTextField nombreLabel;
    private javax.swing.JButton salirButton;
    private javax.swing.JTable tabla;
    // End of variables declaration//GEN-END:variables
}
