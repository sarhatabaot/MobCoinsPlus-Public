package com.chup.mobcoinsplus.api;

import com.chup.mobcoinsplus.mobcoinsplusapi.MobCoinsPlusApi;

import java.util.UUID;

/**
 * @author sarhatabaot
 */
public class MobCoinsPlusApiImpl implements MobCoinsPlusApi {
    @Override
    public String getCurrencyId() {
        return null;
    }

    @Override
    public String getCurrencySymbol() {
        return null;
    }

    @Override
    public int getBalance(final UUID uuid) {
        return 0;
    }

    @Override
    public void setBalance(final UUID uuid, final int amount) {

    }
}
