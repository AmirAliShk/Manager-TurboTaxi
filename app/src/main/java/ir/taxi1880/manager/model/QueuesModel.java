package ir.taxi1880.manager.model;

public class QueuesModel {


    private String name;
    private String queueCode;
    private String id;
    private String capacity;
    private String activeMember;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQueueCode() {
        return queueCode;
    }

    public void setQueueCode(String queueCode) {
        this.queueCode = queueCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getActiveMember() {
        return activeMember;
    }

    public void setActiveMember(String activeMember) {
        this.activeMember = activeMember;
    }

}
