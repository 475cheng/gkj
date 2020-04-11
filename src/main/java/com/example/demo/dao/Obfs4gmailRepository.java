package com.example.demo.dao;

import com.example.demo.entity.BridgeNode;
import com.example.demo.entity.Obfs4gmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Obfs4gmailRepository extends JpaRepository<Obfs4gmail, Integer> {

    @Query(value = "select DISTINCT username from obfs4gmail", nativeQuery = true)
    List<String> queryAllDisUserName();

    @Query(value = "select count(1) from (select DISTINCT host from obfs4gmail where username in ?1 " +
            " and  time >='2019-07-31 00:00:00' and time<'2019-08-01 00:00:00') a ", nativeQuery = true)
    Integer queryCountDisHost(List<String> usernames);
}
