package com.rewards.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

//import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

//import com.rewards.exception.CustomerNotFoundException;
import com.rewards.exception.InvalidMonthsException;
//import com.rewards.exception.NullInputException;
//import com.rewards.model.Customer;
//import com.rewards.model.RewardsResponse;
import com.rewards.model.Transaction;
import com.rewards.repository.CustomerRepository;
import com.rewards.repository.TransactionRepository;

@ExtendWith(MockitoExtension.class)
class RewardsServiceTest {
    @Mock
    private CustomerRepository customerRepository;
    
    @Mock
    private TransactionRepository transactionRepository;
    
    @InjectMocks
    private RewardsService rewardsService;
    
    @Test
    void calculatePoints_Amount_Below50() {
        Transaction transaction = createTransaction(45.0);
        assertEquals(0, rewardsService.calculatePointsForTransaction(transaction));
    }
    
    @Test
    void calculatePoints_Amount_Exactly50() {
        Transaction transaction = createTransaction(50.0);
        assertEquals(0, rewardsService.calculatePointsForTransaction(transaction));
    }
    
    @Test
    void calculatePoints_Amount_Between50And100() {
        Transaction transaction = createTransaction(75.0);
        assertEquals(25, rewardsService.calculatePointsForTransaction(transaction));
    }
    
    @Test
    void calculatePoints_Amount_Exactly100() {
        Transaction transaction = createTransaction(100.0);
        assertEquals(50, rewardsService.calculatePointsForTransaction(transaction));
    }
    
    @Test
    void calculatePoints_Amount_Above100() {
        Transaction transaction = createTransaction(120.0);
        assertEquals(90, rewardsService.calculatePointsForTransaction(transaction));
    }
    
    @Test
    void calculateRewards_NullMonths_ThrowsException() {
        assertThrows(InvalidMonthsException.class, () -> 
            rewardsService.calculateRewards(1L, null));
    }
    
    @Test
    void calculateRewards_NegativeMonths_ThrowsException() {
        assertThrows(InvalidMonthsException.class, () -> 
            rewardsService.calculateRewards(1L, -1));
    }
    
    private Transaction createTransaction(Double amount) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        return transaction;
    }
}