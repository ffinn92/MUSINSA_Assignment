package com.example.musinsa_assignment.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64)
    private String name;

    public Category(String name, Category parentCategory) {
        this.name = name;
        this.parentCategory = parentCategory;
    }

    public Category(String name) {
        this.name = name;
    }

    protected Category() {
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory", cascade = {CascadeType.PERSIST})
    private List<Category> childCategories = new ArrayList<>();

    public void editName(String name) {
        this.name = name;
    }
}
