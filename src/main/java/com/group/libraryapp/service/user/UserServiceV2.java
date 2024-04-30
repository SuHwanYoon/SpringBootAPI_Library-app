package com.group.libraryapp.service.user;

import com.group.libraryapp.domain.user.User;
import com.group.libraryapp.domain.user.UserRepository;
import com.group.libraryapp.dto.user.request.UserCreateRequest;
import com.group.libraryapp.dto.user.request.UserUpdateRequest;
import com.group.libraryapp.dto.user.response.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceV2 {

    private final UserRepository userRepository;

    public UserServiceV2(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    //트랜잭셔널 어노테이션으로 인해 모든 트랜잭션이 처리가 끝나고 반영됨
    //not error -> commit
    //error -> rollback
    @Transactional
    public void saveUser(UserCreateRequest request){
        //"insert into user (name,age) values (?,?)";
        // save -> Insert query
        //user.getId  -> 1,2,3.......... auto generated
        //화면에서 입력받은 이름, 나이를 받아서 insert
        User user = userRepository.save(new User(request.getName(),request.getAge()));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getUsers(){
        //1-1. 유저테이블 정보가져오기
/*        List<User> users = userRepository.findAll();
        // 1-2. 목록버튼 클릭시 유저테이블정보에 response된 ID,name,age를 매핑후 리스트로 return
        return users.stream()
                .map(userDto -> new UserResponse(userDto.getId(),userDto.getName(),userDto.getAge()))
                .collect(Collectors.toList());*/


        //3. 1-1,1-3를 리팩토링 UserResponse의 생성자 메소드(new)를 호출
        return userRepository.findAll().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional//persistent context - dirty check
    public void updateUser(UserUpdateRequest request){
        // 유저테이블에서 입력받은 ID로 select후 존재하지않으면 예외를 던진다
        User user = userRepository.findById(request.getId())
                .orElseThrow(IllegalAccessError :: new);

        //영속성 컨텍스트를 사용 케이스 (1-1,1-2)

        //1-1.직접 save메서드로 변경사항을 명시적으로 update처리
        //userRepository.save(user);
        //1-2. 1-1을 리팩토링
        //select 한 유저엔티티에 입력받은 유저이름의 이름을 넣는다
        //영속성 컨텍스트 효과로 인해 변화를 감지하고 자동으로 update처리(dirty check)
        user.updateName(request.getName());
    }

    @Transactional
    public void deleteUser(String name){

         //1-1.삭제 버튼을 눌렀을 경우 입력받은 name으로 하나의 유저를 검색
/*       User deleteuser = userRepository.findByOneName(name);
        //삭제할 유저가 엔티티가 null이라면 예외를 던진다
        if (deleteuser == null){
            throw new IllegalArgumentException();
        }

 */      //1-2. 입력받은 name으로 검색한 엔티티가 존재하지않는다면
        /*  if(!userRepository.existByName(name)){
            //예외를 던진다
            throw new IllegalArgumentException();
        }
        * */

        //2-1. 입력받은 name으로 user엔티티를 검색하여 존재하지않으면 예외를 던진다
        User deleteuser = userRepository.findByName(name)
                .orElseThrow(IllegalAccessError::new);
        //2-2. 존재하는 경우는 delete메서드로 검색한 엔티티를 삭제
        //delete from user where name = ?;
        userRepository.delete(deleteuser);
    }

}
