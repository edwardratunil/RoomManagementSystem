import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

public class reserveroom {

    private JLabel usernameLabel;
    private JComboBox<String> roomComboBox;
    private JComboBox<String> hourComboBox;
    private JComboBox<String> minuteComboBox;
    private JComboBox<String> apmComboBox;
    private JComboBox<String> hour2ComboBox;
    private JComboBox<String> minute2ComboBox;
    private JComboBox<String> apm2ComboBox;
    private JDateChooser dateChooser;
    private JTable table = new JTable();
    private JButton setlabel = new JButton("ROOM101"); // button default name
    public JButton deleteButton;
    int selectedReservationID;

    public void deleteReservation() {
        String jdbcUrl = "jdbc:mysql://localhost:3306/rms";
        String dbusername = "root";
        String dbpassword = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(jdbcUrl, dbusername, dbpassword)) {
                String deleteQuery = "DELETE FROM resroom WHERE id = ?";

                try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                    preparedStatement.setInt(1, selectedReservationID);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Reservation deleted successfully!");
                        checkData(); // Refresh the table after deletion
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to delete reservation.");
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }


    public void checkData() {
        String jdbcUrl = "jdbc:mysql://localhost:3306/rms";
        String dbusername = "root";
        String dbpassword = "";
    
        // Get the selected date from the JDateChooser
        Date selectedDate = dateChooser.getDate();
    
        if (selectedDate != null) {
            // Format the date to display the "MM-dd-yyyy" format
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
            String formattedDate = dateFormat.format(selectedDate);
    
            String room = (String) roomComboBox.getSelectedItem();
    
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                try (Connection connection = DriverManager.getConnection(jdbcUrl, dbusername, dbpassword)) {
                    String selectQuery = "SELECT * FROM resroom WHERE room = ? AND date = ?";
    
                    try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                        preparedStatement.setString(1, room);
                        preparedStatement.setString(2, formattedDate);
    
                        try (ResultSet resultSet = preparedStatement.executeQuery()) {
                            DefaultTableModel model = (DefaultTableModel) table.getModel();
                            model.setRowCount(0); // Clear existing rows

                            String selectedText = (String) roomComboBox.getSelectedItem();
                            setlabel.setText(selectedText);
    
                            while (resultSet.next()) {

                                // Retrieve reservation details
                                int id = resultSet.getInt("id");
                                String username = resultSet.getString("username");
                                String time = resultSet.getString("time");
    
                                // Add the details to the table
                                model.addRow(new Object[]{id, room, username, time, formattedDate});
                            }

                            int rowCount = table.getRowCount();
                            setlabel.setText((String) roomComboBox.getSelectedItem());
                            
                            if (rowCount >= 0 && rowCount <= 5) {
                                setlabel.setBackground(Color.decode("#00FF00"));  // Green
                            }
                            
                            else if (rowCount >= 6 && rowCount <= 9) {
                                setlabel.setBackground(Color.YELLOW);
                            }
                            
                            else {
                                setlabel.setBackground(Color.RED);
    }
                        }
                    }
                }
            } catch (ClassNotFoundException | SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a date before checking availability.");
        }
    }

    
    public void inData() {
        String jdbcUrl = "jdbc:mysql://localhost:3306/rms";
        String dbusername = "root";
        String dbpassword = "";

        String room = (String) roomComboBox.getSelectedItem();
        String selectedhour = (String) hourComboBox.getSelectedItem();
        String selectedminute = (String) minuteComboBox.getSelectedItem();
        String selectedapm = (String) apmComboBox.getSelectedItem();
        String selectedhour2 = (String) hour2ComboBox.getSelectedItem();
        String selectedminute2 = (String) minute2ComboBox.getSelectedItem();
        String selectedapm2 = (String) apm2ComboBox.getSelectedItem();
        String username = UserSession.getInstance().getLoggedInUsername();

        if (dateChooser.getDate() != null) {
            // Get the selected date from the JDateChooser
            Date selectedDate = dateChooser.getDate();
        
            // Format the date to display the "mm-dd-yyyy" format
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
            String formattedDate = dateFormat.format(selectedDate);
            
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                try (Connection connection = DriverManager.getConnection(jdbcUrl, dbusername, dbpassword)) {
                    // Note: ID column is excluded as it is auto-incremented
                    String insertQuery = "INSERT INTO resroom (username, room, date, time) VALUES (?, ?, ?, ?)";
                    
                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                        preparedStatement.setString(1, username);
                        preparedStatement.setString(2, room);
                        preparedStatement.setString(3, formattedDate);
                        preparedStatement.setString(4, selectedhour + ":" + selectedminute + selectedapm + " - " + selectedhour2 + ":" + selectedminute2 + selectedapm2);
                        
                        int rowsAffected = preparedStatement.executeUpdate();
                        
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(null, "Data inserted successfully!");
                        }
                        
                        else {
                            JOptionPane.showMessageDialog(null, "Failed to insert data.");
                        }
                        // Retrieve the auto-generated ID
                        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                        
                        if (generatedKeys.next()) {
                            int autoGeneratedID = generatedKeys.getInt(1);
                            System.out.println("Auto-generated ID: " + autoGeneratedID);
                        }
                    }
                }
            }
            
            catch (ClassNotFoundException | SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        }

        else {
            System.out.println("No date selected.");
            JOptionPane.showMessageDialog(null, "No date selected.");
        }
    }


    public void rmsReserve(){
        JFrame rwhframe = new JFrame(); // Overall Frame
        JPanel rwshfpanel = new JPanel();
        JPanel rwhfpanel = new JPanel();
        JPanel rwResDate = new JPanel();
        JPanel reservePanel = new JPanel();
        JLabel rwhlogo = new JLabel(); // USTP LOGO
        JLabel rwhlabel = new JLabel("Room Management System");
        usernameLabel = new JLabel("Hello! " + UserSession.getInstance().getLoggedInUsername() + ",");
        JLabel rwhuserlogo = new JLabel(); // User Logo
        JButton rwhmenubutton = new JButton();
        JPanel rwhlogopanel = new JPanel(); // Background of the User Panel
        

        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        //-------------- BUTTONS ---------------------//
        
        JButton backButton = new JButton();
        backButton.setIcon(new ImageIcon(new ImageIcon("img/back.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH))); //USTP LOGO
        backButton.setBounds(10,70,50,50);
        backButton.setBackground(Color.decode("#F1C232"));
        backButton.setFocusPainted(false);

        backButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0){
                rwhframe.dispose();
                mainroom mr = new mainroom();
                mr.rmsMain();
            }
        });

        //-------------- LABEL PANEL -----------------//
        rwhlabel.setFont(new Font("Sans", Font.BOLD, 20));
        rwhlabel.setBounds(80, 15, 300, 50);
        rwhlabel.setForeground(Color.decode("#20124d")); //Blue background

        usernameLabel.setFont(new Font("Sans", Font.BOLD, 15)); // users name display
        usernameLabel.setBounds(120, 70, 300, 50);
        usernameLabel.setForeground(Color.decode("#FFBF00")); //Orange background


        //----------------- LOGO ---------------------//

        rwhlogo.setIcon(new ImageIcon(new ImageIcon("img/USTP.jpg").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH))); //USTP LOGO
        rwhlogo.setBounds(10,15,50,50);

        rwhuserlogo.setIcon(new ImageIcon(new ImageIcon("img/userW.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH))); // PROFILE PICTURE
        rwhuserlogo.setBounds(70, 70, 300, 50);
    
        //--------------- LOGO PANEL ------------------//
        JLabel rwbckgrnd = new JLabel();
        rwbckgrnd.setIcon(new ImageIcon(new ImageIcon("img/ustpbk.jpg").getImage().getScaledInstance(765, 570, Image.SCALE_SMOOTH)));
        rwbckgrnd.setBounds(10,120,765,433);

        rwshfpanel.setBounds(10,10,765,3);
        rwshfpanel.setBackground(Color.decode("#FFBF00")); //Orange background

        rwhfpanel.setBounds(10,10,765,600);
        rwhfpanel.setBackground(Color.decode("#FFFFFF")); //White background

        rwhlogopanel.setBounds(10,70,765,50);
        rwhlogopanel.setBackground(Color.decode("#20124d")); //Blue background

        //------------- Table ------------//
        reservePanel.setBounds(50,170,410,5);
        reservePanel.setBackground(Color.decode("#FFBF00")); //Orange background

        JPanel Tpanel = new JPanel(new BorderLayout());
        Tpanel.setBounds(50,175,410,275);

        DefaultTableModel model = new DefaultTableModel();
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        model.addColumn("ID");
        model.addColumn("ROOM");
        model.addColumn("NAME");
        model.addColumn("TIME");
        model.addColumn("DATE");

        Tpanel.add(scrollPane, BorderLayout.CENTER);
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(10);
        columnModel.getColumn(3).setPreferredWidth(150);

        //-------------

        rwResDate.setBounds(500,175,250,270);
        rwResDate.setBackground(Color.decode("#FFFFFF")); //White background
        Border lineBorder = BorderFactory.createLineBorder(Color.decode("#999999"), 1);
        rwResDate.setBorder(lineBorder);

        JPanel rwResDateL = new JPanel();
        rwResDateL.setBounds(500,170,250,5);
        rwResDateL.setBackground(Color.decode("#FFBF00")); //Orange background

        //---------- ROOM BUTTON IDENTIFIER ------------------//
        
        setlabel.setFont(new Font("Sans", Font.BOLD, 12));
        setlabel.setBounds(50,130,100,30);
        setlabel.setBackground(Color.decode("#00FF00"));//Green background
        setlabel.setForeground(Color.decode("#000000"));
        setlabel.setFocusPainted(false);
        //setlabel.setEnabled(false);

        //----------------- DATE CHOOSER ---------------------//

        // Create a JComboBox with month names
        JLabel mlabel = new JLabel("Select Date: ");
        mlabel.setFont(new Font("Sans", Font.BOLD, 14));
        mlabel.setBounds(510, 190, 150, 20);
        mlabel.setForeground(Color.decode("#20124d"));
        
        dateChooser = new JDateChooser();
        dateChooser.setBounds(620, 185, 120, 30); // Adjust the values as needed
        JTextFieldDateEditor editor = (JTextFieldDateEditor) dateChooser.getDateEditor();
        editor.setEditable(false);


        //----------------- Room ------------//
        JLabel roomlabel = new JLabel("Room: ");
        roomlabel.setFont(new Font("Sans", Font.BOLD, 14));
        roomlabel.setBounds(510, 235, 150, 20);
        roomlabel.setForeground(Color.decode("#20124d"));

        String[] room = {"ROOM101", "ROOM102", "ROOM103", "ROOM104", "ROOM105", "ROOM106", "ROOM107", "ROOM108"};
        roomComboBox = new JComboBox<>(room);
        roomComboBox.setSelectedIndex(0);
        roomComboBox.getSelectedItem();
        roomComboBox.setBounds(620, 230, 120, 30); // Adjust the values as needed

        //---------------- Check Availability ------.//

        JButton chckAButton = new JButton("Check Availability");
        chckAButton.setFont(new Font("Sans", Font.BOLD, 12));
        chckAButton.setBounds(600, 275, 140, 30);
        chckAButton.setBackground(Color.decode("#FFBF00"));
        chckAButton.setForeground(Color.decode("#20124d"));

        chckAButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                checkData();
                //System.out.println("You clicked me");
                
            }
        });

        //---------------- Time --------------//

        JLabel startLabel = new JLabel("Time Start: ");
        startLabel.setFont(new Font("Sans", Font.BOLD, 14));
        startLabel.setBounds(510, 325, 150, 20);
        startLabel.setForeground(Color.decode("#20124d"));
        
        String[] hour = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        hourComboBox = new JComboBox<>(hour);
        hourComboBox.setSelectedIndex(0);
        hourComboBox.getSelectedItem();
        hourComboBox.setBounds(600, 320, 40, 30);   

        String[] minute = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
                            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
                            "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
                            "31", "32", "33", "34", "35", "36", "37", "38", "39", "40",
                            "41", "42", "43", "44", "45", "46", "47", "48", "49", "50",
                            "51", "52", "53", "54", "55", "56", "57", "58", "59"};
        minuteComboBox = new JComboBox<>(minute);
        minuteComboBox.setSelectedIndex(0);
        minuteComboBox.getSelectedItem();
        minuteComboBox.setBounds(645, 320, 40, 30);

        String[] apm = {"AM", "PM"};
        apmComboBox = new JComboBox<>(apm);
        apmComboBox.setSelectedIndex(0);
        apmComboBox.getSelectedItem();
        apmComboBox.setBounds(690, 320, 50, 30);
        
        JLabel endLabel = new JLabel("Time End: ");
        endLabel.setFont(new Font("Sans", Font.BOLD, 14));
        endLabel.setBounds(510, 365, 150, 20);
        endLabel.setForeground(Color.decode("#20124d"));

        String[] hour2 = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        hour2ComboBox = new JComboBox<>(hour2);
        hour2ComboBox.setSelectedIndex(0);
        hour2ComboBox.getSelectedItem();
        hour2ComboBox.setBounds(600, 360, 40, 30);
        
        String[] minute2 = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
                            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
                            "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
                            "31", "32", "33", "34", "35", "36", "37", "38", "39", "40",
                            "41", "42", "43", "44", "45", "46", "47", "48", "49", "50",
                            "51", "52", "53", "54", "55", "56", "57", "58", "59"};
        minute2ComboBox = new JComboBox<>(minute2);
        minute2ComboBox.setSelectedIndex(0);
        minute2ComboBox.getSelectedItem();
        minute2ComboBox.setBounds(645, 360, 40, 30);

        String[] apm2 = {"AM", "PM"};
        apm2ComboBox = new JComboBox<>(apm2);
        apm2ComboBox.setSelectedIndex(0);
        apm2ComboBox.getSelectedItem();
        apm2ComboBox.setBounds(690, 360, 50, 30);

        JButton rButton = new JButton("Reserve Room");
        rButton.setFont(new Font("Sans", Font.BOLD, 12));
        rButton.setBounds(600, 405, 140, 30);
        rButton.setBackground(Color.decode("#FFBF00"));
        rButton.setForeground(Color.decode("#20124d"));

        rButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                inData();
                
            }
        });

        deleteButton = new JButton("Delete Reservation");
        deleteButton.setBounds(180, 470, 160, 30);
        deleteButton.setBackground(Color.decode("#FFBF00"));
        deleteButton.setForeground(Color.decode("#20124d"));
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
        
                if (selectedRow != -1) {
                    Integer reservationID = (Integer) table.getValueAt(selectedRow, 0);
                    selectedReservationID = reservationID.intValue();
                    try {
                        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this reservation?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            deleteReservation();
                        }
                    }
                    
                    catch (NumberFormatException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error parsing reservation ID: " + ex.getMessage());
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null, "Please select a reservation to delete.");
                }
            }
        });
        
        
        


        //--------------- FRAME SETTINGS --------------//
        rwhframe.setSize(800, 600);
        rwhframe.setLocationRelativeTo(null);
        rwhframe.setResizable(false);
        rwhframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        rwhframe.setTitle("Room Management System");
        rwhframe.getContentPane().setBackground(Color.decode("#d8e4e9")); // set background color

        //--------------- ADD COMPONENTS -------------//
        rwhframe.add(rwhuserlogo);
        rwhframe.add(rwhlabel);
        rwhframe.add(rwhlogo);
        rwhframe.add(usernameLabel);
        rwhframe.add(rwhmenubutton);
        rwhframe.add(backButton);
        rwhframe.add(mlabel);
        rwhframe.add(roomlabel);
        rwhframe.add(roomComboBox);
        rwhframe.add(chckAButton);
        rwhframe.add(hourComboBox);
        rwhframe.add(minuteComboBox);
        rwhframe.add(apmComboBox);
        rwhframe.add(startLabel);
        rwhframe.add(endLabel);
        rwhframe.add(hour2ComboBox);
        rwhframe.add(minute2ComboBox);
        rwhframe.add(apm2ComboBox);
        rwhframe.add(rButton);
        rwhframe.add(deleteButton);

        rwhframe.add(setlabel);
        rwhframe.add(dateChooser);
        rwhframe.add(rwhlogopanel);
        
        rwhframe.add(rwshfpanel);
        rwhframe.add(Tpanel);
        rwhframe.add(reservePanel);
        rwhframe.add(rwbckgrnd);
        
        
        rwhframe.add(rwResDate);
        rwhframe.add(rwResDateL);
        
        rwhframe.add(rwhfpanel);
        
        rwhframe.setLayout(null);
        rwhframe.setVisible(true);

    }

}
