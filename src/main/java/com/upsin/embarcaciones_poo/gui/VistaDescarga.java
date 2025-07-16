/*
 * Click nbfs:
 * Click nbfs:
 */
package com.upsin.embarcaciones_poo.gui;

import com.formdev.flatlaf.json.ParseException;
import com.upsin.embarcaciones_poo.modelo.*;
import com.upsin.embarcaciones_poo.servicio.ContenedorServicio;
import com.upsin.embarcaciones_poo.servicio.EmbarcacionServicio;
import com.upsin.embarcaciones_poo.servicio.EmbarcacionDescargaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class VistaDescarga extends javax.swing.JFrame {

    private VistaMain vistaMain;
    private EmbarcacionDescargaServicio embarcacionDescargaServicio;
    private EmbarcacionServicio embarcacionServicio;
    private ContenedorServicio contenedorServicio;
    private EmbarcacionDescarga embarcacionDescarga;
    private DefaultTableModel tablaModelo;
    private Integer permiso;


    public VistaDescarga(EmbarcacionDescargaServicio eds, EmbarcacionServicio es, ContenedorServicio cs) {
        this.embarcacionDescargaServicio = eds;
        this.embarcacionServicio = es;
        this.contenedorServicio = cs;
        embarcacionDescarga = new EmbarcacionDescarga();
        initComponents();
        personalizarTablaEmbarcaciones();
        inicializarEmbarcaciones();
        inicializarContenedores();
        iniciarTabla();

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccion();
            }
        });


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
        
        this.tablaModelo = new DefaultTableModel(0, 4){
            @Override
            public boolean isCellEditable(int row,int column){return false;}
        };

        String[] nombresColumnas = {"Embarcacion","Contenedor","Fecha Descarga","Lote Almacen"};
        this.tablaModelo.setColumnIdentifiers(nombresColumnas);
        this.tabla.setModel(tablaModelo);
        this.tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        listar();
    }

    public void listar(){
        this.tablaModelo.setRowCount(0);
        List<EmbarcacionDescarga> descargas = embarcacionDescargaServicio.listarEmbarcacionDescarga();

        for (EmbarcacionDescarga descarga : descargas) {
            Object[] renglon = {
                    descarga.getEmbarcacion(),
                    descarga.getContenedor(),
                    descarga.getFechaDescarga(),
                    descarga.getLoteAlmacen()
            };
            tablaModelo.addRow(renglon);
        }
    }


    public void inicializarContenedores(){
        List<Contenedor> contenedores = contenedorServicio.listarContenedores();

        ContComboBox.removeAllItems();

        contenedores.forEach(contenedor -> {
            ContComboBox.addItem(contenedor);
        });
    }

    private Boolean verificarSeleccion(){
        if(embarcacionDescarga.getId() == null){
            JOptionPane.showMessageDialog(this,"Selecciona un registro antes de continuar");
            return false;
        }
        return true;
    }

    public void inicializarEmbarcaciones(){
        List<Embarcacion> embarcaciones = embarcacionServicio.listarEmbarcacion();

        EmbComboBox.removeAllItems();

        embarcaciones.forEach(embarcacion ->{
            EmbComboBox.addItem(embarcacion);
        });
    }

    private void guardar() {
        
        if (ContComboBox.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "No existen registros de contenedores");
            return;
        }

        if (EmbComboBox.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "No existen registros de embarcaciones");
            return;
        }

        
        Contenedor contenedor = (Contenedor) ContComboBox.getSelectedItem();
        Embarcacion embarcacion = (Embarcacion) EmbComboBox.getSelectedItem();

        if (embarcacion == null || contenedor == null) return;

        
        Date fecha = FechaDate.getDate();
        String lote = LoteText.getText().trim();

        if (fecha == null || lote.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar fecha y lote");
            return;
        }

        
        EmbarcacionDescargaId id = new EmbarcacionDescargaId(
                embarcacion.getIdEmbarcacion(),
                contenedor.getIdContenedor()
        );

        
        embarcacionDescarga.setId(id);
        embarcacionDescarga.setEmbarcacion(embarcacion);
        embarcacionDescarga.setContenedor(contenedor);
        embarcacionDescarga.setFechaDescarga(fecha);
        embarcacionDescarga.setLoteAlmacen(lote);

        
        embarcacionDescargaServicio.guardar(embarcacionDescarga);

        JOptionPane.showMessageDialog(this, "Registro guardado correctamente");

        limpiar();
        listar();
    }


    private void cargarSeleccion() {
        int renglon = tabla.getSelectedRow();
        if (renglon == -1) return;

        Embarcacion embarcacion = (Embarcacion) tabla.getModel().getValueAt(renglon, 0);
        Contenedor contenedor = (Contenedor) tabla.getModel().getValueAt(renglon, 1);
        Date fechaDescarga = (Date) tabla.getModel().getValueAt(renglon, 2);
        String lote = (String) tabla.getModel().getValueAt(renglon, 3);
        if (lote == null) lote = "";

        EmbarcacionDescargaId id = new EmbarcacionDescargaId(
                embarcacion.getIdEmbarcacion(),
                contenedor.getIdContenedor()
        );

        embarcacionDescarga.setId(id);
        embarcacionDescarga.setEmbarcacion(embarcacion);
        embarcacionDescarga.setContenedor(contenedor);
        embarcacionDescarga.setFechaDescarga(fechaDescarga);
        embarcacionDescarga.setLoteAlmacen(lote);

        EmbComboBox.setSelectedItem(embarcacion);
        ContComboBox.setSelectedItem(contenedor);
        FechaDate.setDate(fechaDescarga);
        LoteText.setText(lote);
    }

    private void limpiar() {
        embarcacionDescarga = new EmbarcacionDescarga();
        EmbComboBox.setSelectedIndex(-1);
        ContComboBox.setSelectedIndex(-1);
        FechaDate.setDate(null);
        LoteText.setText("");
        tabla.clearSelection();
    }

    private void eliminar(){
        embarcacionDescargaServicio.eliminar(embarcacionDescarga);
        limpiar();
        listar();
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

        jPanel1 = new javax.swing.JPanel();
        btnMain = new javax.swing.JLabel();
        btnLogin = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        EmbComboBox = new javax.swing.JComboBox<>();
        ContComboBox = new javax.swing.JComboBox<>();
        EmbLabel = new javax.swing.JLabel();
        ContLabel = new javax.swing.JLabel();
        FechaLabel = new javax.swing.JLabel();
        FechaDate = new com.toedter.calendar.JDateChooser();
        LoteLabel = new javax.swing.JLabel();
        LoteText = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        btnEliminar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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

        jLabel2.setFont(new java.awt.Font("Arial Black", 0, 30)); 
        jLabel2.setForeground(new java.awt.Color(0, 0, 51));
        jLabel2.setText("ALMACEN");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(btnMain)
                .addGap(401, 401, 401)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 494, Short.MAX_VALUE)
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

        EmbComboBox.setBackground(new java.awt.Color(255, 255, 255));
        EmbComboBox.setFont(new java.awt.Font("Segoe UI", 0, 16)); 
        EmbComboBox.setForeground(new java.awt.Color(0, 0, 0));

        ContComboBox.setBackground(new java.awt.Color(255, 255, 255));
        ContComboBox.setFont(new java.awt.Font("Segoe UI", 0, 16)); 
        ContComboBox.setForeground(new java.awt.Color(0, 0, 0));

        EmbLabel.setFont(new java.awt.Font("Arial", 1, 18)); 
        EmbLabel.setText("Embarcacion:");

        ContLabel.setFont(new java.awt.Font("Arial", 1, 18)); 
        ContLabel.setText("Contenedor:");

        FechaLabel.setFont(new java.awt.Font("Arial", 1, 18)); 
        FechaLabel.setText("Fecha Descarga:");

        FechaDate.setFont(new java.awt.Font("Segoe UI", 0, 16)); 

        LoteLabel.setFont(new java.awt.Font("Arial", 1, 18)); 
        LoteLabel.setText("Lote Almacen:");

        LoteText.setFont(new java.awt.Font("Segoe UI", 0, 16)); 

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
        ));
        jScrollPane1.setViewportView(tabla);

        btnEliminar.setBackground(new java.awt.Color(0, 133, 189));
        btnEliminar.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        btnEliminar.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminar.setText("ELIMINAR");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnModificar.setBackground(new java.awt.Color(0, 133, 189));
        btnModificar.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        btnModificar.setForeground(new java.awt.Color(255, 255, 255));
        btnModificar.setText("MODIFICAR");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnLimpiar.setBackground(new java.awt.Color(0, 133, 189));
        btnLimpiar.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        btnLimpiar.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiar.setText("LIMPIAR");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        btnGuardar.setBackground(new java.awt.Color(0, 133, 189));
        btnGuardar.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setText("GUARDAR");
        btnGuardar.setMaximumSize(new java.awt.Dimension(84, 27));
        btnGuardar.setMinimumSize(new java.awt.Dimension(84, 27));
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
                .addGap(54, 54, 54)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(ContComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(FechaDate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(LoteText, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(LoteLabel)
                                .addComponent(FechaLabel)
                                .addComponent(EmbLabel)
                                .addComponent(ContLabel))
                            .addGap(270, 270, 270))
                        .addComponent(EmbComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnModificar)
                        .addGap(18, 18, 18)
                        .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 767, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(EmbLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(EmbComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(ContLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ContComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(48, 48, 48)
                        .addComponent(FechaLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(FechaDate, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addComponent(LoteLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(LoteText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 505, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 84, Short.MAX_VALUE))
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

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {
        if(verificarPermisos(3)){
            if (verificarSeleccion()) {
                eliminar();
            }
        }
    }

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {
        if(verificarPermisos(1)){
            if (verificarSeleccion()) {
                guardar();
            }
        }
    }

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {
           limpiar();
    }

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {
        if(verificarPermisos(1)){
            embarcacionDescarga = new EmbarcacionDescarga();
            guardar();
        }
    }

    
    private javax.swing.JComboBox<Contenedor> ContComboBox;
    private javax.swing.JLabel ContLabel;
    private javax.swing.JComboBox<Embarcacion> EmbComboBox;
    private javax.swing.JLabel EmbLabel;
    private com.toedter.calendar.JDateChooser FechaDate;
    private javax.swing.JLabel FechaLabel;
    private javax.swing.JLabel LoteLabel;
    private javax.swing.JTextField LoteText;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JLabel btnLogin;
    private javax.swing.JLabel btnMain;
    private javax.swing.JButton btnModificar;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabla;
    
}
