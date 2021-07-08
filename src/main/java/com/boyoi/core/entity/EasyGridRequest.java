package com.boyoi.core.entity;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.*;

/**
 * 描述：
 * 分页条件查询实体类
 *
 * @author ZhouJL
 * @Date 2018/11/28 16:39
 */
public class EasyGridRequest implements Serializable {


    private static final long serialVersionUID = -7396566898816024079L;
    @NotBlank
    @Pattern(regexp = "[1-9]\\d*")
    @Setter
    private String page = "0";

    @NotBlank
    @Pattern(regexp = "[1-9]\\d*")
    @Setter
    private String rows = "10";

    @Pattern(regexp = "[1-9]\\d*")
    @Setter
    private String offset = "0";

    @Getter
    private String sort = "tablealins.opt_time";

    @Getter
    private String order = "desc";

    @Setter
    @Getter
    private boolean paging = true;

    @Setter
    private Map<String, String> map = new HashMap<>();

    @Setter
    private Map<String, String> map2 = new HashMap<>();

    public EasyGridRequest() {
    }

    public void setSort(String sort) {
        if (!sort.contains(".")) {
            if ("optTimeStr".equals(sort)) {
                this.sort = "tablealins.opt_time";
            } else if ("".equals(sort)) {
                this.sort = "tablealins.opt_time";
            } else {
                this.sort = "tablealins." + StrUtil.toUnderlineCase(sort);
            }
        } else {
            this.sort = sort;
        }
    }

    public void setSort2(String sort) {

        this.sort = sort;

    }

    public void setOrder(String order) {
        if ("ascend".equals(order)) {
            this.order = "asc";
        } else if ("descend".equals(order)) {
            this.order = "desc";
        } else if ("".equals(order)) {
            this.order = "desc";
        } else {
            this.order = order;
        }
    }

    public Integer getOffset() {
        return Integer.parseInt(offset);
    }

    public Integer getPage() {
        return Integer.parseInt(page);
    }

    public Integer getRows() {
        return Integer.parseInt(rows);
    }

    public Map<String, String> getMap() {
        if (!this.map.isEmpty()) {
            Set<Map.Entry<String, String>> entries = this.map.entrySet();
            Iterator i$ = entries.iterator();

            while (i$.hasNext()) {
                Map.Entry entry = (Map.Entry) i$.next();
                String value = ((String) entry.getValue()).trim();
                if ("".equals(value)) {
                    this.map.remove(entry.getKey());
                } else if (value.indexOf(0) != 37 && value.indexOf(value.length() - 1) != 37) {
                    entry.setValue("%" + value + "%");
                }
            }
        }

        return this.map;
    }

    public Map<String, String> getMap2() {
        if (!this.map.isEmpty()) {
            Set<Map.Entry<String, String>> entries = this.map.entrySet();
            Iterator i$ = entries.iterator();

            while (i$.hasNext()) {
                Map.Entry entry = (Map.Entry) i$.next();
                String value = (String) entry.getValue();
                int prefix = 0;
                char[] arr$ = value.toCharArray();
                int len$ = arr$.length;

                for (int i$1 = 0; i$1 < len$; ++i$1) {
                    char c = arr$[i$1];
                    if (c != '%') {
                        break;
                    }

                    ++prefix;
                }

                int prefixLast;
                for (prefixLast = value.length(); prefixLast > 0 && value.charAt(prefixLast - 1) == '%'; --prefixLast) {

                }
                this.map2.put((String) entry.getKey(), value.substring(prefix, prefixLast));
            }
        }

        return this.map2;
    }

    public int getMapSize() {
        return this.getMap2().size();
    }
}
