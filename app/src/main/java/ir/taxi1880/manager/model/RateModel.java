package ir.taxi1880.manager.model;

public class RateModel {

    int id;
    int fromHour;
    int toHour;
    int stopPricePercent;
    int meterPricePercent;
    int entryPricePercent;
    int charterPricePercent;
    int minPricePercent;
    int cityCode;
    String carClass;

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

    public int getStopPricePercent() {
        return stopPricePercent;
    }

    public void setStopPricePercent(int stopPricePercent) {
        this.stopPricePercent = stopPricePercent;
    }

    public int getMeterPricePercent() {
        return meterPricePercent;
    }

    public void setMeterPricePercent(int meterPricePercent) {
        this.meterPricePercent = meterPricePercent;
    }

    public int getEntryPricePercent() {
        return entryPricePercent;
    }

    public void setEntryPricePercent(int entryPricePercent) {
        this.entryPricePercent = entryPricePercent;
    }

    public int getCharterPricePercent() {
        return charterPricePercent;
    }

    public void setCharterPricePercent(int charterPricePercent) {
        this.charterPricePercent = charterPricePercent;
    }

    public int getMinPricePercent() {
        return minPricePercent;
    }

    public void setMinPricePercent(int minPricePercent) {
        this.minPricePercent = minPricePercent;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public String getCarClass() {
        return carClass;
    }

    public void setCarClass(String carClass) {
        this.carClass = carClass;
    }
}
