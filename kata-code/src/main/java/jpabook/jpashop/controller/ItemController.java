package jpabook.jpashop.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ItemController {

	private final ItemService itemService;
	private final OrderService orderService;

	@GetMapping("/items/new")
	public String createForm(Model model){
		model.addAttribute("form",new BookForm());
		return "items/createItemForm";
	}

	@PostMapping("/items/new")
	public String create(BookForm form){
		Book book = new Book();
		book.setName(form.getName());
		book.setPrice(form.getPrice());
		book.setStockQuantity(form.getStockQuantity());
		book.setAuthor(form.getAuthor());
		book.setIsbn(form.getIsbn());

		itemService.saveItem(book);

		return "redirect:/";
	}

	@GetMapping("/items")
	public String list(Model model){
		List<Item> items = itemService.findItems();
		model.addAttribute("items", items);

		return "items/itemList";
	}

	@GetMapping("/items/{itemId}/edit")
	public String updateItemForm(@PathVariable("itemId") Long itemId, Model model){
		Book item = (Book)itemService.findOne(itemId);

		BookForm form = new BookForm();
		form.setId(item.getId());
		form.setName(item.getName());
		form.setPrice(item.getPrice());
		form.setStockQuantity(item.getStockQuantity());
		form.setAuthor(item.getAuthor());
		form.setIsbn(item.getIsbn());

		model.addAttribute("form", form);
		return "items/updateItemForm";
	}

	@PostMapping("/items/{itemId}/edit")
	public String updateItemForm(@PathVariable Long itemId, @ModelAttribute("form") BookForm form){

		// Book book = new Book();
		// book.setId(form.getId());
		// book.setName(form.getName());

		// merge를 사용하면 안되는 이유
		// book.setPrice(form.getPrice());
		// 만약 이때 정책에 의해 변경이 불가능하다고 set을 하지 않는다면? merge때문에 price에는 null이 들어간다.
		// 따라서 merge 사용하지말고, 항상 변경 감지를 사용!

		// book.setStockQuantity(form.getStockQuantity());
		// book.setAuthor(form.getAuthor());
		// book.setIsbn(form.getIsbn());

		itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());

		return "redirect:/items";
	}

	@PostMapping("/orders/{orderId}/cancel")
	public String cancelOrder(@PathVariable("orderId") Long orderId){
		orderService.cancelOrder(orderId);

		return "redirect:/orders";
	}
}
