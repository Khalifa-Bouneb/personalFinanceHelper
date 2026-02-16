package com.finance.config;

import com.finance.enums.GoalType;
import com.finance.enums.TransactionSign;
import com.finance.model.*;
import com.finance.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;
    private final TransactionRepository transactionRepository;
    private final GoalRepository goalRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) {
        // Clear existing data
        log.info("Clearing existing data...");
        transactionRepository.deleteAll();
        goalRepository.deleteAll();
        itemRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
        
        log.info("Seeding database with sample data...");
        
        // Create Users
        User user1 = new User();
        user1.setId(1);
        user1.setName("John Doe");
        user1.setEmail("john.doe@example.com");
        user1.setPasswordHash(passwordEncoder.encode("password123"));
        user1.setCurrency("TND");
        user1.setCreatedAt(LocalDateTime.now());
        
        User user2 = new User();
        user2.setId(2);
        user2.setName("Jane Smith");
        user2.setEmail("jane.smith@example.com");
        user2.setPasswordHash(passwordEncoder.encode("password123"));
        user2.setCurrency("EUR");
        user2.setCreatedAt(LocalDateTime.now());
        
        userRepository.saveAll(List.of(user1, user2));
        log.info("Created {} users", 2);
        
        // Create Categories
        Category groceries = new Category();
        groceries.setName("Groceries");
        groceries.setColor("#4CAF50");
        groceries.setIcon("shopping_cart");
        groceries.setCreatedAt(LocalDateTime.now());
        
        Category transport = new Category();
        transport.setName("Transport");
        transport.setColor("#2196F3");
        transport.setIcon("directions_car");
        transport.setCreatedAt(LocalDateTime.now());
        
        Category entertainment = new Category();
        entertainment.setName("Entertainment");
        entertainment.setColor("#FF9800");
        entertainment.setIcon("movie");
        entertainment.setCreatedAt(LocalDateTime.now());
        
        Category utilities = new Category();
        utilities.setName("Utilities");
        utilities.setColor("#9C27B0");
        utilities.setIcon("home");
        utilities.setCreatedAt(LocalDateTime.now());
        
        Category salary = new Category();
        salary.setName("Salary");
        salary.setColor("#4CAF50");
        salary.setIcon("attach_money");
        salary.setCreatedAt(LocalDateTime.now());

        Category restaurant = new Category();
        restaurant.setName("Restaurant");
        restaurant.setColor("#E91E63");
        restaurant.setIcon("restaurant");
        restaurant.setCreatedAt(LocalDateTime.now());

        Category shopping = new Category();
        shopping.setName("Shopping");
        shopping.setColor("#00BCD4");
        shopping.setIcon("shopping_bag");
        shopping.setCreatedAt(LocalDateTime.now());

        Category health = new Category();
        health.setName("Health");
        health.setColor("#F44336");
        health.setIcon("local_hospital");
        health.setCreatedAt(LocalDateTime.now());

        Category education = new Category();
        education.setName("Education");
        education.setColor("#3F51B5");
        education.setIcon("school");
        education.setCreatedAt(LocalDateTime.now());

        List<Category> categories = categoryRepository.saveAll(
                List.of(groceries, transport, entertainment, utilities, salary,
                        restaurant, shopping, health, education)
        );
        log.info("Created {} categories", categories.size());
        
        // Create Items
        Item milk = new Item();
        milk.setName("Milk");
        milk.setCost(new BigDecimal("3.50"));
        milk.setCategoryId(groceries.getId());
        milk.setCategory(groceries);
        
        Item bread = new Item();
        bread.setName("Bread");
        bread.setCost(new BigDecimal("2.00"));
        bread.setCategoryId(groceries.getId());
        bread.setCategory(groceries);
        
        Item gasFuel = new Item();
        gasFuel.setName("Gas/Fuel");
        gasFuel.setCost(new BigDecimal("50.00"));
        gasFuel.setCategoryId(transport.getId());
        gasFuel.setCategory(transport);
        
        Item movieTicket = new Item();
        movieTicket.setName("Movie Ticket");
        movieTicket.setCost(new BigDecimal("12.00"));
        movieTicket.setCategoryId(entertainment.getId());
        movieTicket.setCategory(entertainment);
        
        Item electricity = new Item();
        electricity.setName("Electricity Bill");
        electricity.setCost(new BigDecimal("80.00"));
        electricity.setCategoryId(utilities.getId());
        electricity.setCategory(utilities);
        
        List<Item> items = itemRepository.saveAll(
                List.of(milk, bread, gasFuel, movieTicket, electricity)
        );
        log.info("Created {} items", items.size());
        
        // Create Transactions
        Transaction t1 = new Transaction();
        t1.setUserId(user1.getId());
        t1.setUser(user1);
        t1.setAmount(new BigDecimal("3500.00"));
        t1.setSign(TransactionSign.POSITIVE);
        t1.setTransactionDate(LocalDateTime.now().minusDays(30));
        t1.setCategoryId(salary.getId());
        t1.setCategory(salary);
        
        Transaction t2 = new Transaction();
        t2.setUserId(user1.getId());
        t2.setUser(user1);
        t2.setAmount(new BigDecimal("150.00"));
        t2.setSign(TransactionSign.NEGATIVE);
        t2.setTransactionDate(LocalDateTime.now().minusDays(25));
        t2.setCategoryId(groceries.getId());
        t2.setCategory(groceries);
        
        Transaction t3 = new Transaction();
        t3.setUserId(user1.getId());
        t3.setUser(user1);
        t3.setAmount(new BigDecimal("50.00"));
        t3.setSign(TransactionSign.NEGATIVE);
        t3.setTransactionDate(LocalDateTime.now().minusDays(20));
        t3.setCategoryId(transport.getId());
        t3.setCategory(transport);
        t3.setItemId(gasFuel.getId());
        t3.setItem(gasFuel);
        
        Transaction t4 = new Transaction();
        t4.setUserId(user1.getId());
        t4.setUser(user1);
        t4.setAmount(new BigDecimal("80.00"));
        t4.setSign(TransactionSign.NEGATIVE);
        t4.setTransactionDate(LocalDateTime.now().minusDays(15));
        t4.setCategoryId(utilities.getId());
        t4.setCategory(utilities);
        t4.setItemId(electricity.getId());
        t4.setItem(electricity);
        
        Transaction t5 = new Transaction();
        t5.setUserId(user2.getId());
        t5.setUser(user2);
        t5.setAmount(new BigDecimal("4000.00"));
        t5.setSign(TransactionSign.POSITIVE);
        t5.setTransactionDate(LocalDateTime.now().minusDays(28));
        t5.setCategoryId(salary.getId());
        t5.setCategory(salary);
        
        Transaction t6 = new Transaction();
        t6.setUserId(user2.getId());
        t6.setUser(user2);
        t6.setAmount(new BigDecimal("24.00"));
        t6.setSign(TransactionSign.NEGATIVE);
        t6.setTransactionDate(LocalDateTime.now().minusDays(10));
        t6.setCategoryId(entertainment.getId());
        t6.setCategory(entertainment);
        t6.setItemId(movieTicket.getId());
        t6.setItem(movieTicket);

        // Additional transactions for better forecasting data
        Transaction t7 = new Transaction();
        t7.setUserId(user1.getId());
        t7.setUser(user1);
        t7.setAmount(new BigDecimal("35.00"));
        t7.setSign(TransactionSign.NEGATIVE);
        t7.setTransactionDate(LocalDateTime.now().minusDays(18));
        t7.setCategoryId(restaurant.getId());
        t7.setCategory(restaurant);

        Transaction t8 = new Transaction();
        t8.setUserId(user1.getId());
        t8.setUser(user1);
        t8.setAmount(new BigDecimal("120.00"));
        t8.setSign(TransactionSign.NEGATIVE);
        t8.setTransactionDate(LocalDateTime.now().minusDays(12));
        t8.setCategoryId(shopping.getId());
        t8.setCategory(shopping);

        Transaction t9 = new Transaction();
        t9.setUserId(user1.getId());
        t9.setUser(user1);
        t9.setAmount(new BigDecimal("45.00"));
        t9.setSign(TransactionSign.NEGATIVE);
        t9.setTransactionDate(LocalDateTime.now().minusDays(8));
        t9.setCategoryId(health.getId());
        t9.setCategory(health);

        Transaction t10 = new Transaction();
        t10.setUserId(user1.getId());
        t10.setUser(user1);
        t10.setAmount(new BigDecimal("200.00"));
        t10.setSign(TransactionSign.NEGATIVE);
        t10.setTransactionDate(LocalDateTime.now().minusDays(5));
        t10.setCategoryId(education.getId());
        t10.setCategory(education);

        Transaction t11 = new Transaction();
        t11.setUserId(user1.getId());
        t11.setUser(user1);
        t11.setAmount(new BigDecimal("60.00"));
        t11.setSign(TransactionSign.NEGATIVE);
        t11.setTransactionDate(LocalDateTime.now().minusDays(3));
        t11.setCategoryId(groceries.getId());
        t11.setCategory(groceries);

        Transaction t12 = new Transaction();
        t12.setUserId(user1.getId());
        t12.setUser(user1);
        t12.setAmount(new BigDecimal("15.00"));
        t12.setSign(TransactionSign.NEGATIVE);
        t12.setTransactionDate(LocalDateTime.now().minusDays(1));
        t12.setCategoryId(transport.getId());
        t12.setCategory(transport);
        
        List<Transaction> transactions = transactionRepository.saveAll(
                List.of(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12)
        );
        log.info("Created {} transactions", transactions.size());
        
        // Create Goals
        Goal monthlyGroceryGoal = new Goal();
        monthlyGroceryGoal.setUserId(user1.getId());
        monthlyGroceryGoal.setUser(user1);
        monthlyGroceryGoal.setCategoryId(groceries.getId());
        monthlyGroceryGoal.setCategory(groceries);
        monthlyGroceryGoal.setMaxAmount(new BigDecimal("500.00"));
        monthlyGroceryGoal.setType(GoalType.MONTHLY);
        monthlyGroceryGoal.setStartDate(LocalDate.now().withDayOfMonth(1));
        monthlyGroceryGoal.setEndDate(LocalDate.now().withDayOfMonth(1).plusMonths(1).minusDays(1));
        monthlyGroceryGoal.setCreatedAt(LocalDateTime.now());
        
        Goal yearlyTransportGoal = new Goal();
        yearlyTransportGoal.setUserId(user1.getId());
        yearlyTransportGoal.setUser(user1);
        yearlyTransportGoal.setCategoryId(transport.getId());
        yearlyTransportGoal.setCategory(transport);
        yearlyTransportGoal.setMaxAmount(new BigDecimal("3000.00"));
        yearlyTransportGoal.setType(GoalType.YEARLY);
        yearlyTransportGoal.setStartDate(LocalDate.now().withDayOfYear(1));
        yearlyTransportGoal.setEndDate(LocalDate.now().withDayOfYear(1).plusYears(1).minusDays(1));
        yearlyTransportGoal.setCreatedAt(LocalDateTime.now());
        
        Goal monthlyEntertainmentGoal = new Goal();
        monthlyEntertainmentGoal.setUserId(user2.getId());
        monthlyEntertainmentGoal.setUser(user2);
        monthlyEntertainmentGoal.setCategoryId(entertainment.getId());
        monthlyEntertainmentGoal.setCategory(entertainment);
        monthlyEntertainmentGoal.setMaxAmount(new BigDecimal("200.00"));
        monthlyEntertainmentGoal.setType(GoalType.MONTHLY);
        monthlyEntertainmentGoal.setStartDate(LocalDate.now().withDayOfMonth(1));
        monthlyEntertainmentGoal.setEndDate(LocalDate.now().withDayOfMonth(1).plusMonths(1).minusDays(1));
        monthlyEntertainmentGoal.setCreatedAt(LocalDateTime.now());
        
        List<Goal> goals = goalRepository.saveAll(
                List.of(monthlyGroceryGoal, yearlyTransportGoal, monthlyEntertainmentGoal)
        );
        log.info("Created {} goals", goals.size());
        
        log.info("Database seeding completed successfully!");
    }
}
