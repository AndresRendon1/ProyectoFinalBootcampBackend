package com.talentotech.energia.dto;

import java.math.BigDecimal;
import java.util.List;

public class EnergyTypeMonthlySeriesResponse {

    private String label;
    private List<BigDecimal> data;

    public EnergyTypeMonthlySeriesResponse() {
    }

    public EnergyTypeMonthlySeriesResponse(String label, List<BigDecimal> data) {
        this.label = label;
        this.data = data;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<BigDecimal> getData() {
        return data;
    }

    public void setData(List<BigDecimal> data) {
        this.data = data;
    }
}
