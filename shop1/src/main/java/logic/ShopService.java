package logic;

import java.util.List;

import dao.ItemDao;

public class ShopService {
	private ItemDao itemDao;
	public void setItemDao(ItemDao itemDao) {
		this.itemDao = itemDao;
	}
	public List<Item> getItemList() {
		return itemDao.list();
	}
	public Item getItemById(Integer id) {
		return itemDao.selectOne(id);
	}
}