// BookCalback.aidl
package com.heaotian.bookservice;

// Declare any non-default types here with import statements

interface BookCalback {
    void getCount(int count);
    void getBookName();
    void addBook();
}
