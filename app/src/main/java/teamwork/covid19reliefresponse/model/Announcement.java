package teamwork.covid19reliefresponse.model;

public class Announcement {
    private String title;
    private String publishedAt;
    private String content;
    private String imageUrl;


    public Announcement() {
    }

    public Announcement(String title, String publishedAt, String content, String imageUrl) {
        this.title = title;
        this.publishedAt = publishedAt;
        this.content = content;
        this.imageUrl = imageUrl;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
