import java.io.*;
import java.net.Socket;

public class Controller  {
    public DataInputStream is;
    public DataOutputStream os;
    public File file = new File( "ClientIO/src/main/resources/1.txt");
    private String path = "ClientIO/src/main/resources/";

    public void connect(){
        try{
            //отурываем канал с помощью сокета на адрес локал хост по порту 8189
            Socket socket = new Socket("localhost", 8189);
            // получем входящий поток из сокета и сохраняем в переменную is
            is = new DataInputStream(socket.getInputStream());
            // передаем исходящий поток и записываем в переменную os
            os = new DataOutputStream(socket.getOutputStream());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void upload(){
        System.out.println(file.getName());
        try{
            os.writeUTF(file.getName());
            os.writeLong(file.length());
            FileInputStream fis = new FileInputStream(file);
            int tmp;
            byte[] buffer = new byte[8192];
            while((tmp = fis.read(buffer)) != -1){
                os.write(buffer,0,tmp);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void download(){
       try{
        while (true){
            String fileName = is.readUTF();
            System.out.println("Save file: " + fileName);
            File file = new File(path + fileName);
            if(!file.exists()){
                file.createNewFile();
            } else {
                System.out.println("ERROR, rename file");
            }
            FileOutputStream fos = new FileOutputStream(file);
            long fileLength = is.readLong();
            System.out.println("Wait: " + fileLength + "bytes");
            byte[] buffer = new byte[10];
            for (int i = 0; i <(fileLength + 9)/10 ; i++) {
                int cnt = is.read(buffer);
                fos.write(buffer,0,cnt);
            }
            System.out.println("File successfully downloaded!");
            fos.close();
        }

    } catch (Exception e){
        e.printStackTrace();
    }
    }



}
