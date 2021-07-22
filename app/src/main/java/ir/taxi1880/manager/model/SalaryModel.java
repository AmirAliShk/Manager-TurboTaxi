package ir.taxi1880.manager.model;

public class SalaryModel {

    int id;
    int fromHour;
    int toHour;
    int servicePer;
    int errorPer;
    int stationPer;
    int answerDriverPer;
    int complaintPer;

    String servicePrice;
    String errorPrice;
    String stationPrice;
    String answerDriverPrice;
    String complaintPrice;

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getErrorPrice() {
        return errorPrice;
    }

    public void setErrorPrice(String errorPrice) {
        this.errorPrice = errorPrice;
    }

    public String getStationPrice() {
        return stationPrice;
    }

    public void setStationPrice(String stationPrice) {
        this.stationPrice = stationPrice;
    }

    public String getAnswerDriverPrice() {
        return answerDriverPrice;
    }

    public void setAnswerDriverPrice(String answerDriverPrice) {
        this.answerDriverPrice = answerDriverPrice;
    }

    public String getComplaintPrice() {
        return complaintPrice;
    }

    public void setComplaintPrice(String complaintPrice) {
        this.complaintPrice = complaintPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromHour() {
        return fromHour;
    }

    public void setFromHour(int fromHour) {
        this.fromHour = fromHour;
    }

    public int getToHour() {
        return toHour;
    }

    public void setToHour(int toHour) {
        this.toHour = toHour;
    }

    public int getServicePer() {
        return servicePer;
    }

    public void setServicePer(int servicePer) {
        this.servicePer = servicePer;
    }

    public int getErrorPer() {
        return errorPer;
    }

    public void setErrorPer(int errorPer) {
        this.errorPer = errorPer;
    }

    public int getStationPer() {
        return stationPer;
    }

    public void setStationPer(int stationPer) {
        this.stationPer = stationPer;
    }

    public int getAnswerDriverPer() {
        return answerDriverPer;
    }

    public void setAnswerDriverPer(int answerDriverPer) {
        this.answerDriverPer = answerDriverPer;
    }

    public int getComplaintPer() {
        return complaintPer;
    }

    public void setComplaintPer(int complaintPer) {
        this.complaintPer = complaintPer;
    }
}
