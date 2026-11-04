package com.kongling.iap.service;

import com.kongling.iap.domain.ProductDefinition;
import com.kongling.iap.domain.ProductType;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class ProductCatalog {
    private final Map<String, ProductDefinition> products;

    public ProductCatalog() {
        Map<String, ProductDefinition> values = new LinkedHashMap<>();
        values.put("com.konglingapp.coin01", new ProductDefinition("com.konglingapp.coin01", ProductType.COIN, 7, "7柚子"));
        values.put("com.konglingapp.coin06", new ProductDefinition("com.konglingapp.coin06", ProductType.COIN, 42, "42柚子"));
        values.put("com.konglingapp.coin30", new ProductDefinition("com.konglingapp.coin30", ProductType.COIN, 210, "210柚子"));
        values.put("com.konglingapp.coin100b", new ProductDefinition("com.konglingapp.coin100b", ProductType.COIN, 700, "700柚子"));
        values.put("com.konglingapp.coin500", new ProductDefinition("com.konglingapp.coin500", ProductType.COIN, 3500, "3500柚子"));
        values.put("com.konglingapp.coin1000", new ProductDefinition("com.konglingapp.coin1000", ProductType.COIN, 7000, "7000柚子"));
        values.put("0001", new ProductDefinition("0001", ProductType.MEMBERSHIP, 1, "月会员"));
        values.put("0002", new ProductDefinition("0002", ProductType.MEMBERSHIP, 3, "季会员"));
        values.put("0003", new ProductDefinition("0003", ProductType.MEMBERSHIP, 12, "年会员"));
        this.products = Collections.unmodifiableMap(values);
    }

    public Optional<ProductDefinition> find(String productId) {
        return Optional.ofNullable(products.get(productId));
    }

    public Map<String, ProductDefinition> all() {
        return products;
    }
}
