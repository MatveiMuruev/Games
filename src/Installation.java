import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Installation {
    public static void main(String[] args) {
        String instalDir = "C:/Games2";
        StringBuilder tempText = new StringBuilder();
        File src = new File(instalDir + "/src");
        File res = new File(instalDir + "/res");
        File savegames = new File(instalDir + "/savegames");
        File temp = new File(instalDir + "/temp");
        if (src.mkdir()) {
            tempText.append(src.getName() + "\n");
        }
        if (res.mkdir()) {
            tempText.append(res.getName() + "\n");
        }
        if (savegames.mkdir()) {
            tempText.append(savegames.getName() + "\n");
        }
        if (temp.mkdir()) {
            tempText.append(temp.getName() + "\n");
        }
        File main = new File(src + "/main");
        File test = new File(src + "/test");
        if (main.mkdir()) {
            tempText.append(main.getName() + "\n");
        }
        if (test.mkdir()) {
            tempText.append(test.getName() + "\n");
        }
        File mainJava = new File(main + "/Main.java");
        File utilsJava = new File(main + "/Utils.java");

        File drawables = new File(res + "/drawables");
        File vectors = new File(res + "/vectors");
        File icons = new File(res + "/icons");

        if (drawables.mkdir()) {
            tempText.append(drawables.getName() + "\n");
        }
        if (vectors.mkdir()) {
            tempText.append(vectors.getName() + "\n");
        }
        if (icons.mkdir()) {
            tempText.append(icons.getName() + "\n");
        }
        File tempTxt = new File(temp + "/temp.txt");
        try {
            mainJava.createNewFile();
            tempText.append(mainJava.getName() + "\n");
            utilsJava.createNewFile();
            tempText.append(utilsJava.getName() + "\n");
            tempTxt.createNewFile();
            tempText.append(tempTxt.getName() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileWriter writer = new FileWriter(tempTxt, false)) {
            writer.write("Успешно созданы:\n" + tempText.toString());
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        GameProgress one = new GameProgress(1, 1, 1, 1.1);
        GameProgress two = new GameProgress(2, 2, 2, 2.2);
        GameProgress three = new GameProgress(3, 3, 3, 3.3);

        saveGame(savegames + "/save1.dat", one);
        saveGame(savegames + "/save2.dat", two);
        saveGame(savegames + "/save3.dat", three);

        zipFiles(savegames + "/zip.zip", savegames.listFiles());

        openZip(savegames.getAbsolutePath() + "/zip.zip", savegames.getAbsolutePath());

        openProgress(savegames + "/save2.dat");
    }

    public static void saveGame(String path, GameProgress progress) {
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(progress);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void zipFiles(String zipFilesPath, File[] filesList) {
        ZipEntry entry;
        byte[] buffer;

        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipFilesPath))) {
            for (File item : filesList) {
                try (FileInputStream fis = new FileInputStream(item.getAbsolutePath())) {
                    entry = new ZipEntry(item.getName());
                    zout.putNextEntry(entry);
                    buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zout.write(buffer);
                    zout.closeEntry();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                item.delete();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void openZip(String zipPath, String openZipPath) {
        try (FileInputStream fis = new FileInputStream(zipPath);
             ZipInputStream zis = new ZipInputStream(fis)) {
            ZipEntry entry;
            String name;

            while ((entry = zis.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fouts = new FileOutputStream(openZipPath + "/" + name);
                for (int c = zis.read(); c != -1; c = zis.read()) {
                    fouts.write(c);
                }
                fouts.flush();
                zis.closeEntry();
                fouts.close();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void openProgress(String pathProgress) {

        try (FileInputStream fis = new FileInputStream(pathProgress);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            GameProgress a = (GameProgress) ois.readObject();
            System.out.println(a.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
