package ${BasePackageName}${ServicePackageName};

import com.boyoi.core.service.BaseService;
import ${BasePackageName}${EntityPackageName}.${ClassName};
import com.boyoi.core.entity.EasyGridRequest;
import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.List;
/**
* 描述：
* ${moduleName} 业务接口层
*
* @author ${Author}
* @date  ${Date}
*/
public interface ${ClassName}Service extends BaseService {

/**
* 保存一个实体
*
* @param ${EntityName}   实体
* @return 受影响的个数
*/
int add(${ClassName} ${EntityName});

/**
* 批量保存
*
* @param list 实体List集合
* @return 受影响的个数
*/
int addBatch(List<${ClassName}> list);

/**
* 删除一个实体
*
* @param ${EntityName} 实体内容
* @return 受影响的个数
*/
int del(${ClassName} ${EntityName});

/**
* 批量删除实体
*
* @param ids id的List集合
* @return 受影响的个数
*/
int delBatch(List
<Serializable> ids);

    /**
    * 更新一个实体
    *
    * @param ${EntityName} 实体
    * @return 受影响的个数
    */
    int update(${ClassName} ${EntityName});

    /**
    * 更新一个实体,条件更新,不为 null 和 空字符
    *
    * @param ${EntityName} 实体
    * @return 受影响的个数
    */
    int updateByNotEmpty(${ClassName} ${EntityName});

    /**
    * 通过ID查找实体
    *
    * @param id id
    * @return 实体
    */
    ${ClassName} findById(Serializable id);

    /**
    * 查找所有实体
    *
    * @return 实体集合
    */
    List<${ClassName}> findAll();

    /**
    * 通过EasyGridRequest条件查找
    *
    * @param gridRequest EasyGridRequest请求
    * @return 符合条件的数据集合
    */
    PageInfo<${ClassName}> findByGridRequest(EasyGridRequest gridRequest);

    }
