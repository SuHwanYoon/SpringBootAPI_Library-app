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

    //not error -> commit
    //error -> rollback
    @Transactional
    public void saveUser(UserCreateRequest request){
        User user = userRepository.save(new User(request.getName(),request.getAge()));
        //String sql = "insert into user (name,age) values (?,?)";
        // save -> Insert query
        //user.getId  -> 1,2,3.......... auto generated
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getUsers(){
            //String sql = "select * from user";
/*        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userDto -> new UserResponse(userDto.getId(),userDto.getName(),userDto.getAge()))
                .collect(Collectors.toList());*/
/*        return userRepository.findAll().stream()
                .map(userDto -> new UserResponse(userDto.getId(),userDto.getName(),userDto.getAge()))
                .collect(Collectors.toList());*/
        return userRepository.findAll().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional//persistent context - dirty check
    public void updateUser(UserUpdateRequest request){
        User user = userRepository.findById(request.getId())
                .orElseThrow(IllegalAccessError :: new);
        //update request user name
        user.updateName(request.getName());
        //insert updateUserName
        //userRepository.save(user);//persistent context - dirty check
    }

    @Transactional
    public void deleteUser(String name){
        //select * from USER where name = ?;

/*       User deleteuser = userRepository.findByName(name);
        if (deleteuser == null){
            throw new IllegalArgumentException();
        }

 */
        /*  if(!userRepository.existByName(name)){
            throw new IllegalArgumentException();
        }

        User user = userRepository.findByName(name);
        userRepository.delete(user)
        *
        * */
        User deleteuser = userRepository.findByName(name)
                .orElseThrow(IllegalAccessError::new);
        //delete from user where name = ?;
        userRepository.delete(deleteuser);
    }

}
