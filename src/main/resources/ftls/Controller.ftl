package ${BasePackageName}${ControllerPackageName};

import com.boyoi.core.controller.BaseController;
import ${BasePackageName}${EntityPackageName}.${ClassName};
import ${BasePackageName}${ServicePackageName}.${ClassName}Service;
import ${BasePackageName}${GroupPackageName}.${ClassName}Group;
import com.boyoi.core.entity.EasyGridRequest;
import com.boyoi.core.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
* 描述：
* ${moduleName} 控制层
*
* @author ${Author}
* @date  ${Date}
*/
@RestController
@RequestMapping(value = "/${EntityName}")
@Slf4j
public class ${ClassName}Controller extends BaseController<${ClassName}, ${ClassName}Service> {

@Resource
protected ${ClassName}Service service;

/**
*  添加(${authorZH})
*/
@PostMapping("/add")
public Result add(@Validated(value = ${ClassName}Group.Add.class) @RequestBody ${ClassName} ${EntityName}) {
return Result.success(service.add(${EntityName}));
}

/**
*  修改(${authorZH})
*/
@PutMapping("/update")
public Result update(@Validated(value = ${ClassName}Group.Update.class) @RequestBody ${ClassName} ${EntityName}) {
return Result.success(service.update(${EntityName}));
}

/**
*  根据主键删除(${authorZH})
*/
@DeleteMapping("/del")
public Result del(@Validated(value = ${ClassName}Group.Del.class) @RequestBody ${ClassName} ${EntityName}) {
return Result.success(service.del(${EntityName}));
}

/**
*  根据主键查询(${authorZH})
*/
@PostMapping("/detail")
public Result detail(@RequestBody ${ClassName} ${EntityName}) {
return Result.success(service.findById(${EntityName}.getGuid()));
}

/**
*  分页条件查询(${authorZH})
*/
@PostMapping("/list")
public Result list(@RequestBody EasyGridRequest gridRequest) {
return Result.success(service.findByGridRequest(gridRequest));
}

/**
*  重复数据校验(${authorZH})
*/
@PostMapping("/check")
public Result check(@RequestBody ${ClassName} ${EntityName}) {
return Result.success(service.findByCheck(${EntityName}));
}
}
