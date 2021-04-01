
import com.mysql.jdbc.Connection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;


public final class app extends javax.swing.JFrame {

    public app() {
        
        initComponents();
        list();
        hideButtonPanel.setVisible(false);
        createPopupMenu(this);
    }
    
    private final JPopupMenu popupMenu = new JPopupMenu();
    private JMenuItem menuItem = null;
    
    Connection connect = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    int deleteItem;
    String copyItem;
    String creationDate = null;
    
    @Override
    public void list() {
        
        int dbColumnCount;
        
        String sql = "SELECT * FROM product";
        
        try {
            statement = dbHelper.getConnection().prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            ResultSetMetaData stData = resultSet.getMetaData();
            
            dbColumnCount = stData.getColumnCount();
            
            DefaultTableModel recordTable = (DefaultTableModel)jTable1.getModel();
            recordTable.setRowCount(0);
            
            while(resultSet.next()) {
                Vector columnData = new Vector();
            
                for(int i=1; i<dbColumnCount; i++) {
                    columnData.add(resultSet.getString("stock_code"));
                    columnData.add(resultSet.getString("stock_name"));
                    columnData.add(resultSet.getShort("stock_type"));
                    columnData.add(resultSet.getString("unit"));
                    columnData.add(resultSet.getString("barcode"));
                    columnData.add(resultSet.getString("VAT_type"));
                    String date[] = resultSet.getString("creation_date").split(" ");
                    columnData.add(date[0]);
                    columnData.add(resultSet.getString("description"));
                }
                recordTable.addRow(columnData);
            } 
        } catch (SQLException ex) {
            Logger.getLogger(app.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void insert() throws SQLException {

        String stockCode = tf_stock_code.getText();
        String stockName = tf_stock_name.getText();
        int stockType = (int) cb_stock_type.getSelectedIndex();
        String unit = cb_unit.getSelectedItem().toString();
        String barcode = tf_barcode.getText();
        Double VATType = Double.parseDouble((String) cb_VAT_type.getSelectedItem());
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        creationDate = dateformat.format(ftf_creation_date.getDate());
        String description = ta_description.getText();
        
        String sql = "INSERT INTO stock_card.product (stock_code, stock_name, stock_type, unit, barcode, VAT_type, creation_date, description)"
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
       
        try {
            statement = dbHelper.getConnection().prepareStatement(sql);

            statement.setString(1, stockCode);
            statement.setString(2, stockName);
            statement.setInt(3, stockType);
            statement.setString(4, unit);
            statement.setString(5, barcode);
            statement.setDouble(6, VATType);
            statement.setString(7, creationDate);
            statement.setString(8, description);

            if (statement.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Başarıyla Kaydedildi");
            }

        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, "Bu Stok Kodu Zaten Kayıtlı");
        } 
    }

    public void delete() throws SQLException {
        
        DefaultTableModel recordTable = (DefaultTableModel)jTable1.getModel();
        int selectedRows = jTable1.getSelectedRow();
        
        String stock_code = recordTable.getValueAt(selectedRows, 0).toString();
        
        deleteItem = JOptionPane.showConfirmDialog(null, "Silmek istediğinize emin misiniz?", "Uyarı", JOptionPane.YES_NO_OPTION);
        
        if(deleteItem == JOptionPane.YES_OPTION) {
            
            String sql = "DELETE FROM product WHERE stock_code = ?";
            statement = dbHelper.getConnection().prepareStatement(sql);
          
            statement.setString(1, stock_code);
            statement.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Silme İşlemi Başarılı");
            }   
    }
    
    public void update() {
        
        String stockCode = tf_stock_code.getText();
        String stockName = tf_stock_name.getText();
        int stockType = (int) cb_stock_type.getSelectedIndex();
        String unit = cb_unit.getSelectedItem().toString();
        String barcode = tf_barcode.getText();
        Double VATType = Double.parseDouble((String) cb_VAT_type.getSelectedItem());
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        creationDate = dateformat.format(ftf_creation_date.getDate());
        String description = ta_description.getText();
        
        String sql = "UPDATE stock_card.product SET stock_name = ?, stock_type = ?, unit = ?, barcode = ?, VAT_type = ?, creation_date = ?, description = ? WHERE stock_code = ?";
        
        try {
            statement = dbHelper.getConnection().prepareStatement(sql);
           
            statement.setString(1, stockName);
            statement.setInt(2, stockType);
            statement.setString(3, unit);
            statement.setString(4, barcode);
            statement.setDouble(5, VATType);
            statement.setString(6, creationDate);
            statement.setString(7, description);
            statement.setString(8, stockCode);
            
            if (statement.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Başarıyla Güncellendi");
            }
            
        } catch (SQLException exception) {
            Logger.getLogger(app.class.getName()).log(Level.SEVERE, null, exception);
        }
    }
    
    public void copy() throws SQLException {
        
        try{
            DefaultTableModel recordTable = (DefaultTableModel)jTable1.getModel();
            int selectedRows = jTable1.getSelectedRow();

            String stock_code = recordTable.getValueAt(selectedRows, 0).toString();

            copyItem = JOptionPane.showInputDialog(null, "Kopyanın Stok Kodu?");

            String sql = "INSERT INTO product (stock_code, stock_name, stock_type, unit, barcode, VAT_type, creation_date, description)"
                    + "SELECT ?, stock_name, stock_type, unit, barcode, VAT_type, creation_date, description FROM product WHERE stock_code = ?";

            statement = dbHelper.getConnection().prepareStatement(sql);

            statement.setString(1, copyItem);
            statement.setString(2, stock_code);

            statement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Kopyalama İşlemi Başarılı");
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(this, "Bu Stok Kodu Zaten Kayıtlı");
        }
        
    }
    
    public void search(String searchText) {
        int dbColumnCount;
        
        String sql = "SELECT * FROM product WHERE stock_code LIKE '"+searchText+"%'";
        
        try {
            statement = dbHelper.getConnection().prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            ResultSetMetaData stData = resultSet.getMetaData();
            
            dbColumnCount = stData.getColumnCount();
            
            DefaultTableModel recordTable = (DefaultTableModel)jTable1.getModel();
            recordTable.setRowCount(0);
            
            while(resultSet.next()) {
                Vector columnData = new Vector();
            
                for(int i=1; i<dbColumnCount; i++) {
                    columnData.add(resultSet.getString("stock_code"));
                    columnData.add(resultSet.getString("stock_name"));
                    columnData.add(resultSet.getShort("stock_type"));
                    columnData.add(resultSet.getString("unit"));
                    columnData.add(resultSet.getString("barcode"));
                    columnData.add(resultSet.getString("VAT_type"));
                    String date[] = resultSet.getString("creation_date").split(" ");
                    columnData.add(date[0]);
                    columnData.add(resultSet.getString("description"));
                }
                recordTable.addRow(columnData);
            } 
        } catch (SQLException ex) {
            Logger.getLogger(app.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteAll() throws SQLException {
        DefaultTableModel recordTable = (DefaultTableModel)jTable1.getModel();
        int selectedRows = jTable1.getSelectedRow();
        
        int dialog= JOptionPane.showConfirmDialog(null, "Tüm kayıları silmek istediğinize emin misiniz?", "Uyarı", JOptionPane.YES_NO_OPTION);
        
        if(dialog == JOptionPane.YES_OPTION) {
            
            String sql = "DELETE FROM product";
            statement = dbHelper.getConnection().prepareStatement(sql);
          
            statement.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Silme İşlemi Başarılı");
        }
        else {
            JOptionPane.getRootFrame().dispose();
        }
    }
    
    public void clean() {
        
        tf_stock_code.setText("");
        tf_stock_name.setText("");
        cb_stock_type.setSelectedIndex(0);
        cb_unit.setSelectedItem(null);
        tf_barcode.setText("");
        cb_VAT_type.setSelectedIndex(0);
        ftf_creation_date.setCalendar(null);
        ta_description.setText("");
        
        tf_stock_code.setEditable(true);
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        dataPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        tf_stock_code = new javax.swing.JTextField();
        tf_stock_name = new javax.swing.JTextField();
        cb_stock_type = new javax.swing.JComboBox<>();
        cb_unit = new javax.swing.JComboBox<>();
        tf_barcode = new javax.swing.JTextField();
        cb_VAT_type = new javax.swing.JComboBox<>();
        ftf_creation_date = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        ta_description = new javax.swing.JTextArea();
        tablePanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        tf_search_bar = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        up_button = new javax.swing.JButton();
        buttonPanel = new javax.swing.JPanel();
        add_button = new javax.swing.JButton();
        delete_button = new javax.swing.JButton();
        update_button = new javax.swing.JButton();
        copy_button = new javax.swing.JButton();
        clean_button = new javax.swing.JButton();
        deleteAll_button = new javax.swing.JButton();
        hideButtonPanel = new javax.swing.JPanel();
        down_button = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(156, 156, 156));
        jPanel1.setAlignmentX(0.0F);
        jPanel1.setAlignmentY(0.0F);

        dataPanel.setBackground(new java.awt.Color(156, 156, 156));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Stok Adı:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Stok Kodu:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Stok Tipi:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("Stok Birimi:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setText("Barkod:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel6.setText("KDV Tipi:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel7.setText("Oluşturma Tarihi:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel8.setText("Açıklama:");

        tf_stock_code.setSelectionColor(new java.awt.Color(51, 51, 51));

        cb_stock_type.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { null, "Süt ve Süt Ürünleri", "Et ve Et Ürünleri", "Yağ", "Meyve ve Sebze Ürünleri", "Konserve Gıda", "Dondurulmuş Gıda", "Kuru Gıda", "Unlu Mamüller", "Su Ürünleri"}));

        cb_unit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { null, "Litre", "Kilogram", "Adet" }));

        cb_VAT_type.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { null, "0.01", "0.08", "0.18"}));

        ta_description.setColumns(20);
        ta_description.setRows(5);
        jScrollPane1.setViewportView(ta_description);

        javax.swing.GroupLayout dataPanelLayout = new javax.swing.GroupLayout(dataPanel);
        dataPanel.setLayout(dataPanelLayout);
        dataPanelLayout.setHorizontalGroup(
            dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dataPanelLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(tf_stock_name)
                    .addComponent(cb_stock_type, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cb_unit, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tf_barcode, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cb_VAT_type, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ftf_creation_date, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
                    .addComponent(tf_stock_code))
                .addGap(47, 47, 47))
        );
        dataPanelLayout.setVerticalGroup(
            dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dataPanelLayout.createSequentialGroup()
                .addContainerGap(32, Short.MAX_VALUE)
                .addGroup(dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tf_stock_code, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(14, 14, 14)
                .addGroup(dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tf_stock_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cb_stock_type, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cb_unit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(tf_barcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cb_VAT_type, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ftf_creation_date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(24, 24, 24))
        );

        tablePanel.setBackground(new java.awt.Color(156, 156, 156));

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setBackground(new java.awt.Color(226, 229, 222));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Stok Kodu", "Stok Adı", "Stok Tipi", "Stok Birimi", "Barkod", "KDV Tipi", "Oluşturma Tarihi", "Açıklama"
            }
        ));
        jTable1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTable1.setGridColor(new java.awt.Color(121, 121, 121));
        jTable1.setSelectionBackground(new java.awt.Color(128, 128, 128));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTable1);

        tf_search_bar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tf_search_barKeyReleased(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jLabel9.setText("Ara:");

        up_button.setBackground(new java.awt.Color(153, 153, 153));
        up_button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/up-arrow (1).png"))); // NOI18N
        up_button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                up_buttonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout tablePanelLayout = new javax.swing.GroupLayout(tablePanel);
        tablePanel.setLayout(tablePanelLayout);
        tablePanelLayout.setHorizontalGroup(
            tablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
            .addGroup(tablePanelLayout.createSequentialGroup()
                .addComponent(up_button, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tf_search_bar, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );
        tablePanelLayout.setVerticalGroup(
            tablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tablePanelLayout.createSequentialGroup()
                .addGroup(tablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tf_search_bar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(up_button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 639, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        buttonPanel.setBackground(new java.awt.Color(156, 156, 156));

        add_button.setBackground(new java.awt.Color(167, 167, 167));
        add_button.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        add_button.setForeground(new java.awt.Color(51, 51, 51));
        add_button.setText("Yeni Kayıt Ekle");
        add_button.setBorder(null);
        add_button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                add_buttonMouseClicked(evt);
            }
        });

        delete_button.setBackground(new java.awt.Color(167, 167, 167));
        delete_button.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        delete_button.setForeground(new java.awt.Color(51, 51, 51));
        delete_button.setText("Kaydı Sil");
        delete_button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                delete_buttonMouseClicked(evt);
            }
        });

        update_button.setBackground(new java.awt.Color(167, 167, 167));
        update_button.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        update_button.setForeground(new java.awt.Color(51, 51, 51));
        update_button.setText("Kaydı Güncelle");
        update_button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                update_buttonMouseClicked(evt);
            }
        });

        copy_button.setBackground(new java.awt.Color(167, 167, 167));
        copy_button.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        copy_button.setForeground(new java.awt.Color(51, 51, 51));
        copy_button.setText("Kaydı Kopyala");
        copy_button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                copy_buttonMouseClicked(evt);
            }
        });

        clean_button.setBackground(new java.awt.Color(167, 167, 167));
        clean_button.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        clean_button.setForeground(new java.awt.Color(51, 51, 51));
        clean_button.setText("Temizle");
        clean_button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                clean_buttonMouseClicked(evt);
            }
        });

        deleteAll_button.setBackground(new java.awt.Color(167, 167, 167));
        deleteAll_button.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        deleteAll_button.setForeground(new java.awt.Color(51, 51, 51));
        deleteAll_button.setText("Tüm Kayıtları Sil");
        deleteAll_button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                deleteAll_buttonMouseClicked(evt);
            }
        });
        deleteAll_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteAll_buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buttonPanelLayout = new javax.swing.GroupLayout(buttonPanel);
        buttonPanel.setLayout(buttonPanelLayout);
        buttonPanelLayout.setHorizontalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonPanelLayout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(add_button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(delete_button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(update_button, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                    .addComponent(copy_button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(clean_button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(deleteAll_button, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        buttonPanelLayout.setVerticalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonPanelLayout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addComponent(add_button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(delete_button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(update_button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(copy_button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(deleteAll_button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(clean_button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(74, Short.MAX_VALUE))
        );

        hideButtonPanel.setBackground(new java.awt.Color(156, 156, 156));

        down_button.setBackground(new java.awt.Color(153, 153, 153));
        down_button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/down-arrow (1).png"))); // NOI18N
        down_button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                down_buttonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout hideButtonPanelLayout = new javax.swing.GroupLayout(hideButtonPanel);
        hideButtonPanel.setLayout(hideButtonPanelLayout);
        hideButtonPanelLayout.setHorizontalGroup(
            hideButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hideButtonPanelLayout.createSequentialGroup()
                .addComponent(down_button, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 26, Short.MAX_VALUE))
        );
        hideButtonPanelLayout.setVerticalGroup(
            hideButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hideButtonPanelLayout.createSequentialGroup()
                .addComponent(down_button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 11, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(hideButtonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(100, 100, 100)
                        .addComponent(dataPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(98, 98, 98))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(hideButtonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dataPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void delete_buttonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_delete_buttonMouseClicked
        if (tf_stock_code.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Silinecek Kaydı Seçiniz");
        }
        
        else {
            try {
                delete();
                list();
                clean();
            } catch (SQLException ex) {
                Logger.getLogger(app.class.getName()).log(Level.SEVERE, null, ex);
            }
        }  
    }//GEN-LAST:event_delete_buttonMouseClicked

    private void add_buttonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_add_buttonMouseClicked
        if (tf_stock_code.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Stok Kodu Alanı Boş Bırakılamaz");
        }
        
        else if (tf_stock_name.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Stok Adı Alanı Boş Bırakılamaz");
        }
        
        else if (cb_stock_type.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Stok Tipi Alanı Boş Bırakılamaz");
        }
        
        else if (cb_unit.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Stok Birimi Alanı Boş Bırakılamaz");
        }
        
        else if (tf_barcode.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Barkod Alanı Boş Bırakılamaz");
        }
        
        else if (cb_VAT_type.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "KDV Tipi Alanı Boş Bırakılamaz");
        }
        
        else if (ftf_creation_date.getDate() == null) {
            JOptionPane.showMessageDialog(null, "Oluşturma Tarihi Alanı Boş Bırakılamaz");
        }
        
        else if (ta_description.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Açıklama Alanı Boş Bırakılamaz");
        }
        
        else {
            try {
                insert();
                list();
                clean();
            } catch (SQLException ex) {
                Logger.getLogger(app.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_add_buttonMouseClicked

    private void update_buttonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_update_buttonMouseClicked
        if (tf_stock_code.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Stok Kodu Alanı Boş Bırakılamaz");
        }
        
        else if (tf_stock_name.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Stok Adı Alanı Boş Bırakılamaz");
        }
        
        else if (cb_stock_type.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Stok Tipi Alanı Boş Bırakılamaz");
        }
        
        else if (cb_unit.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Stok Birimi Alanı Boş Bırakılamaz");
        }
        
        else if (tf_barcode.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Barkod Alanı Boş Bırakılamaz");
        }
        
        else if (cb_VAT_type.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "KDV Tipi Alanı Boş Bırakılamaz");
        }
        
        else if (ftf_creation_date.getDate() == null) {
            JOptionPane.showMessageDialog(null, "Oluşturma Tarihi Alanı Boş Bırakılamaz");
        }
        
        else if (ta_description.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Açıklama Alanı Boş Bırakılamaz");
        }
        
        else {
            update();
            list();
            clean();
        }       
    }//GEN-LAST:event_update_buttonMouseClicked

    private void copy_buttonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_copy_buttonMouseClicked
        if (tf_stock_code.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Kopyalanacak Kaydı Seçiniz");
        }    
        else {
            try {
                copy();
                list();
                clean();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Bu Stok Kodu Zaten Kayıtlı");
            }
        }      
    }//GEN-LAST:event_copy_buttonMouseClicked

    private void clean_buttonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clean_buttonMouseClicked
        clean();
        list();
    }//GEN-LAST:event_clean_buttonMouseClicked

    private void down_buttonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_down_buttonMouseClicked
        buttonPanel.setVisible(true);
        dataPanel.setVisible(true);
        up_button.setVisible(true);
        hideButtonPanel.setVisible(false);
    }//GEN-LAST:event_down_buttonMouseClicked

    private void up_buttonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_up_buttonMouseClicked
        buttonPanel.setVisible(false);
        dataPanel.setVisible(false);
        up_button.setVisible(false);
        hideButtonPanel.setVisible(true);
    }//GEN-LAST:event_up_buttonMouseClicked

    private void tf_search_barKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tf_search_barKeyReleased
        search(tf_search_bar.getText());
    }//GEN-LAST:event_tf_search_barKeyReleased

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        try {
            DefaultTableModel recordTable = (DefaultTableModel)jTable1.getModel();
            int selectedRows = jTable1.getSelectedRow();

            Date date = new SimpleDateFormat("yyyy-MM-dd").parse((String)recordTable.getValueAt(selectedRows, 6).toString());

            tf_stock_code.setText(recordTable.getValueAt(selectedRows, 0).toString());
            tf_stock_code.setEditable(false);
            tf_stock_name.setText(recordTable.getValueAt(selectedRows, 1).toString());
            cb_stock_type.setSelectedIndex(recordTable.getValueAt(selectedRows, 2).hashCode());
            cb_unit.setSelectedItem(recordTable.getValueAt(selectedRows, 3).toString());
            tf_barcode.setText(recordTable.getValueAt(selectedRows, 4).toString());
            cb_VAT_type.setSelectedItem(recordTable.getValueAt(selectedRows, 5).toString());
            ftf_creation_date.setDate(date);
            ta_description.setText(recordTable.getValueAt(selectedRows, 7).toString());

        } catch (ParseException ex) {
            Logger.getLogger(app.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void deleteAll_buttonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deleteAll_buttonMouseClicked
        try {
            deleteAll();
            list();
        } catch (SQLException ex) {
            Logger.getLogger(app.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_deleteAll_buttonMouseClicked

    private void deleteAll_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteAll_buttonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_deleteAll_buttonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(app.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(app.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(app.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(app.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new app().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton add_button;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JComboBox<String> cb_VAT_type;
    private javax.swing.JComboBox<String> cb_stock_type;
    private javax.swing.JComboBox<String> cb_unit;
    private javax.swing.JButton clean_button;
    private javax.swing.JButton copy_button;
    private javax.swing.JPanel dataPanel;
    private javax.swing.JButton deleteAll_button;
    private javax.swing.JButton delete_button;
    private javax.swing.JButton down_button;
    private com.toedter.calendar.JDateChooser ftf_creation_date;
    private javax.swing.JPanel hideButtonPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea ta_description;
    private javax.swing.JPanel tablePanel;
    private javax.swing.JTextField tf_barcode;
    private javax.swing.JTextField tf_search_bar;
    private javax.swing.JTextField tf_stock_code;
    private javax.swing.JTextField tf_stock_name;
    private javax.swing.JButton up_button;
    private javax.swing.JButton update_button;
    // End of variables declaration//GEN-END:variables

    private void createPopupMenu(JFrame frame) {
        menuItem = new JMenuItem("Kaydı Sil");
        menuItem.getAccessibleContext().setAccessibleDescription("Kaydı Sil");
        
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    delete();
                    list();
                    clean();
                } catch (SQLException ex) {
                    Logger.getLogger(app.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        });
        
        popupMenu.add(menuItem);
        
        menuItem = new JMenuItem("Kaydı Kopyala");
        menuItem.getAccessibleContext().setAccessibleDescription("Kaydı Kopyala");
        
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    copy();
                    list();
                    clean();
                } catch (SQLException ex) {
                    Logger.getLogger(app.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        });     
        
        popupMenu.add(menuItem);
        
        menuItem = new JMenuItem("Kaydı Güncelle");
        menuItem.getAccessibleContext().setAccessibleDescription("Kaydı Güncelle");
        
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonPanel.setVisible(true);
                dataPanel.setVisible(true);
                up_button.setVisible(true);
                hideButtonPanel.setVisible(false);
            }
            
        });
        
        popupMenu.add(menuItem);
        
        menuItem = new JMenuItem("Tüm Kayıtları Sil");
        menuItem.getAccessibleContext().setAccessibleDescription("Tüm Kayıtları Sil");
        
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    deleteAll();
                    list();
                } catch (SQLException ex) {
                    Logger.getLogger(app.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        popupMenu.add(menuItem);
        
        jTable1.setComponentPopupMenu(popupMenu);
    }
}
