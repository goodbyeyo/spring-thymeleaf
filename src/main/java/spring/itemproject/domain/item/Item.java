package spring.itemproject.domain.item;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Item {

    private Long id;
    private String itemName;
    private Integer price;  // 가격이 0인경우는 없으므로  null 값이 들어갈수 있도록 Integer
    private Integer quantity;
    
    private Boolean open;   // 판매여부
    private List<String> regions;   // 등록지역
    private ItemType itemType;  // 상품 종류
    private String deliveryCode;    // 배송 방식 

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
