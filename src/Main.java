import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    static class Reader implements Runnable {
        ReaderWriter res;
        Reader(ReaderWriter res) {
            this.res = res;
        }

        @Override
        public void run() {
            try {
                res.read();
            } catch (Exception e) {}
        }
    }

    static class Writer implements Runnable {
        static AtomicInteger id = new AtomicInteger(1);
        ReaderWriter res;
        Writer(ReaderWriter res) {
            this.res = res;
        }

        @Override
        public void run() {
            try {
                res.write(id.getAndIncrement());
            } catch (Exception e) {}
        }
    }

    public static void main(String[] args) {
        // Change this line to modify the implementation
        ReaderWriter runner = new ReaderWriterFair();

        for(int i=0;i<50;i++) {
            if(i%2==0) {
                (new Thread(new Writer(runner))).start();
            } else {
                (new Thread(new Reader(runner))).start();
            }
        }
    }
}