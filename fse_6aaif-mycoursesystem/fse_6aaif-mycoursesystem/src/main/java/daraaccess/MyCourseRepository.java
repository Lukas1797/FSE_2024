package daraaccess;

import domain.CourseTyp;
import domain.courses;

import java.util.Date;
import java.util.List;

public interface MyCourseRepository extends BaseRepository<courses, Long>{


    List<courses> findAllCoursesByName(String name);
    List<courses> findAllCourseByDescription(String description);
    List<courses> findAllCoursesByNameorDescription(String searchText);
    List<courses> findAllCoursesByStartDate(Date startDate);
    List<courses> findAllCoursesByCourseType(CourseTyp CourseTyp );
    List<courses> findAllRunningCourses();



}
