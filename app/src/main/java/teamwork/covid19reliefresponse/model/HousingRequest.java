package teamwork.covid19reliefresponse.model;

public class HousingRequest {
    private String name;
    private String id;
    private String phoneNumber;
    private String emergencyContact;
    private String location;
    private Integer children;


    public HousingRequest() {
    }

    public HousingRequest(String name, String id, String phoneNumber, String emergencyContact, String location, Integer children) {
        this.name = name;
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.emergencyContact = emergencyContact;
        this.location = location;
        this.children = children;
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

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getChildren() {
        return children;
    }

    public void setChildren(Integer children) {
        this.children = children;
    }
}
