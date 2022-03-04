import com.google.gson.*;
import inter.*;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Shop implements ShowProducts, Exit, FilterPrice, FilterManufacturer,
        Basket, TrackingOrder, ReturnOrder
{
    // Все параметры вынесены в поля класса
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy  HH:mm:ss");
    Scanner scanner = new Scanner(System.in);
    private static int countOrder = 1;
    private final String file = "src/main/resources/productsBase.txt";
    private final String separator = " ######################### ";
    private static Shop shop;
    private List<Product> products = new ArrayList<>();
    private List<Product> basket = new ArrayList<>();
    private final HashMap<Integer, HashMap<Date, List<Product>>> order = new HashMap<>();
    private final String nameShop;
    private List<Menu> menu = new ArrayList<>();

    private Shop(String nameShop) {
// Класс JsonDataProvider передает список всех продуктов из файла
        products.addAll(new JsonDataProvider(file).pars());
        this.nameShop = nameShop;
        System.out.println("Добро пожаловать в магазин " + nameShop + "!");
// Каждый раздел Меню имеет один интерфейс с одним методом и константами названия меню и метода исполнения
// Все интерфейсы находятся в пакете inter
// Третий параметр в конструкторе передает информацию о количестве всех продуктов раздела меню
        menu.add(new Menu(ShowProducts.MENU, ShowProducts.METHOD, String.valueOf(products.size())));
        menu.add(new Menu(FilterPrice.MENU, FilterPrice.METHOD));
        menu.add(new Menu(FilterManufacturer.MENU, FilterManufacturer.METHOD));
// Третий параметр в конструкторе передает информацию о количестве элементов в корзине
        menu.add(new Menu(Basket.MENU, Basket.METHOD, String.valueOf(basket.size())));
        menu.add(new Menu(TrackingOrder.MENU, TrackingOrder.METHOD));
        menu.add(new Menu(ReturnOrder.MENU, ReturnOrder.METHOD));
        menu.add(new Menu(Exit.MENU, Exit.METHOD));
// Меню сортируем по id т.е. по номеру Меню
        Collections.sort(menu);
    }
// Метод порождающего шаблона Singleton
    public static Shop getInstance(String nameShop) throws Exception {
        if (shop == null) shop = new Shop(nameShop);
        return shop;
    }
// Метод вывода нумерованного меню в консоль
    public void showMenu() {
        System.out.println("\nМеню магазина " + nameShop + ":");
        for (Menu inter : menu) {
            if (inter.getMethod().equals(Basket.METHOD)) {
                inter.setParam(String.valueOf(basket.size()));
                System.out.println(inter + inter.getParam());
            } else if (inter.getMethod().equals(ShowProducts.METHOD)) {
                inter.setParam(String.valueOf(products.size()));
                System.out.println(inter + inter.getParam());
            } else {
                System.out.println(inter);
            }
        }
    }
// Метод обработки ввода пользователя
    public void action(String action) {
        try {
            for (Menu m : menu) {
                if (m.getId() == Integer.parseInt(action)) {
                    action = m.getMethod();
                    break;
                }
            }
        } catch (Exception err) {
            System.err.println(action + " - Вы ввели не число!!!\n");
            return;
        }
        switch (action) {
            case ShowProducts.METHOD: {
                getAllProduct();
                break;
            }
            case FilterPrice.METHOD: {
                double price;
                while (true) {
                    try {
                        price = Double.parseDouble(inputClient("Введите верхний предел цены: "));
                        break;
                    } catch (Exception err) {
                        System.out.println("Не верный формат цены!!! Попробуйте еще раз.");
                    }
                }
                getFilterPrice(price);
                break;
            }
            case FilterManufacturer.METHOD: {
                Enum<Manufacturer> manufacturerEnum;
                while (true) {
                    int i = 1;
                    System.out.println("\nСписок производителей:");
                    for (Manufacturer manufacturer : Manufacturer.values()) {
                        System.out.println(i++ + ". " + manufacturer);
                    }
                    try {
                        i = Integer.parseInt(inputClient("Введите число производителя: "));
                        manufacturerEnum = Manufacturer.values()[i-1];
                        break;
                    } catch (Exception err) {
                        System.err.println("Такого производителя нет!!! Попробуйте еще раз.\n");
                    }
                }
                getFilterManufacturer(manufacturerEnum);
                break;
            }
            case Basket.METHOD: {
                basket();
                break;
            }
            case TrackingOrder.METHOD: {
                int track = 0;
                trackingOrder(track);
                break;
            }
            case ReturnOrder.METHOD: {
                int order = 0;
                returnOrder(order);
                break;
            }
            case Exit.METHOD: {
                exit("\nМы были рады вам! Заходите к нам еще!");
                break;
            }
            default: {
                System.err.println("Меню с номером " + action + " не существует!!! Попробуйте еще раз.\n");
            }
        }
    }
// Реализация интерфейса (блока Меню)
    public void getAllProduct() {
        System.out.println(separator + ShowProducts.MENU + " (" + products.size() + ")" + separator);
        for (Product product : products) {
            System.out.println(product);
        }
        while (true) {
            if (clienSolution()) {
                break;
            }
        }
    }
// Реализация интерфейса (блока Меню)
    public void getFilterPrice(double price) {
        System.out.println(separator + FilterPrice.MENU + " (" + price + ")" + separator);
        List<Product> list = products.stream()
                .filter(prices -> prices.getPrice() <= price)
                .sorted(Comparator.comparing(Product::getPrice))
                .collect(Collectors.toList());
        list.forEach(System.out::println);
        while (true) {
            if (clienSolution()) {
                break;
            }
        }
    }
// Реализация интерфейса (блока Меню)
    public void getFilterManufacturer(Enum<Manufacturer> manufacturerEnum) {
        System.out.println(separator + FilterManufacturer.MENU + " (" + manufacturerEnum + ")" + separator);
        List<Product> list = products.stream()
                .filter(manufact -> manufact.getManufacturer() == manufacturerEnum)
                .sorted(Comparator.comparing(Product::getPrice))
                .collect(Collectors.toList());
        list.forEach(System.out::println);
        while (true) {
            if (clienSolution()) {
                break;
            }
        }
    }
// Реализация интерфейса (блока Меню)
    public void basket() {
        System.out.println(separator + Basket.MENU + " (" + basket.size() + ")" + separator);
        int totalPrice = 0;
        for (Product product : basket) {
            System.out.println(product);
            totalPrice += product.getPrice();
        }
        System.out.println("Итого к оплате: " + totalPrice);
        while (true) {
            int numSolution;
            try {
                numSolution = Integer.parseInt(inputClient("1 - Купить, 2 - Очистить корзину (0 - Выход): "));
                if (numSolution == 0) {
                    break;
                } else if (numSolution == 2) {
                    System.err.println("Ваша корзина очищена!\n");
                    basket.clear();
                    break;
                } else if (numSolution == 1) {
                    System.err.println("Поздравляем с покупкой! Ваш номер заказа - " + countOrder + "\n");
                    HashMap<Date, List<Product>> dateListHashMap = new HashMap<>();
                    dateListHashMap.put(new Date(), List.copyOf(basket));
                    order.put(countOrder++, dateListHashMap);
                    basket.clear();
                    break;
                }
            } catch (Exception err) {
                System.out.println("Вы ввели не число!!! Попробуйте еще раз.");
            }
        }
    }
// Реализация интерфейса (блока Меню)
    public void trackingOrder(int track) {
        System.out.println(separator + TrackingOrder.MENU + " (" + order.size() + ")" + separator);
        if (order.size() > 0) {
            for (Map.Entry<Integer, HashMap<Date, List<Product>>> hashMapEntry : order.entrySet()) {
                System.out.print("Заказ №" + hashMapEntry.getKey() + " создан - ");
                for (Map.Entry<Date, List<Product>> entry : hashMapEntry.getValue().entrySet()) {
                    System.out.println(dateFormat.format(entry.getKey()));
                    for (Product prodOrder : entry.getValue()) {
                        System.out.println("\t" + prodOrder);
                    }
                }
            }
        } else {
            System.out.println("Пока нечего отслеживать!!!");
        }
    }
// Реализация интерфейса (блока Меню)
    public void returnOrder(int order) {
        System.out.println(separator + ReturnOrder.MENU + " (" + order + ")" + separator);
        System.err.println("Извините, раздел в разработке!!!");
    }
// Реализация интерфейса (блока Меню)
    public void exit(String message) {
        System.out.println(separator + Exit.MENU + separator);
        System.out.println(message);
        System.exit(0);
    }
// Часто повторяющийся код вынесен в отдельный метод
    private String inputClient(String text) {
        System.out.print(text);
        return scanner.nextLine();
    }
// Часто повторяющийся код вынесен в отдельный метод
    private boolean clienSolution() {
        int numProduct;
        try {
            numProduct = Integer.parseInt(inputClient("Введите номер товара чтобы положить в корзину (0 - Выход): "));
            if (numProduct == 0) {
                return true;
            } else {
                for (Product product : products) {
                    if (product.getId() == numProduct) {
                        basket.add(product);
                    }
                }
            }
        } catch (Exception err) {
            System.out.println("Вы ввели не число!!! Попробуйте еще раз.");
        }
        return false;
    }
}
