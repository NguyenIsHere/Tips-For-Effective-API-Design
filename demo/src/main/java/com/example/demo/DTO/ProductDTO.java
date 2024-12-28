package com.example.demo.DTO;

import java.util.List;

import com.example.demo.model.Color;
import lombok.Data;

@Data
public class ProductDTO {
  private String name;
  private String description;
  private String category;
  private List<Color> colors;
}
