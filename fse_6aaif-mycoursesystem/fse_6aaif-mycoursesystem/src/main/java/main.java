import daraaccess.MySqlCourseRepository;
import daraaccess.MySqlStudentRepository;
import daraaccess.MysqlDatabaseConnection;
import ui.Cli;

import java.sql.Connection;
import java.sql.SQLException;

public class main {

    public static void main(String[] args) {
        Cli myCli = new Cli(new MySqlCourseRepository() , new MySqlStudentRepository());
        myCli.start();


    }
}
