import java.util.concurrent.Semaphore;

// ReaderWriter that prefers writers over readers
public class ReaderWriterWPref extends ReaderWriter {
    int resource, readCount = 0, writeCount = 0;
    /*
    resSem protects the resource
    readTry handles turns between reader and writer
    readSem protects readCount, writeSem protects writeCount
    */
    Semaphore resSem = new Semaphore(1), readTry = new Semaphore(1), readSem = new Semaphore(1), writeSem = new Semaphore(1);

    @Override
    public int read() throws InterruptedException {
        readTry.acquire();
        readSem.acquire();
        readCount++;
        if(readCount==1) {
            resSem.acquire();
        }
        readSem.release();
        readTry.release();

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
        writeSem.acquire();
        writeCount++;
        if(writeCount==1) {
            readTry.acquire(); // the first writer who can blocks out readers
        }
        writeSem.release();

        resSem.acquire(); // block out other writers from accessing resource
        resource = x;
        System.out.println("Wrote resource: " + resource);
        resSem.release();

        writeSem.acquire();
        writeCount--;
        if(writeCount==0) {
            readTry.release(); // The last writer is done, it must now give the turn back to readers
        }
        writeSem.release();
    }
}
