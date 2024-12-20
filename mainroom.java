import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class mainroom {
        private JLabel usernameLabel;


        public void rmsMain(){
        JFrame hframe = new JFrame();   //OverAll Frame
        hframe.setSize(800, 600);
        hframe.setLocationRelativeTo(null);
        hframe.setResizable(false);
        hframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        hframe.setTitle("Room Management System");
        hframe.getContentPane().setBackground( Color.decode("#d8e4e9") ); // set background color
        
        //-------------- LABEL PANEL -----------------//
        JLabel hlabel = new JLabel("Room Management System");
        hlabel.setFont(new Font("Sans", Font.BOLD, 20));
        hlabel.setBounds(80, 15, 300, 50);
        hlabel.setForeground(Color.decode("#20124d"));

        usernameLabel = new JLabel("Hello! " + UserSession.getInstance().getLoggedInUsername() + ",");
        usernameLabel.setFont(new Font("Sans", Font.BOLD, 15)); // users name display
        usernameLabel.setBounds(120, 70, 300, 50);
        usernameLabel.setForeground(Color.decode("#FFBF00"));

        //----------------- LOGO ---------------------//
        JLabel hlogo = new JLabel();    //USTP LOGO
        hlogo.setIcon(new ImageIcon(new ImageIcon("img/USTP.jpg").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH))); //USTP LOGO
        hlogo.setBounds(10,15,50,50);

        JLabel huserlogo = new JLabel();    //User Logo
        huserlogo.setIcon(new ImageIcon(new ImageIcon("img/userW.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH))); // PROFILE PICTURE
        huserlogo.setBounds(70, 70, 300, 50);

        // MenuButton Logout//
        JButton hmenubutton = new JButton();
        hmenubutton.setIcon(new ImageIcon(new ImageIcon("img/menubar.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH))); // MENU LOGO
        hmenubutton.setBounds(10,70,50,50);
        hmenubutton.setBackground(Color.decode("#FFBF00"));
        
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem logoutMenuItem = new JMenuItem("Logout");
        logoutMenuItem.setPreferredSize(new Dimension(100, 30));
        logoutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "User Logout.");
                System.out.println(UserSession.getInstance().getLoggedInUsername() + " Logout");
                hframe.dispose();
            }
        });

        popupMenu.add(logoutMenuItem);

        hmenubutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show the pop-up menu at the button's location
                popupMenu.show(hmenubutton, 0, hmenubutton.getHeight());
                
            }
        });
        //--------------- BFrame -----------------------//
        
        JLabel bckgrnd = new JLabel();
        bckgrnd.setIcon(new ImageIcon(new ImageIcon("img/ustpbk.jpg").getImage().getScaledInstance(765, 570, Image.SCALE_SMOOTH)));
        bckgrnd.setBounds(10,120,765,433);
        
        JPanel shfpanel = new JPanel(); // 1st Orange line top
        shfpanel.setBounds(10,10,765,3);
        shfpanel.setBackground(Color.decode("#FFBF00")); //Orange background

        JPanel hfpanel = new JPanel(); // 2nd background white line
        hfpanel.setBounds(10,10,765,60);
        hfpanel.setBackground(Color.decode("#FFFFFF")); //White background

        JPanel hlogopanel = new JPanel();   //Background of the User Panel
        hlogopanel.setBounds(10,70,765,50);
        hlogopanel.setBackground(Color.decode("#20124d")); //Blue background

        //--------------- CENTER LABEL AND BUTTON ------------------//

        JLabel tagLine = new JLabel("\"Academic Excellence Starts with Well-Planned Appointments.\"");
        tagLine.setFont(new Font("Sans", Font.BOLD, 19));
        tagLine.setBounds(100, 400, 700, 50);
        tagLine.setForeground(Color.decode("#FFFFFF"));

        
        JButton h9Button = new JButton("Reserve Room");
        h9Button.setBounds(330,350,120,50);
        h9Button.setBackground(Color.decode("#FFBF00"));
        h9Button.setForeground(Color.black);
        h9Button.setFocusPainted(false);
        
        h9Button.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0){
                hframe.dispose();
                reserveroom rm = new reserveroom();
                rm.rmsReserve();
            }
        });
      
        //--------------- ADD COMPONENTS -------------//
        hframe.add(huserlogo);
        hframe.add(hlabel);
        hframe.add(hlogo);
        hframe.add(usernameLabel);
        hframe.add(hmenubutton);
        hframe.add(h9Button);
        
        hframe.add(hlogopanel);
        hframe.add(tagLine);
        hframe.add(bckgrnd);
        hframe.add(shfpanel);
        hframe.add(hfpanel);
        
        hframe.setLayout(null);
        hframe.setVisible(true);
    }

}


