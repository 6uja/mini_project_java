package com.cafe.kiosk.service;

import com.cafe.kiosk.model.Menu;
import java.util.HashMap;
import java.util.Map;

public class InventoryService {
    // 각 메뉴(Menu 객체)별 남은 수량을 저장하는 맵
    private final Map<Menu, Integer> stockMap = new HashMap<>();

    public InventoryService() {
        // 모든 메뉴에 대해 초기 재고를 개별적으로 설정합니다.
        for (Menu m : Menu.values()) {
            String category = m.getCategory();

            switch (category) {
                case "COFFEE" -> stockMap.put(m, 999);
                case "DRINK" -> stockMap.put(m, 20);
                case "DESSERT", "FOOD" -> stockMap.put(m, 10);
            }
        }
    }

    /**
     * 해당 메뉴의 개별 재고가 남아있는지 확인
     */
    public boolean isAvailable(Menu menu) {
        // 커피 카테고리는 항상 주문 가능(true)
        if (menu.getCategory().equals("COFFEE")) {
            return true;
        }
        return stockMap.getOrDefault(menu, 0) > 0;
    }

    /**
     * 결제 시 실제 재고 차감 (개별 메뉴 단위)
     */
    public synchronized void useStock(Menu menu, int count) throws Exception {
        // 커피 카테고리는 재고를 차감하지 않고 즉시 종료
        if (menu.getCategory().equals("COFFEE")) {
            return;
        }

        int currentStock = stockMap.getOrDefault(menu, 0);

        if (currentStock >= count) {
            stockMap.put(menu, currentStock - count);
        } else {
            // 카테고리 명칭을 가져와 예외 메시지에 활용
            String categoryName = switch (menu.getCategory()) {
                case "DRINK" -> "음료";
                case "DESSERT" -> "디저트";
                case "FOOD" -> "푸드";
                default -> "상품";
            };
            throw new Exception(menu.getName() + "의 재고가 부족합니다! (현재 " + categoryName + " 재고: " + currentStock + ")");
        }
    }
}