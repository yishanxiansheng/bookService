// INewBookArrivedListener.aidl
package com.heaotian.bookservice;

import com.heaotian.bookservice.bean.Book;
interface INewBookArrivedListener {
    void onNewBookArriced(inout Book book);
}
