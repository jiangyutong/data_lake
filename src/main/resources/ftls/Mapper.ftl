<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${BasePackageName}${DaoPackageName}.${ClassName}Dao">

    <resultMap id="${EntityName}Map" type="${BasePackageName}${EntityPackageName}.${ClassName}">
        ${ResultMap}
    </resultMap>

    <!-- 添加 -->
    <insert id="add" parameterType="${BasePackageName}${EntityPackageName}.${ClassName}">
        insert into ${TableName}
        (${InsertProperties})
        values
        (${InsertValues})
    </insert>

    <!-- 批量添加 -->
    <insert id="addBatch" parameterType="List">
        insert into ${TableName}
        (${InsertProperties})
        values
        <foreach collection="list" item="${EntityName}" index="index" separator=",">
            (${InsertBatchValues})
        </foreach>
    </insert>

    <!-- 根据对象值删除 -->
    <update id="del" parameterType="${BasePackageName}${EntityPackageName}.${ClassName}">
        update ${TableName} set is_deleted = 'B'
        <where>
            ${WhereProperties}
        </where>
    </update>

    <!-- 根据ID批量删除 -->
    <update id="delBatch" parameterType="List">
        update ${TableName} set is_deleted = 'B' where ${PrimaryKey} in (
        <foreach collection="list" item="item" index="index" separator=",">
            #(item)
        </foreach>
        )
    </update>

    <!-- 修改 -->
    <update id="update" parameterType="${BasePackageName}${EntityPackageName}.${ClassName}">
        update ${TableName}
        set
        ${UpdateProperties}

        where ${PrimaryKey}=${WhereId}
    </update>

    <!-- 根据对象值修改 -->
    <update id="updateByNotEmpty" parameterType="${BasePackageName}${EntityPackageName}.${ClassName}">
        update ${TableName}
        <set>
            ${UpdateIfProperties}
        </set>
        where ${PrimaryKey}=${WhereId}
    </update>

    <!-- 根据主键查询 -->
    <select id="findById" resultMap="${EntityName}Map">
        select * from ${TableName} where ${PrimaryKey}=${WhereId}
    </select>

    <!-- 查询全部 -->
    <select id="findAll" resultMap="${EntityName}Map">
        select * from ${TableName}
    </select>

    <!-- 根据对象值查询重复个数 -->
    <select id="findByCheck" resultType="Integer">
        select count(1) from ${TableName} tableAlias
        <where>
            ${WherePropertiesNotP}
        </where>
    </select>

    <!-- 根据对象值查询 -->
    <select id="findByDomain" resultMap="${EntityName}Map">
        select * from ${TableName} tableAlias
        <where>
            ${WhereProperties}
        </where>
    </select>

    <!-- 分页查询方法 -->
    <select id="findByGridRequest" resultMap="${EntityName}Map">
        select
        *
        from ${TableName} tableAlins
        <where>
            <foreach item="item" index="key" collection="map2" open="" separator=" AND " close="">
                tableAlias.key = item
            </foreach>
        </where>
    </select>
</mapper>
