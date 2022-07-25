package com.chup.mobcoinsplus.extras;

import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SLAPI {
    public static void save(Object obj, String path) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(path)))) {
            oos.writeObject(obj);
            oos.flush();
        }
    }

    public static Object load(String path) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(path)))) {
            return ois.readObject();
        }
    }

    public static void bukkitSave(Object obj, String path) throws IOException {
        try (BukkitObjectOutputStream oos = new BukkitObjectOutputStream(Files.newOutputStream(Paths.get(path)))) {
            oos.writeObject(obj);
            oos.flush();
        }
    }

    public static Object bukkitLoad(String path) throws IOException, ClassNotFoundException {
        try (BukkitObjectInputStream ois = new BukkitObjectInputStream(Files.newInputStream(Paths.get(path)))) {
            return ois.readObject();
        }

    }

}
