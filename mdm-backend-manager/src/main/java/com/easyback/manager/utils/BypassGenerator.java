package com.easyback.manager.utils;

import com.easyback.model.dto.dep.BypassCodeDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Bypass Code生成工具
 * @Author: zhuangqingdian
 * @Date:2023/7/31
 */
public class BypassGenerator {

   private static final Map<String,String> bypassMap = new HashMap<>();

   static{
      bypassMap.put("C07CV-HL6WZ-HWL5-366K-V9ZJ-WXW1","3e90ac37fe9426ed020fb5e2965d311206e9218b5b61ee334f0e650f326df261");
      bypassMap.put("72RNC-97ZVK-UU8T-7NXK-D0ZZ-61F3","0e53e4b64f455f23c11c9bd42becb3c76196b28ed347d209f38eb99d40437b00");
      bypassMap.put("NKG55-Z5HEK-305C-FVH9-4J9G-MZA1","be343e8217e05dcf5d96a812cff28926b16ff0c795fd70265f39b4de7f6828f8");
      bypassMap.put("2LWDF-F76CH-TPPQ-5N6H-L006-QWM1","af99343fc1ff2a28aa44a3572d20274c1f5860bd64ba3e0561bad5e1a8072c8b");
      bypassMap.put("WCYP5-CL88R-5JUE-ZZ89-2574-8GV5","47b035ae34eaea6720fec6a6bd25c4abce33edcdabf31b56c644ead29d015921");
      bypassMap.put("123U4-F6QT0-YDEJ-U5KK-R7LA-N1G4","386c74f55c0e848cde9fe118c53f10f97ee39f587ba58776210e35d8857914ea");
      bypassMap.put("CH6AM-XFA85-YFUM-ZQA2-838G-PE74","6be54d056658222117d3597e590661dd6abad93f26badae15c2677f07360d1fe");
      bypassMap.put("Z43QT-UR211-DDTF-KEU8-R2KJ-7AP1","5e81014f2351d419349ad7d2c159f0c48f7a584a45d914735bd06f24d11df9c8");
      bypassMap.put("33M5W-K7455-TDTT-YCA9-451N-NAN6","d7f0191e98f11a0b6fff791dfdc98236e7653d33c63be722bc59fadfc2cb3d17");
      bypassMap.put("4083P-7X7YG-2L1A-TXE1-DZQG-J894","e35cacda1290dc1f3a8e30664c58a3438a41d01dec3bf94c8ff10fe15024e886");
      bypassMap.put("F8WAG-WZ3TM-4QAK-ZPAM-PN34-2D64","51c62d3e32917f9785e0e8e092f90b08a8abd06b504f793e46454af201fcc3e2");
      bypassMap.put("HWX6C-XNGYZ-P4HQ-QKJP-60F1-WGG6","f5af88362d212bb789e16ec3b70e869ce5ef104ba0b5127265bcb374f1f19c8d");
      bypassMap.put("J02GH-DZ2ZR-ZA3N-RND2-52QW-6801","869294deeebe583f0751bd54e652cbdf8770c9b93656fd6e57ff3f7a2557afeb");
      bypassMap.put("81V69-TJJUW-ZD3Z-G34W-93YG-ZD37","095d7d19703302a3c5fbf862fc5574c90a3bf3f07a2ea2bb2193e41d56683d34");
      bypassMap.put("XZ9Y3-Z3DXX-UJPK-K75L-GEQH-H543","941675bd4cf031ada203262421d9ed43c5037a6a460f2fe2b9f2a8a7abb7a609");
      bypassMap.put("T40J4-LFHUN-XP8K-642Y-Q3FL-U7V5","14d2efa4bc01b59d5f87d14e30ba5b8dc6b536ff5bcd23c49cd0ba24ae3d298e");
      bypassMap.put("99JC7-15NTU-C87D-GAJ9-FDGX-LAC7","a3d605c3aaf16925747706575a580847b5837b64b405e1f6d1b9add3ec56ec68");
      bypassMap.put("5XXW5-UU5ZT-H7R5-6348-LM8M-R6H5","74e7f7715abd015c44210286f992cea218b77914a7c2ce3e80bd2be128b5319d");
      bypassMap.put("9ZDQR-8LTAW-TKZV-HM6F-9VF1-ZCK0","f2a481d9afb82418dd5909d0695da5af7f099e22999aca18595b3f19a43564e6");
      bypassMap.put("LHKUH-D9A1E-Z766-W62R-KUQZ-3KT6","f728c4dc9d9971036b5ba2e32f40fafba0ce9b0a28f00551ed894d781063a5fa");
      bypassMap.put("AWEEX-KPKU2-6KJD-TLQK-U09H-5JY3","13bdb43697f5a8456efbeada198375832e799c9cae236ec6e70914d9cf630ac7");
      bypassMap.put("2J1W9-LH23X-5X29-CW7U-81XV-2RL2","c1fad3902ecc718bd7788bd0f13d242d941854d2559e8c7fa659c6a56c5ad0a4");
      bypassMap.put("XLTGW-G6JUA-A2Q0-RRM2-0EQ7-ZAF5","c21c9bcbdc23e6a80eae3dc68f8fb813642fa9af5756477c07b53bbf56260fce");
      bypassMap.put("GGPD5-32D5Y-YJ8C-9UZZ-E3T6-X0C2","fe2e91a564c0ae097550fdef234ee8cc67cff54ab098c47e00ad750b3bb459f3");
      bypassMap.put("L51TM-GQXQ7-3VJD-0PXX-L6DQ-7DK3","ccaa38a3cb30aa62dc95b942b78996c5ea78cfe8355047a9fca63766f8a0cd83");
      bypassMap.put("RA5L1-03HED-3EAN-DY52-XPHH-UMJ3","e99c80614908375f3b75bd88354f38757aef7c3165f424c947f063facaa96216");
      bypassMap.put("2KX8M-E1C1V-CMTE-K4W5-VHLX-47K7","9c1155f37addb245bf77348bd7869ef5dbfad4752d0a57471c80427736add710");
      bypassMap.put("0KWGU-NR7JM-YX5H-84PP-DH98-WRX5","c33c29b489b34df9b9e9f0a385b977efe6ba8af1c1ff6729ae1283e3181d0e39");
      bypassMap.put("NW4MD-DDEV4-XWF5-590T-VLR9-4WC1","ba722395484e3ff974de3ad4de25eba55f49e6adbff78a35c66ab547c478e503");
      bypassMap.put("VE140-M3C0G-ELKF-EE46-QVH9-7C44","8a7531186c9dce1bea68cb0fdffbd394e516ec9292ffbbb57cb904a61ac1034d");
      bypassMap.put("0EKP2-VWX5H-F77C-L3H0-GHPC-5DQ5","e319f62b1bd6d25e66b8eb0afbf6281d26da55843159e6efe10f54fa5aea51ac");
      bypassMap.put("TKQM9-0RFVL-L78T-NMN4-VDY4-VD35","906810927775f41f90105678654943d3097e3826fe26af33c328587a1e9d7b69");
      bypassMap.put("1AACH-F6F8T-FPHK-19JX-50N5-FZ52","4a5518707d613b2fe3caef2faf59bb476e4ce0f22ce70806dcc948e382100afc");
      bypassMap.put("NGPX8-EGHNQ-CRTR-JE7V-A7GW-NDM6","2be3f270577d689244793aa415b675df8a847e5575962daa555ea21bcef53ec9");
      bypassMap.put("KYXY7-3QJJQ-JEVH-7LQW-XGX5-32A4","a5add70cfb02c66ca7ebeb4b3d2ea597be5f02a378793b73f1dc06a5245a827f");
      bypassMap.put("MVXG2-96NPM-7VY9-V07N-4K52-PQU0","a749cfe6b2f9d4701b6baf4863eabd0b25f9e4f9119b9a03d9a230ae790bd2f3");
      bypassMap.put("MXMAD-YD7KU-KE1G-4XPG-WHEX-25Z6","0d8da0253d9b26077f0d37edfe8bcde7a12bc88c02741e6d389bb042545f3f67");
      bypassMap.put("QMM1W-JCH21-8KLR-NZWC-MNW5-HY50","23d369299a54ababd398a306d4ed4216556d0f0ff845006fab43f1f00e02de46");
      bypassMap.put("VX04W-UMN9T-XNDQ-KCRY-WH5V-2HC4","d16f88773defaaa5dcf63f30d398551e7688ab04f25ace408992ae4d0bf16e06");
      bypassMap.put("AVMN6-LCY8L-FD7C-89GU-5UUG-RLW7","1ad540d40fc9b386cfe6dcf3f77fc90b680b678f74e1662a6d343db07ed85a5e");
      bypassMap.put("J7HVE-DR3WT-WAUG-1HN6-WKA6-4V44","20655e6badf91b0c298280fec858c8a044cb8ac8d11c6e401c0a53bd4abd6903");
      bypassMap.put("MMD5U-688MN-6DU2-H49X-3L26-V4R0","fcfdd1c1ff134602f38da7002f263856306a0ca5ec934e50bffb052b58589c48");
      bypassMap.put("AMQK4-4FG16-KNDW-MU9H-ANZQ-A942","585ede1e9d58466c6a6486b96bfe637b10cc056a44b24ce37bcb94ac6b328a8c");
      bypassMap.put("L6CZZ-RVHC9-AG63-JYKN-4KMP-0XG7","ff35f4db041d565a5b48a9fb8640a65026e34f101ad5878e90fd147f55fb8c2f");
      bypassMap.put("PVYP7-4DTXJ-K5MF-4C55-J4LJ-0855","a2dab3da7dbe5b26b28f7770a1ac1fc6348ced26cc4e8b9144f1acda1538891b");
      bypassMap.put("E8AK1-W07KC-039J-0RF5-NVKY-44X7","71ad7a5f59d805ac5a5111c34990229885353727be2fbc9a3f121e9f3192cc5c");
      bypassMap.put("6D1TZ-QXLGD-NFFW-CPP5-6R5L-E461","97d160f84ba7dea014a3dcbd578adcaf28cd9729ed0d336c2f19976e5afe7744");
      bypassMap.put("KHNP9-669K5-305M-Y3V9-Q0QK-32Z6","8fd091c2027a7604e6658718bad98791d3e0de888ac46833eff1f5915c82f8f4");
      bypassMap.put("U0YUN-Z0TLE-6VA6-9GWC-TQU4-XP21","ab1808acdb5314134e00850da24766c6cb75a9a9760ba0bf33df36bb1da23b45");
      bypassMap.put("TYV6K-FQ2Y5-GFFW-2HDV-ZF4M-1NN6","581be18a879606e44c6efb89c724ace3e1ec4ac874da99cc5d68a276cae255b8");
      bypassMap.put("W7DE8-X8M8H-UYV3-EA90-73QE-6NE3","116de4a9f78fbbfd510520610dd317832953237fb8b15885d4af715e21201b29");

   }

   public static BypassCodeDTO getByRandom(){
      String[] keys = bypassMap.keySet().toArray(new String[0]);
      Random random = new Random();
      String key = keys[random.nextInt(keys.length)];
      String value = bypassMap.get(key);
      BypassCodeDTO dto = new BypassCodeDTO();
      dto.setByPass(key);
      dto.setHash(value);
      return dto;
   }

}
