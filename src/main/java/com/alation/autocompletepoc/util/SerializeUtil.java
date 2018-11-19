package com.alation.autocompletepoc.util;

import java.io.*;

public class SerializeUtil {
    public static <T> T getObjectFromFile(File flie, Class<T> type) {
        T obj = null;
        try {
            FileInputStream in = new FileInputStream(flie);
            ObjectInputStream ois = new ObjectInputStream(in);
            obj = (T) (ois.readObject());
            ois.close();
            in.close();
        } catch (Exception e) {
            System.out.println("Problem deserializing: " + e);
        }
        return obj;
    }

    public static void writeObjectToFile(String file_name, Object obj) {
        try {
            File file = new File(file_name);
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(obj);
            oos.flush();
            oos.close();
            out.close();
        } catch (Exception e) {
            System.out.println("Problem serializing: " + e);
        }
    }
}
