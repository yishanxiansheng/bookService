package com.heaotian.bookservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import com.heaotian.bookservice.bean.Book;

public class MainActivity extends AppCompatActivity {

    /**
     * 远程书籍管理类
     */
    private IBookManager mBookManager;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            try {
                mBookManager = IBookManager.Stub.asInterface(iBinder);
                mBookManager.registerListener(mBookArrivedlistener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    /**
     * 观察者，观察有没有新书到
     */
    private INewBookArrivedListener mBookArrivedlistener = new INewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArriced(Book book) throws RemoteException {
            //运行在客户端的Binder线程，所以不能访问UI相关的内容
            Log.d("heshufan",book.getName());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 绑定服务
        Intent serviceIntent = new Intent(this, BookMangerService.class);
        bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);

        findViewById(R.id.add_book_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mBookManager.addBook(new Book("android"));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.get_books_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.d("books_size", mBookManager.getBookList().toString() + "");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        unbindService(mConnection);
        if (mBookManager !=null && mBookManager.asBinder().isBinderAlive()){
            try {
                mBookManager.unRegisterListener(mBookArrivedlistener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }
}
