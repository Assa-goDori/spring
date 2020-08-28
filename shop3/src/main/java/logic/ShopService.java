package logic;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dao.AdminDao;
import dao.BoardDao;
import dao.ItemDao;
import dao.UserDao;
import dao.SaleDao;
import dao.SaleItemDao;

@Service	//@Component + service(Controller와 Dao 중간 역할)
public class ShopService {
	@Autowired
	private ItemDao itemDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private SaleDao saleDao;
	@Autowired
	private SaleItemDao saleItemDao;
	@Autowired
	private AdminDao adminDao;
	@Autowired
	private BoardDao boardDao;

	public List<Item> getItemList() {
		return itemDao.list();
	}

	public Item getItem(Integer id) {
		return itemDao.selectOne(id);
	}
	
	//파일 업로드, dao에 데이터 전달
	public void itemCreate(Item item, HttpServletRequest request) {
		//업로드 되는 파일이 존재하는 경우
		if(item.getPicture() != null && !item.getPicture().isEmpty()) {
			uploadFileCreate(item.getPicture(),request,"item/img/");
			item.setPictureUrl(item.getPicture().getOriginalFilename());
		}
		itemDao.insert(item);
	}
	
	private void uploadFileCreate(MultipartFile picture, HttpServletRequest request, String path) {
		//picture : 파일의 내용을 저장
		String orgFile = picture.getOriginalFilename();
		String uploadPath = request.getServletContext().getRealPath("/") + path;
		File fpath = new File(uploadPath);
		if(!fpath.exists()) fpath.mkdirs();
		try {
			//파일의 내용을 실제 파일로 저장
			picture.transferTo(new File(uploadPath + orgFile));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void itemUpdate(Item item, HttpServletRequest request) {
		if(item.getPicture() != null && !item.getPicture().isEmpty()) {
			uploadFileCreate(item.getPicture(),request,"item/img/");
			item.setPictureUrl(item.getPicture().getOriginalFilename());
		}
		itemDao.update(item);
	}

	public void itemDelete(Integer id) {
		itemDao.delete(id);
	}

	public void userInsert(User user) {
		userDao.insert(user);
	}

	public User getUser(String userid) {
		return userDao.selectOne(userid);
	}

	/*
	 * DB에 sale 정보와 saleItem 정보 저장. 저장된 내용을 Sale 객체로 리턴
	 * 1. sale테이블의 saleid 값을 설정 => 최대값 + 1 
	 * 2. sale의 내용 설정. => insert
	 * 3. Cart 정보로부터 SaleItem 내용 설정. => insert
	 * 4. 모든 정보를 Sale 객체로 저장
	 */
	public Sale checkend(User loginUser, Cart cart) {
		Sale sale = new Sale();
		int max = saleDao.idMax();
		sale.setSaleid(max+1);
		sale.setUser(loginUser);
		sale.setUserid(loginUser.getUserid());
		saleDao.insert(sale);
		//장바구니의 판매 상품 정보
		/*
		SaleItem si = new SaleItem();
		for(int i = 0; i<cart.getItemSetList().size(); i++) {
			si.setSeq(++i);
			si.setItem(cart.getItemSetList().get(i).getItem());
			si.setItemid(cart.getItemSetList().get(i).getItem().getId());
			si.setQuantity(cart.getItemSetList().get(i).getQuantity());
			si.setSaleid(sale.getSaleid());
			saleItemDao.insert(si);
			sale.getItemList().add(si);
		}
		*/
		List<ItemSet> itemList = cart.getItemSetList();
		int i = 0;
		for(ItemSet is : itemList) {
			int seq = ++i;
			SaleItem saleItem = new SaleItem(sale.getSaleid(),seq,is);
			sale.getItemList().add(saleItem);	//Sale 객체의 SaleItem추가
			saleItemDao.insert(saleItem);
		}
		return sale;
	}

	public List<Sale> salelist(String id) {
		return saleDao.list(id);
	}
	
	public List<SaleItem> saleItemList(int saleid) {
		return saleItemDao.list(saleid);
	}

	public void userUpdate(User user) {
		userDao.userUpdate(user);
	}

	public void deleteUser(String userid) {
		userDao.deleteUser(userid);
	}

	public List<User> getList() {
		return adminDao.getList();
	}

	public List<User> userList(String[] idchks) {
		return userDao.list(idchks);
	}

	public void boardWrite(Board board, HttpServletRequest request) {
		if(board.getFile1() != null && !board.getFile1().isEmpty()) {
			uploadFileCreate(board.getFile1(), request, "board/file/");
			board.setFileurl(board.getFile1().getOriginalFilename());
		}
		int max = boardDao.maxnum();
		board.setNum(++max);
		board.setGrp(max);
		boardDao.insert(board);
	}

	public List<Board> boardList(int pageNum, int limit, String searchtype, String searchcontent) {
		return boardDao.boardList(pageNum, limit, searchtype, searchcontent);
	}

	public int boardCount(String searchtype, String searchcontent) {
		return boardDao.boardCount(searchtype, searchcontent);
	}

	public Board selectOne(int num) {
		return boardDao.selectOne(num);
	}
	
	
	public Board getBoard(Integer num, boolean able) {
		if(able) {
			boardDao.updatercnt(num);
		}
		return boardDao.selectOne(num);
	}
	

	public void updatercnt(int num) {
		boardDao.updatercnt(num);
	}

	public void boardReply(Board board) {
		boardDao.updateGrpStep(board);	//기존 답글의 grpstep 증가.
		int max = boardDao.maxnum();
		board.setNum(++max);
		board.setGrplevel(board.getGrplevel()+1);
		board.setGrpstep(board.getGrpstep()+1);
		boardDao.insert(board);
	}

	public void updateBoard(Board board, HttpServletRequest request) {
		//업로드 되는 파일ㅇ ㅣ존재하는경우
		if(board.getFile1() != null && !board.getFile1().isEmpty()) {
			uploadFileCreate(board.getFile1(), request, "board/file/");
			board.setFileurl(board.getFile1().getOriginalFilename());
		}
		boardDao.updateBoard(board);
	}

	public void deleteBoard(int num) {
		boardDao.deleteBoard(num);
	}
		
}