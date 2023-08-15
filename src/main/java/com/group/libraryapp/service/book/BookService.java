package com.group.libraryapp.service.book;

import com.group.libraryapp.domain.book.Book;
import com.group.libraryapp.domain.book.BookRepository;
import com.group.libraryapp.domain.user.User;
import com.group.libraryapp.domain.user.UserRepository;
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory;
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository;
import com.group.libraryapp.dto.book.request.BookCreateRequest;
import com.group.libraryapp.dto.book.request.BookLoanRequest;
import com.group.libraryapp.dto.book.request.BookReturnRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class BookService {

    private final BookRepository bookRepository;
    private final UserLoanHistoryRepository userLoanHistoryRepository;
    private final UserRepository userRepository;

    public BookService(BookRepository bookRepository, UserLoanHistoryRepository userLoanHistoryRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userLoanHistoryRepository = userLoanHistoryRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void saveBook(BookCreateRequest request){
        bookRepository.save(new Book(request.getName()));
    }

    @Transactional
    public void loanBook(BookLoanRequest request) {
        //1.get Book Object
        Book book = bookRepository.findByName(request.getBookName())
                .orElseThrow(IllegalArgumentException::new);

        //2.select user_loan_history
        //3. if false throw exception
        if(userLoanHistoryRepository.existsByBookNameAndIsReturn(book.getName(),false)){
            throw new IllegalArgumentException("already loan");
        }

        //4.get User Object by userName
        //5.if null throw IllegalArgumentException
        User user = userRepository.findByName(request.getUserName())
                .orElseThrow(IllegalArgumentException::new);
        //6.insert UserLoanHistory
        //userLoanHistoryRepository.save(new UserLoanHistory(user, book.getName()));

        //7.refactoring insert UserLoanHistory using User Object cascade
        user.loanBook(book.getName());
    }

    @Transactional
    public void returnBook(BookReturnRequest request) {
        //1.get User Object by userName
        //2.if null throw IllegalArgumentException
        User user = userRepository.findByName(request.getUserName())
                .orElseThrow(IllegalArgumentException::new);

        //3. get UserLoanHistory Object by userId and bookName
        //4.  using history constructor if null throw IllegalArgumentException
        //UserLoanHistory history = userLoanHistoryRepository.findByUserIdAndBookName(user.getId(), request.getBookName())
        //        .orElseThrow(IllegalArgumentException::new);

        //5.if not null doReturn
        //history.doReturn();
        //6. update(Dirty Checking) -userLoanHistoryRepository.save(history);

        //confirm Lazy Loading
        System.out.println("Hello");
        //7 refactoring 3,4,5,6
        user.returnBook(request.getBookName());

    }




















}
