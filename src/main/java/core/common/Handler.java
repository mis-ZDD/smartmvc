package core.common;

import java.lang.reflect.Method;

/**
 * 为了方便利用java反射机制去调用处理器的方法
 * 而设计的一个辅助类。
 */
public class Handler {
    //mh:处理器方法对应的Method对象
    private Method mh;
    //obj:处理器
    private Object obj;

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Method getMh() {
        return mh;
    }

    public void setMh(Method mh) {
        this.mh = mh;
    }
}
