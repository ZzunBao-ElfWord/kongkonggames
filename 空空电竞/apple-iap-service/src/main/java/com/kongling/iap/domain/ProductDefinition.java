package com.kongling.iap.domain;

public class ProductDefinition {
    private final String productId;
    private final ProductType type;
    private final int grantValue;
    private final String displayName;

    public ProductDefinition(String productId, ProductType type, int grantValue, String displayName) {
        this.productId = productId;
        this.type = type;
        this.grantValue = grantValue;
        this.displayName = displayName;
    }

    public String getProductId() { return productId; }
    public ProductType getType() { return type; }
    public int getGrantValue() { return grantValue; }
    public String getDisplayName() { return displayName; }
}
