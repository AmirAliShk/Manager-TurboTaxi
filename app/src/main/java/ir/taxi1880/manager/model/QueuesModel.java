package ir.taxi1880.manager.model;

public class QueuesModel {


    private String queueTitle;
    private String permittedNum;
    private String inLineNum;

    public QueuesModel(String queueTitle, String permittedNum, String inLineNum) {
        this.queueTitle = queueTitle;
        this.permittedNum = permittedNum;
        this.inLineNum = inLineNum;
    }

    public String getQueueTitle() {
        return queueTitle;
    }

    public String getPermittedNum() {
        return permittedNum;
    }

    public String getInLineNum() {
        return inLineNum;
    }

    public void setQueueTitle(String queueTitle) {
        this.queueTitle = queueTitle;
    }

    public void setPermittedNum(String permittedNum) {
        this.permittedNum = permittedNum;
    }

    public void setInLineNum(String inLineNum) {
        this.inLineNum = inLineNum;
    }

}
