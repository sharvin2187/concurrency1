import java.util.concurrent.Semaphore;

public class ReaderWriterFair extends ReaderWriter {
    int resource, readCount = 0;
    Semaphore resSem = new Semaphore(1), readSem = new Semaphore(1);
    Semaphore queue = new Semaphore(1, true); // fair semaphore

    @Override
    public int read() throws  InterruptedException {
        queue.acquire();
        readSem.acquire();
        readCount++;
        if(readCount==1) {
            resSem.acquire();
        }
        queue.release(); // queue only orders accesses to the resource
        readSem.release();

        System.out.println("Read resource: " + resource);
        int ret = resource;

        readSem.acquire();
        readCount--;
        if(readCount==0) {
            resSem.release();
        }
        readSem.release();
        return ret;
    }

    @Override
    public void write(int x) throws InterruptedException {
        queue.acquire();
        resSem.acquire();
        queue.release();

        resource = x;
        System.out.println("Wrote resource: " + resource);

        resSem.release();
    }
}
