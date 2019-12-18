package com.lenovo.smarttraffic.Gson;

import java.util.List;

/**
 * @author glsite.com
 * @version $Rev$
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class Left_Gson {

    /**
     * ERRMSG : 成功
     * WCurrent : -10
     * ROWS_DETAIL : [{"temperature":"-11~-7","WData":"2019-12-17","type":"晴"},{"temperature":"-14~-6","WData":"2019-12-18","type":"晴"},{"temperature":"-8~0","WData":"2019-12-19","type":"晴"},{"temperature":"-11~-3","WData":"2019-12-20","type":"小雨"},{"temperature":"-8~-1","WData":"2019-12-21","type":"小雨"},{"temperature":"-10~-5","WData":"2019-12-22","type":"小雨"}]
     * RESULT : S
     */

    private String ERRMSG;
    private int WCurrent;
    private String RESULT;
    private List<ROWSDETAILBean> ROWS_DETAIL;

    public String getERRMSG() {
        return ERRMSG;
    }

    public void setERRMSG(String ERRMSG) {
        this.ERRMSG = ERRMSG;
    }

    public int getWCurrent() {
        return WCurrent;
    }

    public void setWCurrent(int WCurrent) {
        this.WCurrent = WCurrent;
    }

    public String getRESULT() {
        return RESULT;
    }

    public void setRESULT(String RESULT) {
        this.RESULT = RESULT;
    }

    public List<ROWSDETAILBean> getROWS_DETAIL() {
        return ROWS_DETAIL;
    }

    public void setROWS_DETAIL(List<ROWSDETAILBean> ROWS_DETAIL) {
        this.ROWS_DETAIL = ROWS_DETAIL;
    }

    public static class ROWSDETAILBean {
        /**
         * temperature : -11~-7
         * WData : 2019-12-17
         * type : 晴
         */

        private String temperature;
        private String WData;
        private String type;

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        public String getWData() {
            return WData;
        }

        public void setWData(String WData) {
            this.WData = WData;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
