package com.github.pielena.atm.service.impl;

import com.github.pielena.atm.exception.CellException;
import com.github.pielena.atm.exception.MoneyBoxException;
import com.github.pielena.atm.model.Banknote;
import com.github.pielena.atm.model.BanknoteValue;
import com.github.pielena.atm.model.MoneyBox;
import com.github.pielena.atm.service.MoneyBoxService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MoneyBoxServiceImplTest {

    MoneyBoxService moneyBoxService;
    MoneyBox moneyBox;
    List<Banknote> banknotes;

    @BeforeEach
    void setUp() {
        moneyBox = new MoneyBox();
        moneyBoxService = new MoneyBoxServiceImpl(new CellServiceImpl());
        banknotes = List.of(new Banknote(BanknoteValue.ONE_HUNDRED), new Banknote(BanknoteValue.TWO_HUNDRED),
                new Banknote(BanknoteValue.FIVE_HUNDRED), new Banknote(BanknoteValue.ONE_THOUSAND),
                new Banknote(BanknoteValue.TWO_THOUSAND), new Banknote(BanknoteValue.TWO_THOUSAND),
                new Banknote(BanknoteValue.TWO_THOUSAND), new Banknote(BanknoteValue.TWO_THOUSAND));
        moneyBoxService.putMoney(moneyBox, banknotes);
    }

    @Test
    void getCorrectSum() {
        List<Banknote> expected = List.of(new Banknote(BanknoteValue.ONE_HUNDRED), new Banknote(BanknoteValue.TWO_HUNDRED),
                new Banknote(BanknoteValue.TWO_THOUSAND), new Banknote(BanknoteValue.TWO_THOUSAND),
                new Banknote(BanknoteValue.TWO_THOUSAND));
        int expectedBalance = 3500;

        List<Banknote> result = moneyBoxService.getMoney(moneyBox, 6300);
        result.sort(Comparator.comparingInt(el -> el.banknoteValue().getValue()));

        assertEquals(expected, result);
        assertEquals(expectedBalance, moneyBoxService.getBalance(moneyBox));
    }

    @Test
    void getInvalidSum() {
        Exception thrown = assertThrows(MoneyBoxException.class,
                () -> moneyBoxService.getMoney(moneyBox, 6350));
        assertEquals("Invalid sum", thrown.getMessage());
    }

    @Test
    void getNegativeSum() {
        Exception thrown = assertThrows(MoneyBoxException.class,
                () -> moneyBoxService.getMoney(moneyBox, -3000));
        assertEquals("Invalid sum", thrown.getMessage());
    }

    @Test
    void getMoreThanCurrentSum() {
        Exception thrown = assertThrows(MoneyBoxException.class,
                () -> moneyBoxService.getMoney(moneyBox, 27000));
        assertEquals("Not enough money in ATM", thrown.getMessage());
    }

    @Test
    void getMoreThanCurrentBanknotes() {
        Exception thrown = assertThrows(MoneyBoxException.class,
                () -> moneyBoxService.getMoney(moneyBox, 1900));
        assertEquals("Not enough banknotes for this sum", thrown.getMessage());
    }

    @Test
    void putMoney() {
        moneyBoxService.putMoney(moneyBox, banknotes);
        int result = moneyBoxService.getBalance(moneyBox);
        assertEquals(19600, result);
    }

    @Test
    void putLotsMoney() {
        List<Banknote> banknotes = IntStream.rangeClosed(1, 2001)
                .mapToObj(el -> new Banknote(BanknoteValue.ONE_HUNDRED))
                .collect(Collectors.toList());

        Exception thrown = assertThrows(CellException.class,
                () -> moneyBoxService.putMoney(moneyBox, banknotes));
        assertEquals("Not enough cell space", thrown.getMessage());
    }

    @Test
    void getBalance() {
        int result = moneyBoxService.getBalance(moneyBox);
        assertEquals(9800, result);
    }
}