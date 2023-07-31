package com.github.pielena.atm.service;

import com.github.pielena.atm.model.Banknote;
import com.github.pielena.atm.model.Cell;

import java.util.List;

public interface CellService {

    void putBanknotes(Cell cell, List<Banknote> banknotes);

    List<Banknote> getBanknotes(Cell cell, int amount);

    int getCurrentAmount(Cell cell);
}
