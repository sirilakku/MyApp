import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm  extends JDialog{
    private JTextField textFieldEmail;
    private JPasswordField passwordFieldPassword;
    private JButton OKButton;
    private JButton cancelButton;
    private JPanel loginPanel;

    public LoginForm(JFrame parent){
        super(parent);
        setTitle("Login");
        setContentPane(loginPanel);
        setMaximumSize(new Dimension(450,474));
        setSize(450,500);
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String emailAddress = textFieldEmail.getText();
                String password = String.valueOf(passwordFieldPassword.getPassword());

                 user = getAutenticatedUser(emailAddress, password);
                 if(user != null){
                     dispose();
                 } else {
                     JOptionPane.showMessageDialog(LoginForm.this, "Email or Password is Invalid");
                 }

            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        setVisible(true);
    }
    public User user;
    private Connection connect = null;

    public User getAutenticatedUser(String emailAddress,String password)  {
        User user = null;
        final String DB_URL = "jdbc:mysql://localhost:3306/organization?";
        final String USERNAME = "root";
        final String PASSWORD = "123";

        try {
            Connection connection = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);

            Statement s = connection.createStatement();
            String sql = "select * from employee where emailAddress = ? and password = ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,emailAddress);
            preparedStatement.setString(2,password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                user = new User();
                user.firstName = resultSet.getString("firstName");
                user.lastName = resultSet.getString("lastName");
                user.emailAddress = resultSet.getString("emailAddress");
                user.password = resultSet.getString("password");
                user.phoneNumber = resultSet.getString("phoneNumber");
            }
            s.close();
            connection.close();


        } catch (Exception e){
            e.printStackTrace();
        }


        return user;
    }

    public static void main(String[] args) {
        LoginForm loginForm = new LoginForm(null);
        User user = loginForm.user;
        if(user != null) {
            System.out.println("Successful Autentication of : " + user.firstName);
            System.out.println("        Email : " + user.emailAddress);
            System.out.println("        Phone : " + user.phoneNumber);

        }else {
            System.out.println("Authentication Canceled");
        }
    }

}
