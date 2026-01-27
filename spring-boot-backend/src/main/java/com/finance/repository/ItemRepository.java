package com.finance.repository;

import com.finance.model.Item;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends MongoRepository<Item, String> {
    
    List<Item> findByCategoryId(String categoryId);
}
