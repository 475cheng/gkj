package com.example.demo.dao;

import com.example.demo.entity.RequestInformation;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RequestInformationRepository extends JpaRepository<RequestInformation, Integer> {

    @Modifying
    @Transactional
    @Query(value = "update request_information set is_deal=1 where id in (select a.id from (select id from request_information where user_id=?1 and bridge_node_id!=0  ORDER BY create_time desc limit 3) a) ", nativeQuery = true)
    int updateDeal(Integer userId);

    @Modifying
    @Transactional
    @Query(value = "update request_information set is_used=1 where id in (select a.id from (select id from request_information where user_id=?1 and bridge_node_id!=0  ORDER BY create_time desc limit 3) a) ", nativeQuery = true)
    int updateIsValid(Integer userId);

    @Query(value = "select * from request_information where user_id = ?1 and bridge_node_id!=0 ", nativeQuery = true)
    List<RequestInformation> findByUserId(Integer userId);

    @Query(value = "select * from request_information where user_id = ?1 and bridge_node_id=0 ", nativeQuery = true)
    List<RequestInformation> findByUserIdNodeZero(Integer userId);

    @Query(value = "select count(1) from request_information r LEFT JOIN bridge_node b on r.bridge_node_id=b.id " +
            "where r.user_id=?1 and b.is_valid=1", nativeQuery = true)
    int findByUserIdIsValid(Integer userId);
}
