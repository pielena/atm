package com.github.pielena.atm.service.impl;

import com.github.pielena.atm.model.Banknote;
import com.github.pielena.atm.model.BanknoteValue;
import com.github.pielena.atm.model.Cell;
import com.github.pielena.atm.exception.CellException;
import com.github.pielena.atm.service.CellService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CellServiceImplTest {

    private CellService cellService;
    private Cell cell;
    private Banknote banknote;

    @BeforeEach
    void setUp() {
        cellService = new CellServiceImpl();
        cell = new Cell(BanknoteValue.FIVE_HUNDRED);
        banknote = new Banknote(BanknoteValue.FIVE_HUNDRED);
        List<Banknote> banknotes = List.of(banknote, banknote, banknote, banknote, banknote);
        cellService.putBanknotes(cell, banknotes);
    }

    @Test
    void putCorrectAmountBanknotes() {
        List<Banknote> banknotes = List.of(banknote);

        cellService.putBanknotes(cell, banknotes);
        int currentAmount = cellService.getCurrentAmount(cell);

        assertEquals(6, currentAmount);
    }

    @Test
    void putMoreThanCapacity() {
        List<Banknote> banknotes = IntStream.rangeClosed(1, 1996)
                .mapToObj(el -> banknote)
                .collect(Collectors.toList());

        Exception thrown = assertThrows(CellException.class,
                () -> cellService.putBanknotes(cell, banknotes));
        assertEquals("Not enough cell space", thrown.getMessage());
    }

    @Test
    void putInvalidBanknote() {
        List<Banknote> banknotes = new ArrayList<>();
        banknotes.add(banknote);
        banknotes.add(new Banknote(BanknoteValue.FIVE_THOUSAND));

        Exception thrown = assertThrows(CellException.class,
                () -> cellService.putBanknotes(cell, banknotes));
        assertEquals("Banknote value doesn't match cell value", thrown.getMessage());
    }

    @Test
    void getCorrectAmountBanknotes() {
        List<Banknote> resultList = cellService.getBanknotes(cell, 1);
        int currentAmount = cellService.getCurrentAmount(cell);
        assertEquals(4, currentAmount);
        assertEquals(List.of(new Banknote(BanknoteValue.FIVE_HUNDRED)), resultList);
    }

    @Test
    void getMoreThanBalance() {
        Exception thrown = assertThrows(CellException.class,
                () -> cellService.getBanknotes(cell, 6));
        assertEquals("Not enough banknotes in cell", thrown.getMessage());
    }

    @Test
    void getCurrentAmount() {
        int currentAmount = cellService.getCurrentAmount(cell);
        assertEquals(5, currentAmount);
    }
}