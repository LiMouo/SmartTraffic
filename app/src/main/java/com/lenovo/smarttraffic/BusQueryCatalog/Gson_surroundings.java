package com.lenovo.smarttraffic.BusQueryCatalog;

import com.google.gson.annotations.SerializedName;

/**
 * @author glsite.com
 * @version $Rev$
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class Gson_surroundings {

    @SerializedName("pm2.5")
    private int _$Pm2553; // FIXME check this code
    private int co2;
    private int LightIntensity;
    private int humidity;
    private int temperature;
    private String RESULT;
    private String ERRMSG;

    public int get_$Pm2553() {
        return _$Pm2553;
    }

    public void set_$Pm2553(int _$Pm2553) {
        this._$Pm2553 = _$Pm2553;
    }

    public int getCo2() {
        return co2;
    }

    public void setCo2(int co2) {
        this.co2 = co2;
    }

    public int getLightIntensity() {
        return LightIntensity;
    }

    public void setLightIntensity(int LightIntensity) {
        this.LightIntensity = LightIntensity;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public String getRESULT() {
        return RESULT;
    }

    public void setRESULT(String RESULT) {
        this.RESULT = RESULT;
    }

    public String getERRMSG() {
        return ERRMSG;
    }

    public void setERRMSG(String ERRMSG) {
        this.ERRMSG = ERRMSG;
    }
}
