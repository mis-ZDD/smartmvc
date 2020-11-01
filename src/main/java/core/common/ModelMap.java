package core.common;

import java.util.HashMap;
import java.util.Map;

/**
 * ModelMap用于处理器向视图传递数据。
 */
public class ModelMap {
    private Map<String,Object> map =
            new HashMap<>();

    public Map<String, Object> getMap() {
        return map;
    }

    /**
     * 绑订数据的方法
     * @param msg 绑订名
     * @param data 绑订值（也就是要传递的数据）
     */
    public void addAttribute(String msg, Object data) {
           map.put(msg,data);
    }
}
