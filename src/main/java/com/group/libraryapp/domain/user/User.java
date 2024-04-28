package com.group.libraryapp.domain.user;

import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id= null;
    //동명이인 삭제 불가현상 방지를위해 동명이인 등록을 방지
    @Column(nullable = false, length = 20, unique = true)//name varchar2(20)
    private String name;
    @Column(nullable = false)
    private Integer age;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserLoanHistory> userLoanHistories = new ArrayList<>();

    protected User(){}

    public User(String name, Integer age) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(String.format("Invalid name(%s) came in", name));
        }else if (age == null || age < 1 || !(age instanceof Integer)) {
             // age 필드에 대한 추가적인 유효성 검사
            throw new IllegalArgumentException(String.format("Invalid age(%d) came in", age));
        }
    
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public Long getId() {
        return id;
    }


    public void updateName(String name){
        this.name = name;
    }

    public void loanBook(String bookName){



        this.userLoanHistories.add(new UserLoanHistory(this,bookName));
    }

    public void returnBook(String bookName){
        UserLoanHistory targetHistory = this.userLoanHistories.stream()
                //findUserLoanHistory
                .filter(history -> history.getBookName().equals(bookName))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
        //반납상태 확인
        // isReturn 속성이 true일 경우 예외 발생
        if (targetHistory.isReturn()) {
            throw new IllegalArgumentException("This is already loan book");
        }
        // return status true
        targetHistory.doReturn();
    }

}
