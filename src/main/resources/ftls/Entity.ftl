package ${BasePackageName}${EntityPackageName};

import com.boyoi.core.entity.BaseEntity;
import ${BasePackageName}${GroupPackageName}.${ClassName}Group;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.util.Date;

/**
* 描述：
* ${moduleName} 实体类
*
* @author ${Author}
* @date  ${Date}
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class ${ClassName} extends BaseEntity {

private static final long serialVersionUID = 1L;

${Properties}
}
