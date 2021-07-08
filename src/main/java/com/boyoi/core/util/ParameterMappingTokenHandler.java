package com.boyoi.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ParameterMappingTokenHandler {
    private List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();
    private Map paramas;

    public ParameterMappingTokenHandler(Map params) {
        this.paramas = params;
    }

    public ParameterMappingTokenHandler() {
        this.paramas = null;
    }

    // context是参数名称 #{id} #{username}


    //这里是把#{id}换成?
    public String handleToken(String content) throws Exception {
        String newContent = content.replace(" ", "");
        parameterMappings.add(buildParameterMapping(newContent));
        if (null == this.paramas) return "?";
        else {
            if (paramas.keySet().contains(newContent)) return (String) paramas.get(newContent);
            else {
                throw new Exception();
            }
        }
    }


    private ParameterMapping buildParameterMapping(String content) {
        ParameterMapping parameterMapping = new ParameterMapping(content);
        return parameterMapping;
    }

    public List<ParameterMapping> getParameterMappings() {
        return parameterMappings;
    }

    public void setParameterMappings(List<ParameterMapping> parameterMappings) {
        this.parameterMappings = parameterMappings;
    }

}
