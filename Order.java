package com.cafe.kiosk.model;

public class Order {
    private final String menuName;
    private final double totalPrice;
    private Menu menuObject; // ✨ 재고 차감을 위해 추가
    private int count;       // ✨ 주문 수량 저장을 위해 추가

    // 기존 생성자 (혹시 다른 곳에서 쓰고 있을 수 있으니 유지)
    public Order(String menuName, double totalPrice) {
        this.menuName = menuName;
        this.totalPrice = totalPrice;
    }

    // ✨ 새로운 생성자 (Main에서 호출하는 형식: 인자 4개)
    public Order(String menuName, int totalPrice, Menu menuObject, int count) {
        this.menuName = menuName;
        this.totalPrice = totalPrice;
        this.menuObject = menuObject;
        this.count = count;
    }

    // Getter 메서드들
    public String getMenuName() { return menuName; }
    public double getTotalPrice() { return totalPrice; }
    public Menu getMenuObject() { return menuObject; } // 추가
    public int getCount() { return count; }             // 추가
}