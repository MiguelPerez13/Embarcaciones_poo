package com.upsin.embarcaciones_poo.gui;


import com.upsin.embarcaciones_poo.modelo.Contenedor;
import com.upsin.embarcaciones_poo.modelo.Producto;
import com.upsin.embarcaciones_poo.servicio.ContenedorServicio;
import com.upsin.embarcaciones_poo.servicio.ProductoServicio;
import java.awt.Color;
import java.awt.Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

@Component
public class VistaProductos extends javax.swing.JFrame {

    private VistaMain vistaMain;
    private ProductoServicio productoServicio;
    private Producto producto;
    private DefaultTableModel tablaModelo;
    private ContenedorServicio contenedorServicio;


    @Autowired
    public VistaProductos(ProductoServicio productoServicio, ContenedorServicio contenedorServicio) {
        this.productoServicio = productoServicio;
        this.contenedorServicio = contenedorServicio;
        initComponents();
        iniciarTabla();
        inicializarComboBox();
        personalizarTablaBarcos();
        listar();
        producto = new Producto();
    }

    private void llenarComboTipoProducto() {
        jComboBox1.removeAllItems();
        jComboBox1.addItem("Artículos peligrosos");
        jComboBox1.addItem("Bienes de consumo");
        jComboBox1.addItem("Maquinaria y Partes");
        jComboBox1.addItem("Materiales de construcción");
        jComboBox1.addItem("Materiales químicos");
        jComboBox1.addItem("Productos Alimenticios");
        jComboBox1.addItem("Productos de papel");
        jComboBox1.addItem("Productos farmacéuticos");
        jComboBox1.addItem("Vehículos");
    }
    
    private void personalizarTablaBarcos() {
        
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

    public void setVistaMain(VistaMain vistaMain) {
        this.vistaMain = vistaMain;
    }
   
   public void regresar(){
        this.setVisible(false);
        vistaMain.setVisible(true);
    }

    private void iniciarTabla() {
        this.tablaModelo = new DefaultTableModel(0, 7) { 
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] columnas = {"ID", "Nombre", "Tipo", "Cantidad", "Peso Unitario", "ID Contenedor", "Observaciones"};
        this.tablaModelo.setColumnIdentifiers(columnas);
        jTable1.setModel(tablaModelo);

        listar();
    }

    public void inicializarComboBox(){
        contenedorComboBox.removeAllItems();

        List<Contenedor> contenedores = contenedorServicio.listarContenedores();
        contenedores.forEach(contenedor -> {
            contenedorComboBox.addItem(contenedor);
        });

    }


    private void guardar() {

        

        if (contenedorComboBox.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "⚠️ No hay contenedores registrados. Por favor, registre uno antes de continuar.");
            return;
        }

        if(contenedorComboBox.getSelectedItem() == null){
            JOptionPane.showMessageDialog(this,"Seleccione un contenedor antes de continuar");
            return;
        }

        Contenedor contenedor = (Contenedor) contenedorComboBox.getSelectedItem();

        String tipoProducto = (String) jComboBox1.getSelectedItem();
        String nombre = NombreText.getText().trim();
        String cantidadStr = CantidadNumber.getText().trim();
        String pesoStr = PesoUDouble.getText().trim();
        String descripcion = DescripcionText.getText().trim();

        if (tipoProducto == null || tipoProducto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un tipo de producto válido");
            return;
        }

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del producto no puede estar vacío");
            return;
        }

        int cantidad = 0;
        try {
            cantidad = Integer.parseInt(cantidadStr);
            if (cantidad < 0) {
                JOptionPane.showMessageDialog(this, "La cantidad no puede ser negativa");
                return;
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Ingrese un número entero válido para la cantidad");
            return;
        } catch (NullPointerException npe) {
            JOptionPane.showMessageDialog(this, "El campo cantidad no puede estar vacío");
            return;
        }

        double pesoUnitario = 0;
        try {
            pesoUnitario = Double.parseDouble(pesoStr);
            if (pesoUnitario < 0) {
                JOptionPane.showMessageDialog(this, "El peso unitario no puede ser negativo");
                return;
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Ingrese un número decimal válido para el peso unitario");
            return;
        } catch (NullPointerException npe) {
            JOptionPane.showMessageDialog(this, "El campo peso unitario no puede estar vacío");
            return;
        }

        if (descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La descripción no puede estar vacía");
            return;
        }

        if (producto == null) {
            producto = new Producto();
        }

        producto.setTipoProducto(tipoProducto);
        producto.setNombreProducto(nombre);
        producto.setCantidad(cantidad);
        producto.setPesoUnitario(pesoUnitario);
        producto.setDescripcion(descripcion);
        producto.setContenedor(contenedor);


        productoServicio.guardar(producto);
        limpiar();
        listar();

        JOptionPane.showMessageDialog(this, "Producto guardado correctamente");
    }

    public void listar() {
        tablaModelo.setRowCount(0);

        List<Producto> productos = productoServicio.listarProducto();

        productos.forEach(producto -> {
            Object[] fila = {
                    producto.getIdProducto(),
                    producto.getNombreProducto(),
                    producto.getTipoProducto(),
                    producto.getCantidad(),
                    producto.getPesoUnitario(),
                    producto.getContenedor() != null ? producto.getContenedor().getIdContenedor() : "Contenedor no seleccionado",
                    producto.getDescripcion()
            };
            tablaModelo.addRow(fila);
        });
    }



    private void eliminar() {
        if (producto != null) {
            productoServicio.eliminar(producto);
            limpiar();
            listar();
            System.out.println("Producto eliminado.");
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar.");
        }
    }


    private void limpiar() {
        producto = new Producto();
        jComboBox1.setSelectedIndex(0);
        NombreText.setText("");
        CantidadNumber.setText("");
        PesoUDouble.setText("");
        DescripcionText.setText("");
    }

    private void cargarSeleccion() {
        int fila = jTable1.getSelectedRow();
        if (fila < 0) return;

        String nombre = (String) jTable1.getValueAt(fila, 1);
        producto = productoServicio.listarProducto().stream()
                .filter(p -> p.getNombreProducto().equals(nombre))
                .findFirst()
                .orElse(null);

        if (producto != null) {
            jComboBox1.setSelectedItem(producto.getTipoProducto());
            NombreText.setText(producto.getNombreProducto());
            CantidadNumber.setText(producto.getCantidad().toString());
            PesoUDouble.setText(producto.getPesoUnitario().toString());
            DescripcionText.setText(producto.getDescripcion());
        }
    }

    private boolean verificarSeleccion(){
        if(producto.getIdProducto() != null){
            return true;
        }else{
            JOptionPane.showMessageDialog(this,"Seleccione un registro para continuar");
            return false;
        }
    }
    
    @SuppressWarnings("unchecked")
    
    private void initComponents() {

        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        Guardar = new javax.swing.JButton();
        DescripcionText = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        PesoUDouble = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnMain = new javax.swing.JLabel();
        btnLogin = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        Limpiar = new javax.swing.JButton();
        Eliminar = new javax.swing.JButton();
        Editar = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        NombreText = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        CantidadNumber = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        contenedorComboBox = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel6.setFont(new java.awt.Font("Arial", 1, 18)); 
        jLabel6.setText("Tipo de Producto:");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Id", "Nombre", "Tipo", "Cantidad", "Precio Unitario", "Descripcion", "Id Contenedor"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Double.class, java.lang.String.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        Guardar.setBackground(new java.awt.Color(0, 133, 189));
        Guardar.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        Guardar.setForeground(new java.awt.Color(255, 255, 255));
        Guardar.setText("GUARDAR");
        Guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GuardarActionPerformed(evt);
            }
        });

        DescripcionText.setFont(new java.awt.Font("Segoe UI", 0, 16)); 
        DescripcionText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DescripcionTextActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Arial", 1, 16)); 
        jLabel8.setText("Cantidad:");

        PesoUDouble.setFont(new java.awt.Font("Segoe UI", 0, 16)); 
        PesoUDouble.setForeground(new java.awt.Color(0, 0, 0));
        PesoUDouble.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PesoUDoubleActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Arial", 1, 18)); 
        jLabel9.setText("Descripcion:");

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

        jLabel10.setFont(new java.awt.Font("Arial Black", 0, 30)); 
        jLabel10.setForeground(new java.awt.Color(0, 0, 51));
        jLabel10.setText("PRODUCTOS");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(btnMain)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addGap(422, 422, 422)
                .addComponent(btnLogin)
                .addGap(62, 62, 62))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(btnLogin)
                    .addComponent(btnMain))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Limpiar.setBackground(new java.awt.Color(0, 133, 189));
        Limpiar.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        Limpiar.setForeground(new java.awt.Color(255, 255, 255));
        Limpiar.setText("LIMPIAR");
        Limpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LimpiarActionPerformed(evt);
            }
        });

        Eliminar.setBackground(new java.awt.Color(0, 133, 189));
        Eliminar.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        Eliminar.setForeground(new java.awt.Color(255, 255, 255));
        Eliminar.setText("ELIMINAR");
        Eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EliminarActionPerformed(evt);
            }
        });

        Editar.setBackground(new java.awt.Color(0, 133, 189));
        Editar.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        Editar.setForeground(new java.awt.Color(255, 255, 255));
        Editar.setText("MODIFICAR");
        Editar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditarActionPerformed(evt);
            }
        });

        jComboBox1.setFont(new java.awt.Font("Segoe UI", 0, 16)); 
        jComboBox1.setForeground(new java.awt.Color(0, 0, 0));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Productos Alimenticios", "Bienes de consumo", "Maquinaria y Partes", "Vehiculos", "Productos farmacéuticos", "Materiales químicos", "Materiales de construcción", "Productos de papel", "Articulos peligrosos" }));

        NombreText.setFont(new java.awt.Font("Segoe UI", 0, 16)); 
        NombreText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NombreTextActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Arial", 1, 18)); 
        jLabel11.setText("Peso Unitario:");

        jLabel12.setFont(new java.awt.Font("Arial", 1, 18)); 
        jLabel12.setText("Nombre:");

        CantidadNumber.setFont(new java.awt.Font("Segoe UI", 0, 16)); 
        CantidadNumber.setForeground(new java.awt.Color(0, 0, 0));
        CantidadNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CantidadNumberActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); 
        jLabel1.setText("Contenedor:");

        contenedorComboBox.setFont(new java.awt.Font("Segoe UI", 0, 16)); 
        contenedorComboBox.setForeground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(NombreText)
                    .addComponent(jLabel1)
                    .addComponent(contenedorComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(Guardar)
                        .addGap(18, 18, 18)
                        .addComponent(Editar)
                        .addGap(18, 18, 18)
                        .addComponent(Eliminar)
                        .addGap(18, 18, 18)
                        .addComponent(Limpiar))
                    .addComponent(PesoUDouble)
                    .addComponent(DescripcionText)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CantidadNumber)
                    .addComponent(jComboBox1, 0, 404, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 798, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(contenedorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(NombreText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(CantidadNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel11)
                        .addGap(15, 15, 15)
                        .addComponent(PesoUDouble, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(DescripcionText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Editar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Limpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap(73, Short.MAX_VALUE))
        );

        pack();
    }

    private void GuardarActionPerformed(java.awt.event.ActionEvent evt) {
          producto = new Producto(); 
          guardar();
    }

    private void DescripcionTextActionPerformed(java.awt.event.ActionEvent evt) {
        
    }

    private void PesoUDoubleActionPerformed(java.awt.event.ActionEvent evt) {
        
    }

    private void btnMainMouseClicked(java.awt.event.MouseEvent evt) {
        regresar();
    }

    private void btnLoginMouseClicked(java.awt.event.MouseEvent evt) {
        setVisible(false);
        vistaMain.volverLogin();
    }

    private void NombreTextActionPerformed(java.awt.event.ActionEvent evt) {
        
    }

    private void CantidadNumberActionPerformed(java.awt.event.ActionEvent evt) {
        
    }

    private void EditarActionPerformed(java.awt.event.ActionEvent evt) {
        if(verificarSeleccion()){
            guardar();
        }
    }

    private void EliminarActionPerformed(java.awt.event.ActionEvent evt) {
        if(verificarSeleccion()){
            eliminar();
        }
    }

    private void LimpiarActionPerformed(java.awt.event.ActionEvent evt) {
        limpiar();
    }

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {
        int row = jTable1.getSelectedRow();
    if (row >= 0) {
        Integer id = (Integer) tablaModelo.getValueAt(row, 0);

        producto = productoServicio.buscarPorId(id);

        if (producto != null) {
            jComboBox1.setSelectedItem(producto.getTipoProducto());
            NombreText.setText(producto.getNombreProducto());
            CantidadNumber.setText(producto.getCantidad().toString());
            PesoUDouble.setText(producto.getPesoUnitario().toString());
            DescripcionText.setText(producto.getDescripcion());
            
        }
    }
    }


    
    private javax.swing.JTextField CantidadNumber;
    private javax.swing.JTextField DescripcionText;
    private javax.swing.JButton Editar;
    private javax.swing.JButton Eliminar;
    private javax.swing.JButton Guardar;
    private javax.swing.JButton Limpiar;
    private javax.swing.JTextField NombreText;
    private javax.swing.JTextField PesoUDouble;
    private javax.swing.JLabel btnLogin;
    private javax.swing.JLabel btnMain;
    private javax.swing.JComboBox<Contenedor> contenedorComboBox;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    
}
