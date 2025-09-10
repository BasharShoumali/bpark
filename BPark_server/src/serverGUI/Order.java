package serverGUI;

public class Order {
	private int parking_space;

	private int order_number;

	private String order_date;

	private int confirmation_code;

	private int subscriberNumber;

	public int getParking_space() {
		return parking_space;
	}

	public void setParking_space(int parking_space) {
		this.parking_space = parking_space;
	}

	public int getOrder_number() {
		return order_number;
	}

	public void setOrder_number(int order_number) {
		this.order_number = order_number;
	}

	public String getOrder_date() {
		return order_date;
	}

	public void setOrder_date(String order_date) {
		this.order_date = order_date;
	}

	public int getConfirmation_code() {
		return confirmation_code;
	}

	public void setConfirmation_code(int confirmation_code) {
		this.confirmation_code = confirmation_code;
	}

	public int getSubscriberNumber() {
		return subscriberNumber;
	}

	public void setSubscriberNumber(int subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}

	public String getDate_of_placing_an_order() {
		return date_of_placing_an_order;
	}

	public void setDate_of_placing_an_order(String date_of_placing_an_order) {
		this.date_of_placing_an_order = date_of_placing_an_order;
	}

	private String date_of_placing_an_order;

	public Order() {
	}

	public Order(int parking_space, int order_number, String order_date, int confirmation_code, int subscriberNumber,
			String date_of_placing_an_order) {
		this.parking_space = parking_space;
		this.order_number = order_number;
		this.order_date = order_date;
		this.confirmation_code = confirmation_code;
		this.subscriberNumber = subscriberNumber;
		this.date_of_placing_an_order = date_of_placing_an_order;
	}

	public String toString() {
		return "Order [parking_space=" + this.parking_space + ", order_number=" + this.order_number + ", order_date="
				+ order_date + ", confirmation_code=" + this.confirmation_code + ", subscriberNumber="
				+ this.subscriberNumber + ", date_of_placing_an_order=" + this.date_of_placing_an_order + "]";
	}
}
