import java.util.Objects;

public class Menu implements Comparable<Menu>{
    private static int counter = 1;
    private final int id;
    private final String nameMenu;
    private final String method;
    private String param = null;

    public Menu(String nameMenu, String method) {
        id = counter++;
        this.nameMenu = nameMenu;
        this.method = method;
    }
    public Menu(String nameMenu, String method, String param) {
        id = counter++;
        this.param = param;
        this.nameMenu = nameMenu;
        this.method = method;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getParam() {
        return " (" + param + ")";
    }

    public int getId() {
        return id;
    }

    public String getNameMenu() {
        return nameMenu;
    }

    public String getMethod() {
        return method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return id == menu.id && Objects.equals(nameMenu, menu.nameMenu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nameMenu);
    }

    @Override
    public String toString() {
        return id + ". " + nameMenu;
    }

    @Override
    public int compareTo(Menu o) {
        if (this.id > o.id) {
            return 1;
        } else {
            return -1;
        }
    }
}
