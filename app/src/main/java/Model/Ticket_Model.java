package Model;

public class Ticket_Model {

    private String Id,Name,Date,Time,Seats;

    public Ticket_Model(String Id, String Name, String Date, String Time, String Seats){
        this.Id = Id;
        this.Name = Name;
        this.Date = Date;
        this.Time = Time;
        this.Seats = Seats;

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

    public String getSeats() {
        return Seats;
    }

    public void setSeats(String seats) {
        Seats = seats;
    }
}
