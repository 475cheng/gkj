package com.example.demo.dao;

import com.example.demo.entity.BridgeNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BridgeNodeRepository extends JpaRepository<BridgeNode, Integer> {

    @Query(value = "select * from bridge_node", nativeQuery = true)
    List<BridgeNode> queryAll();

    @Query(value = "select * from bridge_node where is_valid= ?1 and is_removed=?2", nativeQuery = true)
    List<BridgeNode> queryByIsValidMove(Integer isValid, Integer isRemoved);

    @Query(value = "select * from bridge_node where is_valid= ?1", nativeQuery = true)
    List<BridgeNode> queryByIsValid(Integer isValid);
}
