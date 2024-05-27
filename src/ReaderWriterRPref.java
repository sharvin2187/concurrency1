import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

// ReaderWriter that prefers readers over writers
public class ReaderWriterRPref extends ReaderWriter {
    int resource, readerCount = 0;
    AtomicInteger readCount = new AtomicInteger(), writeCount = new AtomicInteger();
    Semaphore readerSem = new Semaphore(1), resourceSem = new Semaphore(1);

    @Override
    public int read() throws InterruptedException {
        readerSem.acquire();
        readerCount++;
        if(readerCount==1) {
            resourceSem.acquire();
        }
        readerSem.release();

        System.out.println("Read resource: " + resource);
        int ret = resource;
        readerSem.acquire();
        readerCount--;
        if(readerCount==0) {
            resourceSem.release();
        }
        readerSem.release();
        return ret;
    }

    @Override
    public void write(int x) throws InterruptedException {
        resourceSem.acquire();
        resource = x;
        System.out.println("Wrote resource: " + resource);
        resourceSem.release();
    }
}
