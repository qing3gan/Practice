package util;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Agony on 2018/7/6.
 */
public class ReadWriteLock {

    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public void read(){
        try{
            readWriteLock.readLock().lock();
            System.out.println(Thread.currentThread()+" get read lock.");
        } finally {
            readWriteLock.readLock().unlock();
            System.out.println(Thread.currentThread()+" release read lock.");
        }
    }

    public void write(){
        try {
            readWriteLock.writeLock().lock();
            System.out.println(Thread.currentThread()+" get write lock.");
        } finally {
            readWriteLock.writeLock().unlock();
            System.out.println(Thread.currentThread()+" release write lock.");
        }
    }

    public static void main(String[] args) {
        ReadWriteLock lock = new ReadWriteLock();
        new Thread(()->{
            while (true){
                lock.read();
                lock.write();
            }
        }).start();
        new Thread(()->{
            while (true){
                lock.read();
                lock.write();
            }
        }).start();
    }
}
