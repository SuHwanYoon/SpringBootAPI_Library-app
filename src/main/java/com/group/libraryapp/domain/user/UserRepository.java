package com.group.libraryapp.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

//select * from USER-[find] WHERE-[By] name = ?;

    //user findByName(String name);
    Optional<User> findByName(String name);

    List<User> findAllByNameAndAge(String name, int age);

    List<User> findAllByAgeBetween(int startAge, int endAge);
    boolean existsByName(String name);

    long countByAge(Integer age);

}
