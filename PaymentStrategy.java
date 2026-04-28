package com.cafe.kiosk.pattern;

@FunctionalInterface
public interface PaymentStrategy {
    // 금액(amount)을 받아 결제를 처리하는 추상 메서드 (인터페이스이므로 구현체 없음)
    void pay(int amount);
}