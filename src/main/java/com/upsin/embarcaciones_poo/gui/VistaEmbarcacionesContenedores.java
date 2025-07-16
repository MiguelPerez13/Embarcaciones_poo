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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
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
    private Integer permiso;

    
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
        personalizarTablaEmbarcaciones();
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

    public void setPermiso(Integer permiso){
        this.permiso = permiso;
    }

    private boolean verificarPermisos(Integer nivel) {
        if (permiso == nivel || permiso == 3) {
            return true;
        } else {
            JOptionPane.showMessageDialog(this, "No tienes permisos para realizar la operacion");
            return false;
        }
    }


        public void iniciarTabla(){
        
        this.tablaModelo = new DefaultTableModel(0, 2){
            @Override
            public boolean isCellEditable(int row,int column){return false;}
        };

        String[] nombresColumnas = {"Embarcacion","Contenedor"};
        this.tablaModelo.setColumnIdentifiers(nombresColumnas);
        this.tabla.setModel(tablaModelo);
        this.tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        
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
    
    private void initComponents() {

        embarcacionesComboBox = new javax.swing.JComboBox<>();
        contenedoresComboBox = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        Tabla = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        btnMain = new javax.swing.JLabel();
        btnLogin = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        guardarButton = new javax.swing.JButton();
        editarButton = new javax.swing.JButton();
        eliminarButton = new javax.swing.JButton();
        limpairButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        embarcacionesComboBox.setBackground(new java.awt.Color(255, 255, 255));
        embarcacionesComboBox.setFont(new java.awt.Font("Segoe UI", 0, 16)); 
        embarcacionesComboBox.setForeground(new java.awt.Color(0, 0, 0));

        contenedoresComboBox.setBackground(new java.awt.Color(255, 255, 255));
        contenedoresComboBox.setFont(new java.awt.Font("Segoe UI", 0, 16)); 
        contenedoresComboBox.setForeground(new java.awt.Color(0, 0, 0));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); 
        jLabel2.setText("Embarcacion");

        jLabel3.setFont(new java.awt.Font("Arial", 1, 18)); 
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
        Tabla.setViewportView(tabla);

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
                .addGap(323, 323, 323)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 457, Short.MAX_VALUE)
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
        editarButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); 
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

        limpairButton.setBackground(new java.awt.Color(0, 133, 189));
        limpairButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        limpairButton.setForeground(new java.awt.Color(255, 255, 255));
        limpairButton.setText("LIMPIAR");
        limpairButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limpairButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(embarcacionesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(106, 106, 106)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(contenedoresComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(guardarButton)
                                .addGap(30, 30, 30)
                                .addComponent(editarButton)
                                .addGap(32, 32, 32)
                                .addComponent(eliminarButton)
                                .addGap(28, 28, 28)
                                .addComponent(limpairButton))))
                    .addComponent(Tabla, javax.swing.GroupLayout.PREFERRED_SIZE, 1256, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(80, 80, 80)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(contenedoresComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(guardarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(editarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(eliminarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(limpairButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(embarcacionesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
                .addComponent(Tabla, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66))
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

    private void guardarButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if(verificarPermisos(2)){
            guardar();
        }
    }

    private void editarButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if(verificarPermisos(2)){
            if (verificarSeleccion()) {
                eliminar();
                guardar();
            }
        }
    }

    private void eliminarButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if(verificarPermisos(3)){
            if (verificarSeleccion()) eliminar();
        }
    }

    private void limpairButtonActionPerformed(java.awt.event.ActionEvent evt) {
        limpiar();
    }

    private void tablaMouseClicked(java.awt.event.MouseEvent evt) {
        cargarSeleccion();
    }


    
    private javax.swing.JScrollPane Tabla;
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
    private javax.swing.JButton limpairButton;
    private javax.swing.JTable tabla;
    
}
