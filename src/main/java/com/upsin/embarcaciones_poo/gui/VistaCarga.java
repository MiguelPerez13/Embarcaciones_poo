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
    
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        embarcacionComboBox = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        almacenComboBox = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        regresarButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btnMain = new javax.swing.JLabel();
        btnLogin = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        fecha = new com.toedter.calendar.JDateChooser();
        guardarButton = new javax.swing.JButton();
        modificarButton = new javax.swing.JButton();
        elminarButton = new javax.swing.JButton();
        limpiarButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel3.setText("Embarcacion");

        jLabel4.setText("Almacen");

        jLabel5.setText("Fecha de carga");

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
        jScrollPane2.setViewportView(tabla);

        regresarButton.setText("Regresar");
        regresarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regresarButtonActionPerformed(evt);
            }
        });

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
        jLabel6.setText("CARGA Y DESCARGA");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(btnMain)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 446, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addGap(329, 329, 329)
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

        guardarButton.setText("Guardar");
        guardarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarButtonActionPerformed(evt);
            }
        });

        modificarButton.setText("Modificar");
        modificarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificarButtonActionPerformed(evt);
            }
        });

        elminarButton.setText("Eliminar");
        elminarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                elminarButtonActionPerformed(evt);
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
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(114, 114, 114)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(almacenComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(embarcacionComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(fecha, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addComponent(guardarButton)
                        .addGap(38, 38, 38)
                        .addComponent(modificarButton)
                        .addGap(33, 33, 33)
                        .addComponent(elminarButton)
                        .addGap(30, 30, 30)
                        .addComponent(limpiarButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 680, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(125, 125, 125))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(regresarButton)
                .addGap(46, 46, 46))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(regresarButton)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(embarcacionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(almacenComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(90, 90, 90)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(guardarButton)
                            .addComponent(modificarButton)
                            .addComponent(elminarButton)
                            .addComponent(limpiarButton)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 473, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(86, Short.MAX_VALUE))
        );

        pack();
    }

    private void regresarButtonActionPerformed(java.awt.event.ActionEvent evt) {
        regresar();
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
            embarcacionCarga = new EmbarcacionCarga();
            guardar();
        }
    }

    private void modificarButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if(verificarPermisos(2)){
            if (verificarSeleccion()) {
                guardar();
            }
        }
    }

    private void elminarButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if(verificarPermisos(3)){
            if (verificarSeleccion()) {
                eliminar();
            }
        }
    }

    private void limpiarButtonActionPerformed(java.awt.event.ActionEvent evt) {
        limpiar();
    }

    private void tablaMouseClicked(java.awt.event.MouseEvent evt) {
        cargarSeleccion();
    }


    
    private javax.swing.JComboBox<Almacen> almacenComboBox;
    private javax.swing.JLabel btnLogin;
    private javax.swing.JLabel btnMain;
    private javax.swing.JButton elminarButton;
    private javax.swing.JComboBox<Embarcacion> embarcacionComboBox;
    private com.toedter.calendar.JDateChooser fecha;
    private javax.swing.JButton guardarButton;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton limpiarButton;
    private javax.swing.JButton modificarButton;
    private javax.swing.JButton regresarButton;
    private javax.swing.JTable tabla;
    
}
