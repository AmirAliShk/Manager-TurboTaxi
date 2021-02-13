package ir.taxi1880.manager.model;

public class LinesModel {

    private String name;
    private int id;
    private String number;
    private Boolean statusNew;
    private Boolean support;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public Boolean getNew() {
        return statusNew;
    }

    public Boolean getSupport() {
        return support;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNew(Boolean statusNew) {
        this.statusNew = statusNew;
    }

    public void setSupport(Boolean support) {
        this.support = support;
    }


}
