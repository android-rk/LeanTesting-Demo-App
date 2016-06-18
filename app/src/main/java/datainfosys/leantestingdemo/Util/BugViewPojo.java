package datainfosys.leantestingdemo.Util;

/**
 * Created by Data on 6/9/2016.
 */
public class BugViewPojo {
    private int sectionId;
    private int id;
    private int typeId;
    private int servityId;
    private int reproduceId;
    private String title;
    private int project_version_id;
    private int statusId;
    private String description;
    private String createdAt;
    private String expectedResults;

    public int getTypeId() {
        return typeId;
    }

    public int getProject_version_id() {
        return project_version_id;
    }

    public void setProject_version_id(int project_version_id) {
        this.project_version_id = project_version_id;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getServityId() {
        return servityId;
    }

    public void setServityId(int servityId) {
        this.servityId = servityId;
    }

    public int getReproduceId() {
        return reproduceId;
    }

    public void setReproduceId(int reproduceId) {
        this.reproduceId = reproduceId;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExpectedResults() {
        return expectedResults;
    }

    public void setExpectedResults(String expectedResults) {
        this.expectedResults = expectedResults;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
