package domain;
import java.sql.Date;

public class Student extends BaseEntity{
    String vorname, nachname;
    Date geburtstag;

    public Student(Long id, String vorname, String nachname, Date geburtstag)
    {
        super(id);
        this.setGeburtstag(geburtstag);
        this.setNachname(nachname);
        this.setVorname(vorname);

    }
    public Student(String vorname, String nachname, Date geburtstag)
    {
        super(null);
        this.setGeburtstag(geburtstag);
        this.setNachname(nachname);
        this.setVorname(vorname);

    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) throws InvalidValueException {
        if (vorname.length()>0 && vorname!=null) {
            this.vorname = vorname;
        }else {
            throw new InvalidValueException("Vorname darf nicht leer und mind. 1 Zeichen beinhalten!");
        }
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) throws InvalidValueException {
        if (nachname.length()>0 && nachname !=null) {
            this.nachname = nachname;
        }else {
            throw new InvalidValueException("Nachname darf nicht leer und mind. 1 Zeichen beinhalten!");
        }
    }

    public Date getGeburtstag() {
        return geburtstag;
    }

    public void setGeburtstag(Date geburtstag) throws InvalidValueException {
        if (geburtstag == null){
            throw new InvalidValueException("Geburtsdatum darf nicht leer sein!");
        }else {
            this.geburtstag = geburtstag;
        }

    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + this.getId() + '\'' +
                "vorname='" + vorname + '\'' +
                ", nachname='" + nachname + '\'' +
                ", geburtstag=" + geburtstag +
                '}';
    }
}