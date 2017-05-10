package com.example.jeobmallari.ilib;

/**
 * Created by Jeob Mallari on 5/7/2017.
 */

public class Reservation {

    String bookID;
    String userID;
    String resID;
    String author;
    String book;
    String dueDate;
    String email;
    String student;
    String validUntil;

    public Reservation(){

    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookId) {
        this.bookID = bookId;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getResID() {
        return resID;
    }

    public void setResID(String resID) {
        this.resID = resID;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(String validUntil) {
        this.validUntil = validUntil;
    }

}
