package domain;
import java.sql.Date;


public class courses extends BaseEntity {

    private String name;
    private String description;
    private int hours;
    private Date beginDate;
    private Date endDate;
    private CourseTyp courseTyp;



    public courses(Long id, String name, String description, int hours, Date beginDate, Date endDate, CourseTyp courseTyp) throws InvalidValueException {
        super(id);
        this.setName(name);
        this.setDescription(description);
        this.setHours(hours);
        this.setBeginDate(beginDate);
        this.setEndDate(endDate);
        this.setCourseTyp(courseTyp);
    }

    public courses(String name, String description, int hours, Date beginDate, Date endDate, CourseTyp courseTyp) throws InvalidValueException{
        super(null);
        this.setName(name);
        this.setDescription(description);
        this.setHours(hours);
        this.setBeginDate(beginDate);
        this.setEndDate(endDate);
        this.setCourseTyp(courseTyp);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name!=null && name.length() > 1){
            this.name = name;
        }else {
            throw new InvalidValueException("Kursname muss mindesten 2 Zeichen lang sein");
        }

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description!=null && description.length() > 1){
            this.description = description;
        }else {
            throw new InvalidValueException("Beschreibung muss mindesten 2 Zeichen lang sein");
        }
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        if (hours > 0 && hours < 10){
            this.hours = hours;
        }else {
            throw new InvalidValueException("Kursstunden pro Kurs muss zwischen 1 und 10 liegen");
        }

    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        if (endDate == null) {
            throw new InvalidValueException("Enddatum darf nicht leer sein");
        }
        if (this.beginDate == null) {
            throw new InvalidValueException("Kursbeginn muss vor Kursende gesetzt sein");
        }
        if (this.beginDate.after(endDate)) {
            throw new InvalidValueException("Kurssende muss NACH Kursbegin sein");
        }
        this.endDate = endDate;
    }


    public CourseTyp getCourseTyp() {
        return courseTyp;
    }

    public void setCourseTyp(CourseTyp courseTyp) {
        if (courseTyp!=null){
            this.courseTyp = courseTyp;
        }else {
            throw new InvalidValueException("Kurstyp darf nicht null ein");
        }
    }

    @Override
    public String toString() {
        return "courses{" +
                "id=" + this.getId() +'\'' +
                "name='" + name +
                ", description='" + description + '\'' +
                ", hours=" + hours +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
                ", courseTyp=" + courseTyp +
                '}';
    }
}






