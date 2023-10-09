package dao;

import org.apache.ibatis.annotations.Param;
import pojo.baozi;

/**
 * @description:
 * @author: Andy
 * @time: 2023-9-19 15:16
 */
public interface baozidao {
    void add(@Param("baozi") baozi baozi);
}
