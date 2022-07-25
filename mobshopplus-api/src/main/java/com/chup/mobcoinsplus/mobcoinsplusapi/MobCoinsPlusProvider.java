package com.chup.mobcoinsplus.mobcoinsplusapi;


import org.jetbrains.annotations.NotNull;

public final class MobCoinsPlusProvider {
    private static MobCoinsPlusApi instance = null;

    public static @NotNull MobCoinsPlusApi get() {
        MobCoinsPlusApi instance = MobCoinsPlusProvider.instance;
        if (instance == null) {
            throw new NotLoadedException();
        }
        return instance;
    }

    static void register(MobCoinsPlusApi instance) {
        MobCoinsPlusProvider.instance = instance;
    }

    static void unregister() {
        MobCoinsPlusProvider.instance = null;
    }


    private static final class NotLoadedException extends IllegalStateException {
        private static final String MESSAGE = "The MobCoinsPlus API isn't loaded yet!";

        NotLoadedException() {
            super(MESSAGE);
        }
    }
}
