package com.lenovo.smarttraffic.Gson;

import com.google.gson.annotations.SerializedName;

/**
 * @author glsite.com
 * @version $Rev$
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class Surr_Gson {

    @SerializedName("pm2.5")
    private int _$Pm25272; // FIXME check this code
    private int co2;
    private int LightIntensity;
    private int humidity;
    private int temperature;
    private String RESULT;
    private String ERRMSG;

    public int get_$Pm25272() {
        return _$Pm25272;
    }

    public void set_$Pm25272(int _$Pm25272) {
        this._$Pm25272 = _$Pm25272;
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
