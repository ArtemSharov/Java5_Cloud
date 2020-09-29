import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class ServerNIO {
        public static void main(String[] args) throws IOException {
        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(8190));
        server.configureBlocking(false);
        Selector selector = Selector.open();
        System.out.println("Server started");
        server.register(selector, SelectionKey.OP_ACCEPT);
        while (server.isOpen()){
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey current = iterator.next();
                if(current.isAcceptable()){
                    handleAccept(current,selector);
                }
                if(current.isReadable()){
                    handleRead(current, selector);
                }
                iterator.remove();
            }
        }
    }
    static int cnt = 1;
    public static void handleAccept(SelectionKey current, Selector selector) throws IOException{
        SocketChannel channel = ((ServerSocketChannel)current.channel()).accept();
        channel.configureBlocking(false);
        System.out.println("Client accepted!");
        channel.register(selector,SelectionKey.OP_READ, "user #" + cnt);

    }
    public static void handleRead(SelectionKey current, Selector selector) throws IOException{
        SocketChannel channel = (SocketChannel) current.channel();
        RandomAccessFile outFile = new RandomAccessFile("12_out.txt", "rw");
        FileChannel fileChannel = outFile.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(256);
        while (channel.read(buffer) > 0){
            buffer.flip();
            fileChannel.write(buffer);
        }

    }
}
