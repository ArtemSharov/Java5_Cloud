public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.connect();
        controller.upload();
        controller.download();
    }
}
