import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

        public static void main(String[] args) {
        // делаем try с ресурсами, в ресурс передаем сервер сокет с портом
        try(ServerSocket server = new ServerSocket(8189)){
            String savePath = "ServerIO/src/main/resources/";
            File uploadFile = new File(savePath + "2.txt");
            System.out.println("Server started! Hello :)");

            // ждем подключения к сервер сокету по порту 8189 и записываем подключение в переменную socket
            Socket socket = server.accept();
            System.out.println("Client accepted! Fuck yeah!");

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            while (true){
                String fileName = dis.readUTF();
                System.out.println("Save file: " + fileName);
                File file = new File(savePath + fileName);
                if(!file.exists()){
                    file.createNewFile();
                } else {
                    System.out.println("ERROR, rename file");
                }
                FileOutputStream os = new FileOutputStream(file);
                long fileLength = dis.readLong();
                System.out.println("Wait: " + fileLength + "bytes");
                byte[] buffer = new byte[10];
                for (int i = 0; i <(fileLength + 9)/10 ; i++) {
                    int cnt = dis.read(buffer);
                    os.write(buffer,0,cnt);
                }
                System.out.println("File successfully uploaded!");
                os.close();

                System.out.println(uploadFile.getName());
                try{
                    dos.writeUTF(uploadFile.getName());
                    dos.writeLong(uploadFile.length());
                    FileInputStream fis = new FileInputStream(uploadFile);
                    int tmp;
                    while((tmp = fis.read(buffer)) != -1){
                        dos.write(buffer,0,tmp);
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
