// Source code is decompiled from a .class file using FernFlower decompiler.
package com.example.demo.repository;

import com.example.demo.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.mongodb.repository.Query;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
  @Query("{ 'colors.sizes.price' : { $gte: ?0, $lte: ?1 } }")
  List<Product> findProductsBySizePriceBetween(double minPrice, double maxPrice);
}
