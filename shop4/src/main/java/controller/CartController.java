package controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import exception.CartEmptyException;
import logic.Cart;
import logic.Item;
import logic.ItemSet;
import logic.Sale;
import logic.ShopService;
import logic.User;

@Controller
@RequestMapping("cart")
public class CartController {
	
	@Autowired
	private ShopService service;
	
	/*
	@GetMapping("cartDelete")
	public ModelAndView delete(Integer index, HttpSession session) {
		ModelAndView mav = new ModelAndView("cart/cart");
		Cart cart = (Cart)session.getAttribute("CART");
		String name = cart.delete(index);
		mav.addObject("message", name + " 삭제 완료"); mav.addObject("cart", cart);
		return mav;
	}
	*/
	
	@RequestMapping("cartDelete")
	public ModelAndView delete(int index, HttpSession session) {
		ModelAndView mav = new ModelAndView("cart/cart");
		Cart cart = (Cart)session.getAttribute("CART");
		ItemSet itemset = null;
		try {
			// List.remove(int) : index 해당하는 객체 제거
			// List.remove(Integer) : Integer 타입의 Object 객체 제거
			// itemset : List에서 제거된 객체 지정
			itemset = cart.getItemSetList().remove(index);
			mav.addObject("message", itemset.getItem().getName() + "상품을 삭제하였습니다.");
		} catch (Exception e) {
			mav.addObject("message", "장바구니 상품 삭제 오류");
		}
		mav.addObject("cart", cart);
		return mav;
	}
	
	@RequestMapping("cartView")
	public ModelAndView cartview(HttpSession session) {
		ModelAndView mav = new ModelAndView("cart/cart");
		Cart cart = (Cart)session.getAttribute("CART");
		if(cart == null || cart.getItemSetList().size() == 0) {
			throw new CartEmptyException("장바구니에 상품이 없습니다.", "../item/list.shop");
		}
		mav.addObject("cart", cart);
		return mav;
	}
	
	@RequestMapping("cartAdd")
	public ModelAndView add(Integer id, Integer quantity, HttpSession session) {
		ModelAndView mav = new ModelAndView("cart/cart");
		Item item = service.getItem(id);	//선택된 상품객체
		Cart cart = (Cart)session.getAttribute("CART");
		if(cart == null) {
			cart = new Cart();
			session.setAttribute("CART", cart);
		}	
		cart.push(new ItemSet(item,quantity));
		mav.addObject("message", item.getName() + ":" + quantity + "개 장바구니 추가");
		mav.addObject("cart", cart);
		return mav;
	}
	
	@RequestMapping("checkout")	//AOP 클래스에서 첫번째 매개변수를 사용하므로, 반드시 첫번째 매개변수는 session으로 설정해야함.
	public ModelAndView checkout(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		Cart cart = (Cart)session.getAttribute("CART");
		mav.addObject("cart", cart);
		return mav;
	}
	
	@RequestMapping("end")
	public ModelAndView checkend(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		Cart cart = (Cart)session.getAttribute("CART");
		User loginUser = (User)session.getAttribute("loginUser");
		Sale sale = service.checkend(loginUser,cart);
		long total = cart.getTotal();
		session.removeAttribute("CART");	//주문완료시 장바구니내용 제거
		mav.addObject("sale",sale);
		mav.addObject("total", total);
		return mav;
	}
}