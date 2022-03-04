import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String input;
// Класс Shop создает только один объект так как используется шаблон Singleton
        Shop shop = Shop.getInstance("Лента");

        while (true) {
            shop.showMenu();
            System.out.print("Введите номер меню: ");
            input = scanner.nextLine();
            shop.action(input);
        }
    }
}
