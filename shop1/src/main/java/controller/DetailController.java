package controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Item;
import logic.ShopService;

// /detail.shop 요청시 호출되는 Controller
public class DetailController {
	private ShopService shopService;
	public void setShopService(ShopService shopService) {
		this.shopService = shopService;
	}
	
	@RequestMapping
	//Controller의 메서드 매개변수의 이름은 파라미터 이름이 같은 경우 값이 자동으로 설정
	public ModelAndView detail(Integer id) {
		// item : Id에 해당하는 DB정보를 읽어서 저장하고 있는 객체
		Item item = shopService.getItemById(id);
		ModelAndView mav = new ModelAndView();	//view이름을 설정하지 않을시, url이 기본 view로 설정 => detail.jsp
		mav.addObject("item",item);
		return mav;
	}
}