import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbHelper {
    
    public static Connection getConnection() {
        Connection connect = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = (Connection) DriverManager.getConnection("jdbc:mysql://127.0.0.1/stock_card?useUnicode=true&characterEncoding=UTF8", "root", "");
        } catch (ClassNotFoundException | SQLException exception) {
            System.out.println(exception.getMessage());
        }
            
        return connect;
    }
    
        public void showError(SQLException exception) {
            System.out.println("Error: " + exception.getMessage());
            System.out.println("Error Code: " + exception.getErrorCode());
        }
}
