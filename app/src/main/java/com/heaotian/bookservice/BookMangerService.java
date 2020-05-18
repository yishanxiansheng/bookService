package com.heaotian.bookservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import com.heaotian.bookservice.bean.Book;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 书籍管理线程
 */
public class BookMangerService extends Service {

    /**
     * 具有线程同步功能的List,防止多个客户端同时访问时产生的同步问题
     */
    private CopyOnWriteArrayList<com.heaotian.bookservice.bean.Book> mBooks
            = new CopyOnWriteArrayList<>();

    /**
     * 存放所有注册了的客户端
     * RemoteCallbackList 系统专门提供用于删除跨进程listener的接口
     */
    private RemoteCallbackList<INewBookArrivedListener> mListeners
            = new RemoteCallbackList<>();

    private AtomicBoolean isServiceDestroyed = new AtomicBoolean(false);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new BookMangerBinder();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mBooks.add(new Book("java讲义"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isServiceDestroyed.set(true);
    }

    /**
     * 服务端代理类
     */
    private class BookMangerBinder extends IBookManager.Stub {

        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBooks;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBooks.add(book);
        }

        @Override
        public void registerListener(INewBookArrivedListener listener) throws RemoteException {
            mListeners.register(listener);
        }

        @Override
        public void unRegisterListener(INewBookArrivedListener listener) throws RemoteException {
            mListeners.unregister(listener);
        }
    }

    /**
     * 定时添加书籍
     */
    private class ServiceWorker implements Runnable {

        @Override
        public void run() {
            try {
                while (isServiceDestroyed.get()) {
                    Thread.sleep(50000);
                    Book book = new Book("书籍");
                    onNewBookArrived(book);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 通知客户端书籍到了
     *
     * @param book book
     */
    private void onNewBookArrived(Book book) {
        mBooks.add(book);
        for (int i = 0; i < mListeners.beginBroadcast(); i++) {
            try {
                mListeners.getBroadcastItem(i).onNewBookArriced(book);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mListeners.finishBroadcast();
    }
}
