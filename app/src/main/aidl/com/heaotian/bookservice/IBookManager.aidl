// IBookManager.aidl
package com.heaotian.bookservice;

// Declare any non-default types here with import statements
import com.heaotian.bookservice.bean.Book;
import com.heaotian.bookservice.INewBookArrivedListener;
interface IBookManager {
    List<Book> getBookList();
    void addBook(inout Book book);
    void registerListener(INewBookArrivedListener listener);
    void unRegisterListener(INewBookArrivedListener listener);
}
