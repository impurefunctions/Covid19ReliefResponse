package teamwork.covid19reliefresponse.model;

public class QuestionModel {

    private String type;
    private String name;
    private String question;


    public QuestionModel() {
    }

    public QuestionModel(String type, String name, String question) {
        this.type = type;
        this.name = name;
        this.question = question;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
