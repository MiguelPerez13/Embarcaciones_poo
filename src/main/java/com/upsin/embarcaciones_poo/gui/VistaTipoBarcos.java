package com.upsin.embarcaciones_poo.gui;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.upsin.embarcaciones_poo.modelo.TipoBarco;
import com.upsin.embarcaciones_poo.servicio.TipoBarcoServicio;
import java.awt.Color;
import java.awt.Font;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VistaTipoBarcos extends javax.swing.JFrame {

    private TipoBarcoServicio tipoBarcoServicio;
    private DefaultTableModel tablaModelo;
    private TipoBarco tipoBarco;
    private VistaMain vistaMain;
    private Integer permiso;
    
    
    @Autowired
    public VistaTipoBarcos(TipoBarcoServicio tipoBarcoServicio) {
        this.tipoBarcoServicio = tipoBarcoServicio;
        initComponents();
        iniciarTabla();
        personalizarTablaBarcos();
        tipoBarco = new TipoBarco();
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
    
    private void personalizarTablaBarcos() {
        
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
    
    
    public void setVistaMain(VistaMain vistaMain) {
        this.vistaMain = vistaMain;
    }
    
    
    public void iniciarTabla(){
            
        this.tablaModelo = new DefaultTableModel(0, 3){
            @Override
            public boolean isCellEditable(int row,int column){return false;}
        };

        String[] nombresColumnas = {"ID", "Nombre", "Descripcion"};
        this.tablaModelo.setColumnIdentifiers(nombresColumnas);
        this.tabla.setModel(tablaModelo);
        this.tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        
        listar();
    }

    
    public void listar(){
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
    
    
    
    public void guardar(){      
        String nombre = nombreLabel.getText();
        String descripcion = descripcionLabel.getText();

        if(nombre.isEmpty() || descripcion.isEmpty()){
            JOptionPane.showMessageDialog(this,"Llene todos los campos antes de continuar");
            return;
        }

            
        tipoBarco.setNombreTipo(nombre);
        tipoBarco.setDescripcion(descripcion);
            
        tipoBarcoServicio.guardarTipoBarco(tipoBarco);

        listar();
        limpiar();
    }
    
    
    public void cargarSeleccion(){
        var renglon = tabla.getSelectedRow();
        Integer id = (Integer) tabla.getModel().getValueAt(renglon, 0);
        
        this.tipoBarco = tipoBarcoServicio.bucarTipobarcoId(id);
        
        nombreLabel.setText(tipoBarco.getNombreTipo());
        descripcionLabel.setText(tipoBarco.getDescripcion());
    }

    private void exportarTablaAPDF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar PDF");
        fileChooser.setSelectedFile(new java.io.File("reporte_TipoBarcos.pdf"));

        int seleccion = fileChooser.showSaveDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            try {
                Document document = new Document(PageSize.A4.rotate(), 36, 36, 54, 36);
                PdfWriter.getInstance(document, new FileOutputStream(fileChooser.getSelectedFile()));

                document.open();


                PdfPTable headerTable = new PdfPTable(2);
                headerTable.setWidthPercentage(100);
                headerTable.setWidths(new float[]{1, 3});

                try {
                    InputStream is = getClass().getClassLoader().getResourceAsStream("Icons/LogoFInal.png");
                    Image logo = Image.getInstance(IOUtils.toByteArray(is));
                    logo.scaleToFit(100, 50);
                    PdfPCell logoCell = new PdfPCell(logo, false);
                    logoCell.setBorder(Rectangle.NO_BORDER);
                    headerTable.addCell(logoCell);
                } catch (Exception e) {
                    System.err.println("No se pudo cargar el logo: " + e.getMessage());
                    PdfPCell emptyCell = new PdfPCell(new Phrase(""));
                    emptyCell.setBorder(Rectangle.NO_BORDER);
                    headerTable.addCell(emptyCell);
                }

                SimpleDateFormat formatoFecha = new SimpleDateFormat(
                        "EEEE, d 'de' MMMM 'de' yyyy, HH:mm:ss",
                        new Locale("es", "ES")
                );
                String fechaFormateada = formatoFecha.format(new Date());
                com.lowagie.text.Font fechaFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.DARK_GRAY);
                PdfPCell fechaCell = new PdfPCell(new Phrase("Fecha de generación: " + fechaFormateada, fechaFont));
                fechaCell.setBorder(Rectangle.NO_BORDER);
                fechaCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                fechaCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                headerTable.addCell(fechaCell);

                document.add(headerTable);
                document.add(new Paragraph("\n"));


                com.lowagie.text.Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, Color.BLACK);
                Paragraph titulo = new Paragraph("Reporte de Tipo de Barcos ", tituloFont);
                titulo.setAlignment(Element.ALIGN_CENTER);
                titulo.setSpacingAfter(5);
                document.add(titulo);


                LineSeparator ls = new LineSeparator();
                ls.setLineColor(Color.GRAY);
                document.add(new Chunk(ls));
                document.add(new Paragraph("\n"));


                PdfPTable pdfTable = new PdfPTable(tabla.getColumnCount());
                pdfTable.setWidthPercentage(100);


                com.lowagie.text.Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
                Color headerBgColor = new Color(0, 102, 153);

                for (int i = 0; i < tabla.getColumnCount(); i++) {
                    PdfPCell cell = new PdfPCell(new Phrase(tabla.getColumnName(i), headerFont));
                    cell.setBackgroundColor(headerBgColor);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(8);
                    pdfTable.addCell(cell);
                }


                Color rowColor1 = Color.WHITE;
                Color rowColor2 = new Color(230, 240, 250);
                com.lowagie.text.Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);

                for (int row = 0; row < tabla.getRowCount(); row++) {
                    Color bgColor = (row % 2 == 0) ? rowColor1 : rowColor2;
                    for (int col = 0; col < tabla.getColumnCount(); col++) {
                        Object valor = tabla.getValueAt(row, col);
                        PdfPCell cell = new PdfPCell(new Phrase(valor != null ? valor.toString() : "", cellFont));
                        cell.setBackgroundColor(bgColor);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell.setPadding(6);
                        pdfTable.addCell(cell);
                    }
                }

                document.add(pdfTable);
                document.add(new Paragraph("\n"));


                com.lowagie.text.Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.BLACK);
                Paragraph total = new Paragraph("Total de registros: " + tabla.getRowCount(), totalFont);
                total.setAlignment(Element.ALIGN_RIGHT);
                document.add(total);

                document.close();
                JOptionPane.showMessageDialog(this, "PDF generado con éxito", "Reporte", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al generar PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void eliminar(){
        tipoBarcoServicio.eliminarTipoBarco(tipoBarco);
        listar();
        limpiar();
    }
    
    
    public void limpiar(){
        nombreLabel.setText("");
        descripcionLabel.setText("");
        this.tipoBarco= new TipoBarco();
    }
    
    
    public void regresar(){
        this.setVisible(false);
        vistaMain.setVisible(true);
    }

    private boolean verificarSeleccion(){
        if(tipoBarco.getIdTipoBarco() != null){
            return true;
        }else{
            JOptionPane.showMessageDialog(this,"Seleccione un registro antes de continuar");
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        nombreLabel = new javax.swing.JTextField();
        descripcionLabel = new javax.swing.JTextField();
        guardatButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        editarButton = new javax.swing.JButton();
        limpiarButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btnMain = new javax.swing.JLabel();
        btnLogin = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        imprimirButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel4.setText("Nombre:");

        jLabel5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel5.setText("Descripcion:");

        nombreLabel.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        descripcionLabel.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        descripcionLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                descripcionLabelActionPerformed(evt);
            }
        });

        guardatButton.setBackground(new java.awt.Color(0, 133, 189));
        guardatButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        guardatButton.setForeground(new java.awt.Color(255, 255, 255));
        guardatButton.setText("GUARDAR");
        guardatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardatButtonActionPerformed(evt);
            }
        });

        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabla);

        editarButton.setBackground(new java.awt.Color(0, 133, 189));
        editarButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        editarButton.setForeground(new java.awt.Color(255, 255, 255));
        editarButton.setText("MODIFICAR");
        editarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editarButtonActionPerformed(evt);
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

        jLabel2.setFont(new java.awt.Font("Arial Black", 1, 30)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 51));
        jLabel2.setText("Registro Tipos de Barco");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(btnMain)
                .addGap(300, 300, 300)
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

        imprimirButton.setBackground(new java.awt.Color(0, 133, 189));
        imprimirButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        imprimirButton.setForeground(new java.awt.Color(255, 255, 255));
        imprimirButton.setText("IMPRIMIR");
        imprimirButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imprimirButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(descripcionLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
                                .addComponent(nombreLabel, javax.swing.GroupLayout.Alignment.LEADING)))
                        .addGap(106, 106, 106))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(guardatButton)
                        .addGap(18, 18, 18)
                        .addComponent(editarButton)
                        .addGap(18, 18, 18)
                        .addComponent(limpiarButton)
                        .addGap(18, 18, 18)
                        .addComponent(imprimirButton)
                        .addGap(97, 97, 97)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 719, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(105, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(nombreLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(descripcionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(90, 90, 90)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(editarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(limpiarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(guardatButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(imprimirButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1))
                .addContainerGap(141, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void descripcionLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_descripcionLabelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_descripcionLabelActionPerformed

    private void guardatButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardatButtonActionPerformed
        if(verificarPermisos(2)){
            this.tipoBarco = new TipoBarco(null, "", "");
            guardar();
        }
    }//GEN-LAST:event_guardatButtonActionPerformed

    private void editarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editarButtonActionPerformed
        if(verificarPermisos(2)){
            if (verificarSeleccion()) {
                guardar();
            }
        }
    }//GEN-LAST:event_editarButtonActionPerformed

    private void limpiarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_limpiarButtonActionPerformed
        limpiar();
    }//GEN-LAST:event_limpiarButtonActionPerformed

    private void tablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaMouseClicked
        cargarSeleccion();
    }//GEN-LAST:event_tablaMouseClicked

    private void btnMainMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMainMouseClicked
        regresar();
    }//GEN-LAST:event_btnMainMouseClicked

    private void btnLoginMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLoginMouseClicked
        setVisible(false);
        vistaMain.volverLogin();
    }//GEN-LAST:event_btnLoginMouseClicked

    private void imprimirButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imprimirButtonActionPerformed
        if(verificarPermisos(0)){
            exportarTablaAPDF();
        }
    }//GEN-LAST:event_imprimirButtonActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnLogin;
    private javax.swing.JLabel btnMain;
    private javax.swing.JTextField descripcionLabel;
    private javax.swing.JButton editarButton;
    private javax.swing.JButton guardatButton;
    private javax.swing.JButton imprimirButton;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton limpiarButton;
    private javax.swing.JTextField nombreLabel;
    private javax.swing.JTable tabla;
    // End of variables declaration//GEN-END:variables
}
