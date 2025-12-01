package org.hcm.lifpay.user.controller;

import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.wallet.DeterministicSeed;

import java.security.SecureRandom;
import java.util.List;

public class MnemonicExample {



    /**
     * 生成助记词
     * */
    public static void main(String[] args) throws Exception {
        SecureRandom random = new SecureRandom();

        // 128 bits = 12词；256 bits = 24词
        byte[] entropy = new byte[16]; // 128bit → 12词
        random.nextBytes(entropy);

        // 生成助记词
        List<String> mnemonic = MnemonicCode.INSTANCE.toMnemonic(entropy);

        System.out.println("助记词: " + String.join(" ", mnemonic));

        // 也可以生成 Seed：
        DeterministicSeed seed = new DeterministicSeed(mnemonic, null, "", System.currentTimeMillis());
        System.out.println("Seed: " + seed.getSeedBytes());
    }
}
