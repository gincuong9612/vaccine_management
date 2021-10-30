package src.controllers;

public interface Controller<T> {
    public T[] getExampleData();
    public void loadData();
    public void initData();
    public T get(String id);
    public int size();
}
