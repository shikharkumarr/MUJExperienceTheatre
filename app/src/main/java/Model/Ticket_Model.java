package Model;

public class Ticket_Model {

    private String Id,Name,Date,Time;

    public Ticket_Model(String Id, String Name, String Date, String Time){
        this.Id = Id;
        this.Name = Name;
        this.Date = Date;
        this.Time = Time;
    }

    public Ticket_Model(){}

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
