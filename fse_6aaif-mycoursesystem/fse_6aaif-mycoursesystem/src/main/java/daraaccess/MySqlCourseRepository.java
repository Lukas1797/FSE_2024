package daraaccess;

import domain.CourseTyp;
import domain.DatabaseException;
import domain.courses;
import util.Assert;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class MySqlCourseRepository implements MyCourseRepository{

    private Connection con;

    public MySqlCourseRepository() {

        try {
            this.con = MysqlDatabaseConnection.getConnection("jdbc:mysql://localhost:3306/imstkurssystem", "root", "");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<courses> insert(courses entity) {

        Assert.notNull(entity);
        try {
            String sql = "INSERT INTO `courses`(`name`, `description`, `hours`, `begindate`, `enddate`, `coursetype`) VALUES (?,?,?,?,?,?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getDescription());
            preparedStatement.setInt(3, entity.getHours());
            preparedStatement.setDate(4, entity.getBeginDate());
            preparedStatement.setDate(5, entity.getEndDate());
            preparedStatement.setString(6, entity.getCourseTyp().toString());

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
    public Optional<courses> getById(Long id) {
        Assert.notNull(id);
        if (countCoursesInDbWithId(id) == 0)
        {
            return Optional.empty();
        }else
        {
            try {
                String sql = "SELECT * FROM `courses` WHERE `id` = ?";
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();

                resultSet.next();

                courses course = new courses(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getInt("hours"),
                        resultSet.getDate("begindate"),
                        resultSet.getDate("enddate"),
                        CourseTyp.valueOf(resultSet.getString("coursetype")
                        )
                );
                return Optional.of(course);
            }catch (SQLException e){
                throw new DatabaseException(e.getMessage());
            }

        }

    }

    private int countCoursesInDbWithId(Long id){

        try {
            String countSql = "SELECT Count(*) FROM `courses`  WHERE `id`=?";
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
    public List<courses> getAll() {
        String sql = "SELECT * FROM `courses`";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<courses> courseList = new ArrayList<>();
            while (resultSet.next()){
                courseList.add(new courses(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getInt("hours"),
                        resultSet.getDate("begindate"),
                        resultSet.getDate("endDate"),
                        CourseTyp.valueOf(resultSet.getString("coursetype"))
                ));
            }

            if (courseList.isEmpty()) {
                System.out.println("Kurzliste leer!");
            } else {
                return courseList;
            }

        } catch (SQLException e) {
            throw new DatabaseException("Database error occured" + e.getMessage());
        }
        return null;
    }

    @Override
    public Optional<courses> update(courses entity) {

        Assert.notNull(entity);

        String sql = "UPDATE `courses` SET `name` = ?, `description` = ?, `hours` = ?, `begindate` = ?, `enddate` = ?, `coursetype` = ? WHERE `courses`.`id` = ?";

        if (countCoursesInDbWithId(entity.getId())==0){
            return Optional.empty();
        }else {
            try {
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, entity.getName());
                preparedStatement.setString(2, entity.getDescription());
                preparedStatement.setInt(3, entity.getHours());
                preparedStatement.setDate(4, entity.getBeginDate());
                preparedStatement.setDate(5, entity.getEndDate());
                preparedStatement.setString(6, entity.getCourseTyp().toString());
                preparedStatement.setLong(7, entity.getId());

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0){
                    return Optional.empty();
                }else {
                    return this.getById(entity.getId());
                }

            }catch (SQLException sqlException){
                throw new DatabaseException(sqlException.getMessage());
            }
        }

    }

    @Override
    public boolean deleteById(Long id) {
        Assert.notNull(id);
        String sql = "DELETE FROM `courses` WHERE `id` = ?";
        try {
            if (countCoursesInDbWithId(id) == 0) {
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
    public List<courses> findAllCoursesByName(String name) {
        return null;
    }

    @Override
    public List<courses> findAllCourseByDescription(String description) {
        return null;
    }

    @Override
    public List<courses> findAllCoursesByNameorDescription(String searchText) {

        try {
            String sql = "SELECT * FROM `courses` WHERE LOWER(`description`) LIKE LOWER(?) OR LOWER(`name`) LIKE LOWER(?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, "%"+searchText+"%");
            preparedStatement.setString(2, "%"+searchText+"%");
            ResultSet resultSet = preparedStatement.executeQuery();

            ArrayList<courses> coursesArrayList = new ArrayList<>();
            while (resultSet.next()){
                coursesArrayList.add(new courses(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getInt("hours"),
                        resultSet.getDate("begindate"),
                        resultSet.getDate("endDate"),
                        CourseTyp.valueOf(resultSet.getString("coursetype"))
                    )
                );
            }
            return coursesArrayList;

        }catch (SQLException sqlException){
         throw new DatabaseException(sqlException.getMessage())   ;
        }
    }

    @Override
    public List<courses> findAllCoursesByStartDate(Date startDate) {
        return null;
    }

    @Override
    public List<courses> findAllCoursesByCourseType(CourseTyp CourseTyp) {
        return null;
    }

    @Override
    public List<courses> findAllRunningCourses() {

        String sql = "SELECT * FROM `courses` WHERE NOW() < enddate ";

        try {


        PreparedStatement preparedStatement = con.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<courses> coursesArrayList = new ArrayList<>();

        while (resultSet.next()){
            coursesArrayList.add(new courses(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getInt("hours"),
                    resultSet.getDate("begindate"),
                    resultSet.getDate("endDate"),
                    CourseTyp.valueOf(resultSet.getString("coursetype"))
            )
            );
        }
        return coursesArrayList;
        }catch (SQLException sqlException){
            throw new DatabaseException("Datenbankfehler " + sqlException.getMessage());
        }
    }
}
