package com.github.pielena.atm.model;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class Cell {
    private static final int DEFAULT_CAPACITY = 2000;

    private final BanknoteValue banknoteValue;
    private final int capacity;
    private final List<Banknote> banknoteList;

    public Cell(BanknoteValue banknoteValue) {
        this.banknoteValue = banknoteValue;
        this.capacity = DEFAULT_CAPACITY;
        this.banknoteList = new ArrayList<>(DEFAULT_CAPACITY);
    }
}
