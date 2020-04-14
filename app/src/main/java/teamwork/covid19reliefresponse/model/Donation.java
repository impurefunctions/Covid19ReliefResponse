package teamwork.covid19reliefresponse.model;

public class Donation {

    private String name;
    private String items;
    private String location;
    private String id;
    private String number;
    private String pickUpTime;
    private String status;
    private String volunteer;
    private String volunteerId;
    private String imageurl;
    public Donation() {
    }

    public Donation(String name, String items, String location, String id, String number, String pickUpTime, String status, String volunteer, String volunteerId, String imageurl) {
        this.name = name;
        this.items = items;
        this.location = location;
        this.id = id;
        this.number = number;
        this.pickUpTime = pickUpTime;
        this.status = status;
        this.volunteer = volunteer;
        this.volunteerId = volunteerId;
        this.imageurl = imageurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(String pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(String volunteer) {
        this.volunteer = volunteer;
    }

    public String getVolunteerId() {
        return volunteerId;
    }

    public void setVolunteerId(String volunteerId) {
        this.volunteerId = volunteerId;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
