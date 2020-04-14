package teamwork.covid19reliefresponse.model;

public class Volunteer {
    private String name;
    private String email;
    private String type;
    private String id;
    private String organization;


    public Volunteer() {
    }

    public Volunteer(String name, String email, String type, String id, String organization) {
        this.name = name;
        this.email = email;
        this.type = type;
        this.id = id;
        this.organization = organization;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }
}
