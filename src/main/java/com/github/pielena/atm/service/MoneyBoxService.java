package com.github.pielena.atm.service;

import com.github.pielena.atm.model.Banknote;
import com.github.pielena.atm.model.MoneyBox;

import java.util.List;

public interface MoneyBoxService {

    List<Banknote> getMoney(MoneyBox moneyBox, int sum);

    void putMoney(MoneyBox moneyBox, List<Banknote> banknotes);

    int getBalance(MoneyBox moneyBox);
}
