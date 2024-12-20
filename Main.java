public class Main {
    public static void main(String[] args) {
        MySqlConnection sqlcon = new MySqlConnection();
        Login lg = new Login();
        sqlcon.mysqlcon();
        lg.credential();
        
    }
    
}