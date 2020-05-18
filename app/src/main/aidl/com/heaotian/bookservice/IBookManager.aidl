// IBookManager.aidl
package com.heaotian.bookservice;

// Declare any non-default types here with import statements
import com.heaotian.bookservice.bean.Book;
interface IBookManager {
    int getBookList();
    void addBook(inout Book book);
}
