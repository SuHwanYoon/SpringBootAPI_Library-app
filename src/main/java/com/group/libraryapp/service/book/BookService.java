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

import java.util.Optional;


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
        //1.북 테이블에 대출하려는 책이름이 존재하는지 확인
        Book book = bookRepository.findByName(request.getBookName())
                .orElseThrow(IllegalArgumentException::new);
        //2. 유저 테이블에 해당 책이름으로 대출한 유저가 존재하는지 확인
        User user = userRepository.findByName(request.getUserName())
                .orElseThrow(IllegalArgumentException::new);
        //3. 유저아이디와 책이름으로 대출기록이 있는 유저의 대출기록 가져오기
        Optional<UserLoanHistory> loanHistory
                = userLoanHistoryRepository.findByUserIdAndBookName(user.getId(), request.getBookName());
        //대출기록 추가를 막기 위해 해당 유저가 반납한 기록이존재한다면
        if (loanHistory.isPresent() && loanHistory.get().isReturn()) {
            //기존 대출목록의 상태를 다시 false로 바꿔서 대출상태로 만든다
            loanHistory.get().reLoanBook();
            return;
        }
        //다른 유저가 이미 대출된 책을 대출하려고 하는 것을 방지하기 위해 해당책이 대출중이라면
        if(userLoanHistoryRepository.existsByBookNameAndIsReturn(book.getName(),false)){
            //예외를 던져서 대출을 막는다
            throw new IllegalArgumentException("already loan in service Layer");
        }

        //최초로 대출하는경우
        user.loanBook(book.getName());
    }

    @Transactional
    public void returnBook(BookReturnRequest request) {
        //1.화면에서 입력한 유저이름으로 검색후 존재하지않으면 예외를 던진다
        User user = userRepository.findByName(request.getUserName())
                .orElseThrow(IllegalArgumentException::new);

        //2.유저 ID와 입력한 책이름으로 대출기록을 검색하고 존재하지 않으면 예외를 던진다
        //UserLoanHistory history = userLoanHistoryRepository.findByUserIdAndBookName(user.getId(), request.getBookName())
        //        .orElseThrow(IllegalArgumentException::new);

        //3.대출기록이 존재하면 반납상태를 Ture로 바꿔서 반납처리
        //history.doReturn();
        //4. 변화된 대출기록을 insert해서 update(Dirty Checking)
        // userLoanHistoryRepository.save(history);

        //confirm Lazy Loading
        System.out.println("Hello");

        //7.  2~4의 과정을 User엔티티 로직 리팩토링으로 간소화
        user.returnBook(request.getBookName());

    }




















}
