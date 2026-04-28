package com.cafe.kiosk.model;

public enum Menu {
    // 카피
    ESPRESSO("에스프레소", 3200, "COFFEE", "ONLY_HOT"),
    AMERICANO("아메리카노", 3500, "COFFEE", "ALL"),
    LATTE("카페라떼", 4200, "COFFEE", "ALL"),
    VANILLA_LATTE("바닐라라떼", 4500, "COFFEE", "ALL"),
    CAPPUCCINO("카푸치노", 4200, "COFFEE", "ONLY_HOT"),

    // 음료
    TEA("녹차티", 4000, "DRINK", "ALL"),
    STRAWBERRY_SMOOTHINE("딸기 스무디", 5000, "DRINK", "NONE"),
    MANGO_SMOOTHINE("망고 스무디", 5000, "DRINK", "NONE"),
    GRAPEFRUIT_ADE("자몽에이드", 5500, "DRINK", "NONE"),
    LEMON_ADE("레몽에이드", 5500, "DRINK", "NONE"),

    // 디저트
    CHEESE_CAKE("치즈케이크", 6000, "DESSERT", "NONE"),
    CHOCO_CAKE("초코케이크", 6000, "DESSERT", "NONE"),
    TIRAMISU("티라미수", 6000, "DESSERT", "NONE"),
    COOKIE("쿠키", 2000, "DESSERT", "NONE"),
    BROWNIE("브라우니", 3000, "DESSERT", "NONE"),

    // 푸드
    HAM_CHEESE_SANDWICH("햄치즈 샌드위치", 6500, "FOOD", "NONE"),
    CLUB_SANDWICH("클럽 샌드위치", 7500, "FOOD", "NONE"),
    CROISSANT("크루아상", 3000, "FOOD", "NONE"),
    BAGEL("베이글", 3000, "FOOD", "NONE"),
    SALAD("샐러드", 7000, "FOOD", "NONE");

    private final String name;
    private final int price;
    private final String category;
    private final String optionType;

    Menu(String name, int price, String category, String optionType) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.optionType = optionType;
    }

    public String getName() {
        return name;
    }
    public int getPrice() {
        return price;
    }
    public String getCategory() {
        return category;
    }
    public String getOptionType() {
        return optionType;
    }
}
