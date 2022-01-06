package spring.itemproject.web.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.itemproject.domain.ItemRepository;
import spring.itemproject.domain.item.DeliveryCode;
import spring.itemproject.domain.item.Item;
import spring.itemproject.domain.item.ItemType;

import javax.annotation.PostConstruct;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor    // final이 붙은 객체의 생성자를 만들어준다, 생성자가 하나일때는 자동으로 생성자 만들어준다
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

    @InitBinder
    public void init(WebDataBinder dataBinder) {
        dataBinder.addValidators(itemValidator);
    }

    @ModelAttribute("regions")
    public Map<String, String> regions() {
        Map<String, String> regions = new LinkedHashMap<>();    // 순서보장
        regions.put("SEOUL", "서울");
        regions.put("BUSAN", "부산");
        regions.put("JEJU", "제주");
        return regions;
    }

    @ModelAttribute("itemTypes")
    public ItemType[] itemTypes() {
        return ItemType.values();   // enum의 모든 정보를 배열로 반환한다
    }

    @ModelAttribute("deliveryCodes")
    public List<DeliveryCode> deliveryCodes() {
        List<DeliveryCode> deliveryCodes = new ArrayList<>();
        deliveryCodes.add(new DeliveryCode("FAST", "빠른 배송"));
        deliveryCodes.add(new DeliveryCode("NORMAL", "일반 배송"));
        deliveryCodes.add(new DeliveryCode("SLOW", "느린 배송"));
        return deliveryCodes;
    }

    @GetMapping
    public String items(Model model) {

        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }

    // BindingResult bindingResult 파라미터 위치는 @ModelAttribute Object obejct 바로 다음에 와야한다
    // @PostMapping("/add")
    public String addItemV1(Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        // 검증 오류 결과를 보관
        // Map<String, String> errors = new HashMap<>();

        // 검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
//            errors.put("itemName", "상품 이름은 필수입니다");
            bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수입니다"));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 100000) {
//            errors.put("price", "가격은 1,000 ~ 1,000,000 까지 허용합니다");
            bindingResult.addError(new FieldError("item", "price", "가격은 1,000 ~ 1,000,000 까지 허용합니다"));
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
//            errors.put("quantity", "수량은 최대 9,999 까지 허용합니다");
            bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999 까지 허용합니다"));
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
//                errors.put("globalError", "가격*수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice);
                bindingResult.addError(new ObjectError("item", "가격*수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
            }
        }

        // 검증에 실패하면 다시 입력폼으로
//        if (!errors.isEmpty()) {
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
//            model.addAttribute("errors", errors);
            return "validation/v2/addForm";
        }

        log.info("item.open={}", item.getOpen());
        log.info("item.regions={}", item.getRegions());
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    // @PostMapping("/add")
    public String addItemV2(Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        // 검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(),
                    false, null, null,  "상품 이름은 필수입니다"));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 100000) {
            bindingResult.addError(new FieldError("item", "price", item.getPrice(),
                    false, null, null, "가격은 1,000 ~ 1,000,000 까지 허용합니다"));
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(),
                    false, null, null, "수량은 최대 9,999 까지 허용합니다"));
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
//                errors.put("globalError", "가격*수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice);
                bindingResult.addError(new ObjectError("item", null, null, "가격*수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
            }
        }

        // 검증에 실패하면 다시 입력폼으로
//        if (!errors.isEmpty()) {
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
//            model.addAttribute("errors", errors);
            return "validation/v2/addForm";
        }

        log.info("item.open={}", item.getOpen());
        log.info("item.regions={}", item.getRegions());
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    // @PostMapping("/add")
    public String addItemV3(Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        // 검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(),
                    false, new String[]{"required.item.itemName"}, null, null));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 100000) {
            bindingResult.addError(new FieldError("item", "price", item.getPrice(),
                    false, new String[]{"range.item.price"}, new Object[]{1000, 1000000}, null));
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(),
                    false, new String[]{"max.item.quantity"}, new Object[]{9999}, null));
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000, resultPrice}, null));
            }
        }

        // 검증에 실패하면 다시 입력폼으로
//        if (!errors.isEmpty()) {
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
//            model.addAttribute("errors", errors);
            return "validation/v2/addForm";
        }

        log.info("item.open={}", item.getOpen());
        log.info("item.regions={}", item.getRegions());
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    // @PostMapping("/add")
    public String addItemV4(Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        // 검증 로직

        /*
        if (bindingResult.hasErrors()) {
            log.info("error={}", bindingResult);
            return "validation/v2/addForm";
        }
        StringUtrils.hasText() 와 같은 기능
        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName", "required");
        */
        log.info("objectName={}", bindingResult.getObjectName());
        log.info("target={}", bindingResult.getTarget());

        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.rejectValue("itemName", "required");
        }


        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 100000) {
            bindingResult.rejectValue("price", "range",new Object[]{1000, 1000000}, null);
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        // 검증에 실패하면 다시 입력폼으로
//        if (!errors.isEmpty()) {
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
//            model.addAttribute("errors", errors);
            return "validation/v2/addForm";
        }

        log.info("item.open={}", item.getOpen());
        log.info("item.regions={}", item.getRegions());
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    // @PostMapping("/add")
    public String addItemV5(Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

//        new ItemValidator().validate(item, bindingResult);
        itemValidator.validate(item, bindingResult);

        // 검증에 실패하면 다시 입력폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
//            model.addAttribute("errors", errors);
            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    // @Validated 어노테이션으로 검증동작
    @PostMapping("/add")
    public String addItemV6(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        
        // 검증에 실패하면 다시 입력폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
//            model.addAttribute("errors", errors);
            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }


    /**
     * 테스트 데이터 추가
     */
    @PostConstruct
    public void init() {

        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }


}