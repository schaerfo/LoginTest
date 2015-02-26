package login;

// Taken from Vertretungsplan app
public class Event
{
    private String form;
    private String lesson;
    private String subject;
    private String teacher;
    private String room;
    private String info;

    public Event(String form, String lesson, String teacher, String subject, String room, String info) {
        this.form = form;
        this.lesson = lesson;
        this.teacher = teacher;
        this.subject = subject;
        this.room = room;
        this.info = info;
    }

    public String getLesson() {
        return lesson;
    }

    public String getSubject() {
        return subject;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getRoom() {
        return room;
    }

    public String getInfo() {
        return info;
    }

    public String getForm() {
        return form;
    }
}
