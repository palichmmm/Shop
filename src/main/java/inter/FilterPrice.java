package inter;

public interface FilterPrice {
    String MENU = "Фильтр по цене";
    String METHOD = "getFilterPrice";
    void getFilterPrice(double price);
}
