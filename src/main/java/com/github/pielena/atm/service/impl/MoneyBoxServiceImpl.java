package com.github.pielena.atm.service.impl;

import com.github.pielena.atm.exception.CellException;
import com.github.pielena.atm.model.Banknote;
import com.github.pielena.atm.model.Cell;
import com.github.pielena.atm.model.MoneyBox;
import com.github.pielena.atm.exception.MoneyBoxException;
import com.github.pielena.atm.service.CellService;
import com.github.pielena.atm.service.MoneyBoxService;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class MoneyBoxServiceImpl implements MoneyBoxService {

    private CellService cellService;

    @Override
    public List<Banknote> getMoney(MoneyBox moneyBox, int sum) {

        moneyBox.getCells().sort(Comparator.comparingInt(el -> -el.getBanknoteValue().getValue()));
        List<Banknote> result = new ArrayList<>();

        validateSum(moneyBox, sum);

        for (Cell cell : moneyBox.getCells()) {
            if (sum >= cell.getBanknoteValue().getValue()) {
                int requiredAmount = sum / cell.getBanknoteValue().getValue();
                if (requiredAmount > cellService.getCurrentAmount(cell)) {
                    requiredAmount = cellService.getCurrentAmount(cell);
                }
                sum -= requiredAmount * cell.getBanknoteValue().getValue();
                result.addAll(cellService.getBanknotes(cell, requiredAmount));
            }
        }

        if (sum != 0) {
            putMoney(moneyBox, result);
            throw new MoneyBoxException("Not enough banknotes for this sum");
        }

        return result;
    }

    @Override
    public void putMoney(MoneyBox moneyBox, List<Banknote> banknotes) {
        for (Cell cell : moneyBox.getCells()) {
            List<Banknote> selectedBanknotes = banknotes.stream()
                    .filter(banknote -> banknote.banknoteValue().equals(cell.getBanknoteValue()))
                    .collect(Collectors.toList());
            try {
                cellService.putBanknotes(cell, selectedBanknotes);
            }
            catch (CellException e) {
                throw new MoneyBoxException("Too many banknotes");
            }
        }
    }

    @Override
    public int getBalance(MoneyBox moneyBox) {
        int sum = 0;
        for (Cell cell : moneyBox.getCells()) {
            sum += cell.getBanknoteValue().getValue() * cellService.getCurrentAmount(cell);
        }
        return sum;
    }

    private void validateSum(MoneyBox moneyBox, int sum) {
        if (sum > getBalance(moneyBox)) {
            throw new MoneyBoxException("Not enough money in ATM");
        }
        if (sum < 0 || sum % moneyBox.getCells().get(moneyBox.getCells().size() - 1).getBanknoteValue().getValue() != 0) {
            throw new MoneyBoxException("Invalid sum");
        }
    }
}
