package com.cafe.kiosk;

import com.cafe.kiosk.model.Menu;
import com.cafe.kiosk.model.Order;
import com.cafe.kiosk.service.InventoryService;
import com.cafe.kiosk.service.OrderService;
import java.util.*;

public class Main {
    private static final Map<String, Integer> userPoints = new HashMap<>();
    private static final String ADMIN_PASSWORD = "9999";

    public static void main(String[] args) {
        InventoryService inventory = new InventoryService();
        OrderService orderService = OrderService.getInstance();
        Scanner sc = new Scanner(System.in);

        while (true) {
            try {
                clearConsole();
                System.out.println("----------------------------------------------------");
                System.out.println(" 1. 주문 시작");
                System.out.println(" 2. 관리자");
                System.out.println("----------------------------------------------------");
                System.out.print("▶ 선택: ");

                String startChoice = sc.next();
                if (startChoice.equals("2")) {
                    System.out.print("▶ 비밀번호 입력: ");
                    if (sc.next().equals(ADMIN_PASSWORD)) {
                        clearConsole();
                        printClosingReport(orderService);
                        return;
                    } else {
                        System.out.println("❌ 비밀번호 불일치.");
                        sleep(1500);
                    }
                } else if (startChoice.equals("1")) {
                    processCustomerOrder(sc, inventory, orderService);
                }
            } catch (Exception e) {
                sc.nextLine();
                sleep(1000);
            }
        }
    }

    private static void processCustomerOrder(Scanner sc, InventoryService inventory, OrderService orderService) {
        clearConsole();
        List<Order> currentCart = new ArrayList<>();
        int totalOrderAmount = 0;

        // [1] 메뉴 선택
        while (true) {
            printMenuBoard(inventory);
            System.out.print("▶ 메뉴 번호 선택: ");
            String input = sc.next();

            int menuChoice;
            try {
                menuChoice = Integer.parseInt(input);
                if (menuChoice < 1 || menuChoice > Menu.values().length) {
                    System.out.println("❌ 메뉴판에 없는 번호입니다.");
                    sleep(1000);
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ 숫자만 입력 가능합니다.");
                sleep(1000);
                continue;
            }

            Menu selectedMenu = Menu.values()[menuChoice - 1];
            if (!inventory.isAvailable(selectedMenu)) {
                System.out.println("❌ [" + selectedMenu.getName() + "] 상품은 현재 품절입니다.");
                sleep(1000);
                continue;
            }

            String finalOption = selectTemperature(sc, selectedMenu);
            System.out.print("   ▶ 수량 입력: ");
            int count = sc.nextInt();

            int itemPrice = selectedMenu.getPrice() * count;
            currentCart.add(new Order(finalOption + selectedMenu.getName(), (int)itemPrice, selectedMenu, count));
            totalOrderAmount += itemPrice;

            String nextStep;
            while (true) {
                System.out.print("▶ 추가로 주문하시겠습니까? (1.네 / 2.아니오 / 3.바로결제): ");
                nextStep = sc.next();
                if (nextStep.equals("1") || nextStep.equals("2") || nextStep.equals("3")) break;
                System.out.println("❌ 1, 2, 3 중에서 선택해주세요.");
            }

            if (nextStep.equals("2") || nextStep.equals("3")) {
                if (currentCart.isEmpty()) continue;
                break;
            }
        }

        // [2] 재고 선차감
        try {
            for (Order o : currentCart) {
                inventory.useStock(o.getMenuObject(), o.getCount());
            }
        } catch (Exception e) {
            System.out.println("\n❌ 주문 실패: " + e.getMessage());
            sleep(2500);
            return;
        }

        // [3] 포인트 적립 및 결제 수단 검증
        System.out.println("\n💰 총 결제 금액: ₩" + String.format("%,d", totalOrderAmount));
        while (true) {
            System.out.print("▶ 포인트 적립을 하시겠습니까? (1.네 / 2.아니오): ");
            String pointChoice = sc.next();
            if (pointChoice.equals("1")) {
                System.out.print("   휴대폰 번호 뒤 4자리: ");
                String phone = sc.next();
                int earned = (int)(totalOrderAmount * 0.01);
                int currentPoint = userPoints.getOrDefault(phone, 0);
                userPoints.put(phone, currentPoint + earned);
                System.out.println("   ✨ 적립 완료! (누적: " + (currentPoint + earned) + "P)");
                break;
            } else if (pointChoice.equals("2")) break;
            else System.out.println("❌ 잘못된 입력입니다.");
        }

        while (true) {
            System.out.print("▶ 결제 수단 (1.카드 2.현금 3.삼성페이): ");
            String payChoice = sc.next();
            if (payChoice.equals("1") || payChoice.equals("2") || payChoice.equals("3")) {
                System.out.println("💳 결제가 승인되었습니다. 제조를 시작합니다.");
                break;
            } else System.out.println("❌ 다시 선택해주세요.");
        }

        processCooking(orderService, currentCart);
    }

    private static void printMenuBoard(InventoryService inv) {
        System.out.println("\n==================== MENU BOARD ====================");
        for (int i = 0; i < Menu.values().length; i++) {
            Menu m = Menu.values()[i];
            boolean available = inv.isAvailable(m);

            String idx = String.format("%2d. ", i + 1);
            String cat = String.format("[%-7s] ", m.getCategory());
            String name = m.getName();
            String price = String.format("₩%,d", m.getPrice());

            int totalWidth = 46;
            int currentLen = idx.length() + cat.length() + getKoreanLength(name) + getKoreanLength(price);
            String padding = " ".repeat(Math.max(1, totalWidth - currentLen));

            String row = idx + cat + name + padding + price;

            if (!available) {
                System.out.println("\u0336" + row.replace("", "\u0336") + " [품절]");
            } else {
                System.out.println(row);
            }
        }
        System.out.println("====================================================");
    }

    private static void processCooking(OrderService os, List<Order> cart) {
        try {
            System.out.println();
            String msg = "=====  맛있게 준비하고 있습니다. 잠시만 기다려주세요  =====";
            for (char c : msg.toCharArray()) {
                System.out.print(c);
                System.out.flush();
                Thread.sleep(200);
            }
            System.out.println("\n----------------------------------------------------");
            for (Order item : cart) {
                Thread.sleep(2000);
                System.out.println("🔔 [완료] " + item.getMenuName() + " (x" + item.getCount() + ") 나왔습니다!");
                os.addOrder(item);
            }
            System.out.println("----------------------------------------------------");

            // ✨ 맛있게 드세요 메시지 강조 및 대기 시간 추가
            System.out.println("\n   😊 주문하신 상품이 모두 준비되었습니다.");
            System.out.println("      감사합니다! 맛있게 드세요.      \n");
            System.out.println("----------------------------------------------------");

            sleep(5000); 
        } catch (Exception ignored) {}
    }

    private static String selectTemperature(Scanner sc, Menu menu) {
        if (!menu.getOptionType().equals("ALL")) {
            return menu.getOptionType().equals("ONLY_ICE") ? "[ICE] " : "[HOT] ";
        }
        while (true) {
            System.out.print("   [옵션] 1.HOT / 2.ICE: ");
            String choice = sc.next();
            if (choice.equals("1")) return "[HOT] ";
            if (choice.equals("2")) return "[ICE] ";
        }
    }

    private static void printClosingReport(OrderService os) {
        System.out.println("====================================================");
        System.out.println("          [ 금일 마감 정산 ]          ");
        System.out.println("====================================================");
        System.out.printf("   ▶ 총 매출액      : ₩%,d%n", (int)os.getTotalSales());
        System.out.println("====================================================");
    }

    private static int getKoreanLength(String s) {
        int len = 0;
        for (char c : s.toCharArray()) {
            if (Character.getType(c) == Character.OTHER_LETTER || c == '₩') len += 2;
            else len += 1;
        }
        return len;
    }

    private static void clearConsole() { for (int i = 0; i < 50; i++) System.out.println(); }
    private static void sleep(int ms) { try { Thread.sleep(ms); } catch (Exception ignored) {} }
}