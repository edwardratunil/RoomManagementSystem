import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class Login {

    private final Properties properties;

    public Login() {
        properties = loadProperties();
    }

    private Properties loadProperties() {
        Properties props = new Properties();
        try (FileInputStream input = new FileInputStream("config.txt")) {
            props.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }

    public void credential() {
        SwingUtilities.invokeLater(() -> {
            JTextField userInput = new JTextField(5);
            JPasswordField passInput = new JPasswordField(5);
            JCheckBox showPassword = new JCheckBox("Show Password");

            JPanel credPanel = new JPanel(new GridLayout(5, 5, 3, 2));

            credPanel.add(new JLabel("Username:"));
            credPanel.add(userInput);

            credPanel.add(new JLabel("Password:"));
            credPanel.add(passInput);
            credPanel.add(showPassword);

            showPassword.addActionListener(e -> {
                if (showPassword.isSelected()) {
                    passInput.setEchoChar((char) 0); // Show password in plain text
                } else {
                    passInput.setEchoChar('*'); // Mask password with '*'
                }
            });

            int choice = JOptionPane.showConfirmDialog(
                    null,
                    credPanel,
                    "Account Login",
                    JOptionPane.OK_CANCEL_OPTION
            );

            if (choice == JOptionPane.OK_OPTION) {
                String username = userInput.getText();
                String password = String.valueOf(passInput.getPassword());

                System.out.println("Attempting login with username: " + username);

                try {
                    boolean accountExists = checkAccount(username, password);

                    if (accountExists) {
                        JOptionPane.showMessageDialog(null, "Login Successful!");
                        System.out.println("Logged In");
                        // Call your main application logic here
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid username or password. Please try again.");
                        System.out.println("Invalid Credential");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error connecting to the database.");
                }
            } else {
                System.out.println("Login Canceled");
            }
        });
    }

    private boolean checkAccount(String username, String password) throws SQLException {
        String jdbcUrl = properties.getProperty("jdbcUrl");
        String dbUsername = properties.getProperty("dbusername");
        String dbPassword = properties.getProperty("dbpassword");

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword)) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next(); // If there is at least one result, the account exists
                }
            }
        }
    }

    public static void main(String[] args) {
        Login loginObject = new Login();
        loginObject.credential();
    }
}
