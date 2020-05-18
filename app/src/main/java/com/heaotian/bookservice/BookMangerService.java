package com.heaotian.bookservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import com.heaotian.bookservice.bean.Book;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 书籍管理线程
 */
public class BookMangerService extends Service {

    private CopyOnWriteArrayList<com.heaotian.bookservice.bean.Book> books = new CopyOnWriteArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new BookMangerBinder();
    }



    @Override
    public void onCreate() {
        super.onCreate();
        books.add(new Book("java讲义"));
    }

    /**
     * 服务端代理类
     */
    private class BookMangerBinder extends IBookManager.Stub{
        @Override
        public int getBookList() throws RemoteException {
            return books.size();
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            books.add(book);
        }
    }
}
