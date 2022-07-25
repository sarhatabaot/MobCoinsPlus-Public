package com.chup.mobcoinsplus.mobcoinsplusapi;

import java.util.UUID;

public interface MobCoinsPlusApi {

    String getCurrencyId();

    String getCurrencySymbol();

    int getBalance(final UUID uuid);

    void setBalance(final UUID uuid, final int amount);
}
