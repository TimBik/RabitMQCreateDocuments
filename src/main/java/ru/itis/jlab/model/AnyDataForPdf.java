package ru.itis.jlab.model;

import java.util.HashMap;
import java.util.Map;

public class AnyDataForPdf {
    private Map<String, Object> data;

    public AnyDataForPdf(Map<String, Object> data) {
        this.data = new HashMap<>(data);
    }

    private Object getData(String key) {
        return data.get(key);
    }

    public Map<String, Object> getDataMap() {
        return new HashMap<>(data);
    }
}
