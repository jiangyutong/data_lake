package ${BasePackageName}${InterfacePackageName};

import com.boyoi.core.service.impl.BaseServiceImpl;
import ${BasePackageName}${EntityPackageName}.${ClassName};
import ${BasePackageName}${ServicePackageName}.${ClassName}Service;
import ${BasePackageName}${DaoPackageName}.${ClassName}Dao;
import com.boyoi.core.entity.EasyGridRequest;
import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
* 描述：
* ${moduleName} 业务实现类
*
* @author Harvey Y.L.Q.
* @date 2018/12/20 15:33
*/
@Service
public class ${ClassName}ServiceImpl extends BaseServiceImpl
<${ClassName}Dao> implements ${ClassName}Service {

    @Resource
    private ${ClassName}Dao dao;

    @Override
    public int add(${ClassName} ${EntityName}) {
    ${EntityName}.setGuid(IdUtil.simpleUUID());
    return dao.add(${EntityName});
    }

    @Override
    public int addBatch(List<${ClassName}> list) {
    return dao.addBatch(list);
    }

    @Override
    public int del(${ClassName} ${EntityName}) {
    return dao.del(${EntityName});
    }

    @Override
    public int delBatch(List
    <Serializable> ids) {
        return dao.delBatch(ids);
        }

        @Override
        public int update(${ClassName} ${EntityName}) {
        return dao.update(${EntityName});
        }

        @Override
        public int updateByNotEmpty(${ClassName} ${EntityName}) {
        return dao.updateByNotEmpty(${EntityName});
        }

        @Override
        public ${ClassName} findById(Serializable id) {
        return dao.findById(id);
        }

        @Override
        public List<${ClassName}> findAll() {
        return dao.findAll();
        }

        @Override
        public PageInfo<${ClassName}> findByGridRequest(EasyGridRequest gridRequest) {
        return PageHelper
        //分页
        .startPage(gridRequest.getPage(), gridRequest.getRows())
        //排序
        .setOrderBy(gridRequest.getSort() + " " + gridRequest.getOrder())
        //数据
        .doSelectPageInfo(() -> dao.findByGridRequest(gridRequest));
        }
        }
