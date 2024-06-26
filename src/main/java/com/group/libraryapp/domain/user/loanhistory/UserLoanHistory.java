package com.group.libraryapp.domain.user.loanhistory;

import com.group.libraryapp.domain.user.User;

import javax.persistence.*;

@Entity
public class UserLoanHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;

    @JoinColumn(nullable = false)
    @ManyToOne
    private User user;

    private String bookName;

    private boolean isReturn;



    protected UserLoanHistory(){

    }

    public boolean isReturn() {
        return this.isReturn;
    }


    public UserLoanHistory(User user, String bookName) {
        this.user = user;
        this.bookName = bookName;
        this.isReturn = false;
    }


    // return status true
    public void doReturn(){
        this.isReturn = true;
    }

    // return status true
    public void reLoanBook(){
        this.isReturn = false;
    }

    public String getBookName() {
        return this.bookName;
    }
}
