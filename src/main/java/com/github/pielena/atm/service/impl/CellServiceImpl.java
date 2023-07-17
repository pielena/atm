package com.github.pielena.atm.service.impl;

import com.github.pielena.atm.exception.CellException;
import com.github.pielena.atm.model.Banknote;
import com.github.pielena.atm.model.Cell;
import com.github.pielena.atm.service.CellService;

import java.util.ArrayList;
import java.util.List;

public class CellServiceImpl implements CellService {

    @Override
    public void putBanknotes(Cell cell, List<Banknote> banknotes) {
        checkBanknotesValue(cell, banknotes);
        if (cell.getBanknoteList().size() + banknotes.size() > cell.getCapacity()) {
            throw new CellException("Not enough cell space");
        }
        cell.getBanknoteList().addAll(banknotes);
    }

    @Override
    public List<Banknote> getBanknotes(Cell cell, int amount) {
        if (cell.getBanknoteList().size() < amount) {
            throw new CellException("Not enough banknotes in cell");
        }

        List<Banknote> resultList = new ArrayList<>();
        for (int i = amount - 1; i >= 0; i--) {
            Banknote banknote = cell.getBanknoteList().remove(i);
            resultList.add(banknote);
        }

        return resultList;
    }

    @Override
    public int getCurrentAmount(Cell cell) {
        return cell.getBanknoteList().size();
    }

    private void checkBanknotesValue(Cell cell, List<Banknote> banknotes) throws CellException {
        boolean isValid = banknotes.stream()
                .allMatch(el -> cell.getBanknoteValue().equals(el.banknoteValue()));
        if (!isValid) {
            throw new CellException("Banknote value doesn't match cell value");
        }
    }
}
