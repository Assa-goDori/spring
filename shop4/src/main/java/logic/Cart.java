package logic;

import java.util.ArrayList;
import java.util.List;

public class Cart {
	private List<ItemSet> itemSetList = new ArrayList<ItemSet>();
	
	public List<ItemSet> getItemSetList() {
		return itemSetList;
	}
	
	public void push(ItemSet itemSet) {
		for(int i = 0; i<itemSetList.size(); i++) {
			String name = itemSetList.get(i).getItem().getName();
			 if(name.equals(itemSet.getItem().getName())) {
				ItemSet is = itemSetList.get(i);
				is.setQuantity(is.getQuantity() + itemSet.getQuantity());
				return;
			 }
		}
		itemSetList.add(itemSet);
	}
	
	public long getTotal() {
		long sum = 0;
		for(ItemSet is : itemSetList) {
			sum += is.getItem().getPrice() * is.getQuantity();
		}
		return sum;
	}

	/*
	public String delete(Integer index) {
		String name = itemSetList.get(index).getItem().getName();
		itemSetList.remove(itemSetList.get(index));
		return name;
	}
	*/
}
