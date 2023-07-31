package com.github.pielena.atm.model;

import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
public class MoneyBox {

    private final List<Cell> cells;

    public MoneyBox() {
        this.cells = Arrays.stream(BanknoteValue.values())
                .map(Cell::new)
                .collect(Collectors.toList());
    }
}
