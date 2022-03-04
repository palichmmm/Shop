package inter;

public interface FilterManufacturer {
    String MENU = "Фильтр по производителю";
    String METHOD = "getFilterManufacturer";
    void getFilterManufacturer(Enum<Manufacturer> manufacturerEnum);
}
