package com.upsin.embarcaciones_poo.gui;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.upsin.embarcaciones_poo.modelo.Empresa;
import com.upsin.embarcaciones_poo.servicio.EmpresaServicio;
import java.awt.Color;
import java.awt.Font;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.ParseException;
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
public class VistaEmpresas extends javax.swing.JFrame {

    private VistaMain vistaMain;
    private DefaultTableModel tablaModelo;
    private EmpresaServicio empresaServicio;
    private Empresa empresa;
    private Integer permiso;

    @Autowired
    public VistaEmpresas(EmpresaServicio empresaServicio) {
        this.empresaServicio = empresaServicio;
        initComponents();
        iniciarTabla();
        personalizarTablaBarcos();
        listar();
        empresa = new Empresa();
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

    private void iniciarTabla() {
        this.tablaModelo = new DefaultTableModel(0, 8) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        String[] columnas = {"Id", "Nombre", "RFC", "Telefono", "Email", "Direccion", "Tipo Empresa", "Fecha Registro"};
        this.tablaModelo.setColumnIdentifiers(columnas);
        tabla.setModel(tablaModelo);
    }

    public void regresar() {
        this.setVisible(false);
        vistaMain.setVisible(true);
    }

    public void guardar() {

        String nombre = NombreText.getText().trim();
        String rfc = RFCText.getText().trim();
        String telefono = TelefonoText.getText().trim();
        String email = emailText.getText().trim();
        String direccion = DireccionText.getText().trim();
        String tipoEmpresa = tipoEmpresaField.getText().trim();
        Date fecha = fechaRegistroDate.getDate();

        
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.");
            return;
        }

        
        if (rfc.isEmpty() || rfc.length() < 10) {
            JOptionPane.showMessageDialog(this, "El RFC es inválido o está vacío.");
            return;
        }

        
        try {
            Long.parseLong(telefono);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El teléfono debe contener solo números.");
            return;
        }

        
        if (!email.contains("@") || email.startsWith("@") || email.endsWith("@")) {
            JOptionPane.showMessageDialog(this, "El correo electrónico no es válido.");
            return;
        }

        
        if (direccion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La dirección no puede estar vacía.");
            return;
        }

        if(tipoEmpresa.isEmpty()){
            JOptionPane.showMessageDialog(this,"El tipo de empresa no puede estar vacio");
            return;
        }

        if(fecha == null){
            JOptionPane.showMessageDialog(this,"Seleccione una fecha");
            return;
        }

            try {
                empresa.setNombre(nombre);
                empresa.setRfc(rfc);
                empresa.setTelefono(telefono);
                empresa.setEmail(email);
                empresa.setDireccion(direccion);
                empresa.setTipoEmpresa(tipoEmpresa);
                empresa.setFechaRegistro(fecha);

                empresaServicio.guardar(empresa);
                limpiar();
                listar();
                JOptionPane.showMessageDialog(this, "Empresa guardada correctamente.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar la empresa: " + ex.getMessage());
                ex.printStackTrace();
            }
    }

    public void cargarSeleccion() {
        int renglon = tabla.getSelectedRow();
        if (renglon < 0) {
            System.out.println("No se ha seleccionado ninguna fila");
            return;
        }

        Integer id = (Integer) tabla.getModel().getValueAt(renglon, 0);
        this.empresa = empresaServicio.buscarPorId(id);

        if (empresa != null) {
            NombreText.setText(empresa.getNombre());
            RFCText.setText(empresa.getRfc());
            TelefonoText.setText(empresa.getTelefono());
            emailText.setText(empresa.getEmail());
            DireccionText.setText(empresa.getDireccion());
            tipoEmpresaField.setText(empresa.getTipoEmpresa());
            fechaRegistroDate.setDate(empresa.getFechaRegistro());
        }
    }

    public void eliminar() {
        if (empresa != null) {
            empresaServicio.eliminar(empresa);
            listar();   
            limpiar();  
        } else {
            System.out.println("No hay empresa seleccionada para eliminar");
        }
    }

    private void exportarTablaAPDF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar PDF");
        fileChooser.setSelectedFile(new java.io.File("reporte_empresas.pdf"));

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
                Paragraph titulo = new Paragraph("Reporte de Empresas ", tituloFont);
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

    public void limpiar() {
        empresa = new Empresa();
        NombreText.setText("");
        RFCText.setText("");
        TelefonoText.setText("");
        emailText.setText("");
        DireccionText.setText("");
        tipoEmpresaField.setText("");
    }

    public void listar() {
        this.tablaModelo.setRowCount(0); 

        List<Empresa> empresas = empresaServicio.listarEmpresa();

        empresas.forEach(empresa -> {
            Object[] fila = {
                empresa.getIdEmpresa(),
                empresa.getNombre(),
                empresa.getRfc(),
                empresa.getTelefono(),
                empresa.getEmail(),
                empresa.getDireccion(),
                empresa.getTipoEmpresa(),
                empresa.getFechaRegistro()
            };
            this.tablaModelo.addRow(fila);
        });
    }

    private boolean verificarSeleccion(){
        if(empresa.getIdEmpresa() != null){
            return true;
        }else{
            JOptionPane.showMessageDialog(this,"Seleccione un registro antes de continuar");
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        TelefonoText = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        RFCText = new javax.swing.JTextField();
        DireccionText = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        emailText = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        NombreText = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        tipoEmpresaField = new javax.swing.JTextField();
        btnEditar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btnMain = new javax.swing.JLabel();
        btnLogin = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        fechaRegistroDate = new com.toedter.calendar.JDateChooser();
        limpiarButton = new javax.swing.JButton();
        imprimirButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        TelefonoText.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        TelefonoText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TelefonoTextActionPerformed(evt);
            }
        });

        btnGuardar.setBackground(new java.awt.Color(0, 133, 189));
        btnGuardar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setText("GUARDAR");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel5.setText("Nombre:");

        jLabel6.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel6.setText("RFC:");

        RFCText.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        RFCText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RFCTextActionPerformed(evt);
            }
        });

        DireccionText.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        DireccionText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DireccionTextActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel8.setText("Email:");

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Id", "Nombre ", "RFC", "Telefono", "email", "Direccion", "Tipo Empresa", "Fecha de Registro"
            }
        ));
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabla);

        emailText.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        emailText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailTextActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel9.setText("Direccion:");

        NombreText.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        NombreText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NombreTextActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel7.setText("Telefono:");

        jLabel10.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel10.setText("Fecha de registro:");

        jLabel11.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel11.setText("Tipo de Empresa:");

        tipoEmpresaField.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        tipoEmpresaField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipoEmpresaFieldActionPerformed(evt);
            }
        });

        btnEditar.setBackground(new java.awt.Color(0, 133, 189));
        btnEditar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnEditar.setForeground(new java.awt.Color(255, 255, 255));
        btnEditar.setText("MODIFICAR");
        btnEditar.setToolTipText("");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
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

        jLabel3.setFont(new java.awt.Font("Arial Black", 0, 30)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 51));
        jLabel3.setText("EMPRESAS");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(btnMain)
                .addGap(431, 431, 431)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnLogin)
                .addGap(62, 62, 62))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(btnLogin)
                    .addComponent(btnMain))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        fechaRegistroDate.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        limpiarButton.setBackground(new java.awt.Color(0, 133, 189));
        limpiarButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        limpiarButton.setForeground(new java.awt.Color(255, 255, 255));
        limpiarButton.setText("LIMPIAR");
        limpiarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limpiarButtonActionPerformed(evt);
            }
        });

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
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(DireccionText, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TelefonoText, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(59, 59, 59)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tipoEmpresaField, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(fechaRegistroDate, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(49, 49, 49)
                                        .addComponent(btnGuardar)
                                        .addGap(26, 26, 26)
                                        .addComponent(btnEditar)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(28, 28, 28)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(emailText, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(limpiarButton)
                                        .addGap(18, 18, 18)
                                        .addComponent(imprimirButton))))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(107, 107, 107)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1196, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(RFCText, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel5)
                                .addComponent(NombreText, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)))))
                .addContainerGap(97, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(NombreText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(TelefonoText, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tipoEmpresaField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel10)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(fechaRegistroDate, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel6)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(RFCText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel9)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(DireccionText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(emailText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(51, 51, 51)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(limpiarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(imprimirButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(64, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TelefonoTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TelefonoTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TelefonoTextActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        if(verificarPermisos(2)){
            this.empresa = new Empresa();
            guardar();
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void RFCTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RFCTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RFCTextActionPerformed

    private void DireccionTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DireccionTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DireccionTextActionPerformed

    private void emailTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailTextActionPerformed

    private void tipoEmpresaFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipoEmpresaFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tipoEmpresaFieldActionPerformed

    private void btnMainMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMainMouseClicked
        regresar();
    }//GEN-LAST:event_btnMainMouseClicked

    private void btnLoginMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLoginMouseClicked
        setVisible(false);
        vistaMain.volverLogin();
    }//GEN-LAST:event_btnLoginMouseClicked

    private void NombreTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NombreTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NombreTextActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        if(verificarPermisos(2)){
            if (verificarSeleccion()) {
                guardar();
            }
        }
    }//GEN-LAST:event_btnEditarActionPerformed

    private void tablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaMouseClicked
        cargarSeleccion();
    }//GEN-LAST:event_tablaMouseClicked

    private void limpiarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_limpiarButtonActionPerformed
        limpiar();
    }//GEN-LAST:event_limpiarButtonActionPerformed

    private void imprimirButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imprimirButtonActionPerformed
        if(verificarPermisos(0)){
            exportarTablaAPDF();
        }
    }//GEN-LAST:event_imprimirButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField DireccionText;
    private javax.swing.JTextField NombreText;
    private javax.swing.JTextField RFCText;
    private javax.swing.JTextField TelefonoText;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JLabel btnLogin;
    private javax.swing.JLabel btnMain;
    private javax.swing.JTextField emailText;
    private com.toedter.calendar.JDateChooser fechaRegistroDate;
    private javax.swing.JButton imprimirButton;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton limpiarButton;
    private javax.swing.JTable tabla;
    private javax.swing.JTextField tipoEmpresaField;
    // End of variables declaration//GEN-END:variables
}
