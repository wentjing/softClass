package ch26flyweightpattern.firstcode;

public class WebSite {
    private String name = "";

    public WebSite(String name) {
        super();
        this.name = name;
    }

    public void use() {
        System.out.println("网站分类" + name);
    }
}
