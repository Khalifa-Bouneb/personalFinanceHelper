package com.finance.service;

import com.finance.model.Category;
import com.finance.model.Item;
import com.finance.repository.CategoryRepository;
import com.finance.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {
    
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    
    // Find one item by ID
    public Optional<Item> findOne(String id) {
        return itemRepository.findById(id);
    }
    
    // Find many (all) items
    public List<Item> findMany() {
        return itemRepository.findAll();
    }
    
    // Create one item
    public Item createOne(Item item) {
        // Set category reference if categoryId is provided
        if (item.getCategoryId() != null) {
            categoryRepository.findById(item.getCategoryId()).ifPresent(item::setCategory);
        }
        return itemRepository.save(item);
    }
    
    // Update one item
    public Optional<Item> updateOne(String id, Item itemDetails) {
        return itemRepository.findById(id).map(existingItem -> {
            if (itemDetails.getName() != null) {
                existingItem.setName(itemDetails.getName());
            }
            if (itemDetails.getCost() != null) {
                existingItem.setCost(itemDetails.getCost());
            }
            if (itemDetails.getCategoryId() != null) {
                existingItem.setCategoryId(itemDetails.getCategoryId());
                categoryRepository.findById(itemDetails.getCategoryId())
                        .ifPresent(existingItem::setCategory);
            }
            return itemRepository.save(existingItem);
        });
    }
    
    // Delete one item
    public boolean deleteOne(String id) {
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
