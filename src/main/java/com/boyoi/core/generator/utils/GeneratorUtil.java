package com.boyoi.core.generator.utils;


import com.boyoi.core.generator.entity.ColumnInfo;

import java.sql.Types;
import java.util.List;


/**
 * 动态字段生成工具类
 *
 * @author ZhouJL
 * @date 2019/2/14 10:14
 */
public class GeneratorUtil {


    /**
     * 生成实体类属性字段（基本数据类型，用于单表关系）
     *
     * @param infos 表结构
     * @return
     */
    public static String generateEntityProperties(List<ColumnInfo> infos, String className) {
        StringBuilder sb = new StringBuilder();
        for (ColumnInfo info : infos) {
            sb.append("    /** \n");
            sb.append("     * ").append(info.getPropertyName()).append("\n");
            sb.append("     */ \n");
            if (info.isNull()) {
                if (info.isPrimaryKey()) {
                    sb.append("    @NotBlank(groups = {" + className + "Group.Update.class, " + className + "Group.Del.class})\n");
                } else {
                    sb.append("    @NotBlank(groups = {" + className + "Group.Add.class, " + className + "Group.Update.class})\n");
                }
            }
            String type = TypeUtil.parseTypeFormSqlType(info.getType());
            if ("float".equals(type) || "double".equals(type) || "BigDecimal".equals(type)) {
                sb.append("    @Digits(integer = " + info.getSize() + ", fraction = " + info.getScale() + ")\n");
            } else {
                sb.append("    @Size(max = " + info.getSize() + ")\n");
            }
            sb.append("    private ").append(TypeUtil.parseTypeFormSqlType(info.getType())).append(" ").append(info.getColumnName()).append(";\n\n");
        }
        return sb.toString();
    }


    /**
     * 生成实体类属性字段（引用，用于一对多关系）
     *
     * @param parentClassName 父表类名
     * @param infos           表结构
     * @param foreignKey      外键
     * @return
     */
    public static String generateEntityProperties(String parentClassName, List<ColumnInfo> infos, String foreignKey) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < infos.size(); i++) {
            if (infos.get(i).isPrimaryKey() || !infos.get(i).getColumnName().equals(foreignKey)) {
                if (i != 0) {
                    sb.append("    ");
                }
                sb.append("private ").append(TypeUtil.parseTypeFormSqlType(infos.get(i).getType())).append(" ").append(infos.get(i).getColumnName()).append("; \n");
            }
        }
        // 外键为父表实体引用
        sb.append("    ").append("private ").append(parentClassName).append(" ").append(StringUtil.firstToLowerCase(parentClassName)).append("; \n");
        return sb.toString();
    }


    /**
     * 生成实体类存取方法（用于单表关系）
     *
     * @param infos 表结构
     * @return
     */
    public static String generateEntityMethods(List<ColumnInfo> infos) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < infos.size(); i++) {
            if (i != 0) {
                sb.append("    ");
            }
            sb.append("public void set").append(StringUtil.firstToUpperCase(infos.get(i).getPropertyName())).append(" (").append(TypeUtil.parseTypeFormSqlType(infos.get(i).getType())).append(" ").append(infos.get(i).getPropertyName()).append(") {this.").append(infos.get(i).getPropertyName()).append(" = ").append(infos.get(i).getPropertyName()).append(";} \n");
            if (infos.get(i).getType() == Types.BIT || infos.get(i).getType() == Types.TINYINT) {
                sb.append("    ").append("public ").append(TypeUtil.parseTypeFormSqlType(infos.get(i).getType())).append(" is").append(StringUtil.firstToUpperCase(infos.get(i).getPropertyName())).append("(){ return ").append(infos.get(i).getPropertyName()).append(";} \n");
            } else {
                sb.append("    ").append("public ").append(TypeUtil.parseTypeFormSqlType(infos.get(i).getType())).append(" get").append(StringUtil.firstToUpperCase(infos.get(i).getPropertyName())).append("(){ return ").append(infos.get(i).getPropertyName()).append(";} \n");
            }
        }
        return sb.toString();
    }

    /**
     * 生成实体类存取方法（用于多对多关系）
     *
     * @param parentClassName
     * @param infos
     * @return
     */
    public static String generateEntityMethods(String parentClassName, List<ColumnInfo> infos) {
        StringBuilder sb = new StringBuilder(generateEntityMethods(infos));
        sb.append("    ").append("public void set" + parentClassName + "s (List<" + parentClassName + "> " + StringUtil.firstToLowerCase(parentClassName) + "s) { \n this." + StringUtil.firstToLowerCase(parentClassName) + "s = " + StringUtil.firstToLowerCase(parentClassName) + "s;\n} \n");
        sb.append("    ").append("public List<" + parentClassName + "> get" + parentClassName + "s(){ return this." + StringUtil.firstToLowerCase(parentClassName) + "s;} \n");
        return sb.toString();
    }

    /**
     * 生成实体类存取方法（用于一对多关系）
     *
     * @param parentClassName 父表类名
     * @param infos           表结构
     * @param foreignKey      外键
     * @return
     */
    public static String generateEntityMethods(String parentClassName, List<ColumnInfo> infos, String foreignKey) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < infos.size(); i++) {
            if (infos.get(i).isPrimaryKey() || !infos.get(i).getColumnName().equals(foreignKey)) {
                if (i != 0) {
                    sb.append("    ");
                }
                sb.append("public void set").append(StringUtil.firstToUpperCase(infos.get(i).getPropertyName())).append(" (").append(TypeUtil.parseTypeFormSqlType(infos.get(i).getType())).append(" ").append(infos.get(i).getPropertyName()).append(") {this.").append(infos.get(i).getPropertyName()).append(" = ").append(infos.get(i).getPropertyName()).append(";} \n");
                if (infos.get(i).getType() == Types.BIT || infos.get(i).getType() == Types.TINYINT) {
                    sb.append("    ").append("public ").append(TypeUtil.parseTypeFormSqlType(infos.get(i).getType())).append(" is").append(StringUtil.firstToUpperCase(infos.get(i).getPropertyName())).append("(){ return ").append(infos.get(i).getPropertyName()).append(";} \n");
                } else {
                    sb.append("    ").append("public ").append(TypeUtil.parseTypeFormSqlType(infos.get(i).getType())).append(" get").append(StringUtil.firstToUpperCase(infos.get(i).getPropertyName())).append("(){ return ").append(infos.get(i).getPropertyName()).append(";} \n");
                }
            }
        }
        // 外键为存取父表实体引用
        sb.append("    ").append("public void set").append(parentClassName).append(" (").append(parentClassName).append(" ").append(StringUtil.firstToLowerCase(parentClassName)).append(") {this.").append(StringUtil.firstToLowerCase(parentClassName)).append(" = ").append(StringUtil.firstToLowerCase(parentClassName)).append(";} \n");
        sb.append("    ").append("public ").append(parentClassName).append(" get").append(parentClassName).append("(){ return this.").append(StringUtil.firstToLowerCase(parentClassName)).append(";} \n");
        return sb.toString();
    }

    /**
     * 生成Mapper ColumnMap字段，单表
     */
    public static String generateMapperColumnMap(String tableName, List<ColumnInfo> infos) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < infos.size(); i++) {
            if (i != 0) {
                sb.append("        ");
            }
            sb.append(tableName).append(".").append(infos.get(i).getColumnName()).append(" AS ").append("\"").append(infos.get(i).getColumnName()).append("\",\n");
        }
        return sb.toString().substring(0, sb.toString().length() - 2);
    }

    /**
     * 生成Mapper ColumnMap字段，一对多
     */
    public static String generateMapperColumnMap(String tableName, String parentTableName, List<ColumnInfo> infos, List<ColumnInfo> parentInfos, String parentEntityName, String foreignKey) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < infos.size(); i++) {
            if (infos.get(i).isPrimaryKey() || !infos.get(i).getColumnName().equals(foreignKey)) {
                if (i != 0) {
                    sb.append("        ");
                }
                sb.append(tableName).append(".").append(infos.get(i).getColumnName()).append(" AS ").append("\"").append(infos.get(i).getPropertyName()).append("\",\n");
            }
        }
        for (ColumnInfo info : parentInfos) {
            sb.append("        ").append(parentTableName).append(".").append(info.getColumnName()).append(" AS ").append("\"").append(parentEntityName).append(".").append(info.getPropertyName()).append("\",\n");
        }
        return sb.toString().substring(0, sb.toString().length() - 2);
    }

    /**
     * 生成Mapper ColumnMap字段，多对多
     */
    public static String generateMapperColumnMap(String tableName, String parentTableName, List<ColumnInfo> infos, List<ColumnInfo> parentInfos, String parentEntityName) {
        StringBuilder sb = new StringBuilder(generateMapperColumnMap(tableName, infos));
        sb.append(",\n");
        for (ColumnInfo info : parentInfos) {
            sb.append("        ").append(parentTableName).append(".").append(info.getColumnName()).append(" AS ").append("\"").append(parentEntityName).append("s.").append(info.getPropertyName()).append("\",\n");
        }
        return sb.toString().substring(0, sb.toString().length() - 2);
    }

    /**
     * 对应模板文件${ResultMap}字段 用于 single、one2many、many2many
     *
     * @param infos
     * @return
     */
    public static String generateMapperResultMap(List<ColumnInfo> infos) {
        StringBuilder sb = new StringBuilder();
        for (ColumnInfo info : infos) {
            if (info.isPrimaryKey()) {
                sb.append("<id column=\"").append(info.getClassPropertyName()).append("\" property=\"").append(info.getColumnName()).append("\"/> \n");
            } else {
                sb.append("        ").append("<result column=\"").append(info.getClassPropertyName()).append("\" property=\"").append(info.getColumnName()).append("\"/> \n");
            }
        }
        return sb.toString();
    }

    /**
     * 对应模板文件${Association}字段
     * 用于 one2many
     *
     * @param parentInfos
     * @param parentClassName
     * @param parentClassPackage
     * @return
     */
    public static String generateMapperAssociation(List<ColumnInfo> parentInfos, String parentClassName, String parentClassPackage) {
        StringBuilder sb = new StringBuilder();
        sb.append("<association property=\"").append(StringUtil.firstToLowerCase(parentClassName)).append("\" javaType=\"").append(parentClassPackage).append(".").append(parentClassName).append("\">\n");
        for (ColumnInfo info : parentInfos) {
            if (info.isPrimaryKey()) {
                sb.append("            ").append("<id column=\"").append(StringUtil.firstToLowerCase(parentClassName)).append(".").append(info.getPropertyName()).append("\" property=\"").append(info.getPropertyName()).append("\"/> \n");
            } else {
                sb.append("            ").append("<result column=\"").append(StringUtil.firstToLowerCase(parentClassName)).append(".").append(info.getPropertyName()).append("\" property=\"").append(info.getPropertyName()).append("\"/> \n");
            }
        }
        sb.append("        ").append("</association>");
        return sb.toString();
    }


    /**
     * 对应模板文件${Collection}字段
     * 用于 many2many
     *
     * @param parentInfos
     * @param parentClassName
     * @param parentClassPackage
     * @return
     */
    public static String generateMapperCollection(List<ColumnInfo> parentInfos, String parentClassName, String parentClassPackage) {
        StringBuilder sb = new StringBuilder();
        sb.append("<collection property=\"").append(StringUtil.firstToLowerCase(parentClassName)).append("s\" ofType=\"").append(parentClassPackage).append(".").append(parentClassName).append("\">\n");
        for (ColumnInfo info : parentInfos) {
            if (info.isPrimaryKey()) {
                sb.append("            ").append("<id column=\"").append(StringUtil.firstToLowerCase(parentClassName)).append("s").append(".").append(info.getPropertyName()).append("\" property=\"").append(info.getPropertyName()).append("\"/> \n");
            } else {
                sb.append("            ").append("<result column=\"").append(StringUtil.firstToLowerCase(parentClassName)).append("s").append(".").append(info.getPropertyName()).append("\" property=\"").append(info.getPropertyName()).append("\"/> \n");
            }
        }
        sb.append("        ").append("</collection>");
        return sb.toString();
    }


    /**
     * 生成Mapper Joins字段（一对多关系）
     *
     * @param tableName
     * @param parentTableName
     * @param foreignKey
     * @param parentPrimaryKey
     * @return
     */
    public static String generateMapperJoins(String tableName, String parentTableName, String foreignKey, String parentPrimaryKey) {
        StringBuilder sb = new StringBuilder();
        sb.append("LEFT JOIN ").append(parentTableName).append(" on ").append(parentTableName).append(".").append(parentPrimaryKey).append(" = ").append(tableName).append(".").append(foreignKey);
        return sb.toString();
    }


    /**
     * 生成Mapper Joins字段（多对多关系）
     *
     * @param tableName
     * @param parentTableName
     * @param relationTableName
     * @param foreignKey
     * @param parentForeignKey
     * @param primaryKey
     * @param parentPrimaryKey
     * @return
     */
    public static String generateMapperJoins(String tableName, String parentTableName, String relationTableName, String foreignKey, String parentForeignKey, String primaryKey, String parentPrimaryKey) {
        StringBuilder sb = new StringBuilder();
        sb.append("LEFT JOIN ").append(relationTableName).append(" on ").append(relationTableName).append(".").append(foreignKey).append(" = ").append(tableName).append(".").append(primaryKey).append(" \n")
                .append("        ").append("LEFT JOIN ").append(parentTableName).append(" on ").append(parentTableName).append(".").append(parentPrimaryKey).append(" = ").append(relationTableName).append(".").append(parentForeignKey);
        return sb.toString();
    }


    /**
     * 生成Mapper 插入列名字段（所有关系皆用）
     *
     * @param infos
     * @return
     */
    public static String generateMapperInsertProperties(List<ColumnInfo> infos) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < infos.size(); i++) {
            sb.append(infos.get(i).getClassPropertyName() + ",");
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    /**
     * 生成Mapper 插入属性字段（单表，多对多）
     */
    public static String generateMapperInsertValues(List<ColumnInfo> infos) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < infos.size(); i++) {
            sb.append("#{").append(infos.get(i).getColumnName()).append("},");
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    /**
     * 生成Mapper 批量插入属性字段（单表，多对多）
     */
    public static String generateMapperInsertBatchValues(List<ColumnInfo> infos, String entityName) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < infos.size(); i++) {
            sb.append("#{").append(entityName).append(".").append(infos.get(i).getColumnName()).append("},");
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    /**
     * 生成Mapper 插入属性字段（一对多）
     */
    public static String generateMapperInsertValues(List<ColumnInfo> infos, String parentEntityName, String foreignKey, String primaryKey) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < infos.size(); i++) {
            if (infos.get(i).getColumnName().equals(foreignKey)) {
                if (i != 0) {
                    sb.append("            ");
                }
                // 此处id需要修改为primarykey
                sb.append("#{").append(parentEntityName).append(".").append(primaryKey).append("},\n");
            } else {
                if (i != 0) {
                    sb.append("            ");
                }
                sb.append("#{").append(infos.get(i).getPropertyName()).append("},\n");
            }
        }
        return sb.toString().substring(0, sb.toString().length() - 2);
    }

    /**
     * 生成Mapper 批量插入属性字段（一对多）
     */
    public static String generateMapperInsertBatchValues(List<ColumnInfo> infos, String entityName, String parentEntityName, String foreignKey, String primaryKey) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < infos.size(); i++) {
            if (infos.get(i).getColumnName().equals(foreignKey)) {
                if (i != 0) {
                    sb.append("            ");
                }
                // 此处id需要修改为primarykey
                sb.append("#{").append(entityName).append(".").append(parentEntityName).append(".").append(primaryKey).append("},\n");
            } else {
                if (i != 0) {
                    sb.append("            ");
                }
                sb.append("#{").append(entityName).append(".").append(infos.get(i).getPropertyName()).append("},\n");
            }
        }
        return sb.toString().substring(0, sb.toString().length() - 2);
    }

    /**
     * 生成Mapper 判断属性字段
     */
    public static String generateMapperWhereProperties(List<ColumnInfo> infos) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < infos.size(); i++) {
            if (i != 0) {
                sb.append("            ");
            }
            sb.append("<if test='null != ").append(infos.get(i).getColumnName()).append(" and \"\" != ").append(infos.get(i).getColumnName()).append("'>and ");
            sb.append(infos.get(i).getClassPropertyName()).append(" = #{").append(infos.get(i).getColumnName()).append("}</if>\n");
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    /**
     * 生成Mapper 判断属性字段（主键不包含）
     */
    public static String generateMapperWherePropertiesNotP(List<ColumnInfo> infos) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < infos.size(); i++) {
            if (i != 0) {
                sb.append("            ");
            }
            if (infos.get(i).isPrimaryKey()) {
                sb.append("<if test='null != ").append(infos.get(i).getColumnName()).append(" and \"\" != ").append(infos.get(i).getColumnName()).append("'>and ");
                sb.append(infos.get(i).getClassPropertyName()).append(" != #{").append(infos.get(i).getColumnName()).append("}</if>\n");
            } else {
                sb.append("<if test='null != ").append(infos.get(i).getColumnName()).append(" and \"\" != ").append(infos.get(i).getColumnName()).append("'>and ");
                sb.append(infos.get(i).getClassPropertyName()).append(" = #{").append(infos.get(i).getColumnName()).append("}</if>\n");
            }

        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    /**
     * 生成Mapper 更新属性字段
     */
    public static String generateMapperUpdateProperties(List<ColumnInfo> infos) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < infos.size(); i++) {
            if (i != 0) {
                sb.append("        ");
            }
            sb.append(infos.get(i).getClassPropertyName()).append(" = #{").append(infos.get(i).getColumnName()).append("},\n");
        }
        return sb.toString().substring(0, sb.toString().length() - 2);
    }

    /**
     * 生成Mapper 更新判断属性字段
     */
    public static String generateMapperUpdateIfProperties(List<ColumnInfo> infos) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < infos.size(); i++) {
            if (i != 0) {
                sb.append("            ");
            }
            sb.append("<if test='null != ").append(infos.get(i).getColumnName()).append(" and \"\" != ").append(infos.get(i).getColumnName()).append("'> ");
            if (i == infos.size() - 1) {
                sb.append(infos.get(i).getClassPropertyName()).append(" = #{").append(infos.get(i).getColumnName()).append("}</if>>\n");
            } else {
                sb.append(infos.get(i).getClassPropertyName()).append(" = #{").append(infos.get(i).getColumnName()).append("},</if>\n");
            }

        }
        return sb.toString().substring(0, sb.toString().length() - 2);
    }

    /**
     * 生成Mapper 更新属性字段
     */
    public static String generateMapperUpdateProperties(List<ColumnInfo> infos, String parentEntityName, String foreignKey, String primaryKey) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < infos.size(); i++) {
            if (infos.get(i).getColumnName().equals(foreignKey)) {
                if (i != 0) {
                    sb.append("        ");
                }
                sb.append(infos.get(i).getColumnName()).append(" = #{").append(parentEntityName).append(".").append(primaryKey).append("},\n");
            } else {
                if (i != 0) {
                    sb.append("        ");
                }
                sb.append(infos.get(i).getColumnName()).append(" = #{").append(infos.get(i).getPropertyName()).append("},\n");
            }
        }
        return sb.toString().substring(0, sb.toString().length() - 2);
    }

    /**
     * 以驼峰命名法生成类名，用于未指定类名时自动生成类名，如sys_user自动生成类名SysUser
     *
     * @param tableName
     * @return
     */
    public static String generateClassName(String tableName) {
        String[] nameStrs = tableName.split("_");
        StringBuilder sb = new StringBuilder();
        for (String string : nameStrs) {
            sb.append(string.substring(0, 1).toUpperCase()).append(string.substring(1));
        }
        return sb.toString();
    }

    public static String generateMessage(List<ColumnInfo> infos, String className, String moduleName) {
        StringBuilder sb = new StringBuilder();
        sb.append("# 校验国际化 必须 #\n");
        for (ColumnInfo info : infos) {
            if (info.isNull()) {
                sb.append(className + ".validator." + info.getColumnName() + ".required=" + info.getPropertyName() + "必填！\n");
            }
        }
        sb.append("\n# 校验国际化 最大长度 #\n");
        for (ColumnInfo info : infos) {
            sb.append(className + ".validator." + info.getColumnName() + ".max=" + info.getPropertyName() + "的最大长度为" + info.getSize() + "！\n");
        }
        sb.append("\n# 校验国际化 正则表达式 #\n");
        return sb.toString();
    }

    public static String generateMessageEn(List<ColumnInfo> infos, String className, String moduleName) {
        StringBuilder sb = new StringBuilder();
        sb.append("# 校验国际化 必须 #\n");
        for (ColumnInfo info : infos) {
            if (info.isNull()) {
                sb.append(className + ".validator." + info.getColumnName() + ".required=The " + info.getColumnName() + " is required!\n");
            }
        }
        sb.append("\n# 校验国际化 最大长度 #\n");
        for (ColumnInfo info : infos) {
            sb.append(className + ".validator." + info.getColumnName() + ".max=The maximum length of the " + info.getColumnName() + " is " + info.getSize() + "!\n");
        }
        sb.append("\n# 校验国际化 正则表达式 #\n");
        return sb.toString();
    }

}
