package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

import javafx.application.Platform;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
import serverGUI.ClientConnInfo;
import serverGUI.ServerGUIController;

public class BParkServer extends AbstractServer {
	public static final int DEFAULT_PORT = 5555;

	private static final String DB_URL = "jdbc:mysql://localhost:3306/";

	private BiConsumer<String, String> clientDetailsCallback;

	private final Map<String, ClientConnInfo> activeClients = new ConcurrentHashMap<>();

	private Runnable clientDisconnectionCallback;

	private static Connection conn = null;

	public BParkServer(int port) {
		super(port);
	}

	public BParkServer(int port, String dbName, String dbUser, String dbPassword) {
		super(port);
		String fullDBUrl = DB_URL + dbName + "?serverTimezone=IST";
		try {
			conn = DriverManager.getConnection(fullDBUrl, dbUser, dbPassword);
		} catch (SQLException e) {
			System.out.println("Database connection failed: " + e.getMessage());
		}
	}

	public void setClientDetailsCallback(BiConsumer<String, String> callback) {
		this.clientDetailsCallback = callback;
	}

	public void setClientDisconnectionCallback(Runnable callback) {
		this.clientDisconnectionCallback = callback;
	}

	protected void clientConnected(ConnectionToClient client) {
		super.clientConnected(client);
		String ip = client.getInetAddress().getHostAddress();
		String host = client.getInetAddress().getCanonicalHostName();

		if (clientDetailsCallback != null) {
			clientDetailsCallback.accept(host, ip);
		}
	}

	protected void clientDisconnected(ConnectionToClient client) {
		String ip = client.getInetAddress().getHostAddress();
		String host = client.getInetAddress().getHostName();
		// Update both internal map and GUI
		Platform.runLater(() -> {
			// 1. Update activeClients map
			ClientConnInfo info = activeClients.get(ip);
			if (info != null) {
				info.setStatus("Disconnected");
			} else {
				info = new ClientConnInfo(host, ip, "Disconnected");
				activeClients.put(ip, info);
			}

			// 2. Update GUI table via controller
			ServerGUIController controller = ServerGUIController.getInstance();
			if (controller != null) {
				controller.updateClientDisconnectionDetails(client);
			}
		});

		super.clientDisconnected(client);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void handleMessageFromClient(Object msg, @SuppressWarnings("exports") ConnectionToClient client) {
		try {
			if ("DisConnect".equals(msg)) {
				// Handle graceful disconnect
				clientDisconnected(client);
				return;
			} else if ("Connect".equals(msg)) {
				clientConnected(client);
			} else if (msg instanceof ArrayList) {
				newOrderDateUpdate((ArrayList<String>) msg);
				client.sendToClient("Order Successfully Updated");
			} else if (msg instanceof String[]) {
				String[] message = (String[]) msg;
				client.sendToClient(readFromDB(message[0])); // Send the list of orders back
				System.out.println(">>> Sent results to client");
			} else if (msg instanceof List) {
				parkingSpaceUpdate((List) msg);
				client.sendToClient("Order Successfully Updated");
			}
		} catch (Exception e) {
			System.out.println("Exception occurred: " + e.getMessage());
		}
	}

	private static String readFromDB(String msg) throws SQLException {
		List<Map<String, Object>> results = new ArrayList<>();
		System.out.println(msg.toString());
		if (msg.equals("All")) {
			try {
				String query = "SELECT * FROM `order`";
				PreparedStatement pstmt = conn.prepareStatement(query);
				@SuppressWarnings("unused")
				ResultSet orders = pstmt.executeQuery();// save the orders in a array after executing it

				try (ResultSet rs = pstmt.executeQuery()) {
					ResultSetMetaData metaData = rs.getMetaData();
					int columnCount = metaData.getColumnCount();

					while (rs.next()) {
						Map<String, Object> row = new HashMap<>();
						for (int i = 1; i <= columnCount; i++) {
							row.put(metaData.getColumnName(i), rs.getObject(i));
						}
						results.add(row);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				String query = "SELECT * FROM `order` WHERE subscriberNumber = ?";
				PreparedStatement pstmt = conn.prepareStatement(query);
				pstmt.setString(1, msg);
				@SuppressWarnings("unused")
				ResultSet orders = pstmt.executeQuery();// save the orders in a array after executing it

				try (ResultSet rs = pstmt.executeQuery()) {
					ResultSetMetaData metaData = rs.getMetaData();
					int columnCount = metaData.getColumnCount();

					while (rs.next()) {
						Map<String, Object> row = new HashMap<>();
						for (int i = 1; i <= columnCount; i++) {
							row.put(metaData.getColumnName(i), rs.getObject(i));
						}
						results.add(row);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return results.toString();
	}

	private void parkingSpaceUpdate(List<String> msg) {
		try {
			String pSpace = msg.get(2);
			String subNumber = msg.get(0);
			String OrderNumber = msg.get(1);
			String query = "UPDATE `order` SET parking_space = ? WHERE subscriberNumber = ? AND order_number =?";
			PreparedStatement pstmt = conn.prepareStatement(query);

			// Set the parameters (pSpace and subNumber)
			pstmt.setString(1, pSpace); // If pSpace is numeric, use setInt instead
			pstmt.setString(2, subNumber); // If subNumber is numeric, use setInt instead
			pstmt.setString(3, OrderNumber);

			// Execute the update
			int rowsAffected = pstmt.executeUpdate();
			System.out.println("Rows updated: " + rowsAffected);

		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

	}

	private void newOrderDateUpdate(ArrayList<String> msg) {
		try {
			String selectedDate = msg.get(2);
			String subNumber = msg.get(0);
			String orderNumber = msg.get(1);
			String query = "UPDATE `order` SET order_date = ? WHERE (subscriberNumber = ? AND order_number = ?)";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setDate(1, java.sql.Date.valueOf(selectedDate));
			pstmt.setString(2, subNumber); // If subNumber is numeric, use setInt instead
			pstmt.setString(3, orderNumber);
			int rowsAffected = pstmt.executeUpdate();

			System.out.println("Rows updated: " + rowsAffected);// notation line

		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
			
	}

	public static void main(String[] args) {
		int port = 5555;
		try {
			port = Integer.parseInt(args[0]);
		} catch (Exception exception) {
		}
		BParkServer server = new BParkServer(port);
		try {
			server.listen();
		} catch (Exception e) {
			System.out.println("ERROR - Could not listen for clients!");
		}
	}
}
