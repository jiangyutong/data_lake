package com.boyoi.core.util;

import java.util.ArrayList;
import java.util.List;


/**
 * @author fuwp
 */
public class MyParameterMappingTokenHandler {
    private List<String> parameterMappings = new ArrayList<>();

    public String handleToken(String content) {
        String build = buildParameterMapping(content);
        parameterMappings.add(build);
        return "?";
    }

    private String buildParameterMapping(String content) {
        return content.trim();
    }

    public List<String> getParameterMappings() {
        return parameterMappings;
    }

    public void setParameterMappings(List<String> parameterMappings) {
        this.parameterMappings = parameterMappings;
    }

}
