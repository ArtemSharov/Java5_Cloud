import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ClientNIO {
    public static void main(String[] args) throws IOException {
    SocketChannel channel = SocketChannel.open();
    channel.connect(new InetSocketAddress("localhost", 8190));
    channel.configureBlocking(false);

    Path path = Paths.get("ClientNIO", "src", "main", "resources", "12.txt");

        RandomAccessFile file = new RandomAccessFile("ClientNIO/src/main/resources/12.txt", "rw");
        FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.CREATE);
        System.out.println(path.getFileName());
        ByteBuffer buffer = ByteBuffer.allocate(256);
        while (fileChannel.read(buffer) > 0){
            buffer.flip();

            channel.write(buffer);
            channel.close();
        }

    }
}
