package teamwork.covid19reliefresponse.model;

public class HamperRequest {
    private String name;
    private String id;
    private String phoneNumber;
    private String location;
    private Integer people;
    private Integer infants;
    private String allergies;
    private String lastMealDate;


    public HamperRequest() {
    }

    public HamperRequest(String name, String id, String phoneNumber, String location, Integer people, Integer infants, String allergies, String lastMealDate) {
        this.name = name;
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.location = location;
        this.people = people;
        this.infants = infants;
        this.allergies = allergies;
        this.lastMealDate = lastMealDate;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getPeople() {
        return people;
    }

    public void setPeople(Integer people) {
        this.people = people;
    }

    public Integer getInfants() {
        return infants;
    }

    public void setInfants(Integer infants) {
        this.infants = infants;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getLastMealDate() {
        return lastMealDate;
    }

    public void setLastMealDate(String lastMealDate) {
        this.lastMealDate = lastMealDate;
    }
}
