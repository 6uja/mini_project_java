package com.cafe.kiosk.repository;
import java.util.List;

public interface Repository<T> {
    void save(T data);
    List<T> findAll();
}
