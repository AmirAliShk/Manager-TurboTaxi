package ir.taxi1880.manager.model;

public class LinesModel {

    private String lineTitle;
    private Boolean statusNewCall;
    private Boolean statusSupportCall;

    public LinesModel(String lineTitle, Boolean statusNewCall, Boolean statusSupportCall) {
        this.lineTitle = lineTitle;
        this.statusNewCall = statusNewCall;
        this.statusSupportCall = statusSupportCall;
    }

    public String getLineTitle() {
        return lineTitle;
    }

    public Boolean getStatusNewCall() {
        return statusNewCall;
    }

    public Boolean getStatusSupportCall() {
        return statusSupportCall;
    }

    public void setLineTitle(String lineTitle) {
        this.lineTitle = lineTitle;
    }

    public void setStatusNewCall(Boolean statusNewCall) {
        this.statusNewCall = statusNewCall;
    }

    public void setStatusSupportCall(Boolean statusSupportCall) {
        this.statusSupportCall = statusSupportCall;
    }


}
