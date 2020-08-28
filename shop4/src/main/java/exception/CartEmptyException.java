package exception;

public class CartEmptyException extends RuntimeException {
	//Exception이 아닌 RuntimeException으로 하는 이유 : 예외처리를 안해줘도 되기 때문.
	private String url;
	public CartEmptyException(String msg, String url) {
		super(msg);
		this.url = url;
	}
	public String getUrl() {
		return url;
	}
}