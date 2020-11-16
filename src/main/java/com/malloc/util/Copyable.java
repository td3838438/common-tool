package com.malloc.util;

import java.io.*;

/**
 * 可以通过序列化、反序列化，进行对象深度拷贝的父类，如果对象需要进行深度拷贝，可以继承此类
 *
 * @author tuyh3(tuyh3 @ asiainfo.com)
 * @desc
 * @date 2020/11/13 17:13
 * @Version
 */
public class Copyable implements Serializable {

    private static final long serialVersionUID = 2759261515543521930L;


    /**
     * 通过序列化、反序列化，深度拷贝对象
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Object copy() throws IOException, ClassNotFoundException {
        //将对象序列化后写在流里,因为写在流里面的对象是一份拷贝,原对象仍然在JVM里
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(this);

        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(
                bos.toByteArray()));
        return ois.readObject();
    }
}
