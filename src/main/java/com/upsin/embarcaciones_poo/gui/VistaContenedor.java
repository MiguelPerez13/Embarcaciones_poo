/*
 * Click nbfs:
 * Click nbfs:
 */
package com.upsin.embarcaciones_poo.gui;

import com.upsin.embarcaciones_poo.modelo.Almacen;
import com.upsin.embarcaciones_poo.modelo.Contenedor;
import com.upsin.embarcaciones_poo.modelo.Empresa;
import com.upsin.embarcaciones_poo.servicio.ContenedorServicio;
import com.upsin.embarcaciones_poo.servicio.EmpresaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

@Component
public class VistaContenedor extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VistaContenedor.class.getName());
    private VistaMain vistaMain;
    private ContenedorServicio contenedorServicio;
    private EmpresaServicio empresaServicio;
    private DefaultTableModel tablaModelo;
    private Contenedor contenedor;
    private Integer permiso;

    @Autowired
    public VistaContenedor(ContenedorServicio contenedorServicio, EmpresaServicio empresaServicio) {
        this.contenedorServicio = contenedorServicio;
        this.empresaServicio = empresaServicio;
        initComponents();
        iniciarTabla();
        inicializarComboBox();
        personalizarTablaEmbarcaciones(tabla);
        contenedor = new Contenedor();
    }

    public void setPermiso(Integer permiso){
        this.permiso = permiso;
    }

    public void setVistaMain(VistaMain vistaMain) {
        this.vistaMain = vistaMain;
    }

    public void iniciarTabla(){

        this.tablaModelo = new DefaultTableModel(0, 5){
            @Override
            public boolean isCellEditable(int row,int column){return false;}
        };

        String[] nombresColumnas = {"ID", "Empresa", "Unidad de medida","Peso total","Serie"};
        this.tablaModelo.setColumnIdentifiers(nombresColumnas);
        this.tabla.setModel(tablaModelo);
        this.tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        
        listar();
    }

    private void personalizarTablaEmbarcaciones(JTable tabla) {
        
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

    private void personalizarTablaEmbarcaciones(JScrollPane scrollPane) {
        JTable tabla = (JTable) scrollPane.getViewport().getView();

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

    public void listar(){
        this.tablaModelo.setRowCount(0);

        List<Contenedor> contenedores = contenedorServicio.listarContenedores();

        contenedores.forEach(contenedor -> {

            Object[] renglon ={
                    contenedor.getIdContenedor(),
                    (contenedor.getEmpresa() == null) ? "Empresa no seleccionada" : contenedor.getEmpresa().getNombre(),
                    contenedor.getUnidadMedida(),
                    contenedor.getPesoTotal(),
                    contenedor.getObservaciones()
            };
            tablaModelo.addRow(renglon);
        });
    }

    public void regresar(){
        this.setVisible(false);
        vistaMain.setVisible(true);
    }

    public void inicializarComboBox(){
        empresaComboBox.removeAllItems();

        List<Empresa> empresas = empresaServicio.listarEmpresa();

        empresas.forEach(empresa ->{
            empresaComboBox.addItem(empresa);
        });
    }

    public void guardar() {

        float unidadMedida = 0f;
        float peso = 0f;
        String observaciones = observacionesField.getText();

        if (empresaComboBox.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "⚠️ No hay empresas registradas. No se puede guardar el contenedor.");
            return; 
        }

        if(empresaComboBox.getSelectedItem() == null){
            JOptionPane.showMessageDialog(this,"Seleccione una empresa para continuar");
            return;
        }

        Empresa empresa = (Empresa) empresaComboBox.getSelectedItem();

        try {
            unidadMedida = Float.parseFloat(medidaField.getText());
            if(unidadMedida<0){
                JOptionPane.showMessageDialog(this,"El numero no puede ser negativo");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Digite un número válido en el campo de unidad de medida");
            return;
        }

        try {
            peso = Float.parseFloat(pesoField.getText());
            if(peso<0){
                JOptionPane.showMessageDialog(this,"El numero no puede ser negativo");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Digite un número válido en el campo de peso");
            return;
        }

        if (observaciones.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Rellene todos los campos antes de continuar");
            return;
        }

        if (empresa == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una empresa");
            return;
        }


        contenedor.setEmpresa(empresa);
        contenedor.setUnidadMedida(unidadMedida);
        contenedor.setPesoTotal(peso);
        contenedor.setObservaciones(observaciones);

        contenedorServicio.guardarContenedor(contenedor);

        limpiar();
        listar();

    }

    public void cargarSeleccion(){
        var renglon = tabla.getSelectedRow();
        Integer id = (Integer) tabla.getModel().getValueAt(renglon,0);

        this.contenedor = contenedorServicio.buscarContendorPorId(id);

        empresaComboBox.setSelectedItem(contenedor.getEmpresa());
        medidaField.setText(String.valueOf(contenedor.getUnidadMedida()));
        pesoField.setText(String.valueOf(contenedor.getPesoTotal()));
        observacionesField.setText(contenedor.getObservaciones());
    }

    public boolean verificarSeleccion(){
        if(contenedor.getIdContenedor() != null){
            return true;
        }else{
            JOptionPane.showMessageDialog(this,"Seleccione un registro de la tabla antes de continuar");
            return false;
        }
    }

    public void eliminar(){
        contenedorServicio.eliminarContenedor(contenedor);

        limpiar();
        listar();
    }

    public void limpiar(){
        medidaField.setText("");
        pesoField.setText("");
        observacionesField.setText("");

        contenedor = new Contenedor();
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
    
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        empresaComboBox = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        medidaField = new javax.swing.JTextField();
        pesoField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        observacionesField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        btnMain = new javax.swing.JLabel();
        btnLogin = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        limpiarButton = new javax.swing.JButton();
        guardarButton = new javax.swing.JButton();
        editarButton = new javax.swing.JButton();
        eliminarButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); 
        jLabel2.setText("Empresa:");

        empresaComboBox.setBackground(new java.awt.Color(255, 255, 255));
        empresaComboBox.setFont(new java.awt.Font("Segoe UI", 0, 16)); 
        empresaComboBox.setForeground(new java.awt.Color(0, 0, 0));

        jLabel3.setFont(new java.awt.Font("Arial", 1, 18)); 
        jLabel3.setText("Volumen:");

        jLabel4.setFont(new java.awt.Font("Arial", 1, 18)); 
        jLabel4.setText("Peso total:");

        medidaField.setBackground(new java.awt.Color(255, 255, 255));
        medidaField.setFont(new java.awt.Font("Segoe UI", 0, 16)); 
        medidaField.setForeground(new java.awt.Color(0, 0, 0));

        pesoField.setBackground(new java.awt.Color(255, 255, 255));
        pesoField.setFont(new java.awt.Font("Segoe UI", 0, 16)); 
        pesoField.setForeground(new java.awt.Color(0, 0, 0));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 18)); 
        jLabel5.setText("Serie:");
        jLabel5.setToolTipText("");

        observacionesField.setBackground(new java.awt.Color(255, 255, 255));
        observacionesField.setFont(new java.awt.Font("Segoe UI", 0, 16)); 
        observacionesField.setForeground(new java.awt.Color(0, 0, 0));

        jScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseClicked(evt);
            }
        });

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabla);

        jPanel1.setBackground(new java.awt.Color(102, 204, 255));
        jPanel1.setForeground(new java.awt.Color(102, 204, 255));

        btnMain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/LogoFInal.png"))); 
        btnMain.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnMainMouseClicked(evt);
            }
        });

        btnLogin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/icono_CerrarSesion.png"))); 
        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLoginMouseClicked(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Arial Black", 0, 30)); 
        jLabel6.setForeground(new java.awt.Color(0, 0, 51));
        jLabel6.setText("CONTENEDORES");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(btnMain)
                .addGap(381, 381, 381)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnLogin)
                .addGap(62, 62, 62))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(btnLogin)
                    .addComponent(btnMain))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        limpiarButton.setBackground(new java.awt.Color(0, 133, 189));
        limpiarButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        limpiarButton.setForeground(new java.awt.Color(255, 255, 255));
        limpiarButton.setText("LIMPIAR");
        limpiarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limpiarButtonActionPerformed(evt);
            }
        });

        guardarButton.setBackground(new java.awt.Color(0, 133, 189));
        guardarButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        guardarButton.setForeground(new java.awt.Color(255, 255, 255));
        guardarButton.setText("GUARDAR");
        guardarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarButtonActionPerformed(evt);
            }
        });

        editarButton.setBackground(new java.awt.Color(0, 133, 189));
        editarButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        editarButton.setForeground(new java.awt.Color(255, 255, 255));
        editarButton.setText("MODIFICAR");
        editarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editarButtonActionPerformed(evt);
            }
        });

        eliminarButton.setBackground(new java.awt.Color(0, 133, 189));
        eliminarButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        eliminarButton.setForeground(new java.awt.Color(255, 255, 255));
        eliminarButton.setText("ELIMINAR");
        eliminarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(medidaField)
                    .addComponent(observacionesField)
                    .addComponent(pesoField)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(guardarButton)
                                .addGap(18, 18, 18)
                                .addComponent(editarButton)
                                .addGap(18, 18, 18)
                                .addComponent(limpiarButton)
                                .addGap(18, 18, 18)
                                .addComponent(eliminarButton)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(empresaComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(89, 89, 89)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 788, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(69, 69, 69))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(empresaComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(medidaField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(pesoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(observacionesField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 91, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(guardarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(editarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(limpiarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(eliminarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1))
                .addGap(138, 138, 138))
        );

        pack();
    }

    private void btnMainMouseClicked(java.awt.event.MouseEvent evt) {
        regresar();
    }

    private void btnLoginMouseClicked(java.awt.event.MouseEvent evt) {
        setVisible(false);
        vistaMain.volverLogin();
    }

    private void limpiarButtonActionPerformed(java.awt.event.ActionEvent evt) {
        limpiar();
    }

    private void guardarButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if(verificarPermisos(2)){
            contenedor = new Contenedor();
            guardar();
        }
    }

    private void editarButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if(verificarPermisos(2)){
            if (verificarSeleccion()) {
                guardar();
            }
        }
    }

    private void eliminarButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if(verificarPermisos(3)){
            if (verificarSeleccion()) {
                eliminar();
            }
        }
    }

    private void jScrollPane1MouseClicked(java.awt.event.MouseEvent evt) {

    }

    private void tablaMouseClicked(java.awt.event.MouseEvent evt) {
        cargarSeleccion();
    }


    
    private javax.swing.JLabel btnLogin;
    private javax.swing.JLabel btnMain;
    private javax.swing.JButton editarButton;
    private javax.swing.JButton eliminarButton;
    private javax.swing.JComboBox<Empresa> empresaComboBox;
    private javax.swing.JButton guardarButton;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton limpiarButton;
    private javax.swing.JTextField medidaField;
    private javax.swing.JTextField observacionesField;
    private javax.swing.JTextField pesoField;
    private javax.swing.JTable tabla;
    
}
