package daraaccess;

import domain.CourseTyp;
import domain.DatabaseException;
import domain.Student;
import domain.courses;
import util.Assert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySqlStudentRepository implements MyStudentrepository {

    private Connection con;

    public MySqlStudentRepository() {
        try {
            this.con = MysqlDatabaseConnection.getConnection("jdbc:mysql://localhost:3306/imstkurssystem", "root", "");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Optional<Student> insert(Student entity) {
        Assert.notNull(entity);

        try {
            String sql = "INSERT INTO `student`(`Vorname`, `Nachname`, `Birthdate`) VALUES (?,?,?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.getVorname());
            preparedStatement.setString(2, entity.getNachname());
            preparedStatement.setDate(3, entity.getGeburtstag());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0){
                return Optional.empty();
            }
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()){
                return this.getById(generatedKeys.getLong(1));

            }else {
                return Optional.empty();
            }
        }catch (SQLException sqlException){
            throw new DatabaseException(sqlException.getMessage());
        }

    }


    @Override
    public Optional<Student> getById(Long id) {

        Assert.notNull(id);
        if (countStudentsInDbWithId(id) == 0)
        {
            return Optional.empty();
        }else
        {
            try {
                String sql = "SELECT * FROM `student` WHERE `id` = ?";
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();

                resultSet.next();

                Student student = new Student(
                        resultSet.getString("Vorname"),
                        resultSet.getString("Nachname"),
                        resultSet.getDate("Birthdate")

                        );

                return Optional.of(student);
            }catch (SQLException e){
                throw new DatabaseException(e.getMessage());
            }

        }

    }

    private int countStudentsInDbWithId(Long id){

        try {
            String countSql = "SELECT Count(*) FROM `student`  WHERE `id`=?";
            PreparedStatement preparedStatementCount = null;
            preparedStatementCount = con.prepareStatement(countSql);
            preparedStatementCount.setLong(1, id);
            ResultSet resultSetCount = preparedStatementCount.executeQuery();
            resultSetCount.next();
            int CourseCount = resultSetCount.getInt(1);
            return CourseCount;

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }

    }


    @Override
    public List<Student> getAll() {
        String sql = "SELECT * FROM `student`";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Student> StudentList = new ArrayList<>();
            while (resultSet.next()){
                StudentList.add(new Student(
                        resultSet.getLong("id"),
                        resultSet.getString("Vorname"),
                        resultSet.getString("Nachname"),
                        resultSet.getDate("Birthdate")

                ));
            }

            if (StudentList.isEmpty()) {
                System.out.println("Kurzliste leer!");
            } else {
                return StudentList;
            }

        } catch (SQLException e) {
            throw new DatabaseException("Database error occured" + e.getMessage());
        }
        return null;
    }

    @Override
    public Optional<Student> update(Student entity) {
        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        Assert.notNull(id);
        String sql = "DELETE FROM `student` WHERE `id` = ?";
        try {
            if (countStudentsInDbWithId(id) == 0) {
                return false; // Kein Kurs mit dieser ID vorhanden
            }

            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            int affectedRows = preparedStatement.executeUpdate();

            return affectedRows > 0; // True, wenn mind. eine Zeile betroffen ist
        } catch (SQLException sqlException){
            throw new DatabaseException(sqlException.getMessage());
        }
    }



    @Override
    public List<Student> findByName(String name) {
        try {
            String sql = "SELECT * FROM `courses` WHERE LOWER(`description`) LIKE LOWER(?) OR LOWER(`name`) LIKE LOWER(?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, "%"+name+"%");
            preparedStatement.setString(2, "%"+name+"%");
            ResultSet resultSet = preparedStatement.executeQuery();

            ArrayList<Student> StudentList = new ArrayList<>();
            while (resultSet.next()){
                StudentList.add(new Student(
                        resultSet.getString("Vorname"),
                        resultSet.getString("Nachname"),
                        resultSet.getDate("Birthdate")
                ));
            }
            return StudentList;

        }catch (SQLException sqlException){
            throw new DatabaseException(sqlException.getMessage())   ;
        }
    }


    @Override
    public List<Student> findByBirthYear(int year) {
        return null;
    }


}
