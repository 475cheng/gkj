package com.example.demo.dao;

import com.example.demo.entity.BridgeNode;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User getByUserName(String userName);

    @Query(value = "select a.* from (select * from request_information where user_id in (\n" +
            "select b1.user_id from (\n" +
            "select user_id,count(id) c from request_information and bridge_node_id!=0 \n" +
            "GROUP BY user_id\n" +
            "HAVING c>5) b1 ) and bridge_node_id!=0 ) a left join (select * from request_information where user_id in (\n" +
            "select b1.user_id from (\n" +
            "select user_id,count(id) c from request_information and bridge_node_id!=0 \n" +
            "GROUP BY user_id\n" +
            "HAVING c>5) b1 ) and bridge_node_id!=0 ) b\n" +
            "on a.user_id=b.user_id and a.id<=b.id\n" +
            "group by a.user_id,a.id\n" +
            " having count(a.id)<=3", nativeQuery = true)
    Object[] queryZcUsers();

    @Query(value = "select a.* from request_information a left join request_information b\n" +
            "on a.user_id=b.user_id and a.id<=b.id\n" +
            "group by a.user_id,a.id\n" +
            " having count(a.id)<=3", nativeQuery = true)
    Object[] queryZcUserssss();

    @Query(value = "select COUNT(1) from request_information where user_id= ?1" +
            " and apply_credibility=?2 and node_status_credibility=?3 and totle_credibility=?4", nativeQuery = true)
    int queryTotleCredibility1(Integer userId, Integer apply_credibility, Integer node_status_credibility, Integer totle_credibility);

    @Query(value = "select COUNT(1) from request_information where user_id= ?1" +
            " and apply_credibility=?2 and node_status_credibility=?3", nativeQuery = true)
    int queryTotle(Integer userId, Integer apply_credibility, Integer node_status_credibility);

    @Modifying
    @Transactional
    @Query(value = "update user set node_status_credibility_3=node_status_credibility_3+1 where id in ?1 ", nativeQuery = true)
    int updateNodeStatusCredibilityThree(List<String> userIds);


    @Modifying
    @Transactional
    @Query(value = "update user set node_status_credibility_2=node_status_credibility_2+1 where id in ?1 ", nativeQuery = true)
    int updateNodeStatusCredibilityTwo(List<String> userIds);


    @Modifying
    @Transactional
    @Query(value = "update user set node_status_credibility_1=node_status_credibility_1+1 where id not in ?1 ", nativeQuery = true)
    int updateNodeStatusCredibilityOne(List<String> userIds);

    @Modifying
    @Transactional
    @Query(value = "update user set node_status_credibility_1=node_status_credibility_1+1 ", nativeQuery = true)
    int updateNodeStatusCredibilityOneAll();

    @Modifying
    @Transactional
    @Query(value = "update user set totle_request=totle_request+1 where id = ?1 ", nativeQuery = true)
    int updateNodeStatusAdd1(Integer userId);
}
