package daraaccess;

import daraaccess.BaseRepository;
import domain.Student;
import java.sql.Date;
import java.util.List;

public interface MyStudentrepository extends BaseRepository<Student, Long> {
    List<Student> findByName(String name);
    List<Student> findByBirthYear(int year);
}
