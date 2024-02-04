package ui;

import daraaccess.MyCourseRepository;
import daraaccess.MySqlStudentRepository;
import domain.*;

import java.security.spec.ECField;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.sql.Date;

public class Cli {
    Scanner scanner;
    MyCourseRepository repo;
    MySqlStudentRepository studirepo;

    public Cli(MyCourseRepository repo, MySqlStudentRepository studirepo) {
        this.scanner = new Scanner(System.in);
        this.repo = repo;
        this.studirepo = studirepo;
    }



    public void start(){
        String input = "-";
        while (!input.equals("x")) {
            showMenue();
            input = scanner.nextLine();
            switch (input) {
                case "1":
                    addCourse();
                    break;
                case "2":
                    showAllCourses();
                    break;
                case "3":
                    showCourseDetails();
                    break;
                case "4":
                    upadteCourseDeatails();
                    break;
                case "5":
                    deleteCourse();
                    break;
                case "6":
                    courseSearch();
                    break;
                case "7":
                    runningCourses();
                    break;
                case "8":
                    showAllStudent();
                    break;
                case "9":
                    addStudent();
                    break;
                case "10":
                    deleteStudent();
                    break;
                case "x":
                    System.out.println("Auf Wiedersehen");
                    break;
                default:
                    inputError();
                    break;

            }
        }
        scanner.close();
        }

    private void showAllStudent() {

        List<Student> list = null;

        try {
            list = studirepo.getAll();
            if (list.size() > 0){
                for (Student Student : list){
                    System.out.println(Student);
                }
            }else {
                System.out.println("Kurzliste leer!");
            }
        }catch (DatabaseException databaseException){
            System.out.println("Datenbankfehler beim Anzeigen aller Kurse " + databaseException.getMessage());
        }catch (Exception exception) {
            System.out.println("Unbekannter Fehler bei Anzeige aller Kurse:" + exception.getMessage());
        }

    }



    private void addStudent() {
        System.out.println("Geben Sie die Studentendaten ein:");
        System.out.print("Vorname: ");
        String vorname = scanner.nextLine();

        System.out.print("Nachname: ");
        String nachname = scanner.nextLine();

        System.out.print("Geburtsdatum (YYYY-MM-DD): ");
        Date geburtsdatum = Date.valueOf(scanner.nextLine());

        Student student = new Student(vorname, nachname, geburtsdatum);
        Optional<Student> result = studirepo.insert(student);

        result.ifPresentOrElse(
                s -> System.out.println("Student hinzugefügt: " + s),
                () -> System.out.println("Student konnte nicht hinzugefügt werden.")
        );
    }

    private void deleteStudent() {

        System.out.println("Welchen kurs möchten Sie löschen bitte ID eingeben");
        Long id = Long.parseLong(scanner.nextLine());

        try {
            boolean isDeleted = studirepo.deleteById(id);
            if (isDeleted) {
                System.out.println("Student mit der ID " + id + " wurde erfolgreich gelöscht.");
            } else {
                System.out.println("Student mit der ID " + id + " gefunden.");
            }
        } catch (DatabaseException databaseException){
            System.out.println("Datenbankfehler beim Löschen: " + databaseException.getMessage());
        } catch (Exception e){
            System.out.println("Unbekannter Fehler beim Löschen: " + e.getMessage());
        }
    }

    private void runningCourses() {

        System.out.println("Aktuell laufende Kurse");
        List<courses> list;

        try {
        list = repo.findAllRunningCourses();
        for (courses courses:list){
            System.out.println(courses);
        }
        }catch (DatabaseException databaseException){
            System.out.println("Datenbankfehler bei laufende Kurs Anzeige " + databaseException.getMessage());
        }catch (Exception e){
            System.out.println("Unbekannter fehler bei Kurs-Anzeige für laufende Kurse " + e.getMessage());
        }
    }

    private void courseSearch() {
        System.out.println("Geben Sie einen Suchbegriff an");
        String searchString = scanner.nextLine();
        List<courses> coursesList;
        try {
            coursesList = repo.findAllCoursesByNameorDescription(searchString);
            for (courses courses : coursesList){
                System.out.println(courses);
            }
        }catch (DatabaseException databaseException){
            System.out.println("Datenbankfehler bei der Kurssuche " + databaseException.getMessage());
        }catch (Exception e){
            System.out.println("Unbekannter Fehler bei der Kurssuche " + e.getMessage());
        }
    }

    private void deleteCourse() {
        System.out.println("Welchen kurs möchten Sie löschen bitte ID eingeben");
        Long courseIdToDelete = Long.parseLong(scanner.nextLine());

        try {
            boolean isDeleted = repo.deleteById(courseIdToDelete);
            if (isDeleted) {
                System.out.println("Kurs mit der ID " + courseIdToDelete + " wurde erfolgreich gelöscht.");
            } else {
                System.out.println("Kein Kurs mit der ID " + courseIdToDelete + " gefunden.");
            }
        } catch (DatabaseException databaseException){
            System.out.println("Datenbankfehler beim Löschen: " + databaseException.getMessage());
        } catch (Exception e){
            System.out.println("Unbekannter Fehler beim Löschen: " + e.getMessage());
        }
    }


    private void upadteCourseDeatails() {

        System.out.println("Für welchen Kurs-ID mächsten Sie die Kursdetails ändern");
        Long courseID = Long.parseLong(scanner.nextLine());

        try {
            Optional<courses> coursesOptional = repo.getById(courseID);
            if (coursesOptional.isEmpty()){
                System.out.println("Kurs mit der eingegebenen ID nicht in der Datenbank");
            }else {
                courses courses = coursesOptional.get();
                System.out.println("Änderungen für folgenden Kurs: ");
                System.out.println(courses);

                String name, description, hours, dateFrom, dateTo, courseType;

                System.out.println("Bitte neue Kursdaten eingeben (Enter falls keine Änderungen gewünscht ist");
                System.out.println("Name");
                name = scanner.nextLine();
                System.out.println("BEschreibung");
                description = scanner.nextLine();
                System.out.println("Stundenanzahl");
                hours = scanner.nextLine();
                System.out.println("Startdatum (YYYY-MM-DD)");
                dateFrom = scanner.nextLine();
                System.out.println("Enddatum (YYYY-MM-DD)");
                dateTo = scanner.nextLine();
                System.out.println("Kurstyp: (ZA/BF/FF/DE) ");
                courseType = scanner.nextLine();

                Optional<courses> optionalCourseUpdated = repo.update(
                        new courses(
                                courses.getId(),
                                name.equals("") ? courses.getName() : name,
                                description.equals("") ? courses.getDescription() : description,
                                hours.equals("") ? courses.getHours() : Integer.parseInt(hours),
                                dateFrom.equals("") ? courses.getBeginDate() : Date.valueOf(dateFrom),
                                dateTo.equals("") ? courses.getEndDate() : Date.valueOf(dateTo),
                                courseType.equals("") ? courses.getCourseTyp() : CourseTyp.valueOf(courseType)

                        )
                );

                optionalCourseUpdated.ifPresentOrElse(
                        (c) -> System.out.println("Kurs aktualisiert " + c),
                        () -> System.out.println("Kurs konnte nicht aktualisiert werden")
                );


            }


        }catch (IllegalArgumentException illegalArgumentException){
            System.out.println("Eingabefehler: " + illegalArgumentException.getMessage());
        }catch (InvalidValueException invalidValueException){
            System.out.println("Kursdaten nicht korrekt angegeben" + invalidValueException.getMessage());
        }catch (DatabaseException databaseException){
            System.out.println("Datenbankfehler beim Einfügen: " + databaseException.getMessage());
        }catch (Exception e){
            System.out.println("Unbekannter beim Einfügen: " + e.getMessage());
        }
    }

    private void addCourse() {

        String name, description;
        int hours;
        Date dateFrom, dateTo;
        CourseTyp courseTyp;

        try {
            System.out.println("Bitte alle Kursdaten eingeben");

            System.out.println("Name");
            name = scanner.nextLine();
            if (name.equals("")) throw new IllegalArgumentException("Eingabe darf nicht leer sein");

            System.out.println("Beschreibung");
            description = scanner.nextLine();
            if (description.equals("")) throw new IllegalArgumentException("Eingabe darf nicht leer sein");

            System.out.println("Stundenanzahl");
            hours = Integer.parseInt(scanner.nextLine());

            System.out.println("Startdatum (YYYY-MM-DD):");
            dateFrom = Date.valueOf(scanner.nextLine());

            System.out.println("Enddatum (YYYY-MM-DD):");
            dateTo = Date.valueOf(scanner.nextLine());

            System.out.println("Kurstyp: (ZA/BF/FF/OE): ");
            courseTyp = CourseTyp.valueOf(scanner.nextLine());

            Optional<courses> optionalCourses = repo.insert(
                 new courses(name,description,hours,dateFrom,dateTo,courseTyp)
            );


            if (optionalCourses.isPresent()){
                System.out.println("Kurs angelegt: " + optionalCourses.get());
            }else {
                System.out.println("Kurs konnte nicht angelegt werden");
            }

        }catch (IllegalArgumentException illegalArgumentException){
            System.out.println("Eingabefehler: " + illegalArgumentException.getMessage());
        }catch (InvalidValueException invalidValueException){
            System.out.println("Kursdaten nicht korrekt angegeben" + invalidValueException.getMessage());
        }catch (DatabaseException databaseException){
            System.out.println("Datenbankfehler beim Einfügen: " + databaseException.getMessage());
        }catch (Exception e){
            System.out.println("Unbekannter beim Einfügen: " + e.getMessage());
        }

    }

    private void showCourseDetails() {
        System.out.println("Für welchen Kurs möchten die die Kursdetails anzeigen");
        Long courseID = Long.parseLong(scanner.nextLine());

        try {
            Optional<courses> coursesOptional = repo.getById(courseID);
            if (coursesOptional.isPresent()){
                System.out.println(coursesOptional.get());
            }else {
                System.out.println("Kurs mit der id " + courseID + " nicht gefunden");
            }

        }catch (DatabaseException databaseException){
            System.out.println("Datenbankfehler bei Kurs-DetailAnzeige" + databaseException.getMessage());
        }catch (Exception e){
            System.out.println("Unbekannter Fehler bei Kurs-Dateianzeige " + e.getMessage());
        }
    }

    private void showAllCourses() {

        List<courses> list = null;

        try {
        list = repo.getAll();
        if (list.size() > 0){
            for (courses courses : list){
                System.out.println(courses);
            }
        }else {
            System.out.println("Kurzliste leer!");
        }
    }catch (DatabaseException databaseException){
            System.out.println("Datenbankfehler beim Anzeigen aller Kurse " + databaseException.getMessage());
        }catch (Exception exception) {
            System.out.println("Unbekannter Fehler bei Anzeige aller Kurse:" + exception.getMessage());
        }


        }
    private void inputError(){
            System.out.println("Bitte nur Zahlen der Menü ausgabe eingeben");
        }

        private void showMenue() {
            System.out.println("-------------KURSMANAGMENT------------");
            System.out.println("(1) Kurs eingeben  (2) Alles Kurse anzeigen  (3) Kursdetail anzeigen" );
            System.out.println("(4) Kursdetails ändern  (5) Kurs löschen  (6) Kurssuche (7) Laufende Kurse");
            System.out.println("------------STUDENTENMANAGMENT-----------");
            System.out.println("(8) Alle Studenten Anschauen (9) Studenten hinzufügen /t (10) Student löschen");
            System.out.println("(x) ENDE");
        }




    }


